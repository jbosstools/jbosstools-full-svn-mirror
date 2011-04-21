/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template;

import java.util.Map;

import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;

public class VpeLoadBundleCreator extends VpeAbstractCreator {
	private static final String ATTR_BASENAME = "basename"; //$NON-NLS-1$
	private static final String ATTR_VAR = "var"; //$NON-NLS-1$

	VpeLoadBundleCreator(Element bundleElement, VpeDependencyMap dependencyMap) {
		build(bundleElement, dependencyMap);
	}

	private void build(Element bundleElement, VpeDependencyMap dependencyMap) {
		dependencyMap.setCreator(this, VpeExpressionBuilder.attrSignature(ATTR_BASENAME, true));
		dependencyMap.setCreator(this, VpeExpressionBuilder.attrSignature(ATTR_VAR, true));
	}

	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {
		setBundle(pageContext, (Element)sourceNode);
		return null;
	}
	
	
	public void removeElement(VpePageContext pageContext, Element sourceElement, Map visualNodeMap) {
		/*
		 * Was commented to fix https://jira.jboss.org/jira/browse/JBIDE-4552
		 * Because after text formatting on Ctrl+Shift+F bundle gets unnecessary deleted
		 * from bundle map, that caused message displaying error.
		 * All necessary bundle refreshing job is done by VpeController, 
		 * and there is no need to perform any additional work on removing a bundle.
		 */
//		BundleMap bundle = pageContext.getBundle();
//		bundle.removeBundle(sourceElement.hashCode());
	}

	public void setAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name, String value) {
		setBundle(pageContext, sourceElement);
		pageContext.refreshBundleValues();
	}

	public void removeAttribute(VpePageContext pageContext, Element sourceElement, Map visualNodeMap, String name) {
		setBundle(pageContext, sourceElement);
		pageContext.refreshBundleValues();
	}
	
	private void setBundle(VpePageContext pageContext, Element sourceElement) {
		String basename = sourceElement.getAttribute(ATTR_BASENAME);
		String var = sourceElement.getAttribute(ATTR_VAR);
		BundleMap bundle = pageContext.getBundle();
		if (basename != null && basename.length() > 0 && var != null && var.length() > 0) {
			bundle.changeBundle(sourceElement.hashCode(), var, basename);
		} else {
			bundle.removeBundle(sourceElement.hashCode());
		}
	}
}
