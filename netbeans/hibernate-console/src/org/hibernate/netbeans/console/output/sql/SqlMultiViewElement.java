package org.hibernate.netbeans.console.output.sql;

import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import org.hibernate.netbeans.console.BshCode;
import org.hibernate.netbeans.console.HqlQuery;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;

/**
 * @author leon
 */
public class SqlMultiViewElement extends OutputMultiViewElement {

    private JScrollPane scrollPane;
    
    private JTextArea textArea;
    
    private JToolBar toolBar;
    
    public SqlMultiViewElement(SessionFactoryDescriptor descr) {
        super(descr);
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
    }

    public JComponent getVisualRepresentation() {
        return scrollPane;
    }

    public JComponent getToolbarRepresentation() {
        if (toolBar == null) {
            toolBar = new JToolBar();
        }
        return toolBar;
    }

    public void executeQuery(HqlQuery q) {
        try {
            requestVisible(false, false);
            textArea.setText(getDescriptor().toSql(q.getHql()));
            requestVisible(true, false);
        } catch (Exception ex) {
            SessionFactoryOutput.showError(getDescriptor(), ex);
        }
    }

    public void executeBshCode(BshCode cc) {
        requestVisible(false, false);
        textArea.setText("There is no SQL for BeanShell code"); // TODO - i18n
        requestVisible(true, false);
    }
    

    public void requestFocus() {
        textArea.requestFocusInWindow();
    }

}
