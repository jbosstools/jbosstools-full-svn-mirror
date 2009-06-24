/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IMember;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.parser.ELParserFactory;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELCompletionEngine;
import org.jboss.tools.common.el.core.resolver.ELOperandResolveStatus;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.TypeInfoCollector;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jsf.JSFModelPlugin;
import org.jboss.tools.jsf.model.pv.JSFPromptingProvider;
import org.jboss.tools.jst.web.kb.el.AbstractELCompletionEngine;

/**
 * Utility class used to collect info for EL
 * 
 * @author Viacheslav Kabanovich
 */
public class JSFELCompletionEngine extends AbstractELCompletionEngine<JSFELCompletionEngine.IJSFVariable> implements ELResolver, ELCompletionEngine {
	private static final Image JSF_EL_PROPOSAL_IMAGE = JSFModelPlugin.getDefault().getImage(JSFModelPlugin.CA_JSF_EL_IMAGE_PATH);

	public Image getELProposalImage() {
		return JSF_EL_PROPOSAL_IMAGE;
	}

	private static ELParserFactory factory = ELParserUtil.getDefaultFactory();

	public JSFELCompletionEngine() {}

	protected void log(Exception e) {
		JSFModelPlugin.getPluginLog().logError(e);
	}

	public ELParserFactory getParserFactory() {
		return factory;
	}

	protected ELOperandResolveStatus newELOperandResolveStatus(ELInvocationExpression tokens) {
		return new ELOperandResolveStatus(tokens);
	}

	public List<IJSFVariable> resolveVariables(IFile file, ELInvocationExpression expr, boolean isFinal, boolean onlyEqualNames) {
		IModelNature project = EclipseResourceUtil.getModelNature(file.getProject());
		return resolveVariables(project, expr, isFinal, onlyEqualNames);
	}

	public List<IJSFVariable> resolveVariables(IModelNature project, ELInvocationExpression expr, boolean isFinal, boolean onlyEqualNames) {
		List<IJSFVariable>resolvedVars = new ArrayList<IJSFVariable>();
		
		if (project == null)
			return new ArrayList<IJSFVariable>(); 
		
		String varName = expr.toString();

		if (varName != null) {
			resolvedVars = resolveVariables(project, varName, onlyEqualNames);
		}
		if (resolvedVars != null && !resolvedVars.isEmpty()) {
			List<IJSFVariable> newResolvedVars = new ArrayList<IJSFVariable>();
			for (IJSFVariable var : resolvedVars) {
				if(!isFinal) {
					// Do filter by equals (name)
					// In case of the last pass - do not filter by startsWith(name) instead of equals
					if (varName.equals(var.getName())) {
						newResolvedVars.add(var);
					}
				} else {
					newResolvedVars.add(var);
				}
			}
			return newResolvedVars;
		}
		return new ArrayList<IJSFVariable>(); 
	}

	List<IJSFVariable> resolveVariables(IModelNature project, String varName, boolean onlyEqualNames) {
		if(project == null) return null;
		List<IJSFVariable> beans = new JSFPromptingProvider().getVariables(project.getModel());
		List<IJSFVariable> resolvedVariables = new ArrayList<IJSFVariable>();
		for (IJSFVariable variable: beans) {
			String n = variable.getName();
			if(onlyEqualNames) {
				if (n.equals(varName)) {
					resolvedVariables.add(variable);
				}
			} else {
				if (n.startsWith(varName)) {
					resolvedVariables.add(variable);
				}
			}
		}
		return resolvedVariables;
	}

	public static interface IJSFVariable extends IVariable {
		public String getName();
		public IMember getSourceMember();
	}

	protected TypeInfoCollector.MemberInfo getMemberInfoByVariable(IJSFVariable var, boolean onlyEqualNames) {
		return TypeInfoCollector.createMemberInfo(((IJSFVariable)var).getSourceMember());		
	}

}
