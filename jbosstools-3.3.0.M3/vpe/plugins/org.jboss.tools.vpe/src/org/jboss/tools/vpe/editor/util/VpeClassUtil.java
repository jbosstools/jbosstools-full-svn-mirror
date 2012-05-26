/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.editor.util;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.w3c.dom.Node;

/**
 * @author yradtsevich
 *
 */
public class VpeClassUtil {
	private static final String CLASSES_DELIMITER = ","; //$NON-NLS-1$

	/**
	 * Extracts class names from <code>sourceNode</code> specified by a string given
	 * by <code>expression</code>
	 * 
	 * @param expression
	 * @param sourceNode
	 * @param pageContext
	 * @return names of style classes
	 * @throws VpeExpressionException
	 */
	public static List<String> getClasses(VpeExpression expression, Node sourceNode,
			VpePageContext pageContext) throws VpeExpressionException {
		List<String> ret = new ArrayList<String>();
		if (expression != null && sourceNode != null) {
			String classes = expression.exec(pageContext, sourceNode)
					.stringValue();
			String[] a = classes.split(CLASSES_DELIMITER);
			for (int i = 0; i < a.length; i++) {
				String className = a[i].trim();
				if (className.length() > 0) {
					ret.add(className);
				}
			}
		}
		return ret;
	}
}
