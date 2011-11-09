package org.jboss.tools.forge.ui.console;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.forge.ui.ForgeUIPlugin;


public class ForgeCommandProcessor {
	
	static Map<String, ForgeCommandPostProcessor> POST_PROCESSORS = null;
	
	public static Map<String, ForgeCommandPostProcessor> getPostProcessors() {
		if (POST_PROCESSORS == null) {
			POST_PROCESSORS = new HashMap<String, ForgeCommandPostProcessor>();
			POST_PROCESSORS.put("new-project", new NewProjectPostProcessor());
			POST_PROCESSORS.put("persistence", new PersistencePostProcessor());
			POST_PROCESSORS.put("pick-up", new PickUpPostProcessor());
			POST_PROCESSORS.put("field", new FieldPostProcessor());
			POST_PROCESSORS.put("prettyfaces", new PrettyFacesPostProcessor());
		}
		return POST_PROCESSORS;
	}
	
	private String currentCommand;
	private StringBuffer buffer = new StringBuffer();
	
	public void startCommand(String command) {
		buffer.setLength(0);
		currentCommand = command;
	}
	
	public void stopCurrentCommand() {
		if (currentCommand != null) {
			final String command = currentCommand;
			final String output = buffer.toString();
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					postProcessCurrentCommand(command, output);
				}				
			});
		}
		currentCommand = null;
	}
	
	public void log(String str) {
		if (currentCommand != null) {
			buffer.append(str);
		}
	}
	
	private void postProcessCurrentCommand(String currentCommand, String output) {
		if (currentCommand == null) return;
		String mainCommand = currentCommand;
		int i = mainCommand.indexOf(' ');
		if (i != -1) {
			mainCommand = currentCommand.substring(0, i);
		}
		refreshWorkspace();
		ForgeCommandPostProcessor postProcessor = getPostProcessors().get(mainCommand);
		if (postProcessor != null) {
			postProcessor.postProcessCommand(mainCommand, output);
		}
		showForgeConsole();
	}
	
	private void refreshWorkspace() {
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			ForgeUIPlugin.log(e);
		}
	}
	
	private void showForgeConsole() {
		try {
			IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
			workbenchPage.showView("org.jboss.tools.forge.console").setFocus();
		} catch (PartInitException e) {
			ForgeUIPlugin.log(e);
		}
		
	}
	
}
