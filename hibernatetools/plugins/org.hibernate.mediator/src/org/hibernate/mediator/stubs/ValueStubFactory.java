package org.hibernate.mediator.stubs;

public class ValueStubFactory {
	@SuppressWarnings("unchecked")
	public static ValueStub createValueStub(Object value) {
		if (value == null) {
			return null;
		}
		final Class cl = value.getClass();
		if (0 == "org.hibernate.mapping.Bag".compareTo(cl.getName())) { //$NON-NLS-1$
			return new BagStub(value);
		//} else if (0 == "org.hibernate.mapping.IdentifierCollection".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new IdentifierCollectionStub(value);
		} else if (0 == "org.hibernate.mapping.IdentifierBag".compareTo(cl.getName())) { //$NON-NLS-1$
			return new IdentifierBagStub(value);
		//} else if (0 == "org.hibernate.mapping.IndexedCollection".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new IndexedCollectionStub(value);
		} else if (0 == "org.hibernate.mapping.List".compareTo(cl.getName())) { //$NON-NLS-1$
			return new ListStub(value);
		} else if (0 == "org.hibernate.mapping.Array".compareTo(cl.getName())) { //$NON-NLS-1$
			return new ArrayStub(value);
		} else if (0 == "org.hibernate.mapping.PrimitiveArray".compareTo(cl.getName())) { //$NON-NLS-1$
			return new PrimitiveArrayStub(value);
		} else if (0 == "org.hibernate.mapping.Map".compareTo(cl.getName())) { //$NON-NLS-1$
			return new MapStub(value);
		} else if (0 == "org.hibernate.mapping.Set".compareTo(cl.getName())) { //$NON-NLS-1$
			return new SetStub(value);
		} else if (0 == "org.hibernate.mapping.OneToMany".compareTo(cl.getName())) { //$NON-NLS-1$
			return new OneToManyStub(value);
		//} else if (0 == "org.hibernate.mapping.KeyValue".compareTo(cl.getName())) { //$NON-NLS-1$
		//	return new KeyValueStub(value);
		} else if (0 == "org.hibernate.mapping.SimpleValue".compareTo(cl.getName())) { //$NON-NLS-1$
			return new SimpleValueStub(value);
		} else if (0 == "org.hibernate.mapping.Any".compareTo(cl.getName())) { //$NON-NLS-1$
			return new AnyStub(value);
		} else if (0 == "org.hibernate.mapping.Component".compareTo(cl.getName())) { //$NON-NLS-1$
			return new ComponentStub(value);
		} else if (0 == "org.hibernate.mapping.DependantValue".compareTo(cl.getName())) { //$NON-NLS-1$
			return new DependantValueStub(value);
		} else if (0 == "org.hibernate.mapping.ToOne".compareTo(cl.getName())) { //$NON-NLS-1$
			return new ToOneStub(value);
		} else if (0 == "org.hibernate.mapping.ManyToOne".compareTo(cl.getName())) { //$NON-NLS-1$
			return new ManyToOneStub(value);
		} else if (0 == "org.hibernate.mapping.OneToOne".compareTo(cl.getName())) { //$NON-NLS-1$
			return new OneToOneStub(value);
		}
		return null;
	}
}
