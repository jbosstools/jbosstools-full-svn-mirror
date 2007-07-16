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
package org.jboss.tools.vpe.editor.template.textformating;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.template.VpeTemplateManager;

/**
 * Class contains data from template for text formating (Text Formating Tollbar).
 * @author Igels
 */
public class TextFormatingData {

	private FormatData[] formats;

	/**
	 * @param templateTextFormatingElement - Element <vpe:textFormating>
	 */
	public TextFormatingData(Element templateTextFormatingElement) {
		NodeList list = templateTextFormatingElement.getElementsByTagName(VpeTemplateManager.TAG_FORMAT);
		formats = new FormatData[list.getLength()];
		for(int i=0; i<list.getLength(); i++) {
			Element element = (Element)list.item(i);
			formats[i] = new FormatData(element);
		}
	}

	/**
	 * @return children - <vpe:format>
	 */
	public FormatData[] getAllFormatData() {
		return formats;
	}

	/**
	 * @param type
	 * @return
	 */
	public FormatData[] getFormatDatas(String type) {
		ArrayList result = new ArrayList();
		for(int i=0; i<formats.length; i++) {
			if(type.equals(formats[i].getType())) {
				result.add(formats[i]);
			}
		}
		return (FormatData[])result.toArray(new FormatData[result.size()]);
	}

	/**
	 * @param type
	 * @return
	 */
	public boolean hasFormatData(String type) {
		return getFormatDatas(type).length>0;
	}
}