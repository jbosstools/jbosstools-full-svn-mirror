/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


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
