/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views.cloud.cnf;

import java.util.ArrayList;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.job.LoadCloudImagesJob;
import org.jboss.tools.deltacloud.core.job.LoadCloudInstancesJob;
import org.jboss.tools.deltacloud.ui.Activator;

public class CloudContentProvider implements ITreeContentProvider {
	private TreeViewer viewer;
	public CloudContentProvider() {
	}
	
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer)viewer; 
	}

	public Object[] getElements(Object inputElement) {
		DeltaCloudManager m = DeltaCloudManager.getDefault();
		DeltaCloud[] clouds = new DeltaCloud[]{};
		try {
			clouds = m.getClouds();
		} catch( DeltaCloudException dce) {}
		return clouds;
	}

	public abstract static class CloudAdaptable implements IAdaptable {
		private DeltaCloud cloud;
		public CloudAdaptable(DeltaCloud dc) {
			this.cloud = dc;
		}
		public DeltaCloud getCloud(){ return cloud; }
		public Object getAdapter(Class adapter) {
			if( adapter == DeltaCloud.class )
				return cloud;
			return null;
		}
	}
	
	public abstract static class CategoryContent extends CloudAdaptable {
		protected String name; 
		public CategoryContent(String name, DeltaCloud cloud) {
			super(cloud);
			this.name = name;
		}
		public String getName(){ return name;}
		public abstract Job getFetchChildrenJob(TreeViewer viewer);
		public abstract Object[] getChildren() throws Exception ;
	}
	
	public static class InstancesCategory extends CategoryContent {
		public InstancesCategory(String name, DeltaCloud cloud) {
			super(name, cloud);
		}
		public Job getFetchChildrenJob(TreeViewer viewer) {
			return new LoadCloudInstancesJob(getCloud(),refreshParentRunnable(viewer, this) );
		}
		public Object[] getChildren() throws Exception {
			return getCloud().instancesLoaded() ? getCloud().getInstances() : null;
		}
	}

	public static class ImagesCategory extends CategoryContent {
		public ImagesCategory(String name, DeltaCloud cloud) {
			super(name, cloud);
		}
		public Job getFetchChildrenJob(TreeViewer viewer) {
			return new LoadCloudImagesJob(getCloud(),refreshParentRunnable(viewer, this) );
		}
		public Object[] getChildren() throws Exception {
			if( !getCloud().imagesLoaded() )
				return null;
			DeltaCloudImage[] images = getCloud().getImages();
			int numPages = images.length / ImagesPager.PER_PAGE;
			if( images.length != 0 && 
					images.length % ImagesPager.PER_PAGE != 0)
				numPages++;
			ImagesPager[] pages = new ImagesPager[numPages];
			for( int i = 0; i < numPages; i++ ) {
				pages[i] = new ImagesPager(getCloud(), i);
			}
			return pages;
		}
	}
	
	public static class ImagesPager {
		public static final int PER_PAGE = 50;
		private DeltaCloud cloud;
		private int page;
		private int start, end;
		private ArrayList<DeltaCloudImage> myImages = null;
		public ImagesPager(DeltaCloud cloud, int page) {
			this.cloud = cloud;
			this.page = page;
			try {
				DeltaCloudImage[] images = cloud.getImages();
				start = page * PER_PAGE;
				end = ((page+1) * PER_PAGE);
				end = end > images.length ? images.length : end;
			} catch(DeltaCloudException dce){}
		}
		public Object[] getChildren() throws Exception {
			if( myImages == null ) {
				DeltaCloudImage[] images = cloud.getImages();
				myImages = new ArrayList<DeltaCloudImage>();
				for( int i = start; i < end; i++) {
					myImages.add(images[i]);
				}
			}
			return (DeltaCloudImage[]) myImages.toArray(new DeltaCloudImage[myImages.size()]);
		}
		public int getPage() { return page; }
		public String toString() {
			return start + "..." + end;
		}
	}
	
	public static class DelayObject {
		public Object parent;
		public DelayObject(Object parent) {
			this.parent = parent;
		}
	}

	private static Runnable refreshParentRunnable(final TreeViewer v, final Object parent) {
		return new Runnable() {
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						v.refresh(parent);
					}
				});
			}
		};
	}
	
	public Object[] getChildren(Object parentElement) {
		try {
			if( parentElement instanceof DeltaCloud) {
				return new CategoryContent[] {
						new InstancesCategory("Instances", (DeltaCloud)parentElement),
						new ImagesCategory("Images", (DeltaCloud)parentElement)
				};
			}
			
			if( parentElement instanceof CategoryContent ) {
				CategoryContent cc = (CategoryContent)parentElement;
				Object[] vals = cc.getChildren();
				if( vals == null ) {
					Job j = cc.getFetchChildrenJob(viewer);
					j.schedule();
					return new Object[]{new DelayObject(this)};
				}
				return vals;
			}

			if( parentElement instanceof ImagesPager ) {
				return ((ImagesPager)parentElement).getChildren();
			}
		} catch( Exception e ) {
			// log? show? idk
			Activator.getDefault().getLog().log(new Status(
					IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
		return new Object[]{};
	}
	
	public Object getParent(Object element) {
		return null;
	}
	public boolean hasChildren(Object element) {
		if( element instanceof DelayObject ) 
			return false;
		return true;
	}
}
