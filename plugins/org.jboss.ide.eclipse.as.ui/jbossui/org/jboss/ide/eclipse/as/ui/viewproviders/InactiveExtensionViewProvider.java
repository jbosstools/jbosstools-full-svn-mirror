package org.jboss.ide.eclipse.as.ui.viewproviders;
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

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.jboss.ide.eclipse.as.core.util.ASDebug;
import org.jboss.ide.eclipse.as.ui.JBossServerUIPlugin;
import org.jboss.ide.eclipse.as.ui.JBossServerUIPlugin.ServerViewProvider;
import org.jboss.ide.eclipse.as.ui.views.JBossServerView;


public class InactiveExtensionViewProvider extends JBossServerViewExtension {

	private ITreeContentProvider contentProvider;
	private LabelProvider labelProvider;
	public InactiveExtensionViewProvider() {
		contentProvider = new InactiveContentProvider();
		labelProvider = new InactiveLabelProvider();
	}
	
	
	public class InactiveContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			if( parentElement == provider ) {
				ServerViewProvider[] allExtensions = JBossServerUIPlugin.getDefault().getAllServerViewProviders();
				ArrayList list = new ArrayList();
				for( int i = 0; i < allExtensions.length; i++ ) {
					if( !allExtensions[i].isEnabled()) {
						list.add(allExtensions[i]);
					}
				}
				ServerViewProvider[] retval = new ServerViewProvider[list.size()];
				list.toArray(retval);
				return retval;
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0 ? true : false;
		}

		public Object[] getElements(Object inputElement) {
			// Unused
			return null;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// Do nothing... don't care about which server is selected
		}
		
	}
	
	public class InactiveLabelProvider extends LabelProvider {
	    public Image getImage(Object element) {
	    	if( element instanceof ServerViewProvider ) {
	    		return ((ServerViewProvider)element).getImage();
	    	}
	        return null;
	    }

	    public String getText(Object element) {
	    	if( element instanceof ServerViewProvider ) {
	    		return ((ServerViewProvider)element).getName();
	    	}
	        return element == null ? "" : element.toString();
	    }

	}
	
	public void fillContextMenu(Shell shell, IMenuManager menu, Object selection) {
		final Object selected = selection;
		if( selection instanceof ServerViewProvider && selection != this.provider) {
			Action act = new Action() {
				public void run() {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							if( selected instanceof ServerViewProvider) {
								((ServerViewProvider)selected).setEnabled(true);
								((ServerViewProvider)selected).getDelegate().getContentProvider().
									inputChanged(JBossServerView.getDefault().getJbViewer(), null, JBossServerView.getDefault().getSelectedServer());
								
								try {
									JBossServerView.getDefault().refreshJBTree(null);
								} catch(Exception e) {
								}
							}
						} 
					} );
				}
			};
			act.setText("Re-Enable Category");
			menu.add(act);
		}
	}

	public ITreeContentProvider getContentProvider() {
		return contentProvider;
	}

	public LabelProvider getLabelProvider() {
		return labelProvider;
	}

	public IPropertySheetPage getPropertySheetPage() {
		return null;
	}

}
