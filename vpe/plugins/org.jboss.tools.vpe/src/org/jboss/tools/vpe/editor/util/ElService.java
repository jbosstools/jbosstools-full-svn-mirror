

package org.jboss.tools.vpe.editor.util;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.jboss.tools.vpe.editor.css.ELReferenceList;
import org.jboss.tools.vpe.editor.css.ResourceReference;


/**
 * The {@link IELService} implementation.
 * 
 * @author Eugeny Stherbin
 */
public final class ElService implements IELService {

    /** The Constant INSTANCE. */
    private static final ElService INSTANCE = new ElService();

    /**
     * Checks if is available.
     * 
     * @param resourceFile the resource file
     * 
     * @return true, if is available
     */
    public boolean isAvailable(IFile resourceFile) {
        boolean rst = false;
        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);

        if ((references != null) && (references.length > 0)) {
            rst = true;
        }
        return rst;
    }

    /**
     * Gets the singleton instance.
     * 
     * @return the singleton instance
     */
    public static IELService getInstance() {
        return INSTANCE;
    }

    /**
     * Replace el.
     * 
     * @param resourceFile the resource file
     * @param resourceString the resource string
     * 
     * @return the string
     * 
     * @see IELService#getReplacedElValue(IFile, String)
     */
    public String replaceEl(IFile resourceFile, String resourceString) {
     //   Assert.isNotNull(resourceString);
        if(resourceString == null){
            return "";
        }
        Assert.isNotNull(resourceFile);
        String rst = resourceString;
    
        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);

        if ((references != null) && (references.length > 0)) {
            rst = replace(resourceString, references);
        }
        return rst;
    }

    /**
     * Replace.
     * 
     * @param resourceString the resource string
     * @param references the references
     * 
     * @return the string
     */
    private String replace(String resourceString, ResourceReference[] references) {
        String result = resourceString;

        for (ResourceReference rf : references) {
            if (resourceString.contains(rf.getLocation())) {
                result = result.replace(rf.getLocation(),rf.getProperties());
            }
        }
        return result;
    }

}
