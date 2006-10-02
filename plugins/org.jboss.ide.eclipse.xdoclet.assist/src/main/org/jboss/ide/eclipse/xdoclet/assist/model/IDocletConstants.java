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

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public interface IDocletConstants
{

   /** Description of the Field */
   public static String XTAGS_DTD_PATH = "/xtags_1_1.dtd";//$NON-NLS-1$

   /** Description of the Field */
   public static String VALUES_DTD_PATH = "/jbosside_values_1_0.dtd";//$NON-NLS-1$

   /** Description of the Field */
   public static String VARIABLES_DTD_PATH = "/jbosside_variables_1_0.dtd";//$NON-NLS-1$

   /** Description of the Field */
   public static String TEMPLATES_DTD_PATH = "/jbosside_values_1_0.dtd";//$NON-NLS-1$

   // key for namespace separator in the additional attributes of a node
   /** Description of the Field */
   public static String ATTR_NAMESPACE_SEPARATOR = "separator";//$NON-NLS-1$

   /** Description of the Field */
   public static String ATTR_TYPE = "type";//$NON-NLS-1$

   /** Description of the Field */
   public static String ATTR_MANDATORY = "mandatory";//$NON-NLS-1$

   /** Description of the Field */
   public static String ATTR_UNIQUE = "unique";//$NON-NLS-1$

   /** Description of the Field */
   public static String ATTR_DEFAULT = "default";//$NON-NLS-1$

   /** Description of the Field */
   public static String ATTR_DISCRETE_VALUE_RANGE = "discrete";//$NON-NLS-1$

   /** Description of the Field */
   public static String ATTR_PARSING = "parsing";//$NON-NLS-1$

   /** Description of the Field */
   public static String ATTR_ADDITIONAL_VALUE = "additionalValue";//$NON-NLS-1$

   //This attribute applies to all structure tags.
   /** Description of the Field */
   public static String NAME_ATTRIBUTE = "name";//$NON-NLS-1$

   // value names for xtags.xml
   /** Description of the Field */
   public static String TYPE_BOOL = "bool";//$NON-NLS-1$

   /** Description of the Field */
   public static String TYPE_TEXT = "text";//$NON-NLS-1$

   // tag- and attribute names for xtags.xml
   /** Description of the Field */
   public static String NAMESPACES_TAG = "namespaces";//$NON-NLS-1$

   /** Description of the Field */
   public static String NAMESPACE_TAG = "namespace";//$NON-NLS-1$

   /** Description of the Field */
   public static String MANDATORY_TAG = "mandatory";//$NON-NLS-1$

   /** Description of the Field */
   public static String DEFAULT_TAG = "default";//$NON-NLS-1$

   /** Description of the Field */
   public static String USAGE_DESCRIPTION_TAG = "usage-description";//$NON-NLS-1$

   /** Description of the Field */
   public static String NAME_TAG = "name";//$NON-NLS-1$

   /** Description of the Field */
   public static String LEVEL_TAG = "level";//$NON-NLS-1$

   /** Description of the Field */
   public static String TYPE_TAG = "type";//$NON-NLS-1$

   /** Description of the Field */
   public static String UNIQUE_TAG = "unique";//$NON-NLS-1$

   /** Description of the Field */
   public static String CONDITION_DESCRIPTION_TAG = "condition-description";//$NON-NLS-1$

   /** Description of the Field */
   public static String CONDITION_TAG = "condition";//$NON-NLS-1$

   /** Description of the Field */
   public static String CONDITION_PARAMETER_TAG = "condition-parameter";//$NON-NLS-1$

   /** Description of the Field */
   public static String OPTION_TAG = "option";//$NON-NLS-1$

   /** Description of the Field */
   public static String OPTIONS_TAG = "options";//$NON-NLS-1$

   /** Description of the Field */
   public static String OPTION_SET_TAG = "option-set";//$NON-NLS-1$

   /** Description of the Field */
   public static String OPTION_SETS_TAG = "option-sets";//$NON-NLS-1$

   /** Description of the Field */
   public static String TAGS_TAG = "tags";//$NON-NLS-1$

   /** Description of the Field */
   public static String TAG_TAG = "tag";//$NON-NLS-1$

   /** Description of the Field */
   public static String PARAMETER_TAG = "parameter";//$NON-NLS-1$

   // tag- and attribute names for values
   /** Description of the Field */
   public static String VALUES_TAG = "values";//$NON-NLS-1$

   /** Description of the Field */
   public static String VALUE_TAG = "value";//$NON-NLS-1$

   /** Description of the Field */
   public static String PARSING_TAG = "parsing";//$NON-NLS-1$

   /** Description of the Field */
   public static String PATH_TAG = "path";//$NON-NLS-1$

   // tag- and attribute names for templates
   /** Description of the Field */
   public static String TEMPLATES_TAG = "templates";//$NON-NLS-1$

   /** Description of the Field */
   public static String TEMPLATE_TAG = "template";//$NON-NLS-1$

   /** Description of the Field */
   public static String LEAVE_TAG = "leave";//$NON-NLS-1$

   // tag- and attribute names for variables
   /** Description of the Field */
   public static String VARIABLES_TAG = "variables";//$NON-NLS-1$

   /** Description of the Field */
   public static String VARIABLE_TAG = "variable";//$NON-NLS-1$

   /** Description of the Field */
   public static String SYSTEM_VARIABLE_ATTRIBUTE = "systemvariable";//$NON-NLS-1$

   /** Description of the Field */
   public static String PATTERN_ATTRIBUTE = "pattern";//$NON-NLS-1$

   /** Description of the Field */
   public static String SYSTEM_VARIABLE_CLASSNAME = "classname";//$NON-NLS-1$

   //	public static Serializable MARKER = new Serializable() {
   //	};

   /** Description of the Field */
   public final static String CLASSNAME_VARIABLE = "classname";//$NON-NLS-1$

   /** Description of the Field */
   public final static String PACKAGE_VARIABLE = "package";//$NON-NLS-1$
}
