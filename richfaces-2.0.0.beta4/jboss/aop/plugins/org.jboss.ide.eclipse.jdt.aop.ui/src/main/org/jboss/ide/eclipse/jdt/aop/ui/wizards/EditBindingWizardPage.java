/*
 * Created on Jan 21, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog;
import org.jboss.ide.eclipse.jdt.aop.ui.util.AdvisorDialogUtil;

/**
 * @author Marshall
 */
public class EditBindingWizardPage extends WizardPage {

	protected Text pointcutText;
	protected Button editPointcutButton;
	protected TableViewer interceptorList;
	protected Button addInterceptorButton, removeInterceptorButton, moveInterceptorUpButton, moveInterceptorDownButton;
	protected TableViewer adviceList;
	protected Button addAdviceButton, removeAdviceButton, moveAdviceUpButton, moveAdviceDownButton;
	protected ButtonListener buttonListener;
	protected Binding binding;
	protected AopDescriptor descriptor;
	
	/**
	 * @param pageName
	 */
	public EditBindingWizardPage (Binding binding, AopDescriptor descriptor) {
		super(binding == null ? "Create a new JBossAOP Binding" : "Edit a JBossAOP Binding");
		
		setTitle(binding == null ? "Create a new JBossAOP Binding" : "Edit a JBossAOP Binding");
		setMessage(binding == null ? "Create a new JBossAOP Pointcut Binding" : "Edit an existing JBossAOP Pointcut Binding");
		
		this.binding = binding;
		this.descriptor = descriptor;
	}
	
	

	private class ButtonListener implements SelectionListener
	{
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
		public void widgetSelected(SelectionEvent e) {
			if (e.getSource().equals(editPointcutButton))
				editPointcutPressed();
			else if (e.getSource().equals(addInterceptorButton))
				addInterceptorPressed();
			else if (e.getSource().equals(removeInterceptorButton))
				removeInterceptorPressed();
			else if (e.getSource().equals(moveInterceptorUpButton))
				moveInterceptorUpPressed();
			else if (e.getSource().equals(moveInterceptorDownButton))
				moveInterceptorDownPressed();
			else if (e.getSource().equals(addAdviceButton))
				addAdvicePressed();
			else if (e.getSource().equals(removeAdviceButton))
				removeAdvicePressed();
			else if (e.getSource().equals(moveAdviceUpButton))
				moveAdviceUpPressed();
			else if (e.getSource().equals(moveAdviceDownButton))
				moveAdviceDownPressed();
		}
	}
	
	private void fillGrid (Control control)
	{
		fillGrid (control, true);
	}
	
	private void fillGrid (Control control, boolean fillVertical)
	{
		control.setLayoutData(new GridData(GridData.FILL, fillVertical ? GridData.FILL : GridData.CENTER, true, fillVertical));
	}
	
	private void topAlignInGrid (Control control)
	{
		control.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
	}
	
	private void gridButton (Control control)
	{
		control.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
	}
	
	public void createControl(Composite parent)
	{
		buttonListener = new ButtonListener();
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(3, false));
		
		new Label(main, SWT.NONE).setText("Pointcut:");
		pointcutText = new Text(main, SWT.BORDER);
		pointcutText.setEnabled(false);
		fillGrid(pointcutText, false);
		
		if (binding != null)
			pointcutText.setText(binding.getPointcut());
		
		Composite editPointcutComposite = new Composite(main, SWT.NONE);
		editPointcutComposite.setLayout(new GridLayout(1, true));
		editPointcutComposite.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		
		editPointcutButton = new Button(editPointcutComposite, SWT.PUSH);
		editPointcutButton.setText("Edit...");
		editPointcutButton.addSelectionListener(buttonListener);
		editPointcutButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		createInterceptorControls (main);
		createAdviceControls (main);
		
		setControl(main);
	}
	
	private class InterceptorProvider extends LabelProvider implements IStructuredContentProvider
	{
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
			{
				List list = (List) inputElement;
				return list.toArray();
			}
			
			return null;
		}
		
		public Image getImage(Object element) {
			return AopSharedImages.getImage(AopSharedImages.IMG_INTERCEPTOR);
		}

		public String getText(Object element) {
			if (element instanceof Interceptor)
			{
				Interceptor interceptor = (Interceptor) element;
				return interceptor.getClazz();
			}
			else return element.toString();
		}
	}
	
	protected void createInterceptorControls (Composite main)
	{
		Label label = new Label(main, SWT.NONE);
		label.setText("Interceptors:");
		topAlignInGrid(label);
		
		interceptorList = new TableViewer(main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		InterceptorProvider provider = new InterceptorProvider();
		interceptorList.setLabelProvider(provider);
		interceptorList.setContentProvider(provider);
		fillGrid(interceptorList.getTable());
		interceptorList.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				boolean empty = event.getSelection().isEmpty();
				
				removeInterceptorButton.setEnabled(!empty);				
				moveInterceptorUpButton.setEnabled(interceptorList.getTable().getSelectionIndex() > 0);
				moveInterceptorDownButton.setEnabled(interceptorList.getTable().getSelectionIndex() < interceptorList.getTable().getItemCount() - 1);
			}
		});
		
		Composite interceptorButtons = new Composite(main, SWT.NONE);
		interceptorButtons.setLayout(new GridLayout(1, true));
		interceptorButtons.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		
		addInterceptorButton = new Button(interceptorButtons, SWT.PUSH);
		addInterceptorButton.setText("Add...");
		addInterceptorButton.addSelectionListener(buttonListener);
		gridButton(addInterceptorButton);
		addInterceptorButton.setEnabled(binding != null);
		
		removeInterceptorButton = new Button(interceptorButtons, SWT.PUSH);
		removeInterceptorButton.setText("Remove");
		removeInterceptorButton.addSelectionListener(buttonListener);
		gridButton(removeInterceptorButton);
		removeInterceptorButton.setEnabled(false);
		
		moveInterceptorUpButton = new Button(interceptorButtons, SWT.PUSH);
		moveInterceptorUpButton.setText("Move Up");
		moveInterceptorUpButton.addSelectionListener(buttonListener);
		gridButton(moveInterceptorUpButton);
		moveInterceptorUpButton.setEnabled(false);
		
		moveInterceptorDownButton = new Button(interceptorButtons, SWT.PUSH);
		moveInterceptorDownButton.setText("Move Down");
		moveInterceptorDownButton.addSelectionListener(buttonListener);
		gridButton(moveInterceptorDownButton);
		moveInterceptorDownButton.setEnabled(false);
		
		if (binding != null)
		{
			for (Iterator iter = binding.getInterceptorRefs().iterator(); iter.hasNext(); )
			{
				interceptorList.add(iter.next());
			}
			for (Iterator iter = binding.getInterceptors().iterator(); iter.hasNext(); )
			{
				interceptorList.add(iter.next());
			}
		}
	}
	
	private class AdviceProvider extends LabelProvider implements IStructuredContentProvider
	{
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
			{
				List list = (List) inputElement;
				return list.toArray();
			}
			
			return null;
		}
		
		public Image getImage(Object element) {
			return AopSharedImages.getImage(AopSharedImages.IMG_ADVICE);
		}
		
		public String getText(Object element) {
			if (element instanceof Advice)
			{
				Advice advice = (Advice) element;
				return advice.getName() + " : " + advice.getAspect();
			}
			else return element.toString();
		}
	}
	
	protected void createAdviceControls (Composite main)
	{
		Label label = new Label(main, SWT.NONE);
		label.setText("Advice:");
		topAlignInGrid(label);
		
		adviceList = new TableViewer(main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		AdviceProvider provider = new AdviceProvider();
		adviceList.setLabelProvider(provider);
		adviceList.setContentProvider(provider);
		fillGrid(adviceList.getTable());
		adviceList.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				boolean empty = event.getSelection().isEmpty();
				
				removeAdviceButton.setEnabled(!empty);
				moveAdviceUpButton.setEnabled(adviceList.getTable().getSelectionIndex() > 0);
				moveAdviceDownButton.setEnabled(adviceList.getTable().getSelectionIndex() < adviceList.getTable().getItemCount() - 1);
			}
		});
		
		Composite adviceButtons = new Composite(main, SWT.NONE);
		adviceButtons.setLayout(new GridLayout(1, true));
		adviceButtons.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		
		addAdviceButton = new Button(adviceButtons, SWT.PUSH);
		addAdviceButton.setText("Add...");
		addAdviceButton.addSelectionListener(buttonListener);
		gridButton(addAdviceButton);
		addAdviceButton.setEnabled(binding != null);
		
		removeAdviceButton = new Button(adviceButtons, SWT.PUSH);
		removeAdviceButton.setText("Remove");
		removeAdviceButton.addSelectionListener(buttonListener);
		gridButton(removeAdviceButton);
		removeAdviceButton.setEnabled(false);
		
		moveAdviceUpButton = new Button(adviceButtons, SWT.PUSH);
		moveAdviceUpButton.setText("Move Up");
		moveAdviceUpButton.addSelectionListener(buttonListener);
		gridButton(moveAdviceUpButton);
		moveAdviceUpButton.setEnabled(false);
		
		moveAdviceDownButton = new Button(adviceButtons, SWT.PUSH);
		moveAdviceDownButton.setText("Move Down");
		moveAdviceDownButton.addSelectionListener(buttonListener);
		gridButton(moveAdviceDownButton);
		moveAdviceDownButton.setEnabled(false);
		
		if (binding != null)
		{
			for (Iterator iter = binding.getAdvised().iterator(); iter.hasNext(); )
			{
				adviceList.add(iter.next());
			}
		}
	}
	
	protected void editPointcutPressed ()
	{
		PointcutPreviewDialog previewDialog = new PointcutPreviewDialog(pointcutText.getText(), getShell(), AopCorePlugin.getCurrentJavaProject(), false);
		
		int response = -1;
		do {
			previewDialog.create();
			response = previewDialog.open();
		
			if (response == PointcutPreviewDialog.PREVIEW_ID)
			{
				previewDialog.setPreview(true);
			}
		} while (response == PointcutPreviewDialog.PREVIEW_ID);
		
		if (response == Dialog.OK)
		{
			String pointcut = previewDialog.getPointcut();
			pointcutText.setText(pointcut);
			
			addInterceptorButton.setEnabled(pointcut != null && pointcut.length() > 0);
			addAdviceButton.setEnabled(pointcut != null && pointcut.length() > 0);
			if (pointcut != null && pointcut.length() > 0)
			{
				if (binding == null)
				{
					binding = descriptor.findBinding(pointcut);
				}
				
				binding.setPointcut(pointcut);
			}
		}
	}
	
	private Interceptor getSelectedInterceptor ()
	{
		IStructuredSelection selection = (IStructuredSelection) interceptorList.getSelection();
		Object element = selection.getFirstElement();
		if (element instanceof Interceptor)
		{
			Interceptor interceptor = (Interceptor) element;
			return interceptor;
		}
		
		return null;
	}
	
	private InterceptorRef getSelectedInterceptorRef ()
	{
		IStructuredSelection selection = (IStructuredSelection) interceptorList.getSelection();
		Object element = selection.getFirstElement();
		if (element instanceof InterceptorRef)
		{
			InterceptorRef interceptor = (InterceptorRef) element;
			return interceptor;
		}
		
		return null;
	}
	
	private void addInterceptorPressed() {
		ArrayList interceptors = AdvisorDialogUtil.openInterceptorDialog(getShell());
		for (Iterator iter = interceptors.iterator(); iter.hasNext(); )
		{
			IType type = (IType) iter.next();
			Interceptor interceptor = descriptor.bindInterceptor(binding.getPointcut(), type.getFullyQualifiedName());
			
			interceptorList.add(interceptor);
		}
	}

	private void removeInterceptorPressed() {
		Interceptor interceptor = getSelectedInterceptor();
		if (interceptor != null)
		{
			binding.getInterceptors().remove(interceptor);
			interceptorList.remove(interceptor);
		}
		
		InterceptorRef interceptorRef = getSelectedInterceptorRef();
		if (interceptorRef != null)
		{
			binding.getInterceptorRefs().remove(interceptorRef);
			interceptorList.remove(interceptorRef);
		}
	}

	private void moveInterceptorUpPressed() {
		Interceptor interceptor = getSelectedInterceptor();
		if (interceptor != null)
		{
			int position = interceptorList.getTable().getSelectionIndex();
			if (position - 1 >= 0)
			{
				interceptorList.remove(interceptor);
				interceptorList.insert(interceptor, position-1);
				interceptorList.getTable().select(position-1);
				
				int index = binding.getInterceptors().indexOf(interceptor);
				binding.getInterceptors().remove(index);
				binding.getInterceptors().add(index-1, interceptor);
			}
		}
		
		InterceptorRef interceptorRef = getSelectedInterceptorRef();
		if (interceptorRef != null)
		{
			int position = interceptorList.getTable().getSelectionIndex();
			if (position - 1 >= 0)
			{
				interceptorList.remove(interceptorRef);
				interceptorList.insert(interceptorRef, position-1);
				interceptorList.getTable().select(position-1);
				
				int index = binding.getInterceptorRefs().indexOf(interceptor);
				binding.getInterceptorRefs().remove(index);
				binding.getInterceptorRefs().add(index-1, interceptor);
			}
		}
	}

	private void moveInterceptorDownPressed() {
		Interceptor interceptor = getSelectedInterceptor();
		if (interceptor != null)
		{
			int position = interceptorList.getTable().getSelectionIndex();
			if (position + 1 < interceptorList.getTable().getItemCount())
			{
				interceptorList.remove(interceptor);
				interceptorList.insert(interceptor, position+1);
				interceptorList.getTable().select(position + 1);
				
				int index = binding.getInterceptors().indexOf(interceptor);
				binding.getInterceptors().remove(index);
				binding.getInterceptors().add(index+1, interceptor);
			}
		}
		
		InterceptorRef interceptorRef = getSelectedInterceptorRef();
		if (interceptorRef != null)
		{
			int position = interceptorList.getTable().getSelectionIndex();
			if (position + 1 < interceptorList.getTable().getItemCount())
			{
				interceptorList.remove(interceptorRef);
				interceptorList.insert(interceptorRef, position+1);
				interceptorList.getTable().select(position+1);
				
				int index = binding.getInterceptorRefs().indexOf(interceptor);
				binding.getInterceptorRefs().remove(index);
				binding.getInterceptorRefs().add(index+1, interceptor);
			}
		}
	}

	private Advice getSelectedAdvice ()
	{
		IStructuredSelection selection = (IStructuredSelection) adviceList.getSelection();
		Object element = selection.getFirstElement();
		if (element instanceof Advice)
		{
			Advice advice = (Advice) element;
			return advice;
		}
		
		return null;
	}
		
	private void addAdvicePressed() {
		IMethod adviceMethods[] = AdvisorDialogUtil.openAdviceDialog(binding, getShell(), new NullProgressMonitor());
		for (int i = 0; i < adviceMethods.length; i++)
		{
			Advice advice = descriptor.bindAdvice(binding.getPointcut(), adviceMethods[i].getDeclaringType().getFullyQualifiedName(), adviceMethods[i].getElementName());
			
			adviceList.add(advice);
		}
	}

	private void removeAdvicePressed()
	{
		Advice advice = getSelectedAdvice();
		if (advice != null)
		{
			binding.getAdvised().remove(advice);
			adviceList.remove(advice);
		}
	}

	private void moveAdviceUpPressed() {
		Advice advice = getSelectedAdvice();
		if (advice != null)
		{
			int position = adviceList.getTable().getSelectionIndex();
			if (position - 1 >= 0)
			{
				adviceList.remove(advice);
				adviceList.insert(advice, position-1);
				adviceList.getTable().select(position - 1);
				
				int index = binding.getAdvised().indexOf(advice);
				binding.getAdvised().remove(index);
				binding.getAdvised().add(index-1, advice);
			}
		}
	}	

	private void moveAdviceDownPressed() {
		Advice advice = getSelectedAdvice();
		if (advice != null)
		{
			int position = adviceList.getTable().getSelectionIndex();
			if (position + 1 < adviceList.getTable().getItemCount())
			{
				adviceList.remove(advice);
				adviceList.insert(advice, position+1);
				adviceList.getTable().select(position + 1);
				
				int index = binding.getAdvised().indexOf(advice);
				binding.getAdvised().remove(index);
				binding.getAdvised().add(index+1, advice);
			}
		}
	}
}
