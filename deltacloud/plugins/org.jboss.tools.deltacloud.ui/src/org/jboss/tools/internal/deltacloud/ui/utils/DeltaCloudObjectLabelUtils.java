/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.utils;

import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

/**
 * @author André Dietisheim
 */
public class DeltaCloudObjectLabelUtils {
	public static String getLabel(DeltaCloudImage image) {
		StringBuilder builder = new StringBuilder();
		if (image != null) {
			if (image.getName() != null) {
				builder.append(image.getName()).append(' ');
			}
			if (image.getId() != null) {
				builder.append('[').append(image.getId()).append(']');
			}
		}
		return builder.toString();
	}

	public static String getLabel(DeltaCloudInstance instance) {
		StringBuilder sb = new StringBuilder();
		if (instance != null) {
			String alias = instance.getAlias();
			if (alias != null) {
				sb.append(alias).append(' ');
			}
			String name = instance.getName();
			if (name != null) {
				if (alias == null) {
					sb.append(name);
				} else if (!alias.equals(name)) {
					sb.append('[').append(name).append(']');
				}
			}
			if (instance.getId() != null) {
				sb.append('[').append(instance.getId()).append(']');
			}
		}
		return sb.toString();
	}
}
