package dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T, ID> {
    T create(T t) throws SQLException, IllegalAccessException;
    T read(ID id) throws IllegalAccessException, InstantiationException, SQLException;
    T update(T t) throws IllegalAccessException, SQLException;
    void delete(ID t) throws SQLException, IllegalAccessException;

    List<T> readAll() throws SQLException, IllegalAccessException, InstantiationException;
}
