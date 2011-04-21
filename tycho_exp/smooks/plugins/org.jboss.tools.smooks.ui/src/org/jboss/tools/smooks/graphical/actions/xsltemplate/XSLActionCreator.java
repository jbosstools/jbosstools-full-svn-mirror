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
package org.jboss.tools.smooks.graphical.actions.xsltemplate;

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;

/**
 * @author Dart
 * 
 */
public class XSLActionCreator {

	public static void main(String[] args) {

		Field[] fields = XSLConstants.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String name = fields[i].getName();

			String name1 = name;
			name1 = name1.toLowerCase();
			String[] names = name1.split("_");
			String nn = "";
			for (int j = 0; j < names.length; j++, nn += " ") {
				char[] chars = names[j].toCharArray();
				String fN = new String(new char[] { chars[0] });
				fN = fN.toUpperCase();
				char[] nc = new char[chars.length - 1];
				System.arraycopy(chars, 1, nc, 0, chars.length - 1);
				String eN = new String(nc);
				fN = fN + eN;
				nn += fN;
			}

			String label = " \"Add XSL " + nn + "\"";
			String content = "addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants." + name + ", "
					+ label + ", null);";
			String content1 = "actionRegistry.registerAction(addXSLNodeAction);";
			String content2 = "selectionActions.add(addXSLNodeAction.getId());";
			System.out.println(content);
			System.out.println(content1);
			System.out.println(content2);
			System.out.println();
		}

	}

	public void registXSLActions(ActionRegistry actionRegistry, List selectionActions, IEditorPart editorPart) {
		// add xsl actions
		IAction addXSLNodeAction = new AddElementAction(editorPart);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddAttributeAction(editorPart);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());
		// if(true) return;
		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.ATTRIBUTE,  "Add XSL Attribute ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.ATTRIBUTE_SET,  "Add XSL Attribute Set ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.DECIMAL_FORMAT,  "Add XSL Decimal Format ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.IMPORT,  "Add XSL Import ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.INCLUDE,  "Add XSL Include ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.KEY,  "Add XSL Key ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.NAMESPACE_ALIAS,  "Add XSL Namespace Alias ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.OUTPUT,  "Add XSL Output ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.PARAM,  "Add XSL Param ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.PRESERVE_SPACE,  "Add XSL Preserve Space ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.STYLESHEET,  "Add XSL Stylesheet ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.TEMPLATE,  "Add XSL Template ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.VARIABLE,  "Add XSL Variable ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.APPLY_TEMPLATES,  "Add XSL Apply Templates ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.CALL_TEMPLATE,  "Add XSL Call Template ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.CHOOSE,  "Add XSL Choose ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.COMMENT,  "Add XSL Comment ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.COPY,  "Add XSL Copy ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.COPY_OF,  "Add XSL Copy Of ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.ELEMENT,  "Add XSL Element ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.FALLBACK,  "Add XSL Fallback ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.FOR_EACH,  "Add XSL For Each ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.IF,  "Add XSL If ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.MESSAGE,  "Add XSL Message ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.NUMBER,  "Add XSL Number ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.TEXT,  "Add XSL Text ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.VALUE_OF,  "Add XSL Value Of ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.PROCESSING_INSTRUCTION,  "Add XSL Processing Instruction ", null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());



	}
}
