/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.eclipse.jdt.core.IField;
import org.eclipse.wst.validation.ValidatorMessage;
import org.jboss.tools.ws.jaxrs.core.jdt.Annotation;
import org.jboss.tools.ws.jaxrs.core.metamodel.EnumElementKind;
import org.jboss.tools.ws.jaxrs.core.metamodel.EnumKind;
import org.jboss.tools.ws.jaxrs.core.metamodel.IJaxrsResourceField;

/** @author xcoulon */
public class JaxrsResourceField extends JaxrsResourceElement<IField> implements IJaxrsResourceField {

	public JaxrsResourceField(IField javaField, Annotation annotation, JaxrsResource parentResource,
			JaxrsMetamodel metamodel) {
		super(javaField, annotation, parentResource, metamodel);
	}

	public JaxrsResourceField(IField javaField, List<Annotation> annotations, JaxrsResource parentResource,
			JaxrsMetamodel metamodel) {
		super(javaField, annotations, parentResource, metamodel);
	}

	@Override
	public EnumElementKind getElementKind() {
		return EnumElementKind.RESOURCE_FIELD;
	}

	public Annotation getPathParamAnnotation() {
		return getAnnotation(PathParam.class.getName());
	}

	public Annotation getQueryParamAnnotation() {
		return getAnnotation(QueryParam.class.getName());
	}

	public Annotation getMatrixParamAnnotation() {
		return getAnnotation(MatrixParam.class.getName());
	}

	public Annotation getDefaultValueAnnotation() {
		return getAnnotation(DefaultValue.class.getName());
	}

	@Override
	public List<ValidatorMessage> validate() {
		List<ValidatorMessage> messages = new ArrayList<ValidatorMessage>();
		return messages;
	}

	@Override
	public EnumKind getKind() {
		if (getPathParamAnnotation() != null) {
			return EnumKind.PATH_PARAM_FIELD;
		}
		if (getQueryParamAnnotation() != null) {
			return EnumKind.QUERY_PARAM_FIELD;
		}
		if (getMatrixParamAnnotation() != null) {
			return EnumKind.MATRIX_PARAM_FIELD;
		}
		return EnumKind.UNDEFINED;
	}

	@Override
	public String toString() {
		return "ResourceField '" + getJavaElement().getParent().getElementName() + "." + getJavaElement().getElementName() + "' | annotations=" + getAnnotations();
	}

}
