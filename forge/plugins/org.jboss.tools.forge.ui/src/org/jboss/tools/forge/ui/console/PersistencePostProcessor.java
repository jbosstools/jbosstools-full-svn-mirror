package org.jboss.tools.forge.ui.console;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.jboss.tools.forge.ui.ForgeUIPlugin;

public class PersistencePostProcessor implements ForgeCommandPostProcessor {

	@Override
	public void postProcess(Map<String, String> commandDetails) {
		String command = commandDetails.get("ec");
		if (command.indexOf("setup") != -1) {
			postProcessPersistenceSetup(commandDetails);
		}
	}
	
	private void postProcessPersistenceSetup(Map<String, String> commandDetails) {
			IProject project = ForgeCommandPostProcessorHelper.getProject(commandDetails.get("cpn"));
			if (project == null) return;
			try {
				IFile file = project.getFile("/src/main/resources/META-INF/persistence.xml");
				if (file == null) return;
				Object objectToSelect = file;
				IWorkbenchPage workbenchPage = ForgeCommandPostProcessorHelper.getActiveWorkbenchPage();
				IDE.openEditor(workbenchPage, file);
				IViewPart projectExplorer = workbenchPage.findView("org.eclipse.ui.navigator.ProjectExplorer");
				if (projectExplorer != null && projectExplorer instanceof ISetSelectionTarget) {
					((ISetSelectionTarget)projectExplorer).selectReveal(new StructuredSelection(objectToSelect));
				} 
				IViewPart packageExplorer = workbenchPage.findView("org.eclipse.jdt.ui.PackageExplorer"); 
				if (packageExplorer == null && projectExplorer == null) {
					packageExplorer = workbenchPage.showView("org.eclipse.jdt.ui.PackageExplorer");
				} 
				if (packageExplorer != null && packageExplorer instanceof ISetSelectionTarget) {
					((ISetSelectionTarget)packageExplorer).selectReveal(new StructuredSelection(objectToSelect));
				}
			} catch (PartInitException e) {
				ForgeUIPlugin.log(e);
			}
	}
	
	
}
