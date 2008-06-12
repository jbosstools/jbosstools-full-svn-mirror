/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.ui.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.ws.core.classpath.JbossWSRuntime;
import org.jboss.tools.ws.ui.JbossWSUIPlugin;
import org.jboss.tools.ws.ui.messages.JbossWSUIMessages;

/**
 * @author Grid Qian
 */
public class JbwsLibraryListFieldEditor extends BaseFieldEditor {

	// ------------------------------------------------------------------------
	// Layout parameters
	// ------------------------------------------------------------------------

	static final int GL_COLUMNS = 2;
	static final int GL_HINT_HEIGHT = 200;
	static final int TC_DEFAULT_WIDTH = 21;
	static final int TC_NAME_WIDTH = 100;
	static final int TC_VERSION_WIDTH = 50;
	static final int TC_PATH_WIDTH = 100;

	// ------------------------------------------------------------------------
	// Field declarations
	// ------------------------------------------------------------------------

	private TreeViewer listView = null;

	private Composite root = null;

	private ActionPanel actionPanel;
	
	private JbossWSRuntime tempJbws;




	private Group jarGroup;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Control for editing jbossWSRuntime list
	 * 
	 * @param name
	 *            String
	 * @param label
	 *            String
	 * @param defaultValue
	 *            Object
	 */
	public JbwsLibraryListFieldEditor(String name, String label,
			JbossWSRuntime jbws) {
		super(name, label, jbws);
		this.tempJbws = new JbossWSRuntime();
		if(jbws != null){
			this.tempJbws.setUserConfigClasspath(jbws.isUserConfigClasspath());
			this.tempJbws.getLibraries().addAll(jbws.getLibraries());
		}
		
	}


	public Object getValue(){
		return this.tempJbws;
	}
	/**
	 * TBD
	 * 
	 * @param composite
	 *            Object - instance of Composite
	 * @return Object[]
	 */
	@Override
	public Object[] getEditorControls(Object composite) {

		root = new Composite((Composite) composite, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		root.setLayoutData(gd);

		root.setLayout(new GridLayout());
		
		createCheckButton(root);
		
		jarGroup = new Group(root, SWT.BORDER);
		jarGroup.setText("Library Jars");
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true; 
		
		
		jarGroup.setLayoutData(gd);
		jarGroup.setLayout(new FormLayout());
		
		createListView(jarGroup);
		createActionBar(jarGroup);

		FormData listData = new FormData();
		listData.left = new FormAttachment(0, 5);
		listData.right = new FormAttachment(actionPanel, -5);
		listData.top = new FormAttachment(0, 5);
		listData.bottom = new FormAttachment(100, -5);
		listView.getControl().setLayoutData(listData);

		FormData actionsData = new FormData();
		actionsData.top = new FormAttachment(0, 5);
		actionsData.bottom = new FormAttachment(100, -5);
		actionsData.right = new FormAttachment(100, -5);
		actionPanel.setLayoutData(actionsData);
		
		setJarGroupStatus();
		return new Control[] { root };
	}

	protected void createCheckButton(Composite parent){
		final Button btnDefault = new Button(parent, SWT.CHECK);
		btnDefault.setText(JbossWSUIMessages.JBossWS_Runtime_Check_Field_Default_Classpath);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		btnDefault.setLayoutData(gd);
		
		btnDefault.setSelection(tempJbws.isUserConfigClasspath());
		btnDefault.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				tempJbws.setUserConfigClasspath(btnDefault.getSelection());
				setJarGroupStatus();
				setValue(null);
			}
		});
		
		
	}
	
	protected void setJarGroupStatus(){
		boolean isUserConfig = tempJbws.isUserConfigClasspath();
		jarGroup.setEnabled(isUserConfig);
		listView.getTree().setEnabled(isUserConfig);
		actionPanel.setEnabled(isUserConfig);
	}
	@SuppressWarnings("unchecked")
	protected void createListView(Composite parent) {
		listView = new TreeViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		listView.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		listView.setContentProvider(new ITreeContentProvider() {

			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof JbossWSRuntime) {
					return ((JbossWSRuntime) inputElement).getLibraries().toArray();
				} else {
					throw new IllegalArgumentException(
							JbossWSUIMessages.JBossWS_Runtime_List_Field_Editor_Inputelement_Must_Be
									+ JbossWSUIMessages.JBossWS_Runtime_List_Field_Editor_An_Instance_Of_List);
				}
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				viewer.refresh();
			}

			public Object[] getChildren(Object parentElement) {
				// TODO Auto-generated method stub
				return null;
			}

			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean hasChildren(Object element) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		listView.setLabelProvider(new ILabelProvider() {

			Image jarImg;
			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}

			public Image getImage(Object element) {
				if (jarImg == null){
					ImageDescriptor jarImgDesc = JbossWSUIPlugin.getImageDescriptor("obj16/jar_obj.gif"); 
					jarImg = jarImgDesc.createImage();
				}
				return jarImg;
			}

			public String getText(Object element) {
				String fullName = (String)element;
				File jarFile = new File(fullName);
				return jarFile.getName() + " - " + jarFile.getParentFile().toString();
			}
		});

		
		listView.setInput(getValue());
		
	}

	protected void createActionBar(Composite parent) {
		actionPanel = new ActionPanel(parent, new BaseAction[] { new AddAction(),
				 new RemoveAction() });
		listView.addSelectionChangedListener(actionPanel);
	}

	

	/**
	 * Return array of Controls that forms and editor
	 * 
	 * @return Control[]
	 */
	@Override
	public Object[] getEditorControls() {
		return new Control[] { root };
	}

	/**
	 * Return number of controls in editor
	 * 
	 * @return int
	 */
	@Override
	public int getNumberOfControls() {
		return 1;
	}

	/**
	 * Fill wizard page with editors
	 * 
	 * @param parent
	 *            Composite - parent composite
	 */
	@Override
	public void doFillIntoGrid(Object parent) {
		Assert.isTrue(parent instanceof Composite,
				JbossWSUIMessages.Error_JBossWS_Basic_Editor_Composite);
		Assert.isTrue(((Composite) parent).getLayout() instanceof GridLayout,
				JbossWSUIMessages.Error_JBossWS_Basic_Editor_Support);
		Composite aComposite = (Composite) parent;
		getEditorControls(aComposite);
		GridLayout gl = (GridLayout) ((Composite) parent).getLayout();

		GridData gd = new GridData();
		gd.horizontalSpan = gl.numColumns;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;

		((Control) getEditorControls()[0]).setLayoutData(gd);
	}



	/**
	 * Composite that holds list of BaseActions and presents them as column of
	 * buttons
	 * 
	 */
	public static class ActionPanel extends Composite implements
			ISelectionChangedListener {

		private BaseAction[] actions = null;

		/**
		 * Constructor creates panel with style, grid layout and buttons
		 * represented the actions
		 * 
		 * @param parent
		 *            Composite
		 * @param style
		 *            int
		 * @param actions
		 *            BaseAction[]
		 */
		public ActionPanel(Composite parent, int style, BaseAction[] actions) {
			super(parent, style);
			this.actions = actions;
			setLayout(new GridLayout(1, false));
			for (BaseAction action : this.actions) {
				new ActionButton(this, SWT.PUSH, action);
			}
		}

		/**
		 * Constructor creates panel with default style, grid layout and buttons
		 * represented the actions
		 * 
		 * @param parent
		 *            Composite
		 * @param actions
		 *            BaseAction[]
		 */
		public ActionPanel(Composite parent, BaseAction[] actions) {
			this(parent, SWT.NONE, actions);
		}

		/**
		 * Listen to the selection changes and update actions state
		 * (enable/disable)
		 * 
		 * @param event
		 *            SelectionChangeEvent
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			for (BaseAction action : actions) {
				action.setSelection(event.getSelection());
			}
		}
	}

	/**
	 * Class represents an BaseAction as SWT button control and runs action when
	 * button is prtessed
	 * 
	 */
	public static class ActionButton implements IPropertyChangeListener {

		private Button button;
		private BaseAction action;

		/**
		 * Create Button control with parent control and style that represents
		 * action
		 * 
		 * @param parent
		 *            Composite
		 * @param style
		 *            int
		 * @param action
		 *            BaseAction
		 */
		public ActionButton(Composite parent, int style, BaseAction action) {
			this.button = new Button(parent, style);
			this.action = action;

			GridData gd = new GridData(GridData.FILL_HORIZONTAL,
					GridData.CENTER, false, false);

			gd.horizontalAlignment = GridData.FILL;
			gd.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
			this.button.setLayoutData(gd);
			this.action.addPropertyChangeListener(this);
			this.button.setText(action.getText());
			this.button.setEnabled(action.isEnabled());
			this.button.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					ActionButton.this.action.run();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});

		}

		/**
		 * Return SWT button control that calls provided action
		 * 
		 * @return Control - button swt control
		 */
		public Control getControl() {
			return button;
		}

		/**
		 * Update enabled/disabled button state
		 * 
		 * @param event
		 *            PropertyChangeEvent
		 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty().equals(IAction.ENABLED)) {
				button.setEnabled(((Boolean) event.getNewValue())
						.booleanValue());
			}
		}
	}

	/**
	 * Action that changes state enable/disable based on current table selection
	 * 
	 */
	public abstract class BaseAction extends Action {

		String[] jars = new String[0];

		/**
		 * Constructor creates action with provided name
		 * 
		 * @param name
		 *            String - action name
		 */
		public BaseAction(String name) {
			super(name);
			updateEnablement();
		}

		/**
		 * Set current selection
		 * 
		 * @param selection
		 *            ISelection - selected items
		 */
		public void setSelection(ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				List<String> rts = new ArrayList<String>();
				for (Object jarfile : ((IStructuredSelection) selection).toArray()) {
					rts.add((String) jarfile);
				}
				jars = rts.toArray(new String[] {});
			} else {
				jars = new String[0];
			}
			updateEnablement();
		}

		protected abstract void updateEnablement();
	}

	/**
	 * Action that invokes New JbossWS Runtime Dialog
	 * 
	 */
	public class AddAction extends BaseAction {

		static final String ACTION_NAME = "&Add";

		/**
		 * Constructor create Add action with default name
		 */
		public AddAction() {
			super(ACTION_NAME);
			// This action is always available
			setEnabled(true);
		}

		/**
		 * Do nothing, because Add action should be always available
		 */
		@Override
		protected void updateEnablement() {
			// Add button is always available
		}

		/**
		 * Invoke New JbossWS Runtime Dialog
		 * 
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			FileDialog dialog = new FileDialog(Display.getCurrent()
					.getActiveShell(), SWT.MULTI);
			dialog.setFilterExtensions(new String[] { "*.jar;*.zip" });
			String fileName = dialog.open();
			String[] fileNames = dialog.getFileNames();
			if (fileName != null) {
				File filePath = new File(fileName);
				filePath = filePath.getParentFile();
				for (int i = 0; i < fileNames.length; i++) {
					IPath path = new Path(filePath.getAbsolutePath())
							.append(fileNames[i]);
					if (!tempJbws.getLibraries().contains(path.toOSString())) {
						tempJbws.getLibraries().add(path.toOSString());
					}
				}

				listView.refresh();
				setValue(null);
			}
		}
	}
 

	/**
	 * Action deletes all selected JbossWS Runtimes. A warning message is shown
	 * for used JbossWS Runtimes
	 * 
	 */
	public class RemoveAction extends BaseAction {

		static final String ACTION_NAME = "&Remove";

		/**
		 * Create DeleteAction action with default name
		 */
		public RemoveAction() {
			super(ACTION_NAME);
		}

		@Override
		protected void updateEnablement() {
			setEnabled(jars.length > 0);
		}

		/**
		 * Remove all selected JbossWS Runtimes one by one
		 * 
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			for (String jar : jars) {
				tempJbws.getLibraries().remove(jar);
			}
			listView.refresh();
			// just try to fire property change listener
			setValue(null);
		}

		 
	}
}