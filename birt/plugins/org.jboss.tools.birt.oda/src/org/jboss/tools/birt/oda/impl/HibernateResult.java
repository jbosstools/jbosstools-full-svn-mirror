package org.jboss.tools.birt.oda.impl;

import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.hibernate.EntityMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.jboss.tools.birt.oda.IOdaFactory;

public class HibernateResult {

	private List result;
	private Iterator iterator;
	private Type[] queryReturnTypes;
	private Object currentRow;
	private IOdaFactory odaFactory;
	private HibernateOdaQuery query;

	public HibernateResult(Query q, IOdaFactory odaFactory, HibernateOdaQuery query) {
		this.result = q.list();
		this.iterator = result.iterator();
		this.queryReturnTypes = q.getReturnTypes();
		this.odaFactory = odaFactory;
		this.query = query;
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
			if (queryReturnTypes.length > 0
					&& queryReturnTypes[0].isEntityType()) {
				String checkClass = ((HibernateResultSetMetaData) getMetaData())
						.getColumnClass(rstcol);
				SessionFactory sf = odaFactory.getSessionFactory();
				ClassMetadata metadata = sf.getClassMetadata(checkClass);
				if (metadata == null) {
					metadata = sf.getClassMetadata(obj.getClass());
				} 
				value = metadata.getPropertyValue(obj, getMetaData()
						.getColumnName(rstcol), EntityMode.POJO);
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

	public void close() {
		if (result != null) {
			result.clear();
		}
	}
}
