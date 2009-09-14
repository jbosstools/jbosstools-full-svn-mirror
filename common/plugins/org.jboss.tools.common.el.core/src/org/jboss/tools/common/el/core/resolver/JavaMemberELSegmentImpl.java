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

import org.eclipse.jdt.core.IJavaElement;
import org.jboss.tools.common.el.core.resolver.TypeInfoCollector.MemberInfo;

/**
 * @author Alexey Kazakov
 */
public class JavaMemberELSegmentImpl extends ELSegmentImpl implements JavaMemberElSegment {

	protected IJavaElement element;
	protected MemberInfo memberInfo;
	protected boolean hasSetter;
	protected boolean hasGetter;

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.resolver.JavaMemberElSegment#getJavaElement()
	 */
	public IJavaElement getJavaElement() {
		return element;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.resolver.JavaMemberElSegment#getMemberInfo()
	 */
	public MemberInfo getMemberInfo() {
		return memberInfo;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.resolver.JavaMemberElSegment#hasGetter()
	 */
	public boolean hasGetter() {
		return hasGetter;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.resolver.JavaMemberElSegment#hasSetter()
	 */
	public boolean hasSetter() {
		return hasSetter;
	}

	/**
	 * @return the element
	 */
	public IJavaElement getElement() {
		return element;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(IJavaElement element) {
		this.element = element;
	}

	/**
	 * @return the hasSetter
	 */
	public boolean isHasSetter() {
		return hasSetter;
	}

	/**
	 * @param hasSetter the hasSetter to set
	 */
	public void setHasSetter(boolean hasSetter) {
		this.hasSetter = hasSetter;
	}

	/**
	 * @return the hasGetter
	 */
	public boolean isHasGetter() {
		return hasGetter;
	}

	/**
	 * @param hasGetter the hasGetter to set
	 */
	public void setHasGetter(boolean hasGetter) {
		this.hasGetter = hasGetter;
	}

	/**
	 * @param memberInfo the memberInfo to set
	 */
	public void setMemberInfo(MemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}
}