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

public interface IClayElementList<T> extends IClayElement {
   public Vector<T> getElements();

   public void moveTo(T object, int index);
   public void moveUp(T object);
   public void moveDown(T object);

   public IClayElement findElement(Comparator comparator);
   public IClayElementList findElements(Comparator comparator);

   public int size();
   public boolean isEmpty();
   public boolean contains(T o);
   public Iterator<T> iterator();
   public Object[] toArray();
   public T[] toArray(T a[]);
   public boolean add(T o);
   public void add(IClayElementList<T> list);
   public void add(int index, T element);
   public boolean remove(T o);
   public void remove(Comparator comparator);
//   public Object remove(int index) throws VetoException;
   public void removeAll();
   public T get(int index);
   public int indexOf(T o);
   public void addClayElementListListener(IClayElementListListener l);
   public void removeClayElementListListener(IClayElementListListener l);
}

