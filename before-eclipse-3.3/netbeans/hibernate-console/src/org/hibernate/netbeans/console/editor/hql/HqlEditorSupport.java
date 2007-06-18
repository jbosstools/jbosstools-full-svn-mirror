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


package org.hibernate.netbeans.console.editor.hql;

import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.NeverGetsModifiedEditorSupport;
import org.netbeans.modules.editor.plain.PlainKit;
import org.openide.loaders.DataObject;

/**
 * @author leon
 */
public class HqlEditorSupport extends NeverGetsModifiedEditorSupport {

    private SessionFactoryNode node;

    public HqlEditorSupport(SessionFactoryNode node) {
        this(createDataObject(node, "_editor.hql"));
        this.node = node;
    }

    public HqlEditorSupport(DataObject obj) {
        super(obj);
    }

    protected StyledDocument createStyledDocument(EditorKit kit) {
        return new HqlDocument(node, kit.getClass());
    }

    protected EditorKit createEditorKit() {
        return new HqlEditorKit();
    }

}
