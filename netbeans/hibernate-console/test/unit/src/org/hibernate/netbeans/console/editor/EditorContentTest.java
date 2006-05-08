package org.hibernate.netbeans.console.editor;

import junit.framework.TestCase;

/**
 * @author leon
 */
public class EditorContentTest extends TestCase {
    
    public EditorContentTest() {
    }

    public void testJava2Hql() {
        assertEquals("from A", EditorContentHelper.convertJava2Hql("\"from A\""));
        assertEquals("from A", EditorContentHelper.convertJava2Hql("\"from    A\""));
        assertEquals("from A, B, C", EditorContentHelper.convertJava2Hql("\"from A, \" + \" B, \" + \" C \""));
        assertEquals("from A\n,B", EditorContentHelper.convertJava2Hql("\"from A   \" +\n \",B\""));
        assertEquals("from\nA,\nB,\nC", EditorContentHelper.convertJava2Hql("\"from\"\n+\"A, \"\n+\"B, \"\n\"C\""));
        assertEquals("from A", EditorContentHelper.convertJava2Hql("from A.class.getName()"));
        assertEquals("from A", EditorContentHelper.convertJava2Hql("from A.class.getName(  )"));
        assertEquals("from A", EditorContentHelper.convertJava2Hql("from A.class.getName(  \n)"));
    }
    
}
