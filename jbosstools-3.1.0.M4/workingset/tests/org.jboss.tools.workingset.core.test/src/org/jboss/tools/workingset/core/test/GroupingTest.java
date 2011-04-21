package org.jboss.tools.workingset.core.test;

import static org.junit.Assert.assertEquals;

import org.jboss.tools.workingset.internal.core.NameToWorkingSet;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class GroupingTest {

	static NameToWorkingSet nws = new NameToWorkingSet();
	
	@BeforeClass
	static public void setup() {

		nws.add("org\\.jboss\\.tools\\.([^\\.]+).*", "$1", true);
		nws.add("org\\.eclipse\\.([^\\.]+).*", "eclipse", false);
		nws.add("org\\.eclipse\\.([^\\.]+).*", "$1", true);
		nws.add("org\\.eclipse\\.([^\\.]+).*", "shouldnotbeseen", false);

	}
	
	@Test
	public void testNoMatch() {
 		
		assertEquals(nws.getWorkingSetNames("org.jboss.tools").length,0);
		String[] ws = nws.getWorkingSetNames("org.jboss.tools.vpe");		
		assertEquals(1,ws.length);
		assertEquals("vpe",ws[0]);
		
	}
	
	@Test
	public void testSingleMatch() {
		String[] ws = nws.getWorkingSetNames("org.jboss.tools.vpe.test");		
		assertEquals(1,ws.length);
		assertEquals("vpe",ws[0]);
		
		ws = nws.getWorkingSetNames("org.jboss.tools.vpe.ui.test");		
		assertEquals(1,ws.length);
		assertEquals("vpe",ws[0]);
		
		ws = nws.getWorkingSetNames("org.jboss.tools.hibernate.ui.test");		
		assertEquals(1,ws.length);
		assertEquals("hibernate",ws[0]);
		
		ws = nws.getWorkingSetNames("org.eclipse.jdt");		
		assertEquals(2,ws.length);
		assertEquals("eclipse",ws[0]);
		assertEquals("jdt",ws[1]);
		
	}
	
	@Test
	public void testMultiMatch() {
		String[] ws = nws.getWorkingSetNames("org.eclipse.jdt.ui");		
		assertEquals(2,ws.length);
		assertEquals("eclipse",ws[0]);
		assertEquals("jdt",ws[1]);		
			
	}

	}
