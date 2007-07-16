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


package org.hibernate.netbeans.console.editor.bsh;

import java.awt.Image;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.EditorMultiViewDescription;
import org.hibernate.netbeans.console.editor.EditorMultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class BshMultiViewDescription extends  EditorMultiViewDescription {
    
    private BshMultiViewElement element;
    
    public BshMultiViewDescription(SessionFactoryNode node) {
        super(node);
    }

    public String getDisplayName() {
        return "BeanShell";
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return "Bsh_" + node.getDescriptor().getName();
    }

    public MultiViewElement createElement() {
        element = new BshMultiViewElement(node);
        return element;
    }
    
    public EditorMultiViewElement getEditorElement() {
        return element;
    }
    
}
