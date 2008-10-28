/**
 * 
 */
package org.jboss.tools.smooks.ui;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.jboss.tools.smooks.ui.editors.SmooksFormEditor;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;

/**
 * @author Dart
 * 
 */
public class AbstractSmooksPropertySection extends AbstractPropertySection {
	protected void fireDirty() {
		IStructuredSelection selection = (IStructuredSelection) this
				.getSelection();
		Object obj = selection.getFirstElement();
		if (obj == null)
			return;
		if (obj instanceof GraphicalEditPart) {
			GraphicalViewer viewer  = (GraphicalViewer) ((GraphicalEditPart)obj).getViewer();
			IEditorPart part = ((DefaultEditDomain)viewer.getEditDomain()).getEditorPart();
			if(part instanceof SmooksGraphicalFormPage){
				SmooksFormEditor editor = (SmooksFormEditor)((SmooksGraphicalFormPage)part).getEditor();
				editor.fireEditorDirty(true);
			}
		}
	}
}
