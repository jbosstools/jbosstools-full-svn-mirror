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

import java.util.Iterator;

import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.console.stubs.PropertyStub;
import org.hibernate.console.stubs.RootClassStub;
import org.hibernate.console.stubs.TypeStub;

/**
 * 
 * @author some modifications from Vitali
 */
public class SpecialOrmShape extends OrmShape {
	private Shape parentShape;

	public SpecialOrmShape(SpecialRootClass ioe) {
		super(ioe);
	}

	/**
	 * creates children of the shape, 
	 */
	@Override
	protected void initModel() {
		RootClassStub rootClass = (RootClassStub)getOrmElement();
		PropertyStub identifierProperty = rootClass.getIdentifierProperty();
		if (identifierProperty != null) {
			addChild(new Shape(identifierProperty));
		}

		SpecialRootClass src = (SpecialRootClass)getOrmElement();
		if (src.getParentProperty() != null) {
			Shape bodyOrmShape = new Shape(src.getParentProperty());
			addChild(bodyOrmShape);
			parentShape = bodyOrmShape;
		}
		
		Iterator<PropertyStub> iterator = rootClass.getPropertyIterator();
		while (iterator.hasNext()) {
			PropertyStub field = iterator.next();
			TypeStub type = null;
			if (getOrmDiagram() != null) {
				ConsoleConfiguration cfg = getOrmDiagram().getConsoleConfig();
				final PropertyStub fField = field;
				type = (TypeStub) cfg.execute(new Command() {
					public Object execute() {
						return fField.getValue().getType();
					}});								
			} else {
				type = field.getValue().getType();
			}
			Shape bodyOrmShape = null;
			if (type != null && type.isEntityType()) {
				bodyOrmShape = new ExpandableShape(field);
			} else if (type != null && type.isCollectionType()) {
				bodyOrmShape = new ComponentShape(field);
			} else {
				bodyOrmShape = new Shape(field);
			}
			addChild(bodyOrmShape);
		}
	}

	protected Shape getParentShape() {
		return parentShape;
	}

}
