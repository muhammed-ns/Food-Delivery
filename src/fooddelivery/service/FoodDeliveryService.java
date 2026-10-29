package fooddelivery.service;

import fooddelivery.model.Admin;
import fooddelivery.model.Customer;
import fooddelivery.model.DeliveryPartner;
import fooddelivery.model.FoodItem;
import fooddelivery.model.Order;
import fooddelivery.model.OrderStatus;
import fooddelivery.model.Restaurant;
import fooddelivery.model.User;
import fooddelivery.payment.Payment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service class that stores in-memory data using Java ArrayLists and coordinates
 * operations between Customers, Restaurants, Cart, Orders, Payments, and Delivery Partners.
 */
public class FoodDeliveryService {
    private final Customer currentCustomer;
    private final Admin systemAdmin;
    private final List<Restaurant> restaurants;
    private final List<DeliveryPartner> deliveryPartners;
    private final List<Order> orders;

    public FoodDeliveryService() {
        this.currentCustomer = new Customer(
                "CUST-101",
                "Aarav Sharma",
                "+91 98765 43210",
                "Room 204, B.Tech Hostel Block A"
        );
        this.systemAdmin = new Admin(
                "ADM-001",
                "Prof. Rajesh Nair",
                "+91 98111 22334",
                "Campus Food Court Operations"
        );
        this.restaurants = new ArrayList<>();
        this.deliveryPartners = new ArrayList<>();
        this.orders = new ArrayList<>();

        loadSampleRestaurantsAndMenus();
        loadSampleDeliveryPartners();
    }

    private void loadSampleRestaurantsAndMenus() {
        // 1. ABC Restaurant
        Restaurant abcRestaurant = new Restaurant("REST-01", "ABC Restaurant", "Multi-Cuisine & South Indian", 4.6);
        abcRestaurant.addFoodItem(new FoodItem("ABC-1", "Chicken Dum Biriyani", "Main Course", 220.00));
        abcRestaurant.addFoodItem(new FoodItem("ABC-2", "Veg Paneer Biriyani", "Main Course", 180.00));
        abcRestaurant.addFoodItem(new FoodItem("ABC-3", "Margherita Pizza", "Pizza", 249.00));
        abcRestaurant.addFoodItem(new FoodItem("ABC-4", "Crispy Chicken Burger", "Burgers", 160.00));
        abcRestaurant.addFoodItem(new FoodItem("ABC-5", "Club Grilled Sandwich", "Snacks", 120.00));
        abcRestaurant.addFoodItem(new FoodItem("ABC-6", "Masala Dosa with Sambar", "South Indian", 95.00));

        // 2. XYZ Restaurant
        Restaurant xyzRestaurant = new Restaurant("REST-02", "XYZ Restaurant", "Fast Food, Pizza & Burgers", 4.5);
        xyzRestaurant.addFoodItem(new FoodItem("XYZ-1", "Farmhouse Cheese Pizza", "Pizza", 299.00));
        xyzRestaurant.addFoodItem(new FoodItem("XYZ-2", "Double Cheese Veggie Burger", "Burgers", 149.00));
        xyzRestaurant.addFoodItem(new FoodItem("XYZ-3", "Peri-Peri Grilled Sandwich", "Sandwiches", 135.00));
        xyzRestaurant.addFoodItem(new FoodItem("XYZ-4", "Hyderabadi Special Biriyani", "Main Course", 250.00));
        xyzRestaurant.addFoodItem(new FoodItem("XYZ-5", "Loaded French Fries", "Sides", 99.00));
        xyzRestaurant.addFoodItem(new FoodItem("XYZ-6", "Cold Coffee Frappe", "Beverages", 110.00));

        // 3. Spice Garden Express
        Restaurant spiceGarden = new Restaurant("REST-03", "Spice Garden Express", "North Indian & Biriyani House", 4.7);
        spiceGarden.addFoodItem(new FoodItem("SPG-1", "Malabar Chicken Biriyani", "Biriyani", 210.00));
        spiceGarden.addFoodItem(new FoodItem("SPG-2", "Paneer Tikka Sandwich", "Snacks", 140.00));
        spiceGarden.addFoodItem(new FoodItem("SPG-3", "Tandoori Paneer Pizza", "Fusion Pizza", 279.00));
        spiceGarden.addFoodItem(new FoodItem("SPG-4", "Butter Naan & Paneer Butter Masala", "Main Course", 195.00));
        spiceGarden.addFoodItem(new FoodItem("SPG-5", "Aloo Tikki Burger", "Burgers", 90.00));
        spiceGarden.addFoodItem(new FoodItem("SPG-6", "Mango Lassi", "Beverages", 75.00));

        restaurants.add(abcRestaurant);
        restaurants.add(xyzRestaurant);
        restaurants.add(spiceGarden);
    }

    private void loadSampleDeliveryPartners() {
        deliveryPartners.add(new DeliveryPartner("DP-201", "Rahul Verma", "+91 91234 56780", "Electric Scooter", "KL-07-BZ-4512"));
        deliveryPartners.add(new DeliveryPartner("DP-202", "Kiran Kumar", "+91 92345 67891", "Motorcycle", "KL-07-CA-8890"));
        deliveryPartners.add(new DeliveryPartner("DP-203", "Sneha Menon", "+91 93456 78902", "Scooter", "KL-07-DN-1123"));
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public Admin getSystemAdmin() {
        return systemAdmin;
    }

    /**
     * Returns all sample User objects (Customer, Admin, DeliveryPartners) to showcase
     * Inheritance and Polymorphism across the User hierarchy.
     */
    public List<User> getAllRegisteredUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.add(currentCustomer);
        allUsers.add(systemAdmin);
        allUsers.addAll(deliveryPartners);
        return Collections.unmodifiableList(allUsers);
    }

    public List<Restaurant> getRestaurants() {
        return Collections.unmodifiableList(restaurants);
    }

    public List<DeliveryPartner> getDeliveryPartners() {
        return Collections.unmodifiableList(deliveryPartners);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    /**
     * Places a new order using the items currently in the customer's cart and the chosen Payment object.
     */
    public Order placeOrder(Payment payment, DeliveryPartner preferredPartner) {
        if (currentCustomer.getCart().isEmpty()) {
            throw new IllegalStateException("Your cart is empty! Please add food items before placing an order.");
        }

        Order newOrder = new Order(
                currentCustomer,
                currentCustomer.getCart().getItems(),
                payment
        );

        orders.add(0, newOrder); // Add newest order at the top
        currentCustomer.getCart().clear();
        return newOrder;
    }

    /**
     * Advances the given order to the next lifecycle state and assigns a delivery partner when confirmed.
     */
    public OrderStatus advanceOrderStatus(Order order, DeliveryPartner selectedPartner) {
        if (order == null) {
            throw new IllegalArgumentException("Please select an order first.");
        }
        DeliveryPartner partnerToUse = selectedPartner;
        if (partnerToUse == null) {
            partnerToUse = findAvailablePartner();
        }
        return order.advanceToNextStatus(partnerToUse);
    }

    private DeliveryPartner findAvailablePartner() {
        for (DeliveryPartner partner : deliveryPartners) {
            if (partner.isAvailable()) {
                return partner;
            }
        }
        return deliveryPartners.isEmpty() ? null : deliveryPartners.get(0);
    }
}
