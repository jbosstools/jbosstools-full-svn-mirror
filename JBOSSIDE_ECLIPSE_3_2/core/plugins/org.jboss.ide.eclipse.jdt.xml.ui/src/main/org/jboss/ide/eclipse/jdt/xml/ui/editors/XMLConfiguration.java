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
package org.jboss.ide.eclipse.jdt.xml.ui.editors;

import org.eclipse.jdt.internal.ui.text.HTMLTextPresenter;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.jdt.ui.editors.AnnotationHover;
import org.jboss.ide.eclipse.jdt.ui.text.TextDoubleClickStrategy;
import org.jboss.ide.eclipse.jdt.ui.text.rules.IdentityTranslator;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TagContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.TextContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.assist.XMLContentAssistProcessor;
import org.jboss.ide.eclipse.jdt.xml.ui.text.AttValueDoubleClickStrategy;
import org.jboss.ide.eclipse.jdt.xml.ui.text.SimpleDoubleClickStrategy;
import org.jboss.ide.eclipse.jdt.xml.ui.text.TagDoubleClickStrategy;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * XML editor configuration.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLConfiguration extends SourceViewerConfiguration
{
   /** Description of the Field */
   protected XMLEditor editor;

   /** Description of the Field */
   protected XMLTextTools xmlTextTools;

   private ITextDoubleClickStrategy dcsAttValue;

   private ITextDoubleClickStrategy dcsDefault;

   private ITextDoubleClickStrategy dcsSimple;

   private ITextDoubleClickStrategy dcsTag;

   /**
    *Constructor for the XMLConfiguration object
    *
    * @param tools  Description of the Parameter
    */
   public XMLConfiguration(XMLTextTools tools)
   {
      this.xmlTextTools = tools;

      dcsDefault = new TextDoubleClickStrategy();
      dcsSimple = new SimpleDoubleClickStrategy();
      dcsTag = new TagDoubleClickStrategy();
      dcsAttValue = new AttValueDoubleClickStrategy();
   }

   /**
    * Gets the annotationHover attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The annotationHover value
    */
   public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer)
   {
      return new AnnotationHover();
   }

   /**
    * Gets the configuredContentTypes attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The configuredContentTypes value
    */
   public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
   {
      return new String[]
      {//
      IDocument.DEFAULT_CONTENT_TYPE, //
            XMLPartitionScanner.XML_ATTRIBUTE, //
            XMLPartitionScanner.XML_CDATA, //
            XMLPartitionScanner.XML_COMMENT, //
            XMLPartitionScanner.XML_DECL, //
            XMLPartitionScanner.XML_EMPTY_TAG, //
            XMLPartitionScanner.XML_END_DECL, //
            XMLPartitionScanner.XML_END_TAG, //
            XMLPartitionScanner.XML_PI, //
            XMLPartitionScanner.XML_START_DECL, //
            XMLPartitionScanner.XML_TAG, //
            XMLPartitionScanner.XML_TEXT, //
            XMLPartitionScanner.DTD_INTERNAL, //
            XMLPartitionScanner.DTD_INTERNAL_PI, //
            XMLPartitionScanner.DTD_INTERNAL_COMMENT, //
            XMLPartitionScanner.DTD_INTERNAL_DECL,//
      };
   }

   /**
    * Gets the contentAssistant attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The contentAssistant value
    */
   public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
   {
      if (this.getEditor() != null)
      {
         ContentAssistant assistant = new ContentAssistant();

         XMLContentAssistProcessor contentAssistForText = this.getTextContentAssistProcessor();
         XMLContentAssistProcessor contentAssistForTags = this.getTagsContentAssistProcessor();

         assistant.setContentAssistProcessor(contentAssistForText, IDocument.DEFAULT_CONTENT_TYPE);
         assistant.setContentAssistProcessor(contentAssistForTags, XMLPartitionScanner.XML_TAG);
         assistant.setContentAssistProcessor(contentAssistForTags, XMLPartitionScanner.XML_END_TAG);
         assistant.setContentAssistProcessor(contentAssistForTags, XMLPartitionScanner.XML_EMPTY_TAG);

         assistant.enableAutoActivation(true);
         assistant.enableAutoInsert(true);

         assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));

         return assistant;
      }
      return null;
   }

   /**
    * Gets the doubleClickStrategy attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @param contentType   Description of the Parameter
    * @return              The doubleClickStrategy value
    */
   public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType)
   {
      if (XMLPartitionScanner.XML_COMMENT.equals(contentType))
      {
         return dcsSimple;
      }

      if (XMLPartitionScanner.XML_PI.equals(contentType))
      {
         return dcsSimple;
      }

      if (XMLPartitionScanner.XML_TAG.equals(contentType))
      {
         return dcsTag;
      }

      if (XMLPartitionScanner.XML_ATTRIBUTE.equals(contentType))
      {
         return dcsAttValue;
      }

      if (XMLPartitionScanner.XML_CDATA.equals(contentType))
      {
         return dcsSimple;
      }

      if (contentType.startsWith(XMLPartitionScanner.DTD_INTERNAL))
      {
         return dcsSimple;
      }

      return dcsDefault;
   }

   /**
    * Gets the editor attribute of the XMLConfiguration object
    *
    * @return   The editor value
    */
   public XMLEditor getEditor()
   {
      return this.editor;
   }

   /**
    * Gets the informationControlCreator attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The informationControlCreator value
    */
   public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer)
   {
      return new IInformationControlCreator()
      {
         public IInformationControl createInformationControl(Shell parent)
         {
            return new DefaultInformationControl(parent, SWT.NONE, new HTMLTextPresenter(true));
         }
      };
   }

   /**
    * Gets the overviewRulerAnnotationHover attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The overviewRulerAnnotationHover value
    */
   public IAnnotationHover getOverviewRulerAnnotationHover(ISourceViewer sourceViewer)
   {
      return new AnnotationHover();
   }

   /**
    * Gets the presentationReconciler attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The presentationReconciler value
    */
   public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer)
   {
      PresentationReconciler reconciler = new PresentationReconciler();

      DefaultDamagerRepairer dr;

      dr = new DefaultDamagerRepairer(xmlTextTools.getXMLTextScanner());
      reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
      reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

      dr = new DefaultDamagerRepairer(xmlTextTools.getDTDTextScanner());
      reconciler.setDamager(dr, XMLPartitionScanner.DTD_INTERNAL);
      reconciler.setRepairer(dr, XMLPartitionScanner.DTD_INTERNAL);

      dr = new DefaultDamagerRepairer(xmlTextTools.getXMLPIScanner());
      reconciler.setDamager(dr, XMLPartitionScanner.XML_PI);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_PI);
      reconciler.setDamager(dr, XMLPartitionScanner.DTD_INTERNAL_PI);
      reconciler.setRepairer(dr, XMLPartitionScanner.DTD_INTERNAL_PI);

      dr = new DefaultDamagerRepairer(xmlTextTools.getXMLCommentScanner());
      reconciler.setDamager(dr, XMLPartitionScanner.XML_COMMENT);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_COMMENT);
      reconciler.setDamager(dr, XMLPartitionScanner.DTD_INTERNAL_COMMENT);
      reconciler.setRepairer(dr, XMLPartitionScanner.DTD_INTERNAL_COMMENT);

      dr = new DefaultDamagerRepairer(xmlTextTools.getXMLDeclScanner());
      reconciler.setDamager(dr, XMLPartitionScanner.XML_START_DECL);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_START_DECL);
      reconciler.setDamager(dr, XMLPartitionScanner.XML_DECL);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_DECL);
      reconciler.setDamager(dr, XMLPartitionScanner.XML_END_DECL);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_END_DECL);
      reconciler.setDamager(dr, XMLPartitionScanner.DTD_INTERNAL_DECL);
      reconciler.setRepairer(dr, XMLPartitionScanner.DTD_INTERNAL_DECL);

      dr = new DefaultDamagerRepairer(xmlTextTools.getXMLTagScanner());
      reconciler.setDamager(dr, XMLPartitionScanner.XML_TAG);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_TAG);
      reconciler.setDamager(dr, XMLPartitionScanner.XML_END_TAG);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_END_TAG);
      reconciler.setDamager(dr, XMLPartitionScanner.XML_EMPTY_TAG);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_EMPTY_TAG);

      //      dr = new DefaultDamagerRepairer(xmlTextTools.getXMLAttributeScanner());
      //      reconciler.setDamager(dr, XMLPartitionScanner.XML_ATTRIBUTE);
      //      reconciler.setRepairer(dr, XMLPartitionScanner.XML_ATTRIBUTE);

      dr = new DefaultDamagerRepairer(xmlTextTools.getXMLCDATAScanner());
      reconciler.setDamager(dr, XMLPartitionScanner.XML_CDATA);
      reconciler.setRepairer(dr, XMLPartitionScanner.XML_CDATA);

      return reconciler;
   }

   /**
    * Sets the editor attribute of the XMLConfiguration object
    *
    * @param editor  The new editor value
    */
   public void setEditor(XMLEditor editor)
   {
      this.editor = editor;
   }

   /**
    * Gets the tagsContentAssistProcessor attribute of the XMLConfiguration object
    *
    * @return   The tagsContentAssistProcessor value
    */
   protected XMLContentAssistProcessor getTagsContentAssistProcessor()
   {
      return new TagContentAssistProcessor(this.getEditor(), this.xmlTextTools.getPreferenceStore(),
            IdentityTranslator.INSTANCE);
   }

   /**
    * Gets the textContentAssistProcessor attribute of the XMLConfiguration object
    *
    * @return   The textContentAssistProcessor value
    */
   protected XMLContentAssistProcessor getTextContentAssistProcessor()
   {
      return new TextContentAssistProcessor(this.getEditor(), this.xmlTextTools.getPreferenceStore(),
            IdentityTranslator.INSTANCE);
   }

   /**
    * Gets the informationPresenterControlCreator attribute of the XMLConfiguration object
    *
    * @param sourceViewer  Description of the Parameter
    * @return              The informationPresenterControlCreator value
    */
   private IInformationControlCreator getInformationPresenterControlCreator(ISourceViewer sourceViewer)
   {
      return new IInformationControlCreator()
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
