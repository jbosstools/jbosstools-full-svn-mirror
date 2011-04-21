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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.SelectorAttributes;
import org.jboss.tools.smooks.configuration.editors.input.InputSourceType;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.GlobalParams;
import org.jboss.tools.smooks.model.core.IComponent;
import org.jboss.tools.smooks.model.core.ICoreFactory;
import org.jboss.tools.smooks.model.core.ICorePackage;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.model.javabean.IBean;
import org.jboss.tools.smooks.model.javabean.IExpression;
import org.jboss.tools.smooks.model.javabean.IValue;
import org.jboss.tools.smooks.model.javabean.IWiring;
import org.jboss.tools.smooks.model.javabean.JavaBeanPackage;

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

	public static String parseFilePath(String path) {
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
					throw new IllegalArgumentException("File : " + path + " isn't exsit"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				throw new IllegalArgumentException("This path is un-support" + path + "."); //$NON-NLS-1$ //$NON-NLS-2$
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

	public static IParam getInputTypeParam(SmooksModel resourceList) {
		GlobalParams params = resourceList.getParams();
		if (params != null) {
			return params.getParam(SmooksModelUtils.INPUT_TYPE);
		}
		return null;
	}

	public static IParam recordInputDataInfomation(
			EditingDomain domain, GlobalParams globalParams, String type,
			String path, Properties properties, CompoundCommand compoundCommand) {
				
		if (type != null && path != null && domain != null) {
			String[] values = path.split(";"); //$NON-NLS-1$
			
			if (values != null && values.length > 0) {
				String value = values[0].trim();
				
				if (value.length() > 0) {
					IParam inputParam = ICoreFactory.eINSTANCE.createParam();
					inputParam.setName(type);
					inputParam.setValue(path);
	
					List<IParam> params = generateExtParams(type, path, properties);
					params.add(inputParam);

					Command command = null;
					try {
						command = AddCommand.create(domain, globalParams, ICorePackage.Literals.PARAMS__PARAMS, params);
					} catch (Throwable t) {
						t.printStackTrace();
					}
					if (command.canExecute()) {
						if (compoundCommand != null) {
							compoundCommand.append(command);
						} else {
							domain.getCommandStack().execute(command);
						}
					}
					
					return inputParam;
				}
			}
		}
		
		return null;
	}

	public static void removeInputType(IParam inputToRemove,
			ISmooksModelProvider smooksModelProvider) {
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

	public static List<IParam> getInputTypeList(SmooksModel resourceList) {
		List<IParam> inputs = new ArrayList<IParam>();
		GlobalParams globalParams = resourceList.getParams();
		
		for(IParam param : globalParams.getParams()) {
			if(InputSourceType.isValidName(param.getName())) {
				inputs.add(param);
			}
		}
		
		return inputs;
	}

	public static boolean isInputParamType(IParam param) {
		String type = param.getType();
		if (SmooksModelUtils.INPUT_ACTIVE_TYPE.equals(type)
				|| SmooksModelUtils.INPUT_DEACTIVE_TYPE.equals(type)) {
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
	
	public static EStructuralFeature getSelectorFeature(EObject model) {
		if (model == null)
			return null;
		//@DART
//		if (model instanceof Freemarker) {
//			return FreemarkerPackage.Literals.FREEMARKER__APPLY_ON_ELEMENT;
//		}
//
//		if (model instanceof ResourceConfigType) {
//			return SmooksPackage.Literals.RESOURCE_CONFIG_TYPE__SELECTOR;
//		}
//
//		if (model instanceof SmooksResourceListType) {
//			return SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR;
//		}
		if (model instanceof IBean) {
			return JavaBeanPackage.Literals.BEAN__CREATE_ON_ELEMENT;
		}
		if (model instanceof IWiring) {
			return JavaBeanPackage.Literals.WIRING__WIRE_ON_ELEMENT;
		}
		if (model instanceof IExpression) {
			return JavaBeanPackage.Literals.EXPRESSION__EXEC_ON_ELEMENT;
		}
		if (model instanceof IValue) {
			return JavaBeanPackage.Literals.VALUE__DATA;
		}
		return null;
	}
	
	private static String getRawAttributeName(String name) {
		if (isAttributeName(name)) {
			return name.trim().substring(1);
		}
		return name;
	}
	
	private static boolean isAttributeName(String name) {
		if (name == null)
			return false;
		return name.trim().startsWith("@"); //$NON-NLS-1$
	}
	
	private static IXMLStructuredObject localXMLNodeWithNodeName(String name, IXMLStructuredObject contextNode,
			Map<Object, Object> usedNodeMap) {
		if (name == null || contextNode == null)
			return null;
		String nodeName = contextNode.getNodeName();
		boolean isAttributeName = false;
		String tempName = name;
		if (isAttributeName(tempName)) {
			isAttributeName = true;
			tempName = getRawAttributeName(tempName);
		}
		boolean canCompare = true;
		if (isAttributeName) {
			if (!contextNode.isAttribute()) {
				canCompare = false;
			}
		}

		if (canCompare && tempName.equalsIgnoreCase(nodeName)) {
			return contextNode;
		}
		usedNodeMap.put(contextNode.getID(), new Object());
		List<?> children = contextNode.getChildren();
		IXMLStructuredObject result = null;
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject child = (IXMLStructuredObject) iterator.next();
			if (isAttributeName) {
				if (!child.isAttribute())
					continue;
			}
			if (tempName.equalsIgnoreCase(child.getNodeName())) {
				result = child;
				break;
			}
		}
		if (result == null) {
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				IXMLStructuredObject child = (IXMLStructuredObject) iterator.next();
				// to avoid the "died loop"
				if (usedNodeMap.get(child.getID()) != null) {
					continue;
				}
				try {
					result = localXMLNodeWithNodeName(name, child, usedNodeMap);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				if (result != null) {
					return result;
				}
			}
		}
		return result;
	}

	public static IXMLStructuredObject localXMLNodeWithNodeName(String name, IXMLStructuredObject contextNode) {
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		IXMLStructuredObject node = localXMLNodeWithNodeName(name, contextNode, map);
		map.clear();
		map = null;
		return node;
	}

	public static IXMLStructuredObject localXMLNodeWithPath(String path, IXMLStructuredObject contextNode) {
		if (path == null)
			return null;
		path = path.trim();
		String[] sperators = SELECTOR_SPERATORS;
		String sperator = null;
		boolean hasSperator = false;
		for (int i = 0; i < sperators.length; i++) {
			sperator = sperators[i];
			if (path.indexOf(sperator) != -1) {
				hasSperator = true;
				break;
			}
		}
		if (!hasSperator)
			sperator = null;
		return localXMLNodeWithPath(path, contextNode, sperator, true);
	}

	public static IXMLStructuredObject localXMLNodeWithPath(String path, IXMLStructuredObject contextNode,
			String sperator, boolean throwException) {
		if (contextNode == null || path == null)
			return null;
		if (sperator == null) {
			sperator = " "; //$NON-NLS-1$
		}
		if (path != null)
			path = path.trim();
		String[] pathes = path.split(sperator);
		if (pathes != null && pathes.length > 0 && path.length() != 0) {
			// to find the first node
			// first time , we search the node via context
			String firstNodeName = pathes[0];
			int index = 0;
			while (firstNodeName.length() == 0) {
				index++;
				firstNodeName = pathes[index];
			}
			IXMLStructuredObject firstModel = localXMLNodeWithNodeName(firstNodeName, contextNode);

			// if we can't find the node , to find it from the Root Parent node
			if (firstModel == null) {
				firstModel = localXMLNodeWithNodeName(firstNodeName, getRootParent(contextNode));
			}

			if (firstModel == null) {
				if (throwException)
					throw new RuntimeException("Can't find the node : " + firstNodeName); //$NON-NLS-1$
				else {
					return null;
				}
			}
			for (int i = index + 1; i < pathes.length; i++) {
				firstModel = getChildNodeWithName(pathes[i], firstModel);
				if (firstModel == null && throwException) {
					throw new RuntimeException("Can't find the node : " + pathes[i] + " from parent node " //$NON-NLS-1$ //$NON-NLS-2$
							+ pathes[i - 1]);
				}
			}

			return firstModel;
		}
		return null;
	}
	
	public static IXMLStructuredObject getChildNodeWithName(String name, IXMLStructuredObject parent) {
		if (parent == null)
			return null;
		String tempName = name;
		boolean isAttribute = false;
		if (isAttributeName(tempName)) {
			isAttribute = true;
			tempName = getRawAttributeName(tempName);
		}
		List<IXMLStructuredObject> children = parent.getChildren();
		if (children == null)
			return null;
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator.next();
			if (isAttribute) {
				if (!structuredObject.isAttribute())
					continue;
			}
			if (tempName.equalsIgnoreCase(structuredObject.getNodeName())) {
				return structuredObject;
			}
		}
		return null;
	}
	
	
	public static EStructuralFeature getBeanIDFeature(EObject model) {
		if (model == null) {
			return null;
		}
		//@DART
//		if (model instanceof BindTo) {
//			return FreemarkerPackage.Literals.BIND_TO__ID;
//		}

		// if (model instanceof BindingsType) {
		// return JavabeanPackage.Literals.BINDINGS_TYPE__BEAN_ID;
		// }

		if (model instanceof IBean) {
			return JavaBeanPackage.Literals.BEAN__BEAN_ID;
		}

		return null;
	}
	
	public static EStructuralFeature getBeanIDRefFeature(Object model) {
		if (model == null) {
			return null;
		}
		if (model instanceof IWiring) {
			return JavaBeanPackage.Literals.WIRING__BEAN_ID_REF;
		}
		return null;
	}
	
	public static IXMLStructuredObject getRootParent(IXMLStructuredObject child) {
		IXMLStructuredObject parent = child.getParent();
		if (child.isRootNode())
			return child;
		if (parent == null || parent.isRootNode())
			return child;
		IXMLStructuredObject temp = parent;
		while (temp != null && !temp.isRootNode()) {
			parent = temp;
			temp = temp.getParent();
		}
		return parent;
	}
	
	public static Collection<EObject> getBeanIdModelList(Object model) {
		List<EObject> beanIdModelList = new ArrayList<EObject>();
		fillBeanIdModelList((EObject)model, beanIdModelList);
		return beanIdModelList;
	}

	public static void fillBeanIdModelList(EObject model, final List<EObject> list) {
		EStructuralFeature beanIDFeature = getBeanIDFeature(model);
		if (beanIDFeature != null) {
			list.add(model);
		}
		List<EObject> children = model.eContents();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			EObject eObject = (EObject) iterator.next();
			fillBeanIdModelList(eObject, list);
		}
	}
	
	public static Collection<EObject> getBeanIdRefModelList(Object model) {
		List<EObject> beanIdRefModelList = new ArrayList<EObject>();
		fillBeanIdRefModelList(model, beanIdRefModelList);
		return beanIdRefModelList;
	}

	private static void fillBeanIdRefModelList(Object model, List beanIdRefModelList) {
		EStructuralFeature beanIDRefFeature = getBeanIDRefFeature(model);
		if (beanIDRefFeature != null) {
			beanIdRefModelList.add(model);
		}
		List<EObject> children = null;
		if(model instanceof EObject){
			children = ((EObject)model).eContents();
		}
		if(model instanceof SmooksModel){
			children = new ArrayList<EObject>();
			children.addAll(((SmooksModel)model).getComponents());
			children.add(((SmooksModel)model).getParams());
		}
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			EObject eObject = (EObject) iterator.next();
			fillBeanIdRefModelList(eObject, beanIdRefModelList);
		}
	}
	
	public static void expandGraphTree(List<?> expandNodes, TreeNodeEditPart rootEditPart) {
		for (Iterator<?> iterator = expandNodes.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			if (!(obj instanceof TreeNodeModel))
				continue;
			TreeNodeModel treeNodeModel = (TreeNodeModel) obj;
			TreeNodeModel parent = treeNodeModel;
			if (parent == null)
				continue;
			List<TreeNodeModel> parentList = new ArrayList<TreeNodeModel>();
			boolean canExpand = true;
			while (parent != rootEditPart.getModel()) {
				Object pa = parent.getParent();
				if (pa instanceof TreeNodeModel) {
					parent = (TreeNodeModel) pa;
				} else {
					canExpand = false;
					break;
				}
				if (parent == null) {
					canExpand = false;
					break;
				}
				parentList.add(parent);
			}
			if (!canExpand) {
				parentList.clear();
				parentList = null;
				continue;
			}
			if (parentList.isEmpty())
				continue;
			parentList.remove(parentList.size() - 1);
			((TreeNodeEditPart) rootEditPart).expandNode();
			TreeNodeEditPart tempEditPart = rootEditPart;
			for (int i = parentList.size() - 1; i >= 0; i--) {
				boolean expanded = false;
				TreeNodeModel parentNode = parentList.get(i);
				List<?> editParts = tempEditPart.getChildren();
				for (Iterator<?> iterator2 = editParts.iterator(); iterator2.hasNext();) {
					EditPart editPart = (EditPart) iterator2.next();
					if (editPart instanceof TreeNodeEditPart && editPart.getModel() == parentNode) {
						((TreeNodeEditPart) editPart).expandNode();
						tempEditPart = (TreeNodeEditPart) editPart;
						expanded = true;
						break;
					}
				}
				if (!expanded) {
					break;
				}
			}
		}
	}
	
	public static String generateFullPath(IXMLStructuredObject node, final String sperator) {
		return generatePath(node, getRootParent(node), sperator, true);
	}
	

	public static String generatePath(IXMLStructuredObject node, SelectorAttributes selectorAttributes) {
		String sperator = selectorAttributes.getSelectorSperator();
		String policy = selectorAttributes.getSelectorPolicy();
		if (sperator == null)
			sperator = " "; //$NON-NLS-1$
		if (policy == null)
			policy = SelectorAttributes.FULL_PATH;
		if (policy.equals(SelectorAttributes.FULL_PATH)) {
			return generateFullPath(node, sperator);
		}
		if (policy.equals(SelectorAttributes.INCLUDE_PARENT)) {
			return generatePath(node, node.getParent(), sperator, true);
		}
		if (policy.equals(SelectorAttributes.IGNORE_ROOT)) {

		}
		if (policy.equals(SelectorAttributes.ONLY_NAME)) {
			return node.getNodeName();
		}
		return generateFullPath(node, sperator);
	}

	public static String generatePath(IXMLStructuredObject startNode, IXMLStructuredObject stopNode,
			final String sperator, boolean includeContext) {
		String name = ""; //$NON-NLS-1$
		if (startNode == stopNode) {
			return startNode.getNodeName();
		}
		List<IXMLStructuredObject> nodeList = new ArrayList<IXMLStructuredObject>();
		IXMLStructuredObject temp = startNode;
		if (stopNode != null) {
			while (temp != stopNode.getParent() && temp != null) {
				nodeList.add(temp);
				temp = temp.getParent();
			}
		}
		int length = nodeList.size();
		if (!includeContext) {
			length--;
		}
		for (int i = 0; i < length; i++) {
			IXMLStructuredObject n = nodeList.get(i);
			String nodeName = n.getNodeName();
			if (n.isAttribute()) {
				nodeName = "@" + nodeName; //$NON-NLS-1$
			}
			name = sperator + nodeName + name;
		}
		return name.trim();
	}
	
	
	public static boolean isCollectionJavaGraphModel(EObject parent) {
		String classString = null;
		if (parent instanceof IBean) {
			classString = ((IBean) parent).getBeanClass();
		}
		if (classString != null)
			classString = classString.trim();

		IJavaProject project = SmooksUIUtils.getJavaProject(parent);
		if (project != null) {
			try {
				ProjectClassLoader loader = new ProjectClassLoader(project);
				Class<?> clazz = loader.loadClass(classString);
				if (Collection.class.isAssignableFrom(clazz)) {
					return true;
				}
			} catch (Throwable t) {
			}
		}

		return false;
	}

	public static boolean isArrayJavaGraphModel(EObject parent) {
		String classString = null;
		if (parent instanceof IBean) {
			classString = ((IBean) parent).getBeanClass();
		}
		if (classString != null) {
			classString = classString.trim();
			if (classString.endsWith("]")) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

	
	
	public static boolean isRelatedConnectionFeature(EStructuralFeature feature) {
		// for Bean ID
		//@DART
//		if (FreemarkerPackage.Literals.BIND_TO__ID == feature) {
//			return true;
//		}


		if (feature == JavaBeanPackage.Literals.BEAN__BEAN_ID) {
			return true;
		}

		if (JavaBeanPackage.Literals.WIRING__BEAN_ID_REF == feature) {
			return true;
		}

		// }
		
		//@DART
//		if (FreemarkerPackage.Literals.FREEMARKER__APPLY_ON_ELEMENT == feature) {
//			return true;
//		}
//		if (SmooksPackage.Literals.RESOURCE_CONFIG_TYPE__SELECTOR == feature) {
//			return true;
//		}
//		if (SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR == feature) {
//			return true;
//		}
		
		
		if (JavaBeanPackage.Literals.BEAN__CREATE_ON_ELEMENT == feature) {
			return true;
		}
		if (JavaBeanPackage.Literals.WIRING__WIRE_ON_ELEMENT == feature) {
			return true;
		}
		if (JavaBeanPackage.Literals.EXPRESSION__EXEC_ON_ELEMENT == feature) {
			return true;
		}
		if (JavaBeanPackage.Literals.VALUE__DATA == feature) {
			return true;
		}
		return false;
	}
	
	public static boolean isSmooksFile(IFile file) {
		if (file.getName().indexOf(".xml") != -1) //$NON-NLS-1$
			return true;
		IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
		IContentType[] types = contentTypeManager.findContentTypesFor(file.getName());
		for (IContentType contentType : types) {
			if (contentType.equals(contentTypeManager.getContentType("org.jboss.tools.smooks.ui.smooks.contentType"))) { //$NON-NLS-1$
				return true;
			}
			if (contentType.equals(contentTypeManager.getContentType("org.jboss.tools.smooks.ui.edimap.contentType"))) { //$NON-NLS-1$
				return true;
			}
			if (contentType
					.equals(contentTypeManager.getContentType("org.jboss.tools.smooks.ui.smooks1_0.contentType"))) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}
	
	public static String getDefualtDecoder(IValue value){
		IBean bean= (IBean)((EObject)value).eContainer();
		String clazzString = bean.getBeanClass();
		try {
			ProjectClassLoader loader = new ProjectClassLoader(SmooksUIUtils.getJavaProject(bean));
			Class<?> clazz = loader.loadClass(clazzString);
			Field field = clazz.getDeclaredField(((IValue)value).getProperty());
			if(field != null){
				Class<?> fieldType = field.getType();
				if(fieldType.isEnum()){
					return Messages.SmooksUIUtils_Enum;
				}
				if(fieldType == Integer.class || fieldType == int.class){
					return Messages.SmooksUIUtils_Integer;
				}
				if(fieldType == Float.class || fieldType == float.class){
					return Messages.SmooksUIUtils_Float;
				}
				if(fieldType == Double.class || fieldType == double.class){
					return Messages.SmooksUIUtils_Double;
				}
				if(fieldType == BigInteger.class ){
					return Messages.SmooksUIUtils_BigInteger;
				}
				if(fieldType == BigDecimal.class ){
					return Messages.SmooksUIUtils_BigDecimal;
				}
				if(fieldType == Long.class || fieldType == long.class){
					return Messages.SmooksUIUtils_Long;
				}
				if(fieldType == Boolean.class|| fieldType == boolean.class){
					return Messages.SmooksUIUtils_Boolean;
				}
				if(fieldType == Short.class|| fieldType == short.class){
					return Messages.SmooksUIUtils_Short;
				}
				if(fieldType == Byte.class|| fieldType == byte.class){
					return Messages.SmooksUIUtils_Byte;
				}
				if(fieldType == Short.class|| fieldType == short.class){
					return Messages.SmooksUIUtils_Short;
				}
				if(Calendar.class.isAssignableFrom(fieldType)){
					return Messages.SmooksUIUtils_Calendar;
				}
				if(fieldType == Class.class){
					return Messages.SmooksUIUtils_Class;
				}
				if(fieldType == Date.class){
					return Messages.SmooksUIUtils_Date;
				}
				if(fieldType == Character.class){
					return Messages.SmooksUIUtils_Char;
				}
				if(Charset.class.isAssignableFrom(fieldType)){
					return Messages.SmooksUIUtils_Charset;
				}
				if(fieldType == java.sql.Date.class){
					return Messages.SmooksUIUtils_SqlDate;
				}
				if(fieldType == java.sql.Time.class){
					return Messages.SmooksUIUtils_SqlTiem;
				}
				if(fieldType == URI.class){
					return Messages.SmooksUIUtils_URI;
				}
				if(fieldType == URL.class){
					return Messages.SmooksUIUtils_URL;
				}
			}
		} catch (Throwable e) {
			// ignore
		}
		return null;
	}
	
	public static List<IBean> getBeanTypeList(SmooksModel resourceList) {
		if (resourceList == null) {
			return null;
		}
		List<IComponent> rlist = resourceList.getComponents();
		List<IBean> beanIdList = new ArrayList<IBean>();
		for (Iterator<?> iterator = rlist.iterator(); iterator.hasNext();) {
			IComponent abstractResourceConfig = (IComponent) iterator.next();
			if (abstractResourceConfig instanceof IBean) {
				beanIdList.add((IBean) abstractResourceConfig);
			}
		}
		return beanIdList;
	}
}
