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
package org.jboss.tools.seam.internal.core.scanner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTRequestor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.jboss.tools.common.model.project.ext.impl.ValueInfo;
import org.jboss.tools.seam.internal.core.SeamNamespace;
import org.jboss.tools.seam.internal.core.scanner.LoadedDeclarations;

/**
 * This object collects annotations from package-info.java.
 * 
 * @author Viacheslav Kabanovich
 */
public class PackageInfoRequestor extends ASTRequestor {
	PackageInfoVisitor visitor = new PackageInfoVisitor();
	LoadedDeclarations ds = new LoadedDeclarations();
	IResource resource;
	IPath sourcePath;

	public PackageInfoRequestor(IResource resource) {
		this.resource = resource;
		this.sourcePath = resource.getFullPath();
	}

	public LoadedDeclarations getDeclarations() {
		return ds;
	}
	
	public void acceptAST(ICompilationUnit source, CompilationUnit ast) {
		ast.accept(visitor);
		for (SeamNamespace n: visitor.namespaces) {
			n.setSourcePath(resource.getFullPath());
			n.setPackage(visitor.javaPackage);
		}
		ds.getNamespaces().addAll(visitor.namespaces);
	}

}

class PackageInfoVisitor extends ASTVisitor implements SeamAnnotations {
	List<SeamNamespace> namespaces = new ArrayList<SeamNamespace>();
	String javaPackage = null;

	public PackageInfoVisitor() {}

	public boolean visit(PackageDeclaration node) {
		if(node.getName() != null) {
			javaPackage = node.getName().getFullyQualifiedName();
		}
		return true;
	}

	public boolean visit(NormalAnnotation node) {
		IAnnotationBinding b = node.resolveAnnotationBinding();
		if(b != null) {
			String type = b.getAnnotationType().getQualifiedName();
			if(NAMESPACE_ANNOTATION_TYPE.equals(type)) {
				System.out.println("!!!\n" + node);
				ValueInfo value = ValueInfo.getValueInfo(node, "value");
				ValueInfo prefix = ValueInfo.getValueInfo(node, "prefix");
				SeamNamespace ns = new SeamNamespace();
				if(value != null) {
					ns.setURI(value.getValue());
				}
				if(prefix != null) {
					//
				}
				namespaces.add(ns);
			}
		}
		return true;
	}

}
