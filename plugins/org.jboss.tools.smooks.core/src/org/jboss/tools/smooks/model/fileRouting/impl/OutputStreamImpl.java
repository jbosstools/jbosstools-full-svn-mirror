/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.fileRouting.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;
import org.jboss.tools.smooks.model.fileRouting.HighWaterMark;
import org.jboss.tools.smooks.model.fileRouting.OutputStream;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Stream</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getFileNamePattern <em>File Name Pattern</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getDestinationDirectoryPattern <em>Destination Directory Pattern</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getListFileNamePattern <em>List File Name Pattern</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getHighWaterMark <em>High Water Mark</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getCloseOnCondition <em>Close On Condition</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getOpenOnElement <em>Open On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getOpenOnElementNS <em>Open On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl#getResourceName <em>Resource Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputStreamImpl extends ElementVisitorImpl implements OutputStream {
	/**
	 * The default value of the '{@link #getFileNamePattern() <em>File Name Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileNamePattern()
	 * @generated
	 * @ordered
	 */
	protected static final String FILE_NAME_PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFileNamePattern() <em>File Name Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileNamePattern()
	 * @generated
	 * @ordered
	 */
	protected String fileNamePattern = FILE_NAME_PATTERN_EDEFAULT;

	/**
	 * The default value of the '{@link #getDestinationDirectoryPattern() <em>Destination Directory Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationDirectoryPattern()
	 * @generated
	 * @ordered
	 */
	protected static final String DESTINATION_DIRECTORY_PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDestinationDirectoryPattern() <em>Destination Directory Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationDirectoryPattern()
	 * @generated
	 * @ordered
	 */
	protected String destinationDirectoryPattern = DESTINATION_DIRECTORY_PATTERN_EDEFAULT;

	/**
	 * The default value of the '{@link #getListFileNamePattern() <em>List File Name Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListFileNamePattern()
	 * @generated
	 * @ordered
	 */
	protected static final String LIST_FILE_NAME_PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getListFileNamePattern() <em>List File Name Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListFileNamePattern()
	 * @generated
	 * @ordered
	 */
	protected String listFileNamePattern = LIST_FILE_NAME_PATTERN_EDEFAULT;

	/**
	 * The cached value of the '{@link #getHighWaterMark() <em>High Water Mark</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighWaterMark()
	 * @generated
	 * @ordered
	 */
	protected HighWaterMark highWaterMark;

	/**
	 * The default value of the '{@link #getCloseOnCondition() <em>Close On Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCloseOnCondition()
	 * @generated
	 * @ordered
	 */
	protected static final String CLOSE_ON_CONDITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCloseOnCondition() <em>Close On Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCloseOnCondition()
	 * @generated
	 * @ordered
	 */
	protected String closeOnCondition = CLOSE_ON_CONDITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEncoding()
	 * @generated
	 * @ordered
	 */
	protected static final String ENCODING_EDEFAULT = "UTF-8"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEncoding()
	 * @generated
	 * @ordered
	 */
	protected String encoding = ENCODING_EDEFAULT;

	/**
	 * This is true if the Encoding attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean encodingESet;

	/**
	 * The default value of the '{@link #getOpenOnElement() <em>Open On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String OPEN_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOpenOnElement() <em>Open On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenOnElement()
	 * @generated
	 * @ordered
	 */
	protected String openOnElement = OPEN_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getOpenOnElementNS() <em>Open On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String OPEN_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOpenOnElementNS() <em>Open On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String openOnElementNS = OPEN_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * The default value of the '{@link #getResourceName() <em>Resource Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceName()
	 * @generated
	 * @ordered
	 */
	protected static final String RESOURCE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResourceName() <em>Resource Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceName()
	 * @generated
	 * @ordered
	 */
	protected String resourceName = RESOURCE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OutputStreamImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FileRoutingPackage.Literals.OUTPUT_STREAM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFileNamePattern() {
		return fileNamePattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFileNamePattern(String newFileNamePattern) {
		String oldFileNamePattern = fileNamePattern;
		fileNamePattern = newFileNamePattern;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__FILE_NAME_PATTERN, oldFileNamePattern, fileNamePattern));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDestinationDirectoryPattern() {
		return destinationDirectoryPattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestinationDirectoryPattern(String newDestinationDirectoryPattern) {
		String oldDestinationDirectoryPattern = destinationDirectoryPattern;
		destinationDirectoryPattern = newDestinationDirectoryPattern;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__DESTINATION_DIRECTORY_PATTERN, oldDestinationDirectoryPattern, destinationDirectoryPattern));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getListFileNamePattern() {
		return listFileNamePattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setListFileNamePattern(String newListFileNamePattern) {
		String oldListFileNamePattern = listFileNamePattern;
		listFileNamePattern = newListFileNamePattern;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__LIST_FILE_NAME_PATTERN, oldListFileNamePattern, listFileNamePattern));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighWaterMark getHighWaterMark() {
		return highWaterMark;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHighWaterMark(HighWaterMark newHighWaterMark, NotificationChain msgs) {
		HighWaterMark oldHighWaterMark = highWaterMark;
		highWaterMark = newHighWaterMark;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK, oldHighWaterMark, newHighWaterMark);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHighWaterMark(HighWaterMark newHighWaterMark) {
		if (newHighWaterMark != highWaterMark) {
			NotificationChain msgs = null;
			if (highWaterMark != null)
				msgs = ((InternalEObject)highWaterMark).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK, null, msgs);
			if (newHighWaterMark != null)
				msgs = ((InternalEObject)newHighWaterMark).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK, null, msgs);
			msgs = basicSetHighWaterMark(newHighWaterMark, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK, newHighWaterMark, newHighWaterMark));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCloseOnCondition() {
		return closeOnCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCloseOnCondition(String newCloseOnCondition) {
		String oldCloseOnCondition = closeOnCondition;
		closeOnCondition = newCloseOnCondition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__CLOSE_ON_CONDITION, oldCloseOnCondition, closeOnCondition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEncoding(String newEncoding) {
		String oldEncoding = encoding;
		encoding = newEncoding;
		boolean oldEncodingESet = encodingESet;
		encodingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__ENCODING, oldEncoding, encoding, !oldEncodingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEncoding() {
		String oldEncoding = encoding;
		boolean oldEncodingESet = encodingESet;
		encoding = ENCODING_EDEFAULT;
		encodingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FileRoutingPackage.OUTPUT_STREAM__ENCODING, oldEncoding, ENCODING_EDEFAULT, oldEncodingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEncoding() {
		return encodingESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOpenOnElement() {
		return openOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOpenOnElement(String newOpenOnElement) {
		String oldOpenOnElement = openOnElement;
		openOnElement = newOpenOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT, oldOpenOnElement, openOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOpenOnElementNS() {
		return openOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOpenOnElementNS(String newOpenOnElementNS) {
		String oldOpenOnElementNS = openOnElementNS;
		openOnElementNS = newOpenOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT_NS, oldOpenOnElementNS, openOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResourceName(String newResourceName) {
		String oldResourceName = resourceName;
		resourceName = newResourceName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FileRoutingPackage.OUTPUT_STREAM__RESOURCE_NAME, oldResourceName, resourceName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK:
				return basicSetHighWaterMark(null, msgs);
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
			case FileRoutingPackage.OUTPUT_STREAM__FILE_NAME_PATTERN:
				return getFileNamePattern();
			case FileRoutingPackage.OUTPUT_STREAM__DESTINATION_DIRECTORY_PATTERN:
				return getDestinationDirectoryPattern();
			case FileRoutingPackage.OUTPUT_STREAM__LIST_FILE_NAME_PATTERN:
				return getListFileNamePattern();
			case FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK:
				return getHighWaterMark();
			case FileRoutingPackage.OUTPUT_STREAM__CLOSE_ON_CONDITION:
				return getCloseOnCondition();
			case FileRoutingPackage.OUTPUT_STREAM__ENCODING:
				return getEncoding();
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT:
				return getOpenOnElement();
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT_NS:
				return getOpenOnElementNS();
			case FileRoutingPackage.OUTPUT_STREAM__RESOURCE_NAME:
				return getResourceName();
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
			case FileRoutingPackage.OUTPUT_STREAM__FILE_NAME_PATTERN:
				setFileNamePattern((String)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__DESTINATION_DIRECTORY_PATTERN:
				setDestinationDirectoryPattern((String)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__LIST_FILE_NAME_PATTERN:
				setListFileNamePattern((String)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK:
				setHighWaterMark((HighWaterMark)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__CLOSE_ON_CONDITION:
				setCloseOnCondition((String)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__ENCODING:
				setEncoding((String)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT:
				setOpenOnElement((String)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT_NS:
				setOpenOnElementNS((String)newValue);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__RESOURCE_NAME:
				setResourceName((String)newValue);
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
			case FileRoutingPackage.OUTPUT_STREAM__FILE_NAME_PATTERN:
				setFileNamePattern(FILE_NAME_PATTERN_EDEFAULT);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__DESTINATION_DIRECTORY_PATTERN:
				setDestinationDirectoryPattern(DESTINATION_DIRECTORY_PATTERN_EDEFAULT);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__LIST_FILE_NAME_PATTERN:
				setListFileNamePattern(LIST_FILE_NAME_PATTERN_EDEFAULT);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK:
				setHighWaterMark((HighWaterMark)null);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__CLOSE_ON_CONDITION:
				setCloseOnCondition(CLOSE_ON_CONDITION_EDEFAULT);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__ENCODING:
				unsetEncoding();
				return;
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT:
				setOpenOnElement(OPEN_ON_ELEMENT_EDEFAULT);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT_NS:
				setOpenOnElementNS(OPEN_ON_ELEMENT_NS_EDEFAULT);
				return;
			case FileRoutingPackage.OUTPUT_STREAM__RESOURCE_NAME:
				setResourceName(RESOURCE_NAME_EDEFAULT);
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
			case FileRoutingPackage.OUTPUT_STREAM__FILE_NAME_PATTERN:
				return FILE_NAME_PATTERN_EDEFAULT == null ? fileNamePattern != null : !FILE_NAME_PATTERN_EDEFAULT.equals(fileNamePattern);
			case FileRoutingPackage.OUTPUT_STREAM__DESTINATION_DIRECTORY_PATTERN:
				return DESTINATION_DIRECTORY_PATTERN_EDEFAULT == null ? destinationDirectoryPattern != null : !DESTINATION_DIRECTORY_PATTERN_EDEFAULT.equals(destinationDirectoryPattern);
			case FileRoutingPackage.OUTPUT_STREAM__LIST_FILE_NAME_PATTERN:
				return LIST_FILE_NAME_PATTERN_EDEFAULT == null ? listFileNamePattern != null : !LIST_FILE_NAME_PATTERN_EDEFAULT.equals(listFileNamePattern);
			case FileRoutingPackage.OUTPUT_STREAM__HIGH_WATER_MARK:
				return highWaterMark != null;
			case FileRoutingPackage.OUTPUT_STREAM__CLOSE_ON_CONDITION:
				return CLOSE_ON_CONDITION_EDEFAULT == null ? closeOnCondition != null : !CLOSE_ON_CONDITION_EDEFAULT.equals(closeOnCondition);
			case FileRoutingPackage.OUTPUT_STREAM__ENCODING:
				return isSetEncoding();
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT:
				return OPEN_ON_ELEMENT_EDEFAULT == null ? openOnElement != null : !OPEN_ON_ELEMENT_EDEFAULT.equals(openOnElement);
			case FileRoutingPackage.OUTPUT_STREAM__OPEN_ON_ELEMENT_NS:
				return OPEN_ON_ELEMENT_NS_EDEFAULT == null ? openOnElementNS != null : !OPEN_ON_ELEMENT_NS_EDEFAULT.equals(openOnElementNS);
			case FileRoutingPackage.OUTPUT_STREAM__RESOURCE_NAME:
				return RESOURCE_NAME_EDEFAULT == null ? resourceName != null : !RESOURCE_NAME_EDEFAULT.equals(resourceName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (fileNamePattern: "); //$NON-NLS-1$
		result.append(fileNamePattern);
		result.append(", destinationDirectoryPattern: "); //$NON-NLS-1$
		result.append(destinationDirectoryPattern);
		result.append(", listFileNamePattern: "); //$NON-NLS-1$
		result.append(listFileNamePattern);
		result.append(", closeOnCondition: "); //$NON-NLS-1$
		result.append(closeOnCondition);
		result.append(", encoding: "); //$NON-NLS-1$
		if (encodingESet) result.append(encoding); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", openOnElement: "); //$NON-NLS-1$
		result.append(openOnElement);
		result.append(", openOnElementNS: "); //$NON-NLS-1$
		result.append(openOnElementNS);
		result.append(", resourceName: "); //$NON-NLS-1$
		result.append(resourceName);
		result.append(')');
		return result.toString();
	}

} //OutputStreamImpl
