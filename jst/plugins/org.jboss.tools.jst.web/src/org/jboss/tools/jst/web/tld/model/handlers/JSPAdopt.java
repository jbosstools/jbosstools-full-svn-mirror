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
package org.jboss.tools.jst.web.tld.model.handlers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.*;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.jst.web.tld.model.*;
import org.jboss.tools.jst.web.tld.model.helpers.*;

public class JSPAdopt implements XAdoptManager {
    TLDToPaletteHelper helper = new TLDToPaletteHelper();

    public JSPAdopt() {}

    public boolean isAdoptable(XModelObject target, XModelObject object) {
        if(!isAcceptableTarget(target)) return false;
        return isAdoptableTag(object) || isAdoptableAttribute(object) 
		|| isAdoptableTaglib(object) || isAdoptableMacro(object) || isAdoptableGroup(object);
    }

    public void adopt(XModelObject target, XModelObject object, java.util.Properties p) {
        if(!isAcceptableTarget(target)) return;
        if(isAdoptableTag(object)) adoptTag(target, object, p);
        else if(isAdoptableAttribute(object)) adoptAttribute(target, object, p);
        else if(isAdoptableTaglib(object)) adoptTaglib(target, object, p);
        else if(isAdoptableMacro(object)) adoptMacro(target, object, p);
        else if(isAdoptableGroup(object)) adoptGroup(target, object, p);
    }

    private boolean isAcceptableTarget(XModelObject target) {
        String te = target.getModelEntity().getName();
        return (te.equals("FileJSP") || te.startsWith("FileHTML")) || target.getFileType() == XModelObject.FILE;
    }

	protected boolean isAdoptableTaglib(XModelObject object) {
		return TLDUtil.isTaglib(object);
	}

    protected boolean isAdoptableTag(XModelObject object) {
        return TLDUtil.isTag(object);
    }

    protected boolean isAdoptableAttribute(XModelObject object) {
        return TLDUtil.isAttribute(object);
    }

    public void adoptTag(XModelObject target, XModelObject object, Properties p) {
        if(p == null) return;
        XModelObject macro = helper.createMacroByTag(object, target.getModel());
        if(macro == null) return;
        p.setProperty(TLDToPaletteHelper.START_TEXT, macro.getAttributeValue(TLDToPaletteHelper.START_TEXT));
        p.setProperty(TLDToPaletteHelper.END_TEXT, macro.getAttributeValue(TLDToPaletteHelper.END_TEXT));
        p.setProperty(TLDToPaletteHelper.REFORMAT, macro.getAttributeValue(TLDToPaletteHelper.REFORMAT));
        p.setProperty(TLDToPaletteHelper.URI, object.getParent().getAttributeValue("uri"));
        p.setProperty(TLDToPaletteHelper.DEFAULT_PREFIX, TLDToPaletteHelper.getTldName(object.getParent()));
        p.setProperty("tag name", macro.getAttributeValue("name"));
    }

    public void adoptAttribute(XModelObject target, XModelObject object, Properties p) {
        if(p == null) return;
        int c = -1;
        try { 
        	c = Integer.parseInt(p.getProperty("pos")); 
        } catch (Exception e) {
        	WebModelPlugin.getPluginLog().logError(e);
        }
        if(c < 0) return;
        String text = p.getProperty("text");
        String pref = (c == 0 || Character.isWhitespace(text.charAt(c - 1))) ? "" : " ";
        String start = pref + object.getAttributeValue("name") + "=\"|\"";
        p.setProperty(TLDToPaletteHelper.START_TEXT, start);
        char ch = (c == text.length()) ? '\0' : text.charAt(c);
        String end = (ch == '\0' || Character.isWhitespace(ch) || ch == '/' || ch == '>') ? "" : " ";
        p.setProperty(TLDToPaletteHelper.END_TEXT, end);
    }

	public void adoptTaglib(XModelObject target, XModelObject object, Properties p) {
		if(p == null) return;
		String uri = object.getAttributeValue("uri");
		String shortname = object.getAttributeValue("shortname");
		String start = "<%@ taglib uri=\"" + uri + "\" prefix=\"" + shortname + "\" %>";
		String overAttr = p.getProperty("context:attrName");
		if(overAttr != null && (overAttr.equals("xmlns") || overAttr.startsWith("xmlns:"))) {
			start = uri;
		}
		p.setProperty(TLDToPaletteHelper.START_TEXT, start);
	}
	
	boolean isAdoptableMacro(XModelObject target) {
		return target.getModelEntity().getName().startsWith("SharableMacro");
	}
	
	void adoptMacro(XModelObject target, XModelObject macro, Properties p) {
		String startText = macro.getAttributeValue(TLDToPaletteHelper.START_TEXT);
        p.setProperty(TLDToPaletteHelper.START_TEXT, startText);
        p.setProperty(TLDToPaletteHelper.END_TEXT, macro.getAttributeValue(TLDToPaletteHelper.END_TEXT));
        p.setProperty(TLDToPaletteHelper.REFORMAT, macro.getAttributeValue(TLDToPaletteHelper.REFORMAT));
        p.setProperty(TLDToPaletteHelper.URI, macro.getParent().getAttributeValue(URIConstants.LIBRARY_URI));
        p.setProperty(URIConstants.LIBRARY_VERSION, "" + macro.getParent().getAttributeValue(URIConstants.LIBRARY_VERSION));
        p.setProperty(TLDToPaletteHelper.DEFAULT_PREFIX, macro.getParent().getAttributeValue(TLDToPaletteHelper.DEFAULT_PREFIX));
        String addTaglib = macro.getParent().getAttributeValue(TLDToPaletteHelper.ADD_TAGLIB);
        if(addTaglib != null) p.setProperty(TLDToPaletteHelper.ADD_TAGLIB, addTaglib);
        String name = macro.getAttributeValue("name");
        if(isTagName(startText, name)) {
        	p.setProperty("tag name", name);
        }
	}
	
	private boolean isTagName(String s, String n) {
		if(n == null || n.length() == 0 || s == null) return false;
		n = n.toLowerCase();
		s = s.toLowerCase();
		for (int i = 0; i < n.length(); i++) {
			char c = n.charAt(i);
			if(!Character.isJavaIdentifierPart(c) && c != '.' && c != ':') return false;
		}
		int q = s.indexOf('<');
		if(q < 0) return false;
		if(s.indexOf(n, q) < 0) return false;
		return true;
	}

	boolean isAdoptableGroup(XModelObject target) {
		return target.getModelEntity().getName().startsWith("SharableGroup");
	}
	
	void adoptGroup(XModelObject target, XModelObject group, Properties p) {
		String uri = group.getAttributeValue(URIConstants.LIBRARY_URI);
		String shortname = group.getAttributeValue(URIConstants.DEFAULT_PREFIX);
		String start = "<%@ taglib uri=\"" + uri + "\" prefix=\"" + shortname + "\" %>";
        p.setProperty(TLDToPaletteHelper.START_TEXT, start);
	}

}
