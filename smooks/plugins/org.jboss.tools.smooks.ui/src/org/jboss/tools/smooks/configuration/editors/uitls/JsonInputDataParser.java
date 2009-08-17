/**
 * 
 */
package org.jboss.tools.smooks.configuration.editors.uitls;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.DocumentException;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.XMLObjectAnalyzer;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.json.Key;
import org.jboss.tools.smooks.model.json.KeyMap;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.milyn.Smooks;
import org.milyn.SmooksUtil;
import org.milyn.cdr.Parameter;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.json.JSONReader;
import org.milyn.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Dart
 * 
 */
public class JsonInputDataParser {

	public static final String LINK_JSON_READER = "linkJSONReader";
	public static final String KEY = "_key_";
	public static final String NULL_REPLACE = "nullReplace";
	public static final String ENCODING2 = "encoding";
	public static final String SPACE_REPLACE = "spaceReplace";
	public static final String PREFIX_ON_NUMERIC = "prefixOnNumeric";
	public static final String ILLEGAL_REPLACE = "illegalReplace";
	public static final String ARRAY_ELEMENT_NAME = "arrayElementName";
	public static final String ROOT_NAME = "rootName";

	public IXMLStructuredObject parseJsonFile(InputStream inputStream, JsonReader reader)
			throws ParserConfigurationException, DocumentException {
		String rootName = null;
		String arrayElementName = null;
		String keyWhitspaceReplacement = null;
		String keyPrefixOnNumeric = null;
		String illegalElementNameCharReplacement = null;
		String nullValueReplacement = null;
		String encoding = null;
		Map<String, String> keyMap = new HashMap<String, String>();
		if (reader == null)
			return null;
		rootName = reader.getRootName();
		arrayElementName = reader.getArrayElementName();
		keyPrefixOnNumeric = reader.getKeyPrefixOnNumeric();
		keyWhitspaceReplacement = reader.getKeyWhitspaceReplacement();
		illegalElementNameCharReplacement = reader.getIllegalElementNameCharReplacement();
		nullValueReplacement = reader.getNullValueReplacement();
		encoding = reader.getEncoding();
		KeyMap km = reader.getKeyMap();
		if (km != null) {
			List<Key> keyList = km.getKey();
			for (Iterator<?> iterator = keyList.iterator(); iterator.hasNext();) {
				Key key = (Key) iterator.next();
				keyMap.put(key.getFrom(), key.getTo());
			}
		}
		return this.parseJsonFile(inputStream, rootName, arrayElementName, keyWhitspaceReplacement, keyPrefixOnNumeric,
				illegalElementNameCharReplacement, nullValueReplacement, keyMap, encoding);
	}

	public IXMLStructuredObject parseJsonFile(InputStream stream, InputType inputType,
			SmooksResourceListType resourceList) throws FileNotFoundException, ParserConfigurationException,
			DocumentException, InvocationTargetException {
		List<ParamType> paramList = inputType.getParam();
		String rootName = null;
		String arrayElementName = null;
		String keyWhitspaceReplacement = null;
		String keyPrefixOnNumeric = null;
		String illegalElementNameCharReplacement = null;
		String nullValueReplacement = null;
		String encoding = null;
		Map<String, String> keyMap = new HashMap<String, String>();

		for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (paramType.getName().equals(LINK_JSON_READER)) {
				if (paramType.getValue().equalsIgnoreCase("true") && resourceList != null) {
					List<AbstractReader> readers = resourceList.getAbstractReader();
					int count = 0;
					int index = -1;
					for (Iterator<?> iterator2 = readers.iterator(); iterator2.hasNext();) {
						AbstractReader abstractReader = (AbstractReader) iterator2.next();
						if (abstractReader instanceof JsonReader) {
							count++;
							if (index == -1) {
								index = readers.indexOf(abstractReader);
							}
						}
					}

					if (count > 1) {
						// throw new
						// RuntimeException("The smooks config file should have only one JSON reader");
					}
					if (index != -1) {
						return parseJsonFile(stream, (JsonReader) readers.get(index));
					}

				}
			}
			if (paramType.getName().equals(ROOT_NAME)) {
				rootName = paramType.getValue();
			}
			if (paramType.getName().startsWith(KEY)) {
				String name = paramType.getName().substring(KEY.length(), paramType.getName().length());
				String value = paramType.getValue();
				keyMap.put(name, value);
			}
			if (paramType.getName().equals(ARRAY_ELEMENT_NAME)) {
				arrayElementName = paramType.getValue();
			}
			if (paramType.getName().equals(ILLEGAL_REPLACE)) {
				illegalElementNameCharReplacement = paramType.getValue();
			}
			if (paramType.getName().equals(PREFIX_ON_NUMERIC)) {
				keyPrefixOnNumeric = paramType.getValue();
			}
			if (paramType.getName().equals(SPACE_REPLACE)) {
				keyWhitspaceReplacement = paramType.getValue();
			}
			if (paramType.getName().equals(ENCODING2)) {
				encoding = paramType.getValue();
			}
			if (paramType.getName().equals(NULL_REPLACE)) {
				nullValueReplacement = paramType.getValue();
			}
		}

		return this.parseJsonFile(stream, rootName, arrayElementName, keyWhitspaceReplacement, keyPrefixOnNumeric,
				illegalElementNameCharReplacement, nullValueReplacement, keyMap, encoding);
	}

	public IXMLStructuredObject parseJsonFile(String filePath, InputType inputType, SmooksResourceListType resourceList)
			throws FileNotFoundException, ParserConfigurationException, DocumentException, InvocationTargetException {
		FileInputStream stream = new FileInputStream(filePath);
		return this.parseJsonFile(stream, inputType, resourceList);
	}

	public IXMLStructuredObject parseJsonFile(String filePath, String rootName, String arrayElementName,
			String keyWhitspaceReplacement, String keyPrefixOnNumeric, String illegalElementNameCharReplacement,
			String nullValueReplacement, Map<String, String> keyMap, String encoding) throws FileNotFoundException,
			ParserConfigurationException, DocumentException, InvocationTargetException {
		return this.parseJsonFile(new FileInputStream(SmooksUIUtils.parseFilePath(filePath)), rootName,
				arrayElementName, keyWhitspaceReplacement, keyPrefixOnNumeric, illegalElementNameCharReplacement,
				nullValueReplacement, keyMap, encoding);
	}

	public IXMLStructuredObject parseJsonFile(InputStream inputStream, String rootName, String arrayElementName,
			String keyWhitspaceReplacement, String keyPrefixOnNumeric, String illegalElementNameCharReplacement,
			String nullValueReplacement, Map<String, String> keyMap, String encoding)
			throws ParserConfigurationException, DocumentException {

		Smooks smooks = new Smooks();

		SmooksResourceConfiguration readerConfig = new SmooksResourceConfiguration("org.xml.sax.driver",
				JSONReader.class.getName());

		readerConfig.setParameter(ROOT_NAME, rootName);
		readerConfig.setParameter(ARRAY_ELEMENT_NAME, arrayElementName);
		if (keyWhitspaceReplacement != null) {
			readerConfig.setParameter("keyWhitspaceReplacement", keyWhitspaceReplacement);
		}
		if (keyPrefixOnNumeric != null) {
			readerConfig.setParameter("keyPrefixOnNumeric", keyPrefixOnNumeric);
		}
		if (illegalElementNameCharReplacement != null) {
			readerConfig.setParameter("illegalElementNameCharReplacement", illegalElementNameCharReplacement);
		}
		if (nullValueReplacement != null) {
			readerConfig.setParameter("nullValueReplacement", nullValueReplacement);
		}

		if (keyMap != null) {
			readerConfig.setParameter(keyMapToParameter(keyMap));
		}

		readerConfig.setParameter(ENCODING2, encoding);

		SmooksUtil.registerResource(readerConfig, smooks);

		// Use a DOM result to capture the message model for the supplied CSV
		// message...
		DOMResult domResult = new DOMResult();

		// Filter the message through Smooks and capture the result as a DOM in
		// the domResult instance...
		smooks.filter(new StreamSource(inputStream), domResult);

		// Get the Document object from the domResult. This is the message
		// model!!!...
		Document model = (Document) domResult.getNode();
		StringWriter modelWriter = new StringWriter();
		XmlUtil.serialize(model, true, modelWriter);

		XMLObjectAnalyzer analyzer = new XMLObjectAnalyzer();
		ByteArrayInputStream byteinputStream = new ByteArrayInputStream(modelWriter.toString().getBytes());
		TagList tagList = analyzer.analyze(byteinputStream, null);

		try {
			if (byteinputStream != null) {
				byteinputStream.close();
				byteinputStream = null;
			}
			if (modelWriter != null) {
				modelWriter.close();
				modelWriter = null;
			}
			if (smooks != null) {
				smooks.close();
				smooks = null;
			}
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			model = null;
		} catch (Throwable t) {
			// ignore
//			t.printStackTrace();
		}

		return tagList;
	}

	protected Parameter keyMapToParameter(Map<String, String> keyMap) throws ParserConfigurationException {
		Parameter keyMapParam = new Parameter("keyMap", "dummyVal");
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Element keyMapElement = docBuilder.newDocument().createElement("keyMap");
		Set<Map.Entry<String, String>> keyMapEntries = keyMap.entrySet();

		for (Map.Entry<String, String> keyMapEntry : keyMapEntries) {
			Element keyElement = keyMapElement.getOwnerDocument().createElementNS("*", "key");
			keyElement.setAttribute("from", keyMapEntry.getKey());
			keyElement.setAttribute("to", keyMapEntry.getValue());
			keyMapElement.appendChild(keyElement);
		}

		keyMapParam.setXML(keyMapElement);

		return keyMapParam;
	}
}
