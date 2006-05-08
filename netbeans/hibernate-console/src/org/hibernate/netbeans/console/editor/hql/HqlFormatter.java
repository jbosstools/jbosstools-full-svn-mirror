package org.hibernate.netbeans.console.editor.hql;

import org.netbeans.editor.Syntax;
import org.netbeans.editor.ext.ExtFormatter;

/**
 * @author leon
 */
public class HqlFormatter extends ExtFormatter {
    
    public HqlFormatter() {
        super(HqlEditorKit.class);
    }

    protected boolean acceptSyntax(Syntax syntax) {
        return syntax instanceof HqlSyntax;
    }

}
