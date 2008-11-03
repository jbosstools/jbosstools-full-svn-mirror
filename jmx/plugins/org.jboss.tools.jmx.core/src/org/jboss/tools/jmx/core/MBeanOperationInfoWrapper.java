/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.jboss.tools.jmx.core;

import javax.management.MBeanOperationInfo;

import org.eclipse.core.runtime.Assert;

public class MBeanOperationInfoWrapper extends MBeanFeatureInfoWrapper {

    private MBeanOperationInfo info;

    public MBeanOperationInfoWrapper(MBeanOperationInfo info,
            MBeanInfoWrapper wrapper) {
        super(wrapper);
        Assert.isNotNull(info);
        this.info = info;
    }

    public MBeanOperationInfo getMBeanOperationInfo() {
        return info;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((info == null) ? 0 : info.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MBeanOperationInfoWrapper))
            return false;
        final MBeanOperationInfoWrapper other = (MBeanOperationInfoWrapper) obj;
        if (info == null) {
            if (other.info != null)
                return false;
        } else if (!info.equals(other.info))
            return false;
        return true;
    }
}
