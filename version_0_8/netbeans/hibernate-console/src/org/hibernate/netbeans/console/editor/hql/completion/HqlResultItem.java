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


package org.hibernate.netbeans.console.editor.hql.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.hibernate.netbeans.console.Icons;
import org.hibernate.netbeans.console.editor.hql.HqlDocument;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Utilities;


class HqlResultItem implements CompletionItem {

    public static final ImageIcon CLASS_ICON =
            new ImageIcon(Utilities.loadImage(Icons.CLASS_ICON));

    public static final ImageIcon PROPERTY_ICON =
            new ImageIcon(Utilities.loadImage(Icons.PROPERTY_ICON));
    
    public static final int PRIORITY_SIMPLE_NAME = 0;
    
    public static final int PRIORITY_QUALIFIED_NAME = 100;
    
    public static final int PRIORITY_PROPERTY = 200;
    
    public static final int PRIORITY_ALIAS = 300;
    
    public static final int PRIORITY_OTHER = 400;

    private String text;

    private String displayText;
    
    private ImageIcon icon;

    private String type;

    private int prefixLength;
    
    private int sortPriority;

    public HqlResultItem(ImageIcon icon, String text, String displayText, String type, int priority) {
        this.text = text;
        this.displayText = displayText;
        this.prefixLength = 0;
        this.icon = icon;
        this.type = type;
        this.sortPriority = priority;
    }

    public void defaultAction(JTextComponent component) {
        HqlDocument bd = (HqlDocument) component.getDocument();
        try {
            bd.atomicLock();
            int offset = component.getCaretPosition();
            try {
                bd.replace(offset - prefixLength, prefixLength, text, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }  finally{
            bd.atomicUnlock();
        }
    }

    public void processKeyEvent(KeyEvent evt) {
    }

    public int getPreferredWidth(Graphics g, Font defaultFont) {
        return CompletionUtilities.getPreferredWidth(displayText, type, g, defaultFont) + 204;
    }

    public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(icon, displayText, type, g, defaultFont, defaultColor, width, height, selected);
    }

    public CompletionTask createDocumentationTask() {
        return null;
    }

    public CompletionTask createToolTipTask() {
        return null;
    }

    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    public int getSortPriority() {
        return sortPriority;
    }

    public CharSequence getSortText() {
        return displayText;
    }

    public CharSequence getInsertPrefix() {
        return "";
    }

    public String getText() {
        return text;
    }

    public boolean acceptsPrefix(String prefix) {
        return prefix == null || prefix.length() == 0 || text.startsWith(prefix);
    }

    public void setPrefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
    }

    public int getPrefixLength() {
        return prefixLength;
    }

}
