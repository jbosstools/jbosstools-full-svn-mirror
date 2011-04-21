/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core.project;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;

public class AopProjectNature implements IProjectNature {
	
	public static final String NATURE_ID = "org.jboss.ide.eclipse.jdt.aop.core.AopProjectNature";
	
	private IProject project;
	
	public AopProjectNature() {
	}

	public void configure() throws CoreException {
	
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

	   //add builders to project
	   ICommand builderCommand = desc.newCommand();
	   builderCommand.setBuilderName(AopProjectBuilder.BUILDER_ID);
	   
	   //ICommand preBuilderCommand = desc.newCommand();
	   //preBuilderCommand.setBuilderName(AopProjectPreBuilder.BUILDER_ID);
	   
	   ICommand[] newCommands = new ICommand[commands.length + 1];
	   System.arraycopy(commands, 0, newCommands, 0, commands.length);
	   
	   //newCommands[0] = preBuilderCommand;
	   newCommands[newCommands.length - 1] = builderCommand;
	   
	   desc.setBuildSpec(newCommands);
	   
	   try {
			project.setDescription(desc, new ProgressMonitorPart(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), new RowLayout()));
	   } catch (CoreException e) {
			e.printStackTrace();
		}
	   
	   System.out.println("finished configuring");

	}
	
	public void deconfigure() throws CoreException {
	}

	public IProject getProject()  {
		return this.project;
	}

	public void setProject(IProject project)  {
		IJavaProject javaProject = JavaCore.create(project);
		AopCorePlugin.setCurrentJavaProject (javaProject);
		
		this.project = project;
	}
	
	
	public static void ensureAopProjectNature (IJavaProject project)
	{
		boolean added = AopCorePlugin.addProjectNature(project.getProject(), NATURE_ID);
		
		if (added)
		{
			AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(project);
			descriptor.save();
		}
	}
}
