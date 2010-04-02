package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

public class ClassMetadataStub {
	public static final String CL = "org.hibernate.metadata.ClassMetadata"; //$NON-NLS-1$

	protected ClassMetadata classMetadata;

	protected ClassMetadataStub(Object classMetadata) {
		if (classMetadata == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.classMetadata = (ClassMetadata)classMetadata;
	}

	public String[] getPropertyNames() {
		return classMetadata.getPropertyNames();
	}

	public TypeStub[] getPropertyTypes() {
		Type[] types = classMetadata.getPropertyTypes();
		TypeStub[] res = new TypeStub[types.length];
		for (int i = 0; i < types.length; i++) {
			res[i] = TypeStubFactory.createTypeStub(types[i]);
		}
		return res;
	}

	public String getIdentifierPropertyName() {
		return classMetadata.getIdentifierPropertyName();
	}

	public Object getPropertyValue(Object baseObject, String name, EntityModeStub pojo) {
		return classMetadata.getPropertyValue(baseObject, name, pojo.entityMode);
	}

	public String getEntityName() {
		return classMetadata.getEntityName();
	}

	public TypeStub getIdentifierType() {
		return TypeStubFactory.createTypeStub(classMetadata.getIdentifierType());
	}

	public Class<?> getMappedClass(EntityModeStub pojo) {
		return classMetadata.getMappedClass(pojo.entityMode);
	}

}
