package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.JDBCToHibernateTypeHelper;

public class JDBCToHibernateTypeHelperStub {

	public static String getJDBCTypeName(int intValue) {
		return JDBCToHibernateTypeHelper.getJDBCTypeName(intValue);
	}

	public static String getPreferredHibernateType(int intValue, int length, int precision,
			int scale, boolean nullability, boolean b) {
		return JDBCToHibernateTypeHelper.getPreferredHibernateType(intValue, 
				length, precision, scale, nullability, b);
	}

	public static boolean typeHasLength(int intValue) {
		return JDBCToHibernateTypeHelper.typeHasLength(intValue);
	}

	public static boolean typeHasScaleAndPrecision(int intValue) {
		return JDBCToHibernateTypeHelper.typeHasScaleAndPrecision(intValue);
	}

}
