

package org.jboss.tools.vpe.test;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.vpe.editor.css.ELReferenceList;
import org.jboss.tools.vpe.editor.css.ResourceReference;
import org.jboss.tools.vpe.editor.util.ElService;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;


// TODO: Auto-generated Javadoc
/**
 * The Class ElPreferencesTestCase.
 */
public class ElPreferencesTestCase extends VpeTest {

    /** The Constant KEY_3. */
    private static final String KEY_3 = "#{facesContext.requestPath}";

    /** The Constant KEY_2. */
    private static final String KEY_2 = "#{beanA.property2}";

    /** The Constant IMPORT_PROJECT_NAME. */
    public static final String IMPORT_PROJECT_NAME = "jsfTest";

    /** The Constant DIR_TEST_PAGE_NAME. */
    private static final String DIR_TEST_PAGE_NAME = "JBIDE/2010/page.xhtml";

    /**
     * The Constructor.
     * 
     * @param name the name
     */
    public ElPreferencesTestCase(String name) {
        super(name);

    }

    /**
     * The Constructor.
     */
    public ElPreferencesTestCase() {
        super(ElPreferencesTestCase.class.getName());

    }

    /** The Constant KEY_1. */
    private static final String KEY_1 = "#{beanA.property1}";

    /** The Constant elValuesMap. */
    protected static final Map<String, String> elValuesMap = new HashMap<String, String>();

    /** The file. */
    private IFile file;

    /**
     * Sets the up.
     * 
     * @throws Exception the exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        elValuesMap.put(KEY_1, "/path/image/path/to/image/");
        elValuesMap.put(KEY_2, "/path2/");
        elValuesMap.put(KEY_3, "/facesContext/");
        file = (IFile) TestUtil.getComponentPath(DIR_TEST_PAGE_NAME, IMPORT_PROJECT_NAME);
        ResourceReference[] entries = new ResourceReference[elValuesMap.size()];
        int i = 0;
        for (Entry<String, String> string : elValuesMap.entrySet()) {
            
            entries[i] = new ResourceReference(string.getValue(), ResourceReference.PROJECT_SCOPE);
            entries[i].setProperties(string.getKey());
            i++;

            setValues(entries);
        }
    }

    /**
     * Sets the values.
     * 
     * @param key the key
     * @param value the value
     * @param scope the scope
     */
    protected void setValues(ResourceReference[] entries) {
 
        ELReferenceList.getInstance().setAllResources(file, entries);
    }

    /**
     * Tear down.
     * 
     * @throws Exception the exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    /**
     * Test replace attribute value.
     * 
     * @throws CoreException the core exception
     */
    public void testReplaceAttributeValue() throws CoreException {
        String string1 = "#{beanA.property1}/images/smalle.gif";
        String replacedValue = ElService.getInstance().replaceEl(file, string1);

        assertEquals("Should be equals " + elValuesMap.get(KEY_1) + "/images/smalle.gif", replacedValue, elValuesMap.get(KEY_1)
                + "/images/smalle.gif");

    }

    /**
     * Test replace attribute value2.
     * 
     * @throws CoreException the core exception
     */
    public void testReplaceAttributeValue2() throws CoreException {
        String string1 = "#{beanA.property1}/images/#{beanA.property2}/path2/#{facesContext.requestPath}/smalle.gif";

        final String replacedValue = ElService.getInstance().replaceEl(file, string1);
        final String check = elValuesMap.get(KEY_1) + "/images/" + elValuesMap.get(KEY_2) + "/path2/" + elValuesMap.get(KEY_3)
                + "/smalle.gif";
        assertEquals("Should be equals " + check, check, replacedValue);

    }
}
