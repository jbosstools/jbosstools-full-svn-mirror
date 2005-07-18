/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.ui.dialogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.enterprise.deploy.spi.DeploymentConfiguration;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.ide.eclipse.jsr88deployer.core.PreferenceStoreUtil;
import org.jboss.ide.eclipse.jsr88deployer.core.model.AbstractDeployable;
import org.jboss.ide.eclipse.jsr88deployer.core.model.DDBeanImpl;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.CBeanXpaths;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.DeploymentJarUtils;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.ModelUtils;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.StringProperties;
import org.jboss.ide.eclipse.jsr88deployer.ui.preferences.Deploy88PreferencePage;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateConfigurationDialog extends Dialog {

	private static final String CHANGEME = "REQUIRED FIELD";
	private IResource resource;
	private AbstractDeployable deployable;
	private TreeEditor editor;
	private Tree tree;
	private Combo jarCombo, targetCombo;
	private Button acceptJarButton, saveConfigButton;
	private Label vendorJarLabel, targetLabel;
	private HashMap treesMap, targetsMap;
	
	
	private DeploymentConfiguration currentConfig;
	private ClassLoader vendorClassLoader;
	
	
	public CreateConfigurationDialog(Shell parentShell, IResource resource) {
		super(parentShell);
		this.resource = resource;
		this.deployable = ModelUtils.createDeployableObject(resource);
		this.treesMap = new HashMap();
		this.targetsMap = new HashMap();
	}
	
	protected Control createDialogArea(Composite parent) { 
		getShell().setText("Create Deployment Configuration");
		Composite superGridComposite = (Composite)super.createDialogArea(parent);
			
		Composite main = new Composite(superGridComposite, SWT.NONE);
		
		main.setLayout(new FormLayout());
		
		createWidgets(main);
		fillWidgets();
		addWidgetListeners();
		setLayoutData();
		

		
		return superGridComposite;
	}
	
	private void createWidgets(Composite main) {
		// Combo
		jarCombo = new Combo(main, SWT.READ_ONLY);
		targetCombo = new Combo(main, SWT.READ_ONLY);
		acceptJarButton = new Button(main, SWT.NONE);
		saveConfigButton = new Button(main, SWT.NONE);
		vendorJarLabel = new Label(main, SWT.NONE);
		targetLabel = new Label(main, SWT.NONE);
		

		// Tree stuff
		tree = new Tree(main, SWT.BORDER);
		editor = new TreeEditor(tree);
		//The editor must have the same size as the cell and must
		//not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

	}
	
	
	private void fillWidgets() {
		// Jar Combo
		Object[] jars = PreferenceStoreUtil
		.loadIncrementalPreferences(Deploy88PreferencePage.JAR_PREFIX);
		for( int i = 0; i < jars.length; i++ ) {
			jarCombo.add(jars[i].toString());
		}
		
		Object[] targetObjects = PreferenceStoreUtil.
		loadIncrementalPreferences(Deploy88PreferencePage.TARGET_PREFIX);
		for( int i = 0; i < targetObjects.length; i++ ) {
			if( targetObjects[i] instanceof String ) {
				String tmp = (String)targetObjects[i];
				try {
					StringProperties pair = new StringProperties(tmp);
					
					// TODO USE CONSTANTS
					targetCombo.add(pair.getPiece(0));
					targetsMap.put(pair.getPiece(0), pair);
				} catch( Exception e ) {
				}
			}
		}
		

		acceptJarButton.setText("Generate Missing Xpaths");
		vendorJarLabel.setText("Vendor Deployment Jar: ");
		targetLabel.setText("Target: ");
		saveConfigButton.setText("Save Configuration As...");
	}
	
	private void setLayoutData() {
		FormData vendorLabelData = new FormData();
		vendorLabelData.left = new FormAttachment(0,5);
		vendorLabelData.top = new FormAttachment(0,5);
		vendorJarLabel.setLayoutData(vendorLabelData);

		FormData targetLabelData = new FormData();
		targetLabelData.left = new FormAttachment(0,5);
		targetLabelData.top = new FormAttachment(jarCombo,5);
		targetLabel.setLayoutData(targetLabelData);
		

		
		
		FormData jarComboData = new FormData();
		jarComboData.left = new FormAttachment(vendorJarLabel,5);
		jarComboData.top = new FormAttachment(0,5);
		jarCombo.setLayoutData(jarComboData);

		FormData targetComboData = new FormData();
		targetComboData.left = new FormAttachment(vendorJarLabel,5);
		targetComboData.top = new FormAttachment(jarCombo,5);
		targetCombo.setLayoutData(targetComboData);
		
		FormData jarAcceptButtonData = new FormData();
		jarAcceptButtonData.left = new FormAttachment(targetCombo, 5);
		jarAcceptButtonData.top = new FormAttachment(jarCombo, 5);
		acceptJarButton.setLayoutData(jarAcceptButtonData);
		
		FormData treeData = new FormData();
		treeData.left = new FormAttachment(0, 5);
		treeData.right = new FormAttachment(0,500);
		treeData.top = new FormAttachment(targetCombo,5);
		treeData.bottom = new FormAttachment(0, 200);
		tree.setLayoutData(treeData);
		
		FormData saveButtonData = new FormData();
		saveButtonData.left = new FormAttachment(0,5);
		saveButtonData.top = new FormAttachment(tree, 5);
		saveConfigButton.setLayoutData(saveButtonData);
	}

	private void createCombo(Composite main) {
	}
	
	private void addWidgetListeners() {
		

		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Clean up any previous editor control
				Control oldEditor = editor.getEditor();
				if (oldEditor != null) oldEditor.dispose();
		
				// Identify the selected row
				TreeItem item = (TreeItem)e.item;
				if (item == null) return;
				
				if( item.getData("editable") != null && item.getData("editable") instanceof Boolean ) {
					boolean bool = ((Boolean)item.getData("editable")).booleanValue();
					if( bool ) {
						// The control that will be the editor must be a child of the Table
						Text newEditor = new Text(tree, SWT.NONE);		
						newEditor.setText(item.getText());
						newEditor.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent e) {
								Text text = (Text)editor.getEditor();
								editor.getItem().setText(text.getText());
								Object fromMap = treesMap.get(editor.getItem());
								if( fromMap != null ) {
									DDBeanImpl impl = (DDBeanImpl)fromMap;
									impl.setText(text.getText());									
								}
							}
						});
						newEditor.selectAll();
						newEditor.setFocus();
						editor.setEditor(newEditor, item);
					}
				}
			}
		});
		acceptJarButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int selectedIndex = jarCombo.getSelectionIndex();
				if( selectedIndex != -1 ) {
					String filename = jarCombo.getItem(selectedIndex);
					vendorClassLoader = DeploymentJarUtils.createVendorClassLoader(filename);
					
					String uri = "";
					if( targetCombo.getText() != null ) {
						if( targetsMap.get(targetCombo.getText()) != null ) {
							
							// TODO USE CONSTANTS
							uri = ((StringProperties)targetsMap.get(targetCombo.getText())).getPiece(1);
						}
					}
					System.out.println("URI is " + uri);
					DeploymentManager manager = null;
					try {
						manager = DeploymentJarUtils.
							getDeploymentManager(vendorClassLoader, filename, uri);
						if( manager != null ) {
							try {
								// Perform the next actions using our stored classloader
								ClassLoader cachedLoader = Thread.currentThread().getContextClassLoader();
								Thread.currentThread().setContextClassLoader(vendorClassLoader);

								currentConfig = manager.createConfiguration(deployable);
								CBeanXpaths paths = ModelUtils.parse(currentConfig, deployable);
								tree.removeAll();
								fillTree(tree, paths);
								
								// Restore loader at the end
								Thread.currentThread().setContextClassLoader(cachedLoader);
							} catch( InvalidModuleException ime ) {
							}
						} 
					} catch( Throwable thr ) { 
				          MessageBox mb = new MessageBox(CreateConfigurationDialog.this.getShell(), 
				        		  SWT.ICON_ERROR | SWT.OK );

					          mb.setMessage("The vendor-supplied .jar file did not load properly.");
					          mb.setText("Vendor Jar Error");
					          mb.open();
					}
				} 
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			} 
			
		} );
		
		saveConfigButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if( currentConfig == null ) {
			          MessageBox mb = new MessageBox(CreateConfigurationDialog.this.getShell(), 
			        		  SWT.ICON_ERROR | SWT.OK );

				          mb.setMessage("There is no active configuration to save.");
				          mb.setText("Configuration object does not exist.");
				          mb.open();
				          return;
				}
				// First save the tree's values to the ddbeans
				String fileToSaveTo = new SafeSaveDialog(new Shell()).open();
				if( fileToSaveTo != null ) {
					File file = new File(fileToSaveTo);
					try {
						
						// Perform the next actions using our stored classloader
						ClassLoader cachedLoader = Thread.currentThread().getContextClassLoader();
						Thread.currentThread().setContextClassLoader(vendorClassLoader);

						// First save the file
						OutputStream os = new FileOutputStream(file);
						currentConfig.save(os);
						
						// Restore loader at the end
						Thread.currentThread().setContextClassLoader(cachedLoader);

						
						// Then add it to our preferences
						String vendorJar = jarCombo.getText();
						String key = Deploy88PreferencePage.CONFIG_PREFIX + vendorJar;
						PreferenceStoreUtil.addIncrementalPreference(
								key, fileToSaveTo );
					} catch( FileNotFoundException fne ) {
					} catch( ConfigurationException ce ) {
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			} 
			
		} );
	}
	
	
	private void fillTree( Tree tree , CBeanXpaths paths) {
		
		ArrayList children = paths.getChildren();
		for( Iterator j = children.iterator(); j.hasNext();) {
			CBeanXpaths subPath = (CBeanXpaths)j.next();
			TreeItem subItem = new TreeItem(tree, SWT.NONE);
			subItem.setData("editable", new Boolean(false));
			subItem.setText(subPath.getPath());
			fillTree(subItem, subPath);
		}
	}
	
	private void fillTree(TreeItem item, CBeanXpaths paths ) {
		ArrayList children = paths.getChildren();
		if( children.size() == 0 ) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setData("editable", new Boolean(true));
			subItem.setText(CHANGEME);	
			subItem.setForeground(new Color(subItem.getDisplay(), 200, 0, 0));
			treesMap.put(subItem, paths.getDd());
		} else {
			for( Iterator j = children.iterator(); j.hasNext();) {
				CBeanXpaths subPath = (CBeanXpaths)j.next();
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				subItem.setData("editable", new Boolean(false));
				subItem.setText(subPath.getPath());
				fillTree(subItem, subPath);
			}
		}
		item.setExpanded(true);
	}
	
	/**
	 * This class provides a facade for the "save" FileDialog class. If the selected
	 * file already exists, the user is asked to confirm before overwriting.
	 */
	public class SafeSaveDialog {
	  // The wrapped FileDialog
	  private FileDialog dlg;

	  /**
	   * SafeSaveDialog constructor
	   * 
	   * @param shell the parent shell
	   */
	  public SafeSaveDialog(Shell shell) {
	    dlg = new FileDialog(shell, SWT.SAVE);
	  }

	  public String open() {
	    // We store the selected file name in fileName
	    String fileName = null;

	    // The user has finished when one of the
	    // following happens:
	    // 1) The user dismisses the dialog by pressing Cancel
	    // 2) The selected file name does not exist
	    // 3) The user agrees to overwrite existing file
	    boolean done = false;

	    while (!done) {
	      // Open the File Dialog
	      fileName = dlg.open();
	      if (fileName == null) {
	        // User has cancelled, so quit and return
	        done = true;
	      } else {
	        // User has selected a file; see if it already exists
	        File file = new File(fileName);
	        if (file.exists()) {
	          // The file already exists; asks for confirmation
	          MessageBox mb = new MessageBox(dlg.getParent(), SWT.ICON_WARNING
	              | SWT.YES | SWT.NO);

	          // We really should read this string from a
	          // resource bundle
	          mb.setMessage(fileName + " already exists. Do you want to replace it?");

	          // If they click Yes, we're done and we drop out. If
	          // they click No, we redisplay the File Dialog
	          done = mb.open() == SWT.YES;
	        } else {
	          // File does not exist, so drop out
	          done = true;
	        }
	      }
	    }
	    return fileName;
	  }

	  public String getFileName() {
	    return dlg.getFileName();
	  }

	  public String[] getFileNames() {
	    return dlg.getFileNames();
	  }

	  public String[] getFilterExtensions() {
	    return dlg.getFilterExtensions();
	  }

	  public String[] getFilterNames() {
	    return dlg.getFilterNames();
	  }

	  public String getFilterPath() {
	    return dlg.getFilterPath();
	  }

	  public void setFileName(String string) {
	    dlg.setFileName(string);
	  }

	  public void setFilterExtensions(String[] extensions) {
	    dlg.setFilterExtensions(extensions);
	  }

	  public void setFilterNames(String[] names) {
	    dlg.setFilterNames(names);
	  }

	  public void setFilterPath(String string) {
	    dlg.setFilterPath(string);
	  }

	  public Shell getParent() {
	    return dlg.getParent();
	  }

	  public int getStyle() {
	    return dlg.getStyle();
	  }

	  public String getText() {
	    return dlg.getText();
	  }

	  public void setText(String string) {
	    dlg.setText(string);
	  }
	}

}
