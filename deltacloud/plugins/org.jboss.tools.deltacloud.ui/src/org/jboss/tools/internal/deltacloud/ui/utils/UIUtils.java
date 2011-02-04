/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author Andre Dietisheim
 */
public class UIUtils {

	private static final String CONTEXT_MENU_PREFIX = "popup:";
	private static final String VIEW_MENU_PREFIX = "menu:";
	
	private UIUtils() {
	}

	/**
	 * Returns the selection of the active workbench window.
	 * 
	 * @return the selection
	 * 
	 * @see IWorkbenchWindow#getSelectionService()
	 */
	public static ISelection getWorkbenchWindowSelection() {
		return getActiveWorkbenchWindow().getSelectionService().getSelection();
	}

	/**
	 * Gets the structured selection.
	 * 
	 * @return the structured selection
	 */
	public static IStructuredSelection getStructuredSelection() {
		ISelection selection = getWorkbenchWindowSelection();
		if (selection instanceof IStructuredSelection) {
			return (IStructuredSelection) selection;
		} else {
			return null;
		}
	}

	/**
	 * Gets the first element of a given selection in the given type. Returns
	 * <code>null</null> if selection is selection is empty or adaption of the first element in 
	 * the given selection fails. Adaption is tried by casting and by adapting it.
	 * 
	 * @param selection
	 *            the selection
	 * @param expectedClass
	 *            the expected class
	 * 
	 * @return the first element
	 */
	public static <T> T getFirstAdaptedElement(final ISelection selection, final Class<T> expectedClass) {
		if (selection == null) {
			return null;
		} else {
			Assert.isTrue(selection instanceof IStructuredSelection);
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement == null) {
				return null;
			}
			return adapt(firstElement, expectedClass);
		}
	}

	/**
	 * Returns <code>true</code> if the given selection holds exactly 1 element
	 * of the given type.
	 * 
	 * @param selection
	 *            the selection to check
	 * @param expectedClass
	 *            the expected class
	 * @return <code>true</code>, if the given selection holds a single element
	 *         of the given type.
	 */
	public static <T> boolean isSingleSelection(final ISelection selection, final Class<T> expectedClass) {
		if (!(selection instanceof IStructuredSelection)) {
			return false;
		}

		return ((IStructuredSelection) selection).toList().size() == 1
				&& getFirstAdaptedElement(selection, expectedClass) != null;
	}

	public static <T> List<T> adapt(Collection<?> objects, Class<T> expectedClass) {
		List<T> adaptedObjects = new ArrayList<T>();
		for (Object object : objects) {
			T adaptedObject = adapt(object, expectedClass);
			if (adaptedObject != null) {
				adaptedObjects.add(adaptedObject);
			}
		}
		return adaptedObjects;
	}
	
	/**
	 * Adapts the given object to the given type. Returns <code>null</code> if
	 * the given adaption is not possible. Adaption is tried by casting and by adapting it.
	 * 
	 * @param <T>
	 * @param object
	 * @param expectedClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T adapt(Object object, Class<T> expectedClass) {
		if (object == null) {
			return null;
		} 
		
		Object adaptedObject = null;
		if (expectedClass.isAssignableFrom(object.getClass())) {
			adaptedObject = object;
		} else if (object instanceof IAdaptable) {
			adaptedObject = ((IAdaptable) object).getAdapter(expectedClass);
		} 
		
		if (adaptedObject != null) {
			return (T) adaptedObject;
		} else {
			return (T) Platform.getAdapterManager().loadAdapter(object, expectedClass.getName());			
		}
	}

	/**
	 * Gets the active page.
	 * 
	 * @return the active page
	 */
	public static IWorkbenchPage getActivePage() {
		IWorkbenchPage workbenchPage = getActiveWorkbenchWindow().getActivePage();
		Assert.isNotNull(workbenchPage);
		return workbenchPage;
	}

	/**
	 * Returns the editor that's currently active (focused).
	 * 
	 * @return the active editor
	 */
	public static IEditorPart getActiveEditor() {
		IEditorPart editor = getActivePage().getActiveEditor();
		Assert.isNotNull(editor);
		return editor;
	}

	/**
	 * Gets the active workbench window.
	 * 
	 * @return the active workbench window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		IWorkbenchWindow workbenchWindow = getWorkbench().getActiveWorkbenchWindow();
		Assert.isNotNull(workbenchWindow);
		return workbenchWindow;
	}

	public static Shell getActiveShell() {
		Shell shell = getActiveWorkbenchWindow().getShell();
		Assert.isTrue(shell != null && !shell.isDisposed());
		return shell;
	}

	/**
	 * Gets the workbench.
	 * 
	 * @return the workbench
	 */
	public static IWorkbench getWorkbench() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		Assert.isNotNull(workbench);
		return workbench;
	}

	/**
	 * Replaces an image with the given key by the given image descriptor.
	 * 
	 * @param imageKey
	 *            the image key
	 * @param imageDescriptor
	 *            the image descriptor
	 */
	public static void replaceInJfaceImageRegistry(final String imageKey, final ImageDescriptor imageDescriptor) {
		Assert.isNotNull(imageKey);
		Assert.isNotNull(imageDescriptor);

		JFaceResources.getImageRegistry().remove(imageKey);
		JFaceResources.getImageRegistry().put(imageKey, imageDescriptor);
	}

	/**
	 * Register the given ContributionManager with the given id. The
	 * contribution manager gets unregistered on control disposal.
	 * 
	 * @param id
	 *            the id
	 * @param contributionManager
	 *            the contribution manager
	 * @param control
	 *            the control
	 * 
	 * @see ContributionManager
	 * @see IMenuService
	 * @see DisposeListener
	 */
	public static void registerContributionManager(final String id, final IContributionManager contributionManager,
			final Control control) {
		Assert.isNotNull(id);
		Assert.isNotNull(contributionManager);
		Assert.isTrue(control != null && !control.isDisposed());

		final IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);
		menuService.populateContributionManager((ContributionManager) contributionManager, id);
		contributionManager.update(true);
		control.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				menuService.releaseContributions((ContributionManager) contributionManager);
			}
		});
	}
	
	public static String getContextMenuId(String viewId) {
		return new StringBuffer(CONTEXT_MENU_PREFIX).append(viewId).toString();
	}
	
	public static String getViewMenuId(String viewId) {
		return new StringBuffer(VIEW_MENU_PREFIX).append(viewId).toString();
	}

	/**
	 * Creates context menu to a given control.
	 * 
	 * @param control
	 *            the control
	 * 
	 * @return the i menu manager
	 */
	public static IMenuManager createContextMenu(final Control control) {
		Assert.isTrue(control != null && !control.isDisposed());

		MenuManager menuManager = new MenuManager();
		menuManager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);
		return menuManager;
	}

	/**
	 * Gets the dialog settings for the given identifer and plugin.
	 * 
	 * @param identifier
	 *            the identifier
	 * @param plugin
	 *            the plugin
	 * 
	 * @return the dialog settings
	 */
	public static IDialogSettings getDialogSettings(final String identifier, final AbstractUIPlugin plugin) {
		Assert.isNotNull(plugin);
		IDialogSettings dialogSettings = plugin.getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(identifier);
		if (section == null) {
			section = dialogSettings.addNewSection(identifier);
		}
		return section;
	}

	/**
	 * Returns the page for a given editor.
	 * 
	 * @param editor
	 *            the editor
	 * @return
	 * 
	 * @return the page
	 * 
	 * @see IWorkbenchPage
	 */
	public static IWorkbenchPage getPage(EditorPart editor) {
		Assert.isNotNull(editor);
		IWorkbenchPartSite site = editor.getSite();
		Assert.isNotNull(site);
		return site.getPage();
	}

	public static void showView(String viewId) throws PartInitException {
		Assert.isLegal(viewId != null && viewId.length() > 0);
		getActivePage().showView(viewId);
	}

	
	/**
	 * This method has been stolen from AS-Tools' UIUtil class 
	 * and is useful for creating FormData objects
	 */
	public static FormData createFormData(
			Object topStart, int topOffset, 
			Object bottomStart, int bottomOffset, 
			Object leftStart, int leftOffset, 
			Object rightStart, int rightOffset) {
		
			FormData data = new FormData();

			if( topStart != null ) {
				data.top = topStart instanceof Control ? new FormAttachment((Control)topStart, topOffset) : 
					new FormAttachment(((Integer)topStart).intValue(), topOffset);
			}

			if( bottomStart != null ) {
				data.bottom = bottomStart instanceof Control ? new FormAttachment((Control)bottomStart, bottomOffset) : 
					new FormAttachment(((Integer)bottomStart).intValue(), bottomOffset);
			}

			if( leftStart != null ) {
				data.left = leftStart instanceof Control ? new FormAttachment((Control)leftStart, leftOffset) : 
					new FormAttachment(((Integer)leftStart).intValue(), leftOffset);
			}

			if( rightStart != null ) {
				data.right = rightStart instanceof Control ? new FormAttachment((Control)rightStart, rightOffset) : 
					new FormAttachment(((Integer)rightStart).intValue(), rightOffset);
			}
			return data;
		}

	public static ControlDecoration createErrorDecoration(String errorText, Control control) {
		return createDecoration(errorText, FieldDecorationRegistry.DEC_ERROR, control);
	}
	
	public static ControlDecoration createDecoration(String text, String imageKey, Control control) {
		ControlDecoration decoration = new ControlDecoration(control, SWT.LEFT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(imageKey).getImage();
		decoration.setImage(errorImage);
		decoration.setDescriptionText(text);
		decoration.setShowHover(true);
		decoration.hide();
		return decoration;
	}

}
