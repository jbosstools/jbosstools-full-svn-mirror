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
package org.hibernate.eclipse.console.views.properties;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.hibernate.console.QueryPage;
import org.hibernate.console.stubs.SessionStub;
import org.hibernate.eclipse.console.views.QueryPageTabView;

public class HibernatePropertySourceProvider implements IPropertySourceProvider
{	
	// TODO: refactor to be some interface that can provide currentsession and currentconfiguration
	private final QueryPageTabView view;

	public HibernatePropertySourceProvider(QueryPageTabView view) {
		this.view = view;
	}

	public IPropertySource getPropertySource(Object obj) {
		IPropertySource res = null;
		if (obj instanceof QueryPage) {
			res = new QueryPagePropertySource((QueryPage)obj);
		} else if (obj instanceof IPropertySource) {
			res = (IPropertySource) obj;
		} else {
			// maybe we should be hooked up with the queryview to get this ?
			QueryPage qp = view.getSelectedQueryPage();
			if (qp != null) {
				SessionStub sessionStub = qp.getSessionStub();
				String consoleConfigName = qp.getConsoleConfigName();
				if ((sessionStub.isOpen() && sessionStub.contains(obj)) || sessionStub.hasMetaData(obj)) {
					res = new EntityPropertySource(obj, sessionStub);	
				}
			}
		}
		return res;
	}
}