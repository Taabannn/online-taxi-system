package ir.maktab58.homework6.models.vehicles;

public abstract class Vehicle implements VehicleInterface {
    private String model;
    private String color;
    private String plateNumber;

    public Vehicle(String model, String color, String plateNumber) {
        this.model = model;
        this.color = color;
        this.plateNumber = plateNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Override
    public String toString() {
        return "model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", plateNumber='" + plateNumber + '\'';
    }
}
