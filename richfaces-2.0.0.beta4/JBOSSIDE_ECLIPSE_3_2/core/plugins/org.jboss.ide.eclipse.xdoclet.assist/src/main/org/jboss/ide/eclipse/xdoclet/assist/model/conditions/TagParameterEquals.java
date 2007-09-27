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
public class TagParameterEquals extends Condition
{
   /** desired tag value */
   private final String requiredTagValue;

   /** tag to check */
   private final String tagName;

   /** tag parameter to check */
   private final String tagParameter;

   /**
    *Constructor for the TagParameterEquals object
    *
    * @param tagName       Description of the Parameter
    * @param tagParameter  Description of the Parameter
    * @param tagValue      Description of the Parameter
    */
   public TagParameterEquals(String tagName, String tagParameter, String tagValue)
   {
      this.tagName = tagName;
      this.tagParameter = tagParameter;
      this.requiredTagValue = tagValue;
   }

   /**
    * check whether condition holds
    *
    * @param member  Description of the Parameter
    * @return        whether condition holds
    */
   public boolean evalInternal(IMember member)
   {
      //	We need improved eclipse javadoc capabilities to implement that
      //		String actualTagValue = xprogramelement.getDoc().getTagAttributeValue( _tagName, _tagParameter );
      //
      //		_log.debug( "Tag : " + _tagName + " parameter " + _tagParameter + " value: " + actualTagValue +
      //			"  required: " + _requiredTagValue + " equals: " + _requiredTagValue.equals( actualTagValue ) );
      //		return _requiredTagValue.equals( actualTagValue );

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
      return getClass().getName() + " : " + "@" + tagName + " " + tagParameter + "=\"" + requiredTagValue + "\"";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
   }

}
