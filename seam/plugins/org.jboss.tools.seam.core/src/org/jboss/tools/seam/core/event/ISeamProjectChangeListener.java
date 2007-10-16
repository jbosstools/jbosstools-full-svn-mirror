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
package org.jboss.tools.seam.core.event;

/**
 * Interface of an object listening to seam project modifications.
 * 
 * @author Viacheslav Kabanovich
 */
public interface ISeamProjectChangeListener {
	
	/**
	 * Called when seam project is changed.
	 * Event contains list of structured detailed changes.
	 * It can be conveniently processed using method
	 * event.visit(IChangeVisitor)
	 * @param event
	 */
	public void projectChanged(SeamProjectChangeEvent event);

}
