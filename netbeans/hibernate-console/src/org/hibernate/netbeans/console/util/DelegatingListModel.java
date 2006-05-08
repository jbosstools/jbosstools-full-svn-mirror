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