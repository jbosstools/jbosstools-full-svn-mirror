package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;


public class RootClass extends PersistentClass {
	public static final String CL = "org.hibernate.mapping.RootClass"; //$NON-NLS-1$

	protected RootClass(Object rootClass) {
		super(rootClass, CL);
	}

	public static RootClass newInstance() {
		return new RootClass(newInstance(CL));
	}

	public String getClassName() {
		return (String)invoke(mn());
	}

	public void setDiscriminator(Value discr) {
		invoke(mn(), discr);
	}

	public TableStub getTable() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public void addProperty(Property p) {
		invoke(mn(), p);
	}

	public void setIdentifierProperty(Property p) {
		invoke(mn(), p);
	}

	public Property getIdentifierProperty() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new Property(obj);
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

	public KeyValue getIdentifier() {
		return (KeyValue)ValueFactory.createValueStub(invoke(mn()));
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
	public Iterator<Subclass> getSubclassIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<Subclass> al = new ArrayList<Subclass>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Subclass(obj));
			}
		}
		return al.iterator();
	}

	@Override
	public Value getDiscriminator() {
		return ValueFactory.createValueStub(invoke(mn()));
	}
}
