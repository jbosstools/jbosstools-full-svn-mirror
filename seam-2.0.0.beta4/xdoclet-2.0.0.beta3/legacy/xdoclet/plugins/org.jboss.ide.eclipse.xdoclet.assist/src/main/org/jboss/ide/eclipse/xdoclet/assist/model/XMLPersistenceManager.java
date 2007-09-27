/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
 * @version   $Revision: 1420 $
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
   public XMLPersistenceManager(Reader reader, boolean validate, InputSource inputSource)
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
            builder.setEntityResolver(new EntityResolver()
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
   public void persistDocument(Writer writer) throws IOException, JDOMException
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
