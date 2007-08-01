/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.views;


import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AopReport;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvisor;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;
import org.jboss.ide.eclipse.jdt.aop.ui.util.JumpToCodeUtil;
import org.jboss.ide.eclipse.jdt.aop.ui.views.providers.AdvisedMembersContentProvider;
import org.jboss.ide.eclipse.jdt.aop.ui.views.providers.AdvisedMembersLabelProvider;

/**
 * @author Marshall
 */
public class AdvisedMembersView extends ViewPart implements IAopModelChangeListener {

	private IJavaElement input;
	private Label noAdvisedMembers;
	private TreeViewer advisedMembersTree;
	private Tree tree;
	private Composite main;
	private StackLayout stackLayout;
	private AopReport report;
	private AdvisedMembersContentProvider contentProvider;
	private AdvisedMembersLabelProvider labelProvider;
	
	private static AdvisedMembersView instance;
	
	public AdvisedMembersView ()
	{
		instance = this;
	}
	
	public void createPartControl(Composite parent) {
		//main = new Composite(parent, SWT.NONE);
		//stackLayout = new StackLayout();
		//main.setLayout(stackLayout);
		
		//noAdvisedMembers = new Label(main, SWT.NONE);
		//noAdvisedMembers.setText("There are no advised members for this class.");
		
		advisedMembersTree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		advisedMembersTree.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		
		contentProvider = new AdvisedMembersContentProvider();
		advisedMembersTree.setContentProvider(contentProvider);

		labelProvider = new AdvisedMembersLabelProvider();
		advisedMembersTree.setLabelProvider(labelProvider);
		
		advisedMembersTree.addDoubleClickListener(JumpToCodeUtil.getDoubleClickListener());
		
		AopModel.instance().addAopModelChangeListener(this);
		
		//stackLayout.topControl = tree;
		//main.layout();
	}

	public void setFocus() {}

	public void setInput (IJavaElement element)
	{
	    if (element != null)
	    {
			if (!advisedMembersTree.getControl().isDisposed())
			{
				input = element;
				
				//IJavaProject project = element.getJavaProject();
				//report = AopCorePlugin.getDefault().getProjectReport(project);

				advisedMembersTree.setInput(element);
			}
	    }
	}
	
	public void showNoAdvisedMembers ()
	{
		stackLayout.topControl = noAdvisedMembers;
		main.layout();
	}
	
	public void refresh () {
        final IJavaElement finalElement = input;
        if (!advisedMembersTree.getControl().isDisposed())
        {
	        Display display = advisedMembersTree.getControl().getDisplay();
	        
	        if (!display.isDisposed()) {
	            display.asyncExec(new Runnable() {
	                public void run() {
	                    //make sure the tree still exists
	                    if (advisedMembersTree != null && advisedMembersTree.getControl().isDisposed())
	                        return;
	                    
	                    setInput (finalElement);
	                }
	            });
	        }
        }
	}

	public static AdvisedMembersView instance ()
	{
		return instance;
	}
	
	public void advisorAdded(IAopAdvised advised, IAopAdvisor advisor)
	{
		refresh();
	}

	public void advisorRemoved(IAopAdvised advised, IAopAdvisor advisor)
	{
		refresh();
	}

	/* (non-Javadoc)
	 * @see org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener#typeMatchAdded(org.eclipse.jdt.core.IType, org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher)
	 */
	public void typeMatchAdded(IType type, IAopTypeMatcher matcher) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopModelChangeListener#typeMatchRemoved(org.eclipse.jdt.core.IType, org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher)
	 */
	public void typeMatchRemoved(IType type, IAopTypeMatcher matcher) {
		// TODO Auto-generated method stub
		
	}
}
