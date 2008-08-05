package org.jboss.tools.flow.editor.editpart;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.ModelEvent;
import org.jboss.tools.flow.editor.core.ModelListener;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.editor.figure.ElementFigure;
import org.jboss.tools.flow.editor.policy.ElementDirectEditManager;
import org.jboss.tools.flow.editor.policy.ElementDirectEditPolicy;
import org.jboss.tools.flow.editor.policy.ElementEditPolicy;
import org.jboss.tools.flow.editor.policy.ElementNodeEditPolicy;

/**
 * Default implementation of an element EditPart.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public abstract class ElementEditPart extends AbstractGraphicalEditPart implements NodeEditPart, ModelListener {
    
    private DirectEditManager manager;
    
    public NodeWrapper getElementWrapper() {
        return (NodeWrapper) getModel();
    }
    
    public ElementFigure getElementFigure() {
        return (ElementFigure) getFigure();
    }

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ElementNodeEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ElementDirectEditPolicy());
    }
    
    protected List<AbstractConnectionWrapper> getModelSourceConnections() {
        return getElementWrapper().getOutgoingConnections();
    }
    
    protected List<AbstractConnectionWrapper> getModelTargetConnections() {
        return getElementWrapper().getIncomingConnections();
    }
    
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return getElementFigure().getSourceConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return getElementFigure().getTargetConnectionAnchor();
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return getElementFigure().getSourceConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return getElementFigure().getTargetConnectionAnchor();
    }

    protected void refreshVisuals() {
        NodeWrapper element = getElementWrapper();
        ElementFigure figure = (ElementFigure) getFigure();
        figure.setText(element.getName());
        if (element.getConstraint().width == -1) {
            element.getConstraint().width = figure.getBounds().width;
        }
        if (element.getConstraint().height == -1) {
            element.getConstraint().height = figure.getBounds().height;
        }
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, element.getConstraint());
    }
    
    public void modelChanged(ModelEvent event) {
        if (event.getChange() == NodeWrapper.CHANGE_INCOMING_CONNECTIONS) {
            refreshTargetConnections();
        } else if (event.getChange() == NodeWrapper.CHANGE_OUTGOING_CONNECTIONS) {
            refreshSourceConnections();
        } else if (event.getChange() == NodeWrapper.CHANGE_VISUAL) {
            refreshVisuals();
        }
    }

    public void activate() {
        super.activate();
        getElementWrapper().addListener(this);
    }

    public void deactivate() {
        getElementWrapper().removeListener(this);
        super.deactivate();
    }

    public void performRequest(Request request) {
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            performDirectEdit();
        } if (request.getType() == RequestConstants.REQ_OPEN) {
            doubleClicked();
        } else {
            super.performRequest(request);
        }
    }
    
    protected void doubleClicked() {
        // do nothing
    }
    
    private void performDirectEdit() {
    	Label label = ((ElementFigure) getFigure()).getLabel();
    	if (label == null) {
    		return;
    	}
        if (manager == null) {
            manager = new ElementDirectEditManager(this, new CellEditorLocator(label));
        }
        manager.show();
    }
    
}
