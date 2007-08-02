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
package org.jboss.tools.shale.ui.clay.editor.model;

import java.util.*;
import org.eclipse.swt.graphics.Image;

public interface IComponent extends IClayElement {
	   public ILink[] getInputLinks();
	   public Vector getListInputLinks();
	   public List getVisibleInputLinks();

	   public void addInputLink(ILink link);
	   public void removeInputLink(ILink link);

	   public ILink getLink();
	   
	   public String getName();
	   public Image getImage();
	   public String getExtends();

	   public void removeFromClayModel();

	   public void addComponentListener(IComponentListener l);
	   public void removeComponentListener(IComponentListener l);

	   public boolean isSelected();
	   public void setSelected(boolean set);
	   public void clearSelection();
	   
	   public boolean isExpanded();
	   public boolean isCollapsed();
	   public boolean isHidden();
	   public void hide();
	   public void visible();
	   public void expand();
	   public void collapse();
	   
	   public boolean isConfirmed();
	   public boolean isAnotherFile();
	   
	   public boolean hasErrors();
}
