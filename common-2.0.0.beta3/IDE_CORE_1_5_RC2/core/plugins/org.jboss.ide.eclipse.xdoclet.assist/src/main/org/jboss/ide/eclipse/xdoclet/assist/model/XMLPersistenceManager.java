/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class XMLPersistenceManager
{
   /** Description of the Field */
   public Reader reader;
   /** Description of the Field */
   protected Document document;
   /** Description of the Field */
   protected InputSource inputSource;
   /** Description of the Field */
   protected XMLOutputter outputter = new XMLOutputter();
   /** Description of the Field */
   protected boolean validate;


   /**
    * Constructor for PersistenceManager.
    *
    * @param reader       Description of the Parameter
    * @param validate     Description of the Parameter
    * @param inputSource  Description of the Parameter
    */
   public XMLPersistenceManager(
         Reader reader,
         boolean validate,
         InputSource inputSource)
   {
      if (reader == null)
      {
         throw new IllegalArgumentException();
      }
      this.inputSource = inputSource;
      this.reader = reader;
      this.validate = validate;
      //
      outputter.setNewlines(true);
      outputter.setIndent(true);
      outputter.setOmitDeclaration(false);
      outputter.setOmitEncoding(false);
      // outputter.setIndent()
   }


   /**
    * Returns the document.
    *
    * @return                   Document
    * @exception IOException    Description of the Exception
    * @exception JDOMException  Description of the Exception
    */
   public Document getDocument() throws IOException, JDOMException
   {
      if (document == null)
      {
         SAXBuilder builder = new SAXBuilder();
         builder.setValidation(validate);

         if (inputSource != null && validate)
         {
            // int b;
            builder.setEntityResolver(
               new EntityResolver()
               {
                  public InputSource resolveEntity(String s, String s1)
                  {
                     return inputSource;
                  }
               });
         }
         document = builder.build(reader);

         reader.close();
      }
      return document;
   }


   /**
    * Returns the inputSource.
    *
    * @return   InputSource
    */
   public InputSource getInputSource()
   {
      return inputSource;
   }


   /**
    * Returns the validate.
    *
    * @return   boolean
    */
   public boolean isValidate()
   {
      return validate;
   }

   //	public PersistenceManager(URL url, boolean validate)
   //		throws FileNotFoundException {
   //		if (url == null)
   //			throw new IllegalArgumentException();
   //		this.reader = new FileReader(url.getPath());
   //		this.validate = validate;
   //	}

   /**
    * Description of the Method
    *
    * @param writer             Description of the Parameter
    * @exception IOException    Description of the Exception
    * @exception JDOMException  Description of the Exception
    */
   public void persistDocument(Writer writer)
          throws IOException, JDOMException
   {
      outputter.output(getDocument(), writer);
      writer.close();
   }


   /** Description of the Method */
   public void toConsole()
   {
      try
      {
         outputter.output(getDocument(), System.out);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

}
