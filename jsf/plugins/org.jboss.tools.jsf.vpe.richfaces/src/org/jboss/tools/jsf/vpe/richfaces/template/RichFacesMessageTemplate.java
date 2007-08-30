package org.jboss.tools.jsf.vpe.richfaces.template;

import org.jboss.tools.jsf.vpe.richfaces.HtmlComponentUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author ezheleznyakov@exadel.com
 *
 */
public class RichFacesMessageTemplate extends VpeAbstractTemplate {

	private static String PASSED_LABEL_ATTRIBUTE_NAME = "passedLabel";
	private static String LABEL_CLASS_ATTRIBUTE_NAME = "labelClass";
	private static String MARKER_CLASS_ATTRIBUTE_NAME = "markerClass";
	private static String MARKER_STYLE_ATTRIBUTE_NAME = "markerStyle";

	private static String ERROR_MARKER_CLASS_ATTRIBUTE_NAME = "errorMarkerClass";
	private static String ERROR_LABEL_CLASS_ATTRIBUTE_NAME = "errorLabelClass";
	private static String ERROR_CLASS_ATTRIBUTE_NAME = "errorClass";

	private static String FATAL_MARKER_CLASS_ATTRIBUTE_NAME = "fatalMarkerClass";
	private static String FATAL_LABEL_CLASS_ATTRIBUTE_NAME = "fatalLabelClass";
	private static String FATAL_CLASS_ATTRIBUTE_NAME = "fatalClass";

	private static String INFO_MARKER_CLASS_ATTRIBUTE_NAME = "infoMarkerClass";
	private static String INFO_LABEL_CLASS_ATTRIBUTE_NAME = "infoLabelClass";
	private static String INFO_CLASS_ATTRIBUTE_NAME = "infoClass";

	private static String WARN_MARKER_CLASS_ATTRIBUTE_NAME = "warnMarkerClass";
	private static String WARN_LABEL_CLASS_ATTRIBUTE_NAME = "warnLabelClass";
	private static String WARN_CLASS_ATTRIBUTE_NAME = "warnClass";
	
	private static String ERROR_MESSAGE = "Error message";
	private static String FATAL_MESSAGE = "Fatal message";
	private static String INFO_MESSAGE = "Info message";
	private static String WARNING_MESSAGE = "Warning message";

	
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {
		
		String passedLabelValue = ((Element)sourceNode).getAttribute(PASSED_LABEL_ATTRIBUTE_NAME);
		String labelClassValue = ((Element)sourceNode).getAttribute(LABEL_CLASS_ATTRIBUTE_NAME);
		String markerClassValue = ((Element)sourceNode).getAttribute(MARKER_CLASS_ATTRIBUTE_NAME);
		String markerStyleValue = ((Element)sourceNode).getAttribute(MARKER_STYLE_ATTRIBUTE_NAME);
		
		String errorMarkerClassValue = ((Element)sourceNode).getAttribute(ERROR_MARKER_CLASS_ATTRIBUTE_NAME);
		String errorLabelClassValue = ((Element)sourceNode).getAttribute(ERROR_LABEL_CLASS_ATTRIBUTE_NAME);
		String errorClassValue = ((Element)sourceNode).getAttribute(ERROR_CLASS_ATTRIBUTE_NAME);
		
		String fatalMarkerClassValue = ((Element)sourceNode).getAttribute(FATAL_MARKER_CLASS_ATTRIBUTE_NAME);
		String fatalLabelClassValue = ((Element)sourceNode).getAttribute(FATAL_LABEL_CLASS_ATTRIBUTE_NAME);
		String fatalClassValue = ((Element)sourceNode).getAttribute(FATAL_CLASS_ATTRIBUTE_NAME);
		
		String infoMarkerClassValue = ((Element)sourceNode).getAttribute(INFO_MARKER_CLASS_ATTRIBUTE_NAME);
		String infoLabelClassValue = ((Element)sourceNode).getAttribute(INFO_LABEL_CLASS_ATTRIBUTE_NAME);
		String infoClassValue = ((Element)sourceNode).getAttribute(INFO_CLASS_ATTRIBUTE_NAME);
		
		String warnMarkerClassValue = ((Element)sourceNode).getAttribute(WARN_MARKER_CLASS_ATTRIBUTE_NAME);
		String warnLabelClassValue = ((Element)sourceNode).getAttribute(WARN_LABEL_CLASS_ATTRIBUTE_NAME);
		String warnClassValue = ((Element)sourceNode).getAttribute(WARN_CLASS_ATTRIBUTE_NAME);
		
		String styleValue = ((Element)sourceNode).getAttribute(HtmlComponentUtil.HTML_STYLE_ATTR);
		String styleClassValue = ((Element)sourceNode).getAttribute(HtmlComponentUtil.HTML_CLASS_ATTR);

		
		nsIDOMElement table = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TABLE);
		
		if(styleValue != null && !styleValue.trim().equals(""))
			table.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR, styleValue);
		if(styleClassValue != null &&  !styleClassValue.trim().equals(""))
			table.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, styleClassValue);

		// Create first row PASSED
		nsIDOMElement tr1 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);
		table.appendChild(tr1);

		nsIDOMElement td1 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr1.appendChild(td1);
		
		// set markerClass
		if(markerClassValue != null && !markerClassValue.trim().equals(""))
			td1.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, markerClassValue);
		
		// set markerStyle
		if(markerStyleValue != null && !markerStyleValue.trim().equals(""))
			td1.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR, markerStyleValue);	

		nsIDOMElement img = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_IMG);
		td1.appendChild(img);
		
		nsIDOMElement td2 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr1.appendChild(td2);
		
		// set labelClass
		if(labelClassValue != null && !labelClassValue.trim().equals(""))
			td2.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, labelClassValue);
	
		nsIDOMText text = visualDocument.createTextNode(passedLabelValue == null
										? ""
										: passedLabelValue);
		td2.appendChild(text);
		// ---------------------------------------------------------------------

		// Create second row ERROR
		nsIDOMElement tr2 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);
		table.appendChild(tr2);
		
		// set errorClass
		if(errorClassValue != null && !errorClassValue.trim().equals(""))
			tr2.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, errorClassValue);

		nsIDOMElement td3 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr2.appendChild(td3);
		
		// set errorMarkerClass
		if(errorMarkerClassValue != null && !errorMarkerClassValue.trim().equals(""))
			td3.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, errorMarkerClassValue);

		img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
		td3.appendChild(img);

		nsIDOMElement td4 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr2.appendChild(td4);
		
		// set errorLabelClass
		if(errorLabelClassValue != null && !errorLabelClassValue.trim().equals(""))
			td4.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, errorLabelClassValue);

		text = visualDocument.createTextNode(ERROR_MESSAGE);
		td4.appendChild(text);
		// ---------------------------------------------------------------------

		// Create third row FATAL
		nsIDOMElement tr3 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);
		table.appendChild(tr3);
		
		// set fatalClass
		if(fatalClassValue != null && !fatalClassValue.trim().equals(""))
			tr3.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, fatalClassValue);

		nsIDOMElement td5 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr3.appendChild(td5);
		
		// set fatalMarkerClass
		if(fatalMarkerClassValue != null && !fatalMarkerClassValue.trim().equals(""))
			td5.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, fatalMarkerClassValue);

		img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
		td5.appendChild(img);

		nsIDOMElement td6 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr3.appendChild(td6);
		
		// set fatalLabelClass
		if(fatalLabelClassValue != null && !fatalLabelClassValue.trim().equals(""))
			td6.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, fatalLabelClassValue);

		text = visualDocument.createTextNode(FATAL_MESSAGE);
		td6.appendChild(text);
		// ---------------------------------------------------------------------

		// Create four row INFO
		nsIDOMElement tr4 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);
		table.appendChild(tr4);
		
		// set infoClass
		if(infoClassValue != null && !infoClassValue.trim().equals(""))
			tr4.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, infoClassValue);

		nsIDOMElement td7 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr4.appendChild(td7);
		
		// set infoMarkerClass
		if(infoMarkerClassValue != null && !infoMarkerClassValue.trim().equals(""))
			td7.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, infoMarkerClassValue);

		img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
		td7.appendChild(img);

		nsIDOMElement td8 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr4.appendChild(td8);
		
		// set infoLabelClass
		if(infoLabelClassValue != null && !infoLabelClassValue.trim().equals(""))
			td8.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, infoLabelClassValue);

		text = visualDocument.createTextNode(INFO_MESSAGE);
		td8.appendChild(text);
		// ---------------------------------------------------------------------

		// Create fifth row WARNING
		nsIDOMElement tr5 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);
		table.appendChild(tr5);
		
		// set warnClass
		if(warnClassValue != null && !warnClassValue.trim().equals(""))
			tr5.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, warnClassValue);

		nsIDOMElement td9 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr5.appendChild(td9);
		
		// set warnMarkerClass
		if(warnMarkerClassValue != null && !warnMarkerClassValue.trim().equals(""))
			td9.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, warnMarkerClassValue);

		img = visualDocument.createElement(HtmlComponentUtil.HTML_TAG_IMG);
		td9.appendChild(img);

		nsIDOMElement td10 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		tr5.appendChild(td10);
		
		// set warnLabelClass
		if(warnLabelClassValue != null && !warnLabelClassValue.trim().equals(""))
			td10.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, warnLabelClassValue);

		text = visualDocument.createTextNode(WARNING_MESSAGE);
		td10.appendChild(text);

		return new VpeCreationData(table);
	}
}