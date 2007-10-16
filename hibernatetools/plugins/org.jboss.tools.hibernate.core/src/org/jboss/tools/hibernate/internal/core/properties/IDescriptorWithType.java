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
package org.jboss.tools.hibernate.internal.core.properties;

/**
 * @author kaa
 * akuzmin@exadel.com
 * Aug 3, 2005
 */
public interface IDescriptorWithType {

	public boolean isIsint(); 
	public void setIsint(boolean isint);
	public boolean isUseLeftRange();
	public boolean isUseRightRange();

	public Object getLeftRange();
	public Object getRightRange();

}
