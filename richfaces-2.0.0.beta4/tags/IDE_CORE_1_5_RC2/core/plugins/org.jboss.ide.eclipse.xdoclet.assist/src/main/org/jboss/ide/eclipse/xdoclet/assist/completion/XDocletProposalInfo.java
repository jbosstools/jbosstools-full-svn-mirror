/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.completion;

import org.eclipse.jdt.internal.ui.text.java.ProposalInfo;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 * @todo      Javadoc to complete
 */
public class XDocletProposalInfo extends ProposalInfo
{
   /** Description of the Field */
   protected String text;


   /**
    *Constructor for the XDocletProposalInfo object
    *
    * @param text  Description of the Parameter
    */
   public XDocletProposalInfo(String text)
   {
      super(null);
      if (text == null)
      {
         text = "";//$NON-NLS-1$
      }
      this.text = text;
   }


   /**
    * @return   The info value
    * @see      org.eclipse.jdt.internal.ui.text.java.ProposalInfo#getInfo()
    */
   public String getInfo()
   {
      return text;
   }
}
