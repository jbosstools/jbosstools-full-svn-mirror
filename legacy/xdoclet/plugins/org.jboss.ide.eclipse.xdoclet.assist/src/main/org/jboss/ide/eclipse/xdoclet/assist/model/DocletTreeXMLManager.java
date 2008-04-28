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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.Condition;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.ConditionCache;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.ConditionException;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.ToManyChildrenConditionsException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Inspired by the xtags.ConditionFactory class from Aslak Hellesøy of the XDoclet team
 *
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   15 mai 2003
 */
public class DocletTreeXMLManager
{
   /** Description of the Field */
   protected DocletTree docletTree;

   private Document document;

   /**
    * Returns the docletTree.
    *
    * @return   DocletTree
    */
   public DocletTree getDocletTree()
   {
      return docletTree;
   }

   /**
    * Description of the Method
    *
    * @param urls     Description of the Parameter
    * @param monitor  Description of the Parameter
    * @param range    Description of the Parameter
    * @return         Description of the Return Value
    */
   public DocletTree initDocletTree(URL[] urls, IProgressMonitor monitor, int range)
   {
      int stepSize = 1;
      try
      {
         stepSize = range / urls.length;
      }
      catch (RuntimeException ignore)
      {
      }
      DocletTree tree = new DocletTree();
      for (int i = 0; i < urls.length; i++)
      {
         System.out.println(XDocletAssistMessages.getString("DocletTreeXMLManager.Process___13") + urls[i]);//$NON-NLS-1$
         String showText = ".... " + urls[i].getFile().substring(urls[i].getFile().length() - 50);//$NON-NLS-1$
         monitor.subTask(XDocletAssistMessages.getString("DocletTreeXMLManager.Processing_Module___15") + showText);//$NON-NLS-1$
         processModule(urls[i], tree);
         monitor.worked(stepSize);
      }
      setDocletTree(tree);
      return tree;
   }

   /**
    * Description of the Method
    *
    * @param inputStream                 Description of the Parameter
    * @return                            Description of the Return Value
    * @exception IOException             Description of the Exception
    * @exception ClassNotFoundException  Description of the Exception
    */
   public DocletTree readDocletTreeFromCache(InputStream inputStream) throws IOException, ClassNotFoundException
   {
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
      setDocletTree((DocletTree) objectInputStream.readObject());
      return this.docletTree;
   }

   /**
    * Sets the docletTree.
    *
    * @param docletTree  The docletTree to set
    */
   public void setDocletTree(DocletTree docletTree)
   {
      this.docletTree = docletTree;
   }

   /**
    * Description of the Method
    *
    * @param outputStream     Description of the Parameter
    * @exception IOException  Description of the Exception
    */
   public void writeDocletTreeToCache(OutputStream outputStream) throws IOException
   {
      ObjectOutputStream objectOutputStream;
      objectOutputStream = new ObjectOutputStream(outputStream);
      objectOutputStream.writeObject(docletTree);
      objectOutputStream.close();
   }

   /**
    * Description of the Method
    *
    * @param parameterElement               Description of the Parameter
    * @param parentDocletElement            Description of the Parameter
    * @exception NoSuchMethodException      Description of the Exception
    * @exception InstantiationException     Description of the Exception
    * @exception IllegalAccessException     Description of the Exception
    * @exception InvocationTargetException  Description of the Exception
    * @exception ConditionException         Description of the Exception
    */
   protected void iterateOptions(Element parameterElement, DocletElement parentDocletElement)
         throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException,
         ConditionException
   {
      // find the valid options for this condition
      Element optionSetsElement = parameterElement.getChild(IDocletConstants.OPTION_SETS_TAG);

      if (optionSetsElement != null)
      {
         List optionsSetElements = optionSetsElement.getChildren(IDocletConstants.OPTION_SET_TAG);

         for (Iterator optionSetIterator = optionsSetElements.iterator(); optionSetIterator.hasNext();)
         {
            Element optionSetElement = (Element) optionSetIterator.next();
            String defaultOption = "";//$NON-NLS-1$

            if (optionSetElement.getChild(IDocletConstants.DEFAULT_TAG) != null)
            {
               defaultOption = optionSetElement.getChild(IDocletConstants.DEFAULT_TAG).getTextTrim();
            }

            // find the options
            Element optionsElement = optionSetElement.getChild(IDocletConstants.OPTIONS_TAG);
            // String[] options = null;

            boolean atLeastOneOption = false;
            if (optionsElement != null)
            {
               Condition optionSetCondition = iterateCondition(optionSetElement, null);
               List optionElements = optionsElement.getChildren(IDocletConstants.OPTION_TAG);
               for (Iterator optionIterator = optionElements.iterator(); optionIterator.hasNext();)
               {
                  Element optionElement = (Element) optionIterator.next();
                  DocletElement optionDocletElement = null;
                  try
                  {
                     optionDocletElement = parentDocletElement.addChild(optionElement.getTextTrim());
                  }
                  catch (NodeExistsException e)
                  {
                     System.out.println(e.getMessage());
                     continue;
                  }
                  atLeastOneOption = true;
                  optionDocletElement.getConditionTree().setRoot(optionSetCondition);
                  if (optionDocletElement.getName().equals(defaultOption))
                  {
                     optionDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_DEFAULT,
                           new Marker());
                  }
               }
            }
            if (atLeastOneOption)
            {
               parentDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_DISCRETE_VALUE_RANGE,
                     new Marker());
            }
         }
      }
   }

   /**
    * Description of the Method
    *
    * @param xtagsURL    Description of the Parameter
    * @param docletTree  Description of the Parameter
    */
   protected void processModule(URL xtagsURL, DocletTree docletTree)
   {
      try
      {
         read(xtagsURL);

         Element rootElement = document.getRootElement();
         List namespaceElements = rootElement.getChildren(IDocletConstants.NAMESPACE_TAG);

         for (Iterator namespaces = namespaceElements.iterator(); namespaces.hasNext();)
         {
            Element namespace = (Element) namespaces.next();

            // example: weblogic. typically corresponds to a gui tab
            DocletElement namespaceDocletElement = null;
            try
            {
               namespaceDocletElement = docletTree
                     .addChild(namespace.getChild(IDocletConstants.NAME_TAG).getTextTrim());
            }
            catch (NodeExistsException e)
            {
               System.out.println(e.getMessage());
               continue;
            }
            namespaceDocletElement
                  .setHelpText(namespace.getChild(IDocletConstants.USAGE_DESCRIPTION_TAG).getTextTrim());
            String namespaceConditionDescription = "";//$NON-NLS-1$
            if (namespace.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG) != null)
            {
               namespaceConditionDescription = namespace.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG)
                     .getTextTrim();
            }
            namespaceDocletElement.setConditionDescription(namespaceConditionDescription);
            if (namespace.getChild(IDocletConstants.CONDITION_TAG) != null)
            {
               namespaceDocletElement.getConditionTree().setRoot(iterateCondition(namespace, null));
            }

            List tagsElements = namespace.getChildren(IDocletConstants.TAGS_TAG);

            for (Iterator tagsIterator = tagsElements.iterator(); tagsIterator.hasNext();)
            {
               Element tagsElement = (Element) tagsIterator.next();
               List tagElements = tagsElement.getChildren(IDocletConstants.TAG_TAG);

               for (Iterator tagIterator = tagElements.iterator(); tagIterator.hasNext();)
               {
                  Element tagElement = (Element) tagIterator.next();
                  String fullTagName = tagElement.getChild(IDocletConstants.NAME_TAG).getTextTrim();
                  int pos = fullTagName.indexOf('.');
                  if (pos == -1)
                  {
                     pos = fullTagName.indexOf(':');
                  }
                  // There must be a separator so it is not checked
                  String codeName = fullTagName.substring(pos + 1);
                  //						process level
                  List levelElements = tagElement.getChildren(IDocletConstants.LEVEL_TAG);
                  String[] levelNames = new String[levelElements.size()];
                  for (int i = 0; i < levelNames.length; i++)
                  {
                     Element levelElement = (Element) levelElements.get(i);
                     levelNames[i] = levelElement.getTextTrim();
                  }
                  Arrays.sort(levelNames);
                  String tagName = new String(codeName);
                  for (int i = 0; i < levelNames.length; i++)
                  {
                     tagName += ":" + levelNames[i];//$NON-NLS-1$
                  }
                  DocletElement tagDocletElement = null;
                  try
                  {
                     tagDocletElement = namespaceDocletElement.addChild(tagName);
                  }
                  catch (NodeExistsException e)
                  {
                     System.out.println(e.getMessage());
                     continue;
                  }
                  tagDocletElement.setCodename(codeName);
                  tagDocletElement.getNode().addAdditionalAttribute(IDocletConstants.ATTR_NAMESPACE_SEPARATOR,
                        "" + fullTagName.charAt(pos));//$NON-NLS-1$
                  tagDocletElement.setHelpText(tagElement.getChild(IDocletConstants.USAGE_DESCRIPTION_TAG)
                        .getTextNormalize());
                  String conditionDescription = "";//$NON-NLS-1$
                  if (tagElement.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG) != null)
                  {
                     conditionDescription = tagElement.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG)
                           .getTextNormalize();
                  }
                  tagDocletElement.setConditionDescription(conditionDescription);
                  boolean unique = false;
                  if (tagElement.getChild(IDocletConstants.UNIQUE_TAG) != null)
                  {
                     unique = (new Boolean(true)).toString().equals(
                           tagElement.getChild(IDocletConstants.UNIQUE_TAG).getTextTrim());
                  }
                  // process tag condition
                  // Condition tagCondition;
                  tagDocletElement.getConditionTree().setRoot(iterateCondition(tagElement, null));

                  tagDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_UNIQUE,
                        new Boolean(unique));
                  List parameterElements = tagElement.getChildren(IDocletConstants.PARAMETER_TAG);
                  for (Iterator parameterIterator = parameterElements.iterator(); parameterIterator.hasNext();)
                  {
                     Element parameterElement = (Element) parameterIterator.next();
                     try
                     {
                        parseTagParameter(parameterElement, tagDocletElement);
                     }
                     catch (NodeExistsException e)
                     {
                        System.out.println(e.getMessage());
                        continue;
                     }
                  }
               }
            }
         }

      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }
   }

   /**
    * Description of the Method
    *
    * @param conditionElementParent         Description of the Parameter
    * @param parentCondition                Description of the Parameter
    * @return                               Description of the Return Value
    * @exception NoSuchMethodException      Description of the Exception
    * @exception InstantiationException     Description of the Exception
    * @exception IllegalAccessException     Description of the Exception
    * @exception InvocationTargetException  Description of the Exception
    * @exception ConditionException         Description of the Exception
    */
   private Condition iterateCondition(Element conditionElementParent, Condition parentCondition)
         throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException,
         ConditionException
   {

      Condition result = null;
      // find sub conditions and add them to the current condition
      List conditionElements = conditionElementParent.getChildren(IDocletConstants.CONDITION_TAG);

      for (int i = 0; i < conditionElements.size(); i++)
      {
         Element conditionElement = (Element) conditionElements.get(i);

         // look at type attr and look up corresponding class name for Condition
         String conditionType = conditionElement.getAttributeValue(IDocletConstants.TYPE_TAG);
         // find parameters for constructor. they're always strings, and the last is an
         // array of strings for options. The options might be null.
         List parameterElements = conditionElement.getChildren(IDocletConstants.CONDITION_PARAMETER_TAG);
         Object[] initArgs = new Object[parameterElements.size()];
         for (int p = 0; p < parameterElements.size(); p++)
         {
            initArgs[p] = ((Element) parameterElements.get(p)).getTextTrim();
         }
         Condition condition;
         if ((condition = ConditionCache.getCachedCondition(conditionType, initArgs)) == null)
         {
            // not found this condition - bomb politely and
            // return empty condition
            throw new ConditionException("can not find condition class for: " + conditionType);//$NON-NLS-1$
         }
         result = condition;
         if (parentCondition != null)
         {
            try
            {
               parentCondition.addChildCondition(condition);
            }
            catch (ToManyChildrenConditionsException e)
            {
               String tagName = null;
               String parameterName = null;
               Element parent = conditionElement.getParent();
               while (parent != null && !parent.getName().equals(IDocletConstants.TAG_TAG))
               {
                  if (parent.getName().equals(IDocletConstants.PARAMETER_TAG))
                  {
                     parameterName = parent.getChildText(IDocletConstants.NAME_ATTRIBUTE);
                  }
                  parent = parent.getParent();
               }
               if (parent != null)
               {
                  tagName = parent.getChildText(IDocletConstants.NAME_ATTRIBUTE);
               }
               System.out.println("Exception while reading: " //$NON-NLS-1$
                     + tagName + " " //$NON-NLS-1$
                     + parameterName + " " //$NON-NLS-1$
                     + e.getMessage());

            }
         }
         iterateCondition(conditionElement, condition);
      }

      return result;
   }

   /**
    * Description of the Method
    *
    * @param tagParameterElement            Description of the Parameter
    * @param parentDocletElement            Description of the Parameter
    * @return                               Description of the Return Value
    * @exception NoSuchMethodException      Description of the Exception
    * @exception InstantiationException     Description of the Exception
    * @exception IllegalAccessException     Description of the Exception
    * @exception InvocationTargetException  Description of the Exception
    * @exception ConditionException         Description of the Exception
    */
   private DocletElement parseTagParameter(Element tagParameterElement, DocletElement parentDocletElement)
         throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException,
         ConditionException
   {
      DocletElement parameterDocletElement;
      // Condition condition = iterateCondition(tagParameterElement, null);
      iterateCondition(tagParameterElement, null);

      parameterDocletElement = parentDocletElement.addChild(tagParameterElement.getChild(IDocletConstants.NAME_TAG)
            .getTextTrim());
      parameterDocletElement.setHelpText(tagParameterElement.getChild(IDocletConstants.USAGE_DESCRIPTION_TAG)
            .getTextNormalize());
      String parameterConditionDescription = "";//$NON-NLS-1$
      if (tagParameterElement.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG) != null)
      {
         parameterConditionDescription = tagParameterElement.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG)
               .getTextTrim();
      }
      parameterDocletElement.setConditionDescription(parameterConditionDescription);
      parameterDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_TYPE,
            tagParameterElement.getAttributeValue(IDocletConstants.TYPE_TAG));
      boolean mandatory = false;
      Element tagParameterMandatoryElement = tagParameterElement.getChild(IDocletConstants.MANDATORY_TAG);
      if (tagParameterMandatoryElement != null)
      {
         mandatory = (new Boolean(true)).toString().equals(tagParameterMandatoryElement.getTextTrim());
      }
      parameterDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_MANDATORY,
            new Boolean(mandatory));

      parameterDocletElement.getConditionTree().setRoot(iterateCondition(tagParameterElement, null));

      if (parameterDocletElement.getNode().getAdditionalAttributes().get(IDocletConstants.ATTR_TYPE).equals(
            IDocletConstants.TYPE_BOOL))
      {
         parameterDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_DISCRETE_VALUE_RANGE,
               new Marker());
         Element tagParameterDefaultElement = tagParameterElement.getChild(IDocletConstants.DEFAULT_TAG);
         String defaultValue = null;
         DocletElement trueDocletElement = parameterDocletElement.addChild("true");//$NON-NLS-1$
         DocletElement falseDocletElement = parameterDocletElement.addChild("false");//$NON-NLS-1$
         if (tagParameterDefaultElement != null)
         {
            defaultValue = tagParameterDefaultElement.getTextTrim();
         }
         if (defaultValue != null)
         {
            if ((new Boolean(true)).toString().equals(defaultValue))
            {
               trueDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_DEFAULT, new Marker());
            }
            else
            {
               falseDocletElement.getNode().getAdditionalAttributes().put(IDocletConstants.ATTR_DEFAULT, new Marker());
            }
         }
      }
      else
      {
         iterateOptions(tagParameterElement, parameterDocletElement);
      }
      return parameterDocletElement;
   }

   /**
    * Description of the Method
    *
    * @param xml                Description of the Parameter
    * @exception JDOMException  Description of the Exception
    */
   private void read(URL xml) throws JDOMException
   {
      SAXBuilder builder = new SAXBuilder();

      builder.setEntityResolver(new EntityResolver()
      {
         public InputSource resolveEntity(String s, String s1)
         {
            return XTagsProvider.getXTagsDTDInputSource();
         }
      });
      document = builder.build(xml);
   }
}
