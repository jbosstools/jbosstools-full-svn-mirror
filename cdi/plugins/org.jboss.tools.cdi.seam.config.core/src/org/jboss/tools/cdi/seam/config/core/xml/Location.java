/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.seam.config.core.xml;

import org.eclipse.core.resources.IFile;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class Location {
	int start;
	int length;

	public Location(int start, int length) {
		this.start = start;
		this.length = length;
	}

	public int getStartPosition() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public boolean includes(int offset) {
		return start <= offset && start + length >= offset;
	}

}
