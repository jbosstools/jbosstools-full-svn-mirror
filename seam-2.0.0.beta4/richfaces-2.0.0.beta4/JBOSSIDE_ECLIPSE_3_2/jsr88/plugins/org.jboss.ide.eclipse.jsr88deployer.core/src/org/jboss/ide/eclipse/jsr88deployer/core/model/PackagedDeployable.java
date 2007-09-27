/*
 * Created on Jan 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jsr88deployer.core.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.J2eeApplicationObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;

/**
 * @author rawb
 *
 */
public class PackagedDeployable extends AbstractDeployable {

    /* Constants for the locations of required deployment descriptors */

    private JarFile jarFile;
    private File theFile;
    protected List deployableChildren;
    private ModuleType moduleType;
    private String ddLocation;   // holds the key to default descriptor
    public HashMap beanRoots;
    public ArrayList tempFiles;
	private URLClassLoader urlLoader;
    
    
    //private J2eeApplicationImpl application;

    public static class UnDeployableException extends Exception {
        private static final long serialVersionUID = 3546361729907897143L;
        private static final String UNRECOGNIZED_ARCHIVE = "UNRECOGNiZED_ARCHIVE";
        public UnDeployableException( String s ) {
            super( UNRECOGNIZED_ARCHIVE + ": " + s );
        }
    }
        
    
    public PackagedDeployable( File file ) throws IOException, UnDeployableException {
		this(file, null, null);
    }

	
	/**
     * This smaller constructor is used for the automatically
     * extracting packageddeployables. 
     * 
     * @param file
     * @throws IOException
     * @throws UnDeployableException
     */
    public PackagedDeployable(File file, ArrayList tempFiles) throws IOException, UnDeployableException {
        this(file, null, tempFiles);
    }
    
    public PackagedDeployable(File file, J2eeApplicationObject application, 
    		ArrayList tempFiles) throws IOException, UnDeployableException {
    	        
    	//System.out.println("   * new constructor");
    	
        theFile = file;
        jarFile = new JarFile(file);
        deployableChildren = new ArrayList();
        beanRoots = new HashMap();

		if( tempFiles == null ) 
			this.tempFiles = new ArrayList();
		else 
			this.tempFiles = tempFiles;
        
        loadChildren();
        
        if( getType() == null ) {
            //System.out.println("I am a dead " + 
            //		"(aka not deployable by myself)");
            throw new UnDeployableException("Object not deployable");
        }
        System.out.println("I am a " + getType());
        //if( application != null ) application.addDeployable(this);

        
    }
    
    private void loadChildren() throws IOException {
        // Make a list of deployableObject children
        Enumeration enumer = jarFile.entries();
        
        String suffix;
        JarEntry entry;
        int lastIndex;
        while(enumer.hasMoreElements()) {
            entry = (JarEntry)enumer.nextElement();
            if( hasDeployableExtension(entry.getName()) ) {
                // ok we need to make another deployable object here
                // and add it to our kiddies. 
                File child = File.createTempFile(
                        entry.getName().replace('/', '_'),
                        "." + extension(entry.getName()));
                    
                copyInputStream(jarFile.getInputStream(entry),
                        new BufferedOutputStream(new FileOutputStream(child)));
                
                this.tempFiles.add(child);
                
                try {
                    PackagedDeployable dep_child = new PackagedDeployable(child, this.tempFiles);
                    deployableChildren.add(dep_child);
                } catch( UnDeployableException e ) {
                    // do nothing
                }
            } else if( hasXMLExtension(entry.getName())) {
            	
            	// No one may ever ask to see this or use it,
            	// but better safe than sorry.
            	
            	
                File descriptor = File.createTempFile(
                        entry.getName().replace('/', '_'),
                        "." + extension(entry.getName()));
                
                this.tempFiles.add(descriptor);
                
                // if our descriptor is one of the standard ones,
                // it'll set our type.
                setType(entry.getName());


                copyInputStream(jarFile.getInputStream(entry),
                        new BufferedOutputStream(new FileOutputStream(descriptor)));

                DDBeanRootImpl oneRoot = new DDBeanRootImpl(this, descriptor, 
						entry.getName());
                beanRoots.put(entry.getName(), oneRoot);
            }
        }
    }
    
    /*
     * Used to make temporary file for an extracted internal jar
     */
    public static final void copyInputStream(InputStream in, OutputStream out)
    throws IOException
    {
      byte[] buffer = new byte[1024];
      int len;

      while((len = in.read(buffer)) >= 0)
        out.write(buffer, 0, len);

      in.close();
      out.close();
    }


    /**
     * Simply return the module type
     */
    public ModuleType getType() {
    	return moduleType;
    }

    
    /**
     * Called from loadChildren. 
     * If one of the standard descriptors is passed in, 
     * we set our type and our default descriptor member variable.
     * 
     * @param name The name of a jar entry file
     */
    private void setType(String name) {
        if(name.equals(AbstractDeployable.EJB_XML)) { 
            ddLocation = AbstractDeployable.EJB_XML; 
            moduleType = ModuleType.EJB;
        }
        if(name.equals(AbstractDeployable.WAR_XML)) {
            ddLocation = AbstractDeployable.WAR_XML;
            moduleType = ModuleType.WAR;
        }
        if(name.equals(AbstractDeployable.RAR_XML)) {
            ddLocation = AbstractDeployable.RAR_XML;
            moduleType = ModuleType.RAR;
        }
        if(name.equals(AbstractDeployable.EAR_XML)) {
            ddLocation = AbstractDeployable.EAR_XML;
            moduleType = ModuleType.EAR;
        }
    }

    
    private String extension(String s ) {
        int y;
        if((y=s.lastIndexOf(".")) != 1 ) 
            return s.substring(y+1);
        return "";        
    }
    
    private boolean hasDeployableExtension(String s) {
        String extension = extension(s).toLowerCase();
        if( extension.compareTo("ear")==0) return true;
        if( extension.compareTo("ejb")==0) return true;
        if( extension.compareTo("car")==0) return true;
        if( extension.compareTo("rar")==0) return true;
        if( extension.compareTo("war")==0) return true;
        if( extension.compareTo("jar")==0) return true;
        
        return false;
    }
    
    private boolean hasXMLExtension(String s) {
    	if( extension(s).toLowerCase().compareTo("xml") == 0 ) {
    		return true;
    	}
    	return false;
    }

    
    
    
    
    
    
    
    
    public DDBeanRoot getDDBeanRoot() {
    	if( ddLocation != null ) 
    		return (DDBeanRoot)beanRoots.get(ddLocation);
        return null;
    }

    public DDBean[] getChildBean(String xpath) {
    	DDBeanRoot root = getDDBeanRoot();
    	if( root == null ) 
    		return null;
    	
        return root.getChildBean(xpath);
    }

    public String[] getText(String xpath) {
    	DDBeanRoot root = getDDBeanRoot();
    	if( root == null ) 
    		return null;
    	
        return root.getText(xpath);
    }

    /* TODO: FIX ME */
    public Class getClassFromScope(String className) {
		/*
		try {
			if( urlLoader == null ) {
				ArrayList entryURLs = new ArrayList();
				String prefix = "jar:" + theFile.toURL() + "!/";
				//				 Initialize loader 
				Enumeration entries = entries();
				while(entries.hasMoreElements()) {
					JarEntry entry = (JarEntry)entries.nextElement();
					if( entry.toString().endsWith(".class")) {
						entryURLs.add(prefix + entry.toString());
					}
					
				}
				
				URL[] urls = new URL[entryURLs.size()];
				for( int i = 0; i < urls.length; i++ ) {
					urls[i] = new URL((String)entryURLs.get(i));
					System.out.println(urls[i]);
				}
				urlLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
			}
			
		} catch( Exception e ) {
			System.out.println("dead: " + e.getMessage());
			
		}
		
		
		try {
//			URL tmp = urlLoader.findResource("Item");
//			System.out.println("looking for item: " + tmp);
			Class c = urlLoader.loadClass("crimeportal.web.Catalogue");
			
			System.out.println("still alive: " + c.getName());
			
		} catch( Exception e ) {
			System.out.println("dead in getClassFromScope: " + e.getMessage());
			//e.printStackTrace();
		}
		
		*/
        return null;
    }


    public String getModuleDTDVersion() {
    	DDBeanRoot root = getDDBeanRoot();
    	if( root == null ) 
    		return null;
    	
        return root.getDDBeanRootVersion();
    }


    public DDBeanRoot getDDBeanRoot(String filename) throws FileNotFoundException,
            DDBeanCreateException {
    
    	// if the filename is 100% accurate
    	if( beanRoots.get(filename) != null ) {
    		return ((DDBeanRoot)beanRoots.get(filename));
    	}
    	
    	// otherwise iterate and search for it.
    	Iterator i = beanRoots.keySet().iterator();
    	while(i.hasNext()) {
    		String key = ((String)i.next());
    		if( key.endsWith(filename) ) {
    			return ((DDBeanRoot)beanRoots.get(key));
    		}
    	}
    	
    	// If we can't find one anywhere created... die?
		throw new FileNotFoundException();
    }


    public Enumeration entries() {
        return jarFile.entries();
    }

    public InputStream getEntry(String name) {
        Enumeration entries = entries();
        JarEntry next;
        try {
            while(entries.hasMoreElements()) {
                next = (JarEntry)entries.nextElement();
                if( next.getName().compareTo(name) == 0 ) 
                    return jarFile.getInputStream(next);
            }
        } catch( IOException e ) {
        }
        return null;        
    }


	/* (non-Javadoc)
	 * @see org.jboss.ide.eclipse.jsr88deployer.core.model.AbstractDeployable#Dispose()
	 */
	public void Dispose() {
		for( Iterator i = tempFiles.iterator(); i.hasNext(); ) {
			File file = (File)i.next();
			file.deleteOnExit();
		}
	}
    

}
