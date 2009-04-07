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
package org.hibernate.eclipse.mapper.extractor;

/**
 * Internal class for describing hibernate types.
 * 
 */
class HibernateTypeDescriptor implements Comparable {

	final String name;
	final String returnClass;
	final String primitiveClass;

	public HibernateTypeDescriptor(String name, String returnClass, String primitiveClass) {
		this.name = name;
		this.returnClass = returnClass;
		this.primitiveClass = primitiveClass;
	}

	public String getName() {
		return name;
	}
	

	public String getPrimitiveClass() {
		return primitiveClass;
	}
	

	public String getReturnClass() {
		return returnClass;
	}

	public int compareTo(Object o) {
		
		return name.compareTo( ( (HibernateTypeDescriptor)o).getName() );
	}
	
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof HibernateTypeDescriptor)) {
			return false;
		}
		HibernateTypeDescriptor htd = (HibernateTypeDescriptor) obj;
		return this.name.equals(htd.getName());
	}
	
	public int hashCode() {
		return this.name.hashCode();
	}
	
	public String toString() {
		return name;
	}
	
}