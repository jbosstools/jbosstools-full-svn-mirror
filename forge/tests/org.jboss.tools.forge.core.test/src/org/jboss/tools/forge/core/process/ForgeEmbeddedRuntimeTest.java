package org.jboss.tools.forge.core.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ForgeEmbeddedRuntimeTest {
	
	private String location = null;
	
	@Before 
	public void setUp() throws Exception {
		File file = FileLocator.getBundleFile(Platform.getBundle("org.jboss.tools.forge.runtime"));
		for (String str : file.list()) {
			if (str.startsWith("forge-distribution-")) {
				location = file.getAbsolutePath() + File.separator + str;
			}
		}
	}
	
	@After
	public void tearDown() {
		location = null;
	}
	

	@Test
	public void testForgeEmbeddedRuntime() {
		assertNotNull(ForgeEmbeddedRuntime.INSTANCE);
		assertEquals("embedded", ForgeEmbeddedRuntime.INSTANCE.getName());
		assertEquals(location, ForgeEmbeddedRuntime.INSTANCE.getLocation());
		assertEquals("embedded", ForgeEmbeddedRuntime.INSTANCE.getType());
	}

}
