/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.diagram.editors.model;

import java.util.Properties;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Property;

/**
 * @author some modifications from Vitali
 */
public class ComponentShape extends ExpandableShape {

	public ComponentShape(Object ioe, String consoleConfigName) {	
		super(ioe, consoleConfigName);
		initModel();
	}

	/**
	 * creates children of the shape, 
	 */
	protected void initModel() {
		Object ormElement = getOrmElement();
		if (ormElement instanceof Property) {
			Collection collection = (Collection)((Property)ormElement).getValue();
			Shape bodyOrmShape = new Shape(collection.getKey(), getConsoleConfigName());
			bodyOrmShape.setIndent(20);
			addChild(bodyOrmShape);
			bodyOrmShape = new Shape(collection.getElement(), getConsoleConfigName());
			bodyOrmShape.setIndent(20);
			addChild(bodyOrmShape);
		}
	}

	@Override
	protected void loadFromProperties(Properties properties) {
		super.loadFromProperties(properties);
	}
	
	@Override
	protected void saveInProperties(Properties properties) {
		super.saveInProperties(properties);
	}
}