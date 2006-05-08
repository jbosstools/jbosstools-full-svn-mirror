package org.hibernate.netbeans.console.output.sql;

import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.OutputMultiViewDescription;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class SqlMultiViewDescription extends OutputMultiViewDescription {
    
    private SessionFactoryDescriptor descriptor;
    
    public SqlMultiViewDescription(SessionFactoryDescriptor descr) {
        this.descriptor = descr;
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    public String getDisplayName() {
        return "SQL";
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return "SQL_" + descriptor.getStorageFile().getName();
    }

    public OutputMultiViewElement createOutputElement() {
        return new SqlMultiViewElement(descriptor);
    }
    
}
