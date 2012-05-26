package org.jboss.tools.vpe.editor.xpl;

import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.vpe.VpePlugin;

 
/**
 * EditorSettings holds a set of settings and drive their life cycle.
 * @since 1.1.0
 */
public class EditorSettings {
	
	public final static String SEPERATOR = ";"; //$NON-NLS-1$
	
	
	protected IResource file;
	protected IEditorPart editor;	
	protected ArrayList settings = new ArrayList();
	
	
	/**
	 * The ability to apply/monitor/persist some editor setting
	 * It is up to the setting to monitor and persist it setting details.
	 * the apply method will be called when the editor comes up.
	 * @since 1.1.0
	 */
	public interface ISetting {
		public void apply();
		public void dispose();	
		public void setQualifier(String q);
		public void setEditor(IEditorPart e);
		public void setResource(IResource r);
	}
	
	protected EditorSettings(IResource f, IEditorPart e) {
		file = f;
		editor = e;			
	}
	
	
	public void apply() {
		for (int i = 0; i < settings.size(); i++) {
			((ISetting)settings.get(i)).apply();			
		}
	}
	
	public void addSetting(ISetting s) {
		s.setQualifier(VpePlugin.PLUGIN_ID);
		s.setEditor(editor);
		s.setResource(file);
		settings.add(s);
	}
	
	public void addSettingAndApply(ISetting s) {
		addSetting(s);
		s.apply();
	}
	
	public void dispose() {
		for (int i = 0; i < settings.size(); i++) {
			((ISetting)settings.get(i)).dispose();			
		}
		editor=null;
	}
	
	public void clearOldSettings() {
		settings.clear();
	}
	
	public void setInput(FileEditorInput input) {
		file = input.getFile();
		for (int i = 0; i < settings.size(); i++) {
			((ISetting)settings.get(i)).setResource(file);			
		}		
	}
	
	
	public static EditorSettings getEditorSetting (IEditorPart editor) {
		EditorSettings settings = null;
		if (editor!=null && editor.getEditorInput() instanceof FileEditorInput) {
		   IResource r = ((FileEditorInput)editor.getEditorInput()).getFile();
		   settings = new EditorSettings(r, editor);
		}
		return settings;				 
	}
	
	public String toString() {
		return file.getName();
	}

}
