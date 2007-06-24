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
package org.jboss.tools.vpe.editor.bundle;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.preferences.VpePreference;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.util.ELParser;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

public class BundleMap {
//	private static final String BEGIN_BUNDLE = "#{";
//	private static final String END_BUNDLE = "}";
//	private static final String BEGIN_ARRAY_STYLE_PROPERTY_NAME_SEPARATOR = "['";
//	private static final String END_ARRAY_STYLE_PROPERTY_NAME_SEPARATOR = "']";

	public static final String TITLE_ATTRIBUTE_NAME = "title";
	
	private BundleMapListener[] bundleMapListeners = new BundleMapListener[0];
	private StructuredTextEditor editor;
	
	private String[] javaSources;
    
    private BundleEntry[] bundles = new BundleEntry[0];
    private Map<String,UsedKey> usedKeys = new HashMap<String,UsedKey>();
    
    boolean isShowBundleUsageAsEL = "yes".equals(VpePreference.SHOW_RESOURCE_BUNDLES.getValue());
    XModelTreeListener modelListener = new ML();
	
	public void init(StructuredTextEditor editor){
		this.editor = editor;
		IEditorInput input = editor.getEditorInput();
		
		if (input instanceof IFileEditorInput) {
			javaSources = getJavaProjectSrcLocations(((IFileEditorInput)input).getFile().getProject());
		}
		refreshRegisteredBundles();
		PreferenceModelUtilities.getPreferenceModel().addModelTreeListener(modelListener);
	}
	
	public void refreshRegisteredBundles() {
		if (!hasJsfProjectNatureType() || !(editor.getEditorInput() instanceof IFileEditorInput)) return;
		IProject project = ((IFileEditorInput)editor.getEditorInput()).getFile().getProject();
		XModel model = EclipseResourceUtil.getModelNature(project).getModel();
		List l = WebPromptingProvider.getInstance().getList(model, WebPromptingProvider.JSF_REGISTERED_BUNDLES, null, null);
		if(l == null || l.size() < 2 || !(l.get(1) instanceof Map)) return;
		Map map = (Map)l.get(1);
		Iterator it = map.keySet().iterator();
		while(it.hasNext()) {
			String uri = it.next().toString();
			String prefix = map.get(uri).toString();
			int hash = (prefix + ":" + uri).hashCode();
			removeBundle(hash);
			addBundle(hash, prefix, uri, -1000, true);
		}
	}
	
	public void clearAll(){
		bundles = new BundleEntry[0];
	    usedKeys = new HashMap<String,UsedKey>();
	}
	
	public void dispose() {
		PreferenceModelUtilities.getPreferenceModel().removeModelTreeListener(modelListener);
	}	

	private static final String[] JSF_PROJECT_NATURES = {
			WebProject.JSF_NATURE_ID
		};

	private boolean hasJsfProjectNatureType() {
		try {
			IEditorInput ei = editor.getEditorInput();
			if(!(ei instanceof IFileEditorInput)) return false;
			IProject project = ((IFileEditorInput)ei).getFile().getProject();

			for (int i = 0; i < JSF_PROJECT_NATURES.length; i++) {
				if (project.getNature(JSF_PROJECT_NATURES[i]) != null) 
					return true;
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return false;
	}
	
	public boolean openBundle(String expression, String locale){
		ELParser.Token token = parseJSFExpression(expression);
		if(token == null) return false;
		ELParser.Token t = token;
		String prefix = null;
		String propertyName = null;
		while(t != null) {
			if(t.kind == ELParser.OPEN) {
				String[] values = getCall(expression, t);
				if(values != null) {
					prefix = values[0];
					propertyName = values[1];
					break;
				}
			}
			t = t.next;
		}
		if(prefix == null) return false;

		BundleEntry entry = getBundle(prefix);
		
		if(entry == null){
			if (hasJsfProjectNatureType()) {
				IProject project = ((IFileEditorInput)editor.getEditorInput()).getFile().getProject();
				XModel model = EclipseResourceUtil.getModelNature(project).getModel();
				String prefix2 = prefix;
				if(propertyName != null && prefix != null) {
					prefix2 = prefix + "." + propertyName;
				}
				WebPromptingProvider.getInstance().getList(model, WebPromptingProvider.JSF_BEAN_OPEN, prefix2, null);
			}
			return false;
		}

		if (hasJsfProjectNatureType()) {
			Properties p = new Properties();
			p.put(WebPromptingProvider.BUNDLE, entry.uri);
			p.put(WebPromptingProvider.KEY, propertyName);
			if (locale != null) p.put(WebPromptingProvider.LOCALE, locale);
			p.put(WebPromptingProvider.FILE, ((IFileEditorInput)editor.getEditorInput()).getFile().getProject());
	
			IProject project = ((IFileEditorInput)editor.getEditorInput()).getFile().getProject();
			XModel model = EclipseResourceUtil.getModelNature(project).getModel();
	
			WebPromptingProvider.getInstance().getList(model, WebPromptingProvider.JSF_OPEN_KEY, entry.uri, p);
			String error = p.getProperty(WebPromptingProvider.ERROR); 
			return (error == null || error.length() == 0);
		}
		return false;
	}

	public IFile getBundleFile(String uri){
		IEditorInput input = editor.getEditorInput();
		IProject project = ((FileEditorInput)input).getFile().getProject();
		String name = uri.replace('.','/')+".properties";
		
		if(project == null || !project.isOpen()) return null;
		try {
			if(!project.hasNature(JavaCore.NATURE_ID)) return null;
			IJavaProject javaProject = JavaCore.create(project);		
			IClasspathEntry[] es = javaProject.getRawClasspath();
			for (int i = 0; i < es.length; i++) {
				if(es[i].getEntryKind() != IClasspathEntry.CPE_SOURCE) continue;
				IFile file = (IFile)project.getWorkspace().getRoot().getFolder(es[i].getPath()).findMember("/"+name);
				if(file != null && file.exists()) return file;
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			return null;
		}
		return null;
	}
	
	private ResourceBundle getBundleByUrl(String uri) {
		try {
			if (javaSources!=null) {
				File file;
				URL[] urls = new URL[javaSources.length];
				for (int i=0;i<javaSources.length;++i) {
					try {
						file = new File(javaSources[i]).getCanonicalFile();
						urls[i] = file.toURL();
					} catch (Exception e) {
						VpePlugin.reportProblem(e);
						return null;
					}
				}
				ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
				ResourceBundle bundle = ResourceBundle.getBundle(uri, Locale.getDefault(), classLoader);
				
				return bundle;
			}
		} catch(Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return null;
	}
	
	private static String[] getJavaProjectSrcLocations(IProject project) {
		String[] EMPTY = new String[0];
		if(project == null || !project.isOpen()) return EMPTY;
		try {
			if(!project.hasNature(JavaCore.NATURE_ID)) return EMPTY;
			IJavaProject javaProject = JavaCore.create(project);		
			IClasspathEntry[] es = javaProject.getRawClasspath();
			ArrayList<String> l = new ArrayList<String>();
			for (int i = 0; i < es.length; i++) {
				if(es[i].getEntryKind() != IClasspathEntry.CPE_SOURCE) continue;
				l.add(project.findMember(es[i].getPath().removeFirstSegments(1)).getLocation().toString());
				try {
					l.add(project.findMember(es[i].getOutputLocation().removeFirstSegments(1)).getLocation().toString());
				} catch (Exception e) { } 
			}
			return (String[])l.toArray(new String[0]);
		} catch (Exception e) {
			return EMPTY;
		}
	}
	
	private void removeBundle(int hashCode, boolean refresh) {
		if (bundles.length == 0) return;
		int index = -1;
		for (int i = 0; i < bundles.length; i++) {
			if (hashCode == bundles[i].hashCode){
				index = i;
				break;
			}
		}
		if (index == -1) return;
		if (bundles.length == 1) {
			bundles = new BundleEntry[0];
			return;
		}
		BundleEntry[] newBundles = new BundleEntry[bundles.length - 1];
		System.arraycopy(bundles, 0, newBundles, 0, index);
		System.arraycopy(bundles, index + 1, newBundles, index, bundles.length - index - 1);
		bundles = newBundles;
		if(refresh)refreshUsedKeys();
	}

	public void removeBundle(int hashCode) {
		removeBundle(hashCode, true);
	}
	
//	public void addBundle(int hashCode, String prefix, String uri, int offset){
//		addBundle(hashCode, prefix, uri, offset, true);
//	}
	
	private void addBundle(int hashCode, String prefix, String uri, int offset, boolean refresh) {
		ResourceBundle bundle = getBundleByUrl(uri);
		BundleEntry entry = new BundleEntry(bundle, uri, prefix, hashCode, offset);
		if(bundle!=null) {
			BundleEntry[] newBundles = new BundleEntry[bundles.length + 1];
			System.arraycopy(bundles, 0, newBundles, 0, bundles.length);
			bundles = newBundles;
			bundles[bundles.length - 1] = entry;
		}
		if(refresh)refreshUsedKeys();
	}
	
	public void changeBundle(int hashCode, String prefix, String uri, int offset){
		removeBundle(hashCode, false);
		addBundle(hashCode, prefix, uri, offset, true);
	}
	
	private void changeBundleWithoutRefresh(int hashCode, String prefix, String uri, int offset){
		removeBundle(hashCode, false);
		addBundle(hashCode, prefix, uri, offset, false);
	}

	private BundleEntry getBundle(String prefix) {
		if(prefix == null) return null;
		BundleEntry lastBundle = null;
		for (int i = 0; i < bundles.length; i++) {
			if (prefix.equals(bundles[i].prefix)){
				if (lastBundle == null || lastBundle.offset > bundles[i].offset) {
					lastBundle = bundles[i];
				}
			}
		}
		return lastBundle;
	}
	
	public void refresh(){
		refreshRegisteredBundles();
		IEditorInput input = editor.getEditorInput();
			
		if (input instanceof IFileEditorInput) {
			javaSources = getJavaProjectSrcLocations(((IFileEditorInput)input).getFile().getProject());
			UsedKey key;
			UsedKey[] array = new UsedKey[0];
			array = usedKeys.values().toArray(array);			
			
			for(int i=0; i<array.length;i++){
				key = (UsedKey)array[i];
				changeBundleWithoutRefresh(key.hashCode, key.prefix, key.uri, key.offset);
			}
			refreshUsedKeys();
		}
	}
	
	private void refreshUsedKeys(){
		UsedKey key;
		UsedKey[] array = new UsedKey[0];
		array = (UsedKey[])usedKeys.values().toArray(array);
		
		for(int i=0; i<array.length;i++){
			key = (UsedKey)array[i];
			BundleEntry entry = getBundle(key.prefix);
			if(entry != null){
				String value;
				try{
					value = (String)entry.bundle.getObject(key.key);
				}catch(Exception ex){
					value = null;
					fireBundleKeyChanged(key.prefix, key.key, value);
					usedKeys.remove(key);
					continue;
				}
				if((value == null && key.value != null) || (value != null && key.value == null)){
					key.value = value;
					fireBundleKeyChanged(key.prefix, key.key, value);
					continue;
				}else if(value != null && key.value != null && !value.equals(key.value)){
					key.value = value;
					fireBundleKeyChanged(key.prefix, key.key, value);
					continue;
				}
			} else{
				key.value = null;
				fireBundleKeyChanged(key.prefix, key.key, null);
			}
		}
	}
	
	private ELParser.Token parseJSFExpression(String expression){
		ELParser parser = new ELParser();
		ELParser.Token token = parser.parse(expression);
		return token;		
	}
	
	public String getBundleValue(String name, int offset){
		if(isShowBundleUsageAsEL) return name;
		ELParser.Token token = parseJSFExpression(name);
		if(token == null) return null;
		ELParser.Token t = token;
		StringBuffer sb = new StringBuffer();
		while(t != null) {
			if(t.kind == ELParser.OPEN) {
				String[] values = getCall(name, t);
				ELParser.Token te = t;
				while(te.next != null && te.kind != ELParser.CLOSE) te = te.next;
				String dv = name.substring(t.start, te.end);
				String value = (values == null) ? null : getBundleValue(values[0], values[1], offset);
				if(value == null) {
					sb.append(dv);
				} else {
					sb.append(value);
				}
				t = te;
			} else {
				sb.append(name.substring(t.start, t.end));
			}
			t = t.next;
		}
		return sb.toString();
	}
	
	private ELParser.Token next(ELParser.Token t) {
		if(t == null) return null;
		t = t.next;
		if(t != null && t.kind == ELParser.SPACES) t = t.next;
		return t;
	}
	
	String[] getCall(String expression, ELParser.Token t) {
		if(t == null || t.kind != ELParser.OPEN) return null;
		t = next(t);
		if(t == null || t.kind != ELParser.NAME) return null;
		String prefix = expression.substring(t.start, t.end);
		String propName = null;
		t = next(t);
		if(t != null && t.kind == ELParser.DOT) {
			t = next(t);
			if(t == null || t.kind != ELParser.NAME) return null;
			propName = expression.substring(t.start, t.end);
		} else if(t != null && t.kind == ELParser.OPEN_ARG) {
			t = next(t);
			if(t == null || t.kind != ELParser.ARGUMENT) return null;
			propName = expression.substring(t.start, t.end);
			t = next(t);
			if(t == null || t.kind != ELParser.CLOSE_ARG) return null;
		}
		t = next(t);
		if(t == null || t.kind != ELParser.CLOSE) return null;
		return propName == null ? null : new String[]{prefix, propName};
	}
	
	private String getBundleValue(String prefix, String propertyName, int offset) {
		BundleEntry entry = getBundle(prefix);
		if(entry != null) {
			if(entry.offset > offset)return null;
			String name = prefix + "." + propertyName;
			try{
				String value = (String)entry.bundle.getObject(propertyName);
				if(!usedKeys.containsKey(name))
					usedKeys.put(name, new UsedKey(entry.uri, prefix, propertyName, value, entry.hashCode, entry.offset));
				return value;
			}catch(Exception ex){
				return null;
			}
		} 
		return null;
	}
	
	public void addBundleMapListener(BundleMapListener listener) {
		if (listener != null) {
			BundleMapListener[] newBundleMapListener = new BundleMapListener[bundleMapListeners.length + 1];
			System.arraycopy(bundleMapListeners, 0, newBundleMapListener, 0, bundleMapListeners.length);
			bundleMapListeners = newBundleMapListener;
			bundleMapListeners[bundleMapListeners.length - 1] = listener;
		}
	}
	
	public void removeBundleMapListener(BundleMapListener listener) {
		if (listener == null || bundleMapListeners.length == 0) return;
		int index = -1;
		for (int i = 0; i < bundleMapListeners.length; i++) {
			if (listener == bundleMapListeners[i]){
				index = i;
				break;
			}
		}
		if (index == -1) return;
		if (bundleMapListeners.length == 1) {
			bundleMapListeners = new BundleMapListener[0];
			return;
		}
		BundleMapListener[] newBundleMapListener = new BundleMapListener[bundleMapListeners.length - 1];
		System.arraycopy(bundleMapListeners, 0, newBundleMapListener, 0, index);
		System.arraycopy(bundleMapListeners, index + 1, newBundleMapListener, index, bundleMapListeners.length - index - 1);
		bundleMapListeners = newBundleMapListener;
	}
	
	private void fireBundleKeyChanged(String prefix, String key, String value) {
		for (int i = 0; i < bundleMapListeners.length; i++) {
			bundleMapListeners[i].bundleKeyChanged(prefix, key, value);
		}
	}
	
	class Expression{
		public String prefix;
		public String propertyName;
	}
	
	class BundleEntry{
		public ResourceBundle bundle;
		public String uri;
		public String prefix;
		public int hashCode;
		public int offset;
		
		public BundleEntry(ResourceBundle bundle, String uri, String prefix, int hashCode, int offset){
			this.bundle = bundle;
			this.uri = uri;
			this.prefix = prefix;
			this.hashCode = hashCode;
			this.offset = offset;
		}
	}
	
	class UsedKey{
		public int hashCode;
		public String uri;
		public String prefix;
		public String key;
		public String value;
		public int offset;
		
		public UsedKey(String uri, String prefix, String key, String value, int hashCode, int offset){
			this.uri = uri;
			this.prefix = prefix;
			this.key = key;
			this.value = value;
			this.hashCode = hashCode;
			this.offset = offset;
		}
	}
	
	class ML implements XModelTreeListener {

		public void nodeChanged(XModelTreeEvent event) {
		    boolean b = "yes".equals(VpePreference.SHOW_RESOURCE_BUNDLES.getValue());	
			if(isShowBundleUsageAsEL != b) {
				isShowBundleUsageAsEL = b;
				refresh();
			}			
		}

		public void structureChanged(XModelTreeEvent event) {
		}
		
	}

}
