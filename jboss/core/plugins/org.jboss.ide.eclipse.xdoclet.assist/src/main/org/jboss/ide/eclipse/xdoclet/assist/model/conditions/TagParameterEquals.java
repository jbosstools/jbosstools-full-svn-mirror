/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   public TagParameterEquals(
         String tagName,
         String tagParameter,
         String tagValue)
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

