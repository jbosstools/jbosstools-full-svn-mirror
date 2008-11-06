/**
 * 
 */
package org.jboss.tools.smooks.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.editors.text.TextEditor;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;

/**
 * @author Dart
 * 
 */
public class SmooksTextEdtor extends TextEditor {

	private Throwable error;

	public SmooksTextEdtor(Throwable error) {
		super();
		Assert.isNotNull(error);
		this.error = error;
		while(this.error != null && this.error instanceof InvocationTargetException){
			this.error = ((InvocationTargetException)this.error).getTargetException();
		}
	}

	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		parent.setLayout(gridLayout);
		Composite errorComposite = new Composite(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		errorComposite.setLayoutData(gd);

		Label noticeLabel = new Label(errorComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		noticeLabel.setLayoutData(gd);
		noticeLabel
				.setText(Messages.getString("SmooksTextEdtor.NotifyTitleMessage")); //$NON-NLS-1$

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		errorComposite.setLayout(gl);

		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		Label imagelabel = new Label(errorComposite, SWT.NONE);
		imagelabel.setLayoutData(gd);
		imagelabel.setImage(SmooksUIActivator.getDefault().getImageRegistry()
				.get(SmooksGraphConstants.IMAGE_ERROR));

	    Label messageLabel = new Label(errorComposite, SWT.NONE);
	    String errorMessage = error.getLocalizedMessage();
	    if(errorMessage == null ) errorMessage = Messages.getString("SmooksTextEdtor.UnKnownErrorMessage"); //$NON-NLS-1$
		messageLabel.setText(errorMessage);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		messageLabel.setLayoutData(gd);

		Composite textComposite = new Composite(parent, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		textComposite.setLayoutData(gd);
		textComposite.setLayout(new FillLayout());
		super.createPartControl(textComposite);
	}
}
