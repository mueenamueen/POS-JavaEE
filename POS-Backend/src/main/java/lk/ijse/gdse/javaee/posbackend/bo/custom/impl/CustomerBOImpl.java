package lk.ijse.gdse.javaee.posbackend.bo.custom.impl;

import lk.ijse.gdse.javaee.posbackend.bo.custom.CustomerBO;
import lk.ijse.gdse.javaee.posbackend.dao.DAOFactory;
import lk.ijse.gdse.javaee.posbackend.dao.custom.CustomerDAO;
import lk.ijse.gdse.javaee.posbackend.dto.CustomerDto;
import lk.ijse.gdse.javaee.posbackend.entity.Customer;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    @Override
    public String saveCustomer(CustomerDto customer, Connection connection) throws Exception {
        return customerDAO.save(new Customer(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getSalary()
        ), connection);
    }

    @Override
    public boolean updateCustomer(String id, CustomerDto customer, Connection connection) throws Exception {
        return customerDAO.update(id,new Customer(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getSalary()
        ),connection);
    }

    @Override
    public boolean deleteCustomer(String id, Connection connection) throws Exception {
        return customerDAO.delete(id,connection);
    }

    @Override
    public CustomerDto getCustomer(String id, Connection connection) throws Exception {
        Customer customer = customerDAO.get(id, connection);
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getSalary()
        );
    }

    @Override
    public List<CustomerDto> getAllCustomers(Connection connection) throws Exception {
        List<Customer> customers = customerDAO.getAll(connection);
        return customers.stream()
                .map(customer -> new CustomerDto(
                        customer.getId(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getSalary()
                ))
                .collect(Collectors.toList());
    }
}
