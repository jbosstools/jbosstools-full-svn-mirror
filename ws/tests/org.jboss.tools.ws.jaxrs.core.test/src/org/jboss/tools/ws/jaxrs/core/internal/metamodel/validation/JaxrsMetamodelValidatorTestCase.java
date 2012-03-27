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
package org.jboss.tools.ws.jaxrs.core.internal.metamodel.validation;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.wst.validation.ValidatorMessage;
import org.jboss.tools.ws.jaxrs.core.WorkbenchUtils;
import org.jboss.tools.ws.jaxrs.core.builder.AbstractMetamodelBuilderTestCase;
import org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain.JaxrsBaseElement;
import org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain.JaxrsHttpMethod;
import org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain.JaxrsResourceMethod;
import org.jboss.tools.ws.jaxrs.core.jdt.Annotation;
import org.junit.Test;

/**
 * @author Xi
 *
 */
public class JaxrsMetamodelValidatorTestCase extends AbstractMetamodelBuilderTestCase {
	
	@Test
	public void shouldValidateHttpMethod() throws CoreException {
		// preconditions
		IType fooType = WorkbenchUtils.getType("org.jboss.tools.ws.jaxrs.sample.services.FOO", javaProject);
		final JaxrsBaseElement httpMethod = metamodel.getElement(fooType);
		// operation
		final List<ValidatorMessage> validationMessages = httpMethod.validate();
		// validation
		assertThat(validationMessages.size(), equalTo(0));
	}

	@Test
	public void shouldNotValidateHttpMethod() throws CoreException {
		// preconditions
		IType fooType = WorkbenchUtils.getType("org.jboss.tools.ws.jaxrs.sample.services.FOO", javaProject);
		final JaxrsHttpMethod httpMethod = metamodel.getElement(fooType, JaxrsHttpMethod.class);
		Annotation httpAnnotation = WorkbenchUtils.getAnnotation(fooType, HttpMethod.class, new String[0]);
		httpMethod.addOrUpdateAnnotation(httpAnnotation);
		// operation
		final List<ValidatorMessage> validationMessages = httpMethod.validate();
		// validation
		assertThat(validationMessages.size(), equalTo(1));
	}
	
	@Test
	public void shouldValidateResourceMethod() throws CoreException {
		// preconditions
		IType customerJavaType = WorkbenchUtils.getType("org.jboss.tools.ws.jaxrs.sample.services.CustomerResource", javaProject);
		final JaxrsBaseElement customerResource = metamodel.getElement(customerJavaType);
		// operation
		final List<ValidatorMessage> validationMessages = customerResource.validate();
		// validation
		assertThat(validationMessages.size(), equalTo(0));
	}

	@Test
	public void shouldNotValidateResourceMethod() throws CoreException {
		// preconditions
		IType customerJavaType = WorkbenchUtils.getType("org.jboss.tools.ws.jaxrs.sample.services.CustomerResource", javaProject);
		final JaxrsBaseElement customerResource = metamodel.getElement(customerJavaType);
		IMethod customerJavaMethod = WorkbenchUtils.getMethod(customerJavaType, "getCustomer");
		final JaxrsResourceMethod customerResourceMethod = metamodel.getElement(customerJavaMethod, JaxrsResourceMethod.class);
		Annotation pathAnnotation = WorkbenchUtils.getAnnotation(customerJavaMethod, Path.class, "/{foo}");
		customerResourceMethod.addOrUpdateAnnotation(pathAnnotation);
		// operation
		final List<ValidatorMessage> validationMessages = customerResource.validate();
		// validation
		assertThat(validationMessages.size(), equalTo(2));
	}

}