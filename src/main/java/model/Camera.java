package model;


public class Camera extends Product<Camera> {
    private Integer pixels;


    public Camera(Long id, String name, double price, Integer pixels) {
        super(id, name, price);
        this.pixels = pixels;
    }

    public Integer getPix() {
        return pixels;
    }

    public void setPix(Integer pix) {
        this.pixels = pix;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "pixels=" + pixels +
                "} " + super.toString();
    }
}
