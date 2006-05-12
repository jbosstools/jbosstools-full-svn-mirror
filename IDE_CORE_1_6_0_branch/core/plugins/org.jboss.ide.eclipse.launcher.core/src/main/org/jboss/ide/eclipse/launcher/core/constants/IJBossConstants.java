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

import java.io.File;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public interface IJBossConstants
{
   /** Description of the Field */
   public final static String ATTR_JBOSS_HOME_DIR = "org.jboss.rocklet.HomeDir";//$NON-NLS-1$

   /** Description of the Field */
   public final static String JBOSS_MAIN_CLASS = "org.jboss.Main";//$NON-NLS-1$

   /** Description of the Field */
   public final static String RUN_JAR_RELATIVE_TO_JBOSS_HOME = "bin" + File.separator + "run.jar";//$NON-NLS-1$ //$NON-NLS-2$

   /** Description of the Field */
   public final static String JBOSS_HOME_OPTION = "-Djboss.home=";//$NON-NLS-1$

   /** Description of the Field */
   public final static String RELATIVE_WORKING_DIR = "bin";//$NON-NLS-1$

   /** Description of the Field */
   public final static String ATTR_SERVER_CONFIGURATION = "org.jboss.rocklet.ServerConfiguration";//$NON-NLS-1$

   /** Description of the Field */
   public final static String JBOSS_CONFIGURATION_OPTION = "-c";

   // Only 2.4.x
   /** Description of the Field */
   public final static String DOCUMENT_BUILDER_FACTORY_OPTION_2X = "-Djavax.xml.parsers.DocumentBuilderFactory=";//$NON-NLS-1$

   /** Description of the Field */
   public final static String DEFAULT_DOCUMENT_BUILDER_FACTORY_2X = "org.apache.crimson.jaxp.DocumentBuilderFactoryImpl";//$NON-NLS-1$

   /** Description of the Field */
   public final static String SAX_PARSER_FACTORY_OPTION_2X = "-Djavax.xml.parsers.SAXParserFactory=";//$NON-NLS-1$

   /** Description of the Field */
   public final static String DEFAULT_SAX_PARSER_FACTORY_2X = "org.apache.crimson.jaxp.SAXParserFactoryImpl";//$NON-NLS-1$

   /** Description of the Field */
   public final static String CRIMSON_JAR_RELATIVE_TO_JBOSS_HOME_2X = "lib" + File.separator + "crimson.jar";//$NON-NLS-1$ //$NON-NLS-2$

   /** Description of the Field */
   public final static String ATTR_DOCUMENT_BUILDER_FACTORY_2X = "org.rocklet.jboss.2x.DocumentBuilderFactory";//$NON-NLS-1$

   /** Description of the Field */
   public final static String ATTR_SAX_PARSER_FACTORY_2X = "org.rocklet.jboss.2x.SaxParserFactory";//$NON-NLS-1$

   // Only 3.x
   /** Description of the Field */
   public final static String JBOSS_SHUTDOWN_CLASS_3X = "org.jboss.Shutdown";//$NON-NLS-1$

   /** Description of the Field */
   public final static String SHUTDOWN_JAR_RELATIVE_TO_JBOSS_HOME_3X = "bin" + File.separator + "shutdown.jar";//$NON-NLS-1$ //$NON-NLS-2$

   /** Description of the Field */
   public final static String TOOLS_JAR_RELATIVE_TO_JDK_HOME_3X = "lib" + File.separator + "tools.jar";//$NON-NLS-1$ //$NON-NLS-2$

   // Only 4.x
   /** Description of the Field */
   public final static String JBOSSALL_CLIENT_RELATIVE_TO_JBOSS_HOME_4X = "client" + File.separator + "jbossall-client.jar";//$NON-NLS-1$ //$NON-NLS-2$
}
