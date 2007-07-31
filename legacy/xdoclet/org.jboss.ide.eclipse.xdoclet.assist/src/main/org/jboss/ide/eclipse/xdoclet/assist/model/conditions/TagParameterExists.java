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
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   17 mai 2003
 */
public class TagParameterExists extends Condition
{
   private final String tagName;

   /** tag parameter to check */
   private final String tagParameter;

   /**
    *Constructor for the TagParameterExists object
    *
    * @param tagName       Description of the Parameter
    * @param tagParameter  Description of the Parameter
    */
   public TagParameterExists(String tagName, String tagParameter)
   {
      this.tagName = tagName;
      this.tagParameter = tagParameter;
   }

   /**
    * check whether condition holds
    *
    * @param member  Description of the Parameter
    * @return        whether condition holds
    */
   public boolean evalInternal(IMember member)
   {
      // We need improved eclipse javadoc capabilities to implement that
      // return xprogramelement.getDoc().getTagAttributeValue( _tagName, _tagParameter ) != null;

      try
      {
         String source = member.getSource();
         int tagPos = source.indexOf(this.tagName);
         if (tagPos >= 0)
         {
            return (source.indexOf(this.tagParameter, tagPos) >= 0);
         }
      }
      catch (JavaModelException jme)
      {
         AbstractPlugin.log(jme);
      }
      return false;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName() + " : " + "@" + tagName + " " + tagParameter;//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }

}
