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
package org.jboss.tools.shale.model.clay;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.impl.OrderedObjectImpl;
import org.jboss.tools.shale.model.clay.helpers.*;
import org.jboss.tools.jst.web.model.ReferenceObject;
import org.jboss.tools.jst.web.model.WebProcess;

public class ClayProcessImpl extends OrderedObjectImpl implements WebProcess, ReferenceObject, ShaleClayConstants {
	private static final long serialVersionUID = 1L;
	protected XModelObject reference;
	protected ClayProcessHelper phelper = new ClayProcessHelper(this);
	protected ClayUpdateHelper uhelper = null;
	protected boolean isPrepared = false;

	public XModelObject getReference() {
		return reference;
	}

	public void setReference(XModelObject reference) {
		this.reference = reference;
		if(reference != null) {
			String shape = get("SHAPE");
			if(shape != null && shape.length() > 0) reference.set("_shape", shape);
		}
	}

	public boolean isPrepared() {
		return isPrepared;
	}
    
	public void firePrepared() {
		 isPrepared = true;
		 fireStructureChanged(3, getPath());
	}
    
	public void autolayout() {
///		phelper.autolayout();
	}

	protected void loadChildren() {
		if (isPrepared && reference == null && isActive()) {
			restoreRefs();
			registerListener();
			updateProcess();
		}
	}
    
	protected void restoreRefs() {
		phelper.restoreRefs();
	}
    
	protected void updateProcess() {
		phelper.updateProcess();
	}
    
	protected void registerListener() {
		if (uhelper == null) {
			uhelper = new ClayUpdateHelper(this);
		}
	}
    
	protected void deactivate() {
		if (uhelper != null) {
			uhelper.unregister();
			uhelper = null;
		}
	}
    
	public ClayProcessHelper getHelper() {
		return phelper;
	}

	protected void changeTimeStamp() {
		boolean actualBody = false;
		String abts = null;
		XModelObject parent = (XModelObject)getParent();
		if(parent != null) {
			abts = parent.get("actualBodyTimeStamp");
			actualBody = (abts != null && (abts.equals("0") || abts.equals("" + parent.getTimeStamp())));
		}
		super.changeTimeStamp();
		if(actualBody && !abts.equals("0")) {
			parent.set("actualBodyTimeStamp", "" + parent.getTimeStamp());
		}
	}
    
}
