package org.jboss.ide.eclipse.archives.ui.preferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IPreferenceManager;
import org.jboss.ide.eclipse.archives.core.model.IVariableProvider;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVFS;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariableManager;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVariableManager.DefaultVariableProvider;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.dialogs.ArchivesVariableDialog;

public class VariablesPreferencePage extends PropertyPage implements
		IWorkbenchPreferencePage {
	
	private Group variablesGroup;
	private TreeViewer variablesViewer;
	private Button add,edit,remove,moveUp,moveDown,enable,disable;
	private HashMap<IVariableProvider, Integer> newWeights;
	private HashMap<IVariableProvider, Boolean> newEnablement;
	
	public VariablesPreferencePage() {
		super();
		setTitle(ArchivesUIMessages.ArchivesVariables);
		setImageDescriptor(ArchivesSharedImages.getImageDescriptor(ArchivesSharedImages.IMG_PACKAGE));
		newWeights = new HashMap<IVariableProvider, Integer>();
		newEnablement = new HashMap<IVariableProvider, Boolean>();
	}

	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		createVariablesGroup(main);
		return main;
	}
	
	public IPreferenceManager getPrefManager() {
		return ArchivesCore.getInstance().getPreferenceManager();
	}
	
	protected void createVariablesGroup(Composite main) {
		variablesGroup = new Group(main, SWT.NONE);
		variablesGroup.setText(ArchivesUIMessages.VariablesGroup);
		variablesGroup.setLayout(new FormLayout());
		variablesGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		variablesViewer = new TreeViewer(variablesGroup);
		FormData fd = new FormData();
		fd.left = new FormAttachment(0,5);
		fd.top = new FormAttachment(0,5);
		fd.right = new FormAttachment(0,300);
		fd.bottom = new FormAttachment(80,0);
		variablesViewer.getTree().setLayoutData(fd);
		variablesViewer.setContentProvider(new VariablesContentProvider());
		variablesViewer.setLabelProvider(new VariablesLabelProvider());
		variablesViewer.setInput("");
		
		variablesViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				viewerSelectionChanged();
			} 
		});
		
		// buttons
		add = new Button(variablesGroup, SWT.NONE);
		edit = new Button(variablesGroup, SWT.NONE);
		remove = new Button(variablesGroup, SWT.NONE);
		moveUp = new Button(variablesGroup, SWT.NONE);
		moveDown = new Button(variablesGroup, SWT.NONE);
		enable = new Button(variablesGroup, SWT.NONE);
		disable = new Button(variablesGroup, SWT.NONE);
		add.setLayoutData(createButtonLayoutData(null));
		edit.setLayoutData(createButtonLayoutData(add));
		remove.setLayoutData(createButtonLayoutData(edit));
		moveUp.setLayoutData(createButtonLayoutData(remove));
		moveDown.setLayoutData(createButtonLayoutData(moveUp));
		enable.setLayoutData(createButtonLayoutData(moveDown));
		disable.setLayoutData(createButtonLayoutData(enable));
		add.setText(ArchivesUIMessages.Add);
		edit.setText(ArchivesUIMessages.Edit);
		remove.setText(ArchivesUIMessages.Remove);
		moveUp.setText(ArchivesUIMessages.MoveUp);
		moveDown.setText(ArchivesUIMessages.MoveDown);
		enable.setText(ArchivesUIMessages.Enable);
		disable.setText(ArchivesUIMessages.Disable);
		
		add.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				addPressed();
			} });
		edit.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				editPressed();
			} });
		remove.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				removePressed();
			} });

		moveUp.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				Object sel = getSelection();
				List<IVariableProvider> sorted = getSortedDelegates();
				int current = sorted.indexOf(sel);
				if( current > 0 ) {
					newWeights.put(sorted.get(current-1), new Integer(current));
					newWeights.put(sorted.get(current), new Integer(current-1));
				}
				refreshAll();
			} });

		moveDown.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				Object sel = getSelection();
				List<IVariableProvider> sorted = getSortedDelegates();
				int current = sorted.indexOf(sel);
				if( current < sorted.size()-1 ) {
					newWeights.put(sorted.get(current+1), new Integer(current));
					newWeights.put(sorted.get(current), new Integer(current+1));
				}
				refreshAll();
			} });

		
		enable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				Object sel = getSelection();
				if( sel instanceof IVariableProvider )
					newEnablement.put((IVariableProvider)sel, new Boolean(true));
				refreshAll();
			} });
		disable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				Object sel = getSelection();
				if( sel instanceof IVariableProvider )
					newEnablement.put((IVariableProvider)sel, new Boolean(false));
				refreshAll();
			} });
		
	}
	protected void addPressed() {
		ArchivesVariableDialog dialog= new ArchivesVariableDialog(getShell()); 
		if (dialog.open() == Window.OK) {
			String name= dialog.getName();
			String value = dialog.getValue();
			WorkspaceVariableManager mgr = ((WorkspaceVFS)ArchivesCore.getInstance().getVFS()).getVariableManager();
			mgr.setValue(name, value);
		}
		refreshAll();
	}
	protected void editPressed() {
		Object sel = getSelection();
		if( sel instanceof Wrapped && ((Wrapped)sel).getProvider().getId().equals(DefaultVariableProvider.ID)) {
			String name = ((Wrapped)sel).getName();
			String value = ((WorkspaceVFS)ArchivesCore.getInstance().getVFS()).getVariableManager().getVariableValue(name);
			ArchivesVariableDialog dialog= new ArchivesVariableDialog(getShell(), name, value); 
			if (dialog.open() == Window.OK) {
				name= dialog.getName();
				value = dialog.getValue();
				WorkspaceVariableManager mgr = ((WorkspaceVFS)ArchivesCore.getInstance().getVFS()).getVariableManager();
				mgr.setValue(name, value);
			}
		}
		refreshAll();
	}
	protected void removePressed() {
		Object sel = getSelection();
		if( sel instanceof Wrapped && ((Wrapped)sel).getProvider().getId().equals(DefaultVariableProvider.ID)) {
			String name = ((Wrapped)sel).getName();
			((WorkspaceVFS)ArchivesCore.getInstance().getVFS()).getVariableManager().setValue(name, null);
		}
			
		refreshAll();
	}

	protected void refreshAll() {
		variablesViewer.refresh();
		viewerSelectionChanged();
	}
	
	protected FormData createButtonLayoutData(Control top) {
		FormData fd = new FormData();
		if( top != null )
			fd.top = new FormAttachment(top, 5);
		else 
			fd.top = new FormAttachment(0,5);
		fd.left = new FormAttachment(variablesViewer.getTree(), 5);
		fd.right = new FormAttachment(100,-5);
		return fd;
	}

	protected class Wrapped {
		protected IVariableProvider p;
		protected String name;
		public Wrapped(IVariableProvider p, String name) {
			this.p = p;
			this.name = name;
		}
		public IVariableProvider getProvider() { return p; }
		public String getName() { return name; }
		public String toString() {
			return name + ": " + p.getVariableValue(name);
		}
	}
	
	protected class VariablesContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			if( parentElement instanceof IVariableProvider ) {
				String[] props = ((IVariableProvider)parentElement).getVariableNames();
				Wrapped[] items = new Wrapped[props.length];
				for( int i = 0; i < props.length; i++ ) 
					items[i] = new Wrapped((IVariableProvider)parentElement, props[i]);
				return items;
			}
			return null;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return element instanceof IVariableProvider && ((IVariableProvider)element).getVariableNames().length > 0;
		}

		public Object[] getElements(Object inputElement) {
			List<IVariableProvider> elements = getSortedDelegates();
			return (IVariableProvider[]) elements
					.toArray(new IVariableProvider[elements.size()]);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected List<IVariableProvider> getSortedDelegates() {
		WorkspaceVFS vfs = (WorkspaceVFS)ArchivesCore.getInstance().getVFS();
		WorkspaceVariableManager mgr = vfs.getVariableManager();
		IVariableProvider[] delegates = mgr.getDelegates();
		
		// sort with changed data
		List<IVariableProvider> l = Arrays.asList(delegates);
		Comparator<IVariableProvider> x = new Comparator<IVariableProvider>() {
			public int compare(IVariableProvider o1, IVariableProvider o2) {
				if( nowEnabled(o1) != nowEnabled(o2))
					return nowEnabled(o1) ? -1 : 1;

				// now weights
				if( nowWeight(o1) != nowWeight(o2) ) 
					return nowWeight(o1) > nowWeight(o2) ? 1 : -1;
				return 0;
			}
		};
		Collections.sort(l, x);
		return l;
	}
	protected boolean nowEnabled(IVariableProvider o) {
		boolean enabled = o.getEnabled();
		if( newEnablement.get(o) != null)
			enabled = newEnablement.get(o).booleanValue();
		return enabled;
	}
	protected int nowWeight(IVariableProvider o) {
		int weight = o.getWeight();
		if( newWeights.get(o) != null )
			weight = newWeights.get(o).intValue();
		return weight;
	}
	
	protected class VariablesLabelProvider extends BaseLabelProvider implements ILabelProvider {
		
		private HashMap<Image, Image> disabledImages;
		public VariablesLabelProvider() {
			disabledImages = new HashMap<Image, Image>();
		}
		public Image getImage(Object element) {
			if( element instanceof IVariableProvider ) {
				String id = ((IVariableProvider)element).getId();
				boolean enabled = nowEnabled((IVariableProvider)element);
				if(id.equals("org.jboss.ide.eclipse.archives.core.resourceVariableProvider"))
					return getImage2(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER), enabled);
				if( id.equals("org.jboss.ide.eclipse.archives.core.classpathVariableProvider"))
					return getImage2(JavaPlugin.getDefault().getImageRegistry().get(JavaPluginImages.IMG_OBJS_EXTJAR), enabled);
				if( id.equals("org.jboss.ide.eclipse.archives.core.stringReplacementValueVariables"))
					return getImage2(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_PASTE), enabled);
			}
			if( element instanceof Wrapped ) {
				return getImage(((Wrapped)element).getProvider());
			}
			return null;
		}
		protected Image getImage2(Image original, boolean enabled) {
			if( enabled ) 
				return original;
			if( disabledImages.get(original) == null ) {
				Image i2 = new DecorationOverlayIcon(original, JavaPluginImages.DESC_OVR_DEPRECATED, IDecoration.TOP_LEFT).createImage();
				disabledImages.put(original, i2);
			}
			return disabledImages.get(original);
		}
		public String getText(Object element) {
			if( element instanceof IVariableProvider ) {
				return ((IVariableProvider)element).getName();
			}
			if( element instanceof Wrapped )
				return ((Wrapped)element).toString();
			return "";
		}
	    public void dispose() {
	    	super.dispose();
	    	Iterator<Image> i = disabledImages.values().iterator();
	    	while(i.hasNext()) 
	    		i.next().dispose();
	    }
	}
	
	protected Object getSelection() {
		IStructuredSelection sel = (IStructuredSelection)variablesViewer.getSelection();
		return sel.getFirstElement();
	}
	
	protected void viewerSelectionChanged() {
		Object selected = getSelection();
		List<IVariableProvider> dels = getSortedDelegates();
		boolean variableProvider = selected != null && selected instanceof IVariableProvider;
		boolean defaultProvider = selected != null && 
			(variableProvider ? (IVariableProvider)selected : ((Wrapped)selected).getProvider())
				.getId().equals(WorkspaceVariableManager.DEFAULT_PROVIDER);
		boolean removable = !variableProvider && defaultProvider;
		boolean notLast = dels.indexOf(selected) != dels.size()-1;
		boolean notFirst = dels.indexOf(selected) != 0;
		boolean canMoveDown = variableProvider && notLast 
			&& nowEnabled(dels.get(dels.indexOf(selected)+1)) == nowEnabled((IVariableProvider)selected);
		boolean canMoveUp = variableProvider && notFirst 
			&& nowEnabled(dels.get(dels.indexOf(selected)-1)) == nowEnabled((IVariableProvider)selected);
		
		add.setEnabled(defaultProvider || selected == null);
		edit.setEnabled(removable);
		remove.setEnabled(removable);
		moveUp.setEnabled(variableProvider && canMoveUp);
		moveDown.setEnabled(variableProvider && canMoveDown);
		enable.setEnabled(variableProvider && !nowEnabled((IVariableProvider)selected));
		disable.setEnabled(variableProvider && nowEnabled((IVariableProvider)selected));
	}
	
	public void init(IWorkbench workbench) {
	}

	public void performDefaults() {
		List<IVariableProvider> dels = getSortedDelegates();
		IVariableProvider provider;
		for( int i = 0; i < dels.size(); i++ ) {
			provider = dels.get(i);
			newEnablement.put(provider, new Boolean(true));
			newWeights.put(provider, new Integer(provider.getDefaultWeight()));
		}
		refreshAll();
	}

	public boolean performOk() {
		List<IVariableProvider> dels = getSortedDelegates();
		IVariableProvider provider;
		for( int i = 0; i < dels.size(); i++ ) {
			provider = dels.get(i);
			if( newEnablement.get(provider) != null )
				provider.setEnabled(newEnablement.get(provider).booleanValue());
			if( newWeights.get(provider) != null )
				provider.setWeight(newWeights.get(provider).intValue());
		}
		return true;
	}
}
