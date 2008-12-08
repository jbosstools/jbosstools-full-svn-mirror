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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.ui.gef.editparts.SmooksEditPartFactory;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.RootModel;
import org.jboss.tools.smooks.ui.gef.tools.MappingPanelDropTargetListener;
import org.jboss.tools.smooks.ui.gef.tools.TargetTreeDropTargetListener;


/**
 * @deprecated
 * @author Dart Peng
 * @Date Aug 5, 2008
 */
public class MappingGraphCompositeCreator {
	protected TreeViewer sourceViewer;
	protected TreeViewer targetViewer;
	protected SelectionSynchronizer selectionSynchronizer;
	protected GraphicalViewer graphicalViewer;
	protected DefaultEditDomain editDomain;
	protected GraphRootModel rootModel;

	public void createMappingGraphComposite(FormToolkit toolkit,Composite mappingMainComposite){
		{
			Composite composite1 = toolkit
					.createComposite(mappingMainComposite);
			GridLayout layout = new GridLayout();
			layout.marginHeight = 1;
			layout.marginWidth = 1;
			GridData gd = new GridData(GridData.FILL_BOTH);
			composite1.setLayout(layout);
			sourceViewer = new TreeViewer(composite1, SWT.NONE);
			sourceViewer.getTree().setLayoutData(gd);
			sourceViewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY
					| DND.DROP_LINK, new Transfer[] { TemplateTransfer
					.getInstance() }, new DragSourceAdapter() {
				public void dragStart(DragSourceEvent event) {
					event.data = sourceViewer.getSelection();
					TemplateTransfer.getInstance().setTemplate(
							sourceViewer.getSelection());
					event.doit = true;
				}
			});
			sourceViewer.getTree().addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					rootModel.firePropertyChange(RootModel.P_REFRESH_PANEL,
							null, new Object());
				}
			});
			// TODO Test
//			sourceViewer.setContentProvider(new BeanContentProvider());
//			sourceViewer.setLabelProvider(new BeanlabelProvider());
//			JavaBeanModel model = JavaBeanModelFactory
//					.getJavaBeanModel(Order.class);
			List modelList = new ArrayList();
//			modelList.add(model);
			sourceViewer.setInput(modelList);
			sourceViewer.expandAll();
			composite1.setLayoutData(gd);
			toolkit.paintBordersFor(composite1);
		}
		{
			Composite composite2 = toolkit
					.createComposite(mappingMainComposite);
			GridData composite2LData = new GridData();
			composite2LData.grabExcessHorizontalSpace = true;
			composite2LData.grabExcessVerticalSpace = true;
			composite2LData.horizontalAlignment = GridData.FILL;
			composite2LData.verticalAlignment = GridData.FILL;
			composite2.setLayoutData(composite2LData);
			composite2.setLayout(new FillLayout());
			this.setGraphicalViewer(createGraphicalViewer(composite2));
		}
		{
			Composite composite3 = toolkit
					.createComposite(mappingMainComposite);
			GridData composite3LData = new GridData();
			composite3LData.grabExcessHorizontalSpace = true;
			composite3LData.verticalAlignment = GridData.FILL;
			composite3LData.grabExcessVerticalSpace = true;
			composite3LData.horizontalAlignment = GridData.FILL;
			GridLayout layout = new GridLayout();
			layout.marginWidth = 1;
			layout.marginHeight = 1;
			composite3.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_BOTH);
			targetViewer = createTargetTreeViewer(composite3, SWT.NONE);
			composite3.setLayoutData(gd);
			targetViewer.getTree().setLayoutData(gd);
			targetViewer.addDropSupport(DND.DROP_TARGET_MOVE | DND.DROP_MOVE
					| DND.DROP_LINK | DND.DROP_LINK,
					new Transfer[] { TemplateTransfer.getInstance() },
					new TargetTreeDropTargetListener(targetViewer,
							getGraphicalViewer()));
			targetViewer.getTree().addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					rootModel.firePropertyChange(RootModel.P_REFRESH_PANEL,
							null, new Object());
				}
			});

			// TODO Test

//			targetViewer.setContentProvider(new BeanContentProvider());
//			targetViewer.setLabelProvider(new BeanlabelProvider());
//			JavaBeanModel model = JavaBeanModelFactory
//					.getJavaBeanModel(LineOrder.class);
			List modelList = new ArrayList();
//			modelList.add(model);
			targetViewer.setInput(modelList);
			targetViewer.expandAll();
			toolkit.paintBordersFor(composite3);
		}
	}
	
	protected TreeViewer createTargetTreeViewer(Composite parent, int style) {
		TreeViewer viewer = new TreeViewer(parent, SWT.NONE | style);
		return viewer;
	}
	protected GraphicalViewer createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);
		return viewer;
	}
	
	protected void hookGraphicalViewer() {
		getSelectionSynchronizer().addViewer(getGraphicalViewer());
		getGraphicalViewer().addDropTargetListener(
				new MappingPanelDropTargetListener(this.getGraphicalViewer()));
	}

	protected void initGraphicalViewer() {
		this.getGraphicalViewer().getControl().setBackground(
				ColorConstants.white);

		rootModel = new GraphRootModel();
//		this.createSourceGraphModels();
//		this.createTargetGraphModels();

		this.getGraphicalViewer().setEditPartFactory(
				new SmooksEditPartFactory());
		this.getGraphicalViewer().setContents(rootModel);
	}

	public SelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null)
			selectionSynchronizer = new SelectionSynchronizer();
		return selectionSynchronizer;
	}

	public void setSelectionSynchronizer(
			SelectionSynchronizer selectionSynchronizer) {
		this.selectionSynchronizer = selectionSynchronizer;
	}

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(DefaultEditDomain editDomain) {
		this.editDomain = editDomain;
	}

	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	public void setGraphicalViewer(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
		this.getEditDomain().addViewer(graphicalViewer);
	}

}
