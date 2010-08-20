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
package org.jboss.tools.smooks.graphical.editors.template;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.ConnectionModelFactory;
import org.jboss.tools.smooks.graphical.editors.GraphicalModelFactory;
import org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart;
import org.jboss.tools.smooks.graphical.editors.autolayout.IAutoLayout;
import org.jboss.tools.smooks.graphical.editors.editparts.freemarker.FreemarkerAutoLayout;
import org.jboss.tools.smooks.graphical.editors.model.IValidatableModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.IFreemarkerTemplateModel;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.templating.model.ModelBuilder;
import org.jboss.tools.smooks.templating.template.TemplateBuilder;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Dart
 * 
 */
public class SmooksFreemarkerTemplateGraphicalEditor extends SmooksGraphicalEditorPart {

	public static final String ID = "__smooks_freemarker_template_graphical_editpart"; //$NON-NLS-1$

	private IAutoLayout autoLayout = null;

	public SmooksFreemarkerTemplateGraphicalEditor(ISmooksModelProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createEdtiPartFactory()
	 */
	@Override
	protected EditPartFactory createEdtiPartFactory() {
		FreemarkerTemplateEditFactory factory = new FreemarkerTemplateEditFactory();
		((FreemarkerTemplateEditFactory) factory).setDisplayInput(false);
		return factory;
	}

	@Override
	public void validateEnd(List<Diagnostic> diagnosticResult) {
		cleanValidationMarker();
		// validate freemarker model.Because the freemarker csv node
		// model isn't EMF model so they need to validate separately
		if (root == null)
			return;
		List<AbstractSmooksGraphicalModel> children = root.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator.next();
			if (abstractSmooksGraphicalModel instanceof FreemarkerTemplateGraphicalModel) {
				Object data = abstractSmooksGraphicalModel.getData();
				data = AdapterFactoryEditingDomain.unwrap(data);
				if (data instanceof Freemarker) {
					String type = SmooksModelUtils.getTemplateType((Freemarker) data);
					validateTemplate(type, abstractSmooksGraphicalModel);

				}
			}
		}
	}

	protected void validateTemplate(String type, AbstractSmooksGraphicalModel templateGraphModel) {
		List<AbstractSmooksGraphicalModel> templteContentsModels = templateGraphModel.getChildren();
		validateTemplateContentsModel(type, templteContentsModels);
	}

	protected void validateTemplateContentsModel(String type, List<AbstractSmooksGraphicalModel> templteContentsModels) {
		for (Iterator<?> iterator = templteContentsModels.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator.next();
			Object data = abstractSmooksGraphicalModel.getData();
			if (data instanceof IFreemarkerTemplateModel) {
				AbstractSmooksGraphicalModel requiredCollectionLinkParent = parentIsRequriedCollectionNode(abstractSmooksGraphicalModel);
				if (requiredCollectionLinkParent != null && enforceCollectionSubMappingRules(abstractSmooksGraphicalModel)) {
					AbstractXMLObject parentNode = (AbstractXMLObject) requiredCollectionLinkParent.getData();
					abstractSmooksGraphicalModel.addMessage(Messages.SmooksFreemarkerTemplateGraphicalEditor_WarningMessage1 + parentNode.getName()
							+ Messages.SmooksFreemarkerTemplateGraphicalEditor_WarningMessage2);
					abstractSmooksGraphicalModel.setSeverity(IValidatableModel.ERROR);
				}
				if (isRequiredNode(abstractSmooksGraphicalModel)) {
					abstractSmooksGraphicalModel.addMessage(Messages.SmooksFreemarkerTemplateGraphicalEditor_WarningMessage3);
					abstractSmooksGraphicalModel.setSeverity(IValidatableModel.ERROR);
				}
				if (isRequiredCollectionNode(abstractSmooksGraphicalModel)) {
					abstractSmooksGraphicalModel.addMessage(Messages.SmooksFreemarkerTemplateGraphicalEditor_WarningMessage4);
					abstractSmooksGraphicalModel.setSeverity(IValidatableModel.ERROR);
				}
			}
			validateTemplateContentsModel(type, abstractSmooksGraphicalModel.getChildren());
		}
	}

	public static AbstractSmooksGraphicalModel parentIsRequriedCollectionNode(
			AbstractSmooksGraphicalModel abstractSmooksGraphicalModel) {
		AbstractSmooksGraphicalModel parent = abstractSmooksGraphicalModel.getParent();
		Object data = abstractSmooksGraphicalModel.getData();
		if (data instanceof IFreemarkerTemplateModel) {
			if (isRequiredCollectionNode(parent)) {
				return parent;
			}
			return parentIsRequriedCollectionNode(parent);
		}
		return null;
	}

	private boolean isRequiredNode(AbstractSmooksGraphicalModel abstractSmooksGraphicalModel) {
		Object data = abstractSmooksGraphicalModel.getData();
		AbstractSmooksGraphicalModel pm = abstractSmooksGraphicalModel;
		while (pm != null && !(pm instanceof FreemarkerTemplateGraphicalModel)) {
			pm = pm.getParent();
		}

		if (data instanceof IFreemarkerTemplateModel && pm instanceof FreemarkerTemplateGraphicalModel) {
			TemplateBuilder builder = ((FreemarkerTemplateGraphicalModel) pm).getTemplateBuilder();
			if (((IFreemarkerTemplateModel) data).isRequired() && !((IFreemarkerTemplateModel) data).isHidden(builder)) {
				if (abstractSmooksGraphicalModel.getTargetConnections().isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isRequiredCollectionNode(AbstractSmooksGraphicalModel abstractSmooksGraphicalModel) {
		Object data = abstractSmooksGraphicalModel.getData();
		if (data instanceof IFreemarkerTemplateModel) {
			if (((IFreemarkerTemplateModel) data).isManyOccurs()) {
				if (abstractSmooksGraphicalModel.getTargetConnections().isEmpty()) {
					return enforceCollectionSubMappingRules(abstractSmooksGraphicalModel);
				}
			}
		}
		return false;
	}

	public static boolean enforceCollectionSubMappingRules(AbstractSmooksGraphicalModel abstractSmooksGraphicalModel) {
		Object data = abstractSmooksGraphicalModel.getData();
		if (data instanceof IFreemarkerTemplateModel) {
			Node modelNode = ((IFreemarkerTemplateModel) data).getModelNode();
			if(modelNode instanceof Element) {
				return ModelBuilder.getEnforceCollectionSubMappingRules((Element) modelNode);
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * getAutoLayout()
	 */
	@Override
	public IAutoLayout getAutoLayout() {
		if (autoLayout == null) {
			autoLayout = new FreemarkerAutoLayout();
		}
		return autoLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createConnectionModelFactory()
	 */
	@Override
	protected ConnectionModelFactory createConnectionModelFactory() {
		return new FreemarkerTemplateConnectionModelFactory(this.getSmooksModelProvider(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createGraphicalModelFactory()
	 */
	@Override
	protected GraphicalModelFactory createGraphicalModelFactory() {
		return new FreemarkerTemplateGraphicalModelFactory(this.getSmooksModelProvider(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#getID
	 * ()
	 */
	@Override
	public String getID() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createInputDataGraphModel()
	 */
	@Override
	protected List<AbstractSmooksGraphicalModel> createInputDataGraphModel() {
		return Collections.emptyList();
	}
}
