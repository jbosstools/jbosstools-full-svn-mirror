package org.jboss.ide.eclipse.archives.test;

import org.jboss.ide.eclipse.archives.test.util.TruezipUtilTest;
import org.jboss.ide.eclipse.archives.test.xb.MarshallUnmarshallTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ArchivesTestSuite extends TestSuite {
    public static Test suite() { 
        TestSuite suite = new TestSuite("Archives Tests");

        suite.addTestSuite(TruezipUtilTest.class);
        suite.addTestSuite(MarshallUnmarshallTest.class);
        suite.addTestSuite(ModelUtilTest.class);

        return suite; 
   }

}
