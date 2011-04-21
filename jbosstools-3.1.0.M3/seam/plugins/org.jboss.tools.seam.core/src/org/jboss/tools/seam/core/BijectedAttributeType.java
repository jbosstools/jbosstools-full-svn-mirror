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
package org.jboss.tools.seam.core;

import org.jboss.tools.seam.internal.core.scanner.java.SeamAnnotations;

/**
 * Represents Type of Bijected Attribute.
 * @author Alexey Kazakov
 */
public enum BijectedAttributeType implements SeamAnnotations {
	IN(IN_ANNOTATION_TYPE, true, false),
	OUT(OUT_ANNOTATION_TYPE, true, true),
	DATA_BINDER(DATA_MODEL_ANNOTATION_TYPE, true, true),
	DATA_MODEL_SELECTION(DATA_MODEL_SELECTION_ANNOTATION_TYPE, false, true),
	DATA_MODEL_SELECTION_INDEX(DATA_MODEL_SELECTION_INDEX_ANNOTATION_TYPE, false, true);
	
	boolean isUsingMemberName;
	boolean isOutjection;
	
	BijectedAttributeType(String annotationType, boolean isUsingMemberName, boolean isOutjection) {
		this.annotationType = annotationType;
		this.isUsingMemberName = isUsingMemberName;
		this.isOutjection = isOutjection;
	}

	String annotationType;
	public String getAnnotationType() {
		return annotationType;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isUsingMemberName() {
		return isUsingMemberName;
	}

	public boolean isOut() {
		return isOutjection;
	}

}
