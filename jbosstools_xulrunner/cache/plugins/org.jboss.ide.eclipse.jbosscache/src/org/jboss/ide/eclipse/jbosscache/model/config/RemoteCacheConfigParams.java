/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.config;

import java.util.ArrayList;
import java.util.List;

public class RemoteCacheConfigParams
{
   private String url;
   private String port;
   private String jndi;
   private List jarList = new ArrayList();
   
   public RemoteCacheConfigParams(){
      
   }
   
   public RemoteCacheConfigParams(String url,String port,String jndi){
      this.url = url;
      this.port = port;
      this.jndi = jndi;
   }

   public String getJndi()
   {
      return jndi;
   }

   public void setJndi(String jndi)
   {
      this.jndi = jndi;
   }

   public String getPort()
   {
      return port;
   }

   public void setPort(String port)
   {
      this.port = port;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
   }

   public List getJarList()
   {
      return jarList;
   }

   public void setJarList(List jarList)
   {
      this.jarList = jarList;
   }
   
   

}
