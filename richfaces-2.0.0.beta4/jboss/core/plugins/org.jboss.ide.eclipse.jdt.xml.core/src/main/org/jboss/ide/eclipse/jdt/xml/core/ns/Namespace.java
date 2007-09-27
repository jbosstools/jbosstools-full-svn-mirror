/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.core.ns;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDContentTypeCategory;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDEnumerationFacet;
import org.eclipse.xsd.XSDFacet;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDPatternFacet;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTerm;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.XSDWildcard;
import org.eclipse.xsd.impl.XSDElementDeclarationImpl;

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAny;
import com.wutka.dtd.DTDAttlist;
import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDComment;
import com.wutka.dtd.DTDContainer;
import com.wutka.dtd.DTDDecl;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEmpty;
import com.wutka.dtd.DTDEnumeration;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDNotationList;
import com.wutka.dtd.DTDOutput;
import com.wutka.dtd.DTDParser;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class Namespace
{
   private Map attributeValues;
   private Map attributes;
   private String dtd;
   // The parsed DTD. Used to provide better content assist. 
   // In the ideal world we abstract this away so both XSD and DTD driven stuff has the same interface,
   // but at the moment I don't know enough about XSD to do that.
   private DTD parsedDTD;
   private Map entities;
   private Map includes;
   private Map comments;
   private String prefix;
   private String uri;

   /** Description of the Field */
   public final static String DEFAULTNAMESPACE = "<default>";//$NON-NLS-1$
   /** Description of the Field */
   public final static String TOPLEVEL = "-toplevel-";//$NON-NLS-1$




   /**
    *Constructor for the Namespace object
    *
    * @param prefix  Description of the Parameter
    * @param uri     Description of the Parameter
    * @param dtd     Description of the Parameter
    */
   public Namespace(String prefix, String uri, String dtd)
   {
      this.prefix = prefix;
      this.uri = uri;
      this.dtd = dtd;
   }


   /**
    * Gets the attributeValues attribute of the Namespace object
    *
    * @param tagName        Description of the Parameter
    * @param attributeName  Description of the Parameter
    * @return               The attributeValues value
    */
   public List getAttributeValues(String tagName, String attributeName)
   {
      return (List) attributeValues.get(tagName + ">" + attributeName);//$NON-NLS-1$
   }


   /**
    * Returns the attributes.
    *
    * @return   Map
    */
   public Map getAttributes()
   {
      return attributes;
   }


   /**
    * Gets the attributesForTag attribute of the Namespace object
    *
    * @param name  Description of the Parameter
    * @return      The attributesForTag value
    */
   public List getAttributesForTag(String name)
   {
      return (List) attributes.get(name);
   }


   /**
    * Returns the dtd.
    *
    * @return   String
    */
   public String getDtd()
   {
      return dtd;
   }


   /**
    * Returns the entities.
    *
    * @return   Map
    */
   public Map getEntities()
   {
      return entities;
   }


   /**
    * Returns the includes.
    *
    * @return   Map
    */
   public Map getIncludes()
   {
      return includes;
   }

   /**
    * @return Returns the comments.
    */
   public Map getComments() {
   	return comments;
   }

   /**
    * Gets the prefix attribute of the Namespace object
    *
    * @return   The prefix value
    */
   public String getPrefix()
   {
      return this.prefix;
   }


   /**
    * Returns the uri.
    *
    * @return   String
    */
   public String getUri()
   {
      return uri;
   }


   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   public boolean readDTD(String filename)
   {
      entities = new TreeMap();
      includes = new TreeMap();
      attributes = new TreeMap();
      attributeValues = new TreeMap();

      return this.parseDTD(filename);
   }


   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   public boolean readSchema(String filename)
   {
      entities = new TreeMap();
      includes = new TreeMap();
      attributes = new TreeMap();
      attributeValues = new TreeMap();

      return this.parseXSD(filename);
   }


   /**
    * Sets the dtd.
    *
    * @param dtd  The dtd to set
    */
   public void setDtd(String dtd)
   {
      this.dtd = dtd;
      this.readDTD(dtd);
   }


   /**
    * Sets the uri.
    *
    * @param uri  The uri to set
    */
   public void setUri(String uri)
   {
      this.uri = uri;
   }


   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    */
   private void handleDTDAttributeDeclaration(DTDElement element)
   {
      String eName = element.getName();

      List attrs = (List) attributes.get(eName);

      Hashtable elementAttributes = element.attributes;
      Iterator it = elementAttributes.keySet().iterator();
      while (it.hasNext())
      {
         String aName = (String) it.next();
         DTDAttribute attribute = (DTDAttribute) elementAttributes.get(aName);

         Object type = attribute.getType();
         String valueDefault = attribute.getDecl().name;
         String value = attribute.getDefaultValue();

         // Get the values for the attribute
         List attrValues = (List) attributeValues.get(eName + ">" + aName);//$NON-NLS-1$

         if (attrs == null)
         {
            attrs = new ArrayList();
            attributes.put(eName, attrs);
         }

         // Add the attribute to the element
         attrs.add(aName);

         if (type instanceof String)
         {
            if (value != null)
            {
               if (attrValues == null)
               {
                  attrValues = new ArrayList();
                  attributeValues.put(eName + ">" + aName, attrValues);//$NON-NLS-1$
               }
               attrValues.add(value);
            }
         }
         else if (type instanceof DTDEnumeration)
         {
            if (attrValues == null)
            {
               attrValues = new ArrayList();
               attributeValues.put(eName + ">" + aName, attrValues);//$NON-NLS-1$
            }
            attrValues.addAll(((DTDEnumeration) type).getItemsVec());
         }
         else if (type instanceof DTDNotationList)
         {
            if (attrValues == null)
            {
               attrValues = new ArrayList();
               attributeValues.put(eName + ">" + aName, attrValues);//$NON-NLS-1$
            }
            attrValues.addAll(((DTDNotationList) type).getItemsVec());
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    */
   private void handleDTDElementDeclaration(DTDElement element)
   {
      List toplevel = (List) this.includes.get(TOPLEVEL);

      if (toplevel == null)
      {
         toplevel = new ArrayList();
         this.includes.put(TOPLEVEL, toplevel);
      }

      String name = element.getName();

      toplevel.add(name);

      List inc = this.processDTDElement(element);

      if (inc != null && inc.size() > 0)
      {
         this.includes.put(name, inc);
      }
   }


   /**
    * Description of the Method
    *
    * @param e     Description of the Parameter
    * @param name  Description of the Parameter
    */
   private void handleXSDElementDeclaration(XSDElementDeclaration e, String name)
   {
      XSDTypeDefinition type = e.getTypeDefinition();
      if (type instanceof XSDSimpleTypeDefinition)
      {
      }
      else
      {
         XSDComplexTypeDefinition xsdComplexTypeDefinition = (XSDComplexTypeDefinition) type;
         switch (xsdComplexTypeDefinition.getContentTypeCategory().getValue())
         {
            case XSDContentTypeCategory.EMPTY:
               break;
            case XSDContentTypeCategory.SIMPLE:
               break;
            case XSDContentTypeCategory.ELEMENT_ONLY:
            case XSDContentTypeCategory.MIXED:
               XSDParticle xsdParticle = (XSDParticle) xsdComplexTypeDefinition.getContentType();
               XSDTerm xsdTerm = xsdParticle.getTerm();
               if (xsdTerm instanceof XSDModelGroup)
               {
                  XSDModelGroup xsdModelGroup = (XSDModelGroup) xsdTerm;
                  handleXSDModelGroup(name, xsdModelGroup);
               }
               else if (xsdTerm instanceof XSDElementDeclaration)
               {
                  String innerTagname = ((XSDElementDeclaration) xsdTerm).getName();
                  List list = (List) includes.get(name);
                  if (list == null)
                  {
                     list = new ArrayList();
                     includes.put(name, list);
                  }
                  if (prefix != null && !DEFAULTNAMESPACE.equals(prefix))
                  {
                     innerTagname = prefix + ":" + innerTagname;//$NON-NLS-1$
                  }
                  if (!list.contains(innerTagname))
                  {
                     list.add(innerTagname);
                  }
               }
               else if (xsdTerm instanceof XSDWildcard)
               {
               }
               break;
         }
         for (Iterator it = xsdComplexTypeDefinition.getAttributeUses().iterator(); it.hasNext(); )
         {
            XSDAttributeUse use = (XSDAttributeUse) it.next();
            XSDAttributeDeclaration decl = use.getAttributeDeclaration();
            List attrs = (List) attributes.get(name);

            if (attrs == null)
            {
               attrs = new ArrayList();
               attributes.put(name, attrs);
            }

            if (!attrs.contains(decl.getName()))
            {
               attrs.add(decl.getName());

               List attrValues = (List) attributeValues.get(name + ">" + decl.getName());//$NON-NLS-1$
               XSDTypeDefinition typedef = decl.getType();
               if (typedef != null)
               {
                  XSDSimpleTypeDefinition xsdSimpleTypeDefinition = (XSDSimpleTypeDefinition) typedef;
                  EList xsdSimpleTypeDefinitionFacets = xsdSimpleTypeDefinition.getFacets();
                  for (Iterator i = xsdSimpleTypeDefinitionFacets.iterator(); i.hasNext(); )
                  {
                     XSDFacet xsdFacet = (XSDFacet) i.next();
                     if (xsdFacet instanceof XSDEnumerationFacet)
                     {
                        if (attrValues == null)
                        {
                           attrValues = new ArrayList();
                           attributeValues.put(name + ">" + decl.getName(), attrValues);//$NON-NLS-1$
                        }
                        for (Iterator j = ((XSDEnumerationFacet) xsdFacet).getValue().iterator(); j.hasNext(); )
                        {
                           Object value = (Object) j.next();

                           if (value instanceof String)
                           {
                              attrValues.add(value);
                           }
                           else if (value instanceof Collection)
                           {
                              attrValues.addAll((Collection) value);
                           }
                        }
                     }
                     else if (xsdFacet instanceof XSDPatternFacet)
                     {
                     }
                  }
               }
            }
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param name           Description of the Parameter
    * @param xsdModelGroup  Description of the Parameter
    */
   private void handleXSDModelGroup(String name, XSDModelGroup xsdModelGroup)
   {
      for (Iterator i = xsdModelGroup.getParticles().iterator(); i.hasNext(); )
      {
         XSDParticle childXSDParticle = (XSDParticle) i.next();
         XSDTerm childXSDTerm = childXSDParticle.getTerm();

         if (childXSDTerm instanceof XSDElementDeclaration)
         {
            String innerTagname = ((XSDElementDeclaration) childXSDTerm).getName();
            List list = (List) includes.get(name);
            if (list == null)
            {
               list = new ArrayList();
               includes.put(name, list);
            }
            if (prefix != null && !DEFAULTNAMESPACE.equals(prefix))
            {
               innerTagname = prefix + ":" + innerTagname;//$NON-NLS-1$
            }
            if (!list.contains(innerTagname))
            {
               list.add(innerTagname);
            }
            if (!((List) includes.get(Namespace.TOPLEVEL)).contains(innerTagname))
            {
               handleXSDElementDeclaration((XSDElementDeclaration) childXSDTerm, innerTagname);
            }
         }
         else if (childXSDTerm instanceof XSDModelGroup)
         {
            for (Iterator it = ((XSDModelGroup) childXSDTerm).getParticles().iterator(); it.hasNext(); )
            {
               XSDParticle childXSDParticle2 = (XSDParticle) it.next();
               XSDTerm childXSDTerm2 = childXSDParticle2.getTerm();

               if (childXSDTerm2 instanceof XSDElementDeclaration)
               {
                  String innerTagname = ((XSDElementDeclaration) childXSDTerm2).getName();
                  List list = (List) includes.get(name);
                  if (list == null)
                  {
                     list = new ArrayList();
                     includes.put(name, list);
                  }
                  if (prefix != null && !DEFAULTNAMESPACE.equals(prefix))
                  {
                     innerTagname = prefix + ":" + innerTagname;//$NON-NLS-1$
                  }
                  if (!list.contains(innerTagname))
                  {
                     list.add(innerTagname);
                     handleXSDElementDeclaration((XSDElementDeclaration) childXSDTerm2, innerTagname);
                  }
               }
            }
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param url  Description of the Parameter
    * @return     Description of the Return Value
    */
   private boolean parseDTD(String url)
   {
      DTDParser parser;
      try
      {
         // To delegate
         parser = new DTDParser(new URL(url));
         DTD dtd = parser.parse();

         Hashtable dtdElements = dtd.elements;
         Iterator it = dtdElements.values().iterator();
         while (it.hasNext())
         {
            DTDElement elem = (DTDElement) it.next();
            this.handleDTDElementDeclaration(elem);
            this.handleDTDAttributeDeclaration(elem);
         }
         
         extractDocumentation(dtd);
         this.parsedDTD = dtd;
      }
      catch (MalformedURLException e)
      {
         e.printStackTrace();
         return false;
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return false;
      }

      return true;
   }


   /**
    * @param dtd2
    */
   private void extractDocumentation(DTD dtd) {
   		comments = new Hashtable();
        String lastComment = null;      
   		DTDAttlist lastAttribute = null;
   		for(int pos=0;pos<dtd.items.size();pos++) {
   			DTDOutput output = (DTDOutput) dtd.items.get(pos);
   			
   			if(output instanceof DTDComment) {
   				DTDComment comment = (DTDComment) output;
   				lastComment = comment.text;
   		        // TODO: get the attribute just after an <!ATTLIST>
   				/*if(lastAttribute!=null && lastComment!=null) {
   					DTDAttribute[] attribs = lastAttribute.getAttribute();
   					for (int i = 0; i < attribs.length; i++) {
						DTDAttribute att = attribs[i];						
						comments.put(lastAttribute.name + ">" + att.getName(), lastComment.toString());
					}
   					lastComment=null;
   				}*/
   				continue;
   			}
   			
   			if(lastComment != null && output instanceof DTDElement) {
   				DTDElement element = (DTDElement) output;
   				comments.put(element.name, lastComment);
   				lastComment = null;
   				continue;
   			}
   			
   			/*
   			if(output instanceof DTDAttlist) {
   				lastAttribute = (DTDAttlist) output;
   				lastComment = null; // clear any comment.
   				continue;
   			}*/
   			/*
   			if(lastComment==null) { 
   				System.err.println("No Docs for " + output.getClass() + " -> " + output);
   			} else {
   				System.err.println("Ignored " + output.getClass() + " -> " + output);
   			}*/
   		}
   }


/**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   private boolean parseXSD(String filename)
   {
      try
      {
         XSDSchema schema = LocalCache.getInstance().getSchema(filename);
         List toplevel = (List) includes.get(TOPLEVEL);

         if (toplevel == null)
         {
            toplevel = new ArrayList();
            includes.put(TOPLEVEL, toplevel);
         }

         for (Iterator it = schema.getElementDeclarations().iterator(); it.hasNext(); )
         {
            XSDElementDeclarationImpl e = (XSDElementDeclarationImpl) it.next();
            String name = e.getName();
            if (prefix != null && !DEFAULTNAMESPACE.equals(prefix))
            {
               name = prefix + ":" + name;//$NON-NLS-1$
            }
            toplevel.add(name);
            this.handleXSDElementDeclaration(e, name);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         // Do nothing
         return false;
      }

      return true;
   }


   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    */
   private List processDTDElement(DTDElement element)
   {
      List inc = new ArrayList();
      DTDItem content = element.getContent();

      if (content instanceof DTDEmpty)
      {
         return null;
      }
      else if (content instanceof DTDAny)
      {
         return (List) includes.get(TOPLEVEL);
      }
      else if (content instanceof DTDName)
      {
         this.processDTDElementIncludes(inc, content);
      }
      else if (content instanceof DTDContainer)
      {
         this.processDTDElementIncludes(inc, content);
      }

      return inc;
   }


   /**
    * Description of the Method
    *
    * @param includes  Description of the Parameter
    * @param item      Description of the Parameter
    */
   private void processDTDElementIncludes(List includes, DTDItem item)
   {
      if (item instanceof DTDName)
      {
         DTDName name = (DTDName) item;
         String value = name.getValue();
         if (!includes.contains(value))
         {
            includes.add(value);
         }
      }
      else if (item instanceof DTDContainer)
      {
         DTDContainer container = (DTDContainer) item;
         List list = container.getItemsVec();
         for (int i = 0; i < list.size(); i++)
         {
            DTDItem subItem = (DTDItem) list.get(i);
            this.processDTDElementIncludes(includes, subItem);
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param dtdString  Description of the Parameter
    * @return           Description of the Return Value
    */
   public static Namespace createDTD(String dtdString)
   {
      Namespace ns = new Namespace(null, null, null);
      ns.parseDTD(dtdString);
      return ns;
   }


   /**
    * 
    * @param tag
    * @return contentmodel as a String if it exists, otherwise null
    */
   public String getContentModel(String tag) {
   	if(parsedDTD!=null) { // only support this for DTD based grammars.
   		DTDElement element = (DTDElement) parsedDTD.elements.get(tag);
   		StringWriter writer = new StringWriter();
   		if(element!=null && element.getContent()!=null) {
   			try {
   				element.getContent().write(new PrintWriter(writer));
			} catch (IOException e) {
				//log but where ? (should at least not happen - so not a biggie)
			}
   			return writer.toString();
   		} else {
   			return null;
   		}   		
   	} else {
   		return null;
   	}
   }

   
   /**
    * @param tag
    * @param attribute
    * @return the datatype for an attribute (e.g. ENUM, NOTATION or just the string type)
    */
   public String getDataType(String tag, String attribute) {
   	if(parsedDTD!=null) {		
   		DTDAttribute dtdAttrib = getAttribute(tag, attribute);
   		
   		if(dtdAttrib!=null) {
   			Object type = dtdAttrib.getType();
   			if(type instanceof String) {
   				return (String)type;
   			} else  if (type instanceof DTDEnumeration) {
   				return "ENUM";
   			} else if (type instanceof DTDNotationList) {
   				return "NOTATION";
   			}			
   		}
   	}
   	return null;
   }
   
   
   /**
    * @param tag
    * @param attribute
    * @return the attribute, null if does not exist
    */
   private DTDAttribute getAttribute(String tag, String attribute) {
   	DTDAttribute dtdAttrib = null;
   	DTDElement element = (DTDElement) parsedDTD.elements.get(tag);
   	if(element!=null) {
   		dtdAttrib = element.getAttribute(attribute);
   	}
   	return dtdAttrib;
   }
   
   
   /**
    * @param tag
    * @param attribute
    * @return default attribute value, null if none exist
    */
   public String getDefaultAttributeValue(String tag, String attribute) {
   	DTDAttribute dtdAttrib = getAttribute(tag, attribute);
   	if(dtdAttrib!=null) {
   		return dtdAttrib.getDefaultValue();
   	} else {
   		return null;
   	}
   }
   
   /**
    * TODO: returns String[] to avoid to many unnecessary copies. (should return List, to comply with the rest)
    * @param tag
    * @param attribute
    * @return String[] of enumarated values, null if does not exist
    */
   public String[] getEnumeratedValues(String tag, String attribute) {
   	DTDAttribute dtdAttrib = getAttribute(tag, attribute);
   	
   	Object type = dtdAttrib.getType();
   	if(type instanceof String) {
   		return null;
   	} else  if (type instanceof DTDEnumeration) {
   		DTDEnumeration dtdEnum = (DTDEnumeration) type;
   		return dtdEnum.getItems();
   	} else if (type instanceof DTDNotationList) {
   		DTDNotationList list = (DTDNotationList) type;
   		return list.getItems();
   	}
   	return null;
   }

   
   /**
    * 
    * @param tag
    * @return a string with required attribs. null if not available.
    */
   public String getRequiredAttribsString(String tag) {
   		if(parsedDTD!=null) {
   			DTDElement element = (DTDElement) parsedDTD.elements.get(tag);
   			if(element!=null) {
   				//TODO:  the parser does not keep these attributes in order, so their sequence is "random"..would be better if they were added in the sequence of the DTD.
   	   			// probably not a biggie, unless you have a big set of required attributes.
   				StringBuffer line = new StringBuffer(); 
  					Enumeration enumeration = element.attributes.elements();
  					while (enumeration.hasMoreElements()) {
						DTDAttribute dtdAttrib = (DTDAttribute) enumeration.nextElement();
						if(dtdAttrib.decl == DTDDecl.REQUIRED) {
							line.append(" " + dtdAttrib.getName() + "=\"" + (dtdAttrib.getDefaultValue()==null?"":dtdAttrib.getDefaultValue()) + "\"");
						}
					}
  					return line.toString();
   			}
   		}
   		return null;
   }
}
