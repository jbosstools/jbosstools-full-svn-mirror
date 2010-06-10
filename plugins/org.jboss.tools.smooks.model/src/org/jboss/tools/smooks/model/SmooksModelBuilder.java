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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.milyn.javabean.dynamic.Model;
import org.milyn.javabean.dynamic.ModelBuilder;
import org.xml.sax.SAXException;

/**
 * {@link SmooksModel} Builder.
 * <p/>
 * Simple wrapper for the {@link ModelBuilder}.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class SmooksModelBuilder {

	private ModelBuilder modelBuilder;

	public SmooksModelBuilder() throws SAXException, IOException {
        modelBuilder = new ModelBuilder(SmooksModel.MODEL_DESCRIPTOR, false);
	}
	
	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	public Model<SmooksModel> readModel(InputStream configStream) throws SAXException, IOException {
        return modelBuilder.readModel(configStream, SmooksModel.class);		
	}
	
	public Model<SmooksModel> readModel(Reader configStream) throws SAXException, IOException {
        return modelBuilder.readModel(configStream, SmooksModel.class);		
	}
}
