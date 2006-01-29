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
package org.jboss.ide.eclipse.xdoclet.run.configuration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   18 mars 2003
 * @todo      Javadoc to complete
 */
public class StandardConfigurations extends Configurations
{
   /**Constructor for the StandardConfigurations object */
   public StandardConfigurations()
   {
      try
      {
         this.load();
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Can't load the generics configurations", ce);//$NON-NLS-1$
      }
   }

   /**
    * Description of the Method
    *
    * @exception CoreException  Description of the Exception
    */
   public void loadConfigurations() throws CoreException
   {
      this.load();
   }

   /**
    * Gets the contents attribute of the StandardConfigurations object
    *
    * @return                   The contents value
    * @exception CoreException  Description of the Exception
    */
   protected InputStream getContents() throws CoreException
   {
      try
      {
         URL genericsFile = XDocletRunPlugin.getDefault().find(new Path(XDocletRunPlugin.GENERICS_FILE));
         InputStream is = new BufferedInputStream(genericsFile.openStream());
         return is;
      }
      catch (IOException ioe)
      {
         throw AbstractPlugin.wrapException(ioe);
      }
   }
}
