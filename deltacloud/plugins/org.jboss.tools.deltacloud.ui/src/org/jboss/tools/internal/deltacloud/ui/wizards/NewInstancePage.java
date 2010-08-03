package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

public class NewInstancePage extends WizardPage {

	private final static String NAME = "NewInstance.name"; //$NON-NLS-1$
	private final static String DESCRIPTION = "NewInstance.desc"; //$NON-NLS-1$
	private final static String TITLE = "NewInstance.title"; //$NON-NLS-1$

	private DeltaCloud cloud;
	private DeltaCloudImage image;
	
	public NewInstancePage(DeltaCloud cloud, DeltaCloudImage image) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		this.image = image;
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setPageComplete(false);
	}
	
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);
		setControl(container);
	}

}
