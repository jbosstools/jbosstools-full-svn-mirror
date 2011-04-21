/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.core.model;

import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.XpathListener;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NullDDBean implements DDBean {

	public String getXpath() {
		return null;
	}

	public String getText() {
		return null;
	}

	public String getId() {
		return null;
	}

	public DDBeanRoot getRoot() {
		return null;
	}

	public DDBean[] getChildBean(String arg0) {
		return null;
	}

	public String[] getText(String arg0) {
		return null;
	}

	public void addXpathListener(String arg0, XpathListener arg1) {
	}

	public void removeXpathListener(String arg0, XpathListener arg1) {
	}

	public String[] getAttributeNames() {
		return null;
	}

	public String getAttributeValue(String arg0) {
		return null;
	}

}
