/*
 * Created on Sep 22, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import org.eclipse.jdt.core.IMethod;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportAdvice;

/**
 * @author Marshall
 */
public interface IReportModelAdvice extends IReportModelAdvisor {

	public ReportAdvice getReportAdvice();
	public IMethod getAdvisingMethod();
	
}
