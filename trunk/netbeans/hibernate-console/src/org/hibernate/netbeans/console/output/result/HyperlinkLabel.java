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
