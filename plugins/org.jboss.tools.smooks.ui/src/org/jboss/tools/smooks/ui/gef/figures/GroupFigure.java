package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.ViewportLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.ui.gef.util.figures.ContainerLayout;
import org.jboss.tools.smooks.ui.gef.util.figures.FillLayout;


//----------------------------------
//| GroupFigure Head Area          |
//|                                |
//| -----------------------------  |
//| |                           |  |
//| |  GroupFigure Client Area  |  |
//| |                           |  |
//| |                           |  |
//| |                           |  |
//| |                           |  |
//| |                           |  |
//| |                           |  |
//| -----------------------------  |
//----------------------------------
public class GroupFigure extends RectangleFigure implements GraphicsConstants {
	
	private static final int DEFAULT_MIN_HEIGHT = 250;
	
	protected ScrollPane scrollpane;
	/**
	 * Text Name
	 */
	protected Label label;
	protected ContainerFigure outerPane;
	
	public GroupFigure() {

	    FillLayout outerLayout = new FillLayout() {
			protected Dimension calculatePreferredSize(IFigure parent,
					int width, int height) {
				Dimension d = super.calculatePreferredSize(parent, width,
						height);
				d.union(new Dimension(100, DEFAULT_MIN_HEIGHT));
				return d;
			}
		};
		setLayoutManager(outerLayout);
		setFill(false);
		createFigure();
	}
	
	public void doLayout() {
		layout();
		setValid(true);
	}
	
	public FillLayout getFillLayout() {
		return (FillLayout)getLayoutManager();
	}
	
	protected void createFigure() {
		outerPane = new ContainerFigure();
	    outerPane.setBorder(new RoundedLineBorder(1, 6));
	    outerPane.setForegroundColor(groupBorderColor);
	    
	    ContainerFigure r = new ContainerFigure();
	    //r.setCornerDimensions(new Dimension(4, 4));   
	    r.setOutline(false);
	    r.setMinimumSize(new Dimension(0, 0));
	    
	    r.setFill(true);

	    r.setBackgroundColor(groupHeaderColor);
	    outerPane.add(r);
	    
	    //ContainerFigure labelHolder = new ContainerFigure();                             
	    //labelHolder.add(label);
	    label = new Label();
	    label.setForegroundColor(ColorConstants.black);
	    label.setBorder(new MarginBorder(2, 4, 2, 4));
	    r.add(label); //Holder);
//	    label.setTextAlignment(PositionConstants.RIGHT);
	    RectangleFigure line = new RectangleFigure();
	    line.setPreferredSize(20, 1);
	    outerPane.add(line);
	    
	    int minHeight = 250;
	    final int theMinHeight = minHeight;
	    FillLayout outerLayout = new FillLayout() {
			protected Dimension calculatePreferredSize(IFigure parent,
					int width, int height) {
				Dimension d = super.calculatePreferredSize(parent, width,
						height);
				d.union(new Dimension(100, theMinHeight));
				return d;
			}
		};
		
	    outerLayout.setHorizontal(false);
//	    outerLayout.setSpacing(10);
	    outerPane.setLayoutManager(outerLayout);
	    
	    scrollpane = new ScrollPane();
	    scrollpane.setForegroundColor(ColorConstants.black);
	    scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC); //ScrollPane.ALWAYS);
	    outerPane.add(scrollpane);
	    
	    ContainerFigure pane = new ContainerFigure();
	    pane.setBorder(new MarginBorder(5, 8, 5, 8));
	    ContainerLayout layout = new ContainerLayout();
	    layout.setHorizontal(false);
	    layout.setSpacing(0);
	    pane.setLayoutManager(layout);
	    
	    Viewport viewport = new Viewport();
		viewport.setContentsTracksHeight(true);
		ViewportLayout viewportLayout = new ViewportLayout() {
			protected Dimension calculatePreferredSize(IFigure parent,
					int width, int height) {
				Dimension d = super.calculatePreferredSize(parent, width,
						height);
				d.height = Math.min(d.height, theMinHeight - 25); // getViewer().getControl().getBounds().height);
				return d;
			}
		};

	    viewport.setLayoutManager(viewportLayout);

	    scrollpane.setViewport(viewport);
	    scrollpane.setContents(pane);
	    
	    this.add(outerPane);
	}
	
	public void setTextName(String name) {
		label.setText(name);
	}

	/**
	 * @return
	 */
	public String getTextName() {
		return label == null ? null : label.getText();
	}
}