package lk.ijse.gdse.javaee.posbackend.util;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public class TransactionUtil {
    public static void beginTransaction(Connection connection) throws Exception {
       if (connection != null){
           connection.setAutoCommit(false);
       }
    }
    public static void commitTransaction(Connection connection) throws Exception {
        if (connection != null){
            connection.commit();
            connection.setAutoCommit(true);
        }
    }
    public static void rollbackTransaction(Connection connection) throws Exception {
        if (connection != null){
           try{
               connection.rollback();
               connection.setAutoCommit(true);
           } catch (Exception e) {
               e.printStackTrace();
           }
        }
    }
}
