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

import org.eclipse.jface.resource.ImageDescriptor;
import org.hibernate.mediator.stubs.AnyStub;
import org.hibernate.mediator.stubs.ArrayStub;
import org.hibernate.mediator.stubs.BagStub;
import org.hibernate.mediator.stubs.ColumnStub;
import org.hibernate.mediator.stubs.ComponentStub;
import org.hibernate.mediator.stubs.DependantValueStub;
import org.hibernate.mediator.stubs.IdentifierBagStub;
import org.hibernate.mediator.stubs.ListStub;
import org.hibernate.mediator.stubs.ManyToOneStub;
import org.hibernate.mediator.stubs.MapStub;
import org.hibernate.mediator.stubs.OneToManyStub;
import org.hibernate.mediator.stubs.OneToOneStub;
import org.hibernate.mediator.stubs.PersistentClassStub;
import org.hibernate.mediator.stubs.PrimitiveArrayStub;
import org.hibernate.mediator.stubs.PropertyStub;
import org.hibernate.mediator.stubs.SetStub;
import org.hibernate.mediator.stubs.SimpleValueStub;
import org.hibernate.mediator.stubs.TableStub;
import org.hibernate.mediator.stubs.TypeStub;
import org.hibernate.mediator.stubs.ValueStub;
import org.jboss.tools.hibernate.ui.diagram.UiPlugin;

/**
 * Map: ORM object -> Image descriptor 
 */
public class OrmImageMap {
	
	private OrmImageMap() {}

	public static ImageDescriptor getImageDescriptor(final Object obj) {
		String imageName = null;
		if (obj instanceof TableStub) {
			imageName = getImageName((TableStub)obj);
		} else if (obj instanceof ColumnStub) {
			imageName = getImageName((ColumnStub)obj);
		} else if (obj instanceof PropertyStub) {
			imageName = getImageName((PropertyStub)obj);
		} else if (obj instanceof OneToManyStub) {
			imageName = getImageName((OneToManyStub)obj);
		} else if (obj instanceof SimpleValueStub) {
			imageName = getImageName((SimpleValueStub)obj);
		} else if (obj instanceof PersistentClassStub) {
			imageName = getImageName((PersistentClassStub)obj);
		} else if (obj instanceof String) {
			imageName = "Image_Error"; //$NON-NLS-1$;
		}
		return UiPlugin.getImageDescriptor("images/" + ImageBundle.getString(imageName)); //$NON-NLS-1$
	}

	/**
	 * the image name for hierarchy:
	 * Table
	 * @param table
	 * @return
	 */
	public static String getImageName(TableStub table) {
		return "Image_DatabaseTable"; //$NON-NLS-1$
	}

	/**
	 * the image name for hierarchy:
	 * Column
	 * @param column
	 * @return
	 */
	public static String getImageName(ColumnStub column) {
		String str = "Image_DatabaseColumn"; //$NON-NLS-1$
		final boolean primaryKey = HibernateUtils.isPrimaryKey(column);
		final boolean foreignKey = HibernateUtils.isForeignKey(column);
		final TableStub table = HibernateUtils.getTable(column);
		if (column.isUnique()) {
			str = "Image_DatabaseUniqueKeyColumn"; //$NON-NLS-1$
		} else if (primaryKey && table != null && foreignKey) {
			str = "Image_DatabasePrimaryForeignKeysColumn"; //$NON-NLS-1$
		} else if (primaryKey) {
			str = "Image_DatabasePrimaryKeyColumn"; //$NON-NLS-1$
		} else if (table != null && foreignKey) {
			str = "Image_DatabaseForeignKeyColumn"; //$NON-NLS-1$
		}
		return str;

	}

	/**
	 * the image name for hierarchy:
	 * Property
	 * @param field
	 * @return
	 */
	public static String getImageName(PropertyStub field) {
		String str = "Image_PersistentFieldSimple"; //$NON-NLS-1$
		if (field == null) {
			return str;
		}
		final PersistentClassStub persistentClass = field.getPersistentClass(); 
		if (persistentClass != null && persistentClass.getVersion() == field) {
			str = "Image_PersistentFieldSimple_version"; //$NON-NLS-1$
		} else if (persistentClass != null && persistentClass.getIdentifierProperty() == field) {
			str = "Image_PersistentFieldSimple_id"; //$NON-NLS-1$
		} else if (field.getValue() != null) {
			final ValueStub value = field.getValue();
			if (value instanceof OneToManyStub) {
				str = "Image_PersistentFieldOne-to-many"; //$NON-NLS-1$
			} else if (value instanceof OneToOneStub) {
				str = "Image_PersistentFieldOne-to-one"; //$NON-NLS-1$
			} else if (value instanceof ManyToOneStub) {
				str = "Image_PersistentFieldMany-to-one"; //$NON-NLS-1$
			} else if (value instanceof AnyStub) {
				str = "Image_PersistentFieldAny"; //$NON-NLS-1$
			} else {
				TypeStub type = null;
				try {
					type = field.getType();
				} catch (Exception ex) {
					// ignore it
				}
				if (type != null && type.isCollectionType()) {
					if (value instanceof PrimitiveArrayStub) {
						str = "Image_Collection_primitive_array"; //$NON-NLS-1$
					} else if (value instanceof ArrayStub) {
						str = "Image_Collection_array"; //$NON-NLS-1$
					} else if (value instanceof ListStub) {
						str = "Image_Collection_list"; //$NON-NLS-1$
					} else if (value instanceof SetStub) {
						str = "Image_Collection_set"; //$NON-NLS-1$
					} else if (value instanceof MapStub) {
						str = "Image_Collection_map"; //$NON-NLS-1$
					} else if (value instanceof BagStub) {
						str = "Image_Collection_bag"; //$NON-NLS-1$
					} else if (value instanceof IdentifierBagStub) {
						str = "Image_Collection_idbag"; //$NON-NLS-1$
					} else {
						str = "Image_Collection"; //$NON-NLS-1$
					}
				}
			}
		} else if ("parent".equals(field.getName())) { //$NON-NLS-1$
			str = "Image_PersistentFieldParent"; //$NON-NLS-1$
		}
		return str;
	}

	/**
	 * the image name for hierarchy:
	 * OneToMany
	 * @param field
	 * @return
	 */
	public static String getImageName(OneToManyStub field) {
		return "Image_PersistentFieldOne-to-many"; //$NON-NLS-1$
	}

	/**
	 * the image name for hierarchy:
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
	public static String getImageName(SimpleValueStub field) {
		String res = "Image_PersistentFieldSimple"; //$NON-NLS-1$
		if (field instanceof AnyStub) {
			res = "Image_PersistentFieldMany-to-any"; //$NON-NLS-1$
		} else if (field instanceof ComponentStub) {
			res = "Image_PersistentFieldComponent"; //$NON-NLS-1$
		} else if (field instanceof DependantValueStub) {
			DependantValueStub mapping = (DependantValueStub)field;
			if (mapping.getTable().getIdentifierValue() == mapping) {
				res = "Image_PersistentFieldComponent_id"; //$NON-NLS-1$				
			}
		} else if (field instanceof ManyToOneStub) {
			res = "Image_PersistentFieldMany-to-many"; //$NON-NLS-1$
		}
		return res;
	}

	/**
	 * the image name for hierarchy:
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
	public static String getImageName(PersistentClassStub persistentClass) {
		return "Image_PersistentClass"; //$NON-NLS-1$
	}

}
