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

import java.util.Collection;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;

/**
 * 
 * @author André Dietisheim
 */
public class DataBindingUtils {

	private DataBindingUtils() {
	}

	/**
	 * Disposes all bindings in a given collection an clears the collection.
	 * 
	 * @param bindingCollection
	 *            the binding collection
	 * 
	 * @return the collection<binding>
	 */
	public static Collection<Binding> disposeBindings(Collection<Binding> bindingCollection) {
		if (bindingCollection != null) {
			for (Binding binding : bindingCollection) {
				binding.dispose();
			}
			bindingCollection.clear();
		}
		return bindingCollection;
	}

	/**
	 * Adds the given change listener to the given observable. Listens for
	 * disposal of the given control and removes the listener when it gets
	 * disposed.
	 * 
	 * @param listener
	 *            the listener to add to the given observable
	 * @param observable
	 *            the observable to add the listener to
	 * @param control
	 *            the control to listen to disposal to
	 */
	public static void addChangeListener(final IChangeListener listener, final IObservable observable, Control control) {
		observable.addChangeListener(listener);
		control.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				observable.removeChangeListener(listener);
			}
		});
	}

	/**
	 * Adds the given value change listener to the given observable. Listens for
	 * disposal of the given control and removes the listener when it gets
	 * disposed.
	 * 
	 * @param listener
	 *            the listener to add to the given observable
	 * @param observable
	 *            the observable to add the listener to
	 * @param control
	 *            the control to listen to disposal to
	 */
	public static void addValueChangeListener(final IValueChangeListener listener, final IObservableValue observable,
			Control control) {
		observable.addValueChangeListener(listener);
		control.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				observable.removeValueChangeListener(listener);
			}
		});
	}

}
