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
package org.jboss.tools.jst.jsp.jspeditor;

import java.util.ResourceBundle;

import org.jboss.tools.common.model.ui.texteditors.AbstractMultiPageContributor;
import org.eclipse.jem.internal.java.adapters.nls.ResourceHandler;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.texteditor.IStatusField;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.ITextEditorExtension;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;
import org.eclipse.wst.sse.ui.internal.actions.ActionDefinitionIds;
import org.eclipse.wst.sse.ui.internal.actions.StructuredTextEditorActionConstants;

import org.jboss.tools.common.text.xml.xpl.ToggleOccurencesMarkUpAction;

/**
 * @author Jeremy
 * 
 */
public class JSPMultiPageContributor extends AbstractMultiPageContributor {
	protected RetargetTextEditorAction fShowTooltipAction = null; // show

	protected RetargetTextEditorAction fContentAssist = null;

	public JSPMultiPageContributor() {
		fToggleOccurencesMarkUp = new ToggleOccurencesMarkUpAction();
	}

	public void init(IActionBars bars) {
		super.init(bars);
		initEditMenu(bars);
		ResourceBundle resourceBundle = ResourceHandler.getResourceBundle();

		fShowTooltipAction = new RetargetTextEditorAction(resourceBundle,
				StructuredTextEditorActionConstants.ACTION_NAME_INFORMATION
						+ StructuredTextEditorActionConstants.DOT);
		fShowTooltipAction
				.setActionDefinitionId(ActionDefinitionIds.INFORMATION);

		fContentAssist = new RetargetTextEditorAction(
				resourceBundle,
				StructuredTextEditorActionConstants.ACTION_NAME_CONTENTASSIST_PROPOSALS
						+ StructuredTextEditorActionConstants.DOT);
		fContentAssist
				.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);

		//TODO-3.3: keep checking if 'quick fix' action appears in WTP
//		fQuickFix = new RetargetTextEditorAction(resourceBundle,
//				StructuredTextEditorActionConstants.ACTION_NAME_QUICK_FIX
//						+ StructuredTextEditorActionConstants.DOT);
//		fQuickFix.setActionDefinitionId(ActionDefinitionIds.QUICK_FIX);
	}

	protected void createAssistObjects() {
		ResourceBundle resourceBundle = ResourceHandler.getResourceBundle();

		fContentAssistProposal = new RetargetTextEditorAction(
				resourceBundle,
				StructuredTextEditorActionConstants.ACTION_NAME_CONTENTASSIST_PROPOSALS
						+ StructuredTextEditorActionConstants.DOT);
		fContentAssistProposal
				.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);

		fContentAssistTip = new RetargetTextEditorAction(resourceBundle,
				StructuredTextEditorActionConstants.ACTION_NAME_INFORMATION
						+ StructuredTextEditorActionConstants.DOT);
		fContentAssistTip
				.setActionDefinitionId(ActionDefinitionIds.INFORMATION);
	}

	public void setActiveEditor(IEditorPart part) {
		IEditorPart activeNestedEditor = null;
		if (part instanceof JSPMultiPageEditor) {
			activeNestedEditor = ((JSPMultiPageEditor) part).getActiveEditor();
		}
		setActivePage(activeNestedEditor);

		mainPart = part;

		IActionBars actionBars = getActionBars();

		if (actionBars != null) {
			IStatusLineManager slm = actionBars.getStatusLineManager();
			if (slm != null) {
				slm.setErrorMessage(null);
				slm.setMessage(null);
			}
		}
	}

	public void setActivePage(IEditorPart part) {
		if (fActiveEditorPart == part)
			return;
		cleanStatusLine();
		fActiveEditorPart = part;
		IActionBars actionBars = getActionBars();
		if (actionBars != null) {

			ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part
					: null;
			if (editor != null) {

				actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(),
						getAction(editor, ITextEditorActionConstants.DELETE));
				actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(),
						getAction(editor, ITextEditorActionConstants.UNDO));
				actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(),
						getAction(editor, ITextEditorActionConstants.REDO));
				actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(),
						getAction(editor, ITextEditorActionConstants.CUT));
				actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
						getAction(editor, ITextEditorActionConstants.COPY));
				actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(),
						getAction(editor, ITextEditorActionConstants.PASTE));
				actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL
						.getId(), getAction(editor,
						ITextEditorActionConstants.SELECT_ALL));
				actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(),
						getAction(editor, ITextEditorActionConstants.FIND));
				actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK
						.getId(), getAction(editor, IDEActionFactory.BOOKMARK
						.getId()));
				actionBars.setGlobalActionHandler(IDEActionFactory.ADD_TASK
						.getId(), getAction(editor, IDEActionFactory.ADD_TASK
						.getId()));
				actionBars.setGlobalActionHandler(ActionFactory.PRINT.getId(),
						getAction(editor, ITextEditorActionConstants.PRINT));
				actionBars.setGlobalActionHandler(ActionFactory.REVERT.getId(),
						getAction(editor, ITextEditorActionConstants.REVERT));
				actionBars.setGlobalActionHandler(ActionFactory.SAVE.getId(),
						getAction(editor, ITextEditorActionConstants.SAVE));

				fShowTooltipAction
						.setAction(getAction(
								editor,
								StructuredTextEditorActionConstants.ACTION_NAME_INFORMATION));
				fContentAssist
						.setAction(getAction(
								editor,
								StructuredTextEditorActionConstants.ACTION_NAME_CONTENTASSIST_PROPOSALS));
				//TODO-3.3: keep checking if 'quick fix' action appears in WTP
                                //				fQuickFix
				//		.setAction(getAction(
				//				editor,
				//				StructuredTextEditorActionConstants.ACTION_NAME_QUICK_FIX));


			}

			// re-register action on key binding service
			IEditorPart localPart = (part != null) ? part : mainPart;
			if (localPart != null) {
				IHandlerService handler = (IHandlerService) localPart
						.getEditorSite().getService(IHandlerService.class);
				if (handler == null && mainPart != null) {
					handler = (IHandlerService) mainPart.getEditorSite()
							.getService(IHandlerService.class);
				}
				if (editor != null && handler != null) {
					// editor
					registerKeyBindings(handler, ACTIONS_2, editor);
					String[] ACTIONS_3 = {
							StructuredTextEditorActionConstants.ACTION_NAME_INFORMATION,
							StructuredTextEditorActionConstants.ACTION_NAME_CONTENTASSIST_PROPOSALS,
// TODO-3.3							StructuredTextEditorActionConstants.ACTION_NAME_QUICK_FIX 
					};
					registerKeyBindings(handler, ACTIONS_3, editor);
				}
			}
			cleanActionBarStatus();
			actionBars.updateActionBars();
		}
		fToggleOccurencesMarkUp.setEditor(getTextEditor(part));
		fToggleOccurencesMarkUp.update();

		updateStatus();
	}

	protected void updateStatus() {
		if (fActiveEditorPart instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) fActiveEditorPart;
			fContentAssistProposal
					.setAction(getAction(
							textEditor,
							StructuredTextEditorActionConstants.ACTION_NAME_CONTENTASSIST_PROPOSALS));
			fContentAssistTip
					.setAction(getAction(
							textEditor,
							StructuredTextEditorActionConstants.ACTION_NAME_INFORMATION));
		}
		if (fActiveEditorPart instanceof ITextEditorExtension) {
			ITextEditorExtension extension = (ITextEditorExtension) fActiveEditorPart;
			for (int i = 0; i < STATUSFIELDS.length; i++)
				extension.setStatusField((IStatusField) fStatusFields
						.get(STATUSFIELDS[i]), STATUSFIELDS[i]);
		}
	}
}
