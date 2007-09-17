/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.common.model.icons.impl;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.meta.impl.XEntityRendererImpl;
import org.jboss.tools.common.model.XModelObject;

/**
 * @author Viacheslav Kabanovich
 */
public class LargeReferencedIcons implements ImageComponent {
	ReferencedIcons ri = new ReferencedIcons();
	private XStudioIcons studioicons = new XStudioIcons();

	public LargeReferencedIcons() {}

	public int getHash(XModelObject obj) {
		String x = obj.getAttributeValue("large icon");
		if (x == null || x.trim().length() == 0) {
			return ri.getHash(obj);
		}
		XModelObject r = obj.getModel().getByPath(x);
		return (r == null) ? "defaultimage".hashCode() : 718 + x.hashCode() + studioicons.getHash(r);
	}

	public Image getImage(XModelObject obj) {
		String v = obj.getAttributeValue("large icon");
		if(v == null || v.trim().length() == 0) {
			return ri.getImage(obj);
		}
		XModelObject ic = (v == null || v.trim().length() == 0) ? null : obj.getModel().getByPath(v);
		Image res = (ic == null || ic == obj) ? null : new XModelObjectIcon(ic).getEclipseImage();
		if(res != null) return res;
		return obj.getModelEntity().getMetaModel().getIconList().getImage("default.unknown");
	}

}
