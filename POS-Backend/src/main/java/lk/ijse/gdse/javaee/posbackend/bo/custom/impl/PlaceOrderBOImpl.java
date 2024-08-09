package lk.ijse.gdse.javaee.posbackend.bo.custom.impl;

import lk.ijse.gdse.javaee.posbackend.bo.custom.PlaceOrderBO;
import lk.ijse.gdse.javaee.posbackend.controller.CustomerController;
import lk.ijse.gdse.javaee.posbackend.dao.DAOFactory;
import lk.ijse.gdse.javaee.posbackend.dao.custom.ItemDAO;
import lk.ijse.gdse.javaee.posbackend.dao.custom.OrderDAO;
import lk.ijse.gdse.javaee.posbackend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse.javaee.posbackend.dto.OrderDTO;
import lk.ijse.gdse.javaee.posbackend.dto.OrderDetailDTO;
import lk.ijse.gdse.javaee.posbackend.entity.Order;
import lk.ijse.gdse.javaee.posbackend.entity.OrderDetail;
import lk.ijse.gdse.javaee.posbackend.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    static Logger logger = LoggerFactory.getLogger(PlaceOrderBOImpl.class);

    @Override
    public String placeOrder(OrderDTO order, Connection connection) {
        System.out.println("place_order:"+order);
        try {
            TransactionUtil.beginTransaction(connection);
            String result = orderDAO.save(new Order(
                    order.getOrderId(),
                    order.getOrderDate(),
                    order.getCustomerId(),
                    order.getTotal(),
                    order.getSubTotal(),
                    order.getPaidAmount(),
                    order.getDiscount(),
                    order.getBalance(),
                    order.getAddress()
            ), connection);
            logger.info("Save order in transaction process "+ result);
            if (!"Success".equals(result)) {
                TransactionUtil.rollbackTransaction(connection);
                return result;
            }
            for (OrderDetailDTO orderDetail : order.getOrderDetails()) {
                result = orderDetailDAO.save(new OrderDetail(
                        orderDetail.getOrderId(),
                        orderDetail.getItemCode(),
                        orderDetail.getItemName(),
                        orderDetail.getItemPrice(),
                        orderDetail.getQuantity(),
                        orderDetail.getTotal()
                ), connection);
                logger.info("Save orderDetail in transaction process "+ result);
                if (!"Success".equals(result)) {
                    TransactionUtil.rollbackTransaction(connection);
                    return result;
                }
                result = itemDAO.updateQtyOnHand(orderDetail.getItemCode(), orderDetail.getQuantity(), connection);
                logger.info("Update item qty in transaction process "+ result);
                if (!"Success".equals(result)) {
                    TransactionUtil.rollbackTransaction(connection);
                    return result;
                }
            }
            TransactionUtil.commitTransaction(connection);
            return "Success";

        } catch (Exception e) {
            try {
                TransactionUtil.rollbackTransaction(connection);
            } catch (Exception rollbackException) {
              rollbackException.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }


    @Override
    public OrderDTO getOrder(String orderId, Connection connection) throws Exception {
        Order order = orderDAO.get(orderId, connection);
        List<OrderDetail> orderDetails = orderDetailDAO.findOrderDetailsByOrderID(orderId, connection);
        System.out.println("order:"+order);
        return new OrderDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getCustomerId(),
                order.getTotal(),
                order.getSubTotal(),
                order.getPaidAmount(),
                order.getDiscount(),
                order.getBalance(),
                order.getAddress(),
                orderDetails.stream()
                        .map(orderDetail -> new OrderDetailDTO(
                                orderDetail.getOrderId(),
                                orderDetail.getItemCode(),
                                orderDetail.getItemName(),
                                orderDetail.getItemPrice(),
                                orderDetail.getQuantity(),
                                orderDetail.getTotal()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<OrderDTO> getAllOrders(Connection connection) throws Exception {
        List <Order> orders = orderDAO.getAll(connection);
        return orders.stream()
                .map( order -> new OrderDTO(
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getCustomerId(),
                        order.getTotal(),
                        order.getSubTotal(),
                        order.getPaidAmount(),
                        order.getDiscount(),
                        order.getBalance(),
                        order.getAddress(),
                        null
                ))
                .collect(Collectors.toList());
    }
}
