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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
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
import org.hibernate.mediator.stubs.PropertyStub;
import org.hibernate.mediator.stubs.SetStub;
import org.hibernate.mediator.stubs.SimpleValueStub;
import org.hibernate.mediator.stubs.ValueStub;
import org.hibernate.mediator.stubs.ValueVisitorStub;

public class PropertyWorkbenchAdapter extends BasicWorkbenchAdapter implements
		IDeferredWorkbenchAdapter {

	public Object[] getChildren(Object o) {
		PropertyStub p = (PropertyStub) o;
		
		Object[] result = (Object[]) p.getValue().accept(new ValueVisitorStub() {
		
			public Object accept(OneToOneStub oto) {
				return NO_CHILDREN;
			}
		
			public Object accept(ManyToOneStub mto) {
				return NO_CHILDREN;
			}
		
			public Object accept(ComponentStub component) {
				return toArray(component.getPropertyIterator(), PropertyStub.class, null);				
			}
		
			public Object accept(DependantValueStub value) {
				return NO_CHILDREN;
			}
		
			public Object accept(SimpleValueStub value) {
				return NO_CHILDREN;
			}
		
			public Object accept(AnyStub any) {
				return NO_CHILDREN;
			}
		
			public Object accept(SetStub set) {
				return NO_CHILDREN; // should it look up the target entity?
			}
					
			public Object accept(OneToManyStub many) {
				return NO_CHILDREN;
			}
		
			public Object accept(MapStub map) {
				return NO_CHILDREN;
			}
		
			public Object accept(ArrayStub list) {
				return NO_CHILDREN;
			}
		
			public Object accept(PrimitiveArrayStub primitiveArray) {
				return NO_CHILDREN;
			}
		
			public Object accept(ListStub list) {
				return NO_CHILDREN;
			}
		
			public Object accept(IdentifierBagStub bag) {
				return NO_CHILDREN;
			}
		
			public Object accept(BagStub bag) {
				return NO_CHILDREN;
			}		
		});
		
		return result;
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		PropertyStub property = ((PropertyStub)object);
		
		return HibernateWorkbenchHelper.getImageDescriptor(property);		 
	}

	public String getLabel(Object o) {
		PropertyStub property = ((PropertyStub)o);
		ValueStub value = property.getValue();
		String typeName = (String) value.accept(new TypeNameValueVisitor(true));
		
		if (typeName!=null) {
			return property.getName() + " : " + typeName; //$NON-NLS-1$
		}
		
		return property.getName(); 
	}

	public Object getParent(Object o) {
		PropertyStub p = (PropertyStub) o;
		return p.getPersistentClass();
	}

	
}
