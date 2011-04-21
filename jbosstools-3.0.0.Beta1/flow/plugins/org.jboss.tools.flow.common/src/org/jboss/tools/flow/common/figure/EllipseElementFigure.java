package org.jboss.tools.flow.common.figure;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class EllipseElementFigure extends AbstractElementFigure {

    private Ellipse ellipse;
    
    protected void customizeFigure() {
        ellipse = new Ellipse();
        add(ellipse, 0);
        ellipse.setBounds(getBounds());
    }
    
    public void setColor(Color color) {
        ellipse.setBackgroundColor(color);
    }
    
    public void setBounds(Rectangle rectangle) {
        super.setBounds(rectangle);
        ellipse.setBounds(rectangle);
    }
    
    public void setSelected(boolean b) {
        super.setSelected(b);
        ellipse.setLineWidth(b ? 3 : 1);
        repaint();
    }
    
    public ConnectionAnchor getSourceConnectionAnchor() {
        return new EllipseAnchor(this);
    }

    public ConnectionAnchor getTargetConnectionAnchor() {
        return new EllipseAnchor(this);
    }
    
}
