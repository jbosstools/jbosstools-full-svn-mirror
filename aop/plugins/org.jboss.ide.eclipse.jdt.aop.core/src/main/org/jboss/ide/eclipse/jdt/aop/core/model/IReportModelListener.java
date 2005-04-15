/*
 * Created on Sep 23, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;


/**
 * @author Marshall
 */
public interface IReportModelListener {

	public void advisorAdded (IReportModelAdvised advised, IReportModelAdvisor advisor);
	public void advisorRemoved (IReportModelAdvised advised, IReportModelAdvisor advisor);
	
}
