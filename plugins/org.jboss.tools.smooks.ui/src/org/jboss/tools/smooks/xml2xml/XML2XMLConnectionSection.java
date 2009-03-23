/**
 * 
 */
package org.jboss.tools.smooks.xml2xml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.ui.AbstractSmooksPropertySection;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;
import org.jboss.tools.smooks.xml.ui.XMLPropertiesSection;

/**
 * @author DartPeng
 * 
 */
public class XML2XMLConnectionSection extends AbstractSmooksPropertySection {

	private Button mappingButton;
	private Button bindingButton;

	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();

		Section section = createRootSection(factory, parent);
		section.setText("XML2XML Properties"); //$NON-NLS-1$
		Composite controlComposite = factory.createComposite(section);
		section.setClient(controlComposite);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;

		controlComposite.setLayout(gl);

		mappingButton = factory.createButton(controlComposite, "Mapping Type",
				SWT.RADIO);
		bindingButton = factory.createButton(controlComposite, "Binding Type",
				SWT.RADIO);

		hookButton();
	}

	private void hookButton() {
		mappingButton.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isLock()) return;
				if(mappingButton.getSelection()) return;
				Object obj = getReferenceSourceModel();
				if(obj instanceof TagPropertyObject){
					return;
				}
				PropertyModel pro = new PropertyModel(XMLPropertiesSection.MAPPING_TYPE,XMLPropertiesSection.MAPPING);
				LineConnectionModel line = getLineConnectionModel();
				if(line != null){
					line.addPropertyModel(pro);
					fireDirty();
				}
			}
			
		});
		bindingButton.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isLock()) return;
				if(bindingButton.getSelection()) return;
				Object obj = getReferenceSourceModel();
				if(obj instanceof TagObject){
					return;
				}
				PropertyModel pro = new PropertyModel(XMLPropertiesSection.MAPPING_TYPE,XMLPropertiesSection.BINDING);
				LineConnectionModel line = getLineConnectionModel();
				if(line != null){
					line.addPropertyModel(pro);
					fireDirty();
				}
			}
			
		});
	}


	@Override
	public void refresh() {
		lockEventFire();
		LineConnectionModel line = getLineConnectionModel();
		if (line != null) {
			Object mapping = line
					.getProperty(XMLPropertiesSection.MAPPING_TYPE);
			Object sourceModel = getReferenceSourceModel();
			Object targetModel = getReferenceTargetModel();
			mappingButton.setEnabled(true);
			bindingButton.setEnabled(true);
			if(sourceModel instanceof TagObject){
				mappingButton.setEnabled(true);
				bindingButton.setEnabled(false);
			}
			if(sourceModel instanceof TagPropertyObject){
				mappingButton.setEnabled(false);
				bindingButton.setEnabled(true);
			}
			if (XMLPropertiesSection.MAPPING.equals(mapping)) {
				mappingButton.setSelection(true);
				bindingButton.setSelection(false);
			}
			if (XMLPropertiesSection.BINDING.equals(mapping)) {
				mappingButton.setSelection(false);
				bindingButton.setSelection(true);
			}
		}
		unLockEventFire();
	}

}
