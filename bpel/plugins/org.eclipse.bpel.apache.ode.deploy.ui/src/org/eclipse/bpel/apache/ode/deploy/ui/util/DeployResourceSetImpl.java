/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.apache.ode.deploy.ui.util;

import org.eclipse.bpel.model.resource.BPELResourceSetImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Bob Brodt (bbrodt@redhat.com)
 * @date Aug 10, 2010
 */

@SuppressWarnings("nls")
public class DeployResourceSetImpl extends BPELResourceSetImpl {

	public DeployResourceSetImpl() {
		super();
	}

	@Override
	public Resource getResource(URI uri, boolean loadOnDemand) {
		String name = uri.toString().toLowerCase();
		if (name.endsWith("wsdl"))
			return getResource(uri,true,"wsdl");
		if (name.endsWith("wsil"))
			return getResource(uri,true,"wsil");
		if (name.endsWith(".xsd"))
			return getResource(uri,true,"xsd");
		if (name.endsWith(".bpel"))
			return getResource(uri,true,"bpel");
		return super.getResource(uri,loadOnDemand);
	}
}
