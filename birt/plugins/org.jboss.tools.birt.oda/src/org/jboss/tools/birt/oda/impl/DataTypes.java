/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.birt.oda.impl;


import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import  org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.birt.oda.Messages;

/**
 * This class hosts the information of data types that are supported by flat
 * file driver
 */

public final class DataTypes
{

	public static final String UNKNOWN = "UNKNOWN"; //$NON-NLS-1$
	public static final int INT = Types.INTEGER;
	public static final int LONG = Types.INTEGER;
	public static final int SHORT = Types.INTEGER;
	public static final int DOUBLE = Types.DOUBLE;
	public static final int STRING = Types.VARCHAR;
	public static final int DATE = Types.DATE;
	public static final int TIME = Types.TIME;
	public static final int TIMESTAMP = Types.TIMESTAMP;
	public static final int BLOB = Types.BLOB;
	public static final int BIGDECIMAL = Types.NUMERIC;
	public static final int NULL = Types.NULL;

	private static Map<String,Integer> typeStringIntPair = new HashMap<String,Integer>();

	static
	{
		typeStringIntPair.put( "INTEGER", new Integer( INT ) ); //$NON-NLS-1$
		typeStringIntPair.put( "INT", new Integer( INT ) ); //$NON-NLS-1$
		typeStringIntPair.put( "LONG", new Integer( INT ) ); //$NON-NLS-1$
		typeStringIntPair.put( "SHORT", new Integer( INT ) ); //$NON-NLS-1$
		typeStringIntPair.put( "DOUBLE", new Integer( DOUBLE ) ); //$NON-NLS-1$
		typeStringIntPair.put( "STRING", new Integer( STRING ) ); //$NON-NLS-1$
		typeStringIntPair.put( "DATE", new Integer( DATE ) ); //$NON-NLS-1$
		typeStringIntPair.put( "TIME", new Integer( TIME ) ); //$NON-NLS-1$
		typeStringIntPair.put( "TIMESTAMP", new Integer( TIMESTAMP ) ); //$NON-NLS-1$
		typeStringIntPair.put( "BLOB", new Integer( BLOB ) ); //$NON-NLS-1$
		typeStringIntPair.put( "BIGDECIMAL", new Integer( BIGDECIMAL ) ); //$NON-NLS-1$
        typeStringIntPair.put( "BIG_DECIMAL", new Integer( BIGDECIMAL ) ); //$NON-NLS-1$
        typeStringIntPair.put( "BIG_INTEGER", new Integer( INT ) ); //$NON-NLS-1$
		typeStringIntPair.put( "NULL", new Integer ( NULL ) ); //$NON-NLS-1$
		typeStringIntPair.put( UNKNOWN, new Integer ( STRING ) );
        
	}

	/**
	 * Return the int which stands for the type specified by input argument
	 *
	 * @param typeName
	 *            the String value of a Type
	 * @return the int which stands for the type specified by input typeName
	 * @throws OdaException
	 *             Once the input argument is not a valid type name
	 */
	public static int getType( String typeName ) throws OdaException
	{
		String name = typeName.trim( ).toUpperCase( );
        while (name.indexOf(".") > 0) { //$NON-NLS-1$
            name = name.substring(name.indexOf(".")+1); //$NON-NLS-1$
        }
		if ( typeStringIntPair.containsKey( name ) )
			return ( (Integer) typeStringIntPair.get( name ) ).intValue( );
		throw new OdaException( NLS.bind(Messages.DataTypes_Invalid_type_name, typeName)); 
	}

	/**
	 * Evaluate whether an input String is a valid type that is supported by driver
	 *
	 * @param typeName
	 * @return
	 */
	public static boolean isValidType( String typeName )
	{
        String name = typeName.trim().toUpperCase();
		boolean valid = typeStringIntPair.containsKey( name );
        if (valid) {
            return true;
        }
        while (name.indexOf(".") > 0) { //$NON-NLS-1$
            name = name.substring(name.indexOf(".")+1); //$NON-NLS-1$
        }
        return typeStringIntPair.containsKey( name );
	}

	private DataTypes( )
	{
	}
}