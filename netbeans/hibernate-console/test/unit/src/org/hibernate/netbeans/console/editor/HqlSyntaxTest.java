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

package org.hibernate.netbeans.console.editor;

import java.util.Arrays;
import java.util.Iterator;
import junit.framework.TestCase;
import org.hibernate.netbeans.console.editor.hql.HqlSyntax;
import org.hibernate.netbeans.console.editor.hql.HqlTokenContext;
import org.netbeans.editor.TokenCategory;
import org.netbeans.editor.TokenID;

/**
 * @author leon
 */
public class HqlSyntaxTest extends TestCase {

    private HqlSyntax syntax;

    public HqlSyntaxTest(String testName) {
        super(testName);
        syntax = new HqlSyntax();
    }

    public void testJavaStrings() {
        parse(
                "\"from A\"",
                new TokenID[] {
                    HqlTokenContext.JAVA_STRING
                });
        parse(
                "\"from \\\"A\"",
                new TokenID[] {
                    HqlTokenContext.JAVA_STRING
                });
        parse(
                "\"from\\\\\"",
                new TokenID[] {
                    HqlTokenContext.JAVA_STRING
                });
    }

    public void testQueries() {
        parse(
                "from art1, art2",
                new TokenID[] {
                    // from [ ]
                    HqlTokenContext.FROM, HqlTokenContext.WHITESPACE,
                    // art1 ,
                    HqlTokenContext.DEFAULT, HqlTokenContext.DEFAULT,
                    // [ ] art2
                    HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT,
                });
        parse(
                "select a1 from A1.A2.A3, A4.A5.A6, A7.A8.A7 where",
                new TokenID[] {
                    // select [ ] a1 [ ]
                    HqlTokenContext.SELECT, HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT, HqlTokenContext.WHITESPACE,
                    // from [ ]
                    HqlTokenContext.FROM, HqlTokenContext.WHITESPACE,
                    // A1.A2.A3 , [ ] A4.A5.A6
                    HqlTokenContext.DEFAULT, HqlTokenContext.DEFAULT, HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT,
                    // , [ ] A7.A8.A7 [ ]
                    HqlTokenContext.DEFAULT, HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT, HqlTokenContext.WHITESPACE,
                    // where
                    HqlTokenContext.WHERE
                });
        parse(
                "(select",
                new TokenID[] {
                    HqlTokenContext.LEFT_PAREN, HqlTokenContext.SELECT
                });
        parse(
                " (select",
                new TokenID[] {
                    HqlTokenContext.WHITESPACE, HqlTokenContext.LEFT_PAREN, HqlTokenContext.SELECT
                });
        parse(
                "select)",
                new TokenID[] {
                    HqlTokenContext.SELECT, HqlTokenContext.RIGHT_PAREN
                });
        parse(
                "id in (select",
                new TokenID[] {
                    // id [ ] in [ ]
                    HqlTokenContext.DEFAULT, HqlTokenContext.WHITESPACE, HqlTokenContext.KEYWORD, HqlTokenContext.WHITESPACE,
                    // ( select
                    HqlTokenContext.LEFT_PAREN, HqlTokenContext.SELECT
                });
        parse(
                "('gaga' ('gaga' from",
                new TokenID[] {
                    // ( 'gaga' [ ]
                    HqlTokenContext.LEFT_PAREN, HqlTokenContext.HQL_STRING, HqlTokenContext.WHITESPACE,
                    // ( 'gaga' [ ] from
                    HqlTokenContext.LEFT_PAREN, HqlTokenContext.HQL_STRING, HqlTokenContext.WHITESPACE, HqlTokenContext.FROM
                });
        parse("\"java-string\"",
                new TokenID[] {
                    HqlTokenContext.JAVA_STRING
                });
        parse(
                "(\"gaga\" (\"gaga\" from",
                new TokenID[] {
                    // ( 'gaga' [ ]
                    HqlTokenContext.LEFT_PAREN, HqlTokenContext.JAVA_STRING, HqlTokenContext.WHITESPACE,
                    // ( 'gaga' [ ] from
                    HqlTokenContext.LEFT_PAREN, HqlTokenContext.JAVA_STRING, HqlTokenContext.WHITESPACE, HqlTokenContext.FROM
                });
        parse(
                "c)",
                new TokenID[] {
                    HqlTokenContext.DEFAULT, HqlTokenContext.RIGHT_PAREN
                });
        parse(
                "c) ",
                new TokenID[] {
                    HqlTokenContext.DEFAULT, HqlTokenContext.RIGHT_PAREN, HqlTokenContext.DEFAULT
                });
        parse(
                "update Table set gaga",
                new TokenID[] {
                    // update [ ] Table [ ]
                    HqlTokenContext.UPDATE, HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT, HqlTokenContext.WHITESPACE,
                    // set [ ] gaga
                    HqlTokenContext.SET, HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT
                });
        parse(
                "delete Table where",
                new TokenID[] {
                    // delete [ ] Table
                    HqlTokenContext.DELETE, HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT,
                    // [ ] where
                    HqlTokenContext.WHITESPACE, HqlTokenContext.WHERE
                });
        parse(
                "do \n \n stuff",
                new TokenID[] {
                    // do [ ]\n[ ]\n[ ] stuff
                    HqlTokenContext.DEFAULT, HqlTokenContext.WHITESPACE, HqlTokenContext.DEFAULT,
                });
    }

    private void parse(String str, TokenID[] tokens) {
        syntax.load(null, str.toCharArray(), 0, str.length(), true, str.length());
        Iterator it = Arrays.asList(tokens).iterator();
        TokenID token = null;
        int i = 0;
        do {
            token = syntax.nextToken();
            if (token != null) {
                if (!it.hasNext()) {
                    fail("Expected more tokens [" + i + "]");
                } else {
                    assertEquals("Tokens differ [" + i + "]", ((TokenID) it.next()).getNumericID(), token.getNumericID());
                }
            } else {
                assertFalse("More tokens expected [" + i + "]", it.hasNext());
            }
            i++;
        } while (token != null);
    }
}

