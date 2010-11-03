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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DeltaCloudClient implements API {

	private static final String BASEURL_API = "/api";
	private static final String PEM_FILE_SUFFIX = "pem";
	private static final String DOCUMENT_ELEMENT_DRIVER = "driver";
	private static final String DOCUMENT_ELEMENT_API = "api";

	public static Logger logger = Logger.getLogger(DeltaCloudClient.class);

	public static enum DeltaCloudType {
		UNKNOWN, MOCK, EC2
	}

	private static enum DCNS {
		TYPE {
			public String getResourceName() {
				return "";
			}
		}
		,
		INSTANCES
		, REALMS
		, IMAGES
		, HARDWARE_PROFILES
		, KEYS
		, START
		, STOP
		, REBOOT
		, DESTROY;

		@Override
		public String toString() {
			return "/" + getResourceName();
		}

		public String getResourceName() {
			return name().toLowerCase();
		}
	}

	private URL baseUrl;
	private String username;
	private String password;

	public DeltaCloudClient(String url) throws MalformedURLException {
		this(url, null, null);
	}

	public DeltaCloudClient(String url, String username, String password) throws MalformedURLException {

		logger.debug("Creating new Delta Cloud Client for Server: " + url);

		this.baseUrl = new URL(url + BASEURL_API);
		this.username = username;
		this.password = password;
	}

	private String sendRequest(String path, HttpMethod httpMethod) throws DeltaCloudClientException {
		DefaultHttpClient httpClient = addCredentials(new DefaultHttpClient());
		String requestUrl = baseUrl.toString() + path;
		logger.debug("Sending Request to: " + requestUrl);

		try {
			HttpUriRequest request = getRequest(httpMethod, requestUrl);
			HttpResponse httpResponse = httpClient.execute(request);
			throwOnHttpErrors(requestUrl, httpResponse);
			return getResponse(httpResponse.getEntity());
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (IOException e) {
			logger.error("Error processing request to: " + requestUrl, e);
			throw new DeltaCloudClientException("Error processing request to: " + requestUrl, e);
		} catch (Exception e) {
			throw new DeltaCloudClientException(e.getMessage());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	private void throwOnHttpErrors(String requestUrl, HttpResponse httpResponse) throws DeltaCloudClientException {
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (HttpStatusCode.OK.isStatus(statusCode)) {
			return;
		} else if (HttpStatusCode.FORBIDDEN.isStatus(statusCode)) {
			throw new DeltaCloudAuthException(
					MessageFormat.format("The server reported an authorization error \"{0}\" on requesting \"{1}\"",
							httpResponse.getStatusLine().getReasonPhrase(), requestUrl));
		} else if (HttpStatusCode.NOT_FOUND.isStatus(statusCode)) {
			throw new DeltaCloudNotFoundException(
						MessageFormat.format("The server could not find the resource \"{0}\"", requestUrl));
		} else if (HttpStatusRange.CLIENT_ERROR.isInRange(statusCode)
				|| HttpStatusRange.SERVER_ERROR.isInRange(statusCode)) {
			throw new DeltaCloudClientException(
					MessageFormat.format("The server reported an error \"{0}\" on requesting \"{1}\"",
							httpResponse.getStatusLine().getReasonPhrase(), requestUrl));
		}
	}

	private String getResponse(HttpEntity entity) throws IOException,
			DeltaCloudClientException {
		if (entity == null) {
			return null;
		}
		String xml = readInputStreamToString(entity.getContent());
		logger.debug("Response\n" + xml);
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
	 */
	protected HttpUriRequest getRequest(HttpMethod httpMethod, String requestUrl) {
		HttpUriRequest request = null;
		switch (httpMethod) {
		case POST:
			request = new HttpPost(requestUrl);
			break;
		case DELETE:
			request = new HttpDelete(requestUrl);
			break;
		case GET:
		default:
			request = new HttpGet(requestUrl);
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
	 */
	private DefaultHttpClient addCredentials(DefaultHttpClient httpClient) {
		if (username != null && password != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(baseUrl.getHost(), baseUrl.getPort()),
					new UsernamePasswordCredentials(username, password));
		}
		return httpClient;
	}

	private static String readInputStreamToString(InputStream is) throws DeltaCloudClientException {
		try {
			try {
				if (is != null) {
					StringBuilder sb = new StringBuilder();
					String line;

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
			throw new DeltaCloudClientException("Error converting Response to String", e);
		}
		return "";
	}

	public DeltaCloudType getServerType() {
		DeltaCloudType serverType = DeltaCloudType.UNKNOWN;
		try {
			String query = "?format=xml";
			String apiResponse = sendRequest(DCNS.TYPE + query, HttpMethod.GET);
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(apiResponse)));

			NodeList elements = document.getElementsByTagName(DOCUMENT_ELEMENT_API);
			if (elements.getLength() > 0) {
				Node n = elements.item(0);
				Node driver = n.getAttributes().getNamedItem(DOCUMENT_ELEMENT_DRIVER);
				if (driver != null) {
					String driverValue = driver.getNodeValue();
					serverType = DeltaCloudType.valueOf(driverValue.toUpperCase());
				}
			}
		} catch (Exception e) {
			serverType = DeltaCloudType.UNKNOWN;
		}
		return serverType;
	}

	@Override
	public Instance createInstance(String imageId) throws DeltaCloudClientException {
		String query = "?image_id=" + imageId;
		return buildInstance(sendRequest(DCNS.INSTANCES + query, HttpMethod.POST));
	}

	@Override
	public Instance createInstance(String imageId, String profileId, String realmId, String name)
			throws DeltaCloudClientException {
		return createInstance(imageId, profileId, realmId, name, null, null, null);
	}

	public Instance createInstance(String imageId, String profileId, String realmId, String name, String memory,
			String storage) throws DeltaCloudClientException {
		return createInstance(imageId, profileId, realmId, name, null, memory, storage);
	}

	public Instance createInstance(String imageId, String profileId, String realmId, String name, String keyname,
			String memory, String storage) throws DeltaCloudClientException {
		String query = "?image_id=" + imageId + "&hwp_id=" + profileId + "&realm_id=" + realmId + "&name=" + name;
		if (memory != null)
			query += "&hwp_memory=" + memory;
		if (storage != null)
			query += "&hwp_storage=" + storage;
		if (keyname != null)
			query += "&keyname=" + keyname;
		query += "&commit=create";
		return buildInstance(sendRequest(DCNS.INSTANCES + query, HttpMethod.POST));
	}

	@Override
	public HardwareProfile listProfile(String profileId) throws DeltaCloudClientException {
		String request = DCNS.HARDWARE_PROFILES + "/" + profileId;
		return buildDeltaCloudObject(HardwareProfile.class, sendRequest(request, HttpMethod.GET));
	}

	@Override
	public List<HardwareProfile> listProfiles() throws DeltaCloudClientException {
		return listDeltaCloudObjects(HardwareProfile.class, DCNS.HARDWARE_PROFILES.toString(), "hardware_profile");
	}

	@Override
	public List<Image> listImages() throws DeltaCloudClientException {
		return listDeltaCloudObjects(Image.class, DCNS.IMAGES.toString(), "image");
	}

	@Override
	public Image listImages(String imageId) throws DeltaCloudClientException {
		return JAXB.unmarshal(new StringReader(sendRequest(DCNS.IMAGES + "/" + imageId, HttpMethod.GET)), Image.class);
	}

	@Override
	public List<Instance> listInstances() throws DeltaCloudClientException {
		return listDeltaCloudObjects(Instance.class, DCNS.INSTANCES.toString(), "instance");
	}

	@Override
	public Instance listInstances(String instanceId) throws DeltaCloudClientException {
		return buildInstance(sendRequest(DCNS.INSTANCES + "/" + instanceId, HttpMethod.GET));
	}

	@Override
	public List<Realm> listRealms() throws DeltaCloudClientException {
		return listDeltaCloudObjects(Realm.class, DCNS.REALMS.toString(), "realm");
	}

	@Override
	public Realm listRealms(String realmId) throws DeltaCloudClientException {
		return JAXB.unmarshal(new StringReader(sendRequest(DCNS.REALMS + "/" + realmId, HttpMethod.GET)), Realm.class);
	}

	/**
	 * Retrieves a key for a given name on the deltacloud server and stores it
	 * in the file at the given path. The file gets created if the file path
	 * does not exist yet.
	 * 
	 * @param keyname
	 *            the name of the key to retrieve from the server
	 * @param keyStoreLocation
	 *            the path to the file to store the key in
	 * @throws DeltaCloudClientException
	 *             the delta cloud client exception
	 */
	public void createKey(String keyname, String keyStoreLocation) throws DeltaCloudClientException {
		String xml = sendRequest(DCNS.KEYS + "?name=" + keyname, HttpMethod.POST);
		try {
			String key = trimKey(getKey(xml));
			File keyFile = createKeyFile(keyname, keyStoreLocation);
			storeKey(key, keyFile);
		} catch (Exception e) {
			throw new DeltaCloudClientException(e);
		}
	}

	private void storeKey(String key, File keyFile) throws IOException {
		FileWriter w = new FileWriter(keyFile);
		w.write(key);
		w.close();
	}

	private String trimKey(List<String> keyText) throws IOException {
		StringBuffer sb = new StringBuffer();
		String line;
		BufferedReader reader = new BufferedReader(new StringReader(keyText.get(0)));
		while ((line = reader.readLine()) != null) {
			// We must trim off the white-space from the xml
			// Complete white-space lines are to be ignored.
			String trimmedLine = line.trim();
			if (trimmedLine.length() > 0) {
				sb.append(trimmedLine).append("\n");
			}
		}
		return sb.toString();
	}

	private List<String> getKey(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(xml)));
		List<String> keyText = getElementText(document, PEM_FILE_SUFFIX); //$NON-NLS-1$
		return keyText;
	}

	private File createKeyFile(String keyname, String keyStoreLocation) throws IOException {
		File keyFile = Path.fromOSString(keyStoreLocation).append(keyname + "." + PEM_FILE_SUFFIX).toFile(); //$NON-NLS-1$
		if (!keyFile.exists()) {
			keyFile.createNewFile();
		}
		keyFile.setReadable(false, false);
		keyFile.setWritable(true, true);
		keyFile.setReadable(true, true);
		return keyFile;
	}

	public void deleteKey(String keyname) throws DeltaCloudClientException {
		sendRequest(DCNS.KEYS + "/" + keyname, HttpMethod.DELETE);
	}

	@Override
	public void rebootInstance(String instanceId) throws DeltaCloudClientException {
		/**
		 * shouldn't that be PUT? changing resource states == PUT!
		 */
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.REBOOT, HttpMethod.GET);
	}

	@Override
	public void shutdownInstance(String instanceId) throws DeltaCloudClientException {
		/**
		 * shouldn't that be PUT? changing resource states == PUT!
		 */
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.STOP, HttpMethod.POST);
	}

	@Override
	public void startInstance(String instanceId) throws DeltaCloudClientException {
		/**
		 * shouldn't that be PUT? changing resource states == PUT!
		 */
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.START, HttpMethod.POST);
	}

	@Override
	public void destroyInstance(String instanceId) throws DeltaCloudClientException {
		sendRequest(DCNS.INSTANCES + "/" + instanceId, HttpMethod.DELETE);
	}

	private Instance buildInstance(String xml) throws DeltaCloudClientException {
		try {
			Instance instance = JAXB.unmarshal(new StringReader(xml), Instance.class);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(xml)));

			instance.setImageId(getIdFromHref(getAttributeValues(document, "image", "href").get(0))); //$NON-NLS-1$ //$NON-NLS-2$
			instance.setProfileId(getIdFromHref(getAttributeValues(document, "hardware_profile", "href").get(0))); //$NON-NLS-1$ //$NON-NLS-2$
			getProfileProperties(instance, getPropertyNodes(document, "hardware_profile")); //$NON-NLS-1$
			instance.setRealmId(getIdFromHref(getAttributeValues(document, "realm", "href").get(0))); //$NON-NLS-1$ //$NON-NLS-2$
			instance.setState(getElementText(document, "state").get(0)); //$NON-NLS-1$
			getAuthentication(document, instance);
			instance.setActions(createInstanceActions(instance, document));

			return instance;
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			DeltaCloudClientException newException = new DeltaCloudClientException(e.getLocalizedMessage());
			throw newException;
		}
	}

	private List<InstanceAction> createInstanceActions(Instance instance, Document document)
			throws DeltaCloudClientException {
		final List<InstanceAction> actions = new ArrayList<InstanceAction>();
		forEachNode(document, "link", new INodeVisitor() {

			@Override
			public void visit(Node node) throws Exception {
				NamedNodeMap attributes = node.getAttributes();
				String name = getAttributeTextContent("rel", attributes, node);
				String url = getAttributeTextContent("href", attributes, node);
				String method = getAttributeTextContent("method", attributes, node);
				actions.add(new InstanceAction(name, stripBaseUrl(url), method));
			}
		});
		return actions;
	}

	private String stripBaseUrl(String url) throws DeltaCloudClientException {
		String baseUrlString = baseUrl.toString();
		if (url.indexOf(baseUrlString) != 0) {
			throw new DeltaCloudClientException(MessageFormat.format(
					"Resource at {0} is not child resource of root resource at {1}", url, baseUrl));
		}

		return url.substring(baseUrlString.length());
	}

	private String getAttributeTextContent(String attributeName, NamedNodeMap namedNodeMap, Node node)
			throws DeltaCloudClientException {
		Node attributeNode = namedNodeMap.getNamedItem(attributeName);
		if (attributeNode == null) {
			throw new DeltaCloudClientException(MessageFormat.format("Could not find attribute {0} in node {1}",
					attributeName, node.getNodeName()));
		}

		return attributeNode.getTextContent();
	}

	private HardwareProfile buildHardwareProfile(String xml) throws DeltaCloudClientException {
		try {
			HardwareProfile profile = JAXB.unmarshal(new StringReader(xml), HardwareProfile.class);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(xml)));

			List<Node> nodes = getPropertyNodes(document, "hardware_profile"); //$NON-NLS-1$

			for (Node n : nodes) {
				Property p = new Property();
				p.setName(n.getAttributes().getNamedItem("name").getNodeValue()); //$NON-NLS-1$
				p.setValue(n.getAttributes().getNamedItem("value").getNodeValue()); //$NON-NLS-1$
				p.setUnit(n.getAttributes().getNamedItem("unit").getNodeValue()); //$NON-NLS-1$
				p.setKind(n.getAttributes().getNamedItem("kind").getNodeValue()); //$NON-NLS-1$
				if (p.getKind().equals("range")) { //$NON-NLS-1$
					NodeList children = n.getChildNodes();
					for (int i = 0; i < children.getLength(); ++i) {
						Node child = children.item(i);
						if (child.getNodeName().equals("range")) { //$NON-NLS-1$
							String first = child.getAttributes().getNamedItem("first").getNodeValue(); //$NON-NLS-1$
							String last = child.getAttributes().getNamedItem("last").getNodeValue(); //$NON-NLS-1$
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
									enums.add(enumChild.getAttributes().getNamedItem("value").getNodeValue()); //$NON-NLS-1$
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

	private void forEachNode(Document document, String tagName, INodeVisitor visitor) throws DeltaCloudClientException {
		NodeList elements = document.getElementsByTagName(tagName);
		for (int i = 0; i < elements.getLength(); i++) {
			try {
				visitor.visit(elements.item(i));
			} catch (Exception e) {
				throw new DeltaCloudClientException(e.getMessage());
			}
		}
	}

	private List<String> getAttributeValues(Document document, String elementName, String attributeName) {
		NodeList elements = document.getElementsByTagName(elementName);
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < elements.getLength(); i++) {
			values.add(elements.item(i).getAttributes().getNamedItem(attributeName).getTextContent());
		}
		return values;
	}

	private List<String> getElementText(Document document, String elementName) {
		NodeList elements = document.getElementsByTagName(elementName);
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < elements.getLength(); i++) {
			values.add(elements.item(i).getTextContent());
		}
		return values;
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

	private void getAuthentication(Document document, Instance instance) {
		NodeList elements = document.getElementsByTagName("authentication");
		for (int i = 0; i < elements.getLength(); i++) {
			Node element = elements.item(i);
			NamedNodeMap attrs = element.getAttributes();
			Node type = attrs.getNamedItem("type"); //$NON-NLS-1$
			if (type.getNodeValue().equals("key")) { //$NON-NLS-1$
				NodeList children = element.getChildNodes();
				for (int j = 0; j < children.getLength(); ++j) {
					Node child = children.item(j);
					if (child.getNodeName().equals("login")) { //$NON-NLS-1$
						NodeList loginChildren = child.getChildNodes();
						for (int k = 0; k < loginChildren.getLength(); ++k) {
							Node loginChild = loginChildren.item(k);
							if (loginChild.getNodeName().equals("keyname")) { //$NON-NLS-1$
								instance.setKey(loginChild.getTextContent());
							}
						}
					}
				}
			}
		}
	}

	private void getProfileProperties(Instance instance, List<Node> propertyNodes) {
		if (propertyNodes != null) {
			for (Iterator<Node> i = propertyNodes.iterator(); i.hasNext();) {
				Node n = i.next();
				NamedNodeMap attrs = n.getAttributes();
				String name = attrs.getNamedItem("name").getNodeValue(); //$NON-NLS-1$
				if (name.equals("memory")) { //$NON-NLS-1$
					String memory = attrs.getNamedItem("value").getNodeValue(); //$NON-NLS-1$
					if (attrs.getNamedItem("unit") != null) { //$NON-NLS-1$
						memory += " " + attrs.getNamedItem("unit").getNodeValue(); //$NON-NLS-1$
					}
					instance.setMemory(memory);
				} else if (name.equals("storage")) { //$NON-NLS-1$
					String storage = attrs.getNamedItem("value").getNodeValue(); //$NON-NLS-1$
					if (attrs.getNamedItem("unit") != null) { //$NON-NLS-1$
						storage += " " + attrs.getNamedItem("unit").getNodeValue(); //$NON-NLS-1$
					}
					instance.setStorage(storage);
				} else if (name.equals("cpu")) { //$NON-NLS-1$
					String cpu = attrs.getNamedItem("value").getNodeValue(); //$NON-NLS-1$
					instance.setCPU(cpu);
				}
			}
		}
	}

	private String getIdFromHref(String href) {
		return href.substring(href.lastIndexOf("/") + 1, href.length());
	}

	private <T extends DeltaCloudObject> List<T> listDeltaCloudObjects(Class<T> clazz, String path, String elementName)
			throws DeltaCloudClientException {
		try {
			Document document = getDocument(path);
			ArrayList<T> dco = new ArrayList<T>();
			NodeList nodeList = document.getElementsByTagName(elementName);
			for (int i = 0; i < nodeList.getLength(); i++) {
				dco.add(buildDeltaCloudObject(clazz, nodeToString(nodeList.item(i))));
			}
			return dco;
		} catch (DeltaCloudClientException e) {
			throw e;
		} catch (Exception e) {
			throw new DeltaCloudClientException("Could not list object of type " + clazz, e);
		}
	}

	private Document getDocument(String path) throws DeltaCloudClientException, ParserConfigurationException,
			SAXException, IOException {
		InputSource is = new InputSource(new StringReader(sendRequest(path, HttpMethod.GET)));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(is);
		return document;
	}

	@SuppressWarnings("unchecked")
	private <T extends Object> T buildDeltaCloudObject(Class<T> clazz, String node) throws DeltaCloudClientException {
		if (clazz.equals(Instance.class)) {
			return (T) buildInstance(node);
		} else if (clazz.equals(HardwareProfile.class)) {
			return (T) buildHardwareProfile(node);
		} else {
			return JAXB.unmarshal(new StringReader(node), clazz);
		}
	}

	/**
	 * Performs an action, indicated by a given action id, on a instance with a
	 * given instance id.
	 * <p>
	 * Retrieves the instance with the given id, checks for the actions that are
	 * available to it and performs the given action only if it is available. It
	 * is important to note that this method performs several REST calls to the
	 * server.
	 * 
	 * @param instanceId
	 *            the id of the instance to perfom the action on
	 * @param action
	 *            the action id to perform
	 * @return true, if the action was performed successfully
	 * @throws DeltaCloudClientException
	 *             indicates that an error occured while performing the action
	 * 
	 * @see #startInstance(String)
	 * @see #shutdownInstance(String)
	 * @see #destroyInstance(String)
	 * @see #rebootInstance(String)
	 */
	public boolean performInstanceAction(String instanceId, String actionName) throws DeltaCloudClientException {
		Instance instance = listInstances(instanceId);
		InstanceAction action = instance.getAction(actionName);
		if (action != null) {
			sendRequest(action.getUrl(), action.getMethod());
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

	private interface INodeVisitor {
		public void visit(Node node) throws Exception;
	}

}
