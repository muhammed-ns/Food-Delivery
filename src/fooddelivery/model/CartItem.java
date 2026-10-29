package fooddelivery.model;

/**
 * Represents a FoodItem along with a selected quantity inside a Cart or Order.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. ENCAPSULATION: Quantity is private and validated to ensure it is always >= 1.
 */
public class CartItem {
    private final FoodItem foodItem;
    private final Restaurant restaurant;
    private int quantity;

    public CartItem(FoodItem foodItem, Restaurant restaurant, int quantity) {
        if (foodItem == null) {
            throw new IllegalArgumentException("Food item cannot be null.");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        this.foodItem = foodItem;
        this.restaurant = restaurant;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int additionalQuantity) {
        if (additionalQuantity <= 0) {
            throw new IllegalArgumentException("Additional quantity must be positive.");
        }
        this.quantity += additionalQuantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return foodItem.getPrice() * quantity;
    }
}
