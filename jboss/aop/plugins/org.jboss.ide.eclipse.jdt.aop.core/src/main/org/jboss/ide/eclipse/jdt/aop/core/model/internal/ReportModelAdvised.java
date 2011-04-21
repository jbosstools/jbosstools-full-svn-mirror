package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IJavaElement;
import org.jboss.ide.eclipse.jdt.aop.core.model.IReportModelAdvised;

/**
 * @author Marshall
 */
public class ReportModelAdvised implements IReportModelAdvised
{

	private int type;
	private IJavaElement advised;
	
	public ReportModelAdvised (int type, IJavaElement advised)
	{
		this.type = type;
		this.advised = advised;
	}

	public IJavaElement getAdvisedElement()
	{
		return advised;
	}

	public int getType()
	{
		return type;
	}

}
