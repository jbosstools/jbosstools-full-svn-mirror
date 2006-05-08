package org.hibernate.netbeans.console.output.oracle;

/**
 * @author leon
 */
public class ExplainPlanNode {

    private String operation;

    private String options;

    private String objectName;

    public ExplainPlanNode(String operation, String options, String objectName) {
        this.operation = operation;
        this.options = options;
        this.objectName = objectName;
    }

    public String toString() {
        String strOptions = "";
        if (options != null) {
            strOptions += " " + options;
        }
        if (objectName != null) {
            strOptions += " " + objectName;
        }
        return operation + strOptions;
    }


    
}
