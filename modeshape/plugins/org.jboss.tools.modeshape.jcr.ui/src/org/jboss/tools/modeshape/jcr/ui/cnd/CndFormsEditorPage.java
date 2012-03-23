/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.ui.cnd;

import static org.jboss.tools.modeshape.jcr.ui.JcrUiConstants.EditorIds.CND_FORMS_PAGE;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.MultiValidationStatus;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.ValidationStatus;
import org.jboss.tools.modeshape.jcr.cnd.ChildNodeDefinition;
import org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType;
import org.jboss.tools.modeshape.jcr.cnd.CndValidator;
import org.jboss.tools.modeshape.jcr.cnd.NamespaceMapping;
import org.jboss.tools.modeshape.jcr.cnd.NodeTypeDefinition;
import org.jboss.tools.modeshape.jcr.cnd.NodeTypeDefinition.PropertyName;
import org.jboss.tools.modeshape.jcr.cnd.PropertyDefinition;
import org.jboss.tools.modeshape.jcr.cnd.QualifiedName;
import org.jboss.tools.modeshape.jcr.ui.JcrUiUtils;
import org.jboss.tools.modeshape.ui.UiMessages;
import org.jboss.tools.modeshape.ui.UiUtils;
import org.jboss.tools.modeshape.ui.actions.DelegateAction;
import org.jboss.tools.modeshape.ui.forms.ErrorMessage;
import org.jboss.tools.modeshape.ui.forms.FormUtils;
import org.jboss.tools.modeshape.ui.forms.FormUtils.Styles;
import org.jboss.tools.modeshape.ui.forms.MessageFormDialog;

/**
 * The GUI part of the CND editor.
 */
class CndFormsEditorPage extends CndEditorPage {

    private IAction addChildNode;
    private IAction addNamespace;
    private IAction addNodeType;
    private IAction addProperty;
    private IAction addSuperType;
    private Button btnAbstract;
    private Button btnMixin;
    private Button btnOrderable;
    private Button btnQueryable;
    private Section childNodeSection;
    private final ErrorMessage childNodesError;
    private TableViewer childNodeViewer;
    private IAction deleteChildNode;
    private IAction deleteNamespace;
    private IAction deleteNodeType;
    private IAction deleteProperty;
    private IAction deleteSuperType;
    private Section detailsSection;
    private IAction editChildNode;
    private IAction editNamespace;
    private IAction editProperty;
    private IAction editSuperType;
    private QualifiedNameEditor nameEditor;
    private final ErrorMessage namespacesError;
    private TableViewer namespaceViewer;
    private final ErrorMessage nodeTypeNameError;
    private String nodeTypeNameFilterPattern;
    private final ErrorMessage nodeTypesError;
    private TableViewer nodeTypeViewer;
    private final ErrorMessage propertiesError;
    private Section propertiesSection;
    private TableViewer propertyViewer;
    private final ErrorMessage superTypesError;
    private TableViewer superTypesViewer;

    /**
     * @param cndEditor the CND editor this page belongs to (cannot be <code>null</code>)
     */
    protected CndFormsEditorPage( final CndEditor cndEditor ) {
        super(cndEditor, CND_FORMS_PAGE, CndMessages.cndEditorFormsPageTitle);

        // construct form messages
        this.childNodesError = new ErrorMessage();
        this.namespacesError = new ErrorMessage();
        this.nodeTypesError = new ErrorMessage();
        this.nodeTypeNameError = new ErrorMessage();
        this.propertiesError = new ErrorMessage();
        this.superTypesError = new ErrorMessage();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.ui.cnd.CndEditorPage#createBody(org.eclipse.swt.widgets.Composite,
     *      org.eclipse.ui.forms.widgets.FormToolkit)
     */
    @Override
    protected void createBody( final Composite body,
                               final FormToolkit toolkit ) {
        // top is namespaces
        createNamespaceSection(getManagedForm(), toolkit, body);

        // bottom is node types
        createNodeTypeSection(getManagedForm(), toolkit, body);

        // fill GUI with CND
        populateUi();
    }

    private void createChildNodeActions() {
        this.addChildNode = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleAddChildNode();
            }
        };
        this.addChildNode.setEnabled(false);
        this.addChildNode.setToolTipText(CndMessages.addChildNodeToolTip);
        this.addChildNode.setImageDescriptor(JcrUiUtils.getNewImageDescriptor());

        this.deleteChildNode = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleDeleteChildNode();
            }
        };
        this.deleteChildNode.setEnabled(false);
        this.deleteChildNode.setToolTipText(CndMessages.deleteChildNodeToolTip);
        this.deleteChildNode.setImageDescriptor(JcrUiUtils.getDeleteImageDescriptor());

        this.editChildNode = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleEditChildNode();
            }
        };
        this.editChildNode.setEnabled(false);
        this.editChildNode.setToolTipText(CndMessages.editChildNodeToolTip);
        this.editChildNode.setImageDescriptor(JcrUiUtils.getEditImageDescriptor());
    }

    private void createChildNodeSection( final IManagedForm managedForm,
                                         final FormToolkit toolkit,
                                         final Composite parent ) {
        this.childNodeSection = FormUtils.createSection(managedForm, toolkit, parent, CndMessages.cndEditorChildNodeSectionTitle,
                                                        CndMessages.cndEditorChildNodeSectionDescription, Styles.SECTION_STYLE
                                                                & ~ExpandableComposite.EXPANDED, true);
        toolkit.paintBordersFor(this.childNodeSection);

        // create actions
        createChildNodeActions();

        // create toolbar
        FormUtils.createSectionToolBar(this.childNodeSection, toolkit, new IAction[] { this.addChildNode, this.editChildNode,
                this.deleteChildNode });

        // create viewer
        final Composite container = toolkit.createComposite(this.childNodeSection);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.childNodeSection.setClient(container);
        toolkit.paintBordersFor(container);

        final Table table = FormUtils.createTable(toolkit, container);
        ((GridData)table.getLayoutData()).heightHint = table.getItemHeight() * 5;

        // table context menu
        final MenuManager menuManager = new MenuManager();
        menuManager.add(new DelegateAction(CndMessages.addChildNodeMenuText, this.addChildNode));
        menuManager.add(new DelegateAction(CndMessages.editChildNodeMenuText, this.editChildNode));
        menuManager.add(new DelegateAction(CndMessages.deleteChildNodeMenuText, this.deleteChildNode));
        table.setMenu(menuManager.createContextMenu(table));

        createChildNodeViewer(table);
        this.childNodesError.setControl(table);
    }

    private void createChildNodeViewer( final Table childNodeTable ) {
        // create custom label provider for child node definitions
        class ChildNodeLabelProvider extends ColumnLabelProvider {

            private final int columnIndex;

            public ChildNodeLabelProvider( final int columnIndex ) {
                this.columnIndex = columnIndex;
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
             */
            @Override
            public String getText( final Object element ) {
                final ChildNodeDefinition childNodeDefinition = (ChildNodeDefinition)element;

                if (this.columnIndex == ChildNodeColumnIndexes.NAME) {
                    return childNodeDefinition.getName();
                }

                final NotationType notationType = NotationType.LONG;

                if (this.columnIndex == ChildNodeColumnIndexes.DEFAULT_TYPE) {
                    return childNodeDefinition.getDefaultType().toCndNotation(notationType);
                }

                if (this.columnIndex == ChildNodeColumnIndexes.REQUIRED_TYPES) {
                    return childNodeDefinition.getRequiredTypesCndNotation(notationType);
                }

                assert (this.columnIndex == ChildNodeColumnIndexes.ATTRIBUTES) : "Unexpected child node column index"; //$NON-NLS-1$
                return childNodeDefinition.getAttributesCndNotation(notationType);
            }
        }

        this.childNodeViewer = new TableViewer(childNodeTable);
        this.childNodeViewer.setContentProvider(new IStructuredContentProvider() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            @Override
            public void dispose() {
                // nothing to do
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements( final Object inputElement ) {
                final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

                if (nodeTypeDefinition == null) {
                    return Utils.EMPTY_OBJECT_ARRAY;
                }

                return nodeTypeDefinition.getChildNodeDefinitions().toArray();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public void inputChanged( final Viewer viewer,
                                      final Object oldInput,
                                      final Object newInput ) {
                // nothing to do
            }
        });

        // open edit child node on double click
        this.childNodeViewer.addDoubleClickListener(new IDoubleClickListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
             */
            @Override
            public void doubleClick( final DoubleClickEvent event ) {
                handleEditChildNode();
            }
        });

        // add selection listener
        this.childNodeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged( final SelectionChangedEvent event ) {
                handleChildNodeSelected();
            }
        });

        { // create name column
            final TableViewerColumn nameColumn = new TableViewerColumn(this.childNodeViewer, SWT.LEFT);
            UiUtils.configureColumn(nameColumn, new ChildNodeLabelProvider(ChildNodeColumnIndexes.NAME),
                                    CndMessages.nameHeaderText, CndMessages.childNodeNameToolTip, false, true);
        }

        { // create type column
            final TableViewerColumn typeColumn = new TableViewerColumn(this.childNodeViewer, SWT.LEFT);
            UiUtils.configureColumn(typeColumn, new ChildNodeLabelProvider(ChildNodeColumnIndexes.REQUIRED_TYPES),
                                    CndMessages.requiredTypesHeaderText, CndMessages.childNodeRequiredTypesToolTip, false, true);
        }

        { // create default values column
            final TableViewerColumn defaultValuesColumn = new TableViewerColumn(this.childNodeViewer, SWT.LEFT);
            UiUtils.configureColumn(defaultValuesColumn, new ChildNodeLabelProvider(ChildNodeColumnIndexes.DEFAULT_TYPE),
                                    CndMessages.defaultTypeHeaderText, CndMessages.childNodeDefaultTypeToolTip, false, true);
        }

        { // create attributes column
            final TableViewerColumn attributesColumn = new TableViewerColumn(this.childNodeViewer, SWT.LEFT);
            UiUtils.configureColumn(attributesColumn, new ChildNodeLabelProvider(ChildNodeColumnIndexes.ATTRIBUTES),
                                    CndMessages.attributesHeaderText, CndMessages.childNodeAttributesToolTip, false, true);
        }

        // this will sort by child node name
        this.childNodeViewer.setSorter(new ViewerSorter() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ViewerComparator#sort(org.eclipse.jface.viewers.Viewer, java.lang.Object[])
             */
            @Override
            public void sort( final Viewer viewer,
                              final Object[] elements ) {
                Arrays.sort(elements);
            }
        });
    }

    private void createDetailsSection( final IManagedForm managedForm,
                                       final FormToolkit toolkit,
                                       final Composite parent ) {
        // create section
        this.detailsSection = FormUtils.createSection(managedForm, toolkit, parent, CndMessages.cndEditorDetailsSectionTitle,
                                                      CndMessages.cndEditorDetailsSectionDescription, Styles.SECTION_STYLE
                                                              & ~ExpandableComposite.TWISTIE & ~ExpandableComposite.EXPANDED, false);
        toolkit.paintBordersFor(this.detailsSection);

        // create contents
        final Composite detailsContainer = toolkit.createComposite(this.detailsSection);
        detailsContainer.setLayout(new GridLayout(2, true));
        detailsContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.detailsSection.setClient(detailsContainer);
        toolkit.paintBordersFor(detailsContainer);

        { // left-side of details section
            final Composite leftContainer = toolkit.createComposite(detailsContainer);
            leftContainer.setLayout(new GridLayout(2, false));
            leftContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            toolkit.paintBordersFor(leftContainer);

            { // name
                this.nameEditor = new QualifiedNameEditor(leftContainer,
                                                          SWT.NONE,
                                                          toolkit,
                                                          Messages.propertyDefinitionName,
                                                          getNamespacePrefixes(),
                                                          null);
                ((GridData)this.nameEditor.getLayoutData()).horizontalSpan = 2;
                this.nameEditor.addListener(SWT.Modify, new Listener() {

                    /**
                     * {@inheritDoc}
                     * 
                     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
                     */
                    @Override
                    public void handleEvent( final Event e ) {
                        handleNodeTypeNameChanged(e.text);
                    }
                });

                this.nodeTypeNameError.setControl(this.nameEditor);
                refreshNameControls(); // populate name editor controls
            }

            { // attributes
                final Group attributesContainer = new Group(leftContainer, SWT.SHADOW_NONE);
                attributesContainer.setText(CndMessages.attributesHeaderText);
                attributesContainer.setLayout(new GridLayout(2, true));
                attributesContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
                ((GridData)attributesContainer.getLayoutData()).horizontalSpan = 2;
                toolkit.adapt(attributesContainer);
                // attributesContainer.setBackground(toolkit.getColors().getColor(IFormColors.H_HOVER_LIGHT));
                toolkit.paintBordersFor(attributesContainer);

                this.btnAbstract = toolkit.createButton(attributesContainer, CndMessages.abstractAttribute, SWT.CHECK);
                this.btnAbstract.setBackground(attributesContainer.getBackground());
                this.btnAbstract.addSelectionListener(new SelectionAdapter() {

                    /**
                     * {@inheritDoc}
                     * 
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected( final SelectionEvent e ) {
                        handleAbstractChanged(((Button)e.widget).getSelection());
                    }
                });
                this.btnAbstract.setToolTipText(CndMessages.abstractAttributeToolTip);

                this.btnMixin = toolkit.createButton(attributesContainer, CndMessages.mixinAttribute, SWT.CHECK);
                this.btnMixin.setBackground(attributesContainer.getBackground());
                this.btnMixin.addSelectionListener(new SelectionAdapter() {

                    /**
                     * {@inheritDoc}
                     * 
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected( final SelectionEvent e ) {
                        handleMixinChanged(((Button)e.widget).getSelection());
                    }
                });
                this.btnMixin.setToolTipText(CndMessages.mixinAttributeToolTip);

                this.btnOrderable = toolkit.createButton(attributesContainer, CndMessages.orderableAttribute, SWT.CHECK);
                this.btnOrderable.setBackground(attributesContainer.getBackground());
                this.btnOrderable.addSelectionListener(new SelectionAdapter() {

                    /**
                     * {@inheritDoc}
                     * 
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected( final SelectionEvent e ) {
                        handleOrderableChanged(((Button)e.widget).getSelection());
                    }
                });
                this.btnOrderable.setToolTipText(CndMessages.orderableAttributeToolTip);

                this.btnQueryable = toolkit.createButton(attributesContainer, CndMessages.queryableAttribute, SWT.CHECK);
                this.btnQueryable.setBackground(attributesContainer.getBackground());
                this.btnQueryable.addSelectionListener(new SelectionAdapter() {

                    /**
                     * {@inheritDoc}
                     * 
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected( final SelectionEvent e ) {
                        handleQueryableChanged(((Button)e.widget).getSelection());
                    }
                });
                this.btnQueryable.setToolTipText(CndMessages.queryableAttributeToolTip);

                // fill with data from CND
                refreshAttributeControls();
            }
        }

        { // right-side of details section (supertypes)
            final Composite rightContainer = toolkit.createComposite(detailsContainer);
            rightContainer.setLayout(new GridLayout(2, false));
            rightContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            toolkit.paintBordersFor(rightContainer);

            final Label label = toolkit.createLabel(rightContainer, CndMessages.supertypesLabel);
            label.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
            ((GridData)label.getLayoutData()).horizontalSpan = 2;

            final Table table = FormUtils.createTable(toolkit, rightContainer);
            table.setHeaderVisible(false);
            table.setLinesVisible(false);
            ((GridData)table.getLayoutData()).heightHint = table.getItemHeight() * 2;
            table.setToolTipText(CndMessages.supertypesToolTip);
            this.superTypesError.setControl(table);

            createSuperTypesActions();

            // table context menu
            final MenuManager menuManager = new MenuManager();
            menuManager.add(new DelegateAction(CndMessages.addSuperTypeMenuText, this.addSuperType));
            menuManager.add(new DelegateAction(CndMessages.editSuperTypeMenuText, this.editSuperType));
            menuManager.add(new DelegateAction(CndMessages.deleteSuperTypeMenuText, this.deleteSuperType));
            table.setMenu(menuManager.createContextMenu(table));

            createSuperTypesViewer(table);

            // add toolbar buttons (add, edit, delete)
            final ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.VERTICAL);
            final ToolBar toolBar = toolBarManager.createControl(rightContainer);

            toolkit.adapt(toolBar);
            final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
            toolBar.setCursor(handCursor);
            toolBarManager.add(this.addSuperType);
            toolBarManager.add(this.editSuperType);
            toolBarManager.add(this.deleteSuperType);
            toolBarManager.update(true);

            // fill with data from CND
            refreshSuperTypes();
        }
    }

    private void createNamespaceActions() {
        this.addNamespace = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleAddNamespace();
            }
        };
        this.addNamespace.setToolTipText(CndMessages.addNamespaceToolTip);
        this.addNamespace.setImageDescriptor(JcrUiUtils.getNewImageDescriptor());

        this.deleteNamespace = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleDeleteNamespace();
            }
        };
        this.deleteNamespace.setEnabled(false);
        this.deleteNamespace.setToolTipText(CndMessages.deleteNamespaceToolTip);
        this.deleteNamespace.setImageDescriptor(JcrUiUtils.getDeleteImageDescriptor());

        this.editNamespace = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleEditNamespace();
            }
        };
        this.editNamespace.setEnabled(false);
        this.editNamespace.setToolTipText(CndMessages.editNamespaceToolTip);
        this.editNamespace.setImageDescriptor(JcrUiUtils.getEditImageDescriptor());
    }

    private void createNamespaceSection( final IManagedForm managedForm,
                                         final FormToolkit toolkit,
                                         final Composite parent ) {
        // create section
        final Section section = FormUtils.createSection(managedForm, toolkit, parent, CndMessages.cndEditorNamespacesSectionTitle,
                                                        CndMessages.cndEditorNamespacesSectionDescription, Styles.SECTION_STYLE
                                                                & ~ExpandableComposite.EXPANDED, true);
        toolkit.paintBordersFor(section);

        // create actions
        createNamespaceActions();

        // create toolbar
        FormUtils.createSectionToolBar(section, toolkit, new IAction[] { this.addNamespace, this.editNamespace,
                this.deleteNamespace });

        // create viewer
        final Composite container = toolkit.createComposite(section);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        section.setClient(container);
        toolkit.paintBordersFor(container);

        final Table table = FormUtils.createTable(toolkit, container);
        ((GridData)table.getLayoutData()).heightHint = table.getItemHeight() * 5;

        // table context menu
        final MenuManager menuManager = new MenuManager();
        menuManager.add(new DelegateAction(CndMessages.addNamespaceMenuText, this.addNamespace));
        menuManager.add(new DelegateAction(CndMessages.editNamespaceMenuText, this.editNamespace));
        menuManager.add(new DelegateAction(CndMessages.deleteNamespaceMenuText, this.deleteNamespace));
        table.setMenu(menuManager.createContextMenu(table));

        createNamespaceViewer(table);
        this.namespacesError.setControl(table);
    }

    private void createNamespaceViewer( final Table namespaceTable ) {
        // create custom label provider for namespace mappings
        class NamespaceLabelProvider extends ColumnLabelProvider {

            private final int columnIndex;

            public NamespaceLabelProvider( final int columnIndex ) {
                this.columnIndex = columnIndex;
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
             */
            @Override
            public String getText( final Object element ) {
                final NamespaceMapping namespaceMapping = (NamespaceMapping)element;

                if (this.columnIndex == NamespaceColumnIndexes.PREFIX) {
                    return namespaceMapping.getPrefix();
                }

                return namespaceMapping.getUri();
            }
        }

        this.namespaceViewer = new TableViewer(namespaceTable);
        this.namespaceViewer.setContentProvider(new IStructuredContentProvider() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            @Override
            public void dispose() {
                // nothing to do
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements( final Object inputElement ) {
                return getCnd().getNamespaceMappings().toArray();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public void inputChanged( final Viewer viewer,
                                      final Object oldInput,
                                      final Object newInput ) {
                // nothing to do
            }
        });

        // open edit namespace on double click
        this.namespaceViewer.addDoubleClickListener(new IDoubleClickListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
             */
            @Override
            public void doubleClick( final DoubleClickEvent event ) {
                handleEditNamespace();
            }
        });

        // add selection listener
        this.namespaceViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged( final SelectionChangedEvent event ) {
                handleNamespaceSelected();
            }
        });

        { // create prefix column
            final TableViewerColumn prefixColumn = new TableViewerColumn(this.namespaceViewer, SWT.LEFT);
            UiUtils.configureColumn(prefixColumn, new NamespaceLabelProvider(NamespaceColumnIndexes.PREFIX),
                                    CndMessages.namespacePrefixHeaderText, CndMessages.namespacePrefixToolTip, false, true);
        }

        { // create URI column
            final TableViewerColumn uriColumn = new TableViewerColumn(this.namespaceViewer, SWT.LEFT);
            UiUtils.configureColumn(uriColumn, new NamespaceLabelProvider(NamespaceColumnIndexes.URI),
                                    CndMessages.namespaceUriHeaderText, CndMessages.namespaceUriToolTip, false, true);
        }

        // this will sort by prefix
        this.namespaceViewer.setSorter(new ViewerSorter() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ViewerComparator#sort(org.eclipse.jface.viewers.Viewer, java.lang.Object[])
             */
            @Override
            public void sort( final Viewer viewer,
                              final Object[] elements ) {
                Arrays.sort(elements);
            }
        });
    }

    private void createNodeTypeActions() {
        this.addNodeType = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleAddNodeType();
            }
        };
        this.addNodeType.setToolTipText(CndMessages.addNodeTypeToolTip);
        this.addNodeType.setImageDescriptor(JcrUiUtils.getNewImageDescriptor());

        this.deleteNodeType = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleDeleteNodeType();
            }
        };
        this.deleteNodeType.setEnabled(false);
        this.deleteNodeType.setToolTipText(CndMessages.deleteNodeTypeToolTip);
        this.deleteNodeType.setImageDescriptor(JcrUiUtils.getDeleteImageDescriptor());
    }

    @SuppressWarnings("unused")
    private void createNodeTypeSection( final IManagedForm managedForm,
                                        final FormToolkit toolkit,
                                        final Composite parent ) {
        // create section
        final Section section = FormUtils.createSection(managedForm, toolkit, parent, CndMessages.cndEditorNodeTypeSectionTitle,
                                                        CndMessages.cndEditorNodeTypeSectionDescription, Styles.SECTION_STYLE
                                                                & ~ExpandableComposite.TWISTIE, false);
        toolkit.paintBordersFor(section);

        // create actions
        createNodeTypeActions();

        // create toolbar
        FormUtils.createSectionToolBar(section, toolkit, new IAction[] { this.addNodeType, this.deleteNodeType });

        // splitter has node type table on left and node type detail, properties, and child nodes on right
        final SashForm splitter = new SashForm(section, SWT.HORIZONTAL);
        toolkit.adapt(splitter);
        splitter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        splitter.setBackground(toolkit.getColors().getColor(IFormColors.SEPARATOR));
        section.setClient(splitter);

        // left side is node type name filter and node type name table
        final Composite leftContainer = toolkit.createComposite(splitter);
        leftContainer.setLayout(new GridLayout());
        leftContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        toolkit.paintBordersFor(leftContainer);

        FILTER: {
            final Text txtFilter = toolkit.createText(leftContainer, Utils.EMPTY_STRING, Styles.TEXT_STYLE | SWT.SEARCH
                    | SWT.ICON_CANCEL);
            txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            txtFilter.setMessage(CndMessages.nodeTypeNamePatternMessage);
            txtFilter.setFont(JFaceResources.getDialogFont());
            txtFilter.addModifyListener(new ModifyListener() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
                 */
                @Override
                public void modifyText( final ModifyEvent e ) {
                    handleNodeTypeNameFilterModified(txtFilter.getText());
                }
            });
        }

        VIEWER: {
            final Composite viewerContainer = toolkit.createComposite(leftContainer);
            viewerContainer.setLayout(new GridLayout());
            viewerContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            toolkit.paintBordersFor(viewerContainer);

            final Table table = FormUtils.createTable(toolkit, viewerContainer);
            table.setLinesVisible(false);

            // table context menu
            final MenuManager menuManager = new MenuManager();
            menuManager.add(new DelegateAction(CndMessages.addNodeTypeMenuText, this.addNodeType));
            menuManager.add(new DelegateAction(CndMessages.deleteNodeTypeMenuText, this.deleteNodeType));
            table.setMenu(menuManager.createContextMenu(table));

            createNodeTypeViewer(table);
            this.nodeTypesError.setControl(table);
        }

        RIGHT_SIDE: {
            final Composite container = toolkit.createComposite(splitter);
            container.setLayout(new GridLayout());
            container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            toolkit.paintBordersFor(container);

            DETAILS: {
                createDetailsSection(managedForm, toolkit, container);
            }

            PROPERTIES: {
                createPropertySection(managedForm, toolkit, container);
            }

            CHILD_NODES: {
                createChildNodeSection(managedForm, toolkit, container);
            }
        }

        splitter.setWeights(new int[] { 20, 80 });
    }

    private void createNodeTypeViewer( final Table nodeTypeTable ) {
        // create custom label provider for node type definitions
        class NodeTypeLabelProvider extends ColumnLabelProvider {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
             */
            @Override
            public String getText( final Object element ) {
                final NodeTypeDefinition nodeTypeDefinition = (NodeTypeDefinition)element;
                return nodeTypeDefinition.getName();
            }
        }

        this.nodeTypeViewer = new TableViewer(nodeTypeTable);
        this.nodeTypeViewer.setContentProvider(new IStructuredContentProvider() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            @Override
            public void dispose() {
                // nothing to do
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements( final Object inputElement ) {
                return getCnd().getNodeTypeDefinitions().toArray();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public void inputChanged( final Viewer viewer,
                                      final Object oldInput,
                                      final Object newInput ) {
                // nothing to do
            }
        });

        // add selection listener
        this.nodeTypeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged( final SelectionChangedEvent event ) {
                handleNodeTypeSelected();
            }
        });

        { // create name column
            final TableViewerColumn nameColumn = new TableViewerColumn(this.nodeTypeViewer, SWT.LEFT);
            UiUtils.configureColumn(nameColumn, new NodeTypeLabelProvider(), CndMessages.nodeTypeNameHeaderText,
                                    CndMessages.nodeTypeNameToolTip, false, true);
        }

        // sort by name
        this.nodeTypeViewer.setSorter(new ViewerSorter() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ViewerComparator#sort(org.eclipse.jface.viewers.Viewer, java.lang.Object[])
             */
            @Override
            public void sort( final Viewer viewer,
                              final Object[] elements ) {
                Arrays.sort(elements);
            }
        });

        // add name filter
        this.nodeTypeViewer.addFilter(new ViewerFilter() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public boolean select( final Viewer viewer,
                                   final Object parentElement,
                                   final Object element ) {
                final String pattern = getNodeTypeNameFilterPattern();

                // no pattern don't filter any out
                if (Utils.isEmpty(pattern)) {
                    return true;
                }

                final NodeTypeDefinition nodeTypeDefinition = (NodeTypeDefinition)element;
                final String name = nodeTypeDefinition.getName();

                // filter out node type without names if there is a pattern
                if (Utils.isEmpty(name)) {
                    return false;
                }

                return name.contains(pattern);
            }
        });
    }

    private void createPropertyActions() {
        this.addProperty = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleAddProperty();
            }
        };
        this.addProperty.setEnabled(false);
        this.addProperty.setToolTipText(CndMessages.addPropertyToolTip);
        this.addProperty.setImageDescriptor(JcrUiUtils.getNewImageDescriptor());

        this.deleteProperty = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleDeleteProperty();
            }
        };
        this.deleteProperty.setEnabled(false);
        this.deleteProperty.setToolTipText(CndMessages.deletePropertyToolTip);
        this.deleteProperty.setImageDescriptor(JcrUiUtils.getDeleteImageDescriptor());

        this.editProperty = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleEditProperty();
            }
        };
        this.editProperty.setEnabled(false);
        this.editProperty.setToolTipText(CndMessages.editPropertyToolTip);
        this.editProperty.setImageDescriptor(JcrUiUtils.getEditImageDescriptor());
    }

    private void createPropertySection( final IManagedForm managedForm,
                                        final FormToolkit toolkit,
                                        final Composite parent ) {
        // create section
        this.propertiesSection = FormUtils.createSection(managedForm, toolkit, parent, CndMessages.cndEditorPropertySectionTitle,
                                                         CndMessages.cndEditorPropertySectionDescription, Styles.SECTION_STYLE
                                                                 & ~ExpandableComposite.EXPANDED, true);
        toolkit.paintBordersFor(this.propertiesSection);

        // create actions
        createPropertyActions();

        // create toolbar
        FormUtils.createSectionToolBar(this.propertiesSection, toolkit, new IAction[] { this.addProperty, this.editProperty,
                this.deleteProperty });

        // create viewer
        final Composite container = toolkit.createComposite(this.propertiesSection);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.propertiesSection.setClient(container);
        toolkit.paintBordersFor(container);

        final Table table = FormUtils.createTable(toolkit, container);
        ((GridData)table.getLayoutData()).heightHint = table.getItemHeight() * 5;

        // table context menu
        final MenuManager menuManager = new MenuManager();
        menuManager.add(new DelegateAction(CndMessages.addPropertyMenuText, this.addProperty));
        menuManager.add(new DelegateAction(CndMessages.editPropertyMenuText, this.editProperty));
        menuManager.add(new DelegateAction(CndMessages.deletePropertyMenuText, this.deleteProperty));
        table.setMenu(menuManager.createContextMenu(table));

        createPropertyViewer(table);
        this.propertiesError.setControl(table);
    }

    private void createPropertyViewer( final Table propertyTable ) {
        // create custom label provider for property definitions
        class PropertyLabelProvider extends ColumnLabelProvider {

            private final int columnIndex;

            public PropertyLabelProvider( final int columnIndex ) {
                this.columnIndex = columnIndex;
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
             */
            @Override
            public String getText( final Object element ) {
                final PropertyDefinition propertyDefinition = (PropertyDefinition)element;

                if (this.columnIndex == PropertyColumnIndexes.NAME) {
                    return propertyDefinition.getName();
                }

                final NotationType notationType = NotationType.LONG;

                if (this.columnIndex == PropertyColumnIndexes.TYPE) {
                    return propertyDefinition.getType().toString();
                }

                if (this.columnIndex == PropertyColumnIndexes.ATTRIBUTES) {
                    return propertyDefinition.getAttributesCndNotation(notationType);
                }

                if (this.columnIndex == PropertyColumnIndexes.DEFAULT_VALUES) {
                    final List<String> defaultValues = propertyDefinition.getDefaultValuesAsStrings();

                    if (Utils.isEmpty(defaultValues)) {
                        return Utils.EMPTY_STRING;
                    }

                    return UiUtils.join(defaultValues, null);
                }

                assert (this.columnIndex == PropertyColumnIndexes.CONSTRAINTS) : "Unexpected property column index"; //$NON-NLS-1$
                return propertyDefinition.getValueConstraintsCndNotation(notationType);
            }
        }

        this.propertyViewer = new TableViewer(propertyTable);
        this.propertyViewer.setContentProvider(new IStructuredContentProvider() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            @Override
            public void dispose() {
                // nothing to do
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements( final Object inputElement ) {
                final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

                if (nodeTypeDefinition == null) {
                    return Utils.EMPTY_OBJECT_ARRAY;
                }

                return nodeTypeDefinition.getPropertyDefinitions().toArray();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public void inputChanged( final Viewer viewer,
                                      final Object oldInput,
                                      final Object newInput ) {
                // nothing to do
            }
        });

        // open edit property on double click
        this.propertyViewer.addDoubleClickListener(new IDoubleClickListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
             */
            @Override
            public void doubleClick( final DoubleClickEvent event ) {
                handleEditProperty();
            }
        });

        // add selection listener
        this.propertyViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged( final SelectionChangedEvent event ) {
                handlePropertySelected();
            }
        });

        { // create name column
            final TableViewerColumn nameColumn = new TableViewerColumn(this.propertyViewer, SWT.LEFT);
            UiUtils.configureColumn(nameColumn, new PropertyLabelProvider(PropertyColumnIndexes.NAME), CndMessages.nameHeaderText,
                                    CndMessages.propertyNameToolTip, false, true);
        }

        { // create type column
            final TableViewerColumn typeColumn = new TableViewerColumn(this.propertyViewer, SWT.LEFT);
            UiUtils.configureColumn(typeColumn, new PropertyLabelProvider(PropertyColumnIndexes.TYPE), CndMessages.typeHeaderText,
                                    CndMessages.propertyTypeToolTip, false, true);
        }

        { // create default values column
            final TableViewerColumn defaultValuesColumn = new TableViewerColumn(this.propertyViewer, SWT.LEFT);
            UiUtils.configureColumn(defaultValuesColumn, new PropertyLabelProvider(PropertyColumnIndexes.DEFAULT_VALUES),
                                    CndMessages.defaultValuesHeaderText, CndMessages.propertyDefaultValuesToolTip, false, true);
        }

        { // create attributes column
            final TableViewerColumn attributesColumn = new TableViewerColumn(this.propertyViewer, SWT.LEFT);
            UiUtils.configureColumn(attributesColumn, new PropertyLabelProvider(PropertyColumnIndexes.ATTRIBUTES),
                                    CndMessages.attributesHeaderText, CndMessages.propertyAttributesToolTip, false, true);
        }

        { // create value constraints column
            final TableViewerColumn constraintsColumn = new TableViewerColumn(this.propertyViewer, SWT.LEFT);
            UiUtils.configureColumn(constraintsColumn, new PropertyLabelProvider(PropertyColumnIndexes.CONSTRAINTS),
                                    CndMessages.valueConstraintsHeaderText, CndMessages.propertyValueConstraintsToolTip, false,
                                    true);
        }

        // this will sort by property name
        this.propertyViewer.setSorter(new ViewerSorter() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ViewerComparator#sort(org.eclipse.jface.viewers.Viewer, java.lang.Object[])
             */
            @Override
            public void sort( final Viewer viewer,
                              final Object[] elements ) {
                Arrays.sort(elements);
            }
        });
    }

    private void createSuperTypesActions() {
        this.addSuperType = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleAddSuperType();
            }
        };
        this.addSuperType.setEnabled(false);
        this.addSuperType.setToolTipText(CndMessages.addSuperTypeToolTip);
        this.addSuperType.setImageDescriptor(JcrUiUtils.getNewImageDescriptor());

        this.deleteSuperType = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleDeleteSuperType();
            }
        };
        this.deleteSuperType.setEnabled(false);
        this.deleteSuperType.setToolTipText(CndMessages.deleteSuperTypeToolTip);
        this.deleteSuperType.setImageDescriptor(JcrUiUtils.getDeleteImageDescriptor());

        this.editSuperType = new Action(Utils.EMPTY_STRING) {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                handleEditSuperType();
            }
        };
        this.editSuperType.setEnabled(false);
        this.editSuperType.setToolTipText(CndMessages.editSuperTypeToolTip);
        this.editSuperType.setImageDescriptor(JcrUiUtils.getEditImageDescriptor());
    }

    private void createSuperTypesViewer( final Table superTypesTable ) {
        this.superTypesViewer = new TableViewer(superTypesTable);
        this.superTypesViewer.setLabelProvider(new LabelProvider());
        this.superTypesViewer.setContentProvider(new IStructuredContentProvider() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            @Override
            public void dispose() {
                // nothing to do
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements( final Object inputElement ) {
                final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

                if (nodeTypeDefinition == null) {
                    return Utils.EMPTY_OBJECT_ARRAY;
                }

                return nodeTypeDefinition.getDeclaredSupertypeNames();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             *      java.lang.Object)
             */
            @Override
            public void inputChanged( final Viewer viewer,
                                      final Object oldInput,
                                      final Object newInput ) {
                // nothing to do
            }
        });

        this.superTypesViewer.addDoubleClickListener(new IDoubleClickListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
             */
            @Override
            public void doubleClick( final DoubleClickEvent event ) {
                handleEditSuperType();
            }
        });

        this.superTypesViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged( final SelectionChangedEvent event ) {
                handleSuperTypeSelected();
            }
        });
    }

    Collection<QualifiedName> getChildNodeNames() {
        assert (getSelectedNodeType() != null) : "getChildNodeNames called but no selected node type"; //$NON-NLS-1$
        final Collection<ChildNodeDefinition> childNodes = getSelectedNodeType().getChildNodeDefinitions();

        if (childNodes.isEmpty()) {
            return Collections.emptyList();
        }

        final Collection<QualifiedName> childNodeNames = new ArrayList<QualifiedName>(childNodes.size());

        for (final ChildNodeDefinition childNode : childNodes) {
            if (!Utils.isEmpty(childNode.getName())) {
                childNodeNames.add(childNode.getQualifiedName());
            }
        }

        return childNodeNames;
    }

    private List<String> getNamespacePrefixes() {
        final List<NamespaceMapping> namespaces = getCnd().getNamespaceMappings();

        if (namespaces.isEmpty()) {
            return null;
        }

        final List<String> prefixes = new ArrayList<String>(namespaces.size());

        for (final NamespaceMapping namespace : namespaces) {
            prefixes.add(namespace.getPrefix());
        }

        return prefixes;
    }

    /**
     * @return the name filter pattern (can be <code>null</code>)
     */
    String getNodeTypeNameFilterPattern() {
        return this.nodeTypeNameFilterPattern;
    }

    Collection<QualifiedName> getNodeTypeNames() {
        final Collection<NodeTypeDefinition> nodeTypes = getCnd().getNodeTypeDefinitions();

        if (nodeTypes.isEmpty()) {
            return Collections.emptyList();
        }

        final Collection<QualifiedName> nodeTypeNames = new ArrayList<QualifiedName>(nodeTypes.size());

        for (final NodeTypeDefinition nodeType : nodeTypes) {
            if (!Utils.isEmpty(nodeType.getName())) {
                nodeTypeNames.add(nodeType.getQualifiedName());
            }
        }

        return nodeTypeNames;
    }

    Collection<QualifiedName> getPropertyNames() {
        assert (getSelectedNodeType() != null) : "getPropertyNames called but no selected node type"; //$NON-NLS-1$
        final Collection<PropertyDefinition> propDefns = getSelectedNodeType().getPropertyDefinitions();

        if (propDefns.isEmpty()) {
            return Collections.emptyList();
        }

        final Collection<QualifiedName> propDefnNames = new ArrayList<QualifiedName>(propDefns.size());

        for (final PropertyDefinition propDefn : propDefns) {
            if (!Utils.isEmpty(propDefn.getName())) {
                propDefnNames.add(propDefn.getQualifiedName());
            }
        }

        return propDefnNames;
    }

    /**
     * @return the selected child node definition or <code>null</code> if the viewer has an empty selection
     */
    private ChildNodeDefinition getSelectedChildNode() {
        final IStructuredSelection selection = (IStructuredSelection)this.childNodeViewer.getSelection();

        if (selection.isEmpty()) {
            return null;
        }

        assert (selection.size() == 1) : "child node viewer should not allow multiple selections"; //$NON-NLS-1$
        assert (selection.getFirstElement() instanceof ChildNodeDefinition) : "selection was not a ChildNodeDefinition"; //$NON-NLS-1$
        return (ChildNodeDefinition)selection.getFirstElement();
    }

    /**
     * @return the selected namespace mapping or <code>null</code> if the viewer has an empty selection
     */
    private NamespaceMapping getSelectedNamespace() {
        final IStructuredSelection selection = (IStructuredSelection)this.namespaceViewer.getSelection();

        if (selection.isEmpty()) {
            return null;
        }

        assert (selection.size() == 1) : "child node viewer should not allow multiple selections"; //$NON-NLS-1$
        assert (selection.getFirstElement() instanceof NamespaceMapping) : "selection was not a NamespaceMapping"; //$NON-NLS-1$
        return (NamespaceMapping)selection.getFirstElement();
    }

    /**
     * @return the selected node type definition or <code>null</code> if the viewer has an empty selection
     */
    NodeTypeDefinition getSelectedNodeType() {
        final IStructuredSelection selection = (IStructuredSelection)this.nodeTypeViewer.getSelection();

        if (selection.isEmpty()) {
            return null;
        }

        assert (selection.size() == 1) : "node type viewer should not allow multiple selections"; //$NON-NLS-1$
        assert (selection.getFirstElement() instanceof NodeTypeDefinition) : "selection was not a NodeTypeDefinition"; //$NON-NLS-1$
        return (NodeTypeDefinition)selection.getFirstElement();
    }

    /**
     * @return the selected property definition or <code>null</code> if the viewer has an empty selection
     */
    private PropertyDefinition getSelectedProperty() {
        final IStructuredSelection selection = (IStructuredSelection)this.propertyViewer.getSelection();

        if (selection.isEmpty()) {
            return null;
        }

        assert (selection.size() == 1) : "property viewer should not allow multiple selections"; //$NON-NLS-1$
        assert (selection.getFirstElement() instanceof PropertyDefinition) : "selection was not a PropertyDefinition"; //$NON-NLS-1$
        return (PropertyDefinition)selection.getFirstElement();
    }

    /**
     * @return the selected super type or <code>null</code> if the viewer has an empty selection
     */
    private String getSelectedSuperType() {
        final IStructuredSelection selection = (IStructuredSelection)this.superTypesViewer.getSelection();

        if (selection.isEmpty()) {
            return null;
        }

        assert (selection.size() == 1) : "super types viewer should not allow multiple selections"; //$NON-NLS-1$
        assert (selection.getFirstElement() instanceof String) : "selection was not a string"; //$NON-NLS-1$
        return (String)selection.getFirstElement();
    }

    void handleAbstractChanged( final boolean newValue ) {
        getSelectedNodeType().setAbstract(newValue);
    }

    void handleAddChildNode() {
        assert (getSelectedNodeType() != null) : "add child node handler called but there is no selected node type"; //$NON-NLS-1$

        final ChildNodeDialog dialog = new ChildNodeDialog(getShell(), getChildNodeNames(), getNamespacePrefixes());
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final ChildNodeDefinition newChildNodeDefinition = dialog.getChildNodeDefinition();

            // add and select new child node definition
            if (getSelectedNodeType().addChildNodeDefinition(newChildNodeDefinition)) {
                this.childNodeViewer.setSelection(new StructuredSelection(newChildNodeDefinition));
            } else {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorAddingChildNode, newChildNodeDefinition.getName()));
            }
        }
    }

    void handleAddNamespace() {
        final NamespaceMappingDialog dialog = new NamespaceMappingDialog(getShell(), getCnd().getNamespaceMappings());
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final NamespaceMapping newNamespaceMapping = dialog.getNamespaceMapping();

            if (getCnd().addNamespaceMapping(newNamespaceMapping)) {
                this.namespaceViewer.setSelection(new StructuredSelection(newNamespaceMapping));
            } else {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorAddingNamespaceMapping, newNamespaceMapping));
            }
        }
    }

    void handleAddNodeType() {
        final QualifiedNameDialog dialog = new QualifiedNameDialog(getShell(),
                                                                   CndMessages.newNodeTypeDialogTitle,
                                                                   Messages.nodeTypeDefinitionName,
                                                                   getNamespacePrefixes());
        dialog.setExistingQNames(getNodeTypeNames());
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final QualifiedName newNodeTypeName = dialog.getQualifiedName();
            final NodeTypeDefinition newNodeTypeDefinition = new NodeTypeDefinition();
            newNodeTypeDefinition.setName(newNodeTypeName.get());

            // add and select new node type definition
            if (getCnd().addNodeTypeDefinition(newNodeTypeDefinition)) {
                this.nodeTypeViewer.setSelection(new StructuredSelection(newNodeTypeDefinition));
            } else {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorAddingNodeType, newNodeTypeName));
            }
        }
    }

    void handleAddProperty() {
        assert (getSelectedNodeType() != null) : "add property handler called but there is no selected node type"; //$NON-NLS-1$

        final PropertyDialog dialog = new PropertyDialog(getShell(), getPropertyNames(), getNamespacePrefixes());
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final PropertyDefinition newPropertyDefinition = dialog.getPropertyDefinition();

            // add and select new property definition
            if (getSelectedNodeType().addPropertyDefinition(newPropertyDefinition)) {
                this.propertyViewer.setSelection(new StructuredSelection(newPropertyDefinition));
            } else {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorAddingProperty, newPropertyDefinition.getName()));
            }
        }
    }

    void handleAddSuperType() {
        assert (getSelectedNodeType() != null) : "add supertype button is enabled when there is no selected node type"; //$NON-NLS-1$

        final QualifiedNameDialog dialog = new QualifiedNameDialog(getShell(),
                                                                   CndMessages.newSuperTypeDialogTitle,
                                                                   Messages.superTypeName,
                                                                   getNamespacePrefixes());
        dialog.setExistingQNames(getSelectedNodeType().getSupertypes());
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();
            final QualifiedName newSupertype = dialog.getQualifiedName();

            // add and select new supertype
            if (nodeTypeDefinition.addSuperType(newSupertype.get())) {
                this.superTypesViewer.setSelection(new StructuredSelection(newSupertype.get()));
            } else {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorAddingSupertype, newSupertype));
            }
        }
    }

    void handleChildNodeSelected() {
        // update button enablements
        boolean enable = (getSelectedNodeType() != null);

        if (this.addChildNode.isEnabled() != enable) {
            this.addChildNode.setEnabled(enable);
        }

        enable = (getSelectedChildNode() != null);

        if (this.editChildNode.isEnabled() != enable) {
            this.editChildNode.setEnabled(enable);
        }

        if (this.deleteChildNode.isEnabled() != enable) {
            this.deleteChildNode.setEnabled(enable);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.ui.cnd.CndEditorPage#handleCndReloaded()
     */
    @Override
    public void handleCndReloaded() {
        // make sure GUI has been constructed before refreshing
        if (this.propertyViewer != null) {
            refreshNameControls();
            refreshAttributeControls();
            refreshSuperTypes();
            refreshPropertyViewer();
            refreshChildNodeViewer();
        }
    }

    void handleDeleteChildNode() {
        assert (getSelectedNodeType() != null) : "Delete child node button is enabled and there is no node type selected"; //$NON-NLS-1$
        assert (getSelectedChildNode() != null) : "Delete child node button is enabled and there is no child node selected"; //$NON-NLS-1$

        final ChildNodeDefinition childNodeDefinition = getSelectedChildNode();
        String name = childNodeDefinition.getName();

        if (Utils.isEmpty(name)) {
            name = Messages.missingName;
        }

        // show confirmation dialog
        if (MessageFormDialog.openQuestion(getShell(), CndMessages.deleteChildNodeDialogTitle, JcrUiUtils.getCndEditorImage(),
                                           NLS.bind(CndMessages.deleteChildNodeDialogMessage, name))) {
            if (!getSelectedNodeType().removeChildNodeDefinition(childNodeDefinition)) {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorDeletingChildNode, name));
            }
        }
    }

    void handleDeleteNamespace() {
        assert (getSelectedNamespace() != null) : "Delete namespace button is enabled and there is no namespace selected"; //$NON-NLS-1$

        final NamespaceMapping namespaceMapping = getSelectedNamespace();
        String prefix = namespaceMapping.getPrefix();

        if (Utils.isEmpty(prefix)) {
            prefix = CndMessages.missingValue;
        }

        // show confirmation dialog
        if (MessageFormDialog.openQuestion(getShell(), CndMessages.deleteNamespaceDialogTitle, JcrUiUtils.getCndEditorImage(),
                                           NLS.bind(CndMessages.deleteNamespaceDialogMessage, prefix))) {
            if (!getCnd().removeNamespaceMapping(namespaceMapping)) {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorDeletingNamespace, prefix));
            }
        }
    }

    void handleDeleteNodeType() {
        assert (getSelectedNodeType() != null) : "Delete node type button is enabled and there is no node type selected"; //$NON-NLS-1$

        final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();
        String name = nodeTypeDefinition.getName();

        if (Utils.isEmpty(name)) {
            name = Messages.missingName;
        }

        // show confirmation dialog
        if (MessageFormDialog.openQuestion(getShell(), CndMessages.deleteNodeTypeDialogTitle, JcrUiUtils.getCndEditorImage(),
                                           NLS.bind(CndMessages.deleteNodeTypeDialogMessage, name))) {
            if (!getCnd().removeNodeTypeDefinition(nodeTypeDefinition)) {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorDeletingNodeType, name));
            }
        }
    }

    void handleDeleteProperty() {
        assert (getSelectedNodeType() != null) : "Delete property button is enabled and there is no node type selected"; //$NON-NLS-1$
        assert (getSelectedProperty() != null) : "Delete property button is enabled and there is no property selected"; //$NON-NLS-1$

        final PropertyDefinition propertyDefinition = getSelectedProperty();
        String name = propertyDefinition.getName();

        if (Utils.isEmpty(name)) {
            name = Messages.missingName;
        }

        // show confirmation dialog
        if (MessageFormDialog.openQuestion(getShell(), CndMessages.deletePropertyDialogTitle, JcrUiUtils.getCndEditorImage(),
                                           NLS.bind(CndMessages.deletePropertyDialogMessage, name))) {
            if (!getSelectedNodeType().removePropertyDefinition(propertyDefinition)) {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorDeletingProperty, name));
            }
        }
    }

    void handleDeleteSuperType() {
        assert (getSelectedNodeType() != null) : "Delete super type button is enabled and there is no node type selected"; //$NON-NLS-1$
        assert (getSelectedSuperType() != null) : "Delete super type button is enabled and there is no super type selected"; //$NON-NLS-1$

        String superTypeName = getSelectedSuperType();

        // should always have a name but just in case
        if (Utils.isEmpty(superTypeName)) {
            superTypeName = Messages.missingName;
        }

        // show confirmation dialog
        if (MessageFormDialog.openQuestion(getShell(), CndMessages.deleteSuperTypeDialogTitle, JcrUiUtils.getCndEditorImage(),
                                           NLS.bind(CndMessages.deleteSuperTypeDialogMessage, superTypeName))) {
            if (!getSelectedNodeType().removeSuperType(superTypeName)) {
                MessageFormDialog.openError(getShell(), UiMessages.errorDialogTitle, JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorDeletingSupertype, superTypeName));
            }
        }
    }

    void handleEditChildNode() {
        assert (getSelectedNodeType() != null) : "edit child node handler called but there is no selected node type"; //$NON-NLS-1$
        assert (getSelectedChildNode() != null) : "edit child node handler has been called when there is no child node selected"; //$NON-NLS-1$

        final ChildNodeDefinition childNodeBeingEdited = getSelectedChildNode();
        final ChildNodeDialog dialog = new ChildNodeDialog(getShell(),
                                                           getChildNodeNames(),
                                                           getNamespacePrefixes(),
                                                           childNodeBeingEdited);
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();
            final ChildNodeDefinition newChildNodeDefinition = dialog.getChildNodeDefinition();
            boolean removed = false;
            boolean added = false;

            if (nodeTypeDefinition.removeChildNodeDefinition(childNodeBeingEdited)) {
                removed = true;

                if (nodeTypeDefinition.addChildNodeDefinition(newChildNodeDefinition)) {
                    added = true;
                }
            }

            if (!removed || !added) {
                MessageFormDialog.openError(getShell(),
                                            UiMessages.errorDialogTitle,
                                            JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorEditingChildNode,
                                                     new Object[] { childNodeBeingEdited.getName(), removed, added }));
            }
        }
    }

    void handleEditNamespace() {
        assert (getSelectedNamespace() != null) : "Edit namespace handler has been called when there is no namespace selected"; //$NON-NLS-1$
        final NamespaceMapping selectedNamespace = getSelectedNamespace();

        // TODO if prefix is changed should qualified names with old prefix be automatically updated?
        final NamespaceMappingDialog dialog = new NamespaceMappingDialog(getShell(),
                                                                         getCnd().getNamespaceMappings(),
                                                                         selectedNamespace);
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final NamespaceMapping modifiedNamespaceMapping = dialog.getNamespaceMapping();
            boolean removed = false;
            boolean added = false;

            // remove existing and add in new
            if (getCnd().removeNamespaceMapping(selectedNamespace)) {
                removed = true;

                if (getCnd().addNamespaceMapping(modifiedNamespaceMapping)) {
                    added = true;
                }
            }

            if (!removed || !added) {
                MessageFormDialog.openError(getShell(),
                                            UiMessages.errorDialogTitle,
                                            JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorEditingNamespaceMapping, new Object[] {
                                                    modifiedNamespaceMapping, removed, added }));
            }
        }
    }

    void handleEditProperty() {
        assert (getSelectedNodeType() != null) : "Edit property handler called but there is no selected node type"; //$NON-NLS-1$
        assert (getSelectedProperty() != null) : "Edit property handler has been called when there is no property selected"; //$NON-NLS-1$

        final PropertyDefinition propertyBeingEdited = getSelectedProperty();
        final PropertyDialog dialog = new PropertyDialog(getShell(),
                                                         getPropertyNames(),
                                                         getNamespacePrefixes(),
                                                         propertyBeingEdited);
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();
            final PropertyDefinition newPropertyDefinition = dialog.getPropertyDefinition();
            boolean removed = false;
            boolean added = false;

            if (nodeTypeDefinition.removePropertyDefinition(propertyBeingEdited)) {
                removed = true;

                if (nodeTypeDefinition.addPropertyDefinition(newPropertyDefinition)) {
                    added = true;
                }
            }

            if (!removed || !added) {
                MessageFormDialog.openError(getShell(),
                                            UiMessages.errorDialogTitle,
                                            JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorEditingProperty, new Object[] {
                                                    propertyBeingEdited.getName(), removed, added }));
            }
        }
    }

    void handleEditSuperType() {
        assert (getSelectedSuperType() != null) : "Edit super type handler has been called when there is no super type selected"; //$NON-NLS-1$
        final String selectedSupertype = getSelectedSuperType();

        final QualifiedNameDialog dialog = new QualifiedNameDialog(getShell(),
                                                                   CndMessages.editSuperTypeDialogTitle,
                                                                   Messages.superTypeName,
                                                                   getNamespacePrefixes(),
                                                                   QualifiedName.parse(selectedSupertype));
        dialog.setExistingQNames(getSelectedNodeType().getSupertypes());
        dialog.create();
        dialog.getShell().pack();

        if (dialog.open() == Window.OK) {
            final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();
            final QualifiedName modifiedSuperType = dialog.getQualifiedName();
            boolean removed = false;
            boolean added = false;

            // remove existing and add in new
            if (nodeTypeDefinition.removeSuperType(selectedSupertype)) {
                removed = true;

                if (nodeTypeDefinition.addSuperType(modifiedSuperType.get())) {
                    added = true;
                }
            }

            if (!removed || !added) {
                MessageFormDialog.openError(getShell(),
                                            UiMessages.errorDialogTitle,
                                            JcrUiUtils.getCndEditorImage(),
                                            NLS.bind(CndMessages.errorEditingSupertype, new Object[] { modifiedSuperType, removed,
                                                    added }));
            }
        }
    }

    void handleMixinChanged( final boolean newValue ) {
        getSelectedNodeType().setMixin(newValue);
    }

    void handleNamespaceSelected() {
        // update button enablements
        final boolean enable = (getSelectedNamespace() != null);

        // this.addNamespace is always enabled

        if (this.editNamespace.isEnabled() != enable) {
            this.editNamespace.setEnabled(enable);
        }

        if (this.deleteNamespace.isEnabled() != enable) {
            this.deleteNamespace.setEnabled(enable);
        }
    }

    void handleNodeTypeNameChanged( final String newNodeTypeName ) {
        if (getSelectedNodeType() != null) {
            getSelectedNodeType().setName(newNodeTypeName);
        }
    }

    void handleNodeTypeNameFilterModified( final String namePattern ) {
        this.nodeTypeNameFilterPattern = namePattern;
        this.nodeTypeViewer.refresh();
    }

    void handleNodeTypeSelected() {
        updateEnabledState();

        // update section descriptions
        if (getSelectedNodeType() == null) {
            this.detailsSection.setDescription(CndMessages.cndEditorDetailsSectionDescription);
            this.propertiesSection.setDescription(CndMessages.cndEditorChildNodeSectionDescription);
            this.childNodeSection.setDescription(CndMessages.cndEditorPropertySectionDescription);
        } else {
            String name = getSelectedNodeType().getName();

            if (Utils.isEmpty(name)) {
                name = Messages.missingName;
            }

            this.nameEditor.setNameBeingEdited(getSelectedNodeType().getQualifiedName());
            this.detailsSection.setDescription(NLS.bind(CndMessages.cndEditorChildNodeSectionDescriptionWithNodeTypeName, name));
            this.propertiesSection.setDescription(NLS.bind(CndMessages.cndEditorChildNodeSectionDescriptionWithNodeTypeName, name));
            this.childNodeSection.setDescription(NLS.bind(CndMessages.cndEditorPropertySectionDescriptionWithNodeTypeName, name));
        }

        // populate details section
        refreshNameControls();
        refreshAttributeControls();
        refreshSuperTypes();
        refreshPropertyViewer();
        refreshChildNodeViewer();
    }

    void handleOrderableChanged( final boolean newValue ) {
        getSelectedNodeType().setOrderableChildNodes(newValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.ui.cnd.CndEditorPage#handlePropertyChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void handlePropertyChanged( final PropertyChangeEvent e ) {
        // return if GUI hasn't been constructed yet
        if (this.childNodeViewer == null) {
            return;
        }

        final Object source = e.getSource();
        final String propName = e.getPropertyName();

        if (source instanceof NodeTypeDefinition) {
            if (NodeTypeDefinition.PropertyName.ABSTRACT.toString().equals(propName)
                    || NodeTypeDefinition.PropertyName.MIXIN.toString().equals(propName)
                    || NodeTypeDefinition.PropertyName.ORDERABLE.toString().equals(propName)
                    || NodeTypeDefinition.PropertyName.QUERYABLE.toString().equals(propName)
                    || NodeTypeDefinition.PropertyName.PRIMARY_ITEM.toString().equals(propName)) {
                validateAttributes();
            } else if (NodeTypeDefinition.PropertyName.CHILD_NODES.toString().equals(propName)) {
                validateChildNodes();
                this.childNodeViewer.refresh();
            } else if (NodeTypeDefinition.PropertyName.NAME.toString().equals(propName)) {
                validateName();
            } else if (NodeTypeDefinition.PropertyName.PROPERTY_DEFINITIONS.toString().equals(propName)) {
                validateProperties();
                this.propertyViewer.refresh();
            } else if (NodeTypeDefinition.PropertyName.SUPERTYPES.toString().equals(propName)) {
                validateSuperTypes();
                this.superTypesViewer.refresh();
            }
        } else if (source instanceof NamespaceMapping) {
            this.namespaceViewer.refresh(source);
        } else if (source instanceof PropertyDefinition) {
            this.propertyViewer.refresh(source);
        } else if (source instanceof ChildNodeDefinition) {
            this.childNodeViewer.refresh(source);
        }
    }

    void handlePropertySelected() {
        // update button enablements
        boolean enable = (getSelectedNodeType() != null);

        if (this.addProperty.isEnabled() != enable) {
            this.addProperty.setEnabled(enable);
        }

        enable = (getSelectedProperty() != null);

        if (this.editProperty.isEnabled() != enable) {
            this.editProperty.setEnabled(enable);
        }

        if (this.deleteProperty.isEnabled() != enable) {
            this.deleteProperty.setEnabled(enable);
        }
    }

    void handleQueryableChanged( final boolean newValue ) {
        getSelectedNodeType().setQueryable(newValue);
    }

    void handleSuperTypeSelected() {
        // update button enablements
        final boolean enable = (getSelectedSuperType() != null);

        if (this.editSuperType.isEnabled() != enable) {
            this.editSuperType.setEnabled(enable);
        }

        if (this.deleteSuperType.isEnabled() != enable) {
            this.deleteSuperType.setEnabled(enable);
        }
    }

    private void populateUi() {
        this.namespaceViewer.setInput(this);
        this.nodeTypeViewer.setInput(this);

        // size columns to the data
        UiUtils.pack(this.namespaceViewer, this.nodeTypeViewer);

        if (this.nodeTypeViewer.getTable().getItemCount() != 0) {
            this.nodeTypeViewer.getTable().select(0);
            final Event e = new Event();
            e.widget = this.nodeTypeViewer.getTable();
            this.nodeTypeViewer.getTable().notifyListeners(SWT.Selection, e);
        }
    }

    private void refreshAttributeControls() {
        boolean notConcrete = false;
        boolean mixin = false;
        boolean orderable = false;
        boolean queryable = false;

        final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

        if (nodeTypeDefinition != null) {
            if (nodeTypeDefinition.isAbstract()) {
                notConcrete = true;
            }
            if (nodeTypeDefinition.isMixin()) {
                mixin = true;
            }
            if (nodeTypeDefinition.hasOrderableChildNodes()) {
                orderable = true;
            }
            if (nodeTypeDefinition.isQueryable()) {
                queryable = true;
            }
        }

        if (this.btnAbstract.getSelection() != notConcrete) {
            this.btnAbstract.setSelection(notConcrete);
        }

        if (this.btnMixin.getSelection() != mixin) {
            this.btnMixin.setSelection(mixin);
        }

        if (this.btnOrderable.getSelection() != orderable) {
            this.btnOrderable.setSelection(orderable);
        }

        if (this.btnQueryable.getSelection() != queryable) {
            this.btnQueryable.setSelection(queryable);
        }
    }

    private void refreshChildNodeViewer() {
        if (this.childNodeViewer != null) {
            this.childNodeViewer.setInput(this);
            UiUtils.pack(this.childNodeViewer);
            validateChildNodes();
        }
    }

    private void refreshNameControls() {
        final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

        if (nodeTypeDefinition == null) {
            this.nameEditor.setNameBeingEdited(null);
        } else {
            // load name editor with current namespace prefixes, current node type names, and selected node type name
            this.nameEditor.setNameBeingEdited(nodeTypeDefinition.getQualifiedName());
            this.nameEditor.setValidQualifiers(getNamespacePrefixes());
            this.nameEditor.setExistingQNames(getNodeTypeNames());
        }
    }

    private void refreshPropertyViewer() {
        if (this.propertyViewer != null) {
            this.propertyViewer.setInput(this);
            UiUtils.pack(this.propertyViewer);
            validateProperties();
        }
    }

    private void refreshSuperTypes() {
        if (this.superTypesViewer != null) {
            this.superTypesViewer.setInput(this);
            UiUtils.pack(this.superTypesViewer);
            validateSuperTypes();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.ui.cnd.CndEditorPage#setResourceReadOnly(boolean)
     */
    @Override
    protected void setResourceReadOnly( final boolean readOnly ) {
        updateEnabledState();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.tools.modeshape.jcr.ui.cnd.CndEditorPage#updateAllMessages()
     */
    @Override
    protected void updateAllMessages() {
        validateChildNodes();
        validateNamespaces();
        validateNodeTypes();
        validateProperties();
    }

    private void updateEnabledState() {
        // return if GUI hasn't been constructed yet
        if (this.nodeTypeViewer == null) {
            return;
        }

        final boolean cndWritable = !getCndEditor().isReadOnly();
        final boolean enableWithNodeTypeSelected = (cndWritable && (getSelectedNodeType() != null));

        { // namespaces section
            if (this.addNamespace.isEnabled() != cndWritable) {
                this.addNamespace.setEnabled(cndWritable);
            }

            if (this.namespaceViewer.getTable().isEnabled() != cndWritable) {
                this.namespaceViewer.getTable().setEnabled(cndWritable);
            }

            final boolean enable = (cndWritable && (getSelectedNamespace() != null));

            if (this.editNamespace.isEnabled() != enable) {
                this.editNamespace.setEnabled(enable);
            }

            if (this.deleteNamespace.isEnabled() != enable) {
                this.deleteNamespace.setEnabled(enable);
            }
        }

        { // node types section
            if (this.addNodeType.isEnabled() != cndWritable) {
                this.addNodeType.setEnabled(cndWritable);
            }

            if (this.nodeTypeViewer.getTable().isEnabled() != cndWritable) {
                this.nodeTypeViewer.getTable().setEnabled(cndWritable);
            }

            if (this.deleteNodeType.isEnabled() != enableWithNodeTypeSelected) {
                this.deleteNodeType.setEnabled(enableWithNodeTypeSelected);
            }
        }

        { // details section
            if (this.nameEditor.isEnabled() != enableWithNodeTypeSelected) {
                this.nameEditor.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.btnAbstract.isEnabled() != enableWithNodeTypeSelected) {
                this.btnAbstract.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.btnMixin.isEnabled() != enableWithNodeTypeSelected) {
                this.btnMixin.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.btnOrderable.isEnabled() != enableWithNodeTypeSelected) {
                this.btnOrderable.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.btnQueryable.isEnabled() != enableWithNodeTypeSelected) {
                this.btnQueryable.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.addSuperType.isEnabled() != enableWithNodeTypeSelected) {
                this.addSuperType.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.superTypesViewer.getTable().isEnabled() != enableWithNodeTypeSelected) {
                this.superTypesViewer.getTable().setEnabled(enableWithNodeTypeSelected);
            }

            final boolean enable = (enableWithNodeTypeSelected && (getSelectedSuperType() != null));

            if (this.editSuperType.isEnabled() != enable) {
                this.editSuperType.setEnabled(enable);
            }

            if (this.deleteSuperType.isEnabled() != enable) {
                this.deleteSuperType.setEnabled(enable);
            }
        }

        { // properties section
            if (this.addProperty.isEnabled() != enableWithNodeTypeSelected) {
                this.addProperty.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.propertyViewer.getTable().isEnabled() != enableWithNodeTypeSelected) {
                this.propertyViewer.getTable().setEnabled(enableWithNodeTypeSelected);
            }

            final boolean enable = (enableWithNodeTypeSelected && (getSelectedProperty() != null));

            if (this.editProperty.isEnabled() != enable) {
                this.editProperty.setEnabled(enable);
            }

            if (this.deleteProperty.isEnabled() != enable) {
                this.deleteProperty.setEnabled(enable);
            }
        }

        { // child nodes section
            if (this.addChildNode.isEnabled() != enableWithNodeTypeSelected) {
                this.addChildNode.setEnabled(enableWithNodeTypeSelected);
            }

            if (this.childNodeViewer.getTable().isEnabled() != enableWithNodeTypeSelected) {
                this.childNodeViewer.getTable().setEnabled(enableWithNodeTypeSelected);
            }

            final boolean enable = (enableWithNodeTypeSelected && (getSelectedChildNode() != null));

            if (this.editChildNode.isEnabled() != enable) {
                this.editChildNode.setEnabled(enable);
            }

            if (this.deleteChildNode.isEnabled() != enable) {
                this.deleteChildNode.setEnabled(enable);
            }
        }
    }

    private void updateMessage( final ValidationStatus status,
                                final ErrorMessage errorMessage ) {
        final IMessageManager msgMgr = getCndEditor().getMessageManager();
        JcrUiUtils.setMessage(status, errorMessage);

        if (errorMessage.isOk()) {
            if (errorMessage.getControl() == null) {
                msgMgr.removeMessage(errorMessage.getKey());
            } else {
                msgMgr.removeMessage(errorMessage.getKey(), errorMessage.getControl());
            }
        } else {
            if (errorMessage.getControl() == null) {
                msgMgr.addMessage(errorMessage.getKey(), errorMessage.getMessage(), errorMessage.getData(),
                                  errorMessage.getMessageType());
            } else {
                msgMgr.addMessage(errorMessage.getKey(), errorMessage.getMessage(), errorMessage.getData(),
                                  errorMessage.getMessageType(), errorMessage.getControl());
            }
        }
    }

    private void validateAttributes() {
        // TODO primary item
    }

    private void validateChildNodes() {
        final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

        if (nodeTypeDefinition == null) {
            this.nodeTypesError.setOkMessage(null);
        } else {
            final MultiValidationStatus status = CndValidator.validateChildNodeDefinitions(nodeTypeDefinition.getName(),
                                                                                           nodeTypeDefinition.getChildNodeDefinitions());
            updateMessage(status, this.nodeTypesError);
        }
    }

    private void validateName() {
        updateMessage(this.nameEditor.getStatus(), this.nodeTypeNameError);
    }

    private void validateNamespaces() {
        final MultiValidationStatus status = CndValidator.validateNamespaceMappings(getCnd().getNamespaceMappings());
        updateMessage(status, this.namespacesError);
    }

    private void validateNodeTypes() {
        final MultiValidationStatus status = CndValidator.validateNodeTypeDefinitions(getCnd().getNodeTypeDefinitions());
        updateMessage(status, this.nodeTypesError);
    }

    private void validateProperties() {
        final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

        if (nodeTypeDefinition == null) {
            this.propertiesError.setOkMessage(null);
        } else {
            final MultiValidationStatus status = CndValidator.validatePropertyDefinitions(nodeTypeDefinition.getName(),
                                                                                          nodeTypeDefinition.getPropertyDefinitions());
            updateMessage(status, this.propertiesError);
        }
    }

    private void validateSuperTypes() {
        final NodeTypeDefinition nodeTypeDefinition = getSelectedNodeType();

        if (nodeTypeDefinition == null) {
            this.superTypesError.setOkMessage(null);
        } else {
            String nodeTypeName = nodeTypeDefinition.getName();

            if (Utils.isEmpty(nodeTypeName)) {
                nodeTypeName = Messages.missingName;
            }

            final MultiValidationStatus status = CndValidator.validateSuperTypes(nodeTypeName,
                                                                                 nodeTypeDefinition.getState(PropertyName.SUPERTYPES),
                                                                                 nodeTypeDefinition.getDeclaredSupertypeNames());
            updateMessage(status, this.superTypesError);
        }
    }

    interface ChildNodeColumnIndexes {
        int ATTRIBUTES = 3;
        int DEFAULT_TYPE = 2;
        int NAME = 0;
        int REQUIRED_TYPES = 1;
    }

    interface NamespaceColumnIndexes {
        int PREFIX = 0;
        int URI = 1;
    }

    interface PropertyColumnIndexes {
        int ATTRIBUTES = 3;
        int CONSTRAINTS = 4;
        int DEFAULT_VALUES = 2;
        int NAME = 0;
        int TYPE = 1;
    }
}
