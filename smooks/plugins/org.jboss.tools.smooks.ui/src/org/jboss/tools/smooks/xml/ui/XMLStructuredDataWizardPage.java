/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import org.jboss.tools.smooks.xml.AbstractFileSelectionWizardPage;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.XMLObjectAnalyzer;

/**
 * @author Dart Peng Date : 2008-8-16
 */
public class XMLStructuredDataWizardPage extends
		AbstractFileSelectionWizardPage {


	public XMLStructuredDataWizardPage(String pageName) {
		super(pageName);
		setPageText();
	}

	private void setPageText() {
		this.setTitle(Messages.getString("XMLStructuredDataWizardPage.XMLDataWizardPageTitle")); //$NON-NLS-1$
		this.setDescription(Messages.getString("XMLStructuredDataWizardPage.XMLDataWizardPageDescription")); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.xml.ui.AbstractFileSelectionWizardPage#loadedTheObject(java.lang.String)
	 */
	@Override
	protected Object loadedTheObject(String path) throws Exception {
		XMLObjectAnalyzer analyzer = new XMLObjectAnalyzer();
		TagList doc = analyzer.analyze(path , null);
		return doc;
	}
}
