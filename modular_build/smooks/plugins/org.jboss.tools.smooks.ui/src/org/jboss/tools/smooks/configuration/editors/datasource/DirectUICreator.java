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
package org.jboss.tools.smooks.configuration.editors.datasource;

import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.datasource.Direct;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class DirectUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			ISmooksModelProvider formEditor, IEditorPart part) {
		if (feature == DatasourcePackage.eINSTANCE.getDirect_AutoCommit()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_BindOnElementNS()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Datasource()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Password()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Url()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Username()) {
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor,part);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#ignoreProperty
	 * (org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == DatasourcePackage.eINSTANCE.getDirect_BindOnElementNS()) {
			return true;
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_BindOnElement()) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

	public List<AttributeFieldEditPart> createExtendUIOnTop(AdapterFactoryEditingDomain editingdomain,
			FormToolkit toolkit, Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart part) {
		return this.createElementSelectionSection(Messages.DirectUICreator_Binding_On_Element, editingdomain, toolkit, parent, model,
				formEditor, part,DatasourcePackage.eINSTANCE.getDirect_BindOnElement(), DatasourcePackage.eINSTANCE
						.getDirect_BindOnElementNS());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#createExtendUI
	 * (org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain,
	 * org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite, java.lang.Object,
	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
	 */
	@Override
	public List<AttributeFieldEditPart> createExtendUIOnBottom(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart part) {
		List<AttributeFieldEditPart> editPartList = new ArrayList<AttributeFieldEditPart>();

		Composite spaceComposite = toolkit.createComposite(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 20;
		spaceComposite.setLayoutData(gd);

		Composite linkComposite = toolkit.createComposite(parent);
		gd.horizontalSpan = 2;
		linkComposite.setLayoutData(gd);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.horizontalSpacing = 2;
		linkComposite.setLayout(gl);

		gd = new GridData();
		gd.verticalAlignment = GridData.CENTER;
		Label imageLabel = toolkit.createLabel(linkComposite, ""); //$NON-NLS-1$
		imageLabel.setLayoutData(gd);
		imageLabel.setImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
				GraphicsConstants.IMAGE_JAVA_ARRAY));
		
		gd = new GridData();
		gd.verticalAlignment = GridData.CENTER;
		gd.horizontalAlignment = GridData.BEGINNING;
		Hyperlink testConnectLink = toolkit.createHyperlink(linkComposite, Messages.DirectUICreator_DB_Connection_Test, SWT.NONE);
		testConnectLink.setLayoutData(gd);
		final Object fm = model;
		final Shell shell = parent.getShell();
		testConnectLink.addHyperlinkListener(new IHyperlinkListener(){

			/* (non-Javadoc)
			 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent)
			 */
			public void linkActivated(HyperlinkEvent e) {
				if(fm instanceof Direct){
					
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
					try {
						dialog.run(true, true, new IRunnableWithProgress(){

							/* (non-Javadoc)
							 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
							 */
							public void run(IProgressMonitor monitor) throws InvocationTargetException,
									InterruptedException {
								monitor.beginTask(Messages.DirectUICreator_Test_Connection, 2);
								String driver = ((Direct)fm).getDriver();
								String url = ((Direct)fm).getUrl();
								String userName = ((Direct)fm).getUsername();
								String password = ((Direct)fm).getPassword();
								IResource resource = SmooksUIUtils.getResource((EObject)fm);
								try {
									if(monitor.isCanceled()){
										throw new InterruptedException();
									}
									monitor.setTaskName(Messages.DirectUICreator_Load_DB_Properties);
									ProjectClassLoader classLoader = new ProjectClassLoader(JavaCore.create(resource.getProject()));
									if(monitor.isCanceled()){
										throw new InterruptedException();
									}
									Driver dri = (Driver) classLoader.loadClass(driver).newInstance();
									monitor.worked(1);
									if(monitor.isCanceled()){
										throw new InterruptedException();
									}
									Properties pros = new Properties();
									pros.setProperty("name", userName); //$NON-NLS-1$
									pros.setProperty("password", password); //$NON-NLS-1$
									if(monitor.isCanceled()){
										throw new InterruptedException();
									}
									monitor.setTaskName(Messages.DirectUICreator_Trying_to_Connect);
									dri.connect(url, pros);
									monitor.worked(1);
								} catch (JavaModelException e1) {
									throw new InvocationTargetException(e1);
								} catch (InstantiationException e1) {
									throw new InvocationTargetException(e1);
								} catch (IllegalAccessException e1) {
									throw new InvocationTargetException(e1);
								} catch (ClassNotFoundException e1) {
									throw new InvocationTargetException(e1);
								} catch (SQLException e1) {
									throw new InvocationTargetException(e1);
								}finally{
									monitor.done();
								}
							}
							
						});
					} catch (InvocationTargetException e2) {
						SmooksUIUtils.showErrorDialog(shell, SmooksUIUtils.createErrorStatus(e2));
						return;
					} catch (InterruptedException e2) {
						MessageDialog.openConfirm(shell, Messages.DirectUICreator_User_Canceled, Messages.DirectUICreator_Msg_User_Canceled);
						return;
					}
					MessageDialog.openConfirm(shell, Messages.DirectUICreator_Test_Success, Messages.DirectUICreator_Msg_Test_Success);
				}
			}

			/* (non-Javadoc)
			 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent)
			 */
			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkExited(org.eclipse.ui.forms.events.HyperlinkEvent)
			 */
			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		// testConnectLink.set
		return editPartList;
	}

	@Override
	public boolean isJavaTypeFeature(EAttribute attribute) {
		if (attribute == DatasourcePackage.eINSTANCE.getDirect_Driver()) {
			return true;
		}
		return super.isJavaTypeFeature(attribute);
	}

	@Override
	public boolean isSelectorFeature(EAttribute attribute) {
		// if (attribute ==
		// DatasourcePackage.eINSTANCE.getDirect_BindOnElement()) {
		// return true;
		// }
		return super.isSelectorFeature(attribute);
	}

}