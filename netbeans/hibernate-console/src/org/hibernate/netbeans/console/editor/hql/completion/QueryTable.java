package org.hibernate.netbeans.console.editor.hql.completion;

import org.hibernate.mapping.PersistentClass;

/**
 * @author leon
 */
public class QueryTable {

    private String alias;

    private String type;

    public QueryTable(String type, String alias) {
        this.type = type;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return alias + ":" + type;
    }



}
