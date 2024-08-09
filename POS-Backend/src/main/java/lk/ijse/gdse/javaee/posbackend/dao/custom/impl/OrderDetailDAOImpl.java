package lk.ijse.gdse.javaee.posbackend.dao.custom.impl;

import lk.ijse.gdse.javaee.posbackend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse.javaee.posbackend.entity.OrderDetail;
import lk.ijse.gdse.javaee.posbackend.util.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    public static String SAVE_ORDER_DETAIL = "INSERT INTO orderdetail VALUES(?, ?, ?, ?, ?, ?)";
    public static String GET_ORDER_DETAIL = "SELECT * FROM orderdetail WHERE orderId=?";

    @Override
    public String save(OrderDetail entity, Connection connection) throws Exception {
        try{
            if (SQLUtil.execute(SAVE_ORDER_DETAIL, connection,
                    entity.getOrderId(),
                    entity.getItemCode(),
                    entity.getItemName(),
                    entity.getItemPrice(),
                    entity.getQuantity(),
                    entity.getTotal())){
                return "Success";
            }else {
                return "Failed to save order details";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(String id, OrderDetail entity, Connection connection) throws Exception {
        return false;
    }

    @Override
    public boolean delete(String id, Connection connection) throws Exception {
        return false;
    }

    @Override
    public OrderDetail get(String id, Connection connection) throws Exception {
        return null;
    }

    @Override
    public List<OrderDetail> getAll(Connection connection) throws Exception {
        return List.of();
    }

    @Override
    public List<OrderDetail> findOrderDetailsByOrderID(String orderId, Connection connection) throws Exception {
        List<OrderDetail> orderDetails = new ArrayList<>();
        try{
            ResultSet rst = SQLUtil.execute(GET_ORDER_DETAIL, connection, orderId);
            while (rst.next()){
               orderDetails.add(new OrderDetail(
                       rst.getString(1),
                       rst.getString(2),
                       rst.getString(3),
                       rst.getBigDecimal(4),
                       rst.getInt(5),
                       rst.getBigDecimal(6)
               ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orderDetails;
    }
}
