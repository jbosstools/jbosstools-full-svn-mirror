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
     * Gets the singleton instance.
     * 
     * @return the singleton instance
     */
    public static IELService getInstance(){
        return INSTANCE;
    }

    /**
     * 
     * @see IELService#getReplacedElValue(IFile, String)
     */
    public String replaceEl(IFile resourceFile, String resourceString) {
        Assert.isNotNull(resourceString);
        Assert.isNotNull(resourceFile);
        String rst = resourceString;
        
        final ResourceReference[] references = ELReferenceList.getInstance().getAllResources(resourceFile);
        
        if((references!=null) && (references.length > 0)){
            rst = replace(resourceString,references);
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
            if (resourceString.contains(rf.getProperties())) {
                result = result.replace(rf.getProperties(), rf.getLocation());
            }
        }
        return result;
    }
    
    
    
    
}
