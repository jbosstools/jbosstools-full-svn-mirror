/**
 * 
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IType;

/**
 * An interface declaring ITypes that are matched by some
 * type expression.
 * 
 * @author Rob Stryker
 */
public interface IAopTypeMatcher {
	
	public static final int TYPEDEF = 0;
	public static final int INTRODUCTION = 1;
	
	public IType[] getMatched ();
	
	public int getType();
	
	public void removeMatchedType (IType type);
	
	public void addMatchedType (IType type);

	public void addMatchedType (IType[] types);

	public boolean matches (IType type);
	
}
