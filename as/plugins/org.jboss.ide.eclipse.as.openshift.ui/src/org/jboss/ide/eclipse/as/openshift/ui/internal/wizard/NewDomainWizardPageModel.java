/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.ide.eclipse.as.openshift.ui.internal.wizard;

import java.io.File;
import java.io.IOException;

import org.jboss.ide.eclipse.as.openshift.core.IDomain;
import org.jboss.ide.eclipse.as.openshift.core.ISSHPublicKey;
import org.jboss.ide.eclipse.as.openshift.core.IUser;
import org.jboss.ide.eclipse.as.openshift.core.OpenshiftException;
import org.jboss.ide.eclipse.as.openshift.core.SSHPublicKey;
import org.jboss.tools.common.ui.databinding.ObservableUIPojo;

/**
 * @author André Dietisheim
 */
public class NewDomainWizardPageModel extends ObservableUIPojo {

	public static final String PROPERTY_NAMESPACE = "namespace";
	public static final String PROPERTY_SSHKEY = "sshKey";
	public static final String PROPERTY_DOMAIN = "domain";

	private String namespace;
	private IDomain domain;
	private String sshKey;
	private ServerAdapterWizardModel wizardModel;

	public NewDomainWizardPageModel(ServerAdapterWizardModel wizardModel) {
		this.wizardModel = wizardModel;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public void createDomain() throws OpenshiftException, IOException {
		IUser user = getUser();
		IDomain domain = user.createDomain(namespace, loadSshKey());
		setDomain(domain);
	}

	public String getSshKey() {
		return sshKey;
	}

	public void setSshKey(String sshKey) {
		firePropertyChange(PROPERTY_SSHKEY, this.sshKey, this.sshKey = sshKey);
	}

	private ISSHPublicKey loadSshKey() throws IOException, OpenshiftException {
		return new SSHPublicKey(new File(sshKey));
	}

	public void setNamespace(String namespace) throws OpenshiftException {
		firePropertyChange(PROPERTY_NAMESPACE, this.namespace, this.namespace = namespace);
	}

	public boolean hasDomain() throws OpenshiftException {
		if (!hasUser()) {
			return false;
		}
		return getUser().hasDomain();
	}

	public IDomain getDomain() {
		return domain;
	}

	public void setDomain(IDomain domain) {
		firePropertyChange(PROPERTY_DOMAIN, this.domain, this.domain = domain);
	}

	public boolean hasUser() {
		return wizardModel.getUser() != null;
	}

	public IUser getUser() {
		return wizardModel.getUser();
	}
}
