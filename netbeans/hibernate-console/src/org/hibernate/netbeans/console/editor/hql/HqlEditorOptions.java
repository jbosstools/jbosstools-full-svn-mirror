package org.hibernate.netbeans.console.editor.hql;

import java.util.MissingResourceException;
import org.netbeans.modules.editor.options.BaseOptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class HqlEditorOptions extends BaseOptions {
    
    public static String HQL = "Hql"; // NOI18N
    
    private static final String HELP_ID = "editing.editor.hql"; // NOI18N
    
    // no hql specific options at this time
    static final String[] HQL_PROP_NAMES = new String[] {};
    
    
    public HqlEditorOptions() {
        super(HqlEditorKit.class, HQL);
    }
    
    public HelpCtx getHelpCtx() {
        return new HelpCtx(HELP_ID);
    }
    
    protected String getString(String key) {
        try {
            return NbBundle.getMessage(HqlEditorOptions.class, key);
        } catch (MissingResourceException e) {
            return super.getString(key);
        }
    }
    
}
