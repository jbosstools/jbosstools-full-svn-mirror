/*
 * Created on Apr 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jsr88deployer.core.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.shared.ModuleType;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DDBeanRootImpl extends DDBeanImpl implements DDBeanRoot {
	
	private DeployableObject deployable;
	private String filename;
	private HashMap listeners;
	private Document doc;

	
	/*
    public DDBeanRootImpl( AbstractDeployable deployable, String filename, String jarEntryName ) {
    	this(deployable, new File(filename), jarEntryName);
    }
    */
    
    public DDBeanRootImpl( AbstractDeployable deployable, File file, String filename ) {
        super();
        this.setRoot(this);
        this.deployable = deployable;
        //this.filename = file.getName();
		this.filename = filename;
        try {
            InputStream stream = new FileInputStream(file);
            SAXReader reader = new SAXReader();
            doc = reader.read(stream);       
        } catch( FileNotFoundException e ) {
            System.out.println("fnfe: " + e.getMessage());
        } catch( Exception f ) {
            System.out.println("exception: " + f.getMessage());
        }
        setNode(doc.getRootElement());
        
    }
    
	public ModuleType getType() {
		return deployable.getType();
	}

	public DeployableObject getDeployableObject() {
		return deployable;
	}

	public String getModuleDTDVersion() {
		// deprecated
		return null;
	}

	
	/**
	 * Manually parse through the xml to find a version
	 */
	public String getDDBeanRootVersion() {
		// manual parsing sucks
		
		String asXml = doc.asXML();
		int startPos = asXml.indexOf("<?xml");
		if( startPos == -1 ) return null;
		int endPos = asXml.indexOf("?>", startPos);
		if( endPos == -1 ) return null;
		
		String releventSection = asXml.substring(startPos+5, endPos).trim();
		int version = releventSection.indexOf("version");
		if( version == -1 ) return null;
		int versionQuote = releventSection.indexOf("\"", version);
		if( versionQuote == -1 ) return null;
		int versionQuoteEnd = releventSection.indexOf("\"", versionQuote+1);
		if( versionQuoteEnd == -1 ) return null;
		
		String retval = releventSection.substring(versionQuote+1, versionQuoteEnd);

		return retval;
	}

	public String getFilename() {
		return this.filename;
	}
	

}
