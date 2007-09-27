/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.contributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.assist.JSPTagLibRepository;
import org.jboss.ide.eclipse.jdt.ui.IJDTUIConstants;
import org.jboss.ide.eclipse.jdt.ui.JDTUIImages;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.IAttributeContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.IAttributeValueContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.ITagContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPTagLibContributor implements ITagContributor, IAttributeContributor, IAttributeValueContributor
{
   /** Description of the Field */
   protected Map attributeValues = new HashMap();
   /** Description of the Field */
   protected Map attributes = new HashMap();
   /** Description of the Field */
   protected IFile file;
   /** Description of the Field */
   protected IJavaProject project;
   /** Description of the Field */
   protected List tags = new ArrayList();


   /**
    *Constructor for the JSPTagLibContributor object
    *
    * @param file     Description of the Parameter
    * @param project  Description of the Parameter
    */
   public JSPTagLibContributor(IFile file, IJavaProject project)
   {
      this.file = file;
      this.project = project;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean appendAtStart()
   {
      return true;
   }


   /**
    * Gets the attributeProposals attribute of the JSPTagLibContributor object
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
      JSPTagLibRepository.getInstance().loadInformations(this.file);
      List words = JSPTagLibRepository.getInstance().getAttributes(this.file, name);

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < words.size(); i++)
      {
         String text = (String) words.get(i);
         if (text.startsWith(start))
         {
            ICompletionProposal proposal = new CompletionProposal(text, offset - start.length(), start.length(), text.length(), JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_ATTRIBUTE), text, null, null);
            proposals.add(proposal);
         }
      }

      return proposals;
   }


   /**
    * Gets the attributeValueProposals attribute of the JSPTagLibContributor object
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
   public List getAttributeValueProposals(IReconcilierHolder holder, IDocument doc, XMLNode node, XMLNode attribute, char quote, String start, int offset)
   {
      // Get possible values
      JSPTagLibRepository.getInstance().loadInformations(this.file);
      List words = JSPTagLibRepository.getInstance().getAttributeValues(this.file, node.getName(), attribute.getName());

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < words.size(); i++)
      {
         String text = (String) words.get(i);
         if (text.startsWith(start))
         {
            if (quote == '\'' || quote == '"')
            {
               ICompletionProposal proposal = new CompletionProposal(text + quote, offset - start.length(), start.length(), text.length() + 1, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_VALUE), text, null, null);
               proposals.add(proposal);
            }
            else
            {
               ICompletionProposal proposal = new CompletionProposal('"' + text + '"', offset - start.length(), start.length(), text.length() + 2,
                  JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_VALUE), text, null, null);
               proposals.add(proposal);
            }
         }
      }

      return proposals;
   }


   /**
    * Gets the tagProposals attribute of the JSPTagLibContributor object
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
      JSPTagLibRepository.getInstance().loadInformations(this.file);
      List words = JSPTagLibRepository.getInstance().getTags(this.file);

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
            String text = (String) words.get(i);

            //if (prefStore.getBoolean("InsertEndTag")) {
            if (true)
            {
               if (isAfterLesserThan)
               {
                  ICompletionProposal proposal = new CompletionProposal(text + "></" + text + ">", offset, 0, text.length() + 1, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_JSP_TAGLIB), text, null, null);//$NON-NLS-1$ //$NON-NLS-2$
                  proposals.add(proposal);
               }
               else
               {
                  ICompletionProposal proposal = new CompletionProposal("<" + text + "></" + text + ">", offset, 0, text.length() + 2, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_JSP_TAGLIB), text, null, null);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                  proposals.add(proposal);
               }
            }
            else
            {
               ICompletionProposal proposal = new CompletionProposal(text, offset, 0, text.length(), JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_JSP_TAGLIB), text, null, null);
               proposals.add(proposal);
            }
         }
      }
      else
      {
         int nodeOffset = offset - (translated - node.getOffset());

         if (start.startsWith("/")//$NON-NLS-1$
         )
         {
            if (lastOpenTag != holder.getReconcilier().getRoot() && lastOpenTag.getName().startsWith(start.substring(1)))
            {
               ICompletionProposal proposal = new CompletionProposal(lastOpenTag.getName() + ">", nodeOffset + 2, offset - nodeOffset - 2, lastOpenTag.getName().length() + 1, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_JSP_TAGLIB), lastOpenTag.getName(), null, null);//$NON-NLS-1$
               proposals.add(proposal);
            }
         }
         else
         {
            for (int i = 0; i < words.size(); i++)
            {
               String text = (String) words.get(i);
               if (text.startsWith(start))
               {
                  //if (prefStore.getBoolean("InsertEndTag")) {
                  if (true)
                  {
                     ICompletionProposal proposal = new CompletionProposal(text + "></" + text + ">", nodeOffset + 1, offset - nodeOffset - 1, text.length() + 1, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_JSP_TAGLIB), text, null, null);//$NON-NLS-1$ //$NON-NLS-2$
                     proposals.add(proposal);
                  }
                  else
                  {
                     ICompletionProposal proposal = new CompletionProposal(text, nodeOffset + 1, offset - nodeOffset - 1, text.length(), JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_JSP_TAGLIB), text, null, null);
                     proposals.add(proposal);
                  }
               }
            }
         }
      }

      return proposals;
   }
}
