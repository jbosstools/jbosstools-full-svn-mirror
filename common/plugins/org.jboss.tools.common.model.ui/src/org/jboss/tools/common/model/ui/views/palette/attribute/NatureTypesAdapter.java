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
package org.jboss.tools.common.model.ui.views.palette.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.model.ui.attribute.adapter.CheckListAdapter;

public class NatureTypesAdapter extends CheckListAdapter {
	public NatureTypesAdapter() {
		super();
		setTags(getNatureTypes());
	}
	
	private String[] getNatureTypes() {
		List<String> editorTypes = new ArrayList<String>();
		IExtension extensions[] = Platform.getExtensionRegistry().getExtensionPoint("org.jboss.tools.common.model.ui.paletteConstraints").getExtensions(); //$NON-NLS-1$
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement elements[] = extensions[i].getConfigurationElements(); 
			for (int j = 0; j < elements.length; j++) 
				if ("natureType".equals(elements[j].getName())) //$NON-NLS-1$
				{
					String editorType = elements[j].getAttribute("id"); //$NON-NLS-1$
					if (editorType != null && editorType.length() > 0)
						editorTypes.add(editorType);
				}
		}
		
		Collections.sort(editorTypes);

		return editorTypes.toArray(new String[editorTypes.size()]);
	}

}
