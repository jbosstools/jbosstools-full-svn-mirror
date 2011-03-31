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
package org.jboss.tools.cdi.core;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.common.text.ITextSourceReference;

/**
 * Represents an annotation declaration. For example a qualifier or a scope
 * declaration of a bean/injection/produce method.
 * 
 * @author Alexey Kazakov
 */
public interface IAnnotationDeclaration extends ITextSourceReference {

	/**
	 * Return the annotation declaration. May be null.
	 * 
	 * @return the annotation declaration.
	 */
//	IAnnotation getDeclaration();

	IMemberValuePair[] getMemberValuePairs();

	/**
	 * Returns the member which is annotated by this declaration.
	 * 
	 * @return the member which is annotated by this declaration
	 */
	IMember getParentMember();

	/**
	 * Returns the corresponding IType of the annotation. May be null.
	 * 
	 * @return the corresponding IType of the annotation
	 */
	IType getType();

	/**
	 * Returns the corresponding annotation. May be null.
	 * 
	 * @return the corresponding annotation
	 */
	ICDIAnnotation getAnnotation();
}