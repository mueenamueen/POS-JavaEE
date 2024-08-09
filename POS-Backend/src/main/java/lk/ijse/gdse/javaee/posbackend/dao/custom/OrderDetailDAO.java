package lk.ijse.gdse.javaee.posbackend.dao.custom;

import lk.ijse.gdse.javaee.posbackend.dao.CrudDAO;
import lk.ijse.gdse.javaee.posbackend.entity.OrderDetail;

import java.sql.Connection;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetail> {
    List<OrderDetail> findOrderDetailsByOrderID(String orderId, Connection connection) throws Exception;
}
