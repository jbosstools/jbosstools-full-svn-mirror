/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.project.AopProjectNature;
import org.jboss.ide.eclipse.jdt.aop.ui.views.AspectManagerView;

/**
 * @author Honain
 *  
 */
public class DescriptorChangeListener implements IResourceChangeListener
{
	private DescriptorDeltaVisitor deltaVisitor;

	public DescriptorChangeListener()
	{
		deltaVisitor = new DescriptorDeltaVisitor();
	}

	public void resourceChanged(IResourceChangeEvent event)
	{
		try
		{
			event.getDelta().accept(deltaVisitor);
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
	}

	private class DescriptorDeltaVisitor implements IResourceDeltaVisitor
	{
		public boolean visit(IResourceDelta delta)
		{
			IResource changedResource = delta.getResource();

			if (changedResource == null)
				return true;

			IProject project = changedResource.getProject();
			
			try {
				if (project == null || ! project.isAccessible() || ! project.hasNature(AopProjectNature.NATURE_ID))
					return true;
			} catch (CoreException e) {
				e.printStackTrace();
				
				return true;
			}
			
			IJavaProject javaProject = JavaCore
					.create(changedResource.getProject());
			AopCorePlugin.setCurrentJavaProject(javaProject);

			if (changedResource.getRawLocation() == null)
			{
				//System.out.println("[descriptor-change-listener] raw location was null for resource: " + changedResource);
				return true;
			}
			
			File resourceFile = changedResource.getRawLocation().toFile();
			Set descriptors = AopCorePlugin.getDefault().getProjectDescriptors(
					javaProject);

			if (descriptors == null)
			{
				AopCorePlugin.getDefault().getDefaultDescriptor(javaProject);
				descriptors = AopCorePlugin.getDefault().getProjectDescriptors(
						javaProject);
				if (descriptors == null)
					return true;
			}

			AopDescriptor result = null;
			boolean isDescriptor = false;

			Iterator dIter = descriptors.iterator();
			while (dIter.hasNext())
			{
				AopDescriptor descriptor = (AopDescriptor) dIter.next();

				if (descriptor.getFile().compareTo(resourceFile) == 0)
				{
					result = descriptor;
					isDescriptor = true;
					break;
				}
			}

			if (!isDescriptor)
				return true;

			
			
			// A model reconciler / delta API needs to be created for the descriptors
			// contents... oy.
			
			//AopCorePlugin.getDefault().updateModel(javaProject);
			
			final IJavaProject finalProject = javaProject;
			new Job ("Updating \"" + javaProject.getProject().getName() + "\" AOP Model")
			{
				protected IStatus run (final IProgressMonitor monitor) {
					Display.getDefault().asyncExec(new Runnable(){
						public void run () {
							System.out.println("[descriptor-change-listener] reconciling");
							AopCorePlugin.getDefault().updateModel(finalProject, monitor);
						}
					});

					return Status.OK_STATUS;
				}		
			}.schedule();
			
//			try
//			{
//				AspectXmlLoader.deployXML(result.getFile().toURL());
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}

//			AopCorePlugin.getDefault().updateProjectReport(javaProject);
			
			if (AspectManagerView.instance() != null)
			{
				AspectManagerView.instance().setDescriptorAsync(result);
			}
			System.out.println("[descriptor-change-listener] ending");


			return true;
		}
	}
}