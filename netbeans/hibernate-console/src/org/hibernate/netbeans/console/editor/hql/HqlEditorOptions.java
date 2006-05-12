/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
