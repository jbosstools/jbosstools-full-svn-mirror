package org.jboss.tools.smooks.configuration.editors.uitls;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SmooksUIUtils {

	public static Composite createJavaTypeSearchFieldEditor(Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, final EObject model) {
		if (model instanceof EObject) {
			final Resource resource = ((EObject) model).eResource();
			URI uri = resource.getURI();
			IResource workspaceResource = null;
			if (uri.isPlatformResource()) {
				String path = uri.toPlatformString(true);
				workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
				JavaTypeFieldDialog dialog = new JavaTypeFieldDialog(workspaceResource);
				return SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor,
						"Search Class", dialog, (EObject) model);
			}
		}
		return null;
	}

	public static Composite createDialogFieldEditor(Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, String buttonName, IFieldDialog dialog,
			final EObject model) {
		String displayName = propertyDescriptor.getDisplayName(model);
		toolkit.createLabel(parent, displayName + " :");
		final Composite classTextComposite = toolkit.createComposite(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		classTextComposite.setLayoutData(gd);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 0;
		fillLayout.marginWidth = 0;
		classTextComposite.setLayout(fillLayout);
		final SearchComposite searchComposite = new SearchComposite(classTextComposite, toolkit, buttonName,
				dialog, SWT.NONE);
		Object value = propertyDescriptor.getPropertyValue(model);
		if (value != null && value instanceof PropertyValueWrapper) {
			Object editValue = ((PropertyValueWrapper) value).getEditableValue(model);
			if (editValue != null) {
				searchComposite.getText().setText(editValue.toString());
			}
		}
		searchComposite.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object value = propertyDescriptor.getPropertyValue(model);
				if (value != null && value instanceof PropertyValueWrapper) {
					Object editValue = ((PropertyValueWrapper) value).getEditableValue(model);
					if (editValue != null) {
						if (!editValue.equals(searchComposite.getText().getText())) {
							propertyDescriptor.setPropertyValue(model, searchComposite.getText().getText());
						}
					} else {
						propertyDescriptor.setPropertyValue(model, searchComposite.getText().getText());
					}
				} else {
					propertyDescriptor.setPropertyValue(model, searchComposite.getText().getText());
				}
			}
		});
		toolkit.paintBordersFor(classTextComposite);
		return classTextComposite;
	}
}
