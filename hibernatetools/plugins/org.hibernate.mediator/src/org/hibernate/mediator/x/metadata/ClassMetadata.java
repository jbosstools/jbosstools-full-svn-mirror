package org.hibernate.mediator.x.metadata;

import java.io.Serializable;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.EntityMode;
import org.hibernate.mediator.x.type.Type;
import org.hibernate.mediator.x.type.TypeFactory;

public class ClassMetadata extends HObject {
	public static final String CL = "org.hibernate.metadata.ClassMetadata"; //$NON-NLS-1$

	public ClassMetadata(Object classMetadata) {
		super(classMetadata, CL);
	}

	public String[] getPropertyNames() {
		return (String[])invoke(mn());
	}

	public Type[] getPropertyTypes() {
		Object[] types = (Object[])invoke(mn());
		Type[] res = new Type[types.length];
		for (int i = 0; i < types.length; i++) {
			res[i] = TypeFactory.createTypeStub(types[i]);
		}
		return res;
	}

	public String getIdentifierPropertyName() {
		return (String)invoke(mn());
	}

	public Object getPropertyValue(Object baseObject, String name, EntityMode pojo) {
		return invoke(mn(), baseObject, name, pojo);
	}

	public String getEntityName() {
		return (String)invoke(mn());
	}

	public Type getIdentifierType() {
		return TypeFactory.createTypeStub(invoke(mn()));
	}

	public Class<?> getMappedClass(EntityMode pojo) {
		return (Class<?>)invoke(mn(), pojo);
	}

	public boolean hasIdentifierProperty() {
		return (Boolean)invoke(mn());
	}

	public Serializable getIdentifier(Object entity, EntityMode pojo) {
		return (Serializable)invoke(mn(), entity, pojo);
	}

}
