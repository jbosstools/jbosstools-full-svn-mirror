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
package org.jboss.tools.smooks.graphical.editors.editparts;

import org.eclipse.gef.EditPart;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanChildGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.ResourceConfigChildNodeGraphModelImpl;
import org.jboss.tools.smooks.graphical.editors.model.ResourceConfigGraphModelImpl;

/**
 * @author Dart
 *
 */
public class ResourceConfigEditFactory {
	public EditPart createEditPart(Object model){
		if(model instanceof JavaBeanGraphModel){
			return new JavaBeanEditPart();
		}
		if(model instanceof JavaBeanChildGraphModel){
			return new JavaBeanChildNodeEditPart();
		}
		if(model instanceof ResourceConfigChildNodeGraphModelImpl){
			return new ResourceConfigChildNodeEditPart();
		}
		if(model instanceof ResourceConfigGraphModelImpl){
			return new ResourceConfigEditPart();
		}
		return null;
	}
}
