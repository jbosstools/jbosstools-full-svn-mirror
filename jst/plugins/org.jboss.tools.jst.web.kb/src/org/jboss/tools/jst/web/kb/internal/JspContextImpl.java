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
package org.jboss.tools.jst.web.kb.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ElVarSearcher;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * JSP page context
 * @author Alexey Kazakov
 */
public class JspContextImpl implements IPageContext {

	private IFile resource;
	private IDocument document;
	private ElVarSearcher varSearcher;
	private ITagLibrary[] libs;
	private ELResolver[] elResolvers;
	private Map<Region, Set<Var>> vars = new HashMap<Region, Set<Var>>();
	private Map<Region, Map<String, INameSpace>> nameSpaces = new HashMap<Region, Map<String, INameSpace>>();
	private Set<Var> allVars = new HashSet<Var>();
	private IResourceBundle[] bundles;

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getResource()
	 */
	public IFile getResource() {
		return resource;
	}

	public void setResource(IFile resource) {
		this.resource = resource;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getLibraries()
	 */
	public ITagLibrary[] getLibraries() {
		return libs;
	}

	public void setLibraries(ITagLibrary[] libs) {
		this.libs = libs;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getElResolvers()
	 */
	public ELResolver[] getElResolvers() {
		return elResolvers;
	}

	public void setElResolvers(ELResolver[] elResolvers) {
		this.elResolvers = elResolvers;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getVars(int)
	 */
	public Var[] getVars(int offset) {
		Set<Var> result = new HashSet<Var>();
		for (Region region : vars.keySet()) {
			if(offset>=region.getOffset() && offset<=region.getOffset() + region.getLength()) {
				result.addAll(vars.get(region));
			}
		}
		return result.toArray(new Var[result.size()]);
	}

	/**
	 * Adds new Var to the context
	 * @param region
	 * @param vars
	 */
	public void addVar(Region region, Var var) {
		this.vars.get(region).add(var);
		allVars.add(var);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getResourceBundles()
	 */
	public IResourceBundle[] getResourceBundles() {
		return bundles;
	}

	/**
	 * Sets resource bundles
	 * @param bundles
	 */
	public void setResourceBundles(IResourceBundle[] bundles) {
		this.bundles = bundles;
	}

	/**
	 * @return the libs
	 */
	public ITagLibrary[] getLibs() {
		return libs;
	}

	/**
	 * @param libs the libs to set
	 */
	public void setLibs(ITagLibrary[] libs) {
		this.libs = libs;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(IDocument document) {
		this.document = document;
	}

	/**
	 * @param varSearcher the varSearcher to set
	 */
	public void setVarSearcher(ElVarSearcher varSearcher) {
		this.varSearcher = varSearcher;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.PageContext#getDocument()
	 */
	public IDocument getDocument() {
		return document;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.resolver.ELContext#getVarSearcher()
	 */
	public ElVarSearcher getVarSearcher() {
		return varSearcher;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.resolver.ELContext#getVars()
	 */
	public Var[] getVars() {
		return allVars.toArray(new Var[allVars.size()]);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IPageContext#getNameSpaces(int)
	 */
	public Map<String, INameSpace> getNameSpaces(int offset) {
		Map<String, INameSpace> result = new HashMap<String, INameSpace>();
		for (Region region : nameSpaces.keySet()) {
			if(offset>=region.getOffset() && offset<=region.getOffset() + region.getLength()) {
				result.putAll(nameSpaces.get(region));
			}
		}
		return result;
	}

	/**
	 * Adds new name space to the context
	 * @param region
	 * @param name space
	 */
	public void addNameSpace(Region region, INameSpace nameSpace) {
		nameSpaces.get(region).put(nameSpace.getPrefix(), nameSpace);
	}
}