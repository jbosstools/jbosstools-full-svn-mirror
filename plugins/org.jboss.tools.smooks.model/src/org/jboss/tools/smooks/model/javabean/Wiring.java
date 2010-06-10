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

/**
 * Wiring binding configuration.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class Wiring extends Binding {

    private String beanIdRef;
    private String wireOnElement;
    private String wireOnElementNS;

    public String getBeanIdRef() {
        return beanIdRef;
    }

    public void setBeanIdRef(String beanIdRef) {
        this.beanIdRef = beanIdRef;
    }

    public String getWireOnElement() {
        return wireOnElement;
    }

    public void setWireOnElement(String wireOnElement) {
        this.wireOnElement = wireOnElement;
    }

    public String getWireOnElementNS() {
        return wireOnElementNS;
    }

    public void setWireOnElementNS(String wireOnElementNS) {
        this.wireOnElementNS = wireOnElementNS;
    }
}
