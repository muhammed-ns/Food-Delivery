package fooddelivery.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Restaurant that offers a menu of FoodItem objects.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. CLASSES & OBJECTS: Models real-world restaurants (e.g., ABC Restaurant, XYZ Restaurant).
 * 2. ENCAPSULATION: Internal menu ArrayList is private; getMenu() returns an unmodifiable view
 *    so external code cannot corrupt the menu directly.
 */
public class Restaurant {
    private final String restaurantId;
    private final String name;
    private final String cuisineType;
    private final double rating;
    private final List<FoodItem> menu;

    public Restaurant(String restaurantId, String name, String cuisineType, double rating) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.menu = new ArrayList<>();
    }

    public void addFoodItem(FoodItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Food item cannot be null.");
        }
        menu.add(item);
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public double getRating() {
        return rating;
    }

    public List<FoodItem> getMenu() {
        return Collections.unmodifiableList(menu);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (★ %.1f)", name, cuisineType, rating);
    }
}
