/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.as.core.publishers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IModule;
import org.jboss.ide.eclipse.as.core.model.EventLogModel.EventLogTreeItem;
import org.jboss.ide.eclipse.as.core.util.SimpleTreeItem;

public interface IJBossServerPublisher {
	// event constants
	public static final String PUBLISH_MAJOR_TYPE = "org.jboss.ide.eclipse.as.core.publishers.MajorType";
	public static final String MODULE_NAME = "org.jboss.ide.eclipse.as.core.publishers.ModuleName";
	public static final int PUBLISH = 0;
	public static final int REMOVE = 1;
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int SKIPPED = 2;
	
	
	
	
	public void publishModule(int kind, int deltaKind, int modulePublishState,
			IModule[] module, IProgressMonitor monitor) 
								throws CoreException;
	public int getPublishState();
	
	public class PublishEvent extends EventLogTreeItem {
		public PublishEvent(SimpleTreeItem parent, String specificType) {
			super(parent, PUBLISH_MAJOR_TYPE, specificType);
		}
		public PublishEvent(SimpleTreeItem parent, String specificType, IModule module) {
			super(parent, PUBLISH_MAJOR_TYPE, specificType);
			setProperty(MODULE_NAME, module.getName());
		}
		
		
	}
	
}
