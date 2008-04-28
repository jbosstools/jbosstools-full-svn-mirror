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
package example5bMultiPageEditor.editors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.impl.xs.dom.DOMParser;
import com.sun.org.apache.xml.internal.serialize.DOMSerializer;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import example5aCustomEditor.editors.XMLEditor;

public class TwoPageEditor extends MultiPageEditorPart {

	// editor on one page, viewer on the other
	private TableViewer viewer;
	private TextEditor editor;
	
	
	protected void createPages() {
	
		// create the pages
		createViewerPage();
		createSourcePage();
		updateTitle();
		
		// tell the viewer how to display data, then set the data
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(editor);
	}
	
	void createViewerPage() {
		viewer = new TableViewer(getContainer(), SWT.NONE);
		int index = addPage(viewer.getControl());
		setPageText(index, "Viewer");
		
		// creating the Menu and registering it
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(false);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
		
		// create our action
		Action act1 = new Action() { 
			public void run() {
				ViewContentProvider provider = (ViewContentProvider)TwoPageEditor.this.viewer.getContentProvider();
				IStructuredSelection selection = (IStructuredSelection)TwoPageEditor.this.viewer.getSelection();
				Node rootNode = provider.getRoot();
				if( selection.size() != 1 ) return;
				Node toRemove = (Node)selection.getFirstElement();
				rootNode.removeChild(toRemove);
				
				StringWriter writer = new StringWriter();
				Document tmpDoc = provider.getDoc();
				OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
				of.setIndent(1);
				of.setIndenting(true);
				XMLSerializer serializer = new XMLSerializer(writer, of);
				try {
					serializer.asDOMSerializer();
					serializer.serialize(tmpDoc);
					writer.close();
				
					String newXML = writer.getBuffer().toString();
					editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(newXML);
					viewer.refresh();
				} catch( Exception e ) {
					
				}
				
			}
		} ; 
		act1.setText("REMOVE");
		
		// add it
		menuMgr.add(act1) ;
		
		// required, for extensions
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

	}
	
	protected class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if( columnIndex == 0 ) 
				return ((Node)element).getNodeName();
			return "unknown";
		}

	}
	protected class ViewContentProvider implements IStructuredContentProvider {
		protected TextEditor ed;
		protected Document myDoc;
		protected Node root;

		public Object[] getElements(Object inputElement) {
			Object[] retval = new Object[0];

			String editorText =
				editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
			byte[] editorBytes = editorText.getBytes();
			InputStream stream = new ByteArrayInputStream(editorBytes);
			try {
				DOMParser parser = new DOMParser();
				InputSource source = new InputSource(stream);
				parser.parse(source);
				myDoc = parser.getDocument();
				NodeList list = myDoc.getChildNodes();
				
				root = list.item(0);
				list = root.getChildNodes();
				
				retval = new Object[list.getLength()];
				for( int i = 0; i < list.getLength(); i++ ) {
					retval[i] = list.item(i);
				}
			} catch( Exception e ) {
				e.printStackTrace();
			}
			return retval;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if( newInput instanceof TextEditor ) 
				this.ed = (TextEditor)newInput;
		}
		
		public Node getRoot() {
			return this.root;
		}
		public Document getDoc() {
			return this.myDoc;
		}
	}
	
	void createSourcePage() {
		try {
			editor = new XMLEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, "Source");
		} catch( PartInitException e ) {
			System.err.println("Error creating the part");
		}
	}
	
	protected void pageChange(int newPage) {
		if( newPage == 0 ) {
			// make it refresh this
			viewer.setInput(editor);
		}
	}

	void updateTitle() {
		IEditorInput input = getEditorInput();
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
	}

	
	public void doSave(IProgressMonitor monitor) {
		editor.doSave(monitor);
	}

	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	public boolean isSaveAsAllowed() {
		return true;	
	}
	



}
