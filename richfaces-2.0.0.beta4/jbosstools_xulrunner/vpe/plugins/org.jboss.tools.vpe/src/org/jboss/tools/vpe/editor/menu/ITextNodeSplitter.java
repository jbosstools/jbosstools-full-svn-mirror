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
package org.jboss.tools.vpe.editor.menu;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 *
 */
public interface ITextNodeSplitter {
	
	/**
	 * 
	 */
	public static final int INSERT_AROUND = 1;

	/**
	 * 
	 */
	public static final int INSERT_BEFORE = 2;

	/**
	 * 
	 */
	public static final int INSERT_AFTER = 3;

	/**
	 * 
	 * @param index
	 * @return
	 */
	public int getSplitIndex(int index);

	/**
	 * 
	 * @param type
	 */
	public void nodeSplit(int type);
}
