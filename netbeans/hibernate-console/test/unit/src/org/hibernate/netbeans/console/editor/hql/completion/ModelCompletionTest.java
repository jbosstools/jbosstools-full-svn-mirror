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

import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import org.hibernate.netbeans.console.model.Model;
import org.hibernate.SessionFactory;

/**
 * @author leon
 */
public class ModelCompletionTest extends TestCase {

    private SessionFactory sf;

    public ModelCompletionTest() throws Exception {
        sf = Model.buildSessionFactory();
    }

    public void testGetMappedClasses() {
        List<HqlResultItem> mappedClasses = CompletionHelper.getMappedClasses(sf, "");
        Collections.sort(mappedClasses, new CompletionComparator());
        assertEquals("Invalid mapped classes count", 4, mappedClasses.size());
        
        HqlResultItem i1 = mappedClasses.get(0);
        assertEquals("Invalid completion item", "Product", i1.getText());
        HqlResultItem i2 = mappedClasses.get(1);
        assertEquals("Invalid completion item", "ProductOwnerAddress", i2.getText());
        HqlResultItem i3 = mappedClasses.get(2);
        assertEquals("Invalid completion item", "Store", i3.getText());
        HqlResultItem i4 = mappedClasses.get(3);
        assertEquals("Invalid completion item", "StoreCity", i4.getText());
        
        mappedClasses = CompletionHelper.getMappedClasses(sf, " ");
        assertTrue("Space prefix should have no classes", mappedClasses.isEmpty());
        mappedClasses = CompletionHelper.getMappedClasses(sf, "pro");
        assertTrue("Completion should be case sensitive", mappedClasses.isEmpty());
        mappedClasses = CompletionHelper.getMappedClasses(sf, "Pro");
        assertEquals("Product should have been found", "Product", mappedClasses.get(0).getText());

    }

    public void testGetProductFields() {
        List<HqlResultItem> attrs = CompletionHelper.getProperties(sf, "Product", "");
        doTestFiels(attrs, "id", "owner", "price", "stores", "version", "weight");
        attrs = CompletionHelper.getProperties(sf, "Product", " ");
        doTestFiels(attrs);
        attrs = CompletionHelper.getProperties(sf, "Product", "v");
        doTestFiels(attrs, "version");
        attrs = CompletionHelper.getProperties(sf, "Product", "V");
        doTestFiels(attrs);
    }

    public void testGetStoreFields() {
        List<HqlResultItem> attrs = CompletionHelper.getProperties(sf, "Store", "");
        doTestFiels(attrs, "city", "id", "name", "name2");
        attrs = CompletionHelper.getProperties(sf, "Store", "name");
        doTestFiels(attrs, "name", "name2");
        attrs = CompletionHelper.getProperties(sf, "Store", "name2");
        doTestFiels(attrs, "name2");
    }

    public void testUnmappedClassFields() {
        List<HqlResultItem> attrs = CompletionHelper.getProperties(sf, "UnmappedClass", "");
        doTestFiels(attrs);
    }

    private void doTestFiels(List<HqlResultItem> attrs, String... fields) {
        if (fields == null) {
            assertTrue("No fields should have been found", attrs.isEmpty());
            return;
        }
        Collections.sort(attrs, new CompletionComparator());
        assertEquals("Invalid field count", fields.length, attrs.size());
        int i = 0;
        for (String f : fields) {
            assertEquals("Invalid field name at " + i, f, attrs.get(i++).getText());
        }
    }
    
    public void testProductOwnerAddress() {
        String query = "select p from Product p where p.owner.";
        List<QueryTable> visible = HqlAnalyzer.getVisibleTables(query.toCharArray(), query.length());
        List<HqlResultItem> items = CompletionHelper.getProperties(sf, CompletionHelper.getCanonicalPath(visible, "p.owner"), null);
        doTestFiels(items, "address", "firstName", "lastName");
        query = "select p from Product p where p.owner.address.";
        visible = HqlAnalyzer.getVisibleTables(query.toCharArray(), query.length());
        items = CompletionHelper.getProperties(sf, CompletionHelper.getCanonicalPath(visible, "p.owner.address"), null);
        doTestFiels(items, "id", "number", "street");
    }
    
    public void testStoreCity() {
        String query = "select p from Product p join p.stores store where store";
        List<QueryTable> visible = HqlAnalyzer.getVisibleTables(query.toCharArray(), query.length());
        List<HqlResultItem> items = CompletionHelper.getProperties(sf, CompletionHelper.getCanonicalPath(visible, "store.city"), null);
        doTestFiels(items, "id", "name");
    }
    
    public void testUnaliasedProductQuery() {
        doTestUnaliasedProductQuery("delete Product where owner.");
        doTestUnaliasedProductQuery("update Product where owner.");
        doTestUnaliasedProductQuery("select from Product where owner.");
    }

    private void doTestUnaliasedProductQuery(final String query) {
        List<QueryTable> visible = HqlAnalyzer.getVisibleTables(query.toCharArray(), query.length());
        List<HqlResultItem> items = CompletionHelper.getProperties(sf, CompletionHelper.getCanonicalPath(visible, "owner"), "f");
        //
        assertEquals(1, items.size());
        assertEquals("firstName", items.get(0).getText());
        //
        items = CompletionHelper.getProperties(sf, CompletionHelper.getCanonicalPath(visible, "owner"), "l");
        assertEquals(1, items.size());
        assertEquals("lastName", items.get(0).getText());
        //
        items = CompletionHelper.getProperties(sf, CompletionHelper.getCanonicalPath(visible, "owner"), "");
        // firstname, lastname, owner
        assertEquals(3, items.size());
        //
        items = CompletionHelper.getProperties(sf, CompletionHelper.getCanonicalPath(visible, "owner"), "g");
        assertEquals(0, items.size());
    }

}
