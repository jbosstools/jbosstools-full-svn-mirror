/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.hibernate.eclipse.jdt.ui.internal.jpa.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.UndoEdit;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.jdt.ui.Activator;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.EntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.Utils;
import org.hibernate.eclipse.jdt.ui.internal.jpa.process.wizard.HibernateJPAWizard;
import org.hibernate.eclipse.jdt.ui.internal.jpa.process.wizard.IHibernateJPAWizardData;
import org.hibernate.eclipse.jdt.ui.internal.jpa.process.wizard.IHibernateJPAWizardParams;

/**
 * Modify entity classes
 *
 * @author Vitali
 */
public class AllEntitiesProcessor implements IHibernateJPAWizardParams {
	/**
	 * place to search compilation units
	 */
	protected IJavaProject javaProject = null;
	/**
	 * place to store default settings
	 */
	protected IPreferenceStore preferenceStore = null;
	/**
	 * annotation style
	 */
	protected AnnotStyle annotationStyle = AnnotStyle.FIELDS;
	/**
	 * annotation style preference of majority
	 */
	protected AnnotStyle annotationStylePreference = AnnotStyle.FIELDS;
	/**
	 * annotation style preference of majority
	 */
	public final static String storePropertyName = 
		"hibernate.jpa.generation.AnnotationStyle.preference"; //$NON-NLS-1$

	/**
	 * change info storage
	 */
	protected ArrayList<ChangeStructure> changes = new ArrayList<ChangeStructure>();
	
	public AllEntitiesProcessor() {
	}

	public IPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			preferenceStore = Activator.getDefault().getPreferenceStore();
		}
		return preferenceStore;
	}

	public void setPreferenceStore(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}

	public void initAnnotationStylePreference() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		int value = preferenceStore.getInt(storePropertyName);
		if (value >= AnnotStyle.values().length) {
			value = 0;
		}
		annotationStyle = AnnotStyle.values()[value];
	}
	
	public void saveAnnotationStylePreference() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		int value = 0;
		while (value < AnnotStyle.values().length) {
			if (AnnotStyle.values()[value] == annotationStyle) {
				break;
			}
			value++;
		}
		if (value >= AnnotStyle.values().length) {
			value = 0;
		}
		preferenceStore.setValue(storePropertyName, value);
	}

	/**
	 * execute modification for collection of entities
	 * @param project - common java project for collection of entities
	 * @param entities - collection
	 * @param askConfirmation - ask user confirmation (show dialog)
	 */
	public void modify(IJavaProject project, Map<String, EntityInfo> entities,
			boolean askConfirmation) {
		changes.clear();
		setJavaProject(project);
		// get the buffer manager
		ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
		Iterator<Map.Entry<String, EntityInfo>> it = entities.entrySet().iterator();
		/*String outText = ""; //$NON-NLS-1$
		String ls = System.getProperties().getProperty("line.separator", "\n");  //$NON-NLS-1$//$NON-NLS-2$
		while (it.hasNext()) {
			Map.Entry<String, EntityInfo> entry = it.next();
			if (entry.getValue().isAbstractFlag()) {
				continue;
			}
			if (entry.getValue().isCompilerProblemsFlag()) {
				// TODO: save entity name as has compiler problems
			}
			outText += entry.getKey() + (it.hasNext() ? ls : ""); //$NON-NLS-1$
		}*/
		boolean performChange = true;
        int res = 0;
        if (askConfirmation) {
        	/** /
    		final String outText2 = outText;
            MessageDialog dialog = new MessageDialog(JavaPlugin.getActiveWorkbenchShell(),
            		JdtUiMessages.AllEntitiesProcessor_header, null,
            		JdtUiMessages.AllEntitiesProcessor_message,
            		MessageDialog.QUESTION,
            		new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0) {
            	protected Control createCustomArea(Composite parent) {
            		Text messageText = new Text(parent, SWT.WRAP | SWT.V_SCROLL);
        			messageText.setText(outText2);
        			messageText.setEditable(false);
        			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL)
        				.grab(true, true)
        				.hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH),
        					convertHorizontalDLUsToPixels(2 * IDialogConstants.BUTTON_BAR_HEIGHT)).applyTo(messageText);

            		return messageText;
            	}
            	protected boolean isResizable() {
            		return true;
            	}
            };
            res = dialog.open();
            /**/
        }
        if (res == 0) {
			// TODO:
			// show warning about abstract classes
			// show warning about compiler problems
			// ...
			// modify accepted items
			if (getAnnotationStyle().equals(AnnotStyle.AUTO)) {
				setAnnotationStyle(getAnnotationStylePreference());
				reCollectModification(bufferManager, entities);
				setAnnotationStyle(AnnotStyle.AUTO);
			}
			else {
				reCollectModification(bufferManager, entities);
			}
		}
        else {
        	performChange = false;
        }
        //
        if (askConfirmation) {
        	if (!showRefactoringDialog(entities, bufferManager)) {
        		performChange = false;
        	}
        }
        if (performChange) {
			performChange(bufferManager);
        }
		performDisconnect(bufferManager);
	}

	protected void performDisconnect(ITextFileBufferManager bufferManager) {
		for (int i = 0; i < changes.size(); i++) {
			ChangeStructure cs = changes.get(i);
			try {
				bufferManager.disconnect(cs.path, LocationKind.IFILE, null);
			} catch (CoreException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("CoreException: ", e); //$NON-NLS-1$
			}
		}
		changes.clear();
	}

	protected void performChange(ITextFileBufferManager bufferManager) {
		for (int i = 0; i < changes.size(); i++) {
			ChangeStructure cs = changes.get(i);
			try {
				if (cs.textFileBuffer != null && cs.document != null && cs.textEdit != null &&
					((cs.change != null && cs.change.isEnabled()) || (cs.change == null))) {
					cs.document = cs.textFileBuffer.getDocument();
					UndoEdit undo = cs.textEdit.apply(cs.document);
					// commit changes to underlying file
					cs.textFileBuffer.commit(null, true);
				}
			} catch (CoreException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("CoreException: ", e); //$NON-NLS-1$
			} catch (MalformedTreeException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("MalformedTreeException: ", e); //$NON-NLS-1$
			} catch (BadLocationException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("BadLocationException: ", e); //$NON-NLS-1$
			}
		}
	}

	public void reCollectModification(ITextFileBufferManager bufferManager, 
			Map<String, EntityInfo> entities) {

		changes.clear();
		Iterator<Map.Entry<String, EntityInfo>> it = entities.entrySet().iterator();
		try {
			while (it.hasNext()) {
				Map.Entry<String, EntityInfo> entry = it.next();
				if (entry.getValue().isAbstractFlag()) {
					continue;
				}
				collectModification(bufferManager, entry.getKey(), entry.getValue(), entities);
			}
		} catch (CoreException e) {
			HibernateConsolePlugin.getDefault().logErrorMessage("CoreException: ", e); //$NON-NLS-1$
		}
	}

	public void collectModification(ITextFileBufferManager bufferManager, String fullyQualifiedName,
			EntityInfo entityInfo, Map<String, EntityInfo> entities) throws CoreException {

		ChangeStructure cs = new ChangeStructure();
		cs.fullyQualifiedName = fullyQualifiedName;
		ICompilationUnit icu = Utils.findCompilationUnit(javaProject, fullyQualifiedName);
		org.eclipse.jdt.core.dom.CompilationUnit cu = Utils.getCompilationUnit(icu, false);
		cs.path = cu.getJavaElement().getPath();
		try {
			bufferManager.connect(cs.path, LocationKind.IFILE, null);
			cs.textFileBuffer = bufferManager.getTextFileBuffer(cs.path, LocationKind.IFILE);
			// retrieve the buffer
			cs.document = cs.textFileBuffer.getDocument();
			AST ast = cu.getAST();
			ASTRewrite rewriter = ASTRewrite.create(ast);
			// ... rewrite
			ProcessEntityInfo processor = new ProcessEntityInfo();
			processor.setAnnotationStyle(annotationStyle);
			processor.setEntityInfo(entityInfo);
			processor.setEntities(entities);
			processor.setASTRewrite(rewriter);
			cu.accept(processor);
			//
			cs.textEdit = rewriter.rewriteAST(cs.document, JavaCore.getOptions());
			// add change to array of changes
			changes.add(cs);
		} catch (JavaModelException e) {
			HibernateConsolePlugin.getDefault().logErrorMessage("JavaModelException: ", e); //$NON-NLS-1$
		} catch (MalformedTreeException e) {
			HibernateConsolePlugin.getDefault().logErrorMessage("MalformedTreeException: ", e); //$NON-NLS-1$
		}
	}

	public boolean showRefactoringDialog(final Map<String, EntityInfo> entities, 
			final ITextFileBufferManager bufferManager) {

		IHibernateJPAWizardData data = new IHibernateJPAWizardData() {

			public ITextFileBufferManager getBufferManager() {
				return bufferManager;
			}

			public Map<String, EntityInfo> getEntities() {
				return entities;
			}

			public ArrayList<ChangeStructure> getChanges() {
				return changes;
			}
			
		};
		HibernateJPAWizard wizard = new HibernateJPAWizard(data, this);
		return wizard.showWizard();
	}

	protected void setJavaProject(IJavaProject project) {
		javaProject = project;
	}

	public AnnotStyle getAnnotationStyle() {
		return annotationStyle;
	}

	public void setAnnotationStyle(AnnotStyle annotationStyle) {
		this.annotationStyle = annotationStyle;
	}

	public AnnotStyle getAnnotationStylePreference() {
		return annotationStylePreference;
	}

	public void setAnnotationStylePreference(AnnotStyle annotationStylePreference) {
		this.annotationStylePreference = annotationStylePreference;
	}
}
