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
package org.jboss.tools.smooks.model.core;

import org.eclipse.emf.common.util.EList;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.SmooksModelTestCase;
import org.milyn.StreamFilterType;
import org.milyn.javabean.dynamic.Model;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class ParamsTest extends SmooksModelTestCase {
	
	public void test_manual() {
		GlobalParams params = new GlobalParams();
		assertEquals(null, params.getParam("a"));
		params.removeParam("a"); // make sure there's no exception
		params.setParam("a", "aVal");
		assertEquals("aVal", params.getParamValue("a"));
		params.setParam("a", "bVal");
		assertEquals("bVal", params.getParamValue("a"));
		params.setFilterType(StreamFilterType.SAX);
		assertTrue(params.getFilterType() == StreamFilterType.SAX);
		params.setFilterType(StreamFilterType.DOM);
		assertTrue(params.getFilterType() == StreamFilterType.DOM);
	}

	public void test_config_v11_01() throws IOException, SAXException {
        Model<SmooksModel> model = test("v1_1/config-01.xml");
        
        GlobalParams params = model.getModelRoot().getParams();
		
        assertTrue(params.getFilterType() == StreamFilterType.SAX);
        EList<IParam> paramList = params.getParams();
        for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
			IParam iParam = (IParam) iterator.next();
			if(iParam.getName().equals("b")){
				assertTrue(iParam.getType().equals("actived"));
			}else{
				assertTrue(iParam.getType()==null);
			}
		}
        params.setFilterType(StreamFilterType.DOM);
        assertModelEquals(model, "v1_1/config-02.xml");
    }

	public void test_config_v11_02() throws IOException, SAXException {
        Model<SmooksModel> model = test("v1_1/config-02.xml");
        
        GlobalParams params = model.getModelRoot().getParams();
		
        assertTrue(params.getFilterType() == StreamFilterType.DOM);
        
        params.setFilterType(StreamFilterType.SAX);
        assertModelEquals(model, "v1_1/config-01.xml");
    }

	public void test_config_v11_03() throws IOException, SAXException {
        test("v1_1/config-03.xml");
    }
}