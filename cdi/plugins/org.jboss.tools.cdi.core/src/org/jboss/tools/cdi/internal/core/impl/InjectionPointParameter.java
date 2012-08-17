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

import org.jboss.tools.cdi.core.CDIConstants;
import org.jboss.tools.cdi.core.CDIUtil;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IInjectionPointParameter;
import org.jboss.tools.common.java.IAnnotationDeclaration;
import org.jboss.tools.common.text.ITextSourceReference;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class InjectionPointParameter extends Parameter implements
		IInjectionPointParameter {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.IInjectionPoint#getDelegateAnnotation()
	 */
	@Override
	public ITextSourceReference getDelegateAnnotation() {
		return getAnnotationPosition(CDIConstants.DELEGATE_STEREOTYPE_TYPE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.IInjectionPoint#isDelegate()
	 */
	@Override
	public boolean isDelegate() {
		return isAnnotationPresent(CDIConstants.DELEGATE_STEREOTYPE_TYPE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.IInjectionPoint#getInjectAnnotation()
	 */
	@Override
	public IAnnotationDeclaration getInjectAnnotation() {
		return beanMethod.inject;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.IInjectionPoint#containsDefaultQualifier()
	 */
	@Override
	public boolean hasDefaultQualifier() {
		return CDIUtil.containsDefaultQualifier(this);
	}

	@Override
	public String getBeanName() {
		AnnotationDeclaration d = getDefinition().getNamedAnnotation();
		if(d != null) {
			Object n = d.getMemberValue(null);
			if(n != null && n.toString().length() > 0) {
				return n.toString();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.cdi.core.IInjectionPoint#getBean()
	 */
	@Override
	public IBean getBean() {
		return (getBeanMethod() instanceof IBean) ? (IBean)getBeanMethod() : getClassBean();
	}
}