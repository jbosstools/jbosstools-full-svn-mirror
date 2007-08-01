/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.as.core.server.runtime;

import java.util.List;

public interface IJBossServerRuntimeDelegate {
	public static final int ACTION_START = 1;
	public static final int ACTION_SHUTDOWN = 2;
	public static final int ACTION_TWIDDLE = 3;
	
	public String getId();
	public String getStartArgs();
	public String getStopArgs();
	public String getVMArgs();
	public List getRuntimeClasspath(int action);
	public String getStartJar();
	public String getShutdownJar();
	public String getStartMainType();
	public String getStopMainType();
}
