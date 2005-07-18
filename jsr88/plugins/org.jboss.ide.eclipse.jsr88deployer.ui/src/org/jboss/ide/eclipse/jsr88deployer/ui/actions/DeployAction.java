package org.jboss.ide.eclipse.jsr88deployer.ui.actions;

import java.io.File;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.jsr88deployer.core.PreferenceStoreUtil;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.DeploymentJarUtils;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.StringProperties;
import org.jboss.ide.eclipse.jsr88deployer.ui.composites.FileAssocDescriptionComposite;
import org.jboss.ide.eclipse.jsr88deployer.ui.preferences.Deploy88PreferencePage;

public class DeployAction extends AbstractDeployAction 
{

	/* (non-Javadoc)
	 * @see org.jboss.ide.eclipse.jsr88deployer.ui.actions.AbstractDeployAction#process(java.util.Collection)
	 */
	protected void process(IResource resource) {
			System.out.print("file extension is: " + 
					resource.getFileExtension() + " and... ");
			int deployType = AbstractDeployAction.getDeploymentType(resource);
			switch( deployType ) {
			case AbstractDeployAction.DEPLOYABLE:
				System.out.println("Deployable");
				handleDeployable(resource);
				break;
			case AbstractDeployAction.DESCRIPTOR:
				System.out.println("Descriptor");
				break;
			case AbstractDeployAction.J2EEAPP:
				System.out.println("j2ee app!");
				break;
			case AbstractDeployAction.UNDEPLOYABLE:
				System.out.println("I do not know how to deploy this.");
				break;
			}
	}
	
	protected void handleDeployable(IResource resource) {
		Object[] fileAssociationPreferences = PreferenceStoreUtil.
			loadIncrementalPreferences(Deploy88PreferencePage.FILE_ASSOC_PREFIX);
		boolean found = false;
		boolean aborted = false;
		
		
		StringProperties fileAssoc = null;
		
		// Find our file association 
		for( int i = 0; i < fileAssociationPreferences.length; i++ ) {
			if( fileAssociationPreferences[i] instanceof String ) {
				if( ((String)fileAssociationPreferences[i]).startsWith(resource.getFullPath() + "\n")) {
					found = true;
					fileAssoc = new StringProperties(((String)fileAssociationPreferences[i]));
				}
			}
		}
		
		if( !found ) {
			// If this file isn't associated with one already, let them do it.
			ChooseFileAssocDialog dialog = new ChooseFileAssocDialog(new Shell());
			if( dialog.open() == IDialogConstants.OK_ID ) {
				String name = dialog.getAssocName();
				String storageVal = resource.getFullPath() + "\n" + name;
				PreferenceStoreUtil.addIncrementalPreference(Deploy88PreferencePage.FILE_ASSOC_PREFIX, storageVal);
				fileAssoc = new StringProperties(storageVal);
			} else {
				aborted = true;
			}
		}
		
		if( aborted ) return;

		if( fileAssoc == null ) return ;
			

		// get the config file, etc from preferences
		StringProperties assocProps = PreferenceStoreUtil.
		getMatchingPreference( PreferenceStoreUtil.ASSOC_PREFIX, 
				PreferenceStoreUtil.ASSOCIATION_PREFERENCE_NAME, 
				fileAssoc.getPiece(PreferenceStoreUtil.FILEASSOC_ASSOC));
	
		// now find our targets uri and such
		if( assocProps == null ) {handleNullAssoc(); return;}
		StringProperties targetProps = 
			PreferenceStoreUtil.getMatchingPreference(
					PreferenceStoreUtil.TARGET_PREFIX, 
					PreferenceStoreUtil.TARGET_NAME,
					assocProps.getPiece(PreferenceStoreUtil.ASSOCIATION_PREFERENCE_TARGET));
	
	
		if( targetProps == null ) { handleNullTarget(); return; }
		

		// separate out the strings we'll use
		String uri = targetProps.getPiece(PreferenceStoreUtil.TARGET_URI);
		String user = targetProps.getPiece(PreferenceStoreUtil.TARGET_USER);
		String pass = targetProps.getPiece(PreferenceStoreUtil.TARGET_PASS);
		String vendorFile = assocProps.getPiece(PreferenceStoreUtil.ASSOCIATION_PREFERENCE_JAR);
		String configFile = assocProps.getPiece(PreferenceStoreUtil.ASSOCIATION_PREFERENCE_CONFIG);
		File resourceFile = resource.getLocation().toFile();
		File deploymentPlanFile = new File(configFile);
		
		ClassLoader vendorClassLoader = 
			DeploymentJarUtils.createVendorClassLoader(vendorFile);
		
		try {
			DeploymentManager manager = 
				DeploymentJarUtils.getDeploymentManager(vendorClassLoader, 
					vendorFile, uri, user, pass);
	
			
			 
			Target[] targets = manager.getTargets();
			manager.distribute(targets, resourceFile, deploymentPlanFile);
		} catch( Throwable thr ) {
			
		}
	}
	
	private void handleNullAssoc() {
		return;
	}
	private void handleNullTarget() {
		return;
	}
	
	private class ChooseFileAssocDialog extends Dialog {

		
		
		private StringProperties[] associationPreferences;
		private Combo selection;
		private Label message;
		private FileAssocDescriptionComposite descComp;
		
		private String retval = null;
		
		
		protected ChooseFileAssocDialog(Shell parentShell) {
			super(parentShell);

			Object[] preferences = PreferenceStoreUtil.
				loadIncrementalPreferences(Deploy88PreferencePage.ASSOC_PREFIX);

			associationPreferences = new StringProperties[preferences.length];
			for( int i = 0; i < preferences.length; i++ ) {
				if( preferences[i] instanceof String ) {
					associationPreferences[i] = new StringProperties((String)preferences[i]);
				}
			}
		}
		
		protected Control createDialogArea(Composite parent) { 
			parent.getShell().setText("Create a deploy association");
			
			Composite superGridComposite = (Composite)super.createDialogArea(parent);
			
			Composite main = new Composite(superGridComposite, SWT.NONE);
			main.setLayout(new FormLayout());
			
			createWidgets(main);
			setWidgetText();
			setWidgetData();
			addWidgetListeners();
			
			
			return main;

		}


		/**
		 * 
		 */
		private void addWidgetListeners() {
			selection.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					int index = selection.getSelectionIndex();
					if( index != -1 ) {
						String val = selection.getItem(index);
						if( val != null ) {
							boolean done = false;
							for( int i = 0; i < associationPreferences.length && !done; i++ ) {
								String name = associationPreferences[i].getPiece(0);
								if( name.equals(val)) {
									done = true;
									descComp.setVals(associationPreferences[i]);
									retval = descComp.getTextFromNameWidget();
								}
							}
						}
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				} 
				
			} );
		}

		/**
		 * 
		 */
		private void setWidgetData() {
			FormData messageData = new FormData();
			messageData.left = new FormAttachment(0,5);
			messageData.right = new FormAttachment(0, 400);
			messageData.top = new FormAttachment(0,5);
			message.setLayoutData(messageData);

			FormData selectionData = new FormData();
			selectionData.left = new FormAttachment(0,5);
			selectionData.right = new FormAttachment(0, 400);
			selectionData.top = new FormAttachment(message,5);
			selection.setLayoutData(selectionData);

			FormData descCompData = new FormData();
			descCompData.left = new FormAttachment(0,5);
			descCompData.right = new FormAttachment(0, 400);
			descCompData.top = new FormAttachment(selection,5);
			descComp.setLayoutData(descCompData);
		}

		/**
		 * 
		 */
		private void setWidgetText() {
			message.setText("This deployable does not have an associated deployment configuration. \n" + 
			"Please select one from below.");

			
			// COMBO
			
			for( int i = 0; i < associationPreferences.length; i++ ) {
				String name = associationPreferences[i].getPiece(0);
				selection.add(name);
			}
			
		}

		/**
		 * @param main
		 */
		private void createWidgets(Composite main) {
			selection = new Combo(main, SWT.READ_ONLY);
			message = new Label(main, SWT.NONE);
			descComp = new FileAssocDescriptionComposite(main, SWT.NONE, false);
//			descComp.setVisible(true);
			
		}

		public String getAssocName() {
			return retval;
		}

		// Must choose one. Can't press OK with nothing selected.
		protected void okPressed() {
			if( retval == null || retval.equals("")) {
		        setReturnCode(CANCEL);
		        close();
			} else {
		        setReturnCode(OK);
		        close();
			}
		}
		
	}
	
}
