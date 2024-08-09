package lk.ijse.gdse.javaee.posbackend.bo.custom;

import lk.ijse.gdse.javaee.posbackend.bo.SuperBO;
import lk.ijse.gdse.javaee.posbackend.dto.OrderDTO;
import lk.ijse.gdse.javaee.posbackend.entity.Order;
import lk.ijse.gdse.javaee.posbackend.entity.OrderDetail;

import java.sql.Connection;
import java.util.List;

public interface PlaceOrderBO extends SuperBO {
    String placeOrder(OrderDTO order, Connection connection);
    OrderDTO getOrder(String orderId, Connection connection) throws Exception;
    List<OrderDTO> getAllOrders(Connection connection) throws Exception;
}
