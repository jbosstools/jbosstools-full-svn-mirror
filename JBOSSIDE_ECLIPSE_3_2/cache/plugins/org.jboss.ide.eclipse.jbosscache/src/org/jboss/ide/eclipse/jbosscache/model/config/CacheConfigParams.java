/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.config;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents specific configuration information with cache instance
 * <p>
 * This class holds the information on cache configuration file directory and related jar locations
 * </p>
 * @author Gurkaner
 */
public class CacheConfigParams
{

   /**Directory of the *-service.xml file of the cache*/
   private String confDirectoryPath;

   /**Cache jar locations*/
   private List confJarUrls;

   /**
    * Default Constructor
    *
    */
   public CacheConfigParams()
   {
      init();
   }

   private void init()
   {
      if (confJarUrls == null)
      {
         confJarUrls = new ArrayList();
      }
   }

   public CacheConfigParams(String directoryName, List jars)
   {
      this.confDirectoryPath = directoryName;
      this.confJarUrls = jars;
   }

   public String getConfDirectoryPath()
   {
      return confDirectoryPath;
   }

   public void setConfDirectoryPath(String confDirectoryPath)
   {
      this.confDirectoryPath = confDirectoryPath;
   }

   public List getConfJarUrls()
   {
      return confJarUrls;
   }

   public void setConfJarUrls(List confJarUrls)
   {
      this.confJarUrls = confJarUrls;
   }

}