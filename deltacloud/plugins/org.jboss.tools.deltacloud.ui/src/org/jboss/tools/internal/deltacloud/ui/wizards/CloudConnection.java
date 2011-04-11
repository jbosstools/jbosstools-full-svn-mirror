package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public interface CloudConnection {
	
	public boolean performTest();
	public DeltaCloud getDeltaCloud();

}
