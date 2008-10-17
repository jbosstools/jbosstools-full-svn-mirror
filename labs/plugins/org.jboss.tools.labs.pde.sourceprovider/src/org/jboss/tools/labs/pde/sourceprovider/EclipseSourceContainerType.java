package org.jboss.tools.labs.pde.sourceprovider;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourceContainerType;
import org.eclipse.debug.core.sourcelookup.ISourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.containers.AbstractSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.AbstractSourceContainerTypeDelegate;
import org.eclipse.debug.core.sourcelookup.containers.ExternalArchiveSourceContainer;
import org.eclipse.debug.ui.sourcelookup.AbstractSourceContainerBrowser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class EclipseSourceContainerType extends AbstractSourceContainerTypeDelegate {

	public ISourceContainer createSourceContainer(String memento)
			throws CoreException {
		Node node = parseDocument(memento );
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element)node;
			if ("eclipseHome".equals(element.getNodeName())) { //$NON-NLS-1$
				String string = element.getAttribute("path"); //$NON-NLS-1$
				if (string == null || string.length() == 0) {
					abort("Eclipse Home Not Found", null); 
				}
				return new EclipseSourceContainer(new File(string));
			} 
			abort("Error unserializing memento", null); 
		}
		abort("Error unserializing memento", null); 
		return null;
	}

	public String getMemento(ISourceContainer container2) throws CoreException {
		EclipseSourceContainer container = (EclipseSourceContainer) container2;
		Document document = newDocument();
		Element element = document.createElement("eclipseHome"); //$NON-NLS-1$
		element.setAttribute("path", container.getFile().getAbsolutePath().toString()); //$NON-NLS-1$
		document.appendChild(element);
		return serializeDocument(document);
	}
	
	public static class EclipseSourceContainer extends AbstractSourceContainer {
		public static final String TYPE_ID = "org.jboss.tools.labs.pde.sourceprovider.eclipseSourceContainerType";
		protected File root;
		protected HashMap<String,ExternalArchiveSourceContainer> pathToContainer;
		public EclipseSourceContainer(File f) {
			this.root = f;
			pathToContainer = new HashMap<String,ExternalArchiveSourceContainer>();				
			search(root);	
		}
		
		protected void search(File f) {
			// recursively seek src.zip
			File[] kids = f.listFiles();
			for( int i = 0; i < kids.length; i++ ) {
				if( kids[i].isDirectory()) {
					search(kids[i]);
				} else if( kids[i].getName().endsWith("src.zip")) {
					// add the match
					pathToContainer.put(kids[i].toString(), new ExternalArchiveSourceContainer(kids[i].getAbsolutePath(), true));
				}
			}
		}
		
		public Object[] findSourceElements(String name) throws CoreException {
			Iterator<ExternalArchiveSourceContainer> i = pathToContainer.values().iterator();
			ExternalArchiveSourceContainer c;
			Object[] results;
			while(i.hasNext()) {
				c = i.next();
				results = c.findSourceElements(name);
				if( results.length > 0 ) 
					return results;
			}
			return new Object[0];
		}

		public String getName() {
			return "Eclipse Installation " + root.getAbsolutePath().toString();
		}

		public ISourceContainerType getType() {
			return getSourceContainerType(TYPE_ID);
		}
		
		public File getFile() {
			return root;
		}
	}

	public static class EclipseSourceContainerBrowser extends AbstractSourceContainerBrowser {
		/* (non-Javadoc)
		 * @see org.eclipse.debug.internal.ui.sourcelookup.ISourceContainerBrowser#addSourceContainers(org.eclipse.swt.widgets.Shell, org.eclipse.debug.core.sourcelookup.ISourceLookupDirector)
		 */
		public ISourceContainer[] addSourceContainers(Shell shell, ISourceLookupDirector director) {
			DirectoryDialog d = new DirectoryDialog(shell, SWT.OPEN);
			String s = d.open();
			if( s != null ) {
				IPath p = new Path(s);
				if( p.append("plugins").toFile().exists()) {
					return new ISourceContainer[] {new EclipseSourceContainer(p.toFile())};
				}
			}
			return new ISourceContainer[0];
		}
	}
}
