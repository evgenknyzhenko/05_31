import dao.AbstractDao;
import model.Product;

import java.util.List;

public class SimpeJDBC {

    public static void main(String[] args) throws Exception {
        Product product = new Product(1L, "Samsung A5", 330.33);

        AbstractDao dao = Factory.getProductDao();
        dao.create(product);

        /*Product product1 = (Product)dao.read(1L);
        System.out.println(product1);*/

        //dao.update(product);

        //dao.delete(2L);

        /*List<Product> products = dao.readAll();
        for (Product prod: products) {
            System.out.println(prod);
        }*/

    }
}
