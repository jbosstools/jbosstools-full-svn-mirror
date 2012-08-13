/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.util;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.mozilla.interfaces.nsIDOMCSSStyleDeclaration;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMElementCSSInlineStyle;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeStyleUtil {

	public static final String UNRESOLVED_IMAGE_PATH = "unresolved_image.gif"; //$NON-NLS-1$

	public static final String ATTRIBUTE_STYLE = "style"; //$NON-NLS-1$

	public static final String PARAMETER_POSITION = "position"; //$NON-NLS-1$
	public static final String PARAMETER_TOP = "top"; //$NON-NLS-1$
	public static final String PARAMETER_LEFT = "left"; //$NON-NLS-1$
	public static final String PARAMETER_WIDTH = "width"; //$NON-NLS-1$
	public static final String PARAMETER_HEIGHT = "height"; //$NON-NLS-1$
	public static final String PARAMETR_BACKGROND = "background"; //$NON-NLS-1$
	public static final String PARAMETR_VERTICAL_ALIGN = "vertical-align"; //$NON-NLS-1$

	public static final String VALUE_ABSOLUTE = "absolute"; //$NON-NLS-1$

	public static final String DOT_STRING = "."; //$NON-NLS-1$
	public static final String COLON_STRING = ":"; //$NON-NLS-1$
	public static final String SEMICOLON_STRING = ";"; //$NON-NLS-1$
	public static final String PX_STRING = "px"; //$NON-NLS-1$
	public static final String SPACE_STRING = " "; //$NON-NLS-1$
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$
	public static final String SINGLE_QUOTE_STRING = "\'"; //$NON-NLS-1$
	public static final String QUOTE_STRING = "\""; //$NON-NLS-1$
	/*
	 * https://issues.jboss.org/browse/JBIDE-10178
	 * Java regexp pattern to match css path from the url(..) construction.
	 * It's implied that the css string has only one URL in it.
	 * For the long string regexp could be updated:
	 * (.*) should be replaced with ([^;]*) 
	 */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$
	public static final Pattern CSS_URL_PATTERN = Pattern.compile("(?<=\\burl\\b)(?:[\\p{Space}]*\\()[\\p{Space}]*([^;]*)[\\p{Space}]*(?:\\)[\\p{Space}]*)(?=(?>[^\\)]*;|[^\\)]*))"); //$NON-NLS-1$
	public static final Pattern CSS_IMPORT_PATTERN = Pattern.compile("@import[\\p{Space}]+(?:\\burl\\b[\\p{Space}]*\\()*[\\p{Space}]*([^;]*)[\\p{Space}]*(?:\\)[\\p{Space}]*(?=(?>[^\\)]*;|[^\\)]*)))*"); //$NON-NLS-1$
	public static final Pattern CSS_URI_PATTERN = Pattern.compile("(?:\\\"{1}(.*)\\\"{1})|(?:\\'{1}(.*)\\'{1})"); //$NON-NLS-1$
	/*
	 * Pattern "|(//.*)" could be added at the end to remove single line comments.
	 */
	private static final String CSS_COMMENT_END = "*/"; //$NON-NLS-1$
	private static final String CSS_COMMENT_START = "/*"; //$NON-NLS-1$
	
	public static String ATTR_URL = "url"; //$NON-NLS-1$
	public static String OPEN_BRACKET = "("; //$NON-NLS-1$
	public static String CLOSE_BRACKET = ")"; //$NON-NLS-1$
	public static String FILE_PROTOCOL = "file:"; //$NON-NLS-1$
	public static String HTTP_PROTOCOL = "http:"; //$NON-NLS-1$
	public static String SLASH = "/"; //$NON-NLS-1$

	/**
	 * Returns CSS style declaration corresponding to the given {@code element}.
	 */
	public static nsIDOMCSSStyleDeclaration getStyle(nsIDOMElement element) {
    	nsIDOMElementCSSInlineStyle inlineStyle = 
				queryInterface(element, nsIDOMElementCSSInlineStyle.class);
		return inlineStyle.getStyle();
	}

	// sets parameter position in attribute style to absolute value
	public static void setAbsolute(Element sourceElement) {
		String style = sourceElement.getAttribute(ATTRIBUTE_STYLE);
		if (style == null) {
			style = EMPTY_STRING;
		} else { // remove old sizes
			style = deleteFromString(style, PARAMETER_POSITION, SEMICOLON_STRING);
		}
		if (style.length() > 0) {
			if (!style.endsWith(SEMICOLON_STRING))
				style += SEMICOLON_STRING;
		}

		style += SPACE_STRING + PARAMETER_POSITION + SPACE_STRING
				+ COLON_STRING + SPACE_STRING + VALUE_ABSOLUTE + SEMICOLON_STRING;

		sourceElement.setAttribute(ATTRIBUTE_STYLE, style);
	}

	// sets parameter position in absolute value
	public static String setAbsolute(String styleString) {
		String style = styleString;
		if (style == null) {
			style = EMPTY_STRING;
		} else { // remove old sizes
			style = deleteFromString(style, PARAMETER_POSITION, SEMICOLON_STRING);
		}
		if (style.length() > 0) {
			if (!style.endsWith(SEMICOLON_STRING))
				style += SEMICOLON_STRING;
		}

		style += SPACE_STRING + PARAMETER_POSITION + SPACE_STRING
				+ COLON_STRING + SPACE_STRING + VALUE_ABSOLUTE + SEMICOLON_STRING;

		return style;
	}

	// return true if parameter position was set to absolute
	public static boolean getAbsolute(Element sourceElement) {
		String style = sourceElement.getAttribute(ATTRIBUTE_STYLE);
		if (style == null) {
			return false;
		} else { // remove old sizes
			if (style.indexOf(VALUE_ABSOLUTE) >= 0)
				return true;
		}
		return false;
	}

	// return true if parameter position was set to absolute
	public static boolean getAbsolute(String style) {
		if (style == null) {
			return false;
		} else { // remove old sizes
			if (style.indexOf(VALUE_ABSOLUTE) >= 0)
				return true;
		}
		return false;
	}

	// return value of parameter described in sizeAttribute, for example
	// "style.width"
	public static int getSizeFromStyle(Element sourceElement, String sizeAttribute) {
		int dotPosition = sizeAttribute.indexOf(DOT_STRING);
		String attribute = sizeAttribute.substring(0, dotPosition);
		String parameter = sizeAttribute.substring(dotPosition + 1, sizeAttribute.length());

		String style = sourceElement.getAttribute(attribute);
		if (style == null || EMPTY_STRING.equals(style)) {
			return -1;
		}

		int parameterPosition = style.indexOf(parameter);
		if (parameterPosition >= 0) {
			int valuePosition = style.indexOf(COLON_STRING, parameterPosition);
			if (valuePosition >= 0) {
				int endPosition = style.indexOf(PX_STRING, valuePosition);
				if (endPosition >= 0) {
					return Integer.parseInt(style.substring(valuePosition + 1, endPosition).trim());
				}
			}
		}
		return -1;
	}

	// return value of parameter described in sizeAttribute, for example
	// "style.width"
	public static String getParameterFromStyle(Element sourceElement, String sizeAttribute) {
		int dotPosition = sizeAttribute.indexOf(DOT_STRING);
		String attribute = sizeAttribute.substring(0, dotPosition);
		String parameter = sizeAttribute.substring(dotPosition + 1, sizeAttribute.length());

		String style = sourceElement.getAttribute(attribute);
		if (style == null || EMPTY_STRING.equals(style))
			return null;

		int parameterPosition = style.indexOf(parameter);
		if (parameterPosition >= 0) {
			int valuePosition = style.indexOf(COLON_STRING, parameterPosition);
			if (valuePosition >= 0) {
				int endPosition = style.indexOf(PX_STRING, valuePosition);
				if (endPosition >= 0) {
					return style.substring(valuePosition + 1, endPosition).trim();
				}
			}
		}
		return null;
	}

	/**
	 * This method is used to get parameter value from <code>style</code> attribute. For instance, in case of
	 * style="width:65px; color:red" for parameter <code>color</code> method should return <code>red</code> string
	 * value.
	 * 
	 * @param styleAttr
	 *            the style attribute value
	 * @param parameter
	 *            the name of parameter of style attribute
	 * @return the parameter value
	 */
	public static String getParameterFromStyleAttribute(String style, String parameter) {
		if (style == null || EMPTY_STRING.equals(style)) {
			return null;
		}
		int parameterPosition = style.indexOf(parameter);
		if (parameterPosition >= 0) {
			int valuePosition = style.indexOf(COLON_STRING, parameterPosition);
			if (valuePosition >= 0) {
				int endPosition = style.indexOf(SEMICOLON_STRING, valuePosition);
				if (endPosition >= 0) {
					style = style.substring(valuePosition + 1, endPosition).trim();
					endPosition = style.indexOf(PX_STRING, valuePosition);
					if (endPosition >= 0) {
						return style.substring(valuePosition + 1, endPosition).trim();
					}
					return style;
				} else {
					// last parameter ends without closing semicolon symbol
					return style.substring(valuePosition + 1).trim();
				}
			}
		}

		return null;
	}

	// sets value of parameter described in sizeAttribute, for example
	// "style.width"
	public static void setParameterInStyle(Element sourceElement, String sizeAttribute, String value) {
		int dotPosition = sizeAttribute.indexOf(DOT_STRING);
		String attribute = sizeAttribute.substring(0, dotPosition);
		String parameter = sizeAttribute.substring(dotPosition + 1, sizeAttribute.length());

		String style = sourceElement.getAttribute(attribute);
		if (style == null) {
			style = EMPTY_STRING;
		} else { // remove old sizes
			style = deleteFromString(style, parameter, SEMICOLON_STRING);
		}
		if (style.length() > 0) {
			if (!style.endsWith(SEMICOLON_STRING))
				style += SEMICOLON_STRING;
		}

		style += SPACE_STRING + parameter + SPACE_STRING + COLON_STRING
				+ SPACE_STRING + value + SEMICOLON_STRING;

		sourceElement.setAttribute(attribute, style);
	}

	public static String setSizeInStyle(String style, String parameter, int size) {
		if (style == null) {
			style = EMPTY_STRING;
		} else { // remove old sizes
			style = deleteFromString(style, parameter, SEMICOLON_STRING);
		}
		if (style.length() > 0) {
			if (!style.endsWith(SEMICOLON_STRING))
				style += SEMICOLON_STRING;
		}

	style += SPACE_STRING + parameter + SPACE_STRING + COLON_STRING
		+ SPACE_STRING + size + PX_STRING + SEMICOLON_STRING;

		return style;
	}

	public static String setParameterInStyle(String style, String parameter, String value) {
		if (style == null) {
			style = EMPTY_STRING;
		} else { // remove old sizes
			style = deleteFromString(style, parameter, SEMICOLON_STRING);
		}
		if (style.length() > 0) {
			if (!style.endsWith(SEMICOLON_STRING))
				style += SEMICOLON_STRING;
		}

	style += SPACE_STRING + parameter + SPACE_STRING + COLON_STRING
		+ SPACE_STRING + value + SEMICOLON_STRING;

		return style;
	}

	// selets parameter from atribute style
	public static void deleteFromStyle(Element sourceElement, String begin, String end) {
		String style = sourceElement.getAttribute(ATTRIBUTE_STYLE);
		style = deleteFromString(style, begin, end);
		sourceElement.setAttribute(ATTRIBUTE_STYLE, style);
	}

	// selects parameter from attribute style
	public static String deleteFromString(String data, String begin, String end) {
		int startPosition = data.indexOf(begin);

		if (startPosition < 0)
			return data;

		int endPosition = data.indexOf(end, startPosition);

		String result = data.substring(0, startPosition).trim();
		if (endPosition > 0) {
			result += data.substring(endPosition + 1, data.length()).trim();
		}

		return result;
	}

	/**
	 * 
	 * @param value
	 *            Css string
	 * @param input
	 *            The editor input
	 * @return format style string
	 */
	public static String addFullPathIntoBackgroundValue(String value, IEditorInput input) {

		if (value.indexOf(FILE_PROTOCOL) != -1) {
			return value;
		}

		if (!new File(value).isAbsolute()) {
			value = getFilePath(input, value);
		}

		value = FILE_PROTOCOL + SLASH + SLASH + value.replace('\\', '/');
		URL url = null;
		try {
			url = new URL(value);
		} catch (MalformedURLException e) {
			return value;
		}

		return url.toString();
	}

	/**
	 * 
	 * @param nput
	 *            The editor input
	 * @param fileName
	 *            Relative path file
	 * @return Absolute path file
	 */
	public static String getFilePath(IEditorInput input, String fileName) {
		IPath inputPath = getInputParentPath(input);
		return inputPath.toOSString() + File.separator + fileName;
	}

	/**
	 * Gets the file path.
	 * 
	 * @param href_val
	 *            the href_val
	 * @param fileName
	 *            the file name
	 * 
	 * @return the file path
	 */
	public static String getFilePath(String href_val, String fileName) {
		IPath inputPath = getInputParentPath(href_val);
		return inputPath.toOSString() + File.separator + fileName;
	}

	/**
	 * 
	 * @param input
	 *            The editor input
	 * @return Path
	 */
	public static IPath getInputParentPath(IEditorInput input) {
		IPath inputPath = null;
		if (input instanceof ILocationProvider) {
			inputPath = ((ILocationProvider) input).getPath(input);
		} else if (input instanceof IFileEditorInput) {
			IFile inputFile = ((IFileEditorInput) input).getFile();
			if (inputFile != null) {
				inputPath = inputFile.getLocation();
			}
		}
		if (inputPath != null && !inputPath.isEmpty()) {
			inputPath = inputPath.removeLastSegments(1);
		}
		return inputPath;
	}

	/**
	 * Gets the href file path.
	 * 
	 * @param href_val
	 *            the href_val
	 * 
	 * @return href file path
	 */
	public static IPath getInputParentPath(String href_val) {
		IPath inputPath = null;
		inputPath = new Path(href_val);
		if (inputPath != null && !inputPath.isEmpty()) {
			/*
			 * Remove href trailing filename
			 */
			inputPath = inputPath.removeLastSegments(1);
		}
		return inputPath;
	}

	/**
	 * Adds full path for URL value
	 * 
	 * @param url the url
	 * @param pageContext VPE page context
	 * @return the full path string
	 */
	public static String addFullPathIntoURLValue(String url, VpePageContext pageContext) {
		String urls[] = url.split(ATTR_URL);
		if (urls.length == 1) {
			return url;
		}
		IFile file = getSourceFileFromPageContext(pageContext);
		for (int i = 1; i < urls.length; i++) {
			urls[i] = removeQuotesUpdate(urls[i]);
			String[] urlParts = splitURL(urls[i]);
			if (urlParts == null) {
				continue;
			}
			if (file != null) {
				urlParts[1] = processUrl(urlParts[1], file, true);
			}
			urls[i] = collectArrayInto1Str(urlParts);
		}
		return collectArrayInto1Str(urls);
	}

	/**
	 * Adds full path for URL value
	 * 
	 * @param url css url string
	 * @param href_val path to css file
	 * @return the full path string
	 */
	public static String addFullPathIntoURLValue(String url, String href_val) {
		String urls[] = url.split(ATTR_URL);
		if (urls.length == 1) {
			return url;
		}
		for (int i = 1; i < urls.length; i++) {
			urls[i] = removeQuotesUpdate(urls[i]);
			String[] urlParts = splitURL(urls[i]);
			if (urlParts == null) {
				continue;
			}
			IFile sourceFile = null;
			try {
				URL url1 = new URL(href_val);
				sourceFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(url1.getPath()));
			} catch (MalformedURLException e1) {
				// ignore
			}
			if (sourceFile != null) {
				urlParts[1] = processUrl(urlParts[1], sourceFile, true);
			} else {
				urlParts[1] = updateURLFilePath(urlParts[1], href_val);
				if (urlParts[1] == null) {
					continue;
				}
			}
			urls[i] = collectArrayInto1Str(urlParts);
		}
		return collectArrayInto1Str(urls);
	}

	private static String[] splitURL(String url) {
		/*
		 * https://issues.jboss.org/browse/JBIDE-10178
		 * The index of closing bracket was wrong.
		 * Thus replaced with java regexp. 
		 */	
		Matcher m = CSS_URL_PATTERN.matcher(url);
		String[] res = null;
		/*
		 * It's implied that the "url" string has only one URL in it.
		 */
		if (m.find()) {
			res = new String[3];
			/*
			 * Before URL
			 */
			res[0] = url.substring(0, m.start(1));
			/*
			 * The URL string itself
			 */
			res[1] = m.group(1);
			/*
			 * After the URL string
			 */
			res[2] = url.substring(m.end(1), url.length());
		}
		return res;
	}

	private static String removeQuotesUpdate(String url) {
		url = url.replace(SINGLE_QUOTE_STRING, EMPTY_STRING);
		url = url.replace(QUOTE_STRING, EMPTY_STRING);
		url = ATTR_URL + url;
		return url;
	}

	private static String collectArrayInto1Str(String arr[]) {
		String finalStr = EMPTY_STRING;
		for (int i = 0; i < arr.length; i++) {
			finalStr += arr[i];
		}
		return finalStr;
	}
	
	private static String updateURLFilePath(String filePath, String href_val) {
		try {
			URL url = new URL(filePath);
			// with url all ok
			return null;
		} catch (MalformedURLException e) {
			// ignore, continue work with url
		}
		if (filePath.indexOf(FILE_PROTOCOL) != -1) {
			return null;
		}
		if (!new File(filePath).isAbsolute()) {
			filePath = getFilePath(href_val, filePath);
		} else {
			filePath = FILE_PROTOCOL + SLASH + SLASH + filePath.replace('\\', '/');
		}
		URL url = null;
		try {
			url = new URL(filePath);
		} catch (MalformedURLException e) {
			return null;
		}
		filePath = url.toString();
		return filePath;
	}

	public static String getAbsoluteResourcePathUrl(String resourcePathInPlugin) {
		return FILE_PROTOCOL + SLASH + SLASH + SLASH 
				+ getAbsoluteResourcePath(resourcePathInPlugin).replace('\\', '/');
	}

	/**
	 * Adds the full path to image "src" attribute.
	 * 
	 * @param path
	 *            image "src" attribute value
	 * @param pageContext
	 *            the pageContext
	 * @param showUnresolvedImage
	 *            flag to display unresolved image
	 * 
	 * @return the full path to image "src" attribute
	 */
	public static String addFullPathToImgSrc(String path,
			VpePageContext pageContext, boolean showUnresolvedImage) {

		if (path == null) {
			if (showUnresolvedImage) {
				return FILE_PROTOCOL + SLASH + SLASH
						+ getAbsoluteResourcePath(UNRESOLVED_IMAGE_PATH).replace('\\', '/');
			} else {
				return EMPTY_STRING;
			}
		}

		IPath tagPath = new Path(path);
		if (tagPath.isEmpty()) {
			if (showUnresolvedImage) {
				return FILE_PROTOCOL + SLASH + SLASH
						+ getAbsoluteResourcePath(UNRESOLVED_IMAGE_PATH).replace('\\', '/');
			} else {
				return path.replace('\\', '/');
			}
		}

		String device = (tagPath.getDevice() == null ? tagPath.segment(0) : tagPath.getDevice());
		if (device != null && (HTTP_PROTOCOL.equalsIgnoreCase(device) || FILE_PROTOCOL.equalsIgnoreCase(device))) {
			if (showUnresolvedImage) {
				return FILE_PROTOCOL + SLASH + SLASH
						+ getAbsoluteResourcePath(UNRESOLVED_IMAGE_PATH).replace('\\', '/');
			} else {
				return path.replace('\\', '/');
			}
		}

		File locFile = tagPath.toFile();
		if (locFile.exists()) {
			return FILE_PROTOCOL + SLASH + SLASH + SLASH + locFile.getAbsolutePath().replace('\\', '/');
		}

		IEditorInput input = pageContext.getEditPart().getEditorInput();
		IPath inputPath = getInputParentPath(input);
		IPath imgPath = null;
		if (input instanceof ILocationProvider) {
			imgPath = inputPath.append(path);
		} else {
			IPath basePath = tagPath.isAbsolute() ? getRootPath(input) : inputPath;
			if (basePath != null) {
				imgPath = basePath.append(tagPath);
			}
		}

		if (imgPath != null && imgPath.toFile().exists()) {
			return FILE_PROTOCOL + SLASH + SLASH + SLASH + imgPath.toString();
		} else {
			IFile file = null;
			if (input instanceof IFileEditorInput) {
				file = ((IFileEditorInput) input).getFile();
			}

			if (null != file) {
				ResourceReference resourceReference = null;
				String pathCopy = path;
				if (SLASH.equals(path.substring(0, 1))) {
					resourceReference = pageContext.getRuntimeAbsoluteFolder(file);
					pathCopy = pathCopy.substring(1);
				} else {
					resourceReference = pageContext.getRuntimeRelativeFolder(file);
				}

				String location = null;
				if (resourceReference != null) {
					location = resourceReference.getLocation();
				}

				if (null == location && null != file.getLocation()) {
					location = file.getLocation().toFile().getParent();
				}

				if (null != location) {
					File f = new File(location + File.separator + pathCopy);
					if (f.exists()) {
						return FILE_PROTOCOL + SLASH + SLASH + SLASH + f.getPath().replace('\\', '/');
					}
				}
			}
		}
		if (showUnresolvedImage) {
			return FILE_PROTOCOL + SLASH + SLASH + getAbsoluteResourcePath(UNRESOLVED_IMAGE_PATH).replace('\\', '/');
		} else {
			return path.replace('\\', '/');
		}
	}

	/**
	 * Gets the root path of the web project.
	 * 
	 * @param input
	 *            the input
	 * 
	 * @return the root path
	 */
	public static IPath getRootPath(IEditorInput input) {
		IPath rootPath = null;
		if (input instanceof IFileEditorInput) {
			rootPath = getRootPath(((IFileEditorInput) input).getFile());
		}
		return rootPath;
	}
	
	public static IPath getRootPath(IFile inputFile) {
		IPath rootPath = null;
		IProject project = inputFile.getProject();
		if (project != null && project.isOpen()) {
			IModelNature modelNature = EclipseResourceUtil.getModelNature(project);
			if (modelNature != null) {
				XModel model = modelNature.getModel();
				String rootPathStr = WebProject.getInstance(model).getWebRootLocation();
				if (rootPathStr != null) {
					rootPath = new Path(rootPathStr);
				} else {
					rootPath = project.getLocation();
				}
			} else {
				rootPath = project.getLocation();
			}
		}
		return rootPath;
	}

	/**
	 * refresh style element
	 * 
	 * @param visualDomBuilder
	 * @param sourceElement
	 * @param oldStyleNode
	 * @return
	 */
	public static void refreshStyleElement(VpeVisualDomBuilder visualDomBuilder, VpeElementMapping elementMapping) {
		nsIDOMNode value = null;
		/*
		 * data property( of "style's" elementMapping ) contains Map<Object,nsIDOMNode>. 
		 * There is only one "style" visual element in this map. So we get this element from map.
		 * There is potential danger in this manner of keeping "style" element ( use property "data" of Object type )
		 */
		Map<Object, nsIDOMNode> map = (Map<Object, nsIDOMNode>) elementMapping.getData();
		// get "style" element
		if (map != null) {
			if (map.size() > 0) {
				value = map.values().iterator().next();
			}
		}
		if (value == null) {
			return;
		}
		// get new value of style element
		Node textNode = elementMapping.getSourceNode().getFirstChild();
		String text = null;
		if (textNode != null) {
			text = textNode.getNodeValue();
		}
		nsIDOMNodeList list = value.getChildNodes();
		// remove all children of style element
		for (int i = 0; i < list.getLength(); i++) {
			value.removeChild(list.item(i));
		}
		// add new value of style element
		value.appendChild(visualDomBuilder.getXulRunnerEditor()
				.getDOMDocument().createTextNode(text));
	}

	public static String getAbsoluteResourcePath(String resourcePathInPlugin) {
		String pluginPath = VpePlugin.getPluginResourcePath();
		IPath pluginFile = new Path(pluginPath);
		File file = pluginFile.append(resourcePathInPlugin).toFile();
		if (file.exists()) {
			return file.getAbsolutePath();
		} else {
			throw new RuntimeException("Can't get path for " //$NON-NLS-1$
					+ resourcePathInPlugin);
		}
	}

	/**
	 * If the {@code size} ends with a digit, adds {@code "px"} to it.
	 * 
	 * @param size
	 *            non-null value of a size attribute (e.g. {@code width}).
	 */
	public static String addPxIfNecessary(final String size) {
		final String trimmed = size.trim();
		int length = trimmed.length();
		if (length > 0) {
			final char lastChar = trimmed.charAt(length - 1);
			if (Character.isDigit(lastChar)) {
				return trimmed + PX_STRING;
			}
		}

		return size;
	}

	/**
	 * Converts the argument to the form {@code "Xpx"}, where {@code X=position}.
	 */
	public static String toPxPosition(int position) {
		return Integer.toString(position) + PX_STRING;
	}

	public static String processUrl(String url, IFile baseFile, boolean putIntoQuotes) {
		String resolvedUrl = url.replaceFirst(
				"^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}", Constants.EMPTY); //$NON-NLS-1$
		resolvedUrl = ElServiceUtil.replaceEl(baseFile, resolvedUrl);
		URI uri = null;
		try {
			uri = new URI(resolvedUrl);
		} catch (URISyntaxException e) {
			/*
			 * here we process user input, and when user enter url,
			 * there possible that we will not be able parse it.
			 * So we just ignore this. 
			 */
		}
		if (uri == null || !uri.isAbsolute()) {
			String decodedUrl = decodeUrl(resolvedUrl);
			Path decodedPath = new Path(decodedUrl);
			if (decodedUrl.startsWith("/") && (null != decodedPath.segment(0))//$NON-NLS-1$
					&& decodedPath.segment(0).equals(baseFile.getProject().getName())) {
				decodedUrl = "/" + decodedPath.removeFirstSegments(1).toPortableString(); //$NON-NLS-1$
			} 
			IFile file = FileUtil.getFile(decodedUrl, baseFile);
			if (file != null && file.getLocation() != null) {
				resolvedUrl = pathToUrl(file.getLocation());
			} 
		}
		/*
		 * https://issues.jboss.org/browse/JBIDE-9975
		 * Put the URL into quotes.
		 * It's default xulrunner behavior.
		 */
		if (putIntoQuotes) {
			resolvedUrl = QUOTE_STRING + resolvedUrl + QUOTE_STRING; 
		}
		return resolvedUrl;
	}

	private static String pathToUrl(IPath location) {
		String fullFilePath = location.toPortableString();
		try {
			return new URI("file", "", "/" + fullFilePath, null).toASCIIString(); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		} catch (URISyntaxException e) {
			VpePlugin.getPluginLog().logError(e);
			return Constants.FILE_PREFIX + fullFilePath;
		}
	}

	private static String decodeUrl(String url) {
		String decodedUrl;
		try {
			decodedUrl = URLDecoder.decode(url, "UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			VpePlugin.getPluginLog().logError(e);
			decodedUrl = url;
		}
		return decodedUrl;
	}

	/**
	 * Applies CSS attributes {@code left:x;top:y;} to the specified {@code element}.
	 */
	public static void moveElementTo(nsIDOMElement element, int x, int y) {
		nsIDOMCSSStyleDeclaration style = VpeStyleUtil.getStyle(element);
		style.setProperty(HTML.STYLE_PARAMETER_LEFT, VpeStyleUtil.toPxPosition(x), HTML.STYLE_PRIORITY_IMPORTANT);
		style.setProperty(HTML.STYLE_PARAMETER_TOP, VpeStyleUtil.toPxPosition(y), HTML.STYLE_PRIORITY_IMPORTANT);
	}

	/**
	 * Applies CSS attribute {@code display} to the specified {@code element} according to the {@code visible}
	 * parameter.
	 */
	public static void setElementVisible(nsIDOMElement element, boolean visible) {
		nsIDOMCSSStyleDeclaration style = VpeStyleUtil.getStyle(element);
		style.setProperty(HTML.STYLE_PARAMETER_DISPLAY,
				visible ? HTML.STYLE_VALUE_DEFAULT_DISPLAY
						: HTML.STYLE_VALUE_NONE, HTML.STYLE_PRIORITY_IMPORTANT);

	}
	
	/**
	 * Finds CSS @import url(".."); construction
	 * 
	 * @param cssText the css text
	 * @param pageContext VPE page context 
	 * @return the map with the import statement as a key and the css file path as a value
	 */
	public static List<String> findCssImportConstruction(String cssText, VpePageContext pageContext) {
		ArrayList<String> list = new ArrayList<String>();
		IFile sourceFile = getSourceFileFromPageContext(pageContext);
		Matcher m = CSS_IMPORT_PATTERN.matcher(cssText);
		while (m.find()) {
			/*
			 * Path should be a well formed URI
			 */
			list.add(processUrl(getCorrectURI(m.group(1)), sourceFile, false));
		}
		return list;
	}
	
	/**
	 * Gets the source file from pageContext
	 * 
	 * @param pageContext
	 * @return the opened file
	 */
	public static IFile getSourceFileFromPageContext(VpePageContext pageContext) {
		IFile file = null;
		final VpeIncludeInfo vii = pageContext.getVisualBuilder().getCurrentIncludeInfo();
		if ((vii != null) && (vii.getStorage() instanceof IFile)) {
			file = (IFile) vii.getStorage();
		}
		return file;
	}
	
	/**
	 * Determine correct uri in the input path:
	 * Remove quotes and brackets
	 * 
	 * @param path input path
	 * @return correct URI string
	 */
	private static String getCorrectURI(String path) {
		String uri = path;
		/*
		 * Closing bracket appears due to regex pattern.
		 * Should be removed.
		 */
		if (path.endsWith(CLOSE_BRACKET)) {
			uri = uri.substring(0, uri.length() - 1);
		}
		Matcher m = CSS_URI_PATTERN.matcher(uri);
		/*
		 * Find uri in " or ' quotes
		 */
		if (m.find()) {
			if ((m.group(1) != null) 
					&& !EMPTY_STRING.equalsIgnoreCase(m.group(1))) {
				uri = m.group(1);
			} else if ((m.group(2) != null) 
					&& !EMPTY_STRING.equalsIgnoreCase(m.group(2))) {
				uri = m.group(2);
			}
		}
		return uri;
	}
	
	/**
	 * Removes all the CSS @import constructions from the text
	 * 
	 * @param cssText the css text
	 * @return updated string
	 */
	public static String removeAllCssImportConstructions(String cssText) {
		return CSS_IMPORT_PATTERN.matcher(cssText).replaceAll(Constants.EMPTY);
	}
	
	/**
	 * Removes all the CSS comments from the text.
	 * 
	 * @param css the CSS text
	 * @return updated string
	 */
	public static String removeAllCssComments(String cssText) {
		// It is too expensive to use regular expressions here, this is why they are not used. See JBIDE-12308
		
		StringBuilder cssBuilder = new StringBuilder(cssText);
		int curIndex = 0;
		int commentStartIndex;
		while ((commentStartIndex = cssBuilder.indexOf(CSS_COMMENT_START, curIndex)) >= 0) {
			int afterCommentEndIndex = cssBuilder.indexOf(CSS_COMMENT_END, commentStartIndex + CSS_COMMENT_START.length());
			if (afterCommentEndIndex >= 0) {
				afterCommentEndIndex += CSS_COMMENT_END.length();
			} else {
				afterCommentEndIndex = cssBuilder.length();
			}
			cssBuilder.replace(commentStartIndex, afterCommentEndIndex, " "); //$NON-NLS-1$
			curIndex = commentStartIndex + 1;
		}
		return cssBuilder.toString();
	}
	
	/**
	 * Method for convert RGB to String
	 *
	 * @param rgb RGB color
	 * @return String color
	 */
	public static String rgbToString(RGB rgb) {
		String colorStr = "#0000FF"; //$NON-NLS-1$
		if (rgb != null) {
			colorStr = "#" //$NON-NLS-1$
					+ (rgb.red < 10 ? "0" : Constants.EMPTY) //$NON-NLS-1$
					+ Integer.toHexString(rgb.red)
					+ (rgb.green < 10 ? "0" : Constants.EMPTY) //$NON-NLS-1$
					+ Integer.toHexString(rgb.green)
					+ Constants.EMPTY
					+ (rgb.blue < 10 ? "0" : Constants.EMPTY) //$NON-NLS-1$
					+ Integer.toHexString(rgb.blue);
			colorStr = colorStr.toUpperCase();
		} else {
			VpePlugin.getDefault().logWarning("VpeStyleUtil.rgbToString(RGB rgb) -> Cannot convert RGB color to string, because it is null"); //$NON-NLS-1$
		}
		return colorStr;
	}
	// org.jboss.tools.jst.css.dialog.common.Util.getColor(..)
	
	/**
	 * Converts "100px" to integer 100
	 * 
	 * @param sizeString string for width or height
	 * @return size number
	 */
	public static int cssSizeToInt(String sizeString) {
		int size = -1;
		if (sizeString != null && !EMPTY_STRING.equalsIgnoreCase(sizeString)) {
			int pxPosition = sizeString.indexOf(PX_STRING);
			if (pxPosition >= 0) {
				sizeString = sizeString.substring(0, pxPosition).trim();
			}
			try {
				size = Integer.parseInt(sizeString);
			} catch (NumberFormatException e) {
				//do nothing
			}
		}
		return size;
	}
}