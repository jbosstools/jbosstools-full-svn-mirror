/******************************************************************************* 
 * Copyright (c) 2009-2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.internal.core.el;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.CDICorePlugin;
import org.jboss.tools.cdi.core.CDIImages;
import org.jboss.tools.cdi.core.CDIUtil;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IBeanMember;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.internal.core.impl.CDIProjectAsYouType;
import org.jboss.tools.common.el.core.ca.AbstractELCompletionEngine;
import org.jboss.tools.common.el.core.ca.DefaultJavaRelevanceCheck;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.parser.ELParserFactory;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.IRelevanceCheck;
import org.jboss.tools.common.el.core.resolver.TypeInfoCollector;
import org.jboss.tools.common.el.core.resolver.TypeInfoCollector.MemberInfo;
import org.jboss.tools.common.el.core.resolver.TypeInfoCollector.MemberPresentation;
import org.jboss.tools.common.text.TextProposal;

/**
 * @author Alexey Kazakov
 */
public class CdiElResolver extends AbstractELCompletionEngine<IBean> {

	private static ELParserFactory factory = ELParserUtil.getJbossFactory();

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.ca.AbstractELCompletionEngine#getELProposalImageForMember(org.jboss.tools.common.el.core.resolver.TypeInfoCollector.MemberInfo)
	 */
	@Override
	public ImageDescriptor getELProposalImageForMember(MemberInfo memberInfo) {
		return (memberInfo instanceof TypeInfoCollector.FieldInfo)?CDIImages.BEAN_FIELD_IMAGE:CDIImages.BEAN_METHOD_IMAGE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.ca.AbstractELCompletionEngine#getELProposalImage(org.jboss.tools.common.el.core.resolver.TypeInfoCollector.MemberPresentation)
	 */
	@Override
	protected ImageDescriptor getELProposalImage(MemberPresentation memberPresentation) {
		return memberPresentation.isProperty()?CDIImages.BEAN_FIELD_IMAGE:CDIImages.BEAN_METHOD_IMAGE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.ca.AbstractELCompletionEngine#setImage(org.jboss.tools.common.text.TextProposal, org.jboss.tools.common.el.core.resolver.IVariable)
	 */
	@Override
	protected void setImage(TextProposal kbProposal, IBean var) {
		kbProposal.setImageDescriptor(CDIImages.getImageDescriptorByElement(var));
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.el.AbstractELCompletionEngine#log(java.lang.Exception)
	 */
	@Override
	protected void log(Exception e) {
		CDICorePlugin.getDefault().logError(e);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.el.AbstractELCompletionEngine#getMemberInfoByVariable(org.jboss.tools.common.el.core.resolver.IVariable, boolean)
	 */
	@Override
	protected MemberInfo getMemberInfoByVariable(IBean bean, ELContext context, boolean onlyEqualNames, int offset) {
		IMember member = null;
		if(bean instanceof IClassBean) {
			member = bean.getBeanClass();
		} else if(bean instanceof IBeanMember) {
			IBeanMember beanMember = (IBeanMember)bean;
			member = beanMember.getSourceMember();
		} else {
			member = bean.getBeanClass();
		}
		return TypeInfoCollector.createMemberInfo(member);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.el.AbstractELCompletionEngine#resolveVariables(org.eclipse.core.resources.IFile, org.jboss.tools.common.el.core.model.ELInvocationExpression, boolean, boolean)
	 */
	@Override
	public List<IBean> resolveVariables(IFile file,	 ELContext context, ELInvocationExpression expr, boolean isFinal, boolean onlyEqualNames, int offset) {
		ArrayList<IBean> beans = new ArrayList<IBean>();

		IProject project = file.getProject();
		if (project == null) {
			return beans; 
		}

		String varName = expr.toString();

		Collection<IBean> resolvedBeans = null;
		if (varName != null) {
			CDICoreNature nature = CDIUtil.getCDINatureWithProgress(project);
			if(nature!=null) {
				ICDIProject cdiProject = nature.getDelegate();
				if (cdiProject != null) {
					if(context!=null && context.isDirty() && ("java".equalsIgnoreCase(file.getFileExtension()) || "beans.xml".equalsIgnoreCase(file.getName()))) {
						cdiProject = new CDIProjectAsYouType(cdiProject, file);
					}
					if(onlyEqualNames) {
						resolvedBeans = cdiProject.getBeans(varName, true);
						if(resolvedBeans.isEmpty()) {
							resolvedBeans = cdiProject.getBeans(varName, false);
						}
						beans.addAll(resolvedBeans);
					} else {
						resolvedBeans = cdiProject.getNamedBeans(true);
						if(resolvedBeans.isEmpty()) {
							resolvedBeans = cdiProject.getBeans(varName, false);
						}
						for (IBean bean : resolvedBeans) {
							if(bean.getName().startsWith(varName)) {
								beans.add(bean);
							}
						}
						resolvedBeans.clear();
						resolvedBeans.addAll(beans);
					}
				}
			}
		}
		if (resolvedBeans != null && !resolvedBeans.isEmpty()) {
			List<IBean> newResolvedVars = new ArrayList<IBean>();
			for (IBean var : resolvedBeans) {
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
		return beans;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.resolver.ELResolver#getParserFactory()
	 */
	public ELParserFactory getParserFactory() {
		return factory;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.ca.AbstractELCompletionEngine#isStaticMethodsCollectingEnabled()
	 */
	@Override
	protected boolean isStaticMethodsCollectingEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.ca.AbstractELCompletionEngine#createRelevanceCheck(org.eclipse.jdt.core.IJavaElement)
	 */
	@Override
	public IRelevanceCheck createRelevanceCheck(IJavaElement element) {
		return new BeanRelevanceCheck(element);
	}

}

class BeanRelevanceCheck extends DefaultJavaRelevanceCheck {
	Set<String> names = new HashSet<String>();
	
	public BeanRelevanceCheck(IJavaElement element) {
		super(element);
		IProject project = element.getJavaProject().getProject();
		ICDIProject cdi = CDICorePlugin.getCDIProject(project, true);
		if(cdi != null) {
			for (IBean b: cdi.getBeans(element)) {
				if(b.getName() != null) {
					names.add(b.getName());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.ca.DefaultJavaRelevanceCheck#isRelevant(java.lang.String)
	 */
	@Override
	public boolean isRelevant(String content) {
		if(super.isRelevant(content)) {
			return true;
		}
		for (String name: names) {
			if(content.contains(name)) {
				return true;
			}
		}
		return false;
	}
}