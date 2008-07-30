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

import org.jboss.tools.flow.editor.editpart.ConnectionEditPart;
import org.jboss.tools.process.ruleflow.editor.core.ConnectionWrapper;

public class ConnectionWrapperEditPart extends ConnectionEditPart {

	protected Class<?> getElementConnectionType() {
		return ConnectionWrapper.class;
	}
	
}
