package org.hibernate.mediator.x.ejb.packaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.mediator.base.HObject;

public class PersistenceMetadata extends HObject {

	public static final String CL = "org.hibernate.ejb.packaging.PersistenceMetadata"; //$NON-NLS-1$

	protected PersistenceMetadata(Object persistenceMetadata) {
		super(persistenceMetadata, CL);
	}

	public String getName() {
		return (String)invoke(mn());
	}

	public void setName(Object name) {
		invoke(mn(), name);
	}

	public String getProvider() {
		return (String)invoke(mn());
	}

	public boolean getExcludeUnlistedClasses() {
		return (Boolean)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public List<String> getClasses() {
		return (List<String>)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public List<String> getPackages() {
		return (List<String>)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public List<String> getMappingFiles() {
		return (List<String>)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public List<NamedInputStream> getHbmfiles() {
		List list = (List)invoke(mn());
		List<NamedInputStream> res = new ArrayList<NamedInputStream>();
		for (Object obj : list) {
			res.add(new NamedInputStream(obj));
		}
		return res;
	}

	public Properties getProps() {
		return (Properties)invoke(mn());
	}
}
