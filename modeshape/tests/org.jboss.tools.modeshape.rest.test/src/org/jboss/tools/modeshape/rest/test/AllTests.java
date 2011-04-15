package org.jboss.tools.modeshape.rest.test;

import org.jboss.tools.modeshape.rest.PersistedServerTest;
import org.jboss.tools.modeshape.rest.ServerManagerTest;
import org.jboss.tools.modeshape.rest.ServerRegistryEventTest;
import org.jboss.tools.modeshape.rest.preferences.IgnoredResourcesModelTest;
import org.jboss.tools.modeshape.rest.preferences.PublishingFileFilterTest;
import org.jboss.tools.modeshape.rest.preferences.ResourcePatternTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ IgnoredResourcesModelTest.class, PersistedServerTest.class, PublishingFileFilterTest.class,
        ResourcePatternTest.class, ServerManagerTest.class, ServerRegistryEventTest.class })
public class AllTests {
    // nothing to do
}
