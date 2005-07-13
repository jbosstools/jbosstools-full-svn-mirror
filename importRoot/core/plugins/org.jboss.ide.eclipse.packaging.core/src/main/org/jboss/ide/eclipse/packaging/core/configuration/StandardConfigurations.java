/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.packaging.core.configuration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.packaging.core.PackagingCorePlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class StandardConfigurations extends Configurations
{
   /** Constructor for the StandardConfigurations object */
   public StandardConfigurations()
   {
      try
      {
         this.load();
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Can't load the generics configurations", ce);//$NON-NLS-1$
      }
   }


   /**
    * Description of the Method
    *
    * @exception CoreException  Description of the Exception
    */
   public void loadConfigurations()
      throws CoreException
   {
      this.load();
   }


   /**
    * Gets the contents attribute of the StandardConfigurations object
    *
    * @return                   The contents value
    * @exception CoreException  Description of the Exception
    */
   protected InputStream getContents()
      throws CoreException
   {
      try
      {
         URL genericsFile = PackagingCorePlugin.getDefault().find(new Path(PackagingCorePlugin.GENERICS_FILE));
         InputStream is = new BufferedInputStream(genericsFile.openStream());
         return is;
      }
      catch (IOException ioe)
      {
         throw AbstractPlugin.wrapException(ioe);
      }
   }
}
