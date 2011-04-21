/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.ui.AbstractSmooksPropertySection;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;

/**
 * @author Dart
 * 
 */
public class XMLNodePropertySection extends AbstractSmooksPropertySection {

	private Text nodeText;

	/**
	 * 
	 */
	public XMLNodePropertySection() {
	}

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();
		Section section = createRootSection(factory, parent);
		section.setText("XML Node"); //$NON-NLS-1$

		section.setLayout(new FillLayout());

		Composite com = factory.createComposite(section);
		section.setClient(com);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		com.setLayout(gl);

		factory.createLabel(com, "Node Name : ");
		nodeText = factory.createText(com, "");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		nodeText.setLayoutData(gd);
		hookNodeText();
		getGraphicalEditor();
		factory.paintBordersFor(com);
	}

	private void hookNodeText() {
		nodeText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isLock())
					return;
				String text = nodeText.getText();
				AbstractXMLObject model = getModel();
				if (model != null && model.isCanEdit()) {
					model.setName(text);
					
				}
			}

		});
	}
	
	

	@Override
	public SmooksGraphicalFormPage getGraphicalEditor() {
		IWorkbenchPart part = getPart();
		return null;
	}

	@Override
	public void refresh() {
		super.refresh();
		lockEventFire();
		AbstractXMLObject model = getModel();
		if (model != null) {
			String name = model.getName();
			nodeText.setText(name);
		}
		unLockEventFire();
	}

	private AbstractXMLObject getModel(){
		ISelection selection =  this.getSelection();
		if(selection instanceof IStructuredSelection){
			Object model = ((IStructuredSelection)selection).getFirstElement();
			if(model instanceof AbstractXMLObject){
				return (AbstractXMLObject)model;
			}
		}
		return null;
	}

}
