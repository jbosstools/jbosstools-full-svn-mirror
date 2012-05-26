package org.jboss.tools.portlet.ui.internal.libprov;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.Messages;

public class JSFPortletServerRuntimeProviderInstallPanel extends AbstractPortletProviderInstallPanel {

	@Override
	protected void addMessage(Composite composite) {
		Link link = new Link( composite, SWT.WRAP );
	    GridData data = new GridData( SWT.FILL, SWT.BEGINNING, true, false );
	    data.widthHint = 300;
	    link.setLayoutData( data );
	    link.setText( Messages.JSFPortletServerRuntimeProviderInstallPanel_The_targeted_runtime_contains_a_portlet_library );
	}

	@Override
	protected void addPortletBridgeGroup(Composite composite, boolean isEPP) {
	}
	
	@Override
	protected List<String> getRichfacesTypes() {
		List<String> types = new ArrayList<String>();
		types.add(IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES);
		return types;
	}
}
