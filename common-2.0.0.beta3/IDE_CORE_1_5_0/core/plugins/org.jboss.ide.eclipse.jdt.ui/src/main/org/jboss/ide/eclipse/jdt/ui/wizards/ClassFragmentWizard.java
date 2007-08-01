/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICodeFormatter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.jdom.DOMFactory;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.eclipse.jdt.core.jdom.IDOMType;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ClassFragmentWizard extends BaseWizard
{
   /** Description of the Field */
   protected IDOMCompilationUnit domCompUnit;
   /** Description of the Field */
   protected IDOMType domType;
   /** Description of the Field */
   protected IType selectedType;
   private final DOMFactory factory = new DOMFactory();


   /**Constructor for the ClassFragmentWizard object */
   public ClassFragmentWizard()
   {
      this.setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
   }


   /**
    * Description of the Method
    *
    * @param workbench  Description of the Parameter
    * @param selection  Description of the Parameter
    */
   public void init(IWorkbench workbench, IStructuredSelection selection)
   {
      super.init(workbench, selection);

      try
      {
         if ((selection != null) && (selection.getFirstElement() != null) && (selection.getFirstElement() instanceof IType))
         {
            this.selectedType = (IType) selection.getFirstElement();

            DOMFactory factory = new DOMFactory();
            this.domCompUnit = factory.createCompilationUnit(this.selectedType.getCompilationUnit().getSource(), selectedType.getCompilationUnit().getElementName());
            this.domType = factory.createType(this.selectedType.getSource());
            this.domType = (IDOMType) this.domCompUnit.getChild(this.domType.getName());
         }
      }
      catch (Exception e)
      {
         AbstractPlugin.logError("Cannot initialize the selection", e);//$NON-NLS-1$
      }
   }


   /**
    * Adds a feature to the Content attribute of the ClassFragmentWizard object
    *
    * @exception Exception  Description of the Exception
    */
   protected abstract void addContent()
      throws Exception;


   /**
    * Description of the Method
    *
    * @param name            Description of the Parameter
    * @param type            Description of the Parameter
    * @param parameterTypes  Description of the Parameter
    * @param parameterNames  Description of the Parameter
    * @return                Description of the Return Value
    */
   protected IDOMMethod createMethod(String name, String type, String[] parameterTypes, String[] parameterNames)
   {
      IDOMMethod method = this.getDOMFactory().createMethod();
      method.setName(name);
      method.setReturnType(type);
      method.setParameters(parameterTypes, parameterNames);
      method.setFlags(Flags.AccPublic);
      return method;
   }


   /**
    * Description of the Method
    *
    * @param monitor                   Description of the Parameter
    * @exception InterruptedException  Description of the Exception
    * @exception CoreException         Description of the Exception
    */
   protected void finishPage(IProgressMonitor monitor)
      throws InterruptedException, CoreException
   {
      if (this.selectedType != null)
      {
         try
         {
            // Add various class fragment
            this.addContent();

            // Alter the compilation unit
            ICompilationUnit cu = selectedType.getCompilationUnit();
            if (cu.isWorkingCopy())
            {
               cu = (ICompilationUnit) cu.getOriginalElement();
            }

            String newContents = domCompUnit.getContents();

            // Formatting
            ICodeFormatter formatter = ToolFactory.createCodeFormatter();
            newContents = formatter.format(newContents, 0, null, null);

            try
            {
               cu.getBuffer().setContents(newContents);
            }
            catch (Exception e)
            {
               AbstractPlugin.logError("Unable to set compilation unit content", e);//$NON-NLS-1$
            }

            // Save the buffer to the file.
            cu.save(monitor, true);

            // Open if necessary the resource
            IResource resource = cu.getCorrespondingResource();
            this.selectAndReveal(resource);
            this.openResource((IFile) resource);
         }
         catch (Exception e)
         {
            AbstractPlugin.logError("Cannot generate class fragment", e);//$NON-NLS-1$
         }
      }
   }


   /**
    * Gets the dOMFactory attribute of the ClassFragmentWizard object
    *
    * @return   The dOMFactory value
    */
   protected DOMFactory getDOMFactory()
   {
      return this.factory;
   }


   /**
    * Gets the type attribute of the ClassFragmentWizard object
    *
    * @return   The type value
    */
   protected IDOMType getType()
   {
      return this.domType;
   }
}
