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
package example4Views.views;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SWTView extends ViewPart {
	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			ArrayList list = new ArrayList();
			list.addAll(Arrays.asList(new String[] {"One", "Two", "Three"}));
			list.add(new Integer(5));
			return list.toArray();
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if( obj instanceof String )
				return getText(obj);
			
			return "Integer: " + getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		
		public Image getImage(Object obj) {
			String image = obj instanceof String ? ISharedImages.IMG_OBJ_ELEMENT :
				ISharedImages.IMG_OBJ_FILE;
			
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(image);
		}
	}

	/**
	 * The constructor.
	 */
	public SWTView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		/*
		 * Our parent composite is always of the type "FillLayout"
		 * It will make whatever you put it fill up everything.
		 */
		 
		createGuiElements(parent);
		createContextMenu();
	}
	
	public void createGuiElements( Composite parent) {
		// We'll have two composites, one on the left, one right
		System.out.println(parent.getLayout());
		Composite left = new Composite(parent,SWT.BORDER);
		left.setLayout(new FormLayout());
		
		Composite right = new Composite(parent, SWT.BORDER);
		right.setLayout(new GridLayout(2,true));
		
		/*
		 *  Now we have to say how these composites fit into the FillLayout
		 *  For most layouts, we will require layoutData to know how to put 
		 *  the pieces together. FillLayout doesn't require this. it just
		 *  mashes everything together.
		 */
		
		// In left, we'll include a label and a viewer
		
		viewer = new TableViewer(left, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		
		Label leftLabel = new Label(left, SWT.NONE);
		leftLabel.setText("LEFT LABEL");
		
		/*
		 * We're putting viewer in left, so we need a FormData
		 */
		
		FormData viewerData = new FormData();
		viewerData.left = new FormAttachment(0,0);
		viewerData.top = new FormAttachment(0,0);
		viewerData.bottom = new FormAttachment(leftLabel,-5);
		viewerData.right = new FormAttachment(100,0);
		
		FormData leftLabelData = new FormData();
		leftLabelData.left = new FormAttachment(0,5);
		leftLabelData.right = new FormAttachment(100, 0);
		leftLabelData.bottom = new FormAttachment(100, -5);
		
		
		// now apply the data. viewer is a JFace element.
		// layout data works on swt elements, the underlying table.
		viewer.getTable().setLayoutData(viewerData);
		leftLabel.setLayoutData(leftLabelData);
		
		
		/*
		 * Now let's add stuff to the right half in a Grid Layout
		 */
		for( int i = 0; i < 6; i++ ) {
			Button b = new Button(right, SWT.NONE);
			b.setText("Button " + i);
			b.setLayoutData(new GridData());
		}

		
	}
	
	public void createContextMenu() {
		
		// creating the Menu and registering it
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(false);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
		
		// create our action
		Action act1 = new Action() { 
			public void run() {
				showMessage("Action1");
			}
		} ; 
		act1.setText("SWT Action 1");
		
		// add it
		menuMgr.add(act1) ;
		
		// required, for extensions
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		
	}


	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Sample View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}