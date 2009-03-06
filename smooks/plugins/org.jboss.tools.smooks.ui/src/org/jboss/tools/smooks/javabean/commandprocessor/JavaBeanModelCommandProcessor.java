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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.javabean.model.Java2JavaAnalyzer;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class JavaBeanModelCommandProcessor implements ICommandProcessor {

	public boolean processEMFCommand(Command emfCommand,
			SmooksConfigurationFileGenerateContext context) {
		return true;
	}

	private boolean isTargetDeep(JavaBeanModel javaBean) {
		JavaBeanModel parent = javaBean.getParent();
		if (parent != null) {
			parent = parent.getParent();
		}
		if (parent != null) {
			parent = parent.getParent();
			if ((parent != null)) {
				return false;
			} else {
				if (parent != null && !(parent instanceof JavaBeanList)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isSecondLevelComplexJavaBean(JavaBeanModel beanModel) {
		if (beanModel.isPrimitive())
			return false;
		JavaBeanModel parent = beanModel.getParent();
		if (parent != null) {
			if (parent instanceof JavaBeanList) {
				return false;
			}
			parent = parent.getParent();
		}
		if (parent != null) {
			if (!(parent instanceof JavaBeanList))
				return false;
		}
		return true;
	}

	private boolean checkSourceToTarget(
			AbstractStructuredDataModel sourceGraphModel,
			AbstractStructuredDataModel targetGraphModel,
			SmooksConfigurationFileGenerateContext context) {
		if (sourceGraphModel instanceof TargetModel)
			return true;
		Object source = ((AbstractStructuredDataModel) sourceGraphModel)
				.getReferenceEntityModel();
		Object t = ((AbstractStructuredDataModel) targetGraphModel)
				.getReferenceEntityModel();
		List connections = ((IConnectableModel) targetGraphModel)
				.getModelTargetConnections();
		// *2Java , allow the target node have two connections :
		// 1.beancreation,2.referencebinding
		if (connections.size() > 1)
			return false;
		if (connections.size() == 1) {
			LineConnectionModel line = (LineConnectionModel) connections.get(0);
			Object bindingType = line
					.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE);
			if (!BeanPopulatorMappingAnalyzer.REFERENCE_BINDING
					.equals(bindingType)) {
				if (sourceGraphModel instanceof SourceModel) {
					return false;
				}
			}
		}
		if (t instanceof JavaBeanModel) {
			if (isSecondLevelComplexJavaBean((JavaBeanModel) t)
					&& sourceGraphModel instanceof SourceModel) {
				createReferenceConnection(source, (JavaBeanModel) t, context);
				return false;
			}
		}

		if (source instanceof IXMLStructuredObject
				&& t instanceof JavaBeanModel
				&& !(source instanceof JavaBeanModel)) {
			boolean isattribute = ((IXMLStructuredObject) source).isAttribute();
			JavaBeanModel targetModel = (JavaBeanModel) t;
			boolean isprimitive = targetModel.isPrimitive();
			if (isattribute && !isprimitive)
				return false;
			boolean canlink = hasBeanCreationParentLink(targetGraphModel,
					context);
			if (isprimitive) {
				if (!canlink){
					linkBeanCreateionParentLink(sourceGraphModel,
							targetGraphModel, context);
				}
			}
		}

		if (source instanceof JavaBeanModel && t instanceof JavaBeanModel
				&& sourceGraphModel instanceof SourceModel) {

			JavaBeanModel sourceModel = (JavaBeanModel) source;
			JavaBeanModel targetModel = (JavaBeanModel) t;
			boolean sis = ((JavaBeanModel) source).isPrimitive();
			boolean tis = ((JavaBeanModel) t).isPrimitive();
			if ((sis && !tis) || (!sis && tis)) {
				return false;
			}

			boolean canlink = hasBeanCreationParentLink(targetGraphModel,
					context);

			if (tis) {
				if (!canlink) {
					linkBeanCreateionParentLink(sourceGraphModel,
							targetGraphModel, context);
				}
			}

			Class sourceClass = sourceModel.getBeanClass();
			Class targetClass = targetModel.getBeanClass();
			boolean isCompositeSource = sourceClass.isArray()
					|| Collection.class.isAssignableFrom(sourceClass);
			boolean isCompositeTarget = targetClass.isArray()
					|| Collection.class.isAssignableFrom(targetClass);
			if (isCompositeSource != isCompositeTarget)
				return false;
		}
		return true;
	}

	private void linkBeanCreateionParentLink(
			AbstractStructuredDataModel sourceGraphModel,
			AbstractStructuredDataModel targetGraphModel,
			SmooksConfigurationFileGenerateContext context) {
		Object obj = sourceGraphModel.getReferenceEntityModel();
		AbstractStructuredDataModel sourceParentGraph = null;
		AbstractStructuredDataModel targetParentGraph = null;
		if (obj instanceof IXMLStructuredObject) {
			IXMLStructuredObject sourceParent = ((IXMLStructuredObject) obj)
					.getParent();
			sourceParentGraph = UIUtils.findGraphModel(context
					.getGraphicalRootModel(), sourceParent);
		}
		Object obj1 = targetGraphModel.getReferenceEntityModel();
		if (obj1 instanceof IXMLStructuredObject) {
			IXMLStructuredObject targetParent = ((IXMLStructuredObject) obj1)
					.getParent();
			targetParentGraph = UIUtils.findGraphModel(context
					.getGraphicalRootModel(), targetParent);
		}
		if (sourceParentGraph != null && targetParentGraph != null) {
			PropertyModel propertyModel = new PropertyModel();
			propertyModel.setName(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE);
			propertyModel.setValue(BeanPopulatorMappingAnalyzer.BEAN_CREATION);
			CreateConnectionCommand command = new CreateConnectionCommand();
			command.addPropertyModel(propertyModel);
			command.setSource((IConnectableModel) sourceParentGraph);
			command.setTarget((IConnectableModel) targetParentGraph);
			context.getGefDomain().getCommandStack().execute(command);
		}

	}

	private boolean hasBeanCreationParentLink(
			AbstractStructuredDataModel sourceGraphModel,
			SmooksConfigurationFileGenerateContext context) {
		Object sourceNode = sourceGraphModel.getReferenceEntityModel();
		if (!(sourceNode instanceof JavaBeanModel)) {
			return false;
		}
		JavaBeanModel sourceParent = ((JavaBeanModel) sourceNode).getParent();
		if (sourceParent == null) {
			return false;
		}
		if (sourceParent instanceof JavaBeanList) {
			return false;
		}
		IConnectableModel sourceParentGraph = (IConnectableModel) UIUtils
				.findGraphModel(context.getGraphicalRootModel(), sourceParent);
		// If the source's parent node haven't any associated resource-config ,
		// refuse the connect request.
		List parentConnections = sourceParentGraph.getModelTargetConnections();
		if (parentConnections.size() == 0)
			return false;
		for (Iterator iterator = parentConnections.iterator(); iterator
				.hasNext();) {
			LineConnectionModel object = (LineConnectionModel) iterator.next();
			if (Java2JavaAnalyzer.BEAN_CREATION.equals(object
					.getProperty(Java2JavaAnalyzer.PRO_BINDING_TYPE))) {
				if (object
						.getProperty(Java2JavaAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG) != null) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkTargetToTarget(
			AbstractStructuredDataModel sourceGraphModel,
			AbstractStructuredDataModel targetGraphModel,
			SmooksConfigurationFileGenerateContext context) {
		if (sourceGraphModel instanceof SourceModel) {
			return true;
		}
		Object sourceParent = sourceGraphModel.getReferenceEntityModel();
		if (!(sourceParent instanceof JavaBeanModel)) {
			return false;
		}
		sourceParent = ((JavaBeanModel) sourceParent).getParent();
		if (sourceParent == null) {
			return false;
		}
		if (sourceParent instanceof JavaBeanList) {
			return false;
		}
		IConnectableModel sourceParentGraph = (IConnectableModel) UIUtils
				.findGraphModel(context.getGraphicalRootModel(), sourceParent);
		List connections = ((IConnectableModel) targetGraphModel)
				.getModelTargetConnections();
		List connections1 = ((IConnectableModel) sourceGraphModel)
				.getModelSourceConnections();
		// If the source's parent node haven't any associated resource-config ,
		// refuse the connect request.
		List parentConnections = sourceParentGraph.getModelTargetConnections();
		if (parentConnections.size() == 0)
			return false;
		for (Iterator iterator = parentConnections.iterator(); iterator
				.hasNext();) {
			LineConnectionModel object = (LineConnectionModel) iterator.next();
			if (object
					.getProperty(Java2JavaAnalyzer.PRO_REFERENCE_RESOURCE_CONFIG) == null) {
				return false;
			}
		}

		if (connections1.size() > 0)
			return false;
		boolean hasBeanCreation = false;
		// *2Java , allow the target node have two kind connections :
		// 1.beancreation,2.referencebinding
		// reference binding need the target node have the bean creation
		// connection already.
		for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
			LineConnectionModel line = (LineConnectionModel) iterator.next();
			Object bindingType = line
					.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE);
			if (BeanPopulatorMappingAnalyzer.BEAN_CREATION.equals(bindingType)) {
				hasBeanCreation = true;
				break;
			}
		}
		if (!hasBeanCreation)
			return false;
		return true;
	}

	private boolean checkGEFCommand(
			org.eclipse.gef.commands.Command gefCommand,
			SmooksConfigurationFileGenerateContext context) {
		if (CreateConnectionCommand.class.isAssignableFrom(gefCommand
				.getClass())) {
			CreateConnectionCommand command = (CreateConnectionCommand) gefCommand;
			if (command.getSource() != null && command.getTarget() != null) {
				Object m = command.getTarget();
				Object s = command.getSource();
				if (m instanceof AbstractStructuredDataModel
						&& s instanceof AbstractStructuredDataModel) {
					AbstractStructuredDataModel sourceGraphModel = (AbstractStructuredDataModel) s;
					AbstractStructuredDataModel targetGraphModel = (AbstractStructuredDataModel) m;
					Object t = targetGraphModel.getReferenceEntityModel();
					if (t instanceof JavaBeanModel) {
						if (!isTargetDeep((JavaBeanModel) t)) {
							return false;
						}
					}
					if (!checkSourceToTarget(sourceGraphModel,
							targetGraphModel, context)) {
						return false;
					}
					if (!checkTargetToTarget(sourceGraphModel,
							targetGraphModel, context)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected void createReferenceConnection(Object source,
			JavaBeanModel target, SmooksConfigurationFileGenerateContext context) {
		CompoundCommand commands = new CompoundCommand();
		JavaBeanList list = (JavaBeanList) target.getRootParent();
		JavaBeanModel cloneModel = (JavaBeanModel) target.clone();
		list.addJavaBean(cloneModel);

		AbstractStructuredDataModel targetGraphModel = UIUtils.findGraphModel(
				context.getGraphicalRootModel(), target);
		AbstractStructuredDataModel cloneTargetGraphModel = UIUtils
				.findGraphModel(context.getGraphicalRootModel(), cloneModel);
		AbstractStructuredDataModel sourceGraphModel = UIUtils.findGraphModel(
				context.getGraphicalRootModel(), source);

		if (cloneTargetGraphModel != null && sourceGraphModel != null
				&& targetGraphModel != null) {
			CreateConnectionCommand beanCreationLineCommand = new CreateConnectionCommand();
			beanCreationLineCommand
					.setSource((IConnectableModel) sourceGraphModel);
			beanCreationLineCommand
					.setTarget((IConnectableModel) cloneTargetGraphModel);
			PropertyModel bindingType = new PropertyModel(
					BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
					BeanPopulatorMappingAnalyzer.BEAN_CREATION);
			beanCreationLineCommand.addPropertyModel(bindingType);
			commands.add(beanCreationLineCommand);

			CreateConnectionCommand referenceLineCommand = new CreateConnectionCommand();
			referenceLineCommand
					.setSource((IConnectableModel) targetGraphModel);
			referenceLineCommand
					.setTarget((IConnectableModel) cloneTargetGraphModel);
			PropertyModel referenceType = new PropertyModel(
					BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
					BeanPopulatorMappingAnalyzer.REFERENCE_BINDING);
			referenceLineCommand.addPropertyModel(referenceType);
			commands.add(referenceLineCommand);
		}

		if (commands.size() > 0) {
			executeGEFCommand(commands, context);
		}
	}

	protected void executeGEFCommand(org.eclipse.gef.commands.Command command,
			SmooksConfigurationFileGenerateContext context) {
		EditDomain domain = context.getGefDomain();
		CommandStack stack = domain.getCommandStack();
		if (stack != null) {
			stack.execute(command);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor#
	 * processGEFCommand(org.eclipse.gef.commands.Command,
	 * org.jboss.tools.smooks
	 * .ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public boolean processGEFCommand(
			org.eclipse.gef.commands.Command gefCommand,
			SmooksConfigurationFileGenerateContext context) {
		// if(true) return true;
		Object source = null;
		JavaBeanModel target = null;
		if (!checkGEFCommand(gefCommand, context)) {
			return false;
		}
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
					if (t instanceof JavaBeanModel) {
						target = (JavaBeanModel) t;
					} else {
						return true;
					}
					if (!UIUtils.isInstanceCreatingConnection(source, t)) {
						if (t instanceof JavaBeanModel) {
							PropertyModel bindingType = new PropertyModel(
									BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
									BeanPopulatorMappingAnalyzer.PROPERTY_BINDING);
							command.addPropertyModel(bindingType);
							Class clazz = ((JavaBeanModel) t).getBeanClass();
							if (clazz != null && clazz != String.class) {
								PropertyModel property = new PropertyModel();
								property.setName("type");
								property.setValue(getTypeString(clazz));
								command.addPropertyModel(property);
							}
						}
					} else {
						if (s instanceof TargetModel) {
							PropertyModel bindingType = new PropertyModel(
									BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
									BeanPopulatorMappingAnalyzer.REFERENCE_BINDING);
							command.addPropertyModel(bindingType);
						}
						if (s instanceof SourceModel) {
							PropertyModel bindingType = new PropertyModel(
									BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
									BeanPopulatorMappingAnalyzer.BEAN_CREATION);
							command.addPropertyModel(bindingType);
						}
					}
				}
			}

			CompoundCommand compoundCommand = new CompoundCommand();
			fillCreateParentLinkCommand(compoundCommand, source, target,
					context);
			if (!compoundCommand.isEmpty()) {
				executeGEFCommand(compoundCommand, context);
			}
		}
		return true;
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
		if (target == null)
			return null;
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
					if (!UIUtils.isInstanceCreatingConnection(source,
							targetParent)) {
						Class clazz = ((JavaBeanModel) targetParent)
								.getBeanClass();
						PropertyModel bindingType = new PropertyModel(
								BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
								BeanPopulatorMappingAnalyzer.PROPERTY_BINDING);
						connectionCommand.addPropertyModel(bindingType);
						if (clazz != null && clazz != String.class) {
							PropertyModel property = new PropertyModel();
							property.setName("type");
							property.setValue(getTypeString(clazz));
							connectionCommand.addPropertyModel(property);
						}
					} else {
						if (sourceParentGraphModel instanceof TargetModel) {
							PropertyModel bindingType = new PropertyModel(
									BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
									BeanPopulatorMappingAnalyzer.REFERENCE_BINDING);
							connectionCommand.addPropertyModel(bindingType);
						}
						if (sourceParentGraphModel instanceof SourceModel) {
							PropertyModel bindingType = new PropertyModel(
									BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE,
									BeanPopulatorMappingAnalyzer.BEAN_CREATION);
							connectionCommand.addPropertyModel(bindingType);
						}
					}
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
