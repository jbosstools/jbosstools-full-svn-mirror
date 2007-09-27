/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import org.netbeans.editor.BaseTokenID;
import org.netbeans.editor.Syntax;
import org.netbeans.editor.TokenID;

/**
 * @author leon
 */
public class HqlSyntax extends Syntax {
    
    private static final HashSet<String> KEYWORDS;

    private static final Map<String, BaseTokenID> KEYWORDS_2_TOKENS;
    
    static {
        String str = "all,any,and,as,asc,avg,between,by,class,count,delete,desc,distinct,elements,escape,exists," +
                "false,fetch,from,full,group,having,in,indices,inner,is,join,left,like,max,min,new,not," +
                "null,or,order,outer,properties,right,select,set,some,sum,true,union,update,where,with," +
                "case,end,else,then,when,on,both,empty,leading,member,object,of,trailing";
        StringTokenizer tok = new StringTokenizer(str, ", ");
        KEYWORDS = new HashSet<String>();
        while (tok.hasMoreTokens()) {
            KEYWORDS.add(tok.nextToken().toLowerCase());
        }

        KEYWORDS_2_TOKENS = new HashMap<String, BaseTokenID>();
        KEYWORDS_2_TOKENS.put("from", HqlTokenContext.FROM);
        KEYWORDS_2_TOKENS.put("where", HqlTokenContext.WHERE);
        KEYWORDS_2_TOKENS.put("left", HqlTokenContext.LEFT);
        KEYWORDS_2_TOKENS.put("right", HqlTokenContext.RIGHT);
        KEYWORDS_2_TOKENS.put("join", HqlTokenContext.JOIN);
        KEYWORDS_2_TOKENS.put("inner", HqlTokenContext.INNER);
        KEYWORDS_2_TOKENS.put("outer", HqlTokenContext.OUTER);
        KEYWORDS_2_TOKENS.put("select", HqlTokenContext.SELECT);
        KEYWORDS_2_TOKENS.put("having", HqlTokenContext.HAVING);
        KEYWORDS_2_TOKENS.put("group", HqlTokenContext.GROUP);
        KEYWORDS_2_TOKENS.put("update", HqlTokenContext.UPDATE);
        KEYWORDS_2_TOKENS.put("delete", HqlTokenContext.DELETE);
        KEYWORDS_2_TOKENS.put("set", HqlTokenContext.SET);
        KEYWORDS_2_TOKENS.put("order", HqlTokenContext.ORDER);
    }

    private static final int ISI_HQL_STRING = 1;
    private static final int ISI_JAVA_STRING = ISI_HQL_STRING + 1;
    private static final int ISI_KEYWORD = ISI_JAVA_STRING + 1;
    private static final int ISI_BROKEN_JAVA_STRING = ISI_KEYWORD + 1;
    private static final int ISI_BROKEN_HQL_STRING = ISI_BROKEN_JAVA_STRING + 1;
    private static final int ISI_WHITESPACE = ISI_BROKEN_HQL_STRING + 1;

    public HqlSyntax() {
        tokenContextPath = HqlTokenContext.CONTEXT_PATH;
    }
    
    protected TokenID parseToken() {
        TokenID result = doParseToken();
        return result;
    }
    
    private TokenID doParseToken() {
        char actChar;
        TokenID retValue = null;
        int backSlashCount = 0;
        while (offset < stopOffset) {
            if (offset >= buffer.length) {
                // XXX - This should not happen, but somehow it's the case
                offset = stopOffset;
                return HqlTokenContext.DEFAULT;
            }
            actChar = buffer[offset];
            retValue = null;
            switch (state) {
                case INIT:
                    // <editor-fold>
                    switch (actChar) {
                        case '"':
                            state = ISI_JAVA_STRING;
                            backSlashCount = 0;
                            break;
                        case '\'':
                            state = ISI_HQL_STRING;
                            break;
                        case '\n':
                        case '\r':
                        case '\t':
                        case ' ':
                            state = ISI_WHITESPACE;
                            break;
                        default:
                            if (Character.isJavaIdentifierPart(actChar)) {
                                state = ISI_KEYWORD;
                            } else {
                                offset++;
                                state = INIT;
                                if (actChar == '(') {
                                    retValue = HqlTokenContext.LEFT_PAREN;
                                } else if (actChar == ')') {
                                    retValue = HqlTokenContext.RIGHT_PAREN;
                                } else {
                                    retValue = HqlTokenContext.DEFAULT;
                                }
                                break;
                            }
                    }
                    break;
                    // </editor-fold>
                case ISI_JAVA_STRING:
                    // <editor-fold>
                    switch (actChar) {
                        case '"':
                            if (backSlashCount % 2 == 0) {
                                offset++;
                                state = INIT;
                                retValue = HqlTokenContext.JAVA_STRING;
                            } else {
                                backSlashCount = 0;
                            }
                            break;
                        case '\\':
                            backSlashCount++;
                            break;
                        case '\n':
                        case '\r':
                            state = ISI_BROKEN_JAVA_STRING;
                            break;
                    }
                    break;
                    // </editor-fold>
                case ISI_HQL_STRING:
                    // <editor-fold>
                    switch (actChar) {
                        case '\'':
                            offset++;
                            state = INIT;
                            retValue = HqlTokenContext.HQL_STRING;
                            break;
                        case '\n':
                        case '\r':
                            state = ISI_BROKEN_HQL_STRING;
                            break;
                    }
                    break;
                    // </editor-fold>
                case ISI_KEYWORD:
                    // <editor-fold>
                    String word = getWordAtOffset(offset).toLowerCase();
                    if (KEYWORDS.contains(word) &&
                            (((offset < stopOffset - 1 && (Character.isWhitespace(buffer[offset + 1]) ||
                            buffer[offset + 1] == '(' || buffer[offset + 1] == ')'))) ||
                            (offset == stopOffset - 1))) {
                        state = INIT;
                        offset++;
                        retValue = KEYWORDS_2_TOKENS.get(word);
                        if (retValue == null) {
                            retValue = HqlTokenContext.KEYWORD;
                        }
                        break;
                    } else if (Character.isWhitespace(actChar)) {
                        state = ISI_WHITESPACE;
                        retValue = HqlTokenContext.DEFAULT;
                        break;
                    } else if (!Character.isLetterOrDigit(actChar) && actChar != '.' && actChar != '_') {
                        state = INIT;
                        retValue = HqlTokenContext.DEFAULT;
                        break;
                    }
                    break;
                    // </editor-fold>
                case ISI_BROKEN_JAVA_STRING:
                    // <editor-fold>
                    if (actChar == '"') {
                        state = INIT;
                        offset++;
                        retValue = HqlTokenContext.BROKEN_JAVA_STRING;
                        break;
                    }
                    break;
                    // </editor-fold>
                case ISI_BROKEN_HQL_STRING:
                    // <editor-fold>
                    if (actChar == '\'') {
                        offset++;
                        state = INIT;
                        retValue = HqlTokenContext.BROKEN_HQL_STRING;
                        break;
                    }
                    break;
                    // </editor-fold>
                case ISI_WHITESPACE:
                    // <editor-fold>
                    if (!Character.isWhitespace(actChar)) {
                        state = INIT;
                        retValue = HqlTokenContext.WHITESPACE;
                    }
                    break;
                    // </editor-fold>
            }
            if (retValue == null) {
                offset++;
            } else {
                return retValue;
            }
        }
        return HqlTokenContext.DEFAULT;
        
    }

    private String getWordAtOffset(int offset) {
        return new String(buffer, tokenOffset, offset - tokenOffset + 1);
    }
    
}
