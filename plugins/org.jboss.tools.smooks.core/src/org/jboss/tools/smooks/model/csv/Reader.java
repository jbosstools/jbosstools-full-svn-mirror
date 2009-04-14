/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.csv;

import java.math.BigInteger;

import org.jboss.tools.smooks.model.smooks.AbstractReader;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reader</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * CSV Reader
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.csv.Reader#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.Reader#getFields <em>Fields</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.Reader#getQuote <em>Quote</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.Reader#getSeparator <em>Separator</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.csv.Reader#getSkipLines <em>Skip Lines</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.csv.CsvPackage#getReader()
 * @model extendedMetaData="name='reader' kind='empty'"
 * @generated
 */
public interface Reader extends AbstractReader {
	/**
	 * Returns the value of the '<em><b>Encoding</b></em>' attribute.
	 * The default value is <code>"UTF-8"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						The encoding of the input stream. Default of 'UTF-8'
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Encoding</em>' attribute.
	 * @see #isSetEncoding()
	 * @see #unsetEncoding()
	 * @see #setEncoding(String)
	 * @see org.jboss.tools.smooks.model.csv.CsvPackage#getReader_Encoding()
	 * @model default="UTF-8" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='encoding'"
	 * @generated
	 */
	String getEncoding();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoding</em>' attribute.
	 * @see #isSetEncoding()
	 * @see #unsetEncoding()
	 * @see #getEncoding()
	 * @generated
	 */
	void setEncoding(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEncoding()
	 * @see #getEncoding()
	 * @see #setEncoding(String)
	 * @generated
	 */
	void unsetEncoding();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getEncoding <em>Encoding</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Encoding</em>' attribute is set.
	 * @see #unsetEncoding()
	 * @see #getEncoding()
	 * @see #setEncoding(String)
	 * @generated
	 */
	boolean isSetEncoding();

	/**
	 * Returns the value of the '<em><b>Fields</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						Comma separated list of CSV record field names
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fields</em>' attribute.
	 * @see #setFields(String)
	 * @see org.jboss.tools.smooks.model.csv.CsvPackage#getReader_Fields()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='fields'"
	 * @generated
	 */
	String getFields();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getFields <em>Fields</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fields</em>' attribute.
	 * @see #getFields()
	 * @generated
	 */
	void setFields(String value);

	/**
	 * Returns the value of the '<em><b>Quote</b></em>' attribute.
	 * The default value is <code>"\""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						Quote character.  Default of '"'.
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Quote</em>' attribute.
	 * @see #isSetQuote()
	 * @see #unsetQuote()
	 * @see #setQuote(String)
	 * @see org.jboss.tools.smooks.model.csv.CsvPackage#getReader_Quote()
	 * @model default="\"" unsettable="true" dataType="csv.Char"
	 *        extendedMetaData="kind='attribute' name='quote'"
	 * @generated
	 */
	String getQuote();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getQuote <em>Quote</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quote</em>' attribute.
	 * @see #isSetQuote()
	 * @see #unsetQuote()
	 * @see #getQuote()
	 * @generated
	 */
	void setQuote(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getQuote <em>Quote</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetQuote()
	 * @see #getQuote()
	 * @see #setQuote(String)
	 * @generated
	 */
	void unsetQuote();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getQuote <em>Quote</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Quote</em>' attribute is set.
	 * @see #unsetQuote()
	 * @see #getQuote()
	 * @see #setQuote(String)
	 * @generated
	 */
	boolean isSetQuote();

	/**
	 * Returns the value of the '<em><b>Separator</b></em>' attribute.
	 * The default value is <code>","</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						 Field separator character.  Default of ','.
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Separator</em>' attribute.
	 * @see #isSetSeparator()
	 * @see #unsetSeparator()
	 * @see #setSeparator(String)
	 * @see org.jboss.tools.smooks.model.csv.CsvPackage#getReader_Separator()
	 * @model default="," unsettable="true" dataType="csv.Char"
	 *        extendedMetaData="kind='attribute' name='separator'"
	 * @generated
	 */
	String getSeparator();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getSeparator <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Separator</em>' attribute.
	 * @see #isSetSeparator()
	 * @see #unsetSeparator()
	 * @see #getSeparator()
	 * @generated
	 */
	void setSeparator(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getSeparator <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSeparator()
	 * @see #getSeparator()
	 * @see #setSeparator(String)
	 * @generated
	 */
	void unsetSeparator();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getSeparator <em>Separator</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Separator</em>' attribute is set.
	 * @see #unsetSeparator()
	 * @see #getSeparator()
	 * @see #setSeparator(String)
	 * @generated
	 */
	boolean isSetSeparator();

	/**
	 * Returns the value of the '<em><b>Skip Lines</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						Number of lines to skip before processing starts. Default of 0.
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Skip Lines</em>' attribute.
	 * @see #isSetSkipLines()
	 * @see #unsetSkipLines()
	 * @see #setSkipLines(BigInteger)
	 * @see org.jboss.tools.smooks.model.csv.CsvPackage#getReader_SkipLines()
	 * @model default="0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Integer"
	 *        extendedMetaData="kind='attribute' name='skipLines'"
	 * @generated
	 */
	BigInteger getSkipLines();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getSkipLines <em>Skip Lines</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Skip Lines</em>' attribute.
	 * @see #isSetSkipLines()
	 * @see #unsetSkipLines()
	 * @see #getSkipLines()
	 * @generated
	 */
	void setSkipLines(BigInteger value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getSkipLines <em>Skip Lines</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSkipLines()
	 * @see #getSkipLines()
	 * @see #setSkipLines(BigInteger)
	 * @generated
	 */
	void unsetSkipLines();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.csv.Reader#getSkipLines <em>Skip Lines</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Skip Lines</em>' attribute is set.
	 * @see #unsetSkipLines()
	 * @see #getSkipLines()
	 * @see #setSkipLines(BigInteger)
	 * @generated
	 */
	boolean isSetSkipLines();

} // Reader
