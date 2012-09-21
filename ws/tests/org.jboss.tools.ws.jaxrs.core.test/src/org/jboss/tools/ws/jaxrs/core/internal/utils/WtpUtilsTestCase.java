package org.jboss.tools.ws.jaxrs.core.internal.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.jboss.tools.ws.jaxrs.core.jdt.EnumJaxrsClassname.APPLICATION;
import static org.junit.Assert.assertThat;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ISourceRange;
import org.jboss.tools.ws.jaxrs.core.AbstractCommonTestCase;
import org.jboss.tools.ws.jaxrs.core.WorkbenchUtils;
import org.junit.Test;

public class WtpUtilsTestCase extends AbstractCommonTestCase {

	@Test
	public void shouldRetrieveWebDeploymentDescriptor() throws Exception {
		// pre-conditions
		// operation
		final IResource webxml= WtpUtils.getWebDeploymentDescriptor(project);
		// verifications
		assertThat(webxml, notNullValue());
	}

	@Test
	public void shouldVerifyProjectHasWebDeploymentDescriptor() throws Exception {
		// pre-conditions
		// operation
		final boolean hasWebxml= WtpUtils.hasWebDeploymentDescriptor(project);
		// verifications
		assertThat(hasWebxml, equalTo(true));
	}

	
	@Test
	public void shouldRetrieveApplicationPathFromInWeb23xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_3-with-default-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, equalTo("/hello/*"));
	}

	@Test
	public void shouldNotRetrieveApplicationPathInWeb23xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_3-without-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, nullValue());
	}

	@Test
	public void shouldRetrieveApplicationPathInWeb24xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_4-with-default-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, equalTo("/hello/*"));
	}

	@Test
	public void shouldRetrieveApplicationPathLocationInWeb24xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_4-with-default-servlet-mapping.xml", bundle);
		// operation
		final ISourceRange location = WtpUtils.getApplicationPathLocation(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(location.getOffset(), not(equalTo(0)));
		assertThat(location.getLength(), not(equalTo(0)));
	}

	@Test
	public void shouldNotRetrieveApplicationPathInWeb24xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_4-without-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, nullValue());
	}
	
	@Test
	public void shouldNotRetrieveApplicationPathLocationInWeb24xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_4-without-servlet-mapping.xml", bundle);
		// operation
		final ISourceRange location = WtpUtils.getApplicationPathLocation(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(location, nullValue());
	}

	@Test
	public void shouldRetrieveApplicationPathInWeb25xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_5-with-default-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, equalTo("/hello/*"));
	}

	@Test
	public void shouldRetrieveApplicationPathLocationInWeb25xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_5-with-default-servlet-mapping.xml", bundle);
		// operation
		final ISourceRange location = WtpUtils.getApplicationPathLocation(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(location.getOffset(), not(equalTo(0)));
		assertThat(location.getLength(), not(equalTo(0)));
	}

	@Test
	public void shouldNotRetrieveApplicationPathInWeb25xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_5-without-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, nullValue());
	}

	@Test
	public void shouldNotRetrieveApplicationPathLocationInWeb25xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-2_5-without-servlet-mapping.xml", bundle);
		// operation
		final ISourceRange location = WtpUtils.getApplicationPathLocation(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(location, nullValue());
	}

	@Test
	public void shouldRetrieveApplicationPathInWeb30xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-3_0-with-default-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, equalTo("/hello/*"));

	}

	@Test
	public void shouldRetrieveApplicationPathLocationInWeb30xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-3_0-with-default-servlet-mapping.xml", bundle);
		// operation
		final ISourceRange location = WtpUtils.getApplicationPathLocation(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(location.getOffset(), not(equalTo(0)));
		assertThat(location.getLength(), not(equalTo(0)));
	}

	@Test
	public void shouldNotRetrieveApplicationPathInWeb30xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-3_0-without-servlet-mapping.xml", bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, nullValue());
	}

	@Test
	public void shouldNotRetrieveApplicationPathLocationInWeb30xml() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, "web-3_0-without-servlet-mapping.xml", bundle);
		// operation
		final ISourceRange location = WtpUtils.getApplicationPathLocation(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(location, nullValue());
	}

	@Test
	public void shouldNotRetrieveApplicationPathWhenWebXmlMissing() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, null, bundle);
		// operation
		final String applicationPath = WtpUtils.getApplicationPath(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(applicationPath, nullValue());
	}

	@Test
	public void shouldNotRetrieveApplicationPathLocationWhenWebxmlMissing() throws Exception {
		// pre-conditions
		final IResource webxmlResource = WorkbenchUtils.replaceDeploymentDescriptorWith(javaProject, null, bundle);
		// operation
		final ISourceRange location = WtpUtils.getApplicationPathLocation(webxmlResource, APPLICATION.qualifiedName);
		// verifications
		assertThat(location, nullValue());
	}


}
