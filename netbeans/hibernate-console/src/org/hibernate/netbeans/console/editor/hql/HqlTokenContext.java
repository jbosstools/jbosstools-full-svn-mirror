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

import org.netbeans.editor.BaseTokenID;
import org.netbeans.editor.TokenContext;
import org.netbeans.editor.TokenContextPath;
import org.netbeans.editor.Utilities;

/**
 * @author leon
 */
public class HqlTokenContext extends TokenContext {

    public static final int HQL_STRING_ID = 1;
    public static final int JAVA_STRING_ID = HQL_STRING_ID + 1;
    public static final int KEYWORD_ID = JAVA_STRING_ID + 1;
    public static final int SELECT_ID = KEYWORD_ID + 1;
    public static final int FROM_ID = SELECT_ID + 1;
    public static final int WHERE_ID = FROM_ID + 1;
    public static final int LEFT_ID = WHERE_ID + 1;
    public static final int RIGHT_ID = LEFT_ID + 1;
    public static final int JOIN_ID = RIGHT_ID + 1;
    public static final int OUTER_ID = JOIN_ID + 1;
    public static final int INNER_ID = OUTER_ID + 1;
    public static final int DEFAULT_ID = INNER_ID + 1;
    public static final int WHITESPACE_ID = DEFAULT_ID + 1;
    public static final int BROKEN_HQL_STRING_ID = WHITESPACE_ID + 1;
    public static final int BROKEN_JAVA_STRING_ID = BROKEN_HQL_STRING_ID + 1;
    public static final int LEFT_PAREN_ID = BROKEN_HQL_STRING_ID + 1;
    public static final int RIGHT_PAREN_ID = LEFT_PAREN_ID + 1;
    public static final int GROUP_ID = RIGHT_PAREN_ID + 1;
    public static final int HAVING_ID = GROUP_ID + 1;
    public static final int UPDATE_ID = HAVING_ID + 1;
    public static final int DELETE_ID = UPDATE_ID + 1;
    public static final int SET_ID = DELETE_ID + 1;
    public static final int ORDER_ID = SET_ID + 1;

    public static final BaseTokenID HQL_STRING = new BaseTokenID("hql-string", HQL_STRING_ID);
    public static final BaseTokenID BROKEN_HQL_STRING = new BaseTokenID("broken-hql-string", BROKEN_HQL_STRING_ID);
    public static final BaseTokenID JAVA_STRING = new BaseTokenID("java-string", JAVA_STRING_ID);
    public static final BaseTokenID BROKEN_JAVA_STRING = new BaseTokenID("broken-java-string", BROKEN_JAVA_STRING_ID);
    public static final BaseTokenID KEYWORD = new BaseTokenID("keyword", KEYWORD_ID);
    public static final BaseTokenID SELECT = new BaseTokenID("keyword", SELECT_ID);
    public static final BaseTokenID FROM = new BaseTokenID("keyword", FROM_ID);
    public static final BaseTokenID WHERE = new BaseTokenID("keyword", WHERE_ID);
    public static final BaseTokenID LEFT = new BaseTokenID("keyword", LEFT_ID);
    public static final BaseTokenID RIGHT = new BaseTokenID("keyword", RIGHT_ID);
    public static final BaseTokenID JOIN = new BaseTokenID("keyword", JOIN_ID);
    public static final BaseTokenID OUTER = new BaseTokenID("keyword", OUTER_ID);
    public static final BaseTokenID INNER = new BaseTokenID("keyword", INNER_ID);
    public static final BaseTokenID DEFAULT = new BaseTokenID("default", DEFAULT_ID);
    public static final BaseTokenID WHITESPACE = new BaseTokenID("default", WHITESPACE_ID);
    public static final BaseTokenID LEFT_PAREN = new BaseTokenID("default", LEFT_PAREN_ID);
    public static final BaseTokenID RIGHT_PAREN = new BaseTokenID("default", RIGHT_PAREN_ID);
    public static final BaseTokenID GROUP = new BaseTokenID("keyword", GROUP_ID);
    public static final BaseTokenID HAVING = new BaseTokenID("keyword", HAVING_ID);
    public static final BaseTokenID UPDATE = new BaseTokenID("keyword", UPDATE_ID);
    public static final BaseTokenID DELETE = new BaseTokenID("keyword", DELETE_ID);
    public static final BaseTokenID SET = new BaseTokenID("keyword", SET_ID);
    public static final BaseTokenID ORDER = new BaseTokenID("keyword", ORDER_ID);
    
    public static final HqlTokenContext CONTEXT = new HqlTokenContext();

    public static final TokenContextPath CONTEXT_PATH = CONTEXT.getContextPath();

    
    private HqlTokenContext() {
        super("hql-");
        try {
            addDeclaredTokenIDs();
        } catch (Exception e) {
            Utilities.annotateLoggable(e);
        }
    }
}
