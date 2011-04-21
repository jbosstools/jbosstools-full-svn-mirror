/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.ide.eclipse.jdt.ui.IJDTUIConstants;
import org.jboss.ide.eclipse.jdt.ui.JDTUIImages;
import org.jboss.ide.eclipse.jdt.xml.core.ns.Namespace;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLAttributeContributor implements IAttributeContributor
{

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean appendAtStart()
   {
      return false;
   }


   /**
    * Gets the attributeProposals attribute of the XMLAttributeContributor object
    *
    * @param holder  Description of the Parameter
    * @param doc     Description of the Parameter
    * @param name    Description of the Parameter
    * @param start   Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        The attributeProposals value
    */
   public List getAttributeProposals(IReconcilierHolder holder, IDocument doc, String name, String start, int offset)
   {
      // Get possible values
      List words = this.getAttributes(holder, doc, name);

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < words.size(); i++)
      {
         String attribute = (String) words.get(i);
         if (attribute.startsWith(start))
         {
         	// generates attrib="<>" or attrib="<>defaultValue" where <> is cursorpos.
         	String defaultValue = getDefaultValueFor(holder.getReconcilier().getDTDGrammar(), name, attribute);
         	String insertion = attribute + (defaultValue==null?"=\"\"":"=\""+defaultValue+"\"");
         	String display = (defaultValue==null?attribute:insertion);
         	int cursorpos = attribute.length()+2;
            ICompletionProposal proposal = new CompletionProposal(insertion, offset - start.length(), start.length(), cursorpos, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_ATTRIBUTE), display, null, getAdditonalInfoFor(holder.getReconcilier().getDTDGrammar(), name, attribute));
            proposals.add(proposal);
         }
      }

      return proposals;
   }


   /**
    * @param grammar
    * @param tag
    * @param attribute
    * @return default value for attribute if it exists
    */
   private String getDefaultValueFor(Namespace grammar, String tag, String attribute) {
   	return grammar.getDefaultAttributeValue(tag, attribute);	
   }


/**
 * @param grammar
 * @param text
 * @return
 */
private String getAdditonalInfoFor(Namespace namespace, String tag, String attribute) {
	String comment = (String) namespace.getComments().get(tag + ">" + attribute);
	
	StringBuffer result = new StringBuffer();
	
	result.append("<p>\n");
	result.append("<b>Attribute:</b> " + attribute + "<br>\n");
	result.append("<b>Data type:</b> " + namespace.getDataType(tag, attribute) + "<br>\n");
	String[] attributeValues = namespace.getEnumeratedValues(tag, attribute);
	if(attributeValues!=null && attributeValues.length!=0) {
		result.append("<b>Enumerated values:</b><br>\n");
		for (int i = 0; i < attributeValues.length; i++) {
			String value = attributeValues[i];
			//TODO: how do indent these ?
			result.append(" - " + value + "<br>\n");
		}
	}
	if(comment!=null) {
		result.append("<br>"  + comment);
		result.append("\n</p>");
	}
	return result.toString();
}


/**
    * Gets the attributes attribute of the XMLAttributeContributor object
    *
    * @param h         Description of the Parameter
    * @param doc       Description of the Parameter
    * @param nodeName  Description of the Parameter
    * @return          The attributes value
    */
   protected List getAttributes(IReconcilierHolder h, IDocument doc, String nodeName)
   {
      List words = new ArrayList();

      Namespace dtd = h.getReconcilier().getDTDGrammar();
      Map namespaces = h.getReconcilier().getNamespaces();

      // Add proposals from DTD
      if (dtd != null)
      {
         List attributes = dtd.getAttributesForTag(nodeName);
         if (attributes != null)
         {
            words.addAll(attributes);
         }
      }

      // Add proposals from namespaces
      if (namespaces != null)
      {
         for (Iterator it = namespaces.keySet().iterator(); it.hasNext(); )
         {
            String key = (String) it.next();
            Namespace ns = (Namespace) namespaces.get(key);
            List attributes = ns.getAttributesForTag(nodeName);
            if (attributes != null)
            {
               words.addAll(attributes);
            }
         }
      }

      // Sort the result
      Collections.sort(words);

      return words;
   }
}
