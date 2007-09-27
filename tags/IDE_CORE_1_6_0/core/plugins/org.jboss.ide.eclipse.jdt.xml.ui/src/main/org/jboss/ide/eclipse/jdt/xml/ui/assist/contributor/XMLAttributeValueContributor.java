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
public class XMLAttributeValueContributor implements IAttributeValueContributor
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
    * Gets the attributeValueProposals attribute of the XMLAttributeValueContributor object
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
   public List getAttributeValueProposals(IReconcilierHolder holder, IDocument doc, XMLNode node, XMLNode attribute,
         char quote, String start, int offset)
   {
      // Get possible values
      List words = this.getAttributeValues(holder, doc, node, attribute);

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < words.size(); i++)
      {
         String text = (String) words.get(i);
         if (text.startsWith(start))
         {
            int replacementStart = offset - start.length();
            String replacementString = text;

            if (quote == '\'' || quote == '"')
            {
               if (!endsWithQuote(doc, quote, start, replacementStart))
               { // only place a quote if there already are one.
                  replacementString = text + quote;
               }

               ICompletionProposal proposal = new CompletionProposal(replacementString, replacementStart, start
                     .length(), replacementString.length(), JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_VALUE), text,
                     null, null);
               proposals.add(proposal);
            }
            else
            {
               ICompletionProposal proposal = new CompletionProposal('"' + text + '"', replacementStart,
                     start.length(), text.length() + 2, JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_VALUE), text,
                     null, null);
               proposals.add(proposal);
            }
         }
      }

      return proposals;
   }

   /**
    * @param doc
    * @param quote
    * @param text
    * @param replacementStart
    * @param replacementString
    * @return
    */
   private boolean endsWithQuote(IDocument doc, char quote, String text, int replacementStart)
   {
      try
      {
         if (doc.getLength() > (replacementStart + text.length() + 1))
         {
            if (quote == (doc.get(replacementStart + text.length(), 1).charAt(0)))
            {
               return true;
            }
         }
      }
      catch (BadLocationException bl)
      {
         // should not happen as we check for the length just before!
         bl.printStackTrace();
      }
      return false;
   }

   /**
    * Gets the attributeValues attribute of the XMLAttributeValueContributor object
    *
    * @param h          Description of the Parameter
    * @param doc        Description of the Parameter
    * @param node       Description of the Parameter
    * @param attribute  Description of the Parameter
    * @return           The attributeValues value
    */
   protected List getAttributeValues(IReconcilierHolder h, IDocument doc, XMLNode node, XMLNode attribute)
   {
      List words = new ArrayList();

      Namespace dtd = h.getReconcilier().getDTDGrammar();
      Map namespaces = h.getReconcilier().getNamespaces();

      // Add the proposals from DTD
      if (dtd != null)
      {
         List values = dtd.getAttributeValues(node.getName(), attribute.getName());
         if (values != null)
         {
            words.addAll(values);
         }
      }

      // Add the proposals from namespaces
      if (namespaces != null)
      {
         for (Iterator it = namespaces.keySet().iterator(); it.hasNext();)
         {
            String key = (String) it.next();
            Namespace ns = (Namespace) namespaces.get(key);
            List values = ns.getAttributeValues(node.getName(), attribute.getName());
            if (values != null)
            {
               words.addAll(values);
            }
         }
      }

      // Sort the result
      Collections.sort(words);

      return words;
   }
}
