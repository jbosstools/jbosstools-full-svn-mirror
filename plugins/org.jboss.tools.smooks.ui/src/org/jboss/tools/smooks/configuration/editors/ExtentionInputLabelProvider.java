/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 12, 2009
 */
public class ExtentionInputLabelProvider extends LabelProvider implements ITableLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang
	 * .Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof InputType) {
			String type = ((InputType) element).getType();
			switch (columnIndex) {
			case 0:
				if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
					return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
							GraphicsConstants.IMAGE_JAVA_OBJECT);
				}
				if (SmooksModelUtils.INPUT_TYPE_XML.equals(type)) {
					return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
							GraphicsConstants.IMAGE_XML_ELEMENT);
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang
	 * .Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof InputType) {
			String value = SmooksModelUtils.getInputPath((InputType) element);
			if (value == null)
				value = "";
			switch (columnIndex) {
			case 0:
				return ((InputType) element).getType();
			case 1:
				return value;
			}
		}
		return "";
	}
}
