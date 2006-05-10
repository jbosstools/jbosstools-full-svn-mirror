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
                Model.class.getResourceAsStream("Product.hbm.xml")).
                addInputStream(Model.class.getResourceAsStream("Store.hbm.xml")).
                addInputStream(Model.class.getResourceAsStream("ProductOwnerAddress.hbm.xml")).
                addInputStream(Model.class.getResourceAsStream("StoreCity.hbm.xml"));
        return cfg.buildSessionFactory();
    }
    
}
