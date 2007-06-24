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

import java.beans.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.*;

public interface IClayElement {
	   public static String NAME_PROPERTY = "name";
	   public static String TARGET_PROPERTY = "extends";

	   public static Point DEFAULT_POINT = new Point(50,50);

	   public IClayElement getRoot();
	   public IClayModel getClayModel();

	   public Object getSource();
	   public void setSource(Object object);

	   public IClayElement getParentClayElement();
	   public void setParentClayElement(IClayElement parent);

	   public String getName();
	   public void setName(String name) throws PropertyVetoException ;

	   public void remove();
	   public String getJSFElementPath();

	   public void setSourceProperty(String name, Object value);

	   public Object getSourceProperty(String name);
	   public Object getSourceProperty(int index);
	   public int getSourcePropertyCounter();
	   public String[] getSourcePropertyNames();
	   public String[] getSourcePropertyDisplayNames();
	   public Menu getPopupMenu(Control control, Object environment);
	   public Menu getPopupMenu(Control control);

	   public void addPropertyChangeListener(PropertyChangeListener l);
	   public void removePropertyChangeListener(PropertyChangeListener l);
	   public void addPropertyChangeListener(String propertyName, PropertyChangeListener l);
	   public void removePropertyChangeListener(String propertyName, PropertyChangeListener l);

	   public String getText();
	   public Object clone();

	   public void structureChanged(Object eventData);
	   public void nodeChanged(Object eventData);
	   public void nodeAdded(Object eventData);
	   public void nodeRemoved(Object eventData);

	   public boolean isConfirmed();

}
