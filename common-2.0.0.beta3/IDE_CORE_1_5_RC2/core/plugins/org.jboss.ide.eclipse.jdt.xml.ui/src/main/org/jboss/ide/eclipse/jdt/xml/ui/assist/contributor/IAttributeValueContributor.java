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
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/**
 * Description of the Interface
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IAttributeValueContributor
{
   /**
    * Gets the attributeValueProposals attribute of the IAttributeValueContributor object
    *
    * @param holder     Description of the Parameter
    * @param doc        Description of the Parameter
    * @param node       Description of the Parameter
    * @param attribute  Description of the Parameter
    * @param quote      Description of the Parameter
    * @param start      Description of the Parameter
    * @param offset     Description of the Parameter
    * @return           The attributeValueProposals value
    */
   public List getAttributeValueProposals(IReconcilierHolder holder, IDocument doc, XMLNode node, XMLNode attribute, char quote, String start, int offset);


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean appendAtStart();
}
