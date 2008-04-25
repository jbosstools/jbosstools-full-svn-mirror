package org.jboss.ide.eclipse.jbosscache.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CacheAttributeMatcher {

	private String[] namedProperty = {"BuddyReplicationConfig",
			"CacheMode",
			"ClusterName",
			"EvictionPolicyClass",
			"EvictionPolicyConfig",
			"CacheLoaderConfiguration",
			"FetchInMemoryState",
			"InitialStateRetrievalTimeout",
			"InactiveOnStartup",
			"NodeLockingScheme",
			"IsolationLevel",
			"LockAcquisitionTimeout",
			"ReplicationVersion",
			"ReplQueueInterval",
			"SyncReplTimeout",
			"ReplQueueMaxElements",
			"StateTransferVersion",
			"TransactionManagerLookupClass",
			"UseInterceptorMbeans",
			"UseRegionBasedMarshalling",
			"UseReplQueue",
			"wakeUpIntervalSeconds",
			"maxNodes",
			"timeToLiveSeconds",
			"maxAgeSeconds",
	};
	
	private String [] SERVER = {"classpath","mbean"};
	private String [] MBEAN = {"depends","attribute"};
	private String [] ATTRIBUTE = {"config"};
	private String [] CONFIG = {"attribute","buddyReplicationEnabled","buddyLocatorClass","buddyLocatorProperties","buddyPoolName","buddyCommunicationTimeout","autoDataGravitation","dataGravitationRemoveOnFind","dataGravitationSearchBackupTrees","passivation","preload","shared","cacheloader","region"};
	private String [] CACHELOADER = {"class","properties","async","fetchPersistentState","ignoreModifications","purgeOnStartup"};
	private String [] REGION ={"attribute"};
	
	
	public CacheAttributeMatcher(){
		
	}
	
	public List findMatchingPropertyTypes(String type){
		List l = new ArrayList();
		for (int i = 0; i < namedProperty.length; i++) {
			String element = namedProperty[i];
			if(element.startsWith(type) ) {
				l.add(element);
			} 
		}
		return l;
	}
	
	public List findTagMatcher(String parentName){
		List l = new ArrayList();
		if(parentName.equals("server")){
			for (int i = 0; i < SERVER.length; i++) {
				String element = SERVER[i];
				l.add(element);
			}
			
			return l;
		}
		if(parentName.equals("mbean")){
			for (int i = 0; i < MBEAN.length; i++) {
				String element = MBEAN[i];
				l.add(element);
			}
			
			return l;
		}
		if(parentName.equals("attribute")){
			for (int i = 0; i < ATTRIBUTE.length; i++) {
				String element = ATTRIBUTE[i];
				l.add(element);
			}
			
			return l;
		}
		if(parentName.equals("config")){
			for (int i = 0; i < CONFIG.length; i++) {
				String element = CONFIG[i];
				l.add(element);
			}
			
			return l;
		}
		
		if(parentName.equals("cacheloader")){
			for (int i = 0; i < CACHELOADER.length; i++) {
				String element = CACHELOADER[i];
				l.add(element);
			}
			
			return l;
		}

		if(parentName.equals("region")){
			for (int i = 0; i < REGION.length; i++) {
				String element = REGION[i];
				l.add(element);
			}
			
			return l;
		}

		return Collections.EMPTY_LIST;

	}
	
	public List findTagMatcher(String parentName,String type){
		List l = new ArrayList();
		if(parentName.equals("server")){
			for (int i = 0; i < SERVER.length; i++) {
				String element = SERVER[i];
				if(element.startsWith(type) ) {
					l.add(element);
				} 

			}
			
			return l;
		}
		if(parentName.equals("mbean")){
			for (int i = 0; i < MBEAN.length; i++) {
				String element = MBEAN[i];
				if(element.startsWith(type) ) {
					l.add(element);
				} 

			}
			
			return l;
		}
		if(parentName.equals("attribute")){
			for (int i = 0; i < ATTRIBUTE.length; i++) {
				String element = ATTRIBUTE[i];
				if(element.startsWith(type) ) {
					l.add(element);
				} 

			}
			
			return l;
		}
		if(parentName.equals("config")){
			for (int i = 0; i < CONFIG.length; i++) {
				String element = CONFIG[i];
				if(element.startsWith(type) ) {
					l.add(element);
				} 

			}
			
			return l;
		}
		
		if(parentName.equals("cacheloader")){
			for (int i = 0; i < CACHELOADER.length; i++) {
				String element = CACHELOADER[i];
				if(element.startsWith(type) ) {
					l.add(element);
				} 

			}
			
			return l;
		}

		if(parentName.equals("region")){
			for (int i = 0; i < REGION.length; i++) {
				String element = REGION[i];
				if(element.startsWith(type) ) {
					l.add(element);
				} 

			}
			
			return l;
		}

		return Collections.EMPTY_LIST;

	}


}
