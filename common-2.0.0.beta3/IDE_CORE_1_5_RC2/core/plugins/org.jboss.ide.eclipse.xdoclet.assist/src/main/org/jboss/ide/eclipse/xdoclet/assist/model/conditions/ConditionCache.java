/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ConditionCache
{
   private final static HashMap cachedConditions = new HashMap();
   private final static HashMap conditionClassesForCache = new HashMap();
   private final static HashMap conditionClassesForNonCache = new HashMap();


   /**
    * Gets the cachedCondition attribute of the ConditionCache class
    *
    * @param classKey                       Description of the Parameter
    * @param initArgs                       Description of the Parameter
    * @return                               The cachedCondition value
    * @exception NoSuchMethodException      Description of the Exception
    * @exception InstantiationException     Description of the Exception
    * @exception IllegalAccessException     Description of the Exception
    * @exception InvocationTargetException  Description of the Exception
    */
   public static Condition getCachedCondition(
         String classKey,
         Object[] initArgs)
          throws
         NoSuchMethodException,
         InstantiationException,
         IllegalAccessException,
         InvocationTargetException
   {
      Class conditionClass;
      if ((conditionClass = (Class) conditionClassesForNonCache.get(classKey))
            != null)
      {
         return getCondition(conditionClass, initArgs);
      }
      else if ((conditionClass = (Class) conditionClassesForCache.get(classKey)) == null)
      {
         return null;
      }
      String cacheKey = classKey;
      for (int i = 0; i < initArgs.length; i++)
      {
         cacheKey += initArgs.toString();
      }
      if (cachedConditions.get(cacheKey) == null)
      {
         cachedConditions.put(cacheKey, getCondition(conditionClass, initArgs));
      }
      return (Condition) cachedConditions.get(cacheKey);
   }


   /**
    * Gets the condition attribute of the ConditionCache class
    *
    * @param conditionClass                 Description of the Parameter
    * @param initArgs                       Description of the Parameter
    * @return                               The condition value
    * @exception NoSuchMethodException      Description of the Exception
    * @exception InstantiationException     Description of the Exception
    * @exception IllegalAccessException     Description of the Exception
    * @exception InvocationTargetException  Description of the Exception
    */
   protected static Condition getCondition(Class conditionClass, Object[] initArgs) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
   {
      Class[] parameterTypes = new Class[initArgs.length];
      for (int i = 0; i < parameterTypes.length; i++)
      {
         parameterTypes[i] = String.class;
      }
      Constructor conditionConstructor =
            conditionClass.getConstructor(parameterTypes);
      return (Condition) conditionConstructor.newInstance(initArgs);
   }

   static
   {
      conditionClassesForCache.put("class", IsClass.class);//$NON-NLS-1$
      conditionClassesForCache.put("method", IsMethod.class);//$NON-NLS-1$
      conditionClassesForCache.put("field", IsField.class);//$NON-NLS-1$
      conditionClassesForCache.put("constructor", IsConstructor.class);//$NON-NLS-1$
      conditionClassesForCache.put("abstract", IsAbstract.class);//$NON-NLS-1$
      conditionClassesForCache.put("public", IsPublic.class);//$NON-NLS-1$
      conditionClassesForCache.put("final", IsFinal.class);//$NON-NLS-1$
      conditionClassesForCache.put("static", IsStatic.class);//$NON-NLS-1$
      conditionClassesForCache.put("type", IsClassOfType.class);//$NON-NLS-1$
      conditionClassesForCache.put("name", NameEquals.class);//$NON-NLS-1$
      conditionClassesForNonCache.put("not", Not.class);//$NON-NLS-1$
      conditionClassesForNonCache.put("or", Or.class);//$NON-NLS-1$
      conditionClassesForNonCache.put("and", And.class);//$NON-NLS-1$
      conditionClassesForCache.put("tag-param", TagParameterEquals.class);//$NON-NLS-1$
      conditionClassesForCache.put("tag-exists", TagExists.class);//$NON-NLS-1$
      conditionClassesForCache.put("tag-param-exists", TagParameterExists.class);//$NON-NLS-1$
      conditionClassesForCache.put("starts-with", NameStartsWith.class);//$NON-NLS-1$
      conditionClassesForNonCache.put("owner", Owner.class);//$NON-NLS-1$
   }

}
