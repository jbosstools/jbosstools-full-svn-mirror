package org.jboss.ide.eclipse.jsr88deployer.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jsr88deployer.core.JSR88CorePlugin;
import org.jboss.ide.eclipse.jsr88deployer.core.PreferenceStoreUtil;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.DeploymentJarUtils;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.StringProperties;
import org.jboss.ide.eclipse.jsr88deployer.ui.composites.FileAssocDescriptionComposite;
import org.jboss.ide.eclipse.jsr88deployer.ui.dialogs.DeployAssocDialog;
import org.jboss.ide.eclipse.jsr88deployer.ui.dialogs.DeployAssocDialog.DeployAssocPojo;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class Deploy88PreferencePage
	extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String JAR_PREFIX = PreferenceStoreUtil.JAR_PREFIX;
	public static final String TARGET_PREFIX = PreferenceStoreUtil.TARGET_PREFIX;
	public static final String CONFIG_PREFIX = PreferenceStoreUtil.CONFIG_PREFIX;
	public static final String ASSOC_PREFIX = PreferenceStoreUtil.ASSOC_PREFIX;
	public static final String FILE_ASSOC_PREFIX = PreferenceStoreUtil.FILE_ASSOC_PREFIX;
	
	
	private DeployPreferenceBlock jarComposite, targetComposite;
	private ConfigurationPreferenceBlock configurationComposite;
	private AssociationPreferenceBlock associationsBlock;
	private FileAssociationPreferenceBlock fileAssocBlock;
	private FileAssocDescriptionComposite fileAssocDescription;
	
	public Deploy88PreferencePage() {
		setPreferenceStore(JSR88CorePlugin.getDefault().getPreferenceStore());
		setDescription("Deployment Preferences for JSR-88 deployment");
	}

	public void init(IWorkbench workbench) {
	}


	protected Control createContents(Composite parent) {
		
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		
		TabItem pieces = new TabItem(folder, SWT.NONE);
		pieces.setText("Pieces");
		Composite piecesComposite = createPiecesComposite(folder);
		pieces.setControl(piecesComposite);

		TabItem associations = new TabItem(folder, SWT.NONE);
		associations.setText("Associations");
		Composite associationsComposite = createAssociationComposite(folder);
		associations.setControl(associationsComposite);
		

		loadPreferences();
		return folder;
	}
	
	private Composite createPiecesComposite(TabFolder folder) {
		Composite main = new Composite(folder, SWT.NONE);
		
		main.setLayout(new FormLayout());
		
		// set where we want the jar composite to go
		jarComposite = new JarPreferenceBlock(main, SWT.NONE, 
				"Vendor Jar Files", Deploy88PreferencePage.JAR_PREFIX);
		FormData jarCompositeData = new FormData();
		jarCompositeData.top = new FormAttachment(0,5);
		jarCompositeData.left = new FormAttachment(0,5);
		jarCompositeData.right = new FormAttachment(100,-5);
		jarComposite.setLayoutData(jarCompositeData);

		configurationComposite = new ConfigurationPreferenceBlock(main, SWT.NONE, 
				"Vendor-specific Configuration Files", Deploy88PreferencePage.CONFIG_PREFIX);
		FormData configurationCompositeData = new FormData();
		configurationCompositeData.top = new FormAttachment(jarComposite,5);
		configurationCompositeData.left = new FormAttachment(0,5);
		configurationCompositeData.right = new FormAttachment(100,-5);
		configurationComposite.setLayoutData(configurationCompositeData);

		
		// set where we want the targets composite to go
		targetComposite = new TargetPreferenceBlock(main, SWT.NONE, "Targets", 
				Deploy88PreferencePage.TARGET_PREFIX);
		FormData targetCompositeData = new FormData();
		targetCompositeData.top = new FormAttachment(configurationComposite, 5);
		targetCompositeData.left = new FormAttachment(0,5);
		targetCompositeData.right = new FormAttachment(100,-5);
		targetComposite.setLayoutData(targetCompositeData);
		
		
		
		// Miscelaneous selection listener
		jarComposite.getViewer().getTable().addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = jarComposite.getViewer().getTable().getSelection();
				if( items.length == 0 ) return;
				
				// The configuration box updates whenever a jar is selected.
				String jarUri = items[0].getData().toString();
				configurationComposite.setVendorJarSelected(jarUri);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			} 
			
		} );
		jarComposite.addSelectRemoveListener(configurationComposite);
		
		
		return main;
	}
	
	private Composite createAssociationComposite(TabFolder folder) {
		Composite main = new Composite(folder, SWT.NONE);
		main.setLayout(new FormLayout());
		
		associationsBlock = new AssociationPreferenceBlock(main, SWT.NONE, 
				"Associations", Deploy88PreferencePage.ASSOC_PREFIX);

		FormData associations = new FormData();
		associations.top = new FormAttachment(0,5);
		associations.left = new FormAttachment(0,5);
		associations.right = new FormAttachment(100,-5);
		associationsBlock.setLayoutData(associations);

		
		fileAssocDescription = new FileAssocDescriptionComposite(main, SWT.NONE, true);
		FormData fileAssocDescData = new FormData();
		fileAssocDescData.left = new FormAttachment(0,5);
		fileAssocDescData.right = new FormAttachment(100,-5);
		fileAssocDescData.top = new FormAttachment(associationsBlock,5);
		fileAssocDescription.setLayoutData(fileAssocDescData);

		
		fileAssocBlock = new FileAssociationPreferenceBlock(main, SWT.NONE, 
				"Files Mapped to Associations", Deploy88PreferencePage.FILE_ASSOC_PREFIX);
		FormData fileAssoc = new FormData();
		fileAssoc.top = new FormAttachment(fileAssocDescription,5);
		fileAssoc.left = new FormAttachment(0,5);
		fileAssoc.right = new FormAttachment(100,-5);
		fileAssocBlock.setLayoutData(fileAssoc);
		
		

		associationsBlock.getViewer().getTable().addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				updateFileAssocComposite();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			} 
			
		});
		

		
		
		return main;
	}

	
	
	private void loadPreferences() {
		Object[] jarObjects = PreferenceStoreUtil.
			loadIncrementalPreferences(Deploy88PreferencePage.JAR_PREFIX);
		jarComposite.getViewer().add(jarObjects);

		Object[] targetObjects = PreferenceStoreUtil.
			loadIncrementalPreferences(Deploy88PreferencePage.TARGET_PREFIX);
		for( int i = 0; i < targetObjects.length; i++ ) {
			if( targetObjects[i] instanceof String ) {
				String tmp = (String)targetObjects[i];
				try {
					StringProperties prop = new StringProperties(tmp);
					targetComposite.getViewer().add(prop);
				} catch( Exception e ) {
				}
			}
		}
		
		
		associationsBlock.loadPreferences();
		fileAssocBlock.loadPreferences();
	}

	
	
	
	public boolean performOk() {
		clearPreferences();
		savePreferences();
		return true;
	}
	
	private void clearPreferences() {
		PreferenceStoreUtil.clearIncrementalPreferences(Deploy88PreferencePage.JAR_PREFIX);
		PreferenceStoreUtil.clearIncrementalPreferences(Deploy88PreferencePage.TARGET_PREFIX);
		configurationComposite.clearPreferences();
		PreferenceStoreUtil.clearIncrementalPreferences(Deploy88PreferencePage.ASSOC_PREFIX);
		PreferenceStoreUtil.clearIncrementalPreferences(Deploy88PreferencePage.FILE_ASSOC_PREFIX);
	}
	
	private void savePreferences() {
		jarComposite.savePreferences();
		targetComposite.savePreferences();
		configurationComposite.savePreferences(jarComposite);
		associationsBlock.savePreferences();
		fileAssocBlock.savePreferences();

	}
	
	private void updateFileAssocComposite() {
		int index = associationsBlock.getViewer().getTable().getSelectionIndex();
		if( index != -1 ) {
			TableItem item = associationsBlock.getViewer().getTable().getItem(index);
			if( item.getData() instanceof DeployAssocPojo ) {
				DeployAssocPojo pojo = (DeployAssocPojo)item.getData();
				fileAssocDescription.setVals(new StringProperties(pojo.toString()));
			}
		}
	}

	public class DeployPreferencesLabelProvider implements ITableLabelProvider {
		
		private String type;
		
		public DeployPreferencesLabelProvider(String type) {
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
			
			if( this.type.equals(Deploy88PreferencePage.TARGET_PREFIX)) {
				if( element instanceof StringProperties ) {
					return ((StringProperties)element).getPiece(TargetPreferenceBlock.NAME);
				}
				return "ERRORED! ___ ";
			} else if( this.type.equals(Deploy88PreferencePage.ASSOC_PREFIX )) {
				DeployAssocPojo pojo = null;
				if( element instanceof DeployAssocPojo ) {
					pojo = (DeployAssocPojo)element;
				} else {
					pojo = new DeployAssocDialog.DeployAssocPojo((String)element);
				}
				return pojo.getName();
			} else if( this.type.equals(Deploy88PreferencePage.FILE_ASSOC_PREFIX)){
				if( element instanceof StringProperties ) {
					return ((StringProperties)element).getPiece(TargetPreferenceBlock.NAME);
				}
				return "ERRORED! ___ ";
			} else {
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
	
	private interface ISelectRemoveListener {
		public void fireSelected(DeployPreferenceBlock block, int index);
		public void fireRemoved(DeployPreferenceBlock block, int index);
	}
	
	public class DeployPreferenceBlock extends Composite {
		
		protected TableViewer myViewer;
		protected Label mainLabel;
		protected Button addButton, removeButton;
		protected String type;
		private ArrayList selectRemoveListeners;


		public DeployPreferenceBlock(Composite parent, int style, 
				String labelText, String type) {
			super(parent, style);
			setLayout(new FormLayout());
			this.type = type;
			this.selectRemoveListeners = new ArrayList();
			
			
			mainLabel = new Label(this, SWT.NONE);
			mainLabel.setText(labelText);
			FormData mainLabelData = new FormData();
			mainLabelData.left = new FormAttachment(0,0);
			mainLabelData.top = new FormAttachment(0,0);
			mainLabelData.right = new FormAttachment(100,-5);
			mainLabel.setLayoutData(mainLabelData);
			
			// Create the buttons
			addButton = new Button(this, SWT.NONE);
			removeButton = new Button(this, SWT.NONE);
			
			addButton.setText("Add");
			removeButton.setText("Remove");

			
			// Set their layout data
			FormData addButtonData = new FormData();
			addButtonData.left = new FormAttachment(100, -150);
			addButtonData.right = new FormAttachment(100,-5);
			addButtonData.top = new FormAttachment(mainLabel, 5);
			addButton.setLayoutData(addButtonData);
			

			FormData removeButtonData = new FormData();
			removeButtonData.left = new FormAttachment(100, -150);
			removeButtonData.right = new FormAttachment(100,-5);
			removeButtonData.top = new FormAttachment(addButton, 5);
			removeButton.setLayoutData(removeButtonData);

			
			myViewer = new TableViewer(this, SWT.BORDER);
			FormData myViewerData = new FormData();
			myViewerData.left = new FormAttachment(0,0);
			myViewerData.right = new FormAttachment(addButton,-10);
			myViewerData.top = new FormAttachment(mainLabel, 5);
			myViewerData.bottom = new FormAttachment(0,90);
			
			myViewer.getControl().setLayoutData(myViewerData);
			myViewer.setLabelProvider(new DeployPreferencesLabelProvider(this.type));
			
			

			// subclassed methods
			createRemoveButtonListener();
			createAddButtonListener();


		}
		
		protected void createRemoveButtonListener() {
			removeButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selected = (IStructuredSelection)myViewer.getSelection();
					int index = myViewer.getTable().getSelectionIndex();
					if( selected.getFirstElement() == null ) return;
					
					fireRemoved();
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
					
					fireSelected();
					myViewer.getTable().setFocus();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );

		}

		protected void fireSelected() {
			int index = myViewer.getTable().getSelectionIndex();
			if( index == -1 ) return;
			for( Iterator i = selectRemoveListeners.iterator(); i.hasNext(); ) {
				((ISelectRemoveListener)i.next()).fireSelected(this, index);
			}
		}
		
		protected void fireRemoved() {
			int index = myViewer.getTable().getSelectionIndex();
			for( Iterator i = selectRemoveListeners.iterator(); i.hasNext(); ) {
				((ISelectRemoveListener)i.next()).fireRemoved(this, index);
			}
		}
		
		protected void createAddButtonListener() {
			// Subclasses override me
		}

		public TableViewer getViewer() {
			return myViewer;
		}
		
		public String getType() {
			return this.type;
		}
		
		public void addSelectRemoveListener(ISelectRemoveListener listener) {
			selectRemoveListeners.add(listener);
		}
		
		public void savePreferences() {
			PreferenceStoreUtil.saveIncrementalPreferences(
					this.type, getViewer().getTable().getItems());
		}
	}
	
	public class JarPreferenceBlock extends DeployPreferenceBlock {

		public JarPreferenceBlock(Composite parent, int style, String labelText, String type) {
			super(parent, style, labelText, type);
		}
		
		protected void createAddButtonListener() {
			addButton.addSelectionListener( new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					boolean done = false;
					while( !done ) {
						FileDialog dialog = new FileDialog(new Shell());
						dialog.setFilterExtensions(new String[]{ "*.jar"});
						String response = dialog.open();
						if( response == null ) {
							done = true;
						} else {
							// is it valid?
							boolean valid = 
								DeploymentJarUtils.isValidDeploymentJar(response);
							if( valid ) {
								
								// Is it already in the list?
								TableItem[] items = myViewer.getTable().getItems();
								boolean contains = false;
								for( int i = 0; i < items.length && !contains; i++ ) {
									if( items[i].getText().equals(response)) 
										contains = true;
								}
								
								// If it's not already there, add it
								if( !contains ) {
									myViewer.add(response);
								}
								
								// Either way, we're done
								done = true;
							} else {
								// throw up an error message
						          MessageBox mb = new MessageBox(dialog.getParent(), SWT.ICON_WARNING
							              | SWT.YES | SWT.NO);

						          // We really should read this string from a
						          // resource bundle
						          mb.setMessage(response + " is not a valid vendor deployment .jar. Would you like to select a different one?");
						          if( mb.open() == SWT.NO ) {
						        	  done = true;
						          }
							}
						}
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			});
			// Pop up a select file mechanism
		}
	}
	
	public class TargetPreferenceBlock extends DeployPreferenceBlock {

		private Button editButton;
		public static final int NAME = PreferenceStoreUtil.TARGET_NAME;
		public static final int URI  = PreferenceStoreUtil.TARGET_URI;
		public static final int USER = PreferenceStoreUtil.TARGET_USER;
		public static final int PASS = PreferenceStoreUtil.TARGET_PASS;
		

		public TargetPreferenceBlock(Composite parent, int style, String labelText, String type) {
			super(parent, style, labelText, type);
			
			createTargetSpecificWidgets();
		}
		
		protected void createAddButtonListener() {
			addButton.addSelectionListener( new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					Deploy88TargetDialog dialog = new Deploy88TargetDialog(new Shell());
					int response = dialog.open();
					
					if( response == IDialogConstants.OK_ID ) {
						StringProperties properties = dialog.getProperties();
						if( properties.getPiece(NAME) != "") {
							myViewer.add(properties);
						}
						
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			});
		}

		private void createTargetSpecificWidgets() {
			editButton = new Button(this, SWT.NONE);
			editButton.setText("Edit");
			FormData editData = new FormData();
			editData.left = new FormAttachment(100, -150);
			editData.right = new FormAttachment(100,-5);
			editData.top = new FormAttachment(removeButton, 5);
			editButton.setLayoutData(editData);
			
			
			
			editButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					int selectedIndex = getViewer().getTable().getSelectionIndex();
					if( selectedIndex != -1 ) {
						TableItem item = getViewer().getTable().getItem(selectedIndex);
						
						if( item.getData() instanceof StringProperties ) {
							StringProperties sp = (StringProperties)item.getData();
							Deploy88TargetDialog dialog = new Deploy88TargetDialog(new Shell(), sp);

							int response = dialog.open();
							
							if( response == IDialogConstants.OK_ID ) {
								StringProperties retProp = dialog.getProperties();
								sp.put(TargetPreferenceBlock.NAME, retProp.getPiece(TargetPreferenceBlock.NAME));
								sp.put(TargetPreferenceBlock.URI, retProp.getPiece(TargetPreferenceBlock.URI));
								sp.put(TargetPreferenceBlock.USER, retProp.getPiece(TargetPreferenceBlock.USER));
								sp.put(TargetPreferenceBlock.PASS, retProp.getPiece(TargetPreferenceBlock.PASS));
							}
						}
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} } 
			);
			
		}
	}
	
	public class ConfigurationPreferenceBlock 
		extends DeployPreferenceBlock implements ISelectRemoveListener {

		private String mainBaseLabel;
		private HashMap loadedConfigPreferences;
		private String currentKey;
		 
		public ConfigurationPreferenceBlock(Composite parent, int style, String labelText, String type) {
			super(parent, style, labelText, type);
			this.mainBaseLabel = labelText;
			this.loadedConfigPreferences = new HashMap();
			this.currentKey = null;
		}
		
		
		public void setVendorJarSelected(String vendorJar) {
			this.currentKey = Deploy88PreferencePage.CONFIG_PREFIX + vendorJar;
			
			mainLabel.setText(mainBaseLabel + " (" + vendorJar + ")");
			
			String key = Deploy88PreferencePage.CONFIG_PREFIX + vendorJar;
			if( loadedConfigPreferences.get(key) == null ) {
				// first try to load, if not
				Object[] configObjects = PreferenceStoreUtil.
					loadIncrementalPreferences(key);
				if( configObjects.length == 0 ) {
					loadedConfigPreferences.put(key, new ArrayList());
				} else {
					loadedConfigPreferences.put(key, new ArrayList(Arrays.asList(configObjects)));
				}
			}
			
			ArrayList vals = (ArrayList)loadedConfigPreferences.get(key);
			getViewer().getTable().removeAll();
			getViewer().add(vals.toArray());
		}
		
		// Will both be called from the preference page
		public void clearPreferences() {
			Iterator keySetIterator =loadedConfigPreferences.keySet().iterator();
			while(keySetIterator.hasNext()) {
				String key = (String)keySetIterator.next();
				PreferenceStoreUtil.clearIncrementalPreferences(key);
			}
		}
		
		public void savePreferences(DeployPreferenceBlock jarBlock) {
			// Can only save based on the jar composite
			if( jarBlock.getType().equals(Deploy88PreferencePage.JAR_PREFIX)) {
				Iterator keySetIterator =loadedConfigPreferences.keySet().iterator();
				while(keySetIterator.hasNext()) {
					String key = (String)keySetIterator.next();
					Object[] objects = ((ArrayList)loadedConfigPreferences.get(key)).toArray();
					PreferenceStoreUtil.saveIncrementalPreferences(key, objects);
				}
			}
			
		}
		
		protected void createRemoveButtonListener() {
			removeButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selected = (IStructuredSelection)myViewer.getSelection();
					int index = myViewer.getTable().getSelectionIndex();
					fireRemoved();
					

					// Remove it from our underlying list of loaded configs
					Object first = selected.getFirstElement();
					Object list = loadedConfigPreferences.get(currentKey);
					if( list != null ) {
						if( list instanceof ArrayList ) {
							ArrayList aList = (ArrayList)list;
							aList.remove(first);
						}
					}
					
					// remove from the viewer
					myViewer.remove(first);
					
					
					// Next select some nearby element and fire it as selected.
					if( index == 0 ) {
						if( myViewer.getTable().getItemCount() > 0 ) {
							myViewer.getTable().setSelection(index);
						}
					} else if( index >= myViewer.getTable().getItemCount() ) {
						myViewer.getTable().setSelection(myViewer.getTable().getItemCount()-1);
					} else {
						myViewer.getTable().setSelection(index);
					}
					
					fireSelected();
					myViewer.getTable().setFocus();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );

		}

		protected void createAddButtonListener() {
			addButton.addSelectionListener( new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					// Throw up a dialog and wait for the response
					FileDialog dialog = new FileDialog(new Shell());
					dialog.setFilterExtensions(new String[]{ "*.xml"});
					if( currentKey != null ) { 
						String response = dialog.open();
						if( response != null ) {
							
							// Get a local copy of the preferences from our hashmap
							Object alreadyLoaded = loadedConfigPreferences.get(currentKey);
							ArrayList list;
							if( alreadyLoaded != null ) {
								list = ((ArrayList)alreadyLoaded);
							} else {
								list = new ArrayList();
								loadedConfigPreferences.put(currentKey, list);
							}
							
							// Just making sure there are no dups here.
							if( !list.contains(response)) {
								// TODO: verify that this is a vendor jar... via manifest etc.
								myViewer.add(response);
								list.add(response);
							}
						}
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			});
		}


		public void fireSelected(DeployPreferenceBlock block, int index) {
			if( block.getType().equals(Deploy88PreferencePage.JAR_PREFIX)) {
				String text = block.getViewer().getTable().getItem(index)
					.getData().toString();
				setVendorJarSelected(text);
			}
		}


		public void fireRemoved(DeployPreferenceBlock block, int index) {
			String text = block.getViewer().getTable().getItem(index)
				.getData().toString();
			String key = Deploy88PreferencePage.CONFIG_PREFIX + text;
			Object o = loadedConfigPreferences.get(key);
			if( o != null ) {
				((ArrayList)o).clear();
			}
		}
	}
	
	
	public class AssociationPreferenceBlock extends DeployPreferenceBlock {
		public static final int NAME = PreferenceStoreUtil.ASSOCIATION_PREFERENCE_NAME;
		public static final int JAR = PreferenceStoreUtil.ASSOCIATION_PREFERENCE_JAR;
		public static final int TARGET = PreferenceStoreUtil.ASSOCIATION_PREFERENCE_TARGET;
		public static final int CONFIG = PreferenceStoreUtil.ASSOCIATION_PREFERENCE_CONFIG;
		
		private Shell parentShell;
		private Button editButton;

		public AssociationPreferenceBlock(Composite parent, int style, String labelText, String type) {
			super(parent, style, labelText, type);
			parentShell = parent.getShell();
			
			associationPreferenceBlockAdditions();
		}
		
		
		private void associationPreferenceBlockAdditions() {
			
			editButton = new Button(this, SWT.NONE);
			editButton.setText("Edit");
			FormData editButtonData = new FormData();
			editButtonData.left = new FormAttachment(100, -150);
			editButtonData.right = new FormAttachment(100, -5);
			editButtonData.top = new FormAttachment(removeButton, 5);
			editButton.setLayoutData(editButtonData);
			editButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					int selected = getViewer().getTable().getSelectionIndex();
					if( selected != -1 ) {
						TableItem item = associationsBlock.getViewer().getTable().getItem(selected);
						if( item.getData() instanceof DeployAssocPojo ) {
							DeployAssocPojo pojo = (DeployAssocPojo)item.getData();

							DeployAssocDialog dialog = new DeployAssocDialog(parentShell, pojo);
						
							boolean done = false;
							boolean success = false;
							int response;
							DeployAssocPojo returnPojo = null;
							do {
								response = dialog.open();
								if( response == IDialogConstants.CANCEL_ID ) {
									done = true;
									success = false;
								} else if( response == IDialogConstants.OK_ID ) {
									returnPojo = dialog.getPojo();
									if( returnPojo.getName().equals("" )) {
								          MessageBox mb = new MessageBox(parentShell, SWT.ICON_WARNING
									              | SWT.OK);
	
								          mb.setMessage("The association must be named.");
								          mb.setText("Error: Name the association");
								          mb.open();
								          done = false;
									} else {
										success = true;
										done = true;
									}
								}
							} while( !done );
							
	
							
							
							if( success ) {
								// update
								pojo.setName(returnPojo.getName());
								pojo.setTarget(returnPojo.getTarget());
								pojo.setJar(returnPojo.getJar());
								pojo.setConfig(returnPojo.getConfig());
								updateFileAssocComposite();
							}
						}

						
						
						
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				
			} );
		}
		
		
		protected void createAddButtonListener() {
			addButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					DeployAssocDialog dialog = new DeployAssocDialog(parentShell);
					boolean done = false;
					boolean success = false;
					int response;
					DeployAssocPojo pojo = null;
					do {
						response = dialog.open();
						if( response == IDialogConstants.CANCEL_ID ) {
							done = true;
							success = false;
						} else if( response == IDialogConstants.OK_ID ) {
							pojo = dialog.getPojo();
							if( pojo.getName().equals("" )) {
						          MessageBox mb = new MessageBox(parentShell, SWT.ICON_WARNING
							              | SWT.OK);

						          // We really should read this string from a
						          // resource bundle
						          mb.setMessage("The association must be named.");
						          mb.setText("Error: Name the association");
						          mb.open();
						          done = false;
							} else {
								success = true;
								done = true;
							}
						}
					} while( !done );
					

					
					
					if( success ) {
						getViewer().add(pojo);
					}
					
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );
		}
		
		public void loadPreferences() {
			Object[] objects = PreferenceStoreUtil.
			loadIncrementalPreferences(this.type);
			for( int i = 0; i < objects.length; i++ ) {
				if( objects[i] instanceof String ) {
					String tmp = (String)objects[i];
					try {
						DeployAssocPojo pair = new DeployAssocDialog.DeployAssocPojo(tmp);
						getViewer().add(pair);
					} catch( Exception e ) {
					}
				}
			}
		}
	}

	
	
	
	public class FileAssociationPreferenceBlock extends DeployPreferenceBlock {

		public static final int FILENAME_INDEX = 0;
		public static final int ASSOC_INDEX = 1;
		
		private Shell parentShell;
		private Combo assocCombo;
		

		public FileAssociationPreferenceBlock(Composite parent, int style, String labelText, String type) {
			super(parent, style, labelText, type);
			parentShell = parent.getShell();
			
			createAssoc();
			addOtherActionListeners();
		}
		
		protected void createAddButtonListener() {
			addButton.setVisible(false);
			addButton.setEnabled(false);
		}
		
		
		private void createAssoc() {
			// widget
			assocCombo = new Combo(this, SWT.READ_ONLY);
			
			// Layout Data
			FormData assocComboData = new FormData();
			assocComboData.left = new FormAttachment(100, -150);
			assocComboData.right = new FormAttachment(100,-5);
			assocComboData.top = new FormAttachment(mainLabel, 5);
			assocCombo.setLayoutData(assocComboData);
			
		}
		
		public void loadPreferences() {
			Object[] objects = PreferenceStoreUtil.loadIncrementalPreferences(this.type);
			Arrays.sort(objects);
			for( int i = 0; i < objects.length; i++ ) {
				if( objects[i] instanceof String ) {
					String s = (String)objects[i];
					StringProperties sp = new StringProperties(s);
					getViewer().add(sp);
				}
			}
		}
		
		private void addOtherActionListeners() {
			getViewer().getTable().addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					int selectedIndex = getViewer().getTable().getSelectionIndex();
					if( selectedIndex != -1 ) {
						fillComboFromAssociatonBlock();
						setComboFromViewer();
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );
			assocCombo.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					int assocIndex = assocCombo.getSelectionIndex();
					int viewerIndex = getViewer().getTable().getSelectionIndex();
					
					if( assocIndex != -1 && viewerIndex != -1 ) {
						String newAssocVal = assocCombo.getItem(assocIndex);
						
						TableItem item = getViewer().getTable().getItem(viewerIndex);
						StringProperties sp = (StringProperties)item.getData();
						
						
						// TODO USE CONSTANTS
						sp.setPiece(ASSOC_INDEX, newAssocVal);
						item.setText(sp.getPiece(FILENAME_INDEX));
						
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
			});
		}
		
		
		private void fillComboFromAssociatonBlock() {
			assocCombo.removeAll();
			TableItem[] items = associationsBlock.getViewer().getTable().getItems();
			for( int i = 0; i < items.length; i++ ) {
				assocCombo.add(items[i].getText());
			}
		}
		
		private void setComboFromViewer() {
			int index = getViewer().getTable().getSelectionIndex();
			if( index != -1 ) {
				TableItem item = getViewer().getTable().getItem(index);
				StringProperties sp = (StringProperties)item.getData();
				
				// TODO USE CONSTANTS!
				String assocKey = sp.getPiece(1);
				int comboIndex = Arrays.asList(assocCombo.getItems()).indexOf(assocKey);
				if( comboIndex != -1 ) 
					assocCombo.select(comboIndex);
			}
		}
		
	}

//	/**
//	 * Simple class used to hold two values separated by a new line.
//	 */
//	public static class ExplodedString {
//		private String s;
//		
//		public ExplodedString() {
//			s = "";
//		}
//		
//		public ExplodedString(String s) {
//			this.s = s;
//		}
//		
//		public void setString( String s ) {
//			this.s = s;
//		}
//
//		public String toString() {
//			return s;
//		}
//		
//		public String getPiece(int num) {
//			String[] temp = s.split("\n");
//			if( temp.length > num ) {
//				return temp[num];
//			}
//			return "";
//		}
//		
//		public void setPiece( int place, String val ) {
//			String[] temp = s.split("\n");
//			if( place > temp.length ) return;
//			
//			temp[place] = val;
//			
//			this.s = "";
//			for( int i = 0; i < temp.length; i++ ) {
//				this.s += temp[i] + "\n";
//			}
//			
//		}
//	}
//	
//	
	/**
	 * A small local class just used to display controls to form a target
	 */
	public class Deploy88TargetDialog extends Dialog {

		private Text name, uri, user, pass;
		//private ExplodedString explodedString;
		private StringProperties stringProperties;

		
		protected Deploy88TargetDialog(Shell parentShell, StringProperties props) {
			this(parentShell);
			stringProperties.put(TargetPreferenceBlock.NAME, props.getPiece(TargetPreferenceBlock.NAME));
			stringProperties.put(TargetPreferenceBlock.URI, props.getPiece(TargetPreferenceBlock.URI));
			stringProperties.put(TargetPreferenceBlock.USER, props.getPiece(TargetPreferenceBlock.USER));
			stringProperties.put(TargetPreferenceBlock.PASS, props.getPiece(TargetPreferenceBlock.PASS));
			
		}
		protected Deploy88TargetDialog(Shell parentShell) {
			super(parentShell);
			stringProperties = new StringProperties();
		}
		
		public StringProperties getProperties() {
			return stringProperties;
		}
		
		
	    protected Control createDialogArea(Composite parent) {
	        Composite main = new Composite(parent, SWT.NONE);
	        main.setLayout(new FormLayout());
	        
	        Label intro = new Label(main, SWT.NONE);
	        intro.setText("Please fill in the required data");
	        
	        
	        Label lTargetName = new Label(main, SWT.NONE); lTargetName.setText("Target Name");
	        Label lTargetUri = new Label(main, SWT.NONE); lTargetUri.setText("Target URI");
	        Label lUser = new Label(main, SWT.NONE); lUser.setText("Username");
	        Label lPass = new Label(main, SWT.NONE); lPass.setText("Password");
	        
	        
	        name = new Text(main, SWT.SINGLE | SWT.BORDER);
	        uri = new Text(main, SWT.SINGLE | SWT.BORDER);
	        user = new Text(main, SWT.SINGLE | SWT.BORDER );
	        pass = new Text(main, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
	        
	        name.setEditable(true);
	        uri.setEditable(true);
	        user.setEditable(true);
	        pass.setEditable(true);
	        
	        
	        name.setText(stringProperties.getPiece(TargetPreferenceBlock.NAME));
	        uri.setText(stringProperties.getPiece(TargetPreferenceBlock.URI));
	        user.setText(stringProperties.getPiece(TargetPreferenceBlock.USER));
	        pass.setText(stringProperties.getPiece(TargetPreferenceBlock.PASS));
	        
	        
	        
	        
	        FormData introData = new FormData();
	        introData.top = new FormAttachment(0,5);
	        introData.left = new FormAttachment(0,5);
	        introData.right = new FormAttachment(0,500);
	        intro.setLayoutData(introData);
	        
	        
	        FormData targetNameData = new FormData();
	        targetNameData.left = new FormAttachment(0,5);
	        targetNameData.top = new FormAttachment(intro,5);
	        targetNameData.right = new FormAttachment(name, 0);
	        lTargetName.setLayoutData(targetNameData);
	        
	        FormData targetUriData = new FormData();
	        targetUriData.left = new FormAttachment(0,5);
	        targetUriData.top = new FormAttachment(name, 5);
	        targetUriData.right = new FormAttachment(name, 0);
	        lTargetUri.setLayoutData(targetUriData);
	        
	        FormData userNameData = new FormData();
	        userNameData.left = new FormAttachment(0,5);
	        userNameData.top = new FormAttachment(uri, 5);
	        userNameData.right = new FormAttachment(name, 0);
	        lUser.setLayoutData(userNameData);
	        
	        FormData userPassData = new FormData();
	        userPassData.left = new FormAttachment(0,5);
	        userPassData.top = new FormAttachment(user, 5);
	        userPassData.right = new FormAttachment(name, 0);
	        lPass.setLayoutData(userPassData);
	        
	        
	        
	        
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
	        
	        FormData userData = new FormData();
	        userData.right = new FormAttachment(100,-5);
	        userData.top = new FormAttachment(uri, 5);
	        userData.left = new FormAttachment(0,80);
	        user.setLayoutData(userData);
	        
	        FormData passData = new FormData();
	        passData.right = new FormAttachment(100,-5);
	        passData.top = new FormAttachment(user, 5);
	        passData.left = new FormAttachment(0,80);
	        pass.setLayoutData(passData);
	        
	        KeyListener listener = new KeyListener() {
				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
					
					
					stringProperties.put(TargetPreferenceBlock.NAME, name.getText());
					stringProperties.put(TargetPreferenceBlock.URI,  uri.getText());
					stringProperties.put(TargetPreferenceBlock.USER, user.getText());
					stringProperties.put(TargetPreferenceBlock.PASS, pass.getText());
					
				} 	        	
	        };

	        name.addKeyListener(listener);
	        uri.addKeyListener(listener);
	        user.addKeyListener(listener);
	        pass.addKeyListener(listener);
	        
	        //add controls to composite as necessary
	        return main;
	    }
	}
}