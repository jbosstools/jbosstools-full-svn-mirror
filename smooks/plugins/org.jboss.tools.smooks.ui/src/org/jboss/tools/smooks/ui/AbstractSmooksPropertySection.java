/**
 * 
 */
package org.jboss.tools.smooks.ui;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.ui.editors.SmooksFormEditor;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 * @author Dart
 * 
 */
public class AbstractSmooksPropertySection extends AbstractPropertySection {

	protected boolean lock = false;

	protected void fireDirty() {
		SmooksGraphicalFormPage page = this.getGraphicalEditor();
		if (page != null) {
			SmooksFormEditor editor = (SmooksFormEditor) ((SmooksGraphicalFormPage) page)
					.getEditor();
			editor.fireEditorDirty(true);
		}
	}

	public SmooksGraphicalFormPage getGraphicalEditor() {
		IStructuredSelection selection = (IStructuredSelection) this
				.getSelection();
		if(selection == null) return null;
		Object obj = selection.getFirstElement();
		if (obj == null)
			return null;
		if (obj instanceof GraphicalEditPart) {
			GraphicalViewer viewer = (GraphicalViewer) ((GraphicalEditPart) obj)
					.getViewer();
			IEditorPart part = ((DefaultEditDomain) viewer.getEditDomain())
					.getEditorPart();
			if (part instanceof SmooksGraphicalFormPage) {
				return (SmooksGraphicalFormPage) part;
			}
		}

		return null;
	}

	protected Section createRootSection(
			TabbedPropertySheetWidgetFactory factory, Composite parent) {
		Composite main = factory.createComposite(parent);
		FillLayout fill = new FillLayout();
		fill.marginHeight = 8;
		fill.marginWidth = 8;
		main.setLayout(fill);
		return factory.createSection(main, Section.TITLE_BAR);
	}

	protected LineConnectionModel getLineConnectionModel() {
		ISelection s = (ISelection) this.getSelection();
		if (s instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) s;
			Object obj = selection.getFirstElement();
			if (obj == null)
				return null;
			if (obj instanceof EditPart) {
				Object model = ((EditPart) obj).getModel();
				if (model instanceof LineConnectionModel) {
					return (LineConnectionModel) model;
				}
			}
		}
		return null;
	}

	public boolean isLock() {
		return lock;
	}

	protected void unLockEventFire() {
		lock = false;
	}

	protected void lockEventFire() {
		lock = true;
	}

}
