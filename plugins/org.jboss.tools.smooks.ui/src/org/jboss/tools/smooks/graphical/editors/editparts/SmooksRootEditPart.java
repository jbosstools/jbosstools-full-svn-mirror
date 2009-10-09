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
package org.jboss.tools.smooks.graphical.editors.editparts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.tree.command.GEFAdapterCommand;
import org.jboss.tools.smooks.gef.tree.editparts.RootEditPart;
import org.jboss.tools.smooks.gef.tree.editpolicy.RootPanelXYLayoutEditPolicy;
import org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart;
import org.jboss.tools.smooks.graphical.editors.commands.IgnoreException;
import org.jboss.tools.smooks.graphical.wizards.JavaBeanCreationWizard;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.JavabeanFactory;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Factory;
import org.jboss.tools.smooks.model.javabean12.ValueType;
import org.jboss.tools.smooks.model.javabean12.WiringType;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class SmooksRootEditPart extends RootEditPart {

	public static final int BEAN_TYPE = 2;

	public static final int BINDINGS_TYPE = 1;
	

	private GEFAdapterCommand createGEFCommand(EditingDomain domain, org.eclipse.emf.common.command.Command emfCommand,
			final ISmooksModelProvider provider, Object collections, Object owner, Object feature) {
		GEFAdapterCommand command = new GEFAdapterCommand(domain, emfCommand) {
			boolean firstTime = true;

			@Override
			public void execute() {
				if (firstTime) {
					JavaBeanCreationWizard wizard = new JavaBeanCreationWizard(provider, null);
					IEditorPart editorPart = getEditorPart();
					if (editorPart != null && collections != null && owner != null && feature != null) {
						final WizardDialog dialog = new WizardDialog(editorPart.getSite().getShell(), wizard);
						if (dialog.open() == Dialog.OK) {
							Object model = collections;
							if (collections instanceof FeatureMap.Entry) {
								model = ((FeatureMap.Entry) collections).getValue();
							}
							int type = BEAN_TYPE;
							if (model instanceof BindingsType)
								type = BINDINGS_TYPE;
							SmooksResourceListType resourceListType = null;
							if (editorPart instanceof SmooksGraphicalEditorPart) {
								resourceListType = ((SmooksGraphicalEditorPart) editorPart).getSmooksResourceListType();
							}
							List<Object> models = createJavaBeanModel(type, wizard.getJavaBeanModel(), wizard
									.getBindings(), resourceListType, new ArrayList<String>());
							String figureID = null;
							int index = 0;
							for (Iterator<?> iterator = models.iterator(); iterator.hasNext();) {
								Object object = (Object) iterator.next();
								if (object instanceof EObject) {
									figureID = SmooksGraphUtil.generateFigureIDViaModel((EObject) object);
									if (figureID != null && editorPart instanceof SmooksGraphicalEditorPart) {
										SmooksGraphicsExtType ext = ((SmooksGraphicalEditorPart) editorPart)
												.getSmooksGraphicsExtType();
										GraphType graph = ext.getGraph();
										if (graph != null) {
											FigureType figureType = SmooksGraphUtil.findFigureType(graph, figureID);
											if (figureType == null) {
												figureType = GraphFactory.eINSTANCE.createFigureType();
												graph.getFigure().add(figureType);
												figureType.setId(figureID);
											}
											String x = String.valueOf(this.x + index);
											String y = String.valueOf(this.y + index);
											figureType.setX(x);
											figureType.setY(y);
											index += 20;
										}
									}
								}
							}
							
							List<Object> creationModels = new ArrayList<Object>();

							if (collections instanceof FeatureMap.Entry) {
								for (Iterator<?> iterator = models.iterator(); iterator.hasNext();) {
									Object object = (Object) iterator.next();
									creationModels.add(FeatureMapUtil.createEntry(((FeatureMap.Entry) collections)
											.getEStructuralFeature(), object));
								}
							}
							collections = creationModels;

							emfCommand = AddCommand.create(domain, owner, feature, creationModels);
						} else {
							throw new IgnoreException();
						}
					}
					firstTime = false;
				}
				super.execute();
			}
		};

		return command;
	}

	private List<Object> createJavaBeanModel(int type, JavaBeanModel parentBeanModel, Object[] properties,
			SmooksResourceListType resourceListType, List<String> ids) {
		List<Object> creationObject = new ArrayList<Object>();
		Object parent = null;
		if (type == BEAN_TYPE) {
			parent = Javabean12Factory.eINSTANCE.createBeanType();
		}
		if (type == BINDINGS_TYPE) {
			parent = JavabeanFactory.eINSTANCE.createBindingsType();
		}
		creationObject.add(parent);
		String beanID = generateBeanID(parentBeanModel, resourceListType, ids);
		ids.add(beanID);
		if (parent instanceof BeanType) {
			((BeanType) parent).setBeanId(beanID);
			((BeanType) parent).setClass(parentBeanModel.getBeanClassString());
			if (properties != null) {
				for (int i = 0; i < properties.length; i++) {
					Object pro = properties[i];
					if (pro instanceof JavaBeanModel && belongsToMe(parentBeanModel, (JavaBeanModel) pro)) {
						if (((JavaBeanModel) pro).isPrimitive()) {
							ValueType value = Javabean12Factory.eINSTANCE.createValueType();
							value.setProperty(((JavaBeanModel) pro).getName());
							((BeanType) parent).getValue().add(value);
						} else {
							WiringType value = Javabean12Factory.eINSTANCE.createWiringType();
							if (((JavaBeanModel) parentBeanModel).isArray()
									|| ((JavaBeanModel) parentBeanModel).isList()) {

							} else {
								value.setProperty(((JavaBeanModel) pro).getName());
							}
							String refID = generateBeanID((JavaBeanModel) pro, resourceListType, ids);
							value.setBeanIdRef(refID);
							((BeanType) parent).getWiring().add(value);
							creationObject.addAll(createJavaBeanModel(type, (JavaBeanModel) pro, properties,
									resourceListType, ids));
						}
					}
				}
			}
		}
		if (parent instanceof BindingsType) {
			((BindingsType) parent).setBeanId(beanID);
			((BindingsType) parent).setClass(parentBeanModel.getBeanClassString());
			if (properties != null) {
				for (int i = 0; i < properties.length; i++) {
					Object pro = properties[i];
					if (pro instanceof JavaBeanModel && belongsToMe(parentBeanModel, (JavaBeanModel) pro)) {
						if (((JavaBeanModel) pro).isPrimitive()) {
							org.jboss.tools.smooks.model.javabean.ValueType value = JavabeanFactory.eINSTANCE
									.createValueType();
							value.setProperty(((JavaBeanModel) pro).getName());
							((BindingsType) parent).getValue().add(value);
						} else {
							org.jboss.tools.smooks.model.javabean.WiringType value = JavabeanFactory.eINSTANCE
									.createWiringType();
							if (((JavaBeanModel) parentBeanModel).isArray()
									|| ((JavaBeanModel) parentBeanModel).isList()) {

							} else {
								value.setProperty(((JavaBeanModel) pro).getName());
							}
							String refID = generateBeanID((JavaBeanModel) pro, resourceListType, ids);
							value.setBeanIdRef(refID);
							((BindingsType) parent).getWiring().add(value);
							creationObject.addAll(createJavaBeanModel(type, (JavaBeanModel) pro, properties,
									resourceListType, ids));
						}
					}
				}
			}
		}
		return creationObject;
	}

	private String generateBeanID(JavaBeanModel parentBeanModel, SmooksResourceListType listType, List<String> ids) {
		String pn = parentBeanModel.getName();
		int index = 1;
		while (idExsit(pn, listType, ids)) {
			pn = pn + String.valueOf(index);
			index = index + 1;
		}
		return pn;
	}

	private boolean idExsit(String id, SmooksResourceListType listType, List<String> ids) {
		Collection<EObject> models = SmooksUIUtils.getBeanIdModelList(listType);
		for (Iterator<?> iterator = models.iterator(); iterator.hasNext();) {
			EObject eObject = (EObject) iterator.next();
			EStructuralFeature feature = SmooksUIUtils.getBeanIDFeature(eObject);
			String id1 = eObject.eGet(feature).toString();
			if (id != null && id1 != null) {
				if (id.equals(id1)) {
					return true;
				}
			}
		}
		for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
			String i = (String) iterator.next();
			if (id != null && i != null) {
				if (id.equals(i)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean belongsToMe(JavaBeanModel parent, JavaBeanModel child) {
		if (parent != null && parent.isExpaned()) {
			return (parent.getChildren().indexOf(child) != -1);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootPanelXYLayoutEditPolicy() {

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				Object model = request.getNewObject();
				Object type = request.getNewObjectType();
				GraphicalViewer viewer = (GraphicalViewer) ((GraphicalEditPart) getHost()).getViewer();
				IEditorPart editorPart = ((DefaultEditDomain) viewer.getEditDomain()).getEditorPart();
				if (editorPart instanceof SmooksGraphicalEditorPart) {
					EObject owner = ((SmooksGraphicalEditorPart) editorPart).getSmooksResourceList();
					EditingDomain domain = ((SmooksGraphicalEditorPart) editorPart).getEditingDomain();
					if (model instanceof FeatureMap.Entry) {
						EStructuralFeature type1 = ((FeatureMap.Entry) model).getEStructuralFeature();
						model = ((FeatureMap.Entry) model).getValue();
						model = EcoreUtil.copy((EObject) model);
						model = FeatureMapUtil.createEntry(type1, model);
					}
					org.eclipse.emf.common.command.Command emfCommand = AddCommand.create(domain, owner, type, model);
					final ISmooksModelProvider provider = (ISmooksModelProvider) ((SmooksGraphicalEditorPart) editorPart)
							.getSmooksModelProvider();
					if (emfCommand.canExecute()) {
						GEFAdapterCommand command = createGEFCommand(domain, emfCommand, provider, model, owner, type);
						command.setCollections(model);
						command.setOwner(owner);
						command.setFeature(type);
						command.setX(request.getLocation().x);
						command.setY(request.getLocation().y);
						return command;
					}
				}
				return null;
			}

		});
	}

	protected IEditorPart getEditorPart() {
		GraphicalViewer viewer = (GraphicalViewer) this.getViewer();
		DefaultEditDomain editDomain = (DefaultEditDomain) viewer.getEditDomain();
		return editDomain.getEditorPart();
	}

}
