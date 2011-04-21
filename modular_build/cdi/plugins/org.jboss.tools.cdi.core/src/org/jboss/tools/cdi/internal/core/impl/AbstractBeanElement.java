/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.internal.core.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.CDIConstants;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.CDICorePlugin;
import org.jboss.tools.cdi.core.IAnnotationDeclaration;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IQualifier;
import org.jboss.tools.cdi.core.IQualifierDeclaration;
import org.jboss.tools.cdi.core.IScopeDeclaration;
import org.jboss.tools.cdi.core.IStereotype;
import org.jboss.tools.cdi.core.IStereotypeDeclaration;
import org.jboss.tools.cdi.core.ITypeDeclaration;
import org.jboss.tools.cdi.internal.core.impl.definition.AbstractMemberDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.AbstractTypeDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.AnnotationDefinition;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AbstractBeanElement extends CDIElement {
	protected AbstractMemberDefinition definition;

	public AbstractBeanElement() {}

	public void setDefinition(AbstractMemberDefinition definition) {
		this.definition = definition;
	}

	public AbstractMemberDefinition getDefinition() {
		return definition;
	}

	protected AnnotationDeclaration findNamedAnnotation() {
		AnnotationDeclaration named = getDefinition().getNamedAnnotation();
		if(named != null) return named;
		Set<IStereotypeDeclaration> ds = getStereotypeDeclarations();
		for (IStereotypeDeclaration d: ds) {
			StereotypeElement s = (StereotypeElement)d.getStereotype();
			if(s == null) continue;
			if(s.getNameDeclaration() != null) return s.getNameDeclaration();
		}
		return null;
	}

	public boolean isAlternative() {
		if(getDefinition().getAlternativeAnnotation() != null) return true;
		Set<IStereotypeDeclaration> ds = getStereotypeDeclarations();
		for (IStereotypeDeclaration d: ds) {
			IStereotype s = d.getStereotype();
			if(s != null && s.isAlternative()) return true;
		}		
		return false;
	}

	public Set<IStereotypeDeclaration> getStereotypeDeclarations() {
		Set<IStereotypeDeclaration> result = new HashSet<IStereotypeDeclaration>();
		for (AnnotationDeclaration d: definition.getAnnotations()) {
			if(d instanceof IStereotypeDeclaration) {
				if(d instanceof IStereotypeDeclaration) {
					result.add((IStereotypeDeclaration)d);
				}
			}
		}
		return result;
	}

	public Set<IQualifierDeclaration> getQualifierDeclarations() {
		return getQualifierDeclarations(false);
	}

	public Set<IQualifierDeclaration> getQualifierDeclarations(boolean includeInherited) {
		Set<IQualifierDeclaration> result = new HashSet<IQualifierDeclaration>();
		Set<IQualifier> qs = new HashSet<IQualifier>();
		for(AnnotationDeclaration a: definition.getAnnotations()) {
			int k = getCDIProject().getNature().getDefinitions().getAnnotationKind(a.getType());
			if(k == AnnotationDefinition.QUALIFIER) {
				IQualifierDeclaration q = (IQualifierDeclaration)a;
				result.add(q);
				if(q.getQualifier() != null) qs.add(q.getQualifier());				
			}
		}
		if(includeInherited) {
			Set<IQualifierDeclaration> ds = getInheritedQualifierDeclarations();
			for (IQualifierDeclaration d : ds) {
				if (d.getQualifier() != null && !qs.contains(d.getQualifier())) {
					result.add(d);
				}
			}
		}
		return result;
	}

	protected Set<IQualifierDeclaration> getInheritedQualifierDeclarations() {
		return Collections.emptySet();
	}

	public Set<IQualifier> getQualifiers() {
		IQualifier any = getCDIProject().getQualifier(CDIConstants.ANY_QUALIFIER_TYPE_NAME);
		IQualifier def = getCDIProject().getQualifier(CDIConstants.DEFAULT_QUALIFIER_TYPE_NAME);
		IQualifier name = getCDIProject().getQualifier(CDIConstants.NAMED_QUALIFIER_TYPE_NAME);

		Set<IQualifier> result = new HashSet<IQualifier>();
		Set<IQualifierDeclaration> ds = getQualifierDeclarations(true);
		for (IQualifierDeclaration d: ds) {
			IQualifier q = d.getQualifier();
			if(q != null) result.add(q);
		}
		if(this instanceof IInjectionPoint) {
			if(def != null && result.isEmpty()) {
				result.add(def);
			}
		} else if(this instanceof IBean) {
			if(def != null) {
				if(result.isEmpty() || (name != null && result.size() == 1 && result.contains(name))) {
					result.add(def);
				}
			}			
			if(any != null) result.add(any);
		}
		return result;
	}

	public Set<IScopeDeclaration> getScopeDeclarations() {
		return getScopeDeclarations(getCDIProject().getNature(), definition.getAnnotations());
	}

	public static Set<IScopeDeclaration> getScopeDeclarations(CDICoreNature n, List<? extends IAnnotationDeclaration> ds) {
		Set<IScopeDeclaration> result = new HashSet<IScopeDeclaration>();
		for (IAnnotationDeclaration d: ds) {
			int k = n.getDefinitions().getAnnotationKind(d.getType());
			if(k == AnnotationDefinition.SCOPE) {
				result.add((IScopeDeclaration)d);
			}
		}
		return result;
	}

	public Set<ITypeDeclaration> getRestrictedTypeDeclaratios() {
		Set<ITypeDeclaration> result = new HashSet<ITypeDeclaration>();
		AnnotationDeclaration typed = getDefinition().getTypedAnnotation();
		if(typed != null) {
			IAnnotation a = typed.getDeclaration();
			try {
				ISourceRange r = a.getSourceRange();
				String txt = null;
				if(r != null && a.getResource() instanceof IFile) {
					AbstractTypeDefinition td = getDefinition().getTypeDefinition();
					if(td != null) {
						String content = td.getContent();
						if(content != null && content.length() > r.getOffset() + r.getLength()) {
							txt = content.substring(r.getOffset(), r.getOffset() + r.getLength());
						}
					}					
				}
				
				IMemberValuePair[] ps = a.getMemberValuePairs();
				if(ps == null || ps.length == 0) return result;
				IMember member = (IMember)definition.getMember();
				IType declaringType = member instanceof IType ? (IType)member : member.getDeclaringType();
				Object value = ps[0].getValue();
				if(value instanceof Object[]) {
					Object[] os = (Object[])value;
					for (int i = 0; i < os.length; i++) {
						String rawTypeName = os[i].toString();
						String typeName = rawTypeName;
						if(!typeName.endsWith(";")) typeName = "Q" + typeName + ";";
						ParametedType p = getCDIProject().getNature().getTypeFactory().getParametedType(declaringType, typeName);
						if(p != null) {
							int offset = 0;
							int length = 0;
							if(txt != null) {
								int q = txt.indexOf(rawTypeName);
								if(q >= 0) {
									offset = r.getOffset() + q;
									length = rawTypeName.length();
								}
							}
							result.add(new TypeDeclaration(p, offset, length));
						}
					}
				} else if(value != null) {
					String rawTypeName = value.toString();
					String typeName = rawTypeName;
					if(!typeName.endsWith(";")) typeName = "Q" + typeName + ";";
					ParametedType p = getCDIProject().getNature().getTypeFactory().getParametedType(declaringType, typeName);
					if(p != null) {
						int offset = 0;
						int length = 0;
						if(txt != null) {
							int q = txt.indexOf(rawTypeName);
							if(q >= 0) {
								offset = r.getOffset() + q;
								length = rawTypeName.length();
							}
						}
						result.add(new TypeDeclaration(p, offset, length));
					}
				}
			} catch (CoreException e) {
				CDICorePlugin.getDefault().logError(e);
			}
		}
		return result;
	}

}
