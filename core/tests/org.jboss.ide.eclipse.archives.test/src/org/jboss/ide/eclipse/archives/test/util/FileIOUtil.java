package org.jboss.ide.eclipse.archives.test.util;

import java.io.File;

public class FileIOUtil {

	public static void clearFolder(File[] children) {
		for( int i = 0; i < children.length; i++ ) {
			File[] second = children[i].listFiles();
			if( second != null && second.length > 0 )
				clearFolder(second);
			children[i].delete();
		}
	}
	
}
