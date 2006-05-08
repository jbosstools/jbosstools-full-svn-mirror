package org.hibernate.netbeans.console.util;

import org.openide.util.RequestProcessor;

/**
 * @author leon
 */
public class HibernateExecutor {
    
    private static RequestProcessor hibernateProcessor = new RequestProcessor("Hibernate", 1);
    
    private HibernateExecutor() {
    }
    
    public static void execute(Runnable runnable) {
        hibernateProcessor.post(runnable);
    }
    
}
