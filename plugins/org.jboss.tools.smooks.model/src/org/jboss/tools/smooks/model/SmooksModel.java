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
package org.jboss.tools.smooks.model;

import org.jboss.tools.smooks.model.core.Component;
import org.jboss.tools.smooks.model.core.Params;
import org.jboss.tools.smooks.model.core.Reader;
import org.milyn.javabean.dynamic.Model;
import org.milyn.javabean.dynamic.serialize.DefaultNamespace;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Smooks Model Root.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@DefaultNamespace(uri = "http://www.milyn.org/xsd/smooks-1.1.xsd")
public class SmooksModel {

    public static final String MODEL_DESCRIPTOR = "org/jboss/tools/smooks/model/descriptor.properties";

    private Params params;
    private List<Reader> readers = new ArrayList<Reader>();
    private List<Component> components = new ArrayList<Component>();

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	public List<Reader> getReaders() {
        return readers;
    }

    public void setReaders(List<Reader> readers) {
        this.readers = readers;
    }

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}
}
