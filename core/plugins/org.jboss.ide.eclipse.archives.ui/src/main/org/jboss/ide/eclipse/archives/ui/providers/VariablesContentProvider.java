package org.jboss.ide.eclipse.archives.ui.providers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IVariableProvider;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVFS;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariableManager;
import org.jboss.ide.eclipse.archives.ui.preferences.VariablesPreferencePage.Wrapped;

public class VariablesContentProvider implements ITreeContentProvider {
	private Comparator<IVariableProvider> comparator;
	public VariablesContentProvider(Comparator<IVariableProvider> c) {
		this.comparator = c;
	}
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof IVariableProvider ) {
			String[] props = ((IVariableProvider)parentElement).getVariableNames();
			Wrapped[] items = new Wrapped[props.length];
			for( int i = 0; i < props.length; i++ ) 
				items[i] = new Wrapped((IVariableProvider)parentElement, props[i]);
			return items;
		}
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return element instanceof IVariableProvider && ((IVariableProvider)element).getVariableNames().length > 0;
	}

	public Object[] getElements(Object inputElement) {
		List<IVariableProvider> elements = getSortedDelegates();
		return (IVariableProvider[]) elements
				.toArray(new IVariableProvider[elements.size()]);
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	public List<IVariableProvider> getSortedDelegates() {
		WorkspaceVFS vfs = (WorkspaceVFS)ArchivesCore.getInstance().getVFS();
		WorkspaceVariableManager mgr = vfs.getVariableManager();
		IVariableProvider[] delegates = mgr.getDelegates();
		
		// sort with changed data
		List<IVariableProvider> l = Arrays.asList(delegates);
		Collections.sort(l, this.comparator);
		return l;
	}
}
