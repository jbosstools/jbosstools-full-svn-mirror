/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopReportModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.IReportModelAdvice;


public class ReportModelAdvice extends ReportModelAdvisor implements IReportModelAdvice
{
	private ReportAdvice reportAdvice;
	public ReportModelAdvice (AopReportModel model, IJavaElement element, ReportAdvice advice)
	{
		super (model, element);
		
		this.reportAdvice = advice;
	}
	
	public ReportAdvice getReportAdvice() {
		return reportAdvice;
	}

	public Object getReportObject() {
		return getReportAdvice();
	}
	
	public IMethod getAdvisingMethod ()
	{
		return (IMethod) getAdvisingElement();
	}
	
	public int getType() { return ADVICE; }
}