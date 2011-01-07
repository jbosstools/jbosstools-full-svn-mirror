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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.eclipse.gef.palette.PaletteRoot;
import org.jboss.tools.flow.common.Activator;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.AbstractFlowWrapper;
import org.jboss.tools.process.ruleflow.model.RuleFlowProcess;
import org.jboss.tools.process.ruleflow.xml.XmlRuleFlowProcessDumper;
import org.jboss.tools.process.ruleflow.xml.XmlRuleFlowProcessReader;

/**
 * Graphical editor for a RuleFlow.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class RuleFlowEditor extends GenericModelEditor {

    protected PaletteRoot createPalette() {
        return new RuleFlowPaletteFactory().createPalette();
    }

    protected Object createModel() {
    	return ElementRegistry.createWrapper("org.jboss.tools.flow.ruleflow.process"); //$NON-NLS-1$
    }
    
    protected void writeModel(OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os);
        try {
            RuleFlowProcess process = (RuleFlowProcess) ((AbstractFlowWrapper) getModel()).getElement();
            String out = XmlRuleFlowProcessDumper.dump(process);
            writer.write(out);
        } catch (Throwable t) {
            Activator.log(t);
        }
        writer.close();
    }
    
    protected void createModel(InputStream is) {
        try {
            InputStreamReader reader = new InputStreamReader(is);
            XmlRuleFlowProcessReader xmlReader = new XmlRuleFlowProcessReader();
            try {
                RuleFlowProcess process = (RuleFlowProcess) xmlReader.read(reader);
                if (process == null) {
                    setModel(createModel());
                } else {
                    setModel(new RuleFlowWrapperBuilder().getProcessWrapper(process));
                }
            } catch (Throwable t) {
            	Activator.log(t);
                setModel(createModel());
            }
            reader.close();
        } catch (Throwable t) {
        	Activator.log(t);
        }
    }
    
}
