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
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.flow.editor.GenericModelEditor;
import org.jboss.tools.process.ruleflow.editor.core.ProcessWrapper;
import org.jboss.tools.process.ruleflow.editor.core.StartNodeWrapper;
import org.jboss.tools.process.ruleflow.editor.editpart.RuleFlowEditPartFactory;

/**
 * Graphical editor for a RuleFlow.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class RuleFlowEditor extends GenericModelEditor {

    protected EditPartFactory createEditPartFactory() {
        return new RuleFlowEditPartFactory();
    }

    protected PaletteRoot createPalette() {
        return new RuleFlowPaletteFactory().createPalette();
    }

    protected Object createModel() {
        ProcessWrapper result = new ProcessWrapper();
        StartNodeWrapper start = new StartNodeWrapper();
        start.setConstraint(new Rectangle(100, 100, -1, -1));
        result.addElement(start);
        IFile file = ((IFileEditorInput) getEditorInput()).getFile();
        String name = file.getName();
        result.setName(name.substring(0, name.length() - 3));
        return result;
    }
    
    public ProcessWrapper getRuleFlowModel() {
        return (ProcessWrapper) getModel();
    }

    protected void writeModel(OutputStream os) throws IOException {
        // TODO
    }
    
    protected void createModel(InputStream is) {
        // TODO
        setModel(createModel());
    }
}
