package org.jboss.ide.eclipse.packages.ui;

import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewActionDelegate;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.osgi.framework.Bundle;

public class NodeContribution implements Comparable {
	private String id, label;
	private IViewActionDelegate actionDelegate;
	private ImageDescriptor icon;
	private int enablesForNodeType;
	private int weight;
	
	public static final int TYPE_ANY = -1;
	
	public NodeContribution (IConfigurationElement element)
	{
		id = element.getAttribute("id");
		label = element.getAttribute("label");
		
		try {
			actionDelegate = (IViewActionDelegate) element.createExecutableExtension("class");
		} catch (CoreException e) {
			Trace.trace(getClass(), e);
		}
		
		String iconPath = element.getAttribute("icon");
		String pluginId = element.getDeclaringExtension().getNamespaceIdentifier();
		Bundle bundle = Platform.getBundle(pluginId);
		URL iconURL = FileLocator.find(bundle, new Path(iconPath), null);
		if (iconURL == null)
		{
			iconURL = bundle.getEntry(iconPath);
		}
		icon = ImageDescriptor.createFromURL(iconURL);
		
		String enablesFor = element.getAttribute("enablesForNodeType");
		if ("any".equals(enablesFor)) {
			enablesForNodeType = TYPE_ANY;
		} else if ("package".equals(enablesFor)) {
			enablesForNodeType = IPackageNode.TYPE_PACKAGE;
		} else if ("folder".equals(enablesFor)) {
			enablesForNodeType = IPackageNode.TYPE_PACKAGE_FOLDER;
		} else if ("fileset".equals(enablesFor)) {
			enablesForNodeType = IPackageNode.TYPE_PACKAGE_FILESET;
		}
	}

	public boolean isEnabledForNodeType (int nodeType)
	{
		if (enablesForNodeType == TYPE_ANY) return true;
		
		if (nodeType == IPackageNode.TYPE_PACKAGE_REFERENCE)
			nodeType = IPackageNode.TYPE_PACKAGE;
		
		return (enablesForNodeType == nodeType);
	}
	
	public int compareTo(Object o) {
		if (o instanceof NodeContribution)
		{
			NodeContribution other = (NodeContribution) o;
			if (weight < other.getWeight()) return -1;
			else if (weight > other.getWeight()) return 1;
			else if (weight == other.getWeight()) {
				return label.compareTo(other.getLabel());
			}
		}
		return -1;
	}

	
	public IViewActionDelegate getActionDelegate() {
		return actionDelegate;
	}

	public ImageDescriptor getIcon() {
		return icon;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	
	public int getWeight() {
		return weight;
	}
}
