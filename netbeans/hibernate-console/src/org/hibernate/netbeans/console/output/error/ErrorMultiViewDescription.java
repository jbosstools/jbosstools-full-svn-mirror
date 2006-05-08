package org.hibernate.netbeans.console.output.error;

import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.OutputMultiViewDescription;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class ErrorMultiViewDescription extends OutputMultiViewDescription {
    
    private SessionFactoryDescriptor descriptor;
    
    public ErrorMultiViewDescription(SessionFactoryDescriptor descr) {
        this.descriptor = descr;
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    public String getDisplayName() {
        return "Error"; // TODO - i18n
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return "SFErrors";
    }

    public OutputMultiViewElement createOutputElement() {
        return new ErrorMultiViewElement(descriptor);
    }
    
}
