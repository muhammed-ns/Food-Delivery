package fooddelivery.ui;

import fooddelivery.model.Cart;
import fooddelivery.model.CartItem;
import fooddelivery.model.Customer;
import fooddelivery.model.DeliveryPartner;
import fooddelivery.model.FoodItem;
import fooddelivery.model.Order;
import fooddelivery.model.OrderStatus;
import fooddelivery.model.Restaurant;
import fooddelivery.payment.CardPayment;
import fooddelivery.payment.CashOnDelivery;
import fooddelivery.payment.Payment;
import fooddelivery.payment.UPIPayment;
import fooddelivery.service.FoodDeliveryService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Main Java Swing GUI Frame for the Food Delivery System prototype.
 * Designed for clarity, responsiveness, and easy demonstration during a college OOP viva.
 */
public class FoodDeliveryFrame extends JFrame {
    // Consistent presentation color palette
    private static final Color PRIMARY_COLOR = new Color(220, 53, 69);      // Crimson / Coral Red
    private static final Color PRIMARY_DARK = new Color(176, 42, 55);
    private static final Color ACCENT_GREEN = new Color(25, 135, 84);       // Success Green
    private static final Color ACCENT_BLUE = new Color(13, 110, 253);       // Action Blue
    private static final Color WARNING_ORANGE = new Color(253, 126, 20);    // Warning Orange
    private static final Color BG_LIGHT = new Color(248, 249, 250);         // Light Gray Background
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_MUTED = new Color(108, 117, 125);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font SUBHEADER_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font SECTION_TITLE_FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BOLD_BODY_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font TOTAL_FONT = new Font("SansSerif", Font.BOLD, 18);

    private final FoodDeliveryService service;

    // Top Navigation / Tabs
    private JTabbedPane tabbedPane;

    // Tab 1: Restaurants, Menu & Cart components
    private DefaultListModel<Restaurant> restaurantListModel;
    private JList<Restaurant> restaurantList;
    private JLabel selectedRestaurantInfoLabel;
    private DefaultTableModel menuTableModel;
    private JTable menuTable;
    private JSpinner quantitySpinner;

    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JLabel cartCountBadgeLabel;
    private JLabel cartTotalLabel;
    private JRadioButton upiRadio;
    private JRadioButton cardRadio;
    private JRadioButton codRadio;
    private JTextField paymentDetailField;
    private JLabel paymentDetailPromptLabel;

    // Tab 2: Orders & Delivery Simulation components
    private DefaultTableModel ordersTableModel;
    private JTable ordersTable;
    private JLabel activeOrderIdLabel;
    private JLabel activeOrderStatusLabel;
    private JLabel activeOrderPartnerLabel;
    private JLabel activeOrderPaymentLabel;
    private JTextArea orderReceiptArea;
    private JComboBox<DeliveryPartner> partnerComboBox;
    private JPanel statusTimelinePanel;
    private JLabel[] timelineStepLabels;

    // Bottom status bar
    private JLabel statusBarLabel;

    public FoodDeliveryFrame(FoodDeliveryService service) {
        this.service = service;
        initializeWindow();
        buildUI();
        loadInitialData();
    }

    private void initializeWindow() {
        setTitle("Food Delivery System — Java OOP Project Prototype");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1220, 780);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT);
        setLayout(new BorderLayout(0, 0));
    }

    private void buildUI() {
        add(createHeaderPanel(), BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBackground(CARD_BG);
        tabbedPane.setBorder(new EmptyBorder(8, 12, 8, 12));

        tabbedPane.addTab(" 1. Restaurants, Menu & Cart ", createOrderingTab());
        tabbedPane.addTab(" 2. Orders & Delivery Tracking ", createOrdersAndDeliveryTab());

        add(tabbedPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(16, 4));
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(14, 22, 14, 22));

        JPanel leftBox = new JPanel();
        leftBox.setOpaque(false);
        leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Food Delivery System");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);

        Customer customer = service.getCurrentCustomer();
        JLabel welcomeLabel = new JLabel(String.format(
                "Welcome, %s (%s)  |  Delivery Address: %s",
                customer.getName(), customer.getUserId(), customer.getDeliveryAddress()
        ));
        welcomeLabel.setFont(SUBHEADER_FONT);
        welcomeLabel.setForeground(new Color(255, 235, 238));

        leftBox.add(titleLabel);
        leftBox.add(Box.createVerticalStrut(4));
        leftBox.add(welcomeLabel);

        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        rightBox.setOpaque(false);

        cartCountBadgeLabel = new JLabel("Cart: 0 items (₹0.00)");
        cartCountBadgeLabel.setFont(BOLD_BODY_FONT);
        cartCountBadgeLabel.setForeground(PRIMARY_COLOR);
        cartCountBadgeLabel.setOpaque(true);
        cartCountBadgeLabel.setBackground(Color.WHITE);
        cartCountBadgeLabel.setBorder(new EmptyBorder(6, 12, 6, 12));

        JButton aboutOopBtn = createStyledButton("OOP Concepts Summary", new Color(33, 37, 41), Color.WHITE);
        aboutOopBtn.addActionListener(e -> showOopSummaryDialog());

        rightBox.add(cartCountBadgeLabel);
        rightBox.add(aboutOopBtn);

        header.add(leftBox, BorderLayout.WEST);
        header.add(rightBox, BorderLayout.EAST);
        return header;
    }

    private JPanel createOrderingTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(BG_LIGHT);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left: Restaurants & Menu
        JPanel leftCatalogPanel = createRestaurantAndMenuPanel();

        // Right: Cart & Checkout
        JPanel rightCartPanel = createCartAndCheckoutPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftCatalogPanel, rightCartPanel);
        splitPane.setResizeWeight(0.58);
        splitPane.setDividerLocation(680);
        splitPane.setBorder(null);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createRestaurantAndMenuPanel() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(BG_LIGHT);

        // 1. Restaurant selection card (West)
        JPanel restaurantCard = createTitledCard("Select Restaurant");
        restaurantCard.setPreferredSize(new Dimension(240, 0));

        restaurantListModel = new DefaultListModel<>();
        restaurantList = new JList<>(restaurantListModel);
        restaurantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        restaurantList.setFont(BODY_FONT);
        restaurantList.setFixedCellHeight(56);
        restaurantList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Restaurant) {
                    Restaurant r = (Restaurant) value;
                    String text = String.format("<html><b>%s</b><br><span style='font-size:10px;'>%s &bull; ★ %.1f</span></html>",
                            r.getName(), r.getCuisineType(), r.getRating());
                    label.setText(text);
                }
                label.setBorder(new EmptyBorder(6, 10, 6, 10));
                if (isSelected) {
                    label.setBackground(PRIMARY_COLOR);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(CARD_BG);
                    label.setForeground(TEXT_DARK);
                }
                return label;
            }
        });
        restaurantList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateMenuForSelectedRestaurant();
            }
        });

        restaurantCard.add(new JScrollPane(restaurantList), BorderLayout.CENTER);

        // 2. Menu items table & Add-to-Cart controls (Center)
        JPanel menuCard = createTitledCard("Food Menu");

        selectedRestaurantInfoLabel = new JLabel("Select a restaurant to view its menu");
        selectedRestaurantInfoLabel.setFont(BOLD_BODY_FONT);
        selectedRestaurantInfoLabel.setForeground(PRIMARY_DARK);
        selectedRestaurantInfoLabel.setBorder(new EmptyBorder(0, 2, 8, 2));
        menuCard.add(selectedRestaurantInfoLabel, BorderLayout.NORTH);

        String[] menuColumns = {"Code", "Food Item", "Category", "Price (₹)"};
        menuTableModel = new DefaultTableModel(menuColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(menuTableModel);
        styleTable(menuTable);
        menuTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        menuTable.getColumnModel().getColumn(1).setPreferredWidth(190);
        menuTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        menuTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        menuCard.add(new JScrollPane(menuTable), BorderLayout.CENTER);

        // Bottom bar of Menu card: Quantity + Add to Cart button
        JPanel addControlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        addControlsPanel.setBackground(CARD_BG);
        addControlsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(BOLD_BODY_FONT);

        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        quantitySpinner.setFont(BOLD_BODY_FONT);
        quantitySpinner.setPreferredSize(new Dimension(70, 34));

        JButton addToCartButton = createStyledButton("+ Add to Cart", ACCENT_GREEN, Color.WHITE);
        addToCartButton.addActionListener(e -> handleAddToCart());

        addControlsPanel.add(qtyLabel);
        addControlsPanel.add(quantitySpinner);
        addControlsPanel.add(addToCartButton);

        menuCard.add(addControlsPanel, BorderLayout.SOUTH);

        container.add(restaurantCard, BorderLayout.WEST);
        container.add(menuCard, BorderLayout.CENTER);
        return container;
    }

    private JPanel createCartAndCheckoutPanel() {
        JPanel cartCard = createTitledCard("Your Cart & Checkout");

        String[] cartCols = {"Food Item", "Restaurant", "Price (₹)", "Qty", "Subtotal (₹)"};
        cartTableModel = new DefaultTableModel(cartCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        styleTable(cartTable);
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(145);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(115);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(75);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(45);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(90);

        cartCard.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // South panel inside Cart card: Cart actions + Payment selection + Place Order
        JPanel checkoutBox = new JPanel();
        checkoutBox.setLayout(new BoxLayout(checkoutBox, BoxLayout.Y_AXIS));
        checkoutBox.setBackground(CARD_BG);
        checkoutBox.setBorder(new EmptyBorder(8, 4, 4, 4));

        // Row 1: Remove Item / Clear Cart / Total Label
        JPanel cartButtonsRow = new JPanel(new BorderLayout(8, 4));
        cartButtonsRow.setBackground(CARD_BG);

        JPanel leftCartBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        leftCartBtns.setBackground(CARD_BG);

        JButton removeItemBtn = createStyledButton("Remove Item", WARNING_ORANGE, Color.WHITE);
        removeItemBtn.addActionListener(e -> handleRemoveCartItem());

        JButton clearCartBtn = createStyledButton("Clear Cart", new Color(108, 117, 125), Color.WHITE);
        clearCartBtn.addActionListener(e -> handleClearCart());

        leftCartBtns.add(removeItemBtn);
        leftCartBtns.add(clearCartBtn);

        cartTotalLabel = new JLabel("Total: ₹0.00", SwingConstants.RIGHT);
        cartTotalLabel.setFont(TOTAL_FONT);
        cartTotalLabel.setForeground(PRIMARY_DARK);

        cartButtonsRow.add(leftCartBtns, BorderLayout.WEST);
        cartButtonsRow.add(cartTotalLabel, BorderLayout.EAST);

        // Row 2: Payment Method Panel (Polymorphism Demo)
        JPanel paymentPanel = new JPanel(new GridBagLayout());
        paymentPanel.setBackground(BG_LIGHT);
        paymentPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(BORDER_COLOR, 1),
                "Select Payment Method (Polymorphism: Payment -> processPayment())",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                TEXT_DARK
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        upiRadio = new JRadioButton("UPI Payment", true);
        cardRadio = new JRadioButton("Card Payment");
        codRadio = new JRadioButton("Cash on Delivery (COD)");

        upiRadio.setFont(BOLD_BODY_FONT);
        cardRadio.setFont(BOLD_BODY_FONT);
        codRadio.setFont(BOLD_BODY_FONT);
        upiRadio.setOpaque(false);
        cardRadio.setOpaque(false);
        codRadio.setOpaque(false);

        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(upiRadio);
        paymentGroup.add(cardRadio);
        paymentGroup.add(codRadio);

        paymentDetailPromptLabel = new JLabel("Simulated UPI ID:");
        paymentDetailPromptLabel.setFont(BODY_FONT);
        paymentDetailField = new JTextField("aarav@okaxis", 16);
        paymentDetailField.setFont(BODY_FONT);

        upiRadio.addActionListener(e -> {
            paymentDetailPromptLabel.setText("Simulated UPI ID:");
            paymentDetailField.setText("aarav@okaxis");
            paymentDetailField.setEnabled(true);
        });
        cardRadio.addActionListener(e -> {
            paymentDetailPromptLabel.setText("Last 4 Card Digits:");
            paymentDetailField.setText("4829");
            paymentDetailField.setEnabled(true);
        });
        codRadio.addActionListener(e -> {
            paymentDetailPromptLabel.setText("COD Instruction:");
            paymentDetailField.setText("Pay cash at doorstep");
            paymentDetailField.setEnabled(false);
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        paymentPanel.add(upiRadio, gbc);
        gbc.gridx = 1;
        paymentPanel.add(cardRadio, gbc);
        gbc.gridx = 2;
        paymentPanel.add(codRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        paymentPanel.add(paymentDetailPromptLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        paymentPanel.add(paymentDetailField, gbc);

        // Row 3: Checkout / Place Order button
        JPanel placeOrderRow = new JPanel(new BorderLayout());
        placeOrderRow.setBackground(CARD_BG);
        placeOrderRow.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton placeOrderBtn = createStyledButton("Confirm & Place Order", PRIMARY_COLOR, Color.WHITE);
        placeOrderBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        placeOrderBtn.setPreferredSize(new Dimension(0, 44));
        placeOrderBtn.addActionListener(e -> handlePlaceOrder());

        placeOrderRow.add(placeOrderBtn, BorderLayout.CENTER);

        checkoutBox.add(cartButtonsRow);
        checkoutBox.add(Box.createVerticalStrut(8));
        checkoutBox.add(paymentPanel);
        checkoutBox.add(placeOrderRow);

        cartCard.add(checkoutBox, BorderLayout.SOUTH);
        return cartCard;
    }

    private JPanel createOrdersAndDeliveryTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(BG_LIGHT);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top: Visual Order Status Pipeline
        statusTimelinePanel = createStatusTimelinePanel();
        mainPanel.add(statusTimelinePanel, BorderLayout.NORTH);

        // Center Split: Left = Orders Table, Right = Order Details & Delivery Partner Controls
        JPanel ordersListCard = createTitledCard("Placed Orders History");
        String[] orderCols = {"Order ID", "Placed Time", "Items Summary", "Total (₹)", "Payment", "Delivery Partner", "Current Status"};
        ordersTableModel = new DefaultTableModel(orderCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(ordersTableModel);
        styleTable(ordersTable);
        ordersTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        ordersTable.getColumnModel().getColumn(1).setPreferredWidth(135);
        ordersTable.getColumnModel().getColumn(2).setPreferredWidth(190);
        ordersTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        ordersTable.getColumnModel().getColumn(4).setPreferredWidth(110);
        ordersTable.getColumnModel().getColumn(5).setPreferredWidth(135);
        ordersTable.getColumnModel().getColumn(6).setPreferredWidth(135);

        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedOrderDetails();
            }
        });

        ordersListCard.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        JPanel rightDetailsCard = createOrderDetailsAndDeliveryControlsCard();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ordersListCard, rightDetailsCard);
        splitPane.setResizeWeight(0.60);
        splitPane.setDividerLocation(700);
        splitPane.setBorder(null);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createStatusTimelinePanel() {
        JPanel panel = createTitledCard("Live Order Status Progression (PLACED → CONFIRMED → PREPARING → READY FOR PICKUP → OUT FOR DELIVERY → DELIVERED)");
        JPanel stepsRow = new JPanel(new GridLayout(1, 6, 8, 0));
        stepsRow.setBackground(CARD_BG);
        stepsRow.setBorder(new EmptyBorder(6, 6, 6, 6));

        OrderStatus[] flowStatuses = {
                OrderStatus.PLACED,
                OrderStatus.CONFIRMED,
                OrderStatus.PREPARING,
                OrderStatus.READY_FOR_PICKUP,
                OrderStatus.OUT_FOR_DELIVERY,
                OrderStatus.DELIVERED
        };

        timelineStepLabels = new JLabel[flowStatuses.length];
        for (int i = 0; i < flowStatuses.length; i++) {
            JLabel stepLabel = new JLabel((i + 1) + ". " + flowStatuses[i].getDisplayName(), SwingConstants.CENTER);
            stepLabel.setFont( new Font("SansSerif", Font.BOLD, 12));
            stepLabel.setOpaque(true);
            stepLabel.setBackground(BG_LIGHT);
            stepLabel.setForeground(TEXT_MUTED);
            stepLabel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(10, 6, 10, 6)
            ));
            timelineStepLabels[i] = stepLabel;
            stepsRow.add(stepLabel);
        }

        panel.add(stepsRow, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrderDetailsAndDeliveryControlsCard() {
        JPanel card = createTitledCard("Selected Order & Delivery Management");

        JPanel infoGrid = new JPanel(new GridLayout(4, 1, 4, 6));
        infoGrid.setBackground(CARD_BG);
        infoGrid.setBorder(new EmptyBorder(4, 6, 8, 6));

        activeOrderIdLabel = new JLabel("Order ID: None selected");
        activeOrderIdLabel.setFont(SECTION_TITLE_FONT);
        activeOrderIdLabel.setForeground(PRIMARY_DARK);

        activeOrderStatusLabel = new JLabel("Current Status: -");
        activeOrderStatusLabel.setFont(BOLD_BODY_FONT);

        activeOrderPartnerLabel = new JLabel("Assigned Delivery Partner: Not assigned yet");
        activeOrderPartnerLabel.setFont(BODY_FONT);

        activeOrderPaymentLabel = new JLabel("Payment Method: -");
        activeOrderPaymentLabel.setFont(BODY_FONT);

        infoGrid.add(activeOrderIdLabel);
        infoGrid.add(activeOrderStatusLabel);
        infoGrid.add(activeOrderPartnerLabel);
        infoGrid.add(activeOrderPaymentLabel);

        card.add(infoGrid, BorderLayout.NORTH);

        orderReceiptArea = new JTextArea();
        orderReceiptArea.setEditable(false);
        orderReceiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        orderReceiptArea.setBackground(new Color(252, 252, 252));
        orderReceiptArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        orderReceiptArea.setText("Place an order in Tab 1 to view its full itemized receipt, payment confirmation, and delivery tracking details here.");

        card.add(new JScrollPane(orderReceiptArea), BorderLayout.CENTER);

        // Delivery Partner Assignment + Status Simulation Controls
        JPanel controlsBox = new JPanel();
        controlsBox.setLayout(new BoxLayout(controlsBox, BoxLayout.Y_AXIS));
        controlsBox.setBackground(CARD_BG);
        controlsBox.setBorder(new EmptyBorder(10, 4, 4, 4));

        JPanel partnerRow = new JPanel(new BorderLayout(8, 4));
        partnerRow.setBackground(CARD_BG);
        partnerRow.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(BORDER_COLOR, 1),
                "Delivery Partner Assignment",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                TEXT_DARK
        ));

        partnerComboBox = new JComboBox<>();
        partnerComboBox.setFont(BODY_FONT);
        for (DeliveryPartner dp : service.getDeliveryPartners()) {
            partnerComboBox.addItem(dp);
        }

        JButton assignPartnerBtn = createStyledButton("Assign Partner", ACCENT_BLUE, Color.WHITE);
        assignPartnerBtn.addActionListener(e -> handleAssignDeliveryPartner());

        partnerRow.add(partnerComboBox, BorderLayout.CENTER);
        partnerRow.add(assignPartnerBtn, BorderLayout.EAST);

        JPanel actionButtonsRow = new JPanel(new GridLayout(1, 2, 10, 0));
        actionButtonsRow.setBackground(CARD_BG);
        actionButtonsRow.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton updateStatusBtn = createStyledButton("Update Status (Next Step →)", ACCENT_GREEN, Color.WHITE);
        updateStatusBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateStatusBtn.setPreferredSize(new Dimension(0, 42));
        updateStatusBtn.addActionListener(e -> handleAdvanceOrderStatus());

        JButton cancelOrderBtn = createStyledButton("Cancel Order", PRIMARY_COLOR, Color.WHITE);
        cancelOrderBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelOrderBtn.setPreferredSize(new Dimension(0, 42));
        cancelOrderBtn.addActionListener(e -> handleCancelOrder());

        actionButtonsRow.add(updateStatusBtn);
        actionButtonsRow.add(cancelOrderBtn);

        controlsBox.add(partnerRow);
        controlsBox.add(actionButtonsRow);

        card.add(controlsBox, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(33, 37, 41));
        bar.setBorder(new EmptyBorder(8, 16, 8, 16));

        statusBarLabel = new JLabel("Ready. Select a restaurant, choose food items & quantity, and click '+ Add to Cart'.");
        statusBarLabel.setFont(BODY_FONT);
        statusBarLabel.setForeground(Color.WHITE);

        JLabel adminInfoLabel = new JLabel("Supervised by: " + service.getSystemAdmin().toString());
        adminInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        adminInfoLabel.setForeground(new Color(173, 181, 189));

        bar.add(statusBarLabel, BorderLayout.WEST);
        bar.add(adminInfoLabel, BorderLayout.EAST);
        return bar;
    }

    private void loadInitialData() {
        List<Restaurant> restaurants = service.getRestaurants();
        for (Restaurant r : restaurants) {
            restaurantListModel.addElement(r);
        }
        if (!restaurants.isEmpty()) {
            restaurantList.setSelectedIndex(0);
        }
        refreshCartDisplay();
    }

    private void updateMenuForSelectedRestaurant() {
        Restaurant selected = restaurantList.getSelectedValue();
        menuTableModel.setRowCount(0);
        if (selected == null) {
            selectedRestaurantInfoLabel.setText("Select a restaurant to view its menu");
            return;
        }

        selectedRestaurantInfoLabel.setText(String.format(
                "%s  —  %s  (Rating: ★ %.1f)",
                selected.getName(), selected.getCuisineType(), selected.getRating()
        ));

        for (FoodItem item : selected.getMenu()) {
            menuTableModel.addRow(new Object[]{
                    item.getItemId(),
                    item.getName(),
                    item.getCategory(),
                    String.format("₹%.2f", item.getPrice())
            });
        }
        if (menuTableModel.getRowCount() > 0) {
            menuTable.setRowSelectionInterval(0, 0);
        }
        setStatusMessage("Viewing menu for " + selected.getName() + ".");
    }

    private void handleAddToCart() {
        Restaurant selectedRestaurant = restaurantList.getSelectedValue();
        if (selectedRestaurant == null) {
            showError("Please select a restaurant first.");
            return;
        }

        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Please select a food item from the menu table.");
            return;
        }

        int quantity;
        try {
            quantitySpinner.commitEdit();
            quantity = (Integer) quantitySpinner.getValue();
        } catch (Exception ex) {
            showError("Please enter a valid numeric quantity between 1 and 20.");
            return;
        }

        if (quantity <= 0) {
            showError("Quantity must be at least 1.");
            return;
        }

        FoodItem selectedItem = selectedRestaurant.getMenu().get(selectedRow);
        service.getCurrentCustomer().getCart().addItem(selectedItem, selectedRestaurant, quantity);

        refreshCartDisplay();
        quantitySpinner.setValue(1);
        setStatusMessage(String.format("Added %d x %s (₹%.2f each) to your cart.",
                quantity, selectedItem.getName(), selectedItem.getPrice()));
    }

    private void handleRemoveCartItem() {
        Cart cart = service.getCurrentCustomer().getCart();
        if (cart.isEmpty()) {
            showError("Your cart is already empty.");
            return;
        }

        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Please select an item in the Cart table to remove.");
            return;
        }

        CartItem removedItem = cart.getItems().get(selectedRow);
        cart.removeItem(selectedRow);
        refreshCartDisplay();
        setStatusMessage("Removed '" + removedItem.getFoodItem().getName() + "' from the cart.");
    }

    private void handleClearCart() {
        Cart cart = service.getCurrentCustomer().getCart();
        if (cart.isEmpty()) {
            showError("Your cart is already empty.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear all items from your cart?",
                "Confirm Clear Cart",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            cart.clear();
            refreshCartDisplay();
            setStatusMessage("Cart cleared.");
        }
    }

    private void refreshCartDisplay() {
        Cart cart = service.getCurrentCustomer().getCart();
        cartTableModel.setRowCount(0);

        for (CartItem item : cart.getItems()) {
            cartTableModel.addRow(new Object[]{
                    item.getFoodItem().getName(),
                    item.getRestaurant().getName(),
                    String.format("₹%.2f", item.getFoodItem().getPrice()),
                    item.getQuantity(),
                    String.format("₹%.2f", item.getSubtotal())
            });
        }

        double total = cart.calculateTotal();
        int count = cart.getTotalItemCount();
        cartTotalLabel.setText(String.format("Total: ₹%.2f", total));
        cartCountBadgeLabel.setText(String.format("Cart: %d item%s (₹%.2f)", count, count == 1 ? "" : "s", total));
    }

    private void handlePlaceOrder() {
        Cart cart = service.getCurrentCustomer().getCart();
        if (cart.isEmpty()) {
            showError("Cannot checkout with an empty cart! Please add at least one food item.");
            return;
        }

        double totalAmount = cart.calculateTotal();

        // Polymorphism in action: Payment reference holds UPIPayment, CardPayment, or CashOnDelivery
        Payment payment;
        String detailInput = paymentDetailField.getText().trim();
        if (upiRadio.isSelected()) {
            if (detailInput.isEmpty() || !detailInput.contains("@")) {
                showError("Please enter a valid simulated UPI ID containing '@' (e.g., aarav@okaxis).");
                return;
            }
            payment = new UPIPayment(totalAmount, detailInput);
        } else if (cardRadio.isSelected()) {
            if (!detailInput.matches("\\d{4}")) {
                showError("Please enter exactly 4 digits for the simulated card number (e.g., 4829).");
                return;
            }
            payment = new CardPayment(totalAmount, detailInput);
        } else {
            payment = new CashOnDelivery(totalAmount);
        }

        try {
            DeliveryPartner selectedPartner = (DeliveryPartner) partnerComboBox.getSelectedItem();
            Order createdOrder = service.placeOrder(payment, selectedPartner);

            refreshCartDisplay();
            refreshOrdersTable();
            ordersTable.setRowSelectionInterval(0, 0);
            updateSelectedOrderDetails();

            String confirmationMsg = String.format(
                    "Order Placed Successfully!\n\n" +
                    "Order ID: %s\n" +
                    "Customer: %s\n" +
                    "Total Amount: ₹%.2f\n" +
                    "Payment Method: %s\n" +
                    "Payment Result: %s\n" +
                    "Current Status: %s\n\n" +
                    "Switching to the 'Orders & Delivery Tracking' tab so you can simulate delivery progress.",
                    createdOrder.getOrderId(),
                    createdOrder.getCustomer().getName(),
                    createdOrder.getTotalAmount(),
                    createdOrder.getPayment().getMethodName(),
                    createdOrder.getPaymentReceipt(),
                    createdOrder.getStatus().getDisplayName()
            );

            JOptionPane.showMessageDialog(
                    this,
                    confirmationMsg,
                    "Order Confirmation — " + createdOrder.getOrderId(),
                    JOptionPane.INFORMATION_MESSAGE
            );

            tabbedPane.setSelectedIndex(1);
            setStatusMessage("Order " + createdOrder.getOrderId() + " placed! Click 'Update Status (Next Step →)' to simulate delivery.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshOrdersTable() {
        int previousSelectedRow = ordersTable.getSelectedRow();
        String selectedOrderId = null;
        if (previousSelectedRow >= 0 && previousSelectedRow < service.getOrders().size()) {
            selectedOrderId = service.getOrders().get(previousSelectedRow).getOrderId();
        }

        ordersTableModel.setRowCount(0);
        List<Order> orders = service.getOrders();
        int rowToSelect = 0;

        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            String partnerText = (o.getDeliveryPartner() == null)
                    ? "Unassigned (Pending Confirm)"
                    : o.getDeliveryPartner().getName();
            ordersTableModel.addRow(new Object[]{
                    o.getOrderId(),
                    o.getCreatedAt(),
                    o.getItemsSummary(),
                    String.format("₹%.2f", o.getTotalAmount()),
                    o.getPayment().getMethodName(),
                    partnerText,
                    o.getStatus().getDisplayName()
            });
            if (selectedOrderId != null && o.getOrderId().equals(selectedOrderId)) {
                rowToSelect = i;
            }
        }

        if (!orders.isEmpty()) {
            ordersTable.setRowSelectionInterval(rowToSelect, rowToSelect);
        }
    }

    private Order getSelectedOrder() {
        int row = ordersTable.getSelectedRow();
        List<Order> orders = service.getOrders();
        if (row < 0 || row >= orders.size()) {
            return null;
        }
        return orders.get(row);
    }

    private void updateSelectedOrderDetails() {
        Order order = getSelectedOrder();
        if (order == null) {
            activeOrderIdLabel.setText("Order ID: None selected");
            activeOrderStatusLabel.setText("Current Status: -");
            activeOrderPartnerLabel.setText("Assigned Delivery Partner: -");
            activeOrderPaymentLabel.setText("Payment Method: -");
            updateTimelineVisuals(null);
            return;
        }

        activeOrderIdLabel.setText("Order ID: " + order.getOrderId() + "  (" + order.getCreatedAt() + ")");
        activeOrderStatusLabel.setText("Current Status: " + order.getStatus().getDisplayName()
                + " — " + order.getStatus().getDescription());

        if (order.getStatus() == OrderStatus.CANCELLED) {
            activeOrderStatusLabel.setForeground(PRIMARY_COLOR);
        } else if (order.getStatus() == OrderStatus.DELIVERED) {
            activeOrderStatusLabel.setForeground(ACCENT_GREEN);
        } else {
            activeOrderStatusLabel.setForeground(ACCENT_BLUE);
        }

        DeliveryPartner dp = order.getDeliveryPartner();
        activeOrderPartnerLabel.setText("Assigned Delivery Partner: "
                + (dp == null ? "Will be assigned on CONFIRMED status (or click 'Assign Partner')" : dp.toString() + " | Phone: " + dp.getPhone()));

        activeOrderPaymentLabel.setText("Payment: " + order.getPayment().getMethodName()
                + " (Ref: " + order.getPayment().getTransactionReference() + ")");

        // Build receipt text
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================\n");
        sb.append("               FOOD DELIVERY ORDER RECEIPT              \n");
        sb.append("========================================================\n");
        sb.append(String.format("Order ID       : %s\n", order.getOrderId()));
        sb.append(String.format("Order Date     : %s\n", order.getCreatedAt()));
        sb.append(String.format("Customer       : %s (%s)\n", order.getCustomer().getName(), order.getCustomer().getPhone()));
        sb.append(String.format("Delivery Addr  : %s\n", order.getCustomer().getDeliveryAddress()));
        sb.append(String.format("Order Status   : %s\n", order.getStatus().getDisplayName()));
        sb.append(String.format("Delivery Agent : %s\n", dp == null ? "Pending Assignment" : dp.toString()));
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("%-24s %-15s %4s %10s\n", "Item", "Restaurant", "Qty", "Subtotal"));
        sb.append("--------------------------------------------------------\n");
        for (CartItem item : order.getItems()) {
            String itemName = truncate(item.getFoodItem().getName(), 23);
            String restName = truncate(item.getRestaurant().getName(), 14);
            sb.append(String.format("%-24s %-15s %4d ₹%8.2f\n",
                    itemName, restName, item.getQuantity(), item.getSubtotal()));
        }
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("TOTAL AMOUNT   : ₹%.2f\n", order.getTotalAmount()));
        sb.append(String.format("PAYMENT METHOD : %s\n", order.getPayment().getMethodName()));
        sb.append(String.format("PAYMENT RESULT : %s\n", order.getPaymentReceipt()));
        sb.append("========================================================\n");

        orderReceiptArea.setText(sb.toString());
        orderReceiptArea.setCaretPosition(0);

        updateTimelineVisuals(order.getStatus());
    }

    private void updateTimelineVisuals(OrderStatus currentStatus) {
        OrderStatus[] flowStatuses = {
                OrderStatus.PLACED,
                OrderStatus.CONFIRMED,
                OrderStatus.PREPARING,
                OrderStatus.READY_FOR_PICKUP,
                OrderStatus.OUT_FOR_DELIVERY,
                OrderStatus.DELIVERED
        };

        int currentIdx = -1;
        if (currentStatus != null && currentStatus != OrderStatus.CANCELLED) {
            for (int i = 0; i < flowStatuses.length; i++) {
                if (flowStatuses[i] == currentStatus) {
                    currentIdx = i;
                    break;
                }
            }
        }

        for (int i = 0; i < timelineStepLabels.length; i++) {
            JLabel lbl = timelineStepLabels[i];
            if (currentStatus == null) {
                lbl.setBackground(BG_LIGHT);
                lbl.setForeground(TEXT_MUTED);
            } else if (currentStatus == OrderStatus.CANCELLED) {
                lbl.setBackground(new Color(248, 215, 218));
                lbl.setForeground(PRIMARY_DARK);
            } else if (i < currentIdx) {
                // Completed step
                lbl.setBackground(new Color(209, 231, 221));
                lbl.setForeground(new Color(15, 81, 50));
            } else if (i == currentIdx) {
                // Active step
                lbl.setBackground(ACCENT_GREEN);
                lbl.setForeground(Color.WHITE);
            } else {
                // Upcoming step
                lbl.setBackground(BG_LIGHT);
                lbl.setForeground(TEXT_MUTED);
            }
        }
    }

    private void handleAssignDeliveryPartner() {
        Order order = getSelectedOrder();
        if (order == null) {
            showError("Please select an order from the table first.");
            return;
        }
        DeliveryPartner selectedPartner = (DeliveryPartner) partnerComboBox.getSelectedItem();
        if (selectedPartner == null) {
            showError("Please select a delivery partner.");
            return;
        }

        try {
            order.assignDeliveryPartner(selectedPartner);
            refreshOrdersTable();
            updateSelectedOrderDetails();
            setStatusMessage("Assigned delivery partner " + selectedPartner.getName() + " to Order " + order.getOrderId() + ".");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleAdvanceOrderStatus() {
        Order order = getSelectedOrder();
        if (order == null) {
            showError("Please place or select an order first.");
            return;
        }

        try {
            DeliveryPartner preferredPartner = (DeliveryPartner) partnerComboBox.getSelectedItem();
            OrderStatus newStatus = service.advanceOrderStatus(order, preferredPartner);
            refreshOrdersTable();
            updateSelectedOrderDetails();
            setStatusMessage(String.format("Order %s status updated to: %s",
                    order.getOrderId(), newStatus.getDisplayName()));

            if (newStatus == OrderStatus.DELIVERED) {
                JOptionPane.showMessageDialog(
                        this,
                        "Order " + order.getOrderId() + " has been marked as DELIVERED!\n"
                                + "Delivered by: " + order.getDeliveryPartner().getName(),
                        "Order Delivered",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleCancelOrder() {
        Order order = getSelectedOrder();
        if (order == null) {
            showError("Please select an order to cancel.");
            return;
        }

        if (!order.getStatus().canCancel()) {
            showError("Cannot cancel Order " + order.getOrderId() + " because its current status is "
                    + order.getStatus().getDisplayName() + ".");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel Order " + order.getOrderId() + "?\n"
                        + "(Current Status: " + order.getStatus().getDisplayName() + ")",
                "Confirm Order Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                order.cancelOrder();
                refreshOrdersTable();
                updateSelectedOrderDetails();
                setStatusMessage("Order " + order.getOrderId() + " has been CANCELLED.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void showOopSummaryDialog() {
        String summary =
                "MANDATORY OOP CONCEPTS IMPLEMENTED IN THIS PROJECT:\n\n" +
                "1. Classes & Objects:\n" +
                "   • Restaurant, FoodItem, Cart, CartItem, Order, Customer, DeliveryPartner, Admin\n\n" +
                "2. Encapsulation:\n" +
                "   • All model fields are private.\n" +
                "   • Order status transitions (PLACED → CONFIRMED → PREPARING → READY FOR PICKUP →\n" +
                "     OUT FOR DELIVERY → DELIVERED) and cancellation validation are strictly encapsulated\n" +
                "     inside Order.advanceToNextStatus() and Order.cancelOrder().\n\n" +
                "3. Inheritance:\n" +
                "   • Abstract parent class User (userId, name, phone).\n" +
                "   • Subclasses: Customer, DeliveryPartner, and Admin.\n\n" +
                "4. Polymorphism:\n" +
                "   • Abstract Payment class with abstract processPayment() method.\n" +
                "   • Overridden in UPIPayment, CardPayment, and CashOnDelivery.\n" +
                "   • Order constructor invokes payment.processPayment() via a base Payment reference.\n\n" +
                "5. Abstraction:\n" +
                "   • Abstract classes Payment and User hide implementation details and expose clean contracts.";

        JOptionPane.showMessageDialog(
                this,
                summary,
                "OOP Architecture & Viva Reference",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JPanel createTitledCard(String title) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(BORDER_COLOR, 1),
                        " " + title + " ",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        SECTION_TITLE_FONT,
                        TEXT_DARK
                ),
                new EmptyBorder(8, 10, 10, 10)
        ));
        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(255, 230, 233));
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(BOLD_BODY_FONT);
        header.setBackground(new Color(233, 236, 239));
        header.setForeground(TEXT_DARK);
        header.setPreferredSize(new Dimension(0, 34));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(BOLD_BODY_FONT);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        return btn;
    }

    private void setStatusMessage(String message) {
        statusBarLabel.setText(message);
    }

    private void showError(String message) {
        setStatusMessage("Action blocked: " + message);
        JOptionPane.showMessageDialog(this, message, "Validation Notice", JOptionPane.WARNING_MESSAGE);
    }

    private String truncate(String text, int maxLen) {
        if (text == null || text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen - 2) + "..";
    }
}
