package ir.maktab58.homework6.models.vehicles;

public class Car extends Vehicle {
    public Car(String model, String color, String plateNumber) {
        super(model, color, plateNumber);
    }

    @Override
    public String toString() {
        return "Car{" + super.toString() + "}";
    }
}
