package model;


@MyTable(tableName = "PRODUCTS")
public class Product<T extends Product<T>>  {
    @FieldsOfClass(fieldName = "ID")
    private Long id;
    @FieldsOfClass(fieldName = "NAME")
    private String name;
    @FieldsOfClass(fieldName = "PRICE")
    private Double price;


    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
