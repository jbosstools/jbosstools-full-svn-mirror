/*
 * Created on Sep 22, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;

/**
 * @author Marshall
 */
public interface IAopAdvisor extends IAdaptable {

	public static final int ADVICE = 0;
	public static final int INTERCEPTOR = 1;
	public static final int TYPEDEF = 2;
	
	public IJavaElement getAdvisingElement();
	
	public IAopAdvised[] getAdvised();
	
	public IAopAdvised getAdvised (IJavaElement element);
	
	public int getType();
	
	public void removeAdvised (IAopAdvised advised);
	
	public void addAdvised (IAopAdvised advised);
	
	public void addAdvised (IAopAdvised advised[]);
	
	public boolean advises (IJavaElement element);
	
	public boolean advises (IAopAdvised advised);
	
	public JDTPointcutExpression[] getAssignedPointcuts();
	
	public void assignPointcut (JDTPointcutExpression expression);
	
	public void unassignPointcut (JDTPointcutExpression expression);
}
