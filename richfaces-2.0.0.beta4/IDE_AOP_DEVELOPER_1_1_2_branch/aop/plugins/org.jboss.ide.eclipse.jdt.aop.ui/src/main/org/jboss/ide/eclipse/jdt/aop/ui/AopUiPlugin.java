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
package org.jboss.ide.eclipse.jdt.aop.ui;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.ui.SharedImages;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.project.AopProjectNature;
import org.jboss.ide.eclipse.jdt.aop.ui.editors.AopJavaEditorUtils;
import org.osgi.framework.BundleContext;

/**
 * The ui plugin class to be used eclipse
 */
public class AopUiPlugin extends AbstractUIPlugin
{
   private static AopUiPlugin plugin;

   public static final String ADVISED_MARKER = "org.jboss.ide.eclipse.jdt.aop.ui.advisedmarker";

   public static final String ADVICE_MARKER = "org.jboss.ide.eclipse.jdt.aop.ui.advicemarker";

   public static final String INTERCEPTOR_MARKER = "org.jboss.ide.eclipse.jdt.aop.ui.interceptormarker";

   public static final String TYPEDEF_MARKER = "org.jboss.ide.eclipse.jdt.aop.ui.typedefmarker";

   public static final String INTRODUCTION_MARKER = "org.jboss.ide.eclipse.jdt.aop.ui.introductionmarker";

   public AopUiPlugin()
   {
      super();
      plugin = this;
   }

   public void start(BundleContext context) throws Exception
   {
      super.start(context);

      // force initialization
      AopJavaEditorUtils.instance();
      AopCorePlugin.getDefault();

      ResourcesPlugin.getWorkspace().addResourceChangeListener(new DescriptorChangeListener(),
            IResourceChangeEvent.POST_BUILD);

      IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();

      for (int i = 0; i < projects.length; i++)
      {
         if (projects[i].isAccessible())
         {
            if (projects[i].hasNature(AopProjectNature.NATURE_ID))
            {
               IJavaProject javaProject = JavaCore.create(projects[i]);

               try
               {
                  AopCorePlugin.getDefault().initModel(javaProject, new NullProgressMonitor());
               }
               catch (Exception e)
               {
                  e.printStackTrace();
               }
            }
         }
      }

      // Don't block eclipse's startup..
      //		Job job = new Job ("Initializing JBossAOP Model...") {
      //			protected IStatus run (final IProgressMonitor monitor) {
      //				Display.getDefault().asyncExec(new Runnable () {
      //					public void run () {
      //						try {
      //							monitor.beginTask("Finding AOP Projects...", IProgressMonitor.UNKNOWN);
      //
      //							monitor.done();
      //						} catch (CoreException e) {
      //							e.printStackTrace();
      //						}
      //					}
      //				});
      //				return Status.OK_STATUS;
      //			}
      //		};
      //		
      //		job.schedule();

   }

   /**
    * Returns the shared instance.
    */
   public static AopUiPlugin getDefault()
   {
      return plugin;
   }

   public String getTreeLabel(IJavaElement element)
   {
      try
      {
         if (element.getElementType() == IJavaElement.METHOD)
         {
            IMethod method = (IMethod) element;

            String methodLabel = Signature.toString(method.getReturnType()) + " ";
            methodLabel += method.getParent().getElementName() + "." + method.getElementName() + "(";
            String paramNames[] = method.getParameterNames();
            String paramTypes[] = method.getParameterTypes();

            for (int i = 0; i < paramNames.length; i++)
            {
               methodLabel += Signature.toString(paramTypes[i]);
               methodLabel += " " + paramNames[i];

               if (i < paramNames.length - 1)
               {
                  methodLabel += ", ";
               }
            }
            methodLabel += ")";
            return methodLabel;
         }
         else if (element.getElementType() == IJavaElement.FIELD)
         {
            IField field = (IField) element;
            String fieldLabel = Signature.toString(field.getTypeSignature()) + " " + field.getParent().getElementName()
                  + "." + field.getElementName();

            return fieldLabel;
         }
         else
            return element.getElementName();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

   public Image getTreeImage(IJavaElement element)
   {
      try
      {
         switch (element.getElementType())
         {
            case IType.METHOD :
               return JavaElementImageProvider.getMethodImageDescriptor(false, ((IMethod) element).getFlags())
                     .createImage();

            case IType.FIELD :
               return JavaElementImageProvider.getFieldImageDescriptor(false, ((IField) element).getFlags())
                     .createImage();

            case IType.CLASS_FILE :
            case IType.COMPILATION_UNIT :
               return JavaUI.getSharedImages().getImage(SharedImages.IMG_OBJS_CLASS);
            case IType.TYPE :
               return JavaUI.getSharedImages().getImage(SharedImages.IMG_OBJS_INNER_CLASS_DEFAULT);
         }

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return null;
   }

   public static void alert(String string)
   {
      MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
            "JBossIDE/AOP - Alert", Display.getDefault().getSystemImage(SWT.ICON_INFORMATION), string,
            MessageDialog.INFORMATION, new String[]
            {"OK",}, 0);

      dialog.setBlockOnOpen(true);

      dialog.open();
   }

   public static boolean confirm(String string)
   {
      MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
            "JBossIDE/AOP - Confirm", Display.getDefault().getSystemImage(SWT.ICON_QUESTION), string,
            MessageDialog.QUESTION, new String[]
            {"Yes", "No"}, 0);

      dialog.setBlockOnOpen(true);
      int response = dialog.open();

      return response == 0;
   }

   public ISourceRange getSourceRange(IJavaElement element) throws CoreException
   {
      ISourceRange elementRange = null;

      if (element instanceof IMethod)
      {
         IMethod method = (IMethod) element;
         elementRange = method.getNameRange();
      }
      else if (element instanceof IField)
      {
         IField field = (IField) element;
         elementRange = field.getNameRange();
      }
      else if (element instanceof IType)
      {
         IType type = (IType) element;
         elementRange = type.getNameRange();
      }
      else if (element instanceof ISourceReference)
      {
         ISourceReference ref = (ISourceReference) element;
         elementRange = ref.getSourceRange();
      }

      return elementRange;
   }

   public IJavaElement findMarkerElement(IMarker marker) throws CoreException
   {
      int offset = ((Integer) marker.getAttribute(IMarker.CHAR_START)).intValue();

      ICompilationUnit element = (ICompilationUnit) JavaCore.create(marker.getResource());

      IType types[] = element.getAllTypes();
      for (int i = 0; i < types.length; i++)
      {
         IJavaElement children[] = types[i].getChildren();
         for (int j = 0; j < children.length; j++)
         {
            IJavaElement child = children[j];

            if (getSourceRange(child).getOffset() == offset)
            {
               return child;
            }
         }
         if (getSourceRange(types[i]).getOffset() == offset)
         {
            return types[i];
         }
      }

      return null;
   }

   public IMarker findElementMarker(IJavaElement element, String markerType) throws CoreException
   {
      IMarker markers[] = element.getResource().findMarkers(markerType, true, IResource.DEPTH_INFINITE);

      for (int i = 0; i < markers.length; i++)
      {
         IMarker marker = markers[i];
         int offset = ((Integer) marker.getAttribute(IMarker.CHAR_START)).intValue();

         if (getSourceRange(element).getOffset() == offset)
         {
            return marker;
         }
      }

      return null;
   }

}