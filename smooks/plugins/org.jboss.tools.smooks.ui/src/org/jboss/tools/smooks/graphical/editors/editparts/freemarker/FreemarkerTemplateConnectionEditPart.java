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
package org.jboss.tools.smooks.graphical.editors.editparts.freemarker;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.command.DeleteConnectionCommand;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeConnectionEditPart;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeEndpointEditPolicy;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerModelAnalyzer;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateNodeGraphicalModel;
import org.jboss.tools.smooks.templating.template.Mapping;
import org.jboss.tools.smooks.templating.template.TemplateBuilder;
import org.jboss.tools.smooks.templating.template.result.RemoveResult;

/**
 * @author Dart
 * 
 */
public class FreemarkerTemplateConnectionEditPart extends
		TreeNodeConnectionEditPart {

	public FreemarkerTemplateConnectionEditPart() {
		super();
	}

	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new FreemarkerTemplateConnectionEditPolicy());
		this.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new TreeNodeEndpointEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		TreeNodeConnection connection = (TreeNodeConnection) this.getModel();
		AbstractSmooksGraphicalModel target = connection.getTargetNode();
		Object data = target.getData();
		if (data instanceof TagObject) {
			if (FreemarkerModelAnalyzer.isChoiceElement(((TagObject) data)
					.getReferenceElement())) {
				this.setMarkerImage(SmooksConfigurationActivator.getDefault()
						.getImageRegistry()
						.get(GraphicsConstants.IMAGE_XSL_CHOICE));
			}
		}
		super.refreshVisuals();
	}

	public class FreemarkerTemplateConnectionEditPolicy extends
			ConnectionEditPolicy {

		@Override
		protected Command getDeleteCommand(GroupRequest request) {
			Object connection = getHost().getModel();
			EditPart host = getHost();
			if (connection != null && connection instanceof TreeNodeConnection) {
				if (host instanceof TreeNodeConnectionEditPart) {
					if (!((TreeNodeConnectionEditPart) host).isCanDelete()) {
						return null;
					}
				}
				DeleteFreeMarkerConnectionCommand command = new DeleteFreeMarkerConnectionCommand(
						(TreeNodeConnection) connection);
				return command;
			}
			return null;
		}

	}

	public class DeleteFreeMarkerConnectionCommand extends
			DeleteConnectionCommand {

		private FreemarkerRemoveConnectionsExecutor removeConnectionExecutor = new FreemarkerRemoveConnectionsExecutor();

		public DeleteFreeMarkerConnectionCommand(TreeNodeConnection connection) {
			super(connection);
		}

		@Override
		public void execute() {
			Object target = connection.getTargetNode();
			if (target instanceof FreemarkerTemplateNodeGraphicalModel) {
				RemoveResult removeResult;
				try {
					TemplateBuilder builder = ((FreemarkerTemplateNodeGraphicalModel) target)
							.getTemplateBuilder();
					Object mapping = connection.getData();
					if (builder == null || mapping == null)
						return;
					if (mapping instanceof Mapping) {
						removeResult = builder.removeMapping((Mapping) mapping);
						removeConnectionExecutor.execute(connection, removeResult.getRemoveMappings());
					}
					for (TreeNodeConnection con : removeConnectionExecutor.getRelatedConnections()) {
						con.disconnect();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.execute();
		}

		@Override
		public void redo() {
			super.redo();
		}

		@Override
		public void undo() {
			super.undo();
			for (TreeNodeConnection c : removeConnectionExecutor.getRelatedConnections()) {
				c.connect();
			}
		}
	}
}
