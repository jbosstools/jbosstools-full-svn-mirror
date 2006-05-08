package org.hibernate.netbeans.console;

/**
 * @author leon
 */
public class HqlQuery {
    
    private String hql;

    private Throwable exception;
    
    public HqlQuery(String hql) {
        this.hql = hql;
    }

    public String getHql() {
        return hql;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    
}
