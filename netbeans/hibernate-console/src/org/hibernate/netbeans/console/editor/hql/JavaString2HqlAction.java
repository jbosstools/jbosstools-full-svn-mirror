package org.hibernate.netbeans.console.editor.hql;

import java.io.Serializable;
import org.hibernate.netbeans.console.editor.EditorContentAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class JavaString2HqlAction extends EditorContentAction implements Serializable {

    private final static long serialVersionUID = 1;

    public String getName() {
        return NbBundle.getMessage(JavaString2HqlAction.class, "CTL_ConvertJava2Hql");
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    protected void performAction(HqlEditorCookie c, String query) {
        c.convertJava2Hql();
    }
    
}
