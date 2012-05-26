package org.jboss.tools.openshift.express.internal.ui.common;


import org.jboss.tools.openshift.express.client.OpenShiftException;

public class MavenImportFailedException extends OpenShiftException {

	public MavenImportFailedException(String bind) {
		super(bind);
	}
}
