package fooddelivery.payment;

/**
 * Simulated Cash on Delivery (COD) Payment implementation.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: Extends abstract class Payment.
 * 2. POLYMORPHISM: Overrides processPayment() with Cash on Delivery simulation logic.
 */
public class CashOnDelivery extends Payment {

    public CashOnDelivery(double amount) {
        super(amount);
    }

    @Override
    public String getMethodName() {
        return "Cash on Delivery (COD)";
    }

    @Override
    public String processPayment() {
        String ref = "COD-" + (100000 + (int) (Math.random() * 900000));
        markAsPaid(ref);
        return String.format(
                "Cash on Delivery confirmed for ₹%.2f. Please pay ₹%.2f in cash upon delivery (Ref: %s).",
                getAmount(), getAmount(), ref
        );
    }
}
