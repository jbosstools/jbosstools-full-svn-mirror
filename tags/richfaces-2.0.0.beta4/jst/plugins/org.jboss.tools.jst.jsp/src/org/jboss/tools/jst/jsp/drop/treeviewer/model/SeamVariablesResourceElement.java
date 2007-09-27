/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.drop.treeviewer.model;

import java.util.List;
import java.util.Properties;

import org.eclipse.ui.IEditorInput;
import org.jboss.tools.jst.jsp.outline.ValueHelper;

/**
 * 
 * @author Viacheslav Kabanovich	
 */
public class SeamVariablesResourceElement extends AttributeValueResource {
	IEditorInput editorInput;
	
	SeamVariableElement[] elements = null;

	public SeamVariablesResourceElement(IEditorInput editorInput, String name, ModelElement parent) {
		super(name, parent);
		this.editorInput = editorInput;
	}

	public ModelElement[] getChildren() {
		if(elements != null) {
			return elements;
		}
		Properties p = new Properties();
		p.put("file", valueHelper.getFile());
		List list = ValueHelper.seamPromptingProvider.getList(null, "seam.variables", "", p);
		if(list == null) return EMPTY_LIST;
		SeamVariableElement[] es = new SeamVariableElement[list.size()];
		for (int i = 0; i < es.length; i++) {
			es[i] = new SeamVariableElement(list.get(i).toString(), this);
		}
		return elements = es;
	}

	public String getName() {
		return "Seam Variables";
	}

}
