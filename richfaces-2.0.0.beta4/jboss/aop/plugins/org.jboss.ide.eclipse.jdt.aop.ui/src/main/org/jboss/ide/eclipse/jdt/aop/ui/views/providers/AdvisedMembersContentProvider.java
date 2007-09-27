/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui.views.providers;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.IAopAdvisor;

/**
 * @author Marshall
 */
public class AdvisedMembersContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
    public static final Object[] NO_ADVISED_CHILDREN = new Object[] {  "noadvisedchildren" };
    
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ICompilationUnit)
		{
			ICompilationUnit unit = (ICompilationUnit) parentElement;
			ArrayList types = new ArrayList();
			try {
				types.addAll(Arrays.asList(unit.getAllTypes()));
				types.remove(unit.findPrimaryType());
			} catch( JavaModelException jme) {
			}
			ArrayList children = AopModel.instance().getAdvisedChildren (unit.findPrimaryType());
			children.addAll(types);

			
			if (children.size() > 0)
				return children.toArray();
			else {
			    return NO_ADVISED_CHILDREN;
			}
			
		}
		else if (parentElement instanceof IType)
		{
			IType type = (IType) parentElement;
			ArrayList children = AopModel.instance().getAdvisedChildren (type);
			if (children.size() > 0)
				return children.toArray();
		}
		else if (parentElement instanceof IMethod || parentElement instanceof IField)
		{
			return AopModel.instance().getElementAdvisors((IJavaElement) parentElement);
		}
		
		return new Object[0];
	}
	
	public Object getParent(Object element) {
		if (element instanceof IMethod)
		{
			return ((IMethod)element).getDeclaringType();
		}
		
		else if (element instanceof IField)
		{
			return ((IField)element).getDeclaringType();
		}
		
		else if (element instanceof IType)
		{
			IType type = (IType) element;
			if (type.getParent() != null)
			{
				return type.getParent();
			}
		}
		
		return null;
	}
	
	public boolean hasChildren(Object element) {
		if (element instanceof ICompilationUnit)
		{
			ICompilationUnit unit = (ICompilationUnit) element;
			return true;
			//findAdvisedChildren(unit.findPrimaryType()).size() > 0;	
		}
		else if (element instanceof IType)
		{
			IType type = (IType) element;
			return AopModel.instance().getAdvisedChildren(type).size() > 0;
		}
		else if (element instanceof IMethod || element instanceof IField)
		{
			try {
				IAopAdvisor advisors[] = AopModel.instance().getElementAdvisors((IJavaElement) element);
				return ! (advisors == null || advisors.length == 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	public Object[] getElements(Object inputElement) {	return getChildren(inputElement); }
	public void dispose() {	}
}