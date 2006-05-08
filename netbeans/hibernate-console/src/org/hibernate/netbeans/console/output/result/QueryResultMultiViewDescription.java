package org.hibernate.netbeans.console.output.result;

import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.OutputMultiViewDescription;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class QueryResultMultiViewDescription extends OutputMultiViewDescription {
    
    public SessionFactoryDescriptor descriptor;
    
    public QueryResultMultiViewDescription(SessionFactoryDescriptor descr) {
        this.descriptor = descr;
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    public String getDisplayName() {
        return "Result"; // TODO
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return "QueryResult_" + descriptor.getStorageFile().getName();
    }

    public OutputMultiViewElement createOutputElement() {
        return new QueryResultMultiViewElement(descriptor);
    }
    
}
