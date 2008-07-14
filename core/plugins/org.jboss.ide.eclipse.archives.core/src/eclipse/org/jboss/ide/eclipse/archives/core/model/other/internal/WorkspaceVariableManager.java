package org.jboss.ide.eclipse.archives.core.model.other.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.ArchivesCorePlugin;
import org.jboss.ide.eclipse.archives.core.model.IArchivesLogger;
import org.jboss.ide.eclipse.archives.core.model.IExtensionManager;
import org.jboss.ide.eclipse.archives.core.model.IVariableManager;
import org.jboss.ide.eclipse.archives.core.model.IVariableProvider;
import org.osgi.service.prefs.BackingStoreException;

public class WorkspaceVariableManager implements IVariableManager {
	public static final String DEFAULT_PROVIDER = "org.jboss.ide.eclipse.archives.core.defaultVariableProvider";
	protected IVariableProvider[] delegates;
	public WorkspaceVariableManager() {
	}
	
	public IVariableProvider[] getDelegates() {
		sort();
		IVariableProvider[] copy = new IVariableProvider[delegates.length];
		for( int i = 0; i < delegates.length; i++ ) 
			copy[i] = delegates[i];
		return copy;
	}
	
	public void sort() {
		if( delegates == null ) {
			IExtensionManager mgr = ArchivesCore.getInstance().getExtensionManager();
			delegates = ((WorkspaceExtensionManager)mgr).getVariableProviders();
		}
		List<IVariableProvider> l = Arrays.asList(delegates);
		Comparator<IVariableProvider> x = new Comparator<IVariableProvider>() {
			public int compare(IVariableProvider o1, IVariableProvider o2) {
				// different enablements
				if( o1.getEnabled() != o2.getEnabled())
					return o1.getEnabled() ? 1 : -1;
				// now weights
				if( o1.getWeight() != o2.getWeight() ) 
					return o1.getWeight() > o2.getWeight() ? 1 : -1;
				return 0;
			} 
		};
		Collections.sort(l, x);
		delegates = (IVariableProvider[]) l.toArray(new IVariableProvider[l.size()]);
	}
	
	public String[] getVariableNames() {
		sort();
		ArrayList<String> list = new ArrayList<String>();
		String[] names;
		for(int i = 0; i < delegates.length; i++ ) {
			if( delegates[i].getEnabled()) {
				names = delegates[i].getVariableNames();
				for( int j = 0; j < names.length; j++ ) {
					if( !list.contains(names[j]))
						list.add(names[j]);
				}
			}
		}

		return (String[]) list.toArray(new String[list.size()]);
	}
	
	public boolean containsVariable(String variable) {
		sort();
		for(int i = 0; i < delegates.length; i++ ) 
			if( delegates[i].getEnabled() && delegates[i].containsVariable(variable))
				return true;
		return false;
	}

	public String getVariableValue(String variable) {
		sort();
		for(int i = 0; i < delegates.length; i++ ) 
			if( delegates[i].getEnabled() && delegates[i].containsVariable(variable))
				return delegates[i].getVariableValue(variable);
		return null;
	}
	
	
	public IVariableProvider getVariableLocation(String variable) {
		sort();
		for(int i = 0; i < delegates.length; i++ ) 
			if( delegates[i].getEnabled() && delegates[i].containsVariable(variable))
				return delegates[i];
		return null;
	}
		
	public static abstract class AbstractVariableProvider implements IVariableProvider {
		protected String id;
		protected String name;
		protected boolean enabled;
		protected int weight;
		protected int defaultWeight;
		
		public AbstractVariableProvider(String id, String name, int defaultWeight) {
			this.id = id;
			this.name = name;
			this.defaultWeight = defaultWeight;
		}
		
		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
		public boolean getEnabled() {
			IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
			return prefs.getBoolean(getPreferencePrefix() + "enabled", true);
		}
		
		public int getWeight() {
			IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
			return prefs.getInt(getPreferencePrefix() + "weight", defaultWeight);
		}

		public void setEnabled(boolean b) {
			try {
				IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
				prefs.putBoolean(getPreferencePrefix() + "enabled", b);
				prefs.flush();
				enabled = b;
			} catch (BackingStoreException e) { 
				ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, e.getMessage(), e);
			}
		}
		
		public void setWeight(int i) {
			try {
				IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
				prefs.putInt(getPreferencePrefix() + "weight", i);
				prefs.flush();
				weight = i;
			} catch (BackingStoreException e) { 
				ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, e.getMessage(), e);
			}
		}
		
		public int getDefaultWeight() {
			return defaultWeight;
		}
		
		public abstract String getPreferencePrefix();
	}
	
	
	public static class DefaultVariableProvider extends AbstractVariableProvider {
		public static final String VALUE_PREFIX = "org.jboss.ide.eclipse.archives.core.model.other.internal.VariableManager.values.";
		public static final String PREFERENCE_PREFIX = "org.jboss.ide.eclipse.archives.core.model.other.internal.VariableManager.prefs.";
		public static final String ID = "org.jboss.ide.eclipse.archives.core.defaultVariableProvider";
		public DefaultVariableProvider() {
			super(ID, "Archives Variables", 0);
		}
		public String getPreferencePrefix() {
			return PREFERENCE_PREFIX;
		}
		public boolean containsVariable(String variable) {
			IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
			return prefs.get(VALUE_PREFIX + variable, null) != null;
		}

		public String[] getVariableNames() {
			int prefixLength = VALUE_PREFIX.length();
			ArrayList<String> list = new ArrayList<String>();
			try {
				IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
				String[] keys = prefs.keys();
				for( int i = 0; i < keys.length; i++ )
					if( keys[i].startsWith(VALUE_PREFIX) && !list.contains(keys[i].substring(prefixLength))) {
						list.add(keys[i].substring(prefixLength));
					}
			} catch( BackingStoreException bse) {}
			return (String[]) list.toArray(new String[list.size()]);
		}

		public String getVariableValue(String variable) {
			IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
			return prefs.get(VALUE_PREFIX + variable, null);
		}
	}
	
	public static class LinkedResourceVariableProvider extends AbstractVariableProvider {
		public static final String PREFERENCE_PREFIX = "org.jboss.ide.eclipse.archives.core.model.other.internal.LinkedResourceVariableManager.prefs.";
		public static final String ID = "org.jboss.ide.eclipse.archives.core.resourceVariableProvider";
		public LinkedResourceVariableProvider() {
			super(ID, "Linked Resources Path Variables", 1);
		}
		public String getPreferencePrefix() {
			return PREFERENCE_PREFIX;
		}
		public boolean containsVariable(String variable) {
			return ResourcesPlugin.getWorkspace().getPathVariableManager().getValue(variable) != null;
		}

		public String[] getVariableNames() {
			return ResourcesPlugin.getWorkspace().getPathVariableManager().getPathVariableNames();
		}

		public String getVariableValue(String variable) {
			return ResourcesPlugin.getWorkspace().getPathVariableManager().getValue(variable).toString();
		}
	}
	
	public static class ClasspathVariableProvider extends AbstractVariableProvider {
		public static final String PREFERENCE_PREFIX = "org.jboss.ide.eclipse.archives.core.model.other.internal.JDTVariableProvider.prefs.";
		public static final String ID = "org.jboss.ide.eclipse.archives.core.classpathVariableProvider";
		public ClasspathVariableProvider() {
			super(ID, "Classpath Variables", 2);
		}
		public String getPreferencePrefix() {
			return PREFERENCE_PREFIX;
		}

		public boolean containsVariable(String variable) {
			return Arrays.asList(JavaCore.getClasspathVariableNames()).contains(variable);
		}

		public String[] getVariableNames() {
			return JavaCore.getClasspathVariableNames();
		}

		public String getVariableValue(String variable) {
			return JavaCore.getClasspathVariable(variable).toString();
		}
	}
	

	public static class ValueVariableProvider extends AbstractVariableProvider {
		public static final String PREFERENCE_PREFIX = "org.jboss.ide.eclipse.archives.core.model.other.internal.ValueVariableProvider.prefs.";
		public static final String ID = "org.jboss.ide.eclipse.archives.core.stringReplacementValueVariables";
		public ValueVariableProvider() {
			super(ID, "String Replacement Variables", 3);
		}
		public String getPreferencePrefix() {
			return PREFERENCE_PREFIX;
		}

		public boolean containsVariable(String variable) {
			return VariablesPlugin.getDefault().getStringVariableManager().getValueVariable(variable) != null;
		}

		public String[] getVariableNames() {
			ArrayList<String> list = new ArrayList<String>();
			IStringVariable[] keys2 = VariablesPlugin.getDefault().getStringVariableManager().getValueVariables();
			for( int i = 0; i < keys2.length; i++ )
				list.add(keys2[i].getName());
			return (String[]) list.toArray(new String[list.size()]);
		}

		public String getVariableValue(String variable) {
			IValueVariable v = VariablesPlugin.getDefault().getStringVariableManager().getValueVariable(variable);
			if( v != null )
				return v.getValue();
			return null;
		}
	}
	
	public void setValue(String name, String value) {
		try {
			IEclipsePreferences prefs = new DefaultScope().getNode(ArchivesCorePlugin.PLUGIN_ID);
			if( value != null )
				prefs.put(DefaultVariableProvider.VALUE_PREFIX + name, value.toString());
			else 
				prefs.remove(DefaultVariableProvider.VALUE_PREFIX + name);
			prefs.flush();
		} catch (BackingStoreException e) { 
			ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, e.getMessage(), e);
		}
	}
}