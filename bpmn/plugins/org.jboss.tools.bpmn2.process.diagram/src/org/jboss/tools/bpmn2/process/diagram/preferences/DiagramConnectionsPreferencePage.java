package org.jboss.tools.bpmn2.process.diagram.preferences;

import org.eclipse.gmf.runtime.diagram.ui.preferences.ConnectionsPreferencePage;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2DiagramEditorPlugin;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2ProcessDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramConnectionsPreferencePage extends ConnectionsPreferencePage {

	/**
	 * @generated
	 */
	public DiagramConnectionsPreferencePage() {
		setPreferenceStore(Bpmn2ProcessDiagramEditorPlugin.getInstance()
				.getPreferenceStore());
	}
}
