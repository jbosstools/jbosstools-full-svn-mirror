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
package org.jboss.tools.smooks.model.freemarker;

import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.Component;
import org.jboss.tools.smooks.model.core.Params;
import org.milyn.javabean.dynamic.Model;
import org.milyn.javabean.dynamic.serialize.DefaultNamespace;

/**
 * FreeMarker Template component model.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
@DefaultNamespace(uri = "http://www.milyn.org/xsd/smooks/freemarker-1.1.xsd", prefix = "ftl11")
public class FreeMarkerTemplate implements Component {
	
	private String template;
	private String applyOnElement;
	private String applyOnElementNS;
	private Boolean applyBefore;
	private Params params;
	
	public String getTemplate() {
		return template;
	}
	public FreeMarkerTemplate setTemplate(String template) {
		this.template = template;
		return this;
	}
	public String getApplyOnElement() {
		return applyOnElement;
	}
	public FreeMarkerTemplate setApplyOnElement(String applyOnElement) {
		this.applyOnElement = applyOnElement;
		return this;
	}
	public String getApplyOnElementNS() {
		return applyOnElementNS;
	}
	public FreeMarkerTemplate setApplyOnElementNS(String applyOnElementNS) {
		this.applyOnElementNS = applyOnElementNS;
		return this;
	}
	public Boolean getApplyBefore() {
		return applyBefore;
	}
	public FreeMarkerTemplate setApplyBefore(Boolean applyBefore) {
		this.applyBefore = applyBefore;
		return this;
	}
	public Params getParams() {
		if(params == null) {
			params = new Params();
		}
		return params;
	}
	public FreeMarkerTemplate setParams(Params params) {
		this.params = params;
		return this;
	}
	
	public static FreeMarkerTemplate newInstance(Model<SmooksModel> model) {
		FreeMarkerTemplate instance = new FreeMarkerTemplate();
		model.registerBean(instance);
		return instance;
	}
}
