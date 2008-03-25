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
package org.jboss.tools.common.model.impl;

import java.io.*;
import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertySource;

import org.jboss.tools.common.meta.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.adapter.IModelObjectAdapter;
import org.jboss.tools.common.model.adapter.ModelObjectAdapterExtensionPoint;
import org.jboss.tools.common.model.icons.impl.*;
import org.jboss.tools.common.model.util.EclipseResourceUtil;

public class XModelObjectImpl implements XModelObject, Serializable, Cloneable {
    private static final long serialVersionUID = 3860648580262144825L;
//    protected static final String ENTITY = XModelConstants.XMODEL_ENTITY_ATTR;
    private XModel model = null;
    private XModelEntity entity = null;
    private XModelObjectImpl parent = null;
    protected XModelObjectImpl.SP properties = new SProperties();
///    protected Hashtable properties = new Hashtable(10);
    protected boolean modified = false;
    protected long timeStamp = System.currentTimeMillis();
    protected long lastModificationTimeStamp = 0;

    public XModelObjectImpl() {}

    public XModel getModel() {
        return model;
    }

    void setModel(XModel model) {
        this.model = model;
    }

    public XModelEntity getModelEntity() {
        return entity;
    }
    
    public void changeEntity(String name) {
    	if(entity.getName().equals(name)) return;
    	XModelEntity newEntity = entity.getMetaModel().getEntity(name);
    	if(newEntity == null) throw new RuntimeException("Entity " + name + " does not exist.");
    	if(entity.getImplementingClass() != newEntity.getImplementingClass()) { 
			throw new RuntimeException("Cannot convert entity " + entity.getName() + " to " + name + " because they have different implementations.");
    	}
    	Properties p = new Properties();
    	XAttribute[] as = entity.getAttributes();
    	for (int i = 0; i < as.length; i++) {
    		String n = as[i].getName();
    		String v = getAttributeValue(n);
    		if(v != null) p.setProperty(n, v); 
    	}
    	entity = newEntity;
		as = newEntity.getAttributes();
		for (int i = 0; i < as.length; i++) {
			String n = as[i].getName();
			String v = p.getProperty(n);
			if(v == null) v = as[i].getDefaultValue();
			if(v == null) v = "";
			setAttributeValue(n, v);
		}    	
    }

    void setEntityName_0(String entityname) {
        entity = getModel().getMetaData().getEntity(entityname);
        ((SProperties)properties).init(entity);
        onSetEntity(entityname);
    }

    protected void onSetEntity(String entity) {}

    public XModelObject getParent() {
        return parent;
    }

    public void setParent_0(XModelObjectImpl parent) {
        this.parent = parent;
        if(parent != null) {
            if(model != parent.getModel()) setModel(parent.getModel());
        } else {
            modified = false;
        }
    }

    public boolean isActive() {
        return (getParent() != null && getParent().isActive());
    }

    public boolean isObjectEditable() {
        return getParent() == null || getParent().isObjectEditable();
    }

    public boolean isAttributeEditable(String name) {
        return isObjectEditable();
    }

    public int getFileType() {
        return NONE;
    }

    // modification

    public String get(String name) {
        return (String)properties.get(name);
    }

    public void set(String name, String value) {
        if(value != null && value.length() < 100) value = value.intern();
        properties.put(name.intern(), value);
    }

    protected String get_0(String name) {
        XAttribute a = getModelEntity().getAttribute(name);
        return (a == null || a.getAdapter() == null) ? null : a.getAdapter().getProperty(this);
    }

    protected void set_0(String name, String value) {
        XAttribute a = getModelEntity().getAttribute(name);
        if(a != null && a.getAdapter() != null) a.getAdapter().setProperty(this, value);
    }
    
    Map<String,Object> context = null;

	public Object getObject(String name) {
		return (context == null) ? null : context.get(name);
	}
	
	public void setObject(String name, Object o) {
		if(context == null) {
			if(o == null) return;
			context = new HashMap<String,Object>();
		}
		if(o == null) context.remove(name); else context.put(name, o);
	}

    public String getAttributeValue(String name) {
        return get_0(name);
    }

    public String setAttributeValue(String name, String value) {
        if(getModelEntity().getAttribute(name) == null) return "";
        String ov = getAttributeValue(name);
        if(value != null && value.equals(ov)) return ov;
        String path = getPath();
        set_0(name, value);
        String nv = getAttributeValue(name);
        if(nv != null && !nv.equals(ov)) {
            changeTimeStamp();
            if(path != null) ((XModelImpl)getModel()).fireNodeChanged(this, path);
        }
        return nv;
    }

	protected void onAttributeValueEdit(String name, String oldValue, String newValue) {
	}

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean value) {
        if(parent != null && value) {
            parent.setModified(value);
        }
        modified = value;
        if(modified) ++lastModificationTimeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    
    public long getLastModificationTimeStamp() {
    	return lastModificationTimeStamp;
    }

    protected void changeTimeStamp() {
        ++timeStamp;
        if(parent != null) parent.changeTimeStamp();
    }
    
    protected void safeChangeTimeStamp() {
    	changeTimeStamp();
    }
    
    

    // children

    public XModelObject[] getChildren() {
        return new XModelObject[0];
    }

    public XModelObject[] getChildren(String name) {
        XModelObject[] mes = getChildren();
        ArrayList<XModelObject> v = new ArrayList<XModelObject>();
        for (int i = 0; i < mes.length; i++) {
            if(name.equals(mes[i].getModelEntity().getName()))
              v.add(mes[i]);
        }
        return v.toArray(new XModelObject[v.size()]);
    }

    /*
     * Non-interface method that are called only by the default
     * saver (XModelObjectLoaderUtil).
     * Should be overrided by subclass that overrides method
     * loadChildren() of RegularObjectImpl
     */

    public XModelObject[] getChildrenForSave() {
        return getChildren();
    } 

    public XModelObject getChildAt(int i) {
        XModelObject[] children = getChildren();
        return (i < 0 || i >= children.length) ? null : children[i];
    }

    public boolean hasChildren() {
        return getChildren().length > 0;
    }

    public boolean addChild(XModelObject object) {
        boolean b = addChild_0(object);
        if(b) fireStructureChanged(org.jboss.tools.common.model.event.XModelTreeEvent.CHILD_ADDED, object);
        return b;
    }

    public boolean addChild_0(XModelObject o) {
        return false;
    }

    public void removeChild(XModelObject child) {
        if(child.getParent() != this) return;
        String path = child.getPath();
        removeChild_0(child);
        fireStructureChanged(org.jboss.tools.common.model.event.XModelTreeEvent.CHILD_REMOVED, path);
    }

    public void removeChild_0(XModelObject o) {}

    public void removeFromParent() {
        if(getParent() != null) getParent().removeChild(this);
    }

    // paths

    public String getLongPath() {
        if(getParent() == null) return null;
        String p = parent.getLongPath();
        return (p == null) ? null : (p.length() == 0)
                           ? getPathPart() : p + "/" + getPathPart();
    }

    public String getPath() {
        String lp = getLongPath();
        if(lp == null) return null;
        XMapping m = getModel().getMetaData().getMapping("Roots");
        String h = "" + lp;
        do {
            int ib = h.lastIndexOf('/');
            String q = h.substring(ib + 1), r = m.getValue(q);
            if(r != null && h.equals(r)) return "%" + q + "%" + lp.substring(r.length());
            if(ib >= 0) h = h.substring(0, ib); else h = null;
        } while(h != null);
        return lp;
    }

    public String getPathPart() {
        String p = name();
        return (p == null || p.indexOf('/') < 0) ? p : p.replace('/', '#');
    }

    public XModelObject getChildByPath(String path) {
        int i = path.indexOf("/");
        String n = (i < 0) ? path : path.substring(0, i);
        String f = (i < 0) ? "" : path.substring(i + 1);
        XModelObject me = getChildByPathPart(n);
        if(me == null) {
        	if(n.equals("..")) me = getParent();
        	else if(n.equals(".")) me = this;
        }
        return (me == null || i < 0) ? me : ((XModelObjectImpl)me).getChildByPath(f);
    }

    public XModelObject getChildByPathPart(String pathpart) {
        XModelObject[] cs = getChildren();
        for (int i = 0; i < cs.length; i++)
          if(cs[i].getPathPart().equals(pathpart)) return cs[i];
        return null;
    }

    // copy

    public XModelObject copy(boolean transform, int level) {
        XModelObject copy = shallow_copy(transform);
        if(copy != null) copy.setModified(true);
        if(level == 0 || copy == null) return copy;
        XModelObject[] cs = getChildrenForSave();
        for (int i = 0; i < cs.length; i++) {
            XModelObject ccopy = cs[i].copy(transform, level - 1);
            if(ccopy == null) continue;
            copy.addChild(ccopy);
        }
        return copy;
    }

    public XModelObject copy(boolean transform) {
        if(getModel() == null) return null;
        XModelObject copy = shallow_copy(transform);
        if(copy != null) copy_children(copy, transform);
        return copy;
    }

    public XModelObject copy(int level) {
        return copy(false, level);
    }

    public XModelObject shallow_copy(boolean transform) {
        XAttribute[] as = getModelEntity().getAttributes();
        Properties p = new Properties();
        for (int i = 0; i < as.length; i++) {
            if(!as[i].isCopyable()) continue;
            String n = as[i].getName();
            String v = getAttributeValue(n);
            if(v != null) p.setProperty(n, getAttributeValue(n));
        }
        String entity = (transform) ? getEntityForCopy() : getModelEntity().getName();
        XModelObject c = getModel().createModelObject(entity, p);
        if(c != null) c.setModified(true);
        return c;
    }

    protected void copy_children(XModelObject copy, boolean transform) {
        XModelObject[] cs = getChildrenForSave();
        for (int i = 0; i < cs.length; i++) {
            XModelObjectImpl c = (XModelObjectImpl)cs[i];
            XModelObject ccopy = c.shallow_copy(transform);
            if(ccopy == null) continue;
            c.copy_children(ccopy, transform);
            if(copy.addChild(ccopy)) continue;
			//child was created during setting attributes
            XModelObject cc = copy.getChildByPath(ccopy.getPathPart());
            if(cc == null) continue;
			copy.removeChild(cc);
			copy.addChild(ccopy);
        }
    }

    public XModelObject copy() {
        return copy(true);
    }

    protected String getEntityForCopy() {
        return getModelEntity().getName();
    }

    // presentation

    public String getPresentationString() {
        return name();
    }

    protected String name() {
        XAttribute a = getModelEntity().getAttribute("name");
        return (a != null) ? getAttributeValue("name") : getModelEntity().getName();
    }

    public String getMainIconName() {
        try {
            return getModelEntity().getRenderer().getIconInfo("main");
        } catch (NullPointerException e) {
            return "main.closedbox";
        }
    }

	public Image getImage() {
		return new XModelObjectIcon(this).getEclipseImage();
	}

	public Image getImage(String[] types) {
		return new XModelObjectIcon(this).getEclipseImage0(types);
	}

    protected void fireStructureChanged(int kind, Object info) {
        changeTimeStamp();
        XModelImpl m = (XModelImpl)getModel();
        m.fireStructureChanged(this, kind, info);
    }

    public boolean isEqual(XModelObject o) {
        if(o == null) return false;
        if(!getModelEntity().getName().equals(o.getModelEntity().getName())) return false;
        XAttribute[] as = getModelEntity().getAttributes();
        for (int i = 0; i < as.length; i++) {
            if(!as[i].isCopyable()) continue;
            String n = as[i].getName();
            String va = getAttributeValue(n);
            String vb = o.getAttributeValue(n);
            if((va == null && vb != null) || !va.equals(vb)) return false;
        }
        XModelObject[] ca = getChildrenForSave(), cb = o.getChildrenForSave();
        if(ca.length != cb.length) return false;
        for (int i = 0; i < ca.length; i++)
          if(!ca[i].isEqual(cb[i])) return false;
        return true;
    }

    public interface SP {
        public String get(String name);
        public void put(String name, String value);
        public void remove(String name);
    }
    
	static ModelObjectAdapterExtensionPoint ep = ModelObjectAdapterExtensionPoint.getInstance();
    
	private static IModelObjectAdapter propertySource = ep.getAdapter("IPropertySource");
	
	public Object getAdapter(Class adapter) {
		if(XModelObject.class == adapter) return this;
		else if(adapter == IResource.class) {
			return null;
		} else if(adapter == IFile.class) {
			XModelObject f = getResourceAncestor();
			Object o = (f == null) ? null : f.getAdapter(IResource.class);
			return (o instanceof IFile) ? o : null;
		} else if(adapter == IJavaElement.class) {
			return EclipseResourceUtil.findJavaElement(this);
		} else if(adapter == IActionFilter.class) {
			IModelObjectAdapter af = ep.getAdapter("IActionFilter");
			if(af != null) af.setModelObject(this);
			return af;
		} else if (adapter == IPropertySource.class) {
			if(propertySource != null) propertySource.setModelObject(this);
			return propertySource;
		}
		return null;
	}
	
	public XModelObject getResourceAncestor() {
		XModelObject p = this;
		while(p != null && p.getFileType() == NONE) p = p.getParent();
		return p;
	}
	
	public void fireObjectChanged(Object details) {
		((XModelImpl)getModel()).fireNodeChanged(this, getPath(), details);
	}
	
	protected int errorState = 0;
	protected int errorChildCount = 0;
	protected int warningChildCount = 0;
	protected HashSet<String> errorAttributes;
	protected HashSet<String> errorAttributesDirty;
	
	public int getErrorState() {
		return errorState;
	}
	
	public int getErrorChildCount() {
		return errorChildCount;
	}
	
	public int getWarningChildCount() {
		return warningChildCount;
	}
	
	public boolean getAttributeErrorState(String attributeName) {
		return errorAttributes != null && errorAttributes.contains(attributeName);
	}
	
	public void setErrorState(int b) {
		if(b != 0) {
			commitErrorAttributes();
		}
		if(errorState == b) return;
		int oldErrorState = errorState;
		errorState = b;
		if(b == 0) {
			errorAttributes = null;
			errorAttributesDirty = null;
		}
		if(errorChildCount > 0 && b == 2 && oldErrorState == 0) return;
		if(warningChildCount > 0 && b == 1 && oldErrorState == 0) return;
		fireErrorStateChanged();
		if(parent != null) {
			if(oldErrorState == 2 && errorChildCount == 0) {
				parent.unregisterErrorChild();
			} else if(oldErrorState == 1 && warningChildCount == 0) {
				parent.unregisterWarningChild();
			}
			if(errorState == 2 && errorChildCount == 0) {
				parent.registerErrorChild();
			} else if(errorState == 1 && warningChildCount == 0) {
				parent.registerWarningChild();
			}
		}
	}
	
	public void registerErrorChild() {
		errorChildCount++;
		if(errorChildCount == 1 && errorState != 2) {
			fireErrorStateChanged();
            if(parent != null) parent.registerErrorChild();
		}
	}
	
	public void unregisterErrorChild() {
		errorChildCount--;
		if(errorChildCount == 0 && errorState != 2) {
			fireErrorStateChanged();
            if(parent != null) parent.unregisterErrorChild();
		}
	}

	public void registerWarningChild() {
		warningChildCount++;
		if(warningChildCount == 1 && errorState != 1) {
			fireErrorStateChanged();
            if(parent != null) parent.registerWarningChild();
		}
	}
	
	public void unregisterWarningChild() {
		warningChildCount--;
		if(warningChildCount == 0 && errorState != 1) {
			fireErrorStateChanged();
            if(parent != null) parent.unregisterWarningChild();
		}
	}

	private void fireErrorStateChanged() {
		safeChangeTimeStamp();
        String path = getPath();
        if(path != null) {
        	fireObjectChanged(null);
        }
	}
	
	public void addErrorAttributeDirty(String attributeName) {
		if(errorAttributesDirty == null) {
			errorAttributesDirty = new HashSet<String>();
		}
		errorAttributesDirty.add(attributeName);
	}

	public void commitErrorAttributes() {
		if(errorAttributes == null) {
			if(errorAttributesDirty == null) return;
			errorAttributes = errorAttributesDirty;
			errorAttributesDirty = null;
			fireErrorStateChanged();
		} else if(errorAttributesDirty == null) {
			errorAttributes = null;
			fireErrorStateChanged();
		} else {
			boolean b = false;
			Iterator<String> it = errorAttributes.iterator();
			while(it.hasNext()) {
				Object o = it.next();
				if(!errorAttributesDirty.contains(o)) {
					b = true;
					it.remove();
				}
			}
			it = errorAttributesDirty.iterator();
			while(it.hasNext()) {
				String o = it.next();
				if(!errorAttributes.contains(o)) {
					b = true;
					errorAttributes.add(o);
				}
			}
			if(errorAttributes.isEmpty()) errorAttributes = null;
			errorAttributesDirty = null;
			if(b) fireErrorStateChanged();
		}
	}

}

class SProperties implements XModelObjectImpl.SP {
    String[] list = null;
    XModelEntity entity;

    void init(XModelEntity entity) {
        this.entity = entity;
        list = new String[entity.getAttributes().length];
    }

    public String get(String name) {
        int i = entity.getPropertyIndex(name, false);
        return (i < 0 || i >= list.length) ? null : list[i];
    }

    public void put(String name, String value) {
        int i = entity.getPropertyIndex(name, true);
        ensureCapacity(i);
        list[i] = value;
    }

    public void remove(String name) {
        int i = entity.getPropertyIndex(name, false);
        if (i >= 0 && i < list.length) list[i] = null;
    }

    private void ensureCapacity(int i) {
        if(i < list.length) return;
        String[] _list = new String[i + 1];
        System.arraycopy(list, 0, _list, 0, list.length);
        list = _list;
    }

}

class SProperty {}
