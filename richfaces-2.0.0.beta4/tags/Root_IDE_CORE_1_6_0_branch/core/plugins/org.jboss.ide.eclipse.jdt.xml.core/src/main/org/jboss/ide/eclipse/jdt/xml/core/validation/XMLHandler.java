/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.core.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.jboss.ide.eclipse.jdt.xml.core.ns.LocalCache;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLHandler extends DefaultHandler
{
   private Attributes attributes;
   private String element;

   private IFile file;


   /**
    *Constructor for the XMLHandler object
    *
    * @param file  Description of the Parameter
    */
   public XMLHandler(IFile file)
   {
      this.file = file;
   }


   /**
    * Description of the Method
    *
    * @param exception         Description of the Parameter
    * @exception SAXException  Description of the Exception
    */
   public void error(SAXParseException exception)
      throws SAXException
   {
      String message = exception.getMessage();
      int line = exception.getLineNumber();
      int column = exception.getColumnNumber();

      IMarker marker;
      try
      {
         marker = XMLMarkerFactory.createMarker(this.file);
         marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
         marker.setAttribute(IMarker.MESSAGE, message);
         if (line != -1)
         {
            marker.setAttribute(IMarker.LINE_NUMBER, line);
         }
      }
      catch (CoreException e)
      {
         // Do nothing
      }
   }


   /**
    * Description of the Method
    *
    * @param exception         Description of the Parameter
    * @exception SAXException  Description of the Exception
    */
   public void fatalError(SAXParseException exception)
      throws SAXException
   {
      String message = exception.getLocalizedMessage();
      int line = exception.getLineNumber();
      int column = exception.getColumnNumber();

      IMarker marker;
      try
      {
         marker = XMLMarkerFactory.createMarker(this.file);
         marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
         marker.setAttribute(IMarker.MESSAGE, message);
         if (line != -1)
         {
            marker.setAttribute(IMarker.LINE_NUMBER, line);
         }
      }
      catch (CoreException e)
      {
         // Do nothing
      }
   }


   /**
    * Description of the Method
    *
    * @param publicId          Description of the Parameter
    * @param systemId          Description of the Parameter
    * @return                  Description of the Return Value
    * @exception SAXException  Description of the Exception
    */
   public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException
   {
      // System.out.println(getClass().getName() + " : resolving " + systemId);
      String location = LocalCache.getInstance().getLocation(systemId);
      InputSource is = null;
      if (location != null)
      {
         is = new InputSource(location);
      }
      return is;
   }


   /**
    * Description of the Method
    *
    * @param exception         Description of the Parameter
    * @exception SAXException  Description of the Exception
    */
   public void warning(SAXParseException exception)
      throws SAXException
   {
   }

}

