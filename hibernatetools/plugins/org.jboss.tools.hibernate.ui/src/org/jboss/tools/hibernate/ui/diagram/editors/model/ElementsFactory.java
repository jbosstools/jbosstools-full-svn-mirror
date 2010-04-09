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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.mediator.x.cfg.Configuration;
import org.hibernate.mediator.x.mapping.CollectionStub;
import org.hibernate.mediator.x.mapping.Column;
import org.hibernate.mediator.x.mapping.Component;
import org.hibernate.mediator.x.mapping.DependantValue;
import org.hibernate.mediator.x.mapping.ForeignKey;
import org.hibernate.mediator.x.mapping.Join;
import org.hibernate.mediator.x.mapping.OneToMany;
import org.hibernate.mediator.x.mapping.PersistentClass;
import org.hibernate.mediator.x.mapping.Property;
import org.hibernate.mediator.x.mapping.RootClass;
import org.hibernate.mediator.x.mapping.SimpleValue;
import org.hibernate.mediator.x.mapping.Subclass;
import org.hibernate.mediator.x.mapping.TableStub;
import org.hibernate.mediator.x.mapping.Value;
import org.hibernate.mediator.x.type.EntityType;
import org.hibernate.mediator.x.type.Type;

/**
 * Responsible to create diagram elements for given
 * Hibernate Console Configuration.
 * 
 * @author Vitali Yemialyanchyk
 */
public class ElementsFactory {

	private final Configuration config;
	private final HashMap<String, OrmShape> elements;
	private final ArrayList<Connection> connections;
	
	public ElementsFactory(Configuration config, HashMap<String, OrmShape> elements,
			ArrayList<Connection> connections) {
		this.config = config;
		this.elements = elements;
		this.connections = connections;
	}
	
	@SuppressWarnings("unchecked")
	public void createForeingKeyConnections() {
		// do clone cause elements could be changed during iteration!
		HashMap<String, OrmShape> elementsTmp = (HashMap<String, OrmShape>)elements.clone();
		Iterator<OrmShape> it = elementsTmp.values().iterator();
		while (it.hasNext()) {
			final OrmShape shape = it.next();
			Object ormElement = shape.getOrmElement();
			if (ormElement instanceof TableStub) {
				TableStub databaseTable = (TableStub)ormElement;
				Iterator<ForeignKey> itFK = (Iterator<ForeignKey>)databaseTable.getForeignKeyIterator();
				while (itFK.hasNext()) {
					final ForeignKey fk = itFK.next();
					TableStub referencedTable = fk.getReferencedTable();
					final OrmShape referencedShape = getOrCreateDatabaseTable(referencedTable);
					//
					Iterator<Column> itColumns = (Iterator<Column>)fk.columnIterator();
					while (itColumns.hasNext()) {
						Column col = itColumns.next();
						Shape shapeColumn = shape.getChild(col);
						Iterator<Column> itReferencedColumns = null;
						if (fk.isReferenceToPrimaryKey()) {
							itReferencedColumns = 
								(Iterator<Column>)referencedTable.getPrimaryKey().columnIterator();
						} else {
							itReferencedColumns = 
								(Iterator<Column>)fk.getReferencedColumns().iterator();
						}
						while (itReferencedColumns != null && itReferencedColumns.hasNext()) {
							Column colReferenced = itReferencedColumns.next();
							Shape shapeReferencedColumn = referencedShape.getChild(colReferenced);
							if (shouldCreateConnection(shapeColumn, shapeReferencedColumn)) {
								connections.add(new Connection(shapeColumn, shapeReferencedColumn));
							}
						}
					}
				}
			}
		}
	}
	
	public void createChildren(BaseElement element) {
		if (element.getClass().equals(ExpandableShape.class)) {
			processExpand((ExpandableShape)element);
		} else if (element.getClass().equals(ComponentShape.class)) {
			refreshComponentReferences((ComponentShape)element);
		}
		Iterator<Shape> it = element.getChildrenList().iterator();
		while (it.hasNext()) {
			final Shape shape = it.next();
			createChildren(shape);
		}
	}
	
	protected void processExpand(ExpandableShape shape) {
		Object element = shape.getOrmElement();
		if (!(element instanceof Property)) {
			return;
		}
		OrmShape s = null;
		Property property = (Property)element;
		if (!property.isComposite()) {
			Type type = ((Property)element).getType();
			if (type.isEntityType()) {
				EntityType et = (EntityType) type;
				Object clazz = config != null ? 
						config.getClassMapping(et.getAssociatedEntityName()) : null;
				if (clazz instanceof RootClass) {
					RootClass rootClass = (RootClass)clazz;
					s = getOrCreatePersistentClass(rootClass, null);
					if (shouldCreateConnection(shape, s)) {
						connections.add(new Connection(shape, s));
					}
				} else if (clazz instanceof Subclass) {
					s = getOrCreatePersistentClass(((Subclass)clazz).getRootClass(), null);
				}
			}
		} else {
			s = getOrCreatePersistentClass(new SpecialRootClass(property), null);
			if (shouldCreateConnection(shape, s)) {
				connections.add(new Connection(shape, s));
			}
			createConnections(s, getOrCreateDatabaseTable(property.getValue().getTable()));
		}
	}

	@SuppressWarnings("unchecked")
	protected void refreshComponentReferences(ComponentShape componentShape) {
		Property property = (Property)componentShape.getOrmElement();
		if (!(property.getValue() instanceof CollectionStub)) {
			return;
		}
		CollectionStub collection = (CollectionStub)property.getValue();
		Value component = collection.getElement();
		Shape csChild0 = null, csChild1 = null;
		Iterator<Shape> tmp = componentShape.getChildrenIterator();
		if (tmp.hasNext()) {
			csChild0 = tmp.next();
		}
		if (tmp.hasNext()) {
			csChild1 = tmp.next();
		}
		OrmShape childShape = null;
		if (component instanceof Component) {
			childShape = elements.get(((Component)component).getComponentClassName());
			if (childShape == null) {
				childShape = getOrCreateComponentClass(property);
			}
			SimpleValue value = (SimpleValue)csChild0.getOrmElement();
			OrmShape tableShape = getOrCreateDatabaseTable(value.getTable());
			if (tableShape != null) {
				Iterator it = value.getColumnIterator();
				while (it.hasNext()) {
					Object el = it.next();
					if (el instanceof Column) {
						Column col = (Column)el;
						Shape shape = tableShape.getChild(col);
						if (shouldCreateConnection(csChild0, shape)) {
							connections.add(new Connection(csChild0, shape));
						}
					}
				}
			}
			if (shouldCreateConnection(csChild1, childShape)) {
				connections.add(new Connection(csChild1, childShape));
			}
			
		} else if (collection.isOneToMany()) {
			childShape = getOrCreateAssociationClass(property);
			if (childShape != null) {
				if (shouldCreateConnection(csChild1, childShape)) {
					connections.add(new Connection(csChild1, childShape));
				}
				OrmShape keyTableShape = getOrCreateDatabaseTable(collection.getKey().getTable());
				Iterator it = collection.getKey().getColumnIterator();
				while (it.hasNext()) {
					Object el = it.next();
					if (el instanceof Column) {
						Column col = (Column)el;
						Shape shape = keyTableShape.getChild(col);
						if (shouldCreateConnection(csChild0, shape)) {
							connections.add(new Connection(csChild0, shape));
						}
					}
				}
			}

		} else {
			// this is case: if (collection.isMap() || collection.isSet())
			childShape = getOrCreateDatabaseTable(collection.getCollectionTable());
			if (childShape != null) {
				Iterator it = ((DependantValue)csChild0.getOrmElement()).getColumnIterator();
				while (it.hasNext()) {
					Object el = it.next();
					if (el instanceof Column) {
						Column col = (Column)el;
						Shape shape = childShape.getChild(col);
						if (shouldCreateConnection(csChild0, shape)) {
							connections.add(new Connection(csChild0, shape));
						}
					}
				}
				it = ((SimpleValue)csChild1.getOrmElement()).getColumnIterator();
				while (it.hasNext()) {
					Object el = it.next();
					if (el instanceof Column) {
						Column col = (Column)el;
						Shape shape = childShape.getChild(col);
						if (shouldCreateConnection(csChild1, shape)) {
							connections.add(new Connection(csChild1, shape));
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected OrmShape getOrCreateDatabaseTable(TableStub databaseTable) {
		OrmShape tableShape = null;
		if (databaseTable != null) {
			tableShape = getShape(databaseTable);
			if (tableShape == null) {
				tableShape = createShape(databaseTable);
				if (config != null) {
					Iterator iterator = config.getClassMappings();
					while (iterator.hasNext()) {
						Object clazz = iterator.next();
						if (clazz instanceof RootClass) {
							RootClass cls = (RootClass)clazz;
							if (databaseTable.equals(cls.getTable())) {
								// create persistent class shape only for RootClass,
								// which has same table reference
								getOrCreatePersistentClass(cls, null);
							}
						}
					}
				}
			}			
		}
		return tableShape;
	}

	protected OrmShape getOrCreatePersistentClass(PersistentClass persistentClass, 
			TableStub componentClassDatabaseTable) {
		OrmShape classShape = null;
		if (persistentClass == null) {
			return classShape;
		}
		OrmShape shape = null;
		classShape = getShape(persistentClass.getEntityName());
		if (classShape == null) {
			classShape = createShape(persistentClass);
		}
		if (componentClassDatabaseTable == null && persistentClass.getTable() != null) {
			componentClassDatabaseTable = persistentClass.getTable();
		}
		if (componentClassDatabaseTable != null) {
			shape = getShape(componentClassDatabaseTable);
			if (shape == null) {
				shape = getOrCreateDatabaseTable(componentClassDatabaseTable);
			}
			createConnections(classShape, shape);
			if (shouldCreateConnection(classShape, shape)) {
				connections.add(new Connection(classShape, shape));
			}
		}
		RootClass rc = (RootClass)persistentClass;
		Iterator<Subclass> iter = rc.getSubclassIterator();
		while (iter.hasNext()) {
			Object element = iter.next();
			if (element instanceof Subclass) {
				Subclass subclass = (Subclass)element;
				OrmShape subclassShape = getShape(subclass);
				if (subclassShape == null) {
					subclassShape = createShape(subclass);
				}
				if (((Subclass)element).isJoinedSubclass()) {
					TableStub jcTable = ((Subclass)element).getTable();
					OrmShape jcTableShape = getOrCreateDatabaseTable(jcTable);
					createConnections(subclassShape, jcTableShape);
					if (shouldCreateConnection(subclassShape, jcTableShape)) {
						connections.add(new Connection(subclassShape, jcTableShape));
					}
				} else {
					createConnections(subclassShape, shape);
					if (shouldCreateConnection(subclassShape, shape)) {
						connections.add(new Connection(subclassShape, shape));
					}
				}
				OrmShape ownerTableShape = getOrCreateDatabaseTable(((Subclass)element).getRootTable());
				createConnections(subclassShape, ownerTableShape);

				Iterator<Join> joinIterator = subclass.getJoinIterator();
				while (joinIterator.hasNext()) {
					Join join = joinIterator.next();
					Iterator<Property> iterator = join.getPropertyIterator();
					while (iterator.hasNext()) {
						Property property = iterator.next();
						OrmShape tableShape =  getOrCreateDatabaseTable(property.getValue().getTable());
						createConnections(subclassShape, tableShape);
					}
				}
			}
		}

		if (persistentClass.getIdentifier() instanceof Component) {
			Component identifier = (Component)persistentClass.getIdentifier();
			if (identifier.getComponentClassName() != null && !identifier.getComponentClassName().equals(identifier.getOwner().getEntityName())) {
				OrmShape componentClassShape = elements.get(identifier.getComponentClassName());
				if (componentClassShape == null && persistentClass instanceof RootClass) {
					componentClassShape = getOrCreateComponentClass(((RootClass)persistentClass).getIdentifierProperty());

					Shape idPropertyShape = classShape.getChild(persistentClass.getIdentifierProperty());
					if (shouldCreateConnection(idPropertyShape, componentClassShape)) {
						connections.add(new Connection(idPropertyShape, componentClassShape));
					}

					OrmShape tableShape = getOrCreateDatabaseTable(identifier.getTable());
					if (componentClassShape != null) {
						createConnections(componentClassShape, tableShape);
					}
				}
			}
		}

		Iterator<Join> joinIterator = persistentClass.getJoinIterator();
		while (joinIterator.hasNext()) {
			Join join = joinIterator.next();
			Iterator<Property> iterator = join.getPropertyIterator();
			while (iterator.hasNext()) {
				Property property = iterator.next();
				OrmShape tableShape = getOrCreateDatabaseTable(property.getValue().getTable());
				createConnections(classShape, tableShape);
			}
		}
		return classShape;
	}

	protected OrmShape getOrCreateComponentClass(Property property) {
		OrmShape classShape = null;
		if (property == null) {
			return classShape;
		}
		if (property.getValue() instanceof CollectionStub) {
			Component component = (Component)((CollectionStub)property.getValue()).getElement();
			if (component != null) {
				classShape = createShape(property);
				OrmShape tableShape = elements.get(Utils.getTableName(component.getTable()));
				if (tableShape == null) {
					tableShape = getOrCreateDatabaseTable(component.getTable());
				}
				createConnections(classShape, tableShape);
				if (shouldCreateConnection(classShape, tableShape)) {
					connections.add(new Connection(classShape, tableShape));
				}
				Shape parentShape = ((SpecialOrmShape)classShape).getParentShape();
				if (parentShape != null) {
					OrmShape parentClassShape = elements.get(
							Utils.getName(((Property)parentShape.getOrmElement()).getPersistentClass().getEntityName()));
					if (shouldCreateConnection(parentShape, parentClassShape)) {
						connections.add(new Connection(parentShape, parentClassShape));
					}
				}
			}
		} else if (property.getValue() instanceof Component) {
			classShape = elements.get(((Component)property.getValue()).getComponentClassName());
			if (classShape == null) {
				classShape = createShape(property);
			}
		}
		return classShape;
	}

	protected OrmShape getOrCreateAssociationClass(Property property) {
		OrmShape classShape = null;
		OneToMany component = (OneToMany)(((CollectionStub)(property.getValue())).getElement());
		if (component == null) {
			return classShape;
		}
		if (component.getAssociatedClass() instanceof RootClass) {
			classShape = getOrCreatePersistentClass(component.getAssociatedClass(), null);
			if (classShape == null) {
				classShape = createShape(component.getAssociatedClass());
			}
			OrmShape tableShape = elements.get(Utils.getTableName(component.getAssociatedClass().getTable()));
			if (tableShape == null) {
				tableShape = getOrCreateDatabaseTable(component.getAssociatedClass().getTable());
			}
			createConnections(classShape, tableShape);
			if (shouldCreateConnection(classShape, tableShape)) {
				connections.add(new Connection(classShape, tableShape));
			}
		}
		return classShape;
	}
	
	protected OrmShape createShape(Object ormElement) {
		OrmShape ormShape = null;
		if (ormElement instanceof Property) {
			SpecialRootClass specialRootClass = new SpecialRootClass((Property)ormElement);
			String key = Utils.getName(specialRootClass.getEntityName());
			ormShape = elements.get(key);
			if (null == ormShape) {
				ormShape = new SpecialOrmShape(specialRootClass);
				elements.put(key, ormShape);
			}
		} else {
			String key = Utils.getName(ormElement);
			ormShape = elements.get(key);
			if (null == ormShape) {
				ormShape = new OrmShape(ormElement);
				elements.put(key, ormShape);
			}
		}
		return ormShape;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createConnections(ExpandableShape persistentClass, ExpandableShape dbTable) {
		boolean res = false;
		if (persistentClass == null || dbTable == null) {
			return res;
		}
		Property parentProperty = null;
		if (persistentClass.getOrmElement() instanceof SpecialRootClass) {
			parentProperty = ((SpecialRootClass)persistentClass.getOrmElement()).getParentProperty();
		}
		Iterator<Shape> itFields = persistentClass.getChildrenIterator();
		Set<Shape> processed = new HashSet<Shape>();
		while (itFields.hasNext()) {
			final Shape shape = itFields.next();
			Object element = shape.getOrmElement();
			if (!(element instanceof Property && parentProperty != element)) {
				continue;
			}
			Value value = ((Property)element).getValue();
			Iterator iterator = value.getColumnIterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (!(o instanceof Column)) {
					continue;
				}
				Column dbColumn = (Column)o;
				Iterator<Shape> itColumns = dbTable.getChildrenIterator();
				while (itColumns.hasNext()) {
					final Shape shapeCol = itColumns.next();
					if (processed.contains(shapeCol)) {
						continue;
					}
					if (shape.equals(shapeCol)) {
						continue;
					}
					Object ormElement = shapeCol.getOrmElement();
					String name2 = ""; //$NON-NLS-1$
					if (ormElement instanceof Column) {
						Column dbColumn2 = (Column)ormElement;
						name2 = dbColumn2.getName();
					} else if (ormElement instanceof Property) {
						Property property2 = (Property)ormElement;
						name2 = property2.getName();
					}
					if (dbColumn.getName().equals(name2)) {
						if (shouldCreateConnection(shape, shapeCol)) {
							connections.add(new Connection(shape, shapeCol));
							res = true;
						}
						processed.add(shapeCol);
					}						
				}
			}
		}
		return res;
	}
	

	public OrmShape getShape(Object ormElement) {
		OrmShape ormShape = null;
		if (ormElement instanceof Property) {
			SpecialRootClass specialRootClass = new SpecialRootClass((Property)ormElement);
			ormShape = elements.get(Utils.getName(specialRootClass.getEntityName()));
		} else {
			ormShape = elements.get(Utils.getName(ormElement));
		}
		return ormShape;
	}
	
	
	private boolean isConnectionExist(Shape source, Shape target) {
		return Utils.isConnectionExist(source, target);
	}
	
	private boolean shouldCreateConnection(Shape source, Shape target) {
		if (source == null || target == null || source == target) {
			return false;
		}
		if (isConnectionExist(source, target)) {
			return false;
		}
		return true;
	}
}
