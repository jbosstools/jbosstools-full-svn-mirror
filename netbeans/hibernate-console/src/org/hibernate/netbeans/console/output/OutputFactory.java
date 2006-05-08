package org.hibernate.netbeans.console.output;

import org.hibernate.netbeans.console.SessionFactoryDescriptor;
import org.hibernate.netbeans.console.output.oracle.ExplainPlanMultiViewDescription;

/**
 * @author leon
 */
public class OutputFactory {

    private final static OutputMultiViewDescription[] EMPTY_ARRAY = new OutputMultiViewDescription[0];
    
    private OutputFactory() {
    }
    
    public static OutputMultiViewDescription[] createDescription(SessionFactoryDescriptor descr) {
        if ("org.hibernate.dialect.Oracle9Dialect".equals(descr.getHibernateDialect())) {
            OutputMultiViewDescription[] descrs = new OutputMultiViewDescription[1];
            descrs[0] = new ExplainPlanMultiViewDescription(descr);
            return descrs;
        }
        return EMPTY_ARRAY;
    }
    
}
