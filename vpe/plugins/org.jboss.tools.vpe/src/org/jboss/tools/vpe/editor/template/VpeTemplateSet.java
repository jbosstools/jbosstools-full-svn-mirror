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
package org.jboss.tools.vpe.editor.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeTemplateSet {
	private List<VpeTemplateSet> templates = new ArrayList<VpeTemplateSet>();
	private VpeTemplate defTemplate;
	
	VpeTemplateSet(){
	}

	void addChild(VpeTemplateSet set) {
		templates.add(set);
	}

	void setDefTemplate(VpeTemplate defTemplate) {
		if (this.defTemplate == null) {
			this.defTemplate = defTemplate;
		}
	}

	VpeTemplate getTemplate(VpePageContext pageContext, Node sourceNode, Set ifDependencySet) {
		for (int i = 0; i < templates.size(); i++) {
			VpeTemplateSet set = (VpeTemplateSet)templates.get(i);
			VpeTemplate template = set.getTemplate(pageContext, sourceNode, ifDependencySet);
			if (template != null) {
				return template;
			}
		}
		return defTemplate;
	}
}
