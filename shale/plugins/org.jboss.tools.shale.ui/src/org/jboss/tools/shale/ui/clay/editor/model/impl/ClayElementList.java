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
package org.jboss.tools.shale.ui.clay.editor.model.impl;

import java.beans.*;
import java.util.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.shale.ui.clay.editor.model.*;

public class ClayElementList<T> extends ClayElement implements IClayElementList<T> {
	Vector<IClayElementListListener> listeners = new Vector<IClayElementListListener>();
	Vector<T> elements = new Vector<T>();
	boolean elementListListenerEnable = true;
	boolean allowDuplicate = false;

	public ClayElementList() {}

	public ClayElementList(IClayElement parent) {
		super(parent);
	}

	public ClayElementList(IClayElement parent,XModelObject source) {
		super(parent,source);
	}

	public Vector<T> getElements(){
		return elements;
	}

	public ClayElementList(Vector<T> vector) {
		elements = vector;
	}
	   
	public void dispose() {
		super.dispose();
		if (listeners != null) listeners.clear();
		listeners = null;
		if (elements != null) elements.clear();
		elements = null;
	}

	public void setAllowDuplicate(boolean set) {
		allowDuplicate = set;
	}

	   public boolean isAllowDuplicate() {
		return allowDuplicate;
	}

	public void moveTo(T object, int index) {
		int currentIndex = indexOf(object);
		if (index < 0 || index >= size())
			return;
		if (currentIndex > index) { // move down
			for (int i = currentIndex - 1; i >= index; i--) {
				T elementAt = get(i);
				set(i + 1, elementAt);
			}
			set(index, object);
			this.fireElementMoved((IClayElement) object, index, currentIndex);
		} else if (currentIndex < index) { // move up
			for (int i = currentIndex + 1; i <= index; i++) {
				T elementAt = get(i);
				set(i - 1, elementAt);
			}
			set(index, object);
			this.fireElementMoved((IClayElement) object, index, currentIndex);
		}
	}

	public void moveUp(T object) {
		int currentIndex = indexOf(object);
		if (currentIndex == 0)
			return;
		set(currentIndex, get(currentIndex - 1));
		set(currentIndex - 1, object);
		this.fireElementMoved((IClayElement) object, currentIndex - 1,
				currentIndex);
	}

	public void moveDown(T object) {
		int currentIndex = indexOf(object);
		if (currentIndex == size())
			return;
		set(currentIndex, get(currentIndex + 1));
		set(currentIndex + 1, object);
		this.fireElementMoved((IClayElement) object, currentIndex + 1,
				currentIndex);
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public boolean contains(T o) {
		return elements.contains(o);
	}

	public Iterator<T> iterator() {
		return elements.iterator();
	}

	public Object[] toArray() {
		return elements.toArray();
	}

	public T[] toArray(T a[]) {
		return elements.toArray(a);
	}

	public boolean add(T o){
		if(!(o instanceof IClayElement)) return false;
		boolean result = elements.add(o);
        // this.fireElementAdded( (IJSFElement) o, index);
		return result;
	}

	public void add(IClayElementList<T> list){
		for(int i = 0 ; i < list.size(); i++) {
			T o = list.get(i);
			if(!(o instanceof IClayElement)) continue;
			add(o);
			//this.fireElementAdded((IJSFElement)list.get(i),elements.size()-1);
		}
	}

	public boolean remove(Object o){
//		int index = indexOf(o);
		boolean result = elements.remove(o);
//		((IStrutsElement)o).removeVetoableChangeListener(this);
//		fireElementRemoved((IJSFElement)o,index);
		return result;
	}

	public void remove(Comparator comp){
		for(int i = size() - 1; i >= 0; i--) {
			if(comp.equals(get(i))) {
				remove(get(i));
			};
		}
	}

	public void removeAll() {
		for(int i = size() - 1; i >= 0; i--) {
			remove(get(i));
		}
	}

	public T get(int index) {
		return elements.get(index);
	}

	public Object get(String name) {
		if(name == null) return null;
		for(int i = 0; i < elements.size(); i++) {
			ClayElement element = (ClayElement)elements.get(i);
			if(name.equals(element.getName())) return element;
		}
		return null;
	}

	public T set(int index, T element) {
//		int oldIndex = elements.indexOf(element);
		T newElement = elements.set(index,element);
		return newElement;
	}

	public void add(int index, T element){
		elements.add(index, element);
		//this.fireElementAdded((IJSFElement)element,index);
	}

	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	public IClayElement findElement(Comparator comparator) {
		return null;
	}

	public IClayElementList findElements(Comparator comparator) {
		return null;
	}

	public String getText() {
		return toString();
	}

	public Object clone() {
		return getClone();
	}

	public ClayElementList<T> getClone() {
		Vector<T> newVector = new Vector<T>();
		synchronized (elements) {
			for (int i = 0; i < elements.size(); i++) {
				newVector.add(elements.get(i));
			}
		}
		return new ClayElementList<T>(newVector);
	}

	public void vetoableChange(PropertyChangeEvent evt)
		throws PropertyVetoException {
	}

	public void addClayElementListListener(IClayElementListListener l) {
		listeners.add(l);
	}

	public void removeClayElementListListener(IClayElementListListener l) {
		listeners.remove(l);
	}

	protected void fireElementMoved(IClayElement element,int newIndex,int oldIndex){
		for(int i = 0; i < listeners.size(); i++) {
			IClayElementListListener listener = (IClayElementListListener)listeners.get(i);
			if(listener != null && listener.isElementListListenerEnable())
				listener.listElementMove(this,element,newIndex,oldIndex);
		}
		//((ClayModel)getClayModel()).fireElementRemoved(element,oldIndex);
		//((ClayModel)getClayModel()).fireElementInserted(element);
	}

	protected void fireElementAdded(IClayElement element,int index){
		for(int i = 0; i < listeners.size(); i++) {
			IClayElementListListener listener = (IClayElementListListener)listeners.get(i);
			if(listener != null && listener.isElementListListenerEnable())
				listener.listElementAdd(this,element,index);
		}
	}

	protected void fireElementRemoved(IClayElement element,int index) {
		for(int i = 0; i < listeners.size(); i++) {
			IClayElementListListener listener = (IClayElementListListener)listeners.get(i);
			if(listener != null && listener.isElementListListenerEnable())
				listener.listElementRemove(this,element,index);
		}
	}

	protected void fireElementChanged(IClayElement element,int index,PropertyChangeEvent event) {
		for(int i = 0; i < listeners.size(); i++) {
			IClayElementListListener listener = (IClayElementListListener)listeners.get(i);
			if(listener != null && listener.isElementListListenerEnable())
				listener.listElementChange(this,element,index,event);
		}
	}

	public void remove(int index) {
		Object obj = elements.get(index);
		elements.remove(obj);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

class Comp implements Comparator {
	String name;
	public Comp(String name) {
		this.name = name;
	}

	public int compare(Object o1,Object o2) {
		return 0;
	}

	public boolean equals(Object obj) {
		if(obj instanceof IClayElement) {
			IClayElement element = (IClayElement)obj;
			return element.getName().equals(name);
		}
		return false;
	}
}

class ElementNameComparator implements Comparator {
	   String elementName;

	public ElementNameComparator(String elementName) {
		this.elementName = elementName;
	}

	public void setName(String name) {
		elementName = name;
	}

	public boolean equals(Object object) {
		if (object instanceof ClayElement) {
			ClayElement unit = (ClayElement) object;
			return unit.getName().equals(elementName);
		}
		return false;
	}

	public int compare(Object obj1, Object obj2) {
		return 0;
	}

}
