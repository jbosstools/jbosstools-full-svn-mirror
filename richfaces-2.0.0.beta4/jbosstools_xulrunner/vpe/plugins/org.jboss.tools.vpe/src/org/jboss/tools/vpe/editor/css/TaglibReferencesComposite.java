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
package org.jboss.tools.vpe.editor.css;

import java.util.List;

public class TaglibReferencesComposite extends ResourceReferencesComposite {

	protected String getEntity() {
		return (file != null) ? "VPETLDReference" : "VPETLDReferenceExt";
	}

	protected ResourceReferencesTableProvider createTableProvider(List dataList) {
		return ResourceReferencesTableProvider.getTLDTableProvider(dataList);
	}

	protected ResourceReferenceList getReferenceList() {
		return TaglibReferenceList.getInstance();
	}

}
