package org.jboss.tools.bpel.runtimes.ui.view.server;

import org.eclipse.bpel.runtimes.IRuntimesUIConstants;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.bpel.runtimes.RuntimesPlugin;
import org.jboss.tools.bpel.runtimes.ui.view.server.BPELModuleContentProvider.BPELVersionDeployment;

public class BPELModuleLabelProvider extends LabelProvider {
	public Image getImage(Object element) {
		
		return RuntimesPlugin.getPlugin().getImage(IRuntimesUIConstants.ICON_BPEL_FACET);
	}

	public String getText(Object element) {
		if( element instanceof BPELVersionDeployment ) {
			return new Path(((BPELVersionDeployment)element).getPath()).lastSegment();
		}
		return null;
	}
}
