package org.jboss.tools.seam.ui.pages.editor.ecore.pages;

/** 
 * @author daniel
 * 
 * Page model interface
 * 
 * @model
 */
public interface Page extends PagesElement{
	/**
	 * returns true if params are visible
	 * @model
	 */
	public boolean isParamsVisible();

	/**
	 * Sets the value of the '{@link org.jboss.tools.seam.ui.pages.editor.ecore.pages.Page#isParamsVisible <em>Params Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Params Visible</em>' attribute.
	 * @see #isParamsVisible()
	 * @generated
	 */
	void setParamsVisible(boolean value);
}
