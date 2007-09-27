/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.tags;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AbstractRepository
{
   private Map attributeValues = new HashMap();
   private Map tagAttributes = new HashMap();

   private List tags = new ArrayList();
   private Map typeChoiceAttributes = new HashMap();
   private static AbstractRepository instance = new AbstractRepository();


   /**Constructor for the AbstractRepository object */
   protected AbstractRepository()
   {
      this.parse();
   }


   /**
    * Gets the attributeValues attribute of the AbstractRepository object
    *
    * @param tag        Description of the Parameter
    * @param attribute  Description of the Parameter
    * @return           The attributeValues value
    */
   public List getAttributeValues(String tag, String attribute)
   {
      String key = tag + ">" + attribute;//$NON-NLS-1$
      List values = (List) this.attributeValues.get(key);
      if (values == null)
      {
         values = new ArrayList();
      }
      return values;
   }


   /**
    * Gets the attributes attribute of the AbstractRepository object
    *
    * @param tag  Description of the Parameter
    * @return     The attributes value
    */
   public List getAttributes(String tag)
   {
      List keywords = (List) this.tagAttributes.get(tag);
      if (keywords == null)
      {
         keywords = new ArrayList();
      }
      return keywords;
   }


   /**
    * Gets the tags attribute of the AbstractRepository object
    *
    * @return   The tags value
    */
   public List getTags()
   {
      return this.tags;
   }


   /**
    * Gets the typeChoiceAttributeValue attribute of the AbstractRepository object
    *
    * @param tag        Description of the Parameter
    * @param attribute  Description of the Parameter
    * @return           The typeChoiceAttributeValue value
    */
   public String getTypeChoiceAttributeValue(String tag, String attribute)
   {
      String key = tag + ">" + attribute;//$NON-NLS-1$
      String filter = (String) this.typeChoiceAttributes.get(key);
      return filter;
   }


   /** Description of the Method */
   private void parse()
   {
      try
      {
         String name = "/" + this.getClass().getName().replace('.', '/') + ".properties";//$NON-NLS-1$ //$NON-NLS-2$
         InputStream is = this.getClass().getResourceAsStream(name);
         if (is != null)
         {
            Properties props = new Properties();
            props.load(is);
            String tagList = props.getProperty("tags", "");//$NON-NLS-1$ //$NON-NLS-2$
            StringTokenizer tagTokenizer = new StringTokenizer(tagList, ",");//$NON-NLS-1$
            while (tagTokenizer.hasMoreTokens())
            {
               String tag = tagTokenizer.nextToken();
               this.tags.add(tag);

               // Search for attributes
               List attributes = new ArrayList();
               String attributeList = props.getProperty("attributes." + tag, "");//$NON-NLS-1$ //$NON-NLS-2$
               StringTokenizer attrTokenizer = new StringTokenizer(attributeList, ",");//$NON-NLS-1$
               while (attrTokenizer.hasMoreTokens())
               {
                  String attribute = attrTokenizer.nextToken();
                  attributes.add(attribute);
               }
               this.tagAttributes.put(tag, attributes);

               // Search for attributes values
               for (int i = 0; i < attributes.size(); i++)
               {
                  String attribute = (String) attributes.get(i);
                  String key = tag + ">" + attribute;//$NON-NLS-1$
                  List attrValues = new ArrayList();

                  String values = props.getProperty("values." + key, "");//$NON-NLS-1$ //$NON-NLS-2$
                  StringTokenizer valuesTokenizer = new StringTokenizer(values, ",");//$NON-NLS-1$
                  while (valuesTokenizer.hasMoreTokens())
                  {
                     String value = valuesTokenizer.nextToken();
                     attrValues.add(value);
                  }
                  this.attributeValues.put(key, attrValues);

                  // Search for TypeChoice attributes
                  String filter = props.getProperty("typechoice." + key, "");//$NON-NLS-1$ //$NON-NLS-2$
                  if (!"".equals(filter)//$NON-NLS-1$
                  )
                  {

                     this.typeChoiceAttributes.put(key, filter);
                  }
               }

            }
            is.close();
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         // Do nothing
      }
   }


   /**
    * Gets the instance attribute of the AbstractRepository class
    *
    * @return   The instance value
    */
   public static synchronized AbstractRepository getInstance()
   {
      return instance;
   }
}
