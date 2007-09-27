package org.jboss.ide.eclipse.ejb3.wizards.ui.wizards.pages;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.jboss.ide.eclipse.ejb3.wizards.core.classpath.EJB3ClasspathContainer;

public class JBossEJB3LibrariesPage extends JBossSelectionPage implements
		IClasspathContainerPage {
	
	public JBossEJB3LibrariesPage() {
		super();
		
	}
	
	public boolean finish() { return true; }

	public boolean isPageComplete () {
		return configuration != null;
	}
	
	public IClasspathEntry getSelection() {
		return JavaCore.newContainerEntry(new Path(EJB3ClasspathContainer.CONTAINER_ID), true);
	}

	public void setSelection(IClasspathEntry containerEntry) { }
	
}
