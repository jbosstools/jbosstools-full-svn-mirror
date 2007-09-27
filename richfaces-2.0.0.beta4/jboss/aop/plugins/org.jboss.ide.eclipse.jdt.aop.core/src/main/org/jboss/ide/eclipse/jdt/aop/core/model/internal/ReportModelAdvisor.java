/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopReportModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.IReportModelAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.IReportModelAdvisor;


public abstract class ReportModelAdvisor implements IReportModelAdvisor
{
	private AopReportModel model;
	private IJavaElement element;
	
	public ReportModelAdvisor (AopReportModel model, IJavaElement advisingElement)
	{
		this.element = advisingElement;
		this.model = model;
	}
	
	public IReportModelAdvised[] getAdvised()
	{
		ArrayList elements = new ArrayList();
		
		for (Iterator paIter = model.getAllProjectAdvisors().keySet().iterator(); paIter.hasNext(); )
		{
			Hashtable advisors = (Hashtable) model.getAllProjectAdvisors().get(paIter.next());
			
			for (Iterator iter = advisors.keySet().iterator(); iter.hasNext(); )
			{
				IReportModelAdvised advised = (IReportModelAdvised) iter.next();
				Collection elementAdvisors = model.getElementAdvisors(advised);
				
				for (Iterator aIter = elementAdvisors.iterator(); aIter.hasNext(); )
				{
					IReportModelAdvisor advisor = (IReportModelAdvisor) aIter.next();
					if (advisor.equals(this))
					{
						elements.add(advised);
						break;
					}
				}
			}
		}
		
		return (IReportModelAdvised[]) elements.toArray(new IReportModelAdvised[elements.size()]);
	}

	public boolean equals (Object other)
	{
		if (other instanceof IReportModelAdvisor)
		{
			IReportModelAdvisor otherAdvisor = (IReportModelAdvisor) other;
			
			return otherAdvisor.getAdvisingElement().equals(element);
		}
		return false;
	}
	
	public IJavaElement getAdvisingElement() {
		return element;
	}
}