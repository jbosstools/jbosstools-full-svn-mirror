/**
 * 
 */
package org.jboss.tools.smooks.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.smooks.ui.editors.ISaveListener;
import org.jboss.tools.smooks.ui.editors.SaveResult;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;

/**
 * @author Dart
 * 
 */
public class SmooksTextEdtor extends StructuredTextEditor {

	private List<ISaveListener> saveListenerList = new ArrayList<ISaveListener>();

	private Throwable error;
	private Composite errorComposite;
	private Label messageLabel;

	public SmooksTextEdtor(Throwable error) {
		super();
		this.setErrorMessage(error);
	}

	public SmooksTextEdtor() {
		this(null);
	}

	public void addSaveListener(ISaveListener listener) {
		if (listener != null)
			this.saveListenerList.add(listener);
	}
	
	public void removeSaveListener(ISaveListener listener){
		if (listener != null)
			this.saveListenerList.remove(listener);
	}
	
	public void cleanSaveListenerList(){
		saveListenerList.clear();
	}

	public void setErrorMessage(Throwable error) {
		if (error == null)
			return;
		this.error = error;
		while (this.error != null
				&& this.error instanceof InvocationTargetException) {
			this.error = ((InvocationTargetException) this.error)
					.getTargetException();
		}
		String errorMessage = null;
		if (error != null)
			errorMessage = error.getLocalizedMessage();
		if (errorMessage == null)
			errorMessage = Messages
					.getString("SmooksTextEdtor.UnKnownErrorMessage"); //$NON-NLS-1$
		if (messageLabel != null)
			messageLabel.setText(errorMessage);
	}

	public void setErrorMessage(String message) {
		if (message == null) {
			GridData gd = new GridData();
			gd.exclude = true;
			this.errorComposite.setLayoutData(gd);
		} else {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			errorComposite.setLayoutData(gd);
			messageLabel.setText(message);
		}
	}

	public void doSave(IProgressMonitor monitor) {
		SaveResult result = new SaveResult();
		result.setSourceEdtior(this);
		for (Iterator<ISaveListener> iterator = saveListenerList.iterator(); iterator.hasNext();) {
			ISaveListener l = (ISaveListener) iterator.next();
			l.preSave(result);
		}
		super.doSave(monitor);
		for (Iterator<ISaveListener> iterator = saveListenerList.iterator(); iterator.hasNext();) {
			ISaveListener l = (ISaveListener) iterator.next();
			l.endSave(result);
		}
	}

	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		parent.setLayout(gridLayout);
		errorComposite = new Composite(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		errorComposite.setLayoutData(gd);

		Label noticeLabel = new Label(errorComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		noticeLabel.setLayoutData(gd);
		noticeLabel.setText(Messages
				.getString("SmooksTextEdtor.NotifyTitleMessage")); //$NON-NLS-1$

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		errorComposite.setLayout(gl);

		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		Label imagelabel = new Label(errorComposite, SWT.NONE);
		imagelabel.setLayoutData(gd);
		imagelabel.setImage(SmooksUIActivator.getDefault().getImageRegistry()
				.get(SmooksGraphConstants.IMAGE_ERROR));

		messageLabel = new Label(errorComposite, SWT.NONE);
		String errorMessage = null;
		if (error != null)
			errorMessage = error.getLocalizedMessage();
		if (errorMessage == null)
			errorMessage = Messages
					.getString("SmooksTextEdtor.UnKnownErrorMessage"); //$NON-NLS-1$
		messageLabel.setText(errorMessage);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		messageLabel.setLayoutData(gd);

		if (error == null) {
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.exclude = true;
			errorComposite.setLayoutData(gd);
		}

		Composite textComposite = new Composite(parent, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		textComposite.setLayoutData(gd);
		textComposite.setLayout(new FillLayout());
		super.createPartControl(textComposite);
	}
}
