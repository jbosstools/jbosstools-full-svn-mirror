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

/**
 * Represents an injection point which is a parameter of a method.
 * 
 * @author Alexey Kazakov
 */
public interface IInjectionPointParameter extends IParameter, IInjectionPoint {

	/**
	 * Extensions can override type.
	 * 
	 * @return type that was set by an extension instead of Java parameter type
	 */
	public ITypeDeclaration getOverridenType();

}