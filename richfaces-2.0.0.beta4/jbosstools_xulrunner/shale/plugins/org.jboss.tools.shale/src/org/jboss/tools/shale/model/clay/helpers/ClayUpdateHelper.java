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
package org.jboss.tools.shale.model.clay.helpers;

import java.util.Set;
import org.jboss.tools.shale.model.clay.ClayProcessImpl;

public class ClayUpdateHelper implements IClayComponentSetListener{
	ClayProcessImpl process;
	ClayProcessHelper helper;

	public ClayUpdateHelper(ClayProcessImpl process) {
		this.process = process;
		this.helper = process.getHelper();
		ClayUpdateManager.getInstance(process.getModel()).register("", this);
		ClayComponentSet.getInstance(process.getModel()).addClayComponentSetListener(this);
	}
	
	public void unregister() {
		ClayUpdateManager.getInstance(process.getModel()).unregister(this);
		ClayComponentSet.getInstance(process.getModel()).removeClayComponentSetListener(this);
	}

	public void update() {
		helper.updateProcess();
	}

	public void componentsChanged(Set removed, Set added) {
		if(helper.isDisposed()) {
			unregister();
		} else {
			helper.updateTargets();
		}
	}

}
