/*
 * Created on Sep 22, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import org.eclipse.jdt.core.IJavaElement;

/**
 * @author Marshall
 */
public interface IReportModelAdvisor {

	public static final int ADVICE = 0;
	public static final int INTERCEPTOR = 1;
	
	public IJavaElement getAdvisingElement();
	public IReportModelAdvised[] getAdvised();
	public Object getReportObject();
	public int getType();
	
}
