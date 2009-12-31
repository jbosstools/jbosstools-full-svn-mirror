/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.model.freemarker;

import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.w3c.dom.Attr;

/**
 * @author Dart
 *
 */
public class FreemarkerAttrModel extends TagPropertyObject implements IFreemarkerTemplateModel {

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.editors.model.freemarker.IFreemarkerTemplateModel#isManyOccurs()
	 */
	public boolean isManyOccurs() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.editors.model.freemarker.IFreemarkerTemplateModel#isRequired()
	 */
	public boolean isRequired() {
		Attr element = this.getReferenceAttibute();
		if (element != null) {
			String value = element.getValue();
			if(value != null){
				value = value.trim();
			}
			if(FreemarkerModelAnalyzer.REQUIRED.equals(value)){
				return true;
			}
		}
		return false;
	}

	public boolean isHidden(RootModel graphRoot) {
		return false;
	}

}
