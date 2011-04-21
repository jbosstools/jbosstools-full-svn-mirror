/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.xml.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.xml.XMLImageConstants;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

/**
 * @author Dart Peng
 * @Date Aug 18, 2008
 */
public class XMLStructuredDataLabelProvider extends LabelProvider {

	public Image getXmlElementImage() {
		return SmooksUIActivator.getDefault().getImageRegistry().get(
				XMLImageConstants.IMAGE_XML_ELEMENT);

	}

	public Image getXmlAttributeImage() {
		return SmooksUIActivator.getDefault().getImageRegistry().get(
				XMLImageConstants.IMAGE_XML_ATTRIBUTE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof TagObject) {
			return getXmlElementImage();
		}
		if (element instanceof TagPropertyObject) {
			return getXmlAttributeImage();
		}
		return super.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof TagList) {
			return "Root";
		}
		if (element instanceof AbstractXMLObject) {
			return ((AbstractXMLObject) element).getName();
		}
		return super.getText(element);
	}

}
