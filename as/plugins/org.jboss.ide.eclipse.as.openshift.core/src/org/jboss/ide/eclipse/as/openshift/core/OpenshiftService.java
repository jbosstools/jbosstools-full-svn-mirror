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
package org.jboss.ide.eclipse.as.openshift.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jboss.ide.eclipse.as.openshift.core.internal.marshalling.ApplicationRequestJsonMarshaller;
import org.jboss.ide.eclipse.as.openshift.core.internal.marshalling.ListCartridgesRequestJsonMarshaller;
import org.jboss.ide.eclipse.as.openshift.core.internal.marshalling.UserInfoRequestJsonMarshaller;
import org.jboss.ide.eclipse.as.openshift.internal.core.Cartridge;
import org.jboss.ide.eclipse.as.openshift.internal.core.HttpClientException;
import org.jboss.ide.eclipse.as.openshift.internal.core.UrlConnectionHttpClient;
import org.jboss.ide.eclipse.as.openshift.internal.core.UserInfo;
import org.jboss.ide.eclipse.as.openshift.internal.core.request.ApplicationAction;
import org.jboss.ide.eclipse.as.openshift.internal.core.request.ApplicationRequest;
import org.jboss.ide.eclipse.as.openshift.internal.core.request.ListCartridgesRequest;
import org.jboss.ide.eclipse.as.openshift.internal.core.request.OpenshiftJsonRequestFactory;
import org.jboss.ide.eclipse.as.openshift.internal.core.request.UserInfoRequest;
import org.jboss.ide.eclipse.as.openshift.internal.core.response.ApplicationResponseUnmarshaller;
import org.jboss.ide.eclipse.as.openshift.internal.core.response.OpenshiftResponse;

/**
 * @author André Dietisheim
 */
public class OpenshiftService implements IOpenshiftService {

	private static final String BASE_URL = "https://openshift.redhat.com/broker";

	private String username;
	private String password;

	public OpenshiftService(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public UserInfo getUserInfo() throws OpenshiftException {
		UserInfoRequest userInfoRequest = new UserInfoRequest(username, true);
		String url = userInfoRequest.getUrlString(BASE_URL);
		try {
			String userInfoRequestString = new UserInfoRequestJsonMarshaller().marshall(userInfoRequest);
			String request = new OpenshiftJsonRequestFactory(password, userInfoRequestString).create();
			String userInfoResponse = createHttpClient(url).post(request);
			throw new UnsupportedOperationException();
		} catch (MalformedURLException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not get user info for user \"{0}\" at \"{1}\"", username, url, e);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not get user info for user \"{0}\" at \"{1}\"", username, url, e);
		}
	}

	@Override
	public List<Cartridge> getCartridges() throws OpenshiftException {
		ListCartridgesRequest listCartridgesRequest = new ListCartridgesRequest(username, true);
		String url = listCartridgesRequest.getUrlString(BASE_URL);
		try {
			String listCartridgesRequestString =
					new ListCartridgesRequestJsonMarshaller().marshall(listCartridgesRequest);
			String request = new OpenshiftJsonRequestFactory(password, listCartridgesRequestString).create();
			String listCatridgesReponse = createHttpClient(url).post(request);
			throw new UnsupportedOperationException();
		} catch (MalformedURLException e) {
			throw new OpenshiftEndpointException(url, e, "Could not list available cartridges at \"{0}\"", url);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(url, e, "Could not list available cartridges at \"{0}\"", url);
		}
	}

	@Override
	public Application createDomain(String name) throws OpenshiftException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Application createApplication(String name, Cartridge cartridge) throws OpenshiftException {
		return requestApplicationAction(name, cartridge,
				new ApplicationRequest(name, cartridge, ApplicationAction.CONFIGURE, username, true));
	}

	@Override
	public Application destroyApplication(String name, Cartridge cartridge) throws OpenshiftException {
		return requestApplicationAction(name, cartridge,
				new ApplicationRequest(name, cartridge, ApplicationAction.DECONFIGURE, username, true));
	}

	protected Application requestApplicationAction(String name, Cartridge cartridge,
			ApplicationRequest applicationRequest) throws OpenshiftException {
		String url = applicationRequest.getUrlString(BASE_URL);
		try {
			String applicationRequestString =
					new ApplicationRequestJsonMarshaller().marshall(applicationRequest);
			String request = new OpenshiftJsonRequestFactory(password, applicationRequestString).create();
			String response = createHttpClient(url).post(request);
			OpenshiftResponse<Application> openshiftResponse = new ApplicationResponseUnmarshaller(response, name,
					cartridge).unmarshall();
			return openshiftResponse.getData();
		} catch (MalformedURLException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not {0} application \"{1}\" at \"{2}\"",
					applicationRequest.getAction().toHumanReadable(), name, url);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not {0} application \"{1}\" at \"{2}\"",
					applicationRequest.getAction().toHumanReadable(), name, url);
		}
	}

	private IHttpClient createHttpClient(String url) throws MalformedURLException {
		return new UrlConnectionHttpClient(new URL(url));
	}

}
