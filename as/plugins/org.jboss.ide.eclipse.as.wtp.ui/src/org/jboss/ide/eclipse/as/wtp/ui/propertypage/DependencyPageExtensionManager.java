package org.jboss.ide.eclipse.as.wtp.ui.propertypage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.jboss.ide.eclipse.as.wtp.ui.WTPOveridePlugin;

public class DependencyPageExtensionManager {
	private static DependencyPageExtensionManager manager = null;
	public static DependencyPageExtensionManager getManager() {
		if( manager == null )
			manager = new DependencyPageExtensionManager();
		return manager;
	}
	
	private HashMap<String, IDependencyPageProvider> providers = null;
	
	public IDependencyPageProvider getProvider(IFacetedProject project) {
		if( providers == null )
			loadProviders();
		Iterator<IDependencyPageProvider> i = providers.values().iterator();
		IDependencyPageProvider temp;
		while(i.hasNext()) {
			temp = i.next();
			if( temp.canHandle(project))
				return temp;
		}
		return null;
	}
	
	private void loadProviders() {
		HashMap<String, IDependencyPageProvider> temp = new HashMap<String, IDependencyPageProvider>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(
				WTPOveridePlugin.PLUGIN_ID, "moduleDependencyPropertyPage"); //$NON-NLS-1$
		for( int i = 0; i < cf.length; i++ ) {
			try {
				temp.put(cf[i].getAttribute("id"),  //$NON-NLS-1$
					(IDependencyPageProvider)cf[i].createExecutableExtension("class"));  //$NON-NLS-1$
			} catch( CoreException ce ) {}
		}
		providers = temp;
	}
	
	public WizardFragment[] loadAllReferenceWizardFragments() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(
				WTPOveridePlugin.PLUGIN_ID, "referenceWizardFragment"); //$NON-NLS-1$
		ArrayList<WizardFragment> list = new ArrayList<WizardFragment>();
		for( int i = 0; i < cf.length; i++ ) {
			try {
				list.add((WizardFragment)cf[i].createExecutableExtension("class"));
			} catch( CoreException ce) {}
		}
		return (WizardFragment[]) list.toArray(new WizardFragment[list.size()]);
	}
	
	public WizardFragment loadReferenceWizardFragment(String id) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(
				WTPOveridePlugin.PLUGIN_ID, "referenceWizardFragment"); //$NON-NLS-1$
		for( int i = 0; i < cf.length; i++ ) {
			if( cf[i].getAttribute("id").equals(id))
				try {
					return (WizardFragment)cf[i].createExecutableExtension("class");
				} catch( CoreException ce) {}
		}
		return null;
	}
	
	public ReferenceExtension[] getReferenceExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(
				WTPOveridePlugin.PLUGIN_ID, "referenceWizardFragment"); //$NON-NLS-1$
		ArrayList<ReferenceExtension> list = new ArrayList<ReferenceExtension>();
		for( int i = 0; i < cf.length; i++ ) {
			list.add(new ReferenceExtension(cf[i]));
		}
		return (ReferenceExtension[]) list
				.toArray(new ReferenceExtension[list.size()]);
	}
	
	public class ReferenceExtension {
		private IConfigurationElement element;
		private String id, name, imageLoc;
		private Image image;
		public ReferenceExtension(IConfigurationElement element) {
			this.element = element;
			this.id = element.getAttribute("id");
			this.name = element.getAttribute("name");
			this.imageLoc = element.getAttribute("icon");
		}
		public String getId() { return this.id;}
		public String getName() { return this.name; }
		public Image getImage() { 
			if( image == null ) {
				if( imageLoc != null && element.getContributor().getName() != null) {
					ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(element.getContributor().getName(), imageLoc);
					image = desc.createImage();
				}
			}
			return image;
		}
		public void disposeImage() {
			if( image != null ) {
				image.dispose();
				image = null;
			}
		}
	}
	
	
	public static IVirtualComponentLabelProvider[] loadDelegates() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(
				WTPOveridePlugin.PLUGIN_ID, "virtualComponentLabelProvider"); //$NON-NLS-1$
		List<IConfigurationElement> list = Arrays.asList(cf);
		Comparator c = new Comparator<IConfigurationElement>() {
			public int compare(IConfigurationElement o1,
					IConfigurationElement o2) {
				String o1String, o2String;
				int o1int, o2int;
				o1String=o1.getAttribute("weight");
				o2String=o2.getAttribute("weight");
				o1int = Integer.parseInt(o1String);
				o2int = Integer.parseInt(o1String);
				return o1int-o2int;
			}
		};
		Collections.sort(list, c);
		ArrayList<IVirtualComponentLabelProvider> retList = new ArrayList<IVirtualComponentLabelProvider>();
		Iterator<IConfigurationElement> i = list.iterator();
		while(i.hasNext()) {
			try {
				retList.add((IVirtualComponentLabelProvider)i.next().createExecutableExtension("class"));
			} catch( CoreException ce) {
				// log
			}
		}
		return (IVirtualComponentLabelProvider[]) retList
				.toArray(new IVirtualComponentLabelProvider[retList.size()]);
	}

	
}
