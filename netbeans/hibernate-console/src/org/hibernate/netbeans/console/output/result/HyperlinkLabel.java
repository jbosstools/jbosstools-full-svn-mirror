package org.hibernate.netbeans.console.output.result;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;


public class HyperlinkLabel extends JLabel {
    
    private Action action;

    public HyperlinkLabel() {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public HyperlinkLabel(Action a) {
        this();
        this.action = a;
        setText((String) action.getValue(Action.NAME));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                performAction();
            }
        });
    }
    
    public void performAction() {
        if (action != null) {
            action.actionPerformed(new ActionEvent(HyperlinkLabel.this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    public Action getAction() {
        return action;
    }
    
    public void setText(String text) {
        super.setText("<html><nobr><a href=\"\" style=\"color: rgb(0, 0, 255)\">" +  text + "</a></nobr></html>"); // NOI18N
    }
    
}
