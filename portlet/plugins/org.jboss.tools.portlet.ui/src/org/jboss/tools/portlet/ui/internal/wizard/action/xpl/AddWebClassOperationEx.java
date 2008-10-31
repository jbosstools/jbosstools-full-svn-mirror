package org.jboss.tools.portlet.ui.internal.wizard.action.xpl;

import static org.eclipse.jst.j2ee.application.internal.operations.IAnnotationsDataModel.USE_ANNOTATIONS;
import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.CLASS_NAME;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.USE_EXISTING_CLASS;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.j2ee.internal.web.operations.AddFilterOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public abstract class AddWebClassOperationEx extends AddJavaEEArtifactOperationEx {
	
	public AddWebClassOperationEx(IDataModel dataModel) {
		super(dataModel);
	}

	/**
	 * Subclasses may extend this method to add their own actions during
	 * execution. The implementation of the execute method drives the running of
	 * the operation. This implementation will create the filter class, and
	 * then it will create the filter metadata for the web deployment descriptor. 
	 * This method will accept null as a parameter.
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 * @see AddFilterOperation#createFilterClass()
	 * @see AddFilterOperation#generateFilterMetaData(NewFilterClassDataModel,
	 *      String)
	 * 
	 * @param monitor
	 *            IProgressMonitor
	 * @param info
	 *            IAdaptable
	 * @throws CoreException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	@Override
	public IStatus doExecute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		
	    boolean useExisting = model.getBooleanProperty(USE_EXISTING_CLASS);
		String qualifiedClassName = model.getStringProperty(CLASS_NAME);

		// create the java class
		if (!useExisting) 
			qualifiedClassName = createClass();

		// If the filter is not annotated, generate the filter metadata for the DD
		if (!model.getBooleanProperty(USE_ANNOTATIONS))
			generateMetaData(model, qualifiedClassName);
		
		return OK_STATUS;
	}
	
	protected abstract void generateMetaData(IDataModel aModel, String qualifiedClassName);

}
