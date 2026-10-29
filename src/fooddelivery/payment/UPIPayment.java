package fooddelivery.payment;

/**
 * Simulated UPI Payment implementation.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: Extends abstract class Payment.
 * 2. POLYMORPHISM: Overrides processPayment() with UPI-specific simulation logic.
 */
public class UPIPayment extends Payment {
    private final String upiId;

    public UPIPayment(double amount, String upiId) {
        super(amount);
        this.upiId = (upiId == null || upiId.trim().isEmpty()) ? "customer@okaxis" : upiId.trim();
    }

    public String getUpiId() {
        return upiId;
    }

    @Override
    public String getMethodName() {
        return "UPI Payment";
    }

    @Override
    public String processPayment() {
        String ref = "UPI-" + (100000 + (int) (Math.random() * 900000));
        markAsPaid(ref);
        return String.format(
                "UPI Payment of ₹%.2f successful via VPA '%s' (Ref: %s).",
                getAmount(), upiId, ref
        );
    }
}
