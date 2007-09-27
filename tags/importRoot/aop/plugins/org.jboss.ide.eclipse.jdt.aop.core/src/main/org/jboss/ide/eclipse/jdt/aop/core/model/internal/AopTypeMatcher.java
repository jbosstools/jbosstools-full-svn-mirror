/**
 * 
 */
package org.jboss.ide.eclipse.jdt.aop.core.model.internal;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.core.IType;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopTypeMatcher;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AopTypeMatcher implements IAopTypeMatcher {

	protected ArrayList matchedTypes;
	private int myType;
	
	public AopTypeMatcher(int type) {
		matchedTypes = new ArrayList();
		this.myType = type;
	}

	public IType[] getMatched() {
		IType[] types = new IType[matchedTypes.size()];
		for( int i = 0; i < matchedTypes.size(); i++ ) {
			types[i] = (IType)matchedTypes.get(i);
		}
		return types;
	}

	public int getType() {
		return this.myType;
	}

	public void removeMatchedType(IType type) {
		matchedTypes.remove(type);
	}

	public void addMatchedType(IType type) {
		matchedTypes.add(type);
	}

	public void addMatchedType(IType[] types) {
		matchedTypes.addAll(Arrays.asList(types));
	}

	public boolean matches(IType type) {
		if( matchedTypes.contains(type)) return true;
		return false;
	}

}
