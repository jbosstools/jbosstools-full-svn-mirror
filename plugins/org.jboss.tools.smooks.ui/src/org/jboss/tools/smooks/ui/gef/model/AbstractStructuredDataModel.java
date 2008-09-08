package org.jboss.tools.smooks.ui.gef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
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

	/**
	 * ���ýڵ��Java���͡��������Ҫ������ȫ������<code>java.lang.String</code>
	 * ���������Ǹ������ͣ�������Ϊ<code>null</code>��
	 * 
	 * @param typeString
	 *            �ڵ��Java����
	 */
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	/**
	 * ����Ԫ�ض����Ӵ�
	 */
	private List children = new ArrayList();

	private AbstractStructuredDataModel parent = null;

	/**
	 * ��ģ�������ü������ģ�ͷ���ı�ʱ����������Ѹı�֪ͨEditPart��
	 * EditPart��Ҫʵ��PropertyChangeListener�ӿ�
	 * �����������ʱ�����PropertyChangeListener�ӿ��е�peopertyChange����4��ɴ�����
	 */
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	/**
	 * Ϊģ����Ӽ�����
	 * 
	 * @param listener
	 *            PropertyChangeSupport ������
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	/**
	 * ɾ��ģ�͵ļ�����
	 * 
	 * @param listener
	 *            PropertyChangeSupport ������
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * ��ģ�ͷ���仯ʱ��Ҫ��������4����������
	 * 
	 * @param propName
	 *            �ı������
	 * @param oldValue
	 * @param newValue
	 */
	public void firePropertyChange(String propName, Object oldValue,
			Object newValue) {
		listeners.firePropertyChange(propName, oldValue, newValue);
	}

	/**
	 * ���ص�ǰģ�����е���Ԫ�ء����ǰģ��û����Ԫ��
	 * 
	 * @return ��ǰģ�����е���Ԫ��
	 */
	public List getChildren() {
		return children;
	}

	/**
	 * �����Ԫ�أ���֪ͨģ����Ԫ�ط���ı�
	 * 
	 * @param child
	 *            ��Ԫ��
	 */
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

	/**
	 * ɾ����Ԫ�أ���֪ͨģ����Ԫ�ط���ı�
	 * 
	 * @param child
	 *            ��Ԫ��
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

		// ����ģ��������Ϊ�ɱ༭������ֵ
		// 
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {

		// ��Ϊ�ڳ���ģ���з���null������쳣��������ﷵ��һ��0���ȵ�����
		// 
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