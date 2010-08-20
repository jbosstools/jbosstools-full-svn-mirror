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
package org.jboss.tools.smooks.configuration.editors.input.contributors;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.ExtentionInputContentProvider;
import org.jboss.tools.smooks.configuration.editors.ExtentionInputLabelProvider;
import org.jboss.tools.smooks.configuration.editors.Messages;
import org.jboss.tools.smooks.configuration.editors.input.InputSourceType;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributor;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributorFactory;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.configuration.editors.wizard.ViewerInitorStore;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.GlobalParams;
import org.jboss.tools.smooks.model.core.ICorePackage;
import org.jboss.tools.smooks.model.core.IParam;
import org.milyn.javabean.dynamic.Model;

/**
 * Simple contributor used to add a message to the configuration composite.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class SampleDataConfigurationContributorFactory implements InputTaskPanelContributorFactory {

	private InputSourceType inputSourceType;

	public void setInputSourceType(InputSourceType inputSourceType) {
		this.inputSourceType = inputSourceType;
	}

	public InputTaskPanelContributor newInstance(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel) {
		return new PanelContributor(toolkit, editorPart, configModel);
	}
	
	private class PanelContributor extends AbstractContributor {

		private EditorPart editorPart;
		private Model<SmooksModel> configModel;
		private ISmooksModelProvider modelProvider;
		private EditingDomain editingDomain;
		private Composite inputSourceConfigComposite;
		private CheckboxTableViewer inputDataViewer;
		private boolean lockCheck;
				
		public Button addInputDataButton;
		private Button removeInputDataButton;		

		public PanelContributor(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel) {
			super(editorPart, configModel);
			this.editorPart = editorPart;
			this.configModel = configModel;
			this.modelProvider = configModel.getModelRoot().getModelProvider();
			this.editingDomain = modelProvider.getEditingDomain();
		}

		public void setInputSourceConfigComposite(Composite inputSourceConfigComposite) {
			this.inputSourceConfigComposite = inputSourceConfigComposite;
		}

		public void addInputSourceTypeConfigControls() {
			Composite mainComposite = new Composite(inputSourceConfigComposite, SWT.NONE);
			GridLayout gl = new GridLayout();
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			
			gl.numColumns = 2;
			mainComposite.setLayout(gl);
			mainComposite.setLayoutData(gd);

			Composite tableComposite = new Composite(mainComposite, SWT.BORDER);
			FillLayout fillLayout = new FillLayout();
			fillLayout.marginHeight = 1;
			fillLayout.marginWidth = 1;
			gd = new GridData(GridData.FILL_BOTH);
			gd.heightHint = 100;
			tableComposite.setLayoutData(gd);
			tableComposite.setLayout(fillLayout);

			inputDataViewer = CheckboxTableViewer.newCheckList(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
			inputDataViewer.setCheckStateProvider(new ICheckStateProvider() {

				public boolean isGrayed(Object element) {
					return !isValidInputType((IParam) element);
				}

				public boolean isChecked(Object element) {
					if (element instanceof IParam) {
						return SmooksModelUtils.INPUT_ACTIVE_TYPE.equals(((IParam)element).getType());
					}
					return false;
				}
			});
			inputDataViewer.addCheckStateListener(new ICheckStateListener() {
				
				public void checkStateChanged(CheckStateChangedEvent event) {
					if (lockCheck)
						return;
					IParam param = (IParam) event.getElement();
					if (!isValidInputType(param)) {
						lockCheck = true;
						inputDataViewer.setChecked(param, false);
						lockCheck = false;
						return;
					}
					CompoundCommand compoundCommand = new CompoundCommand();
					if (event.getChecked()) {
						
						Object[] checkedObjects = inputDataViewer.getCheckedElements();
						for(Object checkedObject : checkedObjects) {							
							if (checkedObject != param) {
								IParam checkedParam = (IParam) checkedObject;
								setParamType(checkedParam, SmooksModelUtils.INPUT_DEACTIVE_TYPE, compoundCommand);
								lockCheck = true;
								inputDataViewer.setChecked(checkedParam, false);
								lockCheck = false;
							}							
						}
						if (param != null) {
							setParamType(param, SmooksModelUtils.INPUT_ACTIVE_TYPE, compoundCommand);
						}
					} else {
						if (param != null) {
							setParamType(param, SmooksModelUtils.INPUT_DEACTIVE_TYPE, compoundCommand);
						}
					}
					editingDomain.getCommandStack().execute(compoundCommand);
				}
			});
			inputDataViewer.addDoubleClickListener(new IDoubleClickListener() {

				public void doubleClick(DoubleClickEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					Object element = selection.getFirstElement();
					if (element instanceof IParam) {
						IParam param = (IParam) element;
						String type = param.getType();
						String filePath = param.getValue();
						if (type != null && filePath != null) {
							if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
								IFile file = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
								IJavaProject javaProject = JavaCore.create(file.getProject());
								if (javaProject != null) {
									try {
										if (filePath.endsWith("[]")) { //$NON-NLS-1$
											filePath = filePath.substring(0, filePath.length() - 2);
										}
										IJavaElement result = javaProject.findType(filePath);
										if (result != null)
											JavaUI.openInEditor(result);
										else {
											MessageDialog.openError(editorPart.getEditorSite().getShell(),
													Messages.SmooksReaderFormPage_CantFindTypeErrorTitle,
													"Can't find type \"" + filePath + "\" in \"" //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
															+ javaProject.getProject().getName() + "\" project."); //$NON-NLS-1$
										}
									} catch (Exception e) {

									}
								}
							} else {
								try {
									filePath = SmooksUIUtils.parseFilePath(filePath);
									if (filePath != null) {
										IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(filePath));
										IFileInfo fetchInfo = fileStore.fetchInfo();
										if (!fetchInfo.isDirectory() && fetchInfo.exists()) {
											IWorkbenchWindow window = editorPart.getEditorSite().getWorkbenchWindow();
											IWorkbenchPage page = window.getActivePage();
											try {
												IDE.openEditorOnFileStore(page, fileStore);
											} catch (PartInitException e) {
												MessageDialog.open(MessageDialog.ERROR, window.getShell(),
														Messages.SmooksReaderFormPage_OpenFileErrorTitle,
														"Can't open the file : '" + filePath + "'", SWT.SHEET); //$NON-NLS-1$ //$NON-NLS-2$
											}
										} else {
										}
									}
								} catch (Exception e) {
									MessageDialog.open(MessageDialog.ERROR, editorPart.getEditorSite().getWorkbenchWindow().getShell(),
											Messages.SmooksReaderFormPage_OpenFileErrorTitle,
											"Can't open the file : '" + filePath + "'", SWT.SHEET); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
								}
							}
						}
					}
				}
			});
			
			TableColumn pathColumn = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
			pathColumn.setText(Messages.SmooksReaderFormPage_PathColumnText);
			pathColumn.setWidth(600);

			inputDataViewer.setContentProvider(new ExtentionInputContentProvider());
			inputDataViewer.setLabelProvider(new InputDataViewerLabelProvider());
			inputDataViewer.getTable().setHeaderVisible(true);
			inputDataViewer.getTable().setLinesVisible(true);
			if (modelProvider != null) {
				inputDataViewer.setInput(getInputs());
			}
			
			Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
			gd = new GridData(GridData.FILL_VERTICAL);
			buttonComposite.setLayoutData(gd);
			GridLayout l = new GridLayout();
			buttonComposite.setLayout(l);

			addInputDataButton = new Button(buttonComposite, SWT.NONE);
			addInputDataButton.setText(Messages.SmooksReaderFormPage_AddButtonLabel);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			addInputDataButton.setLayoutData(gd);
			addInputDataButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					showInputDataWizard();
				}

			});

			removeInputDataButton = new Button(buttonComposite, SWT.NONE);
			removeInputDataButton.setText(Messages.SmooksReaderFormPage_DeleteButtonLabel);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			removeInputDataButton.setLayoutData(gd);
			removeInputDataButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selection = (IStructuredSelection) inputDataViewer.getSelection();
					if (selection != null) {
						List<IParam> inputs = selection.toList();
						CompoundCommand compoundCommand = new CompoundCommand();
						
						for (IParam input : inputs) {
							removeParam(input, compoundCommand);
						}
						
						if(!compoundCommand.isEmpty()) {
							editingDomain.getCommandStack().execute(compoundCommand);
						}
						
						if (!inputs.isEmpty()) {
							List<?> viewerInput = (List<?>) inputDataViewer.getInput();
							viewerInput.removeAll(inputs);
							// inputTypeChanged();
							if (inputDataViewer != null) {
								inputDataViewer.refresh();
							}
						}
					}
				}
			});
			
			refreshInputDataButtons();

			inputSourceConfigComposite.setBackgroundMode(SWT.TRANSPARENT);
			inputSourceConfigComposite.layout();			
		}
		
		protected void showInputDataWizard() {
			String inputType = modelProvider.getInputType();
			SmooksModel rootModel = configModel.getModelRoot();
			
			IStructuredDataSelectionWizard wizard = ViewerInitorStore.getInstance().getStructuredDataCreationWizard(inputType);
			WizardDialog dialog = new WizardDialog(editorPart.getEditorSite().getShell(), wizard);
			wizard.init(editorPart.getEditorSite(), editorPart.getEditorInput());
			
			if (dialog.open() == Dialog.OK) {
				String path = wizard.getStructuredDataSourcePath();
				Properties pros = wizard.getProperties();

				CompoundCommand compoundCommand = new CompoundCommand();
				IParam addedInputType = SmooksUIUtils.recordInputDataInfomation(editingDomain, rootModel.getParams(), inputType, path, pros, compoundCommand);

				if (addedInputType != null) {
					Object obj = this.inputDataViewer.getInput();
					
					if (obj != null && obj instanceof List) {
						((List) obj).add(addedInputType);
					}

					deactivateAllInputs(compoundCommand);
					
					if (inputType.equals(SmooksModelUtils.INPUT_TYPE_CUSTOME)) {
						// don't active the input file
					} else {
						setParamType(addedInputType, SmooksModelUtils.INPUT_ACTIVE_TYPE, compoundCommand);
					}
					
					if (!compoundCommand.isEmpty()) {
						editingDomain.getCommandStack().execute(compoundCommand);
					}
					
					if (inputDataViewer != null) {
						inputDataViewer.refresh();
					}
				}
			}
		}

		private void deactivateAllInputs(CompoundCommand command) {
			Object viewerInput = this.inputDataViewer.getInput();
			if (viewerInput != null && viewerInput instanceof List<?>) {
				List<IParam> inputList = (List<IParam>) viewerInput;
				for (IParam inputType : inputList ) {
					setParamType(inputType, SmooksModelUtils.INPUT_ACTIVE_TYPE, command);
				}
			}
		}
		
		private List<IParam> getInputs() {
			List<IParam> inputs = new ArrayList<IParam>();
			GlobalParams globalParams = configModel.getModelRoot().getParams();
			
			for(IParam param : globalParams.getParams()) {
				if(inputSourceType.isType(param.getName())) {
					inputs.add(param);
				}
			}
			
			return inputs;
		}
		
		private void refreshInputDataButtons() {
			this.addInputDataButton.setEnabled(true);
			this.removeInputDataButton.setEnabled(true);

			String inputType = modelProvider.getInputType();
			if (inputType == null || inputType.trim().equals("")) { //$NON-NLS-1$
				this.addInputDataButton.setEnabled(false);
				this.removeInputDataButton.setEnabled(false);
			}
		}

		protected boolean isValidInputType(IParam inputParam) {
			if (inputParam == null) {
				// not specified is OK...
				return true;
			}
			
			return inputSourceType.isType(inputParam.getName());
		}
		
		private class InputDataViewerLabelProvider extends ExtentionInputLabelProvider implements ITableColorProvider {

			public Color getBackground(Object element, int columnIndex) {
				if (!isValidInputType((IParam) element)) {
					// return ColorConstants.darkGray;
				}
				return null;
			}

			public Color getForeground(Object element, int columnIndex) {
				if (!isValidInputType((IParam) element)) {
					return ColorConstants.lightGray;
				}
				return null;
			}
		}		
	}
}
