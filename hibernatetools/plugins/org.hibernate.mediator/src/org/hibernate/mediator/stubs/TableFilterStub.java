package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class TableFilterStub extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.TableFilter"; //$NON-NLS-1$

	protected TableFilterStub(Object tableFilter) {
		super(tableFilter, CL);
	}
	
	public static TableFilterStub newInstance() {
		return new TableFilterStub(newInstance(CL));
	}

	public void setExclude(Boolean exclude) {
		invoke(mn(), exclude);
	}

	public void setMatchCatalog(String matchCatalog) {
		invoke(mn(), matchCatalog);
	}

	public void setMatchName(String matchName) {
		invoke(mn(), matchName);
	}

	public void setMatchSchema(String matchSchema) {
		invoke(mn(), matchSchema);
	}

	public Boolean getExclude() {
		return (Boolean)invoke(mn());
	}

	public String getMatchCatalog() {
		return (String)invoke(mn());
	}

	public String getMatchSchema() {
		return (String)invoke(mn());
	}

	public String getMatchName() {
		return (String)invoke(mn());
	}

}
