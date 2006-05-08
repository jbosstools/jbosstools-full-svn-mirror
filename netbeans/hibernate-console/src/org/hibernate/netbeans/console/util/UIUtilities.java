package org.hibernate.netbeans.console.util;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author leon
 */
public class UIUtilities {
    
    private final static Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);
    
    private UIUtilities() {
    }
    
    public static Component createToolBarSpacer() {
        return Box.createHorizontalStrut(10);
    }

    public static JButton createTooBarButton(Action a) {
        JButton button = new JButton(a);
        customizeToolBarButton(button);
        return button;
    }
    
    public static JButton createToolBarButton(String text, Icon i) {
        JButton button = new JButton(text, i);
        customizeToolBarButton(button);
        return button;
    }
    
    public static void customizeToolBarButton(JButton button) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addMouseListener(sharedMouseListener);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        button.setFocusPainted(false);
    }
    
    private static MouseListener sharedMouseListener = new MouseAdapter() {
        
        public void mouseEntered(MouseEvent mouseEvent) {
            JButton btn = (JButton) mouseEvent.getSource();
            if (btn.isEnabled()) {
                btn.setContentAreaFilled(true);
                btn.setBorderPainted(true);
            }
        }
        public void mouseExited(MouseEvent mouseEvent) {
            JButton btn = (JButton) mouseEvent.getSource();
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
        }
    };
    
    
}
