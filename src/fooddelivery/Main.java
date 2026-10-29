package fooddelivery;

import fooddelivery.model.Cart;
import fooddelivery.model.Customer;
import fooddelivery.model.DeliveryPartner;
import fooddelivery.model.FoodItem;
import fooddelivery.model.Order;
import fooddelivery.model.OrderStatus;
import fooddelivery.model.Restaurant;
import fooddelivery.model.User;
import fooddelivery.payment.CardPayment;
import fooddelivery.payment.CashOnDelivery;
import fooddelivery.payment.Payment;
import fooddelivery.payment.UPIPayment;
import fooddelivery.service.FoodDeliveryService;
import fooddelivery.ui.FoodDeliveryFrame;
import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for the Java Food Delivery System OOP Project.
 *
 * Usage:
 * - Run normally (`java -cp out fooddelivery.Main`) to launch the Java Swing desktop GUI.
 * - Run with `--verify` (`java -cp out fooddelivery.Main --verify`) to execute an automated
 *   end-to-end verification of all OOP classes, cart calculations, polymorphic payments,
 *   and order status transitions in the terminal.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--verify".equalsIgnoreCase(args[0])) {
            runAutomatedVerification();
            return;
        }

        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Headless environment detected. Running automated verification mode:");
            runAutomatedVerification();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
                // Keep default Look and Feel
            }

            FoodDeliveryService service = new FoodDeliveryService();
            FoodDeliveryFrame frame = new FoodDeliveryFrame(service);
            frame.setVisible(true);
        });
    }

    /**
     * Runs an automated console demonstration & verification of all core project requirements.
     */
    private static void runAutomatedVerification() {
        System.out.println("==============================================================");
        System.out.println("   FOOD DELIVERY SYSTEM — AUTOMATED OOP VERIFICATION SUITE    ");
        System.out.println("==============================================================");

        FoodDeliveryService service = new FoodDeliveryService();

        // 1. Verify Inheritance (User -> Customer, DeliveryPartner, Admin)
        System.out.println("\n[1] Verifying Inheritance Hierarchy (Parent class: User):");
        for (User user : service.getAllRegisteredUsers()) {
            System.out.println("    • " + user.toString());
        }

        // 2. Verify Restaurants & FoodItems
        System.out.println("\n[2] Verifying Sample Restaurants & Food Items:");
        for (Restaurant r : service.getRestaurants()) {
            System.out.println("    • " + r.toString() + " (" + r.getMenu().size() + " menu items)");
        }

        Restaurant abc = service.getRestaurants().get(0); // ABC Restaurant
        FoodItem biriyani = abc.getMenu().get(0);         // Chicken Dum Biriyani (₹220)
        FoodItem pizza = abc.getMenu().get(2);            // Margherita Pizza (₹249)

        // 3. Verify Cart & Quantity Calculations
        Customer customer = service.getCurrentCustomer();
        Cart cart = customer.getCart();
        cart.addItem(biriyani, abc, 2); // 2 x 220 = 440
        cart.addItem(pizza, abc, 1);    // 1 x 249 = 249
        double expectedTotal = 689.00;
        double actualTotal = cart.calculateTotal();
        System.out.printf("\n[3] Verifying Cart Calculation: Expected ₹%.2f | Actual ₹%.2f%n", expectedTotal, actualTotal);
        if (Math.abs(expectedTotal - actualTotal) > 0.001) {
            throw new AssertionError("Cart total mismatch!");
        }

        // 4. Verify Polymorphic Payments (UPIPayment, CardPayment, CashOnDelivery)
        System.out.println("\n[4] Verifying Polymorphism (Payment.processPayment()):");
        Payment upi = new UPIPayment(actualTotal, "aarav@okaxis");
        Payment card = new CardPayment(350.00, "4829");
        Payment cod = new CashOnDelivery(199.00);
        System.out.println("    • UPI  -> " + upi.processPayment());
        System.out.println("    • Card -> " + card.processPayment());
        System.out.println("    • COD  -> " + cod.processPayment());

        // 5. Verify Order Placement & Sequential Status Transitions
        DeliveryPartner partner = service.getDeliveryPartners().get(0);
        Order order = service.placeOrder(upi, partner);
        System.out.println("\n[5] Verifying Order Lifecycle Transitions for " + order.getOrderId() + ":");
        System.out.println("    Initial Status : " + order.getStatus().getDisplayName());

        while (order.getStatus() != OrderStatus.DELIVERED) {
            OrderStatus next = service.advanceOrderStatus(order, partner);
            System.out.println("    Updated Status : " + next.getDisplayName()
                    + " (Partner: " + (order.getDeliveryPartner() != null ? order.getDeliveryPartner().getName() : "None") + ")");
        }

        // 6. Verify Cancellation Validation before and after delivery
        System.out.println("\n[6] Verifying Cancellation & Input Validation Rules:");
        try {
            order.cancelOrder();
            throw new AssertionError("Should not allow cancelling a DELIVERED order!");
        } catch (IllegalStateException ex) {
            System.out.println("    • Correctly blocked cancellation after delivery: " + ex.getMessage());
        }

        // Place second order and cancel it while PLACED
        cart.addItem(abc.getMenu().get(3), abc, 1); // Burger
        Order cancellableOrder = service.placeOrder(new CashOnDelivery(cart.calculateTotal()), partner);
        cancellableOrder.cancelOrder();
        System.out.println("    • Successfully cancelled Order " + cancellableOrder.getOrderId()
                + " before delivery -> Status: " + cancellableOrder.getStatus().getDisplayName());

        System.out.println("\n==============================================================");
        System.out.println("   ALL VERIFICATION CHECKS PASSED SUCCESSFULLY!               ");
        System.out.println("==============================================================");
    }
}
