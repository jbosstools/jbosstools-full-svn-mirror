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

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VpeIncludeInfo {
	private Element element;
	private IFile file;
	private Document document;
	
	public VpeIncludeInfo(Element element, IFile file, Document document) {
		this.element = element;
		this.file = file;
		this.document = document;
	}
	
	public Element getElement() {
		return element;
	}
	
	public IFile getFile() {
		return file;
	}
	
	public Document getDocument() {
		return this.document;
	}
}
