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
import lk.ijse.gdse.javaee.posbackend.bo.custom.PlaceOrderBO;
import lk.ijse.gdse.javaee.posbackend.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(urlPatterns = "/order", loadOnStartup = 2)
public class PlaceOrderController extends HttpServlet {

    PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACE_ORDER);
    static Logger logger = LoggerFactory.getLogger(PlaceOrderController.class);
    private Jsonb jsonb = JsonbBuilder.create();
    Connection connection;

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("Init method Invoked in PlaceOrderController");
        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/posSystem");
            this.connection = pool.getConnection();
            logger.info("Connection initialized: {}", this.connection);
        } catch (Exception e) {
            logger.error("Failed to connect to the database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        try (var writer = resp.getWriter()){
            OrderDTO order = jsonb.fromJson(req.getReader(), OrderDTO.class);
            System.out.println("order:"+order);
            writer.write(placeOrderBO.placeOrder(order, connection));
            logger.info("Order placed successfully");
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            logger.error("Failed to place the order", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        if(orderId !=null && !orderId.trim().isEmpty()) {
            try (var writer = resp.getWriter()) {
                OrderDTO order = placeOrderBO.getOrder(orderId, connection);
                if (order != null && order.getOrderId() != null) {
                    writer.write(jsonb.toJson(order));
                    logger.info("Order retrieved successfully");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    logger.error("Failed to retrieve the order");
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Exception e) {
                logger.error("Failed to retrieve the order", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }
        } else if (req.getParameter("orderId") == null) {
            try (var writer = resp.getWriter()) {
                writer.write(jsonb.toJson(placeOrderBO.getAllOrders(connection)));
                logger.info("All orders retrieved successfully");
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                logger.error("Failed to retrieve all orders", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
