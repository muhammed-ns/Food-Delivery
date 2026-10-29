package fooddelivery.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the items selected by the Customer before placing an order.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. ENCAPSULATION: The internal ArrayList of CartItems is private. Adding, removing,
 *    clearing, and total calculations are performed strictly through validated methods.
 */
public class Cart {
    private final List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a food item with the specified quantity. If the item is already in the cart,
     * its quantity is incremented.
     */
    public void addItem(FoodItem foodItem, Restaurant restaurant, int quantity) {
        if (foodItem == null || restaurant == null) {
            throw new IllegalArgumentException("Food item and restaurant must be selected.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        for (CartItem existing : items) {
            if (existing.getFoodItem().getItemId().equals(foodItem.getItemId())) {
                existing.addQuantity(quantity);
                return;
            }
        }
        items.add(new CartItem(foodItem, restaurant, quantity));
    }

    /**
     * Removes an item from the cart by its index.
     */
    public void removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Please select a valid cart item to remove.");
        }
        items.remove(index);
    }

    /**
     * Clears all items from the cart.
     */
    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalItemCount() {
        int count = 0;
        for (CartItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
