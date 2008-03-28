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
import java.util.List;

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
	 * @param templateTextFormatingElement - Element <vpe:textFormatting>
	 */
	public TextFormatingData(Element templateTextFormatingElement) {
		NodeList list = templateTextFormatingElement.getElementsByTagName(VpeTemplateManager.TAG_FORMAT);
		
		List<FormatData> defaultFormats = new ArrayList<FormatData>();
		
		if(VpeTemplateManager.ATTR_VALUE_YES.
				equals(templateTextFormatingElement.getAttribute(VpeTemplateManager.ATTR_USE_DEFAULT_FORMATS))) {
			//adds default format data
			FormatData[] formats =VpeTemplateManager.getDefaultTextFormattingData().getAllFormatData();
			for (FormatData formatData :formats) {
				
				defaultFormats.add(formatData);
			} 
		}
		List<FormatData> localFormats=new ArrayList<FormatData>();
		
		for(int i=0;i<list.getLength();i++) {
			Element element = (Element)list.item(i);
			localFormats.add(new FormatData(element));
		}
		formats=(FormatData[])mergeLocalAndDefaultFormats(defaultFormats, localFormats).toArray(new FormatData[0]);
	}
	/**
	 * Merges  local and default formats.
	 * If format with some type exist in local copy and remote, we use 
	 * local format
	 * 
	 * @return merged list
	 */
	private List<FormatData> mergeLocalAndDefaultFormats(List<FormatData> defaultFormats,List<FormatData> localFormats) {
	
		List<FormatData> result = new ArrayList<FormatData>(localFormats);
		for (FormatData formatData : defaultFormats) {
			if(!isFormatExistInList(result,formatData)) {
				result.add(formatData);
			}
		}
		return result;
	}
	
	private boolean isFormatExistInList(List<FormatData> formatData, FormatData data) {
	
		if(data.getType()==null) {
			
			return false;
		}
		
		for (FormatData format : formatData) {
			
			if(data.getType().equals(format.getType())) {
				return true;
			}
		}
		
		return false;
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