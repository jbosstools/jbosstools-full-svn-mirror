package org.jboss.ide.eclipse.archives.ui.dialogs;

import java.util.Comparator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.archives.core.model.IVariableProvider;
import org.jboss.ide.eclipse.archives.ui.preferences.VariablesPreferencePage.Wrapped;
import org.jboss.ide.eclipse.archives.ui.providers.VariablesContentProvider;
import org.jboss.ide.eclipse.archives.ui.providers.VariablesLabelProvider;

public class SelectVariableDialog extends Dialog {
	private TreeViewer viewer;
	private Comparator<IVariableProvider> comparator;
	private VariablesContentProvider cProvider;
	private VariablesLabelProvider lProvider;
	private Object selected;
	public SelectVariableDialog(Shell parentShell) {
		super(parentShell);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Title");
	}
	protected Control createButtonBar(Composite parent) {
		Control bar = super.createButtonBar(parent);
		validateFields();
		return bar;
	}
	protected Control createDialogArea(Composite parent) {
		Composite master = new Composite(parent, SWT.NONE);
		master.setLayout(new FormLayout());
		viewer = new TreeViewer(master, SWT.SINGLE | SWT.BORDER);
		comparator = new Comparator<IVariableProvider>() {
			public int compare(IVariableProvider o1, IVariableProvider o2) {
				if( o1.getEnabled() != o2.getEnabled())
					return o1.getEnabled() ? -1 : 1;

				// now weights
				if( o1.getWeight() != o2.getWeight() ) 
					return o1.getWeight() > o2.getWeight() ? 1 : -1;
				return 0;
			} 
		};
		cProvider = new VariablesContentProvider(comparator);
		lProvider = new VariablesLabelProvider(null);
		viewer.setContentProvider(cProvider);
		viewer.setLabelProvider(lProvider);
		viewer.setInput("");
		FormData viewerData = new FormData();
		viewerData.left = new FormAttachment(0,0);
		viewerData.right = new FormAttachment(0,250);
		viewerData.top = new FormAttachment(0,0);
		viewerData.bottom = new FormAttachment(0,400);
		viewer.getTree().setLayoutData(viewerData);
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				selectionChanged2();
				validateFields();
			}});
		return viewer.getTree();
	}
	
	protected void selectionChanged2() {
		IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
		selected = sel.getFirstElement();
	}
	public void validateFields() {
		boolean valid = false;
		if( selected != null && selected instanceof Wrapped && ((Wrapped)selected).getName() != null)
			valid = true;
		getButton(IDialogConstants.OK_ID).setEnabled(valid);
	}
	public String getSelectedVarName() {
		if( selected != null && selected instanceof Wrapped && ((Wrapped)selected).getName() != null)
			return ((Wrapped)selected).getName();
		return null;
	}
}
