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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.ide.eclipse.jdt.ui.IJDTUIConstants;
import org.jboss.ide.eclipse.jdt.ui.JDTUIImages;
import org.jboss.ide.eclipse.jdt.xml.core.ns.Namespace;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLTagContributor implements ITagContributor
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
    * Gets the tagProposals attribute of the XMLTagContributor object
    *
    * @param holder            Description of the Parameter
    * @param doc               Description of the Parameter
    * @param node              Description of the Parameter
    * @param lastOpenTag       Description of the Parameter
    * @param previousStartTag  Description of the Parameter
    * @param nextEndTag        Description of the Parameter
    * @param outerTag          Description of the Parameter
    * @param start             Description of the Parameter
    * @param translated        Description of the Parameter
    * @param offset            Description of the Parameter
    * @return                  The tagProposals value
    */
   public List getTagProposals(IReconcilierHolder holder, IDocument doc, XMLNode node, XMLNode lastOpenTag, XMLNode previousStartTag, XMLNode nextEndTag,
      String outerTag, String start, int translated, int offset)
   {
      // Get possible values
      List words = this.getTags(holder, doc, outerTag);

      // Build completion proposals
      List proposals = new ArrayList();

      if (start.length() == 0)
      {
         boolean isAfterLesserThan = false;

         if (offset > 0)
         {
            try
            {
               isAfterLesserThan = "<".equals(doc.get(offset - 1, 1));//$NON-NLS-1$
            }
            catch (BadLocationException e)
            {
            }
         }

         // Build proposals for found words
         for (int i = 0; i < words.size(); i++)
         {
            String tag = (String) words.get(i);
            Namespace grammar = holder.getReconcilier().getDTDGrammar();
            
			ICompletionProposal proposal = computeSingleElementProposal(grammar, offset, 0, isAfterLesserThan, tag);            
			proposals.add(proposal);
         }

         // Additional proposal for content
         proposals.addAll(0, this.getTagBodyProposals(lastOpenTag, offset, 0));
         
         if(!isAfterLesserThan) {
         	proposals.add(new CompletionProposal("<!--  -->", offset, 0, 5, null, "<!-- XML Comment", null, null));
         }
      }
      else
      { // inside a (partial) tag name (e.g. '<cl')
         int nodeOffset = offset - (translated - node.getOffset());

         if (start.startsWith("/")//$NON-NLS-1$
         )
         {

            if (lastOpenTag != holder.getReconcilier().getRoot() && lastOpenTag.getName().startsWith(start.substring(1)))
            {
            	// TODO: add "close with" text
               ICompletionProposal proposal = new CompletionProposal(lastOpenTag.getName() + ">", nodeOffset + 2, offset - nodeOffset - 2, lastOpenTag.getName().length() + 1, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_TAG), lastOpenTag.getName(), null, null);//$NON-NLS-1$
               proposals.add(proposal);
            }
         }
         else
         {
            //
            for (int i = 0; i < words.size(); i++)
            {
               String text = (String) words.get(i);
               if (text.startsWith(start))
               {
               	//ICompletionProposal proposal = new CompletionProposal(text + "></" + text + ">", nodeOffset + 1, offset - nodeOffset - 1, text.length() + 1, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_TAG), "(old)" + text, null, null);//$NON-NLS-1$ //$NON-NLS-2$
               	//proposals.add(proposal);
               	proposals.add(computeSingleElementProposal(holder.getReconcilier().getDTDGrammar(), nodeOffset + 1, offset - nodeOffset - 1, true, text));
               }
            }

            // Additional proposal for content
            proposals.addAll(0, this.getTagBodyProposals(lastOpenTag, nodeOffset + 1, offset - nodeOffset - 1));
         }
      }

      return proposals;
   }   
   
   /**
 * @param grammar
 * @param offset
 * @param isAfterLesserThan
 * @param tag
 * @return
 */
private ICompletionProposal computeSingleElementProposal(Namespace grammar, int offset, int replacementLength, boolean isAfterLesserThan, String tag) {
	String insertion = tag;
	int cursorpos = 0;
	String additonalInfo = getAdditonalInfoFor(grammar, tag);
	String requiredAttribs = grammar.getRequiredAttribsString(tag);
	
	if(requiredAttribs==null || requiredAttribs.length()==0) {
		insertion = tag + "></" + tag + ">";
		cursorpos = tag.length() + 1;
	} else {
		insertion = tag + requiredAttribs +"></" + tag + ">";
		cursorpos = tag.length() + requiredAttribs.indexOf('"') + 1;
		if(cursorpos<0) { // no req attrib found, so we calculate it 
			cursorpos = tag.length() + requiredAttribs.length() + 1;
		}		
	}
	
	if (!isAfterLesserThan) {
		insertion = "<" + insertion;
		cursorpos = cursorpos + 1;
	}	
	ICompletionProposal proposal = new CompletionProposal(insertion, offset, replacementLength, cursorpos, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_TAG), tag, null, additonalInfo);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	return proposal;
}


private String getAdditonalInfoFor(Namespace namespace, String tag) {   	
   	String comment = (String) namespace.getComments().get(tag);
   	
   	StringBuffer result = new StringBuffer();
   	
   	result.append("<p>\n");
   	result.append("<b>Element:</b> " + tag + "<br>\n");
   	result.append("<b>Content model:</b> " + namespace.getContentModel(tag) + "<br>\n");
   	if(comment!=null) {
   		result.append("<br>"  + comment);
   		result.append("\n</p>");
   	}
   	return result.toString();
   }
   

/**
    * Gets the tagBodyProposals attribute of the XMLTagContributor object
    *
    * @param lastOpenTag        Description of the Parameter
    * @param replacementOffset  Description of the Parameter
    * @param replacementLength  Description of the Parameter
    * @return                   The tagBodyProposals value
    */
   protected List getTagBodyProposals(XMLNode lastOpenTag, int replacementOffset, int replacementLength)
   {
      return Collections.EMPTY_LIST;
   }


   /**
    * Gets the tags attribute of the XMLTagContributor object
    *
    * @param h         Description of the Parameter
    * @param doc       Description of the Parameter
    * @param outerTag  Description of the Parameter
    * @return          The tags value
    */
   protected List getTags(IReconcilierHolder h, IDocument doc, String outerTag)
   {
      List words = new ArrayList();

      Namespace dtd = h.getReconcilier().getDTDGrammar();
      Map namespaces = h.getReconcilier().getNamespaces();

      // Add proposals from DTD
      if (dtd != null)
      {
         List tags = (List) dtd.getIncludes().get(outerTag);
         if (tags != null)
         {
            words.addAll(tags);
         }
      }

      // Add proposals from namespaces
      if (namespaces != null)
      {
         for (Iterator it = namespaces.keySet().iterator(); it.hasNext(); )
         {
            String key = (String) it.next();
            Namespace ns = (Namespace) namespaces.get(key);
            Map inc = ns.getIncludes();
            List tags = (List) inc.get(outerTag);
            if (tags != null)
            {
               words.addAll(tags);
            }
         }
      }

      // Sort the result
      Collections.sort(words);

      return words;
   }
}
