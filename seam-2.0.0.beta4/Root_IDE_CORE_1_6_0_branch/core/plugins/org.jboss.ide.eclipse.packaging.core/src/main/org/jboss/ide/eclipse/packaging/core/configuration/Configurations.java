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
package org.jboss.ide.eclipse.packaging.core.configuration;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.packaging.core.PackagingCoreMessages;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.ui.util.Progress;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class Configurations
{
   /** Description of the Field */
   private ArrayList configurations = new ArrayList();

   /** Constructor for the StandardConfigurations object */
   public Configurations()
   {
   }

   /**
    * Gets the configurations attribute of the ProjectConfigurations object
    *
    * @return   The configurations value
    */
   public List getConfigurations()
   {
      return this.configurations;
   }

   /**
    * Description of the Method
    *
    * @exception CoreException  Description of the Exception
    */
   public abstract void loadConfigurations() throws CoreException;

   /**
    * Description of the Method
    *
    * @param o  Description of the Parameter
    */
   public void moveDown(Object o)
   {
      if (this.configurations.contains(o))
      {
         int pos = this.configurations.indexOf(o) + 1;
         if (pos < this.configurations.size())
         {
            this.configurations.remove(o);
            this.configurations.add(pos, o);
         }
      }
   }

   /**
    * Description of the Method
    *
    * @param o  Description of the Parameter
    */
   public void moveUp(Object o)
   {
      if (this.configurations.contains(o))
      {
         int pos = this.configurations.indexOf(o) - 1;
         if (pos >= 0)
         {
            this.configurations.remove(o);
            this.configurations.add(pos, o);
         }
      }
   }

   /**
    * Adds a feature to the Archive attribute of the Configurations object
    *
    * @return   Description of the Return Value
    */
   protected PackagingArchive createArchive()
   {
      return new PackagingArchive();
   }

   /**
    * Gets the contents attribute of the StandardConfigurations object
    *
    * @return                   The contents value
    * @exception CoreException  Description of the Exception
    */
   protected abstract InputStream getContents() throws CoreException;

   /**
    * Description of the Method
    *
    * @exception CoreException  Description of the Exception
    */
   protected void load() throws CoreException
   {
      IRunnableWithProgress runnable = new IRunnableWithProgress()
      {
         public void run(IProgressMonitor monitor) throws InvocationTargetException
         {
            try
            {
               monitor.beginTask(PackagingCoreMessages.getString("Configurations.configuration.load"), 100);//$NON-NLS-1$

               Configurations.this.getConfigurations().clear();

               DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
               DocumentBuilder docBuilder = factory.newDocumentBuilder();

               Document document = docBuilder.parse(getContents());
               Node root = document.getDocumentElement();
               NodeList children = root.getChildNodes();

               // Compute the progress bar steps
               int step = 1;
               if (children.getLength() > 0)
               {
                  step = 100 / children.getLength();
               }

               for (int i = 0; i < children.getLength(); i++)
               {
                  Node child = children.item(i);

                  monitor.subTask(MessageFormat.format(PackagingCoreMessages
                        .getString("Configurations.configuration.restore"), new Object[]//$NON-NLS-1$
                        {child.getNodeValue()}));

                  if (child.getNodeName().equals("archive")//$NON-NLS-1$
                  )
                  {
                     PackagingArchive archive = Configurations.this.createArchive();
                     archive.readFromXml(child);
                     Configurations.this.configurations.add(archive);
                  }

                  monitor.worked(step);
               }
            }
            catch (Exception e)
            {
               throw new InvocationTargetException(e);
            }
         }
      };

      // Launch the task with progress
      try
      {
         Progress progress = new Progress(AbstractPlugin.getShell(), runnable);
         progress.run();
      }
      catch (InvocationTargetException ite)
      {
         throw AbstractPlugin.wrapException(ite);
      }
      catch (InterruptedException ie)
      {
         throw AbstractPlugin.wrapException(ie);
      }
   }
}
