/**
 * 
 */
package org.jboss.tools.smooks.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.smooks.ui.gef.model.PropertyModel;

/**
 * @author root
 *
 */
public class MappingModel {
	protected Object source;
	protected Object target;
	
	protected List<PropertyModel> properties = new ArrayList<PropertyModel>();
	
	public MappingModel(Object source,Object target){
		setSource(source);
		setTarget(target);
	}
	
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		if(source != null)
			buffer.append("Source : " + source);
		else
			buffer.append("Source is NULL ");
		if(target != null)
			buffer.append(";Target : " + target);
		else
			buffer.append(";Target is NULL ");
		return buffer.toString();
	}

	public List<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyModel> properties) {
		this.properties = properties;
	}
}
