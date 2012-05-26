package org.jboss.tools.openshift.express.internal.ui.common;

import java.io.IOException;

import org.jboss.tools.openshift.express.client.OpenShiftException;

public class WontOverwriteException extends OpenShiftException {

	public WontOverwriteException(String bind) {
		super(bind);
	}

}
