package org.jboss.ide.eclipse.as.ui.views.server.extensions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.jboss.ide.eclipse.as.ui.preferencepages.ViewProviderPreferenceComposite;
import org.jboss.ide.eclipse.as.ui.views.server.JBossServerView;
import org.jboss.ide.eclipse.as.ui.views.server.JBossServerTableViewer.ContentWrapper;

public abstract class JBossServerViewExtension {
	protected ServerViewProvider provider;
	
	/**
	 * Which extension point is mine.
	 * @param provider
	 */
	public void setViewProvider(ServerViewProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * Should query preferencestore to see if I'm enabled or not
	 * @return
	 */
	public boolean isEnabled() {
		return provider.isEnabled();
	}
	
	
	public void init() {
	}
	public void enable() {
	}
	public void disable() {
	}
	public void dispose() {
		if( getPropertySheetPage() != null ) 
			getPropertySheetPage().dispose();
	}
	
	
	public void fillContextMenu(Shell shell, IMenuManager menu, Object selection) {
	}

	
	public ITreeContentProvider getContentProvider() {
		return null;
	}
	public  LabelProvider getLabelProvider() {
		return null;
	}
	
	public IPropertySheetPage getPropertySheetPage() {
		return null;
	}
	
	public ViewProviderPreferenceComposite createPreferenceComposite(Composite parent) {
		return null;
	}
	
	public Image createIcon() {
		return null;
	}
	
	protected void suppressingRefresh(Runnable runnable) {
		JBossServerView.getDefault().getJBViewer().suppressingRefresh(runnable);
	}
	
	protected void refreshViewer() {
		refreshViewer(null);
	}
	protected void refreshViewer(Object o) {
		if( isEnabled() ) {
			try {
				if( o == null )
					JBossServerView.getDefault().getJBViewer().refresh(provider);
				else
					JBossServerView.getDefault().getJBViewer().refresh(new ContentWrapper(o, provider));
			} catch(Exception e) {
			}
		}
	}
	protected void removeElement(Object o) {
		JBossServerView.getDefault().getJBViewer().remove(new ContentWrapper(o, provider));
	}
	protected void addElement(Object parent, Object child) {
		JBossServerView.getDefault().getJBViewer().add(new ContentWrapper(parent, provider), new ContentWrapper(child, provider));
	}
}
