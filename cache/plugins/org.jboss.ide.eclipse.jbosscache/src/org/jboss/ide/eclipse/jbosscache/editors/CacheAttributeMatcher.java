package org.jboss.ide.eclipse.jbosscache.editors;

import java.util.ArrayList;
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
	
	
	public CacheAttributeMatcher(){
		
	}
	
	public List findMatchingPropertyTypes(String type){
		List l = new ArrayList();
		boolean foundFirst = false;
		for (int i = 0; i < namedProperty.length; i++) {
			String element = namedProperty[i];
			if(element.startsWith(type) ) {
				foundFirst = true;
				l.add(element);
			} 
		}
		return l;
	}

}
