package org.jboss.ide.eclipse.archives.ui.providers;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.archives.core.model.IVariableProvider;
import org.jboss.ide.eclipse.archives.ui.preferences.VariablesPreferencePage.Wrapped;

public class VariablesLabelProvider extends BaseLabelProvider implements ILabelProvider {
	private HashMap<Image, Image> disabledImages;
	private IVariableEnablementChecker checker;
	
	public interface IVariableEnablementChecker {
		public boolean isEnabled(IVariableProvider element);
	}
	
	public VariablesLabelProvider(IVariableEnablementChecker checker) {
		disabledImages = new HashMap<Image, Image>();
	}
	public Image getImage(Object element) {
		if( element instanceof IVariableProvider ) {
			String id = ((IVariableProvider)element).getId();
			
			boolean enabled = checker != null ? checker.isEnabled((IVariableProvider)element) : ((IVariableProvider)element).getEnabled();
			if(id.equals("org.jboss.ide.eclipse.archives.core.resourceVariableProvider"))
				return getImage2(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER), enabled);
			if( id.equals("org.jboss.ide.eclipse.archives.core.classpathVariableProvider"))
				return getImage2(JavaPlugin.getDefault().getImageRegistry().get(JavaPluginImages.IMG_OBJS_EXTJAR), enabled);
			if( id.equals("org.jboss.ide.eclipse.archives.core.stringReplacementValueVariables"))
				return getImage2(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_PASTE), enabled);
		}
		if( element instanceof Wrapped ) {
			return getImage(((Wrapped)element).getProvider());
		}
		return null;
	}
	protected Image getImage2(Image original, boolean enabled) {
		if( enabled ) 
			return original;
		if( disabledImages.get(original) == null ) {
			Image i2 = new DecorationOverlayIcon(original, JavaPluginImages.DESC_OVR_DEPRECATED, IDecoration.TOP_LEFT).createImage();
			disabledImages.put(original, i2);
		}
		return disabledImages.get(original);
	}
	public String getText(Object element) {
		if( element instanceof IVariableProvider ) {
			return ((IVariableProvider)element).getName();
		}
		if( element instanceof Wrapped )
			return ((Wrapped)element).toString();
		return "";
	}
    public void dispose() {
    	super.dispose();
    	Iterator<Image> i = disabledImages.values().iterator();
    	while(i.hasNext()) 
    		i.next().dispose();
    }
}
