package org.jboss.ide.eclipse.archives.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ArchivesTestSuite extends TestSuite {
    public static Test suite() { 
        TestSuite suite = new TestSuite("Archives Tests");

        suite.addTestSuite(FileIOTest.class);
        suite.addTestSuite(MarshallUnmarshallTest.class);
        suite.addTestSuite(ModelUtilTest.class);

        return suite; 
   }

}
