/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.model;

import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * @author Dart Peng
 * @Date Jul 31, 2008
 */
public class StrucutredDataElementTransfer extends Transfer {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getSupportedTypes()
	 */
	@Override
	public TransferData[] getSupportedTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	@Override
	protected int[] getTypeIds() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	@Override
	protected String[] getTypeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#isSupportedType(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	public boolean isSupportedType(TransferData transferData) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
		return null;
	}

}
