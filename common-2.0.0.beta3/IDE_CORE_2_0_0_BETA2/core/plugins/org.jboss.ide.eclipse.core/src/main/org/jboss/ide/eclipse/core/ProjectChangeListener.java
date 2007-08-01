/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * The purpose of this class is to encapsulate notification of a project being changed
 * You should be able to install this like a normal IResourceChangeListener
 * 	
 * @author marshall
 */
public abstract class ProjectChangeListener implements IResourceChangeListener {

	public void register ()
	{
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	public void resourceChanged(IResourceChangeEvent event)
	{
		if (event == null || event.getDelta() == null) return;
			
		IResourceDelta deltas[] = event.getDelta().getAffectedChildren();
		
		for (int i = 0; i < deltas.length; i++)
		{
				IResource resource = deltas[i].getResource();
				if (resource.isAccessible() && resource.getType() == IResource.PROJECT)
				{
					projectChanged((IProject) resource, deltas[i].getKind());
				}
		}
	}
	
	public abstract void projectChanged (IProject project, int kind);
}
