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
package org.jboss.tools.smooks.graphical.wizards;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.configuration.editors.xml.XMLObjectAnalyzer;
import org.jboss.tools.smooks.configuration.editors.xml.XSDObjectAnalyzer;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.xsl.Template;
import org.jboss.tools.smooks.model.xsl.Xsl;
import org.jboss.tools.smooks.model.xsl.XslFactory;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.xml.sax.SAXException;

/**
 * @author Dart
 * 
 */
public class XSLTemplateCreationWizard extends SmooksCreationModelConfigureWizard {

	private XSLTemplateCreationWizardPage page;

	private Xsl xslModel = null;

	public XSLTemplateCreationWizard(ISmooksModelProvider modelProvider, Object model) {
		super(modelProvider, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		if (page == null) {
			page = new XSLTemplateCreationWizardPage("XSL Template", this.modelProvider);
			this.addPage(page);
		}
	}

	/**
	 * @return the xslModel
	 */
	public Xsl getXslModel() {
		return xslModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.wizards.SmooksCreationModelConfigureWizard
	 * #performFinish()
	 */
	@Override
	public boolean performFinish() {
		int type = page.getTemplateType();
		xslModel = XslFactory.eINSTANCE.createXsl();
		xslModel.setApplyOnElement("#document");
		Template template = xslModel.getTemplate();

		if (type == XSLTemplateCreationWizardPage.TEMPLATE_TYPE_NONE) {
			return true;
		}

		if (type == XSLTemplateCreationWizardPage.TEMPLATE_TYPE_XSL) {
			SmooksModelUtils.setTextToAnyType(template, page.getExtenalFilePath());
		}

		if (type == XSLTemplateCreationWizardPage.TEMPLATE_TYPE_XML_XSD) {
			String rootElement = page.getRootElementName();
			String filePath = page.getContentsFile();
			if (rootElement != null) {
				XSDObjectAnalyzer analyzer = new XSDObjectAnalyzer();
				try {
					filePath = SmooksUIUtils.parseFilePath(filePath);
					TagObject rootTag = analyzer.loadElement(filePath, rootElement);
					if (rootTag != null) {
						String contents = generateXSLContents(rootTag);
						SmooksModelUtils.setCDATAToAnyType(template, contents);
					}
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				XMLObjectAnalyzer analyzer = new XMLObjectAnalyzer();
				try {
					filePath = SmooksUIUtils.parseFilePath(filePath);
					TagList list = analyzer.analyze(filePath, null);
					if (!list.getRootTagList().isEmpty()) {
						TagObject rootTag = list.getRootTagList().get(0);
						String contents = generateXSLContents(rootTag);
						SmooksModelUtils.setCDATAToAnyType(template, contents);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public static String generateXSLContents(TagObject tag) {
		String tagXML = tag.toString();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n");
		buffer.append("\t<xsl:template match=\"\\\">\n");
		buffer.append(tagXML);
		buffer.append("\t</xsl:template>\n");
		buffer.append("</xsl:stylesheet>");
		return buffer.toString();
	}

}
