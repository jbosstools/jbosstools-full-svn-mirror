/*
 * Created on Sep 23, 2004
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IType;


/**
 * @author Marshall
 */
public interface IAopModelChangeListener {

	public void advisorAdded (IAopAdvised advised, IAopAdvisor advisor);
	public void advisorRemoved (IAopAdvised advised, IAopAdvisor advisor);
	public void typeMatchAdded(IType type, IAopTypeMatcher matcher);
	public void typeMatchRemoved(IType type, IAopTypeMatcher matcher);
	
}
