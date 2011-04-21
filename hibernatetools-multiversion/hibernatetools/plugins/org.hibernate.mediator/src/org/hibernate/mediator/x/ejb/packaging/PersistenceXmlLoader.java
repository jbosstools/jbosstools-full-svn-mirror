package org.hibernate.mediator.x.ejb.packaging;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.mediator.x.spi.PersistenceUnitTransactionType;

import org.hibernate.mediator.base.HObject;
import org.xml.sax.EntityResolver;

public class PersistenceXmlLoader extends HObject {

	public static final String CL = "org.hibernate.ejb.packaging.PersistenceXmlLoader"; //$NON-NLS-1$

	protected PersistenceXmlLoader(Object persistenceXmlLoader) {
		super(persistenceXmlLoader, CL);
	}

    @SuppressWarnings("unchecked")
	public static List<PersistenceMetadata> deploy(URL url, Map overrides, EntityResolver resolver,
			   PersistenceUnitTransactionType defaultTransactionType) {
    	List list = (List)invokeStaticMethod(CL, mn(), url, overrides, resolver, defaultTransactionType);
    	List<PersistenceMetadata> res = new ArrayList<PersistenceMetadata>();
    	for (Object o : list) {
    		res.add(new PersistenceMetadata(o));
    	}
    	return res;
    }
}
