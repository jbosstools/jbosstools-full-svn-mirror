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

import org.hibernate.mediator.stubs.AnyStub;
import org.hibernate.mediator.stubs.ArrayStub;
import org.hibernate.mediator.stubs.BagStub;
import org.hibernate.mediator.stubs.ComponentStub;
import org.hibernate.mediator.stubs.DependantValueStub;
import org.hibernate.mediator.stubs.IdentifierBagStub;
import org.hibernate.mediator.stubs.ListStub;
import org.hibernate.mediator.stubs.ManyToOneStub;
import org.hibernate.mediator.stubs.MapStub;
import org.hibernate.mediator.stubs.OneToManyStub;
import org.hibernate.mediator.stubs.OneToOneStub;
import org.hibernate.mediator.stubs.PrimitiveArrayStub;
import org.hibernate.mediator.stubs.SetStub;
import org.hibernate.mediator.stubs.SimpleValueStub;
import org.hibernate.mediator.stubs.ValueVisitorStub;

public class TypeNameValueVisitor implements ValueVisitorStub {

	/** if true then only return the classname, not the fully qualified classname */
	final boolean dequalify; 
	
	public TypeNameValueVisitor(boolean dequalify) {
		this.dequalify=dequalify;
	}
	
	public Object accept(BagStub bag) {
		return "Bag <" + bag.getElement().accept(this) + ">";  //$NON-NLS-1$//$NON-NLS-2$
	}

	public Object accept(IdentifierBagStub bag) {
		return "IdBag <" + bag.getElement().accept(this) + ">";  //$NON-NLS-1$//$NON-NLS-2$
	}

	public Object accept(ListStub list) {
		return "List <" + list.getElement().accept(this) + ">"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Object accept(PrimitiveArrayStub primitiveArray) {
		return primitiveArray.getElement().accept(this) + "[]"; //$NON-NLS-1$
	}

	public Object accept(ArrayStub list) {
		return list.getElement().accept(this) + "[]"; //$NON-NLS-1$
	}

	public Object accept(MapStub map) {
		return "Map<" + map.getElement().accept(this) + ">"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Object accept(OneToManyStub many) {
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

	public Object accept(AnyStub any) {
		return "Any"; //$NON-NLS-1$
	}

	public Object accept(SimpleValueStub value) {
		return dequalify(value.getTypeName());
	}

	public Object accept(DependantValueStub value) {
		return null;
	}

	public Object accept(ComponentStub component) {
		return dequalify(component.getComponentClassName());
	}

	public Object accept(ManyToOneStub mto) {
		return dequalify(mto.getReferencedEntityName());
	}

	public Object accept(OneToOneStub oto) {
		return dequalify(oto.getEntityName());
	}

}
