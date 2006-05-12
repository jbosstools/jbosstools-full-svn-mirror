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

package org.hibernate.netbeans.console.editor.hql.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.hibernate.netbeans.console.editor.hql.HqlSyntax;
import org.hibernate.netbeans.console.editor.hql.HqlTokenContext;
import org.netbeans.editor.TokenID;

/**
 * @author leon
 */
public class HqlAnalyzer {

    private static HqlSyntax syntax = new HqlSyntax();

    private HqlAnalyzer() {
    }

    public static boolean shouldShowTables(char[] chars, int position) {
        syntax.load(null, chars, 0, position, true, position);
        TokenID tid = null;
        boolean show = false;
        while ((tid = syntax.nextToken()) != null) {
            int numericId = tid.getNumericID();
            if ((numericId == HqlTokenContext.FROM_ID ||
                    numericId == HqlTokenContext.DELETE_ID ||
                    numericId == HqlTokenContext.UPDATE_ID) &&
                    (syntax.getTokenOffset() + syntax.getTokenLength()) < position) {
                show = true;
            } else if (numericId != HqlTokenContext.DEFAULT_ID && numericId != HqlTokenContext.WHITESPACE_ID) {
                show = false;
            }
        }
        return show;
    }

    static List<SubQuery> getVisibleSubQueries(char[] chars, int position) {
        SubQueryList sqList = getSubQueries(chars, position);
        List<SubQuery> visible = new ArrayList<SubQuery>();
        for (SubQuery sq : sqList.subQueries) {
            if (sqList.caretDepth >= sq.depth && (sq.startOffset <= position || sq.endOffset >= position)) {
                visible.add(sq);
            }
        }
        return visible;
    }

    public static List<QueryTable> getVisibleTables(char[] chars, int position) {
        List<SubQuery> sqs = getVisibleSubQueries(chars, position);
        List<QueryTable> tables = new ArrayList<QueryTable>();
        for (SubQuery sq : sqs) {
            tables.addAll(sq.getTables());
        }
        return tables;
    }

    static SubQueryList getSubQueries(char[] chars, int position) {
        syntax.load(null, chars, 0, chars.length, true, chars.length);
        TokenID tid = null;
        List<SubQuery> subQueries = new ArrayList<SubQuery>();
        int depth = 0;
        int caretDepth = 0;
        Map<Integer, SubQuery> level2SubQuery = new HashMap<Integer, SubQuery>();
        SubQuery current = null;
        while ((tid = syntax.nextToken()) != null) {
            int numericId = tid.getNumericID();
            boolean tokenAdded = false;
            if (numericId == HqlTokenContext.LEFT_PAREN_ID) {
                depth++;
                if (position > syntax.getTokenOffset()) {
                    caretDepth = depth;
                }
            } else if (numericId == HqlTokenContext.RIGHT_PAREN_ID) {
                SubQuery currentDepthQuery = level2SubQuery.get(depth);
                // We check if we have a query on the current depth.
                // If yes, we'll have to close it
                if (currentDepthQuery != null && currentDepthQuery.depth == depth) {
                    currentDepthQuery.endOffset = syntax.getOffset();
                    currentDepthQuery.numericIds.add(numericId);
                    currentDepthQuery.tokens.add(String.valueOf(chars, syntax.getTokenOffset(), syntax.getTokenLength()));
                    subQueries.add(currentDepthQuery);
                    level2SubQuery.remove(depth);
                    tokenAdded = true;
                }
                depth--;
                if (position > syntax.getTokenOffset()) {
                    caretDepth = depth;
                }
            }
            switch (numericId) {
                case HqlTokenContext.FROM_ID:
                case HqlTokenContext.UPDATE_ID:
                case HqlTokenContext.DELETE_ID:
                case HqlTokenContext.SELECT_ID:
                    if (!level2SubQuery.containsKey(depth)) {
                        current = new SubQuery();
                        current.depth = depth;
                        current.startOffset = syntax.getTokenOffset();
                        level2SubQuery.put(depth, current);
                    }
                    current.numericIds.add(numericId);
                    current.tokens.add(String.valueOf(chars, syntax.getTokenOffset(), syntax.getTokenLength()));
                    break;
                default:
                    if (!tokenAdded) {
                        SubQuery sq = level2SubQuery.get(depth);
                        int i = depth;
                        while (sq == null && i >= 0) {
                            sq = level2SubQuery.get(i--);
                        }
                        if (sq != null) {
                            sq.numericIds.add(numericId);
                            sq.tokens.add(String.valueOf(chars, syntax.getTokenOffset(), syntax.getTokenLength()));
                        }
                    }
            }
        }
        for (SubQuery sq : level2SubQuery.values()) {
            sq.endOffset = syntax.getTokenOffset() + syntax.getTokenLength();
            subQueries.add(sq);
        }
        Collections.sort(subQueries);
        SubQueryList sql = new SubQueryList();
        sql.caretDepth = caretDepth;
        sql.subQueries = subQueries;
        return sql;
    }

    public static String getTableNamePrefix(char[] chars, int position) {
        StringBuffer buff = new StringBuffer();
        for (int i = position - 1; i >= 0; i--) {
            char c = chars[i];
            if (c == '.' || Character.isJavaIdentifierPart(c)) {
                buff.insert(0, c);
            } else {
                break;
            }
        }
        return buff.toString();
    }

    public static class SubQuery implements Comparable<SubQuery> {

        public int compareTo(SubQuery s) {
            return startOffset - s.startOffset;
        }
        
        private List<Integer> numericIds = new ArrayList<Integer>();

        private List<String> tokens = new ArrayList<String>();

        private int startOffset;

        private int endOffset;
        
        private int depth;

        public int getTokenCount() {
            return numericIds.size();
        }

        public int getNumericId(int i) {
            return numericIds.get(i);
        }

        public String getToken(int i) {
            return tokens.get(i);
        }

        public List<QueryTable> getTables() {
            boolean afterFrom = false;
            boolean afterJoin = false;
            StringBuffer tableNames = new StringBuffer();
            StringBuffer joins = new StringBuffer();
            int i = 0;
            boolean cont = true;
            for (int type : numericIds) {
                if (!cont) {
                    break;
                }
                if (!afterFrom &&
                        (type == HqlTokenContext.FROM_ID ||
                        type == HqlTokenContext.UPDATE_ID ||
                        type == HqlTokenContext.DELETE_ID)) {
                    afterFrom = true;
                } else if (afterJoin) {
                    switch (type) {
                        case HqlTokenContext.ORDER_ID:
                        case HqlTokenContext.WHERE_ID:
                        case HqlTokenContext.GROUP_ID:
                        case HqlTokenContext.HAVING_ID:
                            cont = false;
                            break;
                        case HqlTokenContext.INNER_ID:
                        case HqlTokenContext.OUTER_ID:
                        case HqlTokenContext.LEFT_ID:
                        case HqlTokenContext.RIGHT_ID:
                        case HqlTokenContext.JOIN_ID:
                            joins.append(",");
                            break;
                        case HqlTokenContext.DEFAULT_ID:
                        case HqlTokenContext.WHITESPACE_ID:
                            joins.append(tokens.get(i));

                    }
                } else if (afterFrom) {
                    switch (type) {
                        case HqlTokenContext.ORDER_ID:
                        case HqlTokenContext.WHERE_ID:
                        case HqlTokenContext.GROUP_ID:
                        case HqlTokenContext.HAVING_ID:
                        case HqlTokenContext.SET_ID:
                            cont = false;
                            break;
                        case HqlTokenContext.DEFAULT_ID:
                        case HqlTokenContext.WHITESPACE_ID:
                            tableNames.append(tokens.get(i));
                            break;
                        case HqlTokenContext.JOIN_ID:
                            afterJoin = true;
                            break;
                    }
                }
                i++;
            }
            List<QueryTable> tables = new ArrayList<QueryTable>();
            addTables(tables, tableNames);
            addTables(tables, joins);
            return tables;
        }

        private void addTables(final List<QueryTable> tables, final StringBuffer tableNames) {
            StringTokenizer tableTokenizer = new StringTokenizer(tableNames.toString(), ",");
            while (tableTokenizer.hasMoreTokens()) {
                String table = tableTokenizer.nextToken().trim();
                if (table.indexOf(' ') == -1 && table.length() > 0) {
                    tables.add(new QueryTable(table, table));
                } else {
                    StringTokenizer aliasTokenizer = new StringTokenizer(table, " ");
                    if (aliasTokenizer.countTokens() >= 2) {
                        String type = aliasTokenizer.nextToken().trim();
                        String alias = aliasTokenizer.nextToken().trim();
                        if (type.length() > 0 && alias.length() > 0) {
                            tables.add(new QueryTable(type, alias));
                        }
                    }
                }
            }
        }
    }

    static class SubQueryList {

        int caretDepth;

        List<SubQuery> subQueries;
    }

}
