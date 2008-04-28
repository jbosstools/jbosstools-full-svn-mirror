package org.jboss.ide.eclipse.archives.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.ide.eclipse.archives.test.core.ArchivesCoreTest;
import org.jboss.ide.eclipse.archives.test.model.ModelCreationTest;
import org.jboss.ide.eclipse.archives.test.model.ModelTruezipBridgeTest;
import org.jboss.ide.eclipse.archives.test.model.ModelUtilTest;
import org.jboss.ide.eclipse.archives.test.model.XBMarshallTest;
import org.jboss.ide.eclipse.archives.test.model.XBUnmarshallTest;
import org.jboss.ide.eclipse.archives.test.util.TruezipUtilTest;

public class ArchivesTestSuite extends TestSuite {
    public static Test suite() { 
        TestSuite suite = new TestSuite("Archives Tests");
        suite.addTestSuite(ArchivesCoreTest.class);
        suite.addTestSuite(XBMarshallTest.class);
        suite.addTestSuite(XBUnmarshallTest.class);
        suite.addTestSuite(TruezipUtilTest.class);
        suite.addTestSuite(ModelUtilTest.class);
        suite.addTestSuite(ModelCreationTest.class);
        suite.addTestSuite(ModelTruezipBridgeTest.class);
        return suite; 
   }

}
