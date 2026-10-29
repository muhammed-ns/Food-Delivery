package fooddelivery.model;

/**
 * Represents a Delivery Partner who picks up and delivers food orders.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: Extends User class.
 * 2. ENCAPSULATION: Private vehicleNumber and availability status managed via methods.
 */
public class DeliveryPartner extends User {
    private final String vehicleNumber;
    private final String vehicleType;
    private boolean available;

    public DeliveryPartner(String userId, String name, String phone, String vehicleType, String vehicleNumber) {
        super(userId, name, phone);
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.available = true;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getRole() {
        return "Delivery Partner";
    }

    @Override
    public String toString() {
        return String.format("%s [%s - %s]", getName(), vehicleType, vehicleNumber);
    }
}
