package lk.ijse.gdse.javaee.posbackend.dao.custom.impl;

import lk.ijse.gdse.javaee.posbackend.dao.custom.ItemDAO;
import lk.ijse.gdse.javaee.posbackend.entity.Item;
import lk.ijse.gdse.javaee.posbackend.util.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    public static String SAVE_ITEM = "INSERT INTO item VALUES(?, ?, ?, ?)";
    public static String GET_ITEM = "SELECT * FROM item WHERE code=?";
    public static String GET_ALL_ITEMS = "SELECT * FROM item";
    public static String UPDATE_ITEM = "UPDATE item SET name=?, qty=?, unitPrice=? WHERE code=?";
    public static String DELETE_ITEM = "DELETE FROM item WHERE code=?";
    public static String UPDATE_QTY = "UPDATE item SET qty=qty-? WHERE code=?";
    @Override
    public String save(Item entity, Connection connection) throws Exception {
        try{
            if (SQLUtil.execute(SAVE_ITEM, connection, entity.getCode(),
                    entity.getName(),
                    entity.getQty(),
                    entity.getUnitPrice())){
                return "Item saved successfully!";
            }else {
                return "Failed to save the item!";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean update(String id, Item entity, Connection connection) throws Exception {
       try{
       return SQLUtil.execute(UPDATE_ITEM, connection, entity.getName(),
               entity.getQty(),
               entity.getUnitPrice(),
               id
       );
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
    @Override
    public boolean delete(String id, Connection connection) throws Exception {
        try {
            return SQLUtil.execute(DELETE_ITEM, connection, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Item get(String id, Connection connection) throws Exception {
        Item item = new Item();
        try{
           ResultSet rst = SQLUtil.execute(GET_ITEM, connection, id);
           while (rst.next()){
                item.setCode(rst.getString("code"));
                item.setName(rst.getString("name"));
                item.setQty(rst.getInt("qty"));
                item.setUnitPrice(rst.getDouble("unitPrice"));
           }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return item;
    }
    @Override
    public List<Item> getAll(Connection connection) throws Exception {
        List<Item> items = new ArrayList<>();
        try{
            ResultSet rst = SQLUtil.execute(GET_ALL_ITEMS, connection);
            while (rst.next()){
                items.add(new Item(
                        rst.getString("code"),
                        rst.getString("name"),
                        rst.getInt("qty"),
                        rst.getDouble("unitPrice")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public String updateQtyOnHand(String itemCode, Integer quantity, Connection connection) {
        try{
            boolean isUpdated = SQLUtil.execute(UPDATE_QTY, connection, quantity, itemCode);
            if(isUpdated){
                return "Success";
            }else{
                return "Failed to update item quantity";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
