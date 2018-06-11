package dao;

import model.Product;

import java.sql.*;

public class ProductDaoImpl  extends AbstractDao<Product, Long> implements ProductDao{


    public ProductDaoImpl(Connection connection) {
        super(connection);
    }

    public void saveProduct(Product product) throws SQLException {
        String query = "INSERT INTO PRODUCTS (NAME, PRICE) VALUES (?,?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, product.getName());
        statement.setDouble(2,product.getPrice());
        statement.execute();


       /* String query = "INSERT INTO PRODUCTS (NAME, PRICE) " +
                "VALUES ('" + product.getName() + "', " + product.getPrice() + ")";

        Statement statement = connection.createStatement();
        statement.execute(query);*/
    }


    public Product getId(Long id) throws SQLException {
        String query = "SELECT ID,NAME, PRICE FROM PRODUCTS WHERE ID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();

        resultSet.first();

        Long pId = resultSet.getLong(1);
        String pName = resultSet.getString(2);
        Double pPrice = resultSet.getDouble(3);
        Product product = new Product(pId, pName, pPrice);

        return product;


        /*String query = "SELECT * FROM PRODUCTS WHERE ID = " + id;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        Product product = null;

        while (rs.next()) {
            product = new Product(rs.getString(2), rs.getDouble(3));
        }

        return product;*/
    }


    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE PRODUCTS SET PRICE=" + product.getPrice() + " WHERE NAME='" + product.getName() + "'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }


    public void deleteProduct(Long id) throws SQLException {
        String query = "DELETE FROM PRODUCTS WHERE ID=" + id;
        Statement statement = connection.createStatement();
        statement.execute(query);
    }
}
