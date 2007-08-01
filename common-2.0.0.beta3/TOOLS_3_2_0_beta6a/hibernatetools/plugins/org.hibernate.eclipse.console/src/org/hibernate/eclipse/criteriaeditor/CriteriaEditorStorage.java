/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.criteriaeditor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Storage for Criteria editors without a file
 */
public class CriteriaEditorStorage implements IStorage {
    
	private String contents;
    private String nameLabel;
	private String configurationName;

    public CriteriaEditorStorage( String source ) {
        this( "", source, source );
    }

    public CriteriaEditorStorage( String configurationName, String name, String source ) {
        super();
        setName( name );
        setQuery( source );
        setConfigurationName(configurationName);
    }

	public void setQuery(String source) {
		if(source==null) { return; }
		setContents(source);
	}

    public Object getAdapter( Class key ) {
        return null;
    }

    public InputStream getContents() {
    	return new ByteArrayInputStream( contents.getBytes() );
    }

    /**
     * @return contents as a string
     */
    public String getContentsString() {
        String contentsString = ""; 
        
        InputStream contentsStream = getContents();
        
        // The following code was adapted from StorageDocumentProvider.setDocumentContent method.
        Reader in = null;
        try {
            in = new BufferedReader( new InputStreamReader( contentsStream ));
            StringBuffer buffer = new StringBuffer();
            char[] readBuffer = new char[2048];
            int n = in.read( readBuffer );
            while (n > 0) {
                buffer.append( readBuffer, 0, n );
                n = in.read( readBuffer );
            }
            contentsString = buffer.toString();
        } catch (IOException x) {
            // ignore and save empty content
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException x) {
                    // ignore, too late to do anything here
                }
            }
        }

        return contentsString;
    }
    
    public IPath getFullPath() {
        return new Path("/" + hashCode() + ".crit");
    }

    public String getName() {
        return nameLabel;
    }

    public boolean isReadOnly() {
        return false;
    }


    public void setName( String name ) {
        nameLabel = name;
    }

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;		
	}

	public void setContents(String query) {
		this.contents = query;
	}

}