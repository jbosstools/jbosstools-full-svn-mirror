package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.mapping.Table;

public class DefaultDatabaseCollectorStub {
	protected DefaultDatabaseCollector defaultDatabaseCollector;

	protected DefaultDatabaseCollectorStub(Object defaultDatabaseCollector) {
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
				arr.add(new TableStub(itValue.next()));
			}
			map.put(entry.getKey(), arr);
		}
		return map.entrySet().iterator();
	}
}
