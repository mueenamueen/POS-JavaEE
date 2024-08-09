package lk.ijse.gdse.javaee.posbackend.bo.custom;

import lk.ijse.gdse.javaee.posbackend.bo.SuperBO;
import lk.ijse.gdse.javaee.posbackend.dto.CustomerDto;

import java.sql.Connection;
import java.util.List;

public interface CustomerBO extends SuperBO {
    String saveCustomer(CustomerDto customer, Connection connection) throws Exception;
    boolean updateCustomer (String id, CustomerDto customer, Connection connection) throws Exception;
    boolean deleteCustomer(String id, Connection connection) throws Exception;
    CustomerDto getCustomer(String id,Connection connection) throws Exception;
    List<CustomerDto> getAllCustomers(Connection connection) throws Exception;
}
