import model.Camera;
import model.Product;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Main {


    public static void main(String[] args) {


        Product product = new Product(1L, "iphone", 454.44);
        Product product2 = new Product(2L, "iphone2", 354.44);

        Camera camera = new Camera(1L, "Sony", 454.45, 5);
        Camera camera2 = new Camera(2L, "Sams", 400.45, 8);

        /*System.out.println(camera.compareTo(product));
        System.out.println(camera.compareTo(camera2));
        System.out.println(product.compareTo(product2));
        System.out.println(product.compareTo(camera));*/
    }

}
