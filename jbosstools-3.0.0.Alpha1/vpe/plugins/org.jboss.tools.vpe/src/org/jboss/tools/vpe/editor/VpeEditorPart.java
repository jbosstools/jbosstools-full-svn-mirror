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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IStatusField;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorExtension;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;
import org.jboss.tools.jst.jsp.jspeditor.StorageRevisionEditorInputAdapter;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.IVpeHelpContextIds;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mozilla.EditorLoadWindowListener;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.mozilla.MozillaPreview;
import org.jboss.tools.vpe.editor.xpl.CustomSashForm;
import org.jboss.tools.vpe.editor.xpl.EditorSettings;
import org.jboss.tools.vpe.editor.xpl.SashSetting;
import org.jboss.tools.vpe.selbar.SelectionBar;

public class VpeEditorPart extends EditorPart implements ITextEditor,
		ITextEditorExtension, IReusableEditor, IVisualEditor {
	private IContextActivation fContextActivation;
	private IHandlerActivation sourceActivation,visualActivation, jumpingActivation;
	private IHandler sourceMaxmin,visualMaxmin, jumping;
	private CustomSashForm container;
	protected EditorSettings editorSettings;
	private StructuredTextEditor sourceEditor = null;
	private MozillaEditor visualEditor;
	private IEditorPart activeEditor;
	private XModelTreeListener listener;
	private XModelObject optionsObject;
	private SelectionBar selectionBar = new SelectionBar();
	private ActivationListener activationListener = new ActivationListener();
	private int visualMode = 0;
	private EditorPart multiPageEditor;
	private static final QualifiedName SPLITTER_POSITION_KEY1 = new QualifiedName(
			"", "splitter_position1");
	private static final QualifiedName SPLITTER_POSITION_KEY2 = new QualifiedName(
			"", "splitter_position2");
	private static final QualifiedName SPLITTER_POSITION_KEY3 = new QualifiedName(
			"", "splitter_position3");

	private int controlCount = 0;

	/** default web-browser */
	private MozillaPreview previewWebBrowser = null;

	/** preview content */
	private Composite previewContent = null;

	public StructuredTextEditor getSourceEditor() {
		return sourceEditor;
	}

	// returns JSPMultipageEditor for closing by ctrl+F4 and ctrl+shift+F4 keys
	public EditorPart getParentEditor() {
		return multiPageEditor;
	}

	public void close(boolean save) {
		if (sourceEditor != null)
			sourceEditor.close(save);
	}

	public void doRevertToSaved() {
		if (sourceEditor != null)
			sourceEditor.doRevertToSaved();
	}

	public IDocumentProvider getDocumentProvider() {
		if (sourceEditor != null)
			return sourceEditor.getDocumentProvider();
		else {
			return null;
		}
	}

	public IRegion getHighlightRange() {
		if (sourceEditor != null)
			return sourceEditor.getHighlightRange();
		else {
			return null;
		}
	}

	public ISelectionProvider getSelectionProvider() {
		if (sourceEditor != null)
			return sourceEditor.getSelectionProvider();
		else {
			return null;
		}
	}

	public boolean isEditable() {
		if (sourceEditor != null)
			return sourceEditor.isEditable();
		else {
			return false;
		}
	}

	public void removeActionActivationCode(String actionId) {
		if (sourceEditor != null)
			sourceEditor.removeActionActivationCode(actionId);
	}

	public void resetHighlightRange() {
		if (sourceEditor != null)
			sourceEditor.resetHighlightRange();
	}

	public void selectAndReveal(int offset, int length) {
		if (sourceEditor != null)
			sourceEditor.selectAndReveal(offset, length);
	}

	public void setAction(String actionID, IAction action) {
		if (sourceEditor != null)
			sourceEditor.setAction(actionID, action);
	}

	public void setActionActivationCode(String actionId,
			char activationCharacter, int activationKeyCode,
			int activationStateMask) {
		if (sourceEditor != null)
			sourceEditor.setActionActivationCode(actionId, activationCharacter,
					activationKeyCode, activationStateMask);
	}

	public void setHighlightRange(int offset, int length, boolean moveCursor) {
		if (sourceEditor != null)
			sourceEditor.setHighlightRange(offset, length, moveCursor);
	}

	public void showHighlightRangeOnly(boolean showHighlightRangeOnly) {
		if (sourceEditor != null)
			sourceEditor.showHighlightRangeOnly(showHighlightRangeOnly);
	}

	public boolean showsHighlightRangeOnly() {
		if (sourceEditor != null)
			return sourceEditor.showsHighlightRangeOnly();
		else {
			return false;
		}
	}

	public void addRulerContextMenuListener(IMenuListener listener) {
		if (sourceEditor != null)
			sourceEditor.addRulerContextMenuListener(listener);
	}

	public boolean isEditorInputReadOnly() {
		if (sourceEditor != null)
			return sourceEditor.isEditorInputReadOnly();
		else {
			return false;
		}
	}

	public void removeRulerContextMenuListener(IMenuListener listener) {
		if (sourceEditor != null)
			sourceEditor.removeRulerContextMenuListener(listener);
	}

	public void setStatusField(IStatusField field, String category) {
		if (visualMode == VISUAL_MODE) {
			if (field != null) {
				field.setImage(null);
				field.setText(null);
			}
		} else if (sourceEditor != null)
			sourceEditor.setStatusField(field, category);
	}

	public VpeEditorPart(EditorPart multiPageEditor,
			StructuredTextEditor textEditor, boolean visualMode) {
		sourceEditor = textEditor;
		// this.visualMode = visualMode;
		this.multiPageEditor = multiPageEditor;
	}

	public IAction getAction(String actionID) {
		return sourceEditor.getAction(actionID);
	}

	public VpeEditorPart() {
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

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		if (editorSettings == null)
			editorSettings = EditorSettings.getEditorSetting(this);
		else if (input instanceof FileEditorInput) {
			editorSettings.setInput((FileEditorInput) input);
		}

	}

	public void setInput(IEditorInput input) {
		super.setInput(input);
		if (visualEditor != null && visualEditor.getEditorInput() != null
				&& visualEditor.getEditorInput() != getEditorInput()) {
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
		int[] sizes = new int[3];
		try {
			IEditorInput input = getEditorInput();
			if (!(input instanceof IFileEditorInput))
				return null;

			IFile file = ((IFileEditorInput) input).getFile();
			String s = file.getPersistentProperty(SPLITTER_POSITION_KEY1);
			if (s != null) {
				sizes[0] = Integer.parseInt(s);
			} else
				return null;

			s = file.getPersistentProperty(SPLITTER_POSITION_KEY2);
			if (s != null) {
				sizes[1] = Integer.parseInt(s);
			} else
				return null;

			s = file.getPersistentProperty(SPLITTER_POSITION_KEY3);
			if (s != null) {
				sizes[2] = Integer.parseInt(s);
			} else
				return null;

		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			return null;
		}
		return sizes;
	}

	protected void saveSplitterPosition(int[] weights) {
		IEditorInput input = getEditorInput();
		if (!(input instanceof IFileEditorInput))
			return;
		IFile file = ((IFileEditorInput) input).getFile();
		try {
			String s = String.valueOf(weights[0]);
			file.setPersistentProperty(SPLITTER_POSITION_KEY1, s);
			s = String.valueOf(weights[1]);
			file.setPersistentProperty(SPLITTER_POSITION_KEY2, s);
			s = String.valueOf(weights[2]);
			file.setPersistentProperty(SPLITTER_POSITION_KEY3, s);
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
	}

	Composite sourceContent = null;
	Composite visualContent = null;

	public void setVisualMode(int type) {
		String showSelectionBar = VpePreference.SHOW_SELECTION_TAG_BAR
				.getValue();
		switch (type) {
		case VISUALSOURCE_MODE:
			selectionBar.showBar(showSelectionBar);
			if (sourceContent != null)
				sourceContent.setVisible(true);
			if (visualContent != null)
				visualContent.setVisible(true);
			if (previewContent != null) {
				previewContent.setVisible(false);
			}
			break;

		case VISUAL_MODE:
			selectionBar.showBar(showSelectionBar);
			if (sourceContent != null)
				sourceContent.setVisible(false);
			if (visualContent != null)
				visualContent.setVisible(true);
			if (previewContent != null) {
				previewContent.setVisible(false);
			}
			break;

		case SOURCE_MODE:
			selectionBar.showBar(showSelectionBar);
			if (sourceContent != null) {
				sourceContent.setVisible(true);
				
				//Added by Max Areshkau
				//was fixed bug(border which drawed by iflasher doesn't hide on MACOS when we swith
				// to souce view)
//				if(Platform.getOS().equals(Platform.OS_MACOSX)&&controller!=null) {
//					
//				visualEditor.getController().visualRefresh();
//				}
			}
			if (visualContent != null)
				visualContent.setVisible(false);
			if (previewContent != null) {
				previewContent.setVisible(false);
			}
			break;

		case PREVIEW_MODE:
			if (selectionBar != null) {
				selectionBar.showBar("no");
			}
			if (sourceContent != null) {
				sourceContent.setVisible(false);
			}

			if (visualContent != null) {
				visualContent.setVisible(false);
			}

			if (previewContent != null) {
				previewWebBrowser.rebuildDom();
				previewContent.setVisible(true);
			}
			break;
		}
		container.layout();
		if (visualMode == SOURCE_MODE && type != SOURCE_MODE) {
			visualMode = type;
			if (visualEditor != null && visualEditor.getController() != null) {
				visualEditor.getController().visualRefresh();
				if(type!=PREVIEW_MODE) {
				visualEditor.getController().sourceSelectionChanged();
				}
			}
		}
		visualMode = type;
	}

	public int getVisualMode() {
		return visualMode;
	}

	public void createPartControl(final Composite parent) {
		controlCount++;
		if (controlCount > 1)
			return;
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IVpeHelpContextIds.VISUAL_PAGE_EDITOR);
		// //////////////////////////////////////////////////////////////

		Composite cmpEdTl = new Composite(parent, SWT.NONE);
		GridLayout layoutEdTl = new GridLayout(1, false);
		layoutEdTl.verticalSpacing = 0;
		layoutEdTl.marginHeight = 0;
		layoutEdTl.marginBottom = 3;
		layoutEdTl.marginWidth = 0;
		cmpEdTl.setLayout(layoutEdTl);
		cmpEdTl.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite cmpEd = new Composite(cmpEdTl, SWT.NATIVE);
		GridLayout layoutEd = new GridLayout(1, false);
		layoutEd.marginBottom = 0;
		layoutEd.marginHeight = 1;
		layoutEd.marginWidth = 0;
		layoutEd.marginRight = 0;
		layoutEd.marginLeft = 1;
		layoutEd.verticalSpacing = 0;
		layoutEd.horizontalSpacing = 0;
		cmpEd.setLayout(layoutEd);
		cmpEd.setLayoutData(new GridData(GridData.FILL_BOTH));
		// /////////////////////////////////////////////////////////////////
		//container = new SashForm(cmpEd, SWT.VERTICAL);
		container = new CustomSashForm(cmpEd, SWT.VERTICAL);
		if (editorSettings != null)
			editorSettings.addSetting(new SashSetting(container));

		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sourceContent = new Composite(container, SWT.NONE);
		sourceContent.setLayout(new FillLayout());
		visualContent = new Composite(container, SWT.NONE);
		visualContent.setLayout(new FillLayout());

		// Create a preview content
		previewContent = new Composite(container, SWT.NONE);
		//previewContent.setLayout(new FillLayout());
		previewContent.setLayout(new GridLayout());

		// ////////////////////////////////////////////////////

		selectionBar.createToolBarComposite(cmpEdTl, true);
		// ///////////////////////////////////////////////////
		if (sourceEditor == null)
			sourceEditor = new StructuredTextEditor() {
				public void safelySanityCheckState(IEditorInput input) {
					super.safelySanityCheckState(input);
				}
			};
		int[] weights = loadSplitterPosition();
		if (weights != null)
			container.setWeights(weights);
		container.setSashBorders(new boolean[] { true, true, true });
		
		final PropertyChangeListener weightsChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				saveSplitterPosition(container.getWeights());
			}
		};
		container.addWeightsChangeListener(weightsChangeListener);
		container.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				container.removeWeightsChangeListener(weightsChangeListener);
				container.removeDisposeListener(this);
			}
			
		});
		final ControlListener controlListener = new ControlListener() {
			public void controlMoved(ControlEvent event) {

			}

			public void controlResized(ControlEvent event) {
				container.layout();
			}
		};
		parent.addControlListener(controlListener);
		parent.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				parent.removeControlListener(controlListener);
				parent.removeDisposeListener(this);
			}
			
		});
		
		final ControlListener visualContentControlListener = new ControlListener() {
			public void controlMoved(ControlEvent event) {

			}

			public void controlResized(ControlEvent event) {
				Point point = visualContent.getSize();
				if (point.x == 0 || point.y == 0) {
					VpeController controller = getController();
					if (controller != null)
						controller.setVisualEditorVisible(false);
				} else {
					VpeController controller = getController();
					if (controller != null
							&& !controller.isVisualEditorVisible()) {
						controller.setVisualEditorVisible(true);
						if (!controller.isSynced())
							controller.rebuildDom();
					}
				}
			}
		};
		visualContent.addControlListener(visualContentControlListener);
		visualContent.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				visualContent
						.removeControlListener(visualContentControlListener);
				visualContent.removeDisposeListener(this);
			}

		});

		// createVisualEditor();

		// createPreviewBrowser();

		try {
			sourceEditor.addPropertyListener(new IPropertyListener() {
				public void propertyChanged(Object source, int propId) {
					if (propId == IWorkbenchPartConstants.PROP_TITLE) {
						VpeEditorPart.this.setPartName(sourceEditor.getTitle());
					}
					VpeEditorPart.this.firePropertyChange(propId);
				}
			});
			IEditorInput input = getEditorInput();
			if (!( input instanceof IModelObjectEditorInput) && input instanceof IStorageEditorInput) {
				input = new StorageRevisionEditorInputAdapter((IStorageEditorInput) input);
			}
			sourceEditor.init(getEditorSite(), input);

			if (sourceContent != null) {
				sourceEditor.createPartControl(sourceContent);
			}
			

			/*if (previewWebBrowser != null) {
				previewWebBrowser
						.setEditorLoadWindowListener(new EditorLoadWindowListener() {
							public void load() {
								previewWebBrowser
										.setEditorLoadWindowListener(null);
								previewWebBrowser.buildDom();
							}
						});
				previewWebBrowser.createPartControl(previewContent);
			}
*/
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
						if (visualEditor != null
								&& activeEditor != visualEditor) {
							activeEditor = visualEditor;
							setFocus();
						}
					}
				}
			});

			previewContent.addListener(SWT.Activate, new Listener() {
				public void handleEvent(Event event) {
					if (event.type == SWT.Activate) {
						if (previewWebBrowser != null
								&& activeEditor != previewWebBrowser) {
							activeEditor = previewWebBrowser;
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
		// setVisualMode(visualMode);
		// ///////////////////////////////////////
		// ///// Add preference listener
		optionsObject = ModelUtilities.getPreferenceModel().getByPath(
				VpePreference.EDITOR_PATH);
		listener = new XModelTreeListener() {

			public void nodeChanged(XModelTreeEvent event) {
				String showSelectionBar = VpePreference.SHOW_SELECTION_TAG_BAR
						.getValue();
				selectionBar.showBar(showSelectionBar);
			}

			public void structureChanged(XModelTreeEvent event) {
			}

		};
		optionsObject.getModel().addModelTreeListener(listener);
		if (editorSettings != null)
			editorSettings.apply();

		// ///////////////////////////////////////
		cmpEd.layout();
		sourceMaxmin = new AbstractHandler() {
			public Object execute(ExecutionEvent event)
					throws ExecutionException {
				if (getVisualMode() == IVisualEditor.VISUALSOURCE_MODE) {
					Point p = visualContent.getSize();
					if (p.x == 0 || p.y == 0) {
						container.upClicked();
					} else {
						container.maxDown();
					}
				}
				return null;
			}
		};
		visualMaxmin = new AbstractHandler() {
			public Object execute(ExecutionEvent event)
					throws ExecutionException {
				if (getVisualMode() == IVisualEditor.VISUALSOURCE_MODE) {
					Point p = sourceContent.getSize();
					if (p.x == 0 || p.y == 0) {
						container.downClicked();
					} else {
						container.maxUp();
					}
				}
				return null;
			}
		};
		jumping = new AbstractHandler() {
			public Object execute(ExecutionEvent event)
					throws ExecutionException {
				if (getVisualMode() == IVisualEditor.VISUALSOURCE_MODE) {
					StructuredTextEditor editor = getSourceEditor();
					if (editor == null)
						return null;
					StructuredTextViewer viewer = editor.getTextViewer();
					if (viewer == null)
						return null;
					StyledText widget = viewer.getTextWidget();
					if (widget == null || widget.isDisposed())
						return null;
					if (widget.isFocusControl()) {
						if (visualEditor != null
								&& activeEditor != visualEditor) {
							activeEditor = visualEditor;
							setFocus();
							//visualContent.setFocus();
						}
					} else {
						if (activeEditor != sourceEditor) {
							activeEditor = sourceEditor;
							setFocus();
						}
					}

				}
				return null;
			}
		};
	}

	public void createVisualEditor() {
		visualEditor = new MozillaEditor();
		try {
			visualEditor.init(getEditorSite(), getEditorInput());
		} catch (Exception e) {
			VpePlugin.reportProblem(e);
		}
		if (visualEditor != null) {
			visualEditor
					.setEditorLoadWindowListener(new EditorLoadWindowListener() {
						public void load() {
							visualEditor.setEditorLoadWindowListener(null);
							visualEditor.setController(new VpeController(
									VpeEditorPart.this));
							selectionBar.setVpeController(visualEditor.getController());
							visualEditor.getController().setSelectionBarController(selectionBar);
							try {
								visualEditor.getController().init(sourceEditor, visualEditor);
							} catch (Exception e) {
								VpePlugin.reportProblem(e);
							}
						}
					});
			visualEditor.createPartControl(visualContent);
		}
	}

	public void createPreviewBrowser() {
		previewWebBrowser = new MozillaPreview(this, sourceEditor);
		try {
			previewWebBrowser.init(getEditorSite(), getEditorInput());
			previewWebBrowser
					.setEditorLoadWindowListener(new EditorLoadWindowListener() {
						public void load() {
							previewWebBrowser.setEditorLoadWindowListener(null);
							previewWebBrowser.buildDom();
						}
					});
			previewWebBrowser.createPartControl(previewContent);
		} catch (Exception e) {
			VpePlugin.reportProblem(e);
		}
	}

	public void setFocus() {
		if (activeEditor != null) {
			activeEditor.setFocus();
		}
	}

	public void dispose() {
		if (optionsObject != null) {
			optionsObject.getModel().removeModelTreeListener(listener);
			listener=null;
			optionsObject = null;
		}
		if (editorSettings != null) {
			editorSettings.dispose();
			editorSettings = null;
		}
		if (activationListener != null) {
			IWorkbenchWindow window = getSite().getWorkbenchWindow();
			window.getPartService().removePartListener(activationListener);
			Shell shell = window.getShell();
			if (shell != null && !shell.isDisposed())
				shell.removeShellListener(activationListener);
			activationListener = null;
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

		if (previewWebBrowser != null) {
			previewWebBrowser.dispose();
			previewWebBrowser=null;
		}
		if (previewContent != null) {
			previewContent.dispose();
			previewContent = null;
		}
		
		if (selectionBar != null) {
			selectionBar.dispose();
			selectionBar = null;
		}
		activeEditor = null;
		multiPageEditor = null;
		super.dispose();
	}

	public Object getAdapter(Class adapter) {
		if (sourceEditor != null) {
			return sourceEditor.getAdapter(adapter);
		} else {
			return null;
		}
	}

	private class ActivationListener extends ShellAdapter implements
			IPartListener {
		private static final String VPE_EDITOR_CONTEXT = "org.jboss.tools.vpe.editorContext";
		private static final String VPE_VISUAL_MAXMIN = "org.jboss.tools.vpe.visual.maxmin";
		private static final String VPE_SOURCE_MAXMIN = "org.jboss.tools.vpe.source.maxmin";
		private static final String VPE_JUMPING = "org.jboss.tools.vpe.jumping";
		private IWorkbenchPart fActivePart;
		private boolean fIsHandlingActivation = false;

		public void partActivated(IWorkbenchPart part) {
			fActivePart = part;
			handleActivation();
			if (part == multiPageEditor)
				activateKeys();
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
		}

		public void partDeactivated(IWorkbenchPart part) {
			fActivePart = null;
			if (part == multiPageEditor) {
				IWorkbench workbench = PlatformUI.getWorkbench();
				if (fContextActivation != null) {
					IContextService contextService = (IContextService) workbench
							.getAdapter(IContextService.class);
					contextService.deactivateContext(fContextActivation);
				}

				IHandlerService handlerService = (IHandlerService) workbench
						.getService(IHandlerService.class);
				if (handlerService != null) {
					if (sourceActivation != null)
						handlerService.deactivateHandler(sourceActivation);
					if (visualActivation != null)
						handlerService.deactivateHandler(visualActivation);
					if (jumpingActivation != null)
						handlerService.deactivateHandler(jumpingActivation);
				}
			}
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
						if (visualEditor != null)
						//added by estherbin
						//fix http://jira.jboss.com/jira/browse/JBIDE-2337
						if ((visualEditor.getController() != null) && !container.isHidden()) {
							visualEditor.getController().refreshTemplates();
						}
						sourceEditor.safelySanityCheckState(getEditorInput());
					}
				} finally {
					fIsHandlingActivation = false;
				}
			}
		}

		private void activateKeys() {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IContextService contextService = (IContextService) workbench
					.getAdapter(IContextService.class);
			fContextActivation = contextService
					.activateContext(VPE_EDITOR_CONTEXT); //$NON-NLS-1$
			IHandlerService handlerService = (IHandlerService) workbench
					.getService(IHandlerService.class);
			if (handlerService != null) {
				sourceActivation = handlerService.activateHandler(
						VPE_SOURCE_MAXMIN,
						sourceMaxmin);
				visualActivation = handlerService.activateHandler(
						VPE_VISUAL_MAXMIN,
						visualMaxmin);
				jumpingActivation = handlerService.activateHandler(
						VPE_JUMPING,
						jumping);
			}
		}
	}

	public VpeController getController() {
		if (visualEditor == null)
			return null;
		return visualEditor.getController();
	}

	public MozillaPreview getPreviewWebBrowser() {
		return previewWebBrowser;
	}

	public MozillaEditor getVisualEditor() {
		return visualEditor;
	}

	public void maximizeSource() {
		if (container != null)
			container.maxDown();
	}
	
	public void maximizeVisual() {
		if (container != null)
			container.maxUp();
	}
}
