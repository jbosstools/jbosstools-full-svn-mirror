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
package org.jboss.tools.smooks.core;

import java.util.Map;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMLHandler;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * @author Dart
 * 
 */
public class SmooksSAXXMLHandler extends SAXXMLHandler {

	public SmooksSAXXMLHandler(XMLResource xmiResource, XMLHelper helper, Map<?, ?> options) {
		super(xmiResource, helper, options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLHandler#comment(char[], int, int)
	 */
	@Override
	public void comment(char[] ch, int start, int length) {
		String comment = new String(ch, start, length);
		Object obj = objects.get(objects.size() - 1);

		if (mixedTargets.peek() != null) {
			if (text != null) {
				handleMixedText();
			}
			handleComment(comment);

		} else {
			// if (obj != null && obj instanceof AnyType) {
			// FeatureMap featureMap = ((AnyType) obj).getMixed();
			// if (featureMap != null) {
			// try {
			// featureMap.add(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT,
			// comment);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// text = null;
			// }
			// }
			if (obj != null && obj instanceof AbstractAnyType) {
				int currentSize = ((AbstractAnyType) obj).eContents().size();
				((AbstractAnyType) obj).addComment(comment, new Integer(currentSize));
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.ecore.xmi.impl.XMLHandler#handleComment(java.lang.String)
	 */
	@Override
	protected void handleComment(String comment) {
		FeatureMap featureMap = mixedTargets.peek();
		featureMap.add(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT, comment);
		text = null;
	}

}
