/**
 * 
 */
package org.jboss.tools.openshift.express.internal.ui.wizard.application;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.openshift.express.internal.core.connection.Connection;

import com.openshift.client.IApplication;

/**
 * @author Xavier Coulon
 *
 */
public class ImportOpenShiftExpressApplicationWizard extends OpenShiftExpressApplicationWizard {

	/**
	 * Constructor
	 */
	public ImportOpenShiftExpressApplicationWizard() {
		super(true, "Import OpenShift Application");
	}

	/**
	 * Constructor
	 * @param user
	 * @param project
	 * @param application
	 */
	public ImportOpenShiftExpressApplicationWizard(Connection user, IProject project, IApplication application) {
		super(user, project, application, true, "Import OpenShift Application");
	}
}
