/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.common.model.ui.reporting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.about.ISystemSummarySection;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.jboss.tools.common.CommonPlugin;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.XEntityData;
import org.jboss.tools.common.meta.action.impl.XEntityDataImpl;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.attribute.XAttributeSupport;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.widgets.ReferenceListener;
import org.jboss.tools.common.model.ui.widgets.TextAndReferenceComponent;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizard;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizardView;
import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.osgi.framework.Bundle;

public class ReportProblemWizard extends AbstractQueryWizard {

	public ReportProblemWizard() {
		setView(new ReportProblemWizardView());
	}

}

class ReportProblemWizardView extends AbstractQueryWizardView {
	
	static final char SEPARATOR = System.getProperty ("file.separator").charAt (0);

	/** LOG_DATE_FORMAT */
	final private SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat(
			"dd_MMM_yyyy__HH_mm_ss_SSS");

	/** log file name */
	private Text logFileName;

	/** Browse button */
	private Button browseButton;

	class PixelConverter {

		private FontMetrics fFontMetrics;

		public PixelConverter(Control control) {
			GC gc = new GC(control);
			gc.setFont(control.getFont());
			fFontMetrics = gc.getFontMetrics();
			gc.dispose();
		}

		/**
		 * @see DialogPage#convertHorizontalDLUsToPixels
		 */
		public int convertHorizontalDLUsToPixels(int dlus) {
			return Dialog.convertHorizontalDLUsToPixels(fFontMetrics, dlus);
		}

		/**
		 * @see DialogPage#convertWidthInCharsToPixels
		 */
		public int convertWidthInCharsToPixels(int chars) {
			return Dialog.convertWidthInCharsToPixels(fFontMetrics, chars);
		}
	}

	int stackTracesCount;
	Text problemDescription;
	XAttributeSupport sendSupport;
	XAttributeSupport infoSupport;
	XModelObject reportOptions = PreferenceModelUtilities.getPreferenceModel()
			.getByPath(ReportPreference.OPTIONS_REPORT_PROBLEM_PATH);
	XModelObject exceptionsObject = PreferenceModelUtilities
			.getPreferenceModel()
			.createModelObject("ProblemBufferEditor", null);

	XEntityData sendData = XEntityDataImpl.create(new String[][] {
			{ "SharableReportProblem" },
			{ ReportPreference.ATT_ATTACH_REDHAT_LOG, "no" },
	// {ReportPreference.ATT_ATTACH_ECLIPSE_LOG, "no"}
			});
	XEntityData sendData2 = XEntityDataImpl
			.create(new String[][] { { "SharableReportProblem" },
			// {ReportPreference.ATT_ATTACH_ECLIPSE_LOG, "no"}
			});
	XEntityData infoData = XEntityDataImpl.create(new String[][] {
			{ "SharableReportProblem" }, { ReportPreference.ATT_E_MAIL, "no" },
			{ ReportPreference.ATT_OTHER, "no" } });

	public ReportProblemWizardView() {
		//folderLogFileName = Platform.getLocation().toOSString() + SEPARATOR;
	}

	public Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		Layout layout = new GridLayout();
		composite.setLayout(layout);
		createFilenameLogControl(composite);
		createStackTracesControl(composite);
		createProblemControl(composite);
		createContactInfoControl(composite);
		
		initializeData();
		 
		return composite;
	}

	/**
	 * 
	 */
	private void initializeData() {
		File defaultLocation = new File(Platform.getLocation().toFile(), getDefaultZipFilename());
		logFileName.setText(defaultLocation.toString());
	}

	/**
	 * 
	 */
	private byte[] getStackTracesFile() {
		StringBuffer sb = new StringBuffer();

		try {
			InputStreamReader in = new FileReader(Platform.getLogFileLocation()
					.toOSString());

			char[] tempBuffer = new char[512];
			int len = 0;
			while ((len = in.read(tempBuffer)) != -1) {
				sb.append(tempBuffer);
			}

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		return sb.toString().getBytes();
	};

	/**
	 * 
	 */
	private byte[] getUsercommentFile() {
		StringBuffer sb = new StringBuffer();

		sb = sb.append("email : ").append(
				ReportPreference.E_MAIL_OPTION.getValue()).append("\n");
		sb = sb.append("description : ").append(problemDescription.getText())
				.append("\n");
		sb.append("other : ").append(ReportPreference.OTHER_OPTION.getValue())
				.append("\n");

		return sb.toString().getBytes();
	}

	private IConfigurationElement[] getSortedExtensions() {
		IConfigurationElement[] configElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						PlatformUI.PLUGIN_ID,
						IWorkbenchRegistryConstants.PL_SYSTEM_SUMMARY_SECTIONS);

		Arrays.sort(configElements, new Comparator() {
			Collator collator = Collator.getInstance(Locale.getDefault());

			public int compare(Object a, Object b) {
				IConfigurationElement element1 = (IConfigurationElement) a;
				IConfigurationElement element2 = (IConfigurationElement) b;

				String id1 = element1.getAttribute("id"); //$NON-NLS-1$
				String id2 = element2.getAttribute("id"); //$NON-NLS-1$

				if (id1 != null && id2 != null && !id1.equals(id2)) {
					return collator.compare(id1, id2);
				}

				String title1 = element1.getAttribute("sectionTitle"); //$NON-NLS-1$ 
				String title2 = element2.getAttribute("sectionTitle"); //$NON-NLS-1$

				if (title1 == null) {
					title1 = ""; //$NON-NLS-1$
				}
				if (title2 == null) {
					title2 = ""; //$NON-NLS-1$
				}

				return collator.compare(title1, title2);
			}
		});

		return configElements;
	}

	/*
	 * Appends the contents of all extentions to the configurationLogSections
	 * extension point.
	 */
	private void appendExtensions(PrintWriter writer) {
		IConfigurationElement[] configElements = getSortedExtensions();
		for (int i = 0; i < configElements.length; ++i) {
			IConfigurationElement element = configElements[i];

			Object obj = null;
			try {
				obj = WorkbenchPlugin.createExtension(element,
						IWorkbenchConstants.TAG_CLASS);
			} catch (CoreException e) {
				WorkbenchPlugin.log(
						"could not create class attribute for extension", //$NON-NLS-1$
						e.getStatus());
			}

			writer.println();
			writer.println(NLS.bind(
					WorkbenchMessages.SystemSummary_sectionTitle, element
							.getAttribute("sectionTitle"))); //$NON-NLS-1$

			if (obj instanceof ISystemSummarySection) {
				ISystemSummarySection logSection = (ISystemSummarySection) obj;
				logSection.write(writer);
			} else {
				writer.println(WorkbenchMessages.SystemSummary_sectionError);
			}
		}
	}

	/**
	 * 
	 */
	private byte[] getEclipsePropertiesFile() {
		StringWriter out = new StringWriter();
		PrintWriter writer = new PrintWriter(out);
		writer.println(NLS.bind(WorkbenchMessages.SystemSummary_timeStamp,
				DateFormat
						.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL)
						.format(new Date())));

		appendExtensions(writer);
		writer.close();
		return out.toString().getBytes();
	}

	/**
	 * create a ZIP file contain following files: 
	 */
	private void createZipFile() throws IOException {
		byte tempBuffer[];
		String[] fileNames = new String[] { "stacktrace.txt",
				"usercomment.txt", "eclipse.properties" };

		//
		// ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		//
		
		File filename = new File(logFileName.getText());
		
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(filename));
		for (int i = 0; i < fileNames.length; i++) {

			switch (i) {
			case 0:
				tempBuffer = getStackTracesFile();
				break;
			case 1:
				tempBuffer = getUsercommentFile();
				break;
			case 2:
				tempBuffer = getEclipsePropertiesFile();
				break;
			default:
				tempBuffer = "Unknown data".getBytes();

			}

			ZipEntry e = new ZipEntry(fileNames[i].replace(File.separatorChar, SEPARATOR));
			zout.putNextEntry(e);
			zout.write(tempBuffer, 0, tempBuffer.length);
			zout.closeEntry();
		}
		zout.close();
		ModelUIPlugin.getDefault().logInfo("Wrote diagnostic info to " + filename);
		//		
		// return byteBuffer.toString();
		//		
	}


	/**
	 * Get a filename for zip-file
	 * 
	 * @return filename for zip-file
	 */
	private String getDefaultZipFilename() {
		String currentDate;
		Date today = new Date();

		currentDate = LOG_DATE_FORMAT.format(today);
		return "jbosstools-diagnostics-" + currentDate.toString() + ".zip";
	}

	private void createStackTracesControl(Composite parent) {
		stackTracesCount = ProblemReportingHelper.buffer.getSize();
		exceptionsObject.setAttributeValue("exceptions",
				formatContent(ProblemReportingHelper.buffer.getContent()));
		XEntityData data = (stackTracesCount > 0) ? sendData : sendData2;
		boolean isSeparatorNeeded = false;
		if (stackTracesCount > 0) {
			createStackTracesLabelControl(parent);
			isSeparatorNeeded = true;
		}
		if (data.getAttributeData().length > 0) {
			sendSupport = new XAttributeSupport(reportOptions, data, true);
			sendSupport.setAutoStore(false);
			GridLayout layout = getDefaultSupportLayout();
			layout.marginWidth = 10;
			sendSupport.setLayout(layout);
			sendSupport.createControl(parent);
			sendSupport.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					updateBar();
				}
			});
			isSeparatorNeeded = true;
		} else {
			sendSupport = null;
		}
		if (isSeparatorNeeded) {
			Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
			separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
	}

	/**
	 * Creates a new label widget
	 * 
	 * @param parent
	 *            the parent composite to add this label widget to
	 * @param text
	 *            the text for the label
	 * @param hspan
	 *            the horizontal span to take up in the parent composite
	 * @return the new label
	 */
	public static Label createLabel(Composite parent, String text, int hspan) {
		Label l = new Label(parent, SWT.NONE);
		l.setFont(parent.getFont());
		l.setText(text);
		GridData gd = new GridData();
		gd.horizontalSpan = hspan;
		l.setLayoutData(gd);
		return l;
	}

	/**
	 * Creates a new text widget
	 * 
	 * @param parent
	 *            the parent composite to add this text widget to
	 * @param hspan
	 *            the horizontal span to take up on the parent composite
	 * @return the new text widget
	 */
	public static Text createSingleText(Composite parent, int hspan) {
		Text t = new Text(parent, SWT.SINGLE | SWT.BORDER);
		t.setFont(parent.getFont());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hspan;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		return t;
	}

	/**
	 * Returns a width hint for a button control.
	 */
	public int getButtonWidthHint(Button button) {
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter
				.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				true).x);
	}

	/**
	 * Sets width and height hint for the button control. <b>Note:</b> This is
	 * a NOP if the button's layout data is not an instance of
	 * <code>GridData</code>.
	 * 
	 * @param the
	 *            button for which to set the dimension hint
	 */
	public void setButtonDimensionHint(Button button) {
		Object gd = button.getLayoutData();
		if (gd instanceof GridData) {
			((GridData) gd).widthHint = getButtonWidthHint(button);
			((GridData) gd).horizontalAlignment = GridData.FILL;
		}
	}

	/**
	 * Creates and returns a new push button with the given label and/or image.
	 * 
	 * @param parent
	 *            parent control
	 * @param label
	 *            button label or <code>null</code>
	 * @param image
	 *            image or <code>null</code>
	 * 
	 * @return a new push button
	 */
	public Button createPushButton(Composite parent, String label, Image image) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		if (image != null) {
			button.setImage(image);
		}
		if (label != null) {
			button.setText(label);
		}
		GridData gd = new GridData();
		button.setLayoutData(gd);
		setButtonDimensionHint(button);
		return button;
	}

	/**
	 * 
	 * @param parent
	 *            a parent Composite object
	 */
	private void createFilenameLogControl(final Composite parent) {
		Group g = new Group(parent, SWT.SHADOW_ETCHED_IN);
		g.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		GridLayout layout = new GridLayout(3, false);
		g.setLayout(layout);

		createLabel(g, "Log file name", 1);
		logFileName = createSingleText(g, 1);
		browseButton = createPushButton(g, "&Browse...", null);

		browseButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(parent.getShell(),
						SWT.SAVE);
				fileDialog.setText("Save");


				File fileName = new File(logFileName.getText());
				
				fileDialog.setFilterPath(fileName.getParent());
				fileDialog.setFileName(fileName.getName());
				
				String[] filterExt = { "*.zip" };
				fileDialog.setFilterExtensions(filterExt);
				String selected = fileDialog.open();

				if (selected != null) {
					logFileName.setText(selected);					
				}
			};

		});
	}

	private void createProblemControl(Composite parent) {
		Group g = new Group(parent, SWT.SHADOW_ETCHED_IN);
		g.setText("Problem Description");
		g.setLayoutData(new GridData(GridData.FILL_BOTH));
		g.setLayout(new GridLayout());
		problemDescription = new Text(g, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		problemDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		problemDescription.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateBar();
			}
		});
	}

	private void createContactInfoControl(Composite parent) {
		Group g = new Group(parent, SWT.SHADOW_ETCHED_IN);
		g.setText("Contact Information (optional)");
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		g.setLayout(new GridLayout());
		infoSupport = new XAttributeSupport(reportOptions, infoData, true);
		infoSupport.setAutoStore(false);
		infoSupport.setLayout(getDefaultSupportLayout());
		Control c = infoSupport.createControl(g);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void createStackTracesLabelControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = getDefaultSupportLayout();
		layout.marginWidth = 10;
		panel.setLayout(layout);
		Label image = new Label(panel, SWT.NONE);
		image.setImage(ModelUIImages.getImage("warning.gif"));
		GridData d = new GridData();
		d.widthHint = 16;
		image.setLayoutData(d);
		image.setLayoutData(new GridData());
		TextAndReferenceComponent t2 = new TextAndReferenceComponent(panel,
				SWT.NONE);
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalIndent = 5;
		t2.setLayoutData(d);
		String text = "You have " + stackTracesCount
				+ " stack traces to send (<a>see details</a>).";
		t2.setText(text, 300);
		t2.addReferenceListener(new ReferenceListener() {
			public void referenceSelected(String reference) {
				XActionInvoker.invoke("EditActions.Edit", exceptionsObject,
						null);
			}
		});
	}

	public Point getPreferredSize() {
		return new Point(400, 500);		
	}

	protected GridLayout getDefaultSupportLayout() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 5;
		return gridLayout;
	}

	public String[] getCommands() {
		return new String[] { "Ok", CANCEL };
	}

	public void action(String command) {
		if (command.equals("Ok")) {
			submitProblems();
			super.action(OK);
		} else {
			super.action(command);
		}
	}

	private void submitProblems() {
		if (sendSupport != null)
			sendSupport.store();
		if (infoSupport != null)
			infoSupport.store();
		String text = CommonPlugin.getEnvironment() + "\n"
				+ problemDescription.getText();
		boolean addRedHatLog = "yes"
				.equals(ReportPreference.ATTACH_REDHAT_LOG_OPTION.getValue());
		if (addRedHatLog && stackTracesCount > 0) {
			String log = exceptionsObject.getAttributeValue("exceptions");
			text += "\n----------Red Hat Log-----------\n" + log + "\n";
		}
		boolean addEclipseLog = "yes"
				.equals(ReportPreference.ATTACH_ECLIPSE_LOG_OPTION.getValue());
		if (addEclipseLog) {
			String eclipseLog = ProblemReportingHelper.buffer
					.getEclipseLogContent();
			text += "\n----------Eclipse Log----------\n" + eclipseLog + "\n";
		}
		/*
		String email = ReportPreference.E_MAIL_OPTION.getValue();
		String other = ReportPreference.OTHER_OPTION.getValue();
		*/
		try {
			createZipFile();
			// Submit.getInstance().submit(reportText, addRedHatLog);
			// ProblemReportingHelper.buffer.report(text, email, other,
			// addRedHatLog);
		} catch (IOException e) {
			ModelUIPlugin.getPluginLog().logError(e);
		}

		if (addRedHatLog) {
			// clean is to be done after report,
			// which is executed as background job.
			// ProblemReportingHelper.buffer.clean();
		}
	}

	static int LINE_LIMIT = 70;

	private String formatContent(String content) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(content, "\n", true);
		while (st.hasMoreTokens()) {
			String t = st.nextToken();
			if ("\n".equals(t)) {
				sb.append(t);
			} else {
				while (t.length() > LINE_LIMIT) {
					String t1 = t.substring(0, LINE_LIMIT);
					int i = t1.lastIndexOf(' ');
					if (i > 40)
						t1 = t1.substring(0, i);
					t = t.substring(t1.length());
					sb.append(t1).append("\n");
				}
				sb.append(t);
			}
		}
		return sb.toString();
	}

	public void updateBar() {
		getCommandBar().setEnabled("Ok", !isMessageEmpty());
	}

	boolean isMessageEmpty() {
		if (sendSupport != null) {
			IModelPropertyEditorAdapter a = sendSupport
					.getPropertyEditorAdapterByName(ReportPreference.ATT_ATTACH_REDHAT_LOG);
			if (a != null && "yes".equals(a.getValue()))
				return false;
			a = sendSupport
					.getPropertyEditorAdapterByName(ReportPreference.ATT_ATTACH_ECLIPSE_LOG);
			if (a != null && "yes".equals(a.getValue()))
				return false;
		}
		String text = problemDescription.getText();
		if (text == null || text.trim().length() == 0)
			return true;
		return false;
	}

}
