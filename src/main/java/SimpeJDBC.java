import dao.AbstractDao;
import dao.CategoryDao;
import dao.ProductDaoImpl;
import model.Camera;
import model.Phone;
import model.Product;

import java.sql.SQLException;

public class SimpeJDBC {

    public static void main(String[] args) throws SQLException {
        /*ProductDaoImpl dao = Factory.getProductDao();
        Product p = dao.getId(1L);
        System.out.println(p);*/

        Product<Camera> camera = new Camera(3L, "Samsung", 400.0, 8);
        //Product<Phone> phone = new Phone(2L, "iPhone", 900.0, 4, "Amoled");

        AbstractDao dao = Factory.getProductDao();
        dao.create(camera);

        //Product<Phone> readPhone = (Phone)dao.read(1);

    }
}
