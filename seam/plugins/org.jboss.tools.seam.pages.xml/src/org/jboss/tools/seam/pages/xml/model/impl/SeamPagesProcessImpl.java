package org.jboss.tools.seam.pages.xml.model.impl;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.impl.OrderedObjectImpl;
import org.jboss.tools.jst.web.model.WebProcess;
import org.jboss.tools.seam.pages.xml.model.SeamPagesConstants;
import org.jboss.tools.seam.pages.xml.model.helpers.SeamPagesProcessHelper;
import org.jboss.tools.seam.pages.xml.model.helpers.SeamPagesUpdateHelper;

public class SeamPagesProcessImpl extends OrderedObjectImpl implements WebProcess, SeamPagesConstants {
	private static final long serialVersionUID = 1981573715076399163L;
	protected XModelObject reference;
	protected SeamPagesProcessHelper phelper = new SeamPagesProcessHelper(this);
	protected SeamPagesUpdateHelper uhelper = null;
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
		phelper.autolayout();
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
			uhelper = new SeamPagesUpdateHelper(this);
		}
	}
    
	protected void deactivate() {
		if (uhelper != null) {
			uhelper.unregister();
			uhelper = null;
		}
	}
    
	public SeamPagesProcessHelper getHelper() {
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
