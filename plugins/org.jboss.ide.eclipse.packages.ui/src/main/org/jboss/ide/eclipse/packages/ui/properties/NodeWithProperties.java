package org.jboss.ide.eclipse.packages.ui.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageNodeImpl;

public class NodeWithProperties implements IAdaptable, IPropertySource {

	private PackageNodeImpl node;
	
	public NodeWithProperties (PackageNodeImpl node)
	{
		this.node = node;
	}

	public Object getEditableValue() {
		return node;
	}
	
	private static class NodePropertyDescriptor extends PropertyDescriptor
	{
		public NodePropertyDescriptor (Object id, String displayName)
		{
			super(id, displayName);
		}
		
		public CellEditor createPropertyEditor(Composite parent) {
			CellEditor editor = new TextCellEditor(parent);
			if (getValidator() != null)
				editor.setValidator(getValidator());
			
			return editor;
		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		Properties props = node.getProperties();
		ArrayList descriptors = new ArrayList();
		
		for (Iterator iter = props.keySet().iterator(); iter.hasNext(); )
		{
			String property = (String) iter.next();
			
			descriptors.add(new NodePropertyDescriptor(property, property));
		}
		
		return (IPropertyDescriptor[]) descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	public Object getPropertyValue(Object id) {
		return node.getProperty((String)id);
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {}

	public void setPropertyValue(Object id, Object value) {
		node.setProperty((String)id, (String)value);
	}

	public PackageNodeImpl getNode() {
		return node;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof IPackageNode)
		{
			return node.equals(obj);
		}
		else return super.equals(obj);
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class)
		{
			return this;
		} else if (adapter == IPackageNode.class) {
			return node;
		}
		return null;
	}
}
