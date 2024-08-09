package lk.ijse.gdse.javaee.posbackend.dao.custom.impl;

import lk.ijse.gdse.javaee.posbackend.dao.custom.OrderDAO;
import lk.ijse.gdse.javaee.posbackend.entity.Order;
import lk.ijse.gdse.javaee.posbackend.util.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    public static String GET_ORDER = "SELECT * FROM orders WHERE orderId=?";
    public static String GET_ALL_ORDERS = "SELECT * FROM orders";
    public static String SAVE_ORDER = "INSERT INTO orders VALUES (?,?,?,?,?,?,?,?,?)";

    @Override
    public String save(Order entity, Connection connection) throws Exception {
        try{
            if (SQLUtil.execute(SAVE_ORDER,connection,
                    entity.getOrderId(),
                    entity.getOrderDate(),
                    entity.getCustomerId(),
                    entity.getTotal(),
                    entity.getSubTotal(),
                    entity.getPaidAmount(),
                    entity.getDiscount(),
                    entity.getBalance(),
                    entity.getAddress())){
                return "Success";
            }else {
                return "Failed to save the order!";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(String id, Order entity, Connection connection) throws Exception {
        return false;
    }

    @Override
    public boolean delete(String id, Connection connection) throws Exception {
        return false;
    }

    @Override
    public Order get(String id, Connection connection) throws Exception {
        Order order = new Order();
        try{
            ResultSet rst = SQLUtil.execute(GET_ORDER, connection, id);
            while (rst.next()){
                order.setOrderId(rst.getString(1));
                order.setOrderDate(rst.getDate(2));
                order.setCustomerId(rst.getString(3));
                order.setTotal(rst.getBigDecimal(4));
                order.setSubTotal(rst.getBigDecimal(5));
                order.setPaidAmount(rst.getBigDecimal(6));
                order.setDiscount(rst.getBigDecimal(7));
                order.setBalance(rst.getBigDecimal(8));
                order.setAddress(rst.getString(9));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public List<Order> getAll(Connection connection) throws Exception {
        List <Order> orders = new ArrayList<>();
        try{
            ResultSet rst = SQLUtil.execute(GET_ALL_ORDERS, connection);
            while (rst.next()){
                Order order = new Order();
                order.setOrderId(rst.getString(1));
                order.setOrderDate(rst.getDate(2));
                order.setCustomerId(rst.getString(3));
                order.setTotal(rst.getBigDecimal(4));
                order.setSubTotal(rst.getBigDecimal(5));
                order.setPaidAmount(rst.getBigDecimal(6));
                order.setDiscount(rst.getBigDecimal(7));
                order.setBalance(rst.getBigDecimal(8));
                order.setAddress(rst.getString(9));
                orders.add(order);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
}
