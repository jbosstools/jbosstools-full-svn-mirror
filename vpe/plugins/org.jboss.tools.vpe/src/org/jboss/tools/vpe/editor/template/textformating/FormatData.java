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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.editor.template.VpeTemplateManager;

/**
 * FormatData describe vpe template text formating part for tag.
 * @author Igels
 */
public class FormatData{

	private FormatAttributeData[] formatAttributes;
	private String type;
	private String addChildren;
	private String addParent;
	private boolean addChildrenIsAllowIfParentDoesntDeny = false;
	private boolean addChildrenIsAllow = false;
	private boolean addChildrenIsDeny = false;
	private boolean addChildrenByItself = false;
	private String addChildrenHandler;
	private boolean addParentIsAllowIfParentDoesntDeny = false;
	private boolean addParentIsAllow = false;
	private boolean addParentIsDeny = false;
	private boolean addParentByItself = false;
	private String handler;
	private boolean setDefault = false;

	/**
	 * @param formatElement - Element <vpe:format>
	 */
	public FormatData(Element formatElement) {
		NodeList list = formatElement.getElementsByTagName(VpeTemplateManager.TAG_FORMAT_ATTRIBUTE);
		formatAttributes = new FormatAttributeData[list.getLength()];
		for(int i=0; i<list.getLength(); i++) {
			Element element = (Element)list.item(i);
			formatAttributes[i] = new FormatAttributeData(this, element);
		}
		type = formatElement.getAttribute(VpeTemplateManager.ATTR_FORMAT_TYPE);
		addChildren = formatElement.getAttribute(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN);
		addChildrenHandler = formatElement.getAttribute(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN_HANDLER);
		handler = formatElement.getAttribute(VpeTemplateManager.ATTR_FORMAT_HANDLER);
		setDefault = "true".equals(formatElement.getAttribute(VpeTemplateManager.ATTR_FORMAT_SET_DEFAULT)); //$NON-NLS-1$
		setAddChildrenFlags();
		addParent = formatElement.getAttribute(VpeTemplateManager.ATTR_FORMAT_ADD_PARENT);
		setAddParentFlags();
	}

	private FormatData() {
		
	}
	
	private void setAddChildrenFlags() {
		if(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN_ALLOW_VALUE.equals(addChildren)) {
			addChildrenIsAllow = true;
		} else if(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN_DENY_VALUE.equals(addChildren)) {
			addChildrenIsDeny = true;
		} else if(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN_ITSELF_VALUE.equals(addChildren)) {
			addChildrenByItself = true;
		} else {
			addChildrenIsAllowIfParentDoesntDeny = true;
		}
	}

	private void setAddParentFlags() {
		if(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN_ALLOW_VALUE.equals(addParent)) {
			addParentIsAllow = true;
		} else if(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN_DENY_VALUE.equals(addParent)) {
			addParentIsDeny = true;
		} else if(VpeTemplateManager.ATTR_FORMAT_ADD_CHILDREN_ALLOW_VALUE.equals(addParent)) {
			addParentByItself = true;
		} else {
			addParentIsAllowIfParentDoesntDeny = true;
		}
	}

	/**
	 * @return true if children of this tag can't add other children to this tag. Any children must be added by this tag itself.
	 */
	public boolean isAddingChildrenByItself() {
		return addChildrenByItself;
	}

	/**
	 * @return true if children of this tag can add other children to this tag and any parent doesnt deny it. Default value is true.
	 */
	public boolean isAddingChildrenAllowIfParentDoesntDeny() {
		return addChildrenIsAllowIfParentDoesntDeny;
	}

	/**
	 * @return true if children of this tag can add other children to this tag.
	 */
	public boolean isAddingChildrenAllow() {
		return addChildrenIsAllow;
	}

	/**
	 * @return true if children of this tag can't add other children to this tag.
	 */
	public boolean isAddingChildrenDeny() {
		return addChildrenIsDeny;
	}

	/**
	 * @return children - <vpe:formatAttributes>
	 */
	public FormatAttributeData[] getFormatAttributes() {
		return formatAttributes;
	}

	/**
	 * @return class name of handler. This handler will be execute for make format this tag.
	 */
	public String getHandler() {
		return handler;
	}

	/**
	 * @return type of this format operation.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the setDefault.
	 */
	public boolean isSetDefault() {
		return setDefault;
	}

	/**
	 * @return Returns the addChildrenHandler.
	 */
	public String getAddChildrenHandler() {
		return addChildrenHandler;
	}

	/**
	 * @return Returns the addParent.
	 */
	public boolean isAddingParentAllow() {
		return addParentIsAllow;
	}

	/**
	 * @return true if parent of this tag can be added and any parent doesnt deny it. Default value is true.
	 */
	public boolean isAddingParentAllowIfParentDoesntDeny() {
		return addParentIsAllowIfParentDoesntDeny;
	}

	/**
	 * @return true if parent of this tag can be added.
	 */
	public boolean isAddingParentDeny() {
		return addParentIsDeny;
	}

	/**
	 * @return true if parent of this tag can't be added. Any parent must be added by this tag itself.
	 */
	public boolean isAddingParentByItself() {
		return addParentByItself;
	}
}