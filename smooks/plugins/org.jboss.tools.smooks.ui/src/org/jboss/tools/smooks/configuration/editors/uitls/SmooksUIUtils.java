/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.uitls;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.SelectorAttributes;
import org.jboss.tools.smooks.configuration.editors.SelectoreSelectionDialog;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaMethodsSelectionDialog;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaPropertiesSelectionDialog;
import org.jboss.tools.smooks.model.graphics.ext.DocumentRoot;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.util.SmooksGraphicsExtResourceFactoryImpl;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.smooks.ConditionType;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class SmooksUIUtils {

	public static final String FILE_PRIX = "File:/"; //$NON-NLS-1$

	public static final String WORKSPACE_PRIX = "Workspace:/"; //$NON-NLS-1$

	public static final String RESOURCE = "Resource:/";

	public static final String XSL_NAMESPACE = " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ";

	public static void createMixedTextFieldEditor(String label, AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent,
			Object model) {
		createMixedTextFieldEditor(label, editingdomain, toolkit, parent, model, false, 0);
	}

	public static void createMultiMixedTextFieldEditor(String label, AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model, int height) {
		createMixedTextFieldEditor(label, editingdomain, toolkit, parent, model, true, height);
	}

	public static void createMixedTextFieldEditor(String label, AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent,
			Object model, boolean multiText, int height) {
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		Section section = null;
		Composite textContainer = null;
		if (multiText) {
			Composite space = toolkit.createComposite(parent);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			gd.heightHint = 10;
			space.setLayoutData(gd);

			section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE);
			FillLayout layout = new FillLayout();
			section.setLayout(layout);
			section.setText(label);

			Composite textComposite = toolkit.createComposite(section);
			section.setClient(textComposite);
			textComposite.setLayout(new GridLayout());
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			section.setLayoutData(gd);
			textContainer = textComposite;
		} else {
			toolkit.createLabel(parent, label + " :").setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
			textContainer = parent;
		}
		gd = new GridData(GridData.FILL_HORIZONTAL);
		int textType = SWT.FLAT;
		if (multiText) {
			textType = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
		}
		final Text valueText = toolkit.createText(textContainer, "", textType);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		if (multiText && height > 0) {
			gd.heightHint = height;
		}
		valueText.setLayoutData(gd);
		if (model instanceof AnyType) {
			String text = SmooksModelUtils.getAnyTypeText((AnyType) model);
			if (text != null) {
				valueText.setText(text);
				if (text.length() > 0 && section != null) {
					section.setExpanded(true);
				}
			}
		}
		final Object fm = model;
		final AdapterFactoryEditingDomain fEditingDomain = editingdomain;
		valueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!(fm instanceof AnyType)) {
					return;
				}
				String text = SmooksModelUtils.getAnyTypeText((AnyType) fm);
				if (!valueText.getText().equals(text)) {
					SmooksModelUtils.setTextToSmooksType(fEditingDomain, (AnyType) fm, valueText.getText());
				}
			}
		});
	}

	public static Control createFiledEditorLabel(Composite parent, FormToolkit formToolKit, IItemPropertyDescriptor itemPropertyDescriptor,
			Object model, boolean isLink) {
		String displayName = itemPropertyDescriptor.getDisplayName(model);
		EAttribute feature = (EAttribute) itemPropertyDescriptor.getFeature(model);
		if (feature.isRequired()) {
			displayName = "*" + displayName;
		}
		if (!isLink) {
			Label label = formToolKit.createLabel(parent, displayName + " :");
			label.setForeground(formToolKit.getColors().getColor(IFormColors.TITLE));
			return label;
		} else {
			Hyperlink link = formToolKit.createHyperlink(parent, displayName + " :", SWT.NONE);
			return link;
		}
	}

	/**
	 * Can't use
	 * 
	 * @param editingdomain
	 * @param toolkit
	 * @param parent
	 * @param model
	 */
	public static void createFilePathFieldEditor(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model) {
		// IHyperlinkListener link
	}

	public static void createLinkMixedTextFieldEditor(String label, AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent,
			Object model, boolean multiText, int height, boolean linkLabel, IHyperlinkListener listener) {
		if (linkLabel) {
			Hyperlink link = toolkit.createHyperlink(parent, label, SWT.NONE);
			if (listener != null) {
				link.addHyperlinkListener(listener);
			}
		} else {
			toolkit.createLabel(parent, label + " :").setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		}
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		int textType = SWT.FLAT;
		if (multiText) {
			textType = SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL;
		}
		final Text valueText = toolkit.createText(parent, "", textType);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		if (multiText && height > 0) {
			gd.heightHint = height;
		}
		valueText.setLayoutData(gd);
		if (model instanceof AnyType) {
			String text = SmooksModelUtils.getAnyTypeText((AnyType) model);
			if (text != null) {
				valueText.setText(text);
			}
		} else {

		}
		final Object fm = model;
		final AdapterFactoryEditingDomain fEditingDomain = editingdomain;
		valueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!(fm instanceof AnyType)) {
					return;
				}
				String text = SmooksModelUtils.getAnyTypeText((AnyType) fm);
				if (!valueText.getText().equals(text)) {
					SmooksModelUtils.setTextToSmooksType(fEditingDomain, (AnyType) fm, valueText.getText());
				}
			}
		});
	}

	public static void createLinkTextValueFieldEditor(String label, AdapterFactoryEditingDomain editingdomain,
			IItemPropertyDescriptor propertyDescriptor, FormToolkit toolkit, Composite parent, Object model, boolean multiText, int height,
			boolean linkLabel, IHyperlinkListener listener) {
		if (linkLabel) {
			Hyperlink link = toolkit.createHyperlink(parent, label, SWT.NONE);
			if (listener != null) {
				link.addHyperlinkListener(listener);
			}
		} else {
			toolkit.createLabel(parent, label + " :").setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		}
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		int textType = SWT.FLAT;
		if (multiText) {
			textType = SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL;
		}
		final Text valueText = toolkit.createText(parent, "", textType);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		if (multiText && height > 0) {
			gd.heightHint = height;
		}
		valueText.setLayoutData(gd);
		Object path = SmooksUIUtils.getEditValue(propertyDescriptor, model);
		String string = null;
		if (path != null) {
			string = path.toString();
		}
		if (string != null) {
			valueText.setText(string);
		}
		final Object fm = model;
		final IItemPropertyDescriptor fpd = propertyDescriptor;
		valueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object text = getEditValue(fpd, fm);
				if (!valueText.getText().equals(text)) {
					fpd.setPropertyValue(fm, valueText.getText());
				}
			}
		});
	}

	public static String parseFilePath(String path) throws InvocationTargetException {
		int index = path.indexOf(FILE_PRIX);
		if (index != -1) {
			path = path.substring(index + FILE_PRIX.length(), path.length());
		} else {
			index = path.indexOf(WORKSPACE_PRIX);
			if (index != -1) {
				path = path.substring(index + WORKSPACE_PRIX.length(), path.length());
				Path wpath = new Path(path);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(wpath);
				if (file.exists()) {
					path = file.getLocation().toOSString();
				} else {
					throw new InvocationTargetException(new Exception("File : " + path + " isn't exsit")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				throw new InvocationTargetException(new Exception("This path is un-support" + path + ".")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return path;
	}

	public static Composite createSelectorFieldEditor(FormToolkit toolkit, Composite parent, final IItemPropertyDescriptor propertyDescriptor,
			Object model, final SmooksGraphicsExtType extType) {
		return createDialogFieldEditor(parent, toolkit, propertyDescriptor, "Browse", new IFieldDialog() {
			public Object open(Shell shell) {
				SelectoreSelectionDialog dialog = new SelectoreSelectionDialog(shell, extType);
				if (dialog.open() == Dialog.OK) {
					Object currentSelection = dialog.getCurrentSelection();
					SelectorAttributes sa = dialog.getSelectorAttributes();
					if (currentSelection instanceof IXMLStructuredObject) {
						String s = SmooksUIUtils.generatePath((IXMLStructuredObject) currentSelection, sa);
						return s;
					}
				}
				return null;
			}

			public IModelProcsser getModelProcesser() {
				return null;
			}

			public void setModelProcesser(IModelProcsser processer) {

			}

		}, (EObject) model);
	}

	public static SmooksGraphicsExtType loadSmooksGraphicsExt(IFile file) throws IOException {
		Resource resource = new SmooksGraphicsExtResourceFactoryImpl().createResource(URI.createPlatformResourceURI(file.getFullPath()
				.toPortableString(), false));
		resource.load(Collections.emptyMap());
		if (resource.getContents().size() > 0) {
			Object obj = resource.getContents().get(0);
			if (obj instanceof DocumentRoot) {
				return ((DocumentRoot) obj).getSmooksGraphicsExt();
			}
		}
		return null;
	}

	public static void createCDATAFieldEditor(String label, AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent,
			Object model) {
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		Composite space = toolkit.createComposite(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 10;
		space.setLayoutData(gd);

		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.DESCRIPTION);
		FillLayout layout = new FillLayout();
		section.setLayout(layout);
		section.setText(label);

		Composite textComposite = toolkit.createComposite(section);
		section.setClient(textComposite);
		textComposite.setLayout(new GridLayout());
		final Text cdataText = toolkit.createText(textComposite, "", SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
		toolkit.paintBordersFor(textComposite);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 300;
		cdataText.setLayoutData(gd);

		if (model instanceof AnyType) {
			String cdata = SmooksModelUtils.getAnyTypeCDATA((AnyType) model);
			if (cdata != null) {
				cdataText.setText(cdata);
				if (cdata.length() > 0) {
					section.setExpanded(true);
				}
			}
		}

		final Object fm = model;
		final AdapterFactoryEditingDomain fEditingDomain = editingdomain;
		cdataText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!(fm instanceof AnyType)) {
					return;
				}
				String text = SmooksModelUtils.getAnyTypeCDATA((AnyType) fm);
				if (!cdataText.getText().equals(text)) {
					SmooksModelUtils.setCDATAToSmooksType(fEditingDomain, (AnyType) fm, cdataText.getText());
				}
			}
		});
	}

	public static void createCommentFieldEditor(String label, AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent,
			Object model) {
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		Composite space = toolkit.createComposite(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 10;
		space.setLayoutData(gd);

		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.DESCRIPTION);
		FillLayout layout = new FillLayout();
		section.setLayout(layout);
		section.setText(label);

		Composite textComposite = toolkit.createComposite(section);
		section.setClient(textComposite);
		textComposite.setLayout(new GridLayout());
		final Text cdataText = toolkit.createText(textComposite, "", SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 300;
		cdataText.setLayoutData(gd);

		if (model instanceof AnyType) {
			String comment = SmooksModelUtils.getAnyTypeComment((AnyType) model);
			if (comment != null) {
				cdataText.setText(comment);
				if (comment.length() > 0)
					section.setExpanded(true);
			}
		}
		final Object fm = model;
		final AdapterFactoryEditingDomain fEditingDomain = editingdomain;
		cdataText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!(fm instanceof AnyType)) {
					return;
				}
				String text = SmooksModelUtils.getAnyTypeCDATA((AnyType) fm);
				if (!cdataText.getText().equals(text)) {
					SmooksModelUtils.setCommentToSmooksType(fEditingDomain, (AnyType) fm, cdataText.getText());
				}
			}
		});
	}

	public static Composite createJavaTypeSearchFieldEditor(Composite parent, FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor,
			final EObject model) {
		if (model instanceof EObject) {
			final Resource resource = ((EObject) model).eResource();
			URI uri = resource.getURI();
			IResource workspaceResource = null;
			if (uri.isPlatformResource()) {
				String path = uri.toPlatformString(true);
				workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
				JavaTypeFieldDialog dialog = new JavaTypeFieldDialog(workspaceResource);
				Hyperlink link = (Hyperlink) createFiledEditorLabel(parent, toolkit, propertyDescriptor, model, true);
				final Composite classTextComposite = toolkit.createComposite(parent);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				classTextComposite.setLayoutData(gd);
				FillLayout fillLayout = new FillLayout();
				fillLayout.marginHeight = 0;
				fillLayout.marginWidth = 0;
				classTextComposite.setLayout(fillLayout);
				final SearchComposite searchComposite = new SearchComposite(classTextComposite, toolkit, "Search Class", dialog, SWT.NONE);
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
									MessageDialog.openInformation(classTextComposite.getShell(), "Can't find type", "Can't find type \"" + typeName
											+ "\" in \"" + javaProject.getProject().getName() + "\" project.");
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

	public static Composite createJavaMethodSearchFieldEditor(BindingsType container, Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, String buttonName, final EObject model) {
		String classString = ((BindingsType) container).getClass_();
		IJavaProject project = getJavaProject(container);
		try {
			ProjectClassLoader classLoader = new ProjectClassLoader(project);
			Class<?> clazz = classLoader.loadClass(classString);
			JavaMethodsSelectionDialog dialog = new JavaMethodsSelectionDialog(project, clazz);
			return SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor, "Select method", dialog, (EObject) model);
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

	public static String generateFullPath(IXMLStructuredObject node, final String sperator) {
		return generatePath(node, getRootParent(node), sperator, true);
	}

	public static IXMLStructuredObject getRootParent(IXMLStructuredObject child) {
		IXMLStructuredObject parent = child.getParent();
		if (child.isRootNode())
			return child;
		if (parent == null || parent.isRootNode())
			return child;
		IXMLStructuredObject temp = parent;
		while (temp != null && !temp.isRootNode()) {
			parent = temp;
			temp = temp.getParent();
		}
		return parent;
	}

	public static String generatePath(IXMLStructuredObject node, SelectorAttributes selectorAttributes) {
		String sperator = selectorAttributes.getSelectorSperator();
		String policy = selectorAttributes.getSelectorPolicy();
		if (sperator == null)
			sperator = " ";
		if (policy == null)
			policy = SelectorAttributes.FULL_PATH;
		if (policy.equals(SelectorAttributes.FULL_PATH)) {
			return generateFullPath(node, sperator);
		}
		if (policy.equals(SelectorAttributes.INCLUDE_PARENT)) {
			return generatePath(node, node.getParent(), sperator, true);
		}
		if (policy.equals(SelectorAttributes.IGNORE_ROOT)) {

		}
		if (policy.equals(SelectorAttributes.ONLY_NAME)) {
			return node.getNodeName();
		}
		return generateFullPath(node, sperator);
	}

	public static String generatePath(IXMLStructuredObject startNode, IXMLStructuredObject stopNode, final String sperator, boolean includeContext) {
		String name = "";
		if (startNode == stopNode) {
			return startNode.getNodeName();
		}
		List<IXMLStructuredObject> nodeList = new ArrayList<IXMLStructuredObject>();
		IXMLStructuredObject temp = startNode;
		if (stopNode != null) {
			while (temp != stopNode.getParent() && temp != null) {
				nodeList.add(temp);
				temp = temp.getParent();
			}
		}
		int length = nodeList.size();
		if (!includeContext) {
			length--;
		}
		for (int i = 0; i < length; i++) {
			IXMLStructuredObject n = nodeList.get(i);
			String nodeName = n.getNodeName();
			if (n.isAttribute()) {
				nodeName = "@" + nodeName;
			}
			name = sperator + nodeName + name;
		}
		return name.trim();
	}

	public static SmooksResourceListType getSmooks11ResourceListType(EObject model) {
		if (model instanceof SmooksResourceListType)
			return (SmooksResourceListType) model;
		EObject parent = model;
		while (parent != null) {
			parent = parent.eContainer();
			if (parent instanceof SmooksResourceListType)
				return (SmooksResourceListType) parent;
		}
		return null;
	}

	public static Control createContionsChoiceFieldEditor(Composite parent, FormToolkit formToolkit, IItemPropertyDescriptor itemPropertyDescriptor,
			Object model) {
		SmooksResourceListType listType = getSmooks11ResourceListType((EObject) model);
		List<String> names = new ArrayList<String>();
		if (listType != null) {
			List<ConditionType> conditions = SmooksModelUtils.collectConditionType(listType);
			for (Iterator<ConditionType> iterator = conditions.iterator(); iterator.hasNext();) {
				ConditionType conditionType = (ConditionType) iterator.next();
				String id = conditionType.getId();
				if (id != null && id.length() != 0) {
					names.add(id);
				}
			}
		}
		return createChoiceFieldEditor(parent, formToolkit, itemPropertyDescriptor, model, names.toArray(new String[] {}), null, false);
	}

	public static Control createChoiceFieldEditor(Composite parent, FormToolkit formToolkit, IItemPropertyDescriptor itemPropertyDescriptor,
			Object model, String[] items, IModelProcsser processer, boolean readOnly) {
		SmooksUIUtils.createFiledEditorLabel(parent, formToolkit, itemPropertyDescriptor, model, false);
		Object editValue = getEditValue(itemPropertyDescriptor, model);
		if (processer != null) {
			editValue = processer.unwrapValue(editValue);
		}
		int currentSelect = -1;
		int style = SWT.BORDER;
		if (readOnly) {
			style = style | SWT.READ_ONLY;
		}
		final Combo combo = new Combo(parent, style);
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				combo.add(items[i]);
				if (items[i].equals(editValue)) {
					currentSelect = i;
				}
			}
		}
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gd);
		if (currentSelect != -1) {
			combo.select(currentSelect);
		}
		final Object fm = model;
		final IItemPropertyDescriptor fipd = itemPropertyDescriptor;
		final IModelProcsser fp = processer;
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = combo.getText();
				Object setValue = text;
				if (fp != null) {
					setValue = fp.wrapValue(text);
				}
				if (setValue.equals(getEditValue(fipd, fm))) {
					return;
				}
				fipd.setPropertyValue(fm, setValue);
			}
		});
		return combo;
	}

	public static Composite createJavaPropertySearchFieldEditor(BindingsType container, Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, String buttonName, final EObject model) {
		String classString = ((BindingsType) container).getClass_();
		IJavaProject project = getJavaProject(container);
		try {
			ProjectClassLoader classLoader = new ProjectClassLoader(project);
			Class<?> clazz = classLoader.loadClass(classString);
			JavaPropertiesSelectionDialog dialog = new JavaPropertiesSelectionDialog(project, clazz);
			return SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor, "Select property", dialog, (EObject) model);
		} catch (Exception e) {
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

	public static Composite createDialogFieldEditor(Composite parent, FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor,
			String buttonName, IFieldDialog dialog, final EObject model) {
		return createDialogFieldEditor(parent, toolkit, propertyDescriptor, buttonName, dialog, model, false, null);
	}

	public static Composite createDialogFieldEditor(Composite parent, FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor,
			String buttonName, IFieldDialog dialog, final EObject model, boolean labelLink, IHyperlinkListener listener) {
		Control label = createFiledEditorLabel(parent, toolkit, propertyDescriptor, model, labelLink);
		if (label instanceof Hyperlink && listener != null) {
			((Hyperlink) label).addHyperlinkListener(listener);
		}
		final Composite classTextComposite = toolkit.createComposite(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		classTextComposite.setLayoutData(gd);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 0;
		fillLayout.marginWidth = 0;
		classTextComposite.setLayout(fillLayout);
		final SearchComposite searchComposite = new SearchComposite(classTextComposite, toolkit, buttonName, dialog, SWT.NONE);
		Object editValue = getEditValue(propertyDescriptor, model);
		if (editValue != null) {
			searchComposite.getText().setText(editValue.toString());
		}
		searchComposite.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object editValue = getEditValue(propertyDescriptor, model);
				Object value = searchComposite.getText().getText();
				if (editValue != null) {
					if (!editValue.equals(value)) {
						propertyDescriptor.setPropertyValue(model, value);
					}
				} else {
					propertyDescriptor.setPropertyValue(model, value);
				}
			}
		});
		toolkit.paintBordersFor(classTextComposite);
		return classTextComposite;
	}

	public static void showErrorDialog(Shell shell, Status status) {
		ErrorDialog.openError(shell, "Error", "error", status); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static Status createErrorStatus(Throwable throwable, String message) {
		while (throwable != null && throwable instanceof InvocationTargetException) {
			throwable = ((InvocationTargetException) throwable).getTargetException();
		}
		return new Status(Status.ERROR, SmooksConfigurationActivator.PLUGIN_ID, message, throwable);
	}

	public static Status createErrorStatus(Throwable throwable) {
		return createErrorStatus(throwable, "Error"); //$NON-NLS-1$
	}

}
