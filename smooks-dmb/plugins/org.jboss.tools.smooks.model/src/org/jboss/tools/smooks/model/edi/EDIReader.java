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
package org.jboss.tools.smooks.model.edi;

import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.Reader;
import org.milyn.javabean.dynamic.Model;
import org.milyn.javabean.dynamic.serialize.DefaultNamespace;

/**
 * EDI Reader mapping model.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
@DefaultNamespace(uri = "http://www.milyn.org/xsd/smooks/edi-1.2.xsd", prefix = "csv12")
public class EDIReader implements Reader {

	private String mappingModel;
	private Boolean validate;

	public String getMappingModel() {
		return mappingModel;
	}
	public EDIReader setMappingModel(String mappingModel) {
		this.mappingModel = mappingModel;
		return this;
	}
	public Boolean getValidate() {
		return validate;
	}
	public EDIReader setValidate(Boolean validate) {
		this.validate = validate;
		return this;
	}
	
	public static EDIReader newInstance(Model<SmooksModel> model) {
		EDIReader instance = new EDIReader();
		model.registerBean(instance);
		return instance;
	}
}
