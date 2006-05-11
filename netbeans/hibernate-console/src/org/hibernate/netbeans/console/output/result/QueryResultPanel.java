package org.hibernate.netbeans.console.output.result;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.FocusManager;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.hibernate.netbeans.console.util.DelegatingListModel;
import org.hibernate.netbeans.console.util.HibernateExecutor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle;
    
/**
 * @author  leon
 */
public class QueryResultPanel extends javax.swing.JPanel {

    private final static String SELECT_NEXT_ELEMENT = 
            "select-next-element";
    
    private final static String SELECT_PREVIOUS_ELEMENT = 
            "select-previous-element";
    
    private final static String SELECT_NEXT_PROPERTY = 
            "select-next-property";
    
    private final static String SELECT_PREVIOUS_PROPERTY = 
            "select-previous-property";
    
    private final static String EDIT_ROW = 
            "edit-row";
    
    private final static KeyStroke EDIT_ROW_KEY = 
            KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
    
    private final static KeyStroke SELECT_NEXT_ELEMENT_KEY = 
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK);
    
    private final static KeyStroke SELECT_PREVIOUS_ELEMENT_KEY = 
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK);
            
    private final static KeyStroke SELECT_NEXT_PROPERTY_KEY = 
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
    
    private final static KeyStroke SELECT_PREVIOUS_PROPERTY_KEY = 
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
    
    private final static PropertyTableModel EMPTY_TABLE_MODEL = new PropertyTableModel(null);

    private final static DefaultListModel EMPTY_LIST_MODEL = new DefaultListModel();
    
    private List<ResultItem> resultItems;

    private Session session;
    
    private ObjectNavigationHandler navigationHandler;
    
    public QueryResultPanel(ObjectNavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
        initComponents();
        addTableListeners();
        registerKeyboardActions();
        elementsList.setCellRenderer(new ResultItemRenderer());
        detailsTable.setModel(EMPTY_TABLE_MODEL);
        detailsTable.getTableHeader().setReorderingAllowed(false);
        detailsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        detailsTable.setDefaultRenderer(Object.class, new PropertyTableRenderer());
        classNameLabel.setText("");
    }
    
    private void registerKeyboardActions() {
        InputMap panelIm = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap panelAm = getActionMap();
        InputMap listIm = elementsList.getInputMap();
        ActionMap listAm = elementsList.getActionMap();
        InputMap tableIm = detailsTable.getInputMap();
        ActionMap tableAm = detailsTable.getActionMap();
        
        // Select previous element
        Action selectPrevElemAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                selectPreviousElement();
            }
        };
        panelIm.put(SELECT_PREVIOUS_ELEMENT_KEY, SELECT_PREVIOUS_ELEMENT);
        panelAm.put(SELECT_PREVIOUS_ELEMENT, selectPrevElemAction);
        tableIm.put(SELECT_PREVIOUS_ELEMENT_KEY, SELECT_PREVIOUS_ELEMENT);
        tableAm.put(SELECT_PREVIOUS_ELEMENT, selectPrevElemAction);
        
        // Select next element
        Action selectNextElemAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                selectNextElement();
            }
        };
        panelIm.put(SELECT_NEXT_ELEMENT_KEY, SELECT_NEXT_ELEMENT);
        panelAm.put(SELECT_NEXT_ELEMENT, selectNextElemAction);
        tableIm.put(SELECT_NEXT_ELEMENT_KEY, SELECT_NEXT_ELEMENT);
        tableAm.put(SELECT_NEXT_ELEMENT, selectNextElemAction);
        
        // Select previous property
        Action selectPrevPropertyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                selectPreviousProperty();
            }
        };
        panelIm.put(SELECT_PREVIOUS_PROPERTY_KEY, SELECT_PREVIOUS_PROPERTY);
        panelAm.put(SELECT_PREVIOUS_PROPERTY, selectPrevPropertyAction);
        listIm.put(SELECT_PREVIOUS_PROPERTY_KEY, SELECT_PREVIOUS_PROPERTY);
        listAm.put(SELECT_PREVIOUS_PROPERTY, selectPrevPropertyAction);
        
        // Select next property
        Action selectNextPropertyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                selectNextProperty();
            }
        };
        panelIm.put(SELECT_NEXT_PROPERTY_KEY, SELECT_NEXT_PROPERTY);
        panelAm.put(SELECT_NEXT_PROPERTY, selectNextPropertyAction);
        listIm.put(SELECT_NEXT_PROPERTY_KEY, SELECT_NEXT_PROPERTY);
        listAm.put(SELECT_NEXT_PROPERTY, selectNextPropertyAction);
        
        // Edit the table
        Action editRowAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                ResultItem ri = (ResultItem) elementsList.getSelectedValue();
                if (ri == null) {
                    return;
                }
                int row = detailsTable.getSelectedRow();
                if (row == -1) {
                    return;
                }
                if (ri.hasMoreDetails(row)) {
                    stepIntoRow(row);
                }
            }
        };
        panelIm.put(EDIT_ROW_KEY, EDIT_ROW);
        panelAm.put(EDIT_ROW, editRowAction);
        tableIm.put(EDIT_ROW_KEY, EDIT_ROW);
        tableAm.put(EDIT_ROW, editRowAction);
        
    }
    
    private void selectNextElement() {
        int idx = elementsList.getSelectedIndex() + 1;
        if (idx < elementsList.getModel().getSize()) {
            elementsList.setSelectedIndex(idx);
            elementsList.ensureIndexIsVisible(idx);
        }
    }
    
    private void selectPreviousElement() {
        int idx = elementsList.getSelectedIndex() - 1;
        if (idx >= 0 && elementsList.getModel().getSize() > 0) {
            elementsList.setSelectedIndex(idx);
            elementsList.ensureIndexIsVisible(idx);
        }
    }
    
    private void selectPreviousProperty() {
        int idx = detailsTable.getSelectedRow() - 1;
        if (idx >= 0 && detailsTable.getRowCount() > 0) {
            detailsTable.setRowSelectionInterval(idx, idx);
            detailsTable.scrollRectToVisible(detailsTable.getCellRect(idx, 0, true));
        }
    }
    
    private void selectNextProperty() {
        int idx = detailsTable.getSelectedRow() + 1;
        int rowCount = detailsTable.getRowCount();
        if (idx < rowCount && rowCount > 0) {
            detailsTable.setRowSelectionInterval(idx, idx);
            detailsTable.scrollRectToVisible(detailsTable.getCellRect(idx, 0, true));
        }
    }
    
    public void setResults(Session s, String hql, Collection results, long time) {
        setResults(s, hql, results, null, time);
    }

    private void setLabels(int itemCount, long time) {
        String queryResult;
        String label;
        if (itemCount >= 0) {
            label = NbBundle.getMessage(QueryResultPanel.class, "LBL_ExecutionTime", String.valueOf(time), String.valueOf(itemCount));
        } else {
            label = " ";
        }    
        itemCountLabel.setText(label);
    }
    
    private void setResults(final Session s, final String hql, final Collection results, final ShowOldResultAction showOldResultAction, final long time) {
        if (results == null || s == null) {
            setLabels(-1, -1);
            elementsList.setModel(EMPTY_LIST_MODEL);
            return;
        }
        QueryResultPanel.this.session = s;
        final ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(QueryResultPanel.class, "LBL_PreparingResults"));
        ph.start();
        HibernateExecutor.execute(new Runnable() {
            public void run() {
                try {
                    SessionFactory sf = session.getSessionFactory();
                    final int sz = results.size();
                    resultItems = new ArrayList<ResultItem>(sz);
                    int i = 0;
                    for (Iterator it = results.iterator(); it.hasNext();) {
                        if (i % 10 == 0) {
                            ph.progress(i + " items found");
                        }
                        i++;
                        resultItems.add(ResultItem.create(sf, hql, it.next()));
                    }
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            elementsList.setModel(new DelegatingListModel<ResultItem>(resultItems));
                            setLabels(sz, time);
                            elementsList.setSelectedIndex(0);
                            if (showOldResultAction != null) {
                                navigationHandler.addNavigationAction(showOldResultAction);
                            }
                        }
                    });
                } finally {
                    ph.finish();
                }
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        elementsScrollPane = new javax.swing.JScrollPane();
        elementsList = new javax.swing.JList();
        detailsScrollPane = new javax.swing.JScrollPane();
        detailsTable = new javax.swing.JTable();
        itemCountLabel = new javax.swing.JLabel();
        classNameLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 0, 0));
        elementsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        elementsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                elementsListValueChanged(evt);
            }
        });

        elementsScrollPane.setViewportView(elementsList);

        detailsScrollPane.setViewportView(detailsTable);

        itemCountLabel.setText("Query result");

        classNameLabel.setText("Details");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(itemCountLabel)
                    .add(elementsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(classNameLabel)
                        .addContainerGap())
                    .add(detailsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(itemCountLabel)
                    .add(classNameLabel))
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(detailsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .add(elementsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void elementsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_elementsListValueChanged
            if (!evt.getValueIsAdjusting()) {
            ResultItem ri = (ResultItem) elementsList.getSelectedValue();
            showResultDetails(ri);
        }
    }//GEN-LAST:event_elementsListValueChanged
    
    private void showResultDetails(ResultItem result) {
        if (result == null) {
            classNameLabel.setText("");
            detailsTable.setModel(EMPTY_TABLE_MODEL);
        } else {
            int oldRowCount = detailsTable.getRowCount();
            int oldSelection = detailsTable.getSelectedRow();
            Rectangle oldVisibleRect = detailsTable.getVisibleRect();
            classNameLabel.setText(result.getClassName());
            detailsTable.setModel(new PropertyTableModel(result));
            int newRowCount = detailsTable.getRowCount();
            if (newRowCount > 0) {
                if (oldSelection != -1) {
                    detailsTable.setRowSelectionInterval(oldSelection, oldSelection);
                } else {
                    detailsTable.setRowSelectionInterval(0, 0);
                }
                detailsTable.scrollRectToVisible(oldVisibleRect);
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel classNameLabel;
    private javax.swing.JScrollPane detailsScrollPane;
    private javax.swing.JTable detailsTable;
    private javax.swing.JList elementsList;
    private javax.swing.JScrollPane elementsScrollPane;
    private javax.swing.JLabel itemCountLabel;
    // End of variables declaration//GEN-END:variables
   
    private static class ResultItemRenderer extends DefaultListCellRenderer {
        
        public Component getListCellRendererComponent(JList list, Object object, int i, boolean isSelected, boolean hasFocus) {
            ResultItem it = (ResultItem) object;
            String val = it.getDisplayName();
            return super.getListCellRendererComponent(list, val, i, isSelected, hasFocus);
        }
        
    }

    public boolean requestFocusInWindow() {
        return elementsList.requestFocusInWindow();
    }

    private boolean isPointOverLink(Point p) {
        int row = detailsTable.rowAtPoint(p);
        int col = detailsTable.columnAtPoint(p);
        return isLink(row, col) && getLabelBounds(row).contains(p);
    }
    
    private boolean isLink(int row, int col) {
        if (row < 0 || row >= detailsTable.getRowCount() || col < 0 || col >= detailsTable.getColumnCount()) {
            return false;
        }
        if (col == 1) {
            return false;
        }
        if (col == 0 && row < detailsTable.getRowCount()) {
            ResultItem it = (ResultItem) elementsList.getSelectedValue();
            if (it.hasMoreDetails(row)) {
                return true;
            }
        }
        return false;
    }
    
    private Rectangle getLabelBounds(int row) {
        Rectangle rect = detailsTable.getCellRect(row, 0, true);
        rect.y--;
        rect.height++;
        String label = (String) detailsTable.getValueAt(row, 0);
        FontMetrics fm = detailsTable.getFontMetrics(detailsTable.getFont());
        int width = fm.stringWidth(label);
        rect.width = width;
        return rect;
    }
    
    private void addTableListeners() {
        detailsTable.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent evt) {
                if (isPointOverLink(evt.getPoint())) {
                    detailsTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    detailsTable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        detailsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (isPointOverLink(evt.getPoint())) {
                    int row = detailsTable.getSelectedRow();
                    stepIntoRow(row);
                }
            }
        });
    }

    private void stepIntoRow(final int row) {
        if (row == -1) {
            return;
        }
        final ResultItem ri = (ResultItem) elementsList.getSelectedValue();
        final ProgressHandle ph = ProgressHandleFactory.createHandle("Fetching " + ri.getPropertyName(row));
        ph.setInitialDelay(200);
        HibernateExecutor.execute(new Runnable() {
            public void run() {
                ph.start();
                Cursor cursor = getCursor();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                final Component focused = FocusManager.getCurrentManager().getFocusOwner();
                detailsTable.setEnabled(false);
                elementsList.setEnabled(false);
                try {
                    long start = System.currentTimeMillis();
                    final Object value = ri.getPropertyValue(row);
                    if (value instanceof Collection) {
                        // Call this method as a lazy collection might need initializing
                        ((Collection) value).size();
                    }
                    final long time = System.currentTimeMillis() - start;
                    String name = ri.getPropertyName(row);
                    final ShowOldResultAction action = new ShowOldResultAction(name);
                    try {
                        EventQueue.invokeAndWait(new Runnable() {
                            public void run() {
                                if (value instanceof Collection) {
                                    setResults(session, null, (Collection) value, action, time);
                                } else {
                                    setResults(session, null, Collections.singleton(value), action, time);
                                }
                                int selTableRow = detailsTable.getSelectedRow();
                                if (selTableRow != -1) {
                                    detailsTable.scrollRectToVisible(detailsTable.getCellRect(selTableRow, 0, true));
                                }
                            }
                        });
                    } catch (InterruptedException ex) {
                        // Ignore it
                    } catch (InvocationTargetException ex) {
                        // Ignore it
                    }
                } finally {
                    elementsList.setEnabled(true);
                    detailsTable.setEnabled(true);
                    focused.requestFocusInWindow();
                    setCursor(cursor);
                    ph.finish();
                }
            }
        });
    }
    
    private class PropertyTableRenderer extends DefaultTableCellRenderer {
        
        private HyperlinkLabel label = new HyperlinkLabel();

        public PropertyTableRenderer() {
            label.setOpaque(true);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c;
            if (column == 1) {
                // The value column
                value = ((PropertyTableModel) table.getModel()).getResultItem().getPropertyValueAsString(row);
                c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            } else if (isLink(row, column)) {
                String propName = (String) value;
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                } else {
                    label.setBackground(table.getBackground());
                }
                label.setText(propName);
                label.setFont(table.getFont());
                c = label;
            } else {
                c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
            return c;
        }
    }
    
    public void clear() {
        elementsList.setModel(EMPTY_LIST_MODEL);
        detailsTable.setModel(EMPTY_TABLE_MODEL);
        setLabels(-1, -1);
    }
    
    private class ShowOldResultAction extends AbstractAction {

        private ListModel listModel;

        private int listIndex;

        private int tableRow;
        
        public ShowOldResultAction(String name) {
            super(name);
            this.listModel = elementsList.getModel();
            this.listIndex = elementsList.getSelectedIndex();
            this.tableRow = detailsTable.getSelectedRow();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            elementsList.clearSelection();
            elementsList.setModel(listModel);
            elementsList.setSelectedIndex(listIndex);
            elementsList.ensureIndexIsVisible(listIndex);
            detailsTable.setRowSelectionInterval(tableRow, tableRow);
            detailsTable.scrollRectToVisible(detailsTable.getCellRect(tableRow, 0, true));
            setLabels(elementsList.getModel().getSize(), 0);
        }
        
    }
    
}
