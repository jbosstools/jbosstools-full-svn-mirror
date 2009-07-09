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
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.attribute.adapter.AdapterFactory;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.VpePlugin;
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
 */

public class SelectionBar extends Layout implements SelectionListener {
    private Splitter splitter;

    private VpeController vpeController = null;

    private ToolBar selBar = null;
    private ToolBar closeSelectionBar = null;

    private int itemCount = 0;

    private Composite cmpToolBar = null;
    private Composite cmpTlEmpty = null;
    private Composite cmpBar = null;
    private Composite closeBar = null;

	private List<VisibilityListener> visibilityListeners = new ArrayList<VisibilityListener>(1);

	/**
	 * Visibility state of the {@code SelectionBar}.
	 */
	private boolean visible;

    //Listener selbarListener = null;

    final static String PREFERENCE_YES = "yes"; //$NON-NLS-1$
    final static String PREFERENCE_NO = "no"; //$NON-NLS-1$

	public Composite createToolBarComposite(Composite parent, boolean visible) {
		splitter = new Splitter(parent, SWT.NONE);
		splitter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		/*
		 * The empty composite
		 */
		cmpTlEmpty = new Composite(splitter, SWT.NONE) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point point = super.computeSize(wHint, hHint, changed);
				point.y = 1;
				return point;
			}
		};

		cmpTlEmpty.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Main composite of the visible splitter
		cmpToolBar = new Composite(splitter, SWT.NONE);
		cmpToolBar.setLayout(this);

		GridLayout layoutTl = new GridLayout(1, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 0;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;

		// Middle composite, witch contain the selectbar
		cmpBar = new Composite(cmpToolBar, SWT.NONE);
		cmpBar.setLayout(layoutTl);
		cmpBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		closeBar = new Composite(cmpToolBar, SWT.NONE);
		closeBar.setLayout(layoutTl);
		closeBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		closeSelectionBar = new ToolBar(closeBar, SWT.HORIZONTAL | SWT.FLAT);
		ToolItem closeItem = new ToolItem(closeSelectionBar, SWT.FLAT);
		closeItem.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_DELETE));
		closeItem.setToolTipText(VpeUIMessages.HIDE_SELECTION_BAR);
		closeItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (!getHideWithoutPromptOption()) {
					MessageDialogWithToggle dialog = MessageDialogWithToggle
							.openOkCancelConfirm(
									PlatformUI.getWorkbench()
											.getActiveWorkbenchWindow()
											.getShell(),
									VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_TITLE,
									VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_MESSAGE,
									VpeUIMessages.CONFIRM_SELECTION_BAR_DIALOG_TOGGLE_MESSAGE,
									false, null, null);
					if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
						return;
					}
					if (dialog.getToggleState()) {
						setHideWithoutPromptOption(true);
					}
				}

				setAlwaysVisibleOption(false);
				setVisible(false);
			}

		});
		// Create selection bar
		selBar = new ToolBar(cmpBar, SWT.HORIZONTAL | SWT.FLAT);
		setVisible(visible);

		return splitter;
	}

	private void setPersistentOption(String name, String value) {
		XModelObject optionsObject = getOptionsObject();
		optionsObject.setAttributeValue(name, value);

		/*
		 * Fixes http://jira.jboss.com/jira/browse/JBIDE-2298
		 * To get stored in xml XModelObject 
		 * should be marked as modified.
		 */
		optionsObject.setModified(true);
		performStore(optionsObject);
	}

	private XModelObject getOptionsObject() {
		XModelObject optionsObject = ModelUtilities.getPreferenceModel()
				.getByPath(VpePreference.VPE_EDITOR_PATH);
		return optionsObject;
	}

	public void setAlwaysVisibleOption(boolean visible) {
		final String optionValue;
		if (visible) {
			optionValue = PREFERENCE_YES;
		} else {
			optionValue = PREFERENCE_NO;
		}

		setPersistentOption(VpePreference.ATT_SHOW_SELECTION_TAG_BAR, optionValue);
	}

	public boolean getAlwaysVisibleOption() {
		return VpePreference.SHOW_SELECTION_TAG_BAR.getValue().equals(PREFERENCE_YES);
	}

	public void setHideWithoutPromptOption(boolean hideWithoutPrompt) {
		final String optionValue;
		if (hideWithoutPrompt) {
			optionValue = PREFERENCE_YES;
		} else {
			optionValue = PREFERENCE_NO;
		}

		setPersistentOption(VpePreference.ATT_ALWAYS_HIDE_SELECTION_BAR_WITHOUT_PROMT,
				optionValue);
	}
	
	public boolean getHideWithoutPromptOption() {
		return VpePreference.ALWAYS_HIDE_SELECTION_BAR_WITHOUT_PROMT
				.getValue().equals(PREFERENCE_YES);
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

    public void selectionChanged() {
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

		removeNodeListenerFromAllNodes();
		int elementCounter = 0;
		while (node != null 
				&& (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.COMMENT_NODE)) {
			addNodeListenerTo(node);

			ToolItem item = null;
			if (selBar.getItemCount() > elementCounter) {
				item = selBar.getItem(selBar.getItemCount() - 1 - elementCounter);
				item.setData(node);
			} else {
				item = new ToolItem(selBar, SWT.FLAT, 0);
				item.addSelectionListener(this);
				item.setData(node);
			}

			item.setText(node.getNodeName());
			elementCounter++;
			node = node.getParentNode();
		}
		if (node != null && node.getNodeType() == Node.DOCUMENT_NODE) {
			addNodeListenerTo(node);
		}

		itemCount = elementCounter;
		cmpToolBar.layout();
		// bug was fixed when toolbar are not shown for resizeble components
		cmpToolBar.layout();
		splitter.getParent().layout(true,true);
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

    protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		Point point = selBar.computeSize(SWT.DEFAULT, hHint);
		point.y = closeBar.getSize().y;
		return point;
	}

    protected void layout(Composite composite, boolean flushCache) {
		Rectangle rect = null;
		try {
			rect = composite.getBounds();
		} catch (SWTException e) {
			VpePlugin.getPluginLog().logError(e);
		}

		Rectangle closeBarRect = closeSelectionBar.getItem(0).getBounds();
		rect.width -= closeBarRect.width;

		int allItems = selBar.getItems().length;
		if (allItems == 0) {
			cmpBar.setBounds(new Rectangle(rect.x, rect.y, rect.width,
					closeBarRect.height));
			closeBar.setBounds(new Rectangle(rect.width, rect.y, rect.width
					+ closeBarRect.width, closeBarRect.height));
			return;
		}
		Rectangle r = selBar.getItem(allItems - 1).getBounds();
		int width = r.x + r.width ;
		int height = r.height;

		if (allItems >= itemCount) {
			int x = 0;

			if (itemCount == 0) {
				x = selBar.getBounds().width;
			} else {
				x = selBar.getItem(allItems - itemCount).getBounds().x;
			}
			
			rect.x -= x;

			if (rect.width < (r.x + r.width - Math.abs(rect.x) + 10)) {
				rect.x -= (r.x + r.width - Math.abs(rect.x)) - rect.width;
			}
			
			cmpBar.setBounds(new Rectangle(rect.x, 0, width, height));
			selBar.setSize(width, closeBarRect.height);
			closeBar.setBounds(new Rectangle(rect.width, rect.y, rect.width
					+ closeBarRect.width, closeBarRect.height));
		}
	}

    public void dispose() {
    	removeNodeListenerFromAllNodes();

		if (!selBar.isDisposed()) {
			for (int i = 0; i < selBar.getItemCount(); i++) {
				if (!selBar.getItem(i).isDisposed()) {
					selBar.getItem(i).removeSelectionListener(this);
				}
			}
			selBar.dispose();
			selBar = null;
		}
		if (!closeSelectionBar.isDisposed()) {
			for (int i = 0; i < closeSelectionBar.getItemCount(); i++) {
				if (!closeSelectionBar.getItem(i).isDisposed()) {
					closeSelectionBar.getItem(i).removeSelectionListener(this);
				}
			}
			closeSelectionBar.dispose();
			closeSelectionBar = null;
		}
		if (splitter != null) {
			splitter.dispose();
			splitter = null;
		}
	}

    public void widgetSelected(SelectionEvent e) {
		ToolItem toolItem = (ToolItem) e.widget;

		SelectionUtil.setSourceSelection(vpeController.getPageContext(),
				(Node) toolItem.getData());
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/**
	 * Performs storing model object in the model and xml file.
	 * 
	 * @param xmo the model object to store
	 */
	private void performStore(XModelObject xmo) {
		if (null == xmo || null == xmo.getModel()
				|| null == xmo.getModelEntity()) {
			return;
		}
		
		ArrayList<IModelPropertyEditorAdapter> adapters = new ArrayList<IModelPropertyEditorAdapter>();
		XAttribute[] attribute = xmo.getModelEntity().getAttributes();
		for (int i = 0; i < attribute.length; i++) {
			if(!attribute[i].isVisible()) {
				continue;
			}
			IModelPropertyEditorAdapter adapter = AdapterFactory.getAdapter(attribute[i], xmo, xmo.getModel());
			adapters.add(adapter);
		}
		/*
		 * Stores model object by its adaptors. 
		 */
		for (IModelPropertyEditorAdapter adapter : adapters) {
			adapter.store();
		}

		/*
		 * Saves model options
		 */
		xmo.getModel().saveOptions();
	}

    public String toString() {
		StringBuffer st = new StringBuffer("CountItem: "); //$NON-NLS-1$
		st.append(itemCount);
		st.append(" Parent Composite: " + cmpToolBar.getBounds().width); //$NON-NLS-1$
		st.append(" Midle composite: " + cmpBar.getBounds().width); //$NON-NLS-1$
		st.append(" Bar : " + selBar.getBounds().width); //$NON-NLS-1$
		return st.toString();
	}
}

/**
 * Listener for nodes that are implementing {@link INodeAdapter}.
 * Calls {@link SelectionBar#selectionChanged()} every time when these nodes are changed.
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
   		selectionBar.selectionChanged();
    }
}
