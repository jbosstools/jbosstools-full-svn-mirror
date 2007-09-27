/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package example6ResourceTracking;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;

public class Example6Model implements IResourceChangeListener, IElementChangedListener {
	
	public static final String RESOURCES = "RESOURCES";
	public static final String ELEMENTS = "ELEMENTS";
	
	
	
	private IWorkspace workspace;
	private ModelRoot root;
	private ArrayList listeners;
	
	private static Example6Model instance;
	public static Example6Model getDefault() {
		if( Example6Model.instance == null ) 
			instance = new Example6Model();
		return instance;
	}

	public Example6Model() {
		root = new ModelRoot();
		listeners = new ArrayList();
		workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(this);
		JavaCore.addElementChangedListener(this);
		instance = this;
	}
	
	
	public void resourceChanged(IResourceChangeEvent event) {
		root.addResourceChange(event);
		for( Iterator i = listeners.iterator(); i.hasNext();) {
			((MyChangeListener)i.next()).fireMyChangeEvent();
		}
	}


	public void elementChanged(ElementChangedEvent event) {
		root.addElementChange(event);
		for( Iterator i = listeners.iterator(); i.hasNext();) {
			System.out.println("firing");
			((MyChangeListener)i.next()).fireMyChangeEvent();
		}
	}

	public ModelRoot getModel() {
		return root;
	}
	
	public void addListener(MyChangeListener listener) {
		listeners.add(listener);
	}
	
	public interface MyChangeListener {
		public void fireMyChangeEvent();
	}

	public class ModelRoot {
		private ArrayList resourceChanges;
		private ArrayList elementChanges;
		public ModelRoot() {
			resourceChanges = new ArrayList();
			elementChanges = new ArrayList();			
		}
		
		public ArrayList getResourceChanges() {
			return resourceChanges;
		}
		public ArrayList getElementChanges() {
			return elementChanges;
		}
		
		public void addElementChange(ElementChangedEvent e) {
			elementChanges.add(e);
		}
		public void addResourceChange(IResourceChangeEvent e) {
			resourceChanges.add(e);
		}
		
		
	}
}
