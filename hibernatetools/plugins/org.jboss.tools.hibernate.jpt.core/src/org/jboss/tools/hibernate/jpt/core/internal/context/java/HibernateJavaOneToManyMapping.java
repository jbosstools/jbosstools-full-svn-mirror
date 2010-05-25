/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.hibernate.jpt.core.internal.context.java;

import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaRelationshipReference;
import org.eclipse.jpt.core.internal.jpa1.context.java.GenericJavaOneToManyMapping;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernateJavaOneToManyMapping extends GenericJavaOneToManyMapping {

	public HibernateJavaOneToManyMapping(JavaPersistentAttribute parent) {
		super(parent);
	}
	
	@Override
	protected JavaRelationshipReference buildRelationshipReference() {
		return super.buildRelationshipReference();
	}

}
