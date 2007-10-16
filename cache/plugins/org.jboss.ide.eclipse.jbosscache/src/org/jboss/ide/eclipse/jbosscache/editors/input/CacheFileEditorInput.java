/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.editors.input;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * @since 3.0
 */
public class CacheFileEditorInput implements IPathEditorInput, ILocationProvider
{

   private class WorkbenchAdapter implements IWorkbenchAdapter
   {
      /*
       * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
       */
      public Object[] getChildren(Object o)
      {
         return null;
      }

      /*
       * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
       */
      public ImageDescriptor getImageDescriptor(Object object)
      {
         return null;
      }

      /*
       * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
       */
      public String getLabel(Object o)
      {
         return ((CacheFileEditorInput) o).getName();
      }

      /*
       * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
       */
      public Object getParent(Object o)
      {
         return null;
      }
   }

   private File fFile;

   private WorkbenchAdapter fWorkbenchAdapter = new WorkbenchAdapter();

   public CacheFileEditorInput(File file)
   {
      super();
      fFile = file;
      fWorkbenchAdapter = new WorkbenchAdapter();
   }

   /*
    * @see org.eclipse.ui.IEditorInput#exists()
    */
   public boolean exists()
   {
      return fFile.exists();
   }

   /*
    * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
    */
   public ImageDescriptor getImageDescriptor()
   {
      return null;
   }

   /*
    * @see org.eclipse.ui.IEditorInput#getName()
    */
   public String getName()
   {
      return fFile.getName();
   }

   /*
    * @see org.eclipse.ui.IEditorInput#getPersistable()
    */
   public IPersistableElement getPersistable()
   {
      return null;
   }

   /*
    * @see org.eclipse.ui.IEditorInput#getToolTipText()
    */
   public String getToolTipText()
   {
      return fFile.getAbsolutePath();
   }

   /*
    * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
    */
   public Object getAdapter(Class adapter)
   {
      if (ILocationProvider.class.equals(adapter))
         return this;
      if (IWorkbenchAdapter.class.equals(adapter))
         return fWorkbenchAdapter;
      return Platform.getAdapterManager().getAdapter(this, adapter);
   }

   /*
    * @see org.eclipse.ui.editors.text.ILocationProvider#getPath(java.lang.Object)
    */
   public IPath getPath(Object element)
   {
      if (element instanceof CacheFileEditorInput)
      {
         CacheFileEditorInput input = (CacheFileEditorInput) element;
         return Path.fromOSString(input.fFile.getAbsolutePath());
      }
      return null;
   }

   /*
    * @see org.eclipse.ui.IPathEditorInput#getPath()
    * @since 3.1
    */
   public IPath getPath()
   {
      return Path.fromOSString(fFile.getAbsolutePath());
   }

   /*
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object o)
   {
      if (o == this)
         return true;

      if (o instanceof CacheFileEditorInput)
      {
         CacheFileEditorInput input = (CacheFileEditorInput) o;
         return fFile.equals(input.fFile);
      }

      if (o instanceof IPathEditorInput)
      {
         IPathEditorInput input = (IPathEditorInput) o;
         return getPath().equals(input.getPath());
      }

      return false;
   }

   /*
    * @see java.lang.Object#hashCode()
    */
   public int hashCode()
   {
      return fFile.hashCode();
   }
}
