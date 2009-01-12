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
package org.jboss.tools.jsf.model.pv;

import java.util.*;
import org.eclipse.jdt.core.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.impl.RegularObjectImpl;
import org.jboss.tools.common.model.util.EclipseJavaUtil;
import org.jboss.tools.jsf.JSFModelPlugin;

public class JSFProjectBean extends RegularObjectImpl {
	private static final long serialVersionUID = 3044316361003259426L;
	protected JSFProjectBeans beans = null;
	protected XModelObject[] beanList = new XModelObject[0];
	protected IType type = null;
	protected boolean isLoading = false;
	protected boolean loaded = false;
	
	public String getPresentationString() {
		String s = "" + getAttributeValue("class name");
		int d = s.lastIndexOf('.');
		if(d >= 0) s = s.substring(d + 1);
		return s;
	}
	
    public boolean isAttributeEditable(String name) {
        return false;
    }

    public void setBeans(JSFProjectBeans beans) {
		this.beans = beans;
	}
    
    void cleanBeans() {
    	beanList = new XModelObject[0];
    }
	
	void addBean(XModelObject bean) {
		XModelObject[] l = new XModelObject[beanList.length + 1];
		System.arraycopy(beanList, 0, l, 0, beanList.length);
		l[beanList.length] = bean;
		beanList = l;
	}
	
	public XModelObject[] getBeanList() {
		return beanList;
	}
	
	public void setType(IType type) {
		this.type = type;
		if(loaded) {
			try {
				update();
			} catch (JavaModelException e) {
				JSFModelPlugin.getPluginLog().logError(e);
			}
		}
	}
	
	protected void loadChildren() {
		if(loaded) return;
		loaded = true;
		isLoading = true;
		try {
			update();
		} catch (JavaModelException e) {
			JSFModelPlugin.getPluginLog().logError(e);
		}
		isLoading = false;
	}
	
	public boolean hasChildren() {
		if(type == null) return false;
		if(!loaded) return true;
		return super.hasChildren(); 
	}

	public void update() throws JavaModelException {
		if(!loaded) return;
		Map<String,XModelObject> map = new HashMap<String,XModelObject>();
		Set<String> properties = new HashSet<String>();
		XModelObject[] cs = getChildren();
		for (int i = 0; i < cs.length; i++) map.put(cs[i].getPathPart(), cs[i]);
		if(type != null) {
			IType _type = type;
			while(_type != null) {
			IField[] fs = _type.getFields();
			if(fs != null) for (int i = 0; i < fs.length; i++) {
				String n = fs[i].getElementName();
				if(properties.contains(n)) continue;
				JSFProjectBeanMember c = (JSFProjectBeanMember)map.get(n);
				if(c != null && !c.getModelEntity().getName().equals("JSFProjectBeanProperty")) {
					c.removeFromParent();
					map.remove(n);
					c = null;
				}
				if(c != null) {
					map.remove(n);
					String typeName = EclipseJavaUtil.getMemberTypeAsString(fs[i]);
					c.setType(beans.getType(typeName));
					if(typeName == null) typeName = "";
					c.setAttributeValue("class name", typeName);
					c.setAttributeValue("declaring class", fs[i].getDeclaringType().getFullyQualifiedName());
				} else {
					c = createMember(n, fs[i], "JSFProjectBeanProperty"); 
				}
				properties.add(n);
			}
			IMethod[] ms = _type.getMethods();
			if(ms != null) for (int i = 0; i < ms.length; i++) {
				if(ms[i].isConstructor()) continue;
				if(!Flags.isPublic(ms[i].getFlags()) && !_type.isInterface()) continue;				
				String n = ms[i].getElementName();
				boolean isProperty = false;
				if((isGetter(ms[i], "get") || isSetter(ms[i])) && n.length() > 3) {
					n = n.substring(3, 4).toLowerCase() + n.substring(4);
					isProperty = true;
				} else if(isGetter(ms[i], "is")) {
					String typeName = EclipseJavaUtil.getMemberTypeAsString(ms[i]);
					if("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
						n = n.substring(2, 3).toLowerCase() + n.substring(3);
						isProperty = true;
					}
				}
				String entity = (isProperty) ? "JSFProjectBeanProperty" : "JSFProjectBeanMethod";
				JSFProjectBeanMember c = (JSFProjectBeanMember)map.get(n);
				if(c != null && !c.getModelEntity().getName().equals(entity)) {
					c.removeFromParent();
					map.remove(n);
					c = null;
				}
				if(c != null) {
					map.remove(n);
					String typeName = EclipseJavaUtil.getMemberTypeAsString(ms[i]);
					c.setType(beans.getType(typeName));
					if(typeName == null) typeName = "";
					c.setAttributeValue("class name", typeName);
					c.setMember(ms[i]);
					c.setAttributeValue("declaring class", ms[i].getDeclaringType().getFullyQualifiedName());
				} else if(!properties.contains(n)) {
					c = createMember(n, ms[i], entity);
				}
				if(isProperty) {
					properties.add(n);
				} else {
					//add it anyway
					properties.add(n);
				}
			}
			
				String sc = _type.getSuperclassName();
				if(sc == null || sc.length() == 0 || "java.lang.Object".equals(sc)) break;
				sc = EclipseJavaUtil.resolveType(_type, sc);
				if(sc == null || sc.length() == 0 || "java.lang.Object".equals(sc)) break;
				_type = beans.getType(sc);
			}
		} else if(beanList.length > 0) {
			XModelObject[] ps = beanList[0].getChildren();
			for (int i = 0; i < ps.length; i++) {
				String n = ps[i].getPathPart();
				JSFProjectBeanMember c = (JSFProjectBeanMember)map.get(n);
				if(c != null) {
					map.remove(n);
				} else {
					c = (JSFProjectBeanMember)getModel().createModelObject("JSFProjectBeanProperty", null);
					c.setAttributeValue("name", n);
					c.setBeans(beans);
					c.setType(null);
					addChild(c);
				}
			}
		}
		Iterator<XModelObject> it = map.values().iterator();
		while(it.hasNext()) {
			it.next().removeFromParent();
		}
	}
	
	JSFProjectBeanMember createMember(String n, IMember member, String entity) {
		JSFProjectBeanMember c = (JSFProjectBeanMember)getModel().createModelObject(entity, null);
		c.setAttributeValue("name", n);
		c.setBeans(beans);
		String className = EclipseJavaUtil.getMemberTypeAsString(member);
		c.setAttributeValue("class name", (className == null) ? "" : className);
		c.setAttributeValue("declaring class", member.getDeclaringType().getFullyQualifiedName());
		c.setType(beans.getType(className));
		c.setMember(member);
		if(isLoading) addChild_0(c); else addChild(c);
		return c;
	}
	
	private boolean isGetter(IMethod method, String pref) {
		if(method == null) return false;
		String name = method.getElementName();
		if(!name.startsWith(pref) || name.length() <= pref.length()) return false;
		try {
			String[] ps = method.getParameterNames();
			if(ps == null || ps.length != 0) return false;
			String t = EclipseJavaUtil.getMemberTypeAsString(method);
			if(t == null || t.equals("void")) return false;
		} catch (JavaModelException e) {
			return false;
		}
		
		return true;
	}

	private boolean isSetter(IMethod method) {
		if(method == null) return false;
		String name = method.getElementName();
		if(!name.startsWith("set") || name.length() <= 3) return false;
		try {
			String[] ps = method.getParameterNames();
			if(ps == null || ps.length != 1) return false;
		} catch (JavaModelException e) {
			return false;
		}
		
		return true;
	}

}
