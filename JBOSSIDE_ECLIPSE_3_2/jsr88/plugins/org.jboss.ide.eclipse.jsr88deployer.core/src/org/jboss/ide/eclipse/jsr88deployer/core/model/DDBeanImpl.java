/*
 * Created on Apr 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jsr88deployer.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.XpathEvent;
import javax.enterprise.deploy.model.XpathListener;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.AbstractElement;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DDBeanImpl implements DDBean {

	public final String SEPARATOR = "/";
	
    protected Node node;
    protected DDBeanRootImpl root;
    protected String xPath;
	protected HashMap listeners;

	
    protected DDBeanImpl() {
        this.root = null;
        this.xPath = SEPARATOR;
        // Key = xpath, value = arraylist of XPathListeners
        listeners = new HashMap();
    }

	public DDBeanImpl(Node node, DDBeanRootImpl root, String xPath ) {
		this.node = node;
		this.root = root;
		this.xPath = xPath;
        // Key = xpath, value = arraylist of XPathListeners
        listeners = new HashMap();
		//System.out.println("\n(  setting xpath in constructor: " + xPath);
	}

	public String getXpath() {
		return this.xPath;
	}

	public String getText() {
		return node.asXML();
	}

	public String getId() {
		return node.getUniquePath();
	}

	public DDBeanRoot getRoot() {
		return root;
	}

	public DDBean[] getChildBean(String xpath) {
		/*
		String childXpath = globalizeXpath(xpath);
		return root.getChildDDBeans(childXpath);
		*/
		try {
			List l = node.selectNodes(xpath);
			DDBeanImpl[] impls = new DDBeanImpl[l.size()];
			int x = 0;
			for( Iterator i = l.iterator(); i.hasNext(); ) {
				impls[x++] = new DDBeanImpl(((Node)i.next()), root, xpath);
			}
			return impls;
		} catch( Exception e ) {
			System.out.println("Dead");
		}
		
		return null;

	}

	public String[] getText(String xpath) {
		List l = node.selectNodes(xpath);
		String[] strings = new String[l.size()];
		int x = 0;
		for( Iterator i = l.iterator(); i.hasNext(); ) {
			strings[x++] = ((Node)i.next()).getText();
		}
		return strings;
	}


	public void addXpathListener(String xpath, XpathListener xpl) {
		ArrayList list;

		if( listeners.get(xpath) == null ) {
			list = new ArrayList();
			listeners.put(xpath, list);
		} else {
			list = (ArrayList)(listeners.get(xpath));
		}
		list.add(xpl);
	}

	public void removeXpathListener(String xpath, XpathListener xpl) {
		if( listeners.get(xpath) != null ) {
			ArrayList list = (ArrayList)(listeners.get(xpath));
			list.remove(xpl);
			if( list.size() == 0 ) 
				listeners.remove(xpath);
		}
	}


	
    public String[] getAttributeNames() {
        if( node instanceof AbstractElement ) {
        	List attribList = ((AbstractElement)node).attributes();
        	String[] retVal = new String[attribList.size()];
        	int i = 0;
        	
        	for( Iterator iter = attribList.iterator(); iter.hasNext();) {
        		retVal[i++] = ((Attribute)iter.next()).getName();
        	}
        	return retVal;
        }
        return new String[]{};
    }

    public String getAttributeValue(String attrName) {
        return node.valueOf("@" + attrName);
    }

    protected void setRoot(DDBeanRootImpl root) {
        this.root = root;
    }
    
    protected void setNode(Node node) {
        this.node = node;
    }
    
    public Node getNode() {
    	return node;
    }
	
    
    // TODO: Fix, implement, etc
    public DDBeanImpl addXpath( String xpath ) {
    	return addElement(xpath);
    }

	
	public DDBeanImpl addElement(String name) {
		return addElement(name, null);
	}
	
	public DDBeanImpl addElement(String name, String value) {
		if( this.node.getNodeType() == Node.ELEMENT_NODE ) {
			Element newElement = ((Element)this.node).addElement(name);
			if( value != null ) 
				newElement.setText(value);

			DDBeanImpl retval = new DDBeanImpl(newElement, root, name);
			
			fireBeanAdded(retval, name);
			
			return retval;
		}
		return null;
	}
	
	private void fireBeanAdded(DDBeanImpl newBean, String xpath) {
		Object o = listeners.get(xpath);
		if( o == null ) return;
		
		ArrayList pathListeners = (ArrayList)o;
		for(Iterator i = pathListeners.iterator(); i.hasNext(); ) {
			XpathListener el = (XpathListener)i.next();
			el.fireXpathEvent(new XpathEvent(newBean, XpathEvent.BEAN_ADDED));
		}
		
	}
	
	public void setText(String text) {
		this.node.setText(text);
		//fireBeanChanged();
	}
	
	

}
