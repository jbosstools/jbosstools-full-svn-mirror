/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.core.wizards.generation;

import java.util.Hashtable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class VelocityGenerationEngine implements IGenerationEngine
{
   private Hashtable context;
   private String templateName;


   /**
    *Constructor for the VelocityGenerationEngine object
    *
    * @param baseDir  Description of the Parameter
    */
   public VelocityGenerationEngine(String baseDir) { }


   /**
    * Description of the Method
    *
    * @param type                    Description of the Parameter
    * @param monitor                 Description of the Parameter
    * @exception JavaModelException  Description of the Exception
    */
   public void generate(IType type, IProgressMonitor monitor)
      throws JavaModelException
   {
      //		ICompilationUnit cu = type.getCompilationUnit();
      //		if (cu.isWorkingCopy()) {
      //			cu = (ICompilationUnit) cu.getOriginalElement();
      //		}
      //
      //		try {
      //			Template template = Velocity.getTemplate(this.templateName);
      //			StringWriter sw = new StringWriter();
      //			VelocityContext vContext = new VelocityContext(this.context);
      //			template.merge(vContext, sw);
      //			cu.getBuffer().setContents(sw.getBuffer().toString());
      //			cu.save(monitor, true);
      //		} catch (Exception e) {
      //			AbstractPlugin.logError("Unable to merge context", e);
      //		}
   }


   /**
    * Sets the context attribute of the VelocityGenerationEngine object
    *
    * @param context  The new context value
    */
   public void setContext(Hashtable context)
   {
      this.context = context;
   }


   /**
    * Sets the template attribute of the VelocityGenerationEngine object
    *
    * @param templateName  The new template value
    */
   public void setTemplate(String templateName)
   {
      this.templateName = templateName;
   }
}
