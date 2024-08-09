package lk.ijse.gdse.javaee.posbackend.dao;

import java.sql.Connection;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO {
    String save(T entity, Connection connection) throws Exception;
    boolean update(String id, T entity, Connection connection) throws Exception;
    boolean delete(String id, Connection connection) throws Exception;
    T get(String id, Connection connection) throws Exception;
    List<T> getAll(Connection connection) throws Exception;
}