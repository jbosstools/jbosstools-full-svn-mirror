/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/

package org.jboss.tools.birt.oda.ui.impl;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.NameValuePair;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.QueryInputModel;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.QueryEditor;
import org.hibernate.eclipse.console.utils.QLFormatHelper;
import org.hibernate.eclipse.console.viewers.xpl.MTreeViewer;
import org.hibernate.eclipse.console.views.KnownConfigurationsProvider;
import org.hibernate.eclipse.console.workbench.LazySessionFactory;
import org.hibernate.eclipse.console.workbench.xpl.AnyAdaptableLabelProvider;
import org.hibernate.eclipse.hqleditor.HQLEditorDocumentSetupParticipant;
import org.hibernate.eclipse.hqleditor.HQLSourceViewerConfiguration;
import org.hibernate.engine.query.HQLQueryPlan;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.type.Type;
import org.jboss.tools.birt.oda.IOdaFactory;
import org.jboss.tools.birt.oda.impl.HibernateDriver;
import org.jboss.tools.birt.oda.ui.Activator;
import org.jboss.tools.birt.oda.ui.Messages;

/**
 * Hibernate data set designer page
 * 
 * @author snjeza
 */
public class CustomDataSetWizardPage extends DataSetWizardPage {

	private static String DEFAULT_MESSAGE = Messages.CustomDataSetWizardPage_Define_the_query_text_for_the_data_set;

	private StyledText styledText;

	private SourceViewer sourceViewer;

	private IHandlerService fService;

	private IHandlerActivation fActivation;

	private TreeViewer viewer;

	private Button testButton;

	/**
	 * Constructor
	 * 
	 * @param pageName
	 */
	public CustomDataSetWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setMessage(DEFAULT_MESSAGE);

		setPageComplete(false);
	}

	/**
	 * Constructor
	 * 
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public CustomDataSetWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		setMessage(DEFAULT_MESSAGE);
		setPageComplete(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageCustomControl(Composite parent) {
		setControl(createPageControl(parent));
		validateData();
		setMessage(DEFAULT_MESSAGE);
	}

	/**
	 * Creates custom control for user-defined query text.
	 */
	private Control createPageControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Control left = createTableSelectionComposite(composite);
		GridData gridData = (GridData) left.getLayoutData();
		gridData.widthHint = 160;
		// left.setLayoutData(gridData);

		Sash sash = createSash(composite);
		Control right = createQueryComposite(composite);
		// setWidthHints(composite, left, right, sash);
		addDragListerner(sash, composite, left, right);
		initDragAndDrop();
		return composite;
	}

	private void initDragAndDrop() {
		new HibernateDSDragSource(viewer);
		new HibernateDSDropSource(sourceViewer);
	}

	private void addDragListerner(final Sash sash, final Composite parent,
			final Control left, final Control right) {
		sash.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event.detail == SWT.DRAG) {
					return;
				}
				Sash sash = (Sash) event.widget;
				int shift = event.x - sash.getBounds().x;

				left.setSize(left.getSize().x + shift, left.getSize().y);
				right.setSize(right.getSize().x - shift, right.getSize().y);
				right.setLocation(right.getLocation().x + shift, right
						.getLocation().y);
				sash.setLocation(sash.getLocation().x + shift, sash
						.getLocation().y);
			}
		});
	}

	private Control createQueryComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.FILL
				| SWT.LEFT_TO_RIGHT);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn lineNumbers = new LineNumberRulerColumn();
		ruler.addDecorator(0, lineNumbers);

		sourceViewer = new SourceViewer(composite, ruler, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER);
		sourceViewer.setInput(parent);
		styledText = sourceViewer.getTextWidget();
		styledText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validateData();
			}

		});
		styledText.setFont(JFaceResources.getTextFont());
		Control control = sourceViewer.getControl();
		GridData gd = new GridData(GridData.FILL_BOTH);
		control.setLayoutData(gd);

		IDocument document = new Document();
		document.set(getInitialQueryString());
		HQLEditorDocumentSetupParticipant docSetupParticipant = new HQLEditorDocumentSetupParticipant();
		docSetupParticipant.setup(document);
		sourceViewer.setEditable(true);
		sourceViewer.setDocument(document);
		IUndoManager undoManager = new TextViewerUndoManager(10);
		sourceViewer.setUndoManager(undoManager);
		undoManager.connect(sourceViewer);

		attachMenus(sourceViewer);
		// add support of additional accelerated key
		sourceViewer.getTextWidget().addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (isUndoKeyPress(e)) {
					sourceViewer.doOperation(ITextOperationTarget.UNDO);
				} else if (isRedoKeyPress(e)) {
					sourceViewer.doOperation(ITextOperationTarget.REDO);
				}
				validateData();
			}

			private boolean isUndoKeyPress(KeyEvent e) {
				// CTRL + z
				return ((e.stateMask & SWT.CONTROL) > 0)
						&& ((e.keyCode == 'z') || (e.keyCode == 'Z'));
			}

			private boolean isRedoKeyPress(KeyEvent e) {
				// CTRL + y
				return ((e.stateMask & SWT.CONTROL) > 0)
						&& ((e.keyCode == 'y') || (e.keyCode == 'Y'));
			}

			public void keyReleased(KeyEvent e) {
				// do nothing
			}
		});

		IHandler handler = new AbstractHandler() {
			public Object execute(ExecutionEvent event)
					throws org.eclipse.core.commands.ExecutionException {
				sourceViewer.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
				return null;
			}
		};
		IWorkbench workbench = PlatformUI.getWorkbench();
		fService = (IHandlerService) workbench
				.getAdapter(IHandlerService.class);
		fActivation = fService.activateHandler(
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS,
				handler);

		QueryEditor editor = new QueryEditor() {

			public boolean askUserForConfiguration(String name) {
				return false;
			}

			public void executeQuery(ConsoleConfiguration cfg) {
			}

			public ConsoleConfiguration getConsoleConfiguration() {
				return getInternalConsoleConfiguration();
			}

			public String getConsoleConfigurationName() {
				return getConfigurationName();
			}

			public QueryInputModel getQueryInputModel() {
				return null;
			}

			public String getQueryString() {
				return null;
			}

			public void setConsoleConfigurationName(String text) {
			}

		};
		SourceViewerConfiguration svc = new HQLSourceViewerConfiguration(editor);
		sourceViewer.configure(svc);
		testButton = new Button(composite, SWT.NONE);
		testButton.setText(Messages.CustomDataSetWizardPage_Test_query);
		testButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				testQuery();
			}

		});
		return composite;
	}

	private void testQuery() {
		String title = Messages.CustomDataSetWizardPage_Test_query1;
		ConsoleConfiguration configuration = getInternalConsoleConfiguration();
		if (configuration == null || configuration.getSessionFactory() == null) {
			MessageDialog.openConfirm(getShell(), title,
					NLS.bind(Messages.CustomDataSetWizardPage_Invalid_configuration, getConfigurationName()));
		}
		try {
			//session = configuration.getSessionFactory().openSession();
			//Query q = session.createQuery(getQueryText());
			String sql = generateSQL(configuration.getSessionFactory(),getQueryText());
			//System.out.println(sql);
			//q.setFirstResult(0);
			//q.setMaxResults(1);
			//q.list();
			MessageDialog.openInformation(getShell(), title,
					Messages.CustomDataSetWizardPage_The_query_is_valid);
		} catch (Exception e) {
			String message = Messages.CustomDataSetWizardPage_The_query_is_not_valid;
			IStatus status = new Status(Status.ERROR, Activator.PLUGIN_ID, e
					.getLocalizedMessage(), e);
			Activator.getDefault().getLog().log(status);
			ErrorDialog.openError(getShell(), title, message, status);
		} 
	}

	/* 
	 * copied from the DynamicSQLPreviewView.generateSQL method 
	 */
	private String generateSQL(SessionFactory sessionFactory, String queryText) {
		SessionFactoryImpl sfimpl = (SessionFactoryImpl) sessionFactory; // hack - to get to the actual queries..
		StringBuffer str = new StringBuffer(256);
		HQLQueryPlan plan = new HQLQueryPlan(queryText, false, Collections.EMPTY_MAP, sfimpl);

		QueryTranslator[] translators = plan.getTranslators();
		for (int i = 0; i < translators.length; i++) {
			QueryTranslator translator = translators[i];
			if(translator.isManipulationStatement()) {
				str.append("Manipulation of" + i + ":"); //$NON-NLS-1$ //$NON-NLS-2$
				Iterator iterator = translator.getQuerySpaces().iterator();
				while ( iterator.hasNext() ) {
					Object qspace = iterator.next();
					str.append(qspace);
					if(iterator.hasNext()) { str.append(", "); } //$NON-NLS-1$
				}

			} else {
				Type[] returnTypes = translator.getReturnTypes();
				str.append(i +": "); //$NON-NLS-1$
				for (int j = 0; j < returnTypes.length; j++) {
					Type returnType = returnTypes[j];
					str.append(returnType.getName());
					if(j<returnTypes.length-1) { str.append(", "); }							 //$NON-NLS-1$
				}
			}
			str.append("\n-----------------\n"); //$NON-NLS-1$
			Iterator sqls = translator.collectSqlStrings().iterator();
			while ( sqls.hasNext() ) {
				String sql = (String) sqls.next();
				str.append(QLFormatHelper.formatForScreen(sql));
				str.append("\n\n");	 //$NON-NLS-1$
			}
		};
		return str.toString();
	}

	private final void attachMenus(SourceViewer viewer) {
		StyledText widget = viewer.getTextWidget();
		TextMenuManager menuManager = new TextMenuManager(viewer);
		widget.setMenu(menuManager.getContextMenu(widget));
	}

	private String getConfigurationName() {
		DataSetDesign design = getInitializationDesign();
		DataSourceDesign dsDesign = design.getDataSourceDesign();
		Property property = dsDesign.getPublicProperties().findProperty(
				IOdaFactory.CONFIGURATION);
		NameValuePair propertyValue = property.getNameValue();
		String name = propertyValue.getValue();
		return name;
	}

	private ConsoleConfiguration getInternalConsoleConfiguration() {
		return getConsoleConfiguration(getConfigurationName());
	}

	private Sash createSash(final Composite composite) {
		final Sash sash = new Sash(composite, SWT.VERTICAL);
		sash.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		return sash;
	}

	private Control createTableSelectionComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();

		composite.setLayout(layout);

		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.grabExcessVerticalSpace = true;
		composite.setLayoutData(data);

		Label dataSourceLabel = new Label(composite, SWT.LEFT);
		dataSourceLabel.setText(Messages.CustomDataSetWizardPage_Avaliable_Items);
		data = new GridData();
		dataSourceLabel.setLayoutData(data);

		viewer = new MTreeViewer(composite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;

		viewer.getControl().setLayoutData(data);

		viewer.setLabelProvider(new AnyAdaptableLabelProvider());
		viewer.setContentProvider(new KnownConfigurationsProvider());
		ConsoleConfiguration configuration = getInternalConsoleConfiguration();
		if (configuration != null) {
			SessionFactory sessionFactory = configuration.getSessionFactory();
			if (sessionFactory == null) {
				configuration.build();
				configuration.buildSessionFactory();
				configuration.getSessionFactory();
			}
			viewer.setInput(new LazySessionFactory(configuration));
		} else
			viewer.setInput(KnownConfigurations.getInstance());

		return composite;
	}

	private ConsoleConfiguration getConsoleConfiguration(String name) {
		ConsoleConfiguration[] configurations = KnownConfigurations
				.getInstance().getConfigurations();
		for (int i = 0; i < configurations.length; i++) {
			if (configurations[i].getName().equals(name)) {
				return configurations[i];
			}
		}
		return null;
	}

	private void setWidthHints(Composite pageContainer, Control left,
			Control right, Sash sash) {
		int leftWidth = left.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		int totalWidth = pageContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;

		if ((double) leftWidth / (double) totalWidth > 0.4) {
			// if left side is too wide, set it to default value 40:60
			leftWidth = totalWidth * 40 / 100;
			leftWidth = leftWidth
					- sash.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			GridData data = (GridData) left.getLayoutData();
			data.widthHint = leftWidth;
			data = (GridData) right.getLayoutData();
			data.widthHint = totalWidth - leftWidth;
		} else {
			GridData data = (GridData) left.getLayoutData();
			data.widthHint = leftWidth;
			data = (GridData) right.getLayoutData();
			data.widthHint = totalWidth - leftWidth;
		}
	}

	/**
	 * Initializes the page control with the last edited data set design.
	 */
	private String getInitialQueryString() {
		DataSetDesign dataSetDesign = getInitializationDesign();
		if (dataSetDesign == null)
			return ""; // nothing to initialize //$NON-NLS-1$

		String queryText = dataSetDesign.getQueryText();
		if (queryText == null) {
			return ""; //$NON-NLS-1$
		}
		return queryText;
	}

	/**
	 * Obtains the user-defined query text of this data set from page control.
	 * 
	 * @return query text
	 */
	private String getQueryText() {
		return styledText.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.
	 * DataSetDesign)
	 */
	protected DataSetDesign collectDataSetDesign(DataSetDesign design) {
		if (!hasValidData())
			return design;
		savePage(design);
		return design;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #collectResponseState()
	 */
	protected void collectResponseState() {
		super.collectResponseState();
		/*
		 * To optionally assign a custom response state, for inclusion in the
		 * ODA design session response, use setResponseSessionStatus(
		 * SessionStatus status ); setResponseDesignerState( DesignerState
		 * customState );
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #canLeave()
	 */
	protected boolean canLeave() {
		return isPageComplete();
	}

	/**
	 * Validates the user-defined value in the page control exists and not a
	 * blank text. Set page message accordingly.
	 */
	private void validateData() {
		boolean isValid = (styledText != null && getQueryText() != null && getQueryText()
				.trim().length() > 0);

		if (isValid)
			setMessage(DEFAULT_MESSAGE);
		else
			setMessage(Messages.CustomDataSetWizardPage_Requires_input_value, ERROR);

		if (testButton != null) {
			testButton.setEnabled(isValid);
		}
		setPageComplete(isValid);
	}

	/**
	 * Indicates whether the custom page has valid data to proceed with defining
	 * a data set.
	 */
	private boolean hasValidData() {
		validateData();

		return canLeave();
	}

	/**
	 * Saves the user-defined value in this page, and updates the specified
	 * dataSetDesign with the latest design definition.
	 */
	private void savePage(DataSetDesign dataSetDesign) {
		// save user-defined query text
		String queryText = getQueryText();
		dataSetDesign.setQueryText(queryText);

		// obtain query's current runtime metadata, and maps it to the
		// dataSetDesign
		IConnection customConn = null;
		try {
			// instantiate your custom ODA runtime driver class
			/*
			 * Note: You may need to manually update your ODA runtime
			 * extension's plug-in manifest to export its package for visibility
			 * here.
			 */
			IDriver customDriver = new HibernateDriver();

			// obtain and open a live connection
			customConn = customDriver.getConnection(null);
			java.util.Properties connProps = DesignUtil
					.convertDataSourceProperties(getInitializationDesign()
							.getDataSourceDesign());
			customConn.open(connProps);

			// update the data set design with the
			// query's current runtime metadata
			updateDesign(dataSetDesign, customConn, queryText);
		} catch (OdaException e) {
			// not able to get current metadata, reset previous derived metadata
			dataSetDesign.setResultSets(null);
			dataSetDesign.setParameters(null);

			e.printStackTrace();
		} finally {
			closeConnection(customConn);
		}
	}

	/**
	 * Updates the given dataSetDesign with the queryText and its derived
	 * metadata obtained from the ODA runtime connection.
	 */
	private void updateDesign(DataSetDesign dataSetDesign, IConnection conn,
			String queryText) throws OdaException {
		IQuery query = conn.newQuery(null);
		query.prepare(queryText);

		try {
			IResultSetMetaData md = query.getMetaData();
			updateResultSetDesign(md, dataSetDesign);
		} catch (OdaException e) {
			// no result set definition available, reset previous derived
			// metadata
			dataSetDesign.setResultSets(null);
			e.printStackTrace();
		}

		// proceed to get parameter design definition
		try {
			IParameterMetaData paramMd = query.getParameterMetaData();
			updateParameterDesign(paramMd, dataSetDesign);
		} catch (OdaException ex) {
			// no parameter definition available, reset previous derived
			// metadata
			dataSetDesign.setParameters(null);
			ex.printStackTrace();
		}

		/*
		 * See DesignSessionUtil for more convenience methods to define a data
		 * set design instance.
		 */
	}

	/**
	 * Updates the specified data set design's result set definition based on
	 * the specified runtime metadata.
	 * 
	 * @param md
	 *            runtime result set metadata instance
	 * @param dataSetDesign
	 *            data set design instance to update
	 * @throws OdaException
	 */
	private void updateResultSetDesign(IResultSetMetaData md,
			DataSetDesign dataSetDesign) throws OdaException {
		ResultSetColumns columns = DesignSessionUtil
				.toResultSetColumnsDesign(md);

		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
				.createResultSetDefinition();
		// resultSetDefn.setName( value ); // result set name
		resultSetDefn.setResultSetColumns(columns);

		// no exception in conversion; go ahead and assign to specified
		// dataSetDesign
		dataSetDesign.setPrimaryResultSet(resultSetDefn);
		dataSetDesign.getResultSets().setDerivedMetaData(true);
	}

	/**
	 * Updates the specified data set design's parameter definition based on the
	 * specified runtime metadata.
	 * 
	 * @param paramMd
	 *            runtime parameter metadata instance
	 * @param dataSetDesign
	 *            data set design instance to update
	 * @throws OdaException
	 */
	private void updateParameterDesign(IParameterMetaData paramMd,
			DataSetDesign dataSetDesign) throws OdaException {
		DataSetParameters paramDesign = DesignSessionUtil
				.toDataSetParametersDesign(
						paramMd,
						DesignSessionUtil
								.toParameterModeDesign(IParameterMetaData.parameterModeIn));

		// no exception in conversion; go ahead and assign to specified
		// dataSetDesign
		if (paramDesign == null)
			return;
		paramDesign.setDerivedMetaData(true);
		dataSetDesign.setParameters(paramDesign);

		// TODO replace below with data source specific implementation;
		// hard-coded parameter's default value for demo purpose
		if (paramDesign.getParameterDefinitions().size() > 0) {
			ParameterDefinition paramDef = (ParameterDefinition) paramDesign
					.getParameterDefinitions().get(0);
			if (paramDef != null)
				paramDef.setDefaultScalarValue("dummy default value"); //$NON-NLS-1$
		}
	}

	/**
	 * Attempts to close given ODA connection.
	 */
	private void closeConnection(IConnection conn) {
		try {
			if (conn != null && conn.isOpen())
				conn.close();
		} catch (OdaException e) {
			// ignore
			e.printStackTrace();
		}
	}

	public void dispose() {
		if (fActivation != null) {
			fService.deactivateHandler(fActivation);
		}
		if (sourceViewer != null) {
			sourceViewer.getTextWidget().dispose();
			sourceViewer = null;
		}

		super.dispose();
	}

}
