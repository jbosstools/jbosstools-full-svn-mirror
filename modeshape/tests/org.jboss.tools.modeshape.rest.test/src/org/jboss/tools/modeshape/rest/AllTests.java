package org.jboss.tools.modeshape.rest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( Suite.class )
@Suite.SuiteClasses( {PersistedServerTest.class, ServerManagerTest.class, ServerRegistryEventTest.class} )
public class AllTests {
    // nothing to do
}
