package org.jboss.tools.bpel.runtimes.ui.view.server;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.bpel.runtimes.ui.view.server.BPELModuleContentProvider.BPELVersionDeployment;

public class BPELModuleLabelProvider extends LabelProvider {
	public Image getImage(Object element) {
		// TODO Add an icon 
		return null;
	}

	public String getText(Object element) {
		if( element instanceof BPELVersionDeployment ) {
			return new Path(((BPELVersionDeployment)element).getPath()).lastSegment();
		}
		return element == null ? "" : element.toString();//$NON-NLS-1$
	}
}
