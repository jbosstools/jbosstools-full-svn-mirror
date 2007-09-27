/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.ui.clay.editor;

import java.io.*;
import java.util.*;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.common.editor.AbstractSelectionProvider;
import org.eclipse.gef.*;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.stackview.CommandStackInspectorPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelTransferBuffer;
import org.jboss.tools.common.gef.*;
import org.jboss.tools.common.gef.action.IDiagramSelectionProvider;
import org.jboss.tools.common.gef.edit.GEFRootEditPart;
import org.jboss.tools.common.gef.editor.xpl.DefaultPaletteCustomizer;
import org.jboss.tools.common.gef.outline.xpl.DiagramContentOutlinePage;
import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.shale.model.clay.helpers.ClayStructureHelper;
import org.jboss.tools.shale.ui.clay.editor.action.*;
import org.jboss.tools.shale.ui.clay.editor.edit.*;
import org.jboss.tools.shale.ui.clay.editor.edit.xpl.ClayConnectionRouter;
import org.jboss.tools.shale.ui.clay.editor.figures.*;
import org.jboss.tools.shale.ui.clay.editor.model.*;
import org.jboss.tools.shale.ui.clay.editor.palette.*;
import org.jboss.tools.shale.ui.clay.editor.print.*;

public class ClayEditor extends GEFEditor implements IClayModelListener {
	
	public void componentAdd(IComponent component) {}

	public void processChanged() {}

	public void componentRemove(IComponent component) {}

	public boolean isClayModelListenerEnabled() {
		return true;
	}

	public void linkAdd(ILink link) {}

	public void linkRemove(ILink link) {}

	public static byte JSF_DIAGRAM_RENAME;
	public boolean isBordersPaint(){
		return true;
	}    
	
	//@deprecated
	public static boolean showMessage(Shell p, int type) {
		return true;
	}	
	
	protected void createPaletteViewer(Composite parent) {
		PaletteViewer viewer = new PaletteViewer();
		ClayPaletteViewerPreferences prefs = new ClayPaletteViewerPreferences(this);
		prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_COLUMNS, false);
		prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_LIST, false);
		prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_ICONS, false);
		prefs.setUseLargeIcons(PaletteViewerPreferences.LAYOUT_DETAILS, false);
		prefs.setLayoutSetting(PaletteViewerPreferences.LAYOUT_LIST);
		viewer.createControl(parent);
		setPaletteViewer(viewer);
		setPaletteLayout(prefs, loadPaletteSize());
		paletteViewer.setPaletteViewerPreferences(prefs);
		configurePaletteViewer();
		hookPaletteViewer();
		initializePaletteViewer();
	}
	
	private KeyHandler sharedKeyHandler;

	// This class listens to changes to the file system in the workspace, and 
	// makes changes accordingly.
	// 1) An open, saved file gets deleted -> close the editor
	// 2) An open file gets renamed or moved -> change the editor's input accordingly	

	private IClayModel model; 
	
	public ClayEditor(IEditorInput input) {
	super(input);
		setEditDomain(new DefaultEditDomain(this));
	}

	protected void closeEditor(boolean save) {
		getSite().getPage().closeEditor(ClayEditor.this, save);
	}

	public void commandStackChanged(EventObject event) {
		if (isDirty()) {
			if (!savePreviouslyNeeded()) {
				setSavePreviouslyNeeded(true);
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		} else {
			setSavePreviouslyNeeded(false);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
		super.commandStackChanged(event);
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#configurePaletteViewer()
	 */
	protected void configurePaletteViewer() {
		// super.configurePaletteViewer();
		PaletteViewer viewer = (PaletteViewer) getPaletteViewer();
		ContextMenuProvider provider = new PaletteContextMenuProvider(viewer);
		getPaletteViewer().setContextMenu(provider);
		viewer.setCustomizer(new DefaultPaletteCustomizer());
	}

	ScrollingGraphicalViewer viewer;

	public ScrollingGraphicalViewer getScrollingGraphicalViewer(){
		return viewer;
	}

	protected void configureGraphicalViewer() {
		//super.configureGraphicalViewer();
		viewer = (ScrollingGraphicalViewer)getGraphicalViewer();
		viewer.addSelectionChangedListener(modelSelectionProvider);
		ScalableFreeformRootEditPart root = new GEFRootEditPart();
		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		root.getZoomManager().setZoomLevels(new double[]{.25, .5, .75, 1.0/*, 2.0, 4.0*/});
		root.getZoomManager().setZoom(loadZoomSize());
		root.getZoomManager().addZoomListener(new ZoomListener(){
			public void zoomChanged(double zoom){
				saveZoomSize(zoom);
			}
		});
		register(zoomIn, false, true);
		register(zoomOut, false, true);
		//createActions();
		viewer.setRootEditPart(root);
		viewer.setEditPartFactory(new GraphicalPartFactory());
		ContextMenuProvider provider = new ClayContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(provider);
		getSite().registerContextMenu("ClayContextmenu", //$NON-NLS-1$
		provider, viewer);
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer)
				.setParent(getCommonKeyHandler()));
		//getControl().addMouseListener(new JSFMouseListener());
	}

	public void mouseEnter(MouseEvent e) {}
	
	public void mouseExit(MouseEvent e) {}
	
	public void mouseHover(MouseEvent e) {}
	
	public void mouseDoubleClick(MouseEvent e){
		boolean controlFlag = (e.stateMask&SWT.CONTROL) > 0;
		EditPart part = ClayEditor.this.getGraphicalViewer().findObjectAt(new Point(e.x, e.y));
		if(part instanceof ClayEditPart) ((ClayEditPart)part).doDoubleClick(controlFlag);
		else if(part instanceof LinkEditPart) ((LinkEditPart)part).doDoubleClick(controlFlag);
	}

	public void mouseDown(MouseEvent e) {}

	public void mouseUp(MouseEvent e) {}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public Control getControl(){
		return getPaletteViewer().getControl();
	}

	protected void createOutputStream(OutputStream os) throws IOException {}

	public void dispose() {
		model.removeClayModelListener(this);
		super.dispose();
	}

	public void doSave(IProgressMonitor progressMonitor) {}

	public void doSaveAs() {
	}

	public Object getAdapter(Class type){
		if(type == IDiagramSelectionProvider.class) {
			if(getScrollingGraphicalViewer() == null) return null;
			return new IDiagramSelectionProvider() {
				public ISelection getSelection() {
					if(getScrollingGraphicalViewer() == null) return null;
					return getScrollingGraphicalViewer().getSelection();
				}
			};
		}
		if (type == CommandStackInspectorPage.class)
			return new CommandStackInspectorPage(getCommandStack());
		if (type == IContentOutlinePage.class) {
			DiagramContentOutlinePage outline = new DiagramContentOutlinePage(new TreeViewer());
			outline.setGraphicalViewer(getGraphicalViewer());
			outline.setSelectionSynchronizer(getSelectionSynchronizer());
			return outline;
		}
	
		if (type == ZoomManager.class) {
			if(getGraphicalViewer() != null)
				return ((ScalableFreeformRootEditPart)getGraphicalViewer()
				.getRootEditPart()).getZoomManager();
		}
		return super.getAdapter(type);
	}

	protected int getInitialPaletteSize() {
		return 22;
	}

	protected void handlePaletteResized(int newSize) {}

/**
 * Returns the KeyHandler with common bindings for both the Outline and Graphical Views.
 * For example, delete is a common action.
 */
	protected KeyHandler getCommonKeyHandler(){
		return sharedKeyHandler;
	}

	public IClayModel getClayModel() {
		return model;
	}

	static private GEFConnectionCreationToolEntry connectionCreationTool=null;
	protected PaletteContainer createControlGroup(PaletteRoot root){
		PaletteGroup controlGroup = new PaletteGroup("control");
		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
		ToolEntry tool = new SelectionToolEntry();
		tool.setDescription(ClayEditorMessages.getString("ClayDiagram.select"));
		entries.add(tool);
		root.setDefaultEntry(tool);

		tool = new MarqueeToolEntry();
		tool.setDescription(ClayEditorMessages.getString("ClayDiagram.marquee"));
		entries.add(tool);
	
		PaletteSeparator sep = new PaletteSeparator("separator");
		sep.setUserModificationPermission(PaletteSeparator.PERMISSION_NO_MODIFICATION);
		entries.add(sep);
		connectionCreationTool = new GEFConnectionCreationToolEntry(
				ClayEditorMessages.getString("ClayDiagram.create-new-connection"),
				ClayEditorMessages.getString("ClayDiagram.create-new-connection"),
				null,
				ImageDescriptor.createFromFile(ClayEditor.class,"icons/new_transition.gif"),//$NON-NLS-1$
				null//$NON-NLS-1$
		) {
			protected void dragFinished() {
				XModelTransferBuffer.getInstance().disable();
			}
		};
		connectionCreationTool.setUnloadWhenFinished(switchToSelectionTool);
		entries.add(connectionCreationTool);	
		entries.add(sep);
		CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
				ClayEditorMessages.getString("ClayDiagram.component-template"),
				ClayEditorMessages.getString("ClayDiagram.component-template"),
				"component template", //TemplateConstants.TEMPLATE_DEFINITION,
				new SimpleFactory(String.class),
				ImageDescriptor.createFromFile(ClayEditor.class, "icons/new_component.gif"), 
				null//$NON-NLS-1$
		);
		entries.add(combined);	
		controlGroup.addAll(entries);
		return controlGroup;
	}

	public void gotoMarker(IMarker marker) {}

	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(getClayModel());
		getGraphicalViewer().addDropTargetListener(
				(TransferDropTargetListener)new ClayTemplateTransferDropTargetListener(getGraphicalViewer()));
		((ConnectionLayer)((ScalableFreeformRootEditPart)getGraphicalViewer().getRootEditPart()).getLayer(ScalableFreeformRootEditPart.CONNECTION_LAYER)).setConnectionRouter(new ClayConnectionRouter());
	}

	protected void initializePaletteViewer() {
		getEditDomain().setPaletteRoot(getPaletteRoot());
		FigureCanvas canvas = (FigureCanvas) paletteViewer.getControl();
		makeUnwrapPaletteItems(canvas.getContents());
		canvas.getContents().revalidate();
		canvas.setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
		canvas.setVerticalScrollBarVisibility(FigureCanvas.NEVER);
		getPaletteViewer().addDragSourceListener(
				new TemplateTransferDragSourceListener(getPaletteViewer()));
	}

	protected void createActions() {
		// super.createActions();
		register(new ClayDeleteAction(this), true, false);
		register(new ClayCopyAction(this), true, true);
		register(new ClayPasteAction(this), true, true);
		register(new ClayCutAction(this), true, true);
		register(new MyPrintAction(this), false, true);
//TODO
//		action = new ClayPreferencesAction(this);
//		registry.registerAction(action);
	}
	
	private void register(IAction action, boolean isSelectionAction, boolean isKeyBindingAction) {
		ActionRegistry registry = getActionRegistry();
		registry.registerAction(action);
		if(isSelectionAction) {
			getSelectionActions().add(action.getId());
		}
		if(isKeyBindingAction) {
			getSite().getKeyBindingService().registerAction(action);
		}
	}

public class MyPrintAction extends WorkbenchPartAction{
	private Insets printMargin = new Insets(1, 1, 1, 1);
	
	public MyPrintAction(IEditorPart editor) {
		super(editor);
	}
	
	protected boolean calculateEnabled() {
		return true;
	}	

	protected void init() {
		super.init();
		setText("Print Diagram");
		setToolTipText("Print Diagram");
		setId("Print_Diagram");
	}
	
	public org.eclipse.draw2d.geometry.Rectangle getPrintRegion(Printer printer) {
		org.eclipse.swt.graphics.Rectangle trim = printer.computeTrim(0, 0, 0, 0);
		org.eclipse.swt.graphics.Rectangle clientArea = printer.getClientArea();
		org.eclipse.swt.graphics.Point printerDPI = printer.getDPI();
	
		org.eclipse.draw2d.geometry.Rectangle printRegion = new org.eclipse.draw2d.geometry.Rectangle();
		printRegion.x =
			Math.max(
				(printMargin.left * printerDPI.x) / 72 - trim.width,
				clientArea.x);
		printRegion.y =
			Math.max(
				(printMargin.top * printerDPI.y) / 72 - trim.height,
				clientArea.y);
		printRegion.width =
			(clientArea.x + clientArea.width)
				- printRegion.x
				- Math.max(0, (printMargin.right * printerDPI.x) / 72 - trim.width);
		printRegion.height =
			(clientArea.y + clientArea.height)
				- printRegion.y
				- Math.max(0, (printMargin.bottom * printerDPI.y) / 72 - trim.height);
		org.eclipse.draw2d.geometry.Rectangle r = new org.eclipse.draw2d.geometry.Rectangle(clientArea.x,clientArea.y,clientArea.width,clientArea.height);
		return r;
	}
	
	public void run() {
		GraphicalViewer viewer;
		viewer = (GraphicalViewer)getWorkbenchPart().getAdapter(GraphicalViewer.class);
			
	    PrintPreviewDialog d = new PrintPreviewDialog(this.getWorkbenchPart().getSite().getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		d.setPrintViewer(viewer);
		d.setEditor(ClayEditor.this);
		Printer printer = new Printer();
		Exception ex = null;
		try {
			printer.getDPI();
///			int pw = printer.getClientArea().width;
				printer.getClientArea();
		} catch (Exception ee) {
			ex = ee;
			printer.dispose();
			d = null;
			ProblemReportingHelper.reportProblem(ModelUIPlugin.PLUGIN_ID, ee);
		}
		if(ex == null) {
			d.setPages(new Pages(viewer,new PageFormat(printer,this.getWorkbenchPart().getSite().getShell().getDisplay())));
			String result = d.open();
			if(result!=null&&result.equals("ok")){
				LayerManager lm = (LayerManager)viewer.getEditPartRegistry().get(LayerManager.ID);
				IFigure figure = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
				PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
				PrinterData data = dialog.open();
				 
				if (data != null) {
					printer = new Printer(data);
					d.getPages().getPageFormat();
					double scale = d.getPages().getScale();
					
					double dpiScale = printer.getDPI().x / Display.getCurrent().getDPI().x;
					getPrintRegion(printer);
					figure.getBounds();
					GC printerGC = new GC(printer);
					SWTGraphics g = new SWTGraphics(printerGC);
					Graphics graphics = new PrinterGraphics(g, printer);			
					if (printer.startJob(getWorkbenchPart().getTitle())) {
							Pages p = d.getPages();
							for(int i=0; i<p.getNumberOfPages(); i++){
								if(printer.startPage()){
									graphics.pushState();
									Page pg = p.getPrintable(i);
									Rectangle r1 = pg.getRectangle();
									Rectangle r = new Rectangle(r1.x+p.ix,r1.y+p.iy,r1.width,r1.height);
									org.eclipse.draw2d.geometry.Rectangle clipRect = new org.eclipse.draw2d.geometry.Rectangle();
									graphics.translate(-(int)(r.x*dpiScale*scale), -(int)(r.y*dpiScale*scale));
									graphics.getClip(clipRect);
									clipRect.setLocation((int)(r.x*dpiScale*scale), (int)(r.y*dpiScale*scale));
									graphics.clipRect(clipRect);
									graphics.scale(dpiScale*scale);
									figure.paint(graphics);

									graphics.popState();
									printer.endPage();
								}
							}
							graphics.dispose();
							printer.endJob();
					}
					
				}	
			}	
		}
	}
}

	public boolean isDirty() {
		return isSaveOnCloseNeeded();
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	public boolean isSaveOnCloseNeeded() {
		return getCommandStack().isDirty();
	}

	protected boolean performSaveAs() {
		return false;
	}

	private boolean savePreviouslyNeeded() {
		return savePreviouslyNeeded;
	}

	public void setInput(XModelObject input) {}

	static private boolean switchToSelectionTool=false;

	public void setClayModel(IClayModel diagram) {
		model = diagram;
		model.addClayModelListener(this);
		switchToSelectionTool = false;
	}

	private void setSavePreviouslyNeeded(boolean value) {
		savePreviouslyNeeded = value;
	}

	public ISelectionProvider getModelSelectionProvider() {
		return modelSelectionProvider;
	}
	
	private ModelSelectionProvider modelSelectionProvider = new ModelSelectionProvider();
	
	class ModelSelectionProvider extends AbstractSelectionProvider implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			fireSelectionChanged();
			updateActions(getSelectionActions());
		}
		protected XModelObject getSelectedModelObject() {
			if(viewer == null) return null;
			XModelObject o = getTarget(viewer.getSelection());
			return ClayStructureHelper.instance.getReference(o);			
		}
		
		public void scroll(FreeformViewport vp, ComponentFigure figure) {
			int delta;
			int SCROLL_MARGIN = 20;
			Point origin = vp.getViewLocation();

			if ((figure.getLocation().x - SCROLL_MARGIN) < origin.x) {
				delta = origin.x - (figure.getLocation().x - SCROLL_MARGIN);
				origin.x -= delta;
			} else if ((figure.getLocation().x + figure.getSize().width + SCROLL_MARGIN) > (origin.x + vp
					.getSize().width)) {
				delta = figure.getLocation().x + figure.getSize().width
						+ SCROLL_MARGIN - (origin.x + vp.getSize().width);
				origin.x += delta;
			}

			if ((figure.getLocation().y - SCROLL_MARGIN) < origin.y) {
				delta = origin.y - (figure.getLocation().y - SCROLL_MARGIN);
				origin.y -= delta;
			} else if ((figure.getLocation().y + figure.getSize().height + SCROLL_MARGIN) > (origin.y + vp
					.getSize().height)) {
				delta = figure.getLocation().y + figure.getSize().height
						+ SCROLL_MARGIN - (origin.y + vp.getSize().height);
				origin.y += delta;
			}

			if (origin.x != vp.getViewLocation().x
					|| origin.y != vp.getViewLocation().y)
				vp.setViewLocation(origin);
		}

		protected void setSelectedModelObject(XModelObject object) {
			IClayElement element = getClayModel().findElement(object.getPath());
			if(element == null) return;
			EditPart part = (EditPart)viewer.getEditPartRegistry().get(element);
			if(part != null){
				viewer.setSelection(new StructuredSelection(part));
				ClayDiagramEditPart diagram = (ClayDiagramEditPart)getScrollingGraphicalViewer().getRootEditPart().getChildren().get(0);
				FreeformViewport vp = diagram.getFreeformViewport();
				if(vp != null && part instanceof ClayEditPart){
					ComponentFigure fig = (ComponentFigure)((ClayComponentEditPart)part).getFigure();
					if(fig.getLocation().x == 0 && fig.getLocation().y == 0){
					}
					scroll(vp, fig);
				}
			}
		}
	}

	private XModelObject getTarget(ISelection ss) {
		if(ss.isEmpty() || !(ss instanceof StructuredSelection)) return null;
		return getTarget(((StructuredSelection)ss).getFirstElement());
	}
	
	private XModelObject getTarget(Object selected) {
		if(selected instanceof ClayEditPart) {
			ClayEditPart part = (ClayEditPart)selected;
			Object partModel = part.getModel();
			if(partModel instanceof IClayElement) {
				return (XModelObject)((IClayElement)partModel).getSource();
			}
		}
		if(selected instanceof LinkEditPart) {
			LinkEditPart part = (LinkEditPart)selected;
			Object partModel = part.getModel();
			if(partModel instanceof IClayElement) {
				return (XModelObject)((IClayElement)partModel).getSource();
			}
		}
		return null;
	}
	
	protected void hookGraphicalViewer() {
		getSelectionSynchronizer().addViewer(getGraphicalViewer());
	}

}

// dnd

class ClayTemplateTransferDropTargetListener extends TemplateTransferDropTargetListener {

	public ClayTemplateTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	protected CreationFactory getFactory(Object template) {
		return new ClayTemplateFactory();
	}

	class ClayTemplateFactory implements CreationFactory {
		public Object getNewObject(){
			return new String("component");
		}

		public Object getObjectType(){
			return String.class;
		}
	}

}
