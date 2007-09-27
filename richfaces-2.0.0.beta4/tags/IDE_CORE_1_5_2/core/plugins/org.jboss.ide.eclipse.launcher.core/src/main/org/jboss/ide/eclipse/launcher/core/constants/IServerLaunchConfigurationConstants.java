/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.launcher.core.constants;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public interface IServerLaunchConfigurationConstants
{
   /** Description of the Field */
   public final String ATTR_RELATIVE_TO_HOMEDIR_CLASSPATH = "org.rocklet.launcher.relativeHomeDirClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_RELATIVE_TO_HOMEDIR_SHUTDOWN_CLASSPATH = "org.rocklet.launcher.relativeHomeDirShutdownClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_HOMEDIR_CLASSPATH = "org.rocklet.launcher.HomeDirClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_HOMEDIR_SHUTDOWN_CLASSPATH = "org.rocklet.launcher.HomeDirShutdownClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_RELATIVE_TO_JDK_CLASSPATH = "org.rocklet.launcher.relativeJDKClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_JDK_CLASSPATH = "org.rocklet.launcher.JDKClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_CLASSPATH = "org.rocklet.launcher.classpath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_SHUTDOWN_CLASSPATH = "org.rocklet.launcher.shutdownClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_SHUTDOWN_VM_ARGS = "org.rocklet.launcher.shutdownVmArgs";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_SHUTDOWN_PROGRAM_ARGS = "org.rocklet.launcher.shutdownProgramArgs";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_USER_PROGRAM_ARGS = "org.rocklet.launcher.userProgramArgs";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_USER_VM_ARGS = "org.rocklet.launcher.UserVMArgs";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_USER_SHUTDOWN_PROGRAM_ARGS = "org.rocklet.launcher.userShutdownProgramArgs";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_USER_SHUTDOWN_VM_ARGS = "org.rocklet.launcher.userShutdownVMArgs";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_DEFAULT_VM_ARGS = "org.rocklet.launcher.defaultVMArgs";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_SHUTDOWN_TYPE = "org.rocklet.launcher.shutdownType";//$NON-NLS-1$

   /** Description of the Field */
   public final static String JAVA_PROJECT_ID = "org.eclipse.jdt.core.javanature";//$NON-NLS-1$

   /** Description of the Field */
   public final String LAUNCH_CONFIGURATION_ID = "org.rocklet.launcher.ServerLaunchConfiguration";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_LAUNCH_SHUTDOWN = "org.rocklet.launcher.shutdown";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_CONFIGURATION_ERROR = "org.rocklet.launcher.ConfigurationError";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_POLLING_INTERVALL = "org.rocklet.launcher.pollingIntervall";//$NON-NLS-1$

   /** Description of the Field */
   public final int DEFAULT_POLLING_INTERVALL = 1;

   /** Description of the Field */
   public final String EMPTY_STRING = "";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_LOG_FILES = "org.rocklet.launcher.logFiles";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_LOGGING_ACTIVE = "org.rocklet.launcher.loggingActive";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_DEFAULT_CLASSPATH = "org.rocklet.launcher.defaultClasspath";//$NON-NLS-1$

   /** Description of the Field */
   public final String VALUE_SERVER_CLASSPATH_PROVIDER_ID = "org.jboss.ide.eclipse.launcher.configuratiom.ServerClasspathProvider";//$NON-NLS-1$

   /** Description of the Field */
   public final String ATTR_DEFAULT_PROGRAM_ARGS = "org.rocklet.launcher.defaultProgramArgs";//$NON-NLS-1$;

   /** Description of the Field */
   public final String ATTR_DEFAULT_LOG_FILES = "org.rocklet.launcher.defaultLogFiles";//$NON-NLS-1$
}
