/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.internal.core.scanner;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.ICDIBuilderDelegate;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.internal.core.impl.CDIProject;
import org.jboss.tools.cdi.internal.core.impl.definition.BeansXMLDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.DefinitionContext;
import org.jboss.tools.cdi.internal.core.impl.definition.PackageDefinition;
import org.jboss.tools.cdi.internal.core.impl.definition.TypeDefinition;
import org.jboss.tools.common.model.XModelObject;

public class CDIBuilderDelegate implements ICDIBuilderDelegate {

	public int computeRelevance(IProject project) {
		//nothing to compute, builder works only if cdi nature is present
		return 1;
	}

	public String getID() {
		return getClass().getName();
	}

	public Class<? extends ICDIProject> getProjectImplementationClass() {
		return CDIProject.class;
	}

	public void build(FileSet fileSet, CDICoreNature projectNature) {
		DefinitionContext context = projectNature.getDefinitions().getWorkingCopy();
		build(fileSet, context);
	}

	public void build(FileSet fileSet, DefinitionContext context) {
		Set<IPath> ps = fileSet.getAllPaths();
		for (IPath p: ps) context.clean(p);
		Map<IPath, Set<IType>> as = fileSet.getAnnotations();
		for (IPath f: as.keySet()) {
			Set<IType> ts = as.get(f);
			for (IType type: ts) {
				//this builds annotation definition
				context.getAnnotationKind(type);
			}
		}
		
		Map<IPath, Set<IType>> is = fileSet.getInterfaces();
		for (IPath f: is.keySet()) {
			Set<IType> ts = is.get(f);
			for (IType type: ts) {
				// Jars present package-info as binary interface 
				// whereas sources present it as compilation unit with package declaration. 
				if(type.getElementName().equals("package-info")) {
					PackageDefinition def = new PackageDefinition();
					def.setBinaryType(type, context);
					context.addPackage(f, def.getQualifiedName(), def);
				}
			}
		}
		
		Map<IPath, Set<IType>> cs = fileSet.getClasses();
		for (IPath f: cs.keySet()) {
			Set<IType> ts = cs.get(f);
			for (IType type: ts) {
				TypeDefinition def = new TypeDefinition();
				def.setType(type, context, 0);
				context.addType(f, type.getFullyQualifiedName(), def);
			}
		}

		Map<IPath, IPackageDeclaration> pkgs = fileSet.getPackages();
		for (IPath f: pkgs.keySet()) {
			IPackageDeclaration pkg = pkgs.get(f);
			PackageDefinition def = new PackageDefinition();
			def.setPackage(pkg, context);
			context.addPackage(f, def.getQualifiedName(), def);
		}

		for (IPath f: ps) {
			XModelObject beansXML = fileSet.getBeanXML(f);
			if(beansXML == null) continue;
			
			BeansXMLDefinition def = new BeansXMLDefinition();
			def.setPath(f);
			def.setBeansXML(beansXML);
			
			context.addBeanXML(f, def);			
		}
		
	}

}
