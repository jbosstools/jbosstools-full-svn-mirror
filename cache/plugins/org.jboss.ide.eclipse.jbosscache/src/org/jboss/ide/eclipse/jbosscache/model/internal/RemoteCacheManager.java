/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.cache.aop.TreeCacheAopMBean;
import org.jboss.cache.interceptors.Interceptor;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.RemoteCacheConfigParams;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

public class RemoteCacheManager
{
   private static InitialContext ctx;
   private ICacheRootInstance rootInstance;
   private RemoteCacheConfigParams params;
   private TreeCacheMBean treeCacheMBean;
   private TreeCacheAopMBean treeCacheAopMBean;
   private boolean isAop = false;
   private ClassLoader managerLoader;
   private static MBeanServerConnection con;  
   
   public RemoteCacheManager(ICacheRootInstance rootInstance){
      this.rootInstance = rootInstance;
      this.params = rootInstance.getRemoteCacheConfigParams();
      
      if(rootInstance.getCacheType().equals(ICacheConstants.JBOSS_CACHE_TREE_CACHE_AOP))
         this.isAop = true;
   }

   public void initiliazeContenxt() throws Exception 
   {
      Properties prop = new Properties();
      try{
         
         prop.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
         prop.put("java.naming.provider.url","jnp://"+params.getUrl()+":"+params.getPort());
         prop.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
         
         if(ctx == null)
        	 ctx = new InitialContext(prop);
         
         connectToRemoteCache();
         
      }catch(Exception e){
         e.printStackTrace();
         throw e;
      }
   }
   
   private void connectToRemoteCache() throws Exception{
      try{
         Object remoteCacheMBean = ctx.lookup(params.getJndi());
         if(rootInstance.getCacheType().equals(ICacheConstants.JBOSS_CACHE_TREE_CACHE))
         {
            treeCacheMBean = (TreeCacheMBean)remoteCacheMBean;
         }
         else
         {
            treeCacheAopMBean = (TreeCacheAopMBean)remoteCacheMBean;
         }
         
      }catch(Exception e){
         e.printStackTrace();
         throw e;
      }
   }
   
   
   public  void setChildNodes(String fqn) throws Exception{
      TreeCacheMBean mbean = getTreeCacheMBean();
      
      rootInstance.setRootChilds(null);
      
      Set childNodes = mbean.getChildrenNames(Fqn.fromString(fqn));
      if(childNodes != null)
      {
         Iterator it = childNodes.iterator();
         for(int i=0;it.hasNext();i++)
         {
            String nodeName = it.next().toString();
            if (!nodeName.equals(ICacheConstants.JBOSS_INTERNAL)){
               if(!fqn.equals(ICacheConstants.SEPERATOR))
                  addTreeNodes(this, rootInstance, nodeName, fqn +ICacheConstants.SEPERATOR+ nodeName);
               else
                  addTreeNodes(this, rootInstance, nodeName,ICacheConstants.SEPERATOR+ nodeName);
            }
         }
      }
   }
   
   public Set getKeys_(String fqn) throws Exception{
      ClassLoader current = Thread.currentThread().getContextClassLoader();
      try{
         Thread.currentThread().setContextClassLoader(getManagerLoader());
         TreeCacheMBean mbean = getTreeCacheMBean();
         return mbean.getKeys(Fqn.fromString(fqn));
      }finally{
         Thread.currentThread().setContextClassLoader(current);
      }
   }
   
   public Object getValue_(String fqn,Object key) throws Exception{
      ClassLoader current = Thread.currentThread().getContextClassLoader();
      try{
         Thread.currentThread().setContextClassLoader(getManagerLoader());
         TreeCacheMBean mbean = getTreeCacheMBean();
         return mbean.get(Fqn.fromString(fqn),key);
      
      }finally{
         Thread.currentThread().setContextClassLoader(current);
      }

   }
   
   /**
    * Add cache nodes to the tree
    * @param manager
    * @param obj Parent obj (1. ICacheRootInstance or ICacheInstance)
    * @param nodeName
    * @throws Exception
    */
   private void addTreeNodes(RemoteCacheManager manager, Object obj, String nodeName, String fqn) throws Exception
   {
      ICacheRootInstance cacheRootInstance = null;
      ICacheInstance cacheInstance = null;
      ICacheInstance childInstance = null;
      Set childSet = null;
      Iterator it = null;
      String childName = null;

      //We add tree cache node roots below the CacheRootInstance
      if (obj instanceof ICacheRootInstance)
      {
         cacheRootInstance = (ICacheRootInstance) obj;
         cacheInstance = CacheInstanceFactory.getCacheInstance(nodeName, fqn, null);
         cacheRootInstance.addRootChild(cacheInstance);

         childSet = manager.getTreeCacheMBean().getChildrenNames(fqn);
         if (childSet != null)
         {
            if (!childSet.isEmpty())
            {
               it = childSet.iterator();
               while (it.hasNext())
               {
                  childName = it.next().toString();
                  addTreeNodes(manager, cacheInstance, childName, fqn + ICacheConstants.SEPERATOR + childName);
               }
            }
         }
      }//end of outer if
      else if (obj instanceof ICacheInstance)
      {
         cacheInstance = (ICacheInstance) obj;
         childInstance = CacheInstanceFactory.getCacheInstance(nodeName, fqn, cacheInstance);
         cacheInstance.addInstanceChild(childInstance);
         if (cacheInstance.getRootInstance() != null)
            childInstance.setRootInstance(cacheInstance.getRootInstance());

         Set newSet = manager.getTreeCacheMBean().getChildrenNames(fqn);
         if (newSet != null)
         {
            if (!newSet.isEmpty())
            {
               Iterator itNext = newSet.iterator();
               while (itNext.hasNext())
               {
                  childName = itNext.next().toString();
                  addTreeNodes(manager, childInstance, childName, fqn + ICacheConstants.SEPERATOR + childName);
               }
            }
         }
      }
   }

   
   private TreeCacheMBean getTreeCacheMBean(){
      if(isAop)
         return this.treeCacheAopMBean;
      else
         return this.treeCacheMBean;
   }

   public ClassLoader getManagerLoader()
   {
      return managerLoader;
   }

   public void setManagerLoader(ClassLoader managerLoader)
   {
      this.managerLoader = managerLoader;
   }
   
   public boolean isUseMbeanAttributes(){
	   TreeCacheMBean mbean = getTreeCacheMBean();
	   
	   return mbean.getUseInterceptorMbeans();
   }
   
      
   public Map getStatisticsAttribute(){

	   Map map = new HashMap();
	   
	   try{
		    if(con == null)
		    	con = (MBeanServerConnection)ctx.lookup("jmx/invoker/RMIAdaptor");
		   String [] names = ICacheConstants.STAT_INTERCEPTOR_NAMES;
		   
		   for(int i=0;i<names.length;i++){
			   String tmp = names[i];
			   try{
				   ObjectInstance instance = con.getObjectInstance(new ObjectName("jboss.cache:service=TreeCache,treecache-interceptor="+tmp));
				   map.put(instance.getClassName().substring(29),con.invoke(instance.getObjectName(),"dumpStatistics", new Object[]{},new String[]{}));
			   }catch(Exception e){
				   continue;
			   }
		   }
	   }catch(Exception e){
		   
	   }	   
	   
	   return map;
	   
   }
   
   public static void main(String[] args) {
	System.out.println("org.jboss.cache.interceptors.CacheLoaderInterceptor".substring(29));
}
   
}