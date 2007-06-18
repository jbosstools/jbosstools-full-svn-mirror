package org.jboss.ide.eclipse.jbosscache.model;

import java.util.ArrayList;
import java.util.List;

import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;

public class CacheLoaderPropModel {
	
	private String property;
	private String value;
	
	public CacheLoaderPropModel(){
		
	}
	 
	
	public List getPropertyModels(String classLoaderName,boolean isDs){
		
		List list = new ArrayList();
		
		if(classLoaderName.equals(ICacheConstants.CACHE_LOADER_CLASSES[1]) || classLoaderName.equals(ICacheConstants.CACHE_LOADER_CLASSES[2])){
			String[][] temp = CacheUtil.getPropertiesForJdbc(isDs);
			
			CacheLoaderPropModel model =  null;
			for(int i=0;i<temp.length;i++){
				model = new CacheLoaderPropModel();
				model.property = temp[i][0];
				model.value = temp[i][1];
				list.add(model);
			}
		}
		
		return list;
	}


	public String getProperty() {
		return property;
	}


	public String getValue() {
		return value;
	}


	public void setProperty(String property) {
		this.property = property;
	}


	public void setValue(String value) {
		this.value = value;
	}

}
