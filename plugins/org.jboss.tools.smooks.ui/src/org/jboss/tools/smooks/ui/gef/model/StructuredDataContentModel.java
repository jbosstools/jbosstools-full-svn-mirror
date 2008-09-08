package org.jboss.tools.smooks.ui.gef.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * ��ݶ��������ڽ����е�ģ�͡�
 * <p>
 * ��ģ�Ϳ���������ݶ����еļ򵥺͸������ԣ���(���Ե����֡�Java���͵���Ϣ������������û��Java�
 * ��͵ġ�<br>
 * ���ң����ڼ������ͣ�ֻ֧�����顣������Ϊֻ���������ȷ�е�������Ϣ�������JDK1.4�����������
 * �List�ȼ������ͣ�<br>
 * ��Ϊû��ȷ�е����ͣ����Բ���֧�֡�<br>
 * </p>
 * 
 * @author wangpeng 2007-3-21 15:09:11
 */
public class StructuredDataContentModel extends AbstractStructuredDataModel
		implements IConnectableModel {

	public static final int DATA_OBJECT_NODE_CATEGORY_SOURCE = 1;
	public static final int DATA_OBJECT_NODE_CATEGORY_TARGET = 2;

	/**
	 * �ڵ��� - ������ݶ������Ե�����
	 */
	private String name;

	/**
	 * �ڵ����� - ���Ǹ����Ե�Java���͡��������Ҫ������ȫ������
	 * <code>java.lang.String</code> ���������Ǹ������ͣ�������Ϊ<code>null</code>
	 * ��
	 */
	private String javaType;

	/**
	 * �ڵ�����������Ǽ����ͻ��Ǹ������͡�complexTypeΪ<code>true</code>
	 * ����ʾ�ڵ��Ǹ������ͣ� complexTypeΪ<code>false</code>����ʾ�ڵ��Ǽ����͡�
	 */
	private boolean isComplexType;

	/**
	 * ����ڵ��Ǹ�ڵ㻹����ͨ�ڵ�
	 */
	private boolean isRootNode;

	/**
	 * ����ڵ��Ƿ�Ϊ��������
	 */
	private boolean isListType;

	/**
	 * ����ڵ��Ƿ�Ϊ��������
	 */
	private boolean isObject;

	/**
	 * �����ϵ�λ��
	 */
	private Rectangle constraint;

	/**
	 * ��ݶ���ڵ�ķ��࣬Ŀǰ�ķ�����Դ��ݶ���ڵ��Ŀ����ݶ���ڵ�
	 * 
	 * @see #DATA_OBJECT_NODE_CATEGORY_SOURCE
	 * @see #DATA_OBJECT_NODE_CATEGORY_TARGET
	 */
	private int nodeCategory = -1;

	/**
	 * Դl�Ӽ�
	 */
	private List sourceConnections = new ArrayList();

	/**
	 * Ŀ��l�Ӽ�
	 */
	private List targetConnections = new ArrayList();

	/**
	 * ����ģ���ڻ����ϵ�λ��
	 * 
	 * @return ģ���ڻ����ϵ�λ��
	 */
	public Rectangle getConstraint() {
		return constraint;
	}

	/**
	 * ����ģ���ڻ����ϵ�λ��
	 * 
	 * @param constraint
	 *            �����ϵ�λ��
	 */
	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
	}

	/**
	 * �жϽڵ��Ǹ������ͻ��Ǽ����͡�<code>true</code>����ʾ�ڵ��Ǹ������ͣ�
	 * <code>false</code>����ʾ�ڵ��Ǽ����͡�
	 * 
	 * @return �ڵ��Ǹ������ͻ��Ǽ����͡�
	 */
	public boolean isComplexType() {
		return isComplexType;
	}

	/**
	 * ���ýڵ��Ǽ����ͻ��Ǹ������͡�<code>true</code>����ʾ�ڵ��Ǹ������ͣ�
	 * <code>false</code>����ʾ�ڵ��Ǽ����͡�
	 * 
	 * @param complexType
	 *            �ڵ��Ǹ��ӽڵ㻹�Ǽ򵥽ڵ㡣
	 */
	public void setComplexType(boolean complexType) {
		this.isComplexType = complexType;
	}



	/**
	 * ���ؽڵ���
	 * 
	 * @return �ڵ���
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���ýڵ���
	 * 
	 * @param name
	 *            �ڵ���
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean isRootNode() {
		return isRootNode;
	}

	public void setRootNode(boolean root) {
		this.isRootNode = root;
	}

	public boolean isListType() {
		return isListType;
	}

	public void setListType(boolean array) {
		this.isListType = array;
	}

	/**
	 * @return ���� isObject��
	 */
	public boolean isObject() {
		return isObject;
	}

	/**
	 * @param isObject
	 *            Ҫ���õ� isObject��
	 */
	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

	/**
	 * @return ���� nodeCategory��
	 */
	public int getNodeCategory() {
		return nodeCategory;
	}

	/**
	 * @param nodeCategory
	 *            Ҫ���õ� nodeCategory��
	 */
	public void setNodeCategory(int nodeCategory) {
		this.nodeCategory = nodeCategory;
	}

	/**
	 * ���Դl��
	 * 
	 * @param connx
	 */
	public void addSourceConnection(Object connx) {
		sourceConnections.add(connx);

		firePropertyChange(P_SOURCE_CONNECTION, null, connx);
	}

	/**
	 * ���Ŀ��l��
	 * 
	 * @param connx
	 */
	public void addTargetConnection(Object connx) {
		targetConnections.add(connx);

		firePropertyChange(P_TARGET_CONNECTION, null, connx);
	}

	/**
	 * ����Դl��ģ��
	 * 
	 * @return Դl��ģ��
	 */
	public List getModelSourceConnections() {
		return sourceConnections;
	}

	/**
	 * ����Ŀ��l��ģ��
	 * 
	 * @return Ŀ��l��ģ��
	 */
	public List getModelTargetConnections() {
		return targetConnections;
	}

	/**
	 * �Ƴ�Դl��
	 * 
	 * @param connx
	 */
	public void removeSourceConnection(Object connx) {
		sourceConnections.remove(connx);

		firePropertyChange(P_SOURCE_CONNECTION, connx, null);
	}

	/**
	 * �Ƴ�Ŀ��l��
	 * 
	 * @param connx
	 */
	public void removeTargetConnection(Object connx) {
		targetConnections.remove(connx);

		firePropertyChange(P_TARGET_CONNECTION, connx, null);
	}

	/**
	 * �Ƚ�ͬһ�����ϵ�}��ڵ��Ƿ���ͬ
	 */
	public boolean equals(Object node) {
		return super.equals(node);
		// // ��node����DataObjectNodeModel��ʵ��ؼ�
		// //
		// if ( !(node instanceof StructuredDataContentModel) ) {
		// return false;
		// }
		//
		// StructuredDataContentModel nodeModel =
		// (StructuredDataContentModel)node;
		//
		// if ( nodeModel.getName().equals(this.getName())
		// && nodeModel.isListType() == this.isListType()
		// && nodeModel.isComplexType() == this.isComplexType()
		// && nodeModel.getNodeCategory() == this.getNodeCategory()
		// && (
		// // ��˫�����и����Ҹ�����ͬ������˫����û�и������Ϊ�棬����Ϊ��
		// //
		// (nodeModel.getParent() != null && this.getParent() != null) &&
		// nodeModel.getParent().equals(this.getParent())
		// || (nodeModel.getParent() == null && this.getParent() == null)
		// ) ) {
		// return true;
		// }
		//
		// return false;
	}
}