/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.archives.core;


public class Trace {

	public static final String DEBUG_OPTION_ROOT = "org.jboss.ide.eclipse.packages.core/debug/";
	public static final String DEBUG_OPTION_STREAM_CLOSE = DEBUG_OPTION_ROOT + "streamClose";
	
	public static boolean isDebugging(String option) {
		return ArchivesCore.getInstance().getVariables().isDebugging(option);
	}
	
	public static void trace (Class caller, String message) {
		trace(caller, message, null);
	}
	
	public static void trace (Class caller, String message, String option) {
		trace(caller, message, null, option);
	}
	
	public static void trace (Class caller, Throwable t) {
		trace(caller, t, null);
	}
	
	public static void trace (Class caller, Throwable t, String option) {
		trace(caller, t.getMessage(), t, option);
	}
	
	public static void trace (Class caller, String message, Throwable t, String option) {
		if (!isDebugging(null))
			return;

		if (option != null) {
			if (!isDebugging(option))
				return;
		}
		
		System.out.println("[" + caller.getName() + "] " + message);
		
		if (t != null) {
			t.printStackTrace();
		}
	}
	
}
