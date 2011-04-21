/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.tools.AbstractConnectionCreationTool;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;

/**
 * @author Dart Peng
 * @Date Jul 31, 2008
 */
public class SmooksCustomConnectionCreationTool extends
		AbstractConnectionCreationTool {
	protected Object sourceModel = null;
	public SmooksCustomConnectionCreationTool(Object model, GraphicalViewer viewer) {
		super();
		sourceModel = model;
		activeSourceEditPart(model, viewer);
		CreateConnectionCommand command = new CreateConnectionCommand();
		CreateConnectionRequest request = (CreateConnectionRequest)this.getTargetRequest();
		if(request != null){
			request.setStartCommand(command);
		}
	}
	
	public void createConnection(){
		System.out.println("Create Connection");
		if(!this.handleCreateConnection()){
			System.out.println("Over Create Connection");
			throw new RuntimeException("chuangjian shibai ");
		}
	}

	@Override
	protected boolean handleButtonUp(int button) {
		this.handleFinished();
		return false;
	}

	@Override
	protected String getCommandName() {
		return REQ_CONNECTION_END;
	}

	protected boolean handleDragInProgress() {
		return super.handleDragInProgress();
	}

	protected boolean handleMove() {
		if (isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
		}
		boolean flag = super.handleMove();
		// if (flag)
		// System.out.println("MOVE BE ACCESS");
		return flag;
	}

	public EditPart findTheEditPart(Object model, GraphicalViewer viewer) {
		EditPart rootEditPart = viewer.getContents();
		EditPart resultEditPart = null;
		List childrenEditPartList = rootEditPart.getChildren();
		for (Iterator iterator = childrenEditPartList.iterator(); iterator
				.hasNext();) {
			EditPart childEditPart = (EditPart) iterator.next();
			Object cm = childEditPart.getModel();
			if (cm instanceof AbstractStructuredDataModel) {
				if (((AbstractStructuredDataModel) cm)
						.getReferenceEntityModel() == model) {
					resultEditPart = childEditPart;
					break;
				}
			}
		}

		return resultEditPart;
	}

	public void activeTargetEditPart(Object model, GraphicalViewer viewer) {
		EditPart activeEditPart = findTheEditPart(model, viewer);
		if (activeEditPart != null) {
			this.setTargetEditPart(activeEditPart);
			CreateConnectionRequest request1 = (CreateConnectionRequest) this
					.getSourceRequest();
			CreateConnectionRequest request = (CreateConnectionRequest) this
					.getTargetRequest();
			HashMap map = new HashMap();
			map.put("Model", model);
			request.setExtendedData(map);
			request.setTargetEditPart(activeEditPart);
			request1.setTargetEditPart(activeEditPart);
		}
	}

	public void activeSourceEditPart(Object model, GraphicalViewer viewer) {
		EditPart activeEditPart = findTheEditPart(model, viewer);
		if (activeEditPart != null) {
			this.setConnectionSource(activeEditPart);
			CreateConnectionRequest request1 = (CreateConnectionRequest) this
					.getSourceRequest();
			CreateConnectionRequest request = (CreateConnectionRequest) this
					.getTargetRequest();
			request.setSourceEditPart(activeEditPart);
			request1.setSourceEditPart(activeEditPart);
		}
	}

	public void startConnection(Object model, GraphicalViewer viewer) {
		// handleCreateConnection();
	}

	public Object getSourceModel() {
		return sourceModel;
	}

	public void setSourceModel(Object sourceModel) {
		this.sourceModel = sourceModel;
	}
}
