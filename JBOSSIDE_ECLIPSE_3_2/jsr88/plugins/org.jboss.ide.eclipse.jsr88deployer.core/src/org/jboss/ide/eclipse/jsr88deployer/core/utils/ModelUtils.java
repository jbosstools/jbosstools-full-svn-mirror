/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.core.utils;

import java.io.File;
import java.io.IOException;

import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.DeploymentConfiguration;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;

import org.eclipse.core.resources.IResource;
import org.jboss.ide.eclipse.jsr88deployer.core.model.AbstractDeployable;
import org.jboss.ide.eclipse.jsr88deployer.core.model.DDBeanImpl;
import org.jboss.ide.eclipse.jsr88deployer.core.model.DDBeanRootImpl;
import org.jboss.ide.eclipse.jsr88deployer.core.model.PackagedDeployable;
import org.jboss.ide.eclipse.jsr88deployer.core.model.PackagedDeployable.UnDeployableException;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ModelUtils {

	public static AbstractDeployable createDeployableObject(IResource resource) {
		AbstractDeployable returnObject = null;
		if( resource.exists() ) {
			
			File file = resource.getLocation().toFile();
			try {
				PackagedDeployable deployable = new PackagedDeployable(file);
				return deployable;
			} catch( IOException e ) {
				System.out.println("[ModelUtils] - IO Exception");
			} catch( UnDeployableException f ) {
				System.out.println("[ModelUtils] - Undeployable Exception");
			}
		}
		return returnObject;
	}
	

	
	
	
	
	
	public static CBeanXpaths parse(DeploymentConfiguration config, AbstractDeployable deployable ) {
		try {
			DDBeanRootImpl root = (DDBeanRootImpl)deployable.getDDBeanRoot();
			DConfigBeanRoot dConfigRoot = config.getDConfigBeanRoot(root);
			CBeanXpaths xpaths = new CBeanXpaths("", null, root, dConfigRoot);
			parseDConfigBean(dConfigRoot, xpaths);
			return xpaths;
		} catch( ConfigurationException ce) {
			System.out.println("[parse] - config");
		}
		return null;
	}
	
	public static void parseDConfigBean(DConfigBean dc, CBeanXpaths paths) {
		String[] pathsToFollow = dc.getXpaths();
		DDBeanImpl ddb = (DDBeanImpl)dc.getDDBean();
		
		for( int i = 0; i < pathsToFollow.length; i++ ) {
			String tempPath = pathsToFollow[i];
			DDBean[] ddbeans = ddb.getChildBean(tempPath);
			if( ddbeans.length == 0 ) {
				DDBeanImpl tempDDB = ddb.addXpath(tempPath);
				ddbeans = new DDBean[] { tempDDB };
			}
			
			for( int j = 0; j < ddbeans.length; j++ ) {
				try {
					DConfigBean tmpDcb = dc.getDConfigBean(ddbeans[j]);
					CBeanXpaths tempCBPath = 
						new CBeanXpaths(tempPath, paths, 
								(DDBeanImpl)ddbeans[j], tmpDcb);
					parseDConfigBean(tmpDcb, tempCBPath);
				} catch( ConfigurationException ce) {
				}
			}
			
		}
	}

}
