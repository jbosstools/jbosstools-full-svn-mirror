/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.javabean.model.SelectorAttributes;
import org.jboss.tools.smooks.ui.AbstractSmooksPropertySection;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;

/**
 * @author Dart
 * 
 */
public class BeanPopulatorConnectionSection extends
		AbstractSmooksPropertySection {

	private Button allPathButton;
	private Button nameOnlyButton;
	private Button includeParentButton;
	private Button ignoreRootButton;
	private CCombo speratorCombox;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();

		Section section = createRootSection(factory, parent);
		section.setText("BeanPopulator Connection"); //$NON-NLS-1$
		Composite controlComposite = factory.createComposite(section);
		section.setClient(controlComposite);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		controlComposite.setLayout(layout);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		factory.createLabel(controlComposite, "Selector split character : ");
		speratorCombox = factory.createCCombo(controlComposite);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		speratorCombox.setLayoutData(gd);

		String[] sperators = BeanPopulatorMappingAnalyzer.SELECTOR_SPERATORS;
		for (int i = 0; i < sperators.length; i++) {
			if (sperators[i].equals(" ")) {
				speratorCombox.add("<space>");
				continue;
			}
			speratorCombox.add(sperators[i]);
		}

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		factory.createLabel(controlComposite, "Selector generate policies:")
				.setLayoutData(gd);

		Composite policiesComposite = factory.createComposite(controlComposite);
		policiesComposite.setLayoutData(gd);

		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 4;
		gl1.marginHeight = 0;
		policiesComposite.setLayout(gl1);

		allPathButton = factory.createButton(policiesComposite, "Full path",
				SWT.RADIO);
		nameOnlyButton = factory.createButton(policiesComposite, "Name only",
				SWT.RADIO);
		includeParentButton = factory.createButton(policiesComposite,
				"Include parent name", SWT.RADIO);
		ignoreRootButton = factory.createButton(policiesComposite,
				"Ignore root node name", SWT.RADIO);

		hookButtons();
		hookSperatorCombox();
	}

	private void changeTheSelectorGenerationPolicy(String policy) {
		if(policy == null) return;
		SelectorAttributes attribute = getSelectorAttributes();
		if (attribute != null) {
			if(policy.equals(attribute.getSelectorPolicy())) return;
			attribute.setSelectorPolicy(policy);
		}
		LineConnectionModel line = getLineConnectionModel();
		line
				.addPropertyModel(
						BeanPopulatorMappingAnalyzer.PRO_SELECTOR_ATTRIBUTES,
						attribute);
		fireDirty();
	}

	protected void hookButtons() {
		allPathButton.addSelectionListener(new PolicyButtonSelectionListener(
				SelectorAttributes.FULL_PATH));
		nameOnlyButton.addSelectionListener(new PolicyButtonSelectionListener(
				SelectorAttributes.ONLY_NAME));
		includeParentButton
				.addSelectionListener(new PolicyButtonSelectionListener(
						SelectorAttributes.INCLUDE_PARENT));
		ignoreRootButton
				.addSelectionListener(new PolicyButtonSelectionListener(
						SelectorAttributes.IGNORE_ROOT));
	}

	protected void hookSperatorCombox() {
		speratorCombox.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isLock())
					return;
				String sperator = speratorCombox.getText();
				if (sperator == null) {
					sperator = " ";
				}
				sperator = sperator.trim();
				if (sperator.length() == 0)
					sperator = " ";

				if (sperator.equals("<space>")) {
					sperator = " ";
				}
				SelectorAttributes sa = getSelectorAttributes();
				if (sa != null) {
					if (sperator.equals(sa.getSelectorSperator()))
						return;
					sa.setSelectorSperator(sperator);
					getLineConnectionModel()
							.addPropertyModel(
									new PropertyModel(
											BeanPopulatorMappingAnalyzer.PRO_SELECTOR_ATTRIBUTES,
											sa));
				}
				fireDirty();
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	public void refresh() {
		super.refresh();
		lockEventFire();
		String sperator = getSperator();
		if (sperator == null) {
			sperator = "<space>";
		} else {
			if (sperator.equals(" ")) {
				sperator = "<space>";
			}
		}
		this.speratorCombox.setText(sperator);

		String policy = getSelectorPolicy();
		if (policy == null) {
			policy = SelectorAttributes.FULL_PATH;
		}
		allPathButton.setSelection(false);
		nameOnlyButton.setSelection(false);
		ignoreRootButton.setSelection(false);
		includeParentButton.setSelection(false);
		if (policy.equals(SelectorAttributes.FULL_PATH)) {
			this.allPathButton.setSelection(true);
		}
		if (policy.equals(SelectorAttributes.ONLY_NAME)) {
			this.nameOnlyButton.setSelection(true);
		}
		if (policy.equals(SelectorAttributes.IGNORE_ROOT)) {
			this.ignoreRootButton.setSelection(true);
		}
		if (policy.equals(SelectorAttributes.INCLUDE_PARENT)) {
			this.includeParentButton.setSelection(true);
		}

		unLockEventFire();
	}

	protected SelectorAttributes getSelectorAttributes() {
		LineConnectionModel connection = getLineConnectionModel();
		if (connection != null) {
			return (SelectorAttributes) connection
					.getProperty(BeanPopulatorMappingAnalyzer.PRO_SELECTOR_ATTRIBUTES);
		}
		return null;
	}

	protected String getSperator() {
		SelectorAttributes sa = getSelectorAttributes();
		if (sa != null) {
			return sa.getSelectorSperator();
		}
		return null;
	}

	protected String getSelectorPolicy() {
		SelectorAttributes sa = getSelectorAttributes();
		if (sa != null) {
			return sa.getSelectorPolicy();
		}
		return null;
	}

	private class PolicyButtonSelectionListener extends SelectionAdapter {
		private String policy;

		public PolicyButtonSelectionListener(String policy) {
			this.policy = policy;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if(isLock()) return;
			changeTheSelectorGenerationPolicy(policy);
		}

	}

}
