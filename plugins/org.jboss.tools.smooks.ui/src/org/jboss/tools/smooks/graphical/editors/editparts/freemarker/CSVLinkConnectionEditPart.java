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

import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeConnectionEditPart;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerModelAnalyzer;

/**
 * @author Dart
 * 
 */
public class CSVLinkConnectionEditPart extends TreeNodeConnectionEditPart {

	public CSVLinkConnectionEditPart() {
		super();
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
			if (FreemarkerModelAnalyzer.isChoiceElement(((TagObject) data).getReferenceElement())) {
				this.setMarkerImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_CHOICE));
			}
		}
		super.refreshVisuals();
	}
}
