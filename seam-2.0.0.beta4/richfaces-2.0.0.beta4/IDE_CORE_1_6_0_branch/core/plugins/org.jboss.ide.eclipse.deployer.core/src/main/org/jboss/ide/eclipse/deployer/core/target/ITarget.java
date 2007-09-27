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
package org.jboss.ide.eclipse.deployer.core.target;

import org.eclipse.core.resources.IResource;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface ITarget extends Cloneable
{
   /** Description of the Field */
   public final static int COMPLETE = 0;

   /** Description of the Field */
   public final static int FAILED = 1;

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public ITarget cloneTarget();

   /**
    * Gets the name attribute of the IDeploymentTarget object
    *
    * @return   The name value
    */
   public String getName();

   /**
    * Gets the description attribute of the ITarget object
    *
    * @return   The description value
    */
   public String getDescription();

   /**
    * Sets the name attribute of the IDeploymentTarget object
    *
    * @param name  The new name value
    */
   public void setName(String name);

   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void deploy(IResource resource) throws DeploymentException;

   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void undeploy(IResource resource) throws DeploymentException;

   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void redeploy(IResource resource) throws DeploymentException;

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String getParameters();

   /**
    * Description of the Method
    *
    * @param parameters  Description of the Parameter
    */
   public void setParameters(String parameters);
}
