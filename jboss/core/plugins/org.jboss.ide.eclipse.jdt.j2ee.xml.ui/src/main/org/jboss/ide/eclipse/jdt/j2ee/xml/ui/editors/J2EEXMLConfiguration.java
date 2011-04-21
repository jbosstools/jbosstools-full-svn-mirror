/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.xml.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.ide.eclipse.jdt.ui.text.rules.IdentityTranslator;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TagContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TextContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.XMLContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.TypeChoiceContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLConfiguration;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLTextTools;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class J2EEXMLConfiguration extends XMLConfiguration
{
   /**
    *Constructor for the J2EEXMLConfiguration object
    *
    * @param tools  Description of the Parameter
    */
   public J2EEXMLConfiguration(XMLTextTools tools)
   {
      super(tools);
   }


   /**
    * Gets the tagsContentAssistProcessor attribute of the J2EEXMLConfiguration object
    *
    * @return   The tagsContentAssistProcessor value
    */
   protected XMLContentAssistProcessor getTagsContentAssistProcessor()
   {
      XMLContentAssistProcessor processor = new TagContentAssistProcessor(this.getEditor(), this.xmlTextTools.getPreferenceStore(),
         IdentityTranslator.INSTANCE);

      if (this.getEditor() != null)
      {
         IFile file = null;
         IProject project = null;
         IJavaProject jProject = null;

         IEditorInput input = this.getEditor().getEditorInput();
         if (input instanceof IFileEditorInput)
         {
            IFileEditorInput fileInput = (IFileEditorInput) input;
            file = fileInput.getFile();
            project = file.getProject();
            jProject = JavaCore.create(project);
         }

         processor.addTagContributor(this.getTypeChoiceContributor(jProject));
      }

      return processor;
   }


   /**
    * Gets the textContentAssistProcessor attribute of the J2EEXMLConfiguration object
    *
    * @return   The textContentAssistProcessor value
    */
   protected XMLContentAssistProcessor getTextContentAssistProcessor()
   {
      XMLContentAssistProcessor processor = new TextContentAssistProcessor(this.getEditor(), this.xmlTextTools.getPreferenceStore(),
         IdentityTranslator.INSTANCE);

      if (this.getEditor() != null)
      {
         IFile file = null;
         IProject project = null;
         IJavaProject jProject = null;

         IEditorInput input = this.getEditor().getEditorInput();
         if (input instanceof IFileEditorInput)
         {
            IFileEditorInput fileInput = (IFileEditorInput) input;
            file = fileInput.getFile();
            project = file.getProject();
            jProject = JavaCore.create(project);
         }

         processor.addTagContributor(this.getTypeChoiceContributor(jProject));
      }

      return processor;
   }


   /**
    * Gets the typeChoiceContributor attribute of the J2EEXMLConfiguration object
    *
    * @param jProject  Description of the Parameter
    * @return          The typeChoiceContributor value
    */
   protected TypeChoiceContributor getTypeChoiceContributor(IJavaProject jProject)
   {
      TypeChoiceContributor contributor = new TypeChoiceContributor(jProject);
      this.populateTypeChoiceContributor(contributor);
      return contributor;
   }


   /**
    * Description of the Method
    *
    * @param contributor  Description of the Parameter
    */
   protected abstract void populateTypeChoiceContributor(TypeChoiceContributor contributor);
}
