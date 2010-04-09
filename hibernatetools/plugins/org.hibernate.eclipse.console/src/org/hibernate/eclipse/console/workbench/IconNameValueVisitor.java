/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.console.workbench;

import org.hibernate.console.ImageConstants;
import org.hibernate.mediator.x.mapping.Any;
import org.hibernate.mediator.x.mapping.Array;
import org.hibernate.mediator.x.mapping.Bag;
import org.hibernate.mediator.x.mapping.Component;
import org.hibernate.mediator.x.mapping.DependantValue;
import org.hibernate.mediator.x.mapping.IdentifierBag;
import org.hibernate.mediator.x.mapping.ListStub;
import org.hibernate.mediator.x.mapping.ManyToOne;
import org.hibernate.mediator.x.mapping.MapStub;
import org.hibernate.mediator.x.mapping.OneToMany;
import org.hibernate.mediator.x.mapping.OneToOne;
import org.hibernate.mediator.x.mapping.PrimitiveArray;
import org.hibernate.mediator.x.mapping.SetStub;
import org.hibernate.mediator.x.mapping.SimpleValue;
import org.hibernate.mediator.x.mapping.ValueVisitor;

final class IconNameValueVisitor implements ValueVisitor {
	
	public Object accept(OneToOne oto) {
		return ImageConstants.ONETOONE;
	}

	public Object accept(ManyToOne mto) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(Component component) {
		return ImageConstants.COMPONENT;
	}

	public Object accept(DependantValue value) {
		return ImageConstants.UNKNOWNPROPERTY;
	}

	public Object accept(SimpleValue value) {
		return ImageConstants.PROPERTY;
	}

	public Object accept(Any any) {
		return ImageConstants.PROPERTY;
	}

	public Object accept(SetStub set) {
		return ImageConstants.MANYTOONE;
	}	

	public Object accept(OneToMany many) {
		return ImageConstants.ONETOMANY;
	}

	public Object accept(MapStub map) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(Array list) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(PrimitiveArray primitiveArray) {
		return ImageConstants.MANYTOONE;			
	}

	public Object accept(ListStub list) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(IdentifierBag bag) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(Bag bag) {
		return ImageConstants.MANYTOONE;
	}
	
}