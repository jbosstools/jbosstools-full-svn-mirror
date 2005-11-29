/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Color Manager.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ColorManager
{
   /** Description of the Field */
   protected Map colors = new HashMap(10);


   /**
    * Description of the Method
    *
    * @param key  Description of the Parameter
    * @param rgb  Description of the Parameter
    */
   public void bindColor(String key, RGB rgb)
   {
      Object value = colors.get(key);
      if (value != null)
      {
         throw new UnsupportedOperationException();
      }

      Color color = new Color(Display.getCurrent(), rgb);

      colors.put(key, color);
   }


   /** Description of the Method */
   public void dispose()
   {
      Iterator i = colors.values().iterator();
      while (i.hasNext())
      {
         ((Color) i.next()).dispose();
      }
   }


   /**
    * Gets the color attribute of the ColorManager object
    *
    * @param key  Description of the Parameter
    * @return     The color value
    */
   public Color getColor(String key)
   {
      return (Color) colors.get(key);
   }


   /**
    * Description of the Method
    *
    * @param key  Description of the Parameter
    */
   public void unbindColor(String key)
   {
      Color color = (Color) colors.remove(key);
      if (color != null)
      {
         color.dispose();
      }
   }
}
