/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;

/**
 * @author Dart dpeng@redhat.com
 *
 */
public class SmooksGraphicalEditorPaletteRootCreator {
	public PaletteRoot createPaletteRoot(){
		PaletteRoot root=  new PaletteRoot();
		PaletteDrawer drawer = new PaletteDrawer("Tools");
		drawer.add(new SelectionToolEntry());
		drawer.add(new MarqueeToolEntry());
		CreationFactory factory = new CreationFactory() {
			
			public Object getObjectType() {
				return TreeNodeConnection.class;
			}
			
			public Object getNewObject() {
				return null;
			}
		};
		drawer.add(new ConnectionCreationToolEntry("Link", "Link", factory, null, null));
		root.add(drawer);
		return root;
	}
}
