/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.completion;

import java.util.HashMap;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.xdoclet.assist.model.IDocletConstants;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   15 mai 2003
 * @todo      Javadoc to complete
 */
public class XDocletJavaDocCompletionProcessor implements IJavadocCompletionProcessor
{
   private DocletAssistant docletAssistant;


   /** Constructor for DocletCompletionProcessor. */
   public XDocletJavaDocCompletionProcessor()
   {
      super();
      docletAssistant = new DocletAssistant();
   }


   /**
    * Description of the Method
    *
    * @param cu      Description of the Parameter
    * @param offset  Description of the Parameter
    * @param length  Description of the Parameter
    * @param flags   Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor#computeCompletionProposals(org.eclipse.jdt.core.ICompilationUnit, int, int, int)
    */
   public IJavaCompletionProposal[] computeCompletionProposals(ICompilationUnit cu, int offset, int length, int flags)
   {
      try
      {
         if (cu.getElementAt(offset) instanceof IMember)
         {
            IMember member = (IMember) cu.getElementAt(offset);
            // set variables
            HashMap variables = new HashMap();
            variables.put(IDocletConstants.CLASSNAME_VARIABLE, cu.findPrimaryType().getElementName());
            variables.put(IDocletConstants.PACKAGE_VARIABLE, cu.findPrimaryType().getPackageFragment().getElementName());
            return docletAssistant.getProposals(cu.getSource(), offset, member, variables);
         }
      }
      catch (JavaModelException e)
      {
         AbstractPlugin.log(e);
      }
      // change:stop
      return null;
   }


   /**
    * Description of the Method
    *
    * @param cu      Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor#computeContextInformation(org.eclipse.jdt.core.ICompilationUnit, int)
    */
   public IContextInformation[] computeContextInformation(ICompilationUnit cu, int offset)
   {
      return null;
   }


   /**
    * Gets the errorMessage attribute of the XDocletJavaDocCompletionProcessor object
    *
    * @return   The errorMessage value
    * @see      org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor#getErrorMessage()
    */
   public String getErrorMessage()
   {
      return "ERROR";//$NON-NLS-1$
   }
}
