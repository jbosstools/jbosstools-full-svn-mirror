/**
 * 
 */
package org.jboss.tools.smooks.xsd.ui;

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.jboss.tools.smooks.xml.AbstractFileSelectionWizardPage;

/**
 * @author Dart Peng
 * Date : 2008-8-16
 */
public class XSDStructuredDataWizardPage extends
		AbstractFileSelectionWizardPage {

//	public XSDStructuredDataWizardPage(String pageName, String title,
//			ImageDescriptor titleImage) {
//		super(pageName, title, titleImage);
//		// TODO Auto-generated constructor stub
//	}

	public XSDStructuredDataWizardPage(String pageName) {
		super(pageName);
		this.setTitle("XSD File Selection");
		this.setMessage("Select a *.xsd file to loaded ");
	}
	protected Object loadedTheObject(String path) throws Exception {
		Resource resource = new XSDResourceFactoryImpl().createResource(URI
				.createFileURI(path));
		resource.load(Collections.EMPTY_MAP);
		XSDSchema schema = (XSDSchema) resource.getContents().get(0);
		return schema;
	}
}
