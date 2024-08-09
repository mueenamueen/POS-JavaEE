package lk.ijse.gdse.javaee.posbackend.dao.custom.impl;

import lk.ijse.gdse.javaee.posbackend.dao.custom.CustomerDAO;
import lk.ijse.gdse.javaee.posbackend.entity.Customer;
import lk.ijse.gdse.javaee.posbackend.util.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    public static String SAVE_CUSTOMER = "INSERT INTO customer VALUES(?, ?, ?, ?)";
    public static String UPDATE_CUSTOMER = "UPDATE customer SET name=?, address=?, salary=? WHERE customerId=?";
    public static String DELETE_CUSTOMER = "DELETE FROM customer WHERE customerId=?";
    public static String GET_CUSTOMER = "SELECT * FROM customer WHERE customerId=?";
    public static String GET_ALL_CUSTOMERS ="SELECT * FROM customer";

    @Override
    public String save(Customer entity, Connection connection) throws Exception {
        try{
            if ( SQLUtil.execute(SAVE_CUSTOMER, connection, entity.getId(),
                    entity.getName(),
                    entity.getAddress(),
                    entity.getSalary()
            )){
                return "Customer saved successfully!";
            }else {
                return "Failed to save the customer!";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean update(String id, Customer entity, Connection connection) throws Exception {
        try{
            return SQLUtil.execute(UPDATE_CUSTOMER, connection, entity.getName(),
                    entity.getAddress(),
                    entity.getSalary(),
                    id
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id, Connection connection) throws Exception {
        try {
            return SQLUtil.execute(DELETE_CUSTOMER, connection, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer get(String id, Connection connection) throws Exception {
        Customer customer = new Customer();
        try{
            ResultSet rst = SQLUtil.execute(GET_CUSTOMER, connection, id);
            while (rst.next()){
                customer.setId(rst.getString("customerId"));
                customer.setName(rst.getString("name"));
                customer.setAddress(rst.getString("address"));
                customer.setSalary(rst.getDouble("salary"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection connection) throws Exception {
        List<Customer> customers = new ArrayList<>();
        try{
            ResultSet rst = SQLUtil.execute(GET_ALL_CUSTOMERS, connection);
            while (rst.next()){
                customers.add(new Customer(
                        rst.getString("customerId"),
                        rst.getString("name"),
                        rst.getString("address"),
                        rst.getDouble("salary")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
}
