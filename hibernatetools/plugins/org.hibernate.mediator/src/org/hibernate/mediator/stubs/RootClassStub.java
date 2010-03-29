package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.Subclass;

public class RootClassStub extends PersistentClassStub {
	protected RootClass rootClass;

	protected RootClassStub(Object rootClass) {
		super(rootClass);
		this.rootClass = (RootClass)rootClass;
	}

	public static RootClassStub newInstance() {
		return new RootClassStub(new RootClass());
	}

	public String getClassName() {
		return rootClass.getClassName();
	}

	public void setDiscriminator(ValueStub discr) {
		rootClass.setDiscriminator(discr.value);
	}

	public TableStub getTable() {
		return new TableStub(rootClass.getTable());
	}

	public void addProperty(PropertyStub p) {
		rootClass.addProperty(p.property);
	}

	public void setIdentifierProperty(PropertyStub p) {
		rootClass.setIdentifierProperty(p.property);
	}

	public PropertyStub getIdentifierProperty() {
		return new PropertyStub(rootClass.getIdentifierProperty());
	}

	public void setEntityName(String entityName) {
		rootClass.setEntityName(entityName);
	}

	public void setClassName(String className) {
		rootClass.setClassName(className);
	}

	public void setProxyInterfaceName(String proxyInterfaceName) {
		rootClass.setProxyInterfaceName(proxyInterfaceName);
	}

	public void setLazy(boolean lazy) {
		rootClass.setLazy(lazy);
	}

	public void setTable(TableStub table) {
		rootClass.setTable(table.table);
	}

	public void setAbstract(Boolean isAbstract) {
		rootClass.setAbstract(isAbstract);
	}

	public String getNodeName() {
		return rootClass.getNodeName();
	}

	public KeyValueStub getIdentifier() {
		return (KeyValueStub)ValueStubFactory.createValueStub(rootClass.getIdentifier());
	}

	public boolean isCustomDeleteCallable() {
		return rootClass.isCustomDeleteCallable();
	}

	public boolean isCustomInsertCallable() {
		return rootClass.isCustomInsertCallable();
	}

	public boolean isCustomUpdateCallable() {
		return rootClass.isCustomUpdateCallable();
	}

	public boolean isDiscriminatorInsertable() {
		return rootClass.isDiscriminatorInsertable();
	}

	public boolean isDiscriminatorValueNotNull() {
		return rootClass.isDiscriminatorValueNotNull();
	}

	public boolean isDiscriminatorValueNull() {
		return rootClass.isDiscriminatorValueNull();
	}

	public boolean isExplicitPolymorphism() {
		return rootClass.isExplicitPolymorphism();
	}

	public boolean isForceDiscriminator() {
		return rootClass.isForceDiscriminator();
	}

	public boolean isInherited() {
		return rootClass.isInherited();
	}

	public boolean isJoinedSubclass() {
		return rootClass.isJoinedSubclass();
	}

	public boolean isLazy() {
		return rootClass.isLazy();
	}

	public boolean isLazyPropertiesCacheable() {
		return rootClass.isLazyPropertiesCacheable();
	}

	public boolean isMutable() {
		return rootClass.isMutable();
	}

	public boolean isPolymorphic() {
		return rootClass.isPolymorphic();
	}

	public boolean isVersioned() {
		return rootClass.isVersioned();
	}

	public int getBatchSize() {
		return rootClass.getBatchSize();
	}

	public String getCacheConcurrencyStrategy() {
		return rootClass.getCacheConcurrencyStrategy();
	}

	public String getCustomSQLDelete() {
		return rootClass.getCustomSQLDelete();
	}

	public String getCustomSQLInsert() {
		return rootClass.getCustomSQLInsert();
	}

	public String getCustomSQLUpdate() {
		return rootClass.getCustomSQLUpdate();
	}

	public String getDiscriminatorValue() {
		return rootClass.getDiscriminatorValue();
	}

	public String getLoaderName() {
		return rootClass.getLoaderName();
	}

	public int getOptimisticLockMode() {
		return rootClass.getOptimisticLockMode();
	}

	public String getTemporaryIdTableDDL() {
		return rootClass.getTemporaryIdTableDDL();
	}

	public String getTemporaryIdTableName() {
		return rootClass.getTemporaryIdTableName();
	}

	public String getWhere() {
		return rootClass.getWhere();
	}

	@SuppressWarnings("unchecked")
	public Iterator<SubclassStub> getSubclassIterator() {
		Iterator<Subclass> it = (Iterator<Subclass>)rootClass.getSubclassIterator();
		ArrayList<SubclassStub> al = new ArrayList<SubclassStub>();
		while (it.hasNext()) {
			al.add(new SubclassStub(it.next()));
		}
		return al.iterator();
	}

	@Override
	public ValueStub getDiscriminator() {
		return ValueStubFactory.createValueStub(rootClass.getDiscriminator());
	}
}
