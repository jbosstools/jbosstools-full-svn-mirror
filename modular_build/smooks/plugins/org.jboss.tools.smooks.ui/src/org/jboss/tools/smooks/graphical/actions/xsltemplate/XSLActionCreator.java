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
			String[] names = name1.split("_"); //$NON-NLS-1$
			String nn = ""; //$NON-NLS-1$
			for (int j = 0; j < names.length; j++, nn += " ") { //$NON-NLS-1$
				char[] chars = names[j].toCharArray();
				String fN = new String(new char[] { chars[0] });
				fN = fN.toUpperCase();
				char[] nc = new char[chars.length - 1];
				System.arraycopy(chars, 1, nc, 0, chars.length - 1);
				String eN = new String(nc);
				fN = fN + eN;
				nn += fN;
			}

			String label = " \"Add XSL " + nn + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			String content = "addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants." + name + ", " //$NON-NLS-1$ //$NON-NLS-2$
					+ label + ", null);"; //$NON-NLS-1$
			String content1 = "actionRegistry.registerAction(addXSLNodeAction);"; //$NON-NLS-1$
			String content2 = "selectionActions.add(addXSLNodeAction.getId());"; //$NON-NLS-1$
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
		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.ATTRIBUTE,  Messages.XSLActionCreator_Action_Add_XSL_Attribute, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.ATTRIBUTE_SET,  Messages.XSLActionCreator_Action_Add_XSL_Attribute_Set, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.DECIMAL_FORMAT,  Messages.XSLActionCreator_Action_Add_XSL_Decimal_Format, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.IMPORT,  Messages.XSLActionCreator_Action_Add_XSL_Import, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.INCLUDE,  Messages.XSLActionCreator_Action_Add_XSL_Include, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.KEY,  Messages.XSLActionCreator_Action_Add_XSL_Key, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.NAMESPACE_ALIAS,  Messages.XSLActionCreator_Action_Add_XSL_NS_Alias, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.OUTPUT,  Messages.XSLActionCreator_Action_Add_XSL_Output, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.PARAM,  Messages.XSLActionCreator_Action_Add_XSL_Param, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.PRESERVE_SPACE,  Messages.XSLActionCreator_Action_Add_XSL_Preserve_Space, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.STYLESHEET,  Messages.XSLActionCreator_Action_Add_XSL_Stylesheet, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.TEMPLATE,  Messages.XSLActionCreator_Action_Add_XSL_Template, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.VARIABLE,  Messages.XSLActionCreator_Action_Add_XSL_Variable, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.APPLY_TEMPLATES,  Messages.XSLActionCreator_Action_Add_XSL_Apply_Templates, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.CALL_TEMPLATE,  Messages.XSLActionCreator_Action_Add_XSL_Call_Template, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.CHOOSE,  Messages.XSLActionCreator_Action_Add_XSL_Choose, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.COMMENT,  Messages.XSLActionCreator_Action_Add_XSL_Comment, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.COPY,  Messages.XSLActionCreator_Action_Add_XSL_Copy, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.COPY_OF,  Messages.XSLActionCreator_Action_Add_XSL_Copy_Of, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.ELEMENT,  Messages.XSLActionCreator_Action_Add_XSL_Element, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.FALLBACK,  Messages.XSLActionCreator_Action_Add_XSL_Fallback, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.FOR_EACH,  Messages.XSLActionCreator_Action_Add_XSL_For_Each, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.IF,  Messages.XSLActionCreator_Action_Add_XSL_If, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.MESSAGE,  Messages.XSLActionCreator_Action_Add_XSL_Msg, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.NUMBER,  Messages.XSLActionCreator_Action_Add_XSL_Number, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.TEXT,  Messages.XSLActionCreator_Action_Add_XSL_Text, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.VALUE_OF,  Messages.XSLActionCreator_Action_Add_XSL_Value_Of, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());

		addXSLNodeAction = new AddXSLNodeModelAction(editorPart, XSLConstants.PROCESSING_INSTRUCTION,  Messages.XSLActionCreator_Action_Add_XSL_Processing, null);
		actionRegistry.registerAction(addXSLNodeAction);
		selectionActions.add(addXSLNodeAction.getId());



	}
}
