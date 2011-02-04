/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.utils;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;

/**
 * @author Rob Stryker
 */
public class LayoutUtils {

	/**
	 * This method has been stolen from AS-Tools' UIUtil class and is useful for
	 * creating FormData objects
	 */
	public static FormData createFormData(
			Object topStart, int topOffset,
			Object bottomStart, int bottomOffset,
			Object leftStart, int leftOffset,
			Object rightStart, int rightOffset) {

		FormData data = new FormData();

		if (topStart != null) {
			data.top = topStart instanceof Control ? new FormAttachment((Control) topStart, topOffset) :
					new FormAttachment(((Integer) topStart).intValue(), topOffset);
		}

		if (bottomStart != null) {
			data.bottom = bottomStart instanceof Control ? new FormAttachment((Control) bottomStart, bottomOffset) :
					new FormAttachment(((Integer) bottomStart).intValue(), bottomOffset);
		}

		if (leftStart != null) {
			data.left = leftStart instanceof Control ? new FormAttachment((Control) leftStart, leftOffset) :
					new FormAttachment(((Integer) leftStart).intValue(), leftOffset);
		}

		if (rightStart != null) {
			data.right = rightStart instanceof Control ? new FormAttachment((Control) rightStart, rightOffset) :
					new FormAttachment(((Integer) rightStart).intValue(), rightOffset);
		}
		return data;
	}
}
