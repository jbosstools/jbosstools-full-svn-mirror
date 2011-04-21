/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;

/**
 * @author Dart
 * 
 */
public class DecoraterSelectionDialog extends Dialog implements
		ISelectionChangedListener {

	private SmooksResourceListType resourceList;

	private Decorater selectedDecorater;

	private TreeViewer viewer;

	public Decorater getSelectedDecorater() {
		return selectedDecorater;
	}

	public void setSelectedDecorater(Decorater selectedDecorater) {
		this.selectedDecorater = selectedDecorater;
	}

	public DecoraterSelectionDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	public DecoraterSelectionDialog(Shell parentShell,
			SmooksResourceListType list) {
		super(parentShell);
		this.resourceList = list;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		gd.widthHint = 200;
		composite.setLayoutData(gd);
		composite.setLayout(new FillLayout());
		viewer = new TreeViewer(composite, SWT.NONE);
		viewer.setContentProvider(new DecoraterContentProvider());
		viewer.setLabelProvider(new DecoraterLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				Object obj = selection.getFirstElement();
				if (obj == null) {
					return;
				}
				if (obj instanceof Decorater) {
					okPressed();
				}
			}
		});
		initViewer(viewer);
		return composite;
	}

	private void initViewer(TreeViewer viewer) {
		if (resourceList != null) {
			List<Object> allList = new ArrayList<Object>();
			List<Decorater> dateDecoraterList = new ArrayList<Decorater>();
			allList.add(dateDecoraterList);
			List<AbstractResourceConfig> list = resourceList
					.getAbstractResourceConfig();
			for (Iterator<AbstractResourceConfig> iterator = list.iterator(); iterator
					.hasNext();) {
				AbstractResourceConfig abstractResourceConfig = iterator.next();
				if(!(abstractResourceConfig instanceof ResourceConfigType)) continue;
				ResourceConfigType config = (ResourceConfigType)abstractResourceConfig;
				String selector = config.getSelector();

				if (selector != null && selector.indexOf(":") != -1) {
					String selector1 = selector.substring(0, selector
							.indexOf(":"));
					selector1 = selector1.trim();
					String selector2 = selector.substring(
							selector.indexOf(":") + 1, selector.length());
					selector2 = selector2.trim();
					if (selector1.equals("decoder")) {
						ResourceType resource = config.getResource();
						if (resource != null) {
							String value = resource.getStringValue();
							if (value != null)
								value = value.trim();
							for (int i = 0; i < SmooksModelConstants.DECODER_CLASSES.length; i++) {
								String decoderClass = SmooksModelConstants.DECODER_CLASSES[i];
								if (decoderClass.equals(value)) {
									dateDecoraterList.add(new DateDecorater(
											selector2));
								}
							}
						}
					}
				}
			}
			viewer.setInput(allList);
		}
	}

	private void updateButtonStatus() {
		if (this.getSelectedDecorater() == null) {
			Button button = this.getButton(IDialogConstants.OK_ID);
			if (button != null)
				button.setEnabled(false);
		} else {
			Button button = this.getButton(IDialogConstants.OK_ID);
			if (button != null)
				button.setEnabled(true);
		}
	}

	public static Decorater openDialog(Shell shell,
			SmooksResourceListType resourceList) {
		DecoraterSelectionDialog dialog = new DecoraterSelectionDialog(shell,
				resourceList);
		if (dialog.open() == Dialog.OK) {
			return dialog.getSelectedDecorater();
		}
		return null;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		Object obj = selection.getFirstElement();
		if (obj == null) {
			setSelectedDecorater(null);
		} else {
			if (obj instanceof Decorater) {
				this.setSelectedDecorater((Decorater) obj);
			} else {
				setSelectedDecorater(null);
			}
		}
		updateButtonStatus();
	}

}
