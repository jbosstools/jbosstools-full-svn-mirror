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
package org.jboss.tools.jsf.vpe.richfaces.template;

import java.util.List;

import org.jboss.tools.jsf.vpe.richfaces.template.util.RichFaces;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.expression.VpeExpression;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilderException;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionException;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeClassUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Node;

/**
 * Class {@code RichFacesDataTableStyleClassesApplier} should be used in templates
 * of tags that support attributes {@code 'rowClasses'} and {@code 'columnClasses'}.
 * <P/>
 * It expects it is used in pair with {@link RichFacesDataTableChildrenEncoder}.
 * 
 * @author yradtsevich
 */
public class RichFacesDataTableStyleClassesApplier {
	private static final String COLUMN_CLASSES_EXPRESSION = 
		"{@" + RichFaces.ATTR_COLUMN_CLASSES + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String ROW_CLASSES_EXPRESSION = 
		"{@" + RichFaces.ATTR_ROW_CLASSES + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	private final List<String> rowClasses;
	private final List<String> columnClasses;
	
	/**
	 * Constructs {@code RichFacesDataTableStyleClassesApplier}.
	 * 
	 * @param visualDocument an object of {@link nsIDOMDocument} 
	 * @param pageContext an object of {@link VpePageContext}
	 * @param sourceNode an object of a tag that could have
	 *        attributes {@code 'rowClasses'} and {@code 'columnClasses'}.
	 */
	public RichFacesDataTableStyleClassesApplier(final nsIDOMDocument visualDocument,
			final VpePageContext pageContext, final Node sourceNode) {
		VpeExpression rowClassesExpr;
		VpeExpression columnClassesExpr;
		try {
			rowClassesExpr = VpeExpressionBuilder
				.buildCompletedExpression(ROW_CLASSES_EXPRESSION, true).getExpression();
			columnClassesExpr = VpeExpressionBuilder
				.buildCompletedExpression(COLUMN_CLASSES_EXPRESSION, true).getExpression();
		} catch (final VpeExpressionBuilderException e) {
			throw new RuntimeException(e);
		}

		try {
			rowClasses = VpeClassUtil.getClasses(rowClassesExpr, sourceNode,
					pageContext);
			columnClasses = VpeClassUtil.getClasses(columnClassesExpr, sourceNode,
					pageContext);
		} catch (final VpeExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Applies style-classes specified in attributes {@code 'rowClasses'} and 
	 * {@code 'columnClasses'} of {@code sourceNode} to the {@code targetTable}.
	 * <P/>
	 * This method should be used in 
	 * {@link VpeAbstractTemplate#validate(VpePageContext, Node, nsIDOMDocument, VpeCreationData) validate()}
	 * method of a template after 
	 * {@link RichFacesDataTableChildrenEncoder#validateChildren(VpePageContext, Node, nsIDOMDocument, VpeCreationData) RichFacesDataTableChildrenEncoder.validateChildren()}
	 * is called. 
	 * <P/>
	 * The basic idea of the method is to add appropriate rowClasses only
	 * to the direct TR children of the table and add appropriate columnClasses
	 * only to direct TD children of that TRs. It gives us opportunity to separate
	 * TRs and TDs that created by the tag directly from TRs and TDs created
	 * by the tag's children.
	 */
	public void applyClasses(final nsIDOMElement targetTable) {
		final nsIDOMNodeList tableChildren = targetTable.getChildNodes();			
		int rowIndex = 0;
		final int tableChildrenLength = (int) tableChildren.getLength();
		for (int i = 0; i < tableChildrenLength; i++) {
			final nsIDOMNode tableChild = tableChildren.item(i);
			if (tableChild.getNodeType() == nsIDOMNode.ELEMENT_NODE 
					&& HTML.TAG_TR.equalsIgnoreCase( tableChild.getNodeName() )) {
				final nsIDOMElement row = 
					(nsIDOMElement) tableChild.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);

				applyClass(rowClasses, row, rowIndex);

				applyClassesToCells(row);
				rowIndex++;
			}
		}
	}

	/**
	 * Applies style-classes specified in attribute {@code 'columnClasses'}
	 * of {@code sourceNode} to the {@code row}.
	 */
	private void applyClassesToCells(final nsIDOMElement row) {
		final int columnClassesSize = columnClasses.size();
		if (columnClassesSize > 0) {
			final nsIDOMNodeList rowChildren = row.getChildNodes();
			int columnIndex = 0;
			int rowChildrenLength = (int)rowChildren.getLength();
			for (int j = 0; j < rowChildrenLength; j++) {
				final nsIDOMNode rowChild = rowChildren.item(j);
				if (rowChild.getNodeType() == nsIDOMNode.ELEMENT_NODE 
						&& HTML.TAG_TD.equalsIgnoreCase( rowChild.getNodeName() )) {
					final nsIDOMElement cell = 
						(nsIDOMElement) rowChild.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
					
					applyClass(columnClasses, cell, columnIndex);
					
					columnIndex++;
				}
			}
		}
	}

	/**
	 * Applies appropriate style-class from the list {@code classes} to the
	 * {@code element} (a row or a cell) position of that is {@code elementIndex}.
	 */
	private static void applyClass(final List<String> classes, final nsIDOMElement element,
			int elementIndex) {
		int classesSize = classes.size();
		if (classesSize > 0) {
			final String clazz = classes.get(elementIndex % classesSize);
			String actualClass = element.getAttribute(HTML.ATTR_CLASS);
			if (actualClass == null) {
				actualClass = clazz;
			} else {
				actualClass+= HTML.VALUE_CLASS_DELIMITER + clazz;
			}
			element.setAttribute(HTML.ATTR_CLASS, actualClass);
		}
	}
}
