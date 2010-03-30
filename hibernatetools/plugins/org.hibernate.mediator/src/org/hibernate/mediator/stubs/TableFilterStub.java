package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.TableFilter;

public class TableFilterStub {
	
	protected TableFilter tableFilter;

	protected TableFilterStub(Object tableFilter) {
		this.tableFilter = (TableFilter)tableFilter;
	}

	public void setExclude(Boolean exclude) {
		tableFilter.setExclude(exclude);
	}

	public void setMatchCatalog(String matchCatalog) {
		tableFilter.setMatchCatalog(matchCatalog);
	}

	public void setMatchName(String matchName) {
		tableFilter.setMatchName(matchName);
	}

	public void setMatchSchema(String matchSchema) {
		tableFilter.setMatchSchema(matchSchema);
	}

	public Boolean getExclude() {
		return tableFilter.getExclude();
	}

	public String getMatchCatalog() {
		return tableFilter.getMatchCatalog();
	}

	public String getMatchSchema() {
		return tableFilter.getMatchSchema();
	}

	public String getMatchName() {
		return tableFilter.getMatchName();
	}

}
