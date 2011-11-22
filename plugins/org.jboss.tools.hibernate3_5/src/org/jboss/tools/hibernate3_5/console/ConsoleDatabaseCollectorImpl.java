/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate3_5.console;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.console.ext.api.ConsoleDatabaseCollector;
import org.hibernate.console.ext.api.ITable;
import org.hibernate.mapping.Table;
import org.jboss.tools.hibernate3_5.mapping.TableImpl;

/**
 * @author Dmitry Geraskov {geraskov@gmail.com}
 *
 */
public class ConsoleDatabaseCollectorImpl implements ConsoleDatabaseCollector {
	
	private DefaultDatabaseCollector collector;
	
	private Map<String, List<ITable>> qualifiers = new HashMap<String, List<ITable>>();
	
	public ConsoleDatabaseCollectorImpl(DefaultDatabaseCollector databaseCollector){
		this.collector = databaseCollector;
		this.initializeTableMap();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initializeTableMap(){
		Iterator qualifierEntries = collector.getQualifierEntries();
		while (qualifierEntries.hasNext()) {
			Entry<String, List<Table>> entry = (Entry<String, List<Table>>) qualifierEntries.next();
			List<ITable> list = new LinkedList<ITable>();
			for (Table table : entry.getValue()) {
				list.add(new TableImpl(table));
			}
			qualifiers.put(entry.getKey(), list);
		}
	}

	@Override
	public Iterator<Entry<String, List<ITable>>> getQualifierEntries() {
		return qualifiers.entrySet().iterator();
	}

}
