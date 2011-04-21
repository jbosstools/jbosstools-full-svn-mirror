/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.core.target;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.DeployerCoreMessages;
import org.jboss.ide.eclipse.deployer.core.util.FileUtils;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FileSystemCopy implements ITarget
{
   private String name;
   private URL url;


   /** Constructor for the DeploymentTarget object */
   public FileSystemCopy()
   {
      this.name = DeployerCoreMessages.getString("FileSystemCopy.target.name.default");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Object clone()
   {
      FileSystemCopy target = new FileSystemCopy();
      target.setName(this.name);
      target.setURL(this.url);
      return target;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public ITarget cloneTarget()
   {
      return (ITarget) this.clone();
   }


   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void deploy(IResource resource)
      throws DeploymentException
   {
      URL url = this.getURL();
      File directory = new File(url.getFile());

      File source = resource.getLocation().toFile();
      File dest = new File(directory, resource.getName());

      switch (resource.getType())
      {
         case IResource.FILE:
            try
            {
               FileUtils.copyFile(source, dest);
            }
            catch (IOException ioe)
            {
               AbstractPlugin.logError("Cannot deploy resource " + resource, ioe);//$NON-NLS-1$
               throw new DeploymentException(DeployerCoreMessages.getString("FileSystemCopy.action.deploy.failed.text1") //$NON-NLS-1$
               + source.toString()
                  + DeployerCoreMessages.getString("FileSystemCopy.action.deploy.failed.text2"));//$NON-NLS-1$
            }
            break;
         case IResource.FOLDER:
            try
            {
               FileUtils.copyDirectory(source, dest);
            }
            catch (IOException ioe)
            {
               AbstractPlugin.logError("Cannot deploy resource " + resource, ioe);//$NON-NLS-1$
               throw new DeploymentException(DeployerCoreMessages.getString("FileSystemCopy.action.deploy.failed.text1") //$NON-NLS-1$
               + source.toString()
                  + DeployerCoreMessages.getString("FileSystemCopy.action.deploy.failed.text3"));//$NON-NLS-1$
            }
            break;
         default:
      }
   }


   /**
    * Description of the Method
    *
    * @param obj  Description of the Parameter
    * @return     Description of the Return Value
    */
   public boolean equals(Object obj)
   {
      if (obj instanceof FileSystemCopy)
      {
         FileSystemCopy target = (FileSystemCopy) obj;
         boolean result = this.getName().equals(target.getName());
         if (this.url != null)
         {
            result = result && this.getURL().equals(target.getURL());
         }
         return result;
      }
      return false;
   }


   /**
    * Gets the description attribute of the FileSystemCopy object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DeployerCoreMessages.getString("FileSystemCopy.target.description");//$NON-NLS-1$
   }


   /**
    * Gets the name attribute of the DeploymentTarget object
    *
    * @return   The name value
    */
   public String getName()
   {
      return this.name;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String getParameters()
   {
      if (this.url != null)
      {
         return this.url.toString();
      }
      return "";//$NON-NLS-1$
   }


   /**
    * Gets the uRL attribute of the DeploymentTarget object
    *
    * @return   The uRL value
    */
   public URL getURL()
   {
      return this.url;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public int hashCode()
   {
      int hashcode = this.name.hashCode();
      hashcode = 37 * hashcode + this.url.hashCode();
      return hashcode;
   }


   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void redeploy(IResource resource)
      throws DeploymentException
   {
      URL url = this.getURL();
      File directory = new File(url.getFile());

      File source = resource.getLocation().toFile();
      File dest = new File(directory, resource.getName());

      switch (resource.getType())
      {
         case IResource.FILE:
            try
            {
               // TODO : overwrite must be part of preference
               FileUtils.copyFile(source, dest, false);
            }
            catch (IOException ioe)
            {
               AbstractPlugin.logError("Cannot redeploy resource " + resource, ioe);//$NON-NLS-1$
               throw new DeploymentException(DeployerCoreMessages.getString("FileSystemCopy.action.redeploy.failed.text1") //$NON-NLS-1$
               + source.toString()
                  + DeployerCoreMessages.getString("FileSystemCopy.action.redeploy.failed.text2"));//$NON-NLS-1$
            }
            break;
         case IResource.FOLDER:
            try
            {
               // TODO : overwrite must be part of preference
               FileUtils.copyDirectory(source, dest, false);
            }
            catch (IOException ioe)
            {
               AbstractPlugin.logError("Cannot redeploy resource " + resource, ioe);//$NON-NLS-1$
               throw new DeploymentException(DeployerCoreMessages.getString("FileSystemCopy.action.redeploy.failed.text1") //$NON-NLS-1$
               + source.toString()
                  + DeployerCoreMessages.getString("FileSystemCopy.action.redeploy.failed.text3"));//$NON-NLS-1$
            }
            break;
         default:
      }
   }


   /**
    * Sets the name.
    *
    * @param name  The name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }


   /**
    * Description of the Method
    *
    * @param parameters  Description of the Parameter
    */
   public void setParameters(String parameters)
   {
      if ("".equals(parameters)//$NON-NLS-1$
      )
      {
         return;
      }
      try
      {
         URL url = new URL(parameters);
         this.setURL(url);
      }
      catch (MalformedURLException mfue)
      {
         AbstractPlugin.logError("Error while setting URL in FileSystemTarget", mfue);//$NON-NLS-1$
      }
   }


   /**
    * Sets the url.
    *
    * @param url  The url to set
    */
   public void setURL(URL url)
   {
      this.url = url;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      StringBuffer result = new StringBuffer();
      result.append(this.name);
      if (this.url != null)
      {
         result.append(" [");//$NON-NLS-1$
         result.append(this.url.toString());
         result.append("]");//$NON-NLS-1$
      }
      return result.toString();
   }


   /**
    * Description of the Method
    *
    * @param resource                 Description of the Parameter
    * @exception DeploymentException  Description of the Exception
    */
   public void undeploy(IResource resource)
      throws DeploymentException
   {
      URL url = this.getURL();
      File directory = new File(url.getFile());

      File source = new File(directory, resource.getName());

      switch (resource.getType())
      {
         case IResource.FILE:
            if (!source.delete())
            {
               AbstractPlugin.logError("Cannot undeploy resource " + resource);//$NON-NLS-1$
               throw new DeploymentException(DeployerCoreMessages.getString("FileSystemCopy.action.undeploy.failed.text1") //$NON-NLS-1$
               + source.toString()
                  + DeployerCoreMessages.getString("FileSystemCopy.action.undeploy.failed.text2"));//$NON-NLS-1$
            }
            break;
         case IResource.FOLDER:
            try
            {
               FileUtils.deleteDirectory(source);
            }
            catch (IOException ioe)
            {
               AbstractPlugin.logError("Cannot undeploy resource " + resource, ioe);//$NON-NLS-1$
               throw new DeploymentException(DeployerCoreMessages.getString("FileSystemCopy.action.undeploy.failed.text1") //$NON-NLS-1$
               + source.toString()
                  + DeployerCoreMessages.getString("FileSystemCopy.action.undeploy.failed.text3"));//$NON-NLS-1$
            }
            break;
         default:
            AbstractPlugin.logError("Unknown resource type " + resource);//$NON-NLS-1$
      }
   }
}
