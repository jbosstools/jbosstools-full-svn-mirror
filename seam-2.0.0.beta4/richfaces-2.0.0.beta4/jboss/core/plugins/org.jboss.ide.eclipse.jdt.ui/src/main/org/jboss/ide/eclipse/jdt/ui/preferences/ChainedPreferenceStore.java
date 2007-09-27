/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
      for (Iterator i = keys.iterator(); i.hasNext(); )
      {
         String key = (String) i.next();
         target.setDefault(key, source.getString(key));
      }

      source.addPropertyChangeListener(
         new IPropertyChangeListener()
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
