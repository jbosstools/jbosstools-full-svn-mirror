/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopReportModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.IReportModelInterceptor;


public class ReportModelInterceptor extends ReportModelAdvisor implements IReportModelInterceptor
{
	private ReportInterceptor reportInterceptor;
	public ReportModelInterceptor (AopReportModel model, IJavaElement element, ReportInterceptor interceptor)
	{
		super(model, element);
		this.reportInterceptor = interceptor;
	}
	
	public ReportInterceptor getReportInterceptor() {
		return reportInterceptor;
	}

	public Object getReportObject() {
		return getReportInterceptor();
	}

	public IType getAdvisingType ()
	{
		return (IType) getAdvisingElement();
	}
	
	public int getType() { return INTERCEPTOR; }
}