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
package org.jboss.tools.vpe.editor.template;
/**
 * Class which store properties for any template
 * 
 * @author mareshkau
 *
 */
public class VpeAnyData extends VpeTemplateData {
	private String uri;
	private String tagForDisplay;
	private boolean showIcon;
	private String value;
	private String style;


	public VpeAnyData(String name) {
		super(name);
	}
/**
 * 
 * @param tagForDisplay
 * @param value
 * @param style
 * @param showIcon
 */
	public VpeAnyData(
				String tagForDisplay,
				String value,
				String style,
				boolean showIcon
			) {
		this.value = value;
		this.showIcon = showIcon;
		this.tagForDisplay = tagForDisplay;
		this.style=style;
	}
	
	public String getUri() {
		
		return uri;
	}
	
	public void setUri(String uri) {
		
		this.uri = uri;
	}

	public boolean isShowIcon(){
		return showIcon;
	}
	
	public void setShowIcon(boolean flag){
		showIcon = flag;
	}

	public String getValue() {
		
		return value;
	}
	public void setValue(String value) {
		
		this.value = value;
	}

    public String getTagForDisplay() {
    
    	return tagForDisplay;
    }
    
    public void setTagForDisplay(String tagForDisplay) {
       
    	this.tagForDisplay = tagForDisplay;
    }

	/**
	 * @return the style
	 */
	public String getStyle() {
		
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		
		this.style = style;
	}
}
