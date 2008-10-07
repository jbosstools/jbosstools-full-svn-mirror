/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.model;

import java.util.List;

import org.eclipse.draw2d.IFigure;

/**
 * @author Dart Peng
 * 
 */
public interface IConnectableModel {

	public static final String P_SOURCE_CONNECTION = "_source_connection";
	public static final String P_TARGET_CONNECTION = "_target_connection";

	public void addSourceConnection(Object connx);

	public void addTargetConnection(Object connx);

	public List<Object> getModelSourceConnections();

	public List<Object> getModelTargetConnections();

	public void removeSourceConnection(Object connx);

	public void removeTargetConnection(Object connx);
	
	public boolean isSourceConnectWith(IConnectableModel target);
	
	public boolean isTargetConnectWith(IConnectableModel source);
}
