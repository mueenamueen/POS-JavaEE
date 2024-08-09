package lk.ijse.gdse.javaee.posbackend.dao.custom;

import lk.ijse.gdse.javaee.posbackend.dao.CrudDAO;
import lk.ijse.gdse.javaee.posbackend.entity.Item;

import java.sql.Connection;

public interface ItemDAO extends CrudDAO<Item> {
    String updateQtyOnHand(String itemCode, Integer quantity, Connection connection);
}
