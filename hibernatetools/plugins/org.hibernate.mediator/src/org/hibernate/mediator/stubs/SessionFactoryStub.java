package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.mediator.Messages;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.util.ELTransformer;
import org.hibernate.mediator.util.QLFormatHelper;

public class SessionFactoryStub extends HObject {
	public static final String CL = "org.hibernate.SessionFactory"; //$NON-NLS-1$

	protected SessionFactoryStub(Object sessionFactory) {
		super(sessionFactory, CL);
	}

	public boolean isSessionFactoryCreated() {
		return Obj() != null;
	}

	public SessionStub openSession() {
		if (Obj() != null) {
			return new SessionStub(invoke(mn()));
		}
		return null;
	}

	public void close() {
		if (Obj() != null) {
			invoke(mn());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getClasses() {
		List<String> res = Collections.emptyList();
		if (Obj() != null) {
			res = new ArrayList<String>();
			res.addAll(((Map)invoke("getAllClassMetadata")).keySet()); //$NON-NLS-1$
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Map<String, ClassMetadataStub> getClassMetaData() {
		if (Obj() == null) {
			return new HashMap<String, ClassMetadataStub>();
		}
		Map allClassMetadata = (Map)invoke("getAllClassMetadata"); //$NON-NLS-1$
		Iterator it = allClassMetadata.entrySet().iterator();
		Map<String, ClassMetadataStub> res = new HashMap<String, ClassMetadataStub>();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			res.put((String)entry.getKey(), new ClassMetadataStub(entry.getValue()));
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Map<String, CollectionMetadataStub> getCollectionMetaData() {
		if (Obj() == null) {
			return new HashMap<String, CollectionMetadataStub>();
		}
		Map allClassMetadata = (Map)invoke("getAllCollectionMetadata"); //$NON-NLS-1$
		Iterator it = allClassMetadata.entrySet().iterator();
		Map<String, CollectionMetadataStub> res = new HashMap<String, CollectionMetadataStub>();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			res.put((String)entry.getKey(), new CollectionMetadataStub(entry.getValue()));
		}
		return res;
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
		if (Obj() != null) {
			if (allowEL) {
				query = ELTransformer.removeEL(query);
			}
			HQLQueryPlanStub.newInstance(query, false, Collections.EMPTY_MAP, this);
		}
	}

	public String generateSQL(String query) {
		try {
			StringBuffer str = new StringBuffer(256);
			HQLQueryPlanStub plan = HQLQueryPlanStub.newInstance(query, false, Collections.EMPTY_MAP, this);

			QueryTranslatorStub[] translators = plan.getTranslators();
			for (int i = 0; i < translators.length; i++) {
				QueryTranslatorStub translator = translators[i];
				if (translator.isManipulationStatement()) {
					str.append(Messages.DynamicSQLPreviewView_manipulation_of + i
							+ ":"); //$NON-NLS-1$
					Iterator<?> iterator = translator.getQuerySpaces().iterator();
					while (iterator.hasNext()) {
						Object qspace = iterator.next();
						str.append(qspace);
						if (iterator.hasNext()) {
							str.append(", ");} //$NON-NLS-1$
					}

				} else {
					TypeStub[] returnTypes = translator.getReturnTypes();
					str.append(i + ": "); //$NON-NLS-1$
					for (int j = 0; j < returnTypes.length; j++) {
						TypeStub returnType = returnTypes[j];
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
						msgs.append(Messages.DynamicSQLPreviewView_caused_by);
				}
			}
			// t.printStackTrace(new PrintWriter(sw));
			// return sw.getBuffer().toString();
			return msgs.toString();
		}
	}

	public ClassMetadataStub getClassMetadata(String entityName) {
		Object obj = invoke(mn(), entityName);
		if (obj == null) {
			return null;
		}
		return new ClassMetadataStub(obj);
	}

	public CollectionMetadataStub getCollectionMetadata(String roleName) {
		Object obj = invoke(mn(), roleName);
		if (obj == null) {
			return null;
		}
		return new CollectionMetadataStub(obj);
	}

	public ClassMetadataStub getClassMetadata(Class<?> classWithoutInitializingProxy) {
		Object obj = invoke(mn(), classWithoutInitializingProxy);
		if (obj == null) {
			return null;
		}
		return new ClassMetadataStub(obj);
	}
}
