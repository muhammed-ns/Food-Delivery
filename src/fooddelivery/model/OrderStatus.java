package fooddelivery.model;

/**
 * Enum representing all valid states of a food delivery order.
 *
 * Sequence:
 * PLACED -> CONFIRMED -> PREPARING -> READY_FOR_PICKUP -> OUT_FOR_DELIVERY -> DELIVERED
 * (or CANCELLED before delivery)
 */
public enum OrderStatus {
    PLACED("PLACED", "Order has been placed by the customer"),
    CONFIRMED("CONFIRMED", "Restaurant confirmed the order & partner assigned"),
    PREPARING("PREPARING", "Kitchen is preparing the food items"),
    READY_FOR_PICKUP("READY FOR PICKUP", "Food is packed and ready for delivery pickup"),
    OUT_FOR_DELIVERY("OUT FOR DELIVERY", "Delivery partner is on the way to customer"),
    DELIVERED("DELIVERED", "Order successfully delivered to the customer"),
    CANCELLED("CANCELLED", "Order was cancelled before delivery");

    private final String displayName;
    private final String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the next logical status in the delivery lifecycle, or null if terminal.
     */
    public OrderStatus getNextStatus() {
        switch (this) {
            case PLACED:
                return CONFIRMED;
            case CONFIRMED:
                return PREPARING;
            case PREPARING:
                return READY_FOR_PICKUP;
            case READY_FOR_PICKUP:
                return OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY:
                return DELIVERED;
            case DELIVERED:
            case CANCELLED:
            default:
                return null;
        }
    }

    public boolean canCancel() {
        return this != DELIVERED && this != CANCELLED;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
