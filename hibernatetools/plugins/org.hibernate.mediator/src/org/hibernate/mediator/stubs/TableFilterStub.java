package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.TableFilter;
import org.hibernate.mediator.Messages;

public class TableFilterStub {
	
	protected TableFilter tableFilter;

	protected TableFilterStub(Object tableFilter) {
		if (tableFilter == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.tableFilter = (TableFilter)tableFilter;
	}
	
	public static TableFilterStub newInstance() {
		return new TableFilterStub(new TableFilter());
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
