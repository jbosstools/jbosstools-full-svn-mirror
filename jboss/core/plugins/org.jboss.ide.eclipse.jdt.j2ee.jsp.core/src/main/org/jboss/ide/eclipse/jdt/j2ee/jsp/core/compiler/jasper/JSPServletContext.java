/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import org.apache.jasper.servlet.JspCServletContext;

/**
 * Extends the JspCServletContext to allow an external Object to
 * provide the JSP content. This is useful when the content is not
 * in sync with the file content.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPServletContext extends JspCServletContext
{

   /** Map of the providers */
   private Map providers = new Hashtable();


   /**
    * Default constructor
    *
    * @param aLogWriter        Description of the Parameter
    * @param aResourceBaseURL  Description of the Parameter
    */
   public JSPServletContext(PrintWriter aLogWriter, URL aResourceBaseURL)
   {
      super(aLogWriter, aResourceBaseURL);
   }


   /**
    * Add an alternate conetnt provider for the given path.
    *
    *
    * @param path
    * @param provider
    */
   public void addContentProvider(String path, IJSPContentProvider provider)
   {
      this.providers.put(path, provider);
   }


   /**
    * Override to select an alternate content provider if there is one.
    *
    * @param path  Description of the Parameter
    * @return      The resourceAsStream value
    */
   public InputStream getResourceAsStream(String path)
   {
      IJSPContentProvider provider = (IJSPContentProvider) this.providers.get(path);
      if (provider != null)
      {
         ByteArrayInputStream bais = new ByteArrayInputStream(provider.getContent());
         return bais;
      }
      return super.getResourceAsStream(path);
   }


   /**
    * Remove the alternate content provider
    *
    *
    * @param path
    */
   public void removeContentProvider(String path)
   {
      this.providers.remove(path);
   }


   /**
    * Interface implemented by an alternate content provider.
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   public static interface IJSPContentProvider
   {

      /**
       * Return the content of the JSP as a byte array.
       *
       * @return   The content value
       */
      public byte[] getContent();
   }
}
