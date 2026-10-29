package fooddelivery.payment;

/**
 * Abstract base class for all payment methods in the Food Delivery System.
 *
 * OOP CONCEPTS DEMONSTRATED:
 * 1. ABSTRACTION: Defines the high-level contract processPayment() and hides specific
 *    payment processing details from the Order and UI classes.
 * 2. POLYMORPHISM: Subclasses (UPIPayment, CardPayment, CashOnDelivery) override
 *    processPayment() and are invoked via a Payment reference.
 * 3. ENCAPSULATION: Stores payment amount and transaction status privately.
 */
public abstract class Payment {
    private final double amount;
    private boolean paid;
    private String transactionReference;

    public Payment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        this.amount = amount;
        this.paid = false;
        this.transactionReference = "PENDING";
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    protected void markAsPaid(String reference) {
        this.paid = true;
        this.transactionReference = reference;
    }

    protected void setTransactionReference(String reference) {
        this.transactionReference = reference;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    /**
     * Returns the display name of the payment method.
     */
    public abstract String getMethodName();

    /**
     * Polymorphic method implemented differently by UPIPayment, CardPayment, and CashOnDelivery.
     *
     * @return A human-readable simulated receipt/confirmation message.
     */
    public abstract String processPayment();
}
