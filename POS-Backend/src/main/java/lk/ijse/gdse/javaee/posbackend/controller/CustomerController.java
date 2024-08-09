package lk.ijse.gdse.javaee.posbackend.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.javaee.posbackend.bo.BOFactory;
import lk.ijse.gdse.javaee.posbackend.bo.custom.CustomerBO;
import lk.ijse.gdse.javaee.posbackend.bo.custom.impl.CustomerBOImpl;
import lk.ijse.gdse.javaee.posbackend.dao.custom.CustomerDAO;
import lk.ijse.gdse.javaee.posbackend.dto.CustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.sound.midi.Soundbank;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(urlPatterns = "/customer", loadOnStartup = 2)
public class CustomerController extends HttpServlet {

    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private Jsonb jsonb = JsonbBuilder.create();
    Connection connection;

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("Init method Invoked in CustomerController");
        try{
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/posSystem");
            this.connection = pool.getConnection();
            logger.info("Connection initialized: {}", this.connection);
        }catch (Exception e){
            logger.error("Failed to connect to the database", e);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        try(var writer = resp.getWriter()){
            CustomerDto customer = jsonb.fromJson(req.getReader(), CustomerDto.class);
            writer.write(customerBO.saveCustomer(customer, connection));
            logger.info("Customer saved successfully");
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            logger.error("Failed to save the customer",e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null && !id.trim().isEmpty()) {
            try(var writer = resp.getWriter()){
                var customerId = req.getParameter("id");
                resp.setContentType("application/json");
                var customer = customerBO.getCustomer(customerId, connection);
                if (customer != null && customer.getSalary() > 1 ){
                    jsonb.toJson(customer, writer);
                    logger.info("Customer retrieved successfully: {}", customer);
                }else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    logger.error("Customer not found: id={}", id);
                    writer.write("Customer not found");
                }
            } catch (Exception e) {
                logger.error("Failed to retrieve customer:",e);
                throw new RuntimeException(e);
            }
        } else if(req.getParameter("id") == null) {
            try(var writer =resp.getWriter()){
                List<CustomerDto> customers = customerBO.getAllCustomers(connection);
                jsonb.toJson(customers, writer);
                logger.info("All customers retrieved successfully");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.error("Failed to retrieve customers:",e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try( var writer = resp.getWriter()){
            var customerId = req.getParameter("id");
            CustomerDto customerDto = jsonb.fromJson(req.getReader(), CustomerDto.class);
            if(customerBO.updateCustomer(customerId, customerDto, connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                logger.info("Customer updated successfully: id={}", customerId);
            }else{
                writer.write("Failed to update the customer");
                logger.error("Failed to update customer: id={}", customerId);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to update customer", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try( var writer = resp.getWriter()){
            var customerId = req.getParameter("id");
            if(customerBO.deleteCustomer(customerId,connection)){
                logger.info("Customer deleted successfully: id={}", customerId);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                writer.write("Failed to delete the customer");
                logger.error("Failed to delete customer: id={}", customerId);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to delete customer", e);
            throw new RuntimeException(e);
        }
    }
}
