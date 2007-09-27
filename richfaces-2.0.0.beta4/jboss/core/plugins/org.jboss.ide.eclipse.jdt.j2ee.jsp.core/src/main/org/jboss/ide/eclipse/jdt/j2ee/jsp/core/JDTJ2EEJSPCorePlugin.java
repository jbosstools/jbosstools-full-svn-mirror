/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.j2ee.core.JDTJ2EECorePlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.classpath.JasperClasspathContainer;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JDTJ2EEJSPCorePlugin extends AbstractPlugin
{
    /** The shared instance */
    private static JDTJ2EEJSPCorePlugin plugin;//$NON-NLS-1$
    /** Description of the Field */
    public final static String JSP_BUILDER_ID = JDTJ2EEJSPCorePlugin.PLUGIN_ID + ".jsp-builder";//$NON-NLS-1$
    /** Description of the Field */
    public final static String JSP_NATURE_ID = JDTJ2EEJSPCorePlugin.PLUGIN_ID + ".jsp-nature";//$NON-NLS-1$
    /** Description of the Field */
    public final static String PLUGIN_ID = "org.jboss.ide.eclipse.jdt.j2ee.jsp.core"; //$NON-NLS-1$

    /** The constructor. */
    public JDTJ2EEJSPCorePlugin()
    {
        super();
        plugin = this;
    }

    /**
     * Description of the Method
     *
     * @param context        Description of the Parameter
     * @exception Exception  Description of the Exception
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);

        ClassPathContainerRepository.getInstance().addClassPathEntry(JasperClasspathContainer.CLASSPATH_CONTAINER);

        // Force the plugin load
        JDTJ2EECorePlugin.getDefault().getBaseDir();
    }

    /**
     * Returns the shared instance.
     *
     * @return   The default value
     */
    public static JDTJ2EEJSPCorePlugin getDefault()
    {
        return plugin;
    }

    /**
     * Convenience method which returns the unique identifier of this plugin.
     *
     * @return   The unique indentifier value
     */
    public static String getUniqueIdentifier()
    {
        if (getDefault() == null)
        {
            // If the default instance is not yet initialized,
            // return a static identifier. This identifier must
            // match the plugin id defined in plugin.xml
            return PLUGIN_ID;//$NON-NLS-1$
        }
        return getDefault().getBundle().getSymbolicName();
    }
}