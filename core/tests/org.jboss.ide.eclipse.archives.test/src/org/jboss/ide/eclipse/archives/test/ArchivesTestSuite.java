package org.jboss.ide.eclipse.archives.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.ide.eclipse.archives.test.core.ArchivesCoreTest;
import org.jboss.ide.eclipse.archives.test.util.TruezipUtilTest;
import org.jboss.ide.eclipse.archives.test.xb.MarshallUnmarshallTest;
import org.jboss.ide.eclipse.archives.test.xb.ValidationTest;

public class ArchivesTestSuite extends TestSuite {
    public static Test suite() { 
        TestSuite suite = new TestSuite("Archives Tests");
        suite.addTestSuite(ArchivesCoreTest.class);
        suite.addTestSuite(MarshallUnmarshallTest.class);
        suite.addTestSuite(ValidationTest.class);
        suite.addTestSuite(TruezipUtilTest.class);
        suite.addTestSuite(ModelUtilTest.class);

        return suite; 
   }

}
