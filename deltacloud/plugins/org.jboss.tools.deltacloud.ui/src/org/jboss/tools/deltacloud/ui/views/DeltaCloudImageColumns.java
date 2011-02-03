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

/**
 * @author André Dietisheim
 */
public class DeltaCloudImageColumns extends Columns<DeltaCloudImage> {

	@SuppressWarnings("unchecked")
	public DeltaCloudImageColumns() {
		super(
				new Column<DeltaCloudImage>("DeltaCloudElementColumn.name.label", 20) {

					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getName();
					}
				},
				new Column<DeltaCloudImage>("DeltaCloudElementColumn.id.label", 20) {

					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getId();
					}
				},
				new Column<DeltaCloudImage>("DeltaCloudElementColumn.architecture.label", 20) {

					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getArchitecture();
					}
				},
				new Column<DeltaCloudImage>("DeltaCloudElementColumn.description.label", 40) {
					@Override
					public String getColumnText(DeltaCloudImage image) {
						return image.getDescription();
					}
				});
	}
}
