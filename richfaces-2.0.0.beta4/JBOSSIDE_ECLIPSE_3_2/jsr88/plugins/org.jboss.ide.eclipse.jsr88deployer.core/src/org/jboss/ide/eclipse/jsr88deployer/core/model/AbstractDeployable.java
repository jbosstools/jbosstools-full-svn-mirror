/*
 * Created on Apr 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jsr88deployer.core.model;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractDeployable implements DeployableObject {
    
	
	public static final String EJB_XML = "META-INF/ejb-jar.xml";
    public static final String WAR_XML = "WEB-INF/web.xml";
    public static final String RAR_XML = "ra.xml";
    public static final String EAR_XML = "META-INF/application.xml";


    public abstract ModuleType getType();

	public abstract DDBeanRoot getDDBeanRoot();

	public abstract DDBean[] getChildBean(String arg0);

	public abstract String[] getText(String arg0);

	public abstract Class getClassFromScope(String className);

	public abstract String getModuleDTDVersion();

	public abstract DDBeanRoot getDDBeanRoot(String filename) throws FileNotFoundException, DDBeanCreateException;

	public abstract Enumeration entries();

	public abstract InputStream getEntry(String arg0);
	
	public abstract void Dispose();

}
