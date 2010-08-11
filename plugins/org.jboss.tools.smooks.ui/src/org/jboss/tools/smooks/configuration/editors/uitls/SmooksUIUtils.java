/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.uitls;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.input.InputParameter;
import org.jboss.tools.smooks.configuration.editors.input.InputType;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.GlobalParams;
import org.jboss.tools.smooks.model.core.ICoreFactory;
import org.jboss.tools.smooks.model.core.ICorePackage;
import org.jboss.tools.smooks.model.core.IParam;
import org.milyn.javabean.dynamic.Model;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class SmooksUIUtils {

	public static String[] SMOOKS_PLATFORM_1_1_CONFLICT_NAMESPACES = new String[] {};

	public static final String FILE_PRIX = "File:/"; //$NON-NLS-1$

	public static final String WORKSPACE_PRIX = "Workspace:/"; //$NON-NLS-1$

	public static final String RESOURCE = "Resource:/"; //$NON-NLS-1$

	public static final String XSL_NAMESPACE = " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" "; //$NON-NLS-1$

	public static int VALUE_TYPE_VALUE = 1;

	public static int VALUE_TYPE_TEXT = 2;

	public static int VALUE_TYPE_COMMENT = 3;

	public static int VALUE_TYPE_CDATA = 0;

	public static final int SELECTOR_EXPAND_MAX_LEVEL = 5;

	public static final String[] SELECTOR_SPERATORS = new String[] { " ", "/" }; //$NON-NLS-1$ //$NON-NLS-2$

	public static String parseFilePath(String path)
			throws InvocationTargetException {
		if (path == null)
			return null;
		if (new File(path).exists()) {
			return path;
		}
		int index = path.indexOf(FILE_PRIX);
		if (index != -1) {
			path = path.substring(index + FILE_PRIX.length(), path.length());
		} else {
			index = path.indexOf(WORKSPACE_PRIX);
			if (index != -1) {
				path = path.substring(index + WORKSPACE_PRIX.length(),
						path.length());
				Path wpath = new Path(path);
				IFile file = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(wpath);
				if (file.exists()) {
					path = file.getLocation().toOSString();
				} else {
					throw new InvocationTargetException(new Exception(
							"File : " + path + " isn't exsit")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				throw new InvocationTargetException(new Exception(
						"This path is un-support" + path + ".")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return path;
	}

	public static void showErrorDialog(Shell shell, Status status) {
		ErrorDialog.openError(shell, "Error", "error", status); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static Status createErrorStatus(Throwable throwable, String message) {
		while (throwable != null
				&& throwable instanceof InvocationTargetException) {
			throwable = ((InvocationTargetException) throwable)
					.getTargetException();
		}
		return new Status(Status.ERROR, SmooksConfigurationActivator.PLUGIN_ID,
				message, throwable);
	}

	public static Status createErrorStatus(Throwable throwable) {
		return createErrorStatus(throwable, "Error"); //$NON-NLS-1$
	}

	public static IParam getInputTypeAssociatedParamType(InputType inputType,
			Model<SmooksModel> resourceList) {
		SmooksModel rootModel = resourceList.getModelRoot();
		GlobalParams params = rootModel.getParams();
		if (params != null) {
			return params.getParam(inputType.getType(), inputType.getPath());
		}
		return null;
	}

	public static IParam getInputTypeParam(SmooksModel resourceList) {
		GlobalParams params = resourceList.getParams();
		if (params != null) {
			return params.getParam(SmooksModelUtils.INPUT_TYPE);
		}
		return null;
	}

	public static List<InputType> recordInputDataInfomation(
			EditingDomain domain, GlobalParams paramsType, String type,
			String path, Properties properties, CompoundCommand compoundCommand) {
		List<InputType> inputTypeList = new ArrayList<InputType>();
		if (type != null && path != null && domain != null) {
			String[] values = path.split(";"); //$NON-NLS-1$
			if (values == null || values.length == 0) {
				values = new String[] { path };
			}
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				value = value.trim();
				if (value.length() == 0)
					continue;
				IParam inputParam = ICoreFactory.eINSTANCE.createParam();
				InputType input = new InputType();
				input.setPath(path);
				input.setType(type);
				input.setActived(false);

				inputParam.setName(type);
				inputParam.setValue(path);
				input.setRelatedParameter(inputParam);
				List<IParam> params = generateExtParams(type, path, properties);
				for (Iterator<?> iterator = params.iterator(); iterator
						.hasNext();) {
					IParam paramType2 = (IParam) iterator.next();
					InputParameter p = new InputParameter();
					p.setName(getInputParameterName(input.getType(),
							paramType2.getName()));
					p.setValue(paramType2.getValue());
					input.getParameters().add(p);
					// input.setRelatedParameter(paramType2);
				}
				params.add(inputParam);
				Command command = null;
				try {
					command = AddCommand.create(domain, paramsType,
							ICorePackage.Literals.PARAMS__PARAMS, params);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				if (command.canExecute()) {
					if (compoundCommand != null) {
						compoundCommand.append(command);
					} else {
						domain.getCommandStack().execute(command);
					}
					input.setRelatedParameter(inputParam);
					inputTypeList.add(input);
				}
			}
		}
		return inputTypeList;
	}

	public static void removeInputType(InputType inputType,
			ISmooksModelProvider smooksModelProvider) {
		SmooksModel resourceList = smooksModelProvider.getSmooksModel()
				.getModelRoot();
		if (resourceList != null) {
			GlobalParams params = resourceList.getParams();
			if (params != null) {
				List<IParam> paramList = params.getParams();
				List<IParam> removingList = new ArrayList<IParam>();
				for (Iterator<?> iterator = paramList.iterator(); iterator
						.hasNext();) {
					IParam paramType = (IParam) iterator.next();
					String name = paramType.getName();
					String value = paramType.getValue();
					if (inputType.getType().equals(name)
							&& inputType.getPath().equals(value)) {
						// find the associated param
						removingList.add(paramType);
						continue;
					}
					if (isInputAssociatedParameter(paramType, inputType)) {
						removingList.add(paramType);
					}
				}
				if (!removingList.isEmpty()) {
					Command removeCommand = RemoveCommand.create(
							smooksModelProvider.getEditingDomain(),
							removingList);
					if (removeCommand.canExecute()) {
						smooksModelProvider.getEditingDomain()
								.getCommandStack().execute(removeCommand);
					}
				}
			}
		}
	}

	public static IJavaProject getJavaProject(EObject model) {
		IResource r = getResource(model);
		if (r != null) {
			IProject p = r.getProject();
			return JavaCore.create(p);
		}
		return null;
	}

	public static Class<?> loadClass(String className, Object resource)
			throws JavaModelException, ClassNotFoundException {
		if (resource == null)
			return null;
		IJavaProject jp = null;
		if (resource instanceof IJavaProject) {
			jp = (IJavaProject) resource;
		}
		if (resource instanceof IResource) {
			IProject project = ((IResource) resource).getProject();
			if (project != null) {
				jp = JavaCore.create(project);
			}
		}
		if (jp != null) {
			ProjectClassLoader loader = new ProjectClassLoader(jp);
			if (className.endsWith("[]")) { //$NON-NLS-1$
				className = className.substring(0, className.length() - 2);
				Class<?> clazz = loader.loadClass(className);
				Object arrayInstance = Array.newInstance(clazz, 0);
				clazz = arrayInstance.getClass();
				arrayInstance = null;
				return clazz;
			}
			if (className.endsWith("]") && !className.endsWith("[]")) { //$NON-NLS-1$ //$NON-NLS-2$
				// int index = className.indexOf("[");
				// String collectionName = className.substring(0,index);
				// String componentName = className.substring(index + 1 ,
				// className.length() - 1);
				// Class<?> clazz = loader.loadClass(className);
				// Object arrayInstance = Array.newInstance(clazz, 0);
				// clazz = arrayInstance.getClass();
				// arrayInstance = null;
				// return clazz;
			}
			return loader.loadClass(className);
		}

		return null;
	}

	public static IResource getResource(EObject model) {
		if (model == null)
			return null;
		final Resource resource = ((EObject) model).eResource();
		return getResource(resource);
	}

	public static IResource getResource(Resource resource) {
		if (resource == null)
			return null;
		URI uri = resource.getURI();

		if (uri == null)
			return null;
		IResource workspaceResource = null;
		if (uri.isPlatformResource()) {
			String path = uri.toPlatformString(true);
			workspaceResource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(new Path(path));
		}
		return workspaceResource;
	}

	public static String getInputParameterName(String type, String name) {
		int index = name.indexOf(type);
		return name.substring(index + type.length() + 1, name.length());
	}

	public static List<IParam> generateExtParams(String type, String path,
			Properties properties) {
		List<IParam> lists = new ArrayList<IParam>();
		if (properties != null) {
			Enumeration<?> enumerations = properties.keys();
			while (enumerations.hasMoreElements()) {
				Object key = (Object) enumerations.nextElement();
				IParam param = ICoreFactory.eINSTANCE.createParam();
				param.setValue(properties.getProperty(key.toString()));
				param.setName(generateInputParameterName(type, key.toString()));
				lists.add(param);
			}
		}
		return lists;
	}

	public static String generateInputParameterName(String type, String name) {
		return type + "." + name; //$NON-NLS-1$
	}

	public static List<InputType> recordInputDataInfomation(
			EditingDomain domain, GlobalParams paramsType, String type,
			String path, Properties properties) {
		return recordInputDataInfomation(domain, paramsType, type, path,
				properties, null);
	}

	public static List<InputType> getInputTypeList(SmooksModel resourceList) {
		List<InputType> inputList = new ArrayList<org.jboss.tools.smooks.configuration.editors.input.InputType>();
		GlobalParams params = resourceList.getParams();
		if (params != null) {
			List<IParam> paramList = params.getParams();
			for (Iterator<?> iterator = paramList.iterator(); iterator
					.hasNext();) {
				IParam paramType = (IParam) iterator.next();
				String type = paramType.getType();
				if (isInputParamType(paramType)) {
					org.jboss.tools.smooks.configuration.editors.input.InputType input = new org.jboss.tools.smooks.configuration.editors.input.InputType();
					input.setType(paramType.getName());
					input.setActived(SmooksModelUtils.INPUT_ACTIVE_TYPE
							.equals(type));
					String path = paramType.getValue();
					if (path != null) {
						path = path.trim();
					}
					input.setPath(path);
					inputList.add(input);
				}
			}

			for (Iterator<?> iterator = inputList.iterator(); iterator
					.hasNext();) {
				org.jboss.tools.smooks.configuration.editors.input.InputType input = (org.jboss.tools.smooks.configuration.editors.input.InputType) iterator
						.next();
				for (Iterator<?> iterator2 = paramList.iterator(); iterator2
						.hasNext();) {
					IParam paramType = (IParam) iterator2.next();
					if (isInputAssociatedParameter(paramType, input)) {
						InputParameter p = new InputParameter();
						p.setName(getInputParameterName(input.getType(),
								paramType.getName()));
						p.setValue(paramType.getValue());
						input.getParameters().add(p);
					}
				}
			}
		}
		return inputList;
	}

	public static boolean isInputParamType(IParam param) {
		String type = param.getType();
		if (SmooksModelUtils.INPUT_ACTIVE_TYPE.equals(type)
				|| SmooksModelUtils.INPUT_DEACTIVE_TYPE.equals(type)) {
			return true;
		}
		return false;
	}

	public static boolean isInputAssociatedParameter(IParam param,
			org.jboss.tools.smooks.configuration.editors.input.InputType input) {
		String type = input.getType();
		String pn = param.getName();
		if (pn != null && pn.startsWith(type) && !pn.equals(type)) {
			return true;
		}
		return false;
	}

	private static void expandSelectorViewer(IXMLStructuredObject model,
			TreeViewer viewer, List<String> expandedModel, int level) {
		if (level >= SELECTOR_EXPAND_MAX_LEVEL)
			return;
		level++;
		if (expandedModel.contains(model.getNodeName())) {
			return;
		} else {
			expandedModel.add(model.getNodeName());
			viewer.expandToLevel(model, 0);
			List<IXMLStructuredObject> children = model.getChildren();
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator
						.next();
				expandSelectorViewer(structuredObject, viewer, expandedModel,
						level);
			}
		}
	}

	public static void expandSelectorViewer(List<Object> models,
			TreeViewer viewer) {
		for (Iterator<?> iterator = models.iterator(); iterator.hasNext();) {
			Object model = (Object) iterator.next();
			if (model instanceof IXMLStructuredObject) {
				expandSelectorViewer((IXMLStructuredObject) model, viewer,
						new ArrayList<String>(), 0);
			}
		}
	}
}
