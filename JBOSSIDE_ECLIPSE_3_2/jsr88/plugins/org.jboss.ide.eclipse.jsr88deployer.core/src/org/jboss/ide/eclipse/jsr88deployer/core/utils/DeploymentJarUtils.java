/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;


/**
 * @author Rob Stryker
 *
 */
public class DeploymentJarUtils {
	
	private static final String NO_VALUE = "__NO_VALUE__";

	public static boolean isValidDeploymentJar( String filename) {
		
		
		File productJarsFile = new File(filename);
		// Get the class name of the factory from the manifest file
		try {
			 JarFile productJar = new JarFile(productJarsFile);
			 Manifest productManifest = productJar.getManifest();
			 Map entryMap = productManifest.getMainAttributes();

			 String className = (String)entryMap.get(new Attributes.Name("J2EE-DeploymentFactory-Implementation-Class"));
			 if( className == null ) return false;
			 return true;
		} catch( IOException ioe ) {
			System.out.println("IO EXCERPTION");
		}

		return false;
	}
	
	public static ClassLoader createVendorClassLoader(String someJarFile) {
		// TODO: FIX ME DESPERATELY!
		// this is dependent on Scott Stark producing a jsr-88
		// compliant jboss-deployment.jar
		ClassLoader loader = null;
		try {
			URL urlList[] = {new File(someJarFile).toURL(),
					new File("c:\\Training\\jboss-4.x\\client\\jbossall-client.jar").toURL()};
		
			loader = new URLClassLoader(urlList, Thread.currentThread().getContextClassLoader());
		} catch( MalformedURLException murle) {
			
		}
		return loader;

	}
	
	public static DeploymentManager getDeploymentManager(
			ClassLoader vendorLoader, String someJarFile, 
			String uri) throws Throwable {
		return getDeploymentManager(vendorLoader, someJarFile, uri, "", "");
	}
	
	
	public static DeploymentManager getDeploymentManager  (
			ClassLoader vendorLoader, String someJarFile, String uri, 
			String username, String pass) throws Throwable {

		File productJarsFile = new File(someJarFile);
		String className = null;
		try {
			 JarFile productJar = new JarFile(productJarsFile);
			 Manifest productManifest = productJar.getManifest();
			 Map entryMap = productManifest.getMainAttributes();

			 className = (String)entryMap.get(new Attributes.Name("J2EE-DeploymentFactory-Implementation-Class"));
			 if( className == null ) return null;
		} catch( IOException ioe ) {
			System.out.println("IOException ioe");
			return null;
		}

		// save a reference to our current thread's loader
		ClassLoader cachedLoader = Thread.currentThread().getContextClassLoader();
		
		// make our loader with the current thread's as a parent.
		Thread.currentThread().setContextClassLoader(vendorLoader);
		
		

		// TODO:  This is temporary code. Delete it
//		try {
//			DeploymentManager manager = 
//				new DeploymentFactoryImpl().getDeploymentManager(uri, username, pass);
//		
//		
//		Thread.currentThread().setContextClassLoader(cachedLoader);
//		return manager;
//		} catch( Exception e ) {
//			Thread.currentThread().setContextClassLoader(cachedLoader);
//			return null;
//		}
		// TODO: Uncomment when we have a working jar
		
		Class c = vendorLoader.loadClass (className);
		Object instance = c.newInstance();
		
		// get our deployment factory
		DeploymentFactory factory = (DeploymentFactory)instance;
		
		
		// now lets call a method
		// TODO if user or pass are NO_VALUE, do disconnected.
		DeploymentManager manager = 
			factory.getDeploymentManager(uri, "", "");
		
		
		Thread.currentThread().setContextClassLoader(cachedLoader);
		return manager;
	}
	

	

}
