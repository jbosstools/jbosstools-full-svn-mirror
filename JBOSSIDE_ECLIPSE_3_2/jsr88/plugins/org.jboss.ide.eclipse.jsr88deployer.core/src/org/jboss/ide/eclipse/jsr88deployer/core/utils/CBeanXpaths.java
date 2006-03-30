/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.core.utils;

import java.util.ArrayList;

import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;

import org.jboss.ide.eclipse.jsr88deployer.core.model.DDBeanImpl;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CBeanXpaths {
	private ArrayList children;
	private String relativePath;
	private CBeanXpaths parent;
	
	private DDBeanImpl dd;
	private DConfigBean dc;
	
	public CBeanXpaths(String relativePath, CBeanXpaths parent) {
		children = new ArrayList();
		this.relativePath = relativePath;
		this.parent = parent;
		if( parent != null ) 
			parent.addChild(this);
	}
	
	public CBeanXpaths(String relativePath, CBeanXpaths parent, 
			DDBeanImpl ddBean, DConfigBean dcBean) {
		this(relativePath, parent);
		this.dd = ddBean;
		this.dc = dcBean;
	}
	
	public CBeanXpaths getParent() {
		return parent;
	}
	
	public void setParent( CBeanXpaths parent ) {
		this.parent = parent;
		parent.addChild(this);
	}
	
	public String getPath() {
		return relativePath;
	}
	
	public ArrayList getChildren() {
		return children;
	}
	
	public void addChild(CBeanXpaths path) {
		children.add(path);
	}

	public DConfigBean getDc() {
		return dc;
	}

	public void setDc(DConfigBean dc) {
		this.dc = dc;
	}

	public DDBeanImpl getDd() {
		return dd;
	}

	public void setDd(DDBeanImpl dd) {
		this.dd = dd;
	}
}

