package org.hibernate.console.stubs.util;

public class ParserHelper {
	public static final String HQL_VARIABLE_PREFIX = ":"; //$NON-NLS-1$

	public static final String HQL_SEPARATORS = " \n\r\f\t,()=<>&|+-=/*'^![]#~\\"; //$NON-NLS-1$
	//NOTICE: no " or . since they are part of (compound) identifiers
	public static final String PATH_SEPARATORS = "."; //$NON-NLS-1$
}
