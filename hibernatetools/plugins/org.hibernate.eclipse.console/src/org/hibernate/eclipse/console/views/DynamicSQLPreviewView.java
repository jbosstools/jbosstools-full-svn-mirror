/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.console.views;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.hqleditor.HQLEditor;
import org.hibernate.eclipse.hqleditor.HQLEditorDocumentSetupParticipant;
import org.hibernate.eclipse.hqleditor.HQLSourceViewer;
import org.hibernate.eclipse.hqleditor.HQLSourceViewerConfiguration;
import org.hibernate.util.StringHelper;

public class DynamicSQLPreviewView extends ViewPart {

	private IPartListener2 partListener = new IPartListener2() {

		public void partInputChanged(IWorkbenchPartReference partRef) {

		}

		public void partVisible(IWorkbenchPartReference partRef) {

		}

		public void partHidden(IWorkbenchPartReference partRef) {

		}

		public void partOpened(IWorkbenchPartReference partRef) {

		}

		public void partDeactivated(IWorkbenchPartReference partRef) {

		}

		public void partClosed(IWorkbenchPartReference partRef) {
			if(partRef.getPart(false)==currentEditor) {
				setCurrentEditor(null);
			}
		}

		public void partBroughtToTop(IWorkbenchPartReference partRef) {
			hookIntoEditor(partRef);
		}

		public void partActivated(IWorkbenchPartReference partRef) {
			hookIntoEditor( partRef );
		}



	};

	private SourceViewer textViewer;
	private HQLEditor currentEditor;
    private MonoReconciler reconciler;
    private HQLEditorDocumentSetupParticipant docSetupParticipant = new HQLEditorDocumentSetupParticipant();

    private void hookIntoEditor(IWorkbenchPartReference partRef) {
    	if(partRef==null) {
    		setCurrentEditor(null);
    		return;
    	}
		IWorkbenchPart part = partRef.getPart(false);
		if(part!=null && (part instanceof HQLEditor)) {
			setCurrentEditor((HQLEditor) part);
		}
	}
	private void setCurrentEditor(HQLEditor editor) {
		if(editor==currentEditor) return;
		if(currentEditor!=null) {
			reconciler.uninstall();
		}

		currentEditor = editor;

		if(currentEditor!=null) {
			ITextViewer editorViewer = currentEditor.getTextViewer();
			reconciler.install(editorViewer);
		}

		updateText(currentEditor);

	}

	private void updateText(HQLEditor editor) {
		if (textViewer == null) {
			return;
		}
		IDocument doc = textViewer.getDocument();
		if (doc != null) {
			if (editor != null) {
				ConsoleConfiguration consoleConfig = editor.getConsoleConfiguration();
				if (StringHelper.isEmpty(editor.getQueryString())) {
					doc.set( HibernateConsoleMessages.DynamicSQLPreviewView_empty_hql_query );
				} else if (consoleConfig != null) {
					if (consoleConfig.isSessionFactoryCreated()) {
						String generateSQL = consoleConfig.generateSQL(editor.getQueryString());
						if (StringHelper.isEmpty(generateSQL)) {
							doc.set( HibernateConsoleMessages.DynamicSQLPreviewView_no_sql_generated );
						} else {
							doc.set(generateSQL);
						}
					} else {
						doc.set(HibernateConsoleMessages.DynamicSQLPreviewView_session_factory_not_created + consoleConfig.getName());
					}
				} else {
					doc.set(HibernateConsoleMessages.DynamicSQLPreviewView_no_console_conf_associated);
				}
			} else {
				doc.set(HibernateConsoleMessages.DynamicSQLPreviewView_no_hql_query_editor);
			}
		}
	}

	public void createPartControl(Composite parent) {
		textViewer = new HQLSourceViewer( parent, new VerticalRuler(1), null, false, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL );
		//textViewer.setEditable(false);
		IDocument doc = new Document();
		textViewer.setDocument( doc );

		docSetupParticipant.setup( doc );

		textViewer.getDocument().set(HibernateConsoleMessages.DynamicSQLPreviewView_no_hql_query_editor_selected);
		textViewer.configure(new HQLSourceViewerConfiguration(null));

		IWorkbenchWindow window = PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow();
		IPartService service = window.getPartService();

		hookIntoEditor(service.getActivePartReference());
	}

	public void init(IViewSite site) throws PartInitException {
		super.init(site);

		IReconcilingStrategy strategy = new AbstractReconcilingStrategy() {

			protected void doReconcile(final IDocument doc) {
				Display display = PlatformUI.getWorkbench().getDisplay();
				display.asyncExec(new Runnable() {

					public void run() {
						//textViewer.getDocument().set(doc.get());
						updateText(currentEditor);
					}
				});
			}

		};

		reconciler = new MonoReconciler(strategy,false);
		reconciler.setDelay(500);

		IWorkbenchWindow window = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow();
		IPartService service = window.getPartService();
		service.addPartListener(partListener);

		hookIntoEditor(service.getActivePartReference());

	}

	public void dispose() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow();
		IPartService service = window.getPartService();
		service.removePartListener(partListener);
		docSetupParticipant.unsetup();
		super.dispose();
	}

	public void setFocus() {

	}
}
