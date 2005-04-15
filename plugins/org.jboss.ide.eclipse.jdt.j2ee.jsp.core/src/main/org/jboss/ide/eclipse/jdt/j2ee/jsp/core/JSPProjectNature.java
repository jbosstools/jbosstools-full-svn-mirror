/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * JSP Nature that installs the JSP builder.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPProjectNature implements IProjectNature
{
   private IProject project;


   /**Constructor for the JSPProjectNature object */
   public JSPProjectNature()
   {
      super();
   }


   /**
    * Perform the JSP builder installation.
    *
    * @exception CoreException  Description of the Exception
    */
   public void configure()
      throws CoreException
   {
      // Add the JSP Builder right after the Java Builder
      IProjectDescription desc = this.project.getDescription();
      ICommand[] commands = desc.getBuildSpec();
      boolean found = false;

      for (int i = 0; i < commands.length; ++i)
      {
         if (commands[i].getBuilderName().equals(JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID))
         {
            found = true;
            break;
         }
      }
      if (!found)
      {
         //add builder to project
         ICommand command = desc.newCommand();
         command.setBuilderName(JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID);
         ICommand[] newCommands = new ICommand[commands.length + 1];

         // Add it after other builders.
         System.arraycopy(commands, 0, newCommands, 0, commands.length);
         newCommands[commands.length] = command;
         desc.setBuildSpec(newCommands);
         this.project.setDescription(desc, new NullProgressMonitor());
      }
   }


   /**
    * Perform the JSP builder removal.
    *
    * @exception CoreException  Description of the Exception
    */
   public void deconfigure()
      throws CoreException
   {
      // Remove the JSP Builder
      IProjectDescription desc = this.project.getDescription();
      ICommand[] commands = desc.getBuildSpec();
      boolean found = false;
      int index = -1;

      for (int i = 0; i < commands.length; ++i)
      {
         if (commands[i].getBuilderName().equals(JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID))
         {
            found = true;
            index = i;
            break;
         }
      }
      if (found && index >= 0)
      {
         ICommand[] newCommands = new ICommand[commands.length - 1];

         // Remove builder to project
         if (index == 0)
         {
            System.arraycopy(commands, 1, newCommands, 0, newCommands.length);
         }
         else if (index == commands.length)
         {
            System.arraycopy(commands, 0, newCommands, 0, newCommands.length);
         }
         else
         {
            System.arraycopy(commands, 0, newCommands, 0, index);
            System.arraycopy(commands, index + 1, newCommands, index, newCommands.length - index);
         }

         desc.setBuildSpec(newCommands);
         this.project.setDescription(desc, new NullProgressMonitor());
      }
   }


   /**
    * Gets the project attribute of the JSPProjectNature object
    *
    * @return   The project value
    */
   public IProject getProject()
   {
      return this.project;
   }


   /**
    * Sets the project attribute of the JSPProjectNature object
    *
    * @param project  The new project value
    */
   public void setProject(IProject project)
   {
      this.project = project;
   }
}
