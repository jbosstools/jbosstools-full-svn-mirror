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
		return (String)invoke("getClassName"); //$NON-NLS-1$
	}

	public void setDiscriminator(ValueStub discr) {
		invoke("setDiscriminator", discr); //$NON-NLS-1$
	}

	public TableStub getTable() {
		Object obj = invoke("getTable"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new TableStub(obj);
	}

	public void addProperty(PropertyStub p) {
		invoke("addProperty", p); //$NON-NLS-1$
	}

	public void setIdentifierProperty(PropertyStub p) {
		invoke("setIdentifierProperty", p); //$NON-NLS-1$
	}

	public PropertyStub getIdentifierProperty() {
		Object obj = invoke("getIdentifierProperty"); //$NON-NLS-1$
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}

	public void setEntityName(String entityName) {
		invoke("setEntityName", entityName); //$NON-NLS-1$
	}

	public void setClassName(String className) {
		invoke("setClassName", className); //$NON-NLS-1$
	}

	public void setProxyInterfaceName(String proxyInterfaceName) {
		invoke("setProxyInterfaceName", proxyInterfaceName); //$NON-NLS-1$
	}

	public void setLazy(boolean lazy) {
		invoke("setLazy", lazy); //$NON-NLS-1$
	}

	public void setTable(TableStub table) {
		invoke("setTable", table); //$NON-NLS-1$
	}

	public void setAbstract(Boolean isAbstract) {
		invoke("setAbstract", isAbstract); //$NON-NLS-1$
	}

	public String getNodeName() {
		return (String)invoke("getNodeName"); //$NON-NLS-1$
	}

	public KeyValueStub getIdentifier() {
		return (KeyValueStub)ValueStubFactory.createValueStub(invoke("getIdentifier")); //$NON-NLS-1$
	}

	public boolean isCustomDeleteCallable() {
		return (Boolean)invoke("isCustomDeleteCallable"); //$NON-NLS-1$
	}

	public boolean isCustomInsertCallable() {
		return (Boolean)invoke("isCustomInsertCallable"); //$NON-NLS-1$
	}

	public boolean isCustomUpdateCallable() {
		return (Boolean)invoke("isCustomUpdateCallable"); //$NON-NLS-1$
	}

	public boolean isDiscriminatorInsertable() {
		return (Boolean)invoke("isDiscriminatorInsertable"); //$NON-NLS-1$
	}

	public boolean isDiscriminatorValueNotNull() {
		return (Boolean)invoke("isDiscriminatorValueNotNull"); //$NON-NLS-1$
	}

	public boolean isDiscriminatorValueNull() {
		return (Boolean)invoke("isDiscriminatorValueNull"); //$NON-NLS-1$
	}

	public boolean isExplicitPolymorphism() {
		return (Boolean)invoke("isExplicitPolymorphism"); //$NON-NLS-1$
	}

	public boolean isForceDiscriminator() {
		return (Boolean)invoke("isForceDiscriminator"); //$NON-NLS-1$
	}

	public boolean isInherited() {
		return (Boolean)invoke("isInherited"); //$NON-NLS-1$
	}

	public boolean isJoinedSubclass() {
		return (Boolean)invoke("isJoinedSubclass"); //$NON-NLS-1$
	}

	public boolean isLazy() {
		return (Boolean)invoke("isLazy"); //$NON-NLS-1$
	}

	public boolean isLazyPropertiesCacheable() {
		return (Boolean)invoke("isLazyPropertiesCacheable"); //$NON-NLS-1$
	}

	public boolean isMutable() {
		return (Boolean)invoke("isMutable"); //$NON-NLS-1$
	}

	public boolean isPolymorphic() {
		return (Boolean)invoke("isPolymorphic"); //$NON-NLS-1$
	}

	public boolean isVersioned() {
		return (Boolean)invoke("isVersioned"); //$NON-NLS-1$
	}

	public int getBatchSize() {
		return (Integer)invoke("getBatchSize"); //$NON-NLS-1$
	}

	public String getCacheConcurrencyStrategy() {
		return (String)invoke("getCacheConcurrencyStrategy"); //$NON-NLS-1$
	}

	public String getCustomSQLDelete() {
		return (String)invoke("getCustomSQLDelete"); //$NON-NLS-1$
	}

	public String getCustomSQLInsert() {
		return (String)invoke("getCustomSQLInsert"); //$NON-NLS-1$
	}

	public String getCustomSQLUpdate() {
		return (String)invoke("getCustomSQLUpdate"); //$NON-NLS-1$
	}

	public String getDiscriminatorValue() {
		return (String)invoke("getDiscriminatorValue"); //$NON-NLS-1$
	}

	public String getLoaderName() {
		return (String)invoke("getLoaderName"); //$NON-NLS-1$
	}

	public int getOptimisticLockMode() {
		return (Integer)invoke("getOptimisticLockMode"); //$NON-NLS-1$
	}

	public String getTemporaryIdTableDDL() {
		return (String)invoke("getTemporaryIdTableDDL"); //$NON-NLS-1$
	}

	public String getTemporaryIdTableName() {
		return (String)invoke("getTemporaryIdTableName"); //$NON-NLS-1$
	}

	public String getWhere() {
		return (String)invoke("getWhere"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Iterator<SubclassStub> getSubclassIterator() {
		Iterator it = (Iterator)invoke("getSubclassIterator"); //$NON-NLS-1$
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
		return ValueStubFactory.createValueStub(invoke("getDiscriminator")); //$NON-NLS-1$
	}
}
