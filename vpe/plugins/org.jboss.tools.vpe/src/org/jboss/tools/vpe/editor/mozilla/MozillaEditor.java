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
package org.jboss.tools.vpe.editor.mozilla;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.internal.part.StatusPart;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage;
import org.jboss.tools.vpe.editor.toolbar.IVpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.VpeDropDownMenu;
import org.jboss.tools.vpe.editor.toolbar.VpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.toolbar.format.TextFormattingToolBar;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.DocTypeUtil;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.ReferenceWizard;
import org.jboss.tools.vpe.resref.core.VpeResourcesDialog;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIEditingSession;
import org.mozilla.interfaces.nsIEditor;
import org.mozilla.interfaces.nsIHTMLAbsPosEditor;
import org.mozilla.interfaces.nsIHTMLInlineTableEditor;
import org.mozilla.interfaces.nsIHTMLObjectResizer;
import org.mozilla.interfaces.nsIPlaintextEditor;

public class MozillaEditor extends EditorPart implements IReusableEditor {
	protected static final File INIT_FILE = new File(VpePlugin.getDefault().getResourcePath("ve"), "init.html"); //$NON-NLS-1$ //$NON-NLS-2$
	public static final String CONTENT_AREA_ID = "__content__area__"; //$NON-NLS-1$
	
	/*
	 * Paths for tool bar icons
	 */
	public static final String ICON_PREFERENCE = "icons/preference.gif"; //$NON-NLS-1$
	public static final String ICON_PREFERENCE_DISABLED = "icons/preference_disabled.gif"; //$NON-NLS-1$
	public static final String ICON_REFRESH = "icons/refresh.gif"; //$NON-NLS-1$
	public static final String ICON_REFRESH_DISABLED = "icons/refresh_disabled.gif"; //$NON-NLS-1$
	public static final String ICON_PAGE_DESIGN_OPTIONS = "icons/point_to_css.gif"; //$NON-NLS-1$
	public static final String ICON_PAGE_DESIGN_OPTIONS_DISABLED = "icons/point_to_css_disabled.gif"; //$NON-NLS-1$
	public static final String ICON_ORIENTATION_SOURCE_LEFT = "icons/source_left.gif"; //$NON-NLS-1$
	public static final String ICON_ORIENTATION_SOURCE_TOP = "icons/source_top.gif"; //$NON-NLS-1$
	public static final String ICON_ORIENTATION_SOURCE_RIGHT = "icons/source_right.gif"; //$NON-NLS-1$
	public static final String ICON_ORIENTATION_SOURCE_BOTTOM = "icons/source_bottom.gif"; //$NON-NLS-1$
	public static final String ICON_ORIENTATION_SOURCE_LEFT_DISABLED = "icons/source_left_disabled.gif"; //$NON-NLS-1$

	static String SELECT_BAR = "SELECT_LBAR"; //$NON-NLS-1$
	private XulRunnerEditor xulRunnerEditor;
	private nsIDOMEventTarget documentEventTarget;
	private nsIDOMElement contentArea;
	private nsIDOMNode headNode;
	private nsIDOMEventTarget contentAreaEventTarget;
	private MozillaDomEventListener contentAreaEventListener;

	private EditorLoadWindowListener editorLoadWindowListener;
	private EditorDomEventListener editorDomEventListener;
	private IVpeToolBarManager vpeToolBarManager;
	private FormatControllerManager formatControllerManager = new FormatControllerManager();
	private VpeController controller;
	private boolean isRefreshPage = false;
	private String doctype;
	
	private static Map<String, String> layoutIcons;
	private static Map<String, String> layoutNames;
	private static List<String> layoutValues;
	static {
	    layoutIcons = new HashMap<String, String>();
	    layoutIcons.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE, ICON_ORIENTATION_SOURCE_LEFT);
	    layoutIcons.put(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE, ICON_ORIENTATION_SOURCE_TOP);
	    layoutIcons.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE, ICON_ORIENTATION_SOURCE_RIGHT);
	    layoutIcons.put(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE, ICON_ORIENTATION_SOURCE_BOTTOM);
	    
	    layoutNames = new HashMap<String, String>();
	    layoutNames.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE, VpeUIMessages.SPLITTING_HORIZ_LEFT_SOURCE);
	    layoutNames.put(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE, VpeUIMessages.SPLITTING_VERT_TOP_SOURCE);
	    layoutNames.put(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE, VpeUIMessages.SPLITTING_HORIZ_LEFT_VISUAL);
	    layoutNames.put(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE, VpeUIMessages.SPLITTING_VERT_TOP_VISUAL);

	    layoutValues= new ArrayList<String>();
	    layoutValues.add(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_SOURCE_VALUE);
	    layoutValues.add(IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE);
	    layoutValues.add(IVpePreferencesPage.SPLITTING_HORIZ_LEFT_VISUAL_VALUE);
	    layoutValues.add(IVpePreferencesPage.SPLITTING_VERT_TOP_VISUAL_VALUE);

	}
	
	/**
	 * Used for manupalation of browser in design mode,
	 * for example enable or disable readOnlyMode
	 */
	private nsIEditor editor;
	private VpeDropDownMenu dropDownMenu = null;
	private ToolBar verBar = null;

	public void doSave(IProgressMonitor monitor) {
	}

	public void doSaveAs() {
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
		super.setInput(input);
	}

	public void setInput(IEditorInput input) {
		boolean isVisualRefreshRequired = (getEditorInput() != null && getEditorInput() != input && controller != null);
		super.setInput(input);
		if(isVisualRefreshRequired) controller.visualRefresh();
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void setController(VpeController controller){
		this.controller = controller;
		formatControllerManager.setVpeController(controller);
		controller.setToolbarFormatControllerManager(formatControllerManager);
	}
	
	public ToolBar createVisualToolbar(Composite parent) {
		verBar = new ToolBar(parent, SWT.VERTICAL|SWT.FLAT);
		verBar.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		ToolItem item = null;
		item = createToolItem(verBar, SWT.BUTTON1, ICON_PREFERENCE,
			ICON_PREFERENCE_DISABLED, VpeUIMessages.PREFERENCES, true);
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				VpeEditorPreferencesPage.openPreferenceDialog();
			}
		});

		item = createToolItem(verBar, SWT.BUTTON1, ICON_REFRESH,
			ICON_REFRESH_DISABLED, VpeUIMessages.REFRESH, true);
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(controller!=null) {
					controller.visualRefresh();
				}
			}
		});
		
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-3966
		 * Disabling Page Design Options for external files. 
		 */
		IEditorInput input = getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		} else if (input instanceof ILocationProvider) {
			ILocationProvider provider = (ILocationProvider) input;
			IPath path = provider.getPath(input);
			if (path != null) {
			    file = FileUtil.getFile(input, path.lastSegment());
			}
		}
		boolean fileExistsInWorkspace = ((file != null) && (file.exists()));
		item = createToolItem(verBar, SWT.BUTTON1, ICON_PAGE_DESIGN_OPTIONS,
			ICON_PAGE_DESIGN_OPTIONS_DISABLED,
			VpeUIMessages.PAGE_DESIGN_OPTIONS, fileExistsInWorkspace);
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IEditorInput input = getEditorInput();
				Object fileLocation = null;
				if (input instanceof IFileEditorInput) {
					IFile file = ((IFileEditorInput) input).getFile();
					fileLocation = file;
				} else if (input instanceof ILocationProvider) {
					ILocationProvider provider = (ILocationProvider) input;
					IPath path = provider.getPath(input);
					if (path != null) {
						fileLocation = path;
					}
				}
				if (null != fileLocation) {
					VpeResourcesDialog dialogNew = 
						new VpeResourcesDialog(PlatformUI.getWorkbench().getDisplay()
									.getActiveShell(), fileLocation);
					dialogNew.open();
				} else {
					VpePlugin.getDefault().logError("Could not open Vpe Resources Dialog."); //$NON-NLS-1$
				}
			}
		});

		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4152
		 * Compute initial icon state and add it to the tool bar.
		 */
		int currentOrientationIndex = layoutValues.indexOf(JspEditorPlugin
				.getDefault().getPreferenceStore().getString(
						IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING));
		int newIndx = currentOrientationIndex+1;
		if (newIndx == layoutValues.size()) {
		    newIndx = newIndx % layoutValues.size();
		}
		String newOrientation = layoutValues.get(newIndx);

		final ToolItem rotateEditorsItem = createToolItem(verBar, SWT.BUTTON1,
				layoutIcons.get(newOrientation),
				ICON_ORIENTATION_SOURCE_LEFT_DISABLED, 
				layoutNames.get(newOrientation), true);
		
		rotateEditorsItem.addListener(SWT.Selection, new Listener() {
		    public void handleEvent(Event event) {
				/*
				 * Rotate editors orientation clockwise. Store this new
				 * orientation to the preferences.
				 */
				int currentOrientationIndex = layoutValues
						.indexOf(JspEditorPlugin
								.getDefault()
								.getPreferenceStore()
								.getString(
										IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING));
				int newIndx = currentOrientationIndex + 1;
				if (newIndx == layoutValues.size()) {
					newIndx = newIndx % layoutValues.size();
				}
				String newOrientation = layoutValues.get(newIndx);
				JspEditorPlugin.getDefault().getPreferenceStore().setValue(
						IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING,
						newOrientation);

				/*
				 * Compute next step orientation and display appropriate icon.
				 */
				currentOrientationIndex = layoutValues
						.indexOf(JspEditorPlugin
								.getDefault()
								.getPreferenceStore()
								.getString(
										IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING));
				newIndx = currentOrientationIndex + 1;
				if (newIndx == layoutValues.size()) {
					newIndx = newIndx % layoutValues.size();
				}
				newOrientation = layoutValues.get(newIndx);
				rotateEditorsItem.setImage(ImageDescriptor.createFromFile(
						MozillaEditor.class, layoutIcons.get(newOrientation))
						.createImage());
				rotateEditorsItem.setToolTipText(layoutNames.get(newOrientation));
				/*
				 * Call <code>filContainer()</code> from VpeEditorPart
				 * to redraw CustomSashForm with new layout.
				 */
				getController().getPageContext().getEditPart().fillContainer();
		    }
		});

		verBar.pack();
		return verBar;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
    	//Setting  Layout for the parent Composite
//		parent.setLayout(new FillLayout());
		
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4062
		 * Creating scrollable eclipse element.
		 */
//		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		sc.setLayout(new FillLayout());
//		Composite composite = new Composite(parent, SWT.NATIVE);

	    GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 2;
		layout.verticalSpacing = 2;
		layout.horizontalSpacing = 2;
		layout.marginBottom = 0;
		parent.setLayout(layout);
//		composite.setLayout(layout);
//		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		// Editors and Toolbar composite 
		Composite cmpEdTl = new Composite(parent, SWT.NONE);
		GridLayout layoutEdTl = new GridLayout(1, false);
		layoutEdTl.verticalSpacing = 0;
		layoutEdTl.marginHeight = 0;
		layoutEdTl.marginBottom = 3;
		layoutEdTl.marginWidth = 0;
		cmpEdTl.setLayout(layoutEdTl);
		cmpEdTl.setLayoutData(new GridData(GridData.FILL_BOTH));

		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4429
		 * Toolbar was moved to VpeEditorPart.
		 *  'verBar' should be created in createVisualToolbar(..) in VpeEditorPart
		 *  and only after that MozillaEditor should be created itself. 
		 */
		if (null != verBar) {
			dropDownMenu = new VpeDropDownMenu(verBar, VpeUIMessages.MENU); 
			// add Invisible tags support to menu
			// create menu item
			MenuItem menuItem = new MenuItem(dropDownMenu.getDropDownMenu(), SWT.PUSH);
			// get default value of flag
			boolean showInvisibleTags = JspEditorPlugin.getDefault().getPreferenceStore().getBoolean(
					IVpePreferencesPage.SHOW_NON_VISUAL_TAGS);
			
			// set text
			menuItem.setText(showInvisibleTags ? VpeUIMessages.HIDE_NON_VISUAL_TAGS
					: VpeUIMessages.SHOW_NON_VISUAL_TAGS);
			
			// add listener
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					MenuItem selectedItem = (MenuItem) e.widget;
					
					// get current value of flag
					boolean showInvisibleTags = !controller.getVisualBuilder()
					.isShowInvisibleTags();
					
					// change text
					selectedItem
					.setText((showInvisibleTags ? VpeUIMessages.HIDE
							: VpeUIMessages.SHOW)
							+ Constants.WHITE_SPACE
							+ VpeUIMessages.NON_VISUAL_TAGS);
					
					// change flag
					controller.getVisualBuilder().setShowInvisibleTags(
							showInvisibleTags);
					// update vpe
					controller.visualRefresh();
				}
			});
			// Use vpeToolBarManager to create a horizontal toolbar.
			vpeToolBarManager = new VpeToolBarManager(dropDownMenu
					.getDropDownMenu());
			if (vpeToolBarManager != null) {
				vpeToolBarManager.createToolBarComposite(cmpEdTl);
				vpeToolBarManager.addToolBar(new TextFormattingToolBar(formatControllerManager));
			}
		}

		
		
		//Create a composite to the Editor
		Composite cmpEd = new Composite (cmpEdTl, SWT.NATIVE);
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

		//TODO Add a paintListener  to cmpEd and give him a border top and left only
		Color buttonDarker = parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		cmpEd.setBackground(buttonDarker);

		try {
			xulRunnerEditor = new XulRunnerEditor(cmpEd) {
				public void onLoadWindow() {
					// if the first load page
					if (!isRefreshPage()) {
						super.onLoadWindow();
//						MozillaEditor.this.onLoadWindow();
					}
					// if only refresh page
					else {
						
						MozillaEditor.this.onReloadWindow();
						setRefreshPage(false);
					}

				}
				public void onElementResize(nsIDOMElement element, int constrains, int top, int left, int width, int height) {
					if (editorDomEventListener != null) {
						editorDomEventListener.elementResized(element, constrains, top, left, width, height);
					}
				}
				public void onShowTooltip(int x, int y, String text) {
					if (editorDomEventListener != null) {
						editorDomEventListener.onShowTooltip(x, y, text);
					}
				}
				public void onHideTooltip() {
					if (editorDomEventListener != null) {
						editorDomEventListener.onHideTooltip();
					}
				}
				public void onDispose() {
					removeDomEventListeners();
					super.onDispose();
				}
			};
			xulRunnerEditor.getBrowser().addProgressListener(new ProgressListener() {

				public void changed(ProgressEvent event) {
				}

				public void completed(ProgressEvent event) {
					MozillaEditor.this.onLoadWindow();
					xulRunnerEditor.getBrowser().removeProgressListener(this);
				}
				
			});
			
			setInitialContent();
			// Wait while visual part is loaded
			//commented by mareshkau, fix for jbide-3032
//			while (!loaded) {
//				if (!Display.getCurrent().readAndDispatch())
//					Display.getCurrent().sleep();
//			}
			xulRunnerEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
		} catch (Throwable t) {
			showXulRunnerException(cmpEd, t);
		}
		
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4062
		 * Computing elements sizes to set up scroll bars.
		 */
//		Point totalSize = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//		sc.setContent(composite);
//		sc.setExpandHorizontal(true);
//		sc.setExpandVertical(true);
//		sc.setMinSize(totalSize);
	}

	/**
	 * Sets initial content to the {@link xulRunnerEditor}.
	 *
	 * @see #INIT_FILE
	 */
	protected void setInitialContent() {
		final String html = DocTypeUtil.prepareInitFile(
				INIT_FILE, getEditorInput());
		
		// Workaround of JBIDE-4345.
		// Due to a bug in org.eclipse.swt.browser.Mozilla we cannot simply
		// set initial html code as xulRunnerEditor.setText(html).
		// Instead of it we create a temporary file containing 
		// the html code and set it to the Mozilla browser as URL.		
		File tmp = null;
		Writer out = null;
		try {
			tmp = File.createTempFile(
					"temp", ".html"); //$NON-NLS-1$//$NON-NLS-2$
			tmp.deleteOnExit();
			out = new FileWriter(tmp);
			out.write(html);
		} catch (IOException e) {
			VpePlugin.getPluginLog().logError(e);
		} finally {
			try {
				if (out != null) {
					out.close();
					if (tmp != null) {
						xulRunnerEditor.setURL("file://"	//$NON-NLS-1$
								+ tmp.getCanonicalPath());
					}
				}
			} catch (IOException e) {
				VpePlugin.getPluginLog().logError(e);
			} finally {
				if (tmp != null) {
					tmp.delete();
				}
			}
		}
	}

	/**
	 * Logs given {@code throwable} and shows error message in 
	 * the {@code parent} composite.
	 */
	protected void showXulRunnerException(Composite parent,
			Throwable throwable) {
		String errorMessage = MessageFormat.format(
				VpeUIMessages.MOZILLA_LOADING_ERROR, throwable.getMessage());

		VpePlugin.getPluginLog().logError(errorMessage, throwable);

		parent.setLayout(new GridLayout());
		Composite statusComposite = new Composite(parent, SWT.NONE);
		Color bgColor= parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		Color fgColor= parent.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		parent.setBackground(bgColor);
		parent.setForeground(fgColor);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = 0;
		gridData.heightHint = 0;
		statusComposite.setLayoutData(gridData);

		IStatus displayStatus = new Status(IStatus.ERROR,
				VpePlugin.PLUGIN_ID, errorMessage, throwable);
		new StatusPart(statusComposite, displayStatus);

		final Link link = new Link(parent, SWT.WRAP);
		link.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		link.setBackground(bgColor);
		link.setText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK_TEXT);
		link.setToolTipText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
		link.setForeground(link.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		link.addMouseListener(new MouseListener() {
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
		        BusyIndicator.showWhile(link.getDisplay(), new Runnable() {
		            public void run() {
		                URL theURL=null;;
						try {
							theURL = new URL(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
						} catch (MalformedURLException e) {
							VpePlugin.reportProblem(e);
						}
		                IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
		                try {
							support.getExternalBrowser().openURL(theURL);
						} catch (PartInitException e) {
							VpePlugin.reportProblem(e);
						}
		            }
		        });
		    }

			public void mouseDoubleClick(MouseEvent e) {
			}
			public void mouseUp(MouseEvent e) {
			}
		});
	}

	private ToolItem createToolItem(ToolBar parent, int type, String image, String disabledImage,
		String toolTipText, boolean isEnabled) {
	    
		ToolItem item = new ToolItem(parent, type);
		item.setImage(ImageDescriptor
				.createFromFile(MozillaEditor.class, image).createImage());
		item.setToolTipText(toolTipText);
		item.setDisabledImage(ImageDescriptor
			.createFromFile(MozillaEditor.class, disabledImage).createImage());
		item.setEnabled(isEnabled);
		// add dispose listener
		item.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				// dispose tollitem's image
				((ToolItem) e.widget).getImage().dispose();
			}
		});
		return item;
	}

	public void setFocus() {
		if(xulRunnerEditor!=null) {
			xulRunnerEditor.setFocus();
		} else {
			//link.setFocus();
		}
	}

	public void dispose() {
		if (vpeToolBarManager != null) {
			vpeToolBarManager.dispose();
			vpeToolBarManager = null;
		}
		
		if (dropDownMenu != null) {
			dropDownMenu.dispose();
			dropDownMenu = null;
		}

//		removeDomEventListeners();
		if(getController()!=null) {
			controller.dispose();
			controller=null;
		}
		if (xulRunnerEditor != null) {
			xulRunnerEditor.dispose();
			xulRunnerEditor = null;
		}

		this.controller = null;
		formatControllerManager.setVpeController(null);
		formatControllerManager=null;
		super.dispose();
		
	}

	public void setEditorLoadWindowListener(EditorLoadWindowListener listener) {
		editorLoadWindowListener = listener;
	}

	public void setEditorDomEventListener(EditorDomEventListener listener) {
		editorDomEventListener = listener;
		if (getContentAreaEventListener() != null) {
			
			getContentAreaEventListener().setEditorDomEventListener(listener);
		}
	}

	public nsIDOMDocument getDomDocument() {
			return xulRunnerEditor.getDOMDocument();
	}
	

	public nsIDOMElement getContentArea() {
		return contentArea;
	}
	/**
	 * Sets content area element
	 * @return
	 */
	protected void setContentArea(nsIDOMElement element) {
		
		 this.contentArea=element;
	}

	public nsIDOMNode getHeadNode() {
		return headNode;
	}

	public Menu getMenu() {
		return xulRunnerEditor.getBrowser().getMenu();
	}

	public Control getControl() {
		return xulRunnerEditor.getBrowser();
	}

	protected nsIDOMElement findContentArea() {
		nsIDOMElement area = null;
		nsIDOMNodeList nodeList = xulRunnerEditor.getDOMDocument().getElementsByTagName(HTML.TAG_BODY);
		long length = nodeList.getLength();
		for(long i=0; i<length; i++) {
			nsIDOMNode node = nodeList.item(i);
			if (isContentArea(node)) {
				if (node.getNodeType() != nsIDOMNode.ELEMENT_NODE) {
					throw new RuntimeException("The content area node should by element node."); //$NON-NLS-1$
				}

				area = (nsIDOMElement) node.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				break;
			}
		}
		if (area == null) {
			//fix for jbide-3396, if we can't find a boody element, we should create it
			area = xulRunnerEditor.getDOMDocument().createElement(HTML.TAG_BODY);
			xulRunnerEditor.getDOMDocument().getDocumentElement().appendChild(area);
		}

		nsIDOMNode root = xulRunnerEditor.getDOMDocument().getDocumentElement();
		
		headNode = findHeadNode(root);

		return area;
	}

	private nsIDOMNode findHeadNode(nsIDOMNode root){
		nsIDOMNode headNode = findChildNode(root, HTML.TAG_HEAD); 
		return headNode;
	}

	private nsIDOMNode findChildNode(nsIDOMNode parent, String name) {
		nsIDOMNodeList list = parent.getChildNodes();
		nsIDOMNode node;
		for (int i=0;i<list.getLength();i++) {
			node = list.item(i);
			if (node.getNodeName().equalsIgnoreCase(name)) {
				return node;
			}
		}
		return null;
	}

	private boolean isContentArea(nsIDOMNode node) {
		boolean ret = false;
    	if (HTML.TAG_BODY.equalsIgnoreCase(node.getNodeName())) {
   	    	nsIDOMNamedNodeMap map = node.getAttributes();
			if (map != null) {
				long length = map.getLength();
    			for (int i = 0; i < length; i++) {
    				nsIDOMNode attr = map.item(i);
    				ret = attr.getNodeType() == nsIDOMNode.ATTRIBUTE_NODE
    						&& HTML.ATTR_ID.equalsIgnoreCase(attr.getNodeName())
							&& CONTENT_AREA_ID.equalsIgnoreCase(attr.getNodeValue());
    				if (ret) {
    	    			break;
    				}
    			}
			}
    	}
    	return ret;
	}

	private void onLoadWindow() {
		contentArea = findContentArea();
		addDomEventListeners();
		addSelectionListener();
		if (editorLoadWindowListener != null) {
			editorLoadWindowListener.load();
		}
	}

	protected void addDomEventListeners() {
		if (contentArea != null) {
			if (getContentAreaEventListener() != null) {
				getContentAreaEventListener().setVisualEditor(xulRunnerEditor);
				setContentAreaEventTarget((nsIDOMEventTarget) contentArea.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID));
				
				//add mozilla event handlers
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.CLICKEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.MOUSEDOWNEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.MOUSEUPEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.MOUSEMOVEEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.CONTEXTMENUEVENTTYPE, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGDROPEVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGENTEREVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGEXITEVENT,getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGGESTUREEVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGOVEREVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DBLCLICK, getContentAreaEventListener(), false);
				documentEventTarget = (nsIDOMEventTarget) xulRunnerEditor.getDOMDocument().queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				documentEventTarget.addEventListener(MozillaDomEventListener.KEYPRESS, getContentAreaEventListener(), false);
			} else {
				//baseEventListener = new MozillaBaseEventListener();
			}
		}
	}

	protected void removeDomEventListeners() {
		if (getContentAreaEventTarget() != null && getContentAreaEventListener() != null) {
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.CLICKEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.MOUSEDOWNEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.MOUSEUPEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.MOUSEMOVEEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.CONTEXTMENUEVENTTYPE, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGDROPEVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGENTEREVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGEXITEVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGGESTUREEVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGOVEREVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DBLCLICK, getContentAreaEventListener(), false);
		
			if (xulRunnerEditor.getDOMDocument() != null && documentEventTarget != null) {
				documentEventTarget.removeEventListener(MozillaDomEventListener.KEYPRESS, getContentAreaEventListener(), false); 
			}
			getContentAreaEventListener().setVisualEditor(null);
			getContentAreaEventListener().setEditorDomEventListener(null);
			setContentAreaEventTarget(null);
			setContentAreaEventListener(null);
			documentEventTarget = null;
		}
	}

	private void addSelectionListener() {
		if (getContentAreaEventListener() != null&&xulRunnerEditor!=null) {
			
			xulRunnerEditor.addSelectionListener(getContentAreaEventListener());
			
		}
	}

	public void setSelectionRectangle(/*nsIDOMElement*/nsIDOMNode element, int resizerConstrains, boolean scroll) {
		if (getContentAreaEventListener() != null) {
			xulRunnerEditor.setSelectionRectangle(element, resizerConstrains, scroll);
		}
	}

	/**
	 * Show resizer markers
	 */
	public void showResizer() {
		if (getContentAreaEventListener() != null) {
			xulRunnerEditor.showResizer();
		}
	}

	/**
	 * Hide resizer markers
	 */
	public void hideResizer() {
		if (getContentAreaEventListener() != null) {
			xulRunnerEditor.hideResizer();
		}
	}

	/**
	 * @return the xulRunnerEditor
	 */
	public XulRunnerEditor getXulRunnerEditor() {
		return xulRunnerEditor;
	}

	/**
	 * @param xulRunnerEditor the xulRunnerEditor to set
	 */
	protected void setXulRunnerEditor(XulRunnerEditor xulRunnerEditor) {
		this.xulRunnerEditor = xulRunnerEditor;
	}

	/**
	 * @return the controller
	 */
	public VpeController getController() {
		return controller;
	}

	/**
	 * @return the contentAreaEventTarget
	 */
	public nsIDOMEventTarget getContentAreaEventTarget() {
		return contentAreaEventTarget;
	}

	/**
	 * @param contentAreaEventTarget the contentAreaEventTarget to set
	 */
	public void setContentAreaEventTarget(nsIDOMEventTarget contentAreaEventTarget) {
		this.contentAreaEventTarget = contentAreaEventTarget;
	}

	/**
	 * @return the contentAreaEventListener
	 */
	public MozillaDomEventListener getContentAreaEventListener() {
		if(contentAreaEventListener==null) {
			
			contentAreaEventListener = new MozillaDomEventListener();
		}
		return contentAreaEventListener;
	}

	/**
	 * @param contentAreaEventListener the contentAreaEventListener to set
	 */
	public void setContentAreaEventListener(
			MozillaDomEventListener contentAreaEventListener) {
		
		this.contentAreaEventListener = contentAreaEventListener;
	}
	
	
	/**
	 * 
	 */
	private void onReloadWindow() {
		removeDomEventListeners();
		xulRunnerEditor.removeResizerListener();
		contentArea = findContentArea();
		addDomEventListeners();
		addSelectionListener();
		xulRunnerEditor.addResizerListener();
		controller.reinit();
	}
	
	/**
	 * 
	 */
	public void reload() {
		
		doctype = DocTypeUtil.getDoctype(getEditorInput());
		//coused page to be refreshed
		setRefreshPage(true);
		setInitialContent();
	}
	/**
	 * Initialized design mode in visual refresh
	 */
	public void initDesingMode() {
		editor=null;
		getEditor();
	}
	
	/**
	 * @return Doctype for document
	 */
	public String getDoctype() {
		return doctype;
	}
	
	private boolean isRefreshPage() {
		return isRefreshPage;
	}

	public void setRefreshPage(boolean isRefreshPage) {
		this.isRefreshPage = isRefreshPage;
	}
	
	public void reinitDesignMode() {
		editor=null;
		getEditor();
	}
	/**
	 * Returns Editor for This Document
	 * @return
	 */
	public nsIEditor getEditor() {
		
		if(editor==null) {
			//creating editing session
			nsIEditingSession iEditingSession = (nsIEditingSession) getXulRunnerEditor().
							getComponentManager().createInstanceByContractID(XPCOM.NS_EDITINGSESSION_CONTRACTID, null, nsIEditingSession.NS_IEDITINGSESSION_IID);
			//make window editable
			iEditingSession.makeWindowEditable(getXulRunnerEditor().getWebBrowser().getContentDOMWindow(), "html", true); //$NON-NLS-1$
			//here we setup editor for window
			iEditingSession.setupEditorOnWindow(getXulRunnerEditor().getWebBrowser().getContentDOMWindow());
			//getting some editor to disable some actions
			editor = iEditingSession.getEditorForWindow(getXulRunnerEditor().getWebBrowser().getContentDOMWindow());
			editor.setFlags(nsIPlaintextEditor.eEditorReadonlyMask);
			//here we hide nsIHTMLObjectResizers
			nsIHTMLObjectResizer htmlObjectResizer = (nsIHTMLObjectResizer) editor.queryInterface(nsIHTMLObjectResizer.NS_IHTMLOBJECTRESIZER_IID);
			//we disable abject resizers
			htmlObjectResizer.hideResizers();
			htmlObjectResizer.setObjectResizingEnabled(false);
			//here we getting position editor and disable it's too
			nsIHTMLAbsPosEditor htmlAbsPosEditor = (nsIHTMLAbsPosEditor) editor.queryInterface(nsIHTMLAbsPosEditor.NS_IHTMLABSPOSEDITOR_IID);
			htmlAbsPosEditor.setAbsolutePositioningEnabled(false);
			//here we getting inline table editor and disable it's too
			nsIHTMLInlineTableEditor inlineTableEditor = (nsIHTMLInlineTableEditor) editor.queryInterface(nsIHTMLInlineTableEditor.NS_IHTMLINLINETABLEEDITOR_IID);
			inlineTableEditor.setInlineTableEditingEnabled(false);
			
		}
		return editor;
	}

	public VpeDropDownMenu getDropDownMenu() {
		return dropDownMenu;
	}
}
