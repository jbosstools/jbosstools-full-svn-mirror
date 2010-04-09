package org.hibernate.mediator.x.metadata;

import java.io.Serializable;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.EntityModeStub;
import org.hibernate.mediator.x.type.TypeStub;
import org.hibernate.mediator.x.type.TypeStubFactory;

public class ClassMetadataStub extends HObject {
	public static final String CL = "org.hibernate.metadata.ClassMetadata"; //$NON-NLS-1$

	public ClassMetadataStub(Object classMetadata) {
		super(classMetadata, CL);
	}

	public String[] getPropertyNames() {
		return (String[])invoke(mn());
	}

	public TypeStub[] getPropertyTypes() {
		Object[] types = (Object[])invoke(mn());
		TypeStub[] res = new TypeStub[types.length];
		for (int i = 0; i < types.length; i++) {
			res[i] = TypeStubFactory.createTypeStub(types[i]);
		}
		return res;
	}

	public String getIdentifierPropertyName() {
		return (String)invoke(mn());
	}

	public Object getPropertyValue(Object baseObject, String name, EntityModeStub pojo) {
		return invoke(mn(), baseObject, name, pojo);
	}

	public String getEntityName() {
		return (String)invoke(mn());
	}

	public TypeStub getIdentifierType() {
		return TypeStubFactory.createTypeStub(invoke(mn()));
	}

	public Class<?> getMappedClass(EntityModeStub pojo) {
		return (Class<?>)invoke(mn(), pojo);
	}

	public boolean hasIdentifierProperty() {
		return (Boolean)invoke(mn());
	}

	public Serializable getIdentifier(Object entity, EntityModeStub pojo) {
		return (Serializable)invoke(mn(), entity, pojo);
	}

}
