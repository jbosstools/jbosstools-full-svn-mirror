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
package org.jboss.tools.smooks.graphical.editors;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XSLModelAnalyzer;
import org.jboss.tools.smooks.configuration.editors.xml.XSLTagObject;
import org.jboss.tools.smooks.model.xsl.Xsl;

/**
 * @author Dart
 * 
 */
public class XSLLabelProvider extends LabelProvider {

	private XMLStructuredDataLabelProvider provider = new XMLStructuredDataLabelProvider();

	private ILabelProvider parentLabelProvider = null;

	public XSLLabelProvider(ILabelProvider parentLabelProvider) {
		this.parentLabelProvider = parentLabelProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof Xsl) {
			return parentLabelProvider.getText(element);
		}
		if (element instanceof TagPropertyObject) {
			String name = ((TagPropertyObject) element).getName();
			String value = ((TagPropertyObject) element).getValue();
			if (value == null || value.trim().length() == 0 ) {
					return name + Messages.XSLLabelProvider_Text_Null_Value;
			}
		}
		if(element instanceof XSLTagObject){
			if(XSLModelAnalyzer.isXSLTagObject((XSLTagObject)element)){
				if(((XSLTagObject)element).isTemplateElement()){
					String match = ((XSLTagObject)element).getMatchValue();
					String name = ((XSLTagObject)element).getName();
					if (match != null) {
						match = match.trim();
						if (!"".equals(match)) { //$NON-NLS-1$
							return name + " (" + match+")"; //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			}
			
			if(XSLModelAnalyzer.isXSLTagObject((XSLTagObject)element)){
				if(((XSLTagObject)element).isApplyTemplatesElement()){
					String select = ((XSLTagObject)element).getSelectValue();
					String name = ((XSLTagObject)element).getName();
					if (select != null) {
						select = select.trim();
						if (!"".equals(select)) { //$NON-NLS-1$
							return name + " -> " + select; //$NON-NLS-1$
						}
					}
				}
			}
		}
		return provider.getText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof Xsl) {
			return parentLabelProvider.getImage(element);
		}

		return provider.getImage(element);
	}
}
