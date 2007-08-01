/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.core.target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.resources.IResource;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.DeployerCoreMessages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class LocalMainDeployer implements ITarget
{
   /** Description of the Field */
   protected String name;
   /** Description of the Field */
   protected String url;


   /** Constructor for the DeploymentTarget object */
   protected LocalMainDeployer()
   {
      this.name = "";//$NON-NLS-1$
      this.url = "";//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Object clone()
   {
      LocalMainDeployer target = new LocalMainDeployer();

      target.setName(this.name);
      target.setUrl(this.url);

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
      try
      {
         URL source = resource.getLocation().toFile().toURL();

         String request = this.buildRequest("deploy");//$NON-NLS-1$
         request = request + source.toString();

         String response = this.sendHttpRequest(request);
         if (!response.startsWith("" + HttpURLConnection.HTTP_OK)//$NON-NLS-1$
         )
         {
            throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.deploy.failed.text1") //$NON-NLS-1$
            + response);
         }
      }
      catch (MalformedURLException mfue)
      {
         AbstractPlugin.logError("Cannot build URL " + resource, mfue);//$NON-NLS-1$
         throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.deploy.failed.text2") //$NON-NLS-1$
         + resource.getLocation().toFile()
            + DeployerCoreMessages.getString("LocalMainDeployer.action.deploy.failed.text3"));//$NON-NLS-1$
      }
      catch (IOException ioe)
      {
         AbstractPlugin.logError("Cannot deploy resource " + resource, ioe);//$NON-NLS-1$
         throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.deploy.failed.text4") //$NON-NLS-1$
         + ioe.getMessage());
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
      if (obj instanceof LocalMainDeployer)
      {
         LocalMainDeployer target = (LocalMainDeployer) obj;
         boolean result = this.getName().equals(target.getName());
         result = result && this.getUrl().equals(target.getUrl());
         return result;
      }
      return false;
   }


   /**
    * Gets the description attribute of the LocalMainDeployer object
    *
    * @return   The description value
    */
   public String getDescription()
   {
      return DeployerCoreMessages.getString("LocalMainDeployer.target.description");//$NON-NLS-1$
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
      return this.getUrl();
   }


   /**
    * @return
    */
   public String getUrl()
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
      hashcode = 37 * hashcode + this.getUrl().hashCode();
      return hashcode;
   }


   /**
    * Description of the Method
    *
    * @exception DeploymentException  Description of the Exception
    * @deprecated
    */
   public void ping()
      throws DeploymentException
   {
      try
      {
         this.sendHttpRequest(this.getUrl());
      }
      catch (IOException ioe)
      {
         throw new DeploymentException(ioe.getMessage());
      }
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
      try
      {
         URL source = resource.getLocation().toFile().toURL();

         String request = this.buildRequest("redeploy");//$NON-NLS-1$
         request = request + source.toString();

         String response = this.sendHttpRequest(request);
         if (!response.startsWith("" + HttpURLConnection.HTTP_OK)//$NON-NLS-1$
         )
         {
            throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.redeploy.failed.text1") //$NON-NLS-1$
            + response);
         }
      }
      catch (MalformedURLException mfue)
      {
         AbstractPlugin.logError("Cannot build URL " + resource, mfue);//$NON-NLS-1$
         throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.redeploy.failed.text2") //$NON-NLS-1$
         + resource.getLocation().toFile()
            + DeployerCoreMessages.getString("LocalMainDeployer.action.redeploy.failed.text3"));//$NON-NLS-1$
      }
      catch (IOException ioe)
      {
         AbstractPlugin.logError("Cannot redeploy resource " + resource, ioe);//$NON-NLS-1$
         throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.redeploy.failed.text4") //$NON-NLS-1$
         + ioe.getMessage());
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
      this.setUrl(parameters);
   }


   /**
    * @param url  The new url value
    */
   public void setUrl(String url)
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

      try
      {
         URL u = new URL(this.url);

         result.append(" [");//$NON-NLS-1$
         result.append(u.getHost());
         result.append(":");//$NON-NLS-1$
         result.append(u.getPort());
         result.append("]");//$NON-NLS-1$
      }
      catch (MalformedURLException mue)
      {
         // Do nothing
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
      try
      {
         URL source = resource.getLocation().toFile().toURL();

         String request = this.buildRequest("undeploy");//$NON-NLS-1$
         request = request + source.toString();

         String response = this.sendHttpRequest(request);
         if (!response.startsWith("" + HttpURLConnection.HTTP_OK)//$NON-NLS-1$
         )
         {
            throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.undeploy.failed.text1") //$NON-NLS-1$
            + response);
         }
      }
      catch (MalformedURLException mfue)
      {
         AbstractPlugin.logError("Cannot build URL " + resource, mfue);//$NON-NLS-1$
         throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.undeploy.failed.text2") //$NON-NLS-1$
         + resource.getLocation().toFile()
            + DeployerCoreMessages.getString("LocalMainDeployer.action.undeploy.failed.text3"));//$NON-NLS-1$
      }
      catch (IOException ioe)
      {
         AbstractPlugin.logError("Cannot undeploy resource " + resource, ioe);//$NON-NLS-1$
         throw new DeploymentException(DeployerCoreMessages.getString("LocalMainDeployer.action.undeploy.failed.text4") //$NON-NLS-1$
         + ioe.getMessage());
      }
   }


   /**
    * Description of the Method
    *
    * @param operation  Description of the Parameter
    * @return           Description of the Return Value
    */
   private String buildRequest(String operation)
   {
      return MessageFormat.format(this.getUrl(), new Object[]
         {operation});
   }


   /**
    * Description of the Method
    *
    * @param request          Description of the Parameter
    * @return                 Description of the Return Value
    * @exception IOException  Description of the Exception
    */
   private String sendHttpRequest(String request)
      throws IOException
   {
      StringBuffer response = new StringBuffer();

      URL url = new URL(request);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.connect();
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null)
      {
         response.append(line);
         response.append("\n");//$NON-NLS-1$
      }
      int code = connection.getResponseCode();
      connection.disconnect();

      return code + "\n" + response.toString();//$NON-NLS-1$
   }
}
