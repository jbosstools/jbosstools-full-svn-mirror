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

import org.hibernate.eclipse.console.workbench.TypeNameValueVisitor;
import org.hibernate.mediator.x.mapping.ColumnStub;
import org.hibernate.mediator.x.mapping.ComponentStub;
import org.hibernate.mediator.x.mapping.DependantValueStub;
import org.hibernate.mediator.x.mapping.OneToManyStub;
import org.hibernate.mediator.x.mapping.PersistentClassStub;
import org.hibernate.mediator.x.mapping.PropertyStub;
import org.hibernate.mediator.x.mapping.SimpleValueStub;
import org.hibernate.mediator.x.mapping.TableStub;
import org.hibernate.mediator.x.mapping.ValueStub;
import org.hibernate.mediator.x.type.TypeStub;
import org.jboss.tools.hibernate.ui.diagram.editors.model.Utils;

/**
 * Map: ORM object -> label 
 * @author some modifications from Vitali
 */
public class OrmLabelMap {
	
	static private String SPACE = " "; //$NON-NLS-1$
	static private String POINTER = " -> "; //$NON-NLS-1$
	
	private OrmLabelMap() {}

	public static String getLabel(final Object obj) {
		String label = null;
		if (obj instanceof TableStub) {
			label = getParticularLabel((TableStub)obj);
		} else if (obj instanceof ColumnStub) {
			label = getParticularLabel((ColumnStub)obj);
		} else if (obj instanceof PropertyStub) {
			label = getParticularLabel((PropertyStub)obj);
		} else if (obj instanceof OneToManyStub) {
			label = getParticularLabel((OneToManyStub)obj);
		} else if (obj instanceof SimpleValueStub) {
			label = getParticularLabel((SimpleValueStub)obj);
		} else if (obj instanceof PersistentClassStub) {
			label = getParticularLabel((PersistentClassStub)obj);
		} else if (obj instanceof String) {
			label = (String)obj;
		} else {
			throw unknownElement(obj);
		}
		if (label == null) {
			label = UIViewMessages.OrmLabelProvider_orm_element;
		}
		return label;
	}

	public static String getParticularLabel(TableStub table) {
		return Utils.getTableName(table);
	}

	public static String getParticularLabel(ColumnStub column) {
		final String sqlType = column.getSqlType();
		StringBuffer name = new StringBuffer();
		name.append(column.getName());
		if (sqlType != null) {
			name.append(" ["); //$NON-NLS-1$
			name.append(sqlType.toUpperCase());
			name.append(column.isNullable() ? " Nullable" : ""); //$NON-NLS-1$ //$NON-NLS-2$
			name.append(HibernateUtils.getTable(column) != null
					&& HibernateUtils.isPrimaryKey(column) ? " PK" : ""); //$NON-NLS-1$ //$NON-NLS-2$
			name.append(HibernateUtils.getTable(column) != null
					&& HibernateUtils.isForeignKey(column) ? " FK" : ""); //$NON-NLS-1$ //$NON-NLS-2$
			name.append("]"); //$NON-NLS-1$
		}
		return name.toString();
	}

	public static String getParticularLabel(PropertyStub field) {
		StringBuffer name = new StringBuffer();
		name.append(field.getName());
		name.append(" :"); //$NON-NLS-1$
		String typeString = null;
		TypeStub type = null;
		try {
			type = field.getType();
		} catch (Exception e) {
			// ignore - this is only way to catch java.lang.reflect.InvocationTargetException
		}
		if (type != null && type.getReturnedClass() != null) {
			typeString = type.getReturnedClass().getName();
		} else {
			if (field.getValue() instanceof ComponentStub) {
				typeString = ((ComponentStub)field.getValue()).getComponentClassName();
			} else if (field.getValue()!= null && field.getValue().isSimpleValue()) {
				typeString = ((SimpleValueStub)field.getValue()).getTypeName();
			}
		}
		if (typeString != null) {
			typeString = correctTypeString(typeString);
			name.append(SPACE);
			name.append(typeString);
			return name.toString();
		}
		ValueStub value = field.getValue();
		String typeName = null;
		if (value != null) {
			typeName = (String) value.accept(new TypeNameValueVisitor(false));
			if (typeName != null) {
				return field.getName() + " : " + typeName; //$NON-NLS-1$
			}
		}
		return field.getName();
	}

	public static String getParticularLabel(OneToManyStub field) {
		return UIViewMessages.OrmLabelProvider_element;
	}

	/**
	 * the label for hierarchy:
	 * SimpleValue
	 * |-- Any
	 * |-- Component 
	 * |-- DependantValue
	 * |-- ToOne
	 *     |-- ManyToOne
	 *     |-- OneToOne
	 * @param field
	 * @return
	 */
	public static String getParticularLabel(SimpleValueStub field) {
		String label = UIViewMessages.OrmLabelProvider_element;
		if (field instanceof DependantValueStub) {
			label = "key"; //$NON-NLS-1$
		} else if (field instanceof ComponentStub) {
			label = "element"; //$NON-NLS-1$
		}
		return label;
	}

	/**
	 * the label for hierarchy:
	 * PersistentClass
	 * |-- RootClass
	 * |   |-- SpecialRootClass
	 * |
	 * |-- Subclass 
	 *     |-- JoinedSubclass
	 *     |-- SingleTableSubclass
	 *     |-- UnionSubclass
	 * @param persistentClass
	 * @return
	 */
	public static String getParticularLabel(PersistentClassStub persistentClass) {
		StringBuffer name = new StringBuffer();
		name.append(persistentClass.getEntityName() != null ? 
				persistentClass.getEntityName() : persistentClass.getClassName());
		TableStub table = persistentClass.getTable();
		if (table != null) {
			final String tableName = Utils.getTableName(table);
			if (tableName != null) {
				name.append(POINTER);
				name.append(tableName);
			}
		}
		return name.toString();
	}

	private static String correctTypeString(String str) {
		String ret = str;
		while (ret.startsWith("[")) { //$NON-NLS-1$
			ret = ret.substring(1).concat("[]"); //$NON-NLS-1$
		}
		switch (ret.toCharArray()[0]) {
		case 'Z': ret = "boolean".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'B': ret = "byte".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'C': ret = "char".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'L': ret = ret.substring(1);break;
		case 'D': ret = "double".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'F': ret = "float".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'I': ret = "int".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'J': ret = "long".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'S': ret = "short".concat(ret.substring(1));break; //$NON-NLS-1$
		}
		return ret;
	}

	private static RuntimeException unknownElement(Object element) {
		String msg = UIViewMessages.OrmLabelProvider_unknown_type_of_element_in_tree_of_type;
		if (element != null && element.getClass() != null ) {
			msg = msg + element.getClass().getName();
		} else {
			msg = msg + element;
		}
		return new RuntimeException(msg);

	}
}