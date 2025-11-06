/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Owner
 */
public class WarnaTableHasilLab extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // If row is selected, use default selection colors
        if(isSelected){
            component.setBackground(table.getSelectionBackground());
            component.setForeground(table.getSelectionForeground());
        }else{
            // Apply color to entire row based on column 6 (Hasil) value
            if(table.getValueAt(row,6).toString().equals("h")){
                component.setBackground(new Color(255,255,255));
                component.setForeground(new Color(61,130,237));
            }else if(table.getValueAt(row,6).toString().equals("l")){
                component.setBackground(new Color(255,255,255));
                component.setForeground(new Color(244,67,54));
            }else{
                component.setBackground(new Color(255,255,255));
                component.setForeground(new Color(50,50,50));
            }
        }
        return component;
    }

}
