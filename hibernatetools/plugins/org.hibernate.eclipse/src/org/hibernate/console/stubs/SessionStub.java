package org.hibernate.console.stubs;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.ConsoleQueryParameter;
import org.hibernate.console.QueryInputModel;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.proxy.HibernateProxyHelper;
import org.hibernate.type.Type;

import bsh.EvalError;
import bsh.Interpreter;

public class SessionStub {

	protected ExecutionContext executionContext;
	protected Session session;

	protected SessionStub(ExecutionContext executionContext, Session session) {
		this.executionContext = executionContext;
		this.session = session;
	}

	public boolean isOpen() {
		boolean res = false;
		if (session != null) {
			res = session.isOpen();
		}
		return res;
	}

	public void close(List<Throwable> exceptions) {
		if (session != null && session.isOpen()) {
			try {
				session.close();
			} catch (HibernateException e) {
				exceptions.add(e);
			}
		}
	}

	public boolean contains(Object obj) {
		boolean res = false;
		if (session != null) {
			res = session.contains(obj);
		}
		return res;
	}

	public boolean hasMetaData(Object obj) {
		boolean res = false;
		if (session != null) {
			res = session.getSessionFactory().getClassMetadata(
					HibernateProxyHelper.getClassWithoutInitializingProxy(obj)) != null;
		}
		return res;
	}

	public boolean hasMetaData(Object obj, List<Throwable> exceptions) {
		boolean res = false;
		if (session != null) {
			try {
				res = session.getSessionFactory().getClassMetadata(
						HibernateProxyHelper.getClassWithoutInitializingProxy(obj)) != null;
			} catch (HibernateException e) {
				exceptions.add(e);
			}
		}
		return res;
	}

	public String getEntityName(Object obj) {
		String res = null;
		if (session != null) {
			res = session.getEntityName(obj);
		}
		return res;
	}
	
	public String getEntityName(Object obj, List<Throwable> exceptions) {
		String res = null;
		if (session != null) {
			try {
				res = session.getEntityName(obj);
			} catch (HibernateException e) {
				exceptions.add(e);
			}
		}
		return res;
	}

	public Object getPropertyValue(Object obj, Object id) {
		Object pv = null;
		if (session != null) {
			SessionFactory sf = session.getSessionFactory();
			ClassMetadata classMetadata;
			if (session.isOpen()) {
				classMetadata = sf.getClassMetadata(session.getEntityName(obj));
			} else {
				classMetadata = sf.getClassMetadata(HibernateProxyHelper
						.getClassWithoutInitializingProxy(obj));
			}
			if (id.equals(classMetadata.getIdentifierPropertyName())) {
				pv = classMetadata.getIdentifier(obj, EntityMode.POJO);
			} else {
				pv = classMetadata.getPropertyValue(obj, (String) id, EntityMode.POJO);
			}
			if (pv instanceof Collection<?>) {
				CollectionMetadata collectionMetadata = sf.getCollectionMetadata(classMetadata
						.getEntityName()
						+ "." + id); //$NON-NLS-1$
				if (collectionMetadata != null) {
					pv = new CollectionPropertySource((Collection<?>) pv);
				}
			}
		}
		return pv;
	}

	public IPropertyDescriptor[] getPropertyDescriptors(final Object obj) {
		IPropertyDescriptor[] propertyDescriptors = null;
		if (executionContext != null) {
			propertyDescriptors = (IPropertyDescriptor[]) executionContext.execute(new Command() {
				public Object execute() {
					SessionFactory sf = session.getSessionFactory();
					ClassMetadata classMetadata;
					if (session.isOpen()) {
						classMetadata = sf.getClassMetadata(session.getEntityName(obj));
					} else {
						classMetadata = sf.getClassMetadata(HibernateProxyHelper
								.getClassWithoutInitializingProxy(obj));
					}
					return initializePropertyDescriptors(classMetadata);
				}
			});
		}
		return propertyDescriptors;
	}

	protected IPropertyDescriptor[] initializePropertyDescriptors(ClassMetadata classMetadata) {

		String[] propertyNames = classMetadata.getPropertyNames();
		int length = propertyNames.length;

		PropertyDescriptor identifier = null;

		if (classMetadata.hasIdentifierProperty()) {
			identifier = new PropertyDescriptor(classMetadata.getIdentifierPropertyName(),
					classMetadata.getIdentifierPropertyName());
			identifier.setCategory(ConsoleMessages.EntityPropertySource_identifier);
			length++;
		}

		PropertyDescriptor[] properties = new PropertyDescriptor[length];

		int idx = 0;
		if (identifier != null) {
			properties[idx++] = identifier;
		}

		for (int i = 0; i < propertyNames.length; i++) {
			PropertyDescriptor prop = new PropertyDescriptor(propertyNames[i], propertyNames[i]);
			prop.setCategory(ConsoleMessages.EntityPropertySource_properties);
			properties[i + idx] = prop;
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	public List<Object> evalCriteria(String criteriaCode, QueryInputModel model, Time queryTime,
			List<Throwable> exceptions) {
		List<Object> res = Collections.emptyList();
		if (criteriaCode.indexOf("System.exit") >= 0) { // TODO: externalize run so we don't need this bogus check! //$NON-NLS-1$
			exceptions.add(new IllegalArgumentException(ConsoleMessages.JavaPage_not_allowed));
			return res;
		}
		try {
			Interpreter ip = setupInterpreter();
			final Integer maxResults = model.getMaxResults();
			long startTime = System.currentTimeMillis();
			Object o = ip.eval(criteriaCode);
			// ugly! TODO: make un-ugly!
			if (o instanceof Criteria) {
				Criteria criteria = (Criteria) o;
				if (maxResults != null) {
					criteria.setMaxResults(maxResults.intValue());
				}
				res = criteria.list();
			} else if (o instanceof List<?>) {
				res = (List<Object>) o;
				if (maxResults != null) {
					res = res.subList(0, Math.min(res.size(), maxResults.intValue()));
				}
			} else {
				res = new ArrayList<Object>();
				res.add(o);
			}
			queryTime.setTime(System.currentTimeMillis() - startTime);
		} catch (EvalError e) {
			exceptions.add(e);
		} catch (HibernateException e) {
			exceptions.add(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private Interpreter setupInterpreter() throws EvalError, HibernateException {
		Interpreter interpreter = new Interpreter();

		interpreter.set("session", session); //$NON-NLS-1$
		interpreter.setClassLoader(Thread.currentThread().getContextClassLoader());
		SessionImplementor si = (SessionImplementor) session;

		Map<String, ?> map = si.getFactory().getAllClassMetadata();

		Iterator<String> iterator = map.keySet().iterator();
		// TODO: filter non classes.
		String imports = ""; //$NON-NLS-1$
		while (iterator.hasNext()) {
			String element = iterator.next();
			imports += "import " + element + ";\n"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		imports += "import org.hibernate.criterion.*;\n"; //$NON-NLS-1$
		imports += "import org.hibernate.*;\n"; //$NON-NLS-1$
		// TODO: expose the parameters as values to be used in the code.
		interpreter.eval(imports);

		return interpreter;
	}

	public List<Object> evalQuery(String queryString, QueryInputModel model, Time queryTime,
			List<Throwable> exceptions) {
		List<Object> res = Collections.emptyList();
		Query query = null;
		try {
			query = session.createQuery(queryString);
		} catch (HibernateException e) {
			exceptions.add(e);
		} catch (Exception e) {
			exceptions.add(e);
		}
		if (query == null) {
			return res;
		}
		try {
			res = new ArrayList<Object>();
			setupParameters(query, model);
			long startTime = System.currentTimeMillis();
			// need to be user-controllable to toggle between iterate, scroll etc.
			Iterator<?> iter = query.list().iterator();
			queryTime.setTime(System.currentTimeMillis() - startTime);
			while (iter.hasNext()) {
				Object element = iter.next();
				res.add(element);
			}
		} catch (HibernateException e) {
			exceptions.add(e);
		} catch (IllegalArgumentException e) {
			exceptions.add(e);
		}
		return res;
	}

	private void setupParameters(Query query, QueryInputModel model) {
		if (model.getMaxResults() != null) {
			query.setMaxResults(model.getMaxResults().intValue());
		}
		ConsoleQueryParameter[] qp = model.getQueryParameters();
		for (int i = 0; i < qp.length; i++) {
			ConsoleQueryParameter parameter = qp[i];
			try {
				int pos = Integer.parseInt(parameter.getName());
				query.setParameter(pos, calcValue(parameter), parameter.getType());
			} catch (NumberFormatException nfe) {
				query.setParameter(parameter.getName(), calcValue(parameter), parameter.getType());
			}
		}
	}

	private Object calcValue(ConsoleQueryParameter parameter) {
		return parameter.getValueForQuery();
	}

	public List<String> evalQueryPathNames(String queryString, QueryInputModel model,
			List<Throwable> exceptions) {
		List<String> res = Collections.emptyList();
		Query query = null;
		try {
			query = session.createQuery(queryString);
		} catch (HibernateException e) {
			exceptions.add(e);
		} catch (Exception e) {
			exceptions.add(e);
		}
		if (query == null) {
			return res;
		}
		try {
			String[] returnAliases = null;
			try {
				returnAliases = query.getReturnAliases();
			} catch (NullPointerException e) {
				// ignore - http://opensource.atlassian.com/projects/hibernate/browse/HHH-2188
			}
			if (returnAliases == null) {
				Type[] t;
				try {
					t = query.getReturnTypes();
				} catch (NullPointerException npe) {
					t = new Type[] { null };
					// ignore - http://opensource.atlassian.com/projects/hibernate/browse/HHH-2188
				}
				res = new ArrayList<String>(t.length);
				for (int i = 0; i < t.length; i++) {
					Type type = t[i];
					if (type == null) {
						res.add("<multiple types>"); //$NON-NLS-1$
					} else {
						res.add(type.getName());
					}
				}
			} else {
				String[] t = returnAliases;
				res = new ArrayList<String>(t.length);
				for (int i = 0; i < t.length; i++) {
					res.add(t[i]);
				}
			}
		} catch (HibernateException e) {
			exceptions.add(e);
		}
		return res;
	}
}
