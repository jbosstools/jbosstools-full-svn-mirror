/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.deltaspike.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IRootDefinitionContext;
import org.jboss.tools.cdi.core.extension.ICDIExtension;
import org.jboss.tools.cdi.core.extension.IDefinitionContextExtension;
import org.jboss.tools.cdi.core.extension.feature.IBuildParticipantFeature;
import org.jboss.tools.cdi.core.extension.feature.IProcessAnnotatedMemberFeature;
import org.jboss.tools.cdi.core.extension.feature.IProcessAnnotatedTypeFeature;
import org.jboss.tools.cdi.core.extension.feature.IValidatorFeature;
import org.jboss.tools.cdi.deltaspike.core.validation.DeltaspikeValidationMessages;
import org.jboss.tools.cdi.internal.core.impl.CDIProject;
import org.jboss.tools.cdi.internal.core.impl.definition.AbstractMemberDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.AnnotationDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.BeanMemberDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.MethodDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.TypeDefinition;
import org.jboss.tools.cdi.internal.core.scanner.FileSet;
import org.jboss.tools.cdi.internal.core.validation.CDICoreValidator;
import org.jboss.tools.common.java.IAnnotationDeclaration;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.preferences.SeverityPreferences;

/**
 * Runtime
 * org.apache.deltaspike.security.impl.authorization.SecurityExtension
 * 
 * @author Viacheslav Kabanovich
 */
public class DeltaspikeSecurityExtension implements ICDIExtension, IBuildParticipantFeature, IProcessAnnotatedTypeFeature, IProcessAnnotatedMemberFeature, IValidatorFeature, DeltaspikeConstants {
	DeltaspikeSecurityDefinitionContext context = new DeltaspikeSecurityDefinitionContext();

	@Override
	public IDefinitionContextExtension getContext() {
		return context;
	}

	@Override
	public void beginVisiting() {}

	@Override
	public void visitJar(IPath path, IPackageFragmentRoot root, XModelObject beansXML) {}

	@Override
	public void visit(IFile file, IPath src, IPath webinf) {}

	@Override
	public void buildDefinitions() {}

	@Override
	public void buildDefinitions(FileSet fileSet) {}

	@Override
	public void buildBeans(CDIProject target) {}

	@Override
	public void processAnnotatedMember(BeanMemberDefinition memberDefinition,
			IRootDefinitionContext context) {
		if(!(memberDefinition instanceof MethodDefinition)) {
			return;
		}
		if(memberDefinition.isAnnotationPresent(SECURES_ANNOTATION_TYPE_NAME)) {
			MethodDefinition method = (MethodDefinition)memberDefinition;
			method.setCDIAnnotated(true);
			DeltaspikeAuthorityMethod authorizer = new DeltaspikeAuthorityMethod(method);
			DeltaspikeSecurityDefinitionContext contextCopy = ((DeltaspikeSecurityDefinitionContext)this.context.getWorkingCopy());
			contextCopy.allAuthorizerMethods.getAuthorizerMembers().add(authorizer);
			List<IAnnotationDeclaration> ds = findAnnotationAnnotatedWithSecurityBindingType(memberDefinition, contextCopy.getRootContext());
			for (IAnnotationDeclaration d: ds) {
				DeltaspikeSecurityBindingConfiguration c = ((DeltaspikeSecurityDefinitionContext)this.context.getWorkingCopy()).getConfiguration(d.getTypeName());
				authorizer.addBinding(d, c);
				c.getAuthorizerMembers().add(authorizer);
				addToDependencies(c, authorizer.getMethod(), context);
			}
		} else {
			addSecurityMember(memberDefinition, context);
		}
	}

	@Override
	public void processAnnotatedType(TypeDefinition typeDefinition,
			IRootDefinitionContext context) {
		addSecurityMember(typeDefinition, context);
	}

	private void addSecurityMember(AbstractMemberDefinition def, IRootDefinitionContext context) {
		List<IAnnotationDeclaration> ds = findAnnotationAnnotatedWithSecurityBindingType(def, context);
		for (IAnnotationDeclaration d: ds) {
			addBoundMember(def, d, context);
		}
	}

	private void addBoundMember(AbstractMemberDefinition def, IAnnotationDeclaration d, IRootDefinitionContext context) {
		String securityBindingType = d.getTypeName();
		if(def instanceof MethodDefinition) {
			((MethodDefinition)def).setCDIAnnotated(true);
		}
		DeltaspikeSecurityBindingConfiguration c = ((DeltaspikeSecurityDefinitionContext)this.context.getWorkingCopy()).getConfiguration(securityBindingType);
		
		c.getBoundMembers().put(def, d);

		addToDependencies(c, def, context);
	}


	private void addToDependencies(DeltaspikeSecurityBindingConfiguration c, AbstractMemberDefinition def, IRootDefinitionContext context) {
		IResource r = def.getResource();
		if(r != null && r.exists() && !c.getInvolvedTypes().contains(r.getFullPath())) {
			IPath newPath = r.getFullPath();
			Set<IPath> ps = c.getInvolvedTypes();
			for (IPath p: ps) {
				context.addDependency(p, newPath);
				context.addDependency(newPath, p);
			}
			ps.add(newPath);				
		}
	}

	static List<IAnnotationDeclaration> EMPTY = Collections.<IAnnotationDeclaration>emptyList();

	private List<IAnnotationDeclaration> findAnnotationAnnotatedWithSecurityBindingType(AbstractMemberDefinition m, IRootDefinitionContext context) {
		List<IAnnotationDeclaration> result = null;
		List<IAnnotationDeclaration> ds = m.getAnnotations();
		for (IAnnotationDeclaration d: ds) {
			if(d.getTypeName() != null) {
//				context.getAnnotationKind(d.getType());
				AnnotationDefinition a = context.getAnnotation(d.getTypeName());
				if(a != null && a.isAnnotationPresent(SECURITY_BINDING_ANNOTATION_TYPE_NAME)) {
					if(result == null) {
						result = new ArrayList<IAnnotationDeclaration>();
					}
					result.add(d);
				}
			}
		}
		return result == null ? EMPTY : result;
	}

	@Override
	public void validateResource(IFile file, CDICoreValidator validator) {
		Set<DeltaspikeAuthorityMethod> authorizers = context.getAuthorityMethods(file.getFullPath());
		for (DeltaspikeAuthorityMethod authorizer: authorizers) {
			IAnnotationDeclaration a = authorizer.getMethod().getAnnotation(SECURES_ANNOTATION_TYPE_NAME);
			if(authorizer.getBindings().isEmpty()) {
				validator.addError(DeltaspikeValidationMessages.INVALID_AUTHORIZER_NO_BINDINGS, 
						DeltaspikeSeverityPreferences.INVALID_AUTHORIZER,  
						new String[]{authorizer.getMethod().getMethod().getElementName()}, 
						a, file);
			} else if(authorizer.getBindings().size() > 1) {
				validator.addError(DeltaspikeValidationMessages.INVALID_AUTHORIZER_MULTIPLE_BINDINGS, 
						DeltaspikeSeverityPreferences.INVALID_AUTHORIZER,  
						new String[]{authorizer.getMethod().getMethod().getElementName()}, 
						a, file);
			}
			try {
				String returnType = authorizer.getMethod().getMethod().getReturnType();
				if(!"Z".equals(returnType)) { //$NON-NLS-1$
					validator.addError(DeltaspikeValidationMessages.INVALID_AUTHORIZER_NOT_BOOLEAN, 
							DeltaspikeSeverityPreferences.INVALID_AUTHORIZER,  
							new String[]{authorizer.getMethod().getMethod().getElementName()}, 
							a, file);
				}
				
			} catch (JavaModelException e) {
				DeltaspikeCorePlugin.getDefault().logError(e);
			}
			
		}
		
		for (DeltaspikeSecurityBindingConfiguration c: context.getConfigurations().values()) {
			if(c.getInvolvedTypes().contains(file.getFullPath())) {
				Set<DeltaspikeAuthorityMethod> authorizers2 = c.getAuthorizerMembers();
				Map<AbstractMemberDefinition, IAnnotationDeclaration> bound = c.getBoundMembers();
				for (AbstractMemberDefinition d: bound.keySet()) {
					String name = d instanceof MethodDefinition ? ((MethodDefinition)d).getMethod().getElementName()
							: d instanceof TypeDefinition ? ((TypeDefinition)d).getQualifiedName() : "";
					if(file.getFullPath().equals(d.getTypeDefinition().getType().getPath())) {
						IAnnotationDeclaration dc = bound.get(d);
						int k = 0;
						for (DeltaspikeAuthorityMethod a: authorizers2) {
							try {
								if(a.isMatching(dc)) k++;
							} catch (CoreException e) {
								DeltaspikeCorePlugin.getDefault().logError(e);
							}
						}
						if(k == 0) {
							validator.addError(DeltaspikeValidationMessages.UNRESOLVED_AUTHORIZER, 
									DeltaspikeSeverityPreferences.UNRESOLVED_AUTHORIZER,  
									new String[]{dc.getTypeName(), name}, 
									dc, file);
						} else if(k > 1) {
							validator.addError(DeltaspikeValidationMessages.AMBIGUOUS_AUTHORIZER, 
									DeltaspikeSeverityPreferences.AMBIGUOUS_AUTHORIZER,  
									new String[]{dc.getTypeName(), name}, 
									dc, file);
						}
							
					}
				}
			}
		}
		//
	}

	@Override
	public SeverityPreferences getSeverityPreferences() {
		return DeltaspikeSeverityPreferences.getInstance();
	}

}
