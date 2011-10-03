/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.console.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.console.AbstractQueryPage;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.QueryInputModel;
import org.hibernate.engine.SessionImplementor;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * @author Dmitry Geraskov
 *
 */
public class JavaPage extends AbstractQueryPage {

    private String criteriaCode;
    private QueryResult queryResult;

    Criteria criteria = null;

    private Interpreter ip;

    /**
     * @param model
     * @param session2
     */
    public JavaPage(ConsoleConfiguration cfg, String criteriaCode, QueryInputModel model) {
		super(cfg, model);
        this.criteriaCode =  criteriaCode;
		setTabName(getQueryString().replace('\n', ' ').replace('\r', ' '));
    }

    @SuppressWarnings("unchecked")
	public void setSession(Session s) {
		super.setSession(s);
        try {
        	if(criteriaCode.indexOf( "System.exit" )>=0) { // TODO: externalize run so we don't need this bogus check! //$NON-NLS-1$
        		list = Collections.emptyList();
        		addException( new IllegalArgumentException(ConsoleMessages.JavaPage_not_allowed) );
        		return;
        	}
            ip = setupInterpreter(getSession() );
            Object o =  ip.eval(criteriaCode);
            // ugly! TODO: make un-ugly!
            if(o instanceof Criteria) {
                criteria = (Criteria) o;
                if(model.getMaxResults()!=null) {
                	criteria.setMaxResults( model.getMaxResults().intValue() );
                }
            } else if (o instanceof List<?>) {
                list = (List<Object>) o;
                if(model.getMaxResults()!=null) {
                	list = list.subList( 0, Math.min( list.size(), model.getMaxResults().intValue() ) );
                }
            } else {
                list = new ArrayList<Object>();
                list.add(o);
            }
        }
        catch (EvalError e) {
            addException(e);
        }
        catch (HibernateException e) {
        	addException(e);
        }
	}

    @SuppressWarnings("unchecked")
	private Interpreter setupInterpreter(Session session) throws EvalError, HibernateException {
        Interpreter interpreter = new Interpreter();

        interpreter.set("session", session); //$NON-NLS-1$
        interpreter.setClassLoader( Thread.currentThread().getContextClassLoader() );
        SessionImplementor si = (SessionImplementor)session;

        Map<String, ?> map = si.getFactory().getAllClassMetadata();

        Iterator<String> iterator = map.keySet().iterator();
        //TODO: filter non classes.
        StringBuilder imports = new StringBuilder(35*(2+map.size()));
        while (iterator.hasNext() ) {
            String element =  iterator.next();
            imports.append("import ").append(element).append(";\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        imports.append("import org.hibernate.criterion.*;\n"); //$NON-NLS-1$
        imports.append("import org.hibernate.*;\n"); //$NON-NLS-1$
        // TODO: expose the parameters as values to be used in the code.
        interpreter.eval(imports.toString());

        return interpreter;
    }

    public List<Object> getList() {
		if (criteriaCode==null) return Collections.emptyList();
		if (list == null) {
			try {
				list = new ArrayList<Object>();
				long startTime = System.currentTimeMillis();
				queryResult = getConsoleConfiguration().getConsoleConfigurationExtension()
						.executeCriteriaQuery(criteriaCode, model);
				Iterator<?> iter = queryResult.list().iterator(); // need to be user-controllable to toggle between iterate, scroll etc.
				queryTime = System.currentTimeMillis() - startTime;
				while (iter.hasNext() ) {
					Object element = iter.next();
					list.add(element);
				}
				pcs.firePropertyChange("list", null, list); //$NON-NLS-1$
			} catch (HibernateException e) {
				list = Collections.emptyList();
				addException(e);				                
			} catch (IllegalArgumentException e) {
				list = Collections.emptyList();
				addException(e);
			}
		}
		return list;
	}

    public List<String> getPathNames() {
    	try {
    		return queryResult == null ? Collections.<String>emptyList() : queryResult.getPathNames();
    	} catch (HibernateException e) {
			addException(e);				                
		}
    	return null;
    }

    public String getQueryString() {
        return criteriaCode;
    }
	public void setQueryString(String queryString) {
		this.criteriaCode = queryString;
		list = null;
		ip = null;
	}
}
