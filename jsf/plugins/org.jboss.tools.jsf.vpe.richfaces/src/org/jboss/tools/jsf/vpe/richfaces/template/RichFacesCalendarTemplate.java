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
package org.jboss.tools.jsf.vpe.richfaces.template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jboss.tools.jsf.vpe.richfaces.ComponentUtil;
import org.jboss.tools.jsf.vpe.richfaces.HtmlComponentUtil;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMText;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Displays template for calendar
 * 
 * @author dsakovich@exadel.com
 * 
 */
public class RichFacesCalendarTemplate extends VpeAbstractTemplate {

    static String[] HEADER_CONTENT = { "<<", "<", "", ">", ">>" };
    private List<String> holidays = new ArrayList<String>();
    private String[] weeks = new String[7];
    final static int MONTH_LENGTH[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
			30, 31 };
	final static int LEAP_MONTH_LENGTH[] = { 31, 29, 31, 30, 31, 30, 31, 31,
			30, 31, 30, 31 }; 
    final static String STYLE_PATH = "calendar/calendar.css";
    final static String BUTTON_IMG = "calendar/calendar.gif";
    final static int COLUMN = 8;
    final static String FILL_WIDTH = "100%";
    final static int NUM_DAYS_IN_WEEK = 7;
    final static int NUM_WEEK_ON_PAGE = 6;
    final static String TODAY = "Today";
    final static int CALENDAR_WIDTH = 200;
    final static int CALENDAR_IMAGE_WIDTH = 20;
    final static String ATTRIBUTE_POPUP = "popup";
    final static String ATTRIBUTE_TEXT = "text";
    
    final static String WEEK_DAY_HTML_CLASS_ATTR = "rich-calendar-days";
    final static String HOL_WEEK_DAY_HTML_CLASS_ATTR = "rich-calendar-days rich-calendar-days-holly";
    final static String TODAY_HTML_CLASS_ATTR = "rich-cell-size rich-calendar-cell rich-calendar-today ";
	final static String CUR_MONTH_HTML_CLASS_ATTR = "rich-cell-size rich-calendar-cell";
	final static String HOL_CUR_MONTH_HTML_CLASS_ATTR = "rich-cell-size rich-calendar-cell rich-calendar-holly "/* rich-right-cell " */;
	final static String OTHER_MONTH_HTML_CLASS_ATTR = "rich-cell-size rich-calendar-cell rich-calendar-other-month ";
	final static String HOL_OTHER_MONTH_HTML_CLASS_ATTR = "rich-cell-size rich-calendar-cell rich-calendar-holly rich-calendar-other-month" /* rich-right-cell " */;
	
    public RichFacesCalendarTemplate() {
		super();
		Calendar cal = Calendar.getInstance();
		int firstDayOfWeek = cal.getFirstDayOfWeek();
		while (firstDayOfWeek != cal.get(Calendar.DAY_OF_WEEK)) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("EE");
		String dayOfWeek = "";
		for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
			dayOfWeek = sdf.format(cal.getTime());
			weeks[i] = dayOfWeek;
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
					|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				holidays.add(dayOfWeek);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

	}

    /**
     * Creates a node of the visual tree on the node of the source tree. This
     * visual node should not have the parent node This visual node can have
     * child nodes.
     * 
     * @param pageContext
     *                Contains the information on edited page.
     * @param sourceNode
     *                The current node of the source tree.
     * @param visualDocument
     *                The document of the visual tree.
     * @return The information on the created node of the visual tree.
     */
    public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
	    nsIDOMDocument visualDocument) {
	Element source = (Element) sourceNode;
	String popup = source.getAttribute(ATTRIBUTE_POPUP);
	ComponentUtil.setCSSLink(pageContext, STYLE_PATH, "calendar");
	VpeCreationData creationData;
	if (popup != null && popup.equalsIgnoreCase("false")) {
	    nsIDOMElement calendar = createCalendar(visualDocument);
	    creationData = new VpeCreationData(calendar);
	} else {
	    nsIDOMElement calendarWithPopup = createCalendarWithPopup(
		    visualDocument, source);
	    creationData = new VpeCreationData(calendarWithPopup);
	}
	return creationData;
    }

    /**
     * 
     * @param visualDocument
     * @return Node of the visual tree.
     */
    private nsIDOMElement createCalendarWithPopup(
	    nsIDOMDocument visualDocument, Element source) {
	nsIDOMElement div = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_DIV);
	div.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR,
		HtmlComponentUtil.HTML_ATR_WIDTH + " : " + CALENDAR_WIDTH
			+ "px;");
	nsIDOMElement input = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_INPUT);
	input.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR,
		HtmlComponentUtil.HTML_ATR_WIDTH + " : "
			+ (CALENDAR_WIDTH - CALENDAR_IMAGE_WIDTH) + "px;");
	input.setAttribute(HtmlComponentUtil.HTML_TYPE_ATTR, ATTRIBUTE_TEXT);
	input.setAttribute(HtmlComponentUtil.HTML_READONLY_ATTR, "true");
	String value = source.getAttribute(HtmlComponentUtil.HTML_VALUE_ATTR);
	if (value != null) {
	    input.setAttribute(HtmlComponentUtil.HTML_VALUE_ATTR, value);
	}

	nsIDOMElement image = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_IMG);
	image.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
		"rich-calendar-popupicon");
	ComponentUtil.setImg(image, BUTTON_IMG);

	div.appendChild(input);
	div.appendChild(image);

	return div;
    }

    /**
     * Checks, whether it is necessary to re-create an element at change of
     * attribute
     * 
     * @param pageContext
     *                Contains the information on edited page.
     * @param sourceElement
     *                The current element of the source tree.
     * @param visualDocument
     *                The document of the visual tree.
     * @param visualNode
     *                The current node of the visual tree.
     * @param data
     *                The arbitrary data, built by a method <code>create</code>
     * @param name
     *                Atrribute name
     * @param value
     *                Attribute value
     * @return <code>true</code> if it is required to re-create an element at
     *         a modification of attribute, <code>false</code> otherwise.
     */
    public boolean isRecreateAtAttrChange(VpePageContext pageContext,
	    Element sourceElement, nsIDOMDocument visualDocument,
	    nsIDOMElement visualNode, Object data, String name, String value) {
	if (name.equalsIgnoreCase(ATTRIBUTE_POPUP)) {
	    return true;
	}
	return false;
    }

    /**
     * 
     * @param visualDocument
     * @return Node of the visual tree.
     */
    private nsIDOMElement createCalendar(nsIDOMDocument visualDocument) {
	nsIDOMElement div = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_DIV);
	div.setAttribute(HtmlComponentUtil.HTML_STYLE_ATTR,
		HtmlComponentUtil.HTML_ATR_WIDTH + " : " + CALENDAR_WIDTH
			+ "px;");
	nsIDOMElement table = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TABLE);
	table.setAttribute(HtmlComponentUtil.HTML_CELLPADDING_ATTR, "0");
	table.setAttribute(HtmlComponentUtil.HTML_BORDER_ATTR, "0");
	table.setAttribute(HtmlComponentUtil.HTML_CELLSPACING_ATTR, "0");
	table.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
		"calendar-exterior");

	nsIDOMElement tbody = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TBODY);
	nsIDOMElement header = createCalendarHeader(visualDocument);
	nsIDOMElement calendarBody = createCalendarBody(visualDocument);
	tbody.appendChild(header);
	tbody.appendChild(calendarBody);
	table.appendChild(tbody);
	div.appendChild(table);

	return div;
    }

    /**
     * 
     * @param visualDocument
     * @return Node of the visual tree.
     */
    private nsIDOMElement createCalendarHeader(nsIDOMDocument visualDocument) {
	nsIDOMElement tr = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TR);
	nsIDOMElement td = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TD);
	td.setAttribute(HtmlComponentUtil.HTML_TABLE_COLSPAN, "" + COLUMN);

	SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy");
	Calendar cal = Calendar.getInstance();
	HEADER_CONTENT[2] = sdf.format(cal.getTime());

	nsIDOMElement table = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TABLE);
	table.setAttribute(HtmlComponentUtil.HTML_CELLPADDING_ATTR, "0");
	table.setAttribute(HtmlComponentUtil.HTML_CELLSPACING_ATTR, "0");
	table.setAttribute(HtmlComponentUtil.HTML_BORDER_ATTR, "0");
	table.setAttribute(HtmlComponentUtil.HTML_ATR_WIDTH, FILL_WIDTH);

	nsIDOMElement tbody = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TBODY);

	nsIDOMElement tr1 = visualDocument
		.createElement(HtmlComponentUtil.HTML_TAG_TR);
	for (int i = 0; i < HEADER_CONTENT.length; i++) {
	    nsIDOMElement td1 = visualDocument
		    .createElement(HtmlComponentUtil.HTML_TAG_TD);
	    td1.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
		    i == 2 ? "rich-calendar-month" : "rich-calendar-tool");

	    nsIDOMText text1 = visualDocument.createTextNode(HEADER_CONTENT[i]);
	    td1.appendChild(text1);
	    tr1.appendChild(td1);
	}

	tbody.appendChild(tr1);
	table.appendChild(tbody);
	td.appendChild(table);
	tr.appendChild(td);
	return tr;
    }

    /**
	 * 
	 * @param visualDocument
	 * @return Node of the visual tree.
	 */
	private nsIDOMElement createCalendarBody(nsIDOMDocument visualDocument) {

		nsIDOMElement tbody = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TBODY);

		nsIDOMElement bodyTR = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);

		Calendar cal1 = Calendar.getInstance();

		SimpleDateFormat wdf = new SimpleDateFormat("EE");

		cal1.set(Calendar.WEEK_OF_MONTH, Calendar.SATURDAY);
		System.out.print(wdf.format(cal1.getTime()));

		for (int i = 0; i < COLUMN; i++) {
			nsIDOMElement td = visualDocument
					.createElement(HtmlComponentUtil.HTML_TAG_TD);

			if (i == 0) {
				td.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
						WEEK_DAY_HTML_CLASS_ATTR);
				nsIDOMElement br = visualDocument
						.createElement(HtmlComponentUtil.HTML_TAG_BR);
				td.appendChild(br);
			} else {
				if (holidays.contains(weeks[i - 1])) {
					td.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
							HOL_WEEK_DAY_HTML_CLASS_ATTR);
				} else {
					td.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
							WEEK_DAY_HTML_CLASS_ATTR);
				}
				nsIDOMText text = visualDocument.createTextNode(i == 0 ? ""
						: weeks[i - 1]);
				td.appendChild(text);
			}
			bodyTR.appendChild(td);
		}

		tbody.appendChild(bodyTR);

		// Calendar body

		Calendar cal = Calendar.getInstance();

		int month = cal.get(Calendar.MONTH);
		int dayN = cal.get(Calendar.DAY_OF_MONTH);

		// shift 'cal' to month's start
		cal.add(Calendar.DAY_OF_MONTH, -dayN);
		// shift 'cal' to week's start
		cal.add(Calendar.DAY_OF_MONTH, -(cal.get(Calendar.DAY_OF_WEEK) - cal
				.getFirstDayOfWeek()));

		//for number of week
		for (int i = NUM_WEEK_ON_PAGE; i > 0; i--) {

			nsIDOMElement tr = visualDocument
					.createElement(HtmlComponentUtil.HTML_TAG_TR);
			// Week in year
			nsIDOMElement weekTD = visualDocument
					.createElement(HtmlComponentUtil.HTML_TAG_TD);
			weekTD.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
					"rich-calendar-week");
			nsIDOMText weekText = visualDocument.createTextNode(""
					+ cal.get(Calendar.WEEK_OF_YEAR));
			weekTD.appendChild(weekText);
			tr.appendChild(weekTD);
			
			//for number of days in week
			for (int j = NUM_DAYS_IN_WEEK; j > 0; j--) {

				nsIDOMElement td = visualDocument
						.createElement(HtmlComponentUtil.HTML_TAG_TD);

				String currentAttr = "";

				int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

				// if 'cal' is a member of month
				if (cal.get(Calendar.MONTH) == month) {

					// if this is current day
					if (cal.get(Calendar.DAY_OF_MONTH) == dayN
							&& cal.get(Calendar.MONTH) == month) {
						currentAttr = TODAY_HTML_CLASS_ATTR;

					}
					// if this is holiday
					else if (dayOfWeek == Calendar.SATURDAY
							|| dayOfWeek == Calendar.SUNDAY) {
						currentAttr = HOL_CUR_MONTH_HTML_CLASS_ATTR;
					} else {
						currentAttr = CUR_MONTH_HTML_CLASS_ATTR;
					}
				}
				// if 'cal' isn't a member of month
				else {
					// if this is holiday
					if (dayOfWeek == Calendar.SATURDAY
							|| dayOfWeek == Calendar.SUNDAY) {
						currentAttr = HOL_OTHER_MONTH_HTML_CLASS_ATTR;
					} else {
						currentAttr = OTHER_MONTH_HTML_CLASS_ATTR;
					}
				}

				td.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR, currentAttr);
				nsIDOMText text = visualDocument.createTextNode(""
						+ cal.get(Calendar.DAY_OF_MONTH));
				td.appendChild(text);
				tr.appendChild(td);

				cal.add(Calendar.DAY_OF_MONTH, 1);

			}
			tbody.appendChild(tr);
		}

		// Footer for calendar

		nsIDOMElement tr = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);

		nsIDOMElement td = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		td.setAttribute(HtmlComponentUtil.HTML_TABLE_COLSPAN, "" + COLUMN);

		nsIDOMElement table = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TABLE);
		table.setAttribute(HtmlComponentUtil.HTML_CELLSPACING_ATTR, "0");
		table.setAttribute(HtmlComponentUtil.HTML_CELLPADDING_ATTR, "0");
		table.setAttribute(HtmlComponentUtil.HTML_BORDER_ATTR, "0");
		table.setAttribute(HtmlComponentUtil.HTML_ATR_WIDTH, FILL_WIDTH);

		nsIDOMElement tr1 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TR);

		nsIDOMElement td1 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		td1.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
				"rich-calendar-toolfooter");

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		cal = Calendar.getInstance();

		nsIDOMText text1 = visualDocument.createTextNode(sdf.format(cal
				.getTime()));
		td1.appendChild(text1);
		tr1.appendChild(td1);

		nsIDOMElement td2 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		td2.setAttribute(HtmlComponentUtil.HTML_ATR_WIDTH, FILL_WIDTH);
		td2.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
				"rich-calendar-toolfooter");
		tr1.appendChild(td2);

		nsIDOMElement td3 = visualDocument
				.createElement(HtmlComponentUtil.HTML_TAG_TD);
		td3.setAttribute(HtmlComponentUtil.HTML_ATR_WIDTH, FILL_WIDTH);
		td3.setAttribute(HtmlComponentUtil.HTML_CLASS_ATTR,
				"rich-calendar-toolfooter");
		td3.setAttribute(HtmlComponentUtil.HTML_ALIGN_ATTR,
				HtmlComponentUtil.HTML_ALIGN_RIGHT_VALUE);
		nsIDOMText text3 = visualDocument.createTextNode(TODAY);
		td3.appendChild(text3);
		tr1.appendChild(td3);

		table.appendChild(tr1);
		td.appendChild(table);
		tr.appendChild(td);

		tbody.appendChild(tr);

		return tbody;
	}

    /**
	 * 
	 */
    public void setAttribute(VpePageContext pageContext, Element sourceElement,
	    nsIDOMDocument visualDocument, nsIDOMNode visualNode, Object data,
	    String name, String value) {
	super.setAttribute(pageContext, sourceElement, visualDocument,
		visualNode, data, name, value);
	if (name.equalsIgnoreCase(HtmlComponentUtil.HTML_VALUE_ATTR)) {
	    String popup = sourceElement.getAttribute(ATTRIBUTE_POPUP);
	    if (popup != null && popup.equalsIgnoreCase("false"))
		return;
	    nsIDOMElement element = (nsIDOMElement) visualNode
		    .queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	    nsIDOMNodeList list = element.getChildNodes();
	    nsIDOMNode tableNode = list.item(0);
	    nsIDOMElement input = (nsIDOMElement) tableNode
		    .queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	    input.setAttribute(HtmlComponentUtil.HTML_VALUE_ATTR, value);
	}

    }

    /**
     * 
     */
    public void removeAttribute(VpePageContext pageContext,
	    Element sourceElement, nsIDOMDocument visualDocument,
	    nsIDOMNode visualNode, Object data, String name) {
	super.removeAttribute(pageContext, sourceElement, visualDocument,
		visualNode, data, name);
	if (name.equalsIgnoreCase(HtmlComponentUtil.HTML_VALUE_ATTR)) {
	    String popup = sourceElement.getAttribute(ATTRIBUTE_POPUP);
	    if (popup != null && popup.equalsIgnoreCase("false"))
		return;
	    nsIDOMElement element = (nsIDOMElement) visualNode
		    .queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	    nsIDOMNodeList list = element.getChildNodes();
	    nsIDOMNode tableNode = list.item(0);
	    nsIDOMElement input = (nsIDOMElement) tableNode
		    .queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	    input.removeAttribute(HtmlComponentUtil.HTML_VALUE_ATTR);
	}
    }
}
