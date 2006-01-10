package org.jboss.ide.eclipse.core.util;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class ResourceUtil {

	public static void safeRefresh (IResource resource, int depth)
	{
		try {
			
			if (resource != null)
			{
				resource.refreshLocal(depth, null);
			}
		
			//	 BUG: JBAS-1218 we're swallowing a clearcase/vss - specific resource handling bug here... 		
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
