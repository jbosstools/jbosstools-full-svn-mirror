/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Abstract Any Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public abstract class AbstractAnyTypeImpl extends AnyTypeImpl implements AbstractAnyType {

	protected List<String> commentsList = new ArrayList<String>();

	protected Map<String, Integer> commentIndexMap = new HashMap<String, Integer>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.model.common.AbstractAnyType#addComment(java.lang
	 * .String, java.lang.Object)
	 */
	public void addComment(String comment, Integer index) {
		commentsList.add(comment);
		setCommentIndex(comment, index);
	}

	/**
	 * @return the commentsList
	 */
	public List<String> getCommentList() {
		return commentsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.model.common.AbstractAnyType#getObjectAfterComment
	 * (java.lang.String)
	 */
	public Integer getCommentIndex(String comment) {
		return commentIndexMap.get(comment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.model.common.AbstractAnyType#setObjectAfterCommecnt
	 * (java.lang.String, java.lang.Object)
	 */
	public void setCommentIndex(String comment, Integer obj) {
		commentIndexMap.put(comment, obj);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AbstractAnyTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommonPackage.Literals.ABSTRACT_ANY_TYPE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getCDATA() {
		return SmooksModelUtils.getAnyTypeCDATA(this);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCDATA(String cdata) {
		SmooksModelUtils.cleanCDATAToSmooksType(this);
		SmooksModelUtils.appendCDATAToSmooksType(this, cdata);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getStringValue() {
		return SmooksModelUtils.getAnyTypeText(this);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStringValue(String stringValue) {
		SmooksModelUtils.cleanTextToSmooksType(this);
		SmooksModelUtils.appendTextToSmooksType(this, stringValue);
	}

} // AbstractAnyTypeImpl
