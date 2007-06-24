/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template.expression;

import java.util.Set;

public class VpeExpressionInfo {
	private VpeExpression expression;
	private Set dependencySet;
	
	VpeExpressionInfo() {
		this(null, null);
	}

	VpeExpressionInfo(VpeExpression expression, Set dependencySet) {
		this.expression = expression;
		this.dependencySet = dependencySet;
	}
	
	public VpeExpression getExpression() {
		return expression;
	}
	
	public Set getDependencySet() {
		return dependencySet;
	}
}
