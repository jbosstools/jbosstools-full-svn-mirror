package org.jboss.tools.bpel.runtimes.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.project.facet.ui.IFacetWizardPage;
import org.eclipse.wst.web.ui.internal.wizards.DataModelFacetInstallPage;
import org.jboss.tools.bpel.runtimes.IBPELModuleFacetConstants;

public class BPELFacetInstallPage extends DataModelFacetInstallPage implements
		IFacetWizardPage {

	private Label contentDirLabel;
	private Text contentDir;
	public BPELFacetInstallPage() {
		super("Test BPEL");
		setTitle("BPEL Title");
		setDescription("BPEL Description");
	}

	@Override
	protected String[] getValidationPropertyNames() {
		return new String[]{IBPELModuleFacetConstants.BPEL_CONTENT_FOLDER};
	}

	@Override
	protected Composite createTopLevelComposite(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		this.contentDirLabel = new Label(composite, SWT.NONE);
		this.contentDirLabel.setText("Content Folder");
		this.contentDirLabel.setLayoutData(new GridData());

		this.contentDir = new Text(composite, SWT.BORDER);
		this.contentDir.setLayoutData(gdhfill());
		this.contentDir.setData("label", this.contentDirLabel); //$NON-NLS-1$
		this.synchHelper.synchText(contentDir, IBPELModuleFacetConstants.BPEL_CONTENT_FOLDER, null);
		this.contentDir.setText(IBPELModuleFacetConstants.BPEL_CONTENT_DEFAULT_FOLDER);
		new Label(composite, SWT.NONE); // pad
		return composite;
	}

}
