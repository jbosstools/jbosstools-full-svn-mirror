package org.jboss.tools.smooks.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
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
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.ViewerInitorStore;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 * 
 * @author Dart
 * 
 */
public class UIUtils {

	public static FillLayout createFillLayout(int marginW, int marginH) {
		FillLayout fill = new FillLayout();
		fill.marginHeight = marginH;
		fill.marginWidth = marginW;
		return fill;
	}
	
	public static Status createErrorStatus(Throwable throwable,String message){
		while(throwable != null && throwable instanceof InvocationTargetException){
			throwable = ((InvocationTargetException)throwable).getTargetException();
		}
		return new Status(Status.ERROR,SmooksUIActivator.PLUGIN_ID,message,throwable);
	}
	
	public static void showErrorDialog(Shell shell,Status status){
		ErrorDialog.openError(shell, "Error", "error", status);
	}
	
	public static Status createErrorStatus(Throwable throwable){
		return createErrorStatus(throwable, "Error");
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
		if(target == null) return null;
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
		if(source == null) return null;
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
		viewer.setLabelProvider(new DecoratingLabelProvider(lprovider, null));
		viewer.setContentProvider(tprovider);
		return true;
	}
}
