/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.core;

import java.util.List;

import org.jboss.tools.deltacloud.client.Property;

public class DeltaCloudHardwareProperty {

	public static enum Kind {FIXED, RANGE, ENUM};
	
	private Property property;

	public class Range {
		private String first;
		private String last;
		
		public Range(String first, String last) {
			this.first = first;
			this.last = last;
		}
		
		public String getFirst() {
			return first;
		}
		
		public String getLast() {
			return last;
		}
	}
	
	public DeltaCloudHardwareProperty(Property property) {
		this.property = property;
	}
	
	public Kind getKind() {
		return Kind.valueOf(property.getKind().toUpperCase());
	}
	
	public Range getRange() {
		return new Range(property.getRange().getFirst(), property.getRange().getLast());
	}
	
	public String getUnit() {
		return property.getUnit();
	}

	public String getName() {
		return property.getName();
	}
	
	public String getValue() {
		return property.getValue();
	}
	
	public List<String> getEnums() {
		return property.getEnums();
	}
	
}
