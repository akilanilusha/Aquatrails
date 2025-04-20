package GUI.components;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private int currentRow;
    private String type;

    // Updated constructor to receive JTable instance
    public ButtonEditor(JCheckBox checkBox, String type, JTable table) {
        super(checkBox);
        this.table = table; // Store table reference
        this.type = type;
        button = new JButton();
        button.setOpaque(true);

        // Set color based on button type (Update or Delete)
        if ("Update".equals(type)) {
            button.setBackground(new java.awt.Color(0, 153, 255));
            button.setForeground(java.awt.Color.WHITE);
        } else if ("Delete".equals(type)) {
            button.setBackground(new java.awt.Color(255, 51, 51));
            button.setForeground(java.awt.Color.WHITE);
        }

        // Add action listener for button click
        button.addActionListener((ActionEvent e) -> {
            fireEditingStopped();  // Stop editing to trigger further actions

            if ("Update".equals(type)) {
                // Show dialog when Update button is clicked
                openUpdateDialog(currentRow);
            } else if ("Delete".equals(type)) {
                // Confirm and delete row when Delete button is clicked
                int confirm = JOptionPane.showConfirmDialog(button, "Delete this row?");
                if (confirm == JOptionPane.YES_OPTION) {
                    ((DefaultTableModel) table.getModel()).removeRow(currentRow);
                    JOptionPane.showMessageDialog(button, "Deleted row: " + currentRow);
                }
            }
        });
    }

    // Get table cell editor component (button)
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.label = (value == null) ? "" : value.toString();
        button.setText(label);
        this.currentRow = row; // Store current row number
        isPushed = true;
        return button; // Return the button component
    }

    // Return the value of the editor (button label)
    @Override
    public Object getCellEditorValue() {
        isPushed = false;  // Reset pushed state
        return label;
    }

    // Stop cell editing when necessary
    @Override
    public boolean stopCellEditing() {
        isPushed = false;  // Reset pushed state
        return super.stopCellEditing();
    }

    // Method to open the Update dialog
    private void openUpdateDialog(int row) {
        // Get data from the selected row
        String bookingId = table.getValueAt(row, 0).toString();  // Assuming column 0 has Booking ID
        String visitorName = table.getValueAt(row, 1).toString(); // Assuming column 1 has Visitor Name
        String packageName = table.getValueAt(row, 2).toString(); // Assuming column 2 has Package Name
        String price = table.getValueAt(row, 3).toString();       // Assuming column 3 has Price

        // Create a dialog to update the row details
        JDialog updateDialog = new JDialog();
        updateDialog.setTitle("Update Booking Details");
        updateDialog.setLayout(new BoxLayout(updateDialog.getContentPane(), BoxLayout.Y_AXIS));
        updateDialog.setSize(300, 200);
        updateDialog.setLocationRelativeTo(null);

        // Create fields to show current data and allow updates
        JTextField visitorNameField = new JTextField(visitorName);
        JTextField packageNameField = new JTextField(packageName);
        JTextField priceField = new JTextField(price);

        // Create a panel for the fields
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Booking ID:"));
        panel.add(new JLabel(bookingId));  // Display the Booking ID (non-editable)
        panel.add(new JLabel("Visitor Name:"));
        panel.add(visitorNameField);
        panel.add(new JLabel("Package Name:"));
        panel.add(packageNameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        updateDialog.add(panel);

        // Create Save and Cancel buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        // Add button actions
        saveButton.addActionListener((ActionEvent e) -> {
            // Update the table with the new values
            table.setValueAt(visitorNameField.getText(), row, 1); // Update Visitor Name
            table.setValueAt(packageNameField.getText(), row, 2); // Update Package Name
            table.setValueAt(priceField.getText(), row, 3);       // Update Price
            updateDialog.dispose();  // Close the dialog
            JOptionPane.showMessageDialog(button, "Booking details updated.");
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            updateDialog.dispose();  // Close the dialog without saving changes
        });

        // Add buttons to the dialog
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        updateDialog.add(buttonPanel);

        // Display the dialog
        updateDialog.setVisible(true);
    }
}
