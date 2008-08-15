package org.jboss.tools.seam.pages.xml.model.handlers;

import java.util.Properties;

import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.seam.pages.xml.model.SeamPagesConstants;
import org.jboss.tools.seam.pages.xml.model.helpers.SeamPagesDiagramStructureHelper;

public class CreateVirtualHandler extends AbstractHandler {

	public CreateVirtualHandler() {}

	public boolean isEnabled(XModelObject object) {
		return object != null && object.isActive();
	}

	public void executeHandler(XModelObject object, Properties prop) throws XModelException {
		XModelObject f = SeamPagesDiagramStructureHelper.getInstance().getParentFile(object);
		if(f == null) return;
		XModelObject pages = f.getChildByPath(SeamPagesConstants.FOLDER_PAGES);
		if(pages == null) return;
		String path = object.getAttributeValue(SeamPagesConstants.ATTR_PATH);
		//TODO handle EL case
		XModelObject page = AddViewSupport.addPage(pages, path);
		if(page != null) {
			page.setModified(true);
		}
	}

}
