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
package org.jboss.tools.common.el.core.resolver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Region;

/**
 * EL context
 * @author Alexey Kazakov
 */
public class ELContextImpl implements ELContext {

	protected IFile resource;
	protected ELResolver[] elResolvers;
	protected Map<Region, Set<Var>> vars = new HashMap<Region, Set<Var>>();
	protected Set<Var> allVars = new HashSet<Var>();

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
	 * @see org.jboss.tools.common.el.core.resolver.ELContext#getVars()
	 */
	public Var[] getVars() {
		return allVars.toArray(new Var[allVars.size()]);
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
}