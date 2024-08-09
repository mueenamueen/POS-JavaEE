package lk.ijse.gdse.javaee.posbackend.bo.custom;

import lk.ijse.gdse.javaee.posbackend.bo.SuperBO;
import lk.ijse.gdse.javaee.posbackend.dto.ItemDTO;

import java.sql.Connection;
import java.util.List;

public interface ItemBO extends SuperBO {
    String saveItem(ItemDTO item, Connection connection) throws Exception;
    ItemDTO getItem(String itemCode, Connection connection) throws Exception;
    List<ItemDTO> getAllItems(Connection connection) throws Exception;
    boolean updateItem(String itemCode, ItemDTO item, Connection connection) throws Exception;
    boolean deleteItem(String itemCode, Connection connection) throws Exception;
}
