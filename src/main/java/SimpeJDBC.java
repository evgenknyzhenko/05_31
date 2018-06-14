import dao.AbstractDao;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public class SimpeJDBC {

    public static void main(String[] args) throws Exception {
        /*ProductDaoImpl dao = Factory.getProductDao();
        Product p = dao.getId(1L);
        System.out.println(p);*/


        Product product = new Product(3L,"Samsung", 880.00);

        AbstractDao dao = Factory.getProductDao();
        //dao.create(product);

        /*Product product1 = (Product)dao.read(1L);
        System.out.println(product1);*/


        //dao.update(product);

        //dao.delete(5L);

        List<Product> products = dao.readAll();

        for (Product prod: products) {
            System.out.println(prod);
        }



    }
}
