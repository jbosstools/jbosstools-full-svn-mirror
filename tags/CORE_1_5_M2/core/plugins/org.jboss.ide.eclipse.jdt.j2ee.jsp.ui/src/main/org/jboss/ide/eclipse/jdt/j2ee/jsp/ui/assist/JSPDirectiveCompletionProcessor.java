/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.tags.AbstractRepository;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.tags.JSPDirectives;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TypeChoiceProposal;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPDirectiveCompletionProcessor implements IContentAssistProcessor
{
   /** Description of the Field */
   protected IFile file;
   /** Description of the Field */
   protected IJavaProject project;

   private final static int AFTER_ATTRIBUTE = 3;
   private final static int AFTER_ATTRIBUTE_VALUE = 4;
   private final static int ATTRIBUTE = 1;
   private final static int ATTRIBUTE_VALUE = 2;
   private final static int DIRECTIVE = 0;
   private final static int DOUBLE_QUOTE = 5;


   /**
    *Constructor for the JSPDirectiveCompletionProcessor object
    *
    * @param file     Description of the Parameter
    * @param project  Description of the Parameter
    */
   public JSPDirectiveCompletionProcessor(IFile file, IJavaProject project)
   {
      this.file = file;
      this.project = project;
   }


   /**
    * Description of the Method
    *
    * @param viewer  Description of the Parameter
    * @param offset  Description of the Parameter
    * @return        Description of the Return Value
    */
   public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset)
   {
      IDocument document = viewer.getDocument();
      PushBackDocumentReader reader = new PushBackDocumentReader(document, offset);
      AbstractRepository directives = JSPDirectives.getInstance();

      String directiveLine = this.getDirectiveLine(reader);
      if (directiveLine != null)
      {
         String directive = "";//$NON-NLS-1$
         String line = directiveLine.substring("<%@".length());//$NON-NLS-1$
         while ((line.length() > 0) && Character.isWhitespace(line.charAt(0)))
         {
            line = line.substring(1);
         }
         int nextSpace = line.indexOf(' ');
         // Is keyword is present
         if (nextSpace > 0)
         {
            directive = line.substring(0, nextSpace);
            int caretOffset = directiveLine.indexOf(directive) + directive.length();
            int state = this.getStateAt(directiveLine, directive, caretOffset);

            char quote = '\0';
            switch (state)
            {
               case ATTRIBUTE:
                  reader.reset();
                  String attributeStart = this.getAttributeStart(reader);
                  return this.computeAttributes(document, directive, attributeStart, offset);
               case DIRECTIVE:
                  return this.computeDirectives(document, directive, offset);
               case DOUBLE_QUOTE:
                  quote = '"';
               case ATTRIBUTE_VALUE:
                  reader.reset();
                  String attributeValueStart = this.getAttributeValueStart(reader, quote);
                  String attribute = this.getAttributeStart(reader);
                  return this.computeAttributeValues(document, directive, attribute, attributeValueStart, offset, quote);
            }
            return this.computeDirectives(document, directive, offset);
         }
         // Keyword is what has been typed
         directive = line;

         // Fetch all keywords
         return this.computeDirectives(document, directive, offset);
      }

      return null;
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
    * Gets the completionProposalAutoActivationCharacters attribute of the JSPDirectiveCompletionProcessor object
    *
    * @return   The completionProposalAutoActivationCharacters value
    */
   public char[] getCompletionProposalAutoActivationCharacters()
   {
      char[] chs = {'<', '%', '@', ' '};
      return chs;
   }


   /**
    * Gets the contextInformationAutoActivationCharacters attribute of the JSPDirectiveCompletionProcessor object
    *
    * @return   The contextInformationAutoActivationCharacters value
    */
   public char[] getContextInformationAutoActivationCharacters()
   {
      return null;
   }


   /**
    * Gets the contextInformationValidator attribute of the JSPDirectiveCompletionProcessor object
    *
    * @return   The contextInformationValidator value
    */
   public IContextInformationValidator getContextInformationValidator()
   {
      return null;
   }


   /**
    * Gets the errorMessage attribute of the JSPDirectiveCompletionProcessor object
    *
    * @return   The errorMessage value
    */
   public String getErrorMessage()
   {
      return JDTJ2EEJSPUIMessages.getString("JSPDirectiveCompletionProcessor.error.message");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param document   Description of the Parameter
    * @param directive  Description of the Parameter
    * @param attribute  Description of the Parameter
    * @param start      Description of the Parameter
    * @param offset     Description of the Parameter
    * @param quote      Description of the Parameter
    * @return           Description of the Return Value
    */
   protected ICompletionProposal[] computeAttributeValues(IDocument document, String directive, String attribute, String start, int offset, char quote)
   {
      // Get possible values
      AbstractRepository directives = JSPDirectives.getInstance();
      List words = directives.getAttributeValues(directive, attribute);
      String filter = directives.getTypeChoiceAttributeValue(directive, attribute);

      // Build the completion proposals
      List proposals = new ArrayList();

      // Type choice proposal
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

         if (quote == '\'' || quote == '"')
         {
            ICompletionProposal proposal = new TypeChoiceProposal(this.project, inclusion, offset - start.length(), offset - start.length(), start.length());
            proposals.add(proposal);
         }
         else
         {
            ICompletionProposal proposal = new TypeChoiceProposal(this.project, inclusion, "\"", "\"", offset - start.length(), offset - start.length(), //$NON-NLS-1$ //$NON-NLS-2$
            start.length());
            proposals.add(proposal);
         }
      }

      // Values from the files
      for (int i = 0; i < words.size(); i++)
      {
         String text = (String) words.get(i);
         if (text.startsWith(start))
         {
            if (quote == '"')
            {
               ICompletionProposal proposal = new CompletionProposal(text + quote, offset - start.length(), start.length(), text.length() + 1, null, text,
                  null, null);
               proposals.add(proposal);
            }
            else
            {
               ICompletionProposal proposal = new CompletionProposal('"' + text + '"', offset - start.length(), start.length(), text.length() + 2, null,
                  text, null, null);
               proposals.add(proposal);
            }
         }
      }

      return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
   }


   /**
    * Description of the Method
    *
    * @param document   Description of the Parameter
    * @param directive  Description of the Parameter
    * @param start      Description of the Parameter
    * @param offset     Description of the Parameter
    * @return           Description of the Return Value
    */
   protected ICompletionProposal[] computeAttributes(IDocument document, String directive, String start, int offset)
   {
      // Get possible values
      AbstractRepository directives = JSPDirectives.getInstance();
      List words = directives.getAttributes(directive);

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < words.size(); i++)
      {
         String text = (String) words.get(i);
         if (text.startsWith(start))
         {
            ICompletionProposal proposal = new CompletionProposal(text, offset - start.length(), start.length(), text.length(), null, text, null, null);
            proposals.add(proposal);
         }
      }

      return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
   }


   /**
    * Description of the Method
    *
    * @param document   Description of the Parameter
    * @param directive  Description of the Parameter
    * @param offset     Description of the Parameter
    * @return           Description of the Return Value
    */
   protected ICompletionProposal[] computeDirectives(IDocument document, String directive, int offset)
   {
      // Get possible values
      AbstractRepository directives = JSPDirectives.getInstance();
      List words = directives.getTags();

      // Build the completion proposals
      List proposals = new ArrayList();
      for (int i = 0; i < words.size(); i++)
      {
         String text = (String) words.get(i);
         if (text.startsWith(directive))
         {
            ICompletionProposal proposal = new CompletionProposal(text, offset - directive.length(), directive.length(), text.length(), null, text, null,
               null);
            proposals.add(proposal);
         }
      }

      return (ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]);
   }


   /**
    * Gets the attributeStart attribute of the JSPDirectiveCompletionProcessor object
    *
    * @param reader  Description of the Parameter
    * @return        The attributeStart value
    */
   protected String getAttributeStart(PushBackDocumentReader reader)
   {
      StringBuffer buffer = new StringBuffer();
      String line = null;

      boolean found = false;
      while (!found)
      {
         char c = reader.readBackward();
         if (c == PushBackDocumentReader.EOS)
         {
            break;
         }
         if (c == ' ')
         {
            found = true;
            line = buffer.toString();
         }
         else if (c != '=')
         {
            buffer.insert(0, c);
         }
      }

      return line;
   }


   /**
    * Gets the attributeValueStart attribute of the JSPDirectiveCompletionProcessor object
    *
    * @param reader  Description of the Parameter
    * @param quote   Description of the Parameter
    * @return        The attributeValueStart value
    */
   protected String getAttributeValueStart(PushBackDocumentReader reader, char quote)
   {
      StringBuffer buffer = new StringBuffer();
      String line = null;
      char limit = (quote == '\0') ? '=' : quote;

      boolean found = false;
      while (!found)
      {
         char c = reader.readBackward();
         if (c == PushBackDocumentReader.EOS)
         {
            break;
         }
         if (c == limit)
         {
            found = true;
            line = buffer.toString();
         }
         else
         {
            buffer.insert(0, c);
         }
      }

      return line;
   }


   /**
    * Gets the directiveLine attribute of the JSPDirectiveCompletionProcessor object
    *
    * @param reader  Description of the Parameter
    * @return        The directiveLine value
    */
   protected String getDirectiveLine(PushBackDocumentReader reader)
   {
      StringBuffer buffer = new StringBuffer();
      String line = null;

      boolean found = false;
      while (!found)
      {
         char c = reader.readBackward();
         if (c == PushBackDocumentReader.EOS)
         {
            break;
         }
         buffer.insert(0, c);

         if (buffer.toString().startsWith("<%@")//$NON-NLS-1$
         )
         {

            found = true;
            line = buffer.toString();
         }
      }

      return line;
   }


   /**
    * Gets the stateAt attribute of the JSPDirectiveCompletionProcessor object
    *
    * @param directive  Description of the Parameter
    * @param keyword    Description of the Parameter
    * @param offset     Description of the Parameter
    * @return           The stateAt value
    */
   protected int getStateAt(String directive, String keyword, int offset)
   {
      int state = DIRECTIVE;

      for (int i = offset; i < directive.length(); i++)
      {
         char c = directive.charAt(i);
         switch (c)
         {
            case '=':
               if (state == AFTER_ATTRIBUTE || state == ATTRIBUTE)
               {
                  state = ATTRIBUTE_VALUE;
               }
               break;
            case '"':
               if (state == DOUBLE_QUOTE)
               {
                  state = AFTER_ATTRIBUTE_VALUE;
               }
               else
               {
                  state = DOUBLE_QUOTE;
               }
               break;
            default:
               if (Character.isWhitespace(c))
               {
                  switch (state)
                  {
                     case DIRECTIVE:
                        state = ATTRIBUTE;
                        break;
                     case ATTRIBUTE:
                        state = AFTER_ATTRIBUTE;
                        break;
                     case AFTER_ATTRIBUTE_VALUE:
                        state = ATTRIBUTE;
                        break;
                  }
               }
         }
      }

      return state;
   }

}
