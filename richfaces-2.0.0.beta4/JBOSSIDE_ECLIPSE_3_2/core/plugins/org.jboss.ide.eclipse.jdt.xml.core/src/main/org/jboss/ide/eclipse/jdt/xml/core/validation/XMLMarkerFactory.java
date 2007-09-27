/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.core.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Factory used to create/delete XML markers on a given resource.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLMarkerFactory
{

   /**Constructor for the XMLMarkerFactory object */
   private XMLMarkerFactory() { }


   /**
    * Description of the Method
    *
    * @param file               Description of the Parameter
    * @return                   Description of the Return Value
    * @exception CoreException  Description of the Exception
    */
   public static IMarker createMarker(IFile file)
      throws CoreException
   {
      IMarker marker = file.createMarker(IMarker.PROBLEM);
      return marker;
   }


   /**
    * Description of the Method
    *
    * @param file               Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public static void deleteMarkers(IFile file)
      throws CoreException
   {
      file.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
   }
}
