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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * @author MAX
 *
 * 
 */
public class JavaPage extends AbstractQueryPage {

    
    private String criteriaCode;    

    Criteria criteria = null;

    private Interpreter ip;

    /**
     * @param queryParameters 
     * @param session2
     */
    public JavaPage(ConsoleConfiguration cfg, String criteriaCode, ConsoleQueryParameter[] queryParameters) {
		super(cfg);
        this.criteriaCode =  criteriaCode;
    }

    public void setSession(Session s) {
		super.setSession(s);
        try {
        	if(criteriaCode.indexOf( "System.exit" )>=0) { // TODO: externalize run so we don't need this bogus check!
        		list = Collections.EMPTY_LIST;
        		addException( new IllegalArgumentException("System.exit not allowed!") );
        		return;
        	}
            ip = setupInterpreter(getSession() );
            Object o =  ip.eval(criteriaCode);
            // ugly! TODO: make un-ugly!
            if(o instanceof Criteria) {
                criteria = (Criteria) o;
            } else if (o instanceof List) {
                list = (List) o;
            } else {
                list = new ArrayList();
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
    
    private Interpreter setupInterpreter(Session session) throws EvalError, HibernateException {
        Interpreter interpreter = new Interpreter();
        
        interpreter.set("session", session);
        interpreter.setClassLoader( Thread.currentThread().getContextClassLoader() );
        SessionImplementor si = (SessionImplementor)session;
        
        Map map = si.getFactory().getAllClassMetadata();
        
        Iterator iterator = map.keySet().iterator();
        //TODO: filter non classes.
        String imports = new String();
        while (iterator.hasNext() ) {
            String element =  (String) iterator.next();
            imports += "import " + element + ";\n";
        }
        
        imports += "import org.hibernate.criterion.*;\n";
        imports += "import org.hibernate.*;\n";
        // TODO: expose the parameters as values to be used in the code.
        interpreter.eval(imports);
        
        return interpreter;
    }

    public List getList() {
        if(list!=null) return list;
        try {
            if(criteria!=null) {
                list = criteria.list();
            } 
            else {
                return Collections.EMPTY_LIST;
            }
        } 
        catch (HibernateException e) {
        	list = Collections.EMPTY_LIST;
            addException(e);
        } 
        return list;
    }

	public List getPathNames() {
        List l = new ArrayList();
        l.add("<no info>");       
        return l;
    }

    public String getQueryString() {
        return criteriaCode;
    }

    public void release() {
        
        super.release();
    }
}
