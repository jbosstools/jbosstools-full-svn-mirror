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
package org.jboss.ide.eclipse.jdt.xml.ui.text.rules;

import org.eclipse.jface.text.rules.IWordDetector;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * XML Name detector.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NameDetector implements IWordDetector
{

   /**
    * Gets the wordPart attribute of the NameDetector object
    *
    * @param ch  Description of the Parameter
    * @return    The wordPart value
    */
   public boolean isWordPart(char ch)
   {
      if (Character.isUnicodeIdentifierPart(ch))
      {
         return true;
      }

      switch (ch)
      {
         case '.' :
         case '-' :
         case '_' :
         case ':' :
            return true;
      }

      return false;
   }

   /**
    * Gets the wordStart attribute of the NameDetector object
    *
    * @param ch  Description of the Parameter
    * @return    The wordStart value
    */
   public boolean isWordStart(char ch)
   {
      if (Character.isUnicodeIdentifierStart(ch))
      {
         return true;
      }

      switch (ch)
      {
         case '_' :
         case ':' :
            return true;
      }

      return false;
   }
}
