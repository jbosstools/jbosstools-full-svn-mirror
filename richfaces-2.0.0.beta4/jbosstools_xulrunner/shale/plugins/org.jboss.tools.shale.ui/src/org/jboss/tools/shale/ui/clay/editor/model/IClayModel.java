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

import java.util.Vector;
import org.jboss.tools.shale.model.clay.helpers.ClayStructureHelper;

public interface IClayModel extends IClayElement {	
	public ClayStructureHelper getHelper();

	public void updateLinks();

	public IComponent getSelectedComponent();
	public void  setSelectedComponent(IComponent component);

	public IClayElement findElement(String key);
	public IComponent getComponent(String name);
	public IComponent getComponent(Object source);
	public IClayElementList<IComponent> getComponentList();
   
	public Vector<IComponent> getVisibleComponentList();
	public void setHidden(IComponent component);
	public void setVisible(IComponent component);

	public void setData(Object object) throws Exception;
   
	public boolean isModified();
	public void setModified(boolean set);

	public void addClayModelListener(IClayModelListener listener);
	public void removeClayModelListener(IClayModelListener listener);

	public boolean isEditable();
	public IClayOptions getOptions();
}
