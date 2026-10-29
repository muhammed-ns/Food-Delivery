package fooddelivery.model;

/**
 * Represents a Customer who can browse restaurants, add items to a cart, and place orders.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: Extends the base User class and reuses userId, name, and phone.
 * 2. ENCAPSULATION: Private deliveryAddress and Cart instance with controlled access.
 */
public class Customer extends User {
    private String deliveryAddress;
    private final Cart cart;

    public Customer(String userId, String name, String phone, String deliveryAddress) {
        super(userId, name, phone);
        this.deliveryAddress = (deliveryAddress == null || deliveryAddress.trim().isEmpty())
                ? "Room 204, B.Tech Hostel Block A"
                : deliveryAddress.trim();
        this.cart = new Cart();
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        if (deliveryAddress != null && !deliveryAddress.trim().isEmpty()) {
            this.deliveryAddress = deliveryAddress.trim();
        }
    }

    public Cart getCart() {
        return cart;
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}
