package org.hibernate.mediator.x;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.hibernate.mediator.Messages;
import org.hibernate.mediator.ConsoleQueryParameter;
import org.hibernate.mediator.QueryInputModel;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.util.CollectionPropertySource;
import org.hibernate.mediator.x.metadata.ClassMetadataStub;
import org.hibernate.mediator.x.metadata.CollectionMetadataStub;
import org.hibernate.mediator.x.proxy.HibernateProxyHelperStub;
import org.hibernate.mediator.x.type.TypeStub;

import bsh.EvalError;
import bsh.Interpreter;

public class SessionStub extends HObject {
	public static final String CL = "org.hibernate.Session"; //$NON-NLS-1$

	protected SessionStub(Object session) {
		super(session, CL);
	}

	public boolean isOpen() {
		boolean res = (Boolean)invoke(mn());
		return res;
	}

	public void close(List<Throwable> exceptions) {
		if (isOpen()) {
			try {
				invoke(mn());
			} catch (Exception e) {
				exceptions.add(e);
			}
		}
	}

	public boolean contains(Object obj) {
		boolean res = (Boolean)invoke(mn(), obj);
		return res;
	}

	public SessionFactoryStub getSessionFactory() {
		return new SessionFactoryStub(invoke(mn()));
	}

	public boolean hasMetaData(Object obj) {
		SessionFactoryStub sf = getSessionFactory();
		boolean res = sf.getClassMetadata(
			HibernateProxyHelperStub.getClassWithoutInitializingProxy(obj)) != null;
		return res;
	}

	public String getEntityName(Object obj) {
		String res = (String)invoke(mn(), obj);
		return res;
	}

	public Object getPropertyValue(Object obj, Object id) {
		Object pv = null;
		SessionFactoryStub sf = getSessionFactory();
		ClassMetadataStub classMetadata;
		if (isOpen()) {
			classMetadata = sf.getClassMetadata(getEntityName(obj));
		} else {
			classMetadata = sf.getClassMetadata(HibernateProxyHelperStub
					.getClassWithoutInitializingProxy(obj));
		}
		if (id.equals(classMetadata.getIdentifierPropertyName())) {
			pv = classMetadata.getIdentifier(obj, EntityModeStub.POJO);
		} else {
			pv = classMetadata.getPropertyValue(obj, (String) id, EntityModeStub.POJO);
		}
		if (pv instanceof Collection<?>) {
			CollectionMetadataStub collectionMetadata = sf.getCollectionMetadata(classMetadata
					.getEntityName()
					+ "." + id); //$NON-NLS-1$
			if (collectionMetadata != null) {
				pv = new CollectionPropertySource((Collection<?>) pv);
			}
		}
		return pv;
	}

	public IPropertyDescriptor[] getPropertyDescriptors(final Object obj) {
		SessionFactoryStub sf = getSessionFactory();
		ClassMetadataStub classMetadata;
		if (isOpen()) {
			classMetadata = sf.getClassMetadata(getEntityName(obj));
		} else {
			classMetadata = sf.getClassMetadata(HibernateProxyHelperStub
					.getClassWithoutInitializingProxy(obj));
		}
		return initializePropertyDescriptors(classMetadata);
	}

	protected IPropertyDescriptor[] initializePropertyDescriptors(ClassMetadataStub classMetadata) {

		String[] propertyNames = classMetadata.getPropertyNames();
		int length = propertyNames.length;

		PropertyDescriptor identifier = null;

		if (classMetadata.hasIdentifierProperty()) {
			identifier = new PropertyDescriptor(classMetadata.getIdentifierPropertyName(),
					classMetadata.getIdentifierPropertyName());
			identifier.setCategory(Messages.EntityPropertySource_identifier);
			length++;
		}

		PropertyDescriptor[] properties = new PropertyDescriptor[length];

		int idx = 0;
		if (identifier != null) {
			properties[idx++] = identifier;
		}

		for (int i = 0; i < propertyNames.length; i++) {
			PropertyDescriptor prop = new PropertyDescriptor(propertyNames[i], propertyNames[i]);
			prop.setCategory(Messages.EntityPropertySource_properties);
			properties[i + idx] = prop;
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	public List<Object> evalCriteria(String criteriaCode, QueryInputModel model, Time queryTime,
			List<Throwable> exceptions) {
		List<Object> res = Collections.emptyList();
		if (criteriaCode.indexOf("System.exit") >= 0) { // TODO: externalize run so we don't need this bogus check! //$NON-NLS-1$
			exceptions.add(new IllegalArgumentException(Messages.JavaPage_not_allowed));
			return res;
		}
		try {
			Interpreter ip = setupInterpreter();
			final Integer maxResults = model.getMaxResults();
			long startTime = System.currentTimeMillis();
			Object o = ip.eval(criteriaCode);
			// ugly! TODO: make un-ugly!
			if (CriteriaStub.CL.equals(o.getClass().getName())) {
				CriteriaStub criteria = new CriteriaStub(o);
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
		} catch (Exception e) {
			exceptions.add(e);
		}
		return res;
	}

	private Interpreter setupInterpreter() throws EvalError {
		Interpreter interpreter = new Interpreter();

		interpreter.set("session", Obj()); //$NON-NLS-1$
		interpreter.setClassLoader(Thread.currentThread().getContextClassLoader());

		List<String> classes = getSessionFactory().getClasses();

		Iterator<String> iterator = classes.iterator();
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
		QueryStub query = null;
		try {
			query = createQuery(queryString);
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
		} catch (IllegalArgumentException e) {
			exceptions.add(e);
		} catch (Exception e) {
			exceptions.add(e);
		}
		return res;
	}

	private void setupParameters(QueryStub query, QueryInputModel model) {
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
	
	public QueryStub createQuery(String queryString) {
		return new QueryStub(invoke(mn(), queryString));
	}

	public List<String> evalQueryPathNames(String queryString, QueryInputModel model,
			List<Throwable> exceptions) {
		List<String> res = Collections.emptyList();
		QueryStub query = null;
		try {
			query = createQuery(queryString);
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
				TypeStub[] t;
				try {
					t = query.getReturnTypes();
				} catch (NullPointerException npe) {
					t = new TypeStub[] { null };
					// ignore - http://opensource.atlassian.com/projects/hibernate/browse/HHH-2188
				}
				res = new ArrayList<String>(t.length);
				for (int i = 0; i < t.length; i++) {
					TypeStub type = t[i];
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
		} catch (Exception e) {
			exceptions.add(e);
		}
		return res;
	}
}
