package org.hibernate.netbeans.console.editor.hql;

import java.io.Serializable;
import org.hibernate.netbeans.console.editor.EditorContentAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class Hql2JavaStringAction extends EditorContentAction implements Serializable {

    private final static long serialVersionUID = 1;

    public String getName() {
        return NbBundle.getMessage(Hql2JavaStringAction.class, "CTL_ConvertHql2Java");
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    protected void performAction(HqlEditorCookie c, String query) {
        c.convertHql2Java();
    }
    
}
