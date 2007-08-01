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
package org.jboss.ide.eclipse.packaging.core.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.util.ProjectUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingFolder extends PackagingFile
{
   /** Description of the Field */
   private String excludes = "";//$NON-NLS-1$

   /** Description of the Field */
   private String includes = "";//$NON-NLS-1$

   /**Constructor for the XDocletTask object */
   public PackagingFolder()
   {
      super();
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    * @see      java.lang.Object#clone()
    */
   public Object clone()
   {
      PackagingFolder folder = new PackagingFolder();

      folder.setUsed(this.isUsed());
      folder.setPrefix(this.getPrefix());
      if (this.isLocal())
      {
         folder.setProject(this.getProject());
         folder.setProjectLocation(this.getProjectLocation());
      }
      else
      {
         folder.setLocation(this.getLocation());
      }
      folder.setIncludes(this.getIncludes());
      folder.setExcludes(this.getExcludes());

      return folder;
   }

   /**
    * @return   String
    */
   public String getExcludes()
   {
      return this.excludes;
   }

   /**
    * @return   String
    */
   public String getIncludes()
   {
      return this.includes;
   }

   /**
    * Description of the Method
    *
    * @param node       Description of the Parameter
    * @param recursive  Description of the Parameter
    * @see              xdoclet.ide.eclipse.configuration.model.IXMLSerializable#readFromXml(org.w3c.dom.Node, boolean)
    */
   public void readFromXml(Node node, boolean recursive)
   {
      Element element = (Element) node;
      super.readFromXml(node, recursive);
      this.setIncludes(element.getAttribute("includes"));//$NON-NLS-1$
      this.setExcludes(element.getAttribute("excludes"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param archive  Description of the Parameter
    * @return         Description of the Return Value
    */
   public IResource resolve(PackagingArchive archive)
   {
      IResource resource = null;
      if ("".equals(this.getProject())//$NON-NLS-1$
      )
      {
         String prj = archive.getProject().getName();
         resource = ProjectUtil.getFolder(prj + IPath.SEPARATOR + this.getProjectLocation());
      }
      else
      {
         resource = ProjectUtil.getFolder(this.getProject() + IPath.SEPARATOR + this.getProjectLocation());
      }
      return resource;
   }

   /**
    * Sets the excludes.
    *
    * @param excludes  The excludes to set
    */
   public void setExcludes(String excludes)
   {
      this.excludes = excludes;
   }

   /**
    * Sets the includes.
    *
    * @param includes  The includes to set
    */
   public void setIncludes(String includes)
   {
      this.includes = includes;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      StringBuffer buffer = new StringBuffer();

      if (this.isLocal())
      {
         buffer.append(this.resolve((PackagingArchive) this.getParent()).getFullPath().toString());
      }
      else
      {
         buffer.append(this.getLocation());
      }

      if (!"".equals(getPrefix())//$NON-NLS-1$
      )
      {
         buffer.append(" -> ");//$NON-NLS-1$
         buffer.append(this.getPrefix());
      }

      return buffer.toString();
   }

   /**
    * Description of the Method
    *
    * @param doc   Description of the Parameter
    * @param node  Description of the Parameter
    */
   public void writeToXml(Document doc, Node node)
   {
      Element element = doc.createElement("folder");//$NON-NLS-1$
      node.appendChild(element);

      element.setAttribute("prefix", this.getPrefix());//$NON-NLS-1$
      element.setAttribute("includes", this.getIncludes());//$NON-NLS-1$
      element.setAttribute("excludes", this.getExcludes());//$NON-NLS-1$
      if (this.isLocal())
      {
         element.setAttribute("project", this.getProject());//$NON-NLS-1$
         element.setAttribute("projectLocation", this.getProjectLocation());//$NON-NLS-1$
         element
               .setAttribute(
                     "name", this.getProjectLocation().substring(this.getProjectLocation().lastIndexOf("" + IPath.SEPARATOR) + 1));//$NON-NLS-1$ //$NON-NLS-2$

         String projectName = ((PackagingArchive) this.getParent()).getProject().getName();
         String absLocation;
         if ("".equals(this.getProject())//$NON-NLS-1$
         )
         {
            absLocation = computeLocation(projectName, projectName, this.getProjectLocation());
         }
         else
         {
            absLocation = computeLocation(projectName, this.getProject(), this.getProjectLocation());
         }
         element.setAttribute("location", absLocation.toString());//$NON-NLS-1$
      }
      else
      {
         element.setAttribute("location", this.getLocation());//$NON-NLS-1$
      }
      element.setAttribute("used", "" + this.isUsed());//$NON-NLS-1$ //$NON-NLS-2$
   }

   /**
    * Description of the Method
    *
    * @param pathRelativeToWorkspace  Description of the Parameter
    * @return                         Description of the Return Value
    */
   protected IResource extractResource(String pathRelativeToWorkspace)
   {
      return ProjectUtil.getFolder(pathRelativeToWorkspace);
   }
}
