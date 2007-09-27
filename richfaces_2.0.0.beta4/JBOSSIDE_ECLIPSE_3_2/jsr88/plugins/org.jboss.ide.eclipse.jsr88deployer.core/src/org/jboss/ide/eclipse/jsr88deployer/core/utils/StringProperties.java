/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.core.utils;

import java.util.Properties;

/**
 * @author Rob Stryker
 */
public class StringProperties {
	
	private Properties properties;
	
	public StringProperties() {
		properties = new Properties();
	}
	
	
	public StringProperties( String s ) {
		this();
		String[] split = s.split("\n");
		for( int i = 0; i < split.length; i++ ) {
			properties.put(new Integer(i), split[i]);
		}
	}
	
	public String getPiece(int num) {
		Object retval = properties.get(new Integer(num));
		if( retval != null && retval instanceof String ) {
			return (String)retval;
		}
		return "";
	}
	
	public void setPiece( int place, String val ) {
		properties.put( new Integer(place), val);
	}

	public void put( int place, String val ) {
		setPiece(place, val);
	}
	
	public String toString() {
		boolean done = false;
		String string = "";
		int i = 0;
		while( !done ) {
			 Object j = properties.get(new Integer(i++));
			 if( j == null ) {
				 done = true;				 
			 }
			 else {
				 String val = (String)j;
				 string += val + "\n";
			 }
		}
		return string;
	}

}
