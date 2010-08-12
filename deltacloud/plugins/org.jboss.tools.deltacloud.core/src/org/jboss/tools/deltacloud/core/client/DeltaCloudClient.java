package org.jboss.tools.deltacloud.core.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DeltaCloudClient implements API 
{
	public static Logger logger = Logger.getLogger(DeltaCloudClient.class);
	
	private static enum DCNS
	{ 
		INSTANCES, REALMS, IMAGES, HARDWARE_PROFILES, START, STOP, REBOOT, DESTROY;
		
		@Override
		public String toString()
		{
			return "/" + name().toLowerCase();
		}
	} 
	
	private static enum RequestType { POST, GET, DELETE };
	
	private URL baseUrl;
	
	private String username;
	
	private String password;
	
	public DeltaCloudClient(URL url, String username, String password) throws MalformedURLException
	{
		
		logger.debug("Creating new Delta Cloud Client for Server: " + url);
		
		this.baseUrl = url;
		
		this.username = username;
		
		this.password = password;
	}

	private String sendRequest(String path, RequestType requestType) throws DeltaCloudClientException
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(baseUrl.getHost(), baseUrl.getPort()), new UsernamePasswordCredentials(username, password));
        
		String requestUrl = baseUrl.toString() + path;
		logger.debug("Sending Request to: " + requestUrl);
		
		try
		{
			HttpUriRequest request = null;
			if(requestType == RequestType.POST)
			{
				request = new HttpPost(requestUrl);
			}
			else if (requestType == RequestType.DELETE)
			{
				request = new HttpDelete(requestUrl);
			}
			else
			{
				request = new HttpGet(requestUrl);
			}
			
			request.setHeader("Accept", "application/xml");
			HttpResponse httpResponse = httpClient.execute(request);
			
			HttpEntity entity = httpResponse.getEntity();
			
			
			if (entity != null)
			{
				InputStream is = entity.getContent();
				String xml = readInputStreamToString(is);
				httpClient.getConnectionManager().shutdown();
				
				logger.debug("Response\n" + xml);
				return xml;
			}
		}
		catch(IOException e)
		{
			logger.error("Error processing request to: " + requestUrl, e);
			throw new DeltaCloudClientException("Error processing request to: " + requestUrl, e);
		} catch (Exception e) {
			throw new DeltaCloudClientException(e.getMessage());
		}
		throw new DeltaCloudClientException("Could not execute request to:" + requestUrl);
	}
	
	private static String readInputStreamToString(InputStream is) throws DeltaCloudClientException
	{
		try
		{
			try
			{
				if (is != null)
				{
					StringBuilder sb = new StringBuilder();
					String line;
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					while ((line = reader.readLine()) != null) 
					{
						sb.append(line).append("\n");	
					}
					return sb.toString();
				}
			}
			finally
			{
				is.close();
			}
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Error converting Response to String", e);
		}
		return "";
	}
	
	@Override
	public Instance createInstance(String imageId) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId;
		return buildInstance(sendRequest(DCNS.INSTANCES + query, RequestType.POST));
	}
	
	@Override
	public Instance createInstance(String imageId, String profileId, String realmId, String name) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId + "&hardware_profile_id=" + profileId + "&realm_id=" + realmId + "&name=" + name + "&commit=create";
		return buildInstance(sendRequest(DCNS.INSTANCES + query, RequestType.POST));
	}

	@Override
	public HardwareProfile listProfile(String profileId) throws DeltaCloudClientException 
	{
		String request = DCNS.HARDWARE_PROFILES + "/" + profileId;
		return JAXB.unmarshal(sendRequest(request, RequestType.GET), HardwareProfile.class);
	}

	@Override
	public List<HardwareProfile> listProfiles() throws DeltaCloudClientException 
	{
		return listDeltaCloudObjects(HardwareProfile.class, DCNS.HARDWARE_PROFILES.toString(), "hardware_profile");
	}
	
	@Override
	public List<Image> listImages() throws DeltaCloudClientException 
	{
		return listDeltaCloudObjects(Image.class, DCNS.IMAGES.toString(), "image");
	}

	@Override
	public Image listImages(String imageId) throws DeltaCloudClientException 
	{
		return JAXB.unmarshal(sendRequest(DCNS.IMAGES + "/" + imageId, RequestType.GET), Image.class);
	}

	@Override
	public List<Instance> listInstances() throws DeltaCloudClientException 
	{
		
		return listDeltaCloudObjects(Instance.class, DCNS.INSTANCES.toString(), "instance");
	}

	@Override
	public Instance listInstances(String instanceId) throws DeltaCloudClientException 
	{
		return buildInstance(sendRequest(DCNS.INSTANCES + "/" + instanceId, RequestType.GET));
	}
	
	@Override
	public List<Realm> listRealms() throws DeltaCloudClientException 
	{
		return listDeltaCloudObjects(Realm.class, DCNS.REALMS.toString(), "realm");
	}

	@Override
	public Realm listRealms(String realmId) throws DeltaCloudClientException 
	{
		return JAXB.unmarshal(sendRequest(DCNS.REALMS + "/" + realmId, RequestType.GET), Realm.class);
	}

	@Override
	public void rebootInstance(String instanceId) throws DeltaCloudClientException
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.REBOOT, RequestType.GET);
	}

	@Override
	public void shutdownInstance(String instanceId) throws DeltaCloudClientException 
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.STOP, RequestType.GET);
	}
	
	@Override
	public void startInstance(String instanceId) throws DeltaCloudClientException 
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.START, RequestType.GET);
	}

	@Override
	public void destroyInstance(String instanceId) throws DeltaCloudClientException
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId, RequestType.DELETE);
	}
	
	private void checkForErrors(Document d) throws DeltaCloudClientException
	{
		NodeList n = d.getElementsByTagName("error");
		for (int i = 0; i < n.getLength();)
		{
			Node node = n.item(i);
			String status = node.getAttributes().getNamedItem("status").getNodeValue();
			if (status.equals("403"))
				throw new DeltaCloudAuthException("Authorization error");
			else
				throw new DeltaCloudClientException("Connection error");
		}
	}
	
	private Instance buildInstance(String xml) throws DeltaCloudClientException
	{
		try
		{
			Instance instance = JAXB.unmarshal(new StringReader(xml), Instance.class);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(xml)));
		
			checkForErrors(document);
			
			instance.setImageId(getIdFromHref(getAttributeValues(document, "image", "href").get(0))); //$NON-NLS-1$ //$NON-NLS-2$
			instance.setProfileId(getIdFromHref(getAttributeValues(document, "hardware_profile", "href").get(0))); //$NON-NLS-1$ //$NON-NLS-2$
			getProfileProperties(instance, getPropertyNodes(document, "hardware_profile")); //$NON-NLS-1$
			instance.setRealmId(getIdFromHref(getAttributeValues(document, "realm", "href").get(0))); //$NON-NLS-1$ //$NON-NLS-2$
			instance.setState(getElementText(document, "state").get(0)); //$NON-NLS-1$
			
			ArrayList<Instance.Action> actions = new ArrayList<Instance.Action>();
			for(String s : getAttributeValues(document, "link", "rel")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				actions.add(Instance.Action.valueOf(s.toUpperCase()));
			}
			instance.setActions(actions);
			
			return instance;
		}
		catch (DeltaCloudClientException e) {
			throw e;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	private HardwareProfile buildHardwareProfile(String xml) throws DeltaCloudClientException
	{
		try
		{
			HardwareProfile profile = JAXB.unmarshal(new StringReader(xml), HardwareProfile.class);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(xml)));
				
			checkForErrors(document);
			
			List<Node> nodes = getPropertyNodes(document, "hardware_profile"); //$NON-NLS-1$
			
			for (Node n : nodes) {
				Property p = new Property();
				p.setName(n.getAttributes().getNamedItem("name").getNodeValue()); //$NON-NLS-1$
				p.setValue(n.getAttributes().getNamedItem("value").getNodeValue()); //$NON-NLS-1$
				p.setUnit(n.getAttributes().getNamedItem("unit").getNodeValue()); //$NON-NLS-1$
				p.setKind(n.getAttributes().getNamedItem("kind").getNodeValue()) ; //$NON-NLS-1$
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
				}
				else if (p.getKind().equals("enum")) { //$NON-NLS-1$
					ArrayList<String> enums = new ArrayList<String>();
					NodeList children = n.getChildNodes();
					for (int i = 0; i < children.getLength(); ++i) {
						Node child = children.item(i);
						if (child.getNodeName().equals("enum")) { //$NON-NLS-1$
							NodeList enumChildren = child.getChildNodes();
							for (int j = 0; j < enumChildren.getLength(); ++j) {
								Node enumChild = enumChildren.item(j);
								if (enumChild.getNodeName().equals("entry")) {
									NamedNodeMap attrs = enumChild.getAttributes();
									Node x = attrs.getNamedItem("value");
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getAttributeValues(Document document, String elementName, String attributeName)
	{
		NodeList elements = document.getElementsByTagName(elementName);
		ArrayList<String> values = new ArrayList<String>();
		for(int i = 0; i < elements.getLength(); i++)
		{
			values.add(elements.item(i).getAttributes().getNamedItem(attributeName).getTextContent());
		}
		return values;
	}
	
	private List<String> getElementText(Document document, String elementName)
	{
		NodeList elements = document.getElementsByTagName(elementName);
		ArrayList<String> values = new ArrayList<String>();
		for(int i = 0; i < elements.getLength(); i++)
		{
			values.add(elements.item(i).getTextContent());
		}
		return values;
	}
	
	private List<Node> getPropertyNodes(Document document, String elementName)
	{
		NodeList elements = document.getElementsByTagName(elementName);
		ArrayList<Node> values = new ArrayList<Node>();
		for(int i = 0; i < elements.getLength(); i++)
		{
			NodeList children = elements.item(i).getChildNodes();
			for (int j = 0; j < children.getLength(); ++j) 
			{
				Node child = children.item(j);
				if (child.getNodeName().equals("property")) { //$NON-NLS-1$
					values.add(child);
				}
			}
		}
		return values;
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
	
	private String getIdFromHref(String href)
	{
		return href.substring(href.lastIndexOf("/") + 1, href.length());
	}
	
	private <T extends DeltaCloudObject> List<T> listDeltaCloudObjects(Class<T> clazz, String path, String elementName) throws DeltaCloudClientException 
	{
		try
		{
			InputSource is = new InputSource(new StringReader(sendRequest(path, RequestType.GET)));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			
			checkForErrors(document);
			
			document.getElementsByTagName(path).toString(); 
						
			ArrayList<T> dco = new ArrayList<T>();
			
			NodeList nodeList = document.getElementsByTagName(elementName);
			for(int i = 0; i < nodeList.getLength(); i ++)
			{
				dco.add(buildDeltaCloudObject(clazz, nodeList.item(i)));
			}
			return dco;
		} catch (DeltaCloudClientException e) 
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Could not list object of type " + clazz, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Object> T buildDeltaCloudObject(Class<T> clazz, Node node) throws DeltaCloudClientException
	{
		if(clazz.equals(Instance.class))
		{
			return (T) buildInstance(nodeToString(node));
		}
		else if (clazz.equals(HardwareProfile.class))
		{
			return (T) buildHardwareProfile(nodeToString(node));
		}	
		else
		{
			return JAXB.unmarshal(new StringReader(nodeToString(node)), clazz);
		}
	}

    public boolean performInstanceAction(String instanceId, String action) throws DeltaCloudClientException
    {
            Instance instance = listInstances(instanceId);
            if(instance.getActionNames().contains(action))
            {
                    String request = DCNS.INSTANCES + "/" + instanceId + "/" + action.toLowerCase();
                    sendRequest(request, RequestType.POST);
                    return true;
            }
            return false;
    }
	
	private String nodeToString(Node node) throws DeltaCloudClientException
	{
		try 
		{
			StringWriter writer = new StringWriter();
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.transform(new DOMSource(node), new StreamResult(writer));
			return writer.toString();
		} 
		catch (TransformerException e) 
		{
			throw new DeltaCloudClientException("Error transforming node to string", e);
		}
		
	}

}
