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
package org.hibernate.console;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.console.stubs.SessionStub;

public class HQLQueryPage extends AbstractQueryPage {

	private String queryString;
	
	public List<Object> getList() {
		if (list != null) {
			return list;
		}
		evalQuery();
		return list;
	}

	/**
	 * @param session
	 * @param string
	 * @param queryParameters 
	 */
	public HQLQueryPage(String consoleConfigName, String string, QueryInputModel model) {
		super(consoleConfigName, model);
		queryString = string;
		setTabName(getQueryString().replace('\n', ' ').replace('\r', ' '));
	}

	@Override
	public void setSessionStub(SessionStub s) {
		super.setSessionStub(s);
		evalQuery();
	}

	protected void evalQuery() {
		List<Throwable> arrExceptions = new ArrayList<Throwable>();
		Time qt = new Time(queryTime);
		list = getSessionStub().evalQuery(queryString, model, qt, arrExceptions);
		queryTime = qt.getTime();
		addExceptions(arrExceptions);
		pcs.firePropertyChange("list", null, list); //$NON-NLS-1$
	}
	
    /**
     * @return
     */
    public String getQueryString() {
    	return queryString; // cannot use query since it might be null because of an error!    
    }
	public void setQueryString(String queryString) {
		this.queryString = queryString;
		list = null;
	}

    public List<String> getPathNames() {
		List<Throwable> arrExceptions = new ArrayList<Throwable>();
		List<String> list = getSessionStub().evalQueryPathNames(queryString, model, arrExceptions);
		addExceptions(arrExceptions);
		return list;
    }
}