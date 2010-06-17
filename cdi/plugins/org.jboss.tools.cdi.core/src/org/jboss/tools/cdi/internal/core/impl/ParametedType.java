/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.internal.core.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.jboss.tools.cdi.core.CDICorePlugin;
import org.jboss.tools.cdi.core.IParametedType;
import org.jboss.tools.cdi.internal.core.impl.definition.ParametedTypeFactory;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ParametedType implements IParametedType {
	protected ParametedTypeFactory typeFactory = null;
	protected IType type;
	protected String arrayPrefix = "";
	protected String signature;
	protected List<ParametedType> parameterTypes = new ArrayList<ParametedType>();
	protected boolean primitive;

	protected boolean isUpper = false;
	protected boolean isLower = false;
	protected boolean isVariable = false;

	boolean inheritanceIsBuilt = false;
	protected ParametedType superType = null;
	protected Set<IParametedType> inheritedTypes = new HashSet<IParametedType>();
	Set<IParametedType> allInheritedTypes = null;

	public static interface PositionProvider {
		ISourceRange getRange(String superTypeName);
	}

	PositionProvider provider = null;

	public ParametedType() {}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.IParametedType#isPrimitive()
	 */
	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}

	public boolean isUpper() {
		return isUpper;
	}

	public void setUpper(boolean b) {
		isUpper = b;
	}

	public boolean isLower() {
		return isLower;
	}

	public void setLower(boolean b) {
		isLower = b;
	}

	public boolean isVariable() {
		return isVariable;
	}

	public void setVariable(boolean b) {
		isVariable = b;
	}

	public ParametedTypeFactory getFactory() {
		return typeFactory;
	}

	public void setFactory(ParametedTypeFactory typefactory) {
		this.typeFactory = typefactory;
	}

	public IType getType() {
		return type;
	}

	public String getArrayPrefix() {
		return arrayPrefix;
	}

	public String getSignature() {
		return signature;
	}

	public void setType(IType type) {
		this.type = type;
	}

	public void setSignature(String signature) {
		this.signature = signature;
		arrayPrefix = "";
		if(signature != null) {
			for (int i = 0; i < signature.length(); i++) {
				if(signature.charAt(i) == '[') arrayPrefix += "["; else break;
			}
		}
	}

	public void addParameter(ParametedType p) {
		parameterTypes.add(p);
	}

	public void setPositionProvider(PositionProvider p) {
		provider = p;
	}

	public boolean equals(Object object) {
		if(!(object instanceof ParametedType)) return false;
		ParametedType other = (ParametedType)object;
		if(signature != null && signature.equals(other.signature)) {
			return true;
		}
		if(type == null || !type.equals(other.type)) {
			return false;
		}
		if(parameterTypes.size() != other.parameterTypes.size()) {
			return false;
		}
		for (int i = 0; i < parameterTypes.size(); i++) {
			if(!parameterTypes.get(i).equals(other.parameterTypes.get(i))) {
				return false;
			}
		}
		if(!arrayPrefix.equals(other.arrayPrefix)) {
			return false;
		}

		return true;
	}

	void buildInheritance() {
		inheritanceIsBuilt = true;
		if(type == null) return;
		try {
			if(!type.isInterface() && !type.isAnnotation()) {
				String sc = type.getSuperclassTypeSignature();
				boolean objectArray = false;
				if(sc != null) {
					try {
						sc = resolveParameters(sc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if(!"java.lang.Object".equals(type.getFullyQualifiedName())) {
					sc = "QObject;";
				} else if("java.lang.Object".equals(type.getFullyQualifiedName()) && arrayPrefix.length() > 0) {
					objectArray = true;
					sc = "QObject;";
				}
				if(!objectArray && arrayPrefix.length() > 0) {
					sc = arrayPrefix + sc;
				}
				
				superType = getFactory().getParametedType(type, sc);
				if(superType != null) {
					if(provider != null) {
						String scn = type.getSuperclassName();
						if(scn != null && provider.getRange(scn) != null) {
							ISourceRange r = provider.getRange(scn);
							superType = new TypeDeclaration(superType, r.getOffset(), r.getLength());
						}
						
					}
					inheritedTypes.add(superType);
				}
			}
			String[] is = type.getSuperInterfaceTypeSignatures();
			if(is != null) for (int i = 0; i < is.length; i++) {
				String p = resolveParameters(is[i]);
				if(arrayPrefix.length() > 0) p = arrayPrefix + p;
				ParametedType t = getFactory().getParametedType(type, p);
				if(t != null) {
					if(provider != null) {
						String scn = type.getSuperInterfaceNames()[i];
						if(scn != null && provider.getRange(scn) != null) {
							ISourceRange r = provider.getRange(scn);
							t = new TypeDeclaration(t, r.getOffset(), r.getLength());
						}
						
					}
					inheritedTypes.add(t);
				}
			}
		} catch (JavaModelException e) {
			CDICorePlugin.getDefault().logError(e);
		}
	}

	public ParametedType getSuperType() {
		if(!inheritanceIsBuilt) {
			buildInheritance();
		}
		return superType;
	}

	public Set<IParametedType> getInheritedTypes() {
		if(!inheritanceIsBuilt) {
			buildInheritance();
		}
		return inheritedTypes;
	}

	public String resolveParameters(String typeSignature) {
		if(typeSignature == null) {
			return typeSignature;
		}
		int i = typeSignature.indexOf('<');
		if(i < 0) {
			if(( typeSignature.startsWith("T") || typeSignature.startsWith("Q")) && typeSignature.endsWith(";")) {
				String param = typeSignature.substring(1, typeSignature.length() - 1);
				String s = findParameterSignature(param);
				return s == null ? typeSignature : s;
			}
			return typeSignature;
		}
		int j = typeSignature.lastIndexOf('>');
		if(j < i) {
			return typeSignature;
		}
		boolean replaced = false;
		StringBuffer newParams = new StringBuffer();
		String[] ps = Signature.getTypeArguments(typeSignature);
		for (String param: ps) {
			String newParam = resolveParameters( param);
			if(!param.equals(newParam)) replaced = true;
			newParams.append(newParam);
		}
		if(replaced) {
			typeSignature = typeSignature.substring(0, i) + '<' + newParams.toString() + '>' + ';';
		}
		return typeSignature;
	}

	public String findParameterSignature(String paramName) {
		if(type == null) {
			return null;
		}
		ITypeParameter[] ps = null;
		try {
			ps = type.getTypeParameters();
		} catch (JavaModelException e) {
			return null;
		}
		if(ps != null) for (int i = 0; i < ps.length; i++) {
			if(ps[i].getElementName().equals(paramName)) {
				if(parameterTypes.size() > i) {
					ParametedType p = parameterTypes.get(i);
					return p.getSignature();
				}
			}
		}
		return null;
	}

	public Set<IParametedType> getAllTypes() {
		if(allInheritedTypes == null) {
			allInheritedTypes = new HashSet<IParametedType>();
			Set<String> processed = new HashSet<String>();
			buildAllTypes(processed, this);
		}
		return allInheritedTypes;
	}

	void buildAllTypes(Set<String> processed, ParametedType p) {
		IType t = p.getType();
		if(t == null) return;
		if(processed.contains(p.getArrayPrefix() + t.getFullyQualifiedName())) return;
		processed.add(p.getArrayPrefix() + t.getFullyQualifiedName());
		allInheritedTypes.add(p);
		Set<IParametedType> ts = p.getInheritedTypes();
		if(ts != null) for (IParametedType pp: ts) {
			buildAllTypes(processed, (ParametedType)pp);
		}
	}

	public String toString() {
		return signature + ":" + super.toString();
	}

	public boolean isAssignableTo(ParametedType other, boolean checkInheritance) {
		if(equals(other)) return true;
		if(this.type == null) return false;
		if(other.isVariable && other.type == null) return true;
		if(this.type.equals(other.type)) {
			if(areTypeParametersAssignableTo(other)) return true;
		}
		if(checkInheritance) {
			for (IParametedType t: getInheritedTypes()) {
				if(((ParametedType)t).isAssignableTo(other, false)) return true;
			}
		}
		return false;
	}
	
	boolean areTypeParametersAssignableTo(ParametedType other) {
		if(other.parameterTypes.size() == 0) return true;
		if(this.parameterTypes.size() != other.parameterTypes.size()) return false;
		for (int i = 0; i < parameterTypes.size(); i++) {
			ParametedType p1 = parameterTypes.get(i);
			ParametedType p2 = other.parameterTypes.get(i);
			if(p1.isLower() || p1.isUpper()) return false;
			if(p1.isVariable()) {
				if(p2.isVariable()) {
					if(p2.isAssignableTo(p1, true)) continue;
				} else if(p2.isLower()) {
					if(p2.isAssignableTo(p1, true)) continue;
				} else if(p2.isUpper()) {
					if(p2.isAssignableTo(p1, true)) continue;
					if(p1.isAssignableTo(p2, true)) continue;
				} else {
					if(p2.isAssignableTo(p1, true)) continue;
				}
			} else {
				if(p2.isLower()) {
					if(p2.isAssignableTo(p1, true)) continue;
				} else {
					if(p1.isAssignableTo(p2, true)) continue;
				}
			}
			
			return false;
		}
		return true;
	}

}
