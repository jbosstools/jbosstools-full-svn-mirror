/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.javabean.commandprocessor;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class JavaBeanModelCommandProcessor implements ICommandProcessor {

	public void processEMFCommand(Command emfCommand,
			SmooksConfigurationFileGenerateContext context) {
	}

	public void processGEFCommand(org.eclipse.gef.commands.Command gefCommand,
			SmooksConfigurationFileGenerateContext context) {
		Object source = null;
		JavaBeanModel target = null;
		if (CreateConnectionCommand.class.isAssignableFrom(gefCommand
				.getClass())) {
			CreateConnectionCommand command = (CreateConnectionCommand) gefCommand;
			if (command.getSource() != null && command.getTarget() != null) {
				Object m = command.getTarget();
				Object s = command.getSource();
				if (m instanceof AbstractStructuredDataModel
						&& s instanceof AbstractStructuredDataModel) {
					source = ((AbstractStructuredDataModel) s)
							.getReferenceEntityModel();
					Object t = ((AbstractStructuredDataModel) m)
							.getReferenceEntityModel();
					if (!UIUtils.isInstanceCreatingConnection(source, t)) {
						if (t instanceof JavaBeanModel) {
							target = (JavaBeanModel) t;
							Class clazz = ((JavaBeanModel) t).getBeanClass();
							if (clazz != null && clazz != String.class) {
								PropertyModel property = new PropertyModel();
								property.setName("type");
								property.setValue(getTypeString(clazz));
								command.addPropertyModel(property);
							}
						}
					}
				}
			}

			CompoundCommand compoundCommand = new CompoundCommand();
			fillCreateParentLinkCommand(compoundCommand, source, target,
					context);
			if (!compoundCommand.isEmpty()) {
				compoundCommand.execute();
			}
		}
	}

	private void fillCreateParentLinkCommand(CompoundCommand compoundCommand,
			Object source, JavaBeanModel target,
			SmooksConfigurationFileGenerateContext context) {
		CreateConnectionCommand c = createParentLinkCommand(source, target,
				context);
		while (c != null) {
			compoundCommand.add(c);
			Object m = c.getTarget();
			Object s = c.getSource();
			Object source1 = null;
			JavaBeanModel target1 = null;
			if (m instanceof AbstractStructuredDataModel
					&& s instanceof AbstractStructuredDataModel) {
				source1 = ((AbstractStructuredDataModel) s)
						.getReferenceEntityModel();
				target1 = (JavaBeanModel) ((AbstractStructuredDataModel) m)
						.getReferenceEntityModel();
			}
			c = createParentLinkCommand(source1, target1, context);
		}
	}

	private CreateConnectionCommand createParentLinkCommand(Object source,
			JavaBeanModel target, SmooksConfigurationFileGenerateContext context) {
		ITreeContentProvider sourceProvider = context.getSourceViewerProvider();
		JavaBeanModel targetParent = target.getParent();
		AbstractStructuredDataModel targetParentGraphModel = UIUtils
				.findGraphModel(context.getGraphicalRootModel(), targetParent);
		if (targetParentGraphModel != null
				&& targetParentGraphModel instanceof IConnectableModel) {
			List list = ((IConnectableModel) targetParentGraphModel)
					.getModelTargetConnections();
			if (list.isEmpty()) {
				Object sourceParent = sourceProvider.getParent(source);
				IConnectableModel sourceParentGraphModel = (IConnectableModel) UIUtils
						.findGraphModel(context.getGraphicalRootModel(),
								sourceParent);
				if (sourceParentGraphModel != null) {
					CreateConnectionCommand connectionCommand = new CreateConnectionCommand();
					connectionCommand.setSource(sourceParentGraphModel);
					connectionCommand.setTarget(targetParentGraphModel);
					return connectionCommand;
				}
			}
		}
		return null;
	}

	public static String getTypeString(Class clazz) {
		if (clazz.isPrimitive()) {
			return getPrimitiveTypeString(clazz);
		}
		String name = clazz.getSimpleName();
		return name;
	}

	public static String getPrimitiveTypeString(Class clazz) {
		String name = clazz.getName();
		if ("int".equalsIgnoreCase(name))
			return "Integer";
		if ("double".equalsIgnoreCase(name))
			return "Double";
		if ("float".equalsIgnoreCase(name))
			return "Float";
		if ("long".equalsIgnoreCase(name))
			return "Long";
		if ("boolean".equalsIgnoreCase(name))
			return "Boolean";
		return "";
	}

}
