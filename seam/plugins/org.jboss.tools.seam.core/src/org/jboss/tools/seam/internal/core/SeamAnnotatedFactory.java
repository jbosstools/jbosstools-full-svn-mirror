 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.internal.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IMethod;
import org.jboss.tools.seam.core.ISeamAnnotatedFactory;
import org.jboss.tools.seam.core.ISeamTextSourceReference;
import org.jboss.tools.seam.core.ISeamXmlComponentDeclaration;
import org.jboss.tools.seam.core.event.Change;
import org.jboss.tools.seam.internal.core.scanner.java.ValueInfo;

/**
 * @author Viacheslav Kabanovich
 */
public class SeamAnnotatedFactory extends SeamJavaContextVariable implements ISeamAnnotatedFactory {
	boolean autoCreate = false;

	protected Map<String,ValueInfo> attributes = new HashMap<String, ValueInfo>();
	
	public IMethod getSourceMethod() {
		return (IMethod)javaSource;
	}
	
	public void setMethod(IMethod method) {
		this.javaSource = method;
	}

	public boolean isAutoCreate() {
		return autoCreate;
	}
	
	public void setAutoCreate(boolean autoCreate) {
		this.autoCreate = autoCreate;
	}

	public List<Change> merge(AbstractContextVariable f) {
		List<Change> changes = super.merge(f);
		SeamAnnotatedFactory af = (SeamAnnotatedFactory)f;

		if(autoCreate != af.autoCreate) {
			changes = Change.addChange(changes, new Change(this, ISeamXmlComponentDeclaration.AUTO_CREATE, autoCreate, af.autoCreate));
			autoCreate = af.autoCreate;
		}
	
		return changes;
	}

	/**
	 * @param path
	 * @return source reference for some member of declaration.
	 * e.g. if you need source reference for @Name you have to 
	 * invoke getLocationFor("name");
	 */
	public ISeamTextSourceReference getLocationFor(String path) {
		final ValueInfo valueInfo = attributes.get(path);
		ISeamTextSourceReference reference = new ISeamTextSourceReference() {
			public int getLength() {
				return valueInfo != null ? valueInfo.getLength() : 0;
			}

			public IResource getResource() {
				return SeamAnnotatedFactory.this.getResource();
			}

			public int getStartPosition() {
				return valueInfo != null ? valueInfo.getStartPosition() : 0;
			}
		};
		return reference;
	}
	
	public void setName(ValueInfo value) {
		attributes.put(ISeamXmlComponentDeclaration.NAME, value);
		name = value == null ? null : value.getValue();
	}

	public void setScope(ValueInfo value) {
		attributes.put(ISeamXmlComponentDeclaration.SCOPE, value);
		setScopeAsString(value == null ? null : value.getValue());
	}

	public void setAutoCreate(ValueInfo value) {
		attributes.put(ISeamXmlComponentDeclaration.AUTO_CREATE, value);
		setAutoCreate(value != null && "true".equals(value.getValue()));
	}

}
