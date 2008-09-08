package org.jboss.tools.smooks.ui.gef.model;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * �����ݶ��������ģ�ͣ���������԰�����ݶ���
 * @deprecated
 * @author wangpeng 2007-3-21 13:53:30
 *
 */
public class DataObjectContainerModel extends AbstractStructuredDataModel {
	
	/**
	 * Դ��ݶ�������
	 */
	public static final int SOURCE_DATA_OBJECT_CONTAINER = 1;
	/**
	 * Ŀ����ݶ�������
	 */
	public static final int TARGET_DATA_OBJECT_CIBTAUBER = 2;
	
	/**
	 * ��ݶ�����������
	 */
	private String name;
	/**
	 * �����ϵ�λ��
	 */
	private Rectangle constraint;
	/**
	 * ��ݶ����������͡���}�����͵���ݶ�������
	 * Դ��ݶ�������{@link DataObjectContainerModel#SOURCE_DATA_OBJECT_CONTAINER }
	 * ��Ŀ����ݶ�������{@link DataObjectContainerModel#TARGET_DATA_OBJECT_CIBTAUBER }
	 */
	private int category = -1;

	/**
	 * ������ݶ������������
	 * 
	 * @return ��ݶ������������
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * ������ݶ������������
	 * Դ��ݶ�������{@link DataObjectContainerModel#SOURCE_DATA_OBJECT_CONTAINER }
	 * ��Ŀ����ݶ�������{@link DataObjectContainerModel#TARGET_DATA_OBJECT_CIBTAUBER }
	 * 
	 * @param category ��ݶ�����������
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * ���������ڻ����ϵ�λ��
	 * 
	 * @return �����ڻ����ϵ�λ��
	 */
	public Rectangle getConstraint() {
		return constraint;
	}

	/**
	 * ���������ڻ����ϵ�λ��
	 * 
	 * @param constraint �����ڻ����ϵ�λ��
	 */
	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
	}

	/**
	 * ������������
	 * @return ��������
	 */
	public String getName() {
		return name;
	}

	/**
	 * ������������
	 * @param name ��������
	 */
	public void setName(String name) {
		this.name = name;
	}
}