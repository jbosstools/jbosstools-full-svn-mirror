package org.hibernate.netbeans.console.model;

import org.hibernate.netbeans.console.editor.hql.completion.ModelCompletionTest;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;

/**
 * @author leon
 */
public class Model {
    
    private Model() {
    }

    public static SessionFactory buildSessionFactory() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
        cfg.addInputStream(
                ModelCompletionTest.class.getResourceAsStream("org/hibernate/netbeans/console/model/Product.hbm.xml")).
                addInputStream(ModelCompletionTest.class.getResourceAsStream("org/hibernate/netbeans/console/model/Store.hbm.xml"));
        return cfg.buildSessionFactory();
    }
    
}
