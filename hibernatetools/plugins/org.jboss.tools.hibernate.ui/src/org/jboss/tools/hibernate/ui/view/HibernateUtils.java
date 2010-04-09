/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.view;

import java.util.Iterator;

import org.hibernate.mediator.x.mapping.Column;
import org.hibernate.mediator.x.mapping.ForeignKey;
import org.hibernate.mediator.x.mapping.TableStub;

/**
 * @author some modifications from Vitali
 */
public class HibernateUtils {
	
	public static boolean isPrimaryKey(Column column) {
		TableStub table = getTable(column);
		if (table != null) {
			if (table.getPrimaryKey() != null) {
				if (table.getPrimaryKey().containsColumn(column)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isForeignKey(Column column) {
		TableStub table = getTable(column);
		if (table != null) {
			Iterator<ForeignKey> iter = table.getForeignKeyIterator();
			while (iter.hasNext()) {
				ForeignKey fk = iter.next();
				if (fk.containsColumn(column)) {
					return true;
				}
			}
		}
		return false;
		
	}
	
	public static TableStub getTable(Column column) {
		if (column.getValue() != null) {
			return column.getValue().getTable();
		}
		return null;
	}
}
