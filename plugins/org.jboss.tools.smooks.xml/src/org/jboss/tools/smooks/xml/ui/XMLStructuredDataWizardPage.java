/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.smooks.xml.AbstractFileSelectionWizardPage;
import org.jboss.tools.smooks.xml.model.DocumentObject;
import org.jboss.tools.smooks.xml.model.XMLObjectAnalyzer;

/**
 * @author Dart Peng Date : 2008-8-16
 */
public class XMLStructuredDataWizardPage extends
		AbstractFileSelectionWizardPage {

	public XMLStructuredDataWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	public XMLStructuredDataWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.xml.ui.AbstractFileSelectionWizardPage#loadedTheObject(java.lang.String)
	 */
	@Override
	protected Object loadedTheObject(String path) throws Exception {

		XMLObjectAnalyzer analyzer = new XMLObjectAnalyzer();
		DocumentObject doc = analyzer.analyze(path);

		return doc;
	}
}
