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

    public final static ImageIcon CLASS_ICON =
            new ImageIcon(Utilities.loadImage(Icons.CLASS_ICON));

    public final static ImageIcon PROPERTY_ICON =
            new ImageIcon(Utilities.loadImage(Icons.PROPERTY_ICON));

    private String text;

    private int prefixLength;

    private ImageIcon icon;

    private String type;

    public HqlResultItem(ImageIcon icon, String text, int prefixLength, String type) {
        this.text = text;
        this.prefixLength = prefixLength;
        this.icon = icon;
        this.type = type;
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
        return CompletionUtilities.getPreferredWidth(text, type, g, defaultFont) + 204;
    }

    public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(icon, text, type, g, defaultFont, defaultColor, width, height, selected);
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
        return 100;
    }

    public CharSequence getSortText() {
        return text;
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
