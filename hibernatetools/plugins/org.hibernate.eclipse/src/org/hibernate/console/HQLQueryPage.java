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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;


public class HQLQueryPage extends AbstractQueryPage {

	private Query query;
	private String queryString;
	
	public List<Object> getList() {
		if (query==null) return Collections.emptyList();
		if (list == null) {
			try {
				
				//list = query.list();
				list = new ArrayList<Object>();
				setupParameters(query, model);
				long startTime = System.currentTimeMillis();
				Iterator<?> iter = query.list().iterator(); // need to be user-controllable to toggle between iterate, scroll etc.
				queryTime = System.currentTimeMillis() - startTime;
				while (iter.hasNext() ) {
					Object element = iter.next();
					list.add(element);
				}
				pcs.firePropertyChange("list", null, list); //$NON-NLS-1$
			} 
			catch (HibernateException e) {
				list = Collections.emptyList();
				addException(e);				                
			} catch (IllegalArgumentException e) {
				list = Collections.emptyList();
				addException(e);
			}
		}
		return list;
	}
		
	
	private void setupParameters(Query query2, QueryInputModel model) {
		
		if(model.getMaxResults()!=null) {
			query2.setMaxResults( model.getMaxResults().intValue() );
		}
		
		ConsoleQueryParameter[] qp = model.getQueryParameters();
		for (int i = 0; i < qp.length; i++) {
			ConsoleQueryParameter parameter = qp[i];
		
			try {
				int pos = Integer.parseInt(parameter.getName());
				//FIXME no method to set positioned list value
				query2.setParameter(pos, calcValue( parameter ), parameter.getType());
			} catch(NumberFormatException nfe) {
				Object value = parameter.getValue();
				if (value != null && value.getClass().isArray()){
					Object[] values = (Object[])value;
					query2.setParameterList(parameter.getName(), Arrays.asList(values), parameter.getType());
				} else {
					query2.setParameter(parameter.getName(), calcValue( parameter ), parameter.getType());
				}
			}
		}		
	}

	private Object calcValue(ConsoleQueryParameter parameter) {
		return parameter.getValueForQuery();				
	}

	/**
	 * @param session
	 * @param string
	 * @param queryParameters 
	 */
	public HQLQueryPage(ConsoleConfiguration cfg, String string, QueryInputModel model) {
		super(cfg, model);
		queryString = string;
		setTabName(getQueryString().replace('\n', ' ').replace('\r', ' '));
	}

	public void setSession(Session s) {
		super.setSession(s);
		try {			             
			query = this.getSession().createQuery(getHQLQueryString());
		} catch (HibernateException e) {
			addException(e);			
		} catch (Exception e) {
			addException( e );
		} 
	}
	
    /**
     * @return
     */
    public String getQueryString() {
    	return queryString; // cannot use query since it might be null because of an error!    
    }
    
    /**
     * The method removes SQL comments from <code>queryString</code>
     * as HSL doesn't support comments.
     * @return
     */
    public String getHQLQueryString(){
    	StringBuilder clearHQL = new StringBuilder();
    	int state = 0;
    	
		for (int j = 0; j < queryString.length(); j++) {
			if ((queryString.charAt(j) == '\n')
					|| ((queryString.charAt(j) == '\r')
							&& (j + 1 < queryString.length())
							&& (queryString.charAt(j + 1) == '\r'))) {
				state = 0;
			}

			switch (state) {
			case -1:// skip all till the end of the line
				break;
			case 0:// initial state
				switch (queryString.charAt(j)) {
				case '-':
					if (queryString.length() > j + 1 && queryString.charAt(j + 1) == '-') {
						state = -1;
					}
					break;
				case '\'':
					state = 1;
					break;
				}
				break;
			case 1:// quoted string
				/*
				 * Escape character for the quote is doubled quote:
				 * Example: 'This is escaped quote ('') inside 1 string'.
				 * Our parser switches to state 0 and back to state 1, hence works correct
				 * without additional efforts.
				 */
				if (queryString.charAt(j) == '\'') {/*there is no way to escape it in HQL string*/
					state = 0;
				}
				break;
			}
			if (state != -1) {
				clearHQL.append(queryString.charAt(j));
			}
		}
    	return clearHQL.toString();
    }
    
	public void setQueryString(String queryString) {
		this.queryString = queryString;
		list = null;
	}

    public List<String> getPathNames() {
    	List<String> l = Collections.emptyList();
    
    	try {
    		if(query==null) return l;
    		String[] returnAliases = null;
    		try {
    			returnAliases = query.getReturnAliases();
    		} catch(NullPointerException e) {
    			// ignore - http://opensource.atlassian.com/projects/hibernate/browse/HHH-2188
    		}
			if(returnAliases==null) {
    		Type[] t;
    		try {
			t = query.getReturnTypes();
    		} catch(NullPointerException npe) {
    			t = new Type[] { null };
    			// ignore - http://opensource.atlassian.com/projects/hibernate/browse/HHH-2188
    		}
    		l = new ArrayList<String>(t.length);
    
    		for (int i = 0; i < t.length; i++) {
    			Type type = t[i];
    			if(type==null) {
    			    l.add("<multiple types>");	 //$NON-NLS-1$
    			} else {
    				l.add(type.getName() );
    			}
    		}
    		} else {
    			String[] t = returnAliases;
        		l = new ArrayList<String>(t.length);
        
        		for (int i = 0; i < t.length; i++) {
        			l.add(t[i]);
        		}			
    		}
    	} catch (HibernateException he) {
    		addException(he);           
    	}
    
    	return l;
    }
}