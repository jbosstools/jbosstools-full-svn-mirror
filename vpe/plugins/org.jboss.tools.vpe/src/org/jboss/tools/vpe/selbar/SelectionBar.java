/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.selbar;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.Splitter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.attribute.adapter.AdapterFactory;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelectionBuilder;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * This class create and manage the Selection Bar under the VPE.
 * Entry point from the class MozilaEditor This bar can be hiden and
 * shown it uses splitter for this.
 *
 * @author erick
 * @author yradtsevich
 */
public class SelectionBar implements SelectionListener {
    /**
	 *
	 */
	private static final int SEL_ITEM_RIGHT_MARGIN = 5;

	private Splitter splitter;

	private boolean resizeListenerAdded = false;
    private VpeController vpeController = null;
    private ToolBar selBar = null;
    private FormData selBarData;
//    private Composite closeBar = null;
    private Menu dropDownMenu = null;
    private int itemCount = 0;
//    private Composite arrowBar;
    private Composite cmpToolBar = null;
    private Composite cmpTlEmpty = null;
	private List<VisibilityListener> visibilityListeners = new ArrayList<VisibilityListener>(1);

	/**
	 * Visibility state of the {@code SelectionBar}.
	 */
	private boolean visible;

	private ImageButton arrowButton;
	private Node currentSelectedNode = null;
	private Node currentLastNode = null;

	public Composite createToolBarComposite(Composite parent, boolean visible) {
		splitter = new Splitter(parent, SWT.NONE);
		splitter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		/*
		 * The empty composite
		 */
		cmpTlEmpty = new Composite(splitter, SWT.NONE) {
			@Override
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point point = super.computeSize(wHint, hHint, changed);
				point.y = 1;
				return point;
			}
		};

		cmpTlEmpty.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Main composite of the visible splitter
		cmpToolBar = new Composite(splitter, SWT.NONE);
		cmpToolBar.setLayout(new FormLayout());

		final Image closeImage = PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_DELETE);

		final Listener closeListener = new Listener() {
			public void handleEvent(Event event) {
				if (askConfirmationOnClosingSelectionBar()) {
					MessageDialogWithToggle dialog = MessageDialogWithToggle
					.openOkCancelConfirm(
							PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow()
							.getShell(),
							VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_TITLE,
							VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_MESSAGE,
//							VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_TOGGLE_MESSAGE,
							VpeUIMessages.ASK_CONFIRMATION_ON_CLOSING_SELECTION_BAR,
							askConfirmationOnClosingSelectionBar(), null, null);
					if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
						return;
					}
					setAskConfirmationOnClosingSelectionBar(dialog.getToggleState());
				}

				setVisible(false);
			}
		};

		ImageButton closeButton = new ImageButton(cmpToolBar, closeImage,
				VpeUIMessages.HIDE_SELECTION_BAR);
		closeButton.addSelectionListener(closeListener);
		FormData closeBarData = new FormData();
		closeBarData.right = new FormAttachment(100);
		closeBarData.top = new FormAttachment(0);
		Composite closeItemComposite = closeButton.getComposite();
		closeItemComposite.setLayoutData(closeBarData);

		// Create selection bar
		selBar = new ToolBar(cmpToolBar, SWT.HORIZONTAL | SWT.FLAT | SWT.NO_BACKGROUND);
		selBarData = new FormData();
		selBarData.left = new FormAttachment(0);
		selBarData.right = new FormAttachment(closeItemComposite, 0, SWT.LEFT);
		selBarData.top = new FormAttachment(0);
		selBar.setLayoutData(selBarData);
		createArrowButton();
		cmpToolBar.layout();
		setVisible(visible);

		return splitter;
	}

	public boolean getAlwaysVisibleOption() {
		return JspEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				IVpePreferencesPage.SHOW_SELECTION_TAG_BAR);
	}

	public void setAskConfirmationOnClosingSelectionBar(boolean askConfirmation) {
		JspEditorPlugin.getDefault().getPreferenceStore().setValue(
				IVpePreferencesPage.ASK_CONFIRMATION_ON_CLOSING_SELECTION_BAR,
				askConfirmation);
	}

	public boolean askConfirmationOnClosingSelectionBar() {
		return JspEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				IVpePreferencesPage.ASK_CONFIRMATION_ON_CLOSING_SELECTION_BAR);
	}

	/**
	 * Sets {@code visible} state to this {@code SelectionBar} and fires
	 * all registered {@code VisibilityListener}s.
	 */
	public void setVisible(boolean visible) {
		if (visible) {
			splitter.setVisible(cmpToolBar, true);
			splitter.setVisible(cmpTlEmpty, false);
		} else {
			splitter.setVisible(cmpToolBar, false);
			splitter.setVisible(cmpTlEmpty, true);
		}
		splitter.getParent().layout(true, true);

		this.visible = visible;
		fireVisibilityListeners();
	}

	/**
	 * Returns {@code visible} state of this {@code SelectionBar}.
	 */
	public boolean isVisible() {
		return visible;
	}

    /**
     * Adds the listener to the collection of listeners who will
     * be notified when the {@code #visible} state is changed.
     *
     * @param listener the listener which should be notified
     *
     * @see VisibilityListener
     * @see VisibilityEvent
     */
    public void addVisibilityListener(VisibilityListener listener) {
    	visibilityListeners.add(listener);
    }

    /**
     * Removes the listener from the collection of listeners who will
     * be notified when the {@link #visible} state is changed.
     *
     * @param listener the listener which should be removed
     *
     * @see VisibilityListener
     */
    public void removeVisibilityListener(VisibilityListener listener) {
    	visibilityListeners.remove(listener);
    }

    /**
     * Fires all registered instances of {@code VisibilityListener} by
     * sending them {@link VisibilityEvent}.
     *
     * @see #addVisibilityListener(VisibilityListener)
     * @see #removeVisibilityListener(VisibilityListener)
     */
    private void fireVisibilityListeners() {
		VisibilityEvent event = new VisibilityEvent(this);
		for (VisibilityListener listener : visibilityListeners) {
			listener.visibilityChanged(event);
		}
	}

	public void setVpeController(VpeController vpeController) {
		this.vpeController = vpeController;
	}

	/**
	 * Updates buttons in the selection bar and the drop-down menu
	 * according to the source selection.
	 */
    public void updateNodes(boolean forceUpdate) {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(
				vpeController.getSourceEditor());
		VpeSourceSelection selection = sourceSelectionBuilder.getSelection();
		if (selection == null) {
			return;
		}

		// Node node = selection.getFocusNode();
		Node node = selection.getStartNode();
		if (node != null && node.getNodeType() == Node.TEXT_NODE) {
			node = node.getParentNode();
		}

		if (currentSelectedNode == node && !forceUpdate) {
			return;
		}

	    final boolean ancestorSelected = isAncestor(node, currentLastNode);
	    if (ancestorSelected) {
	    	if (forceUpdate) {
	    		// reinitialize selBar with currentLastNode
	    		setSelBarItems(currentLastNode);
	    	} else {
	    		// deselect currentSelectedNode
	    		setNodeSelected(currentSelectedNode, false);
	    	}
	    } else {
	    	setSelBarItems(node);
	    	currentLastNode = node;
	    }

		setNodeSelected(node, true);
		currentSelectedNode = node;
	}

    /**
	 * Sets the selection state of the given node in the selection bar.
	 */
	private void setNodeSelected(Node node, boolean selected) {
		for (ToolItem item : selBar.getItems()) {
			if (item.getData() == node) {
				item.setSelection(selected);
				return;
			}
		}

		if (dropDownMenu == null) {
			return;
		}

		for (MenuItem item : dropDownMenu.getItems()) {
			if (item.getData() == node) {
				item.setSelection(selected);
				return;
			}
		}
	}

	/**
     * Cleans {@link #selBar} and adds to it buttons which
     * appropriate the {@code node} and all its ancestors.
	 */
	private void setSelBarItems(Node node) {
		// bug was fixed when toolbar are not shown for resizeble components
		cmpToolBar.layout();
		splitter.getParent().layout(true, true);

		removeNodeListenerFromAllNodes();
		cleanToolBar(selBar);

		disposeDropDownMenu();
		// for now dropDownMenu = null

		int elementCounter = 0;
		while (node != null
				&& (node.getNodeType() == Node.ELEMENT_NODE
						|| node.getNodeType() == Node.COMMENT_NODE)) {
			addNodeListenerTo(node);

			if (dropDownMenu == null) {
				ToolItem item = new ToolItem(selBar, SWT.FLAT | SWT.CHECK, 1);
				item.addSelectionListener(this);
				item.setData(node);
				item.setText(node.getNodeName());

				if (!isItemShown(selBar.getItem(elementCounter + 1))) {
					item.dispose();
					dropDownMenu = new Menu(selBar);
				}
			}

			if (dropDownMenu != null) {
				MenuItem menuItem = new MenuItem(dropDownMenu, SWT.CHECK, 0);
				menuItem.addSelectionListener(this);
				menuItem.setText(node.getNodeName());
				menuItem.setData(node);
			}

			elementCounter++;
			node = node.getParentNode();
		}
		itemCount = elementCounter;
		arrowButton.setEnabled(dropDownMenu != null);

		if (node != null && node.getNodeType() == Node.DOCUMENT_NODE) {
			addNodeListenerTo(node);
		}

		if (!resizeListenerAdded ) {
			cmpToolBar.addListener(SWT.Resize, new Listener() {
				public void handleEvent(Event event) {
					updateNodes(true);
				}
			});
			resizeListenerAdded = true;
		}
	}

	/**
	 * Checks if the {@code potentialAncestor} is an ancestor of
	 * {@code potentialAncestor node}.
	 */
	private boolean isAncestor(Node potentialAncestor, Node node) {
		if (potentialAncestor == null || node == null) {
			return false;
		}

		Node curAncestor = node;
		while ((curAncestor = curAncestor.getParentNode()) != null) {
			if (potentialAncestor == curAncestor) {
				return true;
			}
		}

		return false;
	}

	/**
     * Deletes all items (except the first item-arrow button)
     * from the given {@code #toolBar}.
     */
	private void cleanToolBar(ToolBar toolBar) {
		ToolItem[] oldItems = toolBar.getItems();
		for (int i = 1; i < oldItems.length; i++) {
			oldItems[i].dispose();
		}
	}

	/**
	 * Initializes {@link #arrowButton}.
	 */
    private void createArrowButton() {
		final Image hoverImage = WorkbenchImages.getImage(
				IWorkbenchGraphicConstants.IMG_LCL_RENDERED_VIEW_MENU);

		arrowButton = new ImageButton(selBar, hoverImage,
				VpeUIMessages.SelectionBar_MoreNodes);
		arrowButton.setEnabled(false);
		arrowButton.addSelectionListener(
				new Listener() {
					public void handleEvent(Event event) {
						Rectangle bounds = arrowButton.getButtonBounds();
						Point point = selBar.toDisplay(bounds.x, bounds.y
								+ bounds.height);
						dropDownMenu.setLocation(point);
						dropDownMenu.setVisible(true);
					}
				});

		ToolItem arrowItem = new ToolItem(selBar, SWT.SEPARATOR, 0);
		Composite arrowButtonComposite = arrowButton.getComposite();
		arrowItem.setControl(arrowButtonComposite);
		arrowButtonComposite.pack();
		arrowItem.setWidth(arrowButtonComposite.getSize().x);

		FormData arrowToolBarData = new FormData();
		arrowToolBarData.left = new FormAttachment(0);
		arrowToolBarData.top = new FormAttachment(0);
		arrowButtonComposite.setLayoutData(arrowToolBarData);
	}

    /**
     * Checks if the given {@code toolItem} is fully shown on the screen.
     */
	private boolean isItemShown(ToolItem toolItem) {
		ToolBar toolBar = toolItem.getParent();
		Rectangle toolItemBounds = toolItem.getBounds();

		toolItemBounds.width += SEL_ITEM_RIGHT_MARGIN;
		Rectangle intersection = toolBar.getBounds().intersection(
				toolItemBounds);
		return intersection.equals(toolItemBounds);
	}

	/**
     * List of nodes that are notifying {@link #nodeListener} when they are
     * changed.
     */
    private List<INodeNotifier> nodeNotifiers = new ArrayList<INodeNotifier>();
    /**
     * Listener for all {@link #nodeNotifiers}
     */
    private NodeListener nodeListener = new NodeListener(this);

    /**
     * Adds {@link #nodeListener} to the given {@code node}.
     *
     * @param node the node to that the listener must be added.
     */
	private void addNodeListenerTo(Node node) {
		if (node instanceof INodeNotifier) {
			INodeNotifier notifier = (INodeNotifier) node;
			if (notifier.getExistingAdapter(this) == null) {
				notifier.addAdapter(nodeListener);
				nodeNotifiers.add(notifier);
			}
		}
	}
	/**
	 * Removes {@link #nodeListener} from all nodes associated with this {@code SelectionBar}.
	 */
	private void removeNodeListenerFromAllNodes() {
		for (INodeNotifier notifier : nodeNotifiers) {
			notifier.removeAdapter(nodeListener);
		}
		nodeNotifiers.clear();
	}

    public void dispose() {
    	removeNodeListenerFromAllNodes();

		if (splitter != null) {
			splitter.dispose();
			splitter = null;
		}

    	disposeDropDownMenu();
	}

    /**
     * Disposes {@link #dropDownMenu}.
     */
	private void disposeDropDownMenu() {
		if (dropDownMenu != null) {
			dropDownMenu.dispose();
			dropDownMenu = null;
		}
	}

    public void widgetSelected(SelectionEvent e) {
    	Widget widget = e.widget;
    	
    	/* ensure that the ToolItem or MenuItem is selected
    	 * (for repeated clicks on the same widget)*/
    	if (widget instanceof ToolItem) {
    		((ToolItem)widget).setSelection(true);
    	} else if (widget instanceof MenuItem) {
    		((MenuItem)widget).setSelection(true);
    	}

    	SelectionUtil.setSourceSelection(vpeController.getPageContext(),
				(Node) widget.getData());
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

    @Override
	public String toString() {
		StringBuffer st = new StringBuffer("CountItem: "); //$NON-NLS-1$
		st.append(itemCount);
		st.append(" Parent Composite: " + cmpToolBar.getBounds().width); //$NON-NLS-1$
		st.append(" Bar : " + selBar.getBounds().width); //$NON-NLS-1$
		return st.toString();
	}
}

/**
 * Instances of this class represent a flat button with image.
 * 
 * @author yradtsevich
 */
class ImageButton {
	private ToolItem item;
	private Composite composite;
	private Image emptyImage;

	public ImageButton(Composite parent, Image image, String toolTip) {
		composite = new Composite(parent, SWT.NONE);
		composite.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				release();
			}
		});
		GridLayout layoutTl = new GridLayout(1, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 0;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;
		composite.setLayout(layoutTl);

		ToolBar toolBar = new ToolBar(composite, SWT.HORIZONTAL | SWT.FLAT);
		item = new ToolItem(toolBar, SWT.FLAT);
		item.setImage(image);
		emptyImage = new Image(Display.getCurrent(), 1, 1);
		Color emptyImageColor = toolBar.getBackground();
		emptyImage.setBackground(emptyImageColor);
		GC gc = new GC(emptyImage);
		gc.setForeground(emptyImageColor);
        gc.drawPoint(0, 0);
        gc.dispose();
		item.setDisabledImage(emptyImage);
		item.setToolTipText(toolTip);
	}

	public void addSelectionListener (Listener listener) {
		item.addListener(SWT.Selection, listener);
	}

	/**
	 * Releases resources.
	 */
	protected void release() {
		if (emptyImage != null) {
			emptyImage.dispose();
			emptyImage = null;
		}
	}

	public void dispose () {
		if (composite != null) {
			composite.dispose();
			composite = null;
			item = null;
		}
	}
	public void setEnabled (boolean enabled) {
		item.setEnabled(enabled);
	}

	public Rectangle getButtonBounds() {
		return item.getBounds();
	}


	public Composite getComposite() {
		return composite;
	}
}

/**
 * Listener for nodes that are implementing {@link INodeAdapter}.
 * Calls {@link SelectionBar#updateNodes()} every time when these nodes are changed.
 * <P>
 * This class is a part of fix of JBIDE-3919:
 * Incorrect interaction of block comments with selection bar.
 * </P>
 * @author yradtsevich
 */
class NodeListener implements INodeAdapter {
	private SelectionBar selectionBar;

	public NodeListener(SelectionBar selectionBar) {
		this.selectionBar = selectionBar;
	}

	/* (non-javadoc)
     * The infrastructure calls this method to determine if the adapter is
     * appropriate for 'type'. Typically, adapters return true based on
     * identity comparison to 'type', but this is not required, that is, the
     * decision can be based on complex logic.
     */
    public boolean isAdapterForType(Object type) {
    	return selectionBar == type;
    }

    /* (non-javadoc)
     * Sent to adapter when notifier changes. Each notifier is responsible for
     * defining specific eventTypes, feature changed, etc.
     *
     * ISSUE: may be more evolvable if the argument was one big 'notifier
     * event' instance.
     */
    public void notifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos) {
   		selectionBar.updateNodes(false);
    }
}
