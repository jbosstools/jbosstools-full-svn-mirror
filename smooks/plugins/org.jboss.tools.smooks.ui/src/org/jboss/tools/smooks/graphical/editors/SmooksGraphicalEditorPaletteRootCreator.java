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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.tree.model.TriggerConnection;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.smooks.ConditionType;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart dpeng@redhat.com
 * 
 */
public class SmooksGraphicalEditorPaletteRootCreator {

	protected AdapterFactoryEditingDomain editingDomain;

	protected SmooksResourceListType resourceList;

	protected ISmooksModelProvider smooksModelProvider;

	public SmooksGraphicalEditorPaletteRootCreator(ISmooksModelProvider provider,
			AdapterFactoryEditingDomain editingDomain, SmooksResourceListType resourceList) {
		this.editingDomain = editingDomain;
		this.resourceList = resourceList;
		this.smooksModelProvider = provider;
	}

	protected CreationFactory createConnectionCreationFactory() {
		return new CreationFactory() {

			public Object getObjectType() {
				return TriggerConnection.class;
			}

			public Object getNewObject() {
				return null;
			}
		};
	}

	public PaletteRoot createPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		PaletteDrawer drawer = new PaletteDrawer("General");
		drawer.add(new SelectionToolEntry());
		drawer.add(new MarqueeToolEntry());
		CreationFactory factory = createConnectionCreationFactory();
		ImageRegistry imageRegistry = SmooksConfigurationActivator.getDefault().getImageRegistry();
		drawer.add(new ConnectionCreationToolEntry("Link", "Connect node", factory, imageRegistry
				.getDescriptor(GraphicsConstants.IMAGE_LINK16), imageRegistry
				.getDescriptor(GraphicsConstants.IMAGE_LINK24)));
		root.add(drawer);

		createPaletteDrawer(root);

		return root;
	}

	private void createPaletteDrawer(PaletteRoot root) {
		if (resourceList == null)
			return;

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
					v = EcoreUtil.copy((EObject) v);

					IEditingDomainItemProvider editDomainIemProvider = (IEditingDomainItemProvider) editingDomain
							.getAdapterFactory().adapt(v, IEditingDomainItemProvider.class);
					IItemLabelProvider itemLabelProvider = (IItemLabelProvider) editingDomain.getAdapterFactory()
							.adapt(v, IItemLabelProvider.class);

					if (SmooksUIUtils.isUnSupportElement(smooksModelProvider.getPlatformVersion(), (EObject) v)) {
						continue;
					}
					if (isIgnoreType(v)) {
						continue;
					}
					EClass clazz = ((EObject) v).eClass();

					Object newModel = ((CommandParameter) object).getValue();
					if (newModel instanceof FeatureMap.Entry) {
						newModel = FeatureMapUtil.createEntry(((FeatureMap.Entry) newModel).getEStructuralFeature(), v);
					}

					String entryName = clazz.getName();

					ImageDescriptor smallImage = null;
					if (itemLabelProvider != null) {
						entryName = itemLabelProvider.getText(v);
						Object imageObj = itemLabelProvider.getImage(v);
						smallImage = ExtendedImageRegistry.getInstance().getImageDescriptor(imageObj);
					}

					CombinedTemplateCreationEntry toolEntry = new CombinedTemplateCreationEntry(entryName, entryName,
							new SmooksModelCreationFactory(newModel, ((CommandParameter) object).getFeature()),
							smallImage, smallImage);
					drawer.add(toolEntry);

					if (needtToCreateChildrenEntry(v)) {

						fillDrawer(drawer, editDomainIemProvider.getNewChildDescriptors(v, editingDomain, null));
					}
				}
			}
		}
	}

	private boolean isIgnoreType(Object element) {
		if (element instanceof ConditionType) {
			return true;
		}
		return false;
	}

	private void fillDrawer(PaletteDrawer drawer, Collection<?> allchildren) {
		for (Iterator<?> iterator = allchildren.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof CommandParameter) {
				Object v = ((CommandParameter) object).getValue();
				v = AdapterFactoryEditingDomain.unwrap(v);
				v = EcoreUtil.copy((EObject) v);

				IItemLabelProvider itemLabelProvider = (IItemLabelProvider) editingDomain.getAdapterFactory().adapt(v,
						IItemLabelProvider.class);
				String entryName = null;
				ImageDescriptor smallImage = null;
				if (itemLabelProvider != null) {
					entryName = itemLabelProvider.getText(v);
					Object imageObj = itemLabelProvider.getImage(v);
					smallImage = ExtendedImageRegistry.getInstance().getImageDescriptor(imageObj);
				}
				if (SmooksUIUtils.isUnSupportElement(smooksModelProvider.getPlatformVersion(), (EObject) v)) {
					continue;
				}
				if (isIgnoreType(v)) {
					continue;
				}
				EClass clazz = ((EObject) v).eClass();
				Object newModel = ((CommandParameter) object).getValue();
				if (newModel instanceof FeatureMap.Entry) {
					v = FeatureMapUtil.createEntry(((FeatureMap.Entry) newModel).getEStructuralFeature(), v);
				}

				if (entryName == null) {
					entryName = clazz.getName();
				}

				EStructuralFeature feature = ((CommandParameter) object).getEStructuralFeature();
				CombinedTemplateCreationEntry toolEntry = new CombinedTemplateCreationEntry(entryName, entryName,
						new SmooksModelCreationFactory(v, feature), smallImage, smallImage);
				drawer.add(toolEntry);

				if (needtToCreateChildrenEntry(v)) {
					IEditingDomainItemProvider provider = (IEditingDomainItemProvider) editingDomain
							.getAdapterFactory().adapt(v, IEditingDomainItemProvider.class);
					fillDrawer(drawer, provider.getNewChildDescriptors(v, editingDomain, null));
				}
			}
		}
	}

	private boolean needtToCreateChildrenEntry(Object element) {
		if (element instanceof BeanType) {
			return true;
		}
//		if (element instanceof BindingsType) {
//			return true;
//		}
		return false;
	}

	protected void fillActionGrouper(List<ISmooksActionGrouper> grouperList) {

	}

	protected List<ISmooksActionGrouper> getSmooksActionGrouper() {
		List<ISmooksActionGrouper> grouperList = new ArrayList<ISmooksActionGrouper>();
		fillActionGrouper(grouperList);
		// grouperList.add(new Reader11ActionGrouper());
		// grouperList.add(new Calc11ActionGrouper());
		// grouperList.add(new Database11ActionGrouper());
		// grouperList.add(new Datasources11ActionGrouper());
		// grouperList.add(new FragmentRouting11ActionGrouper());
		// grouperList.add(new Scripting11ActionGrouper());
		// grouperList.add(new Templating11ActionGrouper());
		// grouperList.add(new PersistenceActionGrouper());
		// grouperList.add(new Validation10ActionGrouper());
		return grouperList;
	}

	public class SmooksModelCreationFactory implements CreationFactory {

		private Object type = null;

		private Object model;

		public SmooksModelCreationFactory(Object model, Object type) {
			this.model = model;
			this.type = type;
		}

		public Object getNewObject() {
			return model;
		}

		public Object getObjectType() {
			return type;
		}

	}

}
