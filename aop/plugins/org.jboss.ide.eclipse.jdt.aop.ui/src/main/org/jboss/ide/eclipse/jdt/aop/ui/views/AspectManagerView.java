/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.views;

import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jboss.aop.AspectManager;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aspect;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Binding;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Interceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.InterceptorRef;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.ui.AopSharedImages;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;
import org.jboss.ide.eclipse.jdt.aop.ui.actions.ApplyAdviceAction;
import org.jboss.ide.eclipse.jdt.aop.ui.actions.ApplyInterceptorAction;
import org.jboss.ide.eclipse.jdt.aop.ui.actions.CreateNewNamedPointcutAction;
import org.jboss.ide.eclipse.jdt.aop.ui.actions.ModifyNamedPointcutAction;
import org.jboss.ide.eclipse.jdt.aop.ui.util.JumpToCodeUtil;
import org.jboss.ide.eclipse.jdt.aop.ui.views.providers.AspectManagerContentProvider;
import org.jboss.ide.eclipse.jdt.aop.ui.views.providers.AspectManagerLabelProvider;
import org.jboss.ide.eclipse.jdt.aop.ui.wizards.EditBindingWizard;

/**
 * @author Marshall
 */
public class AspectManagerView extends ViewPart {

	private static AspectManagerView _instance;
	
    private TreeViewer treeViewer;
    private AspectManagerContentProvider contentProvider;
    private AspectManagerLabelProvider labelProvider;
    private ISelection currentSelection;
    private Action createAdviceAction, createAspectAction, createPointcutAction, 
    				goToAdviceAction, showAspectAction, showInterceptorAction, 
    				applyAdviceAction, applyInterceptorsAction,
    				createBindingAction, removeInterceptorAction, 
    				removeAdviceAction, removeInterceptorRefAction, 
    				removeBindingAction, removeNamedPointcutAction, 
    				goToAction, editBindingAction, editPointcutAction;
    
    public AspectManagerView ()
    {
    	_instance = this;
    }
    
    public void createPartControl(Composite parent) {
        treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        
        treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

        contentProvider = new AspectManagerContentProvider();
        treeViewer.setContentProvider(contentProvider);

        labelProvider = new AspectManagerLabelProvider();
        treeViewer.setLabelProvider(labelProvider);

        treeViewer.addDoubleClickListener(JumpToCodeUtil.getDoubleClickListener());
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				currentSelection = event.getSelection();
			}
        });
        createActions();
        createContextMenu();
        
        // initialize

    	IJavaProject project = AopCorePlugin.getCurrentJavaProject();
    	if (project != null)
    	{
    		setDescriptor(AopCorePlugin.getDefault().getDefaultDescriptor(project));
    		if (AdvisedMembersView.instance() != null)
    		    AdvisedMembersView.instance().refresh();
    	}
    }

    private void createActions ()
    {
    	createAdviceAction = new Action() {
    		public void run () {
    			createAdvice((Aspect) ((IStructuredSelection) currentSelection).getFirstElement());
    		}
    	};
    	createAdviceAction.setText("Create Advice");
    	createAdviceAction.setToolTipText("Create a new advice method under this Aspect.");
    	createAdviceAction.setImageDescriptor(AopSharedImages.getImageDescriptor(AopSharedImages.IMG_ADVICE));
    	
    	createPointcutAction = new CreateNewNamedPointcutAction(treeViewer, getSite().getShell());
    	createPointcutAction.setText("Create Pointcut");
    	createPointcutAction.setToolTipText("Create a new named Pointcut.");
    	createPointcutAction.setImageDescriptor(AopSharedImages.getImageDescriptor(AopSharedImages.IMG_POINTCUT));

    	editPointcutAction = new ModifyNamedPointcutAction(this, getSite().getShell());
    	editPointcutAction.setText("Edit Pointcut");
    	editPointcutAction.setToolTipText("Edit this pointcut.");
    	editPointcutAction.setImageDescriptor(AopSharedImages.getImageDescriptor(AopSharedImages.IMG_POINTCUT));

		
		
    	applyAdviceAction = new ApplyAdviceAction(treeViewer);
    	applyAdviceAction.setText("Apply Advice...");
    	applyAdviceAction.setToolTipText("Apply Advice to this Binding");
    	applyAdviceAction.setImageDescriptor(AopSharedImages.getImageDescriptor(AopSharedImages.IMG_ADVICE));
    	
    	applyInterceptorsAction = new ApplyInterceptorAction(treeViewer);
    	applyInterceptorsAction.setText("Apply Interceptor(s)...");
    	applyInterceptorsAction.setToolTipText("Apply Interceptor(s) to this Binding");
    	applyInterceptorsAction.setImageDescriptor(AopSharedImages.getImageDescriptor(AopSharedImages.IMG_INTERCEPTOR));
    	
    	goToAction = new Action () {
    		public void run() {
    	    	JumpToCodeUtil.jumpTo(getSelected());
    		}
    	};
    	goToAction.setText("Go To");
    	goToAction.setToolTipText("Go to this element");
    	goToAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
    	
    	createBindingAction = new Action() {
    	    public void run() {
    	    	EditBindingWizard wizard = new EditBindingWizard(null, (AopDescriptor) treeViewer.getInput());
    	    	WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
    			
    			dialog.create();
    			dialog.getShell().setSize(450, 500);
    			
    			int response = dialog.open();
    			
				// TODO: COMPLETE ME... cancel doesn't work.
    			if (response == WizardDialog.OK)
    			{
    			}
    	    }
    	};
    	createBindingAction.setText("Create Binding");
    	createBindingAction.setToolTipText("Create Binding");
    	createBindingAction.setImageDescriptor(AopSharedImages.getImageDescriptor(AopSharedImages.IMG_NEW_BINDING));
    	
    	editBindingAction = new Action () {
    		public void run () {
    			EditBindingWizard wizard = new EditBindingWizard((Binding) getSelected(), (AopDescriptor) treeViewer.getInput());
    	    	WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
    			
    			dialog.create();
    			dialog.getShell().setSize(450, 500);
    			
    			int response = dialog.open();
    			
    			if (response == WizardDialog.OK)
    			{
    			}
    		}
    	};
    	editBindingAction.setText("Edit Binding");
    	editBindingAction.setToolTipText("Edit Binding");
    	editBindingAction.setImageDescriptor(AopSharedImages.getImageDescriptor(AopSharedImages.IMG_BINDING));
    	
    	removeInterceptorAction = new Action() {
    		public void run() {
    			AopDescriptor descriptor = (AopDescriptor) treeViewer.getInput();
    			Interceptor interceptor = (Interceptor) getSelected();

    			if (AopUiPlugin.confirm("Are you sure you want to delete the Interceptor: \"" + interceptor.getClazz() + "\" ?"))
    			{
    				descriptor.remove(interceptor);
    				descriptor.save();
    			}
    		}
    	};
    	removeInterceptorAction.setText("Remove");
    	removeInterceptorAction.setToolTipText("Remove this Interceptor");
    	removeInterceptorAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
    	
    	removeNamedPointcutAction = new Action() {
    		public void run() {
    			AopDescriptor descriptor = (AopDescriptor) treeViewer.getInput();
    			Pointcut pointcut = (Pointcut) getSelected();

    			if (AopUiPlugin.confirm("Are you sure you want to delete the pointcut: \"" + pointcut.getName() + "\" ?"))
    			{
    				descriptor.remove(pointcut);
					AspectManager.instance().removePointcut(pointcut.getName());
    				descriptor.save();
    			}
    		}
    	};
    	removeNamedPointcutAction.setText("Remove");
		removeNamedPointcutAction.setToolTipText("Remove this Pointcut");
		removeNamedPointcutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
    	
    	removeAdviceAction = new Action() {
    		public void run() {
    			AopDescriptor descriptor = (AopDescriptor) treeViewer.getInput();
    			Advice advice = (Advice) getSelected();

    			if (AopUiPlugin.confirm("Are you sure you want to delete the Advice: \"" + advice.getName() + "\" ?"))
    			{
    				descriptor.remove(advice);
    				descriptor.save();
    			}
    		}
    	};
    	removeAdviceAction.setText("Remove");
    	removeAdviceAction.setToolTipText("Remove this Advice");
    	removeAdviceAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
    	
    	removeInterceptorRefAction = new Action() {
    		public void run() {
    			AopDescriptor descriptor = (AopDescriptor) treeViewer.getInput();
    			InterceptorRef interceptorRef = (InterceptorRef) getSelected();

    			if (AopUiPlugin.confirm("Are you sure you want to delete the Interceptor Reference: \"" + interceptorRef.getName() + "\" ?"))
    			{
    				descriptor.remove(interceptorRef);
    				descriptor.save();
    			}
    		}
    	};
    	removeInterceptorRefAction.setText("Remove");
    	removeInterceptorRefAction.setToolTipText("Remove this Interceptor Reference");
    	removeInterceptorRefAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
    	
    	removeBindingAction = new Action() {
    		public void run() {
    			AopDescriptor descriptor = (AopDescriptor) treeViewer.getInput();
    			Binding binding = (Binding) getSelected();

    			if (AopUiPlugin.confirm("Are you sure you want to delete the Binding: \"" + binding.getPointcut() + "\" ?"))
    			{
    				descriptor.remove(binding);
    				descriptor.save();
    			}	
    		}
    	};
    	removeBindingAction.setText("Remove");
    	removeBindingAction.setToolTipText("Remove this Binding");
    	removeBindingAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
    	
    }
    
	
	/**
	 * Returns the first selected item from the view.
	 * @return
	 */
    public Object getSelected ()
    {
    	if (treeViewer.getSelection() instanceof IStructuredSelection)
    	{
    		return ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
    	}
    	
    	return null;
    }
    
    private void createContextMenu ()
    {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				AspectManagerView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, treeViewer);	
    }
    
    private void fillContextMenu (IMenuManager manager)
    {
    	AopDescriptor descriptor = (AopDescriptor) treeViewer.getInput();
    	
    	if (currentSelection instanceof IStructuredSelection)
    	{
    		IStructuredSelection selection = (IStructuredSelection) currentSelection;
    		if (selection.size() == 1)
    		{
    			Object selected = selection.getFirstElement();
    			if (selected instanceof Aspect)
    			{
    				manager.add(goToAction);
    				manager.add(createAdviceAction);
    				
    			} else if (selected instanceof Binding) {
    			    manager.add(editBindingAction);
    				manager.add(applyInterceptorsAction);
    			    manager.add(applyAdviceAction);
    			    manager.add(new Separator());
    			    manager.add(removeBindingAction);
    			} else if (selected instanceof Interceptor) {
    				manager.add(goToAction);
    				manager.add(new Separator());
    				manager.add(removeInterceptorAction);
    			} else if (selected instanceof Advice) {
    				manager.add(goToAction);
    				manager.add(new Separator());
    				manager.add(removeAdviceAction);
    			} else if (selected instanceof InterceptorRef) {
    				manager.add(goToAction);
    				manager.add(new Separator());
    				manager.add(removeInterceptorRefAction);
    			} else if( selected instanceof Pointcut ) {
					manager.add(editPointcutAction);
					manager.add(new Separator());
					manager.add(removeNamedPointcutAction);
    			}
				
				/* 
				 * If they're not clicking on a sub-element, but a category
				 */
				
				else if (selected instanceof List) {
					// Category with children
    				List list = (List) selected;
					if( list.get(0) == AspectManagerContentProvider.BINDINGS) {
    					manager.add(createBindingAction);
					}
    				else if( list.get(0) == AspectManagerContentProvider.POINTCUTS) {
						manager.add(createPointcutAction);
    				}
    			} 
    		}
    	}
		

    	manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void createAdvice (Aspect aspect)
    {
    	
    }
    
    public void setFocus() {

    }

    public static AspectManagerView instance ()
    {
    	return _instance;
    }
    
    public TreeViewer getTreeViewer() {
        return treeViewer;
    }
    
    public void setDescriptor(AopDescriptor descriptor) {
        if (treeViewer != null && descriptor != null) {
            contentProvider.setDescriptor(descriptor);
            treeViewer.setInput(descriptor);
        }
    }
    
    /**
     * Update this view's descriptor asynchrously (SWT thread safe)
     */
    public void setDescriptorAsync (AopDescriptor descriptor)
    {
        if (treeViewer != null && ! treeViewer.getControl().isDisposed())
        {
	        //post this update to the tree
	        final AopDescriptor finalDescriptor = descriptor;
	        
	        Display display = treeViewer.getControl().getDisplay();
	        
	        if (!display.isDisposed()) {
	            display.asyncExec(new Runnable() {
	                public void run() {
	                    //make sure the tree still exists
	                    if (treeViewer.getControl().isDisposed())
	                        return;
	                    
	                    setDescriptor(finalDescriptor);
	                }
	            });
	        }
        }
    }
}