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
				String style) {
		this.value = value;
		this.tagForDisplay = tagForDisplay;
		this.style=style;
	}
	
	public String getUri() {
		
		return uri;
	}
	
	public void setUri(String uri) {
		
		this.uri = uri;
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
	
	@Override
	public String toString() {
		return "VpeAnyData [name=" + super.getName() + ", uri=" + uri //$NON-NLS-1$ //$NON-NLS-2$
				+ ", tagForDisplay=" + tagForDisplay + ", value=" + value //$NON-NLS-1$ //$NON-NLS-2$
				+ ", style=" + style + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((tagForDisplay == null) ? 0 : tagForDisplay.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VpeAnyData other = (VpeAnyData) obj;
		if (tagForDisplay == null) {
			if (other.tagForDisplay != null) {
				return false;
			}
		} else if (!tagForDisplay.equals(other.tagForDisplay)) {
			return false;
		}
		if (uri == null) {
			if (other.uri != null) {
				return false;
			}
		} else if (!uri.equals(other.uri)) {
			return false;
		}
		return true;
	}
	
}
