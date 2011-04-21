/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   public void deploy(IResource resource)
      throws DeploymentException;


   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void undeploy(IResource resource)
      throws DeploymentException;


   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void redeploy(IResource resource)
      throws DeploymentException;


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
