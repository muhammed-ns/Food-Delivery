package fooddelivery.model;

/**
 * Represents an individual food item on a restaurant's menu.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. CLASSES & OBJECTS: Instantiated for menu items such as Pizza, Burger, Biriyani, and Sandwich.
 * 2. ENCAPSULATION: Private fields with validation in the constructor and public getters.
 */
public class FoodItem {
    private final String itemId;
    private final String name;
    private final String category;
    private final double price;

    public FoodItem(String itemId, String name, String category, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Food item name cannot be empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Food item price must be positive.");
        }
        this.itemId = itemId;
        this.name = name.trim();
        this.category = category;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (₹%.2f)", name, price);
    }
}
