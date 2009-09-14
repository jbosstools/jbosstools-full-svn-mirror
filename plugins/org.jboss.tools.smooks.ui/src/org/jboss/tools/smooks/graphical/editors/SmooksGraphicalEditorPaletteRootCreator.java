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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.jboss.tools.smooks.configuration.editors.actions.Calc11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Database11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Datasources11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.FragmentRouting11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.JavaBean11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.PersistenceActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Reader11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Scripting11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Templating11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Validation10ActionGrouper;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart dpeng@redhat.com
 * 
 */
public class SmooksGraphicalEditorPaletteRootCreator {

	protected AdapterFactoryEditingDomain editingDomain;

	protected SmooksResourceListType resourceList;

	public SmooksGraphicalEditorPaletteRootCreator(AdapterFactoryEditingDomain editingDomain,
			SmooksResourceListType resourceList) {
		this.editingDomain = editingDomain;
		this.resourceList = resourceList;
	}

	public PaletteRoot createPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		PaletteDrawer drawer = new PaletteDrawer("Noraml Tools");
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
		
		createPaletteDrawer(root);
		
		return root;
	}

	private void createPaletteDrawer(PaletteRoot root) {
		if(resourceList == null) return;
		
		IEditingDomainItemProvider provider = (IEditingDomainItemProvider) editingDomain.getAdapterFactory().adapt(
				this.resourceList, IEditingDomainItemProvider.class);
		if (provider != null) {
			Collection<?> newChildrenDescripter = provider.getNewChildDescriptors(this.resourceList, editingDomain,
					null);
			List<ISmooksActionGrouper> grouperList = getSmooksActionGrouper();
			for (Iterator<?> iterator = grouperList.iterator(); iterator.hasNext();) {
				ISmooksActionGrouper iSmooksActionGrouper = (ISmooksActionGrouper) iterator.next();
				PaletteDrawer drawer = new PaletteDrawer(iSmooksActionGrouper.getGroupName());
				fillDrawer(drawer, newChildrenDescripter, iSmooksActionGrouper);
				root.add(drawer);
			}
		}
	}

	private void fillDrawer(PaletteDrawer drawer, Collection<?> allchildren, ISmooksActionGrouper grouper) {
		for (Iterator<?> iterator = allchildren.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();

			if (grouper.belongsToGroup(object)) {
				if (object instanceof CommandParameter) {
					Object v = ((CommandParameter) object).getValue();
					v = AdapterFactoryEditingDomain.unwrap(v);
					EClass clazz = ((EObject) v).eClass();
					CreationToolEntry toolEntry = new CreationToolEntry(clazz.getName(), "", null, null, null);
					drawer.add(toolEntry);
				}
			}
		}
	}

	private List<ISmooksActionGrouper> getSmooksActionGrouper() {
		List<ISmooksActionGrouper> grouperList = new ArrayList<ISmooksActionGrouper>();

		grouperList.add(new JavaBean11ActionGrouper());
		grouperList.add(new Reader11ActionGrouper());
		grouperList.add(new Calc11ActionGrouper());
		grouperList.add(new Database11ActionGrouper());
		grouperList.add(new Datasources11ActionGrouper());
		grouperList.add(new FragmentRouting11ActionGrouper());
		grouperList.add(new Scripting11ActionGrouper());
		grouperList.add(new Templating11ActionGrouper());
		grouperList.add(new PersistenceActionGrouper());
		grouperList.add(new Validation10ActionGrouper());
		return grouperList;
	}

}
