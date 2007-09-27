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
package org.jboss.ide.eclipse.xdoclet.run.model;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.ui.util.Progress;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunMessages;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   20 mars 2003
 * @todo      Javadoc to complete
 */
public class XDocletDataRepository
{
   /** Description of the Field */
   private Hashtable elements = new Hashtable();

   /** Description of the Field */
   private ArrayList tasks = new ArrayList();

   /** Description of the Field */
   private final static String REFERENCE_FILE = "resources/reference.xml";//$NON-NLS-1$

   /**Constructor for the XDocletDataRepository object */
   public XDocletDataRepository()
   {
      this.refreshReference();
   }

   /**
    * Gets the elements attribute of the XDocletDataRepository object
    *
    * @param element  Description of the Parameter
    * @return         The elements value
    */
   public Collection getElements(XDocletElement element)
   {
      XDocletElement e = (XDocletElement) this.elements.get(element.getId());
      if (e != null)
      {
         return e.getNodes();
      }
      return null;
   }

   /**
    * Gets the tasks attribute of the XDocletDataRepository object
    *
    * @return   The tasks value
    */
   public Collection getTasks()
   {
      return this.tasks;
   }

   /** Description of the Method */
   public void refreshReference()
   {
      IRunnableWithProgress runnable = new IRunnableWithProgress()
      {
         public void run(IProgressMonitor monitor)
         {
            try
            {
               monitor.beginTask(XDocletRunMessages.getString("XDocletDataRepository.repository.load"), 100);//$NON-NLS-1$

               XDocletDataRepository.this.tasks.clear();
               XDocletDataRepository.this.elements.clear();

               DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
               DocumentBuilder docBuilder = factory.newDocumentBuilder();

               URL genericsFile = XDocletRunPlugin.getDefault().find(new Path(REFERENCE_FILE));
               Document document = docBuilder.parse(genericsFile.openStream());
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

                  monitor.subTask(MessageFormat.format(XDocletRunMessages
                        .getString("XDocletDataRepository.repository.restore"), new Object[]{child.getNodeValue()}));//$NON-NLS-1$

                  if (child.getNodeName().equals("task")//$NON-NLS-1$
                  )
                  {

                     // Parse the task
                     XDocletTask task = new XDocletTask();
                     task.readFromXml(child, false);
                     XDocletDataRepository.this.tasks.add(task);
                     XDocletDataRepository.this.elements.put(task.getId(), task);

                     // Parse the element inside tasks
                     XDocletDataRepository.this.parseElement(child, task);
                  }

                  monitor.worked(step);
               }
            }
            catch (Exception e)
            {
               new InvocationTargetException(e);
            }
         }
      };

      // Launch the task with progress
      try
      {
         Progress progress = new Progress(XDocletRunPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow()
               .getShell(), runnable);
         progress.run();
      }
      catch (InvocationTargetException ite)
      {
         AbstractPlugin.logError("Can't load reference configurations", ite);//$NON-NLS-1$
      }
      catch (InterruptedException ie)
      {
         AbstractPlugin.logError("Can't load reference configurations", ie);//$NON-NLS-1$
      }
   }

   /**
    * Description of the Method
    *
    * @param parent   Description of the Parameter
    * @param element  Description of the Parameter
    */
   private void parseElement(Node parent, XDocletElement element)
   {
      NodeList children = parent.getChildNodes();
      for (int i = 0; i < children.getLength(); i++)
      {
         Node child = children.item(i);
         if (child.getNodeName().equals("element")//$NON-NLS-1$
         )
         {

            // Parse the task
            XDocletElement elmt = new XDocletElement();
            elmt.readFromXml(child, false);
            element.addNode(elmt);
            this.elements.put(elmt.getId(), elmt);

            // Parse the element inside elements
            this.parseElement(child, elmt);
         }
      }
   }
}
