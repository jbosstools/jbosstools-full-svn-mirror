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
package org.jboss.tools.shale.model.clay;

import org.jboss.tools.common.model.options.Preference;
import org.jboss.tools.jst.web.WebPreference;

public class ClayPreference extends WebPreference {

	public static final String CLAY_DIAGRAM_PATH   = "%Options%/Struts Studio/Editors/Tiles Diagram";
	public static final Preference VERTICAL_SPACING = new ClayPreference(CLAY_DIAGRAM_PATH, "verticalSpacing");
	public static final Preference HORIZONTAL_SPACING = new ClayPreference(CLAY_DIAGRAM_PATH, "horizontalSpacing");
	public static final Preference ALIGNMENT = new ClayPreference(CLAY_DIAGRAM_PATH, "alignment");
	public static final Preference COMPONENT_NAME_FONT = new ClayPreference(CLAY_DIAGRAM_PATH, "definitionNameFont");
	public static final Preference ANIMATION = new ClayPreference(CLAY_DIAGRAM_PATH, "animation");

	protected ClayPreference(String optionPath, String attributeName)	{
		super(optionPath, attributeName);
	}

}
