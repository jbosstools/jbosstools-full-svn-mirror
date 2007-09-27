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
package org.jboss.ide.eclipse.jdt.ui.text.source;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DefaultSourceViewerConfiguration extends SourceViewerConfiguration
{
   private IPreferenceStore store;

   /** Preference key for inserting spaces rather than tabs. */
   public final static String PREFERENCE_SPACES_FOR_TABS = "org.jboss.ide.eclipse.jdt.ui.text.source.spacesForTabs";//$NON-NLS-1$

   /** Preference key used to look up display tab width. */
   public final static String PREFERENCE_TAB_WIDTH = "org.jboss.ide.eclipse.jdt.ui.text.source.tabWidth";//$NON-NLS-1$

   /**
    *Constructor for the DefaultSourceViewerConfiguration object
    *
    * @param store  Description of the Parameter
    */
   public DefaultSourceViewerConfiguration(IPreferenceStore store)
   {
      this.store = store;
   }

   /**
    * Gets the indentPrefixes attribute of the DefaultSourceViewerConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @param contentType   Description of the Parameter
    * @return              The indentPrefixes value
    */
   public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType)
   {
      // prefix[0] is either '\t' or ' ' x tabWidth, depending on useSpaces

      int tabWidth = store.getInt(PREFERENCE_TAB_WIDTH);
      boolean useSpaces = store.getBoolean(PREFERENCE_SPACES_FOR_TABS);

      String[] prefixes = new String[tabWidth + 1];

      for (int i = 0; i <= tabWidth; i++)
      {
         StringBuffer prefix = new StringBuffer(tabWidth - 1);

         if (useSpaces)
         {
            for (int j = 0; j + i < tabWidth; j++)
            {
               prefix.append(' ');
            }

            if (i != 0)
            {
               prefix.append('\t');
            }
         }
         else
         {
            for (int j = 0; j < i; j++)
            {
               prefix.append(' ');
            }

            if (i != tabWidth)
            {
               prefix.append('\t');
            }
         }

         prefixes[i] = prefix.toString();
      }

      prefixes[tabWidth] = "";//$NON-NLS-1$

      return prefixes;
   }
}
