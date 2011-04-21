/**
 * JBoss, Home of Professional Open Source
 * Copyright 2009, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2009, JBoss Inc.
 */
package org.jboss.tools.smooks.model.javabean;

import org.jboss.tools.smooks.model.SmooksModelTestCase;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class BeanTest extends SmooksModelTestCase {

	public void test_v11() throws IOException, SAXException {
        test("v1_1/config-01.xml");
    }

    public void test_v12_01() throws IOException, SAXException {
        test("v1_2/config-01.xml");
    }

    public void test_v12_02() throws IOException, SAXException {
        // mixed namespaces...
        test("v1_2/config-02.xml");
    }

    public void test_v13_01() throws IOException, SAXException {
        test("v1_3/config-01.xml");
    }

    public void test_v13_02() throws IOException, SAXException {
        // mixed namespaces...
        test("v1_3/config-02.xml");
    }
}