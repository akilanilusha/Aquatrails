/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
/**
 *
 * @author akilanilusha
 */
public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, String type) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);

        // Set color based on type
        if ("Update".equals(type)) {
            button.setBackground(new java.awt.Color(0, 153, 255));
            button.setForeground(java.awt.Color.WHITE);
        } else if ("Delete".equals(type)) {
            button.setBackground(new java.awt.Color(255, 51, 51));
            button.setForeground(java.awt.Color.WHITE);
        }

        // Handle click
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();

                int row = table.getSelectedRow();
                if ("Update".equals(type)) {
                    // Trigger update logic (e.g., open dialog)
                    JOptionPane.showMessageDialog(button, "Update row: " + row);
                } else if ("Delete".equals(type)) {
                    // Trigger delete logic
                    int confirm = JOptionPane.showConfirmDialog(button, "Delete this row?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        ((DefaultTableModel) table.getModel()).removeRow(row);
                        // Add your DB delete query here too
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        this.label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}