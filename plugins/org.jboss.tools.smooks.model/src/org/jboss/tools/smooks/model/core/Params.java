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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Params.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class Params {

	private List<Param> params;
	
	public List<Param> getParams() {
		if(params == null) {
			params = new ArrayList<Param>();
		}
		return params;
	}

	public Params setParams(List<Param> params) {
		this.params = params;
        return this;
	}
	
	public String getParam(String name) {
		if(params == null) {
			return null;
		}
		
		for(Param param : params) {
			String paramName = param.getName();
			if(paramName != null && paramName.equals(name)) {
				return param.getValue();
			}
		}
		
		return null;
	}
	
	public Params setParam(String name, String value) {
		if(params == null) {
			params = new ArrayList<Param>();
		}
		
		removeParam(name);
		params.add(new Param().setName(name).setValue(value));
		return this;
	}
	
	public Params removeParam(String name) {
		if(params == null) {
			return this;
		}

		Iterator<Param> paramsIterator = params.iterator();

		while(paramsIterator.hasNext()) {
			String paramName = paramsIterator.next().getName();
			if(paramName != null && paramName.equals(name)) {
				paramsIterator.remove();
				return this;
			}
		}
		
		return this;
	}
}
