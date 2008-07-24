package org.jboss.tools.process.ruleflow.editor.editpart;

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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.editor.editpart.RootEditPart;
import org.jboss.tools.process.ruleflow.editor.core.ConnectionWrapper;
import org.jboss.tools.process.ruleflow.editor.core.ProcessWrapper;
import org.jboss.tools.process.ruleflow.editor.core.StartNodeWrapper;
import org.jboss.tools.process.ruleflow.editor.core.SubProcessWrapper;

/**
 * Factory for RuleFlow EditParts.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class RuleFlowEditPartFactory implements EditPartFactory {
    
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart result = null;
        if (model instanceof ProcessWrapper) {
            result = new RootEditPart();
        } else if (model instanceof StartNodeWrapper) {
            result = new StartNodeEditPart();
        } else if (model instanceof SubProcessWrapper) {
            result = new SubProcessEditPart();
        } else if (model instanceof ConnectionWrapper) {
            result = new ConnectionWrapperEditPart();
        } else {
            throw new IllegalArgumentException(
                "Unknown model object " + model);
        }
        result.setModel(model);
        return result;
    }

}
