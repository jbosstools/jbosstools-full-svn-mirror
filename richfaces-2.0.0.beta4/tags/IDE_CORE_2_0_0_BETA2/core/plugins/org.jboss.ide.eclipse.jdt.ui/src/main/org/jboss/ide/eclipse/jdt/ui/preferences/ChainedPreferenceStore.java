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
package org.jboss.ide.eclipse.jdt.ui.preferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ChainedPreferenceStore
{
   /**
    * Description of the Method
    *
    * @param source  Description of the Parameter
    * @param target  Description of the Parameter
    * @param keys    Description of the Parameter
    */
   public static void startPropagating(IPreferenceStore source, IPreferenceStore target, String[] keys)
   {
      startPropagating(source, target, new HashSet(Arrays.asList(keys)));
   }

   /**
    * Description of the Method
    *
    * @param source  Description of the Parameter
    * @param target  Description of the Parameter
    * @param keys    Description of the Parameter
    */
   public static void startPropagating(final IPreferenceStore source, final IPreferenceStore target, final Set keys)
   {
      for (Iterator i = keys.iterator(); i.hasNext();)
      {
         String key = (String) i.next();
         target.setDefault(key, source.getString(key));
      }

      source.addPropertyChangeListener(new IPropertyChangeListener()
      {
         public void propertyChange(PropertyChangeEvent event)
         {
            String key = event.getProperty();

            if (keys.contains(key))
            {
               target.setDefault(key, source.getString(key));
               if (target.isDefault(key))
               {
                  target.firePropertyChangeEvent(key, event.getOldValue(), event.getNewValue());
               }
            }
         }
      });
   }
}
