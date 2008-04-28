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
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.xml.sax.InputSource;

/**
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   17 mai 2003
 */
public class XTagsProvider
{

   /**
    * Gets the urls attribute of the XTagsProvider object
    *
    * @param dir              Description of the Parameter
    * @param monitor          Description of the Parameter
    * @param range            Description of the Parameter
    * @return                 The urls value
    * @exception IOException  Description of the Exception
    */
   public URL[] getUrls(File dir, IProgressMonitor monitor, int range) throws IOException
   {
      if (!dir.isDirectory())
      {
         return null;
      }
      File[] files = dir.listFiles(new FilenameFilter()
      {
         public boolean accept(File dir, String name)
         {
            return name.endsWith(".jar");//$NON-NLS-1$
         }
      });
      int stepSize = 1;
      try
      {
         stepSize = range / files.length;
      }
      catch (RuntimeException ignore)
      {
      }
      ArrayList xtagsURLList = new ArrayList();
      for (int i = 0; i < files.length; i++)
      {
         monitor.subTask(XDocletAssistMessages.getString("XTagsProvider.Checking__6") + files[i].getName());//$NON-NLS-1$
         JarFile jarFile = new JarFile(files[i]);
         JarEntry xtagsXml = jarFile.getJarEntry("META-INF/xtags.xml");//$NON-NLS-1$
         if (xtagsXml != null)
         {
            xtagsURLList.add(new URL(new URL("jar:" + files[i].toURL() + "!/"), //$NON-NLS-1$ //$NON-NLS-2$
                  "META-INF/xtags.xml"));//$NON-NLS-1$
         }
         monitor.worked(stepSize);
         System.out.println(XDocletAssistMessages.getString("XTagsProvider.XTAGS___11") + files[i]);//$NON-NLS-1$
      }
      return (URL[]) xtagsURLList.toArray(new URL[xtagsURLList.size()]);
   }

   /**
    * Gets the additionalValuesDTDInputSource attribute of the XTagsProvider class
    *
    * @return   The additionalValuesDTDInputSource value
    */
   public static InputSource getAdditionalValuesDTDInputSource()
   {
      return new InputSource(XTagsProvider.class.getResourceAsStream("/jbosside_values_1_0.dtd"));//$NON-NLS-1$
   }

   /**
    * Gets the templatesDTDInputSource attribute of the XTagsProvider class
    *
    * @return   The templatesDTDInputSource value
    */
   public static InputSource getTemplatesDTDInputSource()
   {
      return new InputSource(XTagsProvider.class.getResourceAsStream("/jbosside_templates_1_0.dtd"));//$NON-NLS-1$
   }

   /**
    * Gets the variablesDTDInputSource attribute of the XTagsProvider class
    *
    * @return   The variablesDTDInputSource value
    */
   public static InputSource getVariablesDTDInputSource()
   {
      return new InputSource(XTagsProvider.class.getResourceAsStream("/jbosside_variables_1_0.dtd"));//$NON-NLS-1$
   }

   /**
    * Gets the xTagsDTDInputSource attribute of the XTagsProvider class
    *
    * @return   The xTagsDTDInputSource value
    */
   public static InputSource getXTagsDTDInputSource()
   {
      return new InputSource(XTagsProvider.class.getResourceAsStream("/xtags_1_1.dtd"));//$NON-NLS-1$
   }
}
