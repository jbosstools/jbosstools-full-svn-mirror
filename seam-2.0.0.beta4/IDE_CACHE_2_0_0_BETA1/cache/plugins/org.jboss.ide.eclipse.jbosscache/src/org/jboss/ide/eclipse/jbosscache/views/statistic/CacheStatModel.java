package org.jboss.ide.eclipse.jbosscache.views.statistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CacheStatModel {
	
	private String nameOfInterceptor;
	private List attrChilds = Collections.EMPTY_LIST;
	private static List listModel = Collections.EMPTY_LIST;
	
	public static List getInstance(Map map){
		Set key = map.keySet();
		
		Iterator it = key.iterator();
		
		CacheStatModel model = null;
		listModel = new ArrayList();
		while(it.hasNext()){
			String interceptorName = it.next().toString();
			Map values = (Map)map.get(interceptorName);
			
			List childs = new ArrayList();
			
			Set keyChild = values.keySet();
			
			Iterator itChild = keyChild.iterator();
			
			CacheStatAttributes cacheStateAttrModel = null;
			while(itChild.hasNext()){
				String attName = itChild.next().toString();
				String value = values.get(attName).toString();
				
				cacheStateAttrModel = new CacheStatAttributes(attName,value);
				childs.add(cacheStateAttrModel);								
			}
			
			model = new CacheStatModel(interceptorName,childs);
			cacheStateAttrModel.setParent(model);
			
			listModel.add(model);	
		}		
		
		return listModel;
	}
	
	
	static class CacheStatAttributes{
		private String nameOfAttribute;
		private String valueOfAttribute;
		private CacheStatModel parent;
		
		public CacheStatAttributes(String name,String value){
			this.nameOfAttribute = name;
			this.valueOfAttribute = value;
		}

		public String getNameOfAttribute() {
			return nameOfAttribute;
		}

		public CacheStatModel getParent() {
			return parent;
		}

		public String getValueOfAttribute() {
			return valueOfAttribute;
		}

		public void setParent(CacheStatModel parent) {
			this.parent = parent;
		}
		
		
	}
	
	
	CacheStatModel(String interceptorName,List childs) {
		this.nameOfInterceptor = interceptorName;
		this.attrChilds = childs;
	}
	
	public List getAttrChilds() {
		return attrChilds;
	}

	public String getNameOfInterceptor() {
		return nameOfInterceptor;
	}
}
