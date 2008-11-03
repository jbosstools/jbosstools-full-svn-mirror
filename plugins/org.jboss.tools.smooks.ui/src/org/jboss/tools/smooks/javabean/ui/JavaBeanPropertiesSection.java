/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.javabean.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.AbstractSmooksPropertySection;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng
 * @Date : Oct 27, 2008
 */
public class JavaBeanPropertiesSection extends AbstractSmooksPropertySection {

	private static final String PRO_TYPE = "type";

	private Text instanceClassText;

	private boolean lock = false;

	protected String beanClassType;

	protected String instanceClass;

	private Text classTypeText;

	private Composite typeComposite;

	private Composite instanceClassComposite;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();
		Composite main = factory.createComposite(parent);
		FillLayout fill = new FillLayout();
		fill.marginHeight = 8;
		fill.marginWidth = 8;
		main.setLayout(fill);

		Section section = factory.createSection(main, Section.TITLE_BAR);
		section.setText("JavaBean Properties");
		Composite controlComposite = factory.createComposite(section);
		section.setClient(controlComposite);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;

		controlComposite.setLayout(gl);

		factory.createLabel(controlComposite, "Target instance class name : ");

		instanceClassComposite = factory.createComposite(controlComposite);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		instanceClassComposite.setLayoutData(gd);
		GridLayout beanLayout = new GridLayout();
		beanLayout.numColumns = 2;
		instanceClassComposite.setLayout(beanLayout);

		instanceClassText = factory.createText(instanceClassComposite, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		instanceClassText.setLayoutData(gd);

		Button button1 = factory.createButton(instanceClassComposite, "Browse",
				SWT.NONE);
		button1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				openJavaTypeDialog(instanceClassText);
			}

		});

		factory.createLabel(controlComposite, "Mapping Type : ");
		typeComposite = factory.createComposite(controlComposite);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		typeComposite.setLayoutData(gd);
		GridLayout typeLayout = new GridLayout();
		typeLayout.numColumns = 2;
		typeComposite.setLayout(typeLayout);

		classTypeText = factory.createText(typeComposite, "");
		classTypeText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isLock())
					return;
				beanClassType = classTypeText.getText();
				PropertyModel pro = getTypePropertyModel();
				if (pro != null) {
					pro.setValue(beanClassType);
					fireDirty();
					refresh();
				}
			}

		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		classTypeText.setLayoutData(gd);
		Composite buttonCom = factory.createComposite(typeComposite);
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.numColumns = 2;
		buttonCom.setLayout(buttonLayout);
		Button button3 = factory.createButton(buttonCom, "Browse class",
				SWT.NONE);
		button3.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				openJavaTypeDialog(classTypeText);
			}

		});

		Button button2 = factory.createButton(buttonCom, "Browse custom type",
				SWT.NONE);
		button2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				browseBeanCustomTypeButtonSelected();
			}

		});
		hookBeanClasText();
	}

	private PropertyModel getTypePropertyModel() {
		LineConnectionModel line = this.getLineConnectionModel();
		PropertyModel typePro = null;
		if (line != null) {
			Object[] pros = line.getPropertyArray();
			for (int i = 0; i < pros.length; i++) {
				PropertyModel pro = (PropertyModel) pros[i];
				if (PRO_TYPE.equals(pro.getName())) {
					typePro = pro;
					break;
				}
			}
			if (typePro == null) {
				typePro = new PropertyModel();
				typePro.setName(PRO_TYPE);
				line.addPropertyModel(typePro);
			}
		}
		return typePro;
	}

	protected void browseBeanCustomTypeButtonSelected() {

	}

	protected void openJavaTypeDialog(Text text) {
		IJavaProject javaProject = createNewProjectClassLoader();
		if (javaProject == null) {
			MessageDialog.openError(this.getPart().getSite().getShell(),
					"Error", "can't open type selection dialog");
			return;
		}
		IJavaSearchScope scope = JavaSearchScopeFactory.getInstance()
				.createJavaProjectSearchScope(javaProject, true);
		SelectionDialog dialog;
		try {
			dialog = JavaUI.createTypeDialog(getPart().getSite().getShell(),
					SmooksUIActivator.getDefault().getWorkbench()
							.getActiveWorkbenchWindow(), scope,
					IJavaElementSearchConstants.CONSIDER_CLASSES, false);
			dialog.setMessage("Java Type:");
			dialog.setTitle("Search Java Type");

			if (dialog.open() == Window.OK) {
				Object[] results = dialog.getResult();
				if (results.length > 0) {
					Object result = results[0];
					String packageFullName = JavaModelUtil
							.getTypeContainerName((IType) result);
					if (packageFullName == null
							|| packageFullName.length() <= 0) {
						text.setText(((IType) result).getElementName());
					} else {
						text.setText(packageFullName + "."
								+ ((IType) result).getElementName());
					}
				}
			}
		} catch (Exception e) {
			// this.setErrorMessage("Error occurs!please see log file");
			e.printStackTrace();
		}

	}

	protected void browseBeanClassTypeButtonSelected() {

	}

	protected void browseBeanClassButtonSelected() {
		// TODO Auto-generated method stub

	}

	private void hookBeanClasText() {
		instanceClassText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent arg0) {
				JavaBeanModel model = getTargetJavaBeanModel();
				if (model != null) {
					model.setBeanClassString(instanceClassText.getText());
					instanceClass = instanceClassText.getText();
					if (isLock())
						return;
					fireDirty();
					refresh();
				}
			}

		});
	}

	public boolean isLock() {
		return lock;
	}

	protected IJavaProject createNewProjectClassLoader() {
		try {
			ISelection s = (ISelection) this.getSelection();
			if (s instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) s;
				Object obj = selection.getFirstElement();
				if (obj == null)
					return null;
				if (obj instanceof GraphicalEditPart) {
					DefaultEditDomain domain = (DefaultEditDomain) ((GraphicalViewer) ((GraphicalEditPart) obj)
							.getViewer()).getEditDomain();
					IEditorInput input = domain.getEditorPart()
							.getEditorInput();
					if (input != null && input instanceof IFileEditorInput) {
						IFile file = ((IFileEditorInput) input).getFile();
						IProject project = file.getProject();
						IJavaProject jp = JavaCore.create(project);
						return jp;
					}
				}
			}
		} catch (Exception e) {

		}
		return null;
	}

	private JavaBeanModel getTargetJavaBeanModel() {
		ISelection s = (ISelection) this.getSelection();
		if (s instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) s;
			Object obj = selection.getFirstElement();
			if (obj == null)
				return null;
			if (obj instanceof EditPart) {
				Object model = ((EditPart) obj).getModel();
				if (model instanceof LineConnectionModel) {
					AbstractStructuredDataModel target = (AbstractStructuredDataModel) ((LineConnectionModel) model)
							.getTarget();
					Object referenceObj = target.getReferenceEntityModel();
					if (referenceObj instanceof JavaBeanModel) {
						return (JavaBeanModel) referenceObj;
					}
				}
			}
		}
		return null;
	}

	private LineConnectionModel getLineConnectionModel() {
		ISelection s = (ISelection) this.getSelection();
		if (s instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) s;
			Object obj = selection.getFirstElement();
			if (obj == null)
				return null;
			if (obj instanceof EditPart) {
				Object model = ((EditPart) obj).getModel();
				if (model instanceof LineConnectionModel) {
					return (LineConnectionModel) model;
				}
			}
		}
		return null;
	}

	public void refresh() {
		super.refresh();
		lockEventFire();

		JavaBeanModel model = getTargetJavaBeanModel();
		String type = getTypeProperty(getLineConnectionModel());
		if (type == null)
			type = "";
		LineConnectionModel connection = getLineConnectionModel();
		if(connection == null){
			instanceClassComposite.setEnabled(false);
			typeComposite.setEnabled(false);
			return;
		}
		if (UIUtils.isInstanceCreatingConnection(connection)) {
			if (model != null) {
				// if (!instanceClassComposite.getEnabled())
				instanceClassComposite.setEnabled(true);
//				classTypeText.setEnabled(false);
				String className = model.getBeanClassString();
				lockEventFire();
				this.classTypeText.setText("");
				typeComposite.setEnabled(false);
				instanceClassText.setText(className);
				unLockEventFire();
			}
		} else {
			if (type != null) {
				typeComposite.setEnabled(true);
				lockEventFire();
				instanceClassText.setText("");
				instanceClassComposite.setEnabled(false);
				this.classTypeText.setText(type);
				unLockEventFire();
			}
		}
		unLockEventFire();
	}

	private String getTypeProperty(LineConnectionModel connection) {
		if(connection == null) return null;
		Object[] properties = connection.getPropertyArray();
		if(properties == null) return null;
		for (int i = 0; i < properties.length; i++) {
			PropertyModel pro = (PropertyModel) properties[i];
			if ("type".equalsIgnoreCase(pro.getName())) {
				return pro.getValue();
			}
		}
		return null;
	}

	private void unLockEventFire() {
		lock = false;
	}

	private void lockEventFire() {
		lock = true;
	}

}
