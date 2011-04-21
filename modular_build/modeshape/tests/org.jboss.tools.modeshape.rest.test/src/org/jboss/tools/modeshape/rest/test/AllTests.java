package org.jboss.tools.modeshape.rest.test;

import org.jboss.tools.modeshape.rest.PersistedServerTest;
import org.jboss.tools.modeshape.rest.ServerManagerTest;
import org.jboss.tools.modeshape.rest.ServerRegistryEventTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( Suite.class )
@Suite.SuiteClasses( {PersistedServerTest.class, ServerManagerTest.class, ServerRegistryEventTest.class} )
public class AllTests {
    // nothing to do
}
