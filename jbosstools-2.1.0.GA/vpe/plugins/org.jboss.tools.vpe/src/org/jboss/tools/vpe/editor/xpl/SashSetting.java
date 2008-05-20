package org.jboss.tools.vpe.editor.xpl;

import java.util.StringTokenizer;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.vpe.VpePlugin;


 
/**
 * This setting will monitor the sash' weights
 * This setting is not global and will be persisted on a resource.
 * 
 * @since 1.1.0
 */
public class SashSetting implements EditorSettings.ISetting {
	
	public final static String id = "Settings.Sash"; //$NON-NLS-1$
	
	
	private IEditorPart editor;
	private IResource resource;
	private QualifiedName name;
	private CustomSashForm sash;
	
	private ControlListener listener=null;
	private Control listenedControl=null;
	
	private int weights[];
	
	/**
	 * Save both the current weights, and the (potential) save
	 * weights.  The first value is the size of the 
	 * current weights
	 * 
	 * @since 1.1.0
	 */
	protected void updateWeights() {
		weights = sash.getWeights();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < weights.length; i++) {
			if (i>0)
				sb.append(EditorSettings.SEPERATOR);
			sb.append(weights[i]);
		}
		sb.append(EditorSettings.SEPERATOR);
		sb.append(-1);  // seperate between current weights, and saved weights
		sb.append(EditorSettings.SEPERATOR);
		sb.append(sash.getSavedWeight());
		try {
			resource.setPersistentProperty(name,sb.toString());
		} catch (CoreException e) {
			VpePlugin.getPluginLog().logError(e);
		}
	}
	
	public SashSetting(CustomSashForm s) {
		sash = s;		
	}
	
	protected void addListener() {
		if (listener==null && !sash.isDisposed()) {
			listener = new ControlListener() {	
				public void controlResized(ControlEvent e) {
					updateWeights();
				}	
				public void controlMoved(ControlEvent e) {
					updateWeights();	
				}	
			};
			listenedControl = sash.getChildren()[0];
			listenedControl.addControlListener(listener);			
	    }				
	}
	
	protected void removeListener() {
		if (listenedControl!=null && !listenedControl.isDisposed()) {
			listenedControl.removeControlListener(listener);
			listener=null;
			listenedControl=null;
		}
	}
	
	public void apply() {
		try {
			String val = resource.getPersistentProperty(name);
			if (val!=null) {
				StringTokenizer st = new StringTokenizer(val,EditorSettings.SEPERATOR);
				weights = new int[st.countTokens()];
				int index = 0;				
				while(st.hasMoreTokens()) {
					String s = st.nextToken();
					weights[index++]=Integer.parseInt(s);
				}
				for (index = 0; index < weights.length; index++) {
					if (weights[index]<0)
						break;					
				}
				int saved = -1;
				if (index<weights.length) {
					int w[] = new int[index];
					for (int i = 0; i < w.length; i++) {
						w[i] = weights[i];						
					}
					saved = weights[index+1];					
					weights=w;
				}				
				sash.setWeights(weights);
				if (saved>=0)
				   sash.setCurrentSavedWeight(saved);
			}
		} catch (CoreException e1) {
			VpePlugin.getPluginLog().logError(e1);
		}
		addListener();
	}


	public void dispose() {		
		removeListener();
	}

	public void setQualifier(String q) {
		name = new QualifiedName(q,id);		
	}

	public void setEditor(IEditorPart e) {
		editor = e;		
	}

	public void setResource(IResource r) {
		resource = r;		
	}
}
