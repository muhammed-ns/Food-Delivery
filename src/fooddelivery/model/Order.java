package fooddelivery.model;

import fooddelivery.payment.Payment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a customer's Order containing purchased items, payment details,
 * assigned delivery partner, and order lifecycle status.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. ENCAPSULATION: Order status transitions, cancellation rules, and delivery partner
 *    assignments are strictly validated inside this class. External classes cannot
 *    arbitrarily set an invalid status.
 * 2. POLYMORPHISM: Holds a Payment reference and invokes payment.processPayment()
 *    without knowing the concrete subclass (UPIPayment, CardPayment, or CashOnDelivery).
 */
public class Order {
    private static int orderSequence = 1001;

    private final String orderId;
    private final Customer customer;
    private final List<CartItem> items;
    private final double totalAmount;
    private final Payment payment;
    private final String paymentReceipt;
    private final String createdAt;

    private OrderStatus status;
    private DeliveryPartner deliveryPartner;

    public Order(Customer customer, List<CartItem> cartItems, Payment payment) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an order with an empty cart.");
        }
        if (payment == null) {
            throw new IllegalArgumentException("Payment method must be provided.");
        }

        this.orderId = "ORD-" + (orderSequence++);
        this.customer = customer;
        this.items = new ArrayList<>();
        double calculatedTotal = 0.0;
        for (CartItem item : cartItems) {
            // Create a snapshot copy of each CartItem so clearing the cart doesn't affect the Order
            CartItem snapshot = new CartItem(item.getFoodItem(), item.getRestaurant(), item.getQuantity());
            this.items.add(snapshot);
            calculatedTotal += snapshot.getSubtotal();
        }
        this.totalAmount = calculatedTotal;
        this.payment = payment;
        // Polymorphic call to processPayment()
        this.paymentReceipt = this.payment.processPayment();
        this.status = OrderStatus.PLACED;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a"));
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Payment getPayment() {
        return payment;
    }

    public String getPaymentReceipt() {
        return paymentReceipt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    /**
     * Assigns a delivery partner when the order is confirmed or progressing.
     */
    public void assignDeliveryPartner(DeliveryPartner partner) {
        if (partner == null) {
            throw new IllegalArgumentException("Delivery partner cannot be null.");
        }
        if (status == OrderStatus.CANCELLED || status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot assign a partner to a " + status.getDisplayName() + " order.");
        }
        if (this.deliveryPartner != null) {
            this.deliveryPartner.setAvailable(true);
        }
        this.deliveryPartner = partner;
        this.deliveryPartner.setAvailable(false);
    }

    /**
     * Advances the order to the next sequential status:
     * PLACED -> CONFIRMED -> PREPARING -> READY FOR PICKUP -> OUT FOR DELIVERY -> DELIVERED
     *
     * Encapsulates all state-transition rules inside Order.
     */
    public OrderStatus advanceToNextStatus(DeliveryPartner defaultPartnerIfConfirmed) {
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order " + orderId + " is CANCELLED and cannot be updated further.");
        }
        if (status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order " + orderId + " has already been DELIVERED.");
        }

        OrderStatus next = status.getNextStatus();
        if (next == null) {
            throw new IllegalStateException("No further status transitions available.");
        }

        // Automatically assign delivery partner upon confirmation if not already assigned
        if (next == OrderStatus.CONFIRMED && this.deliveryPartner == null && defaultPartnerIfConfirmed != null) {
            assignDeliveryPartner(defaultPartnerIfConfirmed);
        }

        if ((next == OrderStatus.READY_FOR_PICKUP || next == OrderStatus.OUT_FOR_DELIVERY)
                && this.deliveryPartner == null && defaultPartnerIfConfirmed != null) {
            assignDeliveryPartner(defaultPartnerIfConfirmed);
        }

        this.status = next;

        // Free up delivery partner once order is delivered
        if (this.status == OrderStatus.DELIVERED && this.deliveryPartner != null) {
            this.deliveryPartner.setAvailable(true);
        }

        return this.status;
    }

    /**
     * Cancels the order if it has not yet been delivered or already cancelled.
     */
    public void cancelOrder() {
        if (status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that has already been DELIVERED.");
        }
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already CANCELLED.");
        }

        this.status = OrderStatus.CANCELLED;
        if (this.deliveryPartner != null) {
            this.deliveryPartner.setAvailable(true);
        }
    }

    public String getItemsSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            CartItem ci = items.get(i);
            sb.append(ci.getFoodItem().getName()).append(" x").append(ci.getQuantity());
            if (i < items.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
