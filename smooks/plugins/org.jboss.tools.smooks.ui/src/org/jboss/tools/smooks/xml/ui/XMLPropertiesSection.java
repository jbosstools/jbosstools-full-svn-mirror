/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.ui.AbstractSmooksPropertySection;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;

/**
 * @author Dart
 * 
 */
public class XMLPropertiesSection extends AbstractSmooksPropertySection {

	private static final String SELECTOR_NAMESPACE = "selector-namespace";
	private Text namespaceText;
	private Button checkButton;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();
		Section section = createRootSection(factory, parent);
		section.setText("XML Properties"); //$NON-NLS-1$

		section.setLayout(new FillLayout());

		Composite com = factory.createComposite(section);
		section.setClient(com);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		com.setLayout(gl);

		checkButton = factory.createButton(com, "Set Namespace", SWT.CHECK);
		checkButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isLock())
					return;
				boolean check = checkButton.getSelection();
				if (check) {
					LineConnectionModel connection = getLineConnectionModel();
					AbstractXMLObject obj = getXMLSourceModel();
					if (connection != null && obj != null) {
						PropertyModel property = new PropertyModel();
						property.setName(SELECTOR_NAMESPACE);
						property.setValue(obj.getNamespaceURL());
						connection.addPropertyModel(property);
					}
				} else {
					LineConnectionModel connection = getLineConnectionModel();
					if (connection != null) {
						connection.removePropertyModel(SELECTOR_NAMESPACE);
					}
				}
				fireDirty();
			}

		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		checkButton.setLayoutData(gd);

		factory.createLabel(com, "Namespace : ");
		namespaceText = factory.createText(com, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		namespaceText.setLayoutData(gd);
		namespaceText.setEditable(false);
	}

	@Override
	public void refresh() {
		super.refresh();
		lockEventFire();
		checkButton.setSelection(false);
		unLockEventFire();
		AbstractXMLObject source = this.getXMLSourceModel();
		if (source != null) {
			String namespace = source.getNamespaceURL();
			if (namespace != null) {
				namespaceText.setText(namespace);
				LineConnectionModel connection = getLineConnectionModel();
				if (connection != null) {
					List<PropertyModel> list = connection.getProperties();
					for (Iterator<PropertyModel> iterator = list.iterator(); iterator
							.hasNext();) {
						PropertyModel propertyModel = (PropertyModel) iterator
								.next();
						if (propertyModel.getName()
								.equals(SELECTOR_NAMESPACE)) {
							if (propertyModel.getValue().equals(namespace)) {
								checkButton.setEnabled(true);
								lockEventFire();
								checkButton.setSelection(true);
								unLockEventFire();
							}
						}
					}
				}
			}else{
				checkButton.setEnabled(false);
			}
		}
	}

	/**
	 * Get the XML type source model
	 * 
	 * @return XML data type source model
	 */
	private AbstractXMLObject getXMLSourceModel() {
		LineConnectionModel connection = this.getLineConnectionModel();
		if (connection != null) {
			AbstractStructuredDataModel sourceModel = (AbstractStructuredDataModel) connection
					.getSource();
			if (sourceModel != null) {
				Object source = sourceModel.getReferenceEntityModel();
				if (source instanceof AbstractXMLObject) {
					return (AbstractXMLObject) source;
				}
			}
		}

		return null;
	}

}
