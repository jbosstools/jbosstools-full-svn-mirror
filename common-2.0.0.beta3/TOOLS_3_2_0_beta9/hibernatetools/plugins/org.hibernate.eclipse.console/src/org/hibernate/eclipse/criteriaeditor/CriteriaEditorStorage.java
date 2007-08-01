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

import org.eclipse.core.runtime.IPath;
import org.hibernate.eclipse.console.QueryEditorStorage;

/**
 * Storage for Criteria editors without a file
 */
public class CriteriaEditorStorage extends QueryEditorStorage {
    

    public CriteriaEditorStorage( String source ) {
        super(source);
    }

    public CriteriaEditorStorage( String configurationName, String name, String source ) {
        super(configurationName, name, source );
    }

    
    public CriteriaEditorStorage(String consoleName, String string) {
		super(consoleName, "Criteria: " + (consoleName==null?"<None>":consoleName), string);
	}

	public IPath getFullPath() {
        return null;
    }


}