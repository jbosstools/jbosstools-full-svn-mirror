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
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.assist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;

import org.eclipse.core.resources.IFile;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPElementInfo;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProject;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProjectManager;

/**
 * Fetch taglib informations and store them.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPTagLibRepository
{
   private Map attributeValues = new Hashtable();

   private Map attributes = new Hashtable();

   private Map tags = new Hashtable();

   private static JSPTagLibRepository repository = new JSPTagLibRepository();

   /**Constructor for the JSPTagLibRepository object */
   private JSPTagLibRepository()
   {
   }

   /**
    * Description of the Method
    *
    * @param file  Description of the Parameter
    */
   public synchronized void clearInformations(IFile file)
   {
      this.tags.remove(file);
   }

   /**
    * Gets the attributeValues attribute of the JSPTagLibRepository object
    *
    * @param file       Description of the Parameter
    * @param tag        Description of the Parameter
    * @param attribute  Description of the Parameter
    * @return           The attributeValues value
    */
   public List getAttributeValues(IFile file, String tag, String attribute)
   {
      Map attributeValueMap = (Map) this.attributeValues.get(file);
      List attributeValueList = null;
      if (attributeValueMap != null)
      {
         attributeValueList = (List) attributeValueMap.get(tag + ">" + attribute);//$NON-NLS-1$
      }
      if (attributeValueList == null)
      {
         attributeValueList = new ArrayList();
      }
      return attributeValueList;
   }

   /**
    * Gets the attributes attribute of the JSPTagLibRepository object
    *
    * @param file  Description of the Parameter
    * @param tag   Description of the Parameter
    * @return      The attributes value
    */
   public List getAttributes(IFile file, String tag)
   {
      Map attributeMap = (Map) this.attributes.get(file);
      List attributeList = null;
      if (attributeMap != null)
      {
         attributeList = (List) attributeMap.get(tag);
      }
      if (attributeList == null)
      {
         attributeList = new ArrayList();
      }
      return attributeList;
   }

   /**
    * Gets the tags attribute of the JSPTagLibRepository object
    *
    * @param file  Description of the Parameter
    * @return      The tags value
    */
   public List getTags(IFile file)
   {
      List tagList = (List) this.tags.get(file);
      if (tagList == null)
      {
         tagList = new ArrayList();
      }
      return tagList;
   }

   /**
    * Description of the Method
    *
    * @param file  Description of the Parameter
    */
   public synchronized void loadInformations(IFile file)
   {
      this.clearInformations(file);

      List tagList = new ArrayList();
      Map attributeMap = new HashMap();
      Map attributeValueMap = new HashMap();

      JSPProject jspProject = JSPProjectManager.getJSPProject(file.getProject());
      if (jspProject != null)
      {
         JSPElementInfo info = jspProject.compileJSP(file);
         if (info != null)
         {
            Collection taglibs = info.getPageInfo().getTaglibs();
            for (Iterator it = taglibs.iterator(); it.hasNext();)
            {
               TagLibraryInfo taglibInfo = (TagLibraryInfo) it.next();
               String prefix = taglibInfo.getPrefixString();

               TagInfo[] tagInfos = taglibInfo.getTags();
               for (int i = 0; i < tagInfos.length; i++)
               {
                  TagInfo tagInfo = tagInfos[i];
                  String tagName = prefix + ":" + tagInfo.getTagName();//$NON-NLS-1$
                  tagList.add(tagName);

                  TagAttributeInfo[] tagAttributes = tagInfo.getAttributes();
                  List attributeList = new ArrayList();
                  for (int j = 0; j < tagAttributes.length; j++)
                  {
                     TagAttributeInfo tagAttrInfo = tagAttributes[j];
                     String attrName = tagAttrInfo.getName();
                     attributeList.add(attrName);

                     String key = tagName + ">" + attrName;//$NON-NLS-1$
                     List valueList = new ArrayList();
                     // To do
                     attributeValueMap.put(key, valueList);
                  }
                  attributeMap.put(tagName, attributeList);
               }
            }

            // Sort the taglib tags
            Collections.sort(tagList);
         }
      }

      this.tags.put(file, tagList);
      this.attributes.put(file, attributeMap);
      this.attributeValues.put(file, attributeValueMap);
   }

   /**
    * Gets the instance attribute of the JSPTagLibRepository class
    *
    * @return   The instance value
    */
   public static JSPTagLibRepository getInstance()
   {
      return repository;
   }
}
