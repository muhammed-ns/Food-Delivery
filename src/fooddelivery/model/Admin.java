package fooddelivery.model;

/**
 * Represents a System Admin / Restaurant Manager who oversees restaurants and order operations.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: Extends the base User class alongside Customer and DeliveryPartner.
 * 2. ENCAPSULATION: Private department field with getter.
 */
public class Admin extends User {
    private final String department;

    public Admin(String userId, String name, String phone, String department) {
        super(userId, name, phone);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String getRole() {
        return "System Admin";
    }
}
