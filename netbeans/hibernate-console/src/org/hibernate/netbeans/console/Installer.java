package org.hibernate.netbeans.console;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.netbeans.console.editor.hql.HqlSettingsInitializer;
import org.netbeans.editor.LocaleSupport;
import org.netbeans.editor.Settings;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class Installer extends ModuleInstall {

    private static LocaleSupport.Localizer localizer;

    public void restored() {
        Settings.addInitializer(new HqlSettingsInitializer());
        localizer = new LocaleSupport.Localizer() {
            public String getString(String key) {
                return NbBundle.getMessage(Installer.class, key);
            }
        };
        LocaleSupport.addLocalizer(localizer);
        // Set the logger for org.hibernate on ERROR
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        // Set the logger for ehcache on ERROR
        Logger.getLogger("net.sf.ehcache").setLevel(Level.SEVERE);
    }

    public void uninstalled() {
        super.uninstalled();
        if (localizer != null) {
            LocaleSupport.removeLocalizer(localizer);
        }
    }



}
