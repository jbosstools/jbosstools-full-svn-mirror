package org.jboss.tools.process.ruleflow.editor;

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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.flow.editor.PaletteFactory;
import org.jboss.tools.process.ruleflow.Activator;
import org.jboss.tools.process.ruleflow.editor.core.ConnectionWrapperFactory;
import org.jboss.tools.process.ruleflow.editor.core.StartNodeWrapper;
import org.jboss.tools.process.ruleflow.editor.core.SubProcessWrapper;

/**
 * Factory for creating a RuleFlow palette.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class RuleFlowPaletteFactory extends PaletteFactory {
    
    public RuleFlowPaletteFactory() {
        super(new ConnectionWrapperFactory());
    }

    protected List<PaletteEntry> createComponentEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
        
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
            "Start",
            "Create a new Start",
            StartNodeWrapper.class,
            new SimpleFactory(StartNodeWrapper.class),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/start.gif")),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/start.gif"))
        );
        entries.add(combined);
        
        combined = new CombinedTemplateCreationEntry(
            "SubProcess",
            "Create a new sub-process",
            SubProcessWrapper.class,
            new SimpleFactory(SubProcessWrapper.class),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/process.gif")),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/process.gif"))
        );
        entries.add(combined);
                          
        return entries;
    }
    
}
