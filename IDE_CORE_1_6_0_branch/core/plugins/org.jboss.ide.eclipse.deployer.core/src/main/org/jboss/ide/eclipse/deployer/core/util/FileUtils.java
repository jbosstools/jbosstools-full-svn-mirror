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
package org.jboss.ide.eclipse.deployer.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jboss.ide.eclipse.deployer.core.DeployerCoreMessages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FileUtils
{
   /**Constructor for the FileUtils object */
   private FileUtils()
   {
   }

   /**
    * Description of the Method
    *
    * @param sourceDir        Description of the Parameter
    * @param destDir          Description of the Parameter
    * @param overwrite        Description of the Parameter
    * @exception IOException  Description of the Exception
    */
   public static void copyDirectory(File sourceDir, File destDir, boolean overwrite) throws IOException
   {
      // Scan all the files to be copied
      Map mappings = new HashMap();
      fillMappings(sourceDir, destDir, mappings);

      // Copy all the files on by one
      for (Iterator iterator = mappings.keySet().iterator(); iterator.hasNext();)
      {
         File source = (File) iterator.next();
         File dest = (File) mappings.get(source);
         copyFile(source, dest, overwrite);
      }
   }

   /**
    * Description of the Method
    *
    * @param sourceDir        Description of the Parameter
    * @param destDir          Description of the Parameter
    * @exception IOException  Description of the Exception
    */
   public static void copyDirectory(File sourceDir, File destDir) throws IOException
   {
      copyDirectory(sourceDir, destDir, false);
   }

   /**
    * Description of the Method
    *
    * @param sourceFile       Description of the Parameter
    * @param destFile         Description of the Parameter
    * @exception IOException  Description of the Exception
    */
   public static void copyFile(File sourceFile, File destFile) throws IOException
   {
      copyFile(sourceFile, destFile, false);
   }

   /**
    * Description of the Method
    *
    * @param sourceFile       Description of the Parameter
    * @param destFile         Description of the Parameter
    * @param overwrite        Description of the Parameter
    * @exception IOException  Description of the Exception
    */
   public static void copyFile(File sourceFile, File destFile, boolean overwrite) throws IOException
   {
      if (overwrite || !destFile.exists() || destFile.lastModified() < sourceFile.lastModified())
      {
         //         if (destFile.exists() && destFile.isFile())
         //         {
         //            destFile.delete();
         //         }

         File parent = destFile.getParentFile();
         if (!parent.exists())
         {
            parent.mkdirs();
         }

         FileInputStream in = null;
         FileOutputStream out = null;
         try
         {
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);

            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            do
            {
               out.write(buffer, 0, count);
               count = in.read(buffer, 0, buffer.length);
            }
            while (count != -1);
         }
         finally
         {
            if (out != null)
            {
               out.close();
            }
            if (in != null)
            {
               in.close();
            }
         }
         out = null;
      }
   }

   /**
    * Description of the Method
    *
    * @param directory        Description of the Parameter
    * @exception IOException  Description of the Exception
    */
   public static void deleteDirectory(File directory) throws IOException
   {
      Collection directories = listDirectory(directory);
      if (directories.size() > 0)
      {
         // Delete each directory
         for (Iterator iterator = directories.iterator(); iterator.hasNext();)
         {
            deleteDirectory((File) iterator.next());
         }
      }
      Collection files = listFiles(directory);
      if (files.size() > 0)
      {
         // Delete each file
         for (Iterator iterator = files.iterator(); iterator.hasNext();)
         {
            File file = (File) iterator.next();
            if (!file.delete())
            {
               throw new IOException(
                     DeployerCoreMessages.getString("FileUtils.action.delete.failed.text2") + file + DeployerCoreMessages.getString("FileUtils.action.delete.failed.text1"));//$NON-NLS-1$ //$NON-NLS-2$
            }
         }
      }
      // Eventually delete the directory
      if (!directory.delete())
      {
         throw new IOException(
               DeployerCoreMessages.getString("FileUtils.action.delete.failed.text3") + directory + DeployerCoreMessages.getString("FileUtils.action.delete.failed.text1"));//$NON-NLS-1$ //$NON-NLS-2$
      }
   }

   /**
    * Description of the Method
    *
    * @param sourceDir        Description of the Parameter
    * @param destDir          Description of the Parameter
    * @param mappings         Description of the Parameter
    * @exception IOException  Description of the Exception
    */
   private static void fillMappings(File sourceDir, File destDir, Map mappings) throws IOException
   {
      Collection directories = listDirectory(sourceDir);
      if (directories.size() > 0)
      {
         // Delete each directory
         for (Iterator iterator = directories.iterator(); iterator.hasNext();)
         {
            File source = (File) iterator.next();
            File dest = new File(destDir, source.getName());
            fillMappings(source, dest, mappings);
         }
      }

      Collection files = listFiles(sourceDir);
      if (files.size() > 0)
      {
         // Delete each file
         for (Iterator iterator = files.iterator(); iterator.hasNext();)
         {
            File source = (File) iterator.next();
            File dest = new File(destDir, source.getName());
            mappings.put(source, dest);
         }
      }
   }

   /**
    * Description of the Method
    *
    * @param directory  Description of the Parameter
    * @return           Description of the Return Value
    */
   private static Collection listDirectory(File directory)
   {
      Collection result = new ArrayList();
      File[] files = directory.listFiles();
      if (files != null)
      {
         for (int i = 0; i < files.length; i++)
         {
            if (files[i].isDirectory())
            {
               result.add(files[i]);
            }
         }
      }
      return result;
   }

   /**
    * Description of the Method
    *
    * @param directory  Description of the Parameter
    * @return           Description of the Return Value
    */
   private static Collection listFiles(File directory)
   {
      Collection result = new ArrayList();
      File[] files = directory.listFiles();
      if (files != null)
      {
         for (int i = 0; i < files.length; i++)
         {
            if (files[i].isFile())
            {
               result.add(files[i]);
            }
         }
      }
      return result;
   }
}
