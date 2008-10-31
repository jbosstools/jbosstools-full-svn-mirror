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
package org.jboss.tools.smooks.ui.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.EventObject;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.ui.gef.editparts.SmooksEditPartFactory;
import org.jboss.tools.smooks.ui.gef.model.RootModel;
import org.jboss.tools.smooks.ui.modelparser.ParseEngine;

/**
 * @author Dart Peng
 * 
 */
public class SmooksGraphicalEditor extends GraphicalEditorWithPalette {

	protected ParseEngine engine;

	private boolean isDirty = false;

	private RootModel rootModel;

	public SmooksGraphicalEditor() {
		super();
		this.setEditDomain(new DefaultEditDomain(this));
		engine = new ParseEngine();
	}
	public void createPartControl(Composite parent) {
//		Splitter splitter = new Splitter(parent, SWT.HORIZONTAL);
		parent.setLayout(new FillLayout());
		SashForm splitter = new SashForm(parent,SWT.V_SCROLL);
		createPaletteViewer(splitter);
		createGraphicalViewer(splitter);
//		splitter.maintainSize(getPaletteViewer().getControl());
//		splitter.setFixedSize(getInitialPaletteSize());
//		splitter.addFixedSizeChangeListener(new PropertyChangeListener() {
//			public void propertyChange(PropertyChangeEvent evt) {
//				handlePaletteResized(((Splitter)evt.getSource()).getFixedSize());
//			}
//		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		rootModel = new RootModel();
		initInputContents(rootModel);
		viewer.setContents(rootModel);
	}

	protected void initInputContents(RootModel parent) {

		// TODO Test XML Data
		Resource resource = new XSDResourceFactoryImpl().createResource(URI
				.createFileURI("/root/Public/smooks_1_0.xsd"));
		try {
			resource.load(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		XSDSchema schema = (XSDSchema) resource.getContents().get(0);

//		AbstractStructuredDataModel model = engine.parseModel(schema,
//				new XMLStructuredModelContentProvider(),
//				new XMLStrucutredModelParser());
//		parent.addChild(model);
//
//		XMLObjectAnalyzer an = new XMLObjectAnalyzer();
//		try {
//			DocumentObject doc = an.analyze("/root/Public/test.xml");
//			AbstractStructuredDataModel model1 = engine.parseModel(doc,
//					new XMLStructuredModelContentProvider(),
//					new XMLStrucutredModelParser());
//			parent.addChild(model1);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}

		SmooksFileEditorInput input = null;
		if (this.getEditorInput() instanceof SmooksFileEditorInput) {
			input = (SmooksFileEditorInput) getEditorInput();
		}
		if (input == null)
			return;
//		BeanContentProvider provider = new BeanContentProvider();
//		JavaBeanParser parser = new JavaBeanParser();
//		JavaBeanModel sourceJavaBean = (JavaBeanModel) input.getSourceModel()
//				.get(0);
//		Object contents = engine.parseModel(sourceJavaBean, provider, parser);
//		parent.addChild(contents);
//
//		JavaBeanModel targetJavaBean = (JavaBeanModel) input.getTargetModel()
//				.get(0);
//		parent.addChild(engine.parseModel(targetJavaBean, provider, parser));

	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new SmooksEditPartFactory());
		GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(
				viewer);
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 0), this
				.getActionRegistry().getAction(GEFActionConstants.DELETE));
		viewer.setKeyHandler(keyHandler);
		SmooksGraphViewerContextMenuProvider provider = new SmooksGraphViewerContextMenuProvider(
				viewer, this.getActionRegistry());
		viewer.setContextMenu(provider);
		viewer.getEditDomain().getCommandStack().addCommandStackListener(
				new CommandStackListener() {

					/*
					 * (non-Javadoc)
					 * 
					 * @seeorg.eclipse.gef.commands.CommandStackListener#
					 * commandStackChanged(java.util.EventObject)
					 */
					public void commandStackChanged(EventObject arg0) {
						isDirty = true;
						firePropertyChange(PROP_DIRTY);
					}
				});
		// viewer.setRootEditPart(new FreeformGraphicalRootEditPart());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return isDirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Saving...", -1);
		IMappingAnalyzer analyzer = null;
		// TODO TEST
		if(true) return;
//		JavaBeanAnalyzer analyzer = new JavaBeanAnalyzer();
//		SmooksResourceListType resourceList = null;
//		monitor.worked(1);
//		try {
//			monitor.setTaskName("Anylize graph model...");
//			resourceList = analyzer.analyzeGraphicalModel(rootModel);
//			monitor.worked(1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		DocumentRoot doc = SmooksFactory.eINSTANCE.createDocumentRoot();
//		doc.setSmooksResourceList(resourceList);
//		IFileEditorInput input = (IFileEditorInput) getEditorInput();
//		String filePath = ResourcesPlugin.getWorkspace().getRoot()
//				.getLocation().append(input.getFile().getFullPath()).toString();
//		Resource resource = new SmooksResourceFactoryImpl().createResource(URI
//				.createFileURI(filePath));
//		resource.getContents().add(doc);
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		Exception exp = null;
//		try {
//			monitor.setTaskName("Creating new content stream...");
//			resource.save(stream, Collections.EMPTY_MAP);
//			input.getFile().setContents(
//					new ByteArrayInputStream(stream.toByteArray()), true, true,
//					monitor);
//			monitor.worked(1);
//		} catch (IOException e) {
//			exp = e;
//		} catch (CoreException e) {
//			exp = e;
//		}
//		if (exp != null) {
//			String title = "Error";
//			if (exp instanceof IOException) {
//				title = "IO Error";
//			}
//			MessageDialog
//					.openError(getSite().getShell(), title, exp.toString());
//			return;
//		}
//		isDirty = false;
//		firePropertyChange(PROP_DIRTY);
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		PaletteDrawer container = new PaletteDrawer("");
		container.add(new SelectionToolEntry());
		container.add(new ConnectionCreationToolEntry("Connection",
				"Connection", null, null, null));
//		container.add(new JavaBeanModelCreationToolEntry());
		root.add(container);
		return root;
	}

}
