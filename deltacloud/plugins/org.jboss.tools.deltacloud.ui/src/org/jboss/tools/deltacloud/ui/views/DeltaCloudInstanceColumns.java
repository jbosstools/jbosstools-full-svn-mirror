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

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance.State;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

/**
 * @author André Dietisheim
 */
public class DeltaCloudInstanceColumns extends Columns<DeltaCloudInstance> {

	@SuppressWarnings("unchecked")
	public DeltaCloudInstanceColumns() {
		super(
				new Column<DeltaCloudInstance>("NAME", 20) {

					@Override
					public String getColumnText(DeltaCloudInstance image) {
						return image.getName();
					}
				},
				new Column<DeltaCloudInstance>("ALIAS", 20) {

					@Override
					public String getColumnText(DeltaCloudInstance image) {
						return image.getAlias();
					}
				},
				new Column<DeltaCloudInstance>("STATUS", 20) {

					@Override
					public Image getColumnImage(DeltaCloudInstance instance) {
						State state = instance.getState();
						if (DeltaCloudInstance.State.STOPPED.equals(state)) {
							return SWTImagesFactory.get(SWTImagesFactory.IMG_STOPPED);
						} else if (DeltaCloudInstance.State.RUNNING.equals(state)) {
							return SWTImagesFactory.get(SWTImagesFactory.IMG_RUNNING);
						} else if (DeltaCloudInstance.State.BOGUS.equals(state)) {
							return PlatformUI.getWorkbench().getSharedImages().
										getImage(ISharedImages.IMG_DEC_FIELD_WARNING);
						} else {
							return null;
						}
					}

					@Override
					public String getColumnText(DeltaCloudInstance image) {
						return image.getState().toString();
					}
				},
				new Column<DeltaCloudInstance>("HOSTNAME", 40) {
					@Override
					public String getColumnText(DeltaCloudInstance image) {
						return image.getHostName();
					}
				});
	}
}
