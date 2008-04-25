/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.dialogs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.utils.CacheUtil;
import org.jboss.ide.eclipse.jbosscache.views.content.TableContentProvider.TableContentModel;

/**
 * Content provider for <code>ContentViewDialog</code> control
 * @author Owner
 * @see org.jboss.ide.eclipse.jbosscache.dialogs.ContentViewDialog
 */
public class CViewDialogContentProvider implements ITreeContentProvider
{

   static class ParentModel
   {

      private String name;

      private List children;

      private ParentModel parent;

      private Object value;

      public ParentModel(String name, Object value)
      {
         this.name = name;
         this.value = value;
      }

      public String getName()
      {
         return name;
      }

      public void addChildren(DialogViewModel obj)
      {
         if (children == null)
            children = new ArrayList();
         children.add(obj);
      }

      public boolean hasChilds()
      {
         if (children == null)
            return false;

         return children == null ? false : true;
      }

      public ParentModel getParent()
      {
         return parent;
      }

      public Object getValue()
      {
         return this.value;
      }

      public void setParent(ParentModel parent)
      {
         this.parent = parent;
      }

      public void setChildren(List list)
      {
         this.children = list;
      }

   }

   static class DialogViewModel
   {

      private String fieldName;

      private String fieldValue;

      private ParentModel parent;

      DialogViewModel(String fieldName, String fieldValue)
      {
         this.fieldName = fieldName;
         this.fieldValue = fieldValue;
      }

      public String getFieldName()
      {
         return fieldName;
      }

      public String getFieldValue()
      {
         return this.fieldValue;
      }

      public void setParent(ParentModel model)
      {
         this.parent = model;
      }

      public ParentModel getParent()
      {
         return parent;
      }

   }

   public Object[] getChildren(Object parentElement)
   {
      ClassLoader classLoader = null;
      
      try      
      {
         if(!TableContentModel.isRemoteCache())
            classLoader = TableContentModel.getCacheManager().getManagerClassLoader();
         else
            classLoader = TableContentModel.getRemoteCacheManager().getManagerLoader();
         
         if (parentElement == null)
            return new Object[0];
         else
         {
            if (parentElement instanceof TableContentModel)
            {
               
               TableContentModel contentModel = (TableContentModel) parentElement;
               List listModel = new ArrayList();

               String parentName = contentModel.getValue().getClass().getName();
                                             
               ParentModel parentModel = new ParentModel(parentName, contentModel.getValue());
               
               parentModel.setChildren(new ArrayList());
               listModel.add(parentModel);

               return listModel.toArray(new ParentModel[listModel.size()]);
            }//end of if TableContentModel
            else if (parentElement instanceof ParentModel)
            {
               try
               {
                  ParentModel parentModel = (ParentModel) parentElement;
                  Map fieldsMap = new HashMap();
                  List listModel = new ArrayList();
                  
                  Class clazz = null;

                  if (parentModel.getValue() == null)
                  {
                     try
                     {
                        clazz = Class.forName(parentModel.getName(), true, classLoader);
                     }
                     catch (ClassNotFoundException e)
                     {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                  }
                  else
                     clazz = parentModel.getValue().getClass();
                  
                  //Node content is collection object
                  if(CacheUtil.isCollectionAssignable(clazz)){
                     handleNodeContentCollection(parentModel,listModel);
                     return listModel.toArray(new Object[listModel.size()]);
                  }//end of if Collection                                 
                  

                  fieldsMap = CacheUtil.getAllFields(clazz);

                  Map tempSuperMap = CacheUtil.getAllFields(clazz.getSuperclass());

                  if (fieldsMap != null)
                  {
                     if (tempSuperMap != null)
                        fieldsMap.putAll(tempSuperMap);
                  }
                  else
                  {
                     fieldsMap = tempSuperMap;
                  }

                  if (fieldsMap != null)
                  {
                     Set keySet = fieldsMap.keySet();
                     if (keySet != null)
                     {
                        Iterator it = keySet.iterator();
                        while (it.hasNext())
                        {

                           StringBuffer fieldName = new StringBuffer(it.next().toString());
                           Field f = (Field) fieldsMap.get(fieldName.toString());

                           if (CacheUtil.isImmediate(f.getType()))
                           {
                              String value = "";

                              if (parentModel.getValue() != null)
                              {
                                 if (f.get(parentModel.getValue()) != null)
                                    value = f.get(parentModel.getValue()).toString();
                              }

                              DialogViewModel dialogModel = new DialogViewModel(fieldName.toString(), value);
                              dialogModel.setParent(parentModel);
                              parentModel.addChildren(dialogModel);

                              listModel.add(dialogModel);
                           }
                           else
                           {

                              if (CacheUtil.isCollection(f.getType()))
                              {
                                 handleCollection(f, listModel, parentModel);
                              }
                              else
                              {

                                 ParentModel childParentModel = null;
                                 if (parentModel.getValue() != null)
                                    childParentModel = new ParentModel(f.getType().getName(), f.get(parentModel
                                          .getValue()));
                                 else
                                    childParentModel = new ParentModel(f.getType().getName(), null);

                                 childParentModel.setParent(parentModel);

                                 Map fieldsMapChild = new HashMap();
                                 fieldsMapChild = CacheUtil.getAllFields(f.getType());

                                 tempSuperMap = CacheUtil.getAllFields(f.getType().getSuperclass());

                                 if (fieldsMapChild != null)
                                 {
                                    if (tempSuperMap != null)
                                       fieldsMapChild.putAll(tempSuperMap);
                                 }
                                 else
                                 {
                                    fieldsMapChild = tempSuperMap;
                                 }

                                 if (fieldsMapChild != null)
                                    if (!fieldsMapChild.isEmpty())
                                       childParentModel.setChildren(new ArrayList());

                                 listModel.add(childParentModel);
                              }
                           }
                        }//end of while
                     }//end of if keySet
                  }
                  return listModel.toArray(new Object[listModel.size()]);
               }
               catch (IllegalAccessException e)
               {
                  e.printStackTrace();
               }
            }//end of else if
            else
            {
               DialogViewModel dialogModel = (DialogViewModel) parentElement;
               Object[] obj = new Object[1];
               obj[0] = dialogModel;

               return obj;
            }
         }//end of else 
      }
      catch (Exception e)
      {
         e.printStackTrace();
         IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e.getMessage(),
               e);
         JBossCachePlugin.getDefault().getLog().log(status);
      }
      return new Object[0];
   }

   /** 
    * Node content object field f contains collection type
    * @param f Fields of the node object
    * @param list
    * @param pm
    * @throws IllegalAccessException
    */
   private void handleCollection(Field f, List list, ParentModel pm) throws IllegalAccessException
   {
      Class clazz = f.getType();
      Object collValue = f.get(pm.getValue());
      ParentModel parentModel = null;
      if (pm.getValue() == null || collValue == null)
      {
         parentModel = new ParentModel(clazz.getName(), null);
         parentModel.setParent(pm);
         list.add(parentModel);
         return;
      }

      if (clazz.isAssignableFrom(Map.class))
      {
         Map map = (Map) collValue;
         Set set = map.keySet();
         if ((set != null) && (!(set.isEmpty())))
         {
            Iterator it = set.iterator();
            while (it.hasNext())
            {
               Object objKey = it.next();
               Object obj = map.get(objKey);
               parentModel = new ParentModel(obj.getClass().getName(), obj);
               parentModel.setParent(pm);
               list.add(parentModel);
               handleFields(parentModel, obj.getClass());
            }
         }
      }
      else if (clazz.isAssignableFrom(Set.class))
      {
         Set set = (Set) collValue;
         if ((set != null) && (!(set.isEmpty())))
         {
            Iterator it = set.iterator();
            while (it.hasNext())
            {
               Object obj = it.next();
               parentModel = new ParentModel(obj.getClass().getName(), obj);
               parentModel.setParent(pm);
               list.add(parentModel);
               handleFields(parentModel, obj.getClass());
            }
         }
      }
      else if (clazz.isAssignableFrom(List.class))
      {
         List listList = (List) collValue;
         for (int i = 0; i < listList.size(); i++)
         {
            Object obj = listList.get(i);
            parentModel = new ParentModel(obj.getClass().getName(), obj);
            parentModel.setParent(pm);
            list.add(parentModel);
            handleFields(parentModel, obj.getClass());
         }
      }
   }
   
   /**
    * Node content object is type of collection
    * @param pm Collection model
    * @param list Model elements
    */
   private void handleNodeContentCollection(ParentModel pm,List list){
      Class clazz = pm.getValue().getClass();
      
      ParentModel parentModel = null;
      if(clazz.isAssignableFrom(Map.class) || clazz.isAssignableFrom(HashMap.class)){
         Map map = (Map) pm.getValue();
         Set set = map.keySet();
         if ((set != null) && (!(set.isEmpty())))
         {
            Iterator it = set.iterator();
            while (it.hasNext())
            {
               Object objKey = it.next();
               Object obj = map.get(objKey);             
               parentModel = new ParentModel(obj.getClass().getName(), obj);
               parentModel.setParent(pm);
               list.add(parentModel);
               handleFields(parentModel, obj.getClass());
            }
         }         
      }else if(clazz.isAssignableFrom(Set.class) || clazz.isAssignableFrom(HashSet.class)){
         Set set = (Set) pm.getValue();
         if ((set != null) && (!(set.isEmpty())))
         {
            Iterator it = set.iterator();
            while (it.hasNext())
            {
               Object obj = it.next();
               parentModel = new ParentModel(obj.getClass().getName(), obj);
               parentModel.setParent(pm);
               list.add(parentModel);
               handleFields(parentModel, obj.getClass());
            }
         }
         
      }else if(clazz.isAssignableFrom(List.class) || clazz.isAssignableFrom(ArrayList.class)){
         List listList = (List) pm.getValue();
         for (int i = 0; i < listList.size(); i++)
         {
            Object obj = listList.get(i);
            parentModel = new ParentModel(obj.getClass().getName(), obj);
            parentModel.setParent(pm);
            list.add(parentModel);
            handleFields(parentModel, obj.getClass());
         }         
      }
   }
   

   /**
    * If node content object field is collection, return model objects 
    * related with tihs collection contains
    * @param model
    * @param clazz
    */
   private void handleFields(ParentModel model, Class clazz)
   {
      Map fieldsMap = new HashMap();
      fieldsMap = CacheUtil.getAllFields(clazz);

      Map tempSuperMap = CacheUtil.getAllFields(clazz.getSuperclass());

      if (fieldsMap != null)
      {
         if (tempSuperMap != null)
            fieldsMap.putAll(tempSuperMap);
      }
      else
      {
         fieldsMap = tempSuperMap;
      }

      if (fieldsMap != null)
         if (!fieldsMap.isEmpty())
            model.setChildren(new ArrayList());

   }

   public Object getParent(Object element)
   {
      if (element instanceof DialogViewModel)
         return ((DialogViewModel) element).getParent();
      else if (element instanceof ParentModel)
         return ((ParentModel) element).getParent();
      else
         return null;
   }

   public boolean hasChildren(Object element)
   {
      if (element instanceof DialogViewModel)
         return false;
      else if (element instanceof ParentModel)
      {
         ParentModel model = (ParentModel) element;
         return model.hasChilds();
      }
      else
         return false;
   }

   public Object[] getElements(Object inputElement)
   {
      return getChildren(inputElement);
   }

   public void dispose()
   {
      // TODO Auto-generated method stub

   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
      // TODO Auto-generated method stub

   }
}