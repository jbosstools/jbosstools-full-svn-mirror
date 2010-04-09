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

public class TypeNameValueVisitor implements ValueVisitor {

	/** if true then only return the classname, not the fully qualified classname */
	final boolean dequalify; 
	
	public TypeNameValueVisitor(boolean dequalify) {
		this.dequalify=dequalify;
	}
	
	public Object accept(Bag bag) {
		return "Bag <" + bag.getElement().accept(this) + ">";  //$NON-NLS-1$//$NON-NLS-2$
	}

	public Object accept(IdentifierBag bag) {
		return "IdBag <" + bag.getElement().accept(this) + ">";  //$NON-NLS-1$//$NON-NLS-2$
	}

	public Object accept(ListStub list) {
		return "List <" + list.getElement().accept(this) + ">"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Object accept(PrimitiveArray primitiveArray) {
		return primitiveArray.getElement().accept(this) + "[]"; //$NON-NLS-1$
	}

	public Object accept(Array list) {
		return list.getElement().accept(this) + "[]"; //$NON-NLS-1$
	}

	public Object accept(MapStub map) {
		return "Map<" + map.getElement().accept(this) + ">"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Object accept(OneToMany many) {
		return dequalify(many.getReferencedEntityName());
	}

	private String dequalify(String referencedEntityName) {
		if(dequalify && referencedEntityName!=null && referencedEntityName.indexOf(".")>=0) {			 //$NON-NLS-1$
			return referencedEntityName.substring(referencedEntityName.lastIndexOf('.')+1);
		}
		return referencedEntityName;
	}

	public Object accept(SetStub set) {
		return "Set<" + set.getElement().accept(this) + ">"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Object accept(Any any) {
		return "Any"; //$NON-NLS-1$
	}

	public Object accept(SimpleValue value) {
		return dequalify(value.getTypeName());
	}

	public Object accept(DependantValue value) {
		return null;
	}

	public Object accept(Component component) {
		return dequalify(component.getComponentClassName());
	}

	public Object accept(ManyToOne mto) {
		return dequalify(mto.getReferencedEntityName());
	}

	public Object accept(OneToOne oto) {
		return dequalify(oto.getEntityName());
	}

}
