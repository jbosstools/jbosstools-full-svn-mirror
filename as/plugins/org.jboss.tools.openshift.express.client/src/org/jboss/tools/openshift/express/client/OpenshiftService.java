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
package org.jboss.tools.openshift.express.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jboss.tools.openshift.express.internal.client.Application;
import org.jboss.tools.openshift.express.internal.client.InternalUser;
import org.jboss.tools.openshift.express.internal.client.UserInfo;
import org.jboss.tools.openshift.express.internal.client.httpclient.HttpClientException;
import org.jboss.tools.openshift.express.internal.client.httpclient.NotFoundException;
import org.jboss.tools.openshift.express.internal.client.httpclient.UnauthorizedException;
import org.jboss.tools.openshift.express.internal.client.httpclient.UrlConnectionHttpClient;
import org.jboss.tools.openshift.express.internal.client.request.AbstractDomainRequest;
import org.jboss.tools.openshift.express.internal.client.request.ApplicationAction;
import org.jboss.tools.openshift.express.internal.client.request.ApplicationRequest;
import org.jboss.tools.openshift.express.internal.client.request.ChangeDomainRequest;
import org.jboss.tools.openshift.express.internal.client.request.CreateDomainRequest;
import org.jboss.tools.openshift.express.internal.client.request.ListCartridgesRequest;
import org.jboss.tools.openshift.express.internal.client.request.OpenshiftEnvelopeFactory;
import org.jboss.tools.openshift.express.internal.client.request.UserInfoRequest;
import org.jboss.tools.openshift.express.internal.client.request.marshalling.ApplicationRequestJsonMarshaller;
import org.jboss.tools.openshift.express.internal.client.request.marshalling.DomainRequestJsonMarshaller;
import org.jboss.tools.openshift.express.internal.client.request.marshalling.ListCartridgesRequestJsonMarshaller;
import org.jboss.tools.openshift.express.internal.client.request.marshalling.UserInfoRequestJsonMarshaller;
import org.jboss.tools.openshift.express.internal.client.response.OpenshiftResponse;
import org.jboss.tools.openshift.express.internal.client.response.unmarshalling.ApplicationResponseUnmarshaller;
import org.jboss.tools.openshift.express.internal.client.response.unmarshalling.ApplicationStatusResponseUnmarshaller;
import org.jboss.tools.openshift.express.internal.client.response.unmarshalling.DomainResponseUnmarshaller;
import org.jboss.tools.openshift.express.internal.client.response.unmarshalling.JsonSanitizer;
import org.jboss.tools.openshift.express.internal.client.response.unmarshalling.ListCartridgesResponseUnmarshaller;
import org.jboss.tools.openshift.express.internal.client.response.unmarshalling.UserInfoResponseUnmarshaller;

/**
 * @author André Dietisheim
 */
public class OpenshiftService implements IOpenshiftService {

	private String baseUrl;
	private String id;
	
	public OpenshiftService(String id) {
		this(id, BASE_URL);
	}

	public OpenshiftService(String id, String baseUrl) {
		this.id = id;
		this.baseUrl = baseUrl;
	}

	@Override
	public String getServiceUrl() {
		return baseUrl + SERVICE_PATH;
	}

	@Override
	public String getPlatformUrl() {
		return baseUrl;
	}

	@Override
	public boolean isValid(InternalUser user) throws OpenshiftException {
		return getUserInfo(user) != null;
	}

	@Override
	public UserInfo getUserInfo(InternalUser user) throws OpenshiftException {
		UserInfoRequest request = new UserInfoRequest(user.getRhlogin(), true);
		String url = request.getUrlString(getServiceUrl());
		try {
			String requestString = new UserInfoRequestJsonMarshaller().marshall(request);
			String openShiftRequestString = new OpenshiftEnvelopeFactory(user.getPassword(), requestString)
					.createString();
			String responseString = createHttpClient(id, url).post(openShiftRequestString);
			responseString = JsonSanitizer.sanitize(responseString);
			OpenshiftResponse<UserInfo> response =
					new UserInfoResponseUnmarshaller().unmarshall(responseString);
			return response.getOpenshiftObject();
		} catch (MalformedURLException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not get user info for user \"{0}\" at \"{1}\"", user.getRhlogin(), url);
		} catch (UnauthorizedException e) {
			throw new InvalidCredentialsOpenshiftException(url, e);
		} catch (NotFoundException e) {
			throw new NotFoundOpenshiftException(url, e);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not get user info for user \"{0}\" at \"{1}\"", user.getRhlogin(), url);
		}
	}

	@Override
	public List<ICartridge> getCartridges(InternalUser user) throws OpenshiftException {
		ListCartridgesRequest listCartridgesRequest = new ListCartridgesRequest(user.getRhlogin(), true);
		String url = listCartridgesRequest.getUrlString(getServiceUrl());
		try {
			String listCartridgesRequestString =
					new ListCartridgesRequestJsonMarshaller().marshall(listCartridgesRequest);
			String request = new OpenshiftEnvelopeFactory(user.getPassword(), listCartridgesRequestString)
					.createString();
			String listCatridgesReponse = createHttpClient(id, url).post(request);
			listCatridgesReponse = JsonSanitizer.sanitize(listCatridgesReponse);
			OpenshiftResponse<List<ICartridge>> response =
					new ListCartridgesResponseUnmarshaller().unmarshall(listCatridgesReponse);
			return response.getOpenshiftObject();
			/**
			 * always allowed to list cartridges, even with invalid credentials
			 */
		} catch (MalformedURLException e) {
			throw new OpenshiftEndpointException(url, e, "Could not list available cartridges at \"{0}\"", url);
		} catch (NotFoundException e) {
			throw new NotFoundOpenshiftException(url, e);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(url, e, "Could not list available cartridges at \"{0}\"", url);
		}
	}

	@Override
	public IDomain createDomain(String name, ISSHPublicKey sshKey, InternalUser user) throws OpenshiftException {
		return requestDomainAction(new CreateDomainRequest(name, sshKey, user.getRhlogin(), true), user);
	}

	@Override
	public IDomain changeDomain(String newName, ISSHPublicKey sshKey, InternalUser user) throws OpenshiftException {
		return requestDomainAction(new ChangeDomainRequest(newName, sshKey, user.getRhlogin(), true), user);
	}

	protected IDomain requestDomainAction(AbstractDomainRequest request, InternalUser user) throws OpenshiftException {
		String url = request.getUrlString(getServiceUrl());
		try {
			String requestString =
					new OpenshiftEnvelopeFactory(
							user.getPassword(),
							new DomainRequestJsonMarshaller().marshall(request))
							.createString();
			String responseString = createHttpClient(id, url).post(requestString);
			responseString = JsonSanitizer.sanitize(responseString);
			OpenshiftResponse<IDomain> response =
					new DomainResponseUnmarshaller(request.getName(), user, this).unmarshall(responseString);
			return response.getOpenshiftObject();
		} catch (MalformedURLException e) {
			throw new OpenshiftEndpointException(url, e, "Could reach openshift platform at \"{0}\"", url);
		} catch (UnauthorizedException e) {
			throw new InvalidCredentialsOpenshiftException(url, e);
		} catch (NotFoundException e) {
			throw new NotFoundOpenshiftException(url, e);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(url, e, "Could not {0}", request.toHumanReadable());
		}
	}

	@Override
	public Application createApplication(String name, ICartridge cartridge, InternalUser user)
			throws OpenshiftException {
		Application application = requestApplicationAction(new ApplicationRequest(name, cartridge,
				ApplicationAction.CONFIGURE,
				user.getRhlogin(), true), user);
		return application;
	}

	@Override
	public void destroyApplication(String name, ICartridge cartridge, InternalUser user) throws OpenshiftException {
		IApplication application = requestApplicationAction(new ApplicationRequest(name, cartridge,
				ApplicationAction.DECONFIGURE,
				user.getRhlogin(), true), user);
		user.remove(application);
	}

	@Override
	public IApplication startApplication(String name, ICartridge cartridge, InternalUser user)
			throws OpenshiftException {
		return requestApplicationAction(new ApplicationRequest(name, cartridge, ApplicationAction.START,
				user.getRhlogin(), true), user);
	}

	@Override
	public IApplication restartApplication(String name, ICartridge cartridge, InternalUser user)
			throws OpenshiftException {
		return requestApplicationAction(new ApplicationRequest(name, cartridge, ApplicationAction.RESTART,
				user.getRhlogin(), true), user);
	}

	@Override
	public IApplication stopApplication(String name, ICartridge cartridge, InternalUser user) throws OpenshiftException {
		return requestApplicationAction(new ApplicationRequest(name, cartridge, ApplicationAction.STOP,
				user.getRhlogin(), true), user);
	}

	@Override
	public String getStatus(String applicationName, ICartridge cartridge, InternalUser user) throws OpenshiftException {
		ApplicationRequest applicationRequest =
				new ApplicationRequest(applicationName, cartridge, ApplicationAction.STATUS, user.getRhlogin(), true);
		String url = applicationRequest.getUrlString(getServiceUrl());
		try {
			String applicationRequestString =
					new ApplicationRequestJsonMarshaller().marshall(applicationRequest);
			String request = new OpenshiftEnvelopeFactory(user.getPassword(), applicationRequestString).createString();
			String response = createHttpClient(id, url).post(request);

			response = JsonSanitizer.sanitize(response);
			OpenshiftResponse<String> openshiftResponse =
					new ApplicationStatusResponseUnmarshaller().unmarshall(response);
			return openshiftResponse.getOpenshiftObject();
		} catch (MalformedURLException e) {
			throw new OpenshiftException(
					e, "Could not {0} application \"{1}\" at \"{2}\": Invalid url \"{2}\"",
					applicationRequest.getAction().toHumanReadable(), applicationRequest.getName(), url);
		} catch (UnauthorizedException e) {
			throw new InvalidCredentialsOpenshiftException(url, e);
		} catch (NotFoundException e) {
			throw new NotFoundOpenshiftException(url, e);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not {0} application \"{1}\" at \"{2}\"",
					applicationRequest.getAction().toHumanReadable(), applicationRequest.getName(), url);
		}
	}

	protected Application requestApplicationAction(ApplicationRequest applicationRequest, InternalUser user)
			throws OpenshiftException {
		String url = applicationRequest.getUrlString(getServiceUrl());
		try {
			String applicationRequestString =
					new ApplicationRequestJsonMarshaller().marshall(applicationRequest);
			String request = new OpenshiftEnvelopeFactory(user.getPassword(), applicationRequestString).createString();
			String response = createHttpClient(id, url).post(request);

			response = JsonSanitizer.sanitize(response);
			OpenshiftResponse<Application> openshiftResponse =
					new ApplicationResponseUnmarshaller(applicationRequest.getName(),
							applicationRequest.getCartridge(), user, this).unmarshall(response);
			return openshiftResponse.getOpenshiftObject();
		} catch (MalformedURLException e) {
			throw new OpenshiftException(
					e, "Could not {0} application \"{1}\" at \"{2}\": Invalid url \"{2}\"",
					applicationRequest.getAction().toHumanReadable(), applicationRequest.getName(), url);
		} catch (UnauthorizedException e) {
			throw new InvalidCredentialsOpenshiftException(url, e);
		} catch (NotFoundException e) {
			throw new NotFoundOpenshiftException(url, e);
		} catch (HttpClientException e) {
			throw new OpenshiftEndpointException(
					url, e, "Could not {0} application \"{1}\" at \"{2}\"",
					applicationRequest.getAction().toHumanReadable(), applicationRequest.getName(), url);
		}
	}

	private IHttpClient createHttpClient(String id, String url) throws MalformedURLException {
		return new UrlConnectionHttpClient(id, new URL(url));
	}
}
