package org.jboss.tools.flow.common.command;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.wrapper.Wrapper;

/**
 * A command for renaming an element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class RenameElementCommand extends Command {

    private Wrapper source;
    private String name;
    private String oldName;
    

    public void execute() {
    	IPropertySource propertySource = (IPropertySource)source.getAdapter(IPropertySource.class);
    	if (propertySource != null) {
    		propertySource.setPropertyValue(IPropertyId.NAME, name);
    	}
    }

    public void setName(String string) {
        name = string;
    }

    public void setOldName(String string) {
        oldName = string;
    }

    public void setSource(Wrapper action) {
        source = action;
    }

    public void undo() {
    	IPropertySource propertySource = (IPropertySource)source.getAdapter(IPropertySource.class);
    	if (propertySource != null) {
    		propertySource.setPropertyValue(IPropertyId.NAME, oldName);
    	}
    }
}
