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

/**
 * A command for renaming an element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class RenameElementCommand extends Command {

    private IPropertySource source;
    private String name;
    private String oldName;
    

    public void execute() {
    	if (source != null) {
    		source.setPropertyValue(IPropertyId.NAME, name);
    	}
    }

    public void setName(String string) {
        name = string;
    }

    public void setOldName(String string) {
        oldName = string;
    }

    public void setSource(IPropertySource propertySource) {
        source = propertySource;
    }

    public void undo() {
    	if (source != null) {
    		source.setPropertyValue(IPropertyId.NAME, oldName);
    	}
    }
}
