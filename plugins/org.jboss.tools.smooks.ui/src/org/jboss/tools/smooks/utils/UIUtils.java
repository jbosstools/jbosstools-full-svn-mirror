package org.jboss.tools.smooks.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.smooks.analyzer.CompositeResolveCommand;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.javabean.analyzer.JavaModelConnectionResolveCommand;
import org.jboss.tools.smooks.javabean.analyzer.JavaModelResolveCommand;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
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
import org.jboss.tools.smooks.xml.model.DocumentObject;
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

	public static AbstractXMLObject getRootTagXMLObject(AbstractXMLObject xmlObj) {
		AbstractXMLObject parent = xmlObj.getParent();
		while (true) {
			AbstractXMLObject p = parent.getParent();
			if (p == null)
				break;
			parent = p;
		}
		if (parent instanceof DocumentObject) {
			parent = ((DocumentObject) parent).getRootTag();
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
								Messages
										.getString("UIUtils.SelectorCheckErrorMessage1") + splitString //$NON-NLS-1$
										+ Messages
												.getString("UIUtils.SelectorCheckErrorMessage2") + selector + "\"")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	public static void checkSelector(SmooksResourceListType listType)
			throws InvocationTargetException {
		List<AbstractResourceConfig> lists = listType
				.getAbstractResourceConfig();
		for (Iterator<AbstractResourceConfig> iterator = lists.iterator(); iterator
				.hasNext();) {
			ResourceConfigType resourceConfig = (ResourceConfigType) iterator
					.next();
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
			String pvalue = property.getValue();
			binding.getAnyAttribute()
					.add(
							ExtendedMetaData.INSTANCE.demandFeature(null,
									pname, false), pvalue);
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
		compositeCommand.setResolveDescription(Messages
				.getString("UIUtils.ConnectAllConnections")); //$NON-NLS-1$
		disconnectCommand.setResolveDescription(Messages
				.getString("UIUtils.DisconnectAllConnections") //$NON-NLS-1$
				+ currentNode.getName() + Messages.getString("UIUtils.Node")); //$NON-NLS-1$
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
					connectParent.setResolveDescription(Messages
							.getString("UIUtils.ConnectNode1") //$NON-NLS-1$
							+ context.getSourceViewerLabelProvider().getText(
									sourceParent)
							+ Messages.getString("UIUtils.ConnectNode2") //$NON-NLS-1$
							+ parentNode.getName() + "\""); //$NON-NLS-1$
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
							String errorMessage = Messages
									.getString("UIUtils.ParentNodeConnectErrorMessage1") //$NON-NLS-1$
									+ javaModel.getName()
									+ "\" : \"" //$NON-NLS-1$
									+ parent.getName()
									+ Messages
											.getString("UIUtils.ParentNodeConnectErrorMessage2"); //$NON-NLS-1$
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
								.setErrorMessage(Messages
										.getString("UIUtils.InstanceLoadedErrorMessage1") //$NON-NLS-1$
										+ ((JavaBeanModel) refObj).getName()
										+ Messages
												.getString("UIUtils.InstanceLoadedErrorMessage2") //$NON-NLS-1$
										+ instanceName + "\""); //$NON-NLS-1$
						JavaModelResolveCommand command = new JavaModelResolveCommand(
								context);
						command
								.setResolveDescription(Messages
										.getString("UIUtils.InstanceLoadedResolveMessage1") //$NON-NLS-1$
										+ ((JavaBeanModel) refObj)
												.getBeanClass()
												.getCanonicalName() + "\""); //$NON-NLS-1$
						command.setInstanceName(((JavaBeanModel) refObj)
								.getBeanClass().getCanonicalName());
						command.setJavaBean((JavaBeanModel) refObj);
						result.addResolveCommand(command);
						resultList.add(result);
					}
					if (instanceClazz != null && instanceClazz.isInterface()) {
						DesignTimeAnalyzeResult result = new DesignTimeAnalyzeResult();
						result
								.setErrorMessage(Messages
										.getString("UIUtils.JavaModelLoadedErrorMessage1") //$NON-NLS-1$
										+ ((JavaBeanModel) refObj).getName()
										+ Messages
												.getString("UIUtils.JavaModelLoadedErrorMessage2") //$NON-NLS-1$
										+ instanceName + "\""); //$NON-NLS-1$
						if (List.class.isAssignableFrom(instanceClazz)) {
							JavaModelResolveCommand command = new JavaModelResolveCommand(
									context);
							command
									.setResolveDescription(Messages
											.getString("UIUtils.InstanceClassResolveMessage1")); //$NON-NLS-1$
							command
									.setInstanceName(Messages
											.getString("UIUtils.InstanceClassResolveMessage2")); //$NON-NLS-1$
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
}
