package org.jboss.tools.smooks.configuration.editors.uitls;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaMethodsSelectionDialog;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaPropertiesSelectionDialog;
import org.jboss.tools.smooks.model.javabean.BindingsType;

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
				String displayName = propertyDescriptor.getDisplayName(model);
				Hyperlink link = toolkit.createHyperlink(parent, displayName + " :", SWT.NONE);
				final Composite classTextComposite = toolkit.createComposite(parent);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				classTextComposite.setLayoutData(gd);
				FillLayout fillLayout = new FillLayout();
				fillLayout.marginHeight = 0;
				fillLayout.marginWidth = 0;
				classTextComposite.setLayout(fillLayout);
				final SearchComposite searchComposite = new SearchComposite(classTextComposite, toolkit,
						"Search Class", dialog, SWT.NONE);
				Object editValue = getEditValue(propertyDescriptor, model);
				if (editValue != null) {
					searchComposite.getText().setText(editValue.toString());
				}
				searchComposite.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						Object value = propertyDescriptor.getPropertyValue(model);
						if (value != null && value instanceof PropertyValueWrapper) {
							Object editValue = ((PropertyValueWrapper) value).getEditableValue(model);
							if (editValue != null) {
								if (!editValue.equals(searchComposite.getText().getText())) {
									propertyDescriptor.setPropertyValue(model, searchComposite.getText()
											.getText());
								}
							} else {
								propertyDescriptor.setPropertyValue(model, searchComposite.getText()
										.getText());
							}
						} else {
							propertyDescriptor.setPropertyValue(model, searchComposite.getText().getText());
						}
					}
				});
				final IResource fresource = workspaceResource;
				link.addHyperlinkListener(new IHyperlinkListener() {

					public void linkActivated(HyperlinkEvent e) {
						try {
							if (fresource == null)
								return;
							if (fresource.getProject().hasNature(JavaCore.NATURE_ID)) {
								IJavaProject javaProject = JavaCore.create(fresource.getProject());
								String typeName = searchComposite.getText().getText();
								IJavaElement result = javaProject.findType(typeName);
								if (result != null)
									JavaUI.openInEditor(result);
								else {
									MessageDialog.openInformation(classTextComposite.getShell(),
											"Can't find type", "Can't find type \"" + typeName + "\" in \""
													+ javaProject.getProject().getName() + "\" project.");
								}
							}
						} catch (PartInitException ex) {
							SmooksConfigurationActivator.getDefault().log(ex);
						} catch (JavaModelException ex) {
							Display.getCurrent().beep(); // just for Dejan
						} catch (CoreException ex) {
							SmooksConfigurationActivator.getDefault().log(ex);
						}
					}

					public void linkEntered(HyperlinkEvent e) {

					}

					public void linkExited(HyperlinkEvent e) {

					}

				});

				toolkit.paintBordersFor(classTextComposite);
				return classTextComposite;
			}
		}
		return null;
	}

	public static IResource getResource(EObject model) {
		final Resource resource = ((EObject) model).eResource();
		URI uri = resource.getURI();
		IResource workspaceResource = null;
		if (uri.isPlatformResource()) {
			String path = uri.toPlatformString(true);
			workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
		}
		return workspaceResource;
	}

	public static IJavaProject getJavaProject(EObject model) {
		IResource r = getResource(model);
		if (r != null) {
			IProject p = r.getProject();
			return JavaCore.create(p);
		}
		return null;
	}

	public static Composite createJavaMethodSearchFieldEditor(BindingsType container, Composite parent,
			FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor, String buttonName,
			final EObject model) {
		String classString = ((BindingsType) container).getClass_();
		IJavaProject project = getJavaProject(container);
		try {
			ProjectClassLoader classLoader = new ProjectClassLoader(project);
			Class<?> clazz = classLoader.loadClass(classString);
			JavaMethodsSelectionDialog dialog = new JavaMethodsSelectionDialog(project, clazz);
			return SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor,
					"Select method", dialog, (EObject) model);
		} catch (JavaModelException e) {
			// ignore
		} catch (ClassNotFoundException e) {
			// ignore
		}
		return null;
	}

	public static Composite createJavaPropertySearchFieldEditor(BindingsType container, Composite parent,
			FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor, String buttonName,
			final EObject model) {
		String classString = ((BindingsType) container).getClass_();
		IJavaProject project = getJavaProject(container);
		try {
			ProjectClassLoader classLoader = new ProjectClassLoader(project);
			Class<?> clazz = classLoader.loadClass(classString);
			JavaPropertiesSelectionDialog dialog = new JavaPropertiesSelectionDialog(project, clazz);
			return SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor,
					"Select property", dialog, (EObject) model);
		} catch (JavaModelException e) {
			// ignore
		} catch (ClassNotFoundException e) {
			// ignore
		}
		return null;
	}

	public static Object getEditValue(IItemPropertyDescriptor propertyDescriptor, Object model) {
		Object value = propertyDescriptor.getPropertyValue(model);
		if (value != null && value instanceof PropertyValueWrapper) {
			Object editValue = ((PropertyValueWrapper) value).getEditableValue(model);
			return editValue;
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
		Object editValue = getEditValue(propertyDescriptor, model);
		if (editValue != null) {
			searchComposite.getText().setText(editValue.toString());
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
