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
import lk.ijse.gdse.javaee.posbackend.bo.custom.ItemBO;
import lk.ijse.gdse.javaee.posbackend.dto.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(urlPatterns = "/item", loadOnStartup = 2)
public class ItemController extends HttpServlet {

    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);
    static Logger logger = LoggerFactory.getLogger(ItemController.class);
    Jsonb jsonb = JsonbBuilder.create();
    Connection connection;

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("Init method Invoked in ItemController");
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
       if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")){
           resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
       }
       try(var writer = resp.getWriter()){
           ItemDTO item = jsonb.fromJson(req.getReader(), ItemDTO.class);
              writer.write(itemBO.saveItem(item, connection));
                logger.info("Item saved successfully");
                resp.setStatus(HttpServletResponse.SC_CREATED);
       } catch (Exception e) {
           logger.error("Failed to save the item", e);
           resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           throw new RuntimeException(e);
       }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("code");
        if(id != null && !id.isEmpty()){
           try (var writer = resp.getWriter()){
               var itemCode = req.getParameter("code");
               resp.setContentType("application/json");
               var item = itemBO.getItem(itemCode, connection);
               if(item != null && item.getUnitPrice() > 0){
                   jsonb.toJson(item, writer);
                   logger.info("Item retrieved successfully: {}", item);
               }else{
                   resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                   logger.error("Item not found : id{}", itemCode);
                     writer.write("Item not found");
               }
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
        }else if(req.getParameter("id") == null){
            try(var writer = resp.getWriter()){
                List<ItemDTO> items = itemBO.getAllItems(connection);
                jsonb.toJson(items, writer);
                logger.info("All items retrieved successfully: {}", items);
            }catch (Exception e){
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.error("Failed to retrieve items", e);
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(var writer = resp.getWriter()){
            var itemCode = req.getParameter("code");
            ItemDTO item = jsonb.fromJson(req.getReader(), ItemDTO.class);
            if(itemBO.updateItem(itemCode, item, connection)){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                logger.info("Item updated successfully: {}", item);
            }else {
                writer.write("Failed to update the item!");
                logger.error("Failed to update the item: {}", item);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to update the item", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try( var writer = resp.getWriter()){
            var itemCode = req.getParameter("code");
            if (itemBO.deleteItem(itemCode, connection)) {
                logger.info("Item deleted successfully: {}", itemCode);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                writer.write("Failed to delete the item!");
                logger.error("Failed to delete the item: {}", itemCode);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }catch (Exception e){
            logger.error("Failed to delete the item", e);
            throw new RuntimeException(e);
        }
    }

}