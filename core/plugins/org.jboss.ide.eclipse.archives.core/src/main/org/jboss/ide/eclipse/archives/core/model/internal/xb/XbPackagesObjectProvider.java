package org.jboss.ide.eclipse.archives.core.model.internal.xb;

import org.jboss.xb.binding.GenericObjectModelProvider;
import org.jboss.xb.binding.MarshallingContext;

public class XbPackagesObjectProvider implements GenericObjectModelProvider {

	public Object getRoot(Object o, MarshallingContext context, String namespaceURI, String localName) {
		return o;
	}
	
	protected Object getNodeChildren(XbPackageNode node, String name)
	{
		if ("package".equals(name))
		{
			return node.getChildren(XbPackage.class);
		}
		else if ("folder".equals(name))
		{
			return node.getChildren(XbFolder.class);
		}
		else if ("fileset".equals(name))
		{
			return node.getChildren(XbFileSet.class);
		}
		else if ("properties".equals(name) && node instanceof XbPackageNodeWithProperties)
		{
			return ((XbPackageNodeWithProperties)node).getProperties();
		}
		else if ("property".equals(name) && node instanceof XbProperties)
		{
			return ((XbProperties)node).getProperties().getPropertyElements();
		}
		
		return null;
	}
	
	public Object getChildren(Object object, MarshallingContext context, String namespaceURI, String localName)
	{
		if (object instanceof XbPackageNode) {
			Object ret = getNodeChildren(((XbPackageNode)object), localName);
			return ret;
		}
		return null;
	}
	
	
	public Object getAttributeValue(Object object, MarshallingContext context, String namespaceURI, String localName)
	{
		if (object instanceof XbPackage)
		{
			XbPackage pkg = (XbPackage)object;
			if("id".equals(localName)) {
				return pkg.getId();
			} else if ("type".equals(localName))
				return pkg.getPackageType();
			else if ("name".equals(localName))
				return pkg.getName();
			else if ("exploded".equals(localName))
				return Boolean.valueOf(pkg.isExploded());
			else if ("todir".equals(localName))
				return pkg.getToDir();
			else if ("inWorkspace".equals(localName))
				return ""+pkg.isInWorkspace();
		}
		else if (object instanceof XbFolder)
		{
			XbFolder folder = (XbFolder) object;
			if ("name".equals(localName))
				return folder.getName();
		}
		else if (object instanceof XbFileSet)
		{
			XbFileSet fileset = (XbFileSet)object;
			if ("dir".equals(localName))
				return fileset.getDir();
			else if ("includes".equals(localName))
				return fileset.getIncludes();
			else if ("excludes".equals(localName))
				return fileset.getExcludes();
			else if ("inWorkspace".equals(localName))
				return "" + fileset.isInWorkspace();
		}
		else if (object instanceof XbProperty)
		{
			XbProperty prop = (XbProperty) object;
			if ("name".equals(localName))
				return prop.getName();
			else if ("value".equals(localName))
				return prop.getValue();
		}
		return null;
	}
	
	public Object getElementValue(Object object, MarshallingContext context, String namespaceURI, String localName) {
		return null;
	}
}
