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
package org.jboss.tools.vpe.editor;

import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;

public class VpeDomBuilder {
	protected VpeDomMapping domMapping;
	
	protected VpeTemplateManager templateManager;
	private INodeAdapter sorceAdapter;

	public VpeDomBuilder(VpeDomMapping domMapping, INodeAdapter sorceAdapter, VpeTemplateManager templateManager) {
		this.domMapping = domMapping;
		this.sorceAdapter = sorceAdapter;
		this.templateManager = templateManager;
	}
	
	public VpeDomMapping getDomMapping ()  {
		return domMapping;
	}
	
	public VpeTemplateManager getTemplateManager ()  {
		return templateManager;
	}

	public void registerNodes(VpeNodeMapping nodeMapping) {
		domMapping.mapNodes(nodeMapping);
		Node sourceNode = nodeMapping.getSourceNode();
		if (((INodeNotifier) sourceNode).getExistingAdapter(sorceAdapter) == null) {
			((INodeNotifier) sourceNode).addAdapter(sorceAdapter);
		}
	}
}
