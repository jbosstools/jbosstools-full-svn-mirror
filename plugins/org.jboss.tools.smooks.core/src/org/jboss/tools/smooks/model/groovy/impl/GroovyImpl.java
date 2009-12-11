/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.groovy.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jboss.tools.smooks.model.groovy.Groovy;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.groovy.ScriptType;
import org.jboss.tools.smooks.model.smooks.ParamType;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Groovy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link groovy.impl.GroovyImpl#getImports <em>Imports</em>}</li>
 *   <li>{@link groovy.impl.GroovyImpl#getParam <em>Param</em>}</li>
 *   <li>{@link groovy.impl.GroovyImpl#getScript <em>Script</em>}</li>
 *   <li>{@link groovy.impl.GroovyImpl#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link groovy.impl.GroovyImpl#getExecuteOnElement <em>Execute On Element</em>}</li>
 *   <li>{@link groovy.impl.GroovyImpl#getExecuteOnElementNS <em>Execute On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GroovyImpl extends ElementVisitorImpl implements Groovy {
	/**
	 * The default value of the '{@link #getImports() <em>Imports</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImports()
	 * @generated
	 * @ordered
	 */
	protected static final String IMPORTS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getImports() <em>Imports</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImports()
	 * @generated
	 * @ordered
	 */
	protected String imports = IMPORTS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getParam() <em>Param</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParam()
	 * @generated
	 * @ordered
	 */
	protected EList<ParamType> param;

	/**
	 * The cached value of the '{@link #getScript() <em>Script</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScript()
	 * @generated
	 * @ordered
	 */
	protected ScriptType script;

	/**
	 * The default value of the '{@link #isExecuteBefore() <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXECUTE_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExecuteBefore() <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean executeBefore = EXECUTE_BEFORE_EDEFAULT;

	/**
	 * This is true if the Execute Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean executeBeforeESet;

	/**
	 * The default value of the '{@link #getExecuteOnElement() <em>Execute On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecuteOnElement() <em>Execute On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElement()
	 * @generated
	 * @ordered
	 */
	protected String executeOnElement = EXECUTE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getExecuteOnElementNS() <em>Execute On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecuteOnElementNS() <em>Execute On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String executeOnElementNS = EXECUTE_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroovyImpl() {
		super();
		this.setExecuteBefore(false);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GroovyPackage.Literals.GROOVY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getImports() {
		return imports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImports(String newImports) {
		String oldImports = imports;
		imports = newImports;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GroovyPackage.GROOVY__IMPORTS, oldImports, imports));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ParamType> getParam() {
		if (param == null) {
			param = new EObjectContainmentEList<ParamType>(ParamType.class, this, GroovyPackage.GROOVY__PARAM);
		}
		return param;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScriptType getScript() {
		return script;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScript(ScriptType newScript, NotificationChain msgs) {
		ScriptType oldScript = script;
		script = newScript;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GroovyPackage.GROOVY__SCRIPT, oldScript, newScript);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScript(ScriptType newScript) {
		if (newScript != script) {
			NotificationChain msgs = null;
			if (script != null)
				msgs = ((InternalEObject)script).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GroovyPackage.GROOVY__SCRIPT, null, msgs);
			if (newScript != null)
				msgs = ((InternalEObject)newScript).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GroovyPackage.GROOVY__SCRIPT, null, msgs);
			msgs = basicSetScript(newScript, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GroovyPackage.GROOVY__SCRIPT, newScript, newScript));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExecuteBefore() {
		return executeBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteBefore(boolean newExecuteBefore) {
		boolean oldExecuteBefore = executeBefore;
		executeBefore = newExecuteBefore;
		boolean oldExecuteBeforeESet = executeBeforeESet;
		executeBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GroovyPackage.GROOVY__EXECUTE_BEFORE, oldExecuteBefore, executeBefore, !oldExecuteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetExecuteBefore() {
		boolean oldExecuteBefore = executeBefore;
		boolean oldExecuteBeforeESet = executeBeforeESet;
		executeBefore = EXECUTE_BEFORE_EDEFAULT;
		executeBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, GroovyPackage.GROOVY__EXECUTE_BEFORE, oldExecuteBefore, EXECUTE_BEFORE_EDEFAULT, oldExecuteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetExecuteBefore() {
		return executeBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecuteOnElement() {
		return executeOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteOnElement(String newExecuteOnElement) {
		String oldExecuteOnElement = executeOnElement;
		executeOnElement = newExecuteOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT, oldExecuteOnElement, executeOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecuteOnElementNS() {
		return executeOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteOnElementNS(String newExecuteOnElementNS) {
		String oldExecuteOnElementNS = executeOnElementNS;
		executeOnElementNS = newExecuteOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT_NS, oldExecuteOnElementNS, executeOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GroovyPackage.GROOVY__PARAM:
				return ((InternalEList<?>)getParam()).basicRemove(otherEnd, msgs);
			case GroovyPackage.GROOVY__SCRIPT:
				return basicSetScript(null, msgs);
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
			case GroovyPackage.GROOVY__IMPORTS:
				return getImports();
			case GroovyPackage.GROOVY__PARAM:
				return getParam();
			case GroovyPackage.GROOVY__SCRIPT:
				return getScript();
			case GroovyPackage.GROOVY__EXECUTE_BEFORE:
				return isExecuteBefore() ? Boolean.TRUE : Boolean.FALSE;
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT:
				return getExecuteOnElement();
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT_NS:
				return getExecuteOnElementNS();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GroovyPackage.GROOVY__IMPORTS:
				setImports((String)newValue);
				return;
			case GroovyPackage.GROOVY__PARAM:
				getParam().clear();
				getParam().addAll((Collection<? extends ParamType>)newValue);
				return;
			case GroovyPackage.GROOVY__SCRIPT:
				setScript((ScriptType)newValue);
				return;
			case GroovyPackage.GROOVY__EXECUTE_BEFORE:
				setExecuteBefore(((Boolean)newValue).booleanValue());
				return;
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT:
				setExecuteOnElement((String)newValue);
				return;
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT_NS:
				setExecuteOnElementNS((String)newValue);
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
			case GroovyPackage.GROOVY__IMPORTS:
				setImports(IMPORTS_EDEFAULT);
				return;
			case GroovyPackage.GROOVY__PARAM:
				getParam().clear();
				return;
			case GroovyPackage.GROOVY__SCRIPT:
				setScript((ScriptType)null);
				return;
			case GroovyPackage.GROOVY__EXECUTE_BEFORE:
				unsetExecuteBefore();
				return;
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT:
				setExecuteOnElement(EXECUTE_ON_ELEMENT_EDEFAULT);
				return;
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT_NS:
				setExecuteOnElementNS(EXECUTE_ON_ELEMENT_NS_EDEFAULT);
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
			case GroovyPackage.GROOVY__IMPORTS:
				return IMPORTS_EDEFAULT == null ? imports != null : !IMPORTS_EDEFAULT.equals(imports);
			case GroovyPackage.GROOVY__PARAM:
				return param != null && !param.isEmpty();
			case GroovyPackage.GROOVY__SCRIPT:
				return script != null;
			case GroovyPackage.GROOVY__EXECUTE_BEFORE:
				return isSetExecuteBefore();
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT:
				return EXECUTE_ON_ELEMENT_EDEFAULT == null ? executeOnElement != null : !EXECUTE_ON_ELEMENT_EDEFAULT.equals(executeOnElement);
			case GroovyPackage.GROOVY__EXECUTE_ON_ELEMENT_NS:
				return EXECUTE_ON_ELEMENT_NS_EDEFAULT == null ? executeOnElementNS != null : !EXECUTE_ON_ELEMENT_NS_EDEFAULT.equals(executeOnElementNS);
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
		result.append(" (imports: "); //$NON-NLS-1$
		result.append(imports);
		result.append(", executeBefore: "); //$NON-NLS-1$
		if (executeBeforeESet) result.append(executeBefore); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", executeOnElement: "); //$NON-NLS-1$
		result.append(executeOnElement);
		result.append(", executeOnElementNS: "); //$NON-NLS-1$
		result.append(executeOnElementNS);
		result.append(')');
		return result.toString();
	}

} //GroovyImpl
