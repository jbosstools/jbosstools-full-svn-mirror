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


package org.hibernate.netbeans.console.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.hibernate.netbeans.console.editor.hql.HqlSyntax;
import org.hibernate.netbeans.console.editor.hql.HqlTokenContext;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.TokenID;
import org.netbeans.editor.Utilities;
import org.openide.ErrorManager;

/**
 * @author leon
 */
public class EditorContentHelper {

    private JTextComponent comp;

    private static HqlSyntax syntax = new HqlSyntax();

    public EditorContentHelper(JTextComponent comp) {
        this.comp = comp;
    }

    public int[] getCurrentQueryBounds() {
        String text = comp.getSelectedText();
        if (text != null) {
            return new int[] { comp.getSelectionStart(), comp.getSelectionEnd() };
        }
        Document doc = comp.getDocument();
        if (!(doc instanceof BaseDocument)) {
            return null;
        }
        BaseDocument baseDoc = (BaseDocument) doc;
        int pos = comp.getCaretPosition();
        return getCurrentQueryBounds(baseDoc, pos);
    }

    public String getCurrentQuery() {
        String text = comp.getSelectedText();
        if (text != null) {
            return text;
        }
        Document doc = comp.getDocument();
        if (!(doc instanceof BaseDocument)) {
            return null;
        }
        return getCurrentQuery((BaseDocument) doc, comp.getCaretPosition());
    }

    public static String getCurrentQuery(BaseDocument doc, int pos) {
        int bounds[] = getCurrentQueryBounds(doc, pos);
        if (bounds == null) {
            return null;
        }
        try {
            return String.valueOf(doc.getChars(bounds)).trim();
        } catch (BadLocationException ex) {
            return null;
        }
    }

    public static int[] getCurrentQueryBounds(BaseDocument baseDoc, int pos) {
        try {
            if (Utilities.isRowWhite(baseDoc, pos) || Utilities.isRowEmpty(baseDoc, pos)) {
                return null;
            }
            // Go forward and stop on the first delimiter (first empty line, or first ';')
            int maxLength = baseDoc.getLength();
            int statementEnd = maxLength;
            for (int i = pos + 1; i < maxLength; i++) {
                if (Utilities.isRowWhite(baseDoc, i) || Utilities.isRowEmpty(baseDoc, i)) {
                    statementEnd = i - 1;
                    break;
                }
                // TODO - stop on ;
            }
            int statementStart = 0;
            // Go backward and stop on the first delimiter
            for (int i = pos - 1; i >= 0; i--) {
                if (Utilities.isRowWhite(baseDoc, i) || Utilities.isRowEmpty(baseDoc, i)) {
                    statementStart = i + 1;
                    break;
                }
            }
            return new int[] { statementStart, statementEnd };
        } catch (BadLocationException ex) {
            ErrorManager.getDefault().notify(ex);
            return null;
        }
    }

    public String convertHql2Java() {
        int bounds[] = getCurrentQueryBounds();
        if (bounds == null) {
            return null;
        }
        try {
            BaseDocument doc = (BaseDocument) comp.getDocument();
            String query = String.valueOf(doc.getChars(bounds));
            // Make sure we convert it back to hql, and than to java so that
            // we don't escape the double quotes twice
            boolean startsWithNewLine = query.startsWith("\n");
            boolean endsWithNewLine = query.endsWith("\n");
            query = convertJava2Hql(query);
            query = convertHql2Java(query);
            if (startsWithNewLine) {
                query = "\n" + query;
            }
            if (endsWithNewLine) {
                query = query + "\n";
            }
            doc.replace(bounds[0], bounds[1] - bounds[0], query, null);
            comp.setCaretPosition(comp.getCaretPosition() - 1);
            return query;
        } catch (Exception ex) {
            ErrorManager.getDefault().notify(ex);
            return null;
        }
    }
    
    public String convertJava2Hql() {
        int bounds[] = getCurrentQueryBounds();
        if (bounds == null) {
            return null;
        }
        try {
            BaseDocument doc = (BaseDocument) comp.getDocument();
            String query = String.valueOf(doc.getChars(bounds));
            boolean startsWithNewLine = query.startsWith("\n");
            boolean endsWithNewLine = query.endsWith("\n");
            query = convertJava2Hql(query);
            if (startsWithNewLine) {
                query = "\n" + query;
            }
            if (endsWithNewLine) {
                query = query + "\n";
            }
            doc.replace(bounds[0], bounds[1] - bounds[0], query, null);
            comp.setCaretPosition(comp.getCaretPosition() - 1);
            return query;
        } catch (Exception ex) {
            ErrorManager.getDefault().notify(ex);
            return null;
        }
    }
    
    public static String convertHql2Java(String query) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(query));
        StringBuffer newQuery = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            if (newQuery.length() > 0) {
                newQuery.append(" +\n");
            }
            line = line.replaceAll("\"", "\\\\\"");
            newQuery.append("\"").append(line);
            if (!line.endsWith(" ")) {
                newQuery.append(" ");
            }
            newQuery.append("\"");
        }
        return newQuery.toString();
    }

    public static String convertJava2Hql(String query) {
        syntax.load(null, query.toCharArray(), 0, query.length(), true, query.length());
        StringBuilder sb = new StringBuilder();
        TokenID token;
        // Find out all strings
        do {
            token = syntax.nextToken();
            if (token == null) {
                break;
            }
            int numId = token.getNumericID();
            int tof = syntax.getTokenOffset();
            int tl = syntax.getTokenLength();
            if (numId == HqlTokenContext.JAVA_STRING_ID) {
                sb.append(query.substring(tof + 1, tof + tl - 1));
            } else if (!(numId == HqlTokenContext.DEFAULT_ID && tl == 1 && query.charAt(tof) == '+')) {
                String img = query.substring(tof, tof + tl);
                sb.append(img);
            }
        } while (true);
        if (sb.length() == 0) {
            // Nothing to be converted
            return query;
        }
        // Now we should remove all unnecessary whitespace
        query = sb.toString();
        sb = new StringBuilder();
        syntax.load(null, query.toCharArray(), 0, query.length(), true, query.length());
        do {
            token = syntax.nextToken();
            int tof = syntax.getTokenOffset();
            int tl = syntax.getTokenLength();
            if (token == null) {
                break;
            }
            int numId = token.getNumericID();
            String img = query.substring(tof, tof + tl);
            if (numId == HqlTokenContext.WHITESPACE_ID) {
                int lastChar = sb.charAt(sb.length() - 1);
                if (lastChar == ' ' || lastChar == '\n' || lastChar == '(') {
                    // Remove trailing whitespace
                    img = img.replaceAll("^ +", "");
                }
                // Replace more than 2 spaces with one space
                img = img.replaceAll(" {2,}", " ");
                // Replace spaces before the end of the lines
                img = img.replaceAll(" *\\n *", "\n");
                sb.append(img);
            } else {
                sb.append(img);
            }
        } while (true);
        // Replace empty strings at the end of of the line
        query = sb.toString().replaceAll(" $", "");
        // Now we have an despaceified string, but we still need to replace
        // the .class.getName() stuff
        sb = new StringBuilder();
        syntax.load(null, query.toCharArray(), 0, query.length(), true, query.length());
        boolean wasClassGetName = false;
        StringBuilder parens = null;
        do {
            token = syntax.nextToken();
            int tof = syntax.getTokenOffset();
            int tl = syntax.getTokenLength();
            if (token == null) {
                break;
            }
            int numId = token.getNumericID();
            String img = query.substring(tof, tof + tl);
            if (numId == HqlTokenContext.WHITESPACE_ID) {
                if (!(wasClassGetName && parens != null && parens.toString().equals("("))) {
                    sb.append(img);
                }
            } else if (numId == HqlTokenContext.LEFT_PAREN_ID) {
                if (wasClassGetName && parens == null) {
                    parens = new StringBuilder("(");
                } else {
                    wasClassGetName = false;
                    parens = null;
                    sb.append("(");
                }
            } else if (numId == HqlTokenContext.RIGHT_PAREN_ID) {
                wasClassGetName = false;
                parens = null;
                if (!(wasClassGetName && parens != null && parens.toString().equals("("))) {
                    if (parens != null) {
                        sb.append(parens);
                        sb.append(")");
                    }
                }
            } else {
                sb.append(img.replaceAll("(.*)\\.class\\.getName", "$1"));
                wasClassGetName = img.indexOf(".class.getName") != -1;
                parens = null;
            }
        } while (true);
        return sb.toString();
    }


}
