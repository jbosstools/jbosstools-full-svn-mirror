package org.jboss.ide.eclipse.firstrun.wizard.pages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.firstrun.FirstRunMessages;
import org.jboss.ide.eclipse.firstrun.FirstRunPlugin;
import org.jboss.ide.eclipse.firstrun.providers.AbstractProjectProvider;
import org.jboss.ide.eclipse.firstrun.providers.ProjectLabelProvider;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;
import org.jboss.ide.eclipse.xdoclet.run.builder.XDocletRunBuilder;

public class FirstRunXDocletProjectsPage extends WizardPage {

	private CheckboxTableViewer projectTable;
	
	public FirstRunXDocletProjectsPage ()
	{
		super (FirstRunMessages.getString("XDocletProjectsPage.title"),
				FirstRunMessages.getString("FirstRunWizard.title"),
				FirstRunPlugin.getImageDescriptor(FirstRunPlugin.ICON_JBOSSIDE_LOGO));
	}
	
	public void createControl(Composite parent) {
		setTitle(FirstRunMessages.getString("XDocletProjectsPage.title"));
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		Label label = new Label(main, SWT.WRAP);
		label.setText(FirstRunMessages.getString("XDocletProjectsPage.description"));
		
		projectTable = CheckboxTableViewer.newCheckList(main, SWT.BORDER);
		projectTable.setContentProvider(new AbstractProjectProvider () {
			public boolean checkProject(IProject project)
			{
				return !ProjectUtil.projectHasBuilder(project, XDocletRunBuilder.BUILDER_ID)
					&& project.getFile(new Path(XDocletRunPlugin.BUILD_FILE)).exists();
			}
		});

		projectTable.setLabelProvider(new ProjectLabelProvider());
		
		projectTable.setInput(ResourcesPlugin.getWorkspace());
		projectTable.setAllChecked(true);
		
		projectTable.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite buttonComposite = new Composite(main, SWT.NONE);
		buttonComposite.setLayout(new RowLayout());
		
		Button selectAllButton = new Button(buttonComposite, SWT.NONE);
		selectAllButton.setText("Select All");
		selectAllButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			public void widgetSelected(SelectionEvent e) {
				projectTable.setAllChecked(true);
			}
		});
		
		Button unselectAllButton = new Button(buttonComposite, SWT.NONE);
		unselectAllButton.setText("Unselect All");
		unselectAllButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			public void widgetSelected(SelectionEvent e) {
				projectTable.setAllChecked(false);
			}
		});
		
		setControl(main);
	}

	public IProject[] getSelectedProjects ()
	{
		Object elements[] = projectTable.getCheckedElements();
		IProject projects[] = new IProject[elements.length];
		
		System.arraycopy(elements, 0, projects, 0, elements.length);
		return projects;
	}
}
