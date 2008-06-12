/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.birt.oda.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.InitialContext;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.jboss.tools.birt.oda.Activator;
import org.jboss.tools.birt.oda.IOdaSessionFactory;

/**
 * 
 * @author snjeza
 * 
 */
public class ServerOdaSessionFactory implements IOdaSessionFactory {

	private static final Integer INTZERO = new Integer(0);
	private static Class[] emptyClassArg = new Class[0];
	private static Object[] emptyObjectArg = new Object[0];
	private Object sessionFactory;
	private Integer maxRows;
	private String queryText;
	private List result;
	private Iterator iterator;
	private Object currentRow;
	private HibernateOdaQuery query;
	private Object session;
	private Object[] queryReturnTypes;

	public ServerOdaSessionFactory(Properties properties) throws OdaException {
		getSessionFactory(properties);
		String maxRowString = properties.getProperty(IOdaSessionFactory.MAX_ROWS);
        try {
			maxRows = new Integer(maxRowString);
		} catch (NumberFormatException e) {
			// ignore
		}
	}

	public Object getSessionFactory(Properties properties)
			throws OdaException {
		String configurationName = properties.getProperty(CONFIGURATION);
		if (sessionFactory == null) {
			InitialContext ctx = null;
			try {
				ctx = new InitialContext();
				Object obj = ctx.lookup("java:/" + configurationName);
				sessionFactory = obj;
			} catch (Exception e) {
					e.printStackTrace();
				throw new OdaException(
				"Cannot create Hibernate session factory");
			}
			
		}
		return sessionFactory;
	}

	public Object getSessionFactory() {
		return sessionFactory;
	}

	public void close() {
		closeSession(session);
	}

	public boolean isOpen() {
		if (sessionFactory == null)
			return false;
		boolean isClosed = true;
		try {
			Method method = sessionFactory.getClass().getMethod("isClosed", new Class[0]);
			if (method != null) {
				Object closed = method.invoke(sessionFactory, new Object[0]);
				isClosed = ((Boolean) closed).booleanValue();
			}
		} catch (Exception e) {
			// ignore
		} 
		return !isClosed;
	}

	public int getMaxRows() {
		return maxRows;
	}

	public HibernateResultSetMetaData prepare(String queryText)
			throws OdaException {
		this.queryText = queryText;
		List arColsType = new ArrayList();
		List arCols = new ArrayList();
		List arColClass = new ArrayList();
		String[] props = null;
		Object session = null;
		
		try {
			session = openSession();
			Object query = createQuery(session, queryText);
			if (maxRows > 0) {
				Method setFirstResult = query.getClass().getMethod("setFirstResult", new Class[] {Integer.TYPE});
				setFirstResult.invoke(query, new Object[] {INTZERO});
				Method setMaxResults = query.getClass().getMethod("setMaxResults", new Class[] {Integer.TYPE});
				setMaxResults.invoke(query, new Object[] {new Integer(maxRows)});
			}
			Method getReturnTypes = query.getClass().getMethod("getReturnTypes", emptyClassArg);
			Object returnType = getReturnTypes.invoke(query, emptyObjectArg);
			Object[] qryReturnTypes = (Object[]) returnType;			
			if (checkEntityType(qryReturnTypes)) {
				for (int j = 0; j < qryReturnTypes.length; j++) {
					String clsName = getReturnTypeName(qryReturnTypes[j]);
					props = getHibernateProp(clsName);
					for (int x = 0; x < props.length; x++) {
						String propType = getHibernatePropTypes(clsName,
								props[x]);
						if (DataTypes.isValidType(propType)) {
							arColsType.add(propType);
							arCols.add(props[x]);
							arColClass.add(clsName);
						} else {
							arColsType.add(DataTypes.UNKNOWN);
							arCols.add(props[x]);
							arColClass.add("java.lang.String");
						}
					}
				}
			} else {
				props = extractColumns(queryText);
				for (int t = 0; t < qryReturnTypes.length; t++) {
					String typeName = getReturnTypeName(qryReturnTypes[t]);
					if (DataTypes.isValidType(typeName)) {
						arColsType.add(typeName);
						arCols.add(props[t]);
					} else {
						throw new OdaException("'"
								+ typeName
								+ "' is not a valid type.");
					}
				}
			}
			String[] arLabels = (String[]) arCols.toArray(new String[arCols
					.size()]);
			for (int j = 0; j < arLabels.length; j++) {
				arLabels[j] = arLabels[j].replace('.', ':');
			}

			return new HibernateResultSetMetaData(arLabels,
					(String[]) arColsType
							.toArray(new String[arColsType.size()]), arLabels,
					(String[]) arColClass
							.toArray(new String[arColClass.size()]));
		} catch (Exception e) {
			throw new OdaException(e.getLocalizedMessage());
		} finally {
			closeSession(session);
		}
	}

	private void closeSession(Object session) {
		if (session != null) {
			try {
				Method close = session.getClass().getMethod("close", emptyClassArg);
				close.invoke(session, emptyObjectArg);
				session = null;
			} catch (Exception e) {
				// ignore
			} 
		}
	}

	private boolean checkEntityType(Object[] qryReturnTypes) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object first = qryReturnTypes[0];
		Method isEntityType = first.getClass().getMethod("isEntityType", emptyClassArg);
		Object ret = isEntityType.invoke(first, emptyObjectArg);
		boolean isEntity = ((Boolean)ret).booleanValue();
		return qryReturnTypes.length > 0 && isEntity;
	}

	private Object createQuery(Object session, String queryText)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method createQuery = session.getClass().getMethod("createQuery", new Class[] {String.class});
		Object query = createQuery.invoke(session, new Object[] {queryText});
		return query;
	}

	private Object openSession() throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Object session;
		Method openSession = sessionFactory.getClass().getMethod("openSession", emptyClassArg);
		session = openSession.invoke(sessionFactory, emptyObjectArg);
		return session;
	}

	private static String[] extractColumns(final String query) {
        int fromPosition = query.toLowerCase().indexOf("from");
        int selectPosition = query.toLowerCase().indexOf("select");
        if (selectPosition >= 0) {
            String columns = query.substring(selectPosition + 6, fromPosition);
            StringTokenizer st = new StringTokenizer(columns, ",");
            List columnList = new ArrayList();
            while (st.hasMoreTokens()) {
                columnList.add(st.nextToken().trim());
            }
            return (String[]) columnList.toArray(new String[0]);
        } else {
            return null;
        }
    }
	
	private String getReturnTypeName(Object returnType) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method getName = returnType.getClass().getMethod("getName", emptyClassArg);
		Object name = getName.invoke(returnType, emptyObjectArg);
		return (String) name;
	}

	private  String[] getHibernateProp(String className) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Object classMetadata = getClassMetadata(className);
		Method getPropertyNames = classMetadata.getClass().getMethod("getPropertyNames", emptyClassArg);
		String[] properties = (String[]) getPropertyNames.invoke(classMetadata, emptyObjectArg);
        return properties;
    }

	private Object getClassMetadata(String className)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method getClassMetadata = sessionFactory.getClass().getMethod("getClassMetadata", new Class[] {String.class});
		Object classMetadata = getClassMetadata.invoke(sessionFactory, new Object[] {className});
		return classMetadata;
	} 
	
	private Object getClassMetadata(Class clazz) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Method getClassMetadata = sessionFactory.getClass().getMethod(
				"getClassMetadata", new Class[] { Class.class });
		Object classMetadata = getClassMetadata.invoke(sessionFactory,
				new Object[] { clazz });
		return classMetadata;
	}
	
	private String getHibernatePropTypes(String className, String property) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		Object classMetadata = getClassMetadata(className);
		Method getPropertyType = classMetadata.getClass().getMethod("getPropertyType", new Class[] {String.class});
		Object type = getPropertyType.invoke(classMetadata, new Object[] {property});
		return getReturnTypeName(type);
    }
	
	public void setMaxRows(int max) {
		maxRows = max;
	}

	public void executeQuery(HibernateOdaQuery query) throws OdaException {
		this.query = query;
		try {
			session = openSession();
			Object q = createQuery(session,queryText);
			Method list = q.getClass().getMethod("list", emptyClassArg);
			result = (List) list.invoke(q, emptyObjectArg);
			iterator = result.iterator();
			Method getReturnTypes = q.getClass().getMethod("getReturnTypes", emptyClassArg);
			this.queryReturnTypes = (Object[]) getReturnTypes.invoke(q, emptyObjectArg);
		} catch (Exception e) {
			throw new OdaException(e.getLocalizedMessage());
		}
	}

	public Iterator getIterator() {
		return iterator;
	}

	public List getResult() {
		return result;
	}

	public Object getResult(int rstcol) throws OdaException {
		Object obj = this.currentRow;
		Object value = null;
		try {
			if (checkEntityType(queryReturnTypes)) {
				String checkClass = ((HibernateResultSetMetaData) getMetaData())
						.getColumnClass(rstcol);
				Object metadata = getClassMetadata(checkClass);
				if (metadata == null) {
					metadata = getClassMetadata(obj.getClass());
				} 
				String className = "org.hibernate.EntityMode";
				Class pojo = Activator.classForName(className, getClass());
				Field pojoField = pojo.getField("POJO");
				Object POJO = pojoField.get(null);
				Class[] parameterTypes = new Class[] {Object.class,String.class, POJO.getClass()};
				Method getPropertyValue = metadata.getClass().getMethod("getPropertyValue", parameterTypes);
				Object[] args = new Object[] {obj,getMetaData().getColumnName(rstcol),POJO};
				value = getPropertyValue.invoke(metadata, args);
			} else {
				if (getMetaData().getColumnCount() == 1) {
					value = obj;
				} else {
					Object[] values = (Object[]) obj;
					value = values[rstcol - 1];
				}
			}
		} catch (Exception e) {
			throw new OdaException(e.getLocalizedMessage());
		}
		return (value);
	}

	public void next() {
		currentRow = getIterator().next();
	}

	private IResultSetMetaData getMetaData() throws OdaException {
		return query.getMetaData();
	}
}
