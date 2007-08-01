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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TypeChoiceProposal;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TypeChoiceContributor implements ITagContributor
{
   private Map filterTypeChoiceTags = new HashMap();

   private IJavaProject project;

   private Map rootTypeChoiceTags = new HashMap();

   /**
    *Constructor for the TypeChoiceContributor object
    *
    * @param project  Description of the Parameter
    */
   public TypeChoiceContributor(IJavaProject project)
   {
      this.project = project;
   }

   /**
    * Adds a feature to the TypeChoiceTag attribute of the TypeChoiceContributor object
    *
    * @param name    The feature to be added to the TypeChoiceTag attribute
    * @param filter  The feature to be added to the TypeChoiceTag attribute
    * @param root    The feature to be added to the TypeChoiceTag attribute
    */
   public void addTypeChoiceTag(String name, String filter, String root)
   {
      this.filterTypeChoiceTags.put(name, filter);
      this.rootTypeChoiceTags.put(name, root);
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
    * Gets the tagProposals attribute of the TypeChoiceContributor object
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
   public List getTagProposals(IReconcilierHolder holder, IDocument doc, XMLNode node, XMLNode lastOpenTag,
         XMLNode previousStartTag, XMLNode nextEndTag, String outerTag, String start, int translated, int offset)
   {
      // Build completion proposals
      List proposals = new ArrayList();

      if (previousStartTag != null)
      {
         boolean show = false;
         int replOffset = previousStartTag.getOffset() + previousStartTag.getLength();
         int replLength = 0;

         // Adjust replacement length
         if ((nextEndTag != null) && nextEndTag.getName().equals(previousStartTag.getName()))
         {
            replLength = nextEndTag.getOffset() - replOffset;
         }

         // No node
         if (node == null)
         {
            show = true;
         }
         else
         {
            if (node == previousStartTag)
            {
               show = true;
            }
         }
         if (previousStartTag == lastOpenTag)
         {
            if ((node != null) && (offset <= node.getOffset()))
            {
               show = true;
            }
         }
         if (show)
         {
            proposals.addAll(0, this.getTagBodyProposals(previousStartTag, replOffset, replLength));
         }
      }

      //        if ((previousStartTag != null) && (offset >= (previousStartTag.getOffset() + previousStartTag.length))){
      //            int replOffset = previousStartTag.getOffset() + previousStartTag.getLength();
      //            int replLength = 0;
      //            if ((nextEndTag != null) && nextEndTag.getName().equals(previousStartTag.getName())){
      //                if (offset <= nextEndTag.getOffset()) {
      //                    replLength = nextEndTag.getOffset() - replOffset;
      //                    proposals.addAll(0, this.getTagBodyProposals(previousStartTag, replOffset, replLength));
      //                }
      //            }else{
      //                proposals.addAll(0, this.getTagBodyProposals(previousStartTag, replOffset, replLength));
      //            }
      //        }

      return proposals;
   }

   /**
    * Gets the tagBodyProposals attribute of the TypeChoiceContributor object
    *
    * @param prevStartTag       Description of the Parameter
    * @param replacementOffset  Description of the Parameter
    * @param replacementLength  Description of the Parameter
    * @return                   The tagBodyProposals value
    */
   protected List getTagBodyProposals(XMLNode prevStartTag, int replacementOffset, int replacementLength)
   {
      List proposals = new ArrayList();
      String name = prevStartTag.getName();
      String filter = (String) this.filterTypeChoiceTags.get(name);
      String root = (String) this.rootTypeChoiceTags.get(name);
      if ("".equals(root)//$NON-NLS-1$
      )
      {

         root = null;
      }

      if (filter != null)
      {
         int inclusion = TypeChoiceProposal.SHOW_ALL;
         if ("class".equals(filter)//$NON-NLS-1$
         )
         {

            inclusion = TypeChoiceProposal.SHOW_CLASSES;
         }
         if ("interface".equals(filter)//$NON-NLS-1$
         )
         {

            inclusion = TypeChoiceProposal.SHOW_INTERFACES;
         }
         ICompletionProposal proposal = new TypeChoiceProposal(this.project, root, inclusion,
               "", "", replacementOffset, replacementOffset, //$NON-NLS-1$ //$NON-NLS-2$
               replacementLength);
         proposals.add(proposal);
      }

      return proposals;
   }
}
