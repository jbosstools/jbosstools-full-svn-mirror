package org.jboss.tools.smooks.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.smooks.analyzer.CompositeResolveCommand;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalFactory;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.graphical.Params;
import org.jboss.tools.smooks.javabean.analyzer.JavaModelConnectionResolveCommand;
import org.jboss.tools.smooks.javabean.analyzer.JavaModelResolveCommand;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.SelectorAttributes;
import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.ViewerInitorStore;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;

/**
 * 
 * @author Dart
 * 
 */
public class UIUtils {

	public static final String[] SELECTORE_SPLITER = new String[] { "\\", //$NON-NLS-1$
			"/" }; //$NON-NLS-1$

	public static FillLayout createFillLayout(int marginW, int marginH) {
		FillLayout fill = new FillLayout();
		fill.marginHeight = marginH;
		fill.marginWidth = marginW;
		return fill;
	}

	public synchronized static void addResourceConfigType(EditingDomain domain,
			SmooksResourceListType list, ResourceConfigType obj) {
		Command addResourceConfigCommand = AddCommand.create(domain, list,
				SmooksPackage.eINSTANCE
						.getSmooksResourceListType_AbstractResourceConfig(),
				obj);
		addResourceConfigCommand.execute();
	}
	
	public static SelectorAttributes guessSelectorProperty(String selector,IXMLStructuredObject node){
		SelectorAttributes property = new SelectorAttributes();
		if(selector != null) selector.trim();
		boolean hasSperator = false;
		for (int i = 0; i < BeanPopulatorMappingAnalyzer.SELECTOR_SPERATORS.length; i++) {
			if(selector.indexOf(BeanPopulatorMappingAnalyzer.SELECTOR_SPERATORS[i]) != -1){
				property.setSelectorSperator(BeanPopulatorMappingAnalyzer.SELECTOR_SPERATORS[i]);
				hasSperator = true;
				break;
			}
		}
		
		if(!hasSperator){
			property.setSelectorPolicy(SelectorAttributes.ONLY_NAME);
			return property;
		}
		String[] nodeNames = selector.split(property.getSelectorSperator());
		IXMLStructuredObject parent = node;
		for(int i = 1 ; i < nodeNames.length ; i++){
			parent = parent.getParent();
		}
		IXMLStructuredObject rootNode = getRootParent(node);
		if(parent == rootNode){
			property.setSelectorPolicy(SelectorAttributes.FULL_PATH);
			return property;
		}
		if(parent == node.getParent()){
			property.setSelectorPolicy(SelectorAttributes.INCLUDE_PARENT);
		}else{
			property.setSelectorPolicy(SelectorAttributes.IGNORE_ROOT);
		}
		return property;
	}

	public static AbstractXMLObject getRootTagXMLObject(AbstractXMLObject xmlObj) {
		if (xmlObj == null)
			return null;
		AbstractXMLObject parent = xmlObj.getParent();
		if (parent == null)
			return null;
		if (parent instanceof TagList)
			return xmlObj;
		while (true) {
			AbstractXMLObject p = parent.getParent();
			if (p instanceof TagList)
				break;
			parent = p;
		}
		return parent;
	}

	public static void checkSelector(String selector)
			throws InvocationTargetException {
		if (selector == null)
			return;
		for (int i = 0; i < SELECTORE_SPLITER.length; i++) {
			String splitString = SELECTORE_SPLITER[i];
			if (selector.indexOf(splitString) != -1) {
				throw new InvocationTargetException(
						new Exception(
								NLS.bind(Messages.UIUtils_SelectorCheckErrorMessage,  //$NON-NLS-1$
												splitString,
												selector)));
			}
		}
	}

	public static void checkSelector(SmooksResourceListType listType)
			throws InvocationTargetException {
		List<AbstractResourceConfig> lists = listType
				.getAbstractResourceConfig();
		for (Iterator<AbstractResourceConfig> iterator = lists.iterator(); iterator
				.hasNext();) {
			AbstractResourceConfig resourceConfig1 = (AbstractResourceConfig) iterator
					.next();
			ResourceConfigType resourceConfig = null;
			if (resourceConfig1 instanceof ResourceConfigType) {
				resourceConfig = (ResourceConfigType) resourceConfig1;
			} else {
				continue;
			}
			String selector = resourceConfig.getSelector();
			UIUtils.checkSelector(selector);
			List<Object> list = SmooksModelUtils
					.getBindingListFromResourceConfigType(resourceConfig);
			if (list == null)
				continue;
			for (Iterator<Object> iterator2 = list.iterator(); iterator2
					.hasNext();) {
				AnyType binding = (AnyType) iterator2.next();
				String bindingMessage = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_SELECTOR);
				UIUtils.checkSelector(bindingMessage);
			}
		}
	}

	public static void assignConnectionPropertyToBinding(
			LineConnectionModel connection, AnyType binding,
			String[] ignorePropertiesName) {
		Object[] bindingPros = connection.getPropertyArray();
		for (int i = 0; i < bindingPros.length; i++) {
			PropertyModel property = (PropertyModel) bindingPros[i];
			boolean ignore = false;
			String pname = property.getName();
			for (int j = 0; j < ignorePropertiesName.length; j++) {
				String ignoreName = ignorePropertiesName[j];
				if (pname.equals(ignoreName)) {
					ignore = true;
					break;
				}
			}
			if (ignore)
				continue;
			Object obj = property.getValue();
			if (obj != null) {
				String pvalue = obj.toString();
				binding.getAnyAttribute().add(
						ExtendedMetaData.INSTANCE.demandFeature(null, pname,
								false), pvalue);
			}
		}
	}

	public static void assignBindingPropertyToMappingModel(AnyType binding,
			MappingModel model, Object[] ignoreProperties) {
		FeatureMap it = binding.getAnyAttribute();
		for (int i = 0; i < it.size(); i++) {
			EStructuralFeature feature = it.getEStructuralFeature(i);
			boolean ignore = false;
			for (int j = 0; j < ignoreProperties.length; j++) {
				Object ignoreProperty = ignoreProperties[j];
				if (feature.equals(ignoreProperty)) {
					ignore = true;
					break;
				}
			}
			if (ignore) {
				continue;
			}
			String pname = feature.getName();
			String pvalue = it.get(feature, false).toString();
			PropertyModel pmodel = new PropertyModel();
			pmodel.setName(pname);
			pmodel.setValue(pvalue);
			model.getProperties().add(pmodel);
		}
	}

	public static boolean isInstanceCreatingConnection(
			LineConnectionModel connection) {
		AbstractStructuredDataModel sourceModel = (AbstractStructuredDataModel) connection
				.getSource();
		AbstractStructuredDataModel targetModel = (AbstractStructuredDataModel) connection
				.getTarget();
		Object target = targetModel.getReferenceEntityModel();
		Object source = sourceModel.getReferenceEntityModel();
		return isInstanceCreatingConnection(source, target);
	}

	public static boolean isInstanceCreatingConnection(Object sourceModel,
			Object targetModel) {
		if (targetModel != null) {
			if (targetModel instanceof JavaBeanModel) {
				if (sourceModel instanceof TagObject) {
					return !((JavaBeanModel) targetModel).isPrimitive();
				}
				if (sourceModel instanceof JavaBeanModel) {
					return !((JavaBeanModel) targetModel).isPrimitive();
				}
			}
		}
		return false;
	}

	public static void createJavaModelConnectionErrorResolveCommand(
			DesignTimeAnalyzeResult result,
			SmooksConfigurationFileGenerateContext context,
			JavaBeanModel currentNode, JavaBeanModel parentNode) {
		GraphRootModel root = context.getGraphicalRootModel();
		HashMap<AbstractStructuredDataModel, AbstractStructuredDataModel> tempMap = new HashMap<AbstractStructuredDataModel, AbstractStructuredDataModel>();
		// Disconnect all connections command
		JavaModelConnectionResolveCommand disconnectCommand = new JavaModelConnectionResolveCommand(
				context);
		CompositeResolveCommand compositeCommand = new CompositeResolveCommand(
				context);
		compositeCommand.setResolveDescription(Messages.UIUtils_ConnectAllConnections); //$NON-NLS-1$
		disconnectCommand.setResolveDescription(NLS.bind(Messages.UIUtils_DisconnectAllConnections, //$NON-NLS-1$
						currentNode.getName()));
		AbstractStructuredDataModel targetNode = UIUtils.findGraphModel(root,
				currentNode);
		if (targetNode instanceof IConnectableModel) {
			List<Object> connections = ((IConnectableModel) targetNode)
					.getModelTargetConnections();
			for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
				LineConnectionModel line = (LineConnectionModel) iterator
						.next();
				AbstractStructuredDataModel source = (AbstractStructuredDataModel) line
						.getSource();
				Object sourceBean = (Object) source.getReferenceEntityModel();
				Object sourceParent = context.getSourceViewerProvider()
						.getParent(sourceBean);
				if (sourceParent == null) {
					sourceParent = sourceBean;
				}
				AbstractStructuredDataModel sourceParentNode = UIUtils
						.findGraphModel(root, sourceParent);
				// Connect the parent command
				AbstractStructuredDataModel targetParentNode = UIUtils
						.findGraphModel(root, parentNode);
				if (tempMap.get(sourceParentNode) == null) {
					JavaModelConnectionResolveCommand connectParent = new JavaModelConnectionResolveCommand(
							context);
					String desc = NLS.bind(Messages.UIUtils_ConnectNode, //$NON-NLS-1$
									context.getSourceViewerLabelProvider().getText(
											sourceParent),
									parentNode.getName());
					connectParent.setResolveDescription(desc);
					connectParent.setSourceModel(sourceParentNode);
					connectParent.setTargetModel(targetParentNode);
					result.addResolveCommand(connectParent);
					tempMap.put(sourceParentNode, targetParentNode);
					compositeCommand.addCommand(connectParent);
				}

				disconnectCommand.addDisconnectionModel(line);
			}
		}
		result.addResolveCommand(disconnectCommand);
		if (!compositeCommand.isEmpty()) {
			result.addResolveCommand(compositeCommand);
		}
	}

	public static List<DesignTimeAnalyzeResult> checkJavaModelNodeConnection(
			SmooksConfigurationFileGenerateContext context) {
		GraphRootModel root = context.getGraphicalRootModel();
		List targetList = root.loadTargetModelList();
		List<DesignTimeAnalyzeResult> arList = new ArrayList<DesignTimeAnalyzeResult>();
		for (Iterator iterator = targetList.iterator(); iterator.hasNext();) {
			AbstractStructuredDataModel targetm = (AbstractStructuredDataModel) iterator
					.next();
			if (targetm instanceof IConnectableModel) {
				if (((IConnectableModel) targetm).getModelTargetConnections()
						.isEmpty()) {
					continue;
				}
				if (!(targetm.getReferenceEntityModel() instanceof JavaBeanModel)) {
					continue;
				}
				JavaBeanModel javaModel = (JavaBeanModel) targetm
						.getReferenceEntityModel();
				JavaBeanModel parent = javaModel.getParent();
				if (parent != null) {
					AbstractStructuredDataModel pgm = UIUtils.findGraphModel(
							root, parent);
					if (pgm != null && pgm instanceof IConnectableModel) {
						if (((IConnectableModel) pgm)
								.getModelTargetConnections().isEmpty()) {
							String errorMessage = NLS.bind(Messages.UIUtils_ParentNodeConnectErrorMessage, //$NON-NLS-1$
											javaModel.getName(),
											parent.getName());
							DesignTimeAnalyzeResult dr = new DesignTimeAnalyzeResult();
							dr.setErrorMessage(errorMessage);
							createJavaModelConnectionErrorResolveCommand(dr,
									context, javaModel, parent);
							arList.add(dr);
						}
					}
				}
			}
		}
		return arList;
	}

	public static List<DesignTimeAnalyzeResult> checkTargetJavaModelType(
			SmooksConfigurationFileGenerateContext context) {
		GraphRootModel root = context.getGraphicalRootModel();
		List<DesignTimeAnalyzeResult> resultList = new ArrayList<DesignTimeAnalyzeResult>();
		List<TargetModel> targetList = root.loadTargetModelList();
		for (Iterator<TargetModel> iterator = targetList.iterator(); iterator
				.hasNext();) {
			TargetModel targetModel = (TargetModel) iterator.next();
			Object refObj = targetModel.getReferenceEntityModel();
			if (refObj instanceof JavaBeanModel) {
				if (!targetModel.getModelTargetConnections().isEmpty()) {
					String instanceName = ((JavaBeanModel) refObj)
							.getBeanClassString();
					Class instanceClazz = null;
					if (((JavaBeanModel) refObj).isPrimitive()
							|| ((JavaBeanModel) refObj).isArray()) {
						// TODO process primitive type
						continue;
					}
					String errorMessage = ""; //$NON-NLS-1$
					try {
						ClassLoader loader = newProjectClassLoader(context
								.getSmooksConfigFile());
						instanceClazz = loader.loadClass(instanceName);
					} catch (Exception e) {
						errorMessage = e.toString();
					}
					if (instanceClazz == null) {
						DesignTimeAnalyzeResult result = new DesignTimeAnalyzeResult();
						result
								.setErrorMessage(NLS.bind(Messages.UIUtils_InstanceLoadedErrorMessage, //$NON-NLS-1$
												((JavaBeanModel) refObj).getName(),
												instanceName));
						JavaModelResolveCommand command = new JavaModelResolveCommand(
								context);
						command
								.setResolveDescription(NLS.bind(Messages.UIUtils_InstanceLoadedResolveMessage, //$NON-NLS-1$
												instanceName));
						command.setInstanceName(instanceName);
						command.setJavaBean((JavaBeanModel) refObj);
						result.addResolveCommand(command);
						resultList.add(result);
					}
					if (instanceClazz != null && instanceClazz.isInterface()) {
						DesignTimeAnalyzeResult result = new DesignTimeAnalyzeResult();
						result
								.setErrorMessage(NLS.bind(Messages.UIUtils_JavaModelLoadedErrorMessage, //$NON-NLS-1$
												((JavaBeanModel) refObj).getName(),
												instanceName));
						if (List.class.isAssignableFrom(instanceClazz)) {
							JavaModelResolveCommand command = new JavaModelResolveCommand(
									context);
							command
									.setResolveDescription(Messages.UIUtils_InstanceClassResolveMessage1); //$NON-NLS-1$
							command
									.setInstanceName(Messages.UIUtils_InstanceClassResolveMessage2); //$NON-NLS-1$
							command.setJavaBean((JavaBeanModel) refObj);
							result.addResolveCommand(command);
						}
						resultList.add(result);
					}
				}
			}
		}
		return resultList;
	}

	public static ClassLoader newProjectClassLoader(IFile file)
			throws Exception {
		IProject p = file.getProject();
		IJavaProject jp = JavaCore.create(p);
		ProjectClassLoader loader = new ProjectClassLoader(jp);
		return loader;
	}

	public static Status createErrorStatus(Throwable throwable, String message) {
		while (throwable != null
				&& throwable instanceof InvocationTargetException) {
			throwable = ((InvocationTargetException) throwable)
					.getTargetException();
		}
		return new Status(Status.ERROR, SmooksUIActivator.PLUGIN_ID, message,
				throwable);
	}

	public static void showErrorDialog(Shell shell, Status status) {
		ErrorDialog.openError(shell, "Error", "error", status); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static Status createErrorStatus(Throwable throwable) {
		return createErrorStatus(throwable, "Error"); //$NON-NLS-1$
	}

	public static FillLayout createFormCompositeFillLayout() {
		return createFillLayout(1, 1);
	}

	public static boolean hasSourceConnectionModel(
			AbstractStructuredDataModel model) {
		if (model instanceof IConnectableModel) {
			return !((IConnectableModel) model).getModelSourceConnections()
					.isEmpty();
		}
		return false;
	}

	public static boolean hasTargetConnectionModel(
			AbstractStructuredDataModel model) {
		if (model instanceof IConnectableModel) {
			return !((IConnectableModel) model).getModelTargetConnections()
					.isEmpty();
		}
		return false;
	}

	public static LineConnectionModel getFirstSourceModelViaConnection(
			AbstractStructuredDataModel target) {
		if (target == null)
			return null;
		if (target instanceof IConnectableModel) {
			List list = ((IConnectableModel) target)
					.getModelSourceConnections();
			if (list.isEmpty())
				return null;
			// get the first connection
			return (LineConnectionModel) list.get(0);
		}
		return null;
	}

	public static LineConnectionModel getFirstTargetModelViaConnection(
			AbstractStructuredDataModel source) {
		if (source == null)
			return null;
		if (source instanceof IConnectableModel) {
			List list = ((IConnectableModel) source)
					.getModelTargetConnections();
			if (list.isEmpty())
				return null;
			// get the first connection
			return (LineConnectionModel) list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param parent
	 * @param referenceObject
	 * @return
	 */
	public static AbstractStructuredDataModel findGraphModel(
			AbstractStructuredDataModel parent, Object referenceObject) {
		if (referenceObject == null || parent == null)
			return null;
		Object ref = parent.getReferenceEntityModel();
		if (referenceObject == ref)
			return parent;
		List list = parent.getChildren();
		if (list == null)
			return null;
		if (list.isEmpty())
			return null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AbstractStructuredDataModel child = (AbstractStructuredDataModel) iterator
					.next();
			AbstractStructuredDataModel obj = findGraphModel(child,
					referenceObject);
			if (obj != null)
				return obj;
		}
		return null;
	}

	public static IJavaProject getJavaProjectFromEditorPart(IEditorPart part) {
		IEditorInput input = part.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			IProject project = file.getProject();
			return JavaCore.create(project);

		}
		return null;
	}

	public static GridLayout createGeneralFormEditorLayout(int columns) {
		GridLayout layout = new GridLayout();
		layout.numColumns = columns;
		layout.marginHeight = 13;
		return layout;
	}

	public static boolean setTheProvidersForTreeViewer(TreeViewer viewer,
			String dataTypeID) {
		if (dataTypeID == null || viewer == null)
			return false;
		ILabelProvider lprovider = ViewerInitorStore.getInstance()
				.getLabelProvider(dataTypeID);
		ITreeContentProvider tprovider = ViewerInitorStore.getInstance()
				.getTreeCotentProvider(dataTypeID);
		if (tprovider == null)
			return false;
		viewer.setLabelProvider(new DecoratingLabelProvider(lprovider,
				SmooksUIActivator.getDefault().getWorkbench()
						.getDecoratorManager().getLabelDecorator()));
		viewer.setContentProvider(tprovider);
		return true;
	}

	public static IXMLStructuredObject localXMLNodeWithNodeName(String name,
			IXMLStructuredObject contextNode) {
		HashMap map = new HashMap();
		IXMLStructuredObject node = localXMLNodeWithNodeName(name, contextNode,
				map);
		map.clear();
		map = null;
		return node;
	}

	private static boolean isAttributeName(String name) {
		if (name == null)
			return false;
		return name.trim().startsWith("@");
	}

	private static String getRawAttributeName(String name) {
		if (isAttributeName(name)) {
			return name.trim().substring(1);
		}
		return name;
	}

	private static IXMLStructuredObject localXMLNodeWithNodeName(String name,
			IXMLStructuredObject contextNode, HashMap usedNodeMap) {
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
		List children = contextNode.getChildren();
		IXMLStructuredObject result = null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
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
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				IXMLStructuredObject child = (IXMLStructuredObject) iterator
						.next();
				// to avoid the "died loop"
				if (usedNodeMap.get(child.getID()) != null) {
					continue;
				}
				result = localXMLNodeWithNodeName(name, child, usedNodeMap);
				if (result != null) {
					return result;
				}
			}
		}
		return result;
	}

	public static IXMLStructuredObject getRootParent(IXMLStructuredObject child) {
		IXMLStructuredObject parent = child.getParent();
		if(child.isRootNode()) return child;
		if (parent == null || parent.isRootNode())
			return child;
		IXMLStructuredObject temp = parent;
		while (temp != null && !temp.isRootNode()) {
			parent = temp;
			temp = temp.getParent();
		}
		return parent;
	}
	
	public static String generatePath(IXMLStructuredObject node,SelectorAttributes selectorAttributes){
		String sperator = selectorAttributes.getSelectorSperator();
		String policy = selectorAttributes.getSelectorPolicy();
		if(sperator == null) sperator = " ";
		if(policy == null) policy = SelectorAttributes.FULL_PATH;
		if(policy.equals(SelectorAttributes.FULL_PATH)){
			return generateFullPath(node, sperator);
		}
		if(policy.equals(SelectorAttributes.INCLUDE_PARENT)){
			return generatePath(node, node.getParent(),sperator,true);
		}
		if(policy.equals(SelectorAttributes.IGNORE_ROOT)){
			
		}
		if(policy.equals(SelectorAttributes.ONLY_NAME)){
			return node.getNodeName();
		}
		return generateFullPath(node,sperator);
	}
	
	public static String generateFullPath(IXMLStructuredObject node,final String sperator){
		return generatePath(node, getRootParent(node), sperator, true);
	}

	public static String generatePath(IXMLStructuredObject startNode,
			IXMLStructuredObject stopNode, final String sperator,
			boolean includeContext) {
		String name = "";
		if(startNode == stopNode){
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
		if(!includeContext){
			length--;
		}
		for (int i =0; i < length; i++) {
			IXMLStructuredObject n = nodeList.get(i);
			String nodeName = n.getNodeName();
			if(n.isAttribute()){
				nodeName = "@" + nodeName;
			}
			name = sperator + nodeName  + name;
		}
		return name.trim();
	}

	public static IXMLStructuredObject getChildNodeWithName(String name,
			IXMLStructuredObject parent) {
		String tempName = name;
		boolean isAttribute = false;
		if (isAttributeName(tempName)) {
			isAttribute = true;
			tempName = getRawAttributeName(tempName);
		}
		List<IXMLStructuredObject> children = parent.getChildren();
		if (children == null)
			return null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator
					.next();
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
	
	public static void removeParamToGraphModel(GraphInformations graph,String paramName){
		Params params = graph.getParams();
		if(params == null){
			return;
		}
		List<Param> paramList = params.getParam();
		Param p = null;
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			Param param = (Param) iterator.next();
			if(paramName.equalsIgnoreCase(param.getName())){
				p = param;
				break;
			}
		}
		if(p == null){
			return;
		}
		params.getParam().remove(p);
	}
	
	public static String getParamToGraphModel(GraphInformations graph,String paramName){
		Params params = graph.getParams();
		if(params == null){
			return null;
		}
		List<Param> paramList = params.getParam();
		Param p = null;
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			Param param = (Param) iterator.next();
			if(paramName.equalsIgnoreCase(param.getName())){
				p = param;
				break;
			}
		}
		if(p == null){
			return null;
		}
		return p.getValue();
	}

	public static void addParamToGraphModel(GraphInformations graph,String paramName,String paramValue){
		Params params = graph.getParams();
		if(params == null){
			params = GraphicalFactory.eINSTANCE.createParams();
			graph.setParams(params);
		}
		List<Param> paramList = params.getParam();
		Param p = null;
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			Param param = (Param) iterator.next();
			if(paramName.equalsIgnoreCase(param.getName())){
				p = param;
				break;
			}
		}
		if(p == null){
			p =  GraphicalFactory.eINSTANCE.createParam();
			p.setName(paramName);
			params.getParam().add(p);
		}
		p.setValue(paramValue);
	}

	public static IXMLStructuredObject localXMLNodeWithPath(String path,
			IXMLStructuredObject contextNode) {
		if(path == null) return null;
		path = path.trim();
		String[] sperators = BeanPopulatorMappingAnalyzer.SELECTOR_SPERATORS;
		String sperator = null;
		boolean hasSperator = false;
		for (int i = 0; i < sperators.length; i++) {
			 sperator = sperators[i];
			if(path.indexOf(sperator) != -1){
				hasSperator = true;
				break;
			}
		}
		if(!hasSperator) sperator = null;
		return localXMLNodeWithPath(path, contextNode, sperator, true);
	}

	public static IXMLStructuredObject localXMLNodeWithPath(String path,
			IXMLStructuredObject contextNode, String sperator,
			boolean throwException) {
		if (contextNode == null || path == null)
			return null;
		if (sperator == null) {
			sperator = " ";
		}
		if (path != null)
			path = path.trim();
		String[] pathes = path.split(sperator);
		if (pathes != null && pathes.length > 0) {
			// to find the first node
			// first time , we search the node via context
			String firstNodeName = pathes[0];
			int index = 0;
			while(firstNodeName.length() == 0){
				index ++;
				firstNodeName = pathes[index];
			}
			IXMLStructuredObject firstModel = localXMLNodeWithNodeName(
					firstNodeName, contextNode);

			// if we can't find the node , to find it from the Root Parent node
			if (firstModel == null) {
				firstModel = localXMLNodeWithNodeName(firstNodeName,
						getRootParent(contextNode));
			}

			if (firstModel == null) {
				if (throwException)
					throw new RuntimeException("Can't find the node : "
							+ firstNodeName);
				else {
					return null;
				}
			}
			for (int i = index + 1; i < pathes.length; i++) {
				firstModel = getChildNodeWithName(pathes[i], firstModel);
				if (firstModel == null && throwException) {
					throw new RuntimeException("Can't find the node : "
							+ pathes[i] + " from parent node " + pathes[i - 1]);
				}
			}

			return firstModel;
		}
		return null;
	}
}
