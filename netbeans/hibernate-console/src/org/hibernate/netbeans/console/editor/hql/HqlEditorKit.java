package org.hibernate.netbeans.console.editor.hql;

import javax.swing.text.Document;
import org.netbeans.editor.Syntax;
import org.netbeans.modules.editor.plain.PlainKit;

/**
 * @author leon
 */
public class HqlEditorKit extends PlainKit {
    
    public static final String MIME_TYPE = "text/x-hql"; // NOI18N

    public HqlEditorKit() {
    }

    public String getContentType() {
        return MIME_TYPE;
    }

    public Syntax createSyntax(Document doc) {
        return new HqlSyntax();
    }
    

}
