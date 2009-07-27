/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.configuration.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.action.CopyAction;
import org.eclipse.emf.edit.ui.action.CreateSiblingAction;
import org.eclipse.emf.edit.ui.action.CutAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.action.PasteAction;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.actions.AddSmooksResourceAction;
import org.jboss.tools.smooks.configuration.actions.ValidateSmooksAction;
import org.jboss.tools.smooks.configuration.editors.actions.Calc11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Database11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Datasources11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.FragmentRouting11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.FragmentRouting12ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.JavaBean11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.JavaBean12ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.PersistenceActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Reader11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Reader12ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Scripting11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.SeparatorActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Templating11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Validation10ActionGrouper;
import org.jboss.tools.smooks.model.medi.EdiMap;
import org.jboss.tools.smooks.model.medi.MEdiFactory;
import org.jboss.tools.smooks.model.medi.MEdiPackage;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksFactory;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * This is the action bar contributor for the Smooks model editor. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class SmooksActionBarContributor extends EditingDomainActionBarContributor implements ISelectionChangedListener {

	protected ISelection selection;
	/**
	 * This keeps track of the active editor. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected IEditorPart activeEditorPart;

	/**
	 * This keeps track of the current selection provider. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ISelectionProvider selectionProvider;

	/**
	 * This action opens the Properties view. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected IAction showPropertiesViewAction = new Action("Show Properties") {
		@Override
		public void run() {
			try {
				getPage().showView("org.eclipse.ui.views.PropertySheet");
			} catch (PartInitException exception) {
				SmooksConfigurationActivator.getDefault().log(exception);
			}
		}
	};

	protected IAction addSmooks11ResourceListAction = new Action("Add Smooks Resource List") {

		@Override
		public void run() {
			addSmooks11ResourceList();
		}

	};

	protected IAction addSmooks10ResourceListAction = new Action("Add Smooks Resource List") {

		@Override
		public void run() {
			addSmooks10ResourceList();
		}

	};

	protected IAction addMap10ResourceListAction = new Action("Add MappNode") {

		@Override
		public void run() {
			addMapNode();
		}

	};

	/**
	 * This action refreshes the viewer of the current editor if the editor
	 * implements {@link org.eclipse.emf.common.ui.viewer.IViewerProvider}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IAction refreshViewerAction = new Action("Refresh Viewer") {
		@Override
		public boolean isEnabled() {
			return activeEditorPart instanceof IViewerProvider;
		}

		@Override
		public void run() {
			if (activeEditorPart instanceof IViewerProvider) {
				Viewer viewer = ((IViewerProvider) activeEditorPart).getViewer();
				if (viewer != null) {
					viewer.refresh();
				}
			}
		}
	};

	/**
	 * This will contain one
	 * {@link org.eclipse.emf.edit.ui.action.CreateChildAction} corresponding to
	 * each descriptor generated for the current selection by the item provider.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<IAction> createChildActions;

	/**
	 * This is the menu manager into which menu contribution items should be
	 * added for CreateChild actions. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected IMenuManager createChildMenuManager;

	/**
	 * This will contain one
	 * {@link org.eclipse.emf.edit.ui.action.CreateSiblingAction} corresponding
	 * to each descriptor generated for the current selection by the item
	 * provider. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<IAction> createSiblingActions;

	/**
	 * This is the menu manager into which menu contribution items should be
	 * added for CreateSibling actions. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected IMenuManager createSiblingMenuManager;

	private ValidateSmooksAction validateSmooksAction;

	/**
	 * This creates an instance of the contributor. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public SmooksActionBarContributor() {
		super(ADDITIONS_LAST_STYLE);
		// loadResourceAction = new LoadResourceAction();
		validateAction = new ValidateAction();
		// controlAction = new ControlAction();
	}

	protected void addMapNode() {
		SmooksMultiFormEditor formEditor = (SmooksMultiFormEditor) this.getActiveEditor();
		EObject model = formEditor.getSmooksModel();
		if (model instanceof org.jboss.tools.smooks.model.medi.DocumentRoot) {
			EdiMap mappingNode = MEdiFactory.eINSTANCE.createEdiMap();
			Command command = AddCommand.create(formEditor.getEditingDomain(), model, MEdiPackage.eINSTANCE
					.getMappingNode(), mappingNode);
			formEditor.getEditingDomain().getCommandStack().execute(command);
		}
	}

	protected void addSmooks10ResourceList() {
		SmooksMultiFormEditor formEditor = (SmooksMultiFormEditor) this.getActiveEditor();
		EObject model = formEditor.getSmooksModel();
		if (model instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
			org.jboss.tools.smooks10.model.smooks.SmooksResourceListType resourceList = org.jboss.tools.smooks10.model.smooks.SmooksFactory.eINSTANCE
					.createSmooksResourceListType();
			Command command = AddCommand.create(formEditor.getEditingDomain(), model,
					org.jboss.tools.smooks10.model.smooks.SmooksPackage.eINSTANCE.getSmooksResourceListType(),
					resourceList);
			formEditor.getEditingDomain().getCommandStack().execute(command);
		}
	}

	protected void addSmooks11ResourceList() {
		SmooksMultiFormEditor formEditor = (SmooksMultiFormEditor) this.getActiveEditor();
		EObject model = formEditor.getSmooksModel();
		if (model instanceof DocumentRoot) {
			SmooksResourceListType resourceList = SmooksFactory.eINSTANCE.createSmooksResourceListType();
			Command command = AddCommand.create(formEditor.getEditingDomain(), model, SmooksPackage.eINSTANCE
					.getSmooksResourceListType(), resourceList);
			formEditor.getEditingDomain().getCommandStack().execute(command);
		}
	}

	@Override
	public void update() {
		super.update();
	}

	/**
	 * This adds Separators for editor additions to the tool bar. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(new Separator("smooks-settings"));
		toolBarManager.add(new Separator("smooks-additions"));
	}

	/**
	 * This adds to the menu bar a menu and some separators for editor
	 * additions, as well as the sub-menus for object creation items. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		super.contributeToMenu(menuManager);

		IMenuManager submenuManager = new MenuManager("Smooks", "smooksMenuID");
		menuManager.insertAfter("additions", submenuManager);
		submenuManager.add(new Separator("settings"));
		submenuManager.add(new Separator("actions"));
		submenuManager.add(new Separator("additions"));
		submenuManager.add(new Separator("additions-end"));

		// Prepare for CreateChild item addition or removal.
		//
		createChildMenuManager = new MenuManager("Add Smooks Resource");
		submenuManager.insertBefore("additions", createChildMenuManager);

		// Prepare for CreateSibling item addition or removal.
		//
		createSiblingMenuManager = new MenuManager("New Sibling");
		submenuManager.insertBefore("additions", createSiblingMenuManager);

		submenuManager.insertBefore("additions", addSmooks11ResourceListAction);

		// Force an update because Eclipse hides empty menus now.
		//
		submenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuManager) {
				menuManager.updateAll(true);
			}
		});

		addGlobalActions(submenuManager);
	}

	/**
	 * When the active editor changes, this remembers the change and registers
	 * with it as a selection provider. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		activeEditorPart = part;

		// Switch to the new selection provider.
		//
		if (selectionProvider != null) {
			selectionProvider.removeSelectionChangedListener(this);
		}
		if (part == null) {
			selectionProvider = null;
		} else {
			selectionProvider = part.getSite().getSelectionProvider();
			selectionProvider.addSelectionChangedListener(this);

			// Fake a selection changed event to update the menus.
			//
			if (selectionProvider.getSelection() != null) {
				selectionChanged(new SelectionChangedEvent(selectionProvider, selectionProvider.getSelection()));
			}
		}
	}

	@Override
	public void init(IActionBars actionBars) {
		super.init(actionBars);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

		validateSmooksAction = new ValidateSmooksAction();
		validateSmooksAction.setText("Validate");

		cutAction = new CutAction() {

			public void runWithEvent(Event event) {
				Widget widget = event.widget;
				if (widget instanceof Text) {
					((Text) widget).cut();
					return;
				}
				if (widget instanceof Combo) {
					((Combo) widget).cut();
					return;
				}
				super.runWithEvent(event);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.Action#isEnabled()
			 */
			@Override
			public boolean isEnabled() {
				return super.isEnabled();
			}

		};
		cutAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);

		copyAction = new CopyAction() {

			public void runWithEvent(Event event) {
				Widget widget = event.widget;
				if (widget instanceof Text) {
					((Text) widget).copy();
					return;
				}
				if (widget instanceof Combo) {
					((Combo) widget).copy();
					return;
				}
				super.runWithEvent(event);
			}

		};
		copyAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

		pasteAction = new PasteAction() {

			public void runWithEvent(Event event) {
				Widget widget = event.widget;
				if (widget instanceof Text) {
					((Text) widget).paste();
					return;
				}
				if (widget instanceof Combo) {
					((Combo) widget).paste();
					return;
				}
				super.runWithEvent(event);
			}

			@Override
			public boolean updateSelection(IStructuredSelection selection) {
				super.updateSelection(selection);
				return true;
			}

		};
		pasteAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);

		undoAction = new UndoAction();
		undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		redoAction = new RedoAction();
		redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

	}

	/**
	 * This implements
	 * {@link org.eclipse.jface.viewers.ISelectionChangedListener}, handling
	 * {@link org.eclipse.jface.viewers.SelectionChangedEvent}s by querying for
	 * the children and siblings that can be added to the selected object and
	 * updating the menus accordingly. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		// Remove any menu items for old selection.
		//
		if (createChildMenuManager != null) {
			depopulateManager(createChildMenuManager, createChildActions);
		}
		if (createSiblingMenuManager != null) {
			depopulateManager(createSiblingMenuManager, createSiblingActions);
		}

		// Query the new selection for appropriate new child/sibling descriptors
		//
		Collection<?> newChildDescriptors = null;
		// Collection<?> newSiblingDescriptors = null;

		ISelection selection = event.getSelection();
		this.selection = selection;
		if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1) {
			Object object = ((IStructuredSelection) selection).getFirstElement();
			object = AdapterFactoryEditingDomain.unwrap(object);
			EditingDomain domain = ((IEditingDomainProvider) activeEditorPart).getEditingDomain();

			newChildDescriptors = domain.getNewChildDescriptors(object, null);

			validateSmooksAction.setResource(domain.getResourceSet().getResources().get(0));
			validateSmooksAction.setEditingDomain(domain);
			// newSiblingDescriptors = domain.getNewChildDescriptors(null,
			// object);
		}

		// Generate actions for selection; populate and redraw the menus.
		//
		createChildActions = generateCreateChildActions(newChildDescriptors, selection);
		// createSiblingActions =
		// generateCreateSiblingActions(newSiblingDescriptors, selection);

		if (createChildMenuManager != null) {
			populateManager(createChildMenuManager, createChildActions, null);
			createChildMenuManager.update(true);
		}
		if (createSiblingMenuManager != null) {
			populateManager(createSiblingMenuManager, createSiblingActions, null);
			createSiblingMenuManager.update(true);
		}
	}

	/**
	 * This generates a {@link org.eclipse.emf.edit.ui.action.CreateChildAction}
	 * for each object in <code>descriptors</code>, and returns the collection
	 * of these actions. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<IAction> generateCreateChildActions(Collection<?> descriptors, ISelection selection) {
		Collection<IAction> actions = new ArrayList<IAction>();
		// if (selection != null && selection.isEmpty() && descriptors != null)
		// {
		// CommandParameter cp =
		// createChildParameter(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE,
		// SmooksFactory.eINSTANCE.createSmooksResourceListType());
		// CommandParameter cp2 =
		// createChildParameter(EdiPackage.Literals.EDI_MAP,
		// EdiFactory.eINSTANCE.createEdiMap());
		// descriptors.add(cp);
		// descriptors.add(cp2);
		// }
		if (descriptors != null) {
			for (Object descriptor : descriptors) {
				actions.add(new AddSmooksResourceAction(activeEditorPart, selection, descriptor));
			}
		}
		return actions;
	}

	protected CommandParameter createChildParameter(Object feature, Object child) {
		return new CommandParameter(null, feature, child);
	}

	/**
	 * This generates a
	 * {@link org.eclipse.emf.edit.ui.action.CreateSiblingAction} for each
	 * object in <code>descriptors</code>, and returns the collection of these
	 * actions. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<IAction> generateCreateSiblingActions(Collection<?> descriptors, ISelection selection) {
		Collection<IAction> actions = new ArrayList<IAction>();
		if (descriptors != null) {
			for (Object descriptor : descriptors) {
				actions.add(new CreateSiblingAction(activeEditorPart, selection, descriptor));
			}
		}
		return actions;
	}

	/**
	 * This populates the specified <code>manager</code> with
	 * {@link org.eclipse.jface.action.ActionContributionItem}s based on the
	 * {@link org.eclipse.jface.action.IAction}s contained in the
	 * <code>actions</code> collection, by inserting them before the specified
	 * contribution item <code>contributionID</code>. If
	 * <code>contributionID</code> is <code>null</code>, they are simply added.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void populateManager(IContributionManager manager, Collection<? extends IAction> actions,
			String contributionID) {
		if (actions != null) {
			for (IAction action : actions) {
				if (contributionID != null) {
					manager.insertBefore(contributionID, action);
				} else {
					manager.add(action);
				}
			}
		}
	}

	/**
	 * This removes from the specified <code>manager</code> all
	 * {@link org.eclipse.jface.action.ActionContributionItem}s based on the
	 * {@link org.eclipse.jface.action.IAction}s contained in the
	 * <code>actions</code> collection. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void depopulateManager(IContributionManager manager, Collection<? extends IAction> actions) {
		if (actions != null) {
			IContributionItem[] items = manager.getItems();
			for (int i = 0; i < items.length; i++) {
				// Look into SubContributionItems
				//
				IContributionItem contributionItem = items[i];
				while (contributionItem instanceof SubContributionItem) {
					contributionItem = ((SubContributionItem) contributionItem).getInnerItem();
				}

				// Delete the ActionContributionItems with matching action.
				//
				if (contributionItem instanceof ActionContributionItem) {
					IAction action = ((ActionContributionItem) contributionItem).getAction();
					if (actions.contains(action)) {
						manager.remove(contributionItem);
					}
				}
			}
		}
	}

	/**
	 * This populates the pop-up menu before it appears. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void menuAboutToShow(IMenuManager menuManager) {
		menuManager.add(new Separator("edit"));
		menuManager.add(new ActionContributionItem(undoAction));
		menuManager.add(new ActionContributionItem(redoAction));
		menuManager.add(new Separator());
		menuManager.add(new ActionContributionItem(deleteAction));
		menuManager.add(new Separator());
		menuManager.add(new ActionContributionItem(cutAction));
		menuManager.add(new ActionContributionItem(copyAction));
		menuManager.add(new ActionContributionItem(pasteAction));
		menuManager.add(new Separator());

		// if ((style & ADDITIONS_LAST_STYLE) != 0)
		// {
		// menuManager.add(new Separator("additions"));
		// menuManager.add(new Separator());
		// }
		// Add our other standard marker.
		//
		menuManager.add(new Separator("additions-end"));
		MenuManager submenuManager = null;

		updateRootElementAddAction();

		if (addSmooks11ResourceListAction.isEnabled()) {
			menuManager.insertBefore("edit", addSmooks11ResourceListAction);
		}

		if (addSmooks10ResourceListAction.isEnabled()) {
			menuManager.insertBefore("edit", addSmooks10ResourceListAction);
		}

		if (addMap10ResourceListAction.isEnabled()) {
			menuManager.insertBefore("edit", addMap10ResourceListAction);
		}

		submenuManager = new MenuManager("Add Smooks Resource");
		if (isSmooksResourceListElement()) {
			groupActions(submenuManager, createChildActions);
		} else {
			populateManager(submenuManager, createChildActions, null);
		}
		menuManager.insertBefore("edit", submenuManager);

		submenuManager = new MenuManager("Create Sibling");
		populateManager(submenuManager, createSiblingActions, null);
		menuManager.insertBefore("edit", submenuManager);
		// don't show properties that
		// menuManager.insertAfter("additions-end", showPropertiesViewAction);
		menuManager.insertAfter("additions-end", validateSmooksAction);
		this.addGlobalActions(menuManager);
	}

	private boolean isSmooksResourceListElement() {
		if (this.selection != null && selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof SmooksResourceListType) {
				return true;
			}
		}
		return false;
	}

	private List<ISmooksActionGrouper> getSmooksActionGrouper() {
		List<ISmooksActionGrouper> grouperList = new ArrayList<ISmooksActionGrouper>();

		grouperList.add(new JavaBean11ActionGrouper());
		grouperList.add(new Reader11ActionGrouper());
		grouperList.add(new Calc11ActionGrouper());
		grouperList.add(new Database11ActionGrouper());
		grouperList.add(new Datasources11ActionGrouper());
		grouperList.add(new FragmentRouting11ActionGrouper());
		grouperList.add(new Scripting11ActionGrouper());
		grouperList.add(new Templating11ActionGrouper());
		grouperList.add(new SeparatorActionGrouper("V1.1-V1.2"));
		grouperList.add(new JavaBean12ActionGrouper());
		grouperList.add(new Reader12ActionGrouper());
		grouperList.add(new FragmentRouting12ActionGrouper());
		grouperList.add(new PersistenceActionGrouper());
		grouperList.add(new Validation10ActionGrouper());
		grouperList.add(new SeparatorActionGrouper("No Group actions"));
		return grouperList;
	}

	protected void groupActions(MenuManager manager, Collection<?> createChildActions) {

		Map<Object, Object> map = new HashMap<Object, Object>();

		List<ISmooksActionGrouper> grouperList = getSmooksActionGrouper();
		for (Iterator<?> iterator1 = grouperList.iterator(); iterator1.hasNext();) {
			ISmooksActionGrouper grouper = (ISmooksActionGrouper) iterator1.next();
			if (grouper.isSeparator()) {
				String name = grouper.getGroupName();
				if(name == null) name = "";
				Separator s = new Separator(name);
				manager.add(s);
				continue;
			} else {
				String name = grouper.getGroupName();
				if (name != null) {
					MenuManager newMenu = new MenuManager(name);
					manager.add(newMenu);
					map.put(newMenu, grouper);
				}
			}
		}

		for (Iterator<?> iterator = createChildActions.iterator(); iterator.hasNext();) {
			boolean added = false;
			AddSmooksResourceAction action = (AddSmooksResourceAction) iterator.next();
			Object descriptor = action.getDescriptor();
			Iterator<?> menuIterator = map.keySet().iterator();
			while (menuIterator.hasNext()) {
				MenuManager newMenu = (MenuManager) menuIterator.next();
				ISmooksActionGrouper grouper = (ISmooksActionGrouper) map.get(newMenu);
				if (grouper.belongsToGroup(descriptor)) {
					newMenu.add(action);
					added = true;
					break;
				}
			}

			if (!added) {
				manager.add(action);
			}
		}

		Iterator<?> menuIterator = map.keySet().iterator();
		while (menuIterator.hasNext()) {
			MenuManager newMenu = (MenuManager) menuIterator.next();
			ISmooksActionGrouper grouper = (ISmooksActionGrouper) map.get(newMenu);
			grouper.orderActions(newMenu);
		}

	}

	protected void updateRootElementAddAction() {
		addSmooks11ResourceListAction.setEnabled(false);
		addMap10ResourceListAction.setEnabled(false);
		addSmooks10ResourceListAction.setEnabled(false);

		SmooksMultiFormEditor formEditor = (SmooksMultiFormEditor) this.getActiveEditor();
		EObject model = formEditor.getSmooksModel();
		if (model instanceof DocumentRoot) {
			SmooksResourceListType resourceList = SmooksFactory.eINSTANCE.createSmooksResourceListType();
			Command command = AddCommand.create(formEditor.getEditingDomain(), model, SmooksPackage.eINSTANCE
					.getSmooksResourceListType(), resourceList);
			addSmooks11ResourceListAction.setEnabled(command.canExecute());
		}

		if (model instanceof org.jboss.tools.smooks.model.medi.DocumentRoot) {
			EdiMap mappingNode = MEdiFactory.eINSTANCE.createEdiMap();
			Command command = AddCommand.create(formEditor.getEditingDomain(), model,
					MEdiPackage.eINSTANCE.getEdiMap(), mappingNode);
			addMap10ResourceListAction.setEnabled(command.canExecute());
		}
		if (model instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
			org.jboss.tools.smooks10.model.smooks.SmooksResourceListType resourceList = org.jboss.tools.smooks10.model.smooks.SmooksFactory.eINSTANCE
					.createSmooksResourceListType();
			Command command = AddCommand.create(formEditor.getEditingDomain(), model,
					org.jboss.tools.smooks10.model.smooks.SmooksPackage.eINSTANCE.getSmooksResourceListType(),
					resourceList);
			addSmooks10ResourceListAction.setEnabled(command.canExecute());
		}
	}

	/**
	 * This inserts global actions before the "additions-end" separator. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void addGlobalActions(IMenuManager menuManager) {
		// menuManager.insertAfter("additions-end", new
		// Separator("ui-actions"));
		// menuManager.insertAfter("additions-end", showPropertiesViewAction);

		// refreshViewerAction.setEnabled(refreshViewerAction.isEnabled());
		// menuManager.insertAfter("ui-actions", refreshViewerAction);

		// super.addGlobalActions(menuManager);
	}

	/**
	 * This ensures that a delete action will clean up all references to deleted
	 * objects. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected boolean removeAllReferencesOnDelete() {
		return true;
	}

}