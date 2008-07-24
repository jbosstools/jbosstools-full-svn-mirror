package org.jboss.tools.process.ruleflow.editor.editpart;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.flow.editor.editpart.ElementEditPart;
import org.jboss.tools.flow.editor.figure.RoundedRectangleElementFigure;
import org.jboss.tools.process.ruleflow.Activator;

/**
 * EditPart for a SubFlow node.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class SubProcessEditPart extends ElementEditPart {

    private static final Image ICON = ImageDescriptor.createFromURL(
        Activator.getDefault().getBundle().getEntry("icons/process.gif")).createImage();
    // TODO dispose color when no longer needed
    private static final Color COLOR = new Color(Display.getCurrent(), 255, 250, 205);
    
    protected IFigure createFigure() {
        RoundedRectangleElementFigure figure = new RoundedRectangleElementFigure();
        figure.setIcon(ICON);
        figure.setColor(COLOR);
        return figure;
    }
    
}
