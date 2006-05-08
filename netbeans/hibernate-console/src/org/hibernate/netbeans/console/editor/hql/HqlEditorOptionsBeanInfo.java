package org.hibernate.netbeans.console.editor.hql;

import java.util.MissingResourceException;
import org.netbeans.modules.editor.options.BaseOptionsBeanInfo;
import org.netbeans.modules.editor.options.OptionSupport;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class HqlEditorOptionsBeanInfo extends BaseOptionsBeanInfo {
    
    public HqlEditorOptionsBeanInfo() {
        super("/org/hibernate/netbeans/console/resources/hqlEditorOptions"); // NOI18N
    }
    
    protected String[] getPropNames() {
        return OptionSupport.mergeStringArrays(
                super.getPropNames(),
                HqlEditorOptions.HQL_PROP_NAMES);
    }
    
    protected Class getBeanClass() {
        return HqlEditorOptions.class;
    }
    
    protected String getString(String key) {
        try {
            return NbBundle.getMessage(HqlEditorOptionsBeanInfo.class, key);
        } catch (MissingResourceException e) {
            return super.getString(key);
        }
    }
}
