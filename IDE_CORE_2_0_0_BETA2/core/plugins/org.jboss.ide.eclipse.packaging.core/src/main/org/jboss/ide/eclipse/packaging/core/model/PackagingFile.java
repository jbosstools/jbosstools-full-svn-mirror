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
package org.jboss.ide.eclipse.packaging.core.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.util.ProjectUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingFile extends PackagingData
{
   /** Description of the Field */
   private boolean local = false;

   /** Description of the Field */
   private String location = "";//$NON-NLS-1$

   /** Description of the Field */
   private String prefix = "";//$NON-NLS-1$

   /** Description of the Field */
   private String project = "";//$NON-NLS-1$

   /** Description of the Field */
   private String projectLocation = "";//$NON-NLS-1$

   /** Constructor for the XDocletTask object */
   public PackagingFile()
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
      PackagingFile file = new PackagingFile();

      file.setUsed(this.isUsed());
      file.setPrefix(this.getPrefix());
      if (this.isLocal())
      {
         file.setProject(this.getProject());
         file.setProjectLocation(this.getProjectLocation());
      }
      else
      {
         file.setLocation(this.getLocation());
      }

      return file;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public PackagingData cloneData()
   {
      return (PackagingData) this.clone();
   }

   /**
    * Gets the destination attribute of the PackagingConfiguration object
    *
    * @return   The destination value
    */
   public String getLocation()
   {
      return this.location;
   }

   /**
    * Gets the destination attribute of the PackagingConfiguration object
    *
    * @return   The destination value
    */
   public String getPrefix()
   {
      return this.prefix;
   }

   /**
    * @return   String
    */
   public String getProject()
   {
      return project;
   }

   /**
    * @return   String
    */
   public String getProjectLocation()
   {
      return projectLocation;
   }

   /**
    * Gets the local attribute of the PackagingFile object
    *
    * @return   The local value
    */
   public boolean isLocal()
   {
      return this.local;
   }

   /**
    * Description of the Method
    *
    * @param node       Description of the Parameter
    * @param recursive  Description of the Parameter
    * @see              xdoclet.ide.eclipse.configuration.model.IXMLSerializable#readFromXml(org.w3c.dom.Node,
    *      boolean)
    */
   public void readFromXml(Node node, boolean recursive)
   {
      Element element = (Element) node;
      super.readFromXml(node, recursive);
      this.setPrefix(element.getAttribute("prefix"));//$NON-NLS-1$

      String aProject = element.getAttribute("project");//$NON-NLS-1$
      String aProjectLocation = element.getAttribute("projectLocation");//$NON-NLS-1$
      if ((aProjectLocation != null) && (aProject != null) && (!"".equals(aProjectLocation))//$NON-NLS-1$
      )
      {
         this.setProjectLocation(aProjectLocation);
         if (!"".equals(aProject)//$NON-NLS-1$
         )
         {
            this.setProject(aProject);
         }
      }
      else
      {
         String location = element.getAttribute("location");//$NON-NLS-1$
         this.setLocation(location);
      }
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
         resource = ProjectUtil.getFile(prj + IPath.SEPARATOR + this.getProjectLocation());
      }
      else
      {
         resource = ProjectUtil.getFile(this.getProject() + IPath.SEPARATOR + this.getProjectLocation());
      }
      return resource;
   }

   /**
    * Sets the local attribute of the PackagingFile object
    *
    * @param local  The new local value
    */
   public void setLocal(boolean local)
   {
      this.local = local;
   }

   /**
    * Sets the name.
    *
    * @param location  The new location value
    */
   public void setLocation(String location)
   {
      this.location = location;
      this.project = "";//$NON-NLS-1$
      this.projectLocation = "";//$NON-NLS-1$
      this.setLocal(false);
   }

   /**
    * Sets the prefix attribute of the PackagingFile object
    *
    * @param prefix  The new prefix value
    */
   public void setPrefix(String prefix)
   {
      this.prefix = prefix;
   }

   /**
    * Sets the project.
    *
    * @param project  The project to set
    */
   public void setProject(String project)
   {
      this.project = project;
   }

   /**
    * Sets the projectLocation.
    *
    * @param projectLocation  The projectLocation to set
    */
   public void setProjectLocation(String projectLocation)
   {
      this.projectLocation = projectLocation;
      this.location = "";//$NON-NLS-1$
      this.setLocal(true);
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
      Element element = doc.createElement("file");//$NON-NLS-1$
      node.appendChild(element);

      element.setAttribute("prefix", this.getPrefix());//$NON-NLS-1$
      if (this.isLocal())
      {
         element.setAttribute("project", this.getProject());//$NON-NLS-1$
         element.setAttribute("projectLocation", this.getProjectLocation());//$NON-NLS-1$
         element.setAttribute("name", this.getProjectLocation().substring(this.getProjectLocation().lastIndexOf("" //$NON-NLS-1$ //$NON-NLS-2$
               + IPath.SEPARATOR) + 1));

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
         element.setAttribute("name", this.getLocation().substring(this.getLocation().lastIndexOf("" //$NON-NLS-1$ //$NON-NLS-2$
               + IPath.SEPARATOR) + 1));
      }
      element.setAttribute("used", "" + this.isUsed());//$NON-NLS-1$ //$NON-NLS-2$
   }

   /**
    * Description of the Method
    *
    * @param homeProjectName         Description of the Parameter
    * @param elementProjectName      Description of the Parameter
    * @param elementProjectLocation  Description of the Parameter
    * @return                        Description of the Return Value
    */
   public static String computeLocation(String homeProjectName, String elementProjectName, String elementProjectLocation)
   {
      IProject homeProject = AbstractPlugin.getWorkspace().getRoot().getProject(homeProjectName);
      IProject elementProject = AbstractPlugin.getWorkspace().getRoot().getProject(elementProjectName);

      IPath homeProjectPath = homeProject.getLocation();
      IPath elementProjectPath = elementProject.getLocation();
      IPath result = elementProjectPath.append(new Path(elementProjectLocation));

      // The path is within the project
      if (homeProjectPath.isPrefixOf(result))
      {
         result = result.removeFirstSegments(homeProjectPath.segmentCount());
         result = result.setDevice(null);
      }
      else
      {
         // The path is within the project parent path
         if (homeProjectPath.segmentCount() > 0)
         {
            IPath parentProjectPath = homeProjectPath.removeLastSegments(1);
            if (parentProjectPath.isPrefixOf(result))
            {
               result = result.removeFirstSegments(parentProjectPath.segmentCount());
               result = result.setDevice(null);
               result = new Path("..").append(result);//$NON-NLS-1$
            }
         }
      }

      return result.toString();
   }
}
