package org.jboss.tools.vpe.xulrunner.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;

public class XulRunnerView extends ViewPart {

    private static final String INIT_URL = "about:buildconfig";
    private XulRunnerEditor xulrunnerEditor;

    @Override
    public void createPartControl(Composite parent) {
	try {
	    xulrunnerEditor = new XulRunnerEditor(parent);
	    xulrunnerEditor.setURL(INIT_URL);
	    xulrunnerEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
		    true, true));
	} catch (XulRunnerException e) {
	    // Ignore exception
	}
    }

    @Override
    public void setFocus() {
	xulrunnerEditor.setFocus();
    }

    public XulRunnerEditor getBrowser() {
	return xulrunnerEditor;
    }

}
