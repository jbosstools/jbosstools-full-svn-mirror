/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.core;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.runtime.IProgressMonitor;

import com.openshift.client.IApplication;
import com.openshift.client.IEmbeddableCartridge;
import com.openshift.client.IEmbeddedCartridge;
import com.openshift.client.OpenShiftException;

/**
 * @author Andre Dietisheim
 */
public class EmbedCartridgesOperation {

	private IApplication application;

	public EmbedCartridgesOperation(IApplication application) {
		this.application = application;
	}

	public List<IEmbeddedCartridge> execute(final List<IEmbeddableCartridge> enabledCartridges, final IProgressMonitor monitor) throws SocketTimeoutException, OpenShiftException {
		if (enabledCartridges == null) {
			return Collections.emptyList();
		}
		List<IEmbeddableCartridge> cartridgesToAdd = new ArrayList<IEmbeddableCartridge>();
		List<IEmbeddableCartridge> cartridgesToRemove = new ArrayList<IEmbeddableCartridge>();
		computeAdditionsAndRemovals(
				cartridgesToAdd,
				cartridgesToRemove,
				enabledCartridges,
				application.getEmbeddedCartridges());
		removeEmbeddedCartridges(cartridgesToRemove, application);
		final List<IEmbeddedCartridge> addedCartridges = addEmbeddedCartridges(cartridgesToAdd, application);
		return addedCartridges;

	}

	private void removeEmbeddedCartridges(List<IEmbeddableCartridge> cartridgesToRemove, final IApplication application) throws OpenShiftException,
			SocketTimeoutException {
		if (cartridgesToRemove.isEmpty()) {
			return;
		}
		// Collections.sort(removedCartridges, new CartridgeComparator());
		for (IEmbeddableCartridge cartridgeToRemove : cartridgesToRemove) {
			final IEmbeddedCartridge embeddedCartridge = application.getEmbeddedCartridge(cartridgeToRemove);
			if (embeddedCartridge != null) {
				embeddedCartridge.destroy();
			}
		}
	}

	private List<IEmbeddedCartridge> addEmbeddedCartridges(List<IEmbeddableCartridge> cartridgesToAdd, final IApplication application)
			throws OpenShiftException, SocketTimeoutException {
		if (cartridgesToAdd.isEmpty()) {
			return Collections.emptyList();
		}
		// Collections.sort(addedCartridges, new CartridgeComparator());
		return application.addEmbeddableCartridges(cartridgesToAdd);
	}

	private void computeAdditionsAndRemovals(List<IEmbeddableCartridge> addedCartridges,
			List<IEmbeddableCartridge> removedCartridges, List<IEmbeddableCartridge> selectedCartridges,
			List<IEmbeddedCartridge> embeddedCartidges)
			throws OpenShiftException, SocketTimeoutException {
		ListDiff listDiff = Diffs.computeListDiff(embeddedCartidges, selectedCartridges);
		for (ListDiffEntry entry : listDiff.getDifferences()) {
			if (entry.isAddition()) {
				addedCartridges.add((IEmbeddableCartridge) entry.getElement());
			} else {
				removedCartridges.add((IEmbeddableCartridge) entry.getElement());
			}
		}
		Collections.sort(addedCartridges, new CartridgeComparator());
		Collections.sort(removedCartridges, new CartridgeComparator());
	}

	private static class CartridgeComparator implements Comparator<IEmbeddableCartridge> {

		@Override
		public int compare(IEmbeddableCartridge thisCartridge, IEmbeddableCartridge thatCartridge) {
			// mysql has to be added/removed before phpmyadmin
			if (thisCartridge.equals(IEmbeddableCartridge.MYSQL_51)) {
				return -1;
			} else if (thatCartridge.equals(IEmbeddableCartridge.MYSQL_51)) {
				return 1;
			} else if (thisCartridge.equals(IEmbeddableCartridge.POSTGRESQL_84)) {
				return -1;
			} else if (thatCartridge.equals(IEmbeddableCartridge.POSTGRESQL_84)) {
				return 1;
			} else if (thisCartridge.equals(IEmbeddableCartridge.MONGODB_20)) {
				return -1;
			} else if (thatCartridge.equals(IEmbeddableCartridge.MONGODB_20)) {
				return 1;
			}
			return 0;
		}
	}
	
}
