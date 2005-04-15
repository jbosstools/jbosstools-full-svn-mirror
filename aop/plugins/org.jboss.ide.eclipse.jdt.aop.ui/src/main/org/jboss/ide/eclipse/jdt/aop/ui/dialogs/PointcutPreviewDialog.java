/*
 * Created on Jan 21, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.model.AdvisedCollector;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;

/**
 * @author Marshall
 */
public class PointcutPreviewDialog extends Dialog {

	protected IJavaProject project;
	protected Text pointcutText;
	protected Label errorImage, errorLabel;
	protected TableViewer pointcutPreview;
	protected Button previewButton;
	protected String pointcut;
	protected ArrayList advisable;
	
	protected boolean preview;
	
	private ProgressBar previewProgress;
	
	public static final int PREVIEW_ID = -3000;
	
	public PointcutPreviewDialog (String pointcut, Shell shell, IJavaProject project, boolean preview)
	{
		super(shell);
		
		this.project = project;
		this.pointcut = pointcut;
		this.preview = preview;
		this.advisable = new ArrayList();
	}

	public String getPointcut () 
	{
		return pointcut;
	}
	
	public void setPreview (boolean preview)
	{
		this.preview = preview;
	}
	
	protected Control createDialogArea (Composite parent)
	{
		getShell().setText("Edit Pointcut...");
		
		Composite main = new Composite (parent, SWT.NONE);
		main.setLayout(new GridLayout(2, false));
		
		Label pointcutLabel = new Label(main, SWT.NONE);
		pointcutLabel.setText("Pointcut:");
		pointcutLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		
		pointcutText = new Text(main, SWT.BORDER);
		
		if (pointcut != null)
			pointcutText.setText(pointcut);
		
		pointcutText.addModifyListener(new ModifyListener () {
			public void modifyText(ModifyEvent e) {
				pointcut = pointcutText.getText();
			}
		});
		
		pointcutText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		previewProgress = new ProgressBar(main, SWT.SMOOTH);
		previewProgress.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
		previewProgress.setVisible(false);
		
		Composite errorComposite = new Composite(main, SWT.NONE);
		errorComposite.setLayout(new GridLayout(2, false));
		errorComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		errorImage = new Label(errorComposite, SWT.NONE);
		errorImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
		errorLabel = new Label(errorComposite, SWT.NONE);
		errorLabel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		errorImage.setVisible(false);
		errorLabel.setText("                             ");
		
		if (preview)
		{
			pointcutPreview = new TableViewer(main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			
			AdvisedLabelProvider provider = new AdvisedLabelProvider();
			pointcutPreview.setLabelProvider(provider);
			pointcutPreview.setContentProvider(provider);
			pointcutPreview.getTable().setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1));
			pointcutPreview.setInput(advisable);
		}
		
		return main;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		
		previewButton = createButton(parent, PREVIEW_ID, "Preview >>", false);
		previewButton.setText("Preview >>");
		previewButton.addSelectionListener(new SelectionListener () {
			boolean on = false;
			
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
	
			public void widgetSelected(SelectionEvent e) {
				previewPressed();
			}
		});
		setButtonLayoutData(previewButton);
		
	}
	
	protected void previewPressed ()
	{
		pointcutText.setEnabled(false);
		getButton(OK).setEnabled(false);
		getButton(CANCEL).setEnabled(false);
		previewButton.setEnabled(false);
		
		updatePreview();
	}
	
	private class AdvisedLabelProvider extends LabelProvider implements IStructuredContentProvider
	{
		private JavaUILabelProvider javaUILabelProviderDelegate;
		
		public AdvisedLabelProvider ()
		{
			javaUILabelProviderDelegate = new JavaUILabelProvider();
		}
		
		public Image getImage (Object element) {
			IAopAdvised advised = (IAopAdvised) element;
			return javaUILabelProviderDelegate.getImage(advised.getAdvisedElement());
		}
		
		public String getText (Object element) {
			IAopAdvised advised = (IAopAdvised) element;
			
			return AopUiPlugin.getDefault().getTreeLabel(advised.getAdvisedElement());
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
			{
				return ((List)inputElement).toArray();
			}
			else return new Object[0];
		}
	}
	
	protected void clearError ()
	{
		errorImage.setVisible(false);
		//errorLabel.setVisible(false);
		errorLabel.setText("");
		
		getButton(OK).setEnabled(true);
	}
	
	protected void showError (String error)
	{
		errorImage.setVisible(true);
		errorLabel.setVisible(true);
		
		errorLabel.setText(error);
		errorLabel.setToolTipText(error);
		getButton(OK).setEnabled(false);
		getButton(CANCEL).setEnabled(true);
		previewButton.setEnabled(true);
		previewProgress.setVisible(false);
		pointcutText.setEnabled(true);
		
		errorLabel.getParent().getParent().redraw();
	}
	
	private class PreviewCollector extends AdvisedCollector
	{
		
		public void beginTask (String typeName, final int numberOfAdvised) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					previewProgress.setVisible(true);
					previewProgress.setMinimum(0);
					previewProgress.setMaximum(numberOfAdvised);
					previewProgress.setSelection(0);
					previewProgress.setSize(previewProgress.getSize().x, 10);
					advisable.clear();
				}
			});
		}
		
		public void worked(final int work) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					previewProgress.setSelection(previewProgress.getSelection() + work);
				}
			});
		}
		
		public void collectAdvised(final IAopAdvised advised) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					advisable.add(advised);
				}
			});
		}
		
		public void done () {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					pointcutText.setEnabled(false);
					getButton(OK).setEnabled(false);
					getButton(CANCEL).setEnabled(false);
					previewButton.setEnabled(false);
					
					setReturnCode(PREVIEW_ID);
					close();
				}
			});
		}
		
		public void handleException(final Exception e) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					showError(e.getMessage());
				}
			});
		}
	}
	
	protected void updatePreview ()
	{
		try {
			clearError();
			pointcut = pointcutText.getText();
			
			JDTPointcutExpression expression = new JDTPointcutExpression (new PointcutExpression(null, pointcut));
			AopModel.instance().findAllAdvised(AopModel.instance().getRegisteredTypes(), expression, new PreviewCollector(), new NullProgressMonitor());
		} catch (ParseException e) {
			showError(e.getMessage());
		} catch (RuntimeException e) {
			showError(e.getMessage());
		}
	}
}
