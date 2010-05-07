package org.hibernate.mediator.x.cfg.reveng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.mapping.Table;

public class DefaultDatabaseCollector extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.DefaultDatabaseCollector"; //$NON-NLS-1$

	protected DefaultDatabaseCollector(Object defaultDatabaseCollector) {
		super(defaultDatabaseCollector, CL);
	}
	
	public static DefaultDatabaseCollector newInstance() {
		return new DefaultDatabaseCollector(HObject.newInstance(CL));
	}

	@SuppressWarnings("unchecked")
	public Iterator<Entry<String, List<Table>>> getQualifierEntries() {
		Iterator<Map.Entry<String, List>> it = (Iterator<Map.Entry<String, List>>)invoke(mn());
		HashMap<String, List<Table>> map = new HashMap<String, List<Table>>();
		while (it.hasNext()) {
			Map.Entry<String, List> entry = it.next();
			ArrayList<Table> arr = new ArrayList<Table>();
			Iterator itValue = (Iterator)entry.getValue().iterator();
			while (itValue.hasNext()) {
				Object obj = itValue.next();
				if (obj != null) {
					arr.add(new Table(obj));
				}
			}
			map.put(entry.getKey(), arr);
		}
		return map.entrySet().iterator();
	}
}
