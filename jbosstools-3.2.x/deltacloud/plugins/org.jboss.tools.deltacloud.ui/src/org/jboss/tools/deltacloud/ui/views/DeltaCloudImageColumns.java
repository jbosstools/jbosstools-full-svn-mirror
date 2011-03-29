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
package org.jboss.tools.deltacloud.ui.views;

import org.jboss.tools.deltacloud.core.DeltaCloudImage;

public class DeltaCloudImageColumns extends Columns<DeltaCloudImage> {

	@SuppressWarnings("unchecked")
	public DeltaCloudImageColumns() {
		super(
				new Column<DeltaCloudImage>("NAME", 20) {

					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getName();
					}
				},
				new Column<DeltaCloudImage>("ID", 20) {

					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getId();
					}
				},
				new Column<DeltaCloudImage>("ARCH", 20) {

					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getArchitecture();
					}
				},
				new Column<DeltaCloudImage>("DESC", 40) {
					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getDescription();
					}
				});
	}
}
