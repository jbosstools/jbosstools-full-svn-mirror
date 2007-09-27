/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


package org.hibernate.netbeans.console.util;

import java.util.List;
import javax.swing.AbstractListModel;

public class DelegatingListModel<T> extends AbstractListModel {

    private List<T> delegate;
    
    public DelegatingListModel(List<T> delegate) {
        this.delegate = delegate;
    }

    public int getSize() {
        return delegate.size();
    }

    public T getElementAt(int i) {
        return delegate.get(i);
    }
    
    public void refresh() {
        fireContentsChanged(this, 0, delegate.size());
    }
    
    public void addElements(T[] elements) {
        int added = 0;
        int oldSize = delegate.size();
        for (int i = 0; i < elements.length; i++) {
            T element = elements[i];
            if (delegate.contains(element)) {
                continue;
            }
            delegate.add(element);
            added++;
        }
        if (added != 0) {
            fireIntervalAdded(this, oldSize + 1, oldSize + added);
        }
    }
    
    public void moveElementsDown(int[] indices) {
        for (int i = 0; i < indices.length; i++) {
            T after = delegate.get(indices[i] + 1);
            delegate.set(indices[i] + 1, delegate.get(indices[i]));
            delegate.set(indices[i], after);
            indices[i]++;
        }
    }

    public void moveElementsUp(int[] indices) {
        for (int i = 0; i < indices.length; i++) {
            T before = delegate.get(indices[i] - 1);
            delegate.set(indices[i] - 1, delegate.get(indices[i]));
            delegate.set(indices[i], before);
            indices[i]--;
        }
    }
    
    public void removeElements(int[] indices) {
        int removed = 0;
        for (int i = 0; i < indices.length; i++) {
            delegate.remove(indices[i] - removed);
            fireIntervalRemoved(this, indices[i] - removed, indices[i] - removed);
            removed++;
        }
    }
}