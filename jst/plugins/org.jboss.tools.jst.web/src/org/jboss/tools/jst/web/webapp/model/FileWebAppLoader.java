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
package org.jboss.tools.jst.web.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.*;

import org.jboss.tools.common.meta.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.impl.XModelObjectImpl;
import org.jboss.tools.common.model.loaders.impl.SimpleWebFileLoader;
import org.jboss.tools.common.model.util.*;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

public class FileWebAppLoader extends SimpleWebFileLoader {

    protected XModelObjectLoaderUtil createUtil() {
    	FWLoaderUtil util = new FWLoaderUtil();
    	if(!isCheckingDTD()) util.schema = true;
        return util;
    }

}

class FWLoaderUtil extends XModelObjectLoaderUtil {
	boolean schema = false;

    protected Set<String> getAllowedChildren(XModelEntity entity) {
    	Set<String> children = super.getAllowedChildren(entity);
    	if(entity.getName().startsWith("WebAppResourceCollection")) { //$NON-NLS-1$
    		children.add("url-pattern"); //$NON-NLS-1$
    		children.add("http-method"); //$NON-NLS-1$
    		if("WebAppResourceCollection30".equals(entity.getName())) { //$NON-NLS-1$
    			children.add("http-method-omission"); //$NON-NLS-1$
    		}
    	} else if("WebAppServiceRef".equals(entity.getName())) { //$NON-NLS-1$
    		children.add("port-component-ref"); //$NON-NLS-1$
    		children.add("handler"); //$NON-NLS-1$
    	} else if("WebAppFilterMapping24".equals(entity.getName())) { //$NON-NLS-1$
    		children.add("dispatcher"); //$NON-NLS-1$
    	} else if(entity.getName().startsWith("FileWebApp")) { //$NON-NLS-1$
    		children.add("distributable"); //$NON-NLS-1$
    	} else if(entity.getName().startsWith("WebAppAbsoluteOrdering")) { //$NON-NLS-1$
    		children.add("others"); //$NON-NLS-1$
    	} else if(entity.getName().equals("WebAppSessionConfig30")) { //$NON-NLS-1$
    		children.add("tracking-mode"); //$NON-NLS-1$
    	}    	
    	return children;
    }

    protected boolean isSaveable(XModelEntity entity, String n, String v, String dv) {
		if(v == null) return false;
		if(v.length() == 0 || v.equals(dv)) {
			XAttribute attr = entity.getAttribute(n);
			return (attr != null && "always".equals(attr.getProperty("save"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return super.isSaveable(entity, n, v, dv);
    }

    public String getAttribute(Element element, String xmlname, XAttribute attr) {
        if("distributable".equals(xmlname)) //$NON-NLS-1$
          return (XMLUtil.getUniqueChild(element, "distributable") != null) ? "yes" : "no"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        if("others".equals(xmlname)) //$NON-NLS-1$
            return (XMLUtil.getUniqueChild(element, "others") != null) ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        if("role-names".equals(xmlname)) //$NON-NLS-1$
          return loadArray(element, "role-name"); //$NON-NLS-1$
        if("url-patterns".equals(xmlname)) //$NON-NLS-1$
          return loadArray(element, "url-pattern"); //$NON-NLS-1$
        if("http-methods".equals(xmlname)) //$NON-NLS-1$
          return loadArray(element, "http-method"); //$NON-NLS-1$
        if("http-method-omissions".equals(xmlname)) //$NON-NLS-1$
            return loadArray(element, "http-method-omission"); //$NON-NLS-1$
		if("handlers".equals(xmlname)) //$NON-NLS-1$
		  return loadArray(element, "handler"); //$NON-NLS-1$
		if("port-component-refs".equals(xmlname)) //$NON-NLS-1$
		  return loadArray(element, "port-component-ref"); //$NON-NLS-1$
		if("dispatchers".equals(xmlname)) //$NON-NLS-1$
		  return loadArray(element, "dispatcher"); //$NON-NLS-1$
	    if("tracking-modes".equals(xmlname)) //$NON-NLS-1$
	      return loadArray(element, "tracking-mode"); //$NON-NLS-1$
        return super.getAttribute(element, xmlname, attr);
    }

    public void saveAttributes(Element element, XModelObject o) {
        super.saveAttributes(element, o);
		String entity = o.getModelEntity().getName();
		if("WebAppErrorPage".equals(entity)) { //$NON-NLS-1$
			eitherOr(element, WebAppConstants.ERROR_CODE, WebAppConstants.EXCEPTION_TYPE);
		} else if(entity.startsWith(WebAppHelper.FILTER_ENTITY)) { // both filter and filter mapping
			eitherOr(element, WebAppConstants.URL_PATTERN, WebAppConstants.SERVLET_NAME);
		} else if(WebAppHelper.SERVLET_ENTITY.equals(entity) || WebAppHelper.SERVLET_30_ENTITY.equals(entity)) {
			eitherOr(element, WebAppConstants.SERVLET_CLASS, WebAppConstants.JSP_FILE);
		}
		if(schema) {
			if(o.getModelEntity().getAttribute("description") != null && !o.getModelEntity().getName().startsWith("WebAppResourceCollection")) { //$NON-NLS-1$ //$NON-NLS-2$
				makeChildrenFirst(element, new String[]{"description", "display-name", "icon"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			if("WebAppEnvEntry".equals(o.getModelEntity().getName())) { //$NON-NLS-1$
				Element e = XMLUtilities.getUniqueChild(element, "env-entry-value"); //$NON-NLS-1$
				if(e != null) {
					element.removeChild(e);
					element.appendChild(e);
				}
			}
		}
    }

    public void saveAttribute(Element element, String xmlname, String value) {
        if("distributable".equals(xmlname)) { //$NON-NLS-1$
            if("yes".equals(value)) XMLUtil.createElement(element, "distributable"); //$NON-NLS-1$ //$NON-NLS-2$
        } else if("others".equals(xmlname)) { //$NON-NLS-1$
            if("true".equals(value)) XMLUtil.createElement(element, "others"); //$NON-NLS-1$ //$NON-NLS-2$
        } else if("role-names".equals(xmlname)) { //$NON-NLS-1$
            saveArray(element, "role-name", value); //$NON-NLS-1$
        } else if("url-patterns".equals(xmlname)) { //$NON-NLS-1$
            saveArray(element, "url-pattern", value); //$NON-NLS-1$
        } else if("http-methods".equals(xmlname)) { //$NON-NLS-1$
            saveArray(element, "http-method", value); //$NON-NLS-1$
        } else if("http-method-omissions".equals(xmlname)) { //$NON-NLS-1$
            saveArray(element, "http-method-omission", value); //$NON-NLS-1$
		} else if("handlers".equals(xmlname)) { //$NON-NLS-1$
			saveArray(element, "handler", value); //$NON-NLS-1$
		} else if("port-component-refs".equals(xmlname)) { //$NON-NLS-1$
			saveArray(element, "port-component-ref", value); //$NON-NLS-1$
		} else if("dispatchers".equals(xmlname)) { //$NON-NLS-1$
			saveArray(element, "dispatcher", value); //$NON-NLS-1$
        } else if("tracking-modes".equals(xmlname)) { //$NON-NLS-1$
            saveArray(element, "tracking-mode", value); //$NON-NLS-1$
        } else {
            super.saveAttribute(element, xmlname, value);
        }
    }

    public boolean saveChildren(Element element, XModelObject o) {
    	String entity = o.getModelEntity().getName();
    	if(entity.startsWith("FileWebApp")) { //$NON-NLS-1$
    		return saveWebAppChildren(element, o);
    	} else if(WebAppHelper.FILTER_MAPPING_24_ENTITY.equals(entity)) {
    		
    		return true;
    	} else if(entity.equals(WebAppHelper.SERVLET_ENTITY) || entity.equals(WebAppHelper.SERVLET_30_ENTITY)) {
    		boolean b = super.saveChildren(element, o);
    		String afterName = null;
    		String[] cs = {"run-as", "security-role-ref", "multipart-config"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    		for (int i = 0; i < cs.length && afterName == null; i++) if(XMLUtilities.getChildren(element, cs[i]).length > 0) {
    			afterName = cs[i];
    		}
    		String[] attrs = {"load-on-startup", "enabled", "async-supported"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    		for (String a: attrs) if(o.getModelEntity().getAttribute(a) != null) {
    			moveChild(element, a, afterName);
    		}
    	} else {
    		boolean b = super.saveChildren(element, o);
    		if(o.getModelEntity().getAttribute("mapped-name") != null) { //$NON-NLS-1$
    			moveChild(element, "mapped-name", "injection-target"); //$NON-NLS-1$ //$NON-NLS-2$
    		}
    		if(o.getModelEntity().getAttribute("lookup-name") != null) { //$NON-NLS-1$
				moveChild(element, "lookup-name", null); //$NON-NLS-1$
    		}
    		if(o.getModelEntity().getAttribute("tracking-modes") != null) { //$NON-NLS-1$
    			moveChild(element, "tracking-mode", null); //$NON-NLS-1$ //$NON-NLS-2$
    		}
    		return b;
    	}
        return true;
    }

    protected void saveChildren(Element element, XModelObject o, String entity) {
        XModelObject[] os = o.getChildren(entity);
        for (int i = 0; i < os.length; i++) save(element, os[i]);
    }
    
    private boolean saveWebAppChildren(Element element, XModelObject o) {
        XModelObject[] os = o.getChildrenForSave();
        boolean b = true;
        for (int i = 0; i < os.length; i++) {
        	String xmlname = os[i].getModelEntity().getXMLSubPath();
        	if(xmlname == null || xmlname.length() == 0) {
        		if(!super.saveChildren(element, os[i])) b = false;
        	} else {
        		if(!save(element, os[i])) b = false;
        	}
        }
        return b;
    }
    
    public void loadChildren(Element element, XModelObject o) {
    	String entity = o.getModelEntity().getName();
    	if(entity.startsWith("FileWebApp")) { //$NON-NLS-1$
			addRequiredChildren(o, true);
    		XModelObject[] os = o.getChildren();
    		for (int i = 0; i < os.length; i++) {
            	String xmlname = os[i].getModelEntity().getXMLSubPath();
            	if(xmlname == null || xmlname.length() == 0) {
            		super.loadChildren(element, os[i]);
            	}
    		}
    		super.loadChildren(element, o);
    	} else {
    		super.loadChildren(element, o);
    	}
    }
    
   	private void makeChildrenFirst(Element element, String[] names) {
    	Element[] es = new Element[names.length + 1];
    	int[] indices = new int[names.length + 1];
    	for (int k = 0; k < indices.length; k++) indices[k] = -1;
   		NodeList l = element.getChildNodes();
   		for (int i = 0; i < l.getLength(); i++) {
    		Node n = l.item(i);
    		if(n.getNodeType() != Node.ELEMENT_NODE) continue;
    		int index = getIndex(n.getNodeName(), names);
    		if(index < 0) index = names.length;
    		if(es[index] == null) {
    			es[index] = (Element)n;
    			for (int k = index - 1; k >= 0; k--) {
    				if(es[k] == null && (indices[k] > index || indices[k] < 0)) {
    					indices[k] = index; 
    				}
    			}
    		}
    	}
   		for (int i = names.length - 1; i >= 0; i--) {
   			if(es[i] == null || indices[i] < 0) continue;
   	    	element.removeChild(es[i]);
   	    	element.insertBefore(es[i], es[indices[i]]);
			for (int k = i - 1; k >= 0; k--) {
				if(es[k] != null && indices[k] > indices[i]) indices[k] = indices[i]; 
			}
   		}
    }
    
    private int getIndex(String name, String[] names) {
    	for (int i = 0; i < names.length; i++) {
    		if(names[i].equals(name)) return i;
    	}
    	return -1;
    }

   	private void moveChild(Element element, String child, String childAfter) {
   		NodeList l = element.getChildNodes();
   		List<Element> childE = null;
   		Element childAfterE = null;
   		for (int i = 0; i < l.getLength(); i++) {
    		Node n = l.item(i);
    		if(n.getNodeType() != Node.ELEMENT_NODE) continue;
    		String name = n.getNodeName();
    		if(child.equals(name)) {
    			if(childE == null) childE = new ArrayList<Element>();
    			childE.add((Element)n);
    		} else if(name.equals(childAfter)) {
    			if(childAfterE == null) childAfterE = (Element)n;
    		}
    	}
   		if(childE != null) for (Element c: childE) {
   			element.removeChild(c);
   			if(childAfterE != null) {
   				element.insertBefore(c, childAfterE);
   			} else {
   				element.appendChild(c);
   			}
   		}
    }
    
    public boolean save(Element parent, XModelObject o) {
    	if(!needToSave(o)) return true;
    	return super.save(parent, o);
    }

    boolean needToSave(XModelObject o) {
    	if(o == null) return false;
    	String entity = o.getModelEntity().getName();
    	if("WebAppSessionConfig".equals(entity) || "WebAppLoginConfig".equals(entity)) { //$NON-NLS-1$ //$NON-NLS-2$
    		return hasSetAttributes(o); 
    	} else if("WebAppWelcomFileList".equals(entity) || "WebAppLocaleEncodingMappingList".equals(entity)) { //$NON-NLS-1$ //$NON-NLS-2$
    		return (o.getChildren().length > 0);
    	} else if("WebAppJspConfig".equals(entity)) { //$NON-NLS-1$
    		return (o.getChildren().length > 0);
    	}
    	return true;
    }
    
    private boolean hasSetAttributes(XModelObject o) {
    	XAttribute[] as = o.getModelEntity().getAttributes();
    	for (int i = 0; i < as.length; i++) {
    		String xml = as[i].getXMLName();
    		// it would be more safe to check isSavable
    		if(xml == null || xml.length() == 0 || "NAME".equals(xml)) continue; //$NON-NLS-1$
    		String v = o.getAttributeValue(as[i].getName());
    		if(v != null && v.length() > 0) return true;
    	}
    	String finalComment = o.get("#final-comment"); //$NON-NLS-1$
    	if(finalComment != null && finalComment.length() > 0) return true;
    	return false;
    }

}
