/**
 * 
 */
package org.jboss.tools.smooks.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProviderExtension;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.smooks.ui.editors.ISaveListener;
import org.jboss.tools.smooks.ui.editors.SaveResult;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;

/**
 * @author Dart
 * 
 */
public class SmooksTextEdtor extends StructuredTextEditor implements
		IAnalyzeListener {

	private List<ISaveListener> saveListenerList = new ArrayList<ISaveListener>();

	private Throwable error;
	private Composite errorComposite;
	private Label messageLabel;

	private String errorMessage;

	private boolean dirty = false;

	public boolean isDirty() {
		if (!dirty)
			return false;
		return super.isDirty();
	}
	
	
	public void editorInputChanged(IEditorInput input){
		IDocumentProvider provider = getDocumentProvider();
		try {
			if (provider instanceof IDocumentProviderExtension) {
				IDocumentProviderExtension extension= (IDocumentProviderExtension) provider;
				extension.synchronize(input);
			} else {
				doSetInput(input);
			}
		} catch (CoreException x) {
		}
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public SmooksTextEdtor(Throwable error) {
		super();
		this.setErrorThrowable(error);
	}

	public SmooksTextEdtor() {
		this(null);
	}

	public void addSaveListener(ISaveListener listener) {
		if (listener != null)
			this.saveListenerList.add(listener);
	}

	public void removeSaveListener(ISaveListener listener) {
		if (listener != null)
			this.saveListenerList.remove(listener);
	}

	public void cleanSaveListenerList() {
		saveListenerList.clear();
	}

	public void setErrorThrowable(Throwable error) {
		if (error == null) {
			setErrorMessage(null);
			return;
		}
		this.error = error;
		while (this.error != null
				&& this.error instanceof InvocationTargetException) {
			this.error = ((InvocationTargetException) this.error)
					.getTargetException();
		}
		String errorMessage = null;
		if (this.error != null)
			errorMessage = this.error.getLocalizedMessage();
		if (errorMessage == null)
			errorMessage = Messages
					.getString("SmooksTextEdtor.UnKnownErrorMessage"); //$NON-NLS-1$
		setErrorMessage(errorMessage);
	}

	public void setErrorMessage(String message) {
		boolean flag = false;
		if (errorComposite == null || errorComposite.isDisposed()
				|| messageLabel == null || messageLabel.isDisposed()) {
			return;
		}
		errorMessage = message;
		if (message == null) {
			GridData gd = new GridData();
			if (flag) {
				for (int i = 70; i >= 0; i--) {
					gd.heightHint = i;
					if (gd.heightHint <= 0) {
						gd.heightHint = 0;
						errorComposite.setVisible(false);
						gd.exclude = true;
					}
					this.errorComposite.setLayoutData(gd);
					Composite parent = errorComposite.getParent();
					parent.layout();
				}
			} else {
				gd.heightHint = 0;
				errorComposite.setVisible(false);
				gd.exclude = true;
				this.errorComposite.setLayoutData(gd);
				Composite parent = errorComposite.getParent();
				parent.layout();
			}
		} else {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			errorComposite.setVisible(true);
			messageLabel.setText(message);
			if (flag) {
				for (int i = 0; i < 70; i++) {
					gd.heightHint = i;
					errorComposite.setLayoutData(gd);
					Composite parent = errorComposite.getParent();
					parent.layout();
				}
			} else {
				errorComposite.setLayoutData(gd);
				Composite parent = errorComposite.getParent();
				parent.layout();
			}

		}
	}

	public void doSave(IProgressMonitor monitor) {
		SaveResult result = new SaveResult();
		result.setSourceEdtior(this);
		for (Iterator<ISaveListener> iterator = saveListenerList.iterator(); iterator
				.hasNext();) {
			ISaveListener l = (ISaveListener) iterator.next();
			l.preSave(result);
		}
		super.doSave(monitor);
		for (Iterator<ISaveListener> iterator = saveListenerList.iterator(); iterator
				.hasNext();) {
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
		getTextViewer().getDocument().addDocumentListener(new IDocumentListener(){

			public void documentAboutToBeChanged(DocumentEvent event) {
				setDirty(true);
			}

			public void documentChanged(DocumentEvent event) {
			}
			
		});
	}

	public void endAnalyze(AnalyzeResult result) {
		if (result.getSourceEdtior() instanceof SmooksGraphicalFormPage) {
			setErrorThrowable(result.getError());
		}
	}
}
