package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

public class RootClassStub extends PersistentClassStub {
	public static final String CL = "org.hibernate.mapping.RootClass"; //$NON-NLS-1$

	protected RootClassStub(Object rootClass) {
		super(rootClass, CL);
	}

	public static RootClassStub newInstance() {
		return new RootClassStub(newInstance(CL));
	}

	public String getClassName() {
		return (String)invoke(mn());
	}

	public void setDiscriminator(ValueStub discr) {
		invoke(mn(), discr);
	}

	public TableStub getTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public void addProperty(PropertyStub p) {
		invoke(mn(), p);
	}

	public void setIdentifierProperty(PropertyStub p) {
		invoke(mn(), p);
	}

	public PropertyStub getIdentifierProperty() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public void setEntityName(String entityName) {
		invoke(mn(), entityName);
	}

	public void setClassName(String className) {
		invoke(mn(), className);
	}

	public void setProxyInterfaceName(String proxyInterfaceName) {
		invoke(mn(), proxyInterfaceName);
	}

	public void setLazy(boolean lazy) {
		invoke(mn(), lazy);
	}

	public void setTable(TableStub table) {
		invoke(mn(), table);
	}

	public void setAbstract(Boolean isAbstract) {
		invoke(mn(), isAbstract);
	}

	public String getNodeName() {
		return (String)invoke(mn());
	}

	public KeyValueStub getIdentifier() {
		return (KeyValueStub)ValueStubFactory.createValueStub(invoke(mn()));
	}

	public boolean isCustomDeleteCallable() {
		return (Boolean)invoke(mn());
	}

	public boolean isCustomInsertCallable() {
		return (Boolean)invoke(mn());
	}

	public boolean isCustomUpdateCallable() {
		return (Boolean)invoke(mn());
	}

	public boolean isDiscriminatorInsertable() {
		return (Boolean)invoke(mn());
	}

	public boolean isDiscriminatorValueNotNull() {
		return (Boolean)invoke(mn());
	}

	public boolean isDiscriminatorValueNull() {
		return (Boolean)invoke(mn());
	}

	public boolean isExplicitPolymorphism() {
		return (Boolean)invoke(mn());
	}

	public boolean isForceDiscriminator() {
		return (Boolean)invoke(mn());
	}

	public boolean isInherited() {
		return (Boolean)invoke(mn());
	}

	public boolean isJoinedSubclass() {
		return (Boolean)invoke(mn());
	}

	public boolean isLazy() {
		return (Boolean)invoke(mn());
	}

	public boolean isLazyPropertiesCacheable() {
		return (Boolean)invoke(mn());
	}

	public boolean isMutable() {
		return (Boolean)invoke(mn());
	}

	public boolean isPolymorphic() {
		return (Boolean)invoke(mn());
	}

	public boolean isVersioned() {
		return (Boolean)invoke(mn());
	}

	public int getBatchSize() {
		return (Integer)invoke(mn());
	}

	public String getCacheConcurrencyStrategy() {
		return (String)invoke(mn());
	}

	public String getCustomSQLDelete() {
		return (String)invoke(mn());
	}

	public String getCustomSQLInsert() {
		return (String)invoke(mn());
	}

	public String getCustomSQLUpdate() {
		return (String)invoke(mn());
	}

	public String getDiscriminatorValue() {
		return (String)invoke(mn());
	}

	public String getLoaderName() {
		return (String)invoke(mn());
	}

	public int getOptimisticLockMode() {
		return (Integer)invoke(mn());
	}

	public String getTemporaryIdTableDDL() {
		return (String)invoke(mn());
	}

	public String getTemporaryIdTableName() {
		return (String)invoke(mn());
	}

	public String getWhere() {
		return (String)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Iterator<SubclassStub> getSubclassIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<SubclassStub> al = new ArrayList<SubclassStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new SubclassStub(obj));
			}
		}
		return al.iterator();
	}

	@Override
	public ValueStub getDiscriminator() {
		return ValueStubFactory.createValueStub(invoke(mn()));
	}
}
