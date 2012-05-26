package org.jboss.tools.portlet.ui;

import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.DEFAULT_PACKAGE;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jst.j2ee.internal.dialogs.FilteredFileSelectionDialog;
import org.eclipse.jst.j2ee.internal.dialogs.TwoArrayQuickSorter;
import org.eclipse.jst.j2ee.internal.dialogs.TypedFileViewerFilter;
import org.eclipse.jst.j2ee.internal.web.providers.WebAppEditResourceHandler;
import org.eclipse.jst.servlet.ui.internal.wizard.SimpleTypedElementSelectionValidator;
import org.eclipse.jst.servlet.ui.internal.wizard.StringMatcher;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.portlet.core.PortletCoreActivator;

public class MultiSelectFilteredFileSelectionDialog extends
		FilteredFileSelectionDialog implements SelectionListener {
	

	private static class PackageRenderer extends LabelProvider {
		private final Image PACKAGE_ICON = JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_PACKAGE); 

		public String getText(Object element) {
			IType type = (IType) element;
			String p = type.getPackageFragment().getElementName();
			if ("".equals(p)) //$NON-NLS-1$
				p = DEFAULT_PACKAGE;
			return (p + " - " + type.getPackageFragment().getParent().getPath().toString()); //$NON-NLS-1$
		}
		public Image getImage(Object element) {
			return PACKAGE_ICON;
		}
	}

	private static class TypeRenderer extends LabelProvider {
		private final Image CLASS_ICON = JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS); 

		public String getText(Object element) {
			IType e = ((IType) element);
			return e.getElementName();
		}

		public Image getImage(Object element) {
			return CLASS_ICON;
		}

	}
	
	protected PageBook fPageBook = null;
	protected Control fPortletControl = null;
	protected Composite fChild = null;
	// construction parameters
	protected IRunnableContext fRunnableContext;
	protected ILabelProvider fElementRenderer;
	protected ILabelProvider fQualifierRenderer;
	private Object[] fElements;
	private boolean fIgnoreCase = true;
	private String fUpperListLabel;
	private String fLowerListLabel;
	// SWT widgets
	private Table fUpperList;
	private Table fLowerList;
	protected Text fText;
	private IType[] fIT;
	private String[] fRenderedStrings;
	private int[] fElementMap;
	private Integer[] fQualifierMap;

	private ISelectionStatusValidator fLocalValidator = null;
	/**
	 * MultiSelectFilteredFileSelectionDialog constructor comment.
	 * @param parent Shell
	 * @param title String
	 * @param message String
	 * @parent extensions String[]
	 * @param allowMultiple boolean
	 */
	public MultiSelectFilteredFileSelectionDialog(Shell parent, String title, String message, String[] extensions, boolean allowMultiple, IProject project) {
		super(parent, title, message, extensions, allowMultiple);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);

		if (title == null)
			setTitle(WebAppEditResourceHandler.getString("File_Selection_UI_")); //$NON-NLS-1$
		if (message == null)
			message = WebAppEditResourceHandler.getString("Select_a_file__UI_"); //$NON-NLS-1$
		setMessage(message);
		setExtensions(extensions);
		addFilter(new TypedFileViewerFilter(extensions));
		fLocalValidator = new SimpleTypedElementSelectionValidator(new Class[] { IFile.class }, allowMultiple);
		setValidator(fLocalValidator);
		
		//StatusInfo currStatus = new StatusInfo();
		//currStatus.setOK();
		Status currStatus = new Status(Status.OK, PortletCoreActivator.PLUGIN_ID, Status.OK, "", null); //$NON-NLS-1$
		
		updateStatus(currStatus);
		fElementRenderer = new TypeRenderer();
		fQualifierRenderer = new PackageRenderer();
		fRunnableContext = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		try {
			IJavaElement jelem = null;
			IProject proj = null;
			jelem = (IJavaElement) project.getAdapter(IJavaElement.class);
			if (jelem == null) {
				IResource resource = (IResource) project.getAdapter(IResource.class);
				if (resource != null) {
					proj = resource.getProject();
					if (proj != null) {
						jelem = org.eclipse.jdt.core.JavaCore.create(proj);
					}
				}
			}
			IJavaProject jp = jelem.getJavaProject();

			IType portletType = jp.findType(IPortletUIConstants.QUALIFIED_PORTLET);
			// next 3 lines fix defect 177686
			if (portletType == null) {
				return;
			}

			ArrayList portletClasses = new ArrayList();
			ITypeHierarchy tH = portletType.newTypeHierarchy(jp, null);
			IType[] types = tH.getAllSubtypes(portletType);
			for (int i = 0; i < types.length; i++) {
				if (types[i].isClass() && !portletClasses.contains(types[i]))
					portletClasses.add(types[i]);
			}
			fIT = (IType[]) portletClasses.toArray(new IType[portletClasses.size()]);
			portletClasses = null;

		} catch (Exception e) {
			PortletCoreActivator.log(e);
		}
	}
	
	/**
	 * @private
	 */
	protected void computeResult() {
		IType type = (IType) getWidgetSelection();
		if (type != null) {
			if (type == null) {
				String title = WebAppEditResourceHandler
						.getString("Select_Class_UI_"); //$NON-NLS-1$ = "Select Class"
				String message = WebAppEditResourceHandler
						.getString("Could_not_uniquely_map_the_ERROR_"); //$NON-NLS-1$ = "Could not uniquely map the class name to a class."
				MessageDialog.openError(getShell(), title, message);
				setResult(null);
			} else {
				java.util.List result = new ArrayList(1);
				result.add(type);
				setResult(result);
			}
		}
	}
	
	public void create() {
		super.create();
		fText.setFocus();
		rematch(""); //$NON-NLS-1$
		updateOkState();
	}
	
	/**
	 * Creates and returns the contents of this dialog's 
	 * button bar.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method
	 * lays out a button bar and calls the <code>createButtonsForButtonBar</code>
	 * framework method to populate it. Subclasses may override.
	 * </p>
	 *
	 * @param parent the parent composite to contain the button bar
	 * @return the button bar control
	 */
	protected Control createButtonBar(Composite parent) {

		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();

		layout.numColumns = 2;

		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite composite2 = new Composite(composite, SWT.NONE);

		// create a layout with spacing and margins appropriate for the font size.
		layout = new GridLayout();
		layout.numColumns = 0; // this is incremented by createButton
		layout.makeColumnsEqualWidth = true;
		layout.marginWidth = convertHorizontalDLUsToPixels(org.eclipse.jface.dialogs.IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginHeight = convertVerticalDLUsToPixels(org.eclipse.jface.dialogs.IDialogConstants.VERTICAL_MARGIN);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(org.eclipse.jface.dialogs.IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(org.eclipse.jface.dialogs.IDialogConstants.VERTICAL_SPACING);

		composite2.setLayout(layout);

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
		composite2.setLayoutData(data);

		composite2.setFont(parent.getFont());

		// Add the buttons to the button bar.
		super.createButtonsForButtonBar(composite2);

		return composite;
	}
	
	/*
	 * @private
	 */
	protected Control createDialogArea(Composite parent) {
		GridData gd = new GridData();

		fChild = new Composite(parent, SWT.NONE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(fChild, "com.ibm.etools.webapplicationedit.webx2010"); //$NON-NLS-1$
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginHeight = 0;
		fChild.setLayout(gl);

		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		fChild.setLayoutData(gd);

		fPageBook = new PageBook(fChild, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = 2;
		fPageBook.setLayoutData(gd);
		fPortletControl = super.createDialogArea(fPageBook);
		
		Composite composite = new Composite(fPageBook, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());

		Label messageLabel = new Label(composite, SWT.NONE);
		gd = new GridData();
		messageLabel.setLayoutData(gd);
		messageLabel.setText("Choose a portlet"); //$NON-NLS-1$

		fText = createText(composite);

		messageLabel = new Label(composite, SWT.NONE);
		gd = new GridData();
		messageLabel.setLayoutData(gd);
		messageLabel.setText("Matching portlets"); //$NON-NLS-1$

		fUpperList = createUpperList(composite);

		messageLabel = new Label(composite, SWT.NONE);
		gd = new GridData();
		messageLabel.setLayoutData(gd);
		messageLabel.setText(WebAppEditResourceHandler.getString("Qualifier__3")); //$NON-NLS-1$

		fLowerList = createLowerList(composite);

		fPortletControl = composite;
		fPageBook.showPage(fPortletControl);
		return parent;
	}
	
	/**
	 * Creates the list widget and sets layout data.
	 * @return org.eclipse.swt.widgets.List
	 */
	private Table createLowerList(Composite parent) {
		if (fLowerListLabel != null)
			 (new Label(parent, SWT.NONE)).setText(fLowerListLabel);

		Table list = new Table(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		list.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event evt) {
				handleLowerSelectionChanged();
			}
		});
		list.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event evt) {
				handleLowerDoubleClick();
			}
		});
		list.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				fQualifierRenderer.dispose();
			}
		});
		GridData spec = new GridData();
		spec.widthHint = convertWidthInCharsToPixels(50);
		spec.heightHint = convertHeightInCharsToPixels(5);
		spec.grabExcessVerticalSpace = true;
		spec.grabExcessHorizontalSpace = true;
		spec.horizontalAlignment = GridData.FILL;
		spec.verticalAlignment = GridData.FILL;
		list.setLayoutData(spec);
		return list;
	}
	
	/**
	 * Creates the text widget and sets layout data.
	 * @return org.eclipse.swt.widgets.Text
	 */
	private Text createText(Composite parent) {
		Text text = new Text(parent, SWT.BORDER);
		GridData spec = new GridData();
		spec.grabExcessVerticalSpace = false;
		spec.grabExcessHorizontalSpace = true;
		spec.horizontalAlignment = GridData.FILL;
		spec.verticalAlignment = GridData.BEGINNING;
		text.setLayoutData(spec);
		Listener l = new Listener() {
			public void handleEvent(Event evt) {
				rematch(fText.getText());
			}
		};
		text.addListener(SWT.Modify, l);
		return text;
	}
	
	/**
	 * Creates the list widget and sets layout data.
	 * @return org.eclipse.swt.widgets.List
	 */
	private Table createUpperList(Composite parent) {
		if (fUpperListLabel != null)
			 (new Label(parent, SWT.NONE)).setText(fUpperListLabel);

		Table list = new Table(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		list.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event evt) {
				handleUpperSelectionChanged();
			}
		});
		list.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event evt) {
				handleUpperDoubleClick();
			}
		});
		list.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				fElementRenderer.dispose();
			}
		});
		GridData spec = new GridData();
		spec.widthHint = convertWidthInCharsToPixels(50);
		spec.heightHint = convertHeightInCharsToPixels(15);
		spec.grabExcessVerticalSpace = true;
		spec.grabExcessHorizontalSpace = true;
		spec.horizontalAlignment = GridData.FILL;
		spec.verticalAlignment = GridData.FILL;
		list.setLayoutData(spec);
		return list;
	}
	
	/**
	 * @return the ID of the button that is 'pressed' on doubleClick in the lists.
	 * By default it is the OK button.
	 * Override to change this setting.
	 */
	protected int getDefaultButtonID() {
		return IDialogConstants.OK_ID;
	}
	
	protected Object getWidgetSelection() {
		int i = fLowerList.getSelectionIndex();
		if (fQualifierMap != null) {
			if (fQualifierMap.length == 1)
				i = 0;
			if (i < 0) {
				return null;
			} 
			Integer index = fQualifierMap[i];
			return fElements[index.intValue()];
		}
		return null;
	}
	
	protected final void handleLowerDoubleClick() {
		if (getWidgetSelection() != null)
			buttonPressed(getDefaultButtonID());
	}
	
	protected final void handleLowerSelectionChanged() {
		updateOkState();
	}
	
	protected final void handleUpperDoubleClick() {
		if (getWidgetSelection() != null)
			buttonPressed(getDefaultButtonID());
	}
	
	protected final void handleUpperSelectionChanged() {
		int selection = fUpperList.getSelectionIndex();
		if (selection >= 0) {
			int i = fElementMap[selection];
			int k = i;
			int length = fRenderedStrings.length;
			while (k < length && fRenderedStrings[k].equals(fRenderedStrings[i])) {
				k++;
			}
			updateLowerListWidget(i, k);
		} else
			updateLowerListWidget(0, 0);
	}
	
	public int open() {

		if (fIT == null || fIT.length == 0) {
			MessageDialog.openInformation(getShell(), WebAppEditResourceHandler.getString("Empty_List_1"), Messages.No_portlets_exist_to_add); //$NON-NLS-1$
			return CANCEL;
		}

		setElements(fIT);
		setInitialSelection(""); //$NON-NLS-1$
		return super.open();
	}
	
	/**
	 * update the list to reflect a new match string.
	 * @param matchString java.lang.String
	 */
	protected final void rematch(String matchString) {
		int k = 0;
		String text = fText.getText();
		StringMatcher matcher = new StringMatcher(text + "*", fIgnoreCase, false); //$NON-NLS-1$
		String lastString = null;
		int length = fElements.length;
		for (int i = 0; i < length; i++) {
			while (i < length && fRenderedStrings[i].equals(lastString))
				i++;
			if (i < length) {
				lastString = fRenderedStrings[i];
				if (matcher.match(fRenderedStrings[i])) {
					fElementMap[k] = i;
					k++;
				}
			}
		}
		fElementMap[k] = -1;

		updateUpperListWidget(fElementMap, k);
	}
	
	/**
		 * 
		 * @return java.lang.String[]
		 * @param p org.eclipse.jface.elements.IIndexedProperty
		 */
	private String[] renderStrings(Object[] p) {
		String[] strings = new String[p.length];
		int size = strings.length;
		for (int i = 0; i < size; i++) {
			strings[i] = fElementRenderer.getText(p[i]);
		}
		new TwoArrayQuickSorter(fIgnoreCase).sort(strings, p);
		return strings;
	}

	public void setElements(Object[] elements) {
		fElements = elements;
		fElementMap = new int[fElements.length + 1];
		fRenderedStrings = renderStrings(fElements);
	}

	private void updateLowerListWidget(int from, int to) {
		fLowerList.removeAll();
		fQualifierMap = new Integer[to - from];
		String[] qualifiers = new String[to - from];
		for (int i = from; i < to; i++) {
			// XXX: 1G65LDG: JFUIF:WIN2000 - ILabelProvider used outside a viewer
			qualifiers[i - from] = fQualifierRenderer.getText(fElements[i]);
			fQualifierMap[i - from] = new Integer(i);
		}

		new TwoArrayQuickSorter(fIgnoreCase).sort(qualifiers, fQualifierMap);

		for (int i = 0; i < to - from; i++) {
			TableItem ti = new TableItem(fLowerList, i);
			ti.setText(qualifiers[i]);
			// XXX: 1G65LDG: JFUIF:WIN2000 - ILabelProvider used outside a viewer
			Image img = fQualifierRenderer.getImage(fElements[from + i]);
			if (img != null)
				ti.setImage(img);
		}

		if (fLowerList.getItemCount() > 0)
			fLowerList.setSelection(0);
		updateOkState();
	}
	
	private void updateOkState() {
		Button okButton = getOkButton();
		if (okButton != null)
			okButton.setEnabled(getWidgetSelection() != null);
	}
	
	private void updateUpperListWidget(int[] indices, int size) {
		fUpperList.setRedraw(false);
		int itemCount = fUpperList.getItemCount();
		if (size < itemCount)
			fUpperList.remove(0, itemCount - size - 1);
		TableItem[] items = fUpperList.getItems();
		for (int i = 0; i < size; i++) {
			TableItem ti = null;
			if (i < itemCount)
				ti = items[i];
			else
				ti = new TableItem(fUpperList, i);
			ti.setText(fRenderedStrings[indices[i]]);
			// XXX: 1G65LDG: JFUIF:WIN2000 - ILabelProvider used outside a viewer
			Image img = fElementRenderer.getImage(fElements[indices[i]]);
			if (img != null)
				ti.setImage(img);
		}
		if (fUpperList.getItemCount() > 0)
			fUpperList.setSelection(0);
		fUpperList.setRedraw(true);
		handleUpperSelectionChanged();
	}
	
	/**
	 * Sent when default selection occurs in the control.
	 * <p>
	 * For example, on some platforms default selection occurs
	 * in a List when the user double-clicks an item or types
	 * return in a Text.
	 * </p>
	 *
	 * @param e an event containing information about the default selection
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
		// Do nothing
	}
	
	/**
	 * Sent when selection occurs in the control.
	 * <p>
	 * For example, on some platforms selection occurs in
	 * a List when the user selects an item or items.
	 * </p>
	 *
	 * @param e an event containing information about the selection
	 */
	public void widgetSelected(SelectionEvent e) {
		
	}
	
	/**
	 * @see ElementTreeSelectionDialog#updateOKStatus()
	 */
	protected void updateOKStatus() {
		super.updateOKStatus();
		Button okButton = getOkButton();
		if (okButton != null)
			okButton.setEnabled(fLocalValidator.validate(getResult()).isOK());
	}

}
