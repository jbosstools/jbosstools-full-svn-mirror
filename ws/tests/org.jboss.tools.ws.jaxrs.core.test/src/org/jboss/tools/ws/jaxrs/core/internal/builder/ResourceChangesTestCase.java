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

package org.jboss.tools.ws.jaxrs.core.internal.builder;

import javax.ws.rs.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.ws.jaxrs.core.WorkbenchUtils;
import org.jboss.tools.ws.jaxrs.core.builder.AbstractMetamodelBuilderTestCase;
import org.jboss.tools.ws.jaxrs.core.metamodel.Resource;
import org.jboss.tools.ws.jaxrs.core.metamodel.Resources;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xcoulon
 * 
 */
public class ResourceChangesTestCase extends AbstractMetamodelBuilderTestCase {

	@Test
	public void shouldIncreaseWhenAddingResourceFromFile() throws CoreException {
		// pre-conditions
		Resources resources = metamodel.getResources();
		Assert.assertEquals(5, resources.getAll().size());
		// operation
		WorkbenchUtils.createCompilationUnit(javaProject, "FooResource.txt",
				"org.jboss.tools.ws.jaxrs.sample.services", "FooResource.java", bundle);
		// post-conditions
		Assert.assertEquals(6, resources.getAll().size());
	}

	@Test
	public void shouldNotIncreaseWhenAppendingResourceIntoExistingCompilationUnit() throws CoreException {
		// pre-conditions
		Resources resources = metamodel.getResources();
		Assert.assertEquals(5, resources.getAll().size());
		// operation
		IType resourceType = resources.getAll().get(0).getJavaElement();
		WorkbenchUtils.appendCompilationUnitType(resourceType.getCompilationUnit(), "FooResourceMember.txt", bundle);
		// post-conditions
		// The total number should not change because the type is not a valid
		// root resource (not a public class)
		Assert.assertEquals("Wrong result : the total number of root resources shouldn't have changed", 5, resources
				.getAll().size());
		// TODO: there should be a warning on the added type :
		// "type is not public and thus, it is not reachable"
	}

	@Test
	public void shouldIncreaseWhenAddingPathAnnotationOnExistingPojo() throws CoreException {
		// pre-conditions : add a standard class : no new root resource (yet)
		ICompilationUnit fooCompilationUnit = WorkbenchUtils.createCompilationUnit(javaProject,
				"PseudoFooResource.txt", "org.jboss.tools.ws.jaxrs.sample.services", "FooResource.java", bundle);
		Resources resources = metamodel.getResources();
		Assert.assertEquals(5, resources.getAll().size());
		// operation : add @Path("/foo") annotation on type level
		IType fooType = fooCompilationUnit.getType("FooResource");
		WorkbenchUtils.addTypeAnnotation(fooType, "@Path(\"/foo\") ");
		WorkbenchUtils.addImport(fooCompilationUnit, "javax.ws.rs.Path");
		
		// post-conditions

		// The total number should have increased even the type is not a valid
		// root resource (not request method designator or @Path annotation on a
		// method)
		Assert.assertEquals("Wrong result : the total number of resources should have increased", 6, resources.getAll()
				.size());
		Resource foo = resources.getByType(fooCompilationUnit.findPrimaryType());
		Assert.assertNotNull("Resource not found", foo);
		Assert.assertTrue("Wrong resource kind", foo.isRootResource());
		Assert.assertEquals("No resource method expected", 0, foo.getResourceMethods().size());
		Assert.assertEquals("No subresource method expected", 0, foo.getSubresourceMethods().size());
		Assert.assertEquals("No subresource locator expected", 0, foo.getSubresourceLocators().size());
		// TODO: there should be a warning on the added type :
		// "type has no request method designator"
	}

	@Test
	public void shouldIncreaseWhenAddingPathAnnotationOnExistingSubresource() throws CoreException {
		// pre-conditions : add a subresource class
		Resources resources = metamodel.getResources();
		Assert.assertEquals(3, resources.getRootResources().size());
		Assert.assertEquals(2, resources.getSubresources().size());

		IType subresourceType = resources.getByTypeName("org.jboss.tools.ws.jaxrs.sample.services.BookResource")
				.getJavaElement();
		// operation : add @Path("/foo") annotation on type level
		WorkbenchUtils.addTypeAnnotation(subresourceType, "@Path(\"/foo\") ");
		// post-conditions : switch
		Assert.assertEquals(4, resources.getRootResources().size());
		Assert.assertEquals(1, resources.getSubresources().size());
	}

	@Test
	public void shouldDecreaseWhenRemovingPathAnnotationOnExistingSubresource() throws CoreException {
		// pre-conditions : add a subresource class
		Resources resources = metamodel.getResources();
		Assert.assertEquals(3, resources.getRootResources().size());
		Assert.assertEquals(2, resources.getSubresources().size());
		IType customerResourceType = resources.getByPath("/customers").getJavaElement();
		// operation : add @Path("/foo") annotation on type level
		WorkbenchUtils.removeFirstOccurrenceOfCode(customerResourceType, "@Path(CustomerResource.URI_BASE)");
		// post-conditions : switch
		Assert.assertEquals(2, resources.getRootResources().size());
		Assert.assertEquals(3, resources.getSubresources().size());
	}

	@Test
	public void shouldDecreaseWhenRemovingFile() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource resource = metamodel.getResources().getByPath("/customers");
		// operation
		WorkbenchUtils.delete(resource.getJavaElement().getCompilationUnit());
		// post-conditions
		Assert.assertEquals(4, metamodel.getResources().getAll().size());
	}

	@Test
	public void shouldDecreaseWhenRemovingMember() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource resource = metamodel.getResources().getByPath("/customers");
		// operation
		WorkbenchUtils.delete(resource.getJavaElement().getCompilationUnit());
		// post-conditions
		Assert.assertEquals(4, metamodel.getResources().getAll().size());
	}

	@Test
	public void shouldChangeWhenModifyingPathAnnotationValue() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		// operation
		Resource customersResource = metamodel.getResources().getByPath("/customers");
		Assert.assertNotNull("CustomerResource not found", customersResource);
		WorkbenchUtils.removeFirstOccurrenceOfCode(customersResource.getJavaElement(), "@Path(CustomerResource.URI_BASE)");
		WorkbenchUtils.addTypeAnnotation(customersResource.getJavaElement(), "@Path(\"/foo\")");
		// post-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Assert.assertEquals("Wrong uri path template", "/foo", customersResource.getUriPathTemplate());
	}

	@Test
	public void shouldChangeWhenAddingProducesAnnotation() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource ordersResource = metamodel.getResources().getByPath("/orders");
		Assert.assertNotNull("OrdersResource not found", ordersResource);
		Assert.assertTrue("Wrong mediatype capabilities", ordersResource.getMediaTypeCapabilities()
				.getProducedMimeTypes().isEmpty());
		// operation
		WorkbenchUtils.addTypeAnnotation(ordersResource.getJavaElement(),
				"import javax.ws.rs.Produces;\n@Produces(\"foo/bar\")");
		// post-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Assert.assertEquals("Wrong mediatype capabilities", "foo/bar", ordersResource.getMediaTypeCapabilities()
				.getProducedMimeTypes().get(0));
	}

	@Test
	public void shouldChangeWhenModifyingProducesAnnotation() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource customersResource = metamodel.getResources().getByPath("/customers");
		Assert.assertNotNull("CustomersResource not found", customersResource);
		// operation
		WorkbenchUtils.replaceFirstOccurrenceOfCode(customersResource.getJavaElement(),
				"{ \"application/vnd.bytesparadise.customer+xml\", "
						+ "MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }", "\"foo/bar\"");
		// post-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Assert.assertEquals("Wrong mediatype capabilities", "foo/bar", customersResource.getMediaTypeCapabilities()
				.getProducedMimeTypes().get(0));
	}

	@Test
	public void shouldChangeWhenRemovingProducesAnnotation() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource customersResource = metamodel.getResources().getByPath("/customers");
		Assert.assertNotNull("CustomersResource not found", customersResource);
		// operation
		WorkbenchUtils.removeFirstOccurrenceOfCode(customersResource.getJavaElement(),
				"@Produces({ \"application/vnd.bytesparadise.customer+xml\", "
						+ "MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })");
		// post-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Assert.assertTrue("Wrong mediatype capabilities", customersResource.getMediaTypeCapabilities()
				.getProducedMimeTypes().isEmpty());
	}

	@Test
	public void shouldChangeWhenAddingConsumesAnnotation() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource ordersResource = metamodel.getResources().getByPath("/orders");
		Assert.assertNotNull("OrdersResource not found", ordersResource);
		Assert.assertTrue("Wrong mediatype capabilities", ordersResource.getMediaTypeCapabilities()
				.getConsumedMimeTypes().isEmpty());
		// operation
		WorkbenchUtils.addTypeAnnotation(ordersResource.getJavaElement(),
				"import javax.ws.rs.Consumes;\n@Consumes(\"foo/bar\")");
		// post-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Assert.assertEquals("Wrong mediatype capabilities", "foo/bar", ordersResource.getMediaTypeCapabilities()
				.getConsumedMimeTypes().get(0));
	}

	@Test
	public void shouldChangeWhenModifyingConsumesAnnotation() throws CoreException {
		// pre-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource customersResource = metamodel.getResources().getByPath("/customers");
		Assert.assertNotNull("CustomersResource not found", customersResource);
		// operation
		WorkbenchUtils.replaceFirstOccurrenceOfCode(customersResource.getJavaElement(), "MediaType.APPLICATION_XML",
				"\"foo/bar\"");
		// post-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Assert.assertEquals("Wrong mediatype capabilities", "foo/bar", customersResource.getMediaTypeCapabilities()
				.getConsumedMimeTypes().get(0));
	}

	@Test
	public void shouldChangeWhenRemovingConsumesAnnotation() throws CoreException {
		// pre-condition
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Resource customersResource = metamodel.getResources().getByPath("/customers");
		Assert.assertNotNull("CustomersResource not found", customersResource);
		// operation
		WorkbenchUtils.removeFirstOccurrenceOfCode(customersResource.getJavaElement(),
				"@Consumes(MediaType.APPLICATION_XML)");
		// post-conditions
		Assert.assertEquals(5, metamodel.getResources().getAll().size());
		Assert.assertTrue("Wrong mediatype capabilities", customersResource.getMediaTypeCapabilities()
				.getConsumedMimeTypes().isEmpty());
	}

	@Test
	public void shouldReportErrorsWhenRemovingAnnotationImport() throws JavaModelException {
		// pre-condition
		Resources resources = metamodel.getResources();
		WorkbenchUtils.createCompilationUnit(javaProject, "FooResource.txt",
				"org.jboss.tools.ws.jaxrs.sample.services", "FooResource.java", bundle);
		Assert.assertEquals(6, resources.getAll().size());
		Resource fooResource = resources.getByPath("/foo");
		// operation 1
		WorkbenchUtils.removeImport(fooResource.getJavaElement().getCompilationUnit(), Path.class.getName());
		// post-conditions 1: errors reported (import is missing)
		Assert.assertEquals(6, metamodel.getHttpMethods().size());
		Assert.assertTrue("Resource not marked with errors", fooResource.hasErrors());
		// operation 2
		WorkbenchUtils.addImport(fooResource.getJavaElement().getCompilationUnit(), Path.class.getName());
		// post-conditions 2: errors reported (import is missing)
		Assert.assertEquals(6, metamodel.getHttpMethods().size());
		Assert.assertFalse("Resource still marked with errors", fooResource.hasErrors());
	}
}
