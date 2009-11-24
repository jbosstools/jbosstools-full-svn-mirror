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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.actions.AddSmooksResourceAction;
import org.jboss.tools.smooks.configuration.actions.OpenEditorEditInnerContentsAction;
import org.jboss.tools.smooks.configuration.command.UnSetFeatureCommand;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.ClassPathFileProcessor;
import org.jboss.tools.smooks.configuration.editors.CurrentProjecViewerFilter;
import org.jboss.tools.smooks.configuration.editors.FieldMarkerComposite;
import org.jboss.tools.smooks.configuration.editors.FileSelectionWizard;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.IFilePathProcessor;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.OpenFileHyperLinkListener;
import org.jboss.tools.smooks.configuration.editors.SelectorAttributes;
import org.jboss.tools.smooks.configuration.editors.SelectorCreationDialog;
import org.jboss.tools.smooks.configuration.editors.groovy.GroovyUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaMethodsSelectionDialog;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaPropertiesSelectionDialog;
import org.jboss.tools.smooks.contentassist.TypeContentProposalListener;
import org.jboss.tools.smooks.contentassist.TypeContentProposalProvider;
import org.jboss.tools.smooks.contentassist.TypeProposalLabelProvider;
import org.jboss.tools.smooks.core.SmooksCoreActivator;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.calc.Counter;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.csv12.CSV12Reader;
import org.jboss.tools.smooks.model.csv12.Csv12Package;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.datasource.Direct;
import org.jboss.tools.smooks.model.edi.EDIReader;
import org.jboss.tools.smooks.model.edi.EdiPackage;
import org.jboss.tools.smooks.model.edi12.EDI12Reader;
import org.jboss.tools.smooks.model.edi12.Edi12Package;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage;
import org.jboss.tools.smooks.model.esbrouting.RouteBean;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;
import org.jboss.tools.smooks.model.fileRouting.OutputStream;
import org.jboss.tools.smooks.model.freemarker.BindTo;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;
import org.jboss.tools.smooks.model.graphics.ext.util.GraphResourceFactoryImpl;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.ExpressionType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;
import org.jboss.tools.smooks.model.jmsrouting.JmsRouter;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting12.JMS12Router;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.json12.Json12Package;
import org.jboss.tools.smooks.model.json12.Json12Reader;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.rules10.RuleBasesType;
import org.jboss.tools.smooks.model.rules10.Rules10Package;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.ConditionType;
import org.jboss.tools.smooks.model.smooks.ReaderType;
import org.jboss.tools.smooks.model.smooks.ResourceConfigType;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.validation10.Validation10Package;
import org.jboss.tools.smooks.model.xsl.Xsl;
import org.jboss.tools.smooks.model.xsl.XslPackage;
import org.jboss.tools.smooks10.model.smooks.DocumentRoot;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class SmooksUIUtils {

	public static String[] SMOOKS_PLATFORM_1_2_SPECIAL_NAMESPACES = new String[] { Javabean12Package.eNS_URI,
			Csv12Package.eNS_URI, Edi12Package.eNS_URI, Jmsrouting12Package.eNS_URI, Json12Package.eNS_URI,
			Persistence12Package.eNS_URI, Rules10Package.eNS_URI, Validation10Package.eNS_URI };

	public static String[] SMOOKS_PLATFORM_1_1_CONFLICT_NAMESPACES = new String[] { JavabeanPackage.eNS_URI,
			CsvPackage.eNS_URI, EdiPackage.eNS_URI, JmsroutingPackage.eNS_URI, JsonPackage.eNS_URI };

	public static final String FILE_PRIX = "File:/"; //$NON-NLS-1$

	public static final String WORKSPACE_PRIX = "Workspace:/"; //$NON-NLS-1$

	public static final String RESOURCE = "Resource:/";

	public static final String XSL_NAMESPACE = " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ";

	public static int VALUE_TYPE_VALUE = 1;

	public static int VALUE_TYPE_TEXT = 2;

	public static int VALUE_TYPE_COMMENT = 3;

	public static int VALUE_TYPE_CDATA = 0;

	public static final int SELECTOR_EXPAND_MAX_LEVEL = 5;

	public static final char[] allEnglishCharas = new char[] { 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f',
			'F', 'g', 'G', 'h', 'H', 'i', 'I', 'j', 'J', 'k', 'K', 'l', 'L', 'm', 'M', 'n', 'N', 'o', 'O', 'p', 'P',
			'q', 'Q', 'r', 'R', 's', 'S', 't', 'T', 'u', 'U', 'v', 'V', 'w', 'W', 'x', 'X', 'y', 'Y' };

	public static final String[] SELECTOR_SPERATORS = new String[] { " ", "/" };

	private static void fillBeanIdStringList(EObject model, final Collection<String> beanIdList) {
		EStructuralFeature beanIDFeature = getBeanIDFeature(model);
		if (beanIDFeature != null) {
			Object data = model.eGet(beanIDFeature);
			if (data != null) {
				String beanId = data.toString();
				if (!beanIdList.contains(beanId))
					beanIdList.add(beanId);
			}
		}
		List<EObject> children = model.eContents();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			EObject eObject = (EObject) iterator.next();
			fillBeanIdStringList(eObject, beanIdList);
		}
	}

	public static Collection<EObject> getBeanIdModelList(EObject model) {
		List<EObject> beanIdModelList = new ArrayList<EObject>();
		fillBeanIdModelList(model, beanIdModelList);
		return beanIdModelList;
	}

	public static void fillBeanIdModelList(EObject model, final List<EObject> list) {
		EStructuralFeature beanIDFeature = getBeanIDFeature(model);
		if (beanIDFeature != null) {
			list.add(model);
		}
		List<EObject> children = model.eContents();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			EObject eObject = (EObject) iterator.next();
			fillBeanIdModelList(eObject, list);
		}
	}

	public static Collection<EObject> getBeanIdRefModelList(EObject model) {
		List<EObject> beanIdRefModelList = new ArrayList<EObject>();
		fillBeanIdRefModelList(model, beanIdRefModelList);
		return beanIdRefModelList;
	}

	private static void fillBeanIdRefModelList(EObject model, List<EObject> beanIdRefModelList) {
		EStructuralFeature beanIDRefFeature = getBeanIDRefFeature(model);
		if (beanIDRefFeature != null) {
			beanIdRefModelList.add(model);
		}
		List<EObject> children = model.eContents();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			EObject eObject = (EObject) iterator.next();
			fillBeanIdRefModelList(eObject, beanIdRefModelList);
		}
	}

	public static List<String> getBeanIdStringList(SmooksResourceListType resourceList) {
		if (resourceList == null) {
			return null;
		}
		List<String> beanIdList = new ArrayList<String>();

		fillBeanIdStringList(resourceList, beanIdList);

		// for (Iterator<?> iterator = rlist.iterator(); iterator.hasNext();) {
		// AbstractResourceConfig abstractResourceConfig =
		// (AbstractResourceConfig) iterator.next();
		// EStructuralFeature beanIDFeature =
		// getBeanIDFeature(abstractResourceConfig);
		// if (beanIDFeature != null) {
		// Object data = abstractResourceConfig.eGet(beanIDFeature);
		// if (data != null) {
		// String beanId = data.toString();
		// if (!beanIdList.contains(beanId))
		// beanIdList.add(beanId);
		// }
		// }
		// if (abstractResourceConfig instanceof BindingsType) {
		// String beanId = ((BindingsType) abstractResourceConfig).getBeanId();
		// if (beanId == null)
		// continue;
		// if (!beanIdList.contains(beanId))
		// beanIdList.add(beanId);
		// }
		// if (abstractResourceConfig instanceof Freemarker) {
		// Use use = ((Freemarker) abstractResourceConfig).getUse();
		// if (use != null) {
		// BindTo bindTo = use.getBindTo();
		// if (bindTo != null) {
		// String beanId = ((BindTo) bindTo).getId();
		// if (beanId == null)
		// continue;
		// if (!beanIdList.contains(beanId))
		// beanIdList.add(beanId);
		// }
		// }
		//
		// }
		// if (abstractResourceConfig instanceof Xsl) {
		// org.jboss.tools.smooks.model.xsl.Use use = ((Xsl)
		// abstractResourceConfig).getUse();
		// if (use != null) {
		// org.jboss.tools.smooks.model.xsl.BindTo bindTo = use.getBindTo();
		// if (bindTo != null) {
		// String beanId = ((org.jboss.tools.smooks.model.xsl.BindTo)
		// bindTo).getId();
		// if (beanId == null)
		// continue;
		// if (!beanIdList.contains(beanId))
		// beanIdList.add(beanId);
		// }
		// }
		//
		// }
		// }
		return beanIdList;
	}

	public static List<BindingsType> getBindingsTypeList(SmooksResourceListType resourceList) {
		if (resourceList == null) {
			return null;
		}
		List<AbstractResourceConfig> rlist = resourceList.getAbstractResourceConfig();
		List<BindingsType> beanIdList = new ArrayList<BindingsType>();
		for (Iterator<?> iterator = rlist.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if (abstractResourceConfig instanceof BindingsType) {
				beanIdList.add((BindingsType) abstractResourceConfig);
			}
		}
		return beanIdList;
	}

	public static List<BeanType> getBeanTypeList(SmooksResourceListType resourceList) {
		if (resourceList == null) {
			return null;
		}
		List<AbstractResourceConfig> rlist = resourceList.getAbstractResourceConfig();
		List<BeanType> beanIdList = new ArrayList<BeanType>();
		for (Iterator<?> iterator = rlist.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if (abstractResourceConfig instanceof BeanType) {
				beanIdList.add((BeanType) abstractResourceConfig);
			}
		}
		return beanIdList;
	}

	public static AttributeFieldEditPart createMixedTextFieldEditor(String label,
			AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			boolean linkLabel, IHyperlinkListener listener) {
		return createMixedTextFieldEditor(label, editingdomain, toolkit, parent, model, false, 0, linkLabel, false,
				listener, null);
	}

	public static AttributeFieldEditPart createMultiMixedTextFieldEditor(String label,
			AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model, int height,
			OpenEditorEditInnerContentsAction action) {
		return createMixedTextFieldEditor(label, editingdomain, toolkit, parent, model, true, height, false, false,
				null, action);
	}

	public static AttributeFieldEditPart createMixedTextFieldEditor(String label,
			AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			boolean multiText, int height, boolean linkLabel, boolean openFile, IHyperlinkListener listener,
			OpenEditorEditInnerContentsAction action) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, null, model, multiText, linkLabel,
				openFile, height, listener, VALUE_TYPE_TEXT, action);
	}

	public static FieldMarkerWrapper createFieldEditorLabel(Composite parent, FormToolkit formToolKit,
			IItemPropertyDescriptor itemPropertyDescriptor, Object model, boolean isLink) {
		return createFieldEditorLabel(null, parent, formToolKit, itemPropertyDescriptor, model, isLink);
	}

	public static FieldMarkerWrapper createFieldEditorLabel(String labelText, Composite parent,
			FormToolkit formToolKit, IItemPropertyDescriptor itemPropertyDescriptor, Object model, boolean isLink) {
		FieldMarkerWrapper wrapper = new FieldMarkerWrapper();
		String description = labelText;
		if (itemPropertyDescriptor != null) {
			description = itemPropertyDescriptor.getDescription(model);
		}
		String displayName = labelText;
		if (itemPropertyDescriptor == null) {
		} else {
			if (displayName == null) {
				displayName = itemPropertyDescriptor.getDisplayName(model);
				EAttribute feature = (EAttribute) itemPropertyDescriptor.getFeature(model);
				if (feature.isRequired()) {
					displayName = displayName + "*";
				}
			}
		}
		Composite labelComposite = formToolKit.createComposite(parent);
		// GridLayout layout = new GridLayout();
		// layout.numColumns = 2;
		// layout.marginLeft = 0;
		// layout.marginRight = 0;
		// layout.horizontalSpacing = 0;
		labelComposite.setLayout(new FillLayout());
		// GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Control labelControl = null;

		if (!isLink) {
			Label label = formToolKit.createLabel(labelComposite, displayName + " :");
			label.setForeground(formToolKit.getColors().getColor(IFormColors.TITLE));
			labelControl = label;
		} else {
			Hyperlink link = formToolKit.createHyperlink(labelComposite, displayName + " :", SWT.NONE);
			labelControl = link;
		}
		if (description != null) {
			labelControl.setToolTipText(description);
		}
		// gd = new GridData();
		// labelControl.setLayoutData(gd);

		// FieldMarkerComposite notificationComposite = new
		// FieldMarkerComposite(labelComposite, SWT.NONE);
		// gd = new GridData();
		// gd.heightHint = 8;
		// gd.widthHint = 8;
		// gd.horizontalAlignment = GridData.BEGINNING;
		// gd.verticalAlignment = GridData.BEGINNING;
		// notificationComposite.setLayoutData(gd);

		wrapper.setLabelControl(labelControl);
		// wrapper.setMarker(notificationComposite);
		return wrapper;
	}

	/**
	 * Can't use
	 * 
	 * @param editingdomain
	 * @param toolkit
	 * @param parent
	 * @param model
	 */
	public static void createFilePathFieldEditor(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model) {
		// IHyperlinkListener link
	}

	public static void createLinkMixedTextFieldEditor(String label, AdapterFactoryEditingDomain editingdomain,
			FormToolkit toolkit, Composite parent, Object model, boolean multiText, int height, boolean linkLabel,
			IHyperlinkListener listener) {
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
			IItemPropertyDescriptor propertyDescriptor, FormToolkit toolkit, Composite parent, Object model,
			boolean multiText, int height, boolean linkLabel, IHyperlinkListener listener) {
		FieldMarkerWrapper warpper = createFieldEditorLabel(parent, toolkit, propertyDescriptor, model, linkLabel);
		Control control = warpper.getLabelControl();
		if (linkLabel) {
			Hyperlink link = (Hyperlink) control;
			if (listener != null) {
				link.addHyperlinkListener(listener);
			}
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
		if (path == null)
			return null;
		if (new File(path).exists()) {
			return path;
		}
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

	public static AttributeFieldEditPart createStringFieldEditor(final Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor itemPropertyDescriptor, Object model, boolean linkLabel, boolean openFile,
			IHyperlinkListener listener) {
		return createStringFieldEditor(null, parent, null, toolkit, itemPropertyDescriptor, model, false, linkLabel,
				openFile, 0, listener, VALUE_TYPE_VALUE, null);
	}

	public static AttributeFieldEditPart createFileSelectionTextFieldEditor(String label, final Composite parent,
			EditingDomain editingdomain, FormToolkit toolkit, final IItemPropertyDescriptor itemPropertyDescriptor,
			final Object model, int valueType, String editorID, OpenEditorEditInnerContentsAction action) {
		OpenFileHyperLinkListener listener = new OpenFileHyperLinkListener(valueType, itemPropertyDescriptor, model,
				editorID);
		return createStringFieldEditor(label, parent, editingdomain, toolkit, itemPropertyDescriptor, model, false,
				true, true, 0, listener, valueType, action);
	}

	public static boolean isLinuxOS() {
		Object osName = System.getProperties().get("os.name");
		if (osName != null && "linux".equalsIgnoreCase(osName.toString())) {
			return true;
		}
		return false;
	}

	public static AttributeFieldEditPart createStringFieldEditor(String label, final Composite parent,
			EditingDomain editingdomain, FormToolkit toolkit, final IItemPropertyDescriptor itemPropertyDescriptor,
			Object model, boolean multiText, boolean linkLabel, boolean openFile, int height,
			IHyperlinkListener listener, int valueType, OpenEditorEditInnerContentsAction openEditorAction) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, itemPropertyDescriptor, model, multiText,
				linkLabel, openFile, height, listener, valueType, openEditorAction, false);
	}

	public static AttributeFieldEditPart createNumberFieldEditor(String label, final Composite parent,
			FormToolkit toolkit, final IItemPropertyDescriptor itemPropertyDescriptor, Object model) {
		AttributeFieldEditPart fieldEditPart = new AttributeFieldEditPart();
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		Section section = null;
		Composite textContainer = parent;
		final Object fm = model;
		if (label == null && itemPropertyDescriptor != null) {
			label = itemPropertyDescriptor.getDisplayName(model);
			EAttribute feature = (EAttribute) itemPropertyDescriptor.getFeature(model);
			if (feature.isRequired()) {
				label = label + "*";
			}
		}
		FieldMarkerWrapper warpper = createFieldEditorLabel(label, parent, toolkit, itemPropertyDescriptor, model,
				false);
		fieldEditPart.setFieldMarker(warpper.getMarker());

		Object editValue = getEditValue(itemPropertyDescriptor, model);

		gd = new GridData(GridData.FILL_HORIZONTAL);

		int textType = SWT.FLAT;
		if (isLinuxOS()) {
			textType = SWT.BORDER;
		}
		Composite tcom = toolkit.createComposite(textContainer);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.horizontalSpacing = 0;
		tcom.setLayout(layout);

		FieldMarkerComposite notificationComposite = new FieldMarkerComposite(tcom, SWT.NONE);
		gd = new GridData();
		gd.heightHint = 8;
		gd.widthHint = 8;
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalAlignment = GridData.BEGINNING;
		notificationComposite.setLayoutData(gd);
		fieldEditPart.setFieldMarker(notificationComposite);

		final Text valueText = toolkit.createText(tcom, "", textType);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		valueText.setLayoutData(gd);

		tcom.setLayoutData(gd);

		toolkit.paintBordersFor(textContainer);

		boolean valueIsSet = true;
		if (model != null && model instanceof EObject && itemPropertyDescriptor != null) {
			valueIsSet = ((EObject) model).eIsSet((EAttribute) itemPropertyDescriptor.getFeature(model));
		}
		if (editValue != null && valueIsSet && editValue instanceof Integer) {
			valueText.setText(editValue.toString());
		}
		if (itemPropertyDescriptor != null) {
			valueText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					Object editValue = getEditValue(itemPropertyDescriptor, fm);
					if (editValue != null) {
						String vt = valueText.getText();
						Integer newValue = null;
						try {
							newValue = Integer.parseInt(vt);
						} catch (Throwable t) {
							return;
						}
						if (vt == null || vt.length() == 0) {
							itemPropertyDescriptor.setPropertyValue(fm, null);
						} else {
							if (!editValue.equals(newValue)) {
								itemPropertyDescriptor.setPropertyValue(fm, newValue);
							}
						}
					} else {
						String vt = valueText.getText();
						Integer newValue = null;
						try {
							newValue = Integer.parseInt(vt);
						} catch (Throwable t) {
							return;
						}
						itemPropertyDescriptor.setPropertyValue(fm, newValue);
					}

				}
			});
		}
		if (section != null)
			section.layout();
		fieldEditPart.setContentControl(valueText);
		return fieldEditPart;
	}

	public static AttributeFieldEditPart createStringFieldEditor(String label, final Composite parent,
			EditingDomain editingdomain, FormToolkit toolkit, final IItemPropertyDescriptor itemPropertyDescriptor,
			Object model, boolean multiText, boolean linkLabel, boolean openFile, int height,
			IHyperlinkListener listener, int valueType, OpenEditorEditInnerContentsAction openEditorAction,
			boolean expandEditor) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, itemPropertyDescriptor, model, multiText,
				linkLabel, openFile, new ClassPathFileProcessor(), height, listener, valueType, openEditorAction,
				expandEditor);
	}

	public static AttributeFieldEditPart createStringFieldEditor(String label, final Composite parent,
			EditingDomain editingdomain, FormToolkit toolkit, final IItemPropertyDescriptor itemPropertyDescriptor,
			Object model, boolean multiText, boolean linkLabel, boolean openFile, IFilePathProcessor filePathProcessor,
			int height, IHyperlinkListener listener, int valueType, OpenEditorEditInnerContentsAction openEditorAction,
			boolean expandEditor) {
		AttributeFieldEditPart fieldEditPart = new AttributeFieldEditPart();
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		Section section = null;
		Composite textContainer = null;
		final Object fm = model;
		final EditingDomain fEditingDomain = editingdomain;
		if (label == null && itemPropertyDescriptor != null) {
			label = itemPropertyDescriptor.getDisplayName(model);
			EAttribute feature = (EAttribute) itemPropertyDescriptor.getFeature(model);
			if (feature.isRequired()) {
				label = label + "*";
			}
		}
		if (multiText) {
			Composite space = toolkit.createComposite(parent);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			gd.heightHint = 10;
			space.setLayoutData(gd);

			section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE);
			section.setExpanded(expandEditor);
			FillLayout layout = new FillLayout();
			section.setLayout(layout);
			section.setText(label);
			if (openEditorAction != null) {
				ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);

				ToolBar toolbar = toolBarManager.createControl(section);
				final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
				toolbar.setCursor(handCursor);
				// Cursor needs to be explicitly disposed
				toolbar.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						if ((handCursor != null) && (handCursor.isDisposed() == false)) {
							handCursor.dispose();
						}
					}
				});
				toolBarManager.add(openEditorAction);
				toolBarManager.update(true);
				section.setTextClient(toolbar);
				section.layout();
			}

			Composite textComposite = toolkit.createComposite(section);
			section.setClient(textComposite);
			textComposite.setLayout(new GridLayout());
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			section.setLayoutData(gd);
			textContainer = textComposite;
		} else {
			FieldMarkerWrapper warpper = createFieldEditorLabel(label, parent, toolkit, itemPropertyDescriptor, model,
					linkLabel);
			fieldEditPart.setFieldMarker(warpper.getMarker());
			Control labelControl = warpper.getLabelControl();
			if (labelControl instanceof Hyperlink) {
				Hyperlink link = (Hyperlink) labelControl;
				if (listener != null) {
					link.addHyperlinkListener(listener);
				}
			}
			if (openFile) {
				Composite fileSelectionComposite = toolkit.createComposite(parent);
				GridLayout gl = new GridLayout();
				gl.numColumns = 2;
				gd = new GridData(GridData.FILL_HORIZONTAL);
				fileSelectionComposite.setLayoutData(gd);
				gl.marginHeight = 0;
				gl.marginWidth = 0;
				fileSelectionComposite.setLayout(gl);
				textContainer = fileSelectionComposite;
			} else {
				textContainer = parent;
			}
		}
		String editValue = null;
		if (valueType == VALUE_TYPE_TEXT && model instanceof AnyType) {
			editValue = SmooksModelUtils.getAnyTypeText((AnyType) model);
		}
		if (valueType == VALUE_TYPE_COMMENT && model instanceof AnyType) {
			editValue = SmooksModelUtils.getAnyTypeComment((AnyType) model);
		}
		if (valueType == VALUE_TYPE_CDATA && model instanceof AnyType) {
			editValue = SmooksModelUtils.getAnyTypeCDATA((AnyType) model);
		}
		if (valueType == VALUE_TYPE_VALUE) {
			Object value = getEditValue(itemPropertyDescriptor, model);
			if (value != null) {
				editValue = value.toString();
			} else {
			}
		}

		gd = new GridData(GridData.FILL_HORIZONTAL);

		int textType = SWT.FLAT;
		if (isLinuxOS()) {
			textType = SWT.BORDER;
		}
		if (multiText) {
			textType = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
		}
		Composite tcom = toolkit.createComposite(textContainer);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.horizontalSpacing = 0;
		tcom.setLayout(layout);

		FieldMarkerComposite notificationComposite = new FieldMarkerComposite(tcom, SWT.NONE);
		gd = new GridData();
		gd.heightHint = 8;
		gd.widthHint = 8;
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalAlignment = GridData.BEGINNING;
		notificationComposite.setLayoutData(gd);
		fieldEditPart.setFieldMarker(notificationComposite);

		final Text valueText = toolkit.createText(tcom, "", textType);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		if (multiText && height > 0) {
			gd.heightHint = height;
		}
		valueText.setLayoutData(gd);

		tcom.setLayoutData(gd);

		toolkit.paintBordersFor(textContainer);
		if (openFile) {
			final IFilePathProcessor processor = filePathProcessor;
			Button fileBrowseButton = toolkit.createButton(textContainer, "Browse", SWT.NONE);
			fileBrowseButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					FileSelectionWizard wizard = new FileSelectionWizard();
					IResource resource = getResource((EObject) fm);
					Object[] initSelections = new Object[] {};
					if (resource != null) {
						initSelections = new Object[] { resource };
					}
					wizard.setFilePathProcessor(processor);
					wizard.setInitSelections(initSelections);
					List<ViewerFilter> filterList = new ArrayList<ViewerFilter>();
					filterList.add(new CurrentProjecViewerFilter(resource));
					wizard.setViewerFilters(filterList);
					WizardDialog dialog = new WizardDialog(parent.getShell(), wizard);
					if (dialog.open() == Dialog.OK) {
						valueText.setText(wizard.getFilePath());
					}
				}

			});
		}
		// boolean valueIsSet = true;
		if (model != null && model instanceof EObject && itemPropertyDescriptor != null) {
			// valueIsSet = ((EObject) model).eIsSet((EAttribute)
			// itemPropertyDescriptor.getFeature(model));
		}
		if (editValue != null) {
			valueText.setText(editValue);
			if (editValue.length() > 0 && section != null) {
				section.setExpanded(true);
			}
		}
		if (valueType == VALUE_TYPE_TEXT && model instanceof AnyType && fEditingDomain != null) {
			valueText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (!(fm instanceof AnyType)) {
						return;
					}
					String text = SmooksModelUtils.getAnyTypeText((AnyType) fm);
					if (valueText.getText() == null || valueText.getText().length() == 0) {
						SmooksModelUtils.setTextToSmooksType(fEditingDomain, (AnyType) fm, null);
					} else {
						if (!valueText.getText().equals(text)) {
							String vt = valueText.getText();
							if (vt != null) {
								vt = vt.replaceAll("\r", "");
							}
							SmooksModelUtils.setTextToSmooksType(fEditingDomain, (AnyType) fm, vt);
						}
					}
				}
			});
		}
		if (valueType == VALUE_TYPE_COMMENT && model instanceof AnyType && fEditingDomain != null) {
			valueText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (!(fm instanceof AnyType)) {
						return;
					}
					String text = SmooksModelUtils.getAnyTypeComment((AnyType) fm);
					if (valueText.getText() == null || valueText.getText().length() == 0) {
						SmooksModelUtils.setCommentToSmooksType(fEditingDomain, (AnyType) fm, null);
					} else {
						if (!valueText.getText().equals(text)) {
							String vt = valueText.getText();
							if (vt != null) {
								vt = vt.replaceAll("\r", "");
							}
							SmooksModelUtils.setCommentToSmooksType(fEditingDomain, (AnyType) fm, vt);
						}
					}
				}
			});
		}
		if (valueType == VALUE_TYPE_CDATA && model instanceof AnyType && fEditingDomain != null) {
			valueText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (!(fm instanceof AnyType)) {
						return;
					}
					String text = SmooksModelUtils.getAnyTypeCDATA((AnyType) fm);
					if (valueText.getText() == null || valueText.getText().length() == 0) {
						SmooksModelUtils.setCDATAToSmooksType(fEditingDomain, (AnyType) fm, null);
					} else {
						if (!valueText.getText().equals(text)) {
							String vt = valueText.getText();
							if (vt != null) {
								vt = vt.replaceAll("\r", "");
							}
							SmooksModelUtils.setCDATAToSmooksType(fEditingDomain, (AnyType) fm, vt);
						}
					}
				}
			});
		}
		if (valueType == VALUE_TYPE_VALUE) {
			if (itemPropertyDescriptor != null) {
				valueText.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						Object editValue = getEditValue(itemPropertyDescriptor, fm);
						if (editValue != null) {
							String vt = valueText.getText();
							if (vt == null || vt.length() == 0) {
								itemPropertyDescriptor.setPropertyValue(fm, null);
							} else {
								if (!editValue.equals(vt)) {
									itemPropertyDescriptor.setPropertyValue(fm, vt);
								}
							}
						} else {
							itemPropertyDescriptor.setPropertyValue(fm, valueText.getText());
						}

					}
				});
			}
		}
		if (section != null)
			section.layout();
		fieldEditPart.setContentControl(valueText);
		return fieldEditPart;
	}

	public static AttributeFieldEditPart createSelectorFieldEditor(FormToolkit toolkit, Composite parent,
			final IItemPropertyDescriptor propertyDescriptor, Object model, final SmooksGraphicsExtType extType,
			final IEditorPart currentEditorPart) {
		return createSelectorFieldEditor(null, toolkit, parent, propertyDescriptor, model, extType, currentEditorPart);
	}

	public static AttributeFieldEditPart createSelectorFieldEditor(String labelText, FormToolkit toolkit,
			Composite parent, final IItemPropertyDescriptor propertyDescriptor, Object model,
			final SmooksGraphicsExtType extType, final IEditorPart currentEditorPart) {
		AttributeFieldEditPart fieldEditPart = createDialogFieldEditor(labelText, parent, toolkit, propertyDescriptor,
				"Browse", new IFieldDialog() {
					public Object open(Shell shell) {
						SelectorCreationDialog dialog = new SelectorCreationDialog(shell, extType, currentEditorPart);
						try {
							if (dialog.open() == Dialog.OK) {
								Object currentSelection = dialog.getCurrentSelection();
								SelectorAttributes sa = dialog.getSelectorAttributes();
								if (currentSelection instanceof IXMLStructuredObject) {
									String s = SmooksUIUtils.generatePath((IXMLStructuredObject) currentSelection, sa);
									return s;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					public IModelProcsser getModelProcesser() {
						return null;
					}

					public void setModelProcesser(IModelProcsser processer) {

					}

				}, (EObject) model);

		SearchComposite sc = (SearchComposite) fieldEditPart.getContentControl();

		final FieldAssistDisposer disposer = addSelectorFieldAssistToText(sc.getText(), extType,
				getSmooks11ResourceListType((EObject) model));
		sc.addDisposeListener(new DisposeListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse
			 * .swt.events.DisposeEvent)
			 */
			public void widgetDisposed(DisposeEvent e) {
				disposer.dispose();
			}

		});
		return fieldEditPart;
	}

	public static Class<?> loadClass(String className, Object resource) throws JavaModelException,
			ClassNotFoundException {
		if (resource == null)
			return null;
		IJavaProject jp = null;
		if (resource instanceof IJavaProject) {
			jp = (IJavaProject) resource;
		}
		if (resource instanceof IResource) {
			IProject project = ((IResource) resource).getProject();
			if (project != null) {
				jp = JavaCore.create(project);
			}
		}
		if (jp != null) {
			ProjectClassLoader loader = new ProjectClassLoader(jp);
			if (className.endsWith("[]")) {
				className = className.substring(0, className.length() - 2);
				Class<?> clazz = loader.loadClass(className);
				Object arrayInstance = Array.newInstance(clazz, 0);
				clazz = arrayInstance.getClass();
				arrayInstance = null;
				return clazz;
			}
			if(className.endsWith("]") && !className.endsWith("[]")){
//				int index = className.indexOf("[");
//				String collectionName = className.substring(0,index);
//				String componentName = className.substring(index + 1 , className.length() - 1);
//				Class<?> clazz = loader.loadClass(className);
//				Object arrayInstance = Array.newInstance(clazz, 0);
//				clazz = arrayInstance.getClass();
//				arrayInstance = null;
//				return clazz;
			}
			return loader.loadClass(className);
		}

		return null;
	}

	public static SmooksGraphicsExtType loadSmooksGraphicsExt(IFile file) throws IOException {
		Resource resource = new GraphResourceFactoryImpl().createResource(URI.createPlatformResourceURI(file
				.getFullPath().toPortableString(), false));
		resource.load(Collections.emptyMap());
		if (resource.getContents().size() > 0) {
			Object obj = resource.getContents().get(0);
			if (obj instanceof SmooksGraphExtensionDocumentRoot) {
				return ((SmooksGraphExtensionDocumentRoot) obj).getSmooksGraphicsExt();
			}
		}
		return null;
	}

	public static AttributeFieldEditPart createTextFieldEditor(String label, AdapterFactoryEditingDomain editingdomain,
			FormToolkit toolkit, Composite parent, Object model, OpenEditorEditInnerContentsAction action) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, null, model, true, true, false, 300,
				null, VALUE_TYPE_TEXT, action);
	}

	public static AttributeFieldEditPart createTextFieldEditor(String label, AdapterFactoryEditingDomain editingdomain,
			FormToolkit toolkit, Composite parent, Object model, OpenEditorEditInnerContentsAction action,
			boolean expanedEditor) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, null, model, true, true, false, 300,
				null, VALUE_TYPE_TEXT, action, expanedEditor);
	}

	public static AttributeFieldEditPart createCDATAFieldEditor(String label,
			AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			OpenEditorEditInnerContentsAction action) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, null, model, true, true, false, 300,
				null, VALUE_TYPE_CDATA, action);
	}

	public static AttributeFieldEditPart createCDATAFieldEditor(String label,
			AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			OpenEditorEditInnerContentsAction action, boolean expanedEditor) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, null, model, true, true, false, 300,
				null, VALUE_TYPE_CDATA, action, expanedEditor);
	}

	public static AttributeFieldEditPart createCommentFieldEditor(String label,
			AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			OpenEditorEditInnerContentsAction action) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, null, model, true, true, false, 300,
				null, VALUE_TYPE_COMMENT, action);
	}

	public static AttributeFieldEditPart createCommentFieldEditor(String label,
			AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			OpenEditorEditInnerContentsAction action, boolean expandEditor) {
		return createStringFieldEditor(label, parent, editingdomain, toolkit, null, model, true, true, false, 300,
				null, VALUE_TYPE_COMMENT, action, expandEditor);
	}

	public static AttributeFieldEditPart createJavaTypeSearchFieldEditor(Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, final EObject model, ISmooksModelProvider modelProvider) {
		if (model instanceof EObject) {
			AttributeFieldEditPart editpart = new AttributeFieldEditPart();
			Resource r = ((EObject) model).eResource();
			if (r == null) {
				r = modelProvider.getSmooksModel().eResource();
			}
			final Resource resource = r;
			if (resource == null)
				return null;
			URI uri = resource.getURI();
			IResource workspaceResource = null;
			if (uri.isPlatformResource()) {
				String path = uri.toPlatformString(true);
				workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
				JavaTypeFieldDialog dialog = new JavaTypeFieldDialog(workspaceResource);
				FieldMarkerWrapper warpper = createFieldEditorLabel(parent, toolkit, propertyDescriptor, model, true);
				editpart.setFieldMarker(warpper.getMarker());
				Hyperlink link = (Hyperlink) warpper.getLabelControl();
				final Composite classTextComposite = toolkit.createComposite(parent);
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				classTextComposite.setLayoutData(gd);
				FillLayout fillLayout = new FillLayout();
				fillLayout.marginHeight = 0;
				fillLayout.marginWidth = 0;
				classTextComposite.setLayout(fillLayout);

				Composite tcom = toolkit.createComposite(classTextComposite);
				GridLayout layout = new GridLayout();
				layout.numColumns = 2;
				layout.marginLeft = 0;
				layout.marginRight = 0;
				layout.horizontalSpacing = 0;
				tcom.setLayout(layout);

				EAttribute attribute = (EAttribute) propertyDescriptor.getFeature(model);

				FieldMarkerComposite notificationComposite = new FieldMarkerComposite(tcom, SWT.NONE);
				gd = new GridData();
				gd.heightHint = 8;
				gd.widthHint = 8;
				gd.horizontalAlignment = GridData.BEGINNING;
				gd.verticalAlignment = GridData.BEGINNING;
				notificationComposite.setLayoutData(gd);
				editpart.setFieldMarker(notificationComposite);

				final SearchComposite searchComposite = new SearchComposite(tcom, toolkit, "Browse", dialog, SWT.NONE);
				gd = new GridData(GridData.FILL_HORIZONTAL);
				searchComposite.setLayoutData(gd);
				Object editValue = getEditValue(propertyDescriptor, model);
				if (editValue != null && model.eIsSet(attribute)) {
					searchComposite.getText().setText(editValue.toString());
				}
				searchComposite.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						Object value = propertyDescriptor.getPropertyValue(model);
						if (value != null && value instanceof PropertyValueWrapper) {
							Object editValue = ((PropertyValueWrapper) value).getEditableValue(model);
							if (searchComposite.getText().getText() == null
									|| searchComposite.getText().getText().length() == 0) {
								propertyDescriptor.setPropertyValue(model, null);
								return;
							}
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
								if (typeName.endsWith("[]")) {
									typeName = typeName.substring(0, typeName.length() - 2);
								}
								IJavaElement result = javaProject.findType(typeName);
								if (result != null)
									JavaUI.openInEditor(result);
								else {
									MessageDialog.openError(classTextComposite.getShell(), "Can't find type",
											"Can't find type \"" + typeName + "\" in \""
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
				final TypeFieldAssistDisposer disposer = SmooksUIUtils.addTypeFieldAssistToText(searchComposite
						.getText(), workspaceResource.getProject(), IJavaSearchConstants.CLASS_AND_INTERFACE);
				classTextComposite.addDisposeListener(new DisposeListener() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.swt.events.DisposeListener#widgetDisposed
					 * (org.eclipse.swt.events.DisposeEvent)
					 */
					public void widgetDisposed(DisposeEvent e) {
						disposer.dispose();
					}
				});
				toolkit.paintBordersFor(classTextComposite);
				editpart.setContentControl(classTextComposite);
				return editpart;
			}
		}
		return null;
	}

	public static IResource getResource(EObject model) {
		if (model == null)
			return null;
		final Resource resource = ((EObject) model).eResource();
		return getResource(resource);
	}

	public static IResource getResource(Resource resource) {
		if (resource == null)
			return null;
		URI uri = resource.getURI();

		if (uri == null)
			return null;
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

	public static AttributeFieldEditPart createJavaMethodSearchFieldEditor(BindingsType container, Composite parent,
			FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor, String buttonName,
			final EObject model) {
		String classString = ((BindingsType) container).getClass_();
		return createJavaMethodSearchFieldEditor(classString, container, parent, toolkit, propertyDescriptor,
				buttonName, model);
	}

	public static AttributeFieldEditPart createJavaMethodSearchFieldEditor(String classString, EObject container,
			Composite parent, FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor, String buttonName,
			final EObject model) {
		IJavaProject project = getJavaProject(container);
		Class<?> clazz = null;
		try {
			ProjectClassLoader classLoader = new ProjectClassLoader(project);
			clazz = classLoader.loadClass(classString);
		} catch (Exception e) {
			// ignore
		}
		JavaMethodsSelectionDialog dialog = new JavaMethodsSelectionDialog(project, clazz);
		AttributeFieldEditPart editPart = SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor,
				buttonName, dialog, (EObject) model);

		Control c = editPart.getContentControl();
		if (c instanceof SearchComposite) {
			Text text = ((SearchComposite) c).getText();
			IResource resource = getResource(model);
			if (resource != null) {
				final FieldAssistDisposer disposer = addJavaSetterMethodFieldAssistToText(text, clazz);
				text.addDisposeListener(new DisposeListener() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.swt.events.DisposeListener#widgetDisposed
					 * (org.eclipse.swt.events.DisposeEvent)
					 */
					public void widgetDisposed(DisposeEvent e) {
						disposer.dispose();
					}

				});
			}
		}
		return editPart;
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

	public static String generatePath(IXMLStructuredObject startNode, IXMLStructuredObject stopNode,
			final String sperator, boolean includeContext) {
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
		if (model instanceof org.jboss.tools.smooks.model.smooks.DocumentRoot) {
			return ((org.jboss.tools.smooks.model.smooks.DocumentRoot) model).getSmooksResourceList();
		}
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

	public static AttributeFieldEditPart createConditionsChoiceFieldEditor(Composite parent, FormToolkit formToolkit,
			IItemPropertyDescriptor itemPropertyDescriptor, Object model) {
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
		return createChoiceFieldEditor(parent, formToolkit, itemPropertyDescriptor, model, names
				.toArray(new String[] {}), null, false);
	}

	public static AttributeFieldEditPart createChoiceFieldEditor(Composite parent, FormToolkit formToolkit,
			IItemPropertyDescriptor itemPropertyDescriptor, Object model, String[] items, IModelProcsser processer,
			boolean readOnly) {
		AttributeFieldEditPart fieldEditPart = new AttributeFieldEditPart();
		FieldMarkerWrapper markerWrapper = SmooksUIUtils.createFieldEditorLabel(parent, formToolkit,
				itemPropertyDescriptor, model, false);
		fieldEditPart.setFieldMarker(markerWrapper.getMarker());
		Object editValue = getEditValue(itemPropertyDescriptor, model);
		if (processer != null) {
			editValue = processer.unwrapValue(editValue);
		}
		int currentSelect = -1;
		int style = SWT.BORDER;
		if (readOnly) {
			style = style | SWT.READ_ONLY;
		}

		Composite tcom = formToolkit.createComposite(parent);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.horizontalSpacing = 0;
		tcom.setLayout(layout);

		FieldMarkerComposite notificationComposite = new FieldMarkerComposite(tcom, SWT.NONE);
		GridData gd = new GridData();
		gd.heightHint = 8;
		gd.widthHint = 8;
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalAlignment = GridData.BEGINNING;
		notificationComposite.setLayoutData(gd);
		fieldEditPart.setFieldMarker(notificationComposite);

		final Combo combo = new Combo(tcom, style);
		boolean valueIsSet = false;
		if (model instanceof EObject) {
			valueIsSet = ((EObject) model).eIsSet((EAttribute) itemPropertyDescriptor.getFeature(model));
		}
		combo.add("");
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				combo.add(items[i]);
				if (valueIsSet && items[i].equals(editValue)) {
					currentSelect = i + 1;
				}
			}
		}
		gd = new GridData(GridData.FILL_HORIZONTAL);
		tcom.setLayoutData(gd);
		combo.setLayoutData(gd);

		if (currentSelect != -1) {
			combo.select(currentSelect);
		} else {

		}
		if (valueIsSet && (editValue instanceof String)) {
			combo.setText(editValue.toString());
		}
		final Object fm = model;
		final ItemPropertyDescriptor fipd = (ItemPropertyDescriptor) itemPropertyDescriptor;
		final IModelProcsser fp = processer;
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = combo.getText();
				if (text == null || text.length() == 0) {
					UnSetFeatureCommand command = new UnSetFeatureCommand(fipd, fm);
					EditingDomain domain = fipd.getEditingDomain(fm);
					domain.getCommandStack().execute(command);
					return;
				}
				Object setValue = text;
				if (fp != null) {
					setValue = fp.wrapValue(text);
				}
				if (((EObject) fm).eIsSet((EAttribute) fipd.getFeature(fm)) && setValue.equals(getEditValue(fipd, fm))) {
					return;
				}
				fipd.setPropertyValue(fm, setValue);
			}
		});
		fieldEditPart.setContentControl(combo);
		return fieldEditPart;
	}

	public static AttributeFieldEditPart createJavaPropertySearchFieldEditor(String classString, EObject container,
			Composite parent, FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor, String buttonName,
			final EObject model) {
		IJavaProject project = getJavaProject(container);
		JavaPropertiesSelectionDialog dialog = new JavaPropertiesSelectionDialog(project, classString);
		AttributeFieldEditPart editPart = SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor,
				buttonName, dialog, (EObject) model);
		Control c = editPart.getContentControl();
		if (c instanceof SearchComposite) {
			Text text = ((SearchComposite) c).getText();
			IResource resource = getResource(model);
			if (resource != null) {
				final FieldAssistDisposer disposer = addJavaPropertiesFieldAssistToText(text, resource.getProject(),
						classString);
				text.addDisposeListener(new DisposeListener() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.swt.events.DisposeListener#widgetDisposed
					 * (org.eclipse.swt.events.DisposeEvent)
					 */
					public void widgetDisposed(DisposeEvent e) {
						disposer.dispose();
					}

				});
			}
		}
		return editPart;
	}

	public static AttributeFieldEditPart createJavaPropertySearchFieldEditor(BindingsType container, Composite parent,
			FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor, String buttonName,
			final EObject model) {
		String classString = ((BindingsType) container).getClass_();
		return createJavaPropertySearchFieldEditor(classString, container, parent, toolkit, propertyDescriptor,
				buttonName, model);
	}

	public static Object getEditValue(IItemPropertyDescriptor propertyDescriptor, Object model) {
		Object value = propertyDescriptor.getPropertyValue(model);
		if (value != null && value instanceof PropertyValueWrapper) {
			Object editValue = ((PropertyValueWrapper) value).getEditableValue(model);
			return editValue;
		}
		return null;
	}

	public static AttributeFieldEditPart createDialogFieldEditor(Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, String buttonName, IFieldDialog dialog,
			final EObject model) {
		return createDialogFieldEditor(parent, toolkit, propertyDescriptor, buttonName, dialog, model, false, null);
	}

	public static AttributeFieldEditPart createDialogFieldEditor(String label, Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, String buttonName, IFieldDialog dialog,
			final EObject model) {
		return createDialogFieldEditor(label, parent, toolkit, propertyDescriptor, buttonName, dialog, model, false,
				null);
	}

	public static AttributeFieldEditPart createDialogFieldEditor(Composite parent, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, String buttonName, IFieldDialog dialog,
			final EObject model, boolean labelLink, IHyperlinkListener listener) {
		return createDialogFieldEditor(null, parent, toolkit, propertyDescriptor, buttonName, dialog, model, labelLink,
				listener);
	}

	public static AttributeFieldEditPart createDialogFieldEditor(String labelText, Composite parent,
			FormToolkit toolkit, final IItemPropertyDescriptor propertyDescriptor, String buttonName,
			IFieldDialog dialog, final EObject model, boolean labelLink, IHyperlinkListener listener) {
		AttributeFieldEditPart editpart = new AttributeFieldEditPart();
		FieldMarkerWrapper wrapper = createFieldEditorLabel(labelText, parent, toolkit, propertyDescriptor, model,
				labelLink);
		editpart.setFieldMarker(wrapper.getMarker());
		Control label = wrapper.getLabelControl();
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

		Composite tcom = toolkit.createComposite(classTextComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.horizontalSpacing = 0;
		tcom.setLayout(layout);

		FieldMarkerComposite notificationComposite = new FieldMarkerComposite(tcom, SWT.NONE);
		gd = new GridData();
		gd.heightHint = 8;
		gd.widthHint = 8;
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalAlignment = GridData.BEGINNING;
		notificationComposite.setLayoutData(gd);
		editpart.setFieldMarker(notificationComposite);

		final SearchComposite searchComposite = new SearchComposite(tcom, toolkit, buttonName, dialog, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		searchComposite.setLayoutData(gd);

		Object editValue = getEditValue(propertyDescriptor, model);
		if (editValue != null) {
			searchComposite.getText().setText(editValue.toString());
		}
		searchComposite.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Object editValue = getEditValue(propertyDescriptor, model);
				String value = searchComposite.getText().getText();
				if (value == null || value.length() == 0) {
					propertyDescriptor.setPropertyValue(model, null);
				} else {
					if (editValue != null) {
						if (!editValue.equals(value)) {
							propertyDescriptor.setPropertyValue(model, value);
						}
					} else {
						propertyDescriptor.setPropertyValue(model, value);
					}
				}
			}
		});

		toolkit.paintBordersFor(classTextComposite);
		editpart.setContentControl(searchComposite);
		return editpart;
	}

	public static void openFile(String uri, IProject project) throws PartInitException {
		openFile(uri, project, null);
	}

	public static IFile getFile(String uri, IProject project) {
		if (project == null || uri == null)
			return null;
		if (uri.charAt(0) == '\\' || uri.charAt(0) == '/') {
			uri = uri.substring(1);
		}
		IFile file = project.getFile(uri);
		// it's workspace resource
		if (file.exists()) {

		} else {
			// maybe it's a classpath resource
			try {
				IJavaProject javaProject = JavaCore.create(project);
				if (javaProject != null) {
					IClasspathEntry[] classPathEntrys = javaProject.getRawClasspath();
					for (int i = 0; i < classPathEntrys.length; i++) {
						IClasspathEntry entry = classPathEntrys[i];
						IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(entry.getPath());
						if (folder != null && folder.exists()) {
							IFile temp = folder.getFile(new Path(uri));
							if (temp != null && temp.exists()) {
								file = temp;
								break;
							}
						}
					}
				}
			} catch (Exception e) {

			}
		}
		if (file.exists()) {
			return file;
		}
		return null;
	}

	public static void openFile(String uri, IProject project, String editorID) throws PartInitException {
		IFile file = getFile(uri, project);
		IWorkbenchWindow window = SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (file != null && window != null) {
			FileEditorInput editorInput = new FileEditorInput(file);
			if (editorID != null) {
				window.getActivePage().openEditor(editorInput, editorID);
			} else {
				IDE.openEditor(window.getActivePage(), file);
			}
		}
	}

	private static void expandSelectorViewer(IXMLStructuredObject model, TreeViewer viewer, List<String> expandedModel,
			int level) {
		if (level >= SELECTOR_EXPAND_MAX_LEVEL)
			return;
		level++;
		if (expandedModel.contains(model.getNodeName())) {
			return;
		} else {
			expandedModel.add(model.getNodeName());
			viewer.expandToLevel(model, 0);
			List<IXMLStructuredObject> children = model.getChildren();
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator.next();
				expandSelectorViewer(structuredObject, viewer, expandedModel, level);
			}
		}
	}

	public static void expandSelectorViewer(List<Object> models, TreeViewer viewer) {
		for (Iterator<?> iterator = models.iterator(); iterator.hasNext();) {
			Object model = (Object) iterator.next();
			if (model instanceof IXMLStructuredObject) {
				expandSelectorViewer((IXMLStructuredObject) model, viewer, new ArrayList<String>(), 0);
			}
		}
	}

	public static void loadSelectorObject(IXMLStructuredObject model, List<String> loadedModelName,
			List<IXMLStructuredObject> loadedModels, int level) {
		loadedModels.add(model);
		if (level >= SELECTOR_EXPAND_MAX_LEVEL)
			return;
		level++;
		if (loadedModelName.contains(model.getNodeName())) {
			return;
		} else {
			loadedModelName.add(model.getNodeName());
			List<IXMLStructuredObject> children = model.getChildren();
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator.next();
				loadSelectorObject(structuredObject, loadedModelName, loadedModels, level);
			}
		}
	}

	public static List<IXMLStructuredObject> loadSelectorObject(List<IXMLStructuredObject> firstNodes) {
		List<IXMLStructuredObject> loadedNodes = new ArrayList<IXMLStructuredObject>();
		for (Iterator<?> iterator = firstNodes.iterator(); iterator.hasNext();) {
			IXMLStructuredObject firstNode = (IXMLStructuredObject) iterator.next();
			loadSelectorObject(firstNode, new ArrayList<String>(), loadedNodes, 0);
		}
		return loadedNodes;
	}

	public static FieldAssistDisposer addSelectorFieldAssistToText(Text text, SmooksGraphicsExtType extType,
			SmooksResourceListType resourceList) {
		// Decorate the text widget with the light-bulb image denoting content
		// assist
		int bits = SWT.DOWN | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(text, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		if (isLinuxOS()) {
			controlDecoration.setDescriptionText("Content Assist Available (Ctrl + space)");
		} else {
			controlDecoration.setDescriptionText("Content Assist Available (Alt + /)");
		}
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());

		// Default text widget adapter for field assist
		TextContentAdapter textContentAdapter = new TextContentAdapter();
		// Content assist command
		String command = "org.eclipse.ui.edit.text.contentAssist.proposals"; //$NON-NLS-1$
		// Set auto activation character to be a '.'
		char[] autoActivationChars = new char[] { '/' };

		// Create the proposal provider
		SelectorContentProposalProvider proposalProvider = new SelectorContentProposalProvider(extType, resourceList);
		// Create the adapter
		ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(text, textContentAdapter,
				proposalProvider, command, autoActivationChars);
		// Configure the adapter
		// Add label provider
		ILabelProvider labelProvider = new LabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof SelectorContentProposal) {
					return ((SelectorContentProposal) element).getLabel();
				}
				return super.getText(element);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object
			 * )
			 */
			@Override
			public Image getImage(Object element) {
				return super.getImage(element);
			}

		};
		adapter.setLabelProvider(labelProvider);
		// Replace text field contents with accepted proposals
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		// Disable default filtering - custom filtering done
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		// Add listeners required to reset state for custom filtering
		SelectorConentProposalListener proposalListener = new SelectorConentProposalListener();
		adapter.addContentProposalListener((IContentProposalListener) proposalListener);
		adapter.addContentProposalListener((IContentProposalListener2) proposalListener);

		return new FieldAssistDisposer(adapter, (IContentProposalListener) proposalListener,
				(IContentProposalListener2) proposalListener);

	}

	public static FieldAssistDisposer addJavaPropertiesFieldAssistToText(Text text, IProject project, String className) {
		// Decorate the text widget with the light-bulb image denoting content
		// assist
		int bits = SWT.DOWN | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(text, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		if (isLinuxOS()) {
			controlDecoration.setDescriptionText("Content Assist Available (Ctrl + space)");
		} else {
			controlDecoration.setDescriptionText("Content Assist Available (Alt + /)");
		}
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());

		// Default text widget adapter for field assist
		TextContentAdapter textContentAdapter = new TextContentAdapter();
		// Content assist command
		String command = "org.eclipse.ui.edit.text.contentAssist.proposals"; //$NON-NLS-1$

		// Create the proposal provider
		JavaPropertiesProposalProvider proposalProvider = new JavaPropertiesProposalProvider(project, className);
		// Create the adapter
		ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(text, textContentAdapter,
				proposalProvider, command, allEnglishCharas);
		// Configure the adapter
		// Add label provider
		ILabelProvider labelProvider = new LabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof XMLStructuredModelProposal) {
					return ((XMLStructuredModelProposal) element).getLabel();
				}
				return super.getText(element);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object
			 * )
			 */
			@Override
			public Image getImage(Object element) {
				if (element instanceof XMLStructuredModelProposal) {
					if (((XMLStructuredModelProposal) element).getXmlStructuredObject() instanceof JavaBeanModel) {
						return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
								GraphicsConstants.IMAGE_JAVA_ATTRIBUTE);
					}
				}
				return super.getImage(element);
			}

		};
		adapter.setLabelProvider(labelProvider);
		// Replace text field contents with accepted proposals
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		// Disable default filtering - custom filtering done
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		// Add listeners required to reset state for custom filtering
		SelectorConentProposalListener proposalListener = new SelectorConentProposalListener();
		adapter.addContentProposalListener((IContentProposalListener) proposalListener);
		adapter.addContentProposalListener((IContentProposalListener2) proposalListener);

		return new FieldAssistDisposer(adapter, (IContentProposalListener) proposalListener,
				(IContentProposalListener2) proposalListener);

	}

	public static FieldAssistDisposer addJavaSetterMethodFieldAssistToText(Text text, Class<?> clazz) {
		// Decorate the text widget with the light-bulb image denoting content
		// assist
		int bits = SWT.DOWN | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(text, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		if (isLinuxOS()) {
			controlDecoration.setDescriptionText("Content Assist Available (Ctrl + space)");
		} else {
			controlDecoration.setDescriptionText("Content Assist Available (Alt + /)");
		}
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());

		// Default text widget adapter for field assist
		TextContentAdapter textContentAdapter = new TextContentAdapter();
		// Content assist command
		String command = "org.eclipse.ui.edit.text.contentAssist.proposals"; //$NON-NLS-1$

		// Create the proposal provider
		SetterMethodProposalProvider proposalProvider = new SetterMethodProposalProvider(clazz);
		// Create the adapter
		ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(text, textContentAdapter,
				proposalProvider, command, allEnglishCharas);
		// Configure the adapter
		// Add label provider
		ILabelProvider labelProvider = new LabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof SetterMethodContentProposal) {
					return ((SetterMethodContentProposal) element).getLabel();
				}
				return super.getText(element);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object
			 * )
			 */
			@Override
			public Image getImage(Object element) {
				return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_JAVA_ATTRIBUTE);
			}

		};
		adapter.setLabelProvider(labelProvider);
		// Replace text field contents with accepted proposals
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		// Disable default filtering - custom filtering done
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		// Add listeners required to reset state for custom filtering
		SelectorConentProposalListener proposalListener = new SelectorConentProposalListener();
		adapter.addContentProposalListener((IContentProposalListener) proposalListener);
		adapter.addContentProposalListener((IContentProposalListener2) proposalListener);

		return new FieldAssistDisposer(adapter, (IContentProposalListener) proposalListener,
				(IContentProposalListener2) proposalListener);

	}

	public static FieldAssistDisposer addBeanIdRefAssistToCombo(Combo combo, EObject model) {
		// Decorate the text widget with the light-bulb image denoting content
		// assist
		int bits = SWT.DOWN | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(combo, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		if (isLinuxOS()) {
			controlDecoration.setDescriptionText("Content Assist Available (Ctrl + space)");
		} else {
			controlDecoration.setDescriptionText("Content Assist Available (Alt + /)");
		}
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());

		// Default text widget adapter for field assist
		ComboContentAdapter textContentAdapter = new ComboContentAdapter();
		// Content assist command
		String command = "org.eclipse.ui.edit.text.contentAssist.proposals"; //$NON-NLS-1$
		// Set auto activation character to be a '.'

		// Create the proposal provider
		BeanIdRefProposalProvider proposalProvider = new BeanIdRefProposalProvider(model);
		// Create the adapter
		ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(combo, textContentAdapter,
				proposalProvider, command, allEnglishCharas);
		// Configure the adapter
		// Add label provider
		// ILabelProvider labelProvider = new LabelProvider();
		// adapter.setLabelProvider(labelProvider);
		// Replace text field contents with accepted proposals
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		// Disable default filtering - custom filtering done
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		// Add listeners required to reset state for custom filtering
		SelectorConentProposalListener proposalListener = new SelectorConentProposalListener();
		adapter.addContentProposalListener((IContentProposalListener) proposalListener);
		adapter.addContentProposalListener((IContentProposalListener2) proposalListener);

		return new FieldAssistDisposer(adapter, (IContentProposalListener) proposalListener,
				(IContentProposalListener2) proposalListener);
	}

	public static FieldAssistDisposer addBindingsContextAssistToText(Text text, SmooksResourceListType model) {
		// Decorate the text widget with the light-bulb image denoting content
		// assist
		int bits = SWT.CENTER | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(text, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		if (isLinuxOS()) {
			controlDecoration.setDescriptionText("Content Assist Available (Ctrl + space)");
		} else {
			controlDecoration.setDescriptionText("Content Assist Available (Alt + /)");
		}
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());

		// Default text widget adapter for field assist
		MultiTextContentAdapter textContentAdapter = new MultiTextContentAdapter();
		// Content assist command
		String command = "org.eclipse.ui.edit.text.contentAssist.proposals"; //$NON-NLS-1$
		// Set auto activation character to be a '.'

		// Create the proposal provider
		BindingsContextProposalProvider proposalProvider = new BindingsContextProposalProvider(model, text);
		// Create the adapter
		char[] chars = new char[allEnglishCharas.length + 1];
		System.arraycopy(allEnglishCharas, 0, chars, 0, allEnglishCharas.length);
		chars[chars.length - 1] = '.';
		ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(text, textContentAdapter,
				proposalProvider, command, chars);
		// Configure the adapter
		// Add label provider
		ILabelProvider labelProvider = new LabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object
			 * )
			 */
			@Override
			public Image getImage(Object element) {
				if (element instanceof BindingsContextContentProposal) {
					int type = ((BindingsContextContentProposal) element).getType();
					switch (type) {
					case BindingsContextContentProposal.BINDINGS:
						return SmooksCoreActivator.getDefault().getImageRegistry().get("BindingsType");
					case BindingsContextContentProposal.EXPRESSIONS:
						return SmooksCoreActivator.getDefault().getImageRegistry().get("ExpressionType");
					case BindingsContextContentProposal.PROPERTIES:
						return SmooksCoreActivator.getDefault().getImageRegistry().get("ValueType");
					case BindingsContextContentProposal.WIRTINGS:
						return SmooksCoreActivator.getDefault().getImageRegistry().get("WiringType");
					}
				}
				return super.getImage(element);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof IContentProposal) {
					return ((IContentProposal) element).getLabel();
				}
				return super.getText(element);
			}

		};
		adapter.setLabelProvider(labelProvider);
		// Replace text field contents with accepted proposals
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
		// Disable default filtering - custom filtering done
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		// Add listeners required to reset state for custom filtering
		SelectorConentProposalListener proposalListener = new SelectorConentProposalListener();
		adapter.addContentProposalListener((IContentProposalListener) proposalListener);
		adapter.addContentProposalListener((IContentProposalListener2) proposalListener);

		return new FieldAssistDisposer(adapter, (IContentProposalListener) proposalListener,
				(IContentProposalListener2) proposalListener);
	}

	public static TypeFieldAssistDisposer addTypeFieldAssistToText(Text text, IProject project, int searchScope) {
		// Decorate the text widget with the light-bulb image denoting content
		// assist
		int bits = SWT.DOWN | SWT.LEFT;
		ControlDecoration controlDecoration = new ControlDecoration(text, bits);
		// Configure text widget decoration
		// No margin
		controlDecoration.setMarginWidth(0);
		// Custom hover tip text
		if (isLinuxOS()) {
			controlDecoration.setDescriptionText("Content Assist Available (Ctrl + space)");
		} else {
			controlDecoration.setDescriptionText("Content Assist Available (Alt + /)");
		}
		// Custom hover properties
		controlDecoration.setShowHover(true);
		controlDecoration.setShowOnlyOnFocus(true);
		// Hover image to use
		FieldDecoration contentProposalImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		controlDecoration.setImage(contentProposalImage.getImage());

		// Create the proposal provider
		TypeContentProposalProvider proposalProvider = new TypeContentProposalProvider(project, searchScope);
		// Default text widget adapter for field assist
		TextContentAdapter textContentAdapter = new TextContentAdapter();
		// Content assist command
		String command = "org.eclipse.ui.edit.text.contentAssist.proposals"; //$NON-NLS-1$
		// Set auto activation character to be a '.'
		char[] chars = new char[allEnglishCharas.length + 1];
		System.arraycopy(allEnglishCharas, 0, chars, 0, allEnglishCharas.length);
		chars[chars.length - 1] = '.';
		// char[] autoActivationChars = new char[] { '.' };
		// Create the adapter
		ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(text, textContentAdapter,
				proposalProvider, command, chars);
		// Configure the adapter
		// Add label provider
		ILabelProvider labelProvider = new TypeProposalLabelProvider();
		adapter.setLabelProvider(labelProvider);
		// Replace text field contents with accepted proposals
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		// Disable default filtering - custom filtering done
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		// Add listeners required to reset state for custom filtering
		TypeContentProposalListener proposalListener = new TypeContentProposalListener();
		adapter.addContentProposalListener((IContentProposalListener) proposalListener);
		adapter.addContentProposalListener((IContentProposalListener2) proposalListener);

		return new TypeFieldAssistDisposer(adapter, proposalListener);
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

	private static boolean isAttributeName(String name) {
		if (name == null)
			return false;
		return name.trim().startsWith("@");
	}

	private static String getRawAttributeName(String name) {
		if (isAttributeName(name)) {
			return name.trim().substring(1);
		}
		return name;
	}

	private static IXMLStructuredObject localXMLNodeWithNodeName(String name, IXMLStructuredObject contextNode,
			Map<Object, Object> usedNodeMap) {
		if (name == null || contextNode == null)
			return null;
		String nodeName = contextNode.getNodeName();
		boolean isAttributeName = false;
		String tempName = name;
		if (isAttributeName(tempName)) {
			isAttributeName = true;
			tempName = getRawAttributeName(tempName);
		}
		boolean canCompare = true;
		if (isAttributeName) {
			if (!contextNode.isAttribute()) {
				canCompare = false;
			}
		}

		if (canCompare && tempName.equalsIgnoreCase(nodeName)) {
			return contextNode;
		}
		usedNodeMap.put(contextNode.getID(), new Object());
		List<?> children = contextNode.getChildren();
		IXMLStructuredObject result = null;
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject child = (IXMLStructuredObject) iterator.next();
			if (isAttributeName) {
				if (!child.isAttribute())
					continue;
			}
			if (tempName.equalsIgnoreCase(child.getNodeName())) {
				result = child;
				break;
			}
		}
		if (result == null) {
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				IXMLStructuredObject child = (IXMLStructuredObject) iterator.next();
				// to avoid the "died loop"
				if (usedNodeMap.get(child.getID()) != null) {
					continue;
				}
				try {
					result = localXMLNodeWithNodeName(name, child, usedNodeMap);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				if (result != null) {
					return result;
				}
			}
		}
		return result;
	}

	public static IXMLStructuredObject localXMLNodeWithNodeName(String name, IXMLStructuredObject contextNode) {
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		IXMLStructuredObject node = localXMLNodeWithNodeName(name, contextNode, map);
		map.clear();
		map = null;
		return node;
	}

	public static IXMLStructuredObject localXMLNodeWithPath(String path, IXMLStructuredObject contextNode) {
		if (path == null)
			return null;
		path = path.trim();
		String[] sperators = SELECTOR_SPERATORS;
		String sperator = null;
		boolean hasSperator = false;
		for (int i = 0; i < sperators.length; i++) {
			sperator = sperators[i];
			if (path.indexOf(sperator) != -1) {
				hasSperator = true;
				break;
			}
		}
		if (!hasSperator)
			sperator = null;
		return localXMLNodeWithPath(path, contextNode, sperator, true);
	}

	public static IXMLStructuredObject localXMLNodeWithPath(String path, IXMLStructuredObject contextNode,
			String sperator, boolean throwException) {
		if (contextNode == null || path == null)
			return null;
		if (sperator == null) {
			sperator = " ";
		}
		if (path != null)
			path = path.trim();
		String[] pathes = path.split(sperator);
		if (pathes != null && pathes.length > 0 && path.length() != 0) {
			// to find the first node
			// first time , we search the node via context
			String firstNodeName = pathes[0];
			int index = 0;
			while (firstNodeName.length() == 0) {
				index++;
				firstNodeName = pathes[index];
			}
			IXMLStructuredObject firstModel = localXMLNodeWithNodeName(firstNodeName, contextNode);

			// if we can't find the node , to find it from the Root Parent node
			if (firstModel == null) {
				firstModel = localXMLNodeWithNodeName(firstNodeName, getRootParent(contextNode));
			}

			if (firstModel == null) {
				if (throwException)
					throw new RuntimeException("Can't find the node : " + firstNodeName);
				else {
					return null;
				}
			}
			for (int i = index + 1; i < pathes.length; i++) {
				firstModel = getChildNodeWithName(pathes[i], firstModel);
				if (firstModel == null && throwException) {
					throw new RuntimeException("Can't find the node : " + pathes[i] + " from parent node "
							+ pathes[i - 1]);
				}
			}

			return firstModel;
		}
		return null;
	}

	public static IXMLStructuredObject getChildNodeWithName(String name, IXMLStructuredObject parent) {
		if (parent == null)
			return null;
		String tempName = name;
		boolean isAttribute = false;
		if (isAttributeName(tempName)) {
			isAttribute = true;
			tempName = getRawAttributeName(tempName);
		}
		List<IXMLStructuredObject> children = parent.getChildren();
		if (children == null)
			return null;
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator.next();
			if (isAttribute) {
				if (!structuredObject.isAttribute())
					continue;
			}
			if (tempName.equalsIgnoreCase(structuredObject.getNodeName())) {
				return structuredObject;
			}
		}
		return null;
	}

	public static boolean hasReaderAlready(Class<?> readerClass, SmooksResourceListType resourceList) {
		if (resourceList == null || readerClass == null)
			return false;
		List<AbstractReader> readerList = resourceList.getAbstractReader();
		for (Iterator<?> iterator = readerList.iterator(); iterator.hasNext();) {
			AbstractReader abstractReader = (AbstractReader) iterator.next();
			if (abstractReader.getClass() == readerClass) {
				return true;
			}
		}
		return false;
	}

	public static List<InputType> getActivedInputTypes(SmooksGraphicsExtType extType) {
		List<InputType> inputTypes = extType.getInput();
		List<InputType> types = new ArrayList<InputType>();
		for (Iterator<?> iterator = inputTypes.iterator(); iterator.hasNext();) {
			InputType inputType = (InputType) iterator.next();
			if (isActivedInput(inputType)) {
				types.add(inputType);
			}
		}
		return types;
	}

	public static void fillAllTask(TaskType task, List<TaskType> taskList) {
		taskList.add(task);
		List<TaskType> children = task.getTask();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			TaskType taskType = (TaskType) iterator.next();
			fillAllTask(taskType, taskList);
		}
	}

	public static boolean isActivedInput(InputType input) {
		List<ParamType> params = input.getParam();
		for (Iterator<?> iterator2 = params.iterator(); iterator2.hasNext();) {
			ParamType paramType = (ParamType) iterator2.next();
			if (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName())) {
				String value = paramType.getValue();
				if (value == null)
					continue;
				value = value.trim();
				if ("true".equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public static List<InputType> recordInputDataInfomation(EditingDomain domain, InputType input,
			SmooksGraphicsExtType extType, String type, String path, Properties properties) {
		List<InputType> inputTypeList = new ArrayList<InputType>();
		if (type != null && path != null && extType != null && domain != null) {
			String[] values = path.split(";");
			if (values == null || values.length == 0) {
				values = new String[] { path };
			}
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				value = value.trim();
				if (value.length() == 0)
					continue;
				if (input == null) {
					input = GraphFactory.eINSTANCE.createInputType();
				}
				input.setType(type);
				ParamType pathParam = null;
				List<ParamType> paramList = input.getParam();
				for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
					ParamType paramType = (ParamType) iterator.next();
					if (SmooksModelUtils.PARAM_NAME_PATH.equals(paramType.getName())) {
						pathParam = paramType;
						break;
					}
				}
				if (pathParam == null) {
					pathParam = GraphFactory.eINSTANCE.createParamType();
					pathParam.setName(SmooksModelUtils.PARAM_NAME_PATH);
				}
				pathParam.setValue(value);

				input.getParam().add(pathParam);
				List<ParamType> params = generateExtParams(type, path, properties);
				input.getParam().addAll(params);
				Command command = AddCommand.create(domain, extType,
						GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__INPUT, input);
				if (command.canExecute()) {
					domain.getCommandStack().execute(command);
					inputTypeList.add(input);
				}
				// extType.getInput().add(input);
			}
			// try {
			// extType.eResource().save(Collections.emptyMap());
			List<ISmooksGraphChangeListener> listeners = extType.getChangeListeners();
			for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
				ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator.next();
				smooksGraphChangeListener.inputTypeChanged(extType);
			}
			// } catch (IOException e) {
			// SmooksConfigurationActivator.getDefault().log(e);
			// }
		}

		return inputTypeList;
	}

	public static void expandGraphTree(List<?> expandNodes, TreeNodeEditPart rootEditPart) {
		for (Iterator<?> iterator = expandNodes.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			if (!(obj instanceof TreeNodeModel))
				continue;
			TreeNodeModel treeNodeModel = (TreeNodeModel) obj;
			TreeNodeModel parent = treeNodeModel;
			if (parent == null)
				continue;
			List<TreeNodeModel> parentList = new ArrayList<TreeNodeModel>();
			boolean canExpand = true;
			while (parent != rootEditPart.getModel()) {
				parent = (TreeNodeModel) parent.getParent();
				if (parent == null) {
					canExpand = false;
					break;
				}
				parentList.add(parent);
			}
			if (!canExpand) {
				parentList.clear();
				parentList = null;
				continue;
			}
			if (parentList.isEmpty())
				continue;
			parentList.remove(parentList.size() - 1);
			((TreeNodeEditPart) rootEditPart).expandNode();
			TreeNodeEditPart tempEditPart = rootEditPart;
			for (int i = parentList.size() - 1; i >= 0; i--) {
				boolean expanded = false;
				TreeNodeModel parentNode = parentList.get(i);
				List<?> editParts = tempEditPart.getChildren();
				for (Iterator<?> iterator2 = editParts.iterator(); iterator2.hasNext();) {
					EditPart editPart = (EditPart) iterator2.next();
					if (editPart instanceof TreeNodeEditPart && editPart.getModel() == parentNode) {
						((TreeNodeEditPart) editPart).expandNode();
						tempEditPart = (TreeNodeEditPart) editPart;
						expanded = true;
						break;
					}
				}
				if (!expanded) {
					break;
				}
			}
		}
	}

	public static List<ParamType> generateExtParams(String type, String path, Properties properties) {
		List<ParamType> lists = new ArrayList<ParamType>();
		if (properties != null) {
			Enumeration<?> enumerations = properties.keys();
			while (enumerations.hasMoreElements()) {
				Object key = (Object) enumerations.nextElement();
				ParamType param = GraphFactory.eINSTANCE.createParamType();
				param.setValue(properties.getProperty(key.toString()));
				param.setName(key.toString());
				lists.add(param);
			}
		}
		return lists;
	}

	/**
	 * This generates a {@link org.eclipse.emf.edit.ui.action.CreateChildAction}
	 * for each object in <code>descriptors</code>, and returns the collection
	 * of these actions. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static Collection<IAction> generateCreateChildActions(EditingDomain editingDomain,
			Collection<?> descriptors, ISelection selection) {
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
				actions.add(new AddSmooksResourceAction(editingDomain, selection, descriptor));
			}
		}
		return actions;
	}

	public static boolean isRelatedConnectionFeature(EStructuralFeature feature) {
		// for Bean ID
		if (FreemarkerPackage.Literals.BIND_TO__ID == feature) {
			return true;
		}
		if (feature == XslPackage.Literals.BIND_TO__ID) {
			return true;
		}

		if (feature == JavabeanPackage.Literals.BINDINGS_TYPE__BEAN_ID) {
			return true;
		}

		if (feature == Javabean12Package.Literals.BEAN_TYPE__BEAN_ID) {
			return true;
		}

		// for bean ref id :
		if (JmsroutingPackage.Literals.JMS_ROUTER__BEAN_ID == feature) {
			return true;
		}
		if (Jmsrouting12Package.Literals.JMS12_ROUTER__BEAN_ID == feature) {
			return true;
		}
		if (JavabeanPackage.Literals.WIRING_TYPE__BEAN_ID_REF == feature) {
			return true;
		}
		if (Javabean12Package.Literals.WIRING_TYPE__BEAN_ID_REF == feature) {
			return true;
		}

		// for selector :
		if (JavabeanPackage.Literals.BINDINGS_TYPE__CREATE_ON_ELEMENT == feature) {
			return true;
		}
		if (CalcPackage.Literals.COUNTER__COUNT_ON_ELEMENT == feature) {
			return true;
		}
		if (DatasourcePackage.Literals.DIRECT__BIND_ON_ELEMENT == feature) {
			return true;
		}
		if (EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT == feature) {
			return true;
		}
		if (FileRoutingPackage.Literals.OUTPUT_STREAM__OPEN_ON_ELEMENT == feature) {
			return true;
		}
		if (FreemarkerPackage.Literals.FREEMARKER__APPLY_ON_ELEMENT == feature) {
			return true;
		}
		if (XslPackage.Literals.XSL__APPLY_ON_ELEMENT == feature) {
			return true;
		}
		if (GroovyPackage.Literals.GROOVY__EXECUTE_ON_ELEMENT == feature) {
			return true;
		}
		if (JmsroutingPackage.Literals.JMS_ROUTER__ROUTE_ON_ELEMENT == feature) {
			return true;
		}
		if (SmooksPackage.Literals.RESOURCE_CONFIG_TYPE__SELECTOR == feature) {
			return true;
		}
		if (SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR == feature) {
			return true;
		}
		if (JavabeanPackage.Literals.WIRING_TYPE__WIRE_ON_ELEMENT == feature) {
			return true;
		}
		if (JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT == feature) {
			return true;
		}
		if (JavabeanPackage.Literals.VALUE_TYPE__DATA == feature) {
			return true;
		}
		if (Javabean12Package.Literals.BEAN_TYPE__CREATE_ON_ELEMENT == feature) {
			return true;
		}
		if (Javabean12Package.Literals.WIRING_TYPE__WIRE_ON_ELEMENT == feature) {
			return true;
		}
		if (Javabean12Package.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT == feature) {
			return true;
		}
		if (Javabean12Package.Literals.VALUE_TYPE__DATA == feature) {
			return true;
		}
		return false;
	}

	public static IJavaSearchScope getSearchScope(IJavaProject project) {
		return SearchEngine.createJavaSearchScope(getNonJRERoots(project));
	}

	public static IJavaSearchScope getSearchScope(IProject project) {
		return getSearchScope(JavaCore.create(project));
	}

	public static IPackageFragmentRoot[] getNonJRERoots(IJavaProject project) {
		ArrayList<Object> result = new ArrayList<Object>();
		try {
			IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (!isJRELibrary(roots[i])) {
					result.add(roots[i]);
				}
			}
		} catch (JavaModelException e) {
		}
		return (IPackageFragmentRoot[]) result.toArray(new IPackageFragmentRoot[result.size()]);
	}

	public static boolean isJRELibrary(IPackageFragmentRoot root) {
		try {
			IPath path = root.getRawClasspathEntry().getPath();
			if (path.equals(new Path(JavaRuntime.JRE_CONTAINER)) || path.equals(new Path(JavaRuntime.JRELIB_VARIABLE))) {
				return true;
			}
		} catch (JavaModelException e) {
		}
		return false;
	}

	public static EStructuralFeature getBeanIDFeature(EObject model) {
		if (model == null) {
			return null;
		}
		if (model instanceof BindTo) {
			return FreemarkerPackage.Literals.BIND_TO__ID;
		}
		if (model instanceof org.jboss.tools.smooks.model.xsl.BindTo) {
			return XslPackage.Literals.BIND_TO__ID;
		}

		if (model instanceof BindingsType) {
			return JavabeanPackage.Literals.BINDINGS_TYPE__BEAN_ID;
		}

		if (model instanceof BeanType) {
			return Javabean12Package.Literals.BEAN_TYPE__BEAN_ID;
		}

		return null;
	}

	public static EStructuralFeature getBeanIDRefFeature(EObject model) {
		if (model == null) {
			return null;
		}
		if (model instanceof JmsRouter) {
			return JmsroutingPackage.Literals.JMS_ROUTER__BEAN_ID;
		}
		if (model instanceof JMS12Router) {
			return Jmsrouting12Package.Literals.JMS12_ROUTER__BEAN_ID;
		}
		if (model instanceof WiringType) {
			return JavabeanPackage.Literals.WIRING_TYPE__BEAN_ID_REF;
		}

		if (model instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
			return Javabean12Package.Literals.WIRING_TYPE__BEAN_ID_REF;
		}
		return null;
	}

	public static EStructuralFeature getSelectorFeature(EObject model) {
		if (model == null)
			return null;
		if (model instanceof BindingsType) {
			return JavabeanPackage.Literals.BINDINGS_TYPE__CREATE_ON_ELEMENT;
		}
		if (model instanceof Counter) {
			return CalcPackage.Literals.COUNTER__COUNT_ON_ELEMENT;
		}
		if (model instanceof Direct) {
			return DatasourcePackage.Literals.DIRECT__BIND_ON_ELEMENT;
		}
		if (model instanceof RouteBean) {
			return EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT;
		}
		if (model instanceof OutputStream) {
			return FileRoutingPackage.Literals.OUTPUT_STREAM__OPEN_ON_ELEMENT;
		}
		if (model instanceof Freemarker) {
			return FreemarkerPackage.Literals.FREEMARKER__APPLY_ON_ELEMENT;
		}
		if (model instanceof Xsl) {
			return XslPackage.Literals.XSL__APPLY_ON_ELEMENT;
		}
		if (model instanceof GroovyUICreator) {
			return GroovyPackage.Literals.GROOVY__EXECUTE_ON_ELEMENT;
		}
		if (model instanceof JmsRouter) {
			return JmsroutingPackage.Literals.JMS_ROUTER__ROUTE_ON_ELEMENT;
		}

		if (model instanceof ResourceConfigType) {
			return SmooksPackage.Literals.RESOURCE_CONFIG_TYPE__SELECTOR;
		}

		if (model instanceof SmooksResourceListType) {
			return SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR;
		}

		if (model instanceof WiringType) {
			return JavabeanPackage.Literals.WIRING_TYPE__WIRE_ON_ELEMENT;
		}
		if (model instanceof ExpressionType) {
			return JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT;
		}
		if (model instanceof ValueType) {
			return JavabeanPackage.Literals.VALUE_TYPE__DATA;
		}

		if (model instanceof BeanType) {
			return Javabean12Package.Literals.BEAN_TYPE__CREATE_ON_ELEMENT;
		}
		if (model instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
			return Javabean12Package.Literals.WIRING_TYPE__WIRE_ON_ELEMENT;
		}
		if (model instanceof org.jboss.tools.smooks.model.javabean12.ExpressionType) {
			return Javabean12Package.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT;
		}
		if (model instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
			return Javabean12Package.Literals.VALUE_TYPE__DATA;
		}
		return null;
	}

	public static boolean isSmooks1_2PlatformSpecialXMLNS(String ns) {
		for (int i = 0; i < SMOOKS_PLATFORM_1_2_SPECIAL_NAMESPACES.length; i++) {
			String n = SMOOKS_PLATFORM_1_2_SPECIAL_NAMESPACES[i];
			if (n.equals(ns)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSmooks1_1PlatformConflictXMLNS(String ns) {
		for (int i = 0; i < SMOOKS_PLATFORM_1_1_CONFLICT_NAMESPACES.length; i++) {
			String n = SMOOKS_PLATFORM_1_1_CONFLICT_NAMESPACES[i];
			if (n.equals(ns)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isUnSupportElement(String version, EObject element) {
		if (version == null || element == null)
			return false;
		String ns = element.eClass().getEPackage().getNsURI();
		if (GraphPackage.eNS_URI.equals(ns)) {
			return true;
		}
		if (SmooksConstants.VERSION_1_1.equals(version)) {
			if (isSmooks1_2PlatformSpecialXMLNS(ns)) {
				return true;
			}
		}

		if (SmooksConstants.VERSION_1_2.equals(version)) {
			if (isSmooks1_1PlatformConflictXMLNS(ns)) {
				return true;
			}
		}

		return false;
	}

	public static String judgeSmooksPlatformVersion(EObject smooksModel) {
		if (smooksModel instanceof DocumentRoot) {
			return SmooksConstants.VERSION_1_0;
		}

		if (smooksModel instanceof org.jboss.tools.smooks.model.smooks.DocumentRoot) {
			EMap<String, String> nsMap = ((org.jboss.tools.smooks.model.smooks.DocumentRoot) smooksModel)
					.getXMLNSPrefixMap();
			for (Iterator<String> iterator = nsMap.values().iterator(); iterator.hasNext();) {
				String ns = iterator.next();
				if (isSmooks1_2PlatformSpecialXMLNS(ns)) {
					return SmooksConstants.VERSION_1_2;
				}
			}
			return SmooksConstants.VERSION_1_1;
		}
		return SmooksConstants.VERSION_1_2;
	}

	public static String judgeInputType(EObject smooksModel) {
		String inputType = null;
		if (smooksModel instanceof DocumentRoot) {
			return null;
		}

		if (smooksModel instanceof org.jboss.tools.smooks.model.smooks.DocumentRoot) {
			SmooksResourceListType rlist = ((org.jboss.tools.smooks.model.smooks.DocumentRoot) smooksModel)
					.getSmooksResourceList();
			if (rlist.getAbstractReader().isEmpty())
				return null;
			AbstractReader reader = rlist.getAbstractReader().get(0);
			if (CsvReader.class.isInstance(reader) || CSV12Reader.class.isInstance(reader)) {
				inputType = SmooksModelUtils.INPUT_TYPE_CSV;
			}
			if (EDIReader.class.isInstance(reader) || EDI12Reader.class.isInstance(reader)) {
				inputType = SmooksModelUtils.INPUT_TYPE_EDI_1_1;
			}
			if (JsonReader.class.isInstance(reader) || Json12Reader.class.isInstance(reader)) {
				inputType = SmooksModelUtils.INPUT_TYPE_JSON_1_1;
			}
			if (ReaderType.class.isInstance(reader)) {
				inputType = SmooksModelUtils.INPUT_TYPE_CUSTOME;
			}
		}
		return inputType;
	}

	public static boolean isSmooksFile(IFile file) {
		if (file.getName().indexOf(".xml") != -1)
			return true;
		IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
		IContentType[] types = contentTypeManager.findContentTypesFor(file.getName());
		for (IContentType contentType : types) {
			if (contentType.equals(contentTypeManager.getContentType("org.jboss.tools.smooks.ui.smooks.contentType"))) {
				return true;
			}
			if (contentType.equals(contentTypeManager.getContentType("org.jboss.tools.smooks.ui.edimap.contentType"))) {
				return true;
			}
			if (contentType
					.equals(contentTypeManager.getContentType("org.jboss.tools.smooks.ui.smooks1_0.contentType"))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isCollectionJavaGraphModel(EObject parent) {
		String classString = null;
		if (parent instanceof BeanType) {
			classString = ((BeanType) parent).getClass_();
		}
		if (parent instanceof BindingsType) {
			classString = ((BindingsType) parent).getClass_();
		}
		if (classString != null)
			classString = classString.trim();

		IJavaProject project = SmooksUIUtils.getJavaProject(parent);
		if (project != null) {
			try {
				ProjectClassLoader loader = new ProjectClassLoader(project);
				Class<?> clazz = loader.loadClass(classString);
				if (Collection.class.isAssignableFrom(clazz)) {
					return true;
				}
			} catch (Throwable t) {

			}
		}

		return false;
	}

	public static boolean isArrayJavaGraphModel(EObject parent) {
		String classString = null;
		if (parent instanceof BeanType) {
			classString = ((BeanType) parent).getClass_();
		}
		if (parent instanceof BindingsType) {
			classString = ((BindingsType) parent).getClass_();
		}
		if (classString != null)
			classString = classString.trim();
		if (classString.endsWith("]")) {
			return true;
		}
		return false;
	}

	public static EStructuralFeature getFeature(Object model) {
		if (model instanceof BindingsType) {
			return JavabeanPackage.Literals.DOCUMENT_ROOT__BINDINGS;
		}
		if (model instanceof BeanType) {
			return Javabean12Package.Literals.JAVABEAN12_DOCUMENT_ROOT__BEAN;
		}
		if (model instanceof Xsl) {
			return XslPackage.Literals.DOCUMENT_ROOT__XSL;
		}
		if (model instanceof Freemarker) {
			return FreemarkerPackage.Literals.DOCUMENT_ROOT__FREEMARKER;
		}
		if (model instanceof RuleBasesType) {
			return Rules10Package.Literals.RULES10_DOCUMENT_ROOT__RULE_BASES;
		}
		return null;
	}
}
