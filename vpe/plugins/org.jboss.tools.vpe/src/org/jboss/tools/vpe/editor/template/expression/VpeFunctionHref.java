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
package org.jboss.tools.vpe.editor.template.expression;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;

public class VpeFunctionHref extends VpeFunctionSrc {
    @Override
	protected String getUnresolved() {
	    return ""; //$NON-NLS-1$
    }

    @Override
	String[] getSignatures() {
		return new String[] {VpeExpressionBuilder.SIGNATURE_ANY_ATTR};
	}

    @Override
	public VpeValue exec(VpePageContext pageContext, Node sourceNode) throws VpeExpressionException {
	String tagValue = getParameter(0).exec(pageContext, sourceNode).stringValue();

	//tagValue = resolveEL(pageContext,tagValue);
	
	IPath tagPath = new Path(tagValue);
	if (tagPath.isEmpty()) {
	    return new VpeValue(getUnresolved());
	}

	String device = tagPath.getDevice() == null
			? tagPath.segment(0)
			: tagPath.getDevice();

	if (device != null
		&& ("http:".equalsIgnoreCase(device) //$NON-NLS-1$
			|| "file:".equalsIgnoreCase(device))) { //$NON-NLS-1$
	    return new VpeValue(tagValue);
	}

	File locFile = tagPath.toFile();
	if (locFile.exists()) {
	    return new VpeValue(getPrefix() + locFile.getAbsolutePath());
	}

	IEditorInput input = pageContext.getEditPart().getEditorInput();
	IPath inputPath = getInputParentPath(input);
	IPath imgPath = null;
	if (input instanceof ILocationProvider) {
	    imgPath = inputPath.append(tagValue);
	} else {
	    IPath basePath = tagPath.isAbsolute()
	    		? VpeStyleUtil.getRootPath(input)
	    		: inputPath;
	    if (basePath != null) {
		imgPath = basePath.append(tagPath);
	    }
	}

	if (imgPath != null && imgPath.toFile().exists()) {
	    return new VpeValue(getPrefix() + imgPath.toString());
	}

	return new VpeValue(getUnresolved());
    }
}