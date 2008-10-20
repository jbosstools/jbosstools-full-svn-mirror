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
package org.jboss.tools.smooks.java2xml.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Dart Peng
 * @Date : Sep 25, 2008
 */
public class FreeMarkerContentReplacer {

	public String replaceFreeMarkerTemplate(Reader reader) throws IOException {
		StringBuffer buffer = new StringBuffer();
//		Configuration fmConfiguration = new Configuration();
//		fmConfiguration.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
//		
//		Template template;
//		template = new Template("XML2Java_FreeMarker", reader, fmConfiguration);
//		TemplateElement root = template.getRootTreeNode();
//		visitTemplateElement(root, buffer);
		return buffer.toString();
	}

	public String replaceFreeMarkerTemplate(String templateContents)
			throws IOException {
		Reader reader = new StringReader(templateContents);
		return replaceFreeMarkerTemplate(reader);
	}

//	private void visitTemplateElement(TemplateElement model, StringBuffer buffer) {

//		Enumeration children = ((TemplateElement) model).children();
//		if (!"MixedContent".equals(model.getNodeName())) {
//			buffer
//					.append("<"
//							+ XMLConstants.REPLACE_FREEMARKER_FOR_EXPRESS_ELEMENT_NAME
//							+ " "
//							+ XMLConstants.REPLACE_FREEMARKER_FOR_EXPRESS_ELEMENT_ATTRIBUTE
//							+ "= \"" + model.getDescription() + "\">");
//		}
//		while (children.hasMoreElements()) {
//			Object obj = children.nextElement();
//			if (!(obj instanceof TextBlock)) {
//				TemplateElement child = (TemplateElement) obj;
//				String str = child.getSource();
//				if (child.isLeaf()) {
//					if (child.getNodeName().equals("DollarVariable")) {
//						buffer.append(child.getSource());
//					} else {
//						buffer.append(child.getDescription());
//					}
//					continue;
//				}
//				visitTemplateElement(child, buffer);
//			} else {
//				buffer.append(obj);
//			}
//		}
//		if (!"MixedContent".equals(model.getNodeName())) {
//			buffer.append("</"
//					+ XMLConstants.REPLACE_FREEMARKER_FOR_EXPRESS_ELEMENT_NAME
//					+ ">");
//		}
//	}
}
