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

import java.util.ArrayList;
import java.util.List;

/**
 * Value binding configuration.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class Value {

    private String property;
    private String setterMethod;
    private String data;
    private String dataNS;
    private String decoder;
    private String defaultVal;
    private List<DecodeParam> decodeParams;
    
    public String getProperty() {
        return property;
    }

    public Value setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getSetterMethod() {
        return setterMethod;
    }

    public Value setSetterMethod(String setterMethod) {
        this.setterMethod = setterMethod;
        return this;
    }


    public String getData() {
        return data;
    }

    public Value setData(String data) {
        this.data = data;
		return this;
    }

    public String getDataNS() {
        return dataNS;
    }

    public Value setDataNS(String dataNS) {
        this.dataNS = dataNS;
		return this;
    }

    public String getDecoder() {
        return decoder;
    }

    public Value setDecoder(String decoder) {
        this.decoder = decoder;
		return this;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public Value setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
		return this;
    }

    public List<DecodeParam> getDecodeParams() {
    	if(decodeParams == null) {
    		decodeParams = new ArrayList<DecodeParam>();
    	}
        return decodeParams;
    }

    public Value setDecodeParams(List<DecodeParam> decodeParams) {
        this.decodeParams = decodeParams;
		return this;
    }
}
