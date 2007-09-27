/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.core.wizards.generation;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DefaultGenerationEngine implements IGenerationEngine
{
   /**Constructor for the DefaultGenerationEngine object */
   public DefaultGenerationEngine() { }


   /**
    * Description of the Method
    *
    * @param type                    Description of the Parameter
    * @param monitor                 Description of the Parameter
    * @exception JavaModelException  Description of the Exception
    */
   public void generate(IType type, IProgressMonitor monitor)
      throws JavaModelException { }
}
