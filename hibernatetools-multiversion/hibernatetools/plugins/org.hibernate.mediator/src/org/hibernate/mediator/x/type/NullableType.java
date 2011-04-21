package org.hibernate.mediator.x.type;


public class NullableType extends AbstractType {
	public static final String CL = "org.hibernate.type.NullableType"; //$NON-NLS-1$

	protected NullableType(Object nullableType) {
		super(nullableType, CL);
	}

	protected NullableType(Object nullableType, String cn) {
		super(nullableType, cn);
	}

	public String toString(Object value) {
		return (String)invoke(mn(), value);
	}

	public Object fromStringValue(String xml) {
		return invoke(mn(), xml);
	}
}
