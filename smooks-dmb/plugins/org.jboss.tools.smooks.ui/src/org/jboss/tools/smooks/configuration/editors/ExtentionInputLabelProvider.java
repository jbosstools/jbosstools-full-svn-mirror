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
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.model.core.IParam;

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
		if (element instanceof IParam) {
			String type = ((IParam) element).getType();
			switch (columnIndex) {
			case 0:
				if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
					return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
							GraphicsConstants.IMAGE_JAVA_FILE);
				}
				if (SmooksModelUtils.INPUT_TYPE_XML.equals(type)) {
					return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
							GraphicsConstants.IMAGE_XML_FILE);
				}
				if (SmooksModelUtils.INPUT_TYPE_XSD.equals(type)) {
					return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
							GraphicsConstants.IMAGE_XSD_FILE);
				}
				return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_UNKNOWN_OBJ);
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
		if (element instanceof IParam) {
			IParam inputTypeParam = (IParam) element;

			switch (columnIndex) {
			case 0:
				return inputTypeParam.getValue();
			case 1:
				return ""; //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}
}
