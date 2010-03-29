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
package org.jboss.tools.hibernate.ui.diagram.editors.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.hibernate.mediator.stubs.ColumnStub;
import org.hibernate.mediator.stubs.ManyToOneStub;
import org.hibernate.mediator.stubs.OneToManyStub;
import org.hibernate.mediator.stubs.OneToOneStub;
import org.hibernate.mediator.stubs.PropertyStub;
import org.hibernate.mediator.stubs.RootClassStub;
import org.hibernate.mediator.stubs.TableStub;

/**
 * Directed connection between 2 shapes, from source to target. 
 *
 * @author ?
 * @author Vitali Yemialyanchyk
 */
public class Connection extends BaseElement {
	
	private static IPropertyDescriptor[] descriptors_connection_class_mapping;
	private static IPropertyDescriptor[] descriptors_connection_property_mapping;
	private static IPropertyDescriptor[] descriptors_connection_association;
	private static IPropertyDescriptor[] descriptors_connection_foreign_key_constraint;

	private static final String PROPERTY_SOURCE = "source"; //$NON-NLS-1$
	private static final String PROPERTY_TARGET = "target"; //$NON-NLS-1$
	private static final String PROPERTY_TYPE = "type"; //$NON-NLS-1$
	private static final String PROPERTY_CLASS_NAME = "className"; //$NON-NLS-1$
	private static final String PROPERTY_TABLE_NAME = "tableName"; //$NON-NLS-1$
	private static final String PROPERTY_CLASS_FIELD = "classField"; //$NON-NLS-1$
	private static final String PROPERTY_TABLE_FIELD = "tableField"; //$NON-NLS-1$
	private static final String PROPERTY_CLASS_FIELD_TYPE = "classFieldType"; //$NON-NLS-1$
	private static final String PROPERTY_TABLE_FIELD_TYPE = "tableFieldType"; //$NON-NLS-1$
	private static final String PROPERTY_ASSOCIATION_TYPE = "associationType"; //$NON-NLS-1$
	private static final String PROPERTY_SOURCE_CLASS_FIELD = "sourceClassField"; //$NON-NLS-1$
	private static final String PROPERTY_SOURCE_CLASS_FIELD_TYPE = "sourceClassFieldType"; //$NON-NLS-1$
	private static final String PROPERTY_SOURCE_TABLE_FIELD = "sourceTableField"; //$NON-NLS-1$
	private static final String PROPERTY_TARGET_TABLE_FIELD = "targetTableField"; //$NON-NLS-1$
	private static final String PROPERTY_SOURCE_TABLE_FIELD_TYPE = "sourceTableFieldType"; //$NON-NLS-1$
	private static final String PROPERTY_TARGET_TABLE_FIELD_TYPE = "targetTableFieldType"; //$NON-NLS-1$
	
	static {
		descriptors_connection_class_mapping = new IPropertyDescriptor[] { 
			new TextPropertyDescriptor(PROPERTY_SOURCE, PROPERTY_SOURCE),
			new TextPropertyDescriptor(PROPERTY_TARGET, PROPERTY_TARGET),
			new TextPropertyDescriptor(PROPERTY_TYPE, PROPERTY_TYPE),
			new TextPropertyDescriptor(PROPERTY_CLASS_NAME, PROPERTY_CLASS_NAME),
			new TextPropertyDescriptor(PROPERTY_TABLE_NAME, PROPERTY_TABLE_NAME),
		};
		descriptors_connection_property_mapping = new IPropertyDescriptor[] { 
			new TextPropertyDescriptor(PROPERTY_SOURCE, PROPERTY_SOURCE),
			new TextPropertyDescriptor(PROPERTY_TARGET, PROPERTY_TARGET),
			new TextPropertyDescriptor(PROPERTY_TYPE, PROPERTY_TYPE),
			new TextPropertyDescriptor(PROPERTY_CLASS_NAME, PROPERTY_CLASS_NAME),
			new TextPropertyDescriptor(PROPERTY_TABLE_NAME, PROPERTY_TABLE_NAME),
			new TextPropertyDescriptor(PROPERTY_CLASS_FIELD, PROPERTY_CLASS_FIELD),
			new TextPropertyDescriptor(PROPERTY_TABLE_FIELD, PROPERTY_TABLE_FIELD),
			new TextPropertyDescriptor(PROPERTY_CLASS_FIELD_TYPE, PROPERTY_CLASS_FIELD_TYPE),
			new TextPropertyDescriptor(PROPERTY_TABLE_FIELD_TYPE, PROPERTY_TABLE_FIELD_TYPE),
		};
		descriptors_connection_association = new IPropertyDescriptor[] { 
			new TextPropertyDescriptor(PROPERTY_SOURCE, PROPERTY_SOURCE),
			new TextPropertyDescriptor(PROPERTY_TARGET, PROPERTY_TARGET),
			new TextPropertyDescriptor(PROPERTY_TYPE, PROPERTY_TYPE),
			new TextPropertyDescriptor(PROPERTY_ASSOCIATION_TYPE, PROPERTY_ASSOCIATION_TYPE),
			new TextPropertyDescriptor(PROPERTY_SOURCE_CLASS_FIELD, PROPERTY_SOURCE_CLASS_FIELD),
			new TextPropertyDescriptor(PROPERTY_SOURCE_CLASS_FIELD_TYPE, PROPERTY_SOURCE_CLASS_FIELD_TYPE),
		};
		descriptors_connection_foreign_key_constraint = new IPropertyDescriptor[] { 
			new TextPropertyDescriptor(PROPERTY_SOURCE, PROPERTY_SOURCE),
			new TextPropertyDescriptor(PROPERTY_TARGET, PROPERTY_TARGET),
			new TextPropertyDescriptor(PROPERTY_TYPE, PROPERTY_TYPE),
			new TextPropertyDescriptor(PROPERTY_SOURCE_TABLE_FIELD, PROPERTY_SOURCE_TABLE_FIELD),
			new TextPropertyDescriptor(PROPERTY_TARGET_TABLE_FIELD, PROPERTY_TARGET_TABLE_FIELD),
			new TextPropertyDescriptor(PROPERTY_SOURCE_TABLE_FIELD_TYPE, PROPERTY_SOURCE_TABLE_FIELD_TYPE),
			new TextPropertyDescriptor(PROPERTY_TARGET_TABLE_FIELD_TYPE, PROPERTY_TARGET_TABLE_FIELD_TYPE),
		};
	}

	protected Shape source;
	protected Shape target;
	
	/**
	 * supported connection types 
	 */
	public enum ConnectionType {
		ClassMapping,
		PropertyMapping,
		Association,
		ForeignKeyConstraint,
	};

	/**
	 * flag to prevent cycle call of updateVisibleValue()
	 */
	protected boolean inUpdateVisibleValue = false;
		
	public Connection(Shape s, Shape newTarget) {
		if (s == null || newTarget == null || s == newTarget) {
			throw new IllegalArgumentException();
		}
		this.source = s;
		this.target = newTarget;
		source.addConnection(this);
		target.addConnection(this);
	}			
	
	public Shape getSource() {
		return source;
	}
	
	public Shape getTarget() {
		return target;
	}
	
	/**
	 * Detect connection type from connected source and target.
	 * 
	 * @return
	 */
	public ConnectionType getConnectionType() {
		if (source instanceof OrmShape && target instanceof OrmShape) {
			if ((source.getOrmElement() instanceof TableStub) && (target.getOrmElement() instanceof TableStub)) {
				return ConnectionType.ForeignKeyConstraint;
			}
			boolean bClassMapping = true;
			if (!(source.getOrmElement() instanceof RootClassStub || source.getOrmElement() instanceof TableStub)) {
				bClassMapping = false;
			}
			if (!(target.getOrmElement() instanceof RootClassStub || target.getOrmElement() instanceof TableStub)) {
				bClassMapping = false;
			}
			if (bClassMapping) {
				return ConnectionType.ClassMapping;
			}
		}
		if ((source.getOrmElement() instanceof TableStub && target.getOrmElement() instanceof TableStub) ||
			(source.getOrmElement() instanceof TableStub && target.getOrmElement() instanceof ColumnStub) ||
			(source.getOrmElement() instanceof ColumnStub && target.getOrmElement() instanceof TableStub) ||
			(source.getOrmElement() instanceof ColumnStub && target.getOrmElement() instanceof ColumnStub)) {
			return ConnectionType.ForeignKeyConstraint;
		}
		if (((source instanceof OrmShape) ^ (target instanceof OrmShape))) {
			boolean bAssociation = true;
			if (!(!(source instanceof OrmShape) && source.getOrmElement() instanceof PropertyStub) &&
				!(!(target instanceof OrmShape) && target.getOrmElement() instanceof PropertyStub)) {
				bAssociation = false;
			}
			if (bAssociation) {
				return ConnectionType.Association;
			}
		}
		return ConnectionType.PropertyMapping;
	}

	/**
	 * It has no children, so not possible to add.
	 */
	public boolean addChild(Shape item) {
		return false;
	}
	
	@Override
	public void setSelected(boolean selected) {
		source.setSelected(selected);
		target.setSelected(selected);
		super.setSelected(selected);
	}
	
	@Override
	public void updateVisibleValue(boolean initState) {
		if (inUpdateVisibleValue) {
			return;
		}
		inUpdateVisibleValue = true;
		boolean visible = initState;
		visible = visible && source.isVisible();
		visible = visible && target.isVisible();
		setVisible(visible);
		super.updateVisibleValue(this.visible);
		inUpdateVisibleValue = false;
	}

	/**
	 * It has no parent
	 */
	@Override
	public BaseElement getParent() {
		return null;
	}
	
	@Override
	public void refresh() {
		updateVisibleValue(isVisible());
		super.refresh();
	}

	@Override
	public String getKey() {
		return null;
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ConnectionType connectType = getConnectionType();
		if (connectType == ConnectionType.ClassMapping) {
			return descriptors_connection_class_mapping;
		} else if (connectType == ConnectionType.PropertyMapping) {
			return descriptors_connection_property_mapping;
		} else if (connectType == ConnectionType.Association) {
			return descriptors_connection_association;
		} else if (connectType == ConnectionType.ForeignKeyConstraint) {
			return descriptors_connection_foreign_key_constraint;
		}
		return super.getPropertyDescriptors();
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		Object res = null;
		ConnectionType connectType = getConnectionType();
		if (PROPERTY_SOURCE.equals(propertyId)) {
			res = source;
		} else if (PROPERTY_TARGET.equals(propertyId)) {
			res = target;
		} else if (PROPERTY_TYPE.equals(propertyId)) {
			if (connectType == ConnectionType.ClassMapping) {
				res = "ClassMapping"; //$NON-NLS-1$
			} else if (connectType == ConnectionType.PropertyMapping) {
				res = "PropertyMapping"; //$NON-NLS-1$
			} else if (connectType == ConnectionType.Association) {
				res = "Association"; //$NON-NLS-1$
			} else if (connectType == ConnectionType.ForeignKeyConstraint) {
				res = "ForeignKeyConstraint"; //$NON-NLS-1$
			}
		} else if (PROPERTY_CLASS_NAME.equals(propertyId)) {
			if (connectType == ConnectionType.ClassMapping) {
				if (source.getOrmElement() instanceof RootClassStub) {
					res = ((RootClassStub)(source.getOrmElement())).getClassName();
				} else if (target.getOrmElement() instanceof RootClassStub) {
					res = ((RootClassStub)(target.getOrmElement())).getClassName();
				}
			} else if (connectType == ConnectionType.PropertyMapping) {
				if (((Shape)source.getParent()).getOrmElement() instanceof RootClassStub) {
					res = ((RootClassStub)(((Shape)source.getParent()).getOrmElement())).getClassName();
				} else if (((Shape)target.getParent()).getOrmElement() instanceof RootClassStub) {
					res = ((RootClassStub)(((Shape)target.getParent()).getOrmElement())).getClassName();
				}
			}
		} else if (PROPERTY_TABLE_NAME.equals(propertyId)) {
			if (connectType == ConnectionType.ClassMapping) {
				if (source.getOrmElement() instanceof TableStub) {
					res = ((TableStub)(source.getOrmElement())).getName();
				} else if (target.getOrmElement() instanceof TableStub) {
					res = ((TableStub)(target.getOrmElement())).getName();
				}
			} else if (connectType == ConnectionType.PropertyMapping) {
				if (((Shape)source.getParent()).getOrmElement() instanceof TableStub) {
					res = ((TableStub)(((Shape)source.getParent()).getOrmElement())).getName();
				} else if (((Shape)target.getParent()).getOrmElement() instanceof TableStub) {
					res = ((TableStub)(((Shape)target.getParent()).getOrmElement())).getName();
				}
			}
		} else if (PROPERTY_CLASS_FIELD.equals(propertyId)) {
			if (source.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(source.getOrmElement())).getName();
			} else if (target.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(target.getOrmElement())).getName();
			}
		} else if (PROPERTY_TABLE_FIELD.equals(propertyId)) {
			if (source.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(source.getOrmElement())).getName();
			} else if (target.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(target.getOrmElement())).getName();
			}
		} else if (PROPERTY_CLASS_FIELD_TYPE.equals(propertyId)) {
			if (source.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(source.getOrmElement())).getType().toString();
			} else if (target.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(target.getOrmElement())).getType().toString();
			}
		} else if (PROPERTY_TABLE_FIELD_TYPE.equals(propertyId)) {
			if (source.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(source.getOrmElement())).getSqlType();
			} else if (target.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(target.getOrmElement())).getSqlType();
			}
		} else if (PROPERTY_ASSOCIATION_TYPE.equals(propertyId)) {
			if (source.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(source.getOrmElement())).getValue().toString();
			} else if (target.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(target.getOrmElement())).getValue().toString();
			}
			if (res instanceof OneToOneStub) {
				res = "OneToOne"; //$NON-NLS-1$
			} else if (res instanceof OneToManyStub) {
				res = "OneToMany"; //$NON-NLS-1$
			} else if (res instanceof ManyToOneStub) {
				res = "ManyToOne"; //$NON-NLS-1$
			}
		} else if (PROPERTY_SOURCE_CLASS_FIELD.equals(propertyId)) {
			if (source.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(source.getOrmElement())).getName();
			}
		} else if (PROPERTY_SOURCE_CLASS_FIELD_TYPE.equals(propertyId)) {
			if (source.getOrmElement() instanceof PropertyStub) {
				res = ((PropertyStub)(source.getOrmElement())).getType().toString();
			}
		} else if (PROPERTY_SOURCE_TABLE_FIELD.equals(propertyId)) {
			if (source.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(source.getOrmElement())).getName();
			}
		} else if (PROPERTY_TARGET_TABLE_FIELD.equals(propertyId)) {
			if (target.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(target.getOrmElement())).getName();
			}
		} else if (PROPERTY_SOURCE_TABLE_FIELD_TYPE.equals(propertyId)) {
			if (source.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(source.getOrmElement())).getSqlType();
			}
		} else if (PROPERTY_TARGET_TABLE_FIELD_TYPE.equals(propertyId)) {
			if (target.getOrmElement() instanceof ColumnStub) {
				res = ((ColumnStub)(target.getOrmElement())).getSqlType();
			}
		}
		if (res == null) {
			res = super.getPropertyValue(propertyId);
		}
		return toEmptyStr(res);
	}
}