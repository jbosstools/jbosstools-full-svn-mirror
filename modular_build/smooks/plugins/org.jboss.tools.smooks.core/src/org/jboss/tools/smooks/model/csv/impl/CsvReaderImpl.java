/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.csv.impl;

import java.math.BigInteger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.smooks.impl.AbstractReaderImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reader</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.csv.impl.CsvReaderImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.impl.CsvReaderImpl#getFields <em>Fields</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.impl.CsvReaderImpl#getQuote <em>Quote</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.impl.CsvReaderImpl#getSeparator <em>Separator</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.impl.CsvReaderImpl#getSkipLines <em>Skip Lines</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CsvReaderImpl extends AbstractReaderImpl implements CsvReader {
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
	 * The default value of the '{@link #getFields() <em>Fields</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFields()
	 * @generated
	 * @ordered
	 */
	protected static final String FIELDS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFields() <em>Fields</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFields()
	 * @generated
	 * @ordered
	 */
	protected String fields = FIELDS_EDEFAULT;

	/**
	 * The default value of the '{@link #getQuote() <em>Quote</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuote()
	 * @generated
	 * @ordered
	 */
	protected static final String QUOTE_EDEFAULT = "\""; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getQuote() <em>Quote</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuote()
	 * @generated
	 * @ordered
	 */
	protected String quote = QUOTE_EDEFAULT;

	/**
	 * This is true if the Quote attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean quoteESet;

	/**
	 * The default value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeparator()
	 * @generated
	 * @ordered
	 */
	protected static final String SEPARATOR_EDEFAULT = ","; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeparator()
	 * @generated
	 * @ordered
	 */
	protected String separator = SEPARATOR_EDEFAULT;

	/**
	 * This is true if the Separator attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean separatorESet;

	/**
	 * The default value of the '{@link #getSkipLines() <em>Skip Lines</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSkipLines()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger SKIP_LINES_EDEFAULT = new BigInteger("0"); //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getSkipLines() <em>Skip Lines</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSkipLines()
	 * @generated
	 * @ordered
	 */
	protected BigInteger skipLines = SKIP_LINES_EDEFAULT;

	/**
	 * This is true if the Skip Lines attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean skipLinesESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CsvReaderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CsvPackage.Literals.CSV_READER;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CsvPackage.CSV_READER__ENCODING, oldEncoding, encoding, !oldEncodingESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, CsvPackage.CSV_READER__ENCODING, oldEncoding, ENCODING_EDEFAULT, oldEncodingESet));
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
	public String getFields() {
		return fields;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFields(String newFields) {
		String oldFields = fields;
		fields = newFields;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CsvPackage.CSV_READER__FIELDS, oldFields, fields));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getQuote() {
		return quote;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuote(String newQuote) {
		String oldQuote = quote;
		quote = newQuote;
		boolean oldQuoteESet = quoteESet;
		quoteESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CsvPackage.CSV_READER__QUOTE, oldQuote, quote, !oldQuoteESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetQuote() {
		String oldQuote = quote;
		boolean oldQuoteESet = quoteESet;
		quote = QUOTE_EDEFAULT;
		quoteESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CsvPackage.CSV_READER__QUOTE, oldQuote, QUOTE_EDEFAULT, oldQuoteESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetQuote() {
		return quoteESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSeparator(String newSeparator) {
		String oldSeparator = separator;
		separator = newSeparator;
		boolean oldSeparatorESet = separatorESet;
		separatorESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CsvPackage.CSV_READER__SEPARATOR, oldSeparator, separator, !oldSeparatorESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSeparator() {
		String oldSeparator = separator;
		boolean oldSeparatorESet = separatorESet;
		separator = SEPARATOR_EDEFAULT;
		separatorESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CsvPackage.CSV_READER__SEPARATOR, oldSeparator, SEPARATOR_EDEFAULT, oldSeparatorESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSeparator() {
		return separatorESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getSkipLines() {
		return skipLines;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSkipLines(BigInteger newSkipLines) {
		BigInteger oldSkipLines = skipLines;
		skipLines = newSkipLines;
		boolean oldSkipLinesESet = skipLinesESet;
		skipLinesESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CsvPackage.CSV_READER__SKIP_LINES, oldSkipLines, skipLines, !oldSkipLinesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSkipLines() {
		BigInteger oldSkipLines = skipLines;
		boolean oldSkipLinesESet = skipLinesESet;
		skipLines = SKIP_LINES_EDEFAULT;
		skipLinesESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CsvPackage.CSV_READER__SKIP_LINES, oldSkipLines, SKIP_LINES_EDEFAULT, oldSkipLinesESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSkipLines() {
		return skipLinesESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CsvPackage.CSV_READER__ENCODING:
				return getEncoding();
			case CsvPackage.CSV_READER__FIELDS:
				return getFields();
			case CsvPackage.CSV_READER__QUOTE:
				return getQuote();
			case CsvPackage.CSV_READER__SEPARATOR:
				return getSeparator();
			case CsvPackage.CSV_READER__SKIP_LINES:
				return getSkipLines();
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
			case CsvPackage.CSV_READER__ENCODING:
				setEncoding((String)newValue);
				return;
			case CsvPackage.CSV_READER__FIELDS:
				setFields((String)newValue);
				return;
			case CsvPackage.CSV_READER__QUOTE:
				setQuote((String)newValue);
				return;
			case CsvPackage.CSV_READER__SEPARATOR:
				setSeparator((String)newValue);
				return;
			case CsvPackage.CSV_READER__SKIP_LINES:
				setSkipLines((BigInteger)newValue);
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
			case CsvPackage.CSV_READER__ENCODING:
				unsetEncoding();
				return;
			case CsvPackage.CSV_READER__FIELDS:
				setFields(FIELDS_EDEFAULT);
				return;
			case CsvPackage.CSV_READER__QUOTE:
				unsetQuote();
				return;
			case CsvPackage.CSV_READER__SEPARATOR:
				unsetSeparator();
				return;
			case CsvPackage.CSV_READER__SKIP_LINES:
				unsetSkipLines();
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
			case CsvPackage.CSV_READER__ENCODING:
				return isSetEncoding();
			case CsvPackage.CSV_READER__FIELDS:
				return FIELDS_EDEFAULT == null ? fields != null : !FIELDS_EDEFAULT.equals(fields);
			case CsvPackage.CSV_READER__QUOTE:
				return isSetQuote();
			case CsvPackage.CSV_READER__SEPARATOR:
				return isSetSeparator();
			case CsvPackage.CSV_READER__SKIP_LINES:
				return isSetSkipLines();
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
		result.append(" (encoding: "); //$NON-NLS-1$
		if (encodingESet) result.append(encoding); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", fields: "); //$NON-NLS-1$
		result.append(fields);
		result.append(", quote: "); //$NON-NLS-1$
		if (quoteESet) result.append(quote); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", separator: "); //$NON-NLS-1$
		if (separatorESet) result.append(separator); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", skipLines: "); //$NON-NLS-1$
		if (skipLinesESet) result.append(skipLines); else result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

} //CsvReaderImpl
