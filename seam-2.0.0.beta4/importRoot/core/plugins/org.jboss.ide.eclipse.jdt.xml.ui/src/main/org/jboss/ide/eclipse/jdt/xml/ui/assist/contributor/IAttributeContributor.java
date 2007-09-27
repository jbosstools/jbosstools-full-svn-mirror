/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;


/**
 * Description of the Interface
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IAttributeContributor
{
   /**
    * Gets the attributeProposals attribute of the IAttributeContributor object
    *
    * @param holder  Description of the Parameter
    * @param doc     Description of the Parameter
    * @param name    Description of the Parameter
    * @param start   Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        The attributeProposals value
    */
   public List getAttributeProposals(IReconcilierHolder holder, IDocument doc, String name, String start, int offset);


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean appendAtStart();
}
