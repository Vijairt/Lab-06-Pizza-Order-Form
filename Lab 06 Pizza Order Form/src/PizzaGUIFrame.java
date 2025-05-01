import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppings;
    private JEditorPane orderPane;

    private final String[] toppingNames = {
            "Pepperoni", "Mushrooms", "Dragon Fruit", "Gummy Bears", "Spicy Worms", "Cheese Lava"
    };

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panels
        JPanel crustPanel = createCrustPanel();
        JPanel sizePanel = createSizePanel();
        JPanel toppingPanel = createToppingPanel();
        JPanel outputPanel = createOutputPanel();
        JPanel buttonPanel = createButtonPanel();

        // Layout
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingPanel);

        add(topPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createCrustPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Crust Type"));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);

        panel.add(thinCrust);
        panel.add(regularCrust);
        panel.add(deepDishCrust);

        return panel;
    }

    private JPanel createSizePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Pizza Size"));

        String[] sizes = { "Small - $8", "Medium - $12", "Large - $16", "Super - $20" };
        sizeComboBox = new JComboBox<>(sizes);

        panel.add(sizeComboBox);
        return panel;
    }

    private JPanel createToppingPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Toppings"));
        panel.setLayout(new GridLayout(3, 2));

        toppings = new JCheckBox[toppingNames.length];
        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            panel.add(toppings[i]);
        }

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Your Order"));
        panel.setLayout(new BorderLayout());

        orderPane = new JEditorPane();
        orderPane.setContentType("text/html");
        orderPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderPane);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        JButton orderButton = new JButton("Order");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");

        orderButton.addActionListener(e -> handleOrder());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        panel.add(orderButton);
        panel.add(clearButton);
        panel.add(quitButton);

        return panel;
    }

    private void handleOrder() {
        String crust = null;
        if (thinCrust.isSelected()) crust = "Thin";
        else if (regularCrust.isSelected()) crust = "Regular";
        else if (deepDishCrust.isSelected()) crust = "Deep-dish";

        if (crust == null) {
            JOptionPane.showMessageDialog(this, "Please select a crust.");
            return;
        }

        int sizeIndex = sizeComboBox.getSelectedIndex();
        String size = sizeComboBox.getSelectedItem().toString();
        double basePrice = switch (sizeIndex) {
            case 0 -> 8.0;
            case 1 -> 12.0;
            case 2 -> 16.0;
            case 3 -> 20.0;
            default -> 0.0;
        };

        StringBuilder receipt = new StringBuilder();
        receipt.append("<html><pre>");
        receipt.append("=========================================<br>");
        receipt.append(String.format("Type of Crust & Size\t\t$%.2f<br>", basePrice));
        receipt.append(String.format("%s - %s<br><br>", crust, size.split(" - ")[0]));

        int toppingCount = 0;
        for (JCheckBox topping : toppings) {
            if (topping.isSelected()) {
                receipt.append(String.format("Ingredient: %-20s $1.00<br>", topping.getText()));
                toppingCount++;
            }
        }

        if (toppingCount == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one topping.");
            return;
        }

        double subTotal = basePrice + toppingCount;
        double tax = subTotal * 0.07;
        double total = subTotal + tax;

        receipt.append("<br>");
        receipt.append(String.format("<b>Sub-total:</b>\t\t\t$%.2f<br>", subTotal));
        receipt.append(String.format("<b>Tax:</b>\t\t\t\t$%.2f<br>", tax));
        receipt.append("---------------------------------------------------------------------<br>");
        receipt.append(String.format("<b>Total:</b>\t\t\t\t$%.2f<br>", total));
        receipt.append("=========================================<br>");
        receipt.append("</pre></html>");

        orderPane.setText(receipt.toString());
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDishCrust.setSelected(false);
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        orderPane.setText("");
    }
}
