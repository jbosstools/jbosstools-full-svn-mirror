package org.jboss.tools.bpmn2.process.diagram.preferences;

import org.eclipse.gmf.runtime.diagram.ui.preferences.AppearancePreferencePage;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2DiagramEditorPlugin;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2ProcessDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramAppearancePreferencePage extends AppearancePreferencePage {

	/**
	 * @generated
	 */
	public DiagramAppearancePreferencePage() {
		setPreferenceStore(Bpmn2ProcessDiagramEditorPlugin.getInstance()
				.getPreferenceStore());
	}
}
