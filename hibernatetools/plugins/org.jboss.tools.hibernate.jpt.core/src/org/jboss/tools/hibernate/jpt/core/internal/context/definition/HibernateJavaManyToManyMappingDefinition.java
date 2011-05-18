/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal.context.definition;

import org.eclipse.jpt.common.utility.internal.iterables.ArrayIterable;
import org.eclipse.jpt.common.utility.internal.iterables.CompositeIterable;
import org.eclipse.jpt.jpa.core.JpaFactory;
import org.eclipse.jpt.jpa.core.MappingKeys;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.jpa.core.resource.java.JoinColumnAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JoinColumnsAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JoinTableAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.ManyToOneAnnotation;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.Hibernate;

/**
 * @author Dmitry Geraskov (geraskov@gmail.com)
 *
 */
public class HibernateJavaManyToManyMappingDefinition implements JavaAttributeMappingDefinition
{
	// singleton
	private static final JavaAttributeMappingDefinition INSTANCE = new HibernateJavaManyToManyMappingDefinition();

	/**
	 * Return the singleton.
	 */
	public static JavaAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private HibernateJavaManyToManyMappingDefinition() {
		super();
	}

	public String getKey() {
		return MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY;
	}

	public String getAnnotationName() {
		return ManyToOneAnnotation.ANNOTATION_NAME;
	}

	public boolean isSpecified(JavaPersistentAttribute persistentAttribute) {
		return persistentAttribute.getResourcePersistentAttribute().getAnnotation(this.getAnnotationName()) != null;
	}

	@Override
	public Iterable<String> getSupportingAnnotationNames() {
		return new CompositeIterable<String>(SUPPORTING_ANNOTATION_NAMES, new ArrayIterable<String>(HIBERNATE_ANNOTATION_NAMES_ARRAY));
	}
	
	protected static final String[] HIBERNATE_ANNOTATION_NAMES_ARRAY = new String[] {
		Hibernate.FOREIGN_KEY,
	};

	private static final String[] SUPPORTING_ANNOTATION_NAMES_ARRAY = new String[] {
		JoinTableAnnotation.ANNOTATION_NAME,
		JoinColumnAnnotation.ANNOTATION_NAME,
		JoinColumnsAnnotation.ANNOTATION_NAME
	};
	private static final Iterable<String> SUPPORTING_ANNOTATION_NAMES = new ArrayIterable<String>(SUPPORTING_ANNOTATION_NAMES_ARRAY);

	public JavaAttributeMapping buildMapping(JavaPersistentAttribute persistentAttribute, JpaFactory factory) {
		return factory.buildJavaManyToOneMapping(persistentAttribute);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}