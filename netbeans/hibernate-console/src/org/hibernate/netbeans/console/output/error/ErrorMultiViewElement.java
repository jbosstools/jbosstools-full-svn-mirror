/*
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

package org.hibernate.netbeans.console.output.error;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.swing.Action;
import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.hibernate.netbeans.console.BshCode;
import org.hibernate.netbeans.console.HqlQuery;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.ErrorManager;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;

/**
 * @author leon
 */
public class ErrorMultiViewElement extends OutputMultiViewElement {
    
    private transient JScrollPane errorScrollPane;
    
    private transient JTextArea errorField;
    
    private transient JToolBar toolBar;
    
    private transient MultiViewElementCallback callback;
    
    private final static Rectangle UPPER_LEFT_RECT = new Rectangle(0, 0, 10, 10);
    
    public ErrorMultiViewElement(SessionFactoryDescriptor descr) {
        super(descr);
        errorField = new JTextArea();
        errorField.setEditable(false);
        errorField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        errorField.setForeground(Color.RED);
        errorField.setAutoscrolls(false);
        errorScrollPane = new JScrollPane(errorField);
        errorScrollPane.setBorder(null);
    }

    public JComponent getVisualRepresentation() {
        return errorScrollPane;
    }

    public JComponent getToolbarRepresentation() {
        if (toolBar == null) {
            toolBar = new JToolBar();
        }
        return toolBar;
    }

    public void showError(Throwable t) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            PrintWriter pw = new PrintWriter(new PrintWriter(new OutputStreamWriter(bos, "UTF-8")));
            t.printStackTrace(pw);
            pw.close();
        } catch (UnsupportedEncodingException ex) {
            ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
        }
        try {
            errorField.setText(new String(bos.toByteArray(), "UTF-8"));
            errorField.setCaretPosition(0);
            errorField.scrollRectToVisible(UPPER_LEFT_RECT);
        } catch (UnsupportedEncodingException ex) {
            ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
        }
        // XXX - This is a hack. Strangely, the editor doesn't have the focus after
        // displaying an error, although the focus manager says that it's the focus owner
        Component c = FocusManager.getCurrentManager().getFocusOwner();
        if (c != null) {
            c.requestFocusInWindow();
        }
    }

    public void clearError() {
        errorField.setText("");
    }

    public void executeQuery(HqlQuery q) {
    }

    public void executeBshCode(BshCode cc) {
    }
    
    public void requestFocus() {
        errorField.requestFocusInWindow();
    }
    
}
