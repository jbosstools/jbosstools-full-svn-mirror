/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.text.HTMLTextPresenter;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.JSPDirectiveCompletionProcessor;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.JSPScripletCompletionProcessor;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.contributor.JSPTagContributor;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.contributor.JSPTagLibContributor;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.JSPDocumentPartitioner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.JSPPartitionScanner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.JSPScriptScanner;
import org.jboss.ide.eclipse.jdt.ui.editors.AnnotationHover;
import org.jboss.ide.eclipse.jdt.ui.text.rules.MultiViewTranslator;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TagContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TextContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.XMLContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLConfiguration;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * JSP editor configuration.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPConfiguration extends SourceViewerConfiguration
{
   private JSPEditor editor;
   private JavaSourceViewerConfiguration javaConfiguration;

   private JSPTextTools jspTextTools;

   private XMLConfiguration xmlConfiguration;
   private final static String[] TYPES = {//
   IDocument.DEFAULT_CONTENT_TYPE, //
   JSPPartitionScanner.JSP_DIRECTIVE, //
   JSPPartitionScanner.JSP_COMMENT, //
   JSPPartitionScanner.JSP_DECLARATION, //
   JSPPartitionScanner.JSP_SCRIPLET, //
   JSPPartitionScanner.JSP_EXPRESSION,//
   };


   /**
    *Constructor for the JSPConfiguration object
    *
    * @param jspTools  Description of the Parameter
    * @param editor    Description of the Parameter
    */
   public JSPConfiguration(JSPTextTools jspTools, JSPEditor editor)
   {
      this.editor = editor;
      jspTextTools = jspTools;

      xmlConfiguration = new XMLConfiguration(jspTextTools.getXMLTextTools());
      javaConfiguration = new JavaSourceViewerConfiguration(jspTextTools.getJavaTextTools(), editor);
   }


   /**
    * Gets the annotationHover attribute of the JSPConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The annotationHover value
    */
   public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer)
   {
      return new AnnotationHover();
   }


   /**
    * Gets the configuredContentTypes attribute of the JSPConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The configuredContentTypes value
    */
   public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
   {
      String[] xmlTypes = xmlConfiguration.getConfiguredContentTypes(sourceViewer);
      String[] javaTypes = javaConfiguration.getConfiguredContentTypes(sourceViewer);

      int length = TYPES.length + xmlTypes.length + javaTypes.length;
      String[] types = new String[length];

      int offset = 0;
      System.arraycopy(TYPES, 0, types, offset, TYPES.length);
      offset += TYPES.length;
      System.arraycopy(xmlTypes, 0, types, offset, xmlTypes.length);
      offset += xmlTypes.length;
      System.arraycopy(javaTypes, 0, types, offset, javaTypes.length);

      return types;
   }


   /**
    * Gets the contentAssistant attribute of the JSPConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The contentAssistant value
    */
   public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
   {
      if (this.getEditor() != null)
      {
         ContentAssistant assistant = new ContentAssistant();

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

         XMLContentAssistProcessor contentAssistForText = new TextContentAssistProcessor(this.getEditor(), this.jspTextTools.getPreferenceStore(),
            MultiViewTranslator.INSTANCE);
         XMLContentAssistProcessor contentAssistForTags = new TagContentAssistProcessor(this.getEditor(), this.jspTextTools.getPreferenceStore(),
            MultiViewTranslator.INSTANCE);

         if (jProject != null)
         {
            JSPTagContributor tagContributor = new JSPTagContributor(jProject);
            JSPTagLibContributor taglibContributor = new JSPTagLibContributor(file, jProject);

            // Add custom contributors
            contentAssistForText.addTagContributor(tagContributor);
            contentAssistForText.addTagContributor(taglibContributor);
            contentAssistForText.addAttributeContributor(tagContributor);
            contentAssistForText.addAttributeContributor(taglibContributor);
            contentAssistForText.addAttributeValueContributor(tagContributor);
            contentAssistForText.addAttributeValueContributor(taglibContributor);

            // Add custom contributors
            contentAssistForTags.addTagContributor(tagContributor);
            contentAssistForTags.addTagContributor(taglibContributor);
            contentAssistForTags.addAttributeContributor(tagContributor);
            contentAssistForTags.addAttributeContributor(taglibContributor);
            contentAssistForTags.addAttributeValueContributor(tagContributor);
            contentAssistForTags.addAttributeValueContributor(taglibContributor);
         }

         // To remove as there is no DEFAULT_CONTENT_TYPE
         assistant.setContentAssistProcessor(contentAssistForText, IDocument.DEFAULT_CONTENT_TYPE);
         assistant.setContentAssistProcessor(contentAssistForTags, XMLPartitionScanner.XML_TAG);
         assistant.setContentAssistProcessor(contentAssistForTags, XMLPartitionScanner.XML_END_TAG);
         assistant.setContentAssistProcessor(contentAssistForTags, XMLPartitionScanner.XML_EMPTY_TAG);

         assistant.setContentAssistProcessor(contentAssistForText, JSPDocumentPartitioner.JSP_TEMPLATE_DATA);

         if (file != null)
         {
            JSPScripletCompletionProcessor contentAssistForJava = new JSPScripletCompletionProcessor(file);
            assistant.setContentAssistProcessor(contentAssistForJava, JSPDocumentPartitioner.JSP_SCRIPLET_CODE);
            assistant.setContentAssistProcessor(contentAssistForJava, JSPPartitionScanner.JSP_DECLARATION);
            assistant.setContentAssistProcessor(contentAssistForJava, JSPPartitionScanner.JSP_EXPRESSION);
            assistant.setContentAssistProcessor(contentAssistForJava, JSPPartitionScanner.JSP_SCRIPLET);

            JSPDirectiveCompletionProcessor contentAssistForDirective = new JSPDirectiveCompletionProcessor(file, jProject);
            assistant.setContentAssistProcessor(contentAssistForDirective, JSPPartitionScanner.JSP_DIRECTIVE);
         }

         assistant.enableAutoActivation(true);
         assistant.enableAutoInsert(true);

         return assistant;
      }
      return null;
   }


   /**
    * Gets the editor attribute of the JSPConfiguration object
    *
    * @return   The editor value
    */
   public JSPEditor getEditor()
   {
      return this.editor;
   }


   /**
    * Gets the informationControlCreator attribute of the JSPConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The informationControlCreator value
    */
   public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer)
   {
      return
         new IInformationControlCreator()
         {
            public IInformationControl createInformationControl(Shell parent)
            {
               return new DefaultInformationControl(parent, SWT.NONE, new HTMLTextPresenter(true));
            }
         };
   }


   /**
    * Gets the overviewRulerAnnotationHover attribute of the JSPConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The overviewRulerAnnotationHover value
    */
   public IAnnotationHover getOverviewRulerAnnotationHover(ISourceViewer sourceViewer)
   {
      return new AnnotationHover();
   }


   /**
    * Gets the presentationReconciler attribute of the JSPConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The presentationReconciler value
    */
   public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer)
   {
      PresentationReconciler reconciler = new PresentationReconciler();

      // configure JSP syntax presentation
      DefaultDamagerRepairer dr;

      dr = new DefaultDamagerRepairer(jspTextTools.getJSPTextScanner());
      reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
      reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

      dr = new DefaultDamagerRepairer(jspTextTools.getJSPCommentScanner());
      reconciler.setDamager(dr, JSPPartitionScanner.JSP_COMMENT);
      reconciler.setRepairer(dr, JSPPartitionScanner.JSP_COMMENT);

      dr = new DefaultDamagerRepairer(jspTextTools.getJSPDirectiveScanner());
      reconciler.setDamager(dr, JSPPartitionScanner.JSP_DIRECTIVE);
      reconciler.setRepairer(dr, JSPPartitionScanner.JSP_DIRECTIVE);

      dr = new DefaultDamagerRepairer(jspTextTools.getJSPBracketScanner());
      reconciler.setDamager(dr, JSPScriptScanner.JSP_BRACKET);
      reconciler.setRepairer(dr, JSPScriptScanner.JSP_BRACKET);

      // configure embedded languages

      // xml partitions
      configureEmbeddedPresentationReconciler(reconciler, xmlConfiguration.getPresentationReconciler(sourceViewer), //
      xmlConfiguration.getConfiguredContentTypes(sourceViewer), JSPDocumentPartitioner.JSP_TEMPLATE_DATA);

      // java partitions
      configureEmbeddedPresentationReconciler(reconciler, javaConfiguration.getPresentationReconciler(sourceViewer), //
      javaConfiguration.getConfiguredContentTypes(sourceViewer), JSPDocumentPartitioner.JSP_SCRIPLET_CODE);

      return reconciler;
   }


   /**
    * Description of the Method
    *
    * @param reconciler   Description of the Parameter
    * @param embedded     Description of the Parameter
    * @param types        Description of the Parameter
    * @param defaultType  Description of the Parameter
    */
   private void configureEmbeddedPresentationReconciler(PresentationReconciler reconciler, IPresentationReconciler embedded, String[] types, String defaultType)
   {
      for (int i = 0; i < types.length; i++)
      {
         String type = types[i];

         IPresentationDamager damager = embedded.getDamager(type);
         IPresentationRepairer repairer = embedded.getRepairer(type);

         if (type == IDocument.DEFAULT_CONTENT_TYPE)
         {
            type = defaultType;
         }

         reconciler.setDamager(damager, type);
         reconciler.setRepairer(repairer, type);
      }
   }


   /**
    * Gets the informationPresenterControlCreator attribute of the JSPConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The informationPresenterControlCreator value
    */
   private IInformationControlCreator getInformationPresenterControlCreator(ISourceViewer sourceViewer)
   {
      return
         new IInformationControlCreator()
         {
            public IInformationControl createInformationControl(Shell parent)
            {
               int shellStyle = SWT.RESIZE;
               int style = SWT.V_SCROLL | SWT.H_SCROLL;
               return new DefaultInformationControl(parent, shellStyle, style, new HTMLTextPresenter(false));
            }
         };
   }
}
