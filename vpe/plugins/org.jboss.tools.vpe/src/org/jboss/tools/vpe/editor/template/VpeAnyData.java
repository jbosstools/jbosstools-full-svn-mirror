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

public class VpeAnyData extends VpeTemplateData {
	private String uri;
	private String tagForDisplay;
	private String display;
	private String value;
	private String border;
	private String valueColor;
	private String valueBackgroundColor;
	private String backgroundColor;
	private String borderColor;
	private boolean showIcon;

	public VpeAnyData(String name) {
		super(name);
	}
	
	public VpeAnyData(
			String display,
			String value,
			String border,
			String valueColor,
			String valueBackgroundColor,
			String backgroundColor,
			String borderColor,
			boolean showIcon
		) {
		this(display,
				null,
				value,
				border,
				valueColor,
				valueBackgroundColor,
				backgroundColor,
				borderColor,
				showIcon);
	}
	public VpeAnyData(
				String display,
				String tagForDisplay,
				String value,
				String border,
				String valueColor,
				String valueBackgroundColor,
				String backgroundColor,
				String borderColor,
				boolean showIcon
			) {
		this.display = display;
		this.value = value;
		this.border = border;
		this.valueColor = valueColor;
		this.valueBackgroundColor = valueBackgroundColor;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.showIcon = showIcon;
		this.tagForDisplay = tagForDisplay;
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

	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorder() {
		return border;
	}
	
	public void setBorder(String border) {
		this.border = border;
	}

	public String getBorderColor() {
		return borderColor;
	}
	
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getDisplay() {
		return display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getValueBackgroundColor() {
		return valueBackgroundColor;
	}
	public void setValueBackgroundColor(String valueBackgroundColor) {
		this.valueBackgroundColor = valueBackgroundColor;
	}

	public String getValueColor() {
		return valueColor;
	}
	public void setValueColor(String valueColor) {
		this.valueColor = valueColor;
	}
    public String getTagForDisplay() {
        return tagForDisplay;
    }
    public void setTagForDisplay(String tagForDisplay) {
        this.tagForDisplay = tagForDisplay;
    }
}
