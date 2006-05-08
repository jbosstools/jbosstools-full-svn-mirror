package org.hibernate.netbeans.console.output.result;

import javax.swing.table.AbstractTableModel;

/**
 * @author leon
 */
public class PropertyTableModel extends AbstractTableModel {

    private ResultItem resultItem;
    
    public PropertyTableModel(ResultItem ri) {
        this.resultItem = ri;
    }

    public int getRowCount() {
        return resultItem != null ? resultItem.getPropertyCount() : 0;
    }

    public int getColumnCount() {
        return 2;
    }

    public ResultItem getResultItem() {
        return resultItem;
    }

    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return resultItem.getPropertyName(row);
        } else {
            return resultItem.getPropertyValue(row);
        }
    }

    public String getColumnName(int i) {
        if (i == 0) {
            return "Property"; // TODO
        } else {
            return "Value"; // TODO
        }
    }
    
}
