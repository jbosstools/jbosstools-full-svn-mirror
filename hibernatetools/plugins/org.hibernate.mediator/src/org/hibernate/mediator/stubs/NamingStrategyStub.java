package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class NamingStrategyStub extends HObject {
	public static final String CL = "org.hibernate.cfg.NamingStrategy"; //$NON-NLS-1$

	protected NamingStrategyStub(Object namingStrategy) {
		super(namingStrategy, CL);;
	}

	public String classToTableName(String className) {
		return (String)invoke("classToTableName", className); //$NON-NLS-1$
	}

	public String tableName(String tableName) {
		return (String)invoke("tableName", tableName); //$NON-NLS-1$
	}

	public String columnName(String columnName) {
		return (String)invoke("columnName", columnName); //$NON-NLS-1$
	}

	public String propertyToColumnName(String propertyName) {
		return (String)invoke("propertyToColumnName", propertyName); //$NON-NLS-1$
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return (String)invoke("joinKeyColumnName", joinedColumn, joinedTable); //$NON-NLS-1$
	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable, 
			String associatedEntity, String associatedEntityTable,
			String propertyName) {
		return (String)invoke("collectionTableName", ownerEntity, ownerEntityTable, //$NON-NLS-1$ 
				associatedEntity, associatedEntityTable, propertyName);
	}

	public String logicalColumnName(String columnName, String propertyName) {
		return (String)invoke("logicalColumnName", columnName, propertyName); //$NON-NLS-1$
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName, 
			String propertyTableName, String referencedColumnName) {
		return (String)invoke("foreignKeyColumnName", propertyName, //$NON-NLS-1$ 
				propertyEntityName, propertyTableName, referencedColumnName);
	}
}
