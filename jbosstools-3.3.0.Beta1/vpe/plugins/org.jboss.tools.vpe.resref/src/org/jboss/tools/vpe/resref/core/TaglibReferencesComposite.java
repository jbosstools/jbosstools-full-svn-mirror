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
package org.jboss.tools.vpe.resref.core;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.core.ResourceReferenceList;
import org.jboss.tools.common.resref.ui.AbstractResourceReferencesComposite;
import org.jboss.tools.common.resref.ui.ResourceReferencesTableProvider;

/**
 * 
 * @author Eugene Stherbin
 *
 */
public class TaglibReferencesComposite extends VpeResourceReferencesComposite {

	protected ResourceReferencesTableProvider createTableProvider(List dataList) {
		return ResourceReferencesTableProvider.getTLDTableProvider(dataList);
	}

	protected ResourceReferenceList getReferenceList() {
		return TaglibReferenceList.getInstance();
	}

    protected ReferenceWizardDialog getDialog(ResourceReference resref) {
        return new TaglibReferenceWizardDialog(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(), fileLocation, resref);
    }
}
