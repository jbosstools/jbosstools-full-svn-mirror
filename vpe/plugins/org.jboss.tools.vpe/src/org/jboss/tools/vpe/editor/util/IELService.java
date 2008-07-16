

package org.jboss.tools.vpe.editor.util;


import org.eclipse.core.resources.IFile;


// TODO: Auto-generated Javadoc
/**
 * The service that substitute El values in vpe source editor.
 * For more details see issues
 * http://jira.jboss.com/jira/browse/JBIDE-2010
 * and
 * http://jira.jboss.com/jira/browse/JBIDE-1410
 * 
 * @author Eugeny Stherbin
 */
public interface IELService {

    /**
     * Return the {@link String} with substitued el values for given resource.
     * 
     * @param resourceFile the resource file where resource
     * @param resourceString the source string that will be substitute
     * 
     * @return string where el values was substituted.
     */
    String replaceEl(IFile resourceFile, String resourceString);
    
    
    /**
     * Checks if is available.
     * 
     * @param resourceFile the resource file
     * 
     * @return true, if is available
     */
    boolean isAvailable(IFile resourceFile);

}
