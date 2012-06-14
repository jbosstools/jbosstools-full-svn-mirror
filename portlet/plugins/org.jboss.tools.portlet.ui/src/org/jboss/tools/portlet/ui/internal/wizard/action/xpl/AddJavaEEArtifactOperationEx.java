package org.jboss.tools.portlet.ui.internal.wizard.action.xpl;

import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.CLASS_NAME;
import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.JAVA_PACKAGE;
import static org.eclipse.wst.common.componentcore.internal.operation.IArtifactEditOperationDataModelProperties.PROJECT_NAME;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.internal.common.operations.NewJavaEEArtifactClassOperation;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.jboss.tools.portlet.ui.PortletUIActivator;

public abstract class AddJavaEEArtifactOperationEx extends AbstractDataModelOperation {
	
	protected IModelProvider provider;
	
	public AddJavaEEArtifactOperationEx(IDataModel dataModel) {
		super(dataModel);
	}

	@Override
	public IStatus execute(final IProgressMonitor monitor, final IAdaptable info) throws ExecutionException {
		Runnable runnable = null;
		provider = getModelProvider();
		runnable = new Runnable(){
			public void run() {
				try {
					doExecute(monitor, info);
				} catch (ExecutionException e) {
					PortletUIActivator.log(e);
				}
			}
		};
		if (provider != null) {
		if (provider.validateEdit(null, null).isOK()) {
			
			provider.modify(runnable, null);
		}
		} else {
			runnable.run();
		}
		//return doExecute(monitor, info);
		return Status.CANCEL_STATUS;
	}
	
	private IModelProvider getModelProvider() {
		IProject project = getTargetProject();
		try {
			if (!FacetedProjectFramework.isFacetedProject(project)) {
				return null;
			}
		} catch (CoreException e1) {
			return null;
		}
		IModelProvider modelProvider = null;
		try {
			modelProvider = ModelProviderManager.getModelProvider(getTargetProject());
		} catch (Throwable e) {
			// ignore
		}
		return modelProvider;
	}

	public IStatus doExecute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		// create the java class
		createClass();
		return OK_STATUS;
	}
	
	protected String createClass() {
		// Create bean java class file using the NewBeanClassOperation.
		NewJavaEEArtifactClassOperation op = getNewClassOperation();
		try {
			IStatus status = op.execute(new NullProgressMonitor(), null);
			if (!status.isOK()) {
				PortletUIActivator.getDefault().getLog().log(status);
			}
		} catch (Exception e) {
			PortletUIActivator.log(e);
		}
		// Return the qualified class name of the newly created java class for
		// the bean
		return getQualifiedClassName();
	}
	
	protected abstract NewJavaEEArtifactClassOperation getNewClassOperation();

	public IProject getTargetProject() {
		String projectName = model.getStringProperty(PROJECT_NAME);
		return ProjectUtilities.getProject(projectName);
	}
	
	/**
	 * This method will return the qualified java class name as specified by the
	 * class name and package name properties in the data model. This method
	 * should not return null.
	 * 
	 * @see #CLASS_NAME
	 * @see #JAVA_PACKAGE
	 * 
	 * @return String qualified java class name
	 */
	public final String getQualifiedClassName() {
		// Use the java package name and unqualified class name to create a
		// qualified java class name
		String packageName = model.getStringProperty(JAVA_PACKAGE);
		String className = model.getStringProperty(CLASS_NAME);
		
		// Ensure the class is not in the default package before adding package
		// name to qualified name
		if (packageName != null && packageName.trim().length() > 0)
			return packageName + "." + className; //$NON-NLS-1$
		
		return className;
	}

}
