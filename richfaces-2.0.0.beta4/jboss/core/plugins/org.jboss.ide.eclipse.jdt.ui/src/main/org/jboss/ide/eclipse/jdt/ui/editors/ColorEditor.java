/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.editors;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * A "button" of a certain color determined by the color picker.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ColorEditor
{

   Button button;
   Color color;
   Image image;

   RGB rgb;
   private Point extent;


   /**
    *Constructor for the ColorEditor object
    *
    * @param parent  Description of the Parameter
    */
   public ColorEditor(Composite parent)
   {
      button = new Button(parent, SWT.PUSH);
      extent = computeImageSize(parent);
      image = new Image(parent.getDisplay(), extent.x, extent.y);

      GC gc = new GC(image);

      gc.setBackground(button.getBackground());
      gc.fillRectangle(0, 0, extent.x, extent.y);

      gc.dispose();

      button.setImage(image);

      button.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent event)
            {
               ColorDialog colorDialog = new ColorDialog(button.getShell());
               colorDialog.setRGB(rgb);

               RGB newColor = colorDialog.open();
               if (newColor != null)
               {
                  rgb = newColor;
                  updateColorImage();
               }
            }
         });

      button.addDisposeListener(
         new DisposeListener()
         {
            public void widgetDisposed(DisposeEvent event)
            {
               if (image != null)
               {
                  image.dispose();
                  image = null;
               }

               if (color != null)
               {
                  color.dispose();
                  color = null;
               }
            }
         });
   }


   /**
    * Gets the button attribute of the ColorEditor object
    *
    * @return   The button value
    */
   public Button getButton()
   {
      return button;
   }


   /**
    * Gets the colorValue attribute of the ColorEditor object
    *
    * @return   The colorValue value
    */
   public RGB getColorValue()
   {
      return rgb;
   }


   /**
    * Sets the colorValue attribute of the ColorEditor object
    *
    * @param rgb  The new colorValue value
    */
   public void setColorValue(RGB rgb)
   {
      this.rgb = rgb;

      updateColorImage();
   }


   /**
    * Description of the Method
    *
    * @param control  Description of the Parameter
    * @return         Description of the Return Value
    */
   protected Point computeImageSize(Control control)
   {
      Font f = JFaceResources.getFontRegistry()
         .get(JFaceResources.DEFAULT_FONT);

      GC gc = new GC(control);
      gc.setFont(f);

      int height = gc.getFontMetrics().getHeight();

      gc.dispose();

      return new Point(height * 3 - 6, height);
   }


   /** Description of the Method */
   protected void updateColorImage()
   {
      Display display = button.getDisplay();

      GC gc = new GC(image);

      gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
      gc.drawRectangle(0, 2, extent.x - 1, extent.y - 4);

      if (color != null)
      {
         color.dispose();
      }

      color = new Color(display, rgb);

      gc.setBackground(color);
      gc.fillRectangle(1, 3, extent.x - 2, extent.y - 5);

      gc.dispose();

      button.setImage(image);
   }
}
