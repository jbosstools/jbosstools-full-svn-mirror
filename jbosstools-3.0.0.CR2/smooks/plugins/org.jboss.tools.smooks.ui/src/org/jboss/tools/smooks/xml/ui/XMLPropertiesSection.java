/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

	public static final String MAPPING_TYPE = "mappingType";
	public static final String SELECTOR_NAMESPACE = "selector-namespace";

	public static final String MAPPING = "mapping";

	public static final String BINDING = "binding";

	private Text namespaceText;
	private Button checkButton;
	private CCombo connectionTypeCombo;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();
		Section section = createRootSection(factory, parent);
		section.setText("XML Connection Properties"); //$NON-NLS-1$

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

		factory.createLabel(com, "Connection Type : ").setVisible(false);
		connectionTypeCombo = factory.createCCombo(com);// (com, "");
		connectionTypeCombo.add("mapping");
		connectionTypeCombo.add("binding");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		connectionTypeCombo.setLayoutData(gd);
		connectionTypeCombo.setEditable(false);
		connectionTypeCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isLock())
					return;
				setConnectionType();
			}

		});
		connectionTypeCombo.setVisible(false);
	}

	private void setConnectionType() {
		LineConnectionModel connection = getLineConnectionModel();
		if (connection != null) {
			PropertyModel type = null;
			List<PropertyModel> list = connection.getProperties();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				PropertyModel propertyModel = (PropertyModel) iterator.next();
				if (propertyModel.getName().equals(MAPPING_TYPE)) {
					type = propertyModel;
					break;
				}
			}
			if (type == null) {
				type = new PropertyModel();
				type.setName(MAPPING_TYPE);
			}
			type.setValue(connectionTypeCombo.getText().trim());
			fireDirty();
		}
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
						if (propertyModel.getName().equals(SELECTOR_NAMESPACE)) {
							if (propertyModel.getValue().equals(namespace)) {
								checkButton.setEnabled(true);
								lockEventFire();
								checkButton.setSelection(true);
								unLockEventFire();
							}
						}

						if (propertyModel.getName().equals(MAPPING_TYPE)) {
							Object value = propertyModel.getValue();
							if (value != null)
								connectionTypeCombo.setText(value.toString());
						}
					}
				}
			} else {
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
