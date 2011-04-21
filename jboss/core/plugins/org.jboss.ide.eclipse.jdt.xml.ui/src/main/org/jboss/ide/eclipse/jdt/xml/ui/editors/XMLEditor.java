/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.editors;

import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.jboss.ide.eclipse.jdt.ui.editors.I18NTextEditor;
import org.jboss.ide.eclipse.jdt.xml.core.ns.Namespace;
import org.jboss.ide.eclipse.jdt.xml.core.validation.XMLHandler;
import org.jboss.ide.eclipse.jdt.xml.core.validation.XMLMarkerFactory;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIPlugin;
import org.jboss.ide.eclipse.jdt.xml.ui.outline.XMLOutlinePage;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.NodeReconciler;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLEditor extends I18NTextEditor implements ISelectionChangedListener, IReconcilierHolder, IResourceChangeListener
{
   /** Description of the Field */
   protected XMLOutlinePage outline;
   /** Description of the Field */
   protected NodeReconciler reconciler;


   /**Constructor for the XMLEditor object */
   public XMLEditor() {
   	ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
   }


   /**
    * Gets the adapter attribute of the XMLEditor object
    *
    * @param adapter  Description of the Parameter
    * @return         The adapter value
    */
   public Object getAdapter(Class adapter)
   {
      if (adapter.equals(IContentOutlinePage.class))
      {
         if (this.outline == null)
         {
            this.outline = new XMLOutlinePage(this);
            this.outline.addSelectionChangedListener(this);
         }
         return outline;
      }
      return super.getAdapter(adapter);
   }


   /**
    * Gets the outlinePage attribute of the XMLEditor object
    *
    * @return   The outlinePage value
    */
   public XMLOutlinePage getOutlinePage()
   {
      return (XMLOutlinePage) this.getAdapter(IContentOutlinePage.class);
   }


   /**
    * Gets the reconcilier attribute of the XMLEditor object
    *
    * @return   The reconcilier value
    */
   public NodeReconciler getReconcilier()
   {
      return this.reconciler;
   }


   /**
    * Description of the Method
    *
    * @param evt  Description of the Parameter
    */
   public void selectionChanged(SelectionChangedEvent evt)
   {
      XMLNode xel = (XMLNode) ((StructuredSelection) evt.getSelection()).getFirstElement();

      int start = -1;
      int length = -1;

      if (xel != null)
      {
         start = xel.getOffset();
         length = xel.getLength();
      }

      this.selectAndReveal(start, length);
   }


   /**
    * Sets the reconcilier attribute of the XMLEditor object
    *
    * @param reconciler  The new reconcilier value
    */
   public void setReconcilier(NodeReconciler reconciler)
   {
      this.reconciler = reconciler;
   }


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    * @return       Description of the Return Value
    */
   protected boolean affectsTextPresentation(PropertyChangeEvent event)
   {
      return JDTXMLUIPlugin.getDefault().getXMLTextTools().affectsBehavior(event);
   }


   /**
    * Gets the xMLConfiguration attribute of the XMLEditor object
    *
    * @param xmlTextTools  Description of the Parameter
    * @return              The xMLConfiguration value
    */
   protected XMLConfiguration getXMLConfiguration(XMLTextTools xmlTextTools)
   {
      return new XMLConfiguration(xmlTextTools);
   }


   /** Description of the Method */
   protected void initializeEditor()
   {
      super.initializeEditor();

      this.setPreferenceStore(JDTXMLUIPlugin.getDefault().getPreferenceStore());
      XMLConfiguration configuration = this.getXMLConfiguration(JDTXMLUIPlugin.getDefault().getXMLTextTools());
      configuration.setEditor(this);
      this.setSourceViewerConfiguration(configuration);
      this.setDocumentProvider(new XMLDocumentProvider(this));
   }


   /**
    * Description of the Method
    *
    * @param overwrite        Description of the Parameter
    * @param progressMonitor  Description of the Parameter
    */
   protected void performSave(boolean overwrite, IProgressMonitor progressMonitor)
   {
      super.performSave(overwrite, progressMonitor);
      this.validateXML();
   }


   /**
    * Description of the Method
    *
    * @param progressMonitor  Description of the Parameter
    */
   protected void performSaveAs(IProgressMonitor progressMonitor)
   {
      super.performSaveAs(progressMonitor);
      this.validateXML();
   }


   /** Description of the Method */
   protected void validateXML()
   {
      // Look if validation is possible
      Namespace dtd = this.getReconcilier().getDTDGrammar();
      Map namespaces = this.getReconcilier().getNamespaces();
      boolean hasDTD = (dtd != null);
      boolean hasNS = (namespaces != null) && (!namespaces.keySet().isEmpty());

      IEditorInput input = this.getEditorInput();
      if (input instanceof IFileEditorInput)
      {
         IFileEditorInput fileInput = (IFileEditorInput) input;
         IFile file = fileInput.getFile();
         XMLHandler handler = new XMLHandler(file);
         try
         {
            XMLMarkerFactory.deleteMarkers(file);
            // Force the Xerces Factory
            //SAXParserFactory factory = new SAXParserFactoryImpl();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(hasDTD || hasNS);
            factory.setNamespaceAware(hasNS);
            SAXParser parser = factory.newSAXParser();
            if (hasNS)
            {
               parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");//$NON-NLS-1$ //$NON-NLS-2$
            }
            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(handler);
            reader.setEntityResolver(handler);
            InputSource is = new InputSource(file.getContents());
            reader.parse(is);
         }
         catch (Exception e)
         {
            // Do nothing
         }
      }
   }
   
   /**
    * Respond to resource change events on the file represented by this editor.
    */
	public void resourceChanged(IResourceChangeEvent event) {
		IResource resource = event.getResource();
		if (resource != null)
		{
			IResource editorResource = (IResource) getEditorInput().getAdapter(IResource.class);
			
			System.out.println("Editor Resource=" +editorResource);
			if (resource.equals(editorResource))
			{
				setInput(getEditorInput());
			}
		}
	}
}
