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

import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.Component;
import org.milyn.javabean.dynamic.Model;
import org.milyn.javabean.dynamic.serialize.DefaultNamespace;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean configuration.
 * <p/>
 * Corresponds to the top level &lt;jb:bean&gt; element. 
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@DefaultNamespace(uri = "http://www.milyn.org/xsd/smooks/javabean-1.3.xsd", prefix = "jb13")
public class Bean implements Component {

    private String beanId;
    private String beanClass;
    private String createOnElement;
    private String createOnElementNS;
    private List<Value> valueBindings;
    private List<Wiring> wireBindings;
    private List<Expression> expressionBindings;

    public String getBeanId() {
        return beanId;
    }

    public Bean setBeanId(String beanId) {
        this.beanId = beanId;
		return this;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public Bean setBeanClass(String beanClass) {
        this.beanClass = beanClass;
		return this;
    }

    public String getCreateOnElement() {
        return createOnElement;
    }

    public Bean setCreateOnElement(String createOnElement) {
        this.createOnElement = createOnElement;
		return this;
    }

    public String getCreateOnElementNS() {
        return createOnElementNS;
    }

    public Bean setCreateOnElementNS(String createOnElementNS) {
        this.createOnElementNS = createOnElementNS;
		return this;
    }

    public List<Value> getValueBindings() {
    	if(valueBindings == null) {
    		valueBindings = new ArrayList<Value>();
    	}
        return valueBindings;
    }

    public Bean setValueBindings(List<Value> valueBindings) {
        this.valueBindings = valueBindings;
		return this;
    }

    public List<Wiring> getWireBindings() {
    	if(wireBindings == null) {
    		wireBindings = new ArrayList<Wiring>();
    	}
        return wireBindings;
    }

    public Bean setWireBindings(List<Wiring> wireBindings) {
        this.wireBindings = wireBindings;
		return this;
    }

    public List<Expression> getExpressionBindings() {
    	if(expressionBindings == null) {
    		expressionBindings = new ArrayList<Expression>();
    	}
        return expressionBindings;
    }

    public Bean setExpressionBindings(List<Expression> expressionBindings) {
        this.expressionBindings = expressionBindings;
		return this;
    }

	public static Bean newInstance(Model<SmooksModel> model) {
		Bean instance = new Bean();
		model.registerBean(instance);
		return instance;
	}
}
