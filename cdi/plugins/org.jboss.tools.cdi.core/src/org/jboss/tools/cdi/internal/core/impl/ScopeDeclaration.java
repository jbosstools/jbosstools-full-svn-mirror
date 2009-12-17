/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.internal.core.impl;

import org.jboss.tools.cdi.core.IScope;
import org.jboss.tools.cdi.core.IScopeDeclaration;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ScopeDeclaration extends AnnotationDeclaration implements IScopeDeclaration {

	public ScopeDeclaration() {}

	public ScopeDeclaration(AnnotationDeclaration d) {
		d.copyTo(this);
	}

	public IScope getScope() {
		return project.getDelegate().getScope(getTypeName());
	}

}
