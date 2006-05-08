package org.hibernate.netbeans.console.output.oracle;

import java.awt.Image;
import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.OutputMultiViewDescription;
import org.hibernate.netbeans.console.output.OutputMultiViewElement;
import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class ExplainPlanMultiViewDescription extends OutputMultiViewDescription {
    
    private SessionFactoryDescriptor descr;
    
    public ExplainPlanMultiViewDescription(SessionFactoryDescriptor descr) {
        this.descr = descr;
    }

    public OutputMultiViewElement createOutputElement() {
        return new ExplainPlanMultiViewElement(descr);
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    public String getDisplayName() {
        return "Execution Plan";
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return "SFPlan";
    }
    
}
