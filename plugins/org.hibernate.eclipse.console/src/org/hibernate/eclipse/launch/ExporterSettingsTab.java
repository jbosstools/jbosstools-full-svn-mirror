/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.launch;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.AbstractTableViewer;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.hibernate.console.ImageConstants;
import org.hibernate.eclipse.console.ExtensionManager;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.model.impl.ExporterDefinition;
import org.hibernate.eclipse.console.model.impl.ExporterFactory;
import org.hibernate.eclipse.console.utils.EclipseImages;
import org.hibernate.eclipse.console.wizards.UpDownListComposite;

public class ExporterSettingsTab extends AbstractLaunchConfigurationTab {
	private Button enableEJB3annotations;

	private Button enableJDK5;

	private List selectedExporters;
	
	private Set deletedExporterIds;

	//private CheckboxTableViewer exporterTable;

	private Button selectAll;

	private Button deselectAll;

	private PropertySheetPage propertySheet;

	private Button add;

	private Button remove;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ExporterSettingsTab() {
		super();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		selectedExporters = new ArrayList();		
		deletedExporterIds = new HashSet();
		// ScrolledComposite scrolled = new ScrolledComposite(parent,
		// SWT.V_SCROLL | SWT.H_SCROLL);

		Composite container = new Composite( parent, SWT.NONE );
		GridData controlData = new GridData( GridData.FILL_BOTH );
		container.setLayoutData( controlData );

		GridLayout layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		layout.verticalSpacing = 1;
		container.setLayout( layout );

		createGeneralSettings( container );

		createExporterTable( container );

		createExporterProperties( container );

		dialogChanged();
		setControl( container );
	}

	private void createExporterProperties(Composite parent) {
		Composite exportersComposite = createComposite( parent, "Properties:" );

		exportersComposite.setLayout( new GridLayout( 2, false ) );

		GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
		gd.minimumHeight = 100;
		exportersComposite.setLayoutData( gd );

		Group gr = new Group(exportersComposite, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		
		gr.setLayout( gridLayout );		
		gd = new GridData( SWT.FILL, SWT.FILL, true, true );
		gd.verticalSpan = 2;		
		gr.setLayoutData( gd );

		Control sheet = createPropertySheet( gr );
		gd = new GridData( SWT.FILL, SWT.FILL, true, true );		
		sheet.setLayoutData( gd );
		
		add = new Button( exportersComposite, SWT.PUSH );
		add.setEnabled( false );
		add.setText( "Add..." );
		gd = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		gd.horizontalIndent = 5;
		add.setLayoutData( gd );
		add.addSelectionListener( new SelectionAdapter() {
		
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection ss = (IStructuredSelection) getExporterTable().getSelection();
				ExporterFactory ef = (ExporterFactory) ss.getFirstElement();

				if(ef!=null) {
					AddPropertyDialog dialog = new AddPropertyDialog(getShell(), ef);
					if(dialog.open()==Dialog.OK) {
						ef.setProperty( dialog.getPropertyName(), dialog.getPropertyValue() );
						dialogChanged();	
						refreshPropertySheet();
					}
				}
			}
		
		} );

		remove = new Button( exportersComposite, SWT.PUSH );
		remove.setText( "Remove..." );
		remove.setEnabled( false );
		remove.addSelectionListener( new SelectionAdapter() {
		
			public void widgetSelected(SelectionEvent e) {
				if(currentDescriptor!=null) {
					IStructuredSelection ss = (IStructuredSelection) getExporterTable().getSelection();
					ExporterFactory ef = (ExporterFactory) ss.getFirstElement();
					ef.removeProperty( (String) currentDescriptor.getId() );
					dialogChanged();
					refreshPropertySheet();
				}				
			}
		
		} );
		
		gd = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		gd.horizontalIndent = 5;
		remove.setLayoutData( gd );

	}

	public class MyPropertySheetEntry extends PropertySheetEntry {
		
		public IPropertyDescriptor getMyDescriptor() {
			return super.getDescriptor();
		}
		
		protected PropertySheetEntry createChildEntry() {
			return new MyPropertySheetEntry();
		}
	}
	
	// currently selected in the propertysheet
	private IPropertyDescriptor currentDescriptor;

	private UpDownListComposite exporterUpDown;

	private ObservableFactoryList observableFactoryList;

	
	
	private Control createPropertySheet(Composite exportersComposite) {
		propertySheet = new PropertySheetPage() {
			

			public void handleEntrySelection(ISelection selection) {
				super.handleEntrySelection( selection );
				IStructuredSelection iss = (IStructuredSelection) selection;
				if(iss.isEmpty()) {
					currentDescriptor = null;
				} else {
					MyPropertySheetEntry mse = (MyPropertySheetEntry)iss.getFirstElement();
					currentDescriptor = mse.getMyDescriptor();
				}
			}
		};
		
		propertySheet.createControl( exportersComposite );

		final PropertySheetEntry propertySheetEntry = new MyPropertySheetEntry();

		propertySheetEntry
				.setPropertySourceProvider( new IPropertySourceProvider() {

					public IPropertySource getPropertySource(Object object) {
						if ( object instanceof ExporterFactory ) {
							return new ExporterFactoryPropertySource(
									(ExporterFactory) object ) {
								public void setPropertyValue(Object id, Object value) {
									super.setPropertyValue( id, value );
									dialogChanged();
								}
							};
						}
						else {
							return null;
						}
					}
				} );
		propertySheet.setRootEntry( propertySheetEntry );
		// propertySheetEntry.setValues( new Object[] { this });
		
		getExporterTable()
				.addSelectionChangedListener( new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection s = (IStructuredSelection) event
						.getSelection();
						if(s.isEmpty()) {
							if(add!=null) add.setEnabled( false );
							if(remove!=null) remove.setEnabled( false );	
							
							propertySheetEntry.setValues(new Object[0]);
					
						} else {
							if(add!=null) add.setEnabled( true );
							if(remove!=null) remove.setEnabled( true );		
					
							ExporterFactory ep = (ExporterFactory) s
							.getFirstElement();
							propertySheetEntry.setValues( new Object[] { ep } );
							// if(ep.isEnabled( configuration ))
						}
					}

				} );

		return propertySheet.getControl();
	}

	private void createExporterTable(Composite parent) {
		exporterUpDown = new UpDownListComposite(parent, SWT.NONE, "Exporters:", true, new ExporterLabelProvider(), new ExporterContentProvider()) {
			
			protected Object[] handleAdd(int idx) {
				
				switch (idx) {
				case 0:
					Object[] selectExporters = selectExporters(getShell(), "Add exporter", "Select the exporter(s) you want to add");
					for (int i = 0; i < selectExporters.length; i++) {
						ExporterDefinition exporterDefinition = (ExporterDefinition) selectExporters[i];
						addDef(exporterDefinition);						
					}
					return new Object[0];// { exporterFactory };	
				case 1:
					getExporterTable().setAllChecked( true );
					break;
				case 2:
					getExporterTable().setAllChecked( false );
					break;
				default:
					break;
				}
				return null;
			}

			private void addDef(ExporterDefinition expDef) {
				int initialCount = getTable().getItemCount();
				boolean duplicate = false;
				do {
					duplicate = false;
					initialCount++;
					Iterator iterator = observableFactoryList.getList().iterator();					
					while(iterator.hasNext()) {
						ExporterFactory def = (ExporterFactory) iterator.next();						
						if(def.getId().equals(""+initialCount)) {
							duplicate = true;						     
						}						
					}
				} while(duplicate);
				
				String initialName = "" + initialCount;
				
				ExporterFactory exporterFactory = new ExporterFactory( expDef, initialName );
				observableFactoryList.add(exporterFactory);
				((CheckboxTableViewer)getTableViewer()).setChecked(exporterFactory, true);
			}

			protected void handleRemove() {
					IStructuredSelection selection = (IStructuredSelection) getTableViewer().getSelection();
					if (selection != null) {
						int numSelected= selection.size();
						
						Iterator iterator= selection.iterator();
						while (iterator.hasNext() ) {
							Object item= iterator.next();
							observableFactoryList.remove((ExporterFactory)item);
							deletedExporterIds.add(((ExporterFactory)item).getId());
						}
						//getTableViewer().setSelection(StructuredSelection.EMPTY);
						listChanged();
					}		
			}
			
			protected void moveSelectionDown() {
				Table table = getTableViewer().getTable();
				int indices[]= table.getSelectionIndices();
				if (indices.length < 1) {
					return;
				}
				int newSelection[]= new int[indices.length];
				int max= table.getItemCount() - 1;
				for (int i = indices.length - 1; i >= 0; i--) {
					int index= indices[i];
					if (index < max) {
						ExporterFactory data = (ExporterFactory) getTableViewer().getElementAt(index);						
						observableFactoryList.moveTo(index + 1, (ExporterFactory)data);						
						newSelection[i]= index + 1;
					}
				}
				table.setSelection(newSelection);
				listChanged();
			}

			protected void moveSelectionUp() {
				Table table = getTableViewer().getTable();
				int indices[]= table.getSelectionIndices();
				int newSelection[]= new int[indices.length];
				for (int i = 0; i < indices.length; i++) {
					int index= indices[i];
					if (index > 0) {
						ExporterFactory data = (ExporterFactory) getTableViewer().getElementAt(index);						
						observableFactoryList.moveTo(index - 1, (ExporterFactory)data);						
						newSelection[i]= index - 1;
					}
				}
				table.setSelection(newSelection);
				listChanged();
			}

			protected String[] getAddButtonLabels() {
				return new String[] { "Add...", "Select all", "Deselect all" };				
			}
			protected void listChanged() {
				dialogChanged();
			}
		};

		getExporterTable().addCheckStateListener( new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {

				ExporterFactory factory = (ExporterFactory) event.getElement();

				if ( !event.getChecked()
						&& selectedExporters.contains( factory ) ) {
					selectedExporters.remove( factory );
				}
				else if ( event.getChecked()
						&& !selectedExporters.contains( factory ) ) {
					selectedExporters.add( factory );
				}

				dialogChanged();
			}
		} );

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalAlignment = GridData.FILL;
		exporterUpDown.setLayoutData( gd );
	}
	
	private void createOldExporterTable(Composite parent) {
		Composite exporterOptions = createComposite( parent, "Exporters:" );

		GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
		gd.minimumHeight = 100;
		exporterOptions.setLayoutData( gd );

		exporterOptions.setLayout( new GridLayout( 2, false ) );

		Table table = new Table( exporterOptions, SWT.CHECK | SWT.BORDER
				| SWT.V_SCROLL );
		//setExporterTable(new CheckboxTableViewer( table ));
		getExporterTable().setContentProvider( new ExporterContentProvider() );
		getExporterTable().setLabelProvider( new ExporterLabelProvider() );
		
		// exporterTable.getControl().setLayoutData(
		// new GridData( SWT.FILL, SWT.FILL, true, true ) );
		getExporterTable().setColumnProperties( new String[] { "", "Description" } );
		getExporterTable().addCheckStateListener( new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {

				ExporterFactory factory = (ExporterFactory) event.getElement();

				if ( !event.getChecked()
						&& selectedExporters.contains( factory ) ) {
					selectedExporters.remove( factory );
				}
				else if ( event.getChecked()
						&& !selectedExporters.contains( factory ) ) {
					selectedExporters.add( factory );
				}

				dialogChanged();
			}
		} );

		gd = new GridData( SWT.FILL, SWT.FILL, true, true );
		gd.verticalSpan = 2;
		gd.horizontalSpan = 1;
		table.setLayoutData( gd );

		selectAll = new Button( exporterOptions, SWT.PUSH );
		selectAll.setText( "Select All" );
		selectAll.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getExporterTable().setAllChecked( true );
				dialogChanged();
			}
		} );
		gd = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		gd.horizontalIndent = 5;
		selectAll.setLayoutData( gd );

		deselectAll = new Button( exporterOptions, SWT.PUSH );
		deselectAll.setText( "Deselect All" );
		deselectAll.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getExporterTable().setAllChecked( false );
				dialogChanged();
			}
		} );

		gd = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		gd.horizontalIndent = 5;
		deselectAll.setLayoutData( gd );

	}

	private void createGeneralSettings(Composite parent) {

		SelectionListener fieldlistener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected( e );
			}

			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		};
		Composite generalSettingsComposite = createComposite( parent,
				"General settings:" );
		generalSettingsComposite.setLayoutData( new GridData( SWT.BEGINNING,
				SWT.BEGINNING, false, false ) );

		enableJDK5 = new Button( generalSettingsComposite, SWT.CHECK );
		enableJDK5.setText( "Use Java 5 syntax" );
		enableJDK5.addSelectionListener( fieldlistener );

		enableEJB3annotations = new Button( generalSettingsComposite, SWT.CHECK );
		enableEJB3annotations.setText( "Generate EJB3 annotations" );
		enableEJB3annotations.addSelectionListener( fieldlistener );
	}

	private class ExporterContentProvider implements IStructuredContentProvider, PropertyChangeListener {

		private AbstractTableViewer viewer;

		public Object[] getElements(Object inputElement) {
			return ((ObservableFactoryList) inputElement ).getList().toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.viewer = (AbstractTableViewer) viewer;
			ObservableFactoryList ol = (ObservableFactoryList) oldInput;
			ObservableFactoryList newList = (ObservableFactoryList) newInput;
			
			if(ol!=null) {
				ol.removePropertyChangeListener(this);
			}
			
			if(newList!=null) {
				newList.addPropertyChangeListener(this);
			}
		}

		public void propertyChange(PropertyChangeEvent evt) {
			
			if("addElement".equals(evt.getPropertyName())) {
				viewer.add(evt.getNewValue());
			}
			
			if("removeElement".equals(evt.getPropertyName())) {
				viewer.remove(evt.getOldValue());
			}
			
			if("insertElement".equals(evt.getPropertyName())) {
				viewer.insert(evt.getNewValue(), ((Integer)evt.getOldValue()).intValue());
			}
		}

	}
	
	// Complete hack to get table to work with arbitrary exporters quickly. 
	private class ObservableFactoryList {
		
		List underlyingList = new ArrayList();
		
		PropertyChangeSupport pcs = new PropertyChangeSupport(this);
		
		public ObservableFactoryList(List exporterFactories) {
			underlyingList = exporterFactories;
		}

		public void moveTo(int i, ExporterFactory data) {
			underlyingList.remove((ExporterFactory) data);
			remove(data);
			underlyingList.add(i, data);
			pcs.firePropertyChange("insertElement", new Integer(i), data);
			
		}

		void addPropertyChangeListener(PropertyChangeListener pcl) {
			pcs.addPropertyChangeListener(pcl);		
		}
		
		public List getList() {
			return Collections.unmodifiableList(underlyingList);
		}

		void removePropertyChangeListener(PropertyChangeListener pcl) {
			pcs.removePropertyChangeListener(pcl);
		}
		
		boolean add(ExporterFactory o) {
			boolean changed = underlyingList.add(o);
			pcs.firePropertyChange("addElement", null, o);
			return changed;
		}

		boolean remove(ExporterFactory o) {
			boolean changed = underlyingList.remove(o);
			pcs.firePropertyChange("removeElement", o, null);
			return changed;
		}
		
		
	}

	static private class ExporterLabelProvider implements ITableLabelProvider, ILabelProvider {
		Map exp2img = new HashMap(); // not the most optimized but better
		// than having a finalize method.

		public Image getColumnImage(Object element, int columnIndex) {
			ExporterDefinition definition = getExporterDefinition(element);
			Image image = (Image) exp2img.get( definition.getId() );
			if ( image == null ) {
				image = definition.getIconDescriptor().createImage();
				exp2img.put( definition.getId(), image );
			}
			return image;
		}

		private ExporterDefinition getExporterDefinition(Object element) {
			if(element instanceof ExporterFactory) {
				ExporterFactory ef = (ExporterFactory) element;
				return ef.getExporterDefinition();
			} else {
				return (ExporterDefinition) element;
			}			
		}

		public String getColumnText(Object element, int columnIndex) {
			ExporterDefinition definition = getExporterDefinition(element);
			return definition.getDescription();
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {

			Iterator iterator = exp2img.values().iterator();
			while ( iterator.hasNext() ) {
				Image img = (Image) iterator.next();
				if ( img != null ) {
					img.dispose();
				}
			}
		}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

		public Image getImage(Object element) {
			return getColumnImage(element, 0);
		}

		public String getText(Object element) {
			return getColumnText(element, 0);
		}

	}

	private Composite createComposite(Composite parent, String name) {

		new Label( parent, SWT.NONE ).setText( name );
		Composite client = new Composite( parent, SWT.NONE );
		client.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
		client.setLayout( new GridLayout() );
		// client.setBackground( ColorConstants.cyan );
		return client;
	}

	private void dialogChanged() {
		boolean configSelected = true; // TODO: only active if configname in
		// settings
		// ...getConfigurationName().length()==0;

		if ( !configSelected ) {
			updateStatus( "Console configuration must be specified" );
			return;
		}

		if ( selectedExporters.size() == 0 ) {
			updateStatus( "At least one exporter option must be selected" );
			return;
		}
		
		
		// hard-coded checks: this should be delegated to extension point that knows about the different exporters.
		Iterator iterator = observableFactoryList.getList().iterator();
		while (iterator.hasNext()) {
			ExporterFactory ef = (ExporterFactory) iterator.next();
			String str = (String) ef.getProperties().get("outputdir");
			String msg = null;
			if(str!=null) {
				msg = PathHelper.checkDirectory(PathHelper.pathOrNull(str), "Output directory for " + ef.getExporterDefinition().getDescription(), false);
				if(msg!=null) {
					updateStatus(msg);
					return;
				}
			}

			str = (String) ef.getProperties().get("template_path");
			if(str!=null) {				
				msg = PathHelper.checkDirectory(PathHelper.pathOrNull(str), "Template directory for " + ef.getExporterDefinition().getDescription(), true);
				if(msg!=null) {
					updateStatus(msg);
					return;
				}
			}
			
		}
		updateStatus( null );
	}

	protected String checkDirectory(IPath path, String name) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				path );
		if ( res != null ) {
			int resType = res.getType();
			if ( resType == IResource.PROJECT || resType == IResource.FOLDER ) {
				IProject proj = res.getProject();
				if ( !proj.isOpen() ) {
					return "Project for " + name + " is closed";
				}
			}
			else {
				return name + " has to be a folder or project";
			}
		}
		else {
			return name + " does not exist";
		}
		return null;
	}

	protected String checkFile(IPath path, String name) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				path );
		if ( res != null ) {
			int resType = res.getType();
			if ( resType == IResource.FILE ) {
				return null;
			}
			else {
				return name + " must be a file";
			}
		}
		else {
			return name + " does not exist";
		}
	}

	private void updateStatus(String message) {
		setErrorMessage( message );
		updateLaunchConfigurationDialog();
	}

	private Path pathOrNull(String p) {
		if ( p == null || p.trim().length() == 0 ) {
			return null;
		}
		else {
			return new Path( p );
		}
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// ExporterAttributes tmpAttrs = new ExporterAttributes();
		// tmpAttrs.setEnableAllExporters(true);
		// tmpAttrs.save(configuration);
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			ExporterAttributes attributes = new ExporterAttributes(
					configuration );
			selectedExporters.clear();

			enableEJB3annotations.setSelection( attributes.isEJB3Enabled() );
			enableJDK5.setSelection( attributes.isJDK5Enabled() );

			List exporterFactories = attributes.getExporterFactories();
			observableFactoryList = new ObservableFactoryList(exporterFactories);
			getExporterTable().setInput( observableFactoryList );
			for (Iterator iter = exporterFactories.iterator(); iter.hasNext();) {
				ExporterFactory exporterFactory = (ExporterFactory) iter.next();
				if ( exporterFactory.isEnabled() ) {
					getExporterTable().setChecked( exporterFactory, true );
					selectedExporters.add( exporterFactory );
				}
				else {
					getExporterTable().setChecked( exporterFactory, false );
				}
			}

			refreshPropertySheet();
			
			dialogChanged();

		}
		catch (CoreException ce) {
			HibernateConsolePlugin
					.getDefault()
					.logErrorMessage(
							"Problem when reading hibernate tools launch configuration",
							ce );
		}
	}

	private void refreshPropertySheet() {
		getExporterTable().setSelection( getExporterTable().getSelection() ); // here to make sure the dependent propertysheet actually will reread what ever the selection is.
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(
				HibernateLaunchConstants.ATTR_ENABLE_EJB3_ANNOTATIONS,
				enableEJB3annotations.getSelection() );
		configuration.setAttribute( HibernateLaunchConstants.ATTR_ENABLE_JDK5,
				enableJDK5.getSelection() );

		List exporterFactories = ((ObservableFactoryList)getExporterTable().getInput()).getList();
		ExporterAttributes.saveExporterFactories(configuration, exporterFactories, selectedExporters, deletedExporterIds);
				
		deletedExporterIds.clear();
	}

	

	public String getName() {
		return "Exporters";
	}

	public Image getImage() {
		return EclipseImages.getImage( ImageConstants.MINI_HIBERNATE );
	}

	private CheckboxTableViewer getExporterTable() {
		return (CheckboxTableViewer) exporterUpDown.getTableViewer();
	}

	public static Object[] selectExporters(Shell shell, String title, String description) {
		ILabelProvider labelProvider= new ExporterLabelProvider();
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(shell, labelProvider);
		dialog.setTitle(title);
		dialog.setMessage(description); 
		dialog.setElements(ExtensionManager.findExporterDefinitionsAsMap().values().toArray());
		dialog.setMultipleSelection(true);
		
		if (dialog.open() == Window.OK) {			
			return dialog.getResult();
		} else {			
			return new ExporterDefinition[0];
		}
	}
}
