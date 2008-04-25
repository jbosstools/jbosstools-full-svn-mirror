/**
 * JBoss, a Division of Red Hat
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
package org.jboss.ide.eclipse.ejb3.core.module;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IModuleArtifact;
import org.eclipse.wst.server.core.internal.ModuleFactory;
import org.eclipse.wst.server.core.internal.ServerPlugin;
import org.eclipse.wst.server.core.model.ModuleArtifactAdapterDelegate;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class Ejb30ArtifactAdapter extends ModuleArtifactAdapterDelegate {

	private ModuleFactory mf;
	private ModuleFactory getModuleFactory() {
		if( mf != null ) return mf;
		ModuleFactory[] factories = ServerPlugin.getModuleFactories();
		for( int i = 0; i < factories.length; i++ ) {
			if( factories[i].getId().equals(Ejb30ModuleFactory.ID )) {
				return factories[i];
			}
		}
		return null;
	}
	
	public IModuleArtifact getModuleArtifact(Object obj) {
		IJavaProject jp = null;
		if( obj instanceof IJavaProject ) {
			jp = (IJavaProject)obj;
		} else if( obj instanceof IProject ) {
			jp = JavaCore.create((IProject)obj);
		}
		
		if( jp != null ) {
			ModuleFactory mf = getModuleFactory();
			IModule mod = mf.findModule(jp.getElementName(), new NullProgressMonitor());
			if( mod != null ) {
				return wrap(mod);
			}
		}
		return null;
	}
	
	private IModuleArtifact wrap(final IModule mod) {
		return new IModuleArtifact() {
			public IModule getModule() {
				return mod;
			}
		};
	}
}
