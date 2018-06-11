package dao;

import java.util.List;

public interface GenericDao<T, ID> {
    T create(T t);
    T read(ID id);
    T update(T t);
    void delete(ID t);

    List<T> readAll();
}
