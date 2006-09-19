/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


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
        assertEquals("from A a where a.id in ((1, 2, 3, 4))", EditorContentHelper.convertJava2Hql("from A a where a.id in ((1, 2, 3, 4))"));
    }
    
}
