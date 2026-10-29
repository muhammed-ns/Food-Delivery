# Food Delivery System — Java OOP Project Prototype

A clean, beginner-friendly, fully working **Java Swing Food Delivery System** built for an **Object-Oriented Programming (OOP)** college demonstration and viva.

---

## 1. Project Folder Structure

```text
Food-Delivery/
├── README.md
├── out/                                          # Compiled .class files
└── src/
    └── fooddelivery/
        ├── Main.java                             # Application entry point (GUI + --verify suite)
        ├── model/
        │   ├── User.java                         # Abstract parent class (Inheritance & Abstraction)
        │   ├── Customer.java                     # Subclass of User (owns a Cart)
        │   ├── DeliveryPartner.java              # Subclass of User (assigned to Orders)
        │   ├── Admin.java                        # Subclass of User (system supervisor)
        │   ├── FoodItem.java                     # Represents menu items with price in ₹
        │   ├── Restaurant.java                   # Holds restaurant info & ArrayList<FoodItem>
        │   ├── CartItem.java                     # Associates a FoodItem with quantity & subtotal
        │   ├── Cart.java                         # Encapsulates item addition, removal & total calculation
        │   ├── OrderStatus.java                  # Enum for PLACED -> CONFIRMED -> ... -> DELIVERED / CANCELLED
        │   └── Order.java                        # Encapsulates order lifecycle, partner assignment & payment
        ├── payment/
        │   ├── Payment.java                      # Abstract base class with abstract processPayment()
        │   ├── UPIPayment.java                   # Polymorphic UPI payment simulation
        │   ├── CardPayment.java                  # Polymorphic Card payment simulation
        │   └── CashOnDelivery.java               # Polymorphic Cash on Delivery simulation
        ├── service/
        │   └── FoodDeliveryService.java          # In-memory data store (Restaurants, Partners, Orders)
        └── ui/
            └── FoodDeliveryFrame.java            # Java Swing desktop GUI
```

---

## 2. Exact Terminal Commands to Compile and Run

Open a terminal inside the `Food-Delivery` project directory and run:

### Step 1: Compile all Java source files
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

### Step 2: Launch the Java Swing Desktop GUI
```bash
java -cp out fooddelivery.Main
```

### Step 3 (Optional): Run the Automated Terminal Verification Suite
```bash
java -cp out fooddelivery.Main --verify
```

---

## 3. OOP Concepts Mapped to Exact Classes and Methods

| OOP Concept | Exact Class(es) | Exact Method(s) / Implementation Details |
| :--- | :--- | :--- |
| **1. Classes & Objects** | `Restaurant`, `FoodItem`, `Cart`, `CartItem`, `Order`, `Customer`, `DeliveryPartner`, `Admin` | Instantiated in `FoodDeliveryService.loadSampleRestaurantsAndMenus()` and `loadSampleDeliveryPartners()`. |
| **2. Encapsulation** | `Order`, `Cart`, `Restaurant`, `FoodItem`, `User` | All fields are `private`. Order status transitions are strictly controlled inside `Order.advanceToNextStatus()` and `Order.cancelOrder()`. `Restaurant.getMenu()` and `Cart.getItems()` return `Collections.unmodifiableList(...)`. |
| **3. Inheritance** | `User` (Parent) $\rightarrow$ `Customer`, `DeliveryPartner`, `Admin` (Subclasses) | Subclasses call `super(userId, name, phone)` to reuse common user properties and override `getRole()`. |
| **4. Polymorphism** | `Payment` (Base) $\rightarrow$ `UPIPayment`, `CardPayment`, `CashOnDelivery` | `Payment.processPayment()` is overridden in all three subclasses and invoked polymorphically inside the `Order` constructor via `this.payment.processPayment()`. |
| **5. Abstraction** | `Payment`, `User` | Declared as `abstract` classes with abstract methods (`processPayment()` and `getRole()`), hiding internal payment/role logic behind a simple interface. |

---

## 4. Sample Demonstration Scenario (Live Viva Walkthrough)

1. **Launch the App**: Run `java -cp out fooddelivery.Main`. Point out the header showing **"Food Delivery System"** and the logged-in customer (**Aarav Sharma**).
2. **Browse Restaurants & Menus**:
   - Click **ABC Restaurant** on the left list to view items like *Chicken Dum Biriyani (₹220.00)*, *Margherita Pizza (₹249.00)*, *Crispy Chicken Burger (₹160.00)*, and *Club Grilled Sandwich (₹120.00)*.
   - Click **XYZ Restaurant** to show its distinct menu (*Farmhouse Cheese Pizza (₹299.00)*, *Double Cheese Veggie Burger (₹149.00)*, etc.).
3. **Add Items to Cart**:
   - Select **ABC Restaurant** $\rightarrow$ select **Chicken Dum Biriyani (₹220.00)** $\rightarrow$ set Quantity spinner to `2` $\rightarrow$ click **+ Add to Cart**.
   - Select **Margherita Pizza (₹249.00)** $\rightarrow$ set Quantity to `1` $\rightarrow$ click **+ Add to Cart**.
   - Point out the Cart table showing individual prices, quantities, subtotals (`₹440.00` and `₹249.00`), and the **Total: ₹689.00**.
4. **Select Payment & Place Order**:
   - Select **UPI Payment** (`aarav@okaxis`), **Card Payment** (`4829`), or **Cash on Delivery (COD)**.
   - Click **Confirm & Place Order**.
   - A confirmation dialog displays the generated Order ID (`ORD-1001`), total amount (`₹689.00`), and simulated payment receipt, then automatically switches to Tab 2 (**Orders & Delivery Tracking**).
5. **Simulate Delivery Progress**:
   - In Tab 2, observe the visual status bar at the top highlighted at **1. PLACED**.
   - Click **Update Status (Next Step →)** repeatedly to step through:
     `PLACED` $\rightarrow$ `CONFIRMED` (automatically assigns delivery partner **Rahul Verma**) $\rightarrow$ `PREPARING` $\rightarrow$ `READY FOR PICKUP` $\rightarrow$ `OUT FOR DELIVERY` $\rightarrow$ `DELIVERED`.
6. **Demonstrate Validation & Cancellation**:
   - Try clicking **Cancel Order** after the order is `DELIVERED` — the system blocks it with a clear validation message.
   - Place a second order and click **Cancel Order** before delivery to show valid cancellation.
