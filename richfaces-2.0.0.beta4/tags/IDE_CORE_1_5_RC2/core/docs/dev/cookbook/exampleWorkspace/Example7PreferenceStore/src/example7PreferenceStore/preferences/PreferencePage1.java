package example7PreferenceStore.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import example7PreferenceStore.Example7PreferenceStorePlugin;

public class PreferencePage1 extends PreferencePage implements IWorkbenchPreferencePage {
	
	// We're using these as prefixes for the preference store...
	public static final String JAR_PREFIX = "__JAR_TO_SEARCH__";
	public static final String TARGET_PREFIX = "__TARGET_TO_DEPLOY_TO__";
	
	private PreferenceBlock jarComposite, targetComposite;
	
	public PreferencePage1() {
		setPreferenceStore(Example7PreferenceStorePlugin.getDefault().getPreferenceStore());
		setDescription("Some preference examples");
	}

	public void init(IWorkbench workbench) {
	}


	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new FormLayout());
		
		// set where we want the jar composite to go
		jarComposite = new PreferenceBlock(main, SWT.NONE, 
				"Vendor Jar Files", PreferencePage1.JAR_PREFIX);
		FormData jarCompositeData = new FormData();
		jarCompositeData.top = new FormAttachment(0,5);
		jarCompositeData.left = new FormAttachment(0,5);
		jarCompositeData.right = new FormAttachment(100,-5);
		jarComposite.setLayoutData(jarCompositeData);

		
		// set where we want the targets composite to go
		targetComposite = new PreferenceBlock(main, SWT.NONE, "Targets", 
				PreferencePage1.TARGET_PREFIX);
		FormData targetCompositeData = new FormData();
		targetCompositeData.top = new FormAttachment(jarComposite, 5);
		targetCompositeData.left = new FormAttachment(0,5);
		targetCompositeData.right = new FormAttachment(100,-5);
		targetComposite.setLayoutData(targetCompositeData);
		

		// Fill our widgets with values from the preference store
		loadPreferences();

		return main;
	}
	
	private void loadPreferences() {
		/*
		 * I'm saving the preferences as "__JAR_TO_SEARCH__0", 1,2, etc.
		 */
		
		
		IPreferenceStore store = getPreferenceStore();
		boolean done = false;
		int i = 0;
		while( !done ) {
			String tmp = store.getString(PreferencePage1.JAR_PREFIX + i);
			if( tmp == null || tmp.equals("") ) { 
				done = true;
			} else {
				jarComposite.getViewer().add(tmp);
			}
			i++;
		}
		
		done = false;
		i = 0;
		
		/*
		 * Same for target
		 */
		while( !done ) {
			String tmp = store.getString(PreferencePage1.TARGET_PREFIX + i);
			if( tmp == null || tmp.equals("") ) { 
				done = true;
			} else {
				try {
					NameUriPair pair = new NameUriPair(tmp);
					targetComposite.getViewer().add(pair);
				} catch( Exception e ) {
				}
			}
			i++;
		}
	}

	
	/**
	 * On an ok, we want to delete all JAR_PREFIX and TARGET_PREFIX
	 * preferences and then save whichever are active in the gui.
	 * (We will not delete all saved preferences, as 
	 *  any part of the plug-in can declare and store preferences.) 
	 */
	public boolean performOk() {
		clearPreferences();
		savePreferences();
		return true;
	}
	
	/**
	 * Just clear until we find nulls.
	 */
	private void clearPreferences() {
		IPreferenceStore store = getPreferenceStore();
		boolean done = false;
		int i = 0;
		while( !done ) {
			String tmp = store.getString(PreferencePage1.JAR_PREFIX + i);
			if( tmp == null || tmp.equals("") ) { 
				done = true;
			} else {
				store.setValue(PreferencePage1.JAR_PREFIX + i, "");
			}
			i++;
		}

		done = false;
		i = 0;
		while( !done ) {
			String tmp = store.getString(PreferencePage1.TARGET_PREFIX + i);
			if( tmp == null || tmp.equals("") ) { 
				done = true;
			} else {
				store.setValue(PreferencePage1.TARGET_PREFIX + i, "");
			}
			i++;
		}
	}
	
	/**
	 * Save directly from the gui tables
	 *
	 */
	private void savePreferences() {
		IPreferenceStore store = getPreferenceStore();
		TableViewer jarViewer = jarComposite.getViewer();
		TableViewer targetViewer = targetComposite.getViewer();
		
		/*
		 * Our jar viewer is just using plain text, so we can
		 * just get the items
		 */
		TableItem[] jarItems = jarViewer.getTable().getItems();
		for( int i = 0; i < jarItems.length; i++ ) {
			String temp = jarItems[i].getText();
			store.setValue(PreferencePage1.JAR_PREFIX + i, temp);
		}
		
		/*
		 * WHen we insert the data into our targetViewer, we're inserting
		 * NameUriPairs, so we have to getData() instead of just the text.
		 * GetText gets the text from the labelProvider, which si not what we want.
		 */
		TableItem[] targetItems = targetViewer.getTable().getItems();
		for( int i = 0; i < targetItems.length; i++ ) {
			String temp = targetItems[i].getData().toString();
			store.setValue(PreferencePage1.TARGET_PREFIX + i, temp);
		}
		
		
		
	}
	
	
	
	/**
	 * Here's our label provider
	 * @author bwar
	 *
	 */
	private class Example7PreferencesLabelProvider implements ITableLabelProvider {
		
		private String type;
		
		public Example7PreferencesLabelProvider(String type) {
			this.type = type;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			if(columnIndex == 0 ) {
				String image = ISharedImages.IMG_OBJ_FILE;
				
				return PlatformUI.getWorkbench().
						getSharedImages().getImage(image);
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if( columnIndex != 0 ) return null;
			
			if( this.type.equals(PreferencePage1.TARGET_PREFIX)) {
				// Here we may have either a NameUriPair or a String
				// representation of the NameUriPair

				NameUriPair pair = null;
				if( element instanceof NameUriPair ) {
					pair = (NameUriPair)element;
				} else if( element instanceof String ) {
					pair = new NameUriPair((String)element);
				}
				
				return pair.getName() + " - [" + pair.getUri() + "]";
			} else {
				// For the Jar's, we're just using plain text, so just get it.
				return 	element.toString();

			}
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		public void removeListener(ILabelProviderListener listener) {
		}
		
	}
	
	private class PreferenceBlock extends Composite {
		
		private TableViewer myViewer;
		private Label mainLabel;
		private Button addButton, removeButton, moveUpButton, moveDownButton;
		private String type;


		public PreferenceBlock(Composite parent, int style, 
				String labelText, String type) {
			super(parent, style);
			setLayout(new FormLayout());
			this.type = type;
			
			
			Label mainLabel = new Label(this, SWT.NONE);
			mainLabel.setText(labelText);
			FormData jarListLabelData = new FormData();
			jarListLabelData.left = new FormAttachment(0,0);
			jarListLabelData.top = new FormAttachment(0,0);
			mainLabel.setLayoutData(jarListLabelData);
			
			myViewer = new TableViewer(this, SWT.BORDER);
			FormData myViewerData = new FormData();
			myViewerData.left = new FormAttachment(0,0);
			myViewerData.right = new FormAttachment(0,250);
			myViewerData.top = new FormAttachment(mainLabel, 5);
			myViewerData.bottom = new FormAttachment(0,150);
			
			myViewer.getControl().setLayoutData(myViewerData);
			myViewer.setLabelProvider(new Example7PreferencesLabelProvider(this.type));
			myViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					Object o = ((IStructuredSelection)myViewer.getSelection()).getFirstElement();
					int size = myViewer.getTable().getItemCount();
					if( o == myViewer.getElementAt(0)) {
						moveUpButton.setEnabled(false);
						moveDownButton.setEnabled(true);
					} else if( o == myViewer.getElementAt(size-1)) {
						moveDownButton.setEnabled(false);
						moveUpButton.setEnabled(true);
					} else {
						moveDownButton.setEnabled(true);
						moveUpButton.setEnabled(true);
					}
				} 
			} );

			
			// Create the buttons
			addButton = new Button(this, SWT.NONE);
			removeButton = new Button(this, SWT.NONE);
			moveUpButton = new Button(this, SWT.NONE);
			moveDownButton = new Button(this, SWT.NONE);
			
			addButton.setText("Add");
			removeButton.setText("Remove");
			moveUpButton.setText("Move Up");
			moveDownButton.setText("Move Down");
			
			
			// Set their layout data
			FormData addButtonData = new FormData();
			addButtonData.left = new FormAttachment(myViewer.getControl(), 5);
			addButtonData.right = new FormAttachment(100,-5);
			addButtonData.top = new FormAttachment(mainLabel, 5);
			addButton.setLayoutData(addButtonData);
			

			FormData removeButtonData = new FormData();
			removeButtonData.left = new FormAttachment(myViewer.getControl(), 5);
			removeButtonData.right = new FormAttachment(100,-5);
			removeButtonData.top = new FormAttachment(addButton, 5);
			removeButton.setLayoutData(removeButtonData);
			
			FormData moveUpButtonData = new FormData();
			moveUpButtonData.left = new FormAttachment(myViewer.getControl(), 5);
			moveUpButtonData.right = new FormAttachment(100,-5);
			moveUpButtonData.top = new FormAttachment(removeButton, 5);
			moveUpButton.setLayoutData(moveUpButtonData);

			FormData moveDownButtonData = new FormData();
			moveDownButtonData.left = new FormAttachment(myViewer.getControl(), 5);
			moveDownButtonData.right = new FormAttachment(100,-5);
			moveDownButtonData.top = new FormAttachment(moveUpButton, 5);
			moveDownButton.setLayoutData(moveDownButtonData);

			// add their actions
			removeButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selected = (IStructuredSelection)myViewer.getSelection();
					int index = myViewer.getTable().getSelectionIndex();
					myViewer.remove(selected.getFirstElement());
					
					if( index == 0 ) {
						if( myViewer.getTable().getItemCount() > 0 ) {
							myViewer.getTable().setSelection(index);
						}
					} else if( index >= myViewer.getTable().getItemCount() ) {
						myViewer.getTable().setSelection(myViewer.getTable().getItemCount()-1);
					} else {
						myViewer.getTable().setSelection(index);
					}
					
					myViewer.getTable().setFocus();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );
			
			moveUpButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selected = (IStructuredSelection)myViewer.getSelection();
					Object obj = selected.getFirstElement();
					int index = myViewer.getTable().getSelectionIndex();
					myViewer.remove(myViewer.getElementAt(index));
					myViewer.insert(obj, index-1);
					myViewer.getTable().setSelection(index-1);

				
					int size = myViewer.getTable().getItemCount();
					if( obj == myViewer.getElementAt(0)) {
						moveUpButton.setEnabled(false);
						moveDownButton.setEnabled(true);
					} else if( obj == myViewer.getElementAt(size-1)) {
						moveDownButton.setEnabled(false);
						moveUpButton.setEnabled(true);
					} else {
						moveDownButton.setEnabled(true);
						moveUpButton.setEnabled(true);
					}
					myViewer.getTable().setFocus();

				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}				
			});

			moveDownButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selected = (IStructuredSelection)myViewer.getSelection();
					Object obj = selected.getFirstElement();
					int index = myViewer.getTable().getSelectionIndex();
					myViewer.remove(myViewer.getElementAt(index));
					myViewer.insert(obj, index+1);
					myViewer.getTable().setSelection(index+1);
					
					int size = myViewer.getTable().getItemCount();
					if( obj == myViewer.getElementAt(0)) {
						moveUpButton.setEnabled(false);
						moveDownButton.setEnabled(true);
					} else if( obj == myViewer.getElementAt(size-1)) {
						moveDownButton.setEnabled(false);
						moveUpButton.setEnabled(true);
					} else {
						moveDownButton.setEnabled(true);
						moveUpButton.setEnabled(true);
					}
					myViewer.getTable().setFocus();


				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}				
			});
			
			
			if( this.type.equals(PreferencePage1.JAR_PREFIX)) {
				addButton.addSelectionListener(createAddJarListener());			
			} else if( this.type.equals(PreferencePage1.TARGET_PREFIX)) {
				addButton.addSelectionListener(createAddTargetListener());
			}

		}
		
		private SelectionListener createAddJarListener() {
			return new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					FileDialog dialog = new FileDialog(new Shell());
					//dialog.setFilterExtensions(new String[]{ "jar"});
					String response = dialog.open();
					if( response != null ) {
						// TODO: verify that this is a vendor jar... via manifest etc.
						myViewer.add(response);
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			};
			// Pop up a select file mechanism
		}
		
		private SelectionListener createAddTargetListener() {
			return new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					TargetDialog dialog = new TargetDialog(new Shell());
					System.out.println("opening");
					int response = dialog.open();
					System.out.println("got a response: " + response);
					if( response == IDialogConstants.OK_ID ) {
						myViewer.add(dialog.getPair().toString());
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			};
		}


		public TableViewer getViewer() {
			return myViewer;
		}
		
		
	}
	
	
	public static class NameUriPair {
		private String name;
		private String uri;
		
		public NameUriPair() {
			
		}
		
		public NameUriPair(String s) {
			setName(s.substring(0, s.indexOf('\n')));
			setUri(s.substring(s.indexOf('\n')+1));
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public String toString() {
			return this.name + "\n" + this.uri;
		}
	}
	
	
	
	private class TargetDialog extends Dialog {

		private Text name, uri;
		private NameUriPair myPair;

		/**
		 * @param parentShell
		 */
		protected TargetDialog(Shell parentShell) {
			super(parentShell);
			myPair = new NameUriPair();
		}
		
		public NameUriPair getPair() {
			return myPair;
		}
		
	    protected Control createDialogArea(Composite parent) {
	        Composite main = new Composite(parent, SWT.NONE);
	        main.setLayout(new FormLayout());
	        
	        Label intro = new Label(main, SWT.NONE);
	        intro.setText("Please fill in the required data");
	        
	        
	        Label l1 = new Label(main, SWT.NONE); l1.setText("Target Name");
	        Label l2 = new Label(main, SWT.NONE); l2.setText("Target URI");
	        
	        name = new Text(main, SWT.SINGLE | SWT.BORDER);
	        uri = new Text(main, SWT.SINGLE | SWT.BORDER);
	        name.setEditable(true);
	        uri.setEditable(true);
	        
	        FormData introData = new FormData();
	        introData.top = new FormAttachment(0,5);
	        introData.left = new FormAttachment(0,5);
	        introData.right = new FormAttachment(0,500);
	        intro.setLayoutData(introData);
	        
	        FormData l1Data = new FormData();
	        l1Data.left = new FormAttachment(0,5);
	        l1Data.top = new FormAttachment(intro,5);
	        l1Data.right = new FormAttachment(name, 0);
	        l1.setLayoutData(l1Data);
	        
	        FormData l2Data = new FormData();
	        l2Data.left = new FormAttachment(0,5);
	        l2Data.top = new FormAttachment(name, 5);
	        l2Data.right = new FormAttachment(name, 0);
	        l2.setLayoutData(l2Data);
	        
	        
	        FormData nameData = new FormData();
	        nameData.right = new FormAttachment(100,-5);
	        nameData.top = new FormAttachment(intro,5);
	        nameData.left = new FormAttachment(0, 80);
	        name.setLayoutData(nameData);
	        
	        FormData uriData = new FormData();
	        uriData.right = new FormAttachment(100,-5);
	        uriData.top = new FormAttachment(name, 5);
	        uriData.left = new FormAttachment(0,80);
	        uri.setLayoutData(uriData);
	        
	        
	        uri.addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
					myPair.setName(name.getText());
					myPair.setUri(uri.getText());
				} 
	        } );
	        
	        //add controls to composite as necessary
	        return main;
	    }

	}
}
