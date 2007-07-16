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
package org.jboss.tools.vpe.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IStatusField;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorExtension;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mozilla.EditorLoadWindowListener;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.xpl.SashForm;

public class VpeEditorPart extends EditorPart implements ITextEditor, ITextEditorExtension, IReusableEditor, IVisualEditor {
	private SashForm container;
	private StructuredTextEditor sourceEditor=null;
	private MozillaEditor visualEditor;
	private IEditorPart activeEditor;
	private VpeController controller;
	private ActivationListener activationListener = new ActivationListener();
	private int visualMode=0;
	private EditorPart multiPageEditor;
	private static final QualifiedName SPLITTER_POSITION_KEY1 = new QualifiedName("", "splitter_position1");
	private static final QualifiedName SPLITTER_POSITION_KEY2 = new QualifiedName("", "splitter_position2");
	private int controlCount = 0;
	
	public StructuredTextEditor getSourceEditor(){
		return sourceEditor;
	}
	
	// returns JSPMultipageEditor for closing by ctrl+F4 and ctrl+shift+F4 keys
	public EditorPart getParentEditor(){
		return multiPageEditor;
	}
	
	public void close(boolean save) {
		if(sourceEditor!=null)sourceEditor.close(save);
	}
	public void doRevertToSaved() {
		if(sourceEditor!=null)sourceEditor.doRevertToSaved();
	}
	public IDocumentProvider getDocumentProvider() {
		if(sourceEditor!=null)return sourceEditor.getDocumentProvider();
		else{
			return null;
		}
	}
	public IRegion getHighlightRange() {
		if(sourceEditor!=null)return sourceEditor.getHighlightRange();
		else{
			return null;
		}
	}
	public ISelectionProvider getSelectionProvider() {
		if(sourceEditor!=null)return sourceEditor.getSelectionProvider();
		else{
			return null;
		}
	}
	public boolean isEditable() {
		if(sourceEditor!=null)return sourceEditor.isEditable();
		else{
			return false;
		}
	}
	public void removeActionActivationCode(String actionId) {
		if(sourceEditor!=null)sourceEditor.removeActionActivationCode(actionId);
	}
	public void resetHighlightRange() {
		if(sourceEditor!=null)sourceEditor.resetHighlightRange();
	}
	public void selectAndReveal(int offset, int length) {
		if(sourceEditor!=null)sourceEditor.selectAndReveal(offset, length);
	}
	public void setAction(String actionID, IAction action) {
		if(sourceEditor!=null)sourceEditor.setAction(actionID, action);
	}
	public void setActionActivationCode(String actionId,
			char activationCharacter, int activationKeyCode,
			int activationStateMask) {
		if(sourceEditor!=null)sourceEditor.setActionActivationCode(actionId, activationCharacter, activationKeyCode, activationStateMask);
	}
	public void setHighlightRange(int offset, int length, boolean moveCursor) {
		if(sourceEditor!=null)sourceEditor.setHighlightRange(offset, length, moveCursor);
	}
	public void showHighlightRangeOnly(boolean showHighlightRangeOnly) {
		 if(sourceEditor!=null)sourceEditor.showHighlightRangeOnly(showHighlightRangeOnly);
	}
	public boolean showsHighlightRangeOnly() {
		 if(sourceEditor!=null)return sourceEditor.showsHighlightRangeOnly();
			else{
				return false;
			}
	}

	public void addRulerContextMenuListener(IMenuListener listener) {
		if(sourceEditor!=null)sourceEditor.addRulerContextMenuListener(listener);
	}
	public boolean isEditorInputReadOnly() {
		if(sourceEditor!=null)return sourceEditor.isEditorInputReadOnly();
		else{
			return false;
		}
	}
	public void removeRulerContextMenuListener(IMenuListener listener) {
		if(sourceEditor!=null)sourceEditor.removeRulerContextMenuListener(listener);
	}
	public void setStatusField(IStatusField field, String category) {
		if(visualMode == VISUAL_MODE){
			if(field != null){
			field.setImage(null);
			field.setText(null);
			}
		}else 
			if(sourceEditor!=null)sourceEditor.setStatusField(field, category);
	}
	
	
	public VpeEditorPart(EditorPart multiPageEditor, StructuredTextEditor textEditor, boolean visualMode){
		sourceEditor = textEditor;
		//this.visualMode = visualMode;
		this.multiPageEditor = multiPageEditor;
	}
	
	public IAction getAction(String actionID){
		return sourceEditor.getAction(actionID);
	}
	
	public VpeEditorPart(){
	}
	
	public void doSave(IProgressMonitor monitor) {
		if (sourceEditor != null) {
			sourceEditor.doSave(monitor);
		}
	}

	public void doSaveAs() {
		if (sourceEditor != null) {
			sourceEditor.doSaveAs();
			setInput(sourceEditor.getEditorInput());
		}
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	public void setInput(IEditorInput input) {
		super.setInput(input);
		if(visualEditor != null && visualEditor.getEditorInput() != null && visualEditor.getEditorInput() != getEditorInput()) {
			visualEditor.setInput(input);
		}		
	}

	public boolean isDirty() {
		if (sourceEditor != null) {
			return sourceEditor.isDirty();
		} else {
			return false;
		}
	}

	public boolean isSaveAsAllowed() {
		if (sourceEditor != null) {
			return sourceEditor.isSaveAsAllowed();
		} else {
			return false;
		}
	}
	
	protected int[] loadSplitterPosition() {
		int[] sizes = new int[2];
		try { 
			IEditorInput input = getEditorInput();
			if(!(input instanceof IFileEditorInput)) return null;
			IFile file = ((IFileEditorInput)input).getFile();
			String s = file.getPersistentProperty(SPLITTER_POSITION_KEY1);
			if (s != null) {
				sizes[0] = Integer.parseInt(s);
			}else return null;
			s = file.getPersistentProperty(SPLITTER_POSITION_KEY2);
			if (s != null) {
				sizes[1] = Integer.parseInt(s);
			}else return null;
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			return null;
		}
		return sizes;
	}

	protected void saveSplitterPosition(int[] weights) {
		IEditorInput input = getEditorInput();
		if(!(input instanceof IFileEditorInput)) return;
		IFile file = ((IFileEditorInput)input).getFile();
		try { 
			String s = String.valueOf(weights[0]);
			file.setPersistentProperty(SPLITTER_POSITION_KEY1, s);
			s = String.valueOf(weights[1]);
			file.setPersistentProperty(SPLITTER_POSITION_KEY2, s);
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
	}
	Composite sourceContent=null;
	Composite visualContent=null;
	
	public void setVisualMode(int type){
		switch(type){
			case VISUALSOURCE_MODE:
				if(sourceContent!= null)sourceContent.setVisible(true);
				if(visualContent!= null)visualContent.setVisible(true);
				break;
			case VISUAL_MODE:
				if(sourceContent!= null)sourceContent.setVisible(false);
				if(visualContent!= null)visualContent.setVisible(true);
				break;
			case SOURCE_MODE:
				if(sourceContent!= null)sourceContent.setVisible(true);
				if(visualContent!= null)visualContent.setVisible(false);
				break;
		}
		container.layout();
		if(visualMode == SOURCE_MODE && type != SOURCE_MODE) {
			visualMode = type;
			if(controller!=null) {
				controller.visualRefresh();
				controller.sourceSelectionChanged();
			}
		}
		visualMode = type;
	}
	
	public int getVisualMode(){
		return visualMode;
	}

	public void createPartControl(Composite parent) {
		controlCount++;
		if(controlCount > 1)return;
		
			container = new SashForm(parent, SWT.VERTICAL);
			sourceContent = new Composite(container, SWT.NONE);
			sourceContent.setLayout(new FillLayout());
			visualContent = new Composite(container, SWT.NONE);
			visualContent.setLayout(new FillLayout());
			if(sourceEditor == null)sourceEditor = new StructuredTextEditor() {
				public void safelySanityCheckState(IEditorInput input) {
					super.safelySanityCheckState(input);
				}
			};
			int[] weights = loadSplitterPosition();
			if(weights != null)
				container.setWeights(weights);
			container.addWeightsChangeListener(new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent event){
					saveSplitterPosition(container.getWeights());
				}
			});
			parent.addControlListener(new ControlListener(){
				public void controlMoved(ControlEvent event){
					
				}
				public void controlResized(ControlEvent event){
					container.layout();
				}
				});
			visualEditor = new MozillaEditor();
			try {
				visualEditor.init(getEditorSite(), getEditorInput());
			} catch (Exception e) {
				VpePlugin.reportProblem(e);
			}

		try {
			sourceEditor.addPropertyListener(
					new IPropertyListener() {
						public void propertyChanged(Object source, int propId) {
							if (propId == IWorkbenchPartConstants.PROP_TITLE) {
								VpeEditorPart.this.setPartName(sourceEditor.getTitle());
							}
							VpeEditorPart.this.firePropertyChange(propId);
						}
					});
			sourceEditor.init(getEditorSite(), getEditorInput());

			if(sourceContent!=null){
				sourceEditor.createPartControl(sourceContent);
			}
			if(visualEditor!=null) {
				visualEditor.setEditorLoadWindowListener(new EditorLoadWindowListener() {
					public void load() {
						visualEditor.setEditorLoadWindowListener(null);
						controller = new VpeController(VpeEditorPart.this);
						try {
							controller.init(sourceEditor, visualEditor);
						} catch (Exception e) {
							VpePlugin.reportProblem(e);
						}
					}
				});
				visualEditor.createPartControl(visualContent);
			}
			activeEditor = sourceEditor;

			sourceContent.addListener(SWT.Activate, new Listener() {
				public void handleEvent(Event event) {
					if (event.type == SWT.Activate) {
						if (activeEditor != sourceEditor) {
							activeEditor = sourceEditor;
							setFocus();
						}
					}
				}
			});

			visualContent.addListener(SWT.Activate, new Listener() {
				public void handleEvent(Event event) {
					if (event.type == SWT.Activate) {
						if (visualEditor!=null && activeEditor != visualEditor) {
							activeEditor = visualEditor;
							setFocus();
						}
					}
				}
			});

			IWorkbenchWindow window = getSite().getWorkbenchWindow();
			window.getPartService().addPartListener(activationListener);
			window.getShell().addShellListener(activationListener);

		} catch (Exception e) {
			VpePlugin.reportProblem(e);
		}
		//setVisualMode(visualMode);
	}

	public void setFocus() {
		if (activeEditor != null) {
			activeEditor.setFocus();
		}
	}

	public void dispose() {
		super.dispose();

		if (activationListener != null) {
			IWorkbenchWindow window= getSite().getWorkbenchWindow();
			window.getPartService().removePartListener(activationListener);
			Shell shell= window.getShell();
			if (shell != null && !shell.isDisposed())
				shell.removeShellListener(activationListener);
			activationListener = null;
		}
		if (controller != null) {
			controller.dispose();
			controller = null;
		}

		
// editor will disposed as part of multipart editor
		if (sourceEditor != null) {
			sourceEditor.dispose();
			sourceEditor = null;
		}

		if (visualEditor != null) {
			visualEditor.dispose();
			visualEditor = null;
		}
	}

	public Object getAdapter(Class adapter) {
		if (sourceEditor != null) {
			return sourceEditor.getAdapter(adapter);
		} else {
			return null;
		}
	}
	
	
	private class ActivationListener extends ShellAdapter implements IPartListener {
		private IWorkbenchPart fActivePart;
		private boolean fIsHandlingActivation = false;
		
		public void partActivated(IWorkbenchPart part) {
			fActivePart = part;
			handleActivation();
		}
	
		public void partBroughtToTop(IWorkbenchPart part) {
		}
	
		public void partClosed(IWorkbenchPart part) {
		}
	
		public void partDeactivated(IWorkbenchPart part) {
			fActivePart = null;
		}
	
		public void partOpened(IWorkbenchPart part) {
		}
	
		public void shellActivated(ShellEvent e) {
			e.widget.getDisplay().asyncExec(new Runnable() {
				public void run() {
					handleActivation();
				}
			});
		}
		
		private void handleActivation() {
			if (fIsHandlingActivation)
				return;
				
			if (fActivePart == multiPageEditor) {
				fIsHandlingActivation = true;
				try {
					if (sourceEditor != null) {
						if (controller != null) {
							controller.refreshTemplates();
						}
						sourceEditor.safelySanityCheckState(getEditorInput());
					}
				} finally {
					fIsHandlingActivation = false;
				}
			}
		}
	}
	
	public VpeController getController() {
		return controller;
	}
	
}
