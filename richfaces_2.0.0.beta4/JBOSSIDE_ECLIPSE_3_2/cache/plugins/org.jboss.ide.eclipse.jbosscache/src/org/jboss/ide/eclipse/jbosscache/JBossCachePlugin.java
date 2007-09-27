/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.config.RemoteCacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.utils.BundleWrapperClassLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class JBossCachePlugin extends AbstractUIPlugin
{

   //The shared instance.
   private static JBossCachePlugin plugin;

   private static ResourceBundle resourceBundle;

   private static final String CACHE_NODES = "cache-nodes";

   private static final String CACHE_NODE = "cache-node";

   private static final String CACHE_SERVICE_XML_FILE = "xml-file";

   private static final String CACHE_SERVICE_JAR_FILE = "jar-file";

   private static final String LOCATION = "location";

   private static final String NAME = "name";

   private static final String CACHE_TYPE = "type";
   
   private static final String CACHE_CONNECTION = "connection";//local or remote
   
   private static final String CACHE_REMOTE_URL = "url";
   private static final String CACHE_REMOTE_PORT = "port";
   private static final String CACHE_REMOTE_JNDI = "jndi";

   private static int numberOfInstance;

   static
   {
      resourceBundle = ResourceBundle.getBundle(ICacheConstants.CACHE_PLUGIN_UNIQUE_ID + ".JBossCache");
   }

   /**
    * The constructor.
    */
   public JBossCachePlugin()
   {
      plugin = this;
   }

   /**
    * This method is called upon plug-in activation
    */
   public void start(BundleContext context) throws Exception
   {
      super.start(context);
      
      Bundle bundle = getBundle();
      BundleWrapperClassLoader wrapperLoader = new BundleWrapperClassLoader(bundle);
            
      ISaveParticipant saveParticipant = new CacheSavingParticipant();
      ISavedState lastState = ResourcesPlugin.getWorkspace().addSaveParticipant(this, saveParticipant);
      if (lastState == null)
         return;
      IPath location = lastState.lookup(new Path("save"));
      if (location == null)
         return;
      File f = getStateLocation().append(location).toFile();
      if (f.exists())
         readStateFromFile(f);

      Platform.getAdapterManager().registerAdapters(new ICacheRootInstanceAdaptFactory(), ICacheRootInstance.class);
   }

   private class ICacheRootInstanceAdaptFactory implements IAdapterFactory
   {

      public Object getAdapter(Object adaptableObject, Class adapterType)
      {
         final Object obj = adaptableObject;
         if (adapterType == IWorkbenchAdapter.class)
            return new IWorkbenchAdapter()
            {

               public Object[] getChildren(Object o)
               {
                  // TODO Auto-generated method stub
                  return null;
               }

               public ImageDescriptor getImageDescriptor(Object object)
               {
                  // TODO Auto-generated method stub
                  return null;
               }

               public String getLabel(Object o)
               {
                  ICacheRootInstance rootInstance = (ICacheRootInstance) obj;
                  return rootInstance.getRootName();
               }

               public Object getParent(Object o)
               {
                  // TODO Auto-generated method stub
                  return null;
               }

            };
         return null;
      }

      public Class[] getAdapterList()
      {
         return new Class[]
         {IWorkbenchAdapter.class};
      }

   }

   /**
    * This method is called when the plug-in is stopped
    */
   public void stop(BundleContext context) throws Exception
   {
      super.stop(context);
      plugin = null;
   }

   /**
    * Returns the shared instance.
    */
   public static JBossCachePlugin getDefault()
   {
      return plugin;
   }

   /**
    * Returns an image descriptor for the image file at the given
    * plug-in relative path.
    *
    * @param path the path
    * @return the image descriptor
    */
   public static ImageDescriptor getImageDescriptor(String path)
   {
      return AbstractUIPlugin.imageDescriptorFromPlugin("org.jboss.ide.eclipse.jbosscache", path);
   }

   /**
    * Initialize Image Registry
    */
   protected void initializeImageRegistry(ImageRegistry registry)
   {
      ImageDescriptor imageDesc = null;
      URL url = getBundle().getEntry("/");
      try
      {
         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_ECLIPSE_GIF));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_ECLIPSE_GIF, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_RUN_EXC));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_RUN_EXC, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_TERM_SBOOK));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_TERM_SBOOK, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_NEW_CON));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_NEW_CON, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_NEWPACK_WIZ));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_NEWPACK_WIZ, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_DELETE_EDIT));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_DELETE_EDIT, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_SEARCH_REF_OBJ));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_SEARCH_REF_OBJ, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_WATCHLIST_VIEW));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_WATCHLIST_VIEW, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_EXTERNAL_JAR));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_EXTERNAL_JAR, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_EDIT_CON));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_EDIT_CON, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_CLASS_OBJ));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_CLASS_OBJ, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_FIELD_PUBLIC_OBJ));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_FIELD_PUBLIC_OBJ, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_EXPORT_WIZ));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_EXPORT_WIZ, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_IMPORT_WIZ));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_IMPORT_WIZ, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_FILE_OBJ));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_FILE_OBJ, imageDesc);
         
         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_SERVER));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_SERVER, imageDesc);
         
         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_SERVER_RUNNING));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_SERVER_RUNNING, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_SERVER_NOT_RUNNING));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_SERVER_NOT_RUNNING, imageDesc);


         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_SERVER_NAVIGATOR));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_SERVER_NAVIGATOR, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_SERVER_START_ACTION));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_SERVER_START_ACTION, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_SERVER_SHUT_DOWN_ACTOIN));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_SERVER_SHUT_DOWN_ACTOIN, imageDesc);

         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_REMOTE_CONNECT_ACTION));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_REMOTE_CONNECT_ACTION, imageDesc);
         
         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_REFRESH_NAV_ACTION));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_REFRESH_NAV_ACTION, imageDesc);
         
         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_JBOSS_GIF));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_JBOSS_GIF, imageDesc);
         
         imageDesc = ImageDescriptor.createFromURL(new URL(url, ICacheConstants.PLUGIN_IMAGE_PATH
               + ICacheConstants.IMAGE_KEY_DB16_GIF));
         getImageRegistry().put(ICacheConstants.IMAGE_KEY_DB16_GIF, imageDesc);         
         

      }
      catch (MalformedURLException e)
      {
         //Log the Exception
         IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e.getMessage(),
               e);
         getLog().log(status);
      }
   }//end of method

   /**
    * Get resource bundle of the plugin
    * @return
    */
   public static ResourceBundle getResourceBundle()
   {
      return resourceBundle;
   }

   public static Display getDisplay()
   {
      if (JBossCachePlugin.getDefault().getWorkbench().getDisplay() != null)
         return JBossCachePlugin.getDefault().getWorkbench().getDisplay();
      else
         return Display.getDefault();
   }

   /**
    * Read state from file
    * @param f
    */
   protected void readStateFromFile(File f)
   {
      try
      {
         FileReader reader = new FileReader(f);
         XMLMemento memento = XMLMemento.createReadRoot(reader);
         IMemento[] childs = memento.getChildren(CACHE_NODE);

         numberOfInstance = childs.length;

         if (childs != null)
         {
            for (int i = 0; i < childs.length; i++)
            {
               IMemento child = childs[i];
               String connectionType = child.getString(CACHE_CONNECTION);
               String cacheType = child.getString(CACHE_TYPE);
               
               if(connectionType.equals("local"))
               {
                  IMemento childXmlFileLocation = child.getChild(CACHE_SERVICE_XML_FILE);
                  String directoryName = childXmlFileLocation.getString(LOCATION);

                  ICacheRootInstance rootInstance = CacheInstanceFactory.getCacheRootInstance(child.getString(NAME),
                        directoryName);
                  rootInstance.setCacheType(cacheType);
                  CacheInstanceFactory.getCacheRootMainInstance().addRootInstanceChild(rootInstance);

                  IMemento[] childJarFileLocation = child.getChildren(CACHE_SERVICE_JAR_FILE);
                  CacheConfigParams configParams = new CacheConfigParams();
                  configParams.setConfDirectoryPath(directoryName);
                  List jarPath = new ArrayList();
                  for (int j = 0, k = childJarFileLocation.length; j < k; j++)
                  {
                     childXmlFileLocation = childJarFileLocation[j];
                     String jarLocation = childXmlFileLocation.getString(LOCATION);
                     jarPath.add(jarLocation);
                  }

                  configParams.setConfJarUrls(jarPath);
                  rootInstance.setCacheConfigParams(configParams);
               }
               else
               {
                  ICacheRootInstance rootInstance = CacheInstanceFactory.getCacheRootInstance(child.getString(NAME),null);
                  rootInstance.setCacheType(cacheType);
                  rootInstance.setRemoteCache(true);
                  CacheInstanceFactory.getCacheRootMainInstance().addRootInstanceChild(rootInstance);
                  
                  RemoteCacheConfigParams params = new RemoteCacheConfigParams();
                  IMemento remMemonto = child.getChild(CACHE_REMOTE_URL);
                  params.setUrl(remMemonto.getString(NAME));
                  remMemonto =  child.getChild(CACHE_REMOTE_PORT);
                  params.setPort(remMemonto.getString(NAME));
                  remMemonto =  child.getChild(CACHE_REMOTE_JNDI);
                  params.setJndi(remMemonto.getString(NAME));
                  
                  IMemento[] childJarFileLocation = child.getChildren(CACHE_SERVICE_JAR_FILE);
                  
                  List jarPath = new ArrayList();
                  for (int j = 0, k = childJarFileLocation.length; j < k; j++)
                  {
                     remMemonto = childJarFileLocation[j];
                     String jarLocation = remMemonto.getString(LOCATION);
                     jarPath.add(jarLocation);
                  }                  
                  
                  params.setJarList(jarPath);
                  rootInstance.setRemoteCacheConfigParams(params);
               }
            }
         }

      }
      catch (Exception e)
      {
         e.printStackTrace();
         IStatus status = new Status(IStatus.ERROR, getBundle().getSymbolicName(), IStatus.OK, e.getMessage(), e);
         getLog().log(status);
      }
   }

   /**
    * Write state to file
    * @param f
    */
   protected void writeStateToFile(File f) throws Exception
   {
      try
      {
         FileWriter writer = new FileWriter(f);

         ICacheRootInstance mainRootInstance = CacheInstanceFactory.getCacheRootMainInstance();
         List cacheRootInstances = mainRootInstance.getRootInstanceChilds();

         ICacheRootInstance rootInstance = null;
         if (cacheRootInstances != null)
         {

            XMLMemento memonto = XMLMemento.createWriteRoot(CACHE_NODES);

            for (int i = 0; i < cacheRootInstances.size(); i++)
            {
               rootInstance = (ICacheRootInstance) cacheRootInstances.get(i);
               IMemento childMemento = memonto.createChild(CACHE_NODE);
               childMemento.putString(NAME, rootInstance.getRootName());
               childMemento.putString(CACHE_TYPE, rootInstance.getCacheType());
               
               
               if(!rootInstance.isRemoteCache())
               {
                  childMemento.putString(CACHE_CONNECTION,"local");
                  IMemento nodeChildMemonto = childMemento.createChild(CACHE_SERVICE_XML_FILE);
                  nodeChildMemonto.putString(LOCATION, rootInstance.getRootConfigurationFileName());
   
                  List jarFiles = rootInstance.getCacheConfigParams().getConfJarUrls();
                  if (jarFiles != null)
                  {
                     for (int j = 0; j < jarFiles.size(); j++)
                     {
                        String jarFileLocation = (String) jarFiles.get(j);
                        nodeChildMemonto = childMemento.createChild(CACHE_SERVICE_JAR_FILE);
                        nodeChildMemonto.putString(LOCATION, jarFileLocation);
                     }
                  }
              }else
              {
                 RemoteCacheConfigParams params = rootInstance.getRemoteCacheConfigParams();
                 childMemento.putString(CACHE_CONNECTION,"remote");
                 
                 IMemento nodeChildMemonto = childMemento.createChild(CACHE_REMOTE_URL);
                 nodeChildMemonto.putString(NAME,params.getUrl());
                 
                 nodeChildMemonto = childMemento.createChild(CACHE_REMOTE_PORT);
                 nodeChildMemonto.putString(NAME,params.getPort());
                 
                 nodeChildMemonto = childMemento.createChild(CACHE_REMOTE_JNDI);
                 nodeChildMemonto.putString(NAME,params.getJndi());   
                 
                 List jarFiles = rootInstance.getRemoteCacheConfigParams().getJarList();
                 if (jarFiles != null)
                 {
                    for (int j = 0; j < jarFiles.size(); j++)
                    {
                       String jarFileLocation = (String) jarFiles.get(j);
                       nodeChildMemonto = childMemento.createChild(CACHE_SERVICE_JAR_FILE);
                       nodeChildMemonto.putString(LOCATION, jarFileLocation);
                    }
                 }
                 
              }

            }//end of for

            memonto.save(writer);
         }//end of if

      }
      catch (Exception e)
      {
         throw e;
      }
   }

   public static int getNumberOfCacheInstances()
   {
      return numberOfInstance;
   }
   
   /**
    * Get the base directory of this plugin
    */
   public String getBaseDir()
   {
      try
      {
         URL installURL = Platform.asLocalURL(JBossCachePlugin.getDefault().getBundle().getEntry("/"));//$NON-NLS-1$
         return installURL.getFile().toString();
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
      return null;
   }


}//end of class