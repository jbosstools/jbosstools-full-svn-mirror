/**
 * 
 */
package org.jboss.tools.smooks.ui;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;

/**
 * @author Dart
 * 
 */
public abstract class AbstractConnectionModelSectionFilter implements IFilter {

	
	public LineConnectionModel getConnectionModel(Object input){
		if (input instanceof EditPart) {
			Object model = ((EditPart) input).getModel();
			if (model instanceof LineConnectionModel) {
				return (LineConnectionModel) model;
			}
		}
		return null;
	}
	
	public Object getReferenceSourceObject(Object input){
		LineConnectionModel connection = getConnectionModel(input);
		if(connection != null){
			AbstractStructuredDataModel s = (AbstractStructuredDataModel)connection.getSource();
			if(s != null){
				return s.getReferenceEntityModel();
			}
		}
		return null;
	}
	
	public Object getReferenceTargetObject(Object input){
		LineConnectionModel connection = getConnectionModel(input);
		if(connection != null){
			AbstractStructuredDataModel s = (AbstractStructuredDataModel)connection.getTarget();
			if(s != null){
				return s.getReferenceEntityModel();
			}
		}
		return null;
	}

}
