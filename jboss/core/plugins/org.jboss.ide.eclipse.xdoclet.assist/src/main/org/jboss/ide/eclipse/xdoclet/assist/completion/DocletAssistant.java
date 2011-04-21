/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.completion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProposal;
import org.jboss.ide.eclipse.xdoclet.assist.IXDocletConstants;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistPlugin;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletElement;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletTree;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletType;
import org.jboss.ide.eclipse.xdoclet.assist.model.IDocletConstants;
import org.jboss.ide.eclipse.xdoclet.assist.model.PatternStore;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateList;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateTree;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateTreeFormat;
import org.jboss.ide.eclipse.xdoclet.assist.model.VariableStore;
import org.jboss.ide.eclipse.xdoclet.assist.ui.ImageStore;
import org.jboss.ide.eclipse.xdoclet.ui.IXDocletUIConstants;
import org.jboss.ide.eclipse.xdoclet.ui.XDocletUIImages;

/**
 * A region is checked for a valid doclet-structure and if possible a completions are computed.
 * The class is not statically used as more then one source for the xmldata is possible
 *
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   15 mai 2003
 * @todo      Javadoc to complete
 */
public class DocletAssistant
{
   /** Description of the Field */
   protected DocletTree internalDocletTree = null;
   /** Description of the Field */
   protected TemplateList internalTemplateList = null;
   /** Description of the Field */
   protected VariableStore internalVariableStore = null;
   /** Description of the Field */
   protected Perl5Matcher matcher = new Perl5Matcher();
   /** Description of the Field */
   protected TemplateTreeFormat templateTreeFormat;

   /** Description of the Field */
   DocletParser docletParser = new DocletParser();
   /** Description of the Field */
   boolean equalsAfterCursor;
   /** Description of the Field */
   boolean quoteAfterCursor;//$NON-NLS-1$
   /** Description of the Field */
   private final static String DEFAULT_NAMESPACE_SEPARATOR = ".";//$NON-NLS-1$

   /** Description of the Field */
   private final static String EMPTY_STRING = "";//$NON-NLS-1$
   /** Description of the Field */
   private final static String EQUALS_AFTER_CURSOR_PATTERN = "^[\\t\\f\\ ]*=";//$NON-NLS-1$
   /** Description of the Field */
   private final static String NEW_LINE = System.getProperty("line.separator");//$NON-NLS-1$
   // private final static String QUOTE_AFTER_CURSOR_PATTERN = "^[\\s]{1}\"";//$NON-NLS-1$


   /**Constructor for the DocletAssistant object */
   public DocletAssistant()
   {
      templateTreeFormat = new TemplateTreeFormat();
      templateTreeFormat.setIndentFirstLine(false);
   }


   /**
    * Returns the docletTree.
    *
    * @return   DocletTree
    */
   public DocletTree getDocletTree()
   {
      if (internalDocletTree != null)
      {
         return internalDocletTree;
      }
      else if (XDocletAssistPlugin.getDefault() != null)
      {
         return XDocletAssistPlugin.getDefault().getDocletTree();
      }
      return null;
   }


   /**
    * Returns the proposals to autocomplete elements of a doclet-structure.
    * The attempt for an autocompletion is made if within the section a namespace
    * like "@ejb:" exists. The autocompletion this method provides is for elements
    * which are in their hierarchy lower than the namespace. The first namespace left
    * of the cursorOffset is taken as the starting-point to compute the completion.
    *
    * @param sourceCode              Description of the Parameter
    * @param offSet                  Description of the Parameter
    * @param member                  Description of the Parameter
    * @param variables               Description of the Parameter
    * @return                        Proposal[] The possible autocompletions. If the doclet-structure before the word
    * that is to be completed is invalid null is returned. If the structure is valid and there
    * is no autocompletion for the word an empty array is returned.
    * @exception JavaModelException  Description of the Exception
    */
   public JavaCompletionProposal[] getProposals(String sourceCode, int offSet, IMember member, HashMap variables)
          throws JavaModelException
   {

      // Check beginning of javadoc environemt
      int start = sourceCode.lastIndexOf("/**", offSet);//$NON-NLS-1$
      if (start == -1)
      {
         return null;
      }

      //		System.out.println("Section:" + section);

      // Check the part right of the cursorposition whether the next non-white-space
      // character in the same line is an equals. This is important whether to perhaps insert
      // an equalsign for the replacement later on. Cut the section right of the
      // cursor afterwards
      quoteAfterCursor = false;
      equalsAfterCursor = false;

      // Find first * before cursorposition for indent of templates
      int star = sourceCode.lastIndexOf('*', offSet);
      StringBuffer indent = new StringBuffer("");//$NON-NLS-1$
      if (star != -1)
      {
         int lastPosBeforeStar = sourceCode.lastIndexOf(NEW_LINE, star - 1);
         if (lastPosBeforeStar != -1)
         {
            for (int i = lastPosBeforeStar; i <= star; i++)
            {
               System.out.println((int) sourceCode.charAt(i) + " :" + sourceCode.charAt(i) + ":");//$NON-NLS-1$ //$NON-NLS-2$
            }
            for (int i = lastPosBeforeStar + NEW_LINE.length(); i < star; i++)
            {
               indent.append(sourceCode.charAt(i));
            }
         }
      }

      equalsAfterCursor = matcher.contains(sourceCode.substring(offSet), PatternStore.getPattern(EQUALS_AFTER_CURSOR_PATTERN));
      if (!equalsAfterCursor
            && sourceCode.length() > offSet
            && sourceCode.charAt(offSet) == '"')
      {
         quoteAfterCursor = true;
      }

      //		System.out.println("Equals After Cursor:" + equalsAfterCursor + ":");
      //		System.out.println("Quote After Cursor:" + quoteAfterCursor + ":");
      sourceCode = sourceCode.substring(start, offSet);
      // System.out.println("Trimmed section:" + sourceCode + ":");
      DocletStructure docletStructure = docletParser.getProposals(getDocletTree(), sourceCode, member);
      System.out.println("DocletStructure: " + docletStructure);//$NON-NLS-1$
      if (docletStructure != null)
      {
         return getCompletionInternal(docletStructure, member, variables, indent.toString(), offSet);
      }
      return null;
   }


   /**
    * Returns the templateList.
    *
    * @return   TemplateList
    */
   public TemplateList getTemplateList()
   {
      if (internalTemplateList != null)
      {
         return internalTemplateList;
      }
      else if (XDocletAssistPlugin.getDefault() != null)
      {
         return XDocletAssistPlugin.getDefault().getTemplateList();
      }
      return null;
   }


   /**
    * Returns the variableStore.
    *
    * @return   VariableStore
    */
   public VariableStore getVariableStore()
   {
      if (internalVariableStore != null)
      {
         return internalVariableStore;
      }
      else if (XDocletAssistPlugin.getDefault() != null)
      {
         return XDocletAssistPlugin.getDefault().getVariableStore();
      }
      return null;
   }


   /**
    * Returns the equalsAfterCursor.
    *
    * @return   boolean
    */
   public boolean isEqualsAfterCursor()
   {
      return equalsAfterCursor;
   }


   /**
    * Returns the quoteAfterCursor.
    *
    * @return   boolean
    */
   public boolean isQuoteAfterCursor()
   {
      return quoteAfterCursor;
   }


   /**
    * Sets the internalDocletTree.
    *
    * @param internalDocletTree  The internalDocletTree to set
    */
   public void setInternalDocletTree(DocletTree internalDocletTree)
   {
      this.internalDocletTree = internalDocletTree;
   }


   /**
    * Sets the templateList.
    *
    * @param internalTemplateList  The new internalTemplateList value
    */
   public void setInternalTemplateList(TemplateList internalTemplateList)
   {
      this.internalTemplateList = internalTemplateList;
   }


   /**
    * Sets the internalVariableStore.
    *
    * @param internalVariableStore  The internalVariableStore to set
    */
   public void setInternalVariableStore(VariableStore internalVariableStore)
   {
      this.internalVariableStore = internalVariableStore;
   }


   /**
    * Description of the Method
    *
    * @param wordLeftOfCursor  Description of the Parameter
    * @param variables         Description of the Parameter
    * @param indent            Description of the Parameter
    * @param documentOffset    Description of the Parameter
    * @return                  Description of the Return Value
    */
   protected JavaCompletionProposal[] computeTemplates(
         String wordLeftOfCursor,
         HashMap variables,
         String indent,
         int documentOffset)
   {

      TemplateTree[] trees = getTemplateList().getTemplateTrees();
      ArrayList results = new ArrayList();
      int treeIndex = -1;
      for (int i = 0; i < trees.length; i++)
      {
         if (trees[i].getName().startsWith(wordLeftOfCursor))
         {
            treeIndex = i;
            results.add(
                  transformTreeIntoProposal(
                  trees[treeIndex],
                  variables,
                  indent,
                  wordLeftOfCursor,
                  documentOffset));
         }
      }
      return (JavaCompletionProposal[]) results.toArray(
            new JavaCompletionProposal[results.size()]);
   }


   /**
    * Gets the parsedChildren attribute of the DocletAssistant object
    *
    * @param elements         Description of the Parameter
    * @param systemVariables  Description of the Parameter
    * @return                 The parsedChildren value
    */
   protected String[] getParsedChildren(
         DocletElement[] elements,
         Map systemVariables)
   {
      String[] strings = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         strings[i] =
               elements[i].replaceVariables(
               systemVariables,
               getVariableStore());
      }
      return strings;
   }


   /**
    * Description of the Method
    *
    * @param tree              Description of the Parameter
    * @param variables         Description of the Parameter
    * @param indent            Description of the Parameter
    * @param wordLeftOfCursor  Description of the Parameter
    * @param documentOffset    Description of the Parameter
    * @return                  Description of the Return Value
    */
   protected JavaCompletionProposal transformTreeIntoProposal(
         TemplateTree tree,
         HashMap variables,
         String indent,
         String wordLeftOfCursor,
         int documentOffset)
   {
      templateTreeFormat.setTree(tree);
      templateTreeFormat.setVariableStore(getVariableStore());
      templateTreeFormat.setVariables(variables);
      templateTreeFormat.setIndent(indent);
      templateTreeFormat.setIndentFirstLine(false);
      String text = templateTreeFormat.format();
      JavaCompletionProposal proposal =
            new JavaCompletionProposal(
            text,
            documentOffset - wordLeftOfCursor.length() - 1,
            wordLeftOfCursor.length() + 1,
            XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_TEMPLATE),
            tree.getName(),
            IXDocletConstants.PROPOSAL_RELEVANCE);
      return proposal;
   }


   /**
    * Returns for a given valueRange and the word that is to complete the proposals.
    *
    * @param valueRange              all possible completions independent of the word to complete
    * @param wordLeftOfCursor        the word that is to complete
    * @param prefix                  a string that is to be put before the value to build the replacementtext
    * @param postfix                 a string that is to be put after the value to build the replacementtext
    * @param variables               Description of the Parameter
    * @param replacementOffset       Description of the Parameter
    * @param replacementLength       Description of the Parameter
    * @param relativeCursorPosition  Description of the Parameter
    * @param command                 Description of the Parameter
    * @return                        Proposal[] the proposals that fit to the valuerange and the word to complete.
    * If the valueRange is null or empty or the word to complete doesn't fit to the
    * valueRange an empty array is returned.
    */
   private JavaCompletionProposal[] computeProposals(
         DocletElement[] valueRange,
         HashMap variables,
         String wordLeftOfCursor,
         String prefix,
         String postfix,
         int replacementOffset,
         int replacementLength,
         int relativeCursorPosition,
         boolean command)
   {
      if (valueRange == null
            || (valueRange = getSubset(valueRange, variables, wordLeftOfCursor))
            == null)
      {
         return new JavaCompletionProposal[0];
      }
      JavaCompletionProposal[] proposals =
            new JavaCompletionProposal[valueRange.length];
      String replacementText;
      for (int i = 0; i < valueRange.length; i++)
      {
         String variableOrCodename =
               valueRange[i].replaceVariables(variables, getVariableStore());

         replacementText =
               (command
               ? EMPTY_STRING
               + valueRange[i].getNode().getAdditionalAttributes().get(
               IDocletConstants.ATTR_NAMESPACE_SEPARATOR)
               : EMPTY_STRING)
               + prefix
               + variableOrCodename
               + postfix;

         proposals[i] =
               new JavaCompletionProposal(
               replacementText,
               replacementOffset,
               replacementLength,
               ImageStore.getImage(valueRange[i]),
               valueRange[i].getType() == DocletType.VALUE_WITH_VARIABLE
               ? variableOrCodename
               : valueRange[i].getName(),
               IXDocletConstants.PROPOSAL_RELEVANCE);
         proposals[i].setProposalInfo(
               new XDocletProposalInfo(valueRange[i].getHelpText()));
         proposals[i].setCursorPosition(
               replacementText.length() + relativeCursorPosition);
      }

      return proposals;
   }


   /**
    * @param docletStructure
    * @param member
    * @param variables
    * @param indent
    * @param documentOffset       We have to pass this information to create the proposal later
    * on
    * @return                     JavaCompletionProposal[]
    * @throws JavaModelException
    */
   private JavaCompletionProposal[] getCompletionInternal(DocletStructure docletStructure, IMember member, HashMap variables, String indent, int documentOffset)
          throws JavaModelException
   {
      int wordLeftOfCursorLength =
            docletStructure.wordLeftOfCursor == null
            ? 0
            : docletStructure.wordLeftOfCursor.length();

      if (docletStructure.namespace == null)
      {
         JavaCompletionProposal[] templateProposals =
               computeTemplates(
               docletStructure.wordLeftOfCursor,
               variables,
               indent,
               documentOffset);

         JavaCompletionProposal[] namespaceProposals =
               computeProposals(
               getDocletTree().getChildrenElements(member),
               variables,
               docletStructure.wordLeftOfCursor,
               EMPTY_STRING,
               DEFAULT_NAMESPACE_SEPARATOR,
               documentOffset - wordLeftOfCursorLength,
               wordLeftOfCursorLength,
               0,
               false);

         JavaCompletionProposal[] results =
               new JavaCompletionProposal[templateProposals.length + namespaceProposals.length];

         for (int i = 0; i < results.length; i++)
         {
            if (i < templateProposals.length)
            {
               results[i] = templateProposals[i];
            }
            else
            {
               results[i] = namespaceProposals[i - templateProposals.length];
            }
         }

         return results;
      }
      else if (docletStructure.command == null)
      {
         return computeProposals(
               getDocletTree()
               .getNode(new String[]{docletStructure.namespace})
               .getChildrenElements(member),
               variables,
               docletStructure.wordLeftOfCursor,
               EMPTY_STRING,
               " ", //$NON-NLS-1$
         documentOffset - wordLeftOfCursorLength - 1,
               wordLeftOfCursorLength,
               0,
               true);
      }
      else if (docletStructure.lastElementIsAttribute)
      {
         String attribute =
               ((Property) docletStructure.attributes.get(docletStructure.attributes.size() - 1))
               .getName();
         String wordLeftOfCursorTrimmed = null;
         ;
         // boolean quoteBeforeCursor = false;
         if (docletStructure.wordLeftOfCursor != null)
         {
            if (docletStructure.wordLeftOfCursor.charAt(0) == '"')
            {
               wordLeftOfCursorTrimmed = docletStructure.wordLeftOfCursor.substring(1);
               // quoteBeforeCursor = true;
            }
            else
            {
               wordLeftOfCursorTrimmed = docletStructure.wordLeftOfCursor;
            }
         }
         return computeProposals(
               getDocletTree()
               .getNode(
               new String[]{
               docletStructure.namespace,
               docletStructure.command,
               attribute})
               .getChildrenElements(member),
               variables,
               wordLeftOfCursorTrimmed,
               "\"", //$NON-NLS-1$
         quoteAfterCursor ? EMPTY_STRING : "\"", //$NON-NLS-1$
         documentOffset - wordLeftOfCursorLength,
               wordLeftOfCursorLength,
               quoteAfterCursor ? 1 : 0,
               false);
      }
      else
      {
         DocletElement[] attributes =
               getDocletTree()
               .getNode(
               new String[]{
               docletStructure.namespace,
               docletStructure.command})
               .getChildrenElements(member);
         if (attributes == null)
         {
            return new JavaCompletionProposal[0];
         }

         ArrayList attributeList = new ArrayList(Arrays.asList(attributes));
         Property p;
         // int pos;
         for (int i = 0; i < docletStructure.attributes.size(); i++)
         {
            p = (Property) docletStructure.attributes.get(i);
            for (Iterator iter = attributeList.iterator();
                  iter.hasNext();
                  )
            {
               DocletElement docletElement = (DocletElement) iter.next();
               if (docletElement.getName().equals(p.getName()))
               {
                  iter.remove();
               }
            }
         }

         return computeProposals(
               (DocletElement[]) attributeList.toArray(
               new DocletElement[attributeList.size()]),
               variables,
               docletStructure.wordLeftOfCursor,
               EMPTY_STRING,
               equalsAfterCursor ? null : " = \"\"", //$NON-NLS-1$
         documentOffset - wordLeftOfCursorLength,
               wordLeftOfCursorLength,
               equalsAfterCursor ? 0 : -1,
               false);
      }
   }


   /**
    * Convenient Method that is only called by computeProposals. Returns a subset of
    * an array of strings which contains that strings that start with the sequence of
    * the argument text. If there is no non-empty subset null is returned.
    *
    * @param startSequence  the string that should be the beginning of the searched
    * elements of the given array.
    * @param elements       Description of the Parameter
    * @param variables      Description of the Parameter
    * @return               String[] string of the array that matches the startSequence
    */
   private DocletElement[] getSubset(
         DocletElement[] elements,
         HashMap variables,
         String startSequence)
   {
      if (startSequence == null)
      {
         return elements;
      }
      if (elements == null)
      {
         return null;
      }
      String[] stringArray = getParsedChildren(elements, variables);
      ArrayList list = new ArrayList();
      int searchStart = Arrays.binarySearch(stringArray, startSequence);
      // If it's not contained the result is negative but according to the ordered position
      if (searchStart < 0)
      {
         searchStart = -searchStart - 1;
      }
      for (; searchStart < stringArray.length; searchStart++)
      {
         if (stringArray[searchStart].startsWith(startSequence))
         {
            list.add(elements[searchStart]);
         }
      }
      return list.size() > 0
            ? (DocletElement[]) list.toArray(new DocletElement[list.size()])
            : null;
   }
}
