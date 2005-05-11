package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import org.eclipse.jdt.core.IJavaElement;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;

/**
 * This class represents a java element that is advised
 * by some expression. 
 * 
 * Constants for type are in IAopAdvised and include:
 * 		IAopAdvised.TYPE_FIELD_GET
 * 		IAopAdvised.TYPE_FIELD_SET
 * 		IAopAdvised.TYPE_METHOD_EXECUTION
 * 		IAopAdvised.TYPE_CLASS
 * 
 * @author Marshall
 */
public class AopAdvised implements IAopAdvised
{

	private int type;
	private IJavaElement advised;
	
	public AopAdvised (int type, IJavaElement advised)
	{
		this.type = type;
		this.advised = advised;
	}

	public IJavaElement getAdvisedElement()
	{
		return advised;
	}

	public int getType()
	{
		return type;
	}

}
