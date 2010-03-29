package org.hibernate.mediator.stubs.util;

import java.util.ArrayList;

import org.hibernate.util.ArrayHelper;

// org.hibernate.util.StringHelper
public final class StringHelper {
	public static final String WHITESPACE = " \n\r\f\t"; //$NON-NLS-1$

	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static String unqualify(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf("."); //$NON-NLS-1$
		return (loc < 0) ? qualifiedName : qualifiedName.substring(loc + 1);
	}

	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf("."); //$NON-NLS-1$
		return (loc < 0) ? "" : qualifiedName.substring(0, loc); //$NON-NLS-1$
	}

	public static String qualify(String prefix, String name) {
		if (name == null || prefix == null) {
			throw new NullPointerException();
		}
		return new StringBuffer(prefix.length() + name.length() + 1).append(prefix).append('.')
				.append(name).toString();
	}

	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length == 0)
			return ""; //$NON-NLS-1$
		StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
		for (int i = 1; i < length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	public static int[] locateUnquoted(String string, char character) {
		if ( '\'' == character ) {
			throw new IllegalArgumentException( "Unquoted count of quotes is invalid" ); //$NON-NLS-1$
		}
		if (string == null) {
			return new int[0];
		}

		ArrayList<Integer> locations = new ArrayList<Integer>( 20 );

		// Impl note: takes advantage of the fact that an escpaed single quote
		// embedded within a quote-block can really be handled as two seperate
		// quote-blocks for the purposes of this method...
		int stringLength = string.length();
		boolean inQuote = false;
		for ( int indx = 0; indx < stringLength; indx++ ) {
			char c = string.charAt( indx );
			if ( inQuote ) {
				if ( '\'' == c ) {
					inQuote = false;
				}
			}
			else if ( '\'' == c ) {
				inQuote = true;
			}
			else if ( c == character ) {
				locations.add( new Integer( indx ) );
			}
		}
		return ArrayHelper.toIntArray( locations );
	}
}
