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
package org.jboss.tools.jst.web.tiles.ui.editor.model;

import java.util.Comparator;
import java.util.*;

public interface ITilesElementList extends ITilesElement {
   public List<Object> getElements();

   public void moveTo(Object object, int index);
   public void moveUp(Object object);
   public void moveDown(Object object);

   public ITilesElement findElement(Comparator comparator);
   public ITilesElementList findElements(Comparator comparator);

   public int size();
   public boolean isEmpty();
   public boolean contains(Object o);
   public Iterator iterator();
   public Object[] toArray();
   public Object[] toArray(Object a[]);
   public boolean add(Object o);
   public void add(ITilesElementList list);
   public void add(int index, Object element);
   public boolean remove(Object o);
   public void remove(Comparator comparator);
   public void removeAll();
   public Object get(int index);
   public int indexOf(Object o);
   public void addTilesElementListListener(ITilesElementListListener l);
   public void removeTilesElementListListener(ITilesElementListListener l);
}

