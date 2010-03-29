/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.console.model.impl;

import org.hibernate.eclipse.console.model.ITableFilter;
import org.hibernate.mediator.stubs.TableFilterStub;

public class TableFilterImpl implements ITableFilter {

	TableFilterStub tf = new TableFilterStub();
	private final ReverseEngineeringDefinitionImpl revModel;
	
	protected TableFilterImpl(
			ReverseEngineeringDefinitionImpl reverseEngineeringDefinitionImpl) {
				this.revModel = reverseEngineeringDefinitionImpl;		
	}

	public void setExclude(Boolean exclude) {
		tf.setExclude(exclude);
		revModel.updateTableFilter(this);
	}

	public void setMatchCatalog(String catalog) {
		tf.setMatchCatalog(catalog);
		revModel.updateTableFilter(this);
	}

	public void setMatchSchema(String schema) {
		tf.setMatchSchema(schema);
		revModel.updateTableFilter(this);
	}

	public void setMatchName(String name) {
		tf.setMatchName(name);
		revModel.updateTableFilter(this);
	}

	public Boolean getExclude() {
		return tf.getExclude();		
	}

	public String getMatchCatalog() {
		return tf.getMatchCatalog();
	}

	public String getMatchSchema() {
		return tf.getMatchSchema();
	}

	public String getMatchName() {
		return tf.getMatchName();
	}

}
