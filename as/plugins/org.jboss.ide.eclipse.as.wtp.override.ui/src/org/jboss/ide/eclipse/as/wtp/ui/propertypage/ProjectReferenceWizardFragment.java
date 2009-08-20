package org.jboss.ide.eclipse.as.wtp.ui.propertypage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.j2ee.project.facet.IJavaProjectMigrationDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.JavaProjectMigrationDataModelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;

public class ProjectReferenceWizardFragment extends WizardFragment {
	private LabelProvider labelProvider = null;
	private ITreeContentProvider contentProvider = null;
	private TreeViewer viewer;
	private IProject selected;
	
	public boolean hasComposite() {
		return true;
	}

	public Composite createComposite(Composite parent, IWizardHandle handle) {
		handle.setTitle("Add a Project Reference");
		handle.setDescription("Here you can make a project into a reference.\n" + 
				"If the project is not already a flexible project, it will be converted into one.");
		
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());
		viewer = new TreeViewer(c, SWT.SINGLE | SWT.BORDER);
		viewer.setContentProvider(getContentProvider());
		viewer.setLabelProvider(getLabelProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selChanged();
			}
		});
		viewer.setInput(ResourcesPlugin.getWorkspace());
		return c;
	}
	
	private void selChanged() {
		if( viewer != null ) {
			IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
			if( sel != null ) 
				selected = (IProject)sel.getFirstElement();
		}
	}
	
	public void performFinish(IProgressMonitor monitor) throws CoreException {
		if( !ModuleCoreNature.isFlexibleProject(selected)) {
			try {
				IDataModel migrationdm = DataModelFactory.createDataModel(new JavaProjectMigrationDataModelProvider());
				migrationdm.setProperty(IJavaProjectMigrationDataModelProperties.PROJECT_NAME, selected.getName());
				migrationdm.getDefaultOperation().execute(new NullProgressMonitor(), null);
			} catch( ExecutionException ee) {
				// TODO something
			}
		}
		IVirtualComponent comp = ComponentCore.createComponent(selected);
		String path = selected.getName();
		String extension = ".jar";
		path += extension;

		getTaskModel().putObject(NewReferenceWizard.COMPONENT, comp);
		getTaskModel().putObject(NewReferenceWizard.COMPONENT_PATH, path);
	}

	
	protected LabelProvider getLabelProvider() {
		if( labelProvider == null ) {
			labelProvider = new LabelProvider() {
				public Image getImage(Object element) {
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_PROJECT);
				}
				public String getText(Object element) {
					if( element instanceof IProject )
						return ((IProject)element).getName();
					return element == null ? "" : element.toString();//$NON-NLS-1$
				}
			};
		}
		return labelProvider;
	}
	
	protected ITreeContentProvider getContentProvider() {
		if( contentProvider == null ) {
			contentProvider = new ITreeContentProvider() {
				public Object[] getElements(Object inputElement) {
					IProject root = (IProject)getTaskModel().getObject(NewReferenceWizard.PROJECT);
					IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
					ArrayList<IProject> list = new ArrayList<IProject>(Arrays.asList(projects));
					Iterator<IProject> i = list.iterator();
					IProject p;
					while(i.hasNext()) {
						p = i.next();
						if( !p.isOpen())
							i.remove();
						else if( p.equals(root))
							i.remove();
					}
					return (IProject[]) list.toArray(new IProject[list.size()]);
				}
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				}
				public void dispose() {
				}
				public boolean hasChildren(Object element) {
					return false;
				}
				public Object getParent(Object element) {
					return null;
				}
				public Object[] getChildren(Object parentElement) {
					return null;
				}
			};
		}
		return contentProvider;
	}
}
