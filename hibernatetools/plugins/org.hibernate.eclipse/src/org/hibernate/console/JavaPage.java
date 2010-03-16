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

/**
 * @author MAX
 */
public class JavaPage extends AbstractQueryPage {

	private String criteriaCode;

	/**
	 * @param model
	 * @param session2
	 */
	public JavaPage(String consoleConfigName, String criteriaCode, QueryInputModel model) {
		super(consoleConfigName, model);
		this.criteriaCode = criteriaCode;
		setTabName(getQueryString().replace('\n', ' ').replace('\r', ' '));
	}

	@Override
	public void setSessionStub(SessionStub s) {
		super.setSessionStub(s);
		evalCriteria();
	}

	public List<Object> getList() {
		if (list != null) {
			return list;
		}
		evalCriteria();
		return list;
	}

	protected void evalCriteria() {
		List<Throwable> arrExceptions = new ArrayList<Throwable>();
		Time qt = new Time(queryTime);
		list = getSessionStub().evalCriteria(criteriaCode, model, qt, arrExceptions);
		queryTime = qt.getTime();
		addExceptions(arrExceptions);
		pcs.firePropertyChange("list", null, list); //$NON-NLS-1$
	}

	public List<String> getPathNames() {
		List<String> l = new ArrayList<String>();
		l.add(ConsoleMessages.JavaPage_no_info);
		return l;
	}

	public String getQueryString() {
		return criteriaCode;
	}

	public void setQueryString(String queryString) {
		this.criteriaCode = queryString;
		list = null;
	}

	public void release() {
		super.release();
	}
}
