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
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TagExists extends Condition
{
   /** tag to check */
   private final String tagName;

   /**
    * Constructs a condition that checks to see if the specified tag exists within
    * an XProgramElement.
    *
    * @param tagName  the name of the tag to check for
    */
   public TagExists(String tagName)
   {
      this.tagName = tagName;
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
      // return xprogramelement.getDoc().hasTag( tagName );

      try
      {
         String source = member.getSource();
         return (source.indexOf(this.tagName) >= 0);
      }
      catch (JavaModelException jme)
      {
         AbstractPlugin.log(jme);
      }
      return false;
   }

   /**
    * Describe what the method does
    *
    * @return   Describe the return value
    */
   public String toString()
   {
      return getClass().getName() + " : " + "@" + tagName;//$NON-NLS-1$ //$NON-NLS-2$
   }

}
