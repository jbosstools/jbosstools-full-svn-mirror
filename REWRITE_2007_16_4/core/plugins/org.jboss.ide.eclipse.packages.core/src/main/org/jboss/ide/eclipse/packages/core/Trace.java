package org.jboss.ide.eclipse.packages.core;

import org.eclipse.core.runtime.Platform;

public class Trace {

	public static final String DEBUG_OPTION_ROOT = "org.jboss.ide.eclipse.packages.core/debug/";
	public static final String DEBUG_OPTION_STREAM_CLOSE = DEBUG_OPTION_ROOT + "streamClose";
	
	public static boolean isDebugging(String option)
	{
		return PackagesCorePlugin.getDefault().isDebugging()
			&& "true".equalsIgnoreCase(Platform.getDebugOption(option));
	}
	
	public static void trace (Class caller, String message)
	{
		trace(caller, message, null);
	}
	
	public static void trace (Class caller, String message, String option)
	{
		trace(caller, message, null, option);
	}
	
	public static void trace (Class caller, Throwable t)
	{
		trace(caller, t, null);
	}
	
	public static void trace (Class caller, Throwable t, String option)
	{
		trace(caller, t.getMessage(), t, option);
	}
	
	public static void trace (Class caller, String message, Throwable t, String option)
	{
		if (!PackagesCorePlugin.getDefault().isDebugging())
			return;

		if (option != null) {
			if (!isDebugging(option))
				return;
		}
		
		System.out.println("[" + caller.getName() + "] " + message);
		
		if (t != null)
		{
			t.printStackTrace();
		}
	}
	
}
