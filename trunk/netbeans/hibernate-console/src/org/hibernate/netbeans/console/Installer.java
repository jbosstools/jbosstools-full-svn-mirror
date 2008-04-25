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
