/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.wtp.core.vcf;

import org.eclipse.wst.common.componentcore.internal.ReferencedComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;

public interface IReferenceResolver {
	public boolean canResolve(IVirtualComponent context, ReferencedComponent referencedComponent);
	public IVirtualReference resolve(IVirtualComponent context, ReferencedComponent referencedComponent);
	public boolean canResolve(IVirtualReference reference);
	public ReferencedComponent resolve(IVirtualReference reference);
}
