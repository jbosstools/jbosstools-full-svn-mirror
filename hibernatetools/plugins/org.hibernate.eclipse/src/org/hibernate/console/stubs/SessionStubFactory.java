package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.util.ELTransformer;
import org.hibernate.console.util.QLFormatHelper;
import org.hibernate.engine.query.HQLQueryPlan;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.type.Type;

public class SessionStubFactory {

	protected ExecutionContext executionContext;
	protected SessionFactory sessionFactory;

	public SessionStubFactory(ExecutionContext executionContext, Configuration cfg) {
		this.executionContext = executionContext;
		sessionFactory = cfg.buildSessionFactory();
	}

	public boolean isSessionFactoryCreated() {
		return sessionFactory != null;
	}

	public SessionStub openSession() {
		if (sessionFactory != null) {
			return new SessionStub(executionContext, sessionFactory.openSession());
		}
		return null;
	}

	public void close() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getClasses() {
		List<String> res = Collections.emptyList();
		if (sessionFactory != null) {
			res = new ArrayList<String>();
			res.addAll(sessionFactory.getAllClassMetadata().keySet());
		}
		return res;
	}

	// TODO: get rid of this - ClassMetadata - should not be public
	@SuppressWarnings("unchecked")
	public Map<String, ClassMetadata> getClassMetaData() {
		if (sessionFactory == null) {
			return new HashMap<String, ClassMetadata>();
		}
		return sessionFactory.getAllClassMetadata();
	}
	// TODO: get rid of this - ClassMetadata - should not be public
	@SuppressWarnings("unchecked")
	public Map<String, CollectionMetadata> getCollectionMetaData() {
		if (sessionFactory == null) {
			return new HashMap<String, CollectionMetadata>();
		}
		return sessionFactory.getAllCollectionMetadata();
	}
	
	/**
	 * Given a ConsoleConfiguration and a query this method validates the query through hibernate if
	 * a sessionfactory is available.
	 * 
	 * @param query
	 * @param allowEL
	 *            if true, EL syntax will be replaced as a named variable
	 */
	public void checkQuery(String query, boolean allowEL) {
		if (sessionFactory != null) {
			if (allowEL) {
				query = ELTransformer.removeEL(query);
			}
			new HQLQueryPlan(query, false, Collections.EMPTY_MAP,
					(SessionFactoryImpl) sessionFactory);
		}
	}

	public String generateSQL(String query) {
		try {
			SessionFactoryImpl sfimpl = (SessionFactoryImpl) sessionFactory; // hack - to get to the
																				// actual queries..
			StringBuffer str = new StringBuffer(256);
			HQLQueryPlan plan = new HQLQueryPlan(query, false, Collections.EMPTY_MAP, sfimpl);

			QueryTranslator[] translators = plan.getTranslators();
			for (int i = 0; i < translators.length; i++) {
				QueryTranslator translator = translators[i];
				if (translator.isManipulationStatement()) {
					str.append(ConsoleMessages.DynamicSQLPreviewView_manipulation_of + i
							+ ":"); //$NON-NLS-1$
					Iterator<?> iterator = translator.getQuerySpaces().iterator();
					while (iterator.hasNext()) {
						Object qspace = iterator.next();
						str.append(qspace);
						if (iterator.hasNext()) {
							str.append(", ");} //$NON-NLS-1$
					}

				} else {
					Type[] returnTypes = translator.getReturnTypes();
					str.append(i + ": "); //$NON-NLS-1$
					for (int j = 0; j < returnTypes.length; j++) {
						Type returnType = returnTypes[j];
						str.append(returnType.getName());
						if (j < returnTypes.length - 1) {
							str.append(", ");} //$NON-NLS-1$
					}
				}
				str.append("\n-----------------\n"); //$NON-NLS-1$
				Iterator<?> sqls = translator.collectSqlStrings().iterator();
				while (sqls.hasNext()) {
					String sql = (String) sqls.next();
					str.append(QLFormatHelper.formatForScreen(sql));
					str.append("\n\n"); //$NON-NLS-1$
				}
			}
			return str.toString();
		} catch (Throwable t) {
			// StringWriter sw = new StringWriter();
			StringBuffer msgs = new StringBuffer();

			Throwable cause = t;
			while (cause != null) {
				msgs.append(t);
				if (cause.getCause() == cause) {
					cause = null;
				} else {
					cause = cause.getCause();
					if (cause != null)
						msgs.append(ConsoleMessages.DynamicSQLPreviewView_caused_by);
				}
			}
			// t.printStackTrace(new PrintWriter(sw));
			// return sw.getBuffer().toString();
			return msgs.toString();
		}
	}
}
