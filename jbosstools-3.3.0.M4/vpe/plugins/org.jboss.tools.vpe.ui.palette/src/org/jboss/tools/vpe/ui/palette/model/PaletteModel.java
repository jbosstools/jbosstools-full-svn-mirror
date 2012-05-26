/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.ui.palette.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.common.model.ui.views.palette.editor.PaletteEditor;

public class PaletteModel {
	
	private static PaletteModel instance = null;
	private static Object monitor = new Object();

	private PaletteEditor editor = new PaletteEditor();
	private PaletteRoot paletteRoot = null;
	private PaletteContents paletteContents = new PaletteContents(null);

	private PaletteModel() {
	}

	public static final PaletteModel getInstance(PaletteContents contents) {
		if (instance != null) {
			return instance;
		} else {
			synchronized (monitor) {
				if (instance == null) {
					PaletteModel inst = new PaletteModel();
					inst.setPaletteContentsInternal(contents);
					inst.createModel();
					instance = inst;
				}
			}
			return instance;
		}
	}

	public XModel getXModel() {
		return ModelUtilities.getPreferenceModel();
	}

	private XModelObject getXPaletteRoot() {
		return getXModel().getRoot("Palette"); //$NON-NLS-1$
	}

	private XModelObject[] findXObjects(XModelObject root, String elementType){
		ArrayList v = new ArrayList();
		for (int i = 0; i < root.getChildren().length; i++) {
			if (root.getChildAt(i).getAttributeValue("element type").equals(elementType)) { //$NON-NLS-1$
				v.add(root.getChildAt(i));
			}
		}
		return (v.size() == 0) ? null : (XModelObject[])v.toArray(new XModelObject[0]);
	}
	
	private XModelObject[] getGroups(XModelObject root) {
		ArrayList v = new ArrayList();
		collectGroups(root, v);
		return (v.size() == 0) ? new XModelObject[0] : (XModelObject[])v.toArray(new XModelObject[0]);
	}
	
	private void collectGroups(XModelObject o, ArrayList list) {
		for (int i = 0; i < o.getChildren().length; i++) {
			XModelObject c = o.getChildAt(i);
			if("yes".equals(c.getAttributeValue("hidden"))) continue; //$NON-NLS-1$ //$NON-NLS-2$
			if(PaletteModelHelper.isSubGroup(c)) {
				list.add(c);
			} else if (PaletteModelHelper.isGroup(c)) {
				collectGroups(c, list);
			}
		}
	}
	
	private void createModel() {
		load(null);
	}
	
	public PaletteRoot getPaletteRoot() {
		return paletteRoot;
	}

	public void load(XModelObject lastAddedXCat) {
		XModelObject xpalette = getXPaletteRoot();
		if (paletteRoot == null) {
			paletteRoot = new PaletteRoot(xpalette);
		}
		if (xpalette == null) return;
		XModelObject[] xcats = getGroups(xpalette);
		if (xcats == null) return;
		int i = 0; 
		for (int l = 0; l < xcats.length; l++) {
			if ("yes".equals(xcats[l].getAttributeValue("hidden"))) continue; //$NON-NLS-1$ //$NON-NLS-2$
			int j = indexOf(paletteRoot, xcats[l], i);
			if (j == -1) {
				paletteRoot.add(i, createCategory(xcats[l], lastAddedXCat ==xcats[l]));
			} else {
				PaletteCategory cat = (PaletteCategory)getEntry(paletteRoot, j);
				if (i < j) {
					moveUp(paletteRoot, cat, j - i);
				}
				cat.setXModelObject(xcats[l]);
				cat.setVisible(isCategoryVisible(cat));
				loadCategory(xcats[l], cat);
			}
			i++;
		}
		cutOff(paletteRoot, i);
	}
	
	private PaletteCategory createCategory(XModelObject xcat, boolean open) {
		PaletteCategory cat = new PaletteCategory(xcat, open);
		cat.setVisible(isCategoryVisible(cat));
		loadCategory(xcat, cat);
		return cat;
	}

	private void loadCategory(XModelObject xcat, PaletteCategory cat) {
		XModelObject[] xitems = xcat.getChildren();
		int i = 0;
		for (int l = 0; l < xitems.length; l++) {
			if (xitems[l].getAttributeValue("element type").equals("macro")) { //$NON-NLS-1$ //$NON-NLS-2$
				PaletteItem item = (PaletteItem)getEntry(cat, i++);
				if (item == null) {
					cat.add(new PaletteItem(xitems[l]));
				} else {
					item.setXModelObject(xitems[l]);
				}
			}
		}
		cutOff(cat, i);
	}
	
	private int indexOf(PaletteContainer container, XModelObject xobject, int startIndex) {
		List v = container.getChildren();
		if (v != null) {
			int max = v.size();
			for (int i = startIndex; i < max; i++) {
				if (xobject ==((PaletteXModelObject)v.get(i)).getXModelObject()) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private PaletteEntry getEntry(PaletteContainer container, int index) {
		List v = container.getChildren();
		if (index <v.size()) {
			return (PaletteEntry)v.get(index);
		} else {
			return null;
		}
	}
	
	private void moveUp(PaletteContainer container, PaletteEntry entry, int height) {
		for (int i = 0; i < height; i++) {
			container.moveUp(entry);
		}
	}
	
	private void cutOff(PaletteContainer container, int size) {
		List v = container.getChildren();
		if (v != null) {
			int oldSize = v.size();
			if (oldSize > size) {
				for (int i = oldSize - 1; i >= size; i--) {
					container.remove((PaletteEntry)v.get(i));
				}
			}
		}
	}

	public void addModelTreeListener(XModelTreeListener listener) {
		getXModel().addModelTreeListener(listener);
	}

	public void removeModelTreeListener(XModelTreeListener listener) {
		getXModel().removeModelTreeListener(listener);
	}
	
	public void openEditor(Shell shell) {
		editor.setObject(shell);
		editor.execute();
	}

	public void runShowHideDialog() {
		XActionInvoker.invoke("HiddenTabs", paletteRoot.getXModelObject(), new java.util.Properties()); //$NON-NLS-1$
	}

	public void runImportTLDDialog() {
		XActionInvoker.invoke("ImportTLDToPaletteWizard", "CreateActions.ImportTLD", paletteRoot.getXModelObject(), new java.util.Properties()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private boolean setPaletteContentsInternal(PaletteContents contents) {
		if (!paletteContents.equalsContents(contents)) {
			if (contents != null) {
				paletteContents = contents;
			} else {
				paletteContents = new PaletteContents(null);
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean isCategoryVisible(PaletteCategory cat) {
		return true; ///paletteContents.contains(cat.getNatureTypes(), cat.getEditorTypes()); 
	}

	public void setPaletteContents(PaletteContents contents) {
		if (!setPaletteContentsInternal(contents)) {
			return;
		}
		List children = paletteRoot.getChildren();
		for (int i = 0; i < children.size(); i++) {
			PaletteCategory cat = (PaletteCategory)children.get(i);
			cat.setVisible(isCategoryVisible(cat));
		}
	}
}
