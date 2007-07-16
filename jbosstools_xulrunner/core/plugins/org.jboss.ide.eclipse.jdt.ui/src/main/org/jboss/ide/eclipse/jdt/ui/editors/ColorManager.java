/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
