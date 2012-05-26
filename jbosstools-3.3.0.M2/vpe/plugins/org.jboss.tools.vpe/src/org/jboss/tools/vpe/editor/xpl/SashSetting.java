package org.jboss.tools.vpe.editor.xpl;

import java.util.StringTokenizer;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;


 
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
	
	private Control listenedControl=null;
	
	private int weights[];
	
	public SashSetting(CustomSashForm s) {
		sash = s;		
	}
	
	public void apply() {
		/*
		 * Set weights from the preference page
		 */
		int defaultWeight = JspEditorPlugin.getDefault().getPreferenceStore()
				.getInt(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_WEIGHTS);
		int[] weights = sash.getWeights();
		if (weights.length > 2) {
			String splitting = JspEditorPlugin.getDefault().getPreferenceStore()
			.getString(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING); 
			if (defaultWeight == 0) {
				if (CustomSashForm.isSourceEditorFirst(splitting)) {
					sash.maxDown();
				} else {
					sash.maxUp();
				}
			} else if (defaultWeight == 1000) {
				if (CustomSashForm.isSourceEditorFirst(splitting)) {
					sash.maxUp();
				} else {
					sash.maxDown();
				}
			} else {
				if (CustomSashForm.isSourceEditorFirst(splitting)) {
					weights[0] = 1000 - defaultWeight;
					weights[1] = defaultWeight;
				} else {
					weights[0] = defaultWeight;
					weights[1] = 1000 - defaultWeight;
				}
				sash.setWeights(weights);
			}
		}
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

	public void dispose() {
		
	}
}
