package org.jboss.tools.flow.common.editpart;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.EditPart;
import org.jboss.tools.flow.common.figure.IFigureFactory;
import org.jboss.tools.flow.common.figure.NodeFigureFactory;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class EditPartFactory implements org.eclipse.gef.EditPartFactory {

    public EditPart createEditPart(EditPart context, Object model) {
        EditPart result = null;
        if (!(model instanceof Wrapper)) return result;
        Object element = ((Wrapper)model).getElement();
        if (element != null && element instanceof Element) {
        	result = createEditPart((Element)element);
        } else if (model instanceof ConnectionWrapper) {
        	result = new ConnectionEditPart();
        }
        if (result != null) {
        	result.setModel(model);
        }
        return result;
    }
    
	protected EditPart createEditPart(Element element) {
		IConfigurationElement configurationElement = 
			(IConfigurationElement)element.getMetaData("configurationElement");
		if (configurationElement == null) return null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children.length != 1) return null;
		String type = children[0].getName();
		if ("flow".equals(type)) {
			return createFlowEditPart(element);
		} else if ("container".equals(type)) {
			return createContainerEditPart(element);
		} else if ("node".equals(type)) {
			return createNodeEditPart(element);
		} else if ("connection".equals(type)) {
			return createConnectionEditPart(element);
		} else {
			return null;
		}
	}
	
    public EditPart createFlowEditPart(Element element) {
    	return new RootEditPart();
    }
    
    public EditPart createContainerEditPart(Element element) {
    	return new ContainerEditPart();
    }
    
    public EditPart createNodeEditPart(Element element) {
		IConfigurationElement configurationElement = 
			(IConfigurationElement)element.getMetaData("configurationElement");
		if (configurationElement == null) return null;
    	NodeEditPart result = new NodeEditPart();
    	IFigureFactory figureFactory = new NodeFigureFactory(configurationElement);
    	result.setFigureFactory(figureFactory);
    	return result;
    }
    
    public EditPart createConnectionEditPart(Element element) {
    	return new ConnectionEditPart();
    }
    
}
