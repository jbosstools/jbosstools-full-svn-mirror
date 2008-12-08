/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.Collection;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class NormalResourceConfigDetailPage extends
		AbstractSmooksModelDetailPage {

	private boolean init = true;
	private ParamaterTableViewerCreator paramTable;
	private Text resourceValueText;
	private Text resourceTypeText;
	private Text selectorText;

	public NormalResourceConfigDetailPage(SmooksFormEditor parentEditor,
			EditingDomain domain) {
		super(parentEditor, domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.editors.AbstractSmooksModelDetailPage#
	 * createSectionContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createSectionContents(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		formToolKit.createLabel(parent, "Selector : ");
		selectorText = formToolKit.createText(parent, "");
		selectorText.setLayoutData(gd);

		formToolKit.createLabel(parent, "Resource Type : ");
		resourceTypeText = formToolKit.createText(parent, "");
		resourceTypeText.setLayoutData(gd);

		formToolKit.createLabel(parent, "Resource Value : ");
		resourceValueText = formToolKit.createText(parent, "");
		resourceValueText.setLayoutData(gd);

		formToolKit.paintBordersFor(parent);

		paramTable = new ParamaterTableViewerCreator(parent, formToolKit,
				SWT.NONE);
		paramTable.addParamaterListener(new ParamaterChangeLitener() {

			public void paramaterAdded(ParamType param) {
				parentEditor.fireEditorDirty(true);
			}

			public void paramaterChanged(ParamType param) {
				parentEditor.fireEditorDirty(true);
			}

			public void paramaterRemoved(Collection<ParamType> params) {
				parentEditor.fireEditorDirty(true);
			}

		});

		hookText();
	}

	protected void hookText() {
		selectorText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				resourceConfig.setSelector(selectorText.getText());
				parentEditor.fireEditorDirty(true);
			}

		});

		resourceTypeText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				ResourceType resource = resourceConfig.getResource();
				if (resource == null) {
					resource = SmooksFactory.eINSTANCE.createResourceType();
					resourceConfig.setResource(resource);
				}
				if (resource != null) {
					resource.setType(resourceTypeText.getText());
					parentEditor.fireEditorDirty(true);
				}
			}

		});

		resourceValueText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				ResourceType resource = resourceConfig.getResource();
				if (resource == null) {
					resource = SmooksFactory.eINSTANCE.createResourceType();
					resourceConfig.setResource(resource);
				}
				if (resource != null) {
					SmooksModelUtils.setTextToAnyType(resource,
							resourceValueText.getText());
					parentEditor.fireEditorDirty(true);
				}
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.editors.AbstractSmooksModelDetailPage#initSectionUI
	 * ()
	 */
	@Override
	protected void initSectionUI() {
		paramTable.setInput(resourceConfig);
		paramTable.setResourceConfig(resourceConfig);
		ResourceType resource = resourceConfig.getResource();
		String type = "";
		String value = "";
		if (resource != null) {
			type = resource.getType();
			if (type != null) {
				type = type.trim();

			} else {
				type = "";
			}
			value = SmooksModelUtils.getAnyTypeText(resource);
			if (value != null) {
				value = value.trim();
			} else {
				value = "";
			}
		}
		resourceTypeText.setText(type);
		resourceValueText.setText(value);
		String selector = resourceConfig.getSelector();
		if (selector != null) {
			selector = selector.trim();
		}else{
			selector = "";
		}
		selectorText.setText(selector);

	}

}
