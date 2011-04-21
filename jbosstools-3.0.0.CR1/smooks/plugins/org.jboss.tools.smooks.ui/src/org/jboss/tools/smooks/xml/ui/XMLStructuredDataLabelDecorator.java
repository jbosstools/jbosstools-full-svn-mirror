/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import org.eclipse.jface.viewers.IDecorationContext;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelDecorator;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;

/**
 * @author Dart
 * 
 */
public class XMLStructuredDataLabelDecorator extends LabelDecorator {

	@Override
	public Image decorateImage(Image image, Object element,
			IDecorationContext context) {
		return null;
	}

	@Override
	public String decorateText(String text, Object element,
			IDecorationContext context) {
		return null;
	}

	@Override
	public boolean prepareDecoration(Object element, String originalText,
			IDecorationContext context) {
		return false;
	}

	public Image decorateImage(Image image, Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public String decorateText(String text, Object element) {
		if (element instanceof AbstractXMLObject) {
			AbstractXMLObject parent = UIUtils
					.getRootTagXMLObject((AbstractXMLObject) element);
			String namespace = parent.getNamespaceURL();
			if (parent != element && namespace != null) {
				namespace = namespace.trim();
				if (namespace.equals(((AbstractXMLObject) element).getNamespaceURL())) {
					return text;
				}
			}
			if (((AbstractXMLObject) element).getNamespaceURL() != null)
				text += "    ["
						+ ((AbstractXMLObject) element).getNamespaceURL() + "]";
			return text;
		}
		return text;
	}

	public void addListener(ILabelProviderListener listener) {

	}

	public void dispose() {

	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

}
