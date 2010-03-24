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
import org.hibernate.console.stubs.AnyStub;
import org.hibernate.console.stubs.ArrayStub;
import org.hibernate.console.stubs.BagStub;
import org.hibernate.console.stubs.ComponentStub;
import org.hibernate.console.stubs.DependantValueStub;
import org.hibernate.console.stubs.IdentifierBagStub;
import org.hibernate.console.stubs.ListStub;
import org.hibernate.console.stubs.ManyToOneStub;
import org.hibernate.console.stubs.MapStub;
import org.hibernate.console.stubs.OneToManyStub;
import org.hibernate.console.stubs.OneToOneStub;
import org.hibernate.console.stubs.PrimitiveArrayStub;
import org.hibernate.console.stubs.SetStub;
import org.hibernate.console.stubs.SimpleValueStub;
import org.hibernate.console.stubs.ValueVisitorStub;

final class IconNameValueVisitor implements ValueVisitorStub {
	
	public Object accept(OneToOneStub oto) {
		return ImageConstants.ONETOONE;
	}

	public Object accept(ManyToOneStub mto) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(ComponentStub component) {
		return ImageConstants.COMPONENT;
	}

	public Object accept(DependantValueStub value) {
		return ImageConstants.UNKNOWNPROPERTY;
	}

	public Object accept(SimpleValueStub value) {
		return ImageConstants.PROPERTY;
	}

	public Object accept(AnyStub any) {
		return ImageConstants.PROPERTY;
	}

	public Object accept(SetStub set) {
		return ImageConstants.MANYTOONE;
	}	

	public Object accept(OneToManyStub many) {
		return ImageConstants.ONETOMANY;
	}

	public Object accept(MapStub map) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(ArrayStub list) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(PrimitiveArrayStub primitiveArray) {
		return ImageConstants.MANYTOONE;			
	}

	public Object accept(ListStub list) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(IdentifierBagStub bag) {
		return ImageConstants.MANYTOONE;
	}

	public Object accept(BagStub bag) {
		return ImageConstants.MANYTOONE;
	}
	
}