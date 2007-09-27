package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IJavaElement;

/**
 * @author Marshall
 */
public interface IAopAdvised
{

	/**
	 * This advised element will be advised on field get
	 */
	public static final int TYPE_FIELD_GET = 0;
	
	/**
	 * This advised element will be advised on field set
	 */
	public static final int TYPE_FIELD_SET = 1;
	
	/**
	 * This advised element will be advised when a method is executed
	 */
	public static final int TYPE_METHOD_EXECUTION = 2;
	
	/**
	 * This advised element represents a class that matches some type pattern.
	 */
	public static final int TYPE_CLASS = 3;
	
	
	/**
	 * Return the IJavaElement that corresponds with this object
	 * @return IJavaElement
	 */
	public IJavaElement getAdvisedElement ();
	
	/**
	 * Return the type of execution this advised element represents
	 * @return int
	 */
	public int getType ();
}
