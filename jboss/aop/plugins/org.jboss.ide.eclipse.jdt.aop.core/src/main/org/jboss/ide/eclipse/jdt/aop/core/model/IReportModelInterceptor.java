/*
 * Created on Sep 22, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import org.eclipse.jdt.core.IType;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportInterceptor;

/**
 * @author Marshall
 */
public interface IReportModelInterceptor extends IReportModelAdvisor {

	public ReportInterceptor getReportInterceptor();
	public IType getAdvisingType();
	
}
