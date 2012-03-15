/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.ui.cnd;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableEditor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.jboss.tools.modeshape.jcr.cnd.CndImporter;
import org.jboss.tools.modeshape.jcr.cnd.CompactNodeTypeDefinition;
import org.jboss.tools.modeshape.jcr.ui.Activator;
import org.jboss.tools.modeshape.jcr.ui.UiConstants;
import org.jboss.tools.modeshape.jcr.ui.UiConstants.Images;
import org.jboss.tools.modeshape.ui.UiMessages;
import org.jboss.tools.modeshape.ui.forms.MessageFormDialog;

/**
 * 
 */
public final class CndEditor extends SharedHeaderFormEditor implements IPersistableEditor, IResourceChangeListener,
        PropertyChangeListener {

    /**
     * The memento key for the index of the selected editor.
     */
    private static final String SELECTED_PAGE = "SELECTED_PAGE"; //$NON-NLS-1$

    private boolean dirty = false;
    private boolean readOnly = false;

    private IMemento memento;

    private final FileDocumentProvider documentProvider = new FileDocumentProvider();
    private long modificationStamp = 0;
    private Listener refreshListener;

    private CompactNodeTypeDefinition originalCnd;
    private CompactNodeTypeDefinition cndBeingEdited;

    private ScrolledForm scrolledForm;

    private final CndEditorPage formsPage;
//    private final CndEditorPage sourcePage;

    /**
     * Constructs a CND editor.
     */
    public CndEditor() {
        this.formsPage = new CndFormsEditorPage(this);
    }

    /**
     * Allow inner classes access to this instance.
     * 
     * @return this instance
     */
    CndEditor accessThis() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    @Override
    protected void addPages() {
        try {
            addPage(0, this.formsPage); // Page 0: Forms Editor Page

            TextEditor sourceEditor = new TextEditor() {
                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#isEditable()
                 */
                @Override
                public boolean isEditable() {
                    return false;
                }
            };

            // add text editor and set editor tab title
            addPage(1, sourceEditor, getEditorInput());
            setPageText((getPageCount() - 1), CndMessages.cndEditorSourcePageTitle);

            // handle when CND changed outside of this editor
            this.refreshListener = new Listener() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
                 */
                @Override
                public void handleEvent( final Event event ) {
                    refreshCnd();
                }
            };

            // hook activation listener
            getContainer().addListener(SWT.Activate, this.refreshListener);

            // restore state
            int selectedPageNum = 0;

            if (this.memento != null) {
                final int value = this.memento.getInteger(SELECTED_PAGE);

                if (value != -1) {
                    selectedPageNum = value;
                }
            }

            setActivePage(selectedPageNum);
        } catch (final PartInitException e) {
            // this will open a "Could not open editor" page with exception details
            throw new RuntimeException(CndMessages.errorOpeningCndEditor, e);
        }
    }

    //
    // private void contributeToMenu( IMenuManager menuMgr ) {
    // menuMgr.add(this.updateRegisteryAction);
    // menuMgr.add(this.showRegistryViewAction);
    // menuMgr.update(true);
    // }
    //
    // private void contributeToToolBar( IToolBarManager toolBarMgr ) {
    // toolBarMgr.add(this.updateRegisteryAction);
    // toolBarMgr.add(this.showRegistryViewAction);
    // toolBarMgr.update(true);
    // }
    //
    // private void createActions() {
    // this.updateRegisteryAction = new Action(CndMessages.updateMedInRegistryActionText, SWT.FLAT) {
    // @Override
    // public void run() {
    // IEditorInput editorInput = getEditorInput();
    //
    // if (editorInput instanceof IFileEditorInput) {
    // IFile medFile = ((IFileEditorInput)editorInput).getFile();
    // RegistryDeploymentValidator.deploy(medFile);
    // }
    // }
    // };
    // this.updateRegisteryAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(REGISTERY_MED_UPDATE_ACTION));
    // this.updateRegisteryAction.setToolTipText(CndMessages.updateMedInRegistryActionToolTip);
    //
    // this.showRegistryViewAction = new ShowModelExtensionRegistryViewAction();
    // }

    private void createCnd() throws Exception {
        // TODO implement createCnd
        CndImporter importer = new CndImporter();
        List<Throwable> errors = new ArrayList<Throwable>();
        this.cndBeingEdited = importer.importFrom(getFile().getContents(), errors, getFile().getName());
        // TODO implement
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.forms.editor.SharedHeaderFormEditor#createHeaderContents(org.eclipse.ui.forms.IManagedForm)
     */
    @Override
    protected void createHeaderContents( final IManagedForm headerForm ) {
        this.scrolledForm = headerForm.getForm();
        this.scrolledForm.setImage(Activator.getSharedInstance().getImage(UiConstants.Images.CND_EDITOR));
        this.scrolledForm.setText(CndMessages.cndEditorTitle);

        final Form form = this.scrolledForm.getForm();
        getToolkit().decorateFormHeading(form);
        //
        // createActions();
        // contributeToToolBar(form.getToolBarManager());
        // contributeToMenu(form.getMenuManager());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave( final IProgressMonitor monitor ) {
        internalSave(monitor);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    @Override
    public void doSaveAs() {
        // TODO implement doSaveAs
        final IProgressMonitor progressMonitor = getProgressMonitor();
        // SaveAsDialog dialog = new SaveAsDialog(getShell());
        // dialog.setOriginalFile(getFile());
        // dialog.create();
        //
        // // dialog was canceled
        // if (dialog.open() == Window.CANCEL) {
        // if (progressMonitor != null) {
        // progressMonitor.setCanceled(true);
        // }
        //
        // return;
        // }
        //
        // // dialog OK'd
        // IPath filePath = dialog.getResult();
        //
        // // make sure that file has the right extension
        // if (!ExtensionConstants.MED_EXTENSION.equals(filePath.getFileExtension())) {
        // filePath = filePath.addFileExtension(ExtensionConstants.MED_EXTENSION);
        // }
        //
        // IWorkspace workspace = ResourcesPlugin.getWorkspace();
        // IFile file = workspace.getRoot().getFile(filePath);
        //
        // try {
        // // create set new editor input file
        // InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        // file.create(emptyStream, true, progressMonitor);
        // setInput(new FileEditorInput(file));
        //
        // // save MED in new file
        // internalSave(progressMonitor);
        // } catch (Exception e) {
        // IStatus status = null;
        //
        // if (!(e instanceof CoreException)) {
        // status = new Status(IStatus.ERROR, UiConstants.PLUGIN_ID, e.getLocalizedMessage());
        // } else {
        // status = ((CoreException)e).getStatus();
        // }
        //
        // ErrorDialog.openError(getShell(), UiMessages.errorDialogTitle, CndMessages.cndEditorSaveError, status);
        // }
    }

    /**
     * @return the CND being edited (never <code>null</code>)
     */
    CompactNodeTypeDefinition getCnd() {
        return this.cndBeingEdited;
    }

    /**
     * @return the *.cnd resource (never <code>null</code>)
     */
    protected IFile getFile() {
        return ((IFileEditorInput)getEditorInput()).getFile();
    }

    /**
     * @return the form editor's message manager (never <code>null</code>)
     */
    IMessageManager getMessageManager() {
        return this.scrolledForm.getMessageManager();
    }

    private IProgressMonitor getProgressMonitor() {
        final IStatusLineManager statusLineMgr = getEditorSite().getActionBars().getStatusLineManager();
        return ((statusLineMgr == null) ? null : statusLineMgr.getProgressMonitor());
    }

    /**
     * @return the editor's shell (never <code>null</code>)
     */
    Shell getShell() {
        return getEditorSite().getShell();
    }
//
//    /**
//     * Handler for page change listener.
//     */
//    void handlePageChanged() {
//        final FormPage page = (FormPage)getSelectedPage();
//        this.scrolledForm.setText(page.getTitle());
//        page.setFocus();
//    }

    /**
     * Registers an editor activation listener.
     */
    private void hookRefreshListener() {
        getContainer().addListener(SWT.Activate, this.refreshListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.forms.editor.FormEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init( final IEditorSite site,
                      final IEditorInput input ) throws PartInitException {
        super.init(site, input);
        assert (input instanceof IFileEditorInput) : "MED Editor input is not a file"; //$NON-NLS-1$

        try {
            createCnd();
            ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        } catch (final Exception e) {
            throw new PartInitException(CndMessages.errorOpeningCndEditor, e);
        }
    }

    private void internalSave( final IProgressMonitor progressMonitor ) {
        final IEditorInput input = getEditorInput();

        try {
            // TODO need a CND writer
            // ModelExtensionDefinitionWriter writer = new ModelExtensionDefinitionWriter();
            // String medAsString = writer.writeAsText(this.cndBeingEdited);
            // IDocument document = this.documentProvider.getDocument(input);
            // document.set(medAsString);
            //
            // this.documentProvider.aboutToChange(input);
            // this.documentProvider.saveDocument(progressMonitor, input, document, true);
            // this.modificationStamp = this.documentProvider.getModificationStamp(input);

            // create new original CND that that will then be copied over to the CND being edited
            createCnd();
        } catch (final Exception e) {
            IStatus status = null;

            if (!(e instanceof CoreException)) {
                status = new Status(IStatus.ERROR, UiConstants.PLUGIN_ID, e.getLocalizedMessage());
            } else {
                status = ((CoreException)e).getStatus();
            }

            if ((status == null) || (status.getSeverity() != IStatus.CANCEL)) {
                ErrorDialog.openError(getShell(), UiMessages.errorDialogTitle, CndMessages.cndEditorSaveError, status);
            }
        } finally {
            this.documentProvider.changed(input);

            // update dirty flag
            refreshDirtyState();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.forms.editor.SharedHeaderFormEditor#isDirty()
     */
    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    /**
     * @return <code>true</code> if the file is readonly
     */
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
     * @return <code>true</code> if editor is synchronized with file system
     */
    boolean isSynchronized() {
        final long currentModifiedStamp = this.documentProvider.getModificationStamp(getEditorInput());
        return (this.modificationStamp == currentModifiedStamp);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent e ) {
        refreshDirtyState();
        this.formsPage.handlePropertyChanged(e);
//        this.sourcePage.handlePropertyChanged(e);
    }

    void refreshCnd() {
        if (!isSynchronized()) {
            unhookRefreshListener();

            if (MessageFormDialog.openQuestion(getShell(), CndMessages.cndChangedOnFileSystemDialogTitle,
                                               Activator.getSharedInstance().getImage(Images.CND_EDITOR),
                                               NLS.bind(CndMessages.cndChangedOnFileSystemDialogMsg, getFile().getName()))) {
                try {
                    getFile().refreshLocal(IResource.DEPTH_ONE, null);
                    this.modificationStamp = this.documentProvider.getModificationStamp(getEditorInput());

                    createCnd();

                    this.formsPage.handleCndReloaded();
//                    this.sourcePage.handleCndReloaded();
                } catch (final Exception e) {
                    // TODO log this
                    MessageFormDialog.openError(getShell(), CndMessages.cndEditorRefreshErrorTitle,
                                                Activator.getSharedInstance().getImage(Images.CND_EDITOR),
                                                CndMessages.cndEditorRefreshErrorMsg);
                }
            }

            hookRefreshListener();
        }
    }

    /**
     * Refreshes the editor's dirty state by comparing the MED being edited with the original MED.
     */
    protected void refreshDirtyState() {
        final boolean newValue = !this.originalCnd.equals(this.cndBeingEdited);

        if (isDirty() != newValue) {
            this.dirty = newValue;
            getHeaderForm().dirtyStateChanged();
        }
    }

    /**
     * Checks the *.mxd file permissions and notifies the editor's pages if the permissions have changed.
     */
    private void refreshReadOnlyState() {
        final ResourceAttributes attributes = getFile().getResourceAttributes();
        final boolean newValue = ((attributes == null) ? true : attributes.isReadOnly());

        if (isReadOnly() != newValue) {
            this.readOnly = newValue;

            this.formsPage.setResourceReadOnly(this.readOnly);
            this.formsPage.getManagedForm().refresh();
//
//            this.sourcePage.setResourceReadOnly(this.readOnly);
//            this.sourcePage.getManagedForm().refresh();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
     */
    @Override
    public void resourceChanged( final IResourceChangeEvent event ) {
        final int type = event.getType();

        if (type == IResourceChangeEvent.POST_CHANGE) {
            final IResourceDelta delta = event.getDelta();

            if (delta == null) {
                return;
            }

            try {
                delta.accept(new IResourceDeltaVisitor() {

                    /**
                     * {@inheritDoc}
                     * 
                     * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
                     */
                    @Override
                    public boolean visit( final IResourceDelta delta ) {
                        if (delta.getResource().equals(getFile())) {
                            // MXD file has been deleted so close editor
                            if ((delta.getKind() & IResourceDelta.REMOVED) != 0) {
                                if (!getShell().isDisposed()) {
                                    getShell().getDisplay().asyncExec(new Runnable() {

                                        /**
                                         * {@inheritDoc}
                                         * 
                                         * @see java.lang.Runnable#run()
                                         */
                                        @Override
                                        public void run() {
                                            getEditorSite().getPage().closeEditor(accessThis(), false);
                                        }
                                    });
                                }
                            } else if ((delta.getKind() == IResourceDelta.CHANGED)
                                    && ((delta.getFlags() & org.eclipse.core.resources.IResourceDelta.CONTENT) != 0)) {
                                if (!getShell().isDisposed()) {
                                    getShell().getDisplay().asyncExec(new Runnable() {

                                        /**
                                         * {@inheritDoc}
                                         * 
                                         * @see java.lang.Runnable#run()
                                         */
                                        @Override
                                        public void run() {
                                            refreshCnd();
                                        }
                                    });
                                }
                            }

                            return false; // stop visiting
                        }

                        return true; // keep visiting
                    }
                });
            } catch (final Exception e) {
                // TODO log this
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IPersistableEditor#restoreState(org.eclipse.ui.IMemento)
     */
    @Override
    public void restoreState( final IMemento memento ) {
        this.memento = memento;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IPersistable#saveState(org.eclipse.ui.IMemento)
     */
    @Override
    public void saveState( final IMemento memento ) {
        final int selectedPageNum = getActivePage();
        memento.putInteger(SELECTED_PAGE, selectedPageNum);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.forms.editor.SharedHeaderFormEditor#setFocus()
     */
    @Override
    public void setFocus() {
        super.setFocus();
        refreshCnd();
        refreshReadOnlyState();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
     */
    @Override
    protected void setInput( final IEditorInput input ) {
        if (getEditorInput() != null) {
            // unhook previous document provider
            this.documentProvider.disconnect(getEditorInput());
        }

        if (input instanceof IFileEditorInput) {
            super.setInput(input);

            try {
                // hook new document provider
                this.documentProvider.connect(input);
                this.modificationStamp = this.documentProvider.getModificationStamp(input);
                final IAnnotationModel model = this.documentProvider.getAnnotationModel(input);
                model.connect(this.documentProvider.getDocument(input));

                // set editor tab text
                setPartName(getEditorInput().getName());
            } catch (final Exception e) {
                throw new RuntimeException(CndMessages.errorOpeningCndEditor, e);
            }
        } else {
            throw new RuntimeException(CndMessages.cndEditorInputNotAFile);
        }
    }

    /**
     * Unregisters the editor activation listener.
     */
    private void unhookRefreshListener() {
        if (!getContainer().isDisposed()) {
            getContainer().removeListener(SWT.Activate, this.refreshListener);
        }
    }
}
