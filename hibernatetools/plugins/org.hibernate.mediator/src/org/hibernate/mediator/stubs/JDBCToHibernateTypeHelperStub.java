package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class JDBCToHibernateTypeHelperStub {
	public static final String CL = "org.hibernate.cfg.reveng.JDBCToHibernateTypeHelper"; //$NON-NLS-1$

	public static String getJDBCTypeName(int intValue) {
		return (String)HObject.invokeStaticMethod(CL, "getJDBCTypeName", (Integer)intValue); //$NON-NLS-1$
	}

	public static String getPreferredHibernateType(int intValue, int length, int precision,
			int scale, boolean nullability, boolean b) {
		return (String)HObject.invokeStaticMethod(CL, "getPreferredHibernateType", (Integer)intValue, //$NON-NLS-1$
			(Integer)length, (Integer)precision, (Integer)scale, (Boolean)nullability, (Boolean)b);
	}

	public static boolean typeHasLength(int intValue) {
		return (Boolean)HObject.invokeStaticMethod(CL, "typeHasLength", (Integer)intValue); //$NON-NLS-1$
	}

	public static boolean typeHasScaleAndPrecision(int intValue) {
		return (Boolean)HObject.invokeStaticMethod(CL, "typeHasScaleAndPrecision", (Integer)intValue); //$NON-NLS-1$
	}

}
