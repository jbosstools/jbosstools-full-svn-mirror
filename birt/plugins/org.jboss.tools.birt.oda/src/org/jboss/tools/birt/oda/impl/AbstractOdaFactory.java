package org.jboss.tools.birt.oda.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.osgi.util.NLS;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.query.HQLQueryPlan;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.type.Type;
import org.jboss.tools.birt.oda.IOdaFactory;
import org.jboss.tools.birt.oda.Messages;

public abstract class AbstractOdaFactory implements IOdaFactory {

	protected SessionFactory sessionFactory;
	private int maxRows;
	//private List result;
	//private Iterator iterator;
	//private Type[] queryReturnTypes;
	//private Object currentRow;
	//private HibernateOdaQuery query;
	//private Session session;
	private String queryText;
	
	public void close() {
//		if (session != null) {
//			session.close();
//			session = null;
//		}
	}
	
	public boolean isOpen() {
		return getSessionFactory() != null;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public HibernateResultSetMetaData prepare(String queryText,Session session) throws OdaException {
		this.queryText = queryText;
		return parseQuery(session);
	}

	private HibernateResultSetMetaData parseQuery(Session session)
			throws OdaException {
		List arColsType = new ArrayList();
		List arCols = new ArrayList();
		List arColClass = new ArrayList();
		String[] props = null;
		try {
			Query query = session.createQuery(queryText);
			int maxRows = getMaxRows();
			if (maxRows > 0) {
				query.setFirstResult(0);
				query.setMaxResults(maxRows);
			}
			this.maxRows = maxRows;
			Type[] qryReturnTypes = query.getReturnTypes();
			if (qryReturnTypes.length > 0 && qryReturnTypes[0].isEntityType()) {
				for (int j = 0; j < qryReturnTypes.length; j++) {
					String clsName = qryReturnTypes[j].getName();
					props = getHibernateProp(clsName);
					for (int x = 0; x < props.length; x++) {
						String propType = getHibernatePropTypes(clsName,
								props[x]);
						if (DataTypes.isValidType(propType)) {
							arColsType.add(propType);
							arCols.add(props[x]);
							arColClass.add(clsName);
						} else {
							//throw new OdaException("Data Type is Not Valid");
							arColsType.add(DataTypes.UNKNOWN);
							arCols.add(props[x]);
							arColClass.add("java.lang.String"); //$NON-NLS-1$
						}
					}
				}
			} else {
				props = extractColumns(query.getQueryString());
				for (int t = 0; t < qryReturnTypes.length; t++) {
					if (DataTypes.isValidType(qryReturnTypes[t].getName())) {
						arColsType.add(qryReturnTypes[t].getName());
						arCols.add(props[t]);
					} else {
						throw new OdaException(NLS.bind(Messages.AbstractOdaFactory_The_type_is_not_valid,qryReturnTypes[t].getName()));
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
		}
	}

	private static String[] extractColumns(final String query) {
	    int fromPosition = query.toLowerCase().indexOf("from"); //$NON-NLS-1$
	    int selectPosition = query.toLowerCase().indexOf("select"); //$NON-NLS-1$
	    if (selectPosition >= 0) {
	        String columns = query.substring(selectPosition + 6, fromPosition);
	        StringTokenizer st = new StringTokenizer(columns, ","); //$NON-NLS-1$
	        List columnList = new ArrayList();
	        while (st.hasMoreTokens()) {
	            columnList.add(st.nextToken().trim());
	        }
	        return (String[]) columnList.toArray(new String[0]);
	    } else {
	        return null;
	    }
	}

	private String[] getHibernateProp(String className) {
	    SessionFactory sf = getSessionFactory();
	    String[] properties = sf.getClassMetadata(className).getPropertyNames();
	    return( properties);
	}

	private String getHibernatePropTypes(String className, String property) {
		SessionFactory sf = getSessionFactory();
	    Type hibClassProps = sf.getClassMetadata(className).getPropertyType(property);
	    return(hibClassProps.getName());
	
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public HibernateResult executeQuery(HibernateOdaQuery query,Session session) throws OdaException {
		try {
			Query q = session.createQuery(queryText);
			HibernateParameterMetaData parameterMetaData = (HibernateParameterMetaData) query.getParameterMetaData();
			List<Parameter> parameters = parameterMetaData.getParameters();
			int position=0;
			SessionFactoryImpl sfimpl = (SessionFactoryImpl) sessionFactory; // hack - to get to the actual queries..
			HQLQueryPlan plan = new HQLQueryPlan(queryText, false, Collections.EMPTY_MAP, sfimpl);
			int pCount = plan.getParameterMetadata().getOrdinalParameterCount();
			if (pCount > 0) {
				for(Parameter parameter:parameters) {
					q.setParameter(position++, parameter.getValue(), parameter.getHibernateType());
					if (position >= pCount)
						break;
				}
			}
			HibernateResult hibernateResult = new HibernateResult(q,this, query);
			return hibernateResult;
		} catch (HibernateException e) {
			throw new OdaException(e.getLocalizedMessage());
		}
	}
	
}
