/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class MethodWizard extends ClassFragmentWizard
{
   /** Description of the Field */
   protected MethodWizardPage page;
   /** Description of the Field */
   protected static Map RETURNS = new HashMap();


   /**Constructor for the MethodWizard object */
   public MethodWizard()
   {
      this.setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWMETH);
      this.setWindowTitle(JDTUIMessages.getString("MethodWizard.window.title"));//$NON-NLS-1$
   }


   /** Adds a feature to the Pages attribute of the MethodWizard object */
   public void addPages()
   {
      super.addPages();

      IWorkspace workspace = JavaPlugin.getWorkspace();
      this.page = this.createMethodWizardPage();
      this.addPage(this.page);
      this.page.init(this.getSelection());
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IDOMMethod buildMethod()
   {
      String name = page.getMethodName();
      String type = page.getReturnType();

      IDOMMethod method = this.getDOMFactory().createMethod();
      method.setName(name);
      if ("".equals(type)//$NON-NLS-1$
      )
      {
         method.setReturnType("void");//$NON-NLS-1$
      }
      else
      {
         method.setReturnType(type);
      }
      method.setExceptions(page.getExceptions());
      method.setParameters(page.getParameterTypes(), page.getParameterNames());

      return method;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected String computeReturnClause()
   {
      String result = "";//$NON-NLS-1$
      String type = page.getReturnType();

      if (!"".equals(type)//$NON-NLS-1$
      )
      {
         if (RETURNS.keySet().contains(type))
         {
            result = (String) RETURNS.get(type);
         }
         else
         {
            result = "return null;";//$NON-NLS-1$
         }
      }
      return result;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected abstract MethodWizardPage createMethodWizardPage();

   static
   {
      RETURNS.put("boolean", "return false;");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("byte", "return 0;");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("char", "return '\0';");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("int", "return 0;");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("long", "return 0;");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("short", "return 0;");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("double", "return 0;");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("float", "return 0;");//$NON-NLS-1$ //$NON-NLS-2$
      RETURNS.put("void", "");//$NON-NLS-1$ //$NON-NLS-2$
   }
}
