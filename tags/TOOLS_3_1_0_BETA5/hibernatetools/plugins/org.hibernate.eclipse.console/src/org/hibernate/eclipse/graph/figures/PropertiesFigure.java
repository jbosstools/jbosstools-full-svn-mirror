package org.hibernate.eclipse.graph.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;

public class PropertiesFigure extends Figure {

	public PropertiesFigure(Color bgColor, Color fgColor) {
		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment( FlowLayout.ALIGN_LEFTTOP );
		layout.setStretchMinorAxis( true );		
		layout.setHorizontal( false );
		setLayoutManager( layout );
		setBorder( new PropertiesFigureBorder() );
		setBackgroundColor( bgColor );
		setForegroundColor( fgColor );
		setOpaque( true );
	}

	class PropertiesFigureBorder extends AbstractBorder {

		public Insets getInsets(IFigure figure) {
			return new Insets( 5, 3, 3, 1 );
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(
					getPaintRectangle( figure, insets ).getTopLeft(), tempRect
							.getTopRight() );
		}
	}
}
