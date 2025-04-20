/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.components;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 *
 * @author akilanilusha
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer(String label) {
        setText(label);
        setOpaque(true);
        
        // Set color based on button label
        if ("Update".equals(label)) {
            setBackground(new java.awt.Color(0, 153, 255)); // Blue
            setForeground(java.awt.Color.WHITE);
        } else if ("Delete".equals(label)) {
            setBackground(new java.awt.Color(255, 51, 51)); // Red
            setForeground(java.awt.Color.WHITE);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}