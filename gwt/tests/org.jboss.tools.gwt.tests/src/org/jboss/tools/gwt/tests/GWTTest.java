package org.jboss.tools.gwt.tests;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import org.osgi.framework.Bundle;

import static org.junit.Assert.*;

public class GWTTest {

	@Test
	public void testGwtPlugisArePresentAndActivated() {
		Bundle bundle = Platform.getBundle("org.jboss.tools.gwt.core");
		assertNotNull(bundle);
		bundle = Platform.getBundle("org.jboss.tools.gwt.ui");
		assertNotNull(bundle);
	}
}
