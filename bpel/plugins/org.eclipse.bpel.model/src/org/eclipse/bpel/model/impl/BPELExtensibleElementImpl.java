/**
 * <copyright>
 * </copyright>
 *
 * $Id: BPELExtensibleElementImpl.java,v 1.2 2011/04/05 17:24:30 rbrodt Exp $
 */
package org.eclipse.bpel.model.impl;

import org.eclipse.bpel.model.BPELExtensibleElement;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Documentation;
import org.eclipse.bpel.model.util.ReconciliationHelper;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extensible Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpel.model.impl.BPELExtensibleElementImpl#getDocumentation <em>Documentation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BPELExtensibleElementImpl extends ExtensibleElementImpl implements
		BPELExtensibleElement {
	/**
	 * The cached value of the '{@link #getDocumentation() <em>Documentation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDocumentation()
	 * @generated
	 * @ordered
	 */
	protected Documentation documentation;

	/**
	 * This is true if the Documentation containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean documentationESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BPELExtensibleElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BPELPackage.Literals.BPEL_EXTENSIBLE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Documentation getDocumentation() {
		return documentation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDocumentation(
			Documentation newDocumentation, NotificationChain msgs) {
		Documentation oldDocumentation = documentation;
		documentation = newDocumentation;
		boolean oldDocumentationESet = documentationESet;
		documentationESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this,
					Notification.SET,
					BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION,
					oldDocumentation, newDocumentation, !oldDocumentationESet);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocumentation(Documentation newDocumentation) {
		if (newDocumentation != documentation) {
			NotificationChain msgs = null;
			if (documentation != null)
				msgs = ((InternalEObject) documentation)
						.eInverseRemove(
								this,
								EOPPOSITE_FEATURE_BASE
										- BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION,
								null, msgs);
			if (newDocumentation != null)
				msgs = ((InternalEObject) newDocumentation)
						.eInverseAdd(
								this,
								EOPPOSITE_FEATURE_BASE
										- BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION,
								null, msgs);
			msgs = basicSetDocumentation(newDocumentation, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else {
			boolean oldDocumentationESet = documentationESet;
			documentationESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION,
						newDocumentation, newDocumentation,
						!oldDocumentationESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetDocumentation(NotificationChain msgs) {
		Documentation oldDocumentation = documentation;
		documentation = null;
		boolean oldDocumentationESet = documentationESet;
		documentationESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this,
					Notification.UNSET,
					BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION,
					oldDocumentation, null, oldDocumentationESet);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDocumentation() {
		if (documentation != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject) documentation)
					.eInverseRemove(
							this,
							EOPPOSITE_FEATURE_BASE
									- BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION,
							null, msgs);
			msgs = basicUnsetDocumentation(msgs);
			if (msgs != null)
				msgs.dispatch();
		} else {
			boolean oldDocumentationESet = documentationESet;
			documentationESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET,
						BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION,
						null, null, oldDocumentationESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDocumentation() {
		return documentationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION:
			return basicUnsetDocumentation(msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION:
			return getDocumentation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION:
			setDocumentation((Documentation) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION:
			unsetDocumentation();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case BPELPackage.BPEL_EXTENSIBLE_ELEMENT__DOCUMENTATION:
			return isSetDocumentation();
		}
		return super.eIsSet(featureID);
	}

	// Reconciliation stuff. Has copy in ExtensibilityElement
	// TODO: (DU) remove duplication					
	@Override
	protected void reconcile(Element changedElement) {
		//	    reconcileAttributes(changedElement);
		//	    reconcileContents(changedElement);
		ReconciliationHelper.getInstance().reconcile(this, changedElement);
	}

	@Override
	public void elementChanged(Element changedElement) {
		if (!isUpdatingDOM()) {
			if (!isReconciling) {
				isReconciling = true;
				try {
					reconcile(changedElement);

					WSDLElement theContainer = getContainer();
					if (theContainer != null
							&& theContainer.getElement() == changedElement) {
						((WSDLElementImpl) theContainer)
								.elementChanged(changedElement);
					}
				} finally {
					isReconciling = false;
				}
				traverseToRootForPatching();
			}
		}
	}

	public boolean isReconciling() {
		return this.isReconciling;
	}

	public boolean isUpdatingDOM() {
		return updatingDOM;
	}

	public void setUpdatingDOM(boolean updatingDOM) {
		this.updatingDOM = updatingDOM;
	}

} //BPELExtensibleElementImpl
