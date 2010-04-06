package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class NamingStrategyStub extends HObject {
	public static final String CL = "org.hibernate.cfg.NamingStrategy"; //$NON-NLS-1$

	protected NamingStrategyStub(Object namingStrategy) {
		super(namingStrategy, CL);
	}

	public String classToTableName(String className) {
		return (String)invoke(mn(), className);
	}

	public String tableName(String tableName) {
		return (String)invoke(mn(), tableName);
	}

	public String columnName(String columnName) {
		return (String)invoke(mn(), columnName);
	}

	public String propertyToColumnName(String propertyName) {
		return (String)invoke(mn(), propertyName);
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return (String)invoke(mn(), joinedColumn, joinedTable);
	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable, 
			String associatedEntity, String associatedEntityTable,
			String propertyName) {
		return (String)invoke(mn(), ownerEntity, ownerEntityTable, 
				associatedEntity, associatedEntityTable, propertyName);
	}

	public String logicalColumnName(String columnName, String propertyName) {
		return (String)invoke(mn(), columnName, propertyName);
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName, 
			String propertyTableName, String referencedColumnName) {
		return (String)invoke(mn(), propertyName, 
				propertyEntityName, propertyTableName, referencedColumnName);
	}
}
