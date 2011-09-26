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
package org.jboss.ide.eclipse.as.openshift.core.internal;

import java.text.MessageFormat;
import java.util.Date;

import org.jboss.ide.eclipse.as.openshift.core.ApplicationLogReader;
import org.jboss.ide.eclipse.as.openshift.core.IApplication;
import org.jboss.ide.eclipse.as.openshift.core.ICartridge;
import org.jboss.ide.eclipse.as.openshift.core.OpenshiftException;

/**
 * @author André Dietisheim
 */
public class Application implements IApplication {

	private static final String GIT_URI_PATTERN = "ssh://{0}@{1}-{2}.{3}/~/git/{1}.git/";
	private static final String APPLICATION_URL_PATTERN = "http://{0}-{1}.{2}/";

	private String name;
	private ICartridge cartridge;
	private String uuid;
	private Date creationTime;
	private String embedded;
	private IOpenshiftService service;
	private ApplicationLogReader logReader;

	private User user;

	public Application(String name, ICartridge cartridge, User user, IOpenshiftService service) {
		this(name, null, cartridge, null, null, user, service);
	}

	public Application(String name, String uuid, ICartridge cartridge, String embedded, Date creationTime, User user,
			IOpenshiftService service) {
		this.name = name;
		this.cartridge = cartridge;
		this.uuid = uuid;
		this.embedded = embedded;
		this.creationTime = creationTime;
		this.user = user;
		this.service = service;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getUUID() throws OpenshiftException {
		user.loadLazyValues();
		return uuid;
	}

	@Override
	public ICartridge getCartridge() {
		return cartridge;
	}

	@Override
	public String getEmbedded() throws OpenshiftException {
		user.loadLazyValues();
		return embedded;
	}

	@Override
	public Date getCreationTime() throws OpenshiftException {
		user.loadLazyValues();
		return creationTime;
	}

	@Override
	public void destroy() throws OpenshiftException {
		service.destroyApplication(name, cartridge, user);
	}

	@Override
	public void start() throws OpenshiftException {
		service.startApplication(name, cartridge, user);
	}

	@Override
	public void restart() throws OpenshiftException {
		service.restartApplication(name, cartridge, user);
	}

	@Override
	public void stop() throws OpenshiftException {
		service.stopApplication(name, cartridge, user);
	}

	@Override
	public ApplicationLogReader getLog() throws OpenshiftException {
		if (logReader == null) {
			this.logReader = new ApplicationLogReader(this, user, service);
		}
		return logReader;
	}

	@Override
	public String getGitUri() throws OpenshiftException {
		Domain domain = user.getDomain();
		if (domain == null) {
			return null;
		}
		return MessageFormat
				.format(GIT_URI_PATTERN, getUUID(), getName(), domain.getNamespace(), domain.getRhcDomain());
	}

	@Override
	public String getApplicationUrl() throws OpenshiftException {
		Domain domain = user.getDomain();
		if (domain == null) {
			return null;
		}
		return MessageFormat.format(APPLICATION_URL_PATTERN, name, domain.getNamespace(), domain.getRhcDomain());
	}

	void update(ApplicationInfo applicationInfo) {
		if (applicationInfo == null) {
			return;
		}
		this.cartridge = applicationInfo.getCartridge();
		this.creationTime = applicationInfo.getCreationTime();
		this.name = applicationInfo.getName();
		this.uuid = applicationInfo.getUuid();
	}

	protected IOpenshiftService getService() {
		return service;
	}

	protected User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Application other = (Application) object;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
