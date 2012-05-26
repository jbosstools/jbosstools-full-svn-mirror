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
package org.jboss.tools.internal.deltacloud.ui.common.databinding.validator;

import org.eclipse.swt.widgets.Display;
import org.jboss.tools.internal.deltacloud.core.observable.ObservablePojo;

/**
 * @author André Dietisheim
 * 
 */
public class ObservableUIPojo extends ObservablePojo {

	@Override
	public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				ObservableUIPojo.super.firePropertyChange(propertyName, oldValue, newValue);
			}
		});
	}

	@Override
	public void fireIndexedPropertyChange(final String propertyName, final int index, final Object oldValue,
			final Object newValue) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				ObservableUIPojo.super.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
			}
		});
	}

}
