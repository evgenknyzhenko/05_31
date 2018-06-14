package model;

/**
 * Created by Евгений on 09.06.2018.
 */

public class Phone extends Product<Phone> {
    private int ramMemory;
    private String displayType;

    public Phone(Long id, String name, Double price, int ramMemory, String displayType) {
        super(id, name, price);
        this.ramMemory = ramMemory;
        this.displayType = displayType;
    }

    public int getRamMemory() {
        return ramMemory;
    }

    public String getDisplayType() {
        return displayType;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "ramMemory=" + ramMemory +
                ", displayType='" + displayType + '\'' +
                "} " + super.toString();
    }
}
