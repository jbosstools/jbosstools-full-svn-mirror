package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.mapping.Table;
import org.hibernate.mediator.Messages;

public class DefaultDatabaseCollectorStub {
	public static final String CL = "org.hibernate.cfg.reveng.DefaultDatabaseCollector"; //$NON-NLS-1$

	protected DefaultDatabaseCollector defaultDatabaseCollector;

	protected DefaultDatabaseCollectorStub(Object defaultDatabaseCollector) {
		if (defaultDatabaseCollector == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.defaultDatabaseCollector = (DefaultDatabaseCollector)defaultDatabaseCollector;
	}
	
	public static DefaultDatabaseCollectorStub newInstance() {
		return new DefaultDatabaseCollectorStub(new DefaultDatabaseCollector());
	}

	@SuppressWarnings("unchecked")
	public Iterator<Entry<String, List<TableStub>>> getQualifierEntries() {
		Iterator<Map.Entry<String, List<Table>>> it = defaultDatabaseCollector.getQualifierEntries();
		HashMap<String, List<TableStub>> map = new HashMap<String, List<TableStub>>();
		while (it.hasNext()) {
			Map.Entry<String, List<Table>> entry = it.next();
			ArrayList<TableStub> arr = new ArrayList<TableStub>();
			Iterator<Table> itValue = (Iterator<Table>)entry.getValue().iterator();
			while (itValue.hasNext()) {
				Object obj = itValue.next();
				if (obj != null) {
					arr.add(new TableStub(obj));
				}
			}
			map.put(entry.getKey(), arr);
		}
		return map.entrySet().iterator();
	}
}
