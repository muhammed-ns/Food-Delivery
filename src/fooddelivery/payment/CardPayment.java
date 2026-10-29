package fooddelivery.payment;

/**
 * Simulated Credit/Debit Card Payment implementation.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. INHERITANCE: Extends abstract class Payment.
 * 2. POLYMORPHISM: Overrides processPayment() with Card-specific simulation logic.
 */
public class CardPayment extends Payment {
    private final String maskedCardNumber;

    public CardPayment(double amount, String lastFourDigits) {
        super(amount);
        String digits = (lastFourDigits == null || lastFourDigits.trim().length() != 4)
                ? "4829"
                : lastFourDigits.trim();
        this.maskedCardNumber = "XXXX-XXXX-XXXX-" + digits;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    @Override
    public String getMethodName() {
        return "Card Payment";
    }

    @Override
    public String processPayment() {
        String ref = "CARD-" + (100000 + (int) (Math.random() * 900000));
        markAsPaid(ref);
        return String.format(
                "Card Payment of ₹%.2f authorized on card %s (Auth Ref: %s).",
                getAmount(), maskedCardNumber, ref
        );
    }
}
