/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.core.extensions.descriptors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.eclipse.core.internal.variables.StringSubstitutionEngine;
import org.eclipse.core.internal.variables.StringVariableManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.server.core.IServer;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.extensions.descriptors.XPathFileResult.XPathResultNode;
import org.jboss.ide.eclipse.as.core.resolvers.ConfigNameResolver;
import org.jboss.ide.eclipse.as.core.util.IMemento;

/**
 * A simple value object to hold the XPath query data
 * @author rstryker@redhat.com
 *
 */
public class XPathQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * XPath-important fields
	 */
	protected String name;
	protected String baseDir;
	protected volatile String effectiveBaseDir;
	protected String filePattern;
	protected volatile String effectiveFilePattern;
	protected String xpathPattern;
	protected String attribute;
	
	/*
	 * The filter, need not be saved on serialize
	 */
	protected transient AntFileFilter filter;
	
	/*
	 * The file results, need not be saved on serialize
	 */
	protected transient XPathFileResult[] results;
	protected transient XPathCategory category;
	protected transient XMLDocumentRepository repository = null;
	protected IServer server; // May be null
	
	public XPathQuery(IMemento memento, IServer server) {
		this.server = server;
		this.name = memento.getString("name"); //$NON-NLS-1$
		this.baseDir = memento.getString("dir"); //$NON-NLS-1$
		this.filePattern = memento.getString("filePattern"); //$NON-NLS-1$
		this.xpathPattern = memento.getString("xpathPattern"); //$NON-NLS-1$
		this.attribute = memento.getString("attribute"); //$NON-NLS-1$
		setEffectiveBaseDir();
		setEffectiveFilePattern();
	}
	
	public XPathQuery(String name, List list) {
		this.name = name;
		this.baseDir = list.get(0).equals(XPathModel.EMPTY_STRING) ? null : (String)list.get(0);
		this.filePattern = list.get(1).equals(XPathModel.EMPTY_STRING) ? null : (String)list.get(1);
		this.xpathPattern = list.get(2).equals(XPathModel.EMPTY_STRING) ? null : (String)list.get(2);
		this.attribute = list.size() < 3 || list.get(3).equals(XPathModel.EMPTY_STRING) ? null : (String)list.get(3);			
		setEffectiveBaseDir();
		setEffectiveFilePattern();
	}
	
	public XPathQuery(IServer server, String name, String baseDir, 
			String filePattern, String xpathPattern, String attribute) {
		this.server = server;
		this.name = name;
		this.baseDir = baseDir;
		this.filePattern = filePattern;
		this.xpathPattern = xpathPattern;
		this.attribute = attribute;
		this.results = null;
		setEffectiveBaseDir();
		setEffectiveFilePattern();
	}
	
	private void setEffectiveBaseDir() {
		String serverName = server == null ? "" : server.getName(); //$NON-NLS-1$
		String dir1 = baseDir == null ? null : baseDir;
		String dir2 = new ConfigNameResolver().performSubstitutions(dir1, serverName);
		IPath dir = dir2 == null ? null : new Path(dir2);
		if( dir == null && category != null) {
			dir = getCategory().getServer().getRuntime().getLocation();
		}
		if( dir != null && !dir.isAbsolute() && category != null)
			dir = getCategory().getServer().getRuntime().getLocation().append(dir);
		effectiveBaseDir = dir == null ? null : dir.toString();
	}
	
	private void setEffectiveFilePattern() {
		String serverName = server == null ? "" : server.getName(); //$NON-NLS-1$
		String pattern = filePattern == null ? null : filePattern;
		String pattern2 = new ConfigNameResolver().performSubstitutions(pattern, serverName);
		effectiveFilePattern = pattern2;
	}
	
	
	protected AntFileFilter getFilter() {
		if( filter == null ) {
			filter = new AntFileFilter(effectiveBaseDir, effectiveFilePattern);
		}
		return filter;
	}
	public void refresh() {
		String[] files = getFilter().getIncludedFiles();
		boolean changed = false;
		for( int i = 0; i < files.length; i++ ) {
			changed = changed || getRepository().refresh(new Path(effectiveBaseDir).append(files[i]).toOSString());
		}
		if( changed ) {
			results = null;
		}
	}
	
	/**
	 * Get any files that match the file pattern but may 
	 * or may not actually match the xpath
	 * @return
	 */
	public String[] getPossibleFileMatches() {
		return getFilter().getIncludedFiles();
	}
	
	public XPathFileResult[] getResults() {
		if( results == null ) 
			loadResults();
		return results;
	}
	
	public void clearCache() {
		results = null;
		setEffectiveBaseDir();
	}
	
	public boolean resultsLoaded() {
		return results == null ? false : true;
	}

	protected void loadResults() {
		try {
			String[] files = getFilter().getIncludedFiles();
			String fileLoc;
			ArrayList<XPathFileResult> resultList = new ArrayList<XPathFileResult>();
			List<Node> nodeList = null;
			for( int i = 0; i < files.length; i++ ) {
				fileLoc = new Path(effectiveBaseDir).append(files[i]).toOSString();
				Document d = getRepository().getDocument(fileLoc);
				if( d != null ) {
					XPath xpath = new Dom4jXPath( xpathPattern );
					Map map = XPathModel.getDefault().getNamespaceMap();
					xpath.setNamespaceContext( new SimpleNamespaceContext( map));
					List nodes = xpath.selectNodes(d);
					nodeList = xpath.selectNodes(d);
				}
				if( nodeList != null && nodeList.size() > 0 ) 
					resultList.add(new XPathFileResult(this, fileLoc, nodeList));
			}
			results = resultList.toArray(new XPathFileResult[resultList.size()]);
		} catch( IllegalStateException ise ) {
			// cannot load  TODO log?
			results = new XPathFileResult[0];
		} catch( JaxenException je ) {
			// cannot load  TODO log?
			results = new XPathFileResult[0];
		}
	}
	
	public String getFirstResult() {
		XPathFileResult[] fileResults = getResults();
		if( fileResults.length > 0 ) {
			XPathResultNode[] nodes = fileResults[0].getChildren();
			if( nodes.length > 0 ) {
				return nodes[0].getText();
			}
		}
		return null;
	}
	/*
	 * Field Getters and setters
	 */
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if( category != null ) 
			category.renameQuery(this.name, name);
		this.name = name;
	}
	public String getFilePattern() {
		return filePattern;
	}
	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}
	public String getXpathPattern() {
		return xpathPattern;
	}
	public void setXpathPattern(String xpathPattern) {
		this.xpathPattern = xpathPattern;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getBaseDir() {
		return baseDir;
	}
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
		setEffectiveBaseDir();
	}
	public XPathCategory getCategory() {
		return category;
	}
	public void setCategory(XPathCategory category) {
		boolean hadCategory = this.category != null;
		this.category = category;
		if( !hadCategory ) 
			setEffectiveBaseDir();
	}
	public void setRepository(XMLDocumentRepository repo) {
		this.repository = repo;
	}
	public XMLDocumentRepository getRepository() {
		return repository == null ? XMLDocumentRepository.getDefault() : repository;
	}
}
