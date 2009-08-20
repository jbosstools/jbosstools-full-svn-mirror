package org.jboss.ide.eclipse.as.wtp.override.ui.propertypage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.ui.wizards.BuildPathDialogAccess;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.j2ee.project.facet.IJavaProjectMigrationDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.JavaProjectMigrationDataModelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.jboss.ide.eclipse.as.wtp.override.core.vcf.ComponentUtils;

public class ExternalJarReferenceWizardFragment extends JarReferenceWizardFragment {
	public Composite createComposite(Composite parent, IWizardHandle handle) {
		Composite c = super.createComposite(parent, handle);
		handle.setTitle("Add an External Jar Reference");
		handle.setDescription("Here you can reference a filesystem Jar\n"
						+ "This is not a suggested use-case, but is here for backwards compatability.");
		return c;
	}

	protected void buttonPressed() {
		selected = BuildPathDialogAccess
			.chooseExternalJAREntries(browse.getShell());
		viewer.refresh();
	}

	public void performFinish(IProgressMonitor monitor) throws CoreException {
		IVirtualComponent rootComponent = (IVirtualComponent)getTaskModel().getObject(NewReferenceWizard.ROOT_COMPONENT);
		if (selected != null && selected.length > 0) {
			ArrayList<IVirtualComponent> compList = new ArrayList<IVirtualComponent>();
			ArrayList<String> paths = new ArrayList<String>();
			for (int i = 0; i < selected.length; i++) {
				// IPath fullPath = project.getFile(selected[i]).getFullPath();
				String type = VirtualArchiveComponent.LIBARCHIVETYPE
						+ IPath.SEPARATOR;
				IVirtualComponent archive = ComponentCore
						.createArchiveComponent(rootComponent.getProject(),
								type + selected[i].toString());
				compList.add(archive);
				paths.add(selected[i].lastSegment());
			}
			IVirtualComponent[] components = (IVirtualComponent[]) compList.toArray(new IVirtualComponent[compList.size()]);
			String[] paths2 = (String[]) paths.toArray(new String[paths.size()]);
			getTaskModel().putObject(NewReferenceWizard.COMPONENT, components);
			getTaskModel().putObject(NewReferenceWizard.COMPONENT_PATH, paths2);
		}
	}
}
