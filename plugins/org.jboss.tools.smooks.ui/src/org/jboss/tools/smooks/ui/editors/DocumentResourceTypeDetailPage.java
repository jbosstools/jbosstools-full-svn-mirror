/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksPackage;

/**
 * @author dart
 *
 */
public class DocumentResourceTypeDetailPage extends AbstractSmooksModelDetailPage {

	private Text text;
	private Text selectorText;

	public DocumentResourceTypeDetailPage(SmooksFormEditor parentEditor,
			EditingDomain domain) {
		super(parentEditor, domain);
	}

	protected void createSectionContents(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		this.formToolKit.createLabel(parent, Messages.getString("DocumentResourceTypeDetailPage.Selector")); //$NON-NLS-1$
		selectorText = formToolKit.createText(parent, ""); //$NON-NLS-1$
		selectorText.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				if(!canFireChange) return;
				resetSelector(selectorText.getText());
			}
			
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		selectorText.setLayoutData(gd);
		
		this.formToolKit.createLabel(parent, Messages.getString("DocumentResourceTypeDetailPage.DocPath")); //$NON-NLS-1$
		Composite fileCom = formToolKit.createComposite(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileCom.setLayoutData(gd);
		fileCom.setLayout(layout);
		text = formToolKit.createText(fileCom, ""); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				if(!canFireChange) return;
				resetPath(text.getText());
			}
		});
		
		
		Button browseButton = formToolKit.createButton(fileCom, Messages.getString("DocumentResourceTypeDetailPage.BrowseFile"), SWT.NONE); //$NON-NLS-1$
		browseButton.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				browseFileSystem();
			}
			
		});
		formToolKit.paintBordersFor(parent);
		formToolKit.paintBordersFor(fileCom);
	}
	
	protected void resetSelector(String selector){
		Command command = SetCommand.create(domain, resourceConfigList, SmooksPackage.eINSTANCE.getResourceConfigType_Selector(), selector);
		domain.getCommandStack().execute(command);
//		resourceConfigList.getse
	}
	
	protected void browseFileSystem(){
		IFile[] files = WorkspaceResourceDialog.openFileSelection(this.parentEditor.getSite()
				.getShell(),
				"", "", false, null, Collections.EMPTY_LIST); //$NON-NLS-1$ //$NON-NLS-2$
		// dialog.setInitialSelections(selectedResources);
		if (files.length > 0) {
			IFile file = files[0];
			String s = file.getFullPath().toString();
			text.setText(s);
			return;
		}
	}
	
	protected void resetPath(String path){
		if(this.resourceConfigList != null){
			ResourceType resource = resourceConfigList.getResource();
			if(resource == null){
				return;
			}
			Command command = SetCommand.create(domain, resource, SmooksPackage.eINSTANCE.getResourceType_Value(), path);
			domain.getCommandStack().execute(command);
		}
	}

	protected void initSectionUI() {
		if(this.resourceConfigList != null){
			String selector = resourceConfigList.getSelector();
			if(selector != null) selectorText.setText(selector);
			ResourceType resource = resourceConfigList.getResource();
			if(resource != null){
				String path = resource.getValue();
				if(path == null) path = ""; //$NON-NLS-1$
				text.setText(path);
			}
		}
	}

}
