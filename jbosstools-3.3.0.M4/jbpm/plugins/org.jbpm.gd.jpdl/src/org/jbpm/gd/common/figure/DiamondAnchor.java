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
package org.jbpm.gd.common.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class DiamondAnchor extends AbstractConnectionAnchor {
	
	public DiamondAnchor(IFigure owner) {
		super(owner);
	}
	
	public Point getLocation(Point reference) {
		Rectangle r = Rectangle.SINGLETON;
		r.setBounds(getOwner().getBounds());
		r.translate(-1, -1);
		r.resize(1, 1);
		getOwner().translateToAbsolute(r);
		
		Point ref = r.getCenter().negate().translate(reference);
		
		float centerX = r.x + 0.5f * r.width;
		float centerY = r.y + 0.5f * r.height;
		float dx, dy;
		
		if (Math.abs(ref.x * r.height) == Math.abs(Math.abs(ref.y)* r.width )) {
			dx = ref.x > 0 ? r.width / 4 : - r.width / 4;
			dy = ref.y > 0 ? r.height / 4 : - r.height / 4;
		} else if (ref.x == 0) {
			dx = 0;
			dy = ref.y > 0 ? r.height / 2 : - r.height / 2;
		} else {
		
			float numerator = ref.x * r.height * r.width / 2;
			float firstDenominator = Math.abs(ref.y) * r.width + ref.x * r.height;
			float secondDenominator = Math.abs(ref.y) * r.width - ref.x * r.height;
		
			dx = (ref.x > 0) ? numerator / firstDenominator : numerator / secondDenominator;
			dy = dx * ref.y / ref.x;
			
		}

		return new Point(Math.round(centerX + dx), Math.round(centerY + dy));
	}
	
}