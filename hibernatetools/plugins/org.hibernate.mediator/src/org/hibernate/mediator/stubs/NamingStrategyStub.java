package org.hibernate.mediator.stubs;

import org.hibernate.cfg.NamingStrategy;
import org.hibernate.mediator.Messages;

public class NamingStrategyStub {
	protected NamingStrategy namingStrategy;

	protected NamingStrategyStub(Object namingStrategy) {
		if (namingStrategy == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.namingStrategy = (NamingStrategy)namingStrategy;
	}
	
	@SuppressWarnings("nls")
	public static String getClassName() {
		return "org.hibernate.cfg.NamingStrategy";
	}

	public String classToTableName(String className) {
		return namingStrategy.classToTableName(className);
	}

	public String tableName(String tableName) {
		return namingStrategy.tableName(tableName);
	}

	public String columnName(String columnName) {
		return namingStrategy.columnName(columnName);
	}

	public String propertyToColumnName(String propertyName) {
		return namingStrategy.propertyToColumnName(propertyName);
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return namingStrategy.joinKeyColumnName(joinedColumn, joinedTable);
	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable, 
			String associatedEntity, String associatedEntityTable,
			String propertyName) {
		return namingStrategy.collectionTableName(ownerEntity, ownerEntityTable, 
				associatedEntity, associatedEntityTable, propertyName);
	}

	public String logicalColumnName(String columnName, String propertyName) {
		return namingStrategy.logicalColumnName(columnName, propertyName);
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName, 
			String propertyTableName, String referencedColumnName) {
		return namingStrategy.foreignKeyColumnName(propertyName, 
				propertyEntityName, propertyTableName, referencedColumnName);
	}
}
