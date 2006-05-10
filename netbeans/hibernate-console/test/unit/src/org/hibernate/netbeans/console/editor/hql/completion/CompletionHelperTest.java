package org.hibernate.netbeans.console.editor.hql.completion;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 * @author leon
 */
public class CompletionHelperTest extends TestCase {
    
    public CompletionHelperTest() {
    }

    public void testGetCanonicalPath() {
        List<QueryTable> qts = new ArrayList<QueryTable>();
        qts.add(new QueryTable("Article", "art"));
        qts.add(new QueryTable("art.descriptions", "descr"));
        qts.add(new QueryTable("descr.name", "n"));
        assertEquals("Invalid path", "Article/descriptions/name/locale", CompletionHelper.getCanonicalPath(qts, "n.locale"));
        assertEquals("Invalid path", "Article/descriptions", CompletionHelper.getCanonicalPath(qts, "descr"));
        //
        qts.clear();
        qts.add(new QueryTable("com.company.Clazz", "clz"));
        qts.add(new QueryTable("clz.attr", "a"));
        assertEquals("Invalid path", "com.company.Clazz/attr", CompletionHelper.getCanonicalPath(qts, "a"));
        //
        qts.clear();
        qts.add(new QueryTable("Agga", "a"));
        assertEquals("Invalid path", "Agga", CompletionHelper.getCanonicalPath(qts, "a"));
    }

    public void testStackOverflowInGetCanonicalPath() {
        List<QueryTable> qts = new ArrayList<QueryTable>();
        qts.add(new QueryTable("Article", "art"));
        qts.add(new QueryTable("art.stores", "store"));
        qts.add(new QueryTable("store.articles", "art"));
        // This should not result in a stack overflow
        CompletionHelper.getCanonicalPath(qts, "art");
    }
    
}
