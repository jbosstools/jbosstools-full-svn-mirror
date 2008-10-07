package org.jboss.tools.smooks.ui.gef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * 
 * @author Dart Peng
 * 
 */
public abstract class AbstractStructuredDataModel implements IPropertySource,
		IAdaptable, IGraphicalModel {
	
	public static final String P_REFRESH_PANEL = "__panel_is_changed";
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	private int x = 0;
	private int y = 0;
	private int widht = 0;
	private Rectangle constraint = null;
	private String labelName;

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	protected Object referenceEntityModel = null;

	/**
	 * @return the referenceEntityModel
	 */
	public Object getReferenceEntityModel() {
		return referenceEntityModel;
	}

	/**
	 * @param referenceEntityModel
	 *            the referenceEntityModel to set
	 */
	public void setReferenceEntityModel(Object referenceEntityModel) {
		this.referenceEntityModel = referenceEntityModel;
	}

	public static final String P_BOUNDS_CHANGE = "__property_bounds_changed";

	public Rectangle getConstraint() {
		return constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.ui.gef.model.IGraphicalModel#setConstraint(org
	 *      .eclipse.draw2d.geometry.Rectangle)
	 */
	public void setConstraint(Rectangle constraint) {
		if (constraint != null && !constraint.equals(this.constraint)) {
			Rectangle old = this.constraint;
			this.constraint = constraint;
			firePropertyChange(P_BOUNDS_CHANGE, old, this.constraint);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return widht;
	}

	public void setWidht(int widht) {
		this.widht = widht;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private int height = 0;
	public static final String P_CHILDREN = "_children";
	boolean isLeft = true;
	String typeString;

	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	/**
	 */
	protected List children = new ArrayList();

	private AbstractStructuredDataModel parent = null;

	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public void firePropertyChange(String propName, Object oldValue,
			Object newValue) {
		listeners.firePropertyChange(propName, oldValue, newValue);
	}

	public List getChildren() {
		return children;
	}
	public void addChild(Object child) {
		if (child == null)
			return;
		if (children == null) {
			children = new ArrayList();
		}

		children.add(child);
		if (child instanceof AbstractStructuredDataModel) {
			((AbstractStructuredDataModel) child).setParent(this);
			setLeftValueToChild(((AbstractStructuredDataModel) child));
		}
		firePropertyChange(P_CHILDREN, null, child);
	}

	public void addChildrenList(List chlildren) {
		if (children == null)
			return;
		if (children == null) {
			children = new ArrayList();
		}

		children.addAll(chlildren);
		for (Iterator iterator = chlildren.iterator(); iterator.hasNext();) {
			Object child = (Object) iterator.next();
			if (child instanceof AbstractStructuredDataModel) {
				((AbstractStructuredDataModel) child).setParent(this);
				setLeftValueToChild(((AbstractStructuredDataModel) child));
			}
		}
		firePropertyChange(P_CHILDREN, null, chlildren);
	}

	protected void setLeftValueToChild(AbstractStructuredDataModel child) {
		child.setLeft(this.isLeft);
	}
	
	
	public void removeChildrenList(Collection children){
		if(this.children != null && children != null){
			this.children.removeAll(children);
			firePropertyChange(P_CHILDREN, children, null);
		}
	}

	/**
	 */
	public void removeChild(Object child) {
		if (child == null || children == null)
			return;
		children.remove(child);
		if (child instanceof AbstractStructuredDataModel) {
			((AbstractStructuredDataModel) child).setParent(null);
			// ((AbstractModel)child).setLeft(this.isLeft);
		}
		firePropertyChange(P_CHILDREN, child, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {

		return new IPropertyDescriptor[0];
	}

	public Object getPropertyValue(Object id) {
		return null;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {

	}

	public void setPropertyValue(Object id, Object value) {
	}

	public AbstractStructuredDataModel getParent() {
		return parent;
	}

	public void setParent(AbstractStructuredDataModel parent) {
		this.parent = parent;
	}
}