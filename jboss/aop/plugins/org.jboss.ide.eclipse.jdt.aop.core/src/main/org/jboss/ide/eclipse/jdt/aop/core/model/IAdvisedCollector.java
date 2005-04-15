/*
 * Created on Jan 22, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Marshall
 */
public interface IAdvisedCollector extends IProgressMonitor {
	
	public void collectAdvised (IAopAdvised advised);
	
	public void handleException (Exception e);
}
