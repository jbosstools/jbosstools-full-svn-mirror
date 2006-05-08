package org.hibernate.netbeans.console;

/**
 * @author leon
 */
public class BshCode {

    private Throwable exception;
    
    private String code;
    
    public BshCode(String code) {
        this.code = code;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    public String getCode() {
        return code;
    }
    
}
