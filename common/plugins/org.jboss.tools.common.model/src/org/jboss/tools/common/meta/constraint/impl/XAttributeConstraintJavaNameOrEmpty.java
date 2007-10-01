/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.meta.constraint.impl;

public class XAttributeConstraintJavaNameOrEmpty extends XAttributeConstraintJavaName {

    public XAttributeConstraintJavaNameOrEmpty() {}

    public String getError(String value) {
        return (value.length() == 0) ? null : super.getError(value);
    }

}

