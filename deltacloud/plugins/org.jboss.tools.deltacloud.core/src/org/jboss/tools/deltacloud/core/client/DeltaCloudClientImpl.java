/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.core.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.Assert;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProperty.Kind;
import org.jboss.tools.deltacloud.core.client.request.AbstractListObjectsRequest;
import org.jboss.tools.deltacloud.core.client.request.CreateInstanceRequest;
import org.jboss.tools.deltacloud.core.client.request.CreateKeyRequest;
import org.jboss.tools.deltacloud.core.client.request.DeleteKeyRequest;
import org.jboss.tools.deltacloud.core.client.request.DeltaCloudRequest;
import org.jboss.tools.deltacloud.core.client.request.ListHardwareProfileRequest;
import org.jboss.tools.deltacloud.core.client.request.ListHardwareProfilesRequest;
import org.jboss.tools.deltacloud.core.client.request.ListImageRequest;
import org.jboss.tools.deltacloud.core.client.request.ListImagesRequest;
import org.jboss.tools.deltacloud.core.client.request.ListInstanceRequest;
import org.jboss.tools.deltacloud.core.client.request.ListInstancesRequest;
import org.jboss.tools.deltacloud.core.client.request.ListKeyRequest;
import org.jboss.tools.deltacloud.core.client.request.ListKeysRequest;
import org.jboss.tools.deltacloud.core.client.request.ListRealmRequest;
import org.jboss.tools.deltacloud.core.client.request.ListRealmsRequest;
import org.jboss.tools.deltacloud.core.client.request.PerformInstanceActionRequest;
import org.jboss.tools.deltacloud.core.client.request.TypeRequest;
import org.jboss.tools.deltacloud.core.client.unmarshal.InstanceUnmarshaller;
import org.jboss.tools.deltacloud.core.client.unmarshal.KeyUnmarshaller;
import org.jboss.tools.deltacloud.core.client.unmarshal.KeysUnmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Andre Dietisheim (based on prior implementation by Martyn Taylor)
 */
public class DeltaCloudClientImpl implements InternalDeltaCloudClient {

	private static final String DOCUMENT_ELEMENT_DRIVER = "driver";
	private static final String DOCUMENT_ELEMENT_API = "api";

	public static enum DeltaCloudServerType {
		UNKNOWN, MOCK, EC2
	}

	private URL baseUrl;
	private String username;
	private String password;
	private DocumentBuilderFactory documentBuilderFactory;

	public DeltaCloudClientImpl(String url) throws MalformedURLException,
			DeltaCloudClientException {
		this(url, null, null);
	}

	public DeltaCloudClientImpl(String url, String username, String password) throws DeltaCloudClientException {
		this.baseUrl = createUrl(url);
		this.username = username;
		this.password = password;
		this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
	}

	private URL createUrl(String url) throws DeltaCloudClientException {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new DeltaCloudClientException(MessageFormat.format(
					"Could not create url for {0}", url), e);
		}
	}

	protected InputStream request(DeltaCloudRequest deltaCloudRequest)
			throws DeltaCloudClientException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try {
			URL url = deltaCloudRequest.getUrl();
			addCredentials(url, httpClient, username, password);
			HttpUriRequest request = createRequest(deltaCloudRequest);
			HttpResponse httpResponse = httpClient.execute(request);
			throwOnHttpErrors(deltaCloudRequest.getUrl(), httpResponse);
			if (httpResponse.getEntity() == null) {
				return null;
			}
			return httpResponse.getEntity().getContent();
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (IOException e) {
			throw new DeltaCloudClientException(e);
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		} finally {
			// httpClient.getConnectionManager().shutdown();
		}
	}

	protected String requestStringResponse(DeltaCloudRequest deltaCloudRequest)
			throws DeltaCloudClientException {
		try {
			InputStream inputStream = request(deltaCloudRequest);
			if (inputStream == null) {
				return null;
			}
			return getResponse(inputStream);
		} catch (IOException e) {
			throw new DeltaCloudClientException(e);
		}
	}

	private void throwOnHttpErrors(URL requestUrl, HttpResponse httpResponse)
			throws DeltaCloudClientException {
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (HttpStatusCode.OK.isStatus(statusCode)) {
			return;
		} else if (HttpStatusCode.FORBIDDEN.isStatus(statusCode)) {
			throw new DeltaCloudAuthClientException(
					MessageFormat
							.format("The server reported an authorization error \"{0}\" on requesting \"{1}\"",
									httpResponse.getStatusLine()
											.getReasonPhrase(), requestUrl));
		} else if (HttpStatusCode.NOT_FOUND.isStatus(statusCode)) {
			throw new DeltaCloudNotFoundClientException(MessageFormat.format(
					"The server could not find the resource \"{0}\"",
					requestUrl));
		} else if (HttpStatusRange.CLIENT_ERROR.isInRange(statusCode)
				|| HttpStatusRange.SERVER_ERROR.isInRange(statusCode)) {
			throw new DeltaCloudClientException(
					MessageFormat
							.format("The server reported an error \"{0}\" on requesting \"{1}\"",
									httpResponse.getStatusLine()
											.getReasonPhrase(), requestUrl));
		}
	}

	private String getResponse(InputStream inputStream) throws IOException,
			DeltaCloudClientException {
		if (inputStream == null) {
			return null;
		}
		String xml = readInputStreamToString(inputStream);
		return xml;
	}

	/**
	 * Returns a request instance for the given request type and url.
	 * 
	 * @param httpMethod
	 *            the request type to use
	 * @param requestUrl
	 *            the requested url
	 * @return the request instance
	 * @throws MalformedURLException
	 */
	protected HttpUriRequest createRequest(DeltaCloudRequest deltaCloudRequest)
			throws MalformedURLException {
		HttpUriRequest request = null;
		String url = deltaCloudRequest.getUrl().toString();
		HttpMethod httpMethod = deltaCloudRequest.getHttpMethod();
		switch (httpMethod) {
		case POST:
			request = new HttpPost(url);
			break;
		case DELETE:
			request = new HttpDelete(url);
			break;
		case GET:
		default:
			request = new HttpGet(url);
		}
		request.setHeader("Accept", "application/xml;q=1");
		return request;
	}

	/**
	 * Adds the credentials to the given http client.
	 * 
	 * @param httpClient
	 *            the http client
	 * @return the default http client
	 * @throws UnknownHostException
	 */
	private DefaultHttpClient addCredentials(URL url,
			DefaultHttpClient httpClient, String username, String password)
			throws UnknownHostException {
		if (username != null && password != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(url.getHost(), url.getPort()),
					new UsernamePasswordCredentials(username, password));
		}
		return httpClient;
	}

	private static String readInputStreamToString(InputStream is)
			throws DeltaCloudClientException {
		try {
			try {
				if (is != null) {
					StringBuilder sb = new StringBuilder();
					String line = null;

					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					while ((line = reader.readLine()) != null) {
						sb.append(line).append("\n");
					}
					return sb.toString();
				}
			} finally {
				is.close();
			}
		} catch (Exception e) {
			throw new DeltaCloudClientException(
					"Error converting Response to String", e);
		}
		return "";
	}

	public DeltaCloudServerType getServerType() {
		DeltaCloudServerType serverType = DeltaCloudServerType.UNKNOWN;
		try {
			String apiResponse = requestStringResponse(new TypeRequest(baseUrl));
			Document document = getDocument(apiResponse);
			NodeList elements = document.getElementsByTagName(DOCUMENT_ELEMENT_API);
			if (elements.getLength() > 0) {
				Node n = elements.item(0);
				Node driver = n.getAttributes().getNamedItem(DOCUMENT_ELEMENT_DRIVER);
				if (driver != null) {
					String driverValue = driver.getNodeValue();
					serverType = DeltaCloudServerType.valueOf(driverValue.toUpperCase());
				}
			}
		} catch (Exception e) {
			serverType = DeltaCloudServerType.UNKNOWN;
		}
		return serverType;
	}

	@Override
	public Instance createInstance(String imageId) throws DeltaCloudClientException {
		try {
			return buildInstance(request(new CreateInstanceRequest(baseUrl, imageId)));
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		}

	}

	public Instance createInstance(String name, String imageId, String profileId, String realmId, String memory,
			String storage) throws DeltaCloudClientException {
		return createInstance(name, imageId, profileId, realmId, null, memory, storage);
	}

	public Instance createInstance(String name, String imageId, String profileId, String realmId, String keyId,
			String memory, String storage) throws DeltaCloudClientException {
		try {
			InputStream inputStream = request(
					new CreateInstanceRequest(baseUrl, name, imageId, profileId, realmId, keyId, memory, storage));
			Instance instance = buildInstance(inputStream);
			// TODO: WORKAROUND for
			// https://issues.jboss.org/browse/JBIDE-8005
			if (keyId != null) {
				instance.setKeyId(keyId);
			}
			// TODO: WORKAROUND for
			// https://issues.jboss.org/browse/JBIDE-8005
			return instance;
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		}
	}

	@Override
	public HardwareProfile listProfile(String profileId) throws DeltaCloudClientException {
		try {
			return buildDeltaCloudObject(HardwareProfile.class,
					requestStringResponse(new ListHardwareProfileRequest(baseUrl, profileId)));
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		}
	}

	@Override
	public List<HardwareProfile> listProfiles() throws DeltaCloudClientException {
		try {
			return buildProfiles(request(new ListHardwareProfilesRequest(baseUrl)));
		} catch (Exception e) {
			throw new DeltaCloudClientException(MessageFormat.format("could not get realms on cloud at \"{0}\"",
					baseUrl), e);
		}
	}

	private List<HardwareProfile> buildProfiles(InputStream inputStream)
			throws ParserConfigurationException, SAXException, IOException, DeltaCloudClientException {
		Document document = getDocument(getResponse(inputStream));
		List<HardwareProfile> profiles = new ArrayList<HardwareProfile>();
		NodeList elements = document.getElementsByTagName("hardware_profile");
		for (int i = 0; i < elements.getLength(); i++) {
			HardwareProfile profile = createProfile(elements.item(i));
			profiles.add(profile);
		}
		return profiles;
	}

	private HardwareProfile createProfile(Node node) {
		Assert.isLegal(node instanceof Element);
		Element element = (Element) node;
		HardwareProfile profile = new HardwareProfile();
		profile.setId(element.getAttribute("id"));
		profile.setProperties(createProperties(element.getElementsByTagName("property")));
		return profile;
	}

	private List<Property> createProperties(NodeList propertiesList) {
		List<Property> properties = new ArrayList<Property>();
		for (int i = 0; i < propertiesList.getLength(); i++) {
			Property property = createProperty(propertiesList.item(i));
			properties.add(property);
		}
		return properties;
	}

	private Property createProperty(Node node) {
		Assert.isTrue(node instanceof Element);
		Element element = (Element) node;
		Property property = new Property();
		property.setName(element.getAttribute("name"));
		property.setId(element.getAttribute("id"));
		property.setUnit(element.getAttribute("unit"));
		property.setValue(element.getAttribute("value"));
		String kind = element.getAttribute("kind");
		Assert.isTrue(kind != null);
		kind = kind.toUpperCase();
		property.setKind(kind);
		if (Kind.RANGE.toString().equals(property.getKind())) {
			setRange(element, property);
		} else if (Kind.ENUM.toString().equals(property.getKind())) {
			setEnum(element, property);
		} else if (Kind.FIXED.toString().equals(property.getKind())) {
			// no special treatement
		}
		return property;
	}

	private void setRange(Element propertyElement, Property property) {
		Node node = propertyElement.getElementsByTagName("range").item(0);
		Assert.isLegal(node instanceof Element);
		Element rangeElement = (Element) node;
		property.setRange(rangeElement.getAttribute("first"), rangeElement.getAttribute("last"));
	}

	private void setEnum(Element propertyElement, Property property) {
		Node node = propertyElement.getElementsByTagName("enum").item(0);
		Assert.isLegal(node instanceof Element);
		Element enumElement = (Element) node;
		NodeList nodeList = enumElement.getElementsByTagName("entry");
		ArrayList<String> enumValues = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node entryNode = nodeList.item(i);
			Assert.isTrue(entryNode instanceof Element);
			Element entryElement = (Element) entryNode;
			enumValues.add(entryElement.getAttribute("value"));
		}
		property.setEnums(enumValues);
	}

	@Override
	public List<Image> listImages() throws DeltaCloudClientException {
		return listDeltaCloudObjects(Image.class,
				new ListImagesRequest(baseUrl), "image");
	}

	@Override
	public Image listImages(String imageId) throws DeltaCloudClientException {
		return JAXB.unmarshal(new StringReader(
				requestStringResponse(new ListImageRequest(baseUrl, imageId))),
				Image.class);
	}

	@Override
	public List<Instance> listInstances() throws DeltaCloudClientException {
		return listDeltaCloudObjects(Instance.class,
				new ListInstancesRequest(baseUrl), "instance");
	}

	@Override
	public Instance listInstances(String instanceId) throws DeltaCloudClientException {
		try {
			return buildInstance(request(new ListInstanceRequest(baseUrl, instanceId)));
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		}
	}

	@Override
	public List<Realm> listRealms() throws DeltaCloudClientException {
		try {
			return buildRealms(request(new ListRealmsRequest(baseUrl)));
		} catch (Exception e) {
			throw new DeltaCloudClientException(MessageFormat.format("could not get realms on cloud at \"{0}\"",
					baseUrl), e);
		}
	}

	private List<Realm> buildRealms(InputStream inputStream)
			throws ParserConfigurationException, SAXException, IOException, DeltaCloudClientException {
		Document document = getDocument(getResponse(inputStream));
		List<Realm> realms = new ArrayList<Realm>();
		NodeList elements = document.getElementsByTagName("realm");
		for (int i = 0; i < elements.getLength(); i++) {
			Realm realm = createRealm((Element) elements.item(i));
			realms.add(realm);
		}
		return realms;
	}

	private Realm createRealm(Node node) {
		Assert.isLegal(node instanceof Element);
		Realm realm = new Realm();
		updateRealm(realm, (Element) node);
		return realm;
	}

	private Realm updateRealm(Realm realm, Element element) {
		realm.setId(element.getAttribute("id"));
		realm.setName(element.getElementsByTagName("name").item(0).getTextContent());
		realm.setLimit(element.getElementsByTagName("limit").item(0).getTextContent());
		realm.setState(element.getElementsByTagName("state").item(0).getTextContent());

		return realm;
	}

	@Override
	public Realm listRealms(String realmId) throws DeltaCloudClientException {
		try {
			Document document = getDocument(getResponse(request(new ListRealmRequest(baseUrl, realmId))));
			return createRealm((Element) document.getElementsByTagName("realm").item(0));
		} catch (Exception e) {
			throw new DeltaCloudClientException(
					MessageFormat.format("could not get realms on cloud at \"{0}\"", baseUrl), e);
		}
	}

	public Key createKey(String keyname) throws DeltaCloudClientException {
		try {
			CreateKeyRequest keyRequest = new CreateKeyRequest(baseUrl, keyname);
			InputStream inputStream = request(keyRequest);
			Key key = new KeyUnmarshaller().unmarshall(inputStream, new Key());
			return key;
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		}
	}

	public void deleteKey(String keyname) throws DeltaCloudClientException {
		requestStringResponse(new DeleteKeyRequest(baseUrl, keyname));
	}

	public List<Key> listKeys() throws DeltaCloudClientException {
		InputStream inputStream = request(new ListKeysRequest(baseUrl));
		List<Key> keys = new ArrayList<Key>();
		new KeysUnmarshaller().unmarshall(inputStream, keys);
		return keys;
	}

	public Key listKey(String id) throws DeltaCloudClientException {
		InputStream inputStream = request(new ListKeyRequest(baseUrl, id));
		Key key = new KeyUnmarshaller().unmarshall(inputStream, new Key());
		return key;
	}

	private Instance updateInstance(InputStream inputStream, Instance instance) throws Exception {
		return new InstanceUnmarshaller().unmarshall(inputStream, instance);
	}

	private Instance buildInstance(InputStream inputStream) throws Exception {
		return updateInstance(inputStream, new Instance());
	}

	private Instance buildInstance(String response) throws Exception {
		InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(response.getBytes()));
		return buildInstance(inputStream);
	}

	private HardwareProfile buildHardwareProfile(String xml)
			throws DeltaCloudClientException {
		try {
			HardwareProfile profile = JAXB.unmarshal(new StringReader(xml),
					HardwareProfile.class);

			Document document = getDocument(xml);

			List<Node> nodes = getPropertyNodes(document, "hardware_profile"); //$NON-NLS-1$

			for (Node n : nodes) {
				Property p = new Property();
				p.setName(n.getAttributes().getNamedItem("name").getNodeValue()); //$NON-NLS-1$
				p.setValue(n.getAttributes()
						.getNamedItem("value").getNodeValue()); //$NON-NLS-1$
				p.setUnit(n.getAttributes().getNamedItem("unit").getNodeValue()); //$NON-NLS-1$
				p.setKind(n.getAttributes().getNamedItem("kind").getNodeValue()); //$NON-NLS-1$
				if (p.getKind().equals("range")) { //$NON-NLS-1$
					NodeList children = n.getChildNodes();
					for (int i = 0; i < children.getLength(); ++i) {
						Node child = children.item(i);
						if (child.getNodeName().equals("range")) { //$NON-NLS-1$
							String first = child.getAttributes()
									.getNamedItem("first").getNodeValue(); //$NON-NLS-1$
							String last = child.getAttributes()
									.getNamedItem("last").getNodeValue(); //$NON-NLS-1$
							p.setRange(first, last);
						}
					}
				} else if (p.getKind().equals("enum")) { //$NON-NLS-1$
					ArrayList<String> enums = new ArrayList<String>();
					NodeList children = n.getChildNodes();
					for (int i = 0; i < children.getLength(); ++i) {
						Node child = children.item(i);
						if (child.getNodeName().equals("enum")) { //$NON-NLS-1$
							NodeList enumChildren = child.getChildNodes();
							for (int j = 0; j < enumChildren.getLength(); ++j) {
								Node enumChild = enumChildren.item(j);
								if (enumChild.getNodeName().equals("entry")) {
									enums.add(enumChild
											.getAttributes()
											.getNamedItem("value").getNodeValue()); //$NON-NLS-1$
								}
							}
						}
					}
					p.setEnums(enums);
				}
				profile.getProperties().add(p);
			}
			return profile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Node> getPropertyNodes(Document document, String elementName) {
		NodeList elements = document.getElementsByTagName(elementName);
		ArrayList<Node> values = new ArrayList<Node>();
		for (int i = 0; i < elements.getLength(); i++) {
			NodeList children = elements.item(i).getChildNodes();
			for (int j = 0; j < children.getLength(); ++j) {
				Node child = children.item(j);
				if (child.getNodeName().equals("property")) { //$NON-NLS-1$
					values.add(child);
				}
			}
		}
		return values;
	}

	private <T extends AbstractDeltaCloudObject> List<T> listDeltaCloudObjects(Class<T> clazz,
			AbstractListObjectsRequest request, String elementName) throws DeltaCloudClientException {
		try {
			Document document = getDocument(requestStringResponse(request));
			List<T> dco = new ArrayList<T>();
			NodeList nodeList = document.getElementsByTagName(elementName);
			for (int i = 0; i < nodeList.getLength(); i++) {
				dco.add(buildDeltaCloudObject(clazz, nodeToString(nodeList.item(i))));
			}
			return dco;
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException(MessageFormat.format(
					"Could not list object of type {0}", clazz), e);
		}
	}

	private Document getDocument(String response) throws ParserConfigurationException, SAXException, IOException {
		InputSource is = new InputSource(new StringReader(response));
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(is);
		return document;
	}

	@SuppressWarnings("unchecked")
	private <T extends Object> T buildDeltaCloudObject(Class<T> clazz, String node) throws Exception {
		if (clazz.equals(Instance.class)) {
			return (T) buildInstance(node);
		} else if (clazz.equals(HardwareProfile.class)) {
			return (T) buildHardwareProfile(node);
		} else {
			return JAXB.unmarshal(new StringReader(node), clazz);
		}
	}

	public boolean performInstanceAction(InstanceAction action) throws DeltaCloudClientException {
		if (action != null) {
			try {
				InputStream inputStream = request(new PerformInstanceActionRequest(
						new URL(action.getUrl()), action.getMethod()));
				if (!InstanceAction.DESTROY.equals(action.getName())) {
					updateInstance(inputStream, action.getOwner());
				}
			} catch (MalformedURLException e) {
				throw new DeltaCloudClientException(
						MessageFormat.format("Could not perform action {0} on instance {1}", action.getName(), action
								.getOwner().getName()), e);
			} catch (DeltaCloudClientException e) {
				throw e;
			} catch (Exception e) {
				throw new DeltaCloudClientException(e);
			}
			return true;
		}
		return false;
	}

	private String nodeToString(Node node) throws DeltaCloudClientException {
		try {
			StringWriter writer = new StringWriter();
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.transform(new DOMSource(node), new StreamResult(writer));
			return writer.toString();
		} catch (TransformerException e) {
			throw new DeltaCloudClientException("Error transforming node to string", e);
		}
	}
}
