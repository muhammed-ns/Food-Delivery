package fooddelivery.model;

/**
 * Abstract parent class representing a general User in the Food Delivery System.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: Parent class for Customer, DeliveryPartner, and Admin.
 * 2. ENCAPSULATION: Private fields with public getter methods.
 * 3. ABSTRACTION: Abstract getRole() method forces subclasses to define their specific role.
 */
public abstract class User {
    private final String userId;
    private final String name;
    private final String phone;

    public User(String userId, String name, String phone) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty.");
        }
        this.userId = userId.trim();
        this.name = name.trim();
        this.phone = (phone == null) ? "" : phone.trim();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * Returns the role title of this user (e.g., Customer, Delivery Partner, Admin).
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("%s (%s - ID: %s)", name, getRole(), userId);
    }
}
