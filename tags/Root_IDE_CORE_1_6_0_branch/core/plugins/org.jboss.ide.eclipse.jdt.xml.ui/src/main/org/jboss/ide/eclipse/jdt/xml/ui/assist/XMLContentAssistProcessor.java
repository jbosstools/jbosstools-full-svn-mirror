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
package org.jboss.ide.eclipse.jdt.xml.ui.assist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.jboss.ide.eclipse.jdt.ui.text.rules.IPositionTranslator;
import org.jboss.ide.eclipse.jdt.xml.core.ns.Namespace;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIMessages;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.IAttributeContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.IAttributeValueContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.ITagContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.XMLAttributeContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.XMLAttributeValueContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.XMLTagContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLDocumentPartitioner;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class XMLContentAssistProcessor implements IContentAssistProcessor
{
   /** Description of the Field */
   protected List attributeContributors = new ArrayList();

   /** Description of the Field */
   protected List attributeValueContributors = new ArrayList();

   /** Description of the Field */
   protected IReconcilierHolder holder;

   /** Description of the Field */
   protected IPreferenceStore store;

   /** Description of the Field */
   protected List tagContributors = new ArrayList();

   /** Description of the Field */
   protected IPositionTranslator translator;

   /**
    *Constructor for the XMLContentAssistProcessor object
    *
    * @param holder      Description of the Parameter
    * @param store       Description of the Parameter
    * @param translator  Description of the Parameter
    */
   public XMLContentAssistProcessor(IReconcilierHolder holder, IPreferenceStore store, IPositionTranslator translator)
   {
      this.holder = holder;
      this.store = store;
      this.translator = translator;

      // Add default contributors
      this.addTagContributor(new XMLTagContributor());
      this.addAttributeContributor(new XMLAttributeContributor());
      this.addAttributeValueContributor(new XMLAttributeValueContributor());
   }

   /**
    * Adds a feature to the AttributeContributor attribute of the XMLContentAssistProcessor object
    *
    * @param contributor  The feature to be added to the AttributeContributor attribute
    */
   public void addAttributeContributor(IAttributeContributor contributor)
   {
      this.attributeContributors.add(contributor);
   }

   /**
    * Adds a feature to the AttributeValueContributor attribute of the XMLContentAssistProcessor object
    *
    * @param contributor  The feature to be added to the AttributeValueContributor attribute
    */
   public void addAttributeValueContributor(IAttributeValueContributor contributor)
   {
      this.attributeValueContributors.add(contributor);
   }

   /**
    * Adds a feature to the TagContributor attribute of the XMLContentAssistProcessor object
    *
    * @param contributor  The feature to be added to the TagContributor attribute
    */
   public void addTagContributor(ITagContributor contributor)
   {
      this.tagContributors.add(contributor);
   }

   /**
    * Description of the Method
    *
    * @param viewer  Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        Description of the Return Value
    */
   public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset)
   {
      return null;
   }

   /**
    * Gets the contextInformationAutoActivationCharacters attribute of the XMLContentAssistProcessor object
    *
    * @return   The contextInformationAutoActivationCharacters value
    */
   public char[] getContextInformationAutoActivationCharacters()
   {
      return null;
   }

   /**
    * Gets the contextInformationValidator attribute of the XMLContentAssistProcessor object
    *
    * @return   The contextInformationValidator value
    */
   public IContextInformationValidator getContextInformationValidator()
   {
      return null;
   }

   /**
    * Gets the errorMessage attribute of the XMLContentAssistProcessor object
    *
    * @return   The errorMessage value
    */
   public String getErrorMessage()
   {
      return JDTXMLUIMessages.getString("XMLContentAssistProcessor.error.message");//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param doc         Description of the Parameter
    * @param node        Description of the Parameter
    * @param translated  Description of the Parameter
    * @param offset      Description of the Parameter
    * @param quote       Description of the Parameter
    * @return            Description of the Return Value
    */
   protected ICompletionProposal[] computeAttributeValues(IDocument doc, XMLNode node, int translated, int offset,
         char quote)
   {
      // Get the attribute node
      XMLNode attribute = node.getAttributeAt(translated);

      // Attribute value start
      String start = this.getAttributeValueStart(node, translated, quote);

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < attributeValueContributors.size(); i++)
      {
         IAttributeValueContributor contributor = (IAttributeValueContributor) attributeValueContributors.get(i);
         List result = contributor.getAttributeValueProposals(this.holder, doc, node, attribute, quote, start, offset);
         if (contributor.appendAtStart())
         {
            proposals.addAll(0, result);
         }
         else
         {
            proposals.addAll(result);
         }
      }

      ICompletionProposal[] cp = (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
      return cp;
   }

   /**
    * Description of the Method
    *
    * @param doc         Description of the Parameter
    * @param node        Description of the Parameter
    * @param translated  Description of the Parameter
    * @param offset      Description of the Parameter
    * @return            Description of the Return Value
    */
   protected ICompletionProposal[] computeAttributes(IDocument doc, XMLNode node, int translated, int offset)
   {
      // Attribute start
      String start = this.getAttributeStart(node, translated);

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < attributeContributors.size(); i++)
      {
         IAttributeContributor contributor = (IAttributeContributor) attributeContributors.get(i);
         List result = contributor.getAttributeProposals(this.holder, doc, node.getName(), start, offset);
         if (contributor.appendAtStart())
         {
            proposals.addAll(0, result);
         }
         else
         {
            proposals.addAll(result);
         }
      }

      ICompletionProposal[] cp = (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
      return cp;
   }

   /**
    * Description of the Method
    *
    * @param doc         Description of the Parameter
    * @param node        Description of the Parameter
    * @param translated  Description of the Parameter
    * @param offset      Description of the Parameter
    * @return            Description of the Return Value
    */
   protected ICompletionProposal[] computeTags(IDocument doc, XMLNode node, int translated, int offset)
   {
      XMLNode lastOpenTag = null;
      String start = node == null ? "" : node.getContentTo(translated);//$NON-NLS-1$
      String outerTag = null;

      Position[] pos = this.getPositions(doc);
      if (node == null || node.getParent() == null)
      {
         if (pos.length > 0)
         {
            XMLNode n = (XMLNode) pos[pos.length - 1];
            if (XMLPartitionScanner.XML_TAG.equals(n.getType()))
            {
               lastOpenTag = (XMLNode) pos[pos.length - 1];
            }
            else
            {
               lastOpenTag = ((XMLNode) pos[pos.length - 1]).getParent();
            }
         }
      }
      else if (XMLPartitionScanner.XML_END_TAG.equals(node.getType()))
      {
         int index = this.getIndexOf(pos, node);

         if (index > 0)
         {
            XMLNode n = (XMLNode) pos[index - 1];
            if (XMLPartitionScanner.XML_TAG.equals(n.getType()))
            {
               lastOpenTag = (XMLNode) pos[index - 1];
            }
            else
            {
               lastOpenTag = ((XMLNode) pos[index - 1]).getParent();
            }
         }
      }
      else
      {
         lastOpenTag = node.getParent();
      }

      if (lastOpenTag == null || lastOpenTag == this.holder.getReconcilier().getRoot())
      {
         outerTag = Namespace.TOPLEVEL;
      }
      else
      {
         outerTag = lastOpenTag.getName();
      }

      // Search for previous start tag and next end tag
      XMLNode prevStartTag = this.getPreviousStartTagNode(doc, offset);
      XMLNode nextEndTag = this.getNextEndTagNode(doc, offset);

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < tagContributors.size(); i++)
      {
         ITagContributor contributor = (ITagContributor) tagContributors.get(i);
         List result = contributor.getTagProposals(this.holder, doc, node, lastOpenTag, prevStartTag, nextEndTag,
               outerTag, start, translated, offset);
         if (contributor.appendAtStart())
         {
            proposals.addAll(0, result);
         }
         else
         {
            proposals.addAll(result);
         }
      }

      ICompletionProposal[] cp = (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
      return cp;
   }

   /**
    * Gets the attributeStart attribute of the XMLContentAssistProcessor object
    *
    * @param node    Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        The attributeStart value
    */
   protected String getAttributeStart(XMLNode node, int offset)
   {
      String start = "";//$NON-NLS-1$
      String content = node.getContentTo(offset);
      int index = content.length() - 1;

      while (index >= 0 && !Character.isWhitespace(content.charAt(index)))
      {
         start = content.charAt(index) + start;
         index--;
      }

      return start;
   }

   /**
    * Gets the attributeValueStart attribute of the XMLContentAssistProcessor object
    *
    * @param node        Description of the Parameter
    * @param translated  Description of the Parameter
    * @param quote       Description of the Parameter
    * @return            The attributeValueStart value
    */
   protected String getAttributeValueStart(XMLNode node, int translated, char quote)
   {
      String start = "";//$NON-NLS-1$
      String content = node.getContentTo(translated);
      int index = content.length() - 1;

      if (quote == '\0')
      {
         while (index >= 0 && !Character.isWhitespace(content.charAt(index)) && content.charAt(index) != '=')
         {
            start = content.charAt(index) + start;
            index--;
         }
      }
      else
      {
         while (index >= 0 && content.charAt(index) != quote)
         {
            start = content.charAt(index) + start;
            index--;
         }
      }

      return start;
   }

   /**
    * Gets the indexOf attribute of the XMLContentAssistProcessor object
    *
    * @param pos   Description of the Parameter
    * @param node  Description of the Parameter
    * @return      The indexOf value
    */
   protected int getIndexOf(Position[] pos, XMLNode node)
   {
      for (int i = 0; i < pos.length; i++)
      {
         if (pos[i] == node)
         {
            return i;
         }
      }
      return -1;
   }

   /**
    * Gets the nextEndTagNode attribute of the XMLContentAssistProcessor object
    *
    * @param doc     Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        The nextEndTagNode value
    */
   protected XMLNode getNextEndTagNode(IDocument doc, int offset)
   {
      Position[] pos = this.getPositions(doc);
      for (int i = 0; i < pos.length; i++)
      {
         if (offset <= pos[i].getOffset())
         {
            XMLNode node = (XMLNode) pos[i];
            if (XMLPartitionScanner.XML_END_TAG.equals(node.getType()))
            {
               return node;
            }
            return null;
         }
      }

      return null;
   }

   /**
    * Gets the nodeAt attribute of the XMLContentAssistProcessor object
    *
    * @param doc     Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        The nodeAt value
    */
   protected XMLNode getNodeAt(IDocument doc, int offset)
   {
      Position[] pos = this.getPositions(doc);

      for (int i = 0; i < pos.length; i++)
      {
         if (offset >= pos[i].getOffset() && offset <= pos[i].getOffset() + pos[i].getLength())
         {
            return (XMLNode) pos[i];
         }
      }

      return null;
   }

   /**
    * Gets the positionCategory attribute of the XMLContentAssistProcessor object
    *
    * @return   The positionCategory value
    */
   protected String getPositionCategory()
   {
      return XMLDocumentPartitioner.CONTENT_TYPES_CATEGORY;
   }

   /**
    * Gets the positions attribute of the XMLContentAssistProcessor object
    *
    * @param doc  Description of the Parameter
    * @return     The positions value
    */
   protected Position[] getPositions(IDocument doc)
   {
      try
      {
         return this.translator.getPositions(doc, this.getPositionCategory());
      }
      catch (BadPositionCategoryException e)
      {
         // Do nothing
      }
      return new Position[0];
   }

   /**
    * Gets the previousStartTagNode attribute of the XMLContentAssistProcessor object
    *
    * @param doc     Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        The previousStartTagNode value
    */
   protected XMLNode getPreviousStartTagNode(IDocument doc, int offset)
   {
      Position[] pos = this.getPositions(doc);
      for (int i = (pos.length - 1); i >= 0; i--)
      {
         if (offset >= (pos[i].getOffset() + pos[i].length))
         {
            XMLNode node = (XMLNode) pos[i];
            if (XMLPartitionScanner.XML_TAG.equals(node.getType()))
            {
               return node;
            }
            return null;
         }
      }

      return null;
   }

   /**
    * Gets the reconcilierHolder attribute of the XMLContentAssistProcessor object
    *
    * @return   The reconcilierHolder value
    */
   protected IReconcilierHolder getReconcilierHolder()
   {
      return this.holder;
   }

   /**
    * Description of the Method
    *
    * @param s  Description of the Parameter
    * @return   Description of the Return Value
    */
   protected String normalizeWhitespaces(String s)
   {
      return s.replace((char) 10, ' ');
   }
}
