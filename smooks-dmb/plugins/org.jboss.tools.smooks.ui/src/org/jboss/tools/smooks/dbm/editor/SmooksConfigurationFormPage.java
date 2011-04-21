/**
 * 
 */
package org.jboss.tools.smooks.dbm.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.Messages;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.graphical.editors.ISmooksEditorInitListener;
import org.jboss.tools.smooks.graphical.editors.SmooksMessage;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.core.GlobalParams;
import org.jboss.tools.smooks.model.core.ICorePackage;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.model.core.Param;
import org.jboss.tools.smooks.model.core.Params;
import org.milyn.StreamFilterType;

/**
 * @author Dart
 *
 */
public class SmooksConfigurationFormPage extends FormPage implements ISmooksEditorInitListener , ISourceSynchronizeListener {

	private String currentMessage = null;
	private ISmooksModelProvider smooksModelProvider;
//	private ModelPanelCreator defaultSettingPanelCreator;
	private Section globalParamSection;
	private Section settingSection;
	protected boolean lockEventFire = false;

	private Combo streamFilterTypeCombo;
	private Button defaultSerializationOnCheckbox;
	private Combo versionCombo;
	private int currentMessageType;
	
	public SmooksConfigurationFormPage(FormEditor editor, String id,
			String title, ISmooksModelProvider provider) {
		super(editor, id, title);
		this.smooksModelProvider = provider;
	}

	public SmooksConfigurationFormPage(String id, String title, ISmooksModelProvider provider) {
		super(id, title);
		this.smooksModelProvider = provider;
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		String title = getTitle();
		form.setText(title);
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		form.getBody().setLayout(gl);

		ScrolledPageBook pageBook = new ScrolledPageBook(form.getBody());
		pageBook.setBackground(toolkit.getColors().getBackground());
		Composite mainComposite = pageBook.createPage(pageBook);
		pageBook.showPage(pageBook);

		GridData gd = new GridData(GridData.FILL_BOTH);
		pageBook.setLayoutData(gd);

		GridLayout mgl = new GridLayout();
		mgl.numColumns = 2;
		mgl.horizontalSpacing = 20;
		mainComposite.setLayout(mgl);

		settingSection = toolkit.createSection(mainComposite, Section.TITLE_BAR);
		settingSection.setLayout(new FillLayout());
		settingSection.setText(Messages.SmooksConfigurationOverviewPage_ConfigurationSectionTitle);
		Composite settingComposite = toolkit.createComposite(settingSection);
		settingSection.setClient(settingComposite);
		gd = new GridData();
		gd.widthHint = 500;
		gd.verticalAlignment = GridData.BEGINNING;
		settingSection.setLayoutData(gd);

		GridLayout sgl = new GridLayout();
		settingComposite.setLayout(sgl);
		sgl.numColumns = 2;

		createSettingSection(settingComposite, toolkit);

		globalParamSection = toolkit.createSection(mainComposite, Section.TITLE_BAR | Section.TWISTIE
				| Section.EXPANDED);
		globalParamSection.setText(Messages.SmooksConfigurationOverviewPage_FilterSettingSectionTitle);
		globalParamSection.setLayout(new FillLayout());
		Composite globalParamComposite = toolkit.createComposite(globalParamSection);
		globalParamSection.setClient(globalParamComposite);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.widthHint = 500;
		globalParamSection.setLayoutData(gd);

		GridLayout gpgl = new GridLayout();
		globalParamComposite.setLayout(gpgl);
		gpgl.numColumns = 2;

		createGlobalParamterSection(globalParamComposite, toolkit);

		
		updateFormHeader();
	}
	
	protected void updateFormHeader() {
		if (currentMessageType == IMessageProvider.NONE) {
			if (this.getManagedForm() != null) {
				getManagedForm().getMessageManager().removeAllMessages();
				getManagedForm().getMessageManager().update();

				streamFilterTypeCombo.setEnabled(true);
				defaultSerializationOnCheckbox.setEnabled(true);
			}
		} else {
			if (this.getManagedForm() != null) {
				streamFilterTypeCombo.setEnabled(false);
				defaultSerializationOnCheckbox.setEnabled(false);
				String[] messages = currentMessage.split("\n"); //$NON-NLS-1$
				List<IMessage> messageList = new ArrayList<IMessage>();
				for (int i = 0; i < messages.length; i++) {
					String message = messages[i];
					if (message != null)
						message.trim();
					if (message.length() == 0) {
						continue;
					}
					messageList.add(new SmooksMessage(currentMessageType, message));
				}
				String mainMessage = null;
				if (messageList.isEmpty()) {
					mainMessage = currentMessage;
				} else {
					mainMessage = messageList.get(0).getMessage();
				}
				this.getManagedForm().getForm().getForm().setMessage(mainMessage, currentMessageType,
						messageList.toArray(new IMessage[] {}));
			}
		}
	}
	
	private void createSettingSection(Composite settingComposite, FormToolkit toolkit) {
		toolkit.createLabel(settingComposite, Messages.SmooksConfigurationOverviewPage_VersionLabel).setForeground(
				toolkit.getColors().getColor(IFormColors.TITLE));
		int type = SWT.BORDER;
//		if (SmooksUIUtils.isLinuxOS()) {
//			type = SWT.BORDER;
//		}
		versionCombo = new Combo(settingComposite, type | SWT.READ_ONLY);
		versionCombo.setEnabled(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		versionCombo.setLayoutData(gd);
		for (int i = 0; i < SmooksConstants.SMOOKS_VERSIONS.length; i++) {
			String version = SmooksConstants.SMOOKS_VERSIONS[i];
			versionCombo.add(version);
		}

		String version = getSmooksVersion();
		if (version != null)
			versionCombo.setText(version);
		versionCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				// if (smooksModelProvider != null) {
				// smooksModelProvider.getSmooksGraphicsExt().setPlatformVersion(v);
				// }
			}
		});

		toolkit.paintBordersFor(settingComposite);
	}
	
	private String getSmooksVersion() {
		return "1.1";
	}

	
	private void createGlobalParamterSection(Composite globalParamComposite, FormToolkit toolkit) {
		if (smooksModelProvider != null) {

			toolkit.createLabel(globalParamComposite, Messages.SmooksConfigurationOverviewPage_FilterTypeLabel)
					.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
			GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
			streamFilterTypeCombo = new Combo(globalParamComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
			streamFilterTypeCombo.setItems(new String[] { "SAX", "DOM" }); //$NON-NLS-1$ //$NON-NLS-2$
			streamFilterTypeCombo.setLayoutData(gd);

			toolkit.createLabel(globalParamComposite, Messages.SmooksConfigurationOverviewPage_SerializationLabel)
					.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
			gd = new GridData(SWT.FILL, SWT.NONE, true, false);
			defaultSerializationOnCheckbox = toolkit.createButton(globalParamComposite, null, SWT.CHECK); //$NON-NLS-1$
			defaultSerializationOnCheckbox.setLayoutData(gd);

			initGlobalSettingControls();

			toolkit.paintBordersFor(globalParamComposite);

			streamFilterTypeCombo.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					if (lockEventFire)
						return;
					String value = streamFilterTypeCombo.getText();
					updateGlobalProperty("stream.filter.type", value); //$NON-NLS-1$
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});

			defaultSerializationOnCheckbox.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					if (lockEventFire)
						return;
					String value = Boolean.toString(defaultSerializationOnCheckbox.getSelection());
					updateGlobalProperty("default.serialization.on", value); //$NON-NLS-1$
				}

				public void widgetSelected(SelectionEvent e) {
					widgetDefaultSelected(e);
				}
			});
		}

	}
	
	protected GlobalParams getParams(){
		if(smooksModelProvider != null){
			return smooksModelProvider.getSmooksModel().getModelRoot().getParams();
		}
		return null;
	}
	
	protected void initGlobalSettingControls() {
		// Set defaults first...
		streamFilterTypeCombo.setText(StreamFilterType.SAX.name());
		defaultSerializationOnCheckbox.setSelection(true);
		
		GlobalParams globalParams = getParams();
		if (globalParams != null) {
			StreamFilterType filterType = globalParams.getFilterType();						

			if(filterType != null) {
				streamFilterTypeCombo.setText(filterType.name());
			}
			defaultSerializationOnCheckbox.setSelection(globalParams.isDefaultSerializationOn());
		}
	}
	
	private void updateGlobalProperty(String propertyID, String value) {
		EditingDomain editingDomain = smooksModelProvider.getEditingDomain();
		if(editingDomain == null) throw new RuntimeException("Smooks EMF EditingDomain can't be null");
		boolean foundProperty = false;
		if (getSmooksVersion() == null) {
			return;
		}
		Params parent = getParams();
		Param param = null;
		Param newparam = null;
		if (parent != null) {
			List<?> parmList = parent.getParams();
			for (int i = 0; i < parmList.size(); i++) {
				param = (Param) parmList.get(i);
				if (param.getName().equals(propertyID)) {
					Command command = SetCommand.create(editingDomain, param, ICorePackage.Literals.PARAM__VALUE, value);
					editingDomain.getCommandStack().execute(command);
					foundProperty = true;
					break;
				}
			}
		}
		if(!foundProperty){
			newparam = new Param();
			newparam.setName(propertyID);
			newparam.setValue(value);
			Command ac =  AddCommand.create(editingDomain,parent, ICorePackage.Literals.PARAMS__PARAMS, newparam);
			editingDomain.getCommandStack().execute(ac);
		}
	}

	public void initFailed(int messageType, String message) {
		
	}

	public void sourceChange(Object model) {
		lockEventFire = true;
		if (streamFilterTypeCombo == null || defaultSerializationOnCheckbox == null || versionCombo == null) {
			lockEventFire = false;
			return;
		}
		streamFilterTypeCombo.setEnabled(true);
		defaultSerializationOnCheckbox.setEnabled(true);
		defaultSerializationOnCheckbox.setSelection(false);
		String version = getSmooksVersion();
		if (version == null)
			version = ""; //$NON-NLS-1$
		versionCombo.setText(version);

		if (model == null) {
			// the file was broken
			streamFilterTypeCombo.setEnabled(false);

			defaultSerializationOnCheckbox.setEnabled(false);
			defaultSerializationOnCheckbox.setSelection(false);
		} else {
			initGlobalSettingControls();
		}
		lockEventFire = false;
	}
}
